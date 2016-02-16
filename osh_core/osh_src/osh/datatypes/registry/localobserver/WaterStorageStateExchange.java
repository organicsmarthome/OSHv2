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

package osh.datatypes.registry.localobserver;

import java.util.UUID;

import osh.datatypes.registry.StateExchange;


/**
 * 
 * @author ???
 *
 */
public class WaterStorageStateExchange extends StateExchange {

	private double currenttemp, mintemp, maxtemp;
	
	/**
	 * CONSTRUCTOR
	 * @param sender
	 * @param timestamp
	 * @param currenttemp
	 * @param mintemp
	 * @param maxtemp
	 */
	public WaterStorageStateExchange(
			UUID sender, 
			long timestamp, 
			double currenttemp, 
			double mintemp, 
			double maxtemp) {
		super(sender, timestamp);
		this.currenttemp = currenttemp;
		this.mintemp = mintemp;
		this.maxtemp = maxtemp;
	}

	public double getCurrenttemp() {
		return currenttemp;
	}

	public double getMintemp() {
		return mintemp;
	}

	public double getMaxtemp() {
		return maxtemp;
	}

	public boolean equalData(WaterStorageStateExchange o) {
		if (o instanceof WaterStorageStateExchange) {
			WaterStorageStateExchange oex = (WaterStorageStateExchange) o;
			
			//compare using an epsilon environment
			if (Math.abs(currenttemp - oex.currenttemp) < 0.001 &&
					Math.abs(mintemp - oex.mintemp) < 0.001 &&
					Math.abs(maxtemp - oex.maxtemp) < 0.001)
				return true;
		}
		
		return false;
	}
}
