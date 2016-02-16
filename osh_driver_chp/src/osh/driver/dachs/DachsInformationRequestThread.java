/*
 ************************************************************************************************* 
 * OrganicSmartHome [Version 2.0] is a framework for energy management in intelligent buildings
 * Copyright (C) 2014  Florian Allerding (florian.allerding@kit.edu) and Kaibin Bao and
 *                     Ingo Mauser and Till Schuberth
 * 
 * 
 * This file is part of the OrganicSmartHome.
 * 
 * OrganicSmartHome is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the Free Software Foundation, 
 * either version 3 of the License, or (at your option) any later version.
 * 
 * OrganicSmartHome is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with OrganicSmartHome.  
 * 
 * If not, see <http://www.gnu.org/licenses/>.
 * 
 *************************************************************************************************
 */

package osh.driver.dachs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import osh.core.OSHGlobalLogger;
import osh.core.exceptions.OSHException;
import osh.datatypes.registry.driver.details.chp.raw.DachsDriverDetails;
import osh.driver.DachsDriver;


/**
 * 
 * @author Florian Allerding, Kaibin Bao, Till Schuberth, Ingo Mauser
 *
 */
public class DachsInformationRequestThread implements Runnable {

	private OSHGlobalLogger globalLogger;
	private DachsDriver dachsDriver;
	private ArrayList<String> parametersToGet;
	
	private boolean shutdown;
	private Date lastException = new Date();
	private int reconnectWait;
	
	private String urlToDachs;
	private String loginName;
	private String loginPwd;
	
	
	/**
	 * CONSTRUCTOR
	 * @param globalLogger
	 * @param dachsDriver
	 * @param urlToDachs
	 */
	public DachsInformationRequestThread(
			OSHGlobalLogger globalLogger, 
			DachsDriver dachsDriver, 
			String urlToDachs,
			String loginName,
			String loginPwd,
			ArrayList<String> parametersToGet) {
		this.globalLogger = globalLogger;
		this.dachsDriver = dachsDriver;
		this.urlToDachs = urlToDachs;
		this.loginName = loginName;
		this.loginPwd = loginPwd;
		this.parametersToGet = parametersToGet;
	}

	
	@Override
	public void run() {
		while (!shutdown) {
			try {
//				globalLogger.logDebug("Getting new DACHS data");
				
				DachsDriverDetails dachsDetails = new DachsDriverDetails(
						dachsDriver.getDeviceID(), 
						dachsDriver.getTimer().getUnixTime());

				// get information from DACHS and save into dachsDetails
				HashMap<String, String> values = getDataFromDachs(parametersToGet);
				dachsDetails.setValues(values);
				
				this.dachsDriver.processDachsDetails(dachsDetails);
			} 
			catch (Exception e) {
				this.globalLogger.logError("Reading dachs data failed", e);
				
				long diff = new Date().getTime() - lastException.getTime();
				if (diff < 0 || diff > 300000) {
					reconnectWait = 0;
				}
				else {
					if (reconnectWait <= 0) {
						reconnectWait = 1;
					}
					reconnectWait *= 2;
					if (reconnectWait > 180) {
						reconnectWait = 180;
					}
				}
				lastException = new Date();
				
				try {
					Thread.sleep(reconnectWait * 1000);
				} catch (InterruptedException e1) {}
			}
			try {
				Thread.sleep(1000); // INCREASE???
			} 
			catch (InterruptedException e) {}
		}		
	}

	private HashMap<String, String> getDataFromDachs(List<String> keys) throws OSHException {
		if (keys.size() == 0) {
			return new HashMap<String, String>();
		}
		
		HashMap<String,String> fullMap = new HashMap<String, String>();
		
		int numberOfKeys = keys.size();
		int waitAfter = 5;
		
		StringBuilder reqData = new StringBuilder();
		boolean first = true;
		boolean lastKey = false;
		
		// request only 5 keys at a time
		for (int currentKey = 0; currentKey < numberOfKeys; currentKey++) {
			if (currentKey == numberOfKeys - 1) {
				lastKey = true;
			}
			
			if (currentKey % waitAfter == waitAfter - 1 || lastKey) {
				if (!first) reqData.append('&');
				first = false;
				reqData.append("k=").append(keys.get(currentKey));
				
				DefaultHttpClient client = new DefaultHttpClient();
				try {
					HttpGet httpget = new HttpGet(urlToDachs + "getKey?" + reqData);
					client.getCredentialsProvider().setCredentials(
							new AuthScope(
									httpget.getURI().getHost(), 
									httpget.getURI().getPort()),
									new UsernamePasswordCredentials(loginName, loginPwd));
					HttpResponse response = client.execute(httpget);

					HttpEntity entity = response.getEntity();
					HashMap<String,String> newMap = parseDachsAnswer(EntityUtils.toString(entity));

					for (Entry<String, String> e : newMap.entrySet()) {
						fullMap.put(e.getKey(), e.getValue());
					}
				} 
				catch (IOException ex){
					globalLogger.logWarning("Could not connect to DACHS.");;
				} 
				finally {
					client.getConnectionManager().shutdown();
				}
				
				//reset
				reqData = new StringBuilder();
				first = true;
				
				try {
					Thread.sleep(100); // INCREASE???
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			else {
				if (!first) reqData.append('&');
				first = false;
				reqData.append("k=").append(keys.get(currentKey));
			}
		}
		
		return fullMap;
	}
	
	private HashMap<String, String> parseDachsAnswer(String answer) throws OSHException {
		HashMap<String, String> res = new HashMap<String, String>();
		
		for (String item : answer.split("\n")) {
			if (answer.trim().equals("")) {
				continue;
			}
			String[] keyvalue = item.split("=");
			
			if (keyvalue.length != 2) {
				throw new OSHException("problem parsing dachs answer");
			}
			
			res.put(keyvalue[0].trim(), keyvalue[1].trim());
		}
		
		return res;
	}
	
	public void shutdown() {
		this.shutdown = true;
	}

}
