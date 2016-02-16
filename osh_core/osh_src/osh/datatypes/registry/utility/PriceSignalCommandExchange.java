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

package osh.datatypes.registry.utility;

import java.util.HashMap;
import java.util.UUID;

import osh.datatypes.VirtualCommodity;
import osh.datatypes.limit.PriceSignal;
import osh.datatypes.registry.CommandExchange;


/**
 * 
 * @author Ingo Mauser
 *
 */
public class PriceSignalCommandExchange extends CommandExchange {
	
	private HashMap<VirtualCommodity,PriceSignal> priceSignals;
	
	/**
	 * CONSTRUCTOR
	 * @param sender
	 * @param receiver
	 * @param timestamp
	 * @param priceSignals
	 */
	public PriceSignalCommandExchange(
			UUID sender, 
			UUID receiver, 
			long timestamp,
			HashMap<VirtualCommodity,PriceSignal> priceSignals) {
		super(sender, receiver, timestamp);
		
		this.priceSignals = priceSignals;
	}
	
	
	public HashMap<VirtualCommodity,PriceSignal> getPriceSignals() {
		return priceSignals;
	}
	
	public PriceSignal getPriceSignal(VirtualCommodity c) {
		if (priceSignals != null) {
			return priceSignals.get(c);
		}
		else {
			return null;
		}
	}
	
}
