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

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import osh.datatypes.Commodity;

/**
 * 
 * @author Ingo Mauser
 *
 */
public class LoggerDevicesPowerHALExchange extends HALExchange {
	
	private HashMap<UUID,HashMap<Commodity,Double>> powerStates;

	
	/**
	 * CONSTRUCTOR
	 */
	public LoggerDevicesPowerHALExchange(
			UUID deviceID, 
			Long timestamp, 
			HashMap<UUID,HashMap<Commodity,Double>> powerStates) {
		super(deviceID, timestamp);
		
		this.powerStates = new HashMap<>();
		
		for (Entry<UUID,HashMap<Commodity,Double>> e : powerStates.entrySet()) {
			HashMap<Commodity,Double> current = new HashMap<Commodity,Double>();
			this.powerStates.put(e.getKey(), current);
			
			for (Entry<Commodity,Double> f: e.getValue().entrySet()) {
				current.put(f.getKey(), f.getValue());
			}
		}
	}

	
	public HashMap<UUID, HashMap<Commodity, Double>> getPowerStates() {
		return powerStates;
	}
	
}
