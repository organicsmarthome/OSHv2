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

package osh.busdriver.mielegateway;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import osh.busdriver.mielegateway.data.MieleApplianceRawData;
import osh.busdriver.mielegateway.data.MieleDeviceHomeBusData;
import osh.busdriver.mielegateway.data.MieleDeviceList;
import osh.core.OSHGlobalLogger;


/**
 * Handling the connection to one miele gateway
 * 
 * @author Kaibin Bao, Ingo Mauser
 *
 */
public class MieleGatewayDispatcher implements Runnable {
	
	private OSHGlobalLogger logger;
	
	private HttpClient httpclient;
	private HttpContext httpcontext;
	final private CredentialsProvider httpCredsProvider;
	String homebusUrl;

	// (Miele) UID (sic!) -> device state map
	private HashMap<Integer, MieleDeviceHomeBusData> deviceData;
	
	
	/**
	 * CONSTRUCTOR
	 * @param logger
	 * @param address
	 * @throws MalformedURLException
	 */
	public MieleGatewayDispatcher(
			String gatewayHostAndPort, 
			String username, 
			String password, 
			OSHGlobalLogger logger) {
		super();
		
		this.homebusUrl = "http://" + gatewayHostAndPort + "/homebus/?language=en";
		
		this.httpCredsProvider = new BasicCredentialsProvider();
		this.httpCredsProvider.setCredentials(
			    new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), 
			    new UsernamePasswordCredentials(username, password));
		this.httpcontext = new BasicHttpContext();
		this.httpcontext.setAttribute(ClientContext.CREDS_PROVIDER, httpCredsProvider);
		
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(
		        new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));

		ClientConnectionManager cm = new PoolingClientConnectionManager(schemeRegistry);
		
		this.httpclient = new DefaultHttpClient(cm);
		this.httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1); // Default to HTTP 1.1 (connection persistence)
		this.httpclient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");
		this.httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 1000);
		this.httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1000);
		
		this.logger = logger;
		
		//this.parser = new MieleGatewayParser("http://" + device + "/homebus");
		this.deviceData = new HashMap<Integer, MieleDeviceHomeBusData>();
		
		new Thread(this, "MieleGatewayDispatcher for " + gatewayHostAndPort).start();
	}
	
	/**
	 * Collect information about Miele device and provide it 
	 * (to MieleGatewayDriver)
	 * 
	 * @param id Miele UID (sic!)
	 * @return
	 */
	public MieleDeviceHomeBusData getDeviceData(Integer id) {
		MieleDeviceHomeBusData dev;
		synchronized (this) {
			dev = deviceData.get(id);
		}
		return dev;
	}
	
	/**
	 * Collect all information about Miele devices and provide it 
	 * (to MieleGatewayDriver)
	 * 
	 * @return
	 */
	public Collection<MieleDeviceHomeBusData> getDeviceData() {
		ArrayList<MieleDeviceHomeBusData> devices = new ArrayList<MieleDeviceHomeBusData>();
		synchronized (this) {
			devices.addAll(deviceData.values());
		}
		return devices;
	}
	
	public void sendCommand(String url) {
		HttpGet httpget = new HttpGet(url);
		try {
			HttpResponse response = httpclient.execute(httpget, httpcontext);				
			if( response.getStatusLine().getStatusCode() != 200 ) {
				logger.logWarning("miele@home bus driver: error sending command " + url);
			}
			EntityUtils.consume(response.getEntity());
		} catch (IOException e1) {
			httpget.abort();
			logger.logWarning("miele@home bus driver: error sending command " + url, e1);
		}
	}
	
	@Override
	public void run() {
		MieleDeviceList deviceList = new MieleDeviceList();
		JAXBContext context;

		// initialize empty device list
		deviceList.setDevices(Collections.<MieleDeviceHomeBusData>emptyList());
		
		try {
			context = JAXBContext.newInstance(MieleDeviceList.class);
		} catch (JAXBException e1) {
			logger.logError("unable to initialize XML marshaller", e1);
			return;
		}
		
		while (true) {
			// fetch device list
			try {
				HttpGet httpget = new HttpGet(homebusUrl);
				HttpResponse response = httpclient.execute(httpget, httpcontext);
				HttpEntity entity = response.getEntity();
				
				if (entity != null) {
				    InputStream instream = entity.getContent();
				    
					// Process the XML
					try {
						Unmarshaller unmarshaller = context.createUnmarshaller();
						deviceList = (MieleDeviceList) unmarshaller.unmarshal(instream);
					} catch (JAXBException e) {
						logger.logError("failed to unmarshall miele homebus xml", e);
						deviceList.setDevices(Collections.<MieleDeviceHomeBusData>emptyList()); // set empty list
				    } finally {
				        instream.close();
				        if ( deviceList == null ) {
				        	deviceList = new MieleDeviceList();
				        }
				        if ( deviceList.getDevices() == null ) {
				        	deviceList.setDevices(Collections.<MieleDeviceHomeBusData>emptyList()); // set empty list
				        }
				    }
				}
			} catch (IOException e1) {
				deviceList.setDevices(Collections.<MieleDeviceHomeBusData>emptyList()); // set empty list
				logger.logWarning("miele@home bus driver: failed to fetch device list; " + e1.getMessage());
				logger.logInfo("miele@home bus driver: failed to fetch device list", e1);
			}
			
			// fetch device details
			for ( MieleDeviceHomeBusData dev : deviceList.getDevices() ) {
				try {
					HttpGet httpget = new HttpGet(dev.getDetailsUrl());
					HttpResponse response = httpclient.execute(httpget, httpcontext);
					HttpEntity entity = response.getEntity();
					if (entity != null) {
					    InputStream instream = entity.getContent();
					    
						// Process the XML
						try {
							Unmarshaller unmarshaller = context.createUnmarshaller();
							MieleApplianceRawData deviceDetails = (MieleApplianceRawData) unmarshaller.unmarshal(instream);
							dev.setDeviceDetails(deviceDetails);
						} catch (JAXBException e) {
							logger.logError("failed to unmarshall miele homebus detail xml", e);
					    } finally {
					        instream.close();
					    }
					}
				} catch (IOException e2) {
					// ignore
				}
			}
			
			// store device state
			synchronized (this) {
				deviceData.clear();
				for ( MieleDeviceHomeBusData dev : deviceList.getDevices() ) {
					deviceData.put(dev.getUid(), dev);
				}
				this.notifyAll();
			}
			
			// wait a second till next state fetch
			try {
				Thread.sleep(1000);
			}
			catch (InterruptedException e3) {
				logger.logError("sleep interrupted - miele@home bus driver dies right now...");
				break;
			}
		}
	}
}
