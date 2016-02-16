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

import osh.datatypes.VirtualCommodity;
import osh.datatypes.registry.StateExchange;

/**
 * 
 * @author Ingo Mauser
 *
 */
public class VirtualCommodityPowerStateExchange extends StateExchange {
	
	private HashMap<VirtualCommodity,Integer> map;

	
	/**
	 * CONSTRUCTOR
	 * @param sender
	 * @param timestamp
	 * @param map
	 */
	public VirtualCommodityPowerStateExchange(UUID sender, long timestamp, HashMap<VirtualCommodity,Integer> map) {
		super(sender, timestamp);

		this.map = map;
	}
	
	
	public HashMap<VirtualCommodity, Integer> getMap() {
		return map;
	}


	@Override
	public StateExchange clone() {
		HashMap<VirtualCommodity,Integer> clonedMap = new HashMap<>();
		
		for (Entry<VirtualCommodity,Integer> e : this.map.entrySet()) {
			clonedMap.put(e.getKey(), e.getValue());
		}
		
		VirtualCommodityPowerStateExchange cloned = new VirtualCommodityPowerStateExchange(
				getSender(), 
				getTimestamp(), 
				clonedMap);
		
		return cloned;
	}
}
