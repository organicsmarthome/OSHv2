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

package osh.hal.exchange;

import java.util.UUID;

import osh.configuration.system.DeviceClassification;
import osh.configuration.system.DeviceTypes;
import osh.datatypes.hal.interfaces.IHALDeviceMetaDetails;
import osh.datatypes.registry.details.common.DeviceMetaDriverDetails;

/**
 * Please remind cloning!
 * @author Florian Allerding, Ingo Mauser
 *
 */
public class HALDeviceObserverExchange 
				extends HALObserverExchange
				implements IHALDeviceMetaDetails {
	
	private String name;
	private String location;
	
	private DeviceTypes deviceType;
	private DeviceClassification deviceClass;
	
	private boolean configured;
	
	/**
	 * CONSTRUCTOR
	 * @param deviceID
	 * @param timestamp
	 */
	public HALDeviceObserverExchange(UUID deviceID, Long timestamp) {
		super(deviceID, timestamp);
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
		return deviceClass;
	}

	public void setDeviceClass(DeviceClassification deviceClass) {
		this.deviceClass = deviceClass;
	}

	public boolean isConfigured() {
		return configured;
	}

	public void setConfigured(boolean configured) {
		this.configured = configured;
	}
	
	public void setDeviceMetaDetails(DeviceMetaDriverDetails deviceMetaDetails) {
		this.name = deviceMetaDetails.getName();
		this.location = deviceMetaDetails.getLocation();
		
		this.deviceType = deviceMetaDetails.getDeviceType();
		this.deviceClass = deviceMetaDetails.getDeviceClassification();
		
		this.configured = deviceMetaDetails.isConfigured();
	}

}
