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

package osh.datatypes.registry.driver.details.appliance.miele;

import java.util.UUID;



import osh.datatypes.registry.StateExchange;


/**
 * Communication program duration, starting time, and remaining time
 * @author Kaibin Bao
 *
 */
public class MieleApplianceDriverDetails extends StateExchange {

	protected long expectedProgramDuration;
	protected long startTime;
	protected long programRemainingTime;

	
	/**
	 * CONSTRUCTOR
	 */
	public MieleApplianceDriverDetails(UUID sender, long timestamp) {
		super(sender, timestamp);
	}

	/**
	 * gets the program duration in seconds 
	 * 
	 * @param expectedProgramDuration
	 */
	public long getExpectedProgramDuration() {
		return expectedProgramDuration;
	}

	/**
	 * sets the program duration in seconds 
	 * 
	 * @param expectedProgramDuration
	 */
	public void setExpectedProgramDuration(long expectedProgramDuration) {
		this.expectedProgramDuration = expectedProgramDuration;
	}

	/**
	 * gets the start time from the timer set by the user
	 * 
	 * @return
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * setter for startTime
	 * 
	 * @param startTime
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	
	/**
	 * gets the remaining program time in seconds 
	 * 
	 * @param expectedProgramDuration
	 */
	public long getProgramRemainingTime() {
		return programRemainingTime;
	}

	/**
	 * sets the remaining program time in seconds 
	 * 
	 * @param expectedProgramDuration
	 */
	public void setProgramRemainingTime(long programTimeLeft) {
		this.programRemainingTime = programTimeLeft;
	}
	
}
