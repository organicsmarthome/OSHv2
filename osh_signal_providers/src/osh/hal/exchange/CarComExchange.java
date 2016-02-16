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

import java.util.UUID;


/**
 * Don't use this class
 * 
 * @author Kaibin Bao, Birger Becker
 *
 */
@Deprecated
public class CarComExchange extends HALComExchange {

	private Long depatureTime;
	private Integer minimumRange;
	
	/**
	 * CONSTRUCTOR
	 * @param deviceID
	 * @param timestamp
	 */
	public CarComExchange(UUID deviceID, Long timestamp) {
		super(deviceID, timestamp);
		
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
}
