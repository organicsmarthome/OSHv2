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

package osh.datatypes.registry.details.common;

import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import osh.configuration.system.DeviceClassification;
import osh.configuration.system.DeviceTypes;
import osh.datatypes.registry.StateExchange;

@XmlAccessorType( XmlAccessType.PUBLIC_MEMBER )
@XmlType
public class DeviceMetaDriverDetails extends StateExchange {

	protected String name;
	protected String location;
	
	protected DeviceTypes deviceType;
	protected DeviceClassification deviceClassification;
	
	protected boolean configured;
	
	protected String icon;

	/** for JAXB */
	@SuppressWarnings("unused")
	@Deprecated
	private DeviceMetaDriverDetails() {
		this(null, 0);
	}

	/**
	 * CONSTRUCTOR
	 * @param sender
	 * @param timestamp
	 */
	public DeviceMetaDriverDetails(UUID sender, long timestamp) {
		super(sender, timestamp);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public DeviceTypes getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceTypes deviceType) {
		this.deviceType = deviceType;
	}

	public DeviceClassification getDeviceClassification() {
		return deviceClassification;
	}

	public void setDeviceClassification(DeviceClassification deviceClassification) {
		this.deviceClassification = deviceClassification;
	}
	
	public boolean isConfigured() {
		return configured;
	}
	
	public void setConfigured(boolean configured) {
		this.configured = configured;
	}
	
	
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Override
	public String toString() {
		return "Device \"" + name + "\" in " + location + " type: " + deviceType + "(" + deviceClassification + ")";
	}
	
	@Override
	public DeviceMetaDriverDetails clone() {
		long uuidSenderLSB = this.sender.getLeastSignificantBits();
		long uuidSenderMSB = this.sender.getMostSignificantBits();
		DeviceMetaDriverDetails clone = new DeviceMetaDriverDetails(new UUID(uuidSenderMSB, uuidSenderLSB), this.getTimestamp());
		
		clone.name = this.name;
		clone.location = this.location;
		
		clone.deviceType = this.deviceType;
		clone.deviceClassification = this.deviceClassification;
		
		clone.configured = this.configured;
		
		clone.icon = this.icon;
		
		return clone;
	}
	
}
