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
import java.util.UUID;
import java.util.Map.Entry;

import osh.datatypes.Commodity;
import osh.datatypes.registry.StateExchange;


/**
 * 
 * @author Ingo Mauser
 *
 */
public class DevicesPowerStateExchange extends StateExchange {
	
	private HashMap<UUID,HashMap<Commodity,Double>> powerStates;
	
	/**
	 * CONSTRUCTOR
	 * @param sender
	 * @param timestamp
	 */
	public DevicesPowerStateExchange(UUID sender, long timestamp) {
		this(sender, timestamp, new HashMap<UUID,HashMap<Commodity,Double>>());
	}
	
	/**
	 * CONSTRUCTOR
	 * @param sender
	 * @param timestamp
	 * @param powerStates
	 */
	public DevicesPowerStateExchange(
			UUID sender, 
			long timestamp, 
			HashMap<UUID,HashMap<Commodity,Double>> powerStates) {
		super(sender, timestamp);
		
		this.powerStates = powerStates;
	}
	
	
	public void addPowerState(UUID uuid, Commodity commodity, double value) {
		
		HashMap<Commodity,Double> existingMap = powerStates.get(uuid);
		
		if (existingMap == null) {
			// Create new map and add
			HashMap<Commodity,Double> newMap = new HashMap<>();
			powerStates.put(uuid, newMap);
			existingMap = newMap;
		}
		
		existingMap.put(commodity, value);
	}
	
	public Double getPowerState(UUID uuid, Commodity commodity) {
		
		Double returnValue = null;
		
		HashMap<Commodity,Double> existingMap = powerStates.get(uuid);
		if (existingMap != null) {
			returnValue = existingMap.get(commodity);
		}
		
		return returnValue;
	}
	
	public HashMap<UUID,HashMap<Commodity,Double>> getPowerStateMap(){
		return powerStates;
	}
	
	
	@Override
	public DevicesPowerStateExchange clone() {
		DevicesPowerStateExchange cloned = new DevicesPowerStateExchange(this.getSender(), this.getTimestamp());
		
		for (Entry<UUID,HashMap<Commodity,Double>> e : powerStates.entrySet()) {
			UUID uuid = e.getKey();
			HashMap<Commodity,Double> innerMap = e.getValue();
			
			for (Entry<Commodity,Double> f : innerMap.entrySet()) {
				cloned.addPowerState(uuid, f.getKey(), f.getValue());
			}
		}
		
		return cloned;
	}
	
}
