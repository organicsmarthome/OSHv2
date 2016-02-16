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

package osh.datatypes.registry.observer;

import java.util.BitSet;
import java.util.UUID;

import osh.configuration.system.DeviceTypes;
import osh.datatypes.ea.Schedule;

/**
 * 
 * @author Florian Allerding, Kaibin Bao, Ingo Mauser, Till Schuberth
 *
 */
public class StaticEAProblemPartExchange extends ObservableEAProblemPartExchange {

	private Schedule schedule;
	private String description;
	
	
	/**
	 * CONSTRUCTOR
	 * @param sender
	 * @param timestamp
	 * @param schedule
	 * @param description
	 * @param deviceType
	 */
	public StaticEAProblemPartExchange(
			UUID sender, 
			long timestamp,
			boolean toBeScheduled,
			Schedule schedule, 
			String description,
			DeviceTypes deviceType) {
		super(sender, timestamp, toBeScheduled, deviceType);
		
		this.schedule = schedule;
		this.description = description;
	}

	@Override
	public Schedule getSchedule(BitSet solution) {
		return schedule;
	}

	@Override
	public String problemToString() {
		return description;
	}

}
