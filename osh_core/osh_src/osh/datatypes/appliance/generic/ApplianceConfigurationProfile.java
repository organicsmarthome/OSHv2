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

package osh.datatypes.appliance.generic;

import java.util.UUID;

import osh.datatypes.appliance.LoadProfile;

/**
 * 
 * @author Ingo Mauser
 *
 */
public class ApplianceConfigurationProfile {

	/**
	 * ID for every update (with changes)
	 */
	private UUID acpID;
	/**
	 * Dynamic Profiles using relative times (!)<br>
	 * dim 0: profiles<br>
	 * dim 1: segments<br>
	 * dim 2: pause + phase<br>
	 */
	private LoadProfile[][][] dynamicLoadProfiles;
	/**
	 * Profile after appliance finished<br>
	 * dim 0: segments
	 */
	private LoadProfile[] finishedProfile;
	
	public ApplianceConfigurationProfile(
			UUID acpID,
			LoadProfile[][][] dynamicLoadProfiles,
			LoadProfile[] finishedProfile) {
		
		this.acpID = acpID;
		this.dynamicLoadProfiles = dynamicLoadProfiles;
		this.finishedProfile = finishedProfile;
	}
	

	public UUID getAcpID() {
		return acpID;
	}
	
	public LoadProfile[][][] getDynamicLoadProfiles() {
		return dynamicLoadProfiles;
	}
	
	public LoadProfile[] getFinishedProfile() {
		return finishedProfile;
	}
	
	
	// HELPER METHODS
	
	public static long getMaxDuration(ApplianceConfigurationProfile acp) {
		long val = 0;
		for (int d0 = 0; d0 < acp.dynamicLoadProfiles.length; d0++) {
			long temp = 0;
			for (int d1 = 0; d1 < acp.dynamicLoadProfiles[d0].length; d1++) {
				temp = temp + acp.dynamicLoadProfiles[d0][d1][1].getEndingTimeOfProfile();
			}
			val = Math.max(val, temp);
		}
		return val;
	}
	
}
