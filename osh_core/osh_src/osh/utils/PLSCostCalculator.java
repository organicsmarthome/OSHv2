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

package osh.utils;

import java.util.HashMap;

import osh.core.OSHGlobalLogger;
import osh.datatypes.Commodity;
import osh.datatypes.VirtualCommodity;
import osh.datatypes.appliance.LoadProfile;
import osh.datatypes.limit.PowerLimitSignal;
import osh.datatypes.limit.PriceSignal;

/**
 * 
 * @author Ingo Mauser
 *
 */
public class PLSCostCalculator {

	// ### PLS ###
	static public double calcPlsFitnessValue(
			int plsOptimizationObjective,
			double overlimitFactor,
			long currenttime,
			double timeFactor,
			Commodity c,
			LoadProfile pvProfile,
			LoadProfile chpProfile,
			LoadProfile ecarProfile,
			LoadProfile otherProfile,
			HashMap<VirtualCommodity,PriceSignal> priceSignals,
			HashMap<VirtualCommodity,PowerLimitSignal> powerLimitSignals,
			OSHGlobalLogger globalLogger) {
		
		double fitnessValue = 0;
		
		/*
		 * 0: NO additional costs
		 */
		if (plsOptimizationObjective == 0) {
			// no additional costs
			return 0;
		}
		/*
		 * 1: additional costs (overLimitFactor * ACTIVEPOWEREXTERNAL-price) for ACTIVEPOWEREXTERNAL limit violations<br>
		 */
		else if (plsOptimizationObjective == 1) {
			// Calc costs for active power violations
			if (c.equals(Commodity.ACTIVEPOWER)) {
				try {
					double currentActivePower = 0;
					
					if (pvProfile != null) {
						double currentActivePowerPv = pvProfile.getLoadAt(c, currenttime);
						currentActivePower += currentActivePowerPv;
					}
					
					if (chpProfile != null) {
						double currentActivePowerChp = chpProfile.getLoadAt(c, currenttime);
						currentActivePower += currentActivePowerChp;
					}
					
					if (ecarProfile != null) {
						double currentActivePowerEcar = ecarProfile.getLoadAt(c, currenttime);
						currentActivePower += currentActivePowerEcar;
					}
					
					if (otherProfile != null) {
						double currentActivePowerOther = otherProfile.getLoadAt(c, currenttime);
						currentActivePower += currentActivePowerOther;
					}
					
					double currentLimit = powerLimitSignals.get(VirtualCommodity.ACTIVEPOWEREXTERNAL).getPowerUpperLimit(currenttime);
					
					if (currentActivePower > currentLimit) {
						fitnessValue += overlimitFactor * timeFactor * (currentActivePower - currentLimit) * priceSignals.get(VirtualCommodity.ACTIVEPOWEREXTERNAL).getPrice(currenttime);
						//DEBUG
//							System.out.println("ADDITIONAL ");
					}
					
					
				}
				catch (Exception e) {
					globalLogger.logError(e);
					e.printStackTrace();
					return 0;
				}
			}
			
		}
		else {
			globalLogger.logError("plsOptimizationObjective NOT implemented!");
		}
		
		return fitnessValue;
	}
	
	
	static public double calcPlsFitnessValue(
			double overlimitFactor,
			long currenttime,
			double timeFactor,
			double currentActivePower,
			HashMap<VirtualCommodity,PriceSignal> priceSignals,
			HashMap<VirtualCommodity,PowerLimitSignal> powerLimitSignals
			) {
		double fitnessValue = 0;
		
		double currentLimit = powerLimitSignals.get(VirtualCommodity.ACTIVEPOWEREXTERNAL).getPowerUpperLimit(currenttime);
		
		if (currentActivePower > currentLimit) {
			fitnessValue += overlimitFactor * timeFactor * (currentActivePower - currentLimit) * priceSignals.get(VirtualCommodity.ACTIVEPOWEREXTERNAL).getPrice(currenttime);
		}
		
		return fitnessValue;
	}

}
