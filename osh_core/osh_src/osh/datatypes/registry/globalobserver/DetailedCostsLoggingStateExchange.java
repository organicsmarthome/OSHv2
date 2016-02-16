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
import osh.datatypes.limit.PowerLimitSignal;
import osh.datatypes.limit.PriceSignal;
import osh.datatypes.registry.StateExchange;

/**
 * 
 * @author Ingo Mauser
 *
 */
public class DetailedCostsLoggingStateExchange extends StateExchange {
	
	private HashMap<VirtualCommodity,Integer> map;
	
	private HashMap<VirtualCommodity,PriceSignal> ps;
	private HashMap<VirtualCommodity,PowerLimitSignal> pwrLimit;

	
	/**
	 * CONSTRUCTOR
	 * @param sender
	 * @param timestamp
	 * @param map
	 */
	public DetailedCostsLoggingStateExchange(
			UUID sender, 
			long timestamp, 
			HashMap<VirtualCommodity,Integer> map,
			HashMap<VirtualCommodity,PriceSignal> ps,
			HashMap<VirtualCommodity,PowerLimitSignal> pwrLimit) {
		super(sender, timestamp);

		this.map = map;
		
		this.ps = ps;
		this.pwrLimit = pwrLimit;
	}
	
	
	public HashMap<VirtualCommodity, Integer> getMap() {
		return map;
	}
	
	public HashMap<VirtualCommodity,PriceSignal> getPs() {
		return ps;
	}

	public HashMap<VirtualCommodity,PowerLimitSignal> getPwrLimit() {
		return pwrLimit;
	}


	@Override
	public StateExchange clone() {
		
		HashMap<VirtualCommodity,Integer> clonedMap = new HashMap<>();
		for (Entry<VirtualCommodity,Integer> e : this.map.entrySet()) {
			clonedMap.put(e.getKey(), e.getValue());
		}
		
		HashMap<VirtualCommodity,PriceSignal> newPs = new HashMap<>();
		for (Entry<VirtualCommodity,PriceSignal> e : ps.entrySet()) {
			newPs.put(e.getKey(), e.getValue().clone());
		}
		
		HashMap<VirtualCommodity,PowerLimitSignal> newPls = new HashMap<>();
		for (Entry<VirtualCommodity,PowerLimitSignal> e : pwrLimit.entrySet()) {
			newPls.put(e.getKey(), e.getValue().clone());
		}
		
		DetailedCostsLoggingStateExchange cloned = new DetailedCostsLoggingStateExchange(
				getSender(), 
				getTimestamp(), 
				clonedMap,
				newPs,
				newPls);
		
		return cloned;
	}
}
