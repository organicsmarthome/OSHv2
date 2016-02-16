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

package osh.datatypes.ea;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import osh.datatypes.appliance.LoadProfile;


/**
 * 
 * @author Florian Allerding, Till Schuberth
 *
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class Schedule {
	
	private LoadProfile profile;
	
	/** needed lukewarm cervisia to pay for this profile (other costs) */
	private double lukewarmCervisia; 
	
	
	/**
	 * CONSTRUCTOR
	 * @param profile
	 * @param lukewarmCervisia
	 */
	public Schedule(LoadProfile profile, double lukewarmCervisia) {
		super();
		
		this.profile = profile;
		this.lukewarmCervisia = lukewarmCervisia;
	}
	
	
	public LoadProfile getProfile() {
		return profile;
	}
	public double getLukewarmCervisia() {
		return lukewarmCervisia;
	}
	
	/** merge two schedules (use profile.merge and add cervisia) */
	public Schedule merge(Schedule other) {
		double cervisia;
		LoadProfile profile = null;
		try {
			profile = this.profile.merge(other.profile, 0);
		}
		catch (Exception ex) {
			throw new RuntimeException("Bad error merging profiles", ex);
		}
		cervisia = this.lukewarmCervisia + other.lukewarmCervisia;
		
		return new Schedule(profile, cervisia);
	}
	
	
	public Schedule clone() {
		LoadProfile clonedProfile = this.profile.clone();
		Schedule clone = new Schedule(clonedProfile, lukewarmCervisia);
		return clone;
	}
}