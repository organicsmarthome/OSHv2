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

package osh.datatypes.registry.driver.details.energy;

import java.util.UUID;



import osh.datatypes.registry.StateExchange;


/**
 * 
 * @author Ingo Mauser
 *
 */
public class ElectricCurrentDriverDetails extends StateExchange {


	protected UUID meterUuid;


	protected double current;
	
	/**
	 * CONSTRUCTOR
	 * @param sender
	 * @param timestamp
	 */
	public ElectricCurrentDriverDetails(UUID sender, long timestamp) {
		super(sender, timestamp);
	}


	public UUID getMeterUuid() {
		return meterUuid;
	}

	public void setMeterUuid(UUID meterUuid) {
		this.meterUuid = meterUuid;
	}


	public double getCurrent() {
		return current;
	}

	public void setCurrent(double current) {
		this.current = current;
	}
	
	@Override
	public String toString() {
		return "I=" + Double.toString(current) + " (" + meterUuid + ")";
	}
}
