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

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * 
 * @author Florian Allerding
 * 
 */
public class MieleHomeBusParser {

	Document dom;
	String name;
	String value;
		
	public void update(MieleAppliance device) throws Exception {
		//parse die xml Datei um ein dom (Document) object zu erhalten
		parseXmlFile(device);
		//parse das dom-Object und updatet die entsprechenden Eintr�ge im Device Object
		parseDocument(device);
	}
	
	/**
	 * Create XML document
	 * @param device
	 * @throws Exception
	 */
	private void parseXmlFile(MieleAppliance device) throws Exception {
		DocumentBuilderFactory dbf  = DocumentBuilderFactory.newInstance();
		java.io.InputStream in = null;
		try {
			DocumentBuilder db = null;
			//instance of document builder
			db = dbf.newDocumentBuilder();
			
			URL xmlUrl = new URL(device.getHomebusURL());
			URLConnection urlConn = xmlUrl.openConnection();
			urlConn.setConnectTimeout(15000);
			urlConn.setReadTimeout(15000);
			in = urlConn.getInputStream();
			dom = db.parse(in);
			in.close();
			
//			// for testing in offline usage (instead of lines above)
//			dom = db.parse(device.getHomebusURL());  
			          
		} catch(Exception ex) {
			if (in != null) in.close();
			throw new Exception(ex);
		}
	}
	
	/**
	 * Parse document for Device and save to device
	 * @param device
	 */
	private void parseDocument(MieleAppliance device) {
		//get the root elememt
		Element docEle = dom.getDocumentElement();
				
		// get NodeList consisting of the nodes
		NodeList devicesNodeList = docEle.getChildNodes();
		
		// for-Schleife: updates Class Type, StateID, detailURL/DetailName des ausgew�hlten Device-objektes
		for ( int i = 0; i < devicesNodeList.getLength(); i++ ) {
			Node deviceNode = devicesNodeList.item(i);
			NodeList deviceNodeItems = deviceNode.getChildNodes();
			String UidString=deviceNodeItems.item(1).getTextContent();
						
			// if the current device is the selected one
			if ( Integer.parseInt(UidString) == device.getUid() ){
				device.setClasse(Integer.parseInt(deviceNodeItems.item(0).getTextContent()));
				device.setType(deviceNodeItems.item(2).getTextContent());
				device.setStateID(Integer.parseInt(deviceNodeItems.item(4).getTextContent()));
									
				// set DetailURL, set DetailName
				Node actionsNode = deviceNodeItems.item(8);
				NodeList actionNodeList = actionsNode.getChildNodes();
				Node actionNode=actionNodeList.item(1);
				NamedNodeMap actionAttributes = actionNode.getAttributes();
				if ( actionAttributes.item(1).getNodeValue() != null ) {
					device.setDetailName(actionAttributes.item(1).getNodeValue());
				}
				if ( actionAttributes.item(0).getNodeValue() != null ) {
					device.setDetailURL(actionAttributes.item(0).getNodeValue());
				}
					
			} //end if
		} // end for
	} // end parseDocument()

	
//	// Main-Method for testing in offline mode
//	public static void main(String[] args){
//		//create an instance
//		try{
//		HBParser dpe = new HBParser();
//		Device device = new Device();
//		device.setUid(-1609555628);
//		device.setHomebusURL("state3_lokal.xml");
//		//call run example
//		dpe.update(device);
//		System.out.println(device.toString());
//		} 
//		catch ( MalformedURLException k ){
//			System.out.println("fehler");
//		}	
//	}
	
} 
