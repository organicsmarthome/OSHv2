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

package osh.datatypes.logger;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import osh.datatypes.VirtualCommodity;
import osh.datatypes.limit.PowerLimitSignal;
import osh.datatypes.limit.PriceSignal;
import osh.hal.exchange.HALExchange;

/**
 * 
 * @author Ingo Mauser
 *
 */
public class LoggerDetailedCostsHALExchange extends HALExchange {

	private HashMap<VirtualCommodity,Integer> powerValueMap;
	
	private HashMap<VirtualCommodity,PriceSignal> ps;
	private HashMap<VirtualCommodity,PowerLimitSignal> pwrLimit;
	
	
	/**
	 * CONSTRUCTOR
	 * @param deviceID
	 * @param timestamp
	 */
	public LoggerDetailedCostsHALExchange(
			UUID deviceID, 
			Long timestamp,
			HashMap<VirtualCommodity,Integer> map,
			HashMap<VirtualCommodity,PriceSignal> ps,
			HashMap<VirtualCommodity,PowerLimitSignal> pwrLimit) {
		super(deviceID, timestamp);
		
		this.powerValueMap = new HashMap<>();
		
		for (Entry<VirtualCommodity,Integer> e : map.entrySet()) {
			this.powerValueMap.put(e.getKey(), e.getValue());
		}
		
		this.ps = ps;
		this.pwrLimit = pwrLimit;
	}

	public HashMap<VirtualCommodity, Integer> getPowerValueMap() {
		return powerValueMap;
	}
	
	public HashMap<VirtualCommodity, PriceSignal> getPs() {
		return ps;
	}

	public HashMap<VirtualCommodity,PowerLimitSignal> getPwrLimit() {
		return pwrLimit;
	}

}
