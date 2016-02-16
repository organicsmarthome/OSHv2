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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import osh.datatypes.Commodity;
import osh.datatypes.appliance.PowerProfileTick;
import osh.datatypes.hal.interfaces.IHALProgramRemainingTime;
import osh.datatypes.hal.interfaces.appliance.IHALMieleApplianceProgramDetails;


/**
 * 
 * @author Florian Allerding, Kaibin Bao, Ingo Mauser, Till Schuberth
 *
 */
public class MieleApplianceObserverExchange 
				extends GenericApplianceObserverExchange
				implements IHALProgramRemainingTime, IHALMieleApplianceProgramDetails {
	
	private int programRemainingTime;
	private long deviceStartTime;
	private HashMap<Commodity, ArrayList<PowerProfileTick>> expectedLoadProfiles;
	private String programName;
	private String phaseName;
	
	
	/**
	 * CONSTRUCTOR
	 * @param deviceID
	 * @param timestamp
	 */
	public MieleApplianceObserverExchange(UUID deviceID, Long timestamp) {
		super(deviceID, timestamp);
	}

	@Override
	public int getProgramRemainingTime() {
		return programRemainingTime;
	}

	public void setProgramRemainingTime(int remainingTime) {
		this.programRemainingTime = remainingTime;
	}	
	
	public long getDeviceStartTime() {
		return deviceStartTime;
	}
	
	public void setDeviceStartTime(long deviceStartTime) {
		this.deviceStartTime = deviceStartTime;
	}
	
	public void setExpectedLoadProfiles(HashMap<Commodity, ArrayList<PowerProfileTick>> expectedLoadProfiles) {
		this.expectedLoadProfiles = expectedLoadProfiles;
	}
	
	public HashMap<Commodity, ArrayList<PowerProfileTick>> getExpectedLoadProfiles() {
		//TODO clone
		return expectedLoadProfiles;
	}

	@Override
	public String getProgramName() {
		return programName;
	}
	
	public void setProgramName(String programName) {
		this.programName = programName;
	}

	@Override
	public String getPhaseName() {
		return phaseName;
	}

	public void setPhaseName(String phaseName) {
		this.phaseName = phaseName;
	}

}
