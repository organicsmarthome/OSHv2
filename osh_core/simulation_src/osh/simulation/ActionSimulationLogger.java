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

package osh.simulation;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import osh.core.OSHGlobalLogger;
import osh.datatypes.XMLSerialization;
import osh.simulation.screenplay.Screenplay;
import osh.simulation.screenplay.SubjectAction;

public class ActionSimulationLogger implements ISimulationActionLogger {

	private OSHGlobalLogger logger;
	private OutputStream stream;
	
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = {
	    "element"
	})
	@XmlRootElement(name = "root")
	public static class RootElement {
		
		
		@XmlElement(name = "element")
		private SubjectAction element;

		public RootElement() {}
		
		public RootElement(SubjectAction element) {
			super();
			this.element = element;
		}

		public SubjectAction getElement() {
			return element;
		}

		public void setElement(SubjectAction element) {
			this.element = element;
		}
		
	}
	
	public ActionSimulationLogger(OSHGlobalLogger logger, String filename) throws FileNotFoundException {
		this.logger = logger;
		stream = new FileOutputStream(filename);
	}
	@Override
	public void logAction(SubjectAction action) {
		if (stream == null) {
			logger.logError("logger stream was closed", new Exception());
			return;
		}
		
		try {
			Screenplay sp = new Screenplay();
			sp.getSIMActions().add(action);
			XMLSerialization.marshal(stream, sp);
		} catch (JAXBException e) {
			logger.logError("could not log action", e);
		}
	}
	
	public void closeStream() {
		try {
			if (stream != null) stream.close();
		} catch (IOException e) {}
		stream = null;
	}

}
