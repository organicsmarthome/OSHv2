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

package osh.hal.exchange.xmlannot;

import java.util.UUID;

import javax.xml.bind.annotation.XmlAttribute;

import osh.hal.exchange.IHALExchange;


/**
 * Super class for Universal(Observer|Controller)Exchange
 * This class is independent from HALExchanges to avoid mixing core data structures with xml annotations
 * 
 * @author Kaibin Bao
 *
 */
public class BasicExchange implements IHALExchange {
	@XmlAttribute(name="uuid")
	protected UUID deviceID;
	
	@XmlAttribute
	protected Long timestamp;
	
	/**
	 * Don't use this constructor (except when you're using JAXB)
	 */
	@Deprecated
	public BasicExchange() {
	}
	
	public BasicExchange(UUID deviceID, Long timestamp) {
		super();
		this.deviceID = deviceID;
		this.timestamp = timestamp;
	}

	@Override
	public UUID getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(UUID deviceID) {
		this.deviceID = deviceID;
	}

	@Override
	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
}
