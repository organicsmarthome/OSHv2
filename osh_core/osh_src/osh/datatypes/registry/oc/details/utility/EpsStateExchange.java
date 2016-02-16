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

package osh.datatypes.registry.oc.details.utility;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import osh.datatypes.VirtualCommodity;
import osh.datatypes.limit.PriceSignal;
import osh.datatypes.registry.StateExchange;


/**
 * 
 * @author Ingo Mauser
 *
 */
public class EpsStateExchange extends StateExchange {
	
	private HashMap<VirtualCommodity,PriceSignal> priceSignals;
	
	
	/**
	 * CONSTRUCTOR
	 * @param sender
	 * @param timestamp
	 */
	public EpsStateExchange(UUID sender, long timestamp) {
		super(sender, timestamp);
		
		priceSignals = new HashMap<>();
	}
	
	public void setPriceSignals(HashMap<VirtualCommodity,PriceSignal> priceSignals) {
		this.priceSignals = new HashMap<>();
		
		for (Entry<VirtualCommodity,PriceSignal> e : priceSignals.entrySet()) {
			this.priceSignals.put(e.getKey(), e.getValue().clone());
		}
	}
	
	
	public void setPriceSignal(VirtualCommodity vc, PriceSignal priceSignal) {
		PriceSignal copy = priceSignal.clone();
		priceSignals.put(vc, copy);
	}


	public HashMap<VirtualCommodity, PriceSignal> getPriceSignals() {
		return priceSignals;
	}
	
	@Override
	public EpsStateExchange clone() {
		EpsStateExchange clonedX = new EpsStateExchange(getSender(), getTimestamp());
		clonedX.setPriceSignals(this.getPriceSignals());
		return clonedX;
	}
}