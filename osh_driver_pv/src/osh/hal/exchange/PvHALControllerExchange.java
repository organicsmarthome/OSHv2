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

import osh.hal.exchange.HALControllerExchange;

/**
 * 
 * @author Ingo Mauser
 *
 */
public class PvHALControllerExchange extends HALControllerExchange {

	private Boolean newPvSwitchedOn;
	private Integer reactivePowerTargetValue;
	
	
	/**
	 * CONSTRUCTOR
	 */
	public PvHALControllerExchange(
			UUID deviceID, 
			Long timestamp, 
			Boolean newPvSwitchedOn, 
			Integer reactivePowerTargetValue) {
		super(deviceID, timestamp);
		
		this.newPvSwitchedOn = newPvSwitchedOn;
		this.reactivePowerTargetValue = reactivePowerTargetValue;
	}

	
	public Boolean getNewPvSwitchedOn() {
		return newPvSwitchedOn;
	}

	public void setNewPvSwitchedOn(Boolean newPvSwitchedOn) {
		this.newPvSwitchedOn = newPvSwitchedOn;
	}

	public Integer getNewReactivePower() {
		return reactivePowerTargetValue;
	}

	public void setNewReactivePower(Integer newReactivePower) {
		this.reactivePowerTargetValue = newReactivePower;
	}

	//TODO: Check why this is used... 
	//  (see also IntelligentApplianceControllerExchange)
	@Override
	public UUID getDeviceID() {
		return null;
	}

	@Override
	public void setDeviceID(UUID deviceID) {
		//DO NOT SAVE UUID
	}

}
