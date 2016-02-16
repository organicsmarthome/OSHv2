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

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import osh.configuration.system.DeviceTypes;
import osh.datatypes.Commodity;
import osh.datatypes.registry.StateExchange;


/**
 * Current power consumption of all devices covered by this EMS
 * @author Ingo Mauser
 *
 */
public class CommodityPowerStateExchange extends StateExchange {
	
	HashMap<Commodity,Double> powerState;
	DeviceTypes deviceType;


	/**
	 * CONSTRUCTOR
	 * @param sender
	 * @param timestamp
	 */
	public CommodityPowerStateExchange(
			UUID sender, 
			long timestamp,
			DeviceTypes deviceType) {
		this(
				sender, 
				timestamp, 
				new HashMap<Commodity,Double>(),
				deviceType);
	}
	
	/**
	 * CONSTRUCTOR
	 * @param sender
	 * @param timestamp
	 * @param powerState
	 */
	public CommodityPowerStateExchange(
			UUID sender, 
			long timestamp,
			HashMap<Commodity,Double> powerState,
			DeviceTypes deviceType) {
		super(sender, timestamp);

		this.powerState = powerState;
		this.deviceType = deviceType;
	}
	
	
	public void addPowerState(Commodity commodity, double value) {
		powerState.put(commodity, value);
	}
	
	
	public Double getPowerState(Commodity commodity) {
		return powerState.get(commodity);
	}
	
	
	public HashMap<Commodity, Double> getPowerState() {
		return powerState;
	}
	
	public DeviceTypes getDeviceType() {
		return deviceType;
	}

	@Override
	public CommodityPowerStateExchange clone() {
		CommodityPowerStateExchange cloned = new CommodityPowerStateExchange(
				this.getSender(), 
				this.getTimestamp(),
				this.getDeviceType());
		
		for (Entry<Commodity,Double> e : powerState.entrySet()) {
			Double value = e.getValue();
			if (value != null) {
				value = (double) value;
			}
			cloned.addPowerState(e.getKey(), value);
		}
		
		return cloned;
	
	}

	@Override
	public String toString() {
		return "CommodityPowerState: " + powerState.toString();
	}
	
}
