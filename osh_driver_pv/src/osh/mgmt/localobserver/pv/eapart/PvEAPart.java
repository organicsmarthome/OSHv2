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

package osh.mgmt.localobserver.pv.eapart;

import java.util.BitSet;
import java.util.UUID;

import osh.configuration.system.DeviceTypes;
import osh.datatypes.appliance.LoadProfile;
import osh.datatypes.ea.Schedule;
import osh.datatypes.registry.observer.ObservableEAProblemPartExchange;

/**
 * 
 * @author Ingo Mauser, Till Schuberth
 *
 */
@SuppressWarnings("rawtypes")
public class PvEAPart extends ObservableEAProblemPartExchange {

	private LoadProfile predictedPVProfile;

	
	/**
	 * CONSTRUCTOR
	 */
	public PvEAPart(UUID deviceId, long timestamp, LoadProfile predictedPVProfile) {
		super(deviceId, timestamp, true, DeviceTypes.PVSYSTEM);
		
		this.predictedPVProfile = predictedPVProfile;
	}

	
	@Override
	public Schedule getSchedule(BitSet solution) {
		return new Schedule(predictedPVProfile, 0);
	}

	@Override
	public String problemToString() {
		return "pvproblem: no impl of tostring";
	}

}
