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

import osh.datatypes.VirtualCommodity;
import osh.datatypes.limit.PowerLimitSignal;
import osh.hal.exchange.HALComExchange;


/**
 * 
 * @author Florian Allerding, Kaibin Bao, Ingo Mauser, Till Schuberth
 *
 */
public class GUIPlsComExchange extends HALComExchange {
	
	private HashMap<VirtualCommodity,PowerLimitSignal> powerLimitSignals;

	
	/**
	 * CONSTRUCTOR
	 * @param deviceID
	 * @param timestamp
	 */
	public GUIPlsComExchange(UUID deviceID, Long timestamp) {
		super(deviceID, timestamp);
	}


	public HashMap<VirtualCommodity,PowerLimitSignal> getPowerLimitSignals() {
		return powerLimitSignals;
	}

	public void setPowerLimitSignals(HashMap<VirtualCommodity,PowerLimitSignal> powerLimitSignals) {
		HashMap<VirtualCommodity,PowerLimitSignal> clonedSignal = new HashMap<>();
		for ( Entry<VirtualCommodity,PowerLimitSignal> e : powerLimitSignals.entrySet() ) {
			clonedSignal.put(e.getKey(), e.getValue().clone());
		}
		this.powerLimitSignals = clonedSignal;
	}

}
