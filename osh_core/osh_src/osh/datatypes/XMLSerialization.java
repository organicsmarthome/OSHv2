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

package osh.datatypes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * to simplify the XML-serialization using the java built-in marshaller
 *@author florian.allerding@kit.edu
 *@category smart-home ControllerBox
 */
public class XMLSerialization {
	
    /**
     * marshal (serialize) an object (JaXB!!!) to an outputstream
     * @param os
     * @param obj
     * @throws JAXBException
     */
    public static void marshal(OutputStream os, Object obj) throws JAXBException {
    	
    	JAXBContext jc = JAXBContext.newInstance(obj.getClass().getPackage().getName());
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.marshal(obj, os);
    }
 
    
    /**
     * marshal (serialize) an object (JaXB!!!) to file with the given name
     * @param fileName
     * @param obj
     * @throws FileNotFoundException
     * @throws JAXBException
     */
    public static void marshal2File(String fileName, Object obj) throws FileNotFoundException, JAXBException {
    	
    	FileOutputStream fileStream = new FileOutputStream(fileName);
    	marshal(fileStream, obj);
  
    }
     
    /**
     * unmarshal (deserialize) an inputstream (JaXB!!!) to an object based on the the given targetClass
     * Note: you have to cast from object to the specified object before use ;-)
     * @param is
     * @param targetClass
     * @return
     * @throws JAXBException
     */
    @SuppressWarnings({ "rawtypes" })
	public static Object unmarshal(InputStream is, Class targetClass) throws JAXBException {
    	
		JAXBContext jc = JAXBContext.newInstance(targetClass.getPackage().getName());
		Unmarshaller um = jc.createUnmarshaller();
		return um.unmarshal(is);

    }
    
    /**
     * unmarshal (deserialize) from a file (JaXB!!!) to an object based on the the given targetClass
     * Note: you have to cast from object to the specified object before use ;-)
     * @param fileName
     * @param targetClass
     * @return
     * @throws FileNotFoundException
     * @throws JAXBException
     */
    @SuppressWarnings({ "rawtypes" })
	public static Object file2Unmarshal(String fileName, Class targetClass) throws FileNotFoundException, JAXBException {
    
    	FileInputStream fileStream = new FileInputStream(fileName);
    	return unmarshal(fileStream,targetClass);
    }
    
 

}
