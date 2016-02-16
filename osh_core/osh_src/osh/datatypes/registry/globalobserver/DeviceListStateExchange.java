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

package osh.datatypes.registry.globalobserver;

import java.util.Set;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import osh.datatypes.gui.DeviceTableEntry;
import osh.datatypes.registry.StateExchange;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DeviceListStateExchange extends StateExchange {

	private Set<DeviceTableEntry> deviceList;
	
	@Deprecated
	public DeviceListStateExchange() {
		super(null, 0L);
	}
	
	public DeviceListStateExchange(UUID sender, long timestamp, Set<DeviceTableEntry> deviceList) {
		super(sender, timestamp);
		this.deviceList = deviceList;
	}

	public Set<DeviceTableEntry> getDeviceList() {
		return deviceList;
	}

}
