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

package osh.driver.miele.old;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.HashMap;

import osh.core.IOSH;
import osh.core.OSHGlobalLogger;

/**
 * 
 * @author Florian Allerding, Kaibin Bao, Till Schuberth, Ingo Mauser
 *
 */
public class MieleGatewayDispatcher implements Runnable {
	
	private OSHGlobalLogger logger;

	@SuppressWarnings("unused")
	private String device;
	
	private MieleGatewayParser parser;
	private HashMap<Integer, MieleAppliance> applianceData;
	
	
	/**
	 * CONSTRUCTOR
	 */
	public MieleGatewayDispatcher(IOSH controllerbox, String device, OSHGlobalLogger logger) throws MalformedURLException {
		this.device = device;
		this.parser = new MieleGatewayParser("http://" + device + "/homebus");
		this.applianceData = new HashMap<Integer, MieleAppliance>();
		
		this.logger = logger;
		
		new Thread(this, "MieleGatewayDispatcher for " + device).start();
	}
	
	
	void setApplianceData(Integer id, MieleAppliance data) {
		applianceData.put(id, data);
	}
	
	public MieleAppliance getApplianceData(Integer id) {
		return applianceData.get(id);
	}
	
	public Collection<MieleAppliance> getApplianceData() {
		return applianceData.values();
	}

	@SuppressWarnings("unused")
	private void sendCommand(String url) {
		try {
			URL xmlUrl = new URL(url);
			URLConnection urlConn = xmlUrl.openConnection();
			urlConn.setConnectTimeout(1000);
			urlConn.setReadTimeout(1000);
			urlConn.getInputStream().close();
		} catch (IOException e) {
			// ignore ?!
		}
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				parser.runUpdate(this);
			} catch (Exception e1) {
				logger.logError("parse error: " + e1.getMessage() + "\n continuing...");
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.logError("sleep interrupted - will continue...");
			}
		}
	}
	
}
