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
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * @author Florian Allerding
 * 
 */
public class MieleGatewayParser {

	//No generics
	private Document dom;
	private URL xmlUrl;


	/**
	 * CONSTRUCTOR
	 */
	public MieleGatewayParser(String url) throws MalformedURLException{
		xmlUrl= new URL(url);
	}
	
	
	/**
	 * Get XML from URL, parse it and get device list
	 * 
	 * @throws NoRouteToHostException
	 * @throws ConnectException
	 */
	public void runUpdate( MieleGatewayDispatcher dispatcher ) throws Exception{
		parseXmlFile();

		if(dom!=null){
			parseDocument(dispatcher);
		}
	}
	
	/**
	 * Parse content of URL in Document-Object
	 * @throws NoRouteToHostException
	 * @throws ConnectException
	 */
	private void parseXmlFile() throws Exception{
		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		InputStream in = null;
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			URLConnection urlConn = xmlUrl.openConnection();
			urlConn.setConnectTimeout(15000);
			urlConn.setReadTimeout(15000);
			in = urlConn.getInputStream();
			dom = db.parse(in);
			in.close();
		} catch(Exception ex) {
			if (in != null) in.close();
			throw new Exception(ex);
		}
	}

	/**
	 * create device list from content of XML
	 */
	private void parseDocument(MieleGatewayDispatcher dispatcher) throws Exception{
		//get the root element
		Element docEle = dom.getDocumentElement();
		
		//get a nodelist of <Device> elements
		NodeList nl = docEle.getElementsByTagName("device");
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
				
				//get the Device element
				Element el = (Element)nl.item(i);
				
				//get the Device object
				MieleAppliance e = getDevice(el);
				
				//set pID
				e.setpId((i+1));
			
				//add Device objects to list
				dispatcher.setApplianceData(e.getUid(), e);
			}
		}
	}

	/**
	 * Create Device-Elements
	 * @param empEl
	 * @return Device
	 * @throws Exception 
	 */
	private MieleAppliance getDevice(Element empEl) throws Exception {
		MieleAppliance device= new MieleAppliance();
		device.setName(getTextValue(empEl,"name"));
		device.setClasse (getIntValue(empEl,"class"));
		device.setUid (getIntValue(empEl,"UID"));
		device.setType (getTextValue(empEl, "type"));
        device.setStateID (getIntValue(empEl, "state"));
        device.setAddName(getTextValue  (empEl, "additionalName"));
        // set the content of the room tag
        device.setRoomName(getTextValue  (empEl, "room"));
        // get the attributes of room tag
        parseRoom(empEl, device);
        //get detailName and detailURL
        parseActions(empEl,device);
        
        MieleAddInfoParser addInfo = new MieleAddInfoParser();
        try {
			addInfo.update(device);
		} catch (Exception e) {
			throw new Exception(e);
		}
    
		return device;
	}
	
	/**
	 * Parse information of property "room"
	 * level, id
	 * @param empe
	 * @param device
	 */
	public void parseRoom(Element empe, MieleAppliance device){
    	NodeList nl= empe.getElementsByTagName("room");
    	if( nl != null && nl.getLength() > 0 ) {
			Element el = (Element)nl.item(0);
			if ( el != null ) {
				device.setRoomLevel(el.getAttribute("level"));				
				try {
					int tmp = Integer.parseInt(el.getAttribute("id"));	
					device.setRoomID(tmp);
				} catch (Exception e) {
					//do nothing
				}
			}
		}
    }
	
	/**
	 * Parse Detailurl and Detailname
	 * @param empe
	 * @param device
	 */
	public void parseActions(Element empe, MieleAppliance device) {
		//get a nodelist of <actions> elements
    	NodeList n1= empe.getElementsByTagName("actions");
    	NodeList n2;
    	if ( n1 != null && n1.getLength() > 0 ) {
			Element el = (Element)n1.item(0);
			//get a nodelist of <action> elements
			n2 = el.getElementsByTagName("action");
			if( n2 != null && n2.getLength() > 0 ) {
				Element elx=(Element)n2.item(0);
				if ( elx != null ) {
					device.setDetailName(elx.getAttribute("name"));
					device.setDetailURL(elx.getAttribute("URL"));
				}
			}
		}
    }


	/**
	 * Provide String in element for device
	 * @param ele
	 * @param tagName
	 * @return String
	 */
	private String getTextValue(Element ele, String tagName) {
		String textVal = null;
		//get a nodelist of <tagname> elements
		NodeList nl = ele.getElementsByTagName(tagName);
		if ( nl != null && nl.getLength() > 0 ) {
			Element el = (Element)nl.item(0);
			Node tmpNode = el.getFirstChild();
			if (tmpNode!=null) {
				textVal = tmpNode.getNodeValue();
			}
		}
		return textVal;
	}

	
	/**
	 * Provide Int-value in element for Device
	 * @param ele
	 * @param tagName
	 * @return int
	 */
	private int getIntValue(Element ele, String tagName) {
		//in production application you would catch the exception
		return Integer.parseInt(getTextValue(ele,tagName));
	}
	
}
