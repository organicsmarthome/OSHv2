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

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * Parser for the Miele additional infos (details) for a Miele device
 * @author Florian Allerding
 */

public class MieleAddInfoParser {

	Element docEle;
	Document docu;
	URL xmlUrl;

	LinkedList<MieleApplianceDetails> addInfoList;

	String name;
	String value;
	MieleApplianceDetails deviceDetails;

	LinkedList<MieleApplianceDetails> addActionList;
	
		
	/**
	 * first: call parseXmlFile method
	 * second: call parseDocument method
	 * @param device updated device object
	 */
	public  void update (MieleAppliance device)throws Exception {

		// parse the xml-file, instantiate Document-Object
		parseXmlFile(device);
		
		// parse the document-object
		// update AddInfoList and AddActionList of the
		// object device
		parseDocument(device);
		
	}
	
	/**
	 * update document of XML file
	 * @param device using the device object -> call DetailURL() -> create document
	 * @throws Exception 
	 */
	
	private void parseXmlFile(MieleAppliance device)throws Exception{
		// instantiate factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		InputStream in = null;
		try {
			// instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			xmlUrl = new URL(device.getDetailURL());
			URLConnection urlConn = xmlUrl.openConnection();
			urlConn.setConnectTimeout(15000);
			urlConn.setReadTimeout(15000);
			in = urlConn.getInputStream();
			docu = db.parse(in);
			//get the child element "device" as Document
			docEle = docu.getDocumentElement();
			in.close();
			
			}
		
		catch(Exception ex) {
			if (in != null) in.close();
			throw new Exception();
		}
	} // end parseXmlFile()-Method

	
	/**
	 * Read document element, update device object
	 * @param device list elements of the device are updated
	*/	
	private void parseDocument(MieleAppliance device)throws MalformedURLException{
		
		// get NodeList consisting of the nodes "information" and "action"
		NodeList devices = docEle.getChildNodes();
		// get the child node "information" of "device" 
		Node informationNode = devices.item(0);
		//get the nodelist "keyList" of "information" 
		NodeList keyNodeList = informationNode.getChildNodes();
		//each run a new LinkedList because the number of elements might change
		addInfoList = new LinkedList<MieleApplianceDetails>();
			//fill the addInfoList
			for(int i=0; i<keyNodeList.getLength(); i++){
				Node keyNode = keyNodeList.item(i);
				NamedNodeMap keyAttributes = keyNode.getAttributes();
				
				if(keyAttributes.item(1).getNodeValue()!=null)
				name = keyAttributes.item(1).getNodeValue();
				
				if(keyAttributes.item(0).getNodeValue()!=null)
				value = keyAttributes.item(0).getNodeValue();
				
				deviceDetails = new MieleApplianceDetails(name, value);
				addInfoList.add(deviceDetails);
			} // end for
			
			device.setAddInfoList(addInfoList);
				
				// get the child node "actions" of "device" 
				Node actionsNode = devices.item(1);
										
				//get the nodelist "keyList" of "information" 
				NodeList actionsNodeList = actionsNode.getChildNodes();
				//each run a new LinkedList because the number of elements might change
				addActionList = new LinkedList<MieleApplianceDetails>();
					//fill the addInfoList
				
					for(int i=0; i<actionsNodeList.getLength(); i++){
						Node actionNode = actionsNodeList.item(i);
						NamedNodeMap keyAttributes = actionNode.getAttributes();
					
						if(keyAttributes.item(0).getNodeValue()!=null)
						name = keyAttributes.item(0).getNodeValue();
					
						if(keyAttributes.item(1).getNodeValue()!=null)
						value = keyAttributes.item(1).getNodeValue();
											
						deviceDetails = new MieleApplianceDetails(name, value);
						addActionList.add(deviceDetails);
					} // end - for Schleife
					device.setAddActionList(addActionList);
	} // end parseDocument()-method
	
}
