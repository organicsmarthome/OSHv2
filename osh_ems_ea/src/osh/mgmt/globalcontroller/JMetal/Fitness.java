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
import java.util.Map.Entry;

import osh.configuration.system.DeviceTypes;
import osh.core.OSHGlobalLogger;
import osh.datatypes.Commodity;
import osh.datatypes.VirtualCommodity;
import osh.datatypes.appliance.LoadProfile;
import osh.datatypes.appliance.SparseLoadProfile;
import osh.datatypes.limit.PowerLimitSignal;
import osh.datatypes.limit.PriceSignal;
import osh.utils.EPSCostCalculator;
import osh.utils.PLSCostCalculator;

/**
 * 
 * @author Florian Allerding, Kaibin Bao, Ingo Mauser, Till Schuberth
 *
 */
public class Fitness implements IFitness {
	
	private OSHGlobalLogger globalLogger;

	private int epsOptimizationObjective;
	
	private int plsOptimizationObjective;
	private double overlimitFactor;
	
	
	/**
	 * CONSTRUCTOR
	 * @param epsOptimizationObjective
	 * @param plsOptimizationObjective
	 * @param overlimitfactor
	 */
	public Fitness(
			OSHGlobalLogger globalLogger,
			int epsOptimizationObjective, 
			int plsOptimizationObjective, 
			double overlimitfactor) {
		
		this.globalLogger = globalLogger;
		
		// Energy Price Signals
		this.epsOptimizationObjective = epsOptimizationObjective;
		
		// Power Limit Signal
		this.plsOptimizationObjective = plsOptimizationObjective;
		this.overlimitFactor = overlimitfactor;
	}
	
	/**
	 * @param beginAt begin at this time
	 */
	@Override
	public double getFitnessValue(
			long beginAt,
			long endAt,
			HashMap<DeviceTypes,LoadProfile> profilesMap,
			HashMap<VirtualCommodity,PriceSignal> priceSignals,
			HashMap<VirtualCommodity,PowerLimitSignal> powerLimitSignals
			) {
		
		HashMap<DeviceTypes,LoadProfile> dlhLoadChangeMap = new HashMap<>();
		
		// get PV loadprofile
		LoadProfile pvProfile = profilesMap.get(DeviceTypes.PVSYSTEM);
		if (pvProfile != null) {
			dlhLoadChangeMap.put(DeviceTypes.PVSYSTEM, pvProfile);
			profilesMap.remove(DeviceTypes.PVSYSTEM);
		}
		
		// get CHP loadprofile
		LoadProfile chpProfile = profilesMap.get(DeviceTypes.CHPPLANT);
		if (chpProfile != null) {
			dlhLoadChangeMap.put(DeviceTypes.CHPPLANT, chpProfile);
			profilesMap.remove(DeviceTypes.CHPPLANT);
		}
		
		// get E-Car
		LoadProfile ecarProfile = profilesMap.get(DeviceTypes.ECAR);
		if (ecarProfile != null) {
			dlhLoadChangeMap.put(DeviceTypes.ECAR, ecarProfile);
			profilesMap.remove(DeviceTypes.ECAR);
		}
		
		// merge other loadprofiles
		LoadProfile otherProfile = new SparseLoadProfile();
		for (DeviceTypes d : DeviceTypes.values()) {
			LoadProfile tempProfile = profilesMap.get(d);
			if (tempProfile != null && tempProfile.getEndingTimeOfProfile() > 0) {
				otherProfile = otherProfile.merge(tempProfile, 0);
				profilesMap.remove(d);
			}
		}
		dlhLoadChangeMap.put(DeviceTypes.OTHER, otherProfile);
		
		double fitnessValue = 0;
		
		for (Commodity c : Commodity.values()) {
			long nextLoadChange;
			long currenttime = beginAt;
			long priceNextChange;
			
			do { //iterate over load changes
				
				// GET NEXT OTHER LOAD CHANGE
				nextLoadChange = Long.MAX_VALUE;
				
				for (Entry<DeviceTypes,LoadProfile> e : dlhLoadChangeMap.entrySet()) {
					LoadProfile eProfile = e.getValue();
					if (eProfile != null) {
						Long tempNextLoadChange = eProfile.getNextLoadChange(c, currenttime);
						if (tempNextLoadChange != null) {
							nextLoadChange = Math.min(nextLoadChange, tempNextLoadChange);
						}
					}
					else {
						// ERROR (TODO throw exception)
					}
				}
				
				// GET NEXT PRICE CHANGE
				priceNextChange = Long.MAX_VALUE;
				VirtualCommodity[] relevantVirtualCommodities = {};
				
				// For every Commodity there is a different set of VirtualCommodities relevant
				if (c.equals(Commodity.ACTIVEPOWER)) {
					VirtualCommodity[] tempRelevantVirtualCommodities = { 
							VirtualCommodity.ACTIVEPOWEREXTERNAL,
							VirtualCommodity.PVACTIVEPOWERAUTOCONSUMPTION,
							VirtualCommodity.PVACTIVEPOWERFEEDIN
							};
					
					relevantVirtualCommodities = tempRelevantVirtualCommodities;
				}
				else if (c.equals(Commodity.REACTIVEPOWER)) {
					VirtualCommodity[] tempRelevantVirtualCommodities = { 
							VirtualCommodity.REACTIVEPOWEREXTERNAL
							};
					
					relevantVirtualCommodities = tempRelevantVirtualCommodities;
				}
				else if (c.equals(Commodity.NATURALGASPOWER)) {
					VirtualCommodity[] tempRelevantVirtualCommodities = { 
							VirtualCommodity.NATURALGASPOWEREXTERNAL
							};
					
					relevantVirtualCommodities = tempRelevantVirtualCommodities;
				}
				
				// check the current set of VirtualCommodities
				for (VirtualCommodity vc : relevantVirtualCommodities) {
					PriceSignal tempPriceSignal = priceSignals.get(vc);
					if (tempPriceSignal != null) {
						if (tempPriceSignal.getNextPriceChange(currenttime) != null) {
							priceNextChange = Math.min(priceNextChange, tempPriceSignal.getNextPriceChange(currenttime));
						}
					}
				}
				
				// get change of Load and (relevant) Price
				long minNextChange = Math.min(nextLoadChange, priceNextChange);
				
				if ( minNextChange == Long.MAX_VALUE ) { 
					break;
				}
				else if (minNextChange >= endAt) {
					minNextChange = endAt;
				}
				
				// get time factor (constant time in [s] and price is [cents/kWh], whereas load/power is in [W])
				double timeFactor = (minNextChange - currenttime) / (3600000.0);
				
				// EPS
				double fitnessNew = EPSCostCalculator.calcEpsFitnessValue2(
						epsOptimizationObjective, 
						currenttime, 
						timeFactor, 
						c, 
						pvProfile, 
						chpProfile, 
						ecarProfile, 
						otherProfile, 
						priceSignals, 
						globalLogger);
				
				fitnessValue = fitnessValue + fitnessNew;
				
				// PLS
				double additionalPLS = PLSCostCalculator.calcPlsFitnessValue(
						plsOptimizationObjective,
						overlimitFactor,
						currenttime,
						timeFactor,
						c,
						pvProfile,
						chpProfile,
						ecarProfile,
						otherProfile,
						priceSignals,
						powerLimitSignals,
						globalLogger);
				
				fitnessValue = fitnessValue + additionalPLS;
				
				currenttime = minNextChange;
				if (minNextChange >= endAt) {
					break;
				}
				
			} while (true); //it's not what it seems to be...
		}
		
		return fitnessValue;
	}

}
