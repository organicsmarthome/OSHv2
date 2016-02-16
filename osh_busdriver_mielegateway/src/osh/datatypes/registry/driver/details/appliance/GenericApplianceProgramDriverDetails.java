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

package osh.datatypes.registry.driver.details.appliance;

import java.util.UUID;





import osh.datatypes.registry.StateExchange;
import edu.kit.aifb.osh.XsdCtLoadProfiles;


/**
 * Program driver details
 * (communication of program details)
 * @author Ingo Mauser
 *
 */
public class GenericApplianceProgramDriverDetails extends StateExchange {
	
	protected String programName;
	protected String phaseName;
	protected long startTime;
	protected long endTime;
	protected int remainingTime;

	protected XsdCtLoadProfiles loadProfiles = null;
	
	
	/** for JAXB */
	@SuppressWarnings("unused")
	private GenericApplianceProgramDriverDetails() {
		this(null, 0);
	};

	/**
	 * CONSTRUCTOR
	 * @param sender
	 * @param timestamp
	 */
	public GenericApplianceProgramDriverDetails(
			UUID sender, 
			long timestamp) {
		super(sender, timestamp);
	}
	
	
	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getPhaseName() {
		return phaseName;
	}

	public void setPhaseName(String phaseName) {
		this.phaseName = phaseName;
	}
	
	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	
	public int getRemainingTime() {
		return remainingTime;
	}

	public void setRemainingTime(int remainingTime) {
		this.remainingTime = remainingTime;
	}

	public XsdCtLoadProfiles getLoadProfiles() {
		return loadProfiles;
	}
	
	public void setLoadProfiles(XsdCtLoadProfiles originalLoadProfiles) {
		if (originalLoadProfiles == null) {
			this.loadProfiles = null;
		} else {
			this.loadProfiles = originalLoadProfiles.clone();
		}
	}
	
	//TODO equals
//	@Override
//	public boolean equals(Object obj) {
//
//		if(this.programName == null) {
//			if(other.programName != null)
//				return false;
//		} else if(!this.programName.equals(other.programName)) {
//				return false;
//		}
//		
//		if(this.phaseName == null) {
//			if(other.phaseName != null)
//				return false;
//		} else if(!this.phaseName.equals(other.phaseName)) {
//			return false;
//		}
//		
//		return super.equals(obj);
//	}
	
	// TODO cloning
}
