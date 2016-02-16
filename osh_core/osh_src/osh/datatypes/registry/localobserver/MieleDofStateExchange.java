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

import javax.xml.bind.annotation.XmlElement;

import osh.datatypes.registry.StateExchange;


/**
 * 
 * @author Florian Allerding, Kaibin Bao, Ingo Mauser, Till Schuberth
 *
 */
public class MieleDofStateExchange extends StateExchange {

	/** the duration, NOT an absolute point in time! */
	private long lastDof;
	private long earliestStartTime;
	private long latestStartTime;
	private long expectedStartTime;
	
	
	/**
	 * CONSTRUCTOR
	 * @param sender
	 * @param timestamp
	 * @param lastDof
	 * @param earliestStartTime
	 * @param latestStartTime
	 * @param expectedStartTime
	 */
	public MieleDofStateExchange(
			UUID sender, 
			long timestamp, 
			long lastDof, 
			long earliestStartTime, 
			long latestStartTime, 
			long expectedStartTime) {
		super(sender, timestamp);
		
		this.lastDof = lastDof;
		this.earliestStartTime = earliestStartTime;
		this.latestStartTime = latestStartTime;
		this.expectedStartTime = expectedStartTime;
	}

	/**
	 * returns the last set degree of freedom in seconds as duration
	 */
	@XmlElement
	public long getLastDof() {
		return lastDof;
	}
	@XmlElement
	public long getEarliestStartTime() {
		return earliestStartTime;
	}
	@XmlElement
	public long getLatestStartTime() {
		return latestStartTime;
	}
	@XmlElement
	public long getExpectedStartTime() {
		return expectedStartTime;
	}

	
}
