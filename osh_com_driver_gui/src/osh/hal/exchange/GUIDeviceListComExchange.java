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

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import osh.datatypes.gui.DeviceTableEntry;
import osh.hal.exchange.HALComExchange;


/**
 * 
 * @author Ingo Mauser
 *
 */
public class GUIDeviceListComExchange extends HALComExchange {

	private Set<DeviceTableEntry> deviceList;
	
	
	/**
	 * CONSTRUCTOR
	 */
	public GUIDeviceListComExchange(
			UUID deviceID, 
			Long timestamp,
			Set<DeviceTableEntry> deviceList) {
		super(deviceID, timestamp);
		
		synchronized ( deviceList ) {
			int devListSize = deviceList.size();
			DeviceTableEntry[] dte = new DeviceTableEntry[devListSize];
			Object[] oa = deviceList.toArray();
			
			for ( int i = 0; i < devListSize; i++ ) {
				dte[i] = (DeviceTableEntry) oa[i];
			}
			
			Set<DeviceTableEntry> clonedDeviceList = new HashSet<DeviceTableEntry>();
			
			for (DeviceTableEntry e : dte) {
				clonedDeviceList.add(e.clone());
			}
			
			this.deviceList = clonedDeviceList;
		}
	}


	public Set<DeviceTableEntry> getDeviceList() {
		return deviceList;
	}

}
