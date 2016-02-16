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

package osh.mgmt.globalcontroller.JMetal;

import java.util.HashMap;

import osh.configuration.system.DeviceTypes;
import osh.datatypes.VirtualCommodity;
import osh.datatypes.appliance.LoadProfile;
import osh.datatypes.limit.PowerLimitSignal;
import osh.datatypes.limit.PriceSignal;

/**
 * 
 * @author Florian Allerding, Kaibin Bao, Ingo Mauser, Till Schuberth
 *
 */
public interface IFitness {
	
	/**
	 * 
	 * @param beginAt begin at this time
	 * @param plan current aggregated load profile for the next time, beginning at 0
	 * @param priceSignal price signal with unix timestamps
	 * @param powerSignal power limit signal with unix timestamps
	 * @return fitness value
	 */
	public double getFitnessValue (
			long beginAt, 
			long endAt,
			HashMap<DeviceTypes,LoadProfile> plan,
			HashMap<VirtualCommodity,PriceSignal> priceSignals,
			HashMap<VirtualCommodity,PowerLimitSignal> powerLimitSignals
			);

}
