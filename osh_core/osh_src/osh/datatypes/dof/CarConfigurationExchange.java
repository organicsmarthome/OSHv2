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

package osh.datatypes.dof;

import java.util.UUID;

import osh.datatypes.registry.StateExchange;


/**
 * 
 * @author Kaibin Bao, Birger Becker
 *
 */
public class CarConfigurationExchange extends StateExchange {

	private Long depatureTime;
	private Integer minimumRange;
	
	/**
	 * CONSTRUCTOR
	 * @param sender
	 * @param timestamp
	 */
	public CarConfigurationExchange(UUID sender, long timestamp) {
		super(sender, timestamp);
	}

	public void setDepatureTime(Long depatureTime) {
		this.depatureTime = depatureTime;
	}
	
	public Long getDepatureTime() {
		return depatureTime;
	}
	
	public void setMinimumRange(Integer minimumRange) {
		this.minimumRange = minimumRange;
	}
	
	public Integer getMinimumRange() {
		return minimumRange;
	}

	@Override
	public CarConfigurationExchange clone() {
		CarConfigurationExchange cloned = new CarConfigurationExchange(this.getSender(), this.getTimestamp());
		
		cloned.depatureTime = depatureTime;
		cloned.minimumRange = minimumRange;
		
		return cloned;
	}
	
	@Override
	public String toString() {
		return "Depature: " + depatureTime.toString() + "\t Range: " + minimumRange.toString();
	}

}
