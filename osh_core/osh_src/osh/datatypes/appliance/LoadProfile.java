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

package osh.datatypes.appliance;

import osh.datatypes.Commodity;

/**
 * 
 * @author Florian Allerding, Kaibin Bao, Till Schuberth, Ingo Mauser
 *
 */
public interface LoadProfile {
	
	/**
	 * Returns the EndingTimeOfProfile. EndingTimeOfProfile is defined as the point in time where
	 * the profile stops having a value other than 0, NOT the length of the profile.
	 * E. g.: if the profile starts at 5 with a power of 1 and has a duration of 10, the
	 * power value at 10 will be 0. If the profile starts at -10 with a value of 1 and the
	 * duration is 10, the power at 9 will be 1, and the energy will be 20*10 units*s. 
	 * @return duration
	 */
	public long getEndingTimeOfProfile();
	
	public int getLoadAt(Commodity commodity, long t);
	
	public Long getNextLoadChange(Commodity commodity, long t);
	
	public LoadProfile merge( LoadProfile other, long offset );
	
	/**
	 * cuts off all ticks with a negative time and inserts a tick at time 0
	 * if necessary. This function changes the current object.
	 */
	public void cutOffNegativeTimeValues();
	
	public LoadProfile clone();
	
	public String toStringShort();
}
