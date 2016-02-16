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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import osh.core.OSHGlobalLogger;


/**
 * 
 * @author Florian Allerding, Kaibin Bao, Till Schuberth, Ingo Mauser
 *
 */
public class DachsPowerRequestThread implements Runnable {
	
	private boolean setOn;
	private String dachsURL;
	private OSHGlobalLogger logger;

	
	/**
	 * CONSTRUCTOR
	 * @param setOn
	 * @param dachsURL
	 * @param logger
	 */
	public DachsPowerRequestThread(boolean setOn, String dachsURL, OSHGlobalLogger logger) {
		this.setOn = setOn;
		this.dachsURL = dachsURL;
		this.logger = logger;
	}
	
	
	@Override
	public void run() {
		try {
			doPowerRequest();
		} 
		catch (Exception e) {
			if (logger != null) {
				logger.logError("FIXME:", e);
			}
			
			throw new RuntimeException(e); //re-throw
		}
	}
	
	private void doPowerRequest() {
		
		if (logger != null) {
			logger.logDebug("doPowerRequest()");
		}
		
		// Construct data
		List <NameValuePair> datalist = new ArrayList <NameValuePair>(); 
		if ( setOn ) {
			datalist.add(new BasicNameValuePair("Stromf_Ew.Anforderung_GLT.bAktiv", "1"));
			datalist.add(new BasicNameValuePair("Stromf_Ew.Anforderung_GLT.bAnzahlModule", "1"));
		}
		else {
			datalist.add(new BasicNameValuePair("Stromf_Ew.Anforderung_GLT.bAktiv", "0"));
			datalist.add(new BasicNameValuePair("Stromf_Ew.Anforderung_GLT.bAnzahlModule", "1"));
		}
		UrlEncodedFormEntity data;
		try {
			data = new UrlEncodedFormEntity(datalist, HTTP.UTF_8);
		} 
		catch (UnsupportedEncodingException e) {
			if (logger != null) {
				logger.logError("internal exception", e);
			}
			
			return;
		}					

		DefaultHttpClient client = new DefaultHttpClient();
		try {
			HttpPost httppost = new HttpPost(dachsURL + "setKeys");
			client.getCredentialsProvider().setCredentials(new AuthScope(httppost.getURI().getHost(), httppost.getURI().getPort()), new UsernamePasswordCredentials("glt", ""));

			AuthCache authCache = new BasicAuthCache();
			BasicScheme basicAuth = new BasicScheme();
			authCache.put(new HttpHost(httppost.getURI().getHost(), httppost.getURI().getPort(), httppost.getURI().getScheme()), basicAuth);
			BasicHttpContext localcontext = new BasicHttpContext();
			localcontext.setAttribute(ClientContext.AUTH_CACHE, authCache);
			
			httppost.setEntity(data);

			HttpResponse response = client.execute(httppost, localcontext);
			
			if (logger == null) {
				System.out.println("" + response);
			}

			HttpEntity entity = response.getEntity();
			EntityUtils.consume(entity);
		} 
		catch (IOException ex){
			if (logger != null) {
				logger.logError("changing dachs status failed", ex);
			}
		} 
		finally {
			client.getConnectionManager().shutdown();
		}
	}
	
}
