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

package osh.datatypes.registry.globalcontroller;

import java.util.UUID;

import osh.datatypes.registry.CommandExchange;


public class PvCommandExchange extends CommandExchange {
	
	private Boolean newPvSwitchedOn;
	private Double reactivePowerTargetValue;
	
	public PvCommandExchange(UUID sender, UUID receiver, long time, Boolean newPvSwitchedOn) {
		this(sender, receiver, time, newPvSwitchedOn, null);
	}
	
	public PvCommandExchange(UUID sender, UUID receiver, long time, Double reactivePowerTargetValue) {
		this(sender, receiver, time, null, reactivePowerTargetValue);
	}
	
	public PvCommandExchange(UUID sender, UUID receiver, long timestamp, Boolean newPvSwitchedOn, Double reactivePowerTargetValue) {
		super(sender, receiver, timestamp);
		this.newPvSwitchedOn = newPvSwitchedOn;
		this.reactivePowerTargetValue = reactivePowerTargetValue;
	}

	public Boolean getNewPvSwitchedOn() {
		return newPvSwitchedOn;
	}

	public void setNewPvSwitchedOn(Boolean newPvSwitchedOn) {
		this.newPvSwitchedOn = newPvSwitchedOn;
	}

	public Double getReactivePowerTargetValue() {
		return reactivePowerTargetValue;
	}

	public void setReactivePowerTargetValue(Double reactivePowerTargetValue) {
		this.reactivePowerTargetValue = reactivePowerTargetValue;
	}

}