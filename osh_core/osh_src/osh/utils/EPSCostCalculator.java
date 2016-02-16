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
import osh.datatypes.limit.PriceSignal;

/**
 * 
 * @author Ingo Mauser
 *
 */
public class EPSCostCalculator {
	
	static public double calcEpsFitnessValue2(
			int epsOptimizationObjective,
			long currenttime,
			double timeFactor,
			Commodity c,
			LoadProfile pvProfile,
			LoadProfile chpProfile,
			LoadProfile ecarProfile,
			LoadProfile otherProfile,
			HashMap<VirtualCommodity,PriceSignal> priceSignals,
			OSHGlobalLogger globalLogger) {
		
		double fitnessValue = 0;
		
		// Calc costs for active power
		if (c.equals(Commodity.ACTIVEPOWER)) {
			try {
				double currentActivePowerExternal = 0;
				double currentActivePowerPv = 0;
				double currentActivePowerChp = 0;
				double currentActivePowerEcar = 0;
				double currentActivePowerOther = 0;
				
				if (pvProfile != null) {
					currentActivePowerPv = pvProfile.getLoadAt(c, currenttime);
					currentActivePowerExternal += currentActivePowerPv;
				}
				
				if (chpProfile != null) {
					currentActivePowerChp = chpProfile.getLoadAt(c, currenttime);
					currentActivePowerExternal += currentActivePowerChp;
				}
				
				if (ecarProfile != null) {
					currentActivePowerEcar = ecarProfile.getLoadAt(c, currenttime);
					currentActivePowerExternal += currentActivePowerEcar;
				}
				
				if (otherProfile != null) {
					currentActivePowerOther = otherProfile.getLoadAt(c, currenttime);
					currentActivePowerExternal += currentActivePowerOther;
				}
				
				/*
				 * 0: "ACTIVEPOWEREXTERNAL 
				 * 		+ NATURALGASPOWEREXTERNAL" : <br>
				 * > sum of all activePowers * ACTIVEPOWEREXTERNAL-Price<br>
				 * > gasPower * NATURALGASPOWEREXTERNAL-Price<br>
				 */
				if (epsOptimizationObjective == 0) {
					fitnessValue = fitnessValue + calcEpsOptimizationObjective0(
							currenttime, 
							timeFactor, 
							priceSignals, 
							currentActivePowerExternal);
				}
				
				/* #############################
				 * 1: "ACTIVEPOWEREXTERNAL 
				 * 		+ PVACTIVEPOWERFEEDIN
				 * 		+ NATURALGASPOWEREXTERNAL" : <br>
				 * > if (sum of all activePowers > 0) -> (sum of all activePowers) * ACTIVEPOWEREXTERNAL-Price<br>
				 * > if (sum of all activePowers < 0) -> Math.max(pvPower,(sum of all activePowers)) * PVACTIVEPOWERFEEDIN<br>
				 * > gasPower * NATURALGASPOWEREXTERNAL-Price<br>
				 * #############################*/
				else if (epsOptimizationObjective == 1) {
					fitnessValue = fitnessValue + calcEpsOptimizationObjective1(
							currenttime, 
							timeFactor, 
							priceSignals, 
							currentActivePowerExternal,
							currentActivePowerPv);
				}
				
				/* #################################
				 * 2: "ACTIVEPOWEREXTERNAL 
				 * 		+ PVACTIVEPOWERFEEDIN + PVACTIVEPOWERAUTOCONSUMPTION
				 * 		+ NATURALGASPOWEREXTERNAL"<br>
				 * > if (sum of all activePowers > 0) -> (sum of all activePowers) * ACTIVEPOWEREXTERNAL-Price<br>
				 * > pvPowerToGrid * PVACTIVEPOWERFEEDIN<br>
				 * > pvPowerAutoConsumption * PVACTIVEPOWERAUTOCONSUMPTION<br>
				 * > gasPower * NATURALGASPOWEREXTERNAL-Price<br>
				 * ##################################*/
				else if (epsOptimizationObjective == 2) {
					fitnessValue = fitnessValue + calcEpsOptimizationObjective2(
							currenttime, 
							timeFactor, 
							priceSignals, 
							currentActivePowerExternal,
							currentActivePowerPv);
				}
				/* ################################
				 * 3: "ACTIVEPOWEREXTERNAL 
				 * 		+ PVACTIVEPOWERFEEDIN 
				 * 		+ CHPACTIVEPOWERFEEDIN
				 * 		+ NATURALGASPOWEREXTERNAL" : <br>
				 * > if (sum of all activePowers > 0) -> (sum of all activePowers) * ACTIVEPOWEREXTERNAL-Price<br>
				 * > pvPowerToGrid * PVACTIVEPOWERFEEDIN<br>
				 * > chpPowerToGrid * CHPACTIVEPOWERFEEDIN<br>
				 * > gasPower * NATURALGASPOWEREXTERNAL-Price<br>
				 * > IMPORTANT: PV and CHP to grid depending on their power proportionally!<br>
				 * ##################################
				 */
				else if (epsOptimizationObjective == 3) {
					fitnessValue = fitnessValue + calcEpsOptimizationObjective3(
							currenttime, 
							timeFactor, 
							priceSignals, 
							currentActivePowerExternal,
							currentActivePowerPv,
							currentActivePowerChp);
				}
				/* #################################
				 * 4: "ACTIVEPOWEREXTERNAL 
				 * 		+ PVACTIVEPOWERFEEDIN + PVACTIVEPOWERAUTOCONSUMPTION
				 * 		+ CHPACTIVEPOWERFEEDIN + CHPACTIVEPOWERAUTOCONSUMPTION
				 * 		+ NATURALGASPOWEREXTERNAL"<br>
				 * > if (sum of all activePowers > 0) -> (sum of all activePowers) * ACTIVEPOWEREXTERNAL-Price<br>
				 * > pvPowerToGrid * PVACTIVEPOWERFEEDIN<br>
				 * > pvPowerAutoConsumption * PVACTIVEPOWERAUTOCONSUMPTION<br>
				 * > chpPowerToGrid * CHPACTIVEPOWERFEEDIN<br>
				 * > chpPowerAutoConsumption * CHPACTIVEPOWERAUTOCONSUMPTION<br>
				 * > gasPower * NATURALGASPOWEREXTERNAL-Price<br>
				 * > IMPORTANT: PV and CHP to grid depending on their power proportionally!<br>
				 * ################################## */
				else if (epsOptimizationObjective == 4) {
					fitnessValue = fitnessValue + calcEpsOptimizationObjective4(
							currenttime, 
							timeFactor, 
							priceSignals, 
							currentActivePowerExternal,
							currentActivePowerPv,
							currentActivePowerChp);
				}
				
				
			}
			catch (Exception e) {
				globalLogger.logError(e);
				e.printStackTrace();
				return 0;
			}
			
		}
		else if (c.equals(Commodity.REACTIVEPOWER)) {
			// NO reactivePower pricing...
		}
		else if (c.equals(Commodity.NATURALGASPOWER)) {
			if (epsOptimizationObjective == 0
					|| epsOptimizationObjective == 1
					|| epsOptimizationObjective == 2
					|| epsOptimizationObjective == 3
					|| epsOptimizationObjective == 4) {
				fitnessValue += EPSCostCalculator.calcNaturalGasPowerCosts(currenttime, chpProfile, otherProfile, priceSignals, timeFactor, globalLogger);
			}
			else {
				globalLogger.logError("epsOptimizationObjective not implemented");
			}
		}
		
		return fitnessValue;
	}
	
	static public double calcEpsOptimizationObjective0(
			long currenttime,
			double timeFactor,
			HashMap<VirtualCommodity,PriceSignal> priceSignals,
			double currentActivePowerExternal) {
		double fitnessValue = 0;
		
		if (currentActivePowerExternal > 0) {
			fitnessValue = timeFactor * (currentActivePowerExternal * priceSignals.get(VirtualCommodity.ACTIVEPOWEREXTERNAL).getPrice(currenttime));
		}
		
		return fitnessValue;
	}
	
	static public double calcEpsOptimizationObjective1(
			long currenttime,
			double timeFactor,
			HashMap<VirtualCommodity,PriceSignal> priceSignals,
			double currentActivePowerExternal,
			double currentActivePowerPv) {
		double fitnessValue = 0;
		
		if (currentActivePowerExternal > 0) {
			fitnessValue += timeFactor * currentActivePowerExternal * priceSignals.get(VirtualCommodity.ACTIVEPOWEREXTERNAL).getPrice(currenttime);
		}
		else if (currentActivePowerExternal == 0) {
			// NOTHING to add
		}
		else {
			// refund is only PV-feedin
			fitnessValue += timeFactor * Math.max(currentActivePowerPv, currentActivePowerExternal) * priceSignals.get(VirtualCommodity.PVACTIVEPOWERFEEDIN).getPrice(currenttime);
		}
		
		return fitnessValue;
	}
	
	static public double calcEpsOptimizationObjective2(
			long currenttime,
			double timeFactor,
			HashMap<VirtualCommodity,PriceSignal> priceSignals,
			double currentActivePowerExternal,
			double currentActivePowerPv) {
		double fitnessValue = 0;
		
		// PV available
		if ( currentActivePowerPv != 0 ) {
			
			if (currentActivePowerExternal >= 0) {
				// determine costs of external supply
				fitnessValue += timeFactor * currentActivePowerExternal * priceSignals.get(VirtualCommodity.ACTIVEPOWEREXTERNAL).getPrice(currenttime);
				// refund: PV-autoconsumption
				fitnessValue += timeFactor * currentActivePowerPv * priceSignals.get(VirtualCommodity.PVACTIVEPOWERAUTOCONSUMPTION).getPrice(currenttime);
			}
			else {
				// no costs for external supply
				// refund: PV-feedin
				double pvFeedin = Math.min(0, Math.max(currentActivePowerPv, currentActivePowerExternal));
				fitnessValue += timeFactor * pvFeedin * priceSignals.get(VirtualCommodity.PVACTIVEPOWERFEEDIN).getPrice(currenttime);
				// refund: PV-auto-consumption
				double pvAutoConsumption = Math.max(Math.min(0, currentActivePowerPv - currentActivePowerExternal), currentActivePowerPv);
				fitnessValue += timeFactor * pvAutoConsumption * priceSignals.get(VirtualCommodity.PVACTIVEPOWERAUTOCONSUMPTION).getPrice(currenttime);
			}
			
		}
		// no PV available
		else {
			if (currentActivePowerExternal > 0) {
				fitnessValue += timeFactor * currentActivePowerExternal * priceSignals.get(VirtualCommodity.ACTIVEPOWEREXTERNAL).getPrice(currenttime);
			}
			else {
				// no refund (e.g. CHP)
			}
		}
		
		return fitnessValue;
	}
	
	static public double calcEpsOptimizationObjective3(
			long currenttime,
			double timeFactor,
			HashMap<VirtualCommodity,PriceSignal> priceSignals,
			double currentActivePowerExternal,
			double currentActivePowerPv,
			double currentActivePowerChp) {
		double fitnessValue = 0;
		
		if (currentActivePowerExternal > 0) {
			// costs for external power
			fitnessValue += timeFactor * currentActivePowerExternal * priceSignals.get(VirtualCommodity.ACTIVEPOWEREXTERNAL).getPrice(currenttime);
		}
		else if (currentActivePowerExternal == 0) {
			// NOTHING to add
		}
		else {
			double pvAndChpFeedin = Math.min(0, Math.max(currentActivePowerExternal, currentActivePowerChp + currentActivePowerPv));
			if (pvAndChpFeedin < 0) {
				double pvFeedin = currentActivePowerPv / (currentActivePowerPv + currentActivePowerChp) * pvAndChpFeedin;
				double chpFeedin = currentActivePowerChp / (currentActivePowerPv + currentActivePowerChp) * pvAndChpFeedin;
				// refund: PV-feedin
				fitnessValue += timeFactor * pvFeedin * priceSignals.get(VirtualCommodity.PVACTIVEPOWERFEEDIN).getPrice(currenttime);
				// refund: CHP-feedin
				fitnessValue += timeFactor * chpFeedin * priceSignals.get(VirtualCommodity.CHPACTIVEPOWERFEEDIN).getPrice(currenttime);
			}
		}
		
		return fitnessValue;
	}
	
	static public double calcEpsOptimizationObjective4(
			long currenttime,
			double timeFactor,
			HashMap<VirtualCommodity,PriceSignal> priceSignals,
			double currentActivePowerExternal,
			double currentActivePowerPv,
			double currentActivePowerChp) {
		double fitnessValue = 0;

		if (currentActivePowerExternal >= 0) {
			
			// costs for external power
			fitnessValue += timeFactor * currentActivePowerExternal * priceSignals.get(VirtualCommodity.ACTIVEPOWEREXTERNAL).getPrice(currenttime);
			
			// refund: PV-autoconsumption
			fitnessValue += timeFactor * currentActivePowerPv * priceSignals.get(VirtualCommodity.PVACTIVEPOWERAUTOCONSUMPTION).getPrice(currenttime);
		
			// refund: CHP-autoconsumption
			fitnessValue += timeFactor * currentActivePowerChp * priceSignals.get(VirtualCommodity.CHPACTIVEPOWERAUTOCONSUMPTION).getPrice(currenttime);
			
		}
		else { 
			// ok...we need power from grid
			
			if (currentActivePowerPv + currentActivePowerChp < currentActivePowerExternal) {
				// some power is used, some power goes to grid
				
				double pvAndChpPower = currentActivePowerPv + currentActivePowerChp;
				if (pvAndChpPower != 0) {
					// refund: PV-feedin
					double pvFeedin = currentActivePowerPv / (currentActivePowerPv + currentActivePowerChp) * currentActivePowerExternal;
					fitnessValue += timeFactor * pvFeedin * priceSignals.get(VirtualCommodity.PVACTIVEPOWERFEEDIN).getPrice(currenttime);
					
					// refund: PV-autoconsumption
					double pvAutoconsumption = currentActivePowerPv - pvFeedin;
					fitnessValue += timeFactor * pvAutoconsumption * priceSignals.get(VirtualCommodity.PVACTIVEPOWERAUTOCONSUMPTION).getPrice(currenttime);
					
					// refund: CHP-feedin
					double chpFeedin = currentActivePowerChp / (currentActivePowerPv + currentActivePowerChp) * currentActivePowerExternal;
					fitnessValue += timeFactor * chpFeedin * priceSignals.get(VirtualCommodity.CHPACTIVEPOWERFEEDIN).getPrice(currenttime);
					
					// refund: CHP-autoconsumption
					double chpAutoconsumption = currentActivePowerChp - chpFeedin;
					fitnessValue += timeFactor * chpAutoconsumption * priceSignals.get(VirtualCommodity.CHPACTIVEPOWERAUTOCONSUMPTION).getPrice(currenttime);
					
				}
			}
			
			else { //(currentActivePowerPv + currentActivePowerChp >= currentActivePowerExternal)
				// all power from PV and CHP goes to grid (and there is even more power going to grid)
				// -> there is NO refund because of autoconsumption
				
				// refund: PV-feedin
				fitnessValue += timeFactor * currentActivePowerPv * priceSignals.get(VirtualCommodity.PVACTIVEPOWERFEEDIN).getPrice(currenttime);
			
				// refund: CHP-feedin
				fitnessValue += timeFactor * currentActivePowerChp * priceSignals.get(VirtualCommodity.CHPACTIVEPOWERFEEDIN).getPrice(currenttime);
			}
			
			double pvAndChpFeedin = Math.min(0, Math.max(currentActivePowerExternal, currentActivePowerChp + currentActivePowerPv));
			if (pvAndChpFeedin < 0) {
				double pvFeedin = currentActivePowerPv / (currentActivePowerPv + currentActivePowerChp) * pvAndChpFeedin;
				double chpFeedin = currentActivePowerChp / (currentActivePowerPv + currentActivePowerChp) * pvAndChpFeedin;
				// refund: PV-feedin
				fitnessValue += timeFactor * pvFeedin * priceSignals.get(VirtualCommodity.PVACTIVEPOWERFEEDIN).getPrice(currenttime);
				// refund: CHP-feedin
				fitnessValue += timeFactor * chpFeedin * priceSignals.get(VirtualCommodity.CHPACTIVEPOWERFEEDIN).getPrice(currenttime);
			}
		}
		
		return fitnessValue;
	}
	
	// ### EPS ###
	static public double calcEpsFitnessValue(
			int epsOptimizationObjective,
			long currenttime,
			double timeFactor,
			Commodity c,
			LoadProfile pvProfile,
			LoadProfile chpProfile,
			LoadProfile ecarProfile,
			LoadProfile otherProfile,
			HashMap<VirtualCommodity,PriceSignal> priceSignals,
			OSHGlobalLogger globalLogger) {
		
		double fitnessValue = 0;
		
		/*
		 * 0: "ACTIVEPOWEREXTERNAL 
		 * 		+ NATURALGASPOWEREXTERNAL" : <br>
		 * > sum of all activePowers * ACTIVEPOWEREXTERNAL-Price<br>
		 * > gasPower * NATURALGASPOWEREXTERNAL-Price<br>
		 */
		if (epsOptimizationObjective == 0) {
			
			// Calc costs for active power
			if (c.equals(Commodity.ACTIVEPOWER)) {
				try {
					double currentActivePowerExternal = 0;
					
					if (pvProfile != null) {
						double currentActivePowerPv = pvProfile.getLoadAt(c, currenttime);
						currentActivePowerExternal += currentActivePowerPv;
					}
					
					if (chpProfile != null) {
						double currentActivePowerChp = chpProfile.getLoadAt(c, currenttime);
						currentActivePowerExternal += currentActivePowerChp;
					}
					
					if (ecarProfile != null) {
						double currentActivePowerEcar = ecarProfile.getLoadAt(c, currenttime);
						currentActivePowerExternal += currentActivePowerEcar;
					}
					
					if (otherProfile != null) {
						double currentActivePowerOther = otherProfile.getLoadAt(c, currenttime);
						currentActivePowerExternal += currentActivePowerOther;
					}
					
					if (currentActivePowerExternal > 0) {
						fitnessValue += timeFactor * (currentActivePowerExternal * priceSignals.get(VirtualCommodity.ACTIVEPOWEREXTERNAL).getPrice(currenttime));
					}
					
				}
				catch (Exception e) {
					globalLogger.logError(e);
					e.printStackTrace();
					return 0;
				}
				
			}
			else if (c.equals(Commodity.REACTIVEPOWER)) {
				// NO reactivePower pricing...
			}
			else if (c.equals(Commodity.NATURALGASPOWER)) {
				fitnessValue += EPSCostCalculator.calcNaturalGasPowerCosts(currenttime, chpProfile, otherProfile, priceSignals, timeFactor, globalLogger);
			}
		}
		/* #############################
		 * 1: "ACTIVEPOWEREXTERNAL 
		 * 		+ PVACTIVEPOWERFEEDIN
		 * 		+ NATURALGASPOWEREXTERNAL" : <br>
		 * > if (sum of all activePowers > 0) -> (sum of all activePowers) * ACTIVEPOWEREXTERNAL-Price<br>
		 * > if (sum of all activePowers < 0) -> Math.max(pvPower,(sum of all activePowers)) * PVACTIVEPOWERFEEDIN<br>
		 * > gasPower * NATURALGASPOWEREXTERNAL-Price<br>
		 * #############################*/
		else if (epsOptimizationObjective == 1) {
			// Calc costs for active power
			if (c.equals(Commodity.ACTIVEPOWER)) {
				try {
					double currentActivePowerExternal = 0;
					double currentActivePowerPv = 0;
					
					if (pvProfile != null) {
						currentActivePowerPv = pvProfile.getLoadAt(c, currenttime);
						currentActivePowerExternal += currentActivePowerPv;
					}
					
					if (chpProfile != null) {
						double currentActivePowerChp = chpProfile.getLoadAt(c, currenttime);
						currentActivePowerExternal += currentActivePowerChp;
					}
					
					if (ecarProfile != null) {
						double currentActivePowerEcar = chpProfile.getLoadAt(c, currenttime);
						currentActivePowerExternal += currentActivePowerEcar;
					}
					
					if (otherProfile != null) {
						double currentActivePowerOther = otherProfile.getLoadAt(c, currenttime);
						currentActivePowerExternal += currentActivePowerOther;
					}
					
					if (currentActivePowerExternal > 0) {
						fitnessValue += timeFactor * currentActivePowerExternal * priceSignals.get(VirtualCommodity.ACTIVEPOWEREXTERNAL).getPrice(currenttime);
					}
					else if (currentActivePowerExternal == 0) {
						// NOTHING to add
					}
					else {
						// refund only PV-feedin
						fitnessValue += timeFactor * Math.max(currentActivePowerPv,currentActivePowerExternal) * priceSignals.get(VirtualCommodity.PVACTIVEPOWERFEEDIN).getPrice(currenttime);
					}
					
				}
				catch (Exception e) {
//									if (globalLogger != null)
					globalLogger.logError(e);
					e.printStackTrace();
					return 0;
				}
				
			}
			else if (c.equals(Commodity.REACTIVEPOWER)) {
				// NO reactivePower pricing...
			}
			else if (c.equals(Commodity.NATURALGASPOWER)) {
				fitnessValue += EPSCostCalculator.calcNaturalGasPowerCosts(currenttime, chpProfile, otherProfile, priceSignals, timeFactor, globalLogger);
			}
		}
		/* #################################
		 * 2: "ACTIVEPOWEREXTERNAL 
		 * 		+ PVACTIVEPOWERFEEDIN + PVACTIVEPOWERAUTOCONSUMPTION
		 * 		+ NATURALGASPOWEREXTERNAL"<br>
		 * > if (sum of all activePowers > 0) -> (sum of all activePowers) * ACTIVEPOWEREXTERNAL-Price<br>
		 * > pvPowerToGrid * PVACTIVEPOWERFEEDIN<br>
		 * > pvPowerAutoConsumption * PVACTIVEPOWERAUTOCONSUMPTION<br>
		 * > gasPower * NATURALGASPOWEREXTERNAL-Price<br>
		 * ##################################*/
		else if (epsOptimizationObjective == 2) {
			// Calc costs for active power
			if (c.equals(Commodity.ACTIVEPOWER)) {
				try {
					double currentActivePower = 0;
					double currentActivePowerPv = 0;
					
					if (pvProfile != null) {
						currentActivePowerPv = pvProfile.getLoadAt(c, currenttime);
						currentActivePower += currentActivePowerPv;
					}
					
					if (chpProfile != null) {
						double currentActivePowerChp = chpProfile.getLoadAt(c, currenttime);
						currentActivePower += currentActivePowerChp;
					}
					
					if (ecarProfile != null) {
						double currentActivePowerEcar = chpProfile.getLoadAt(c, currenttime);
						currentActivePower += currentActivePowerEcar;
					}
					
					if (otherProfile != null) {
						double currentActivePowerOther = otherProfile.getLoadAt(c, currenttime);
						currentActivePower += currentActivePowerOther;
					}
					
					if ( pvProfile != null) {
						
						if (currentActivePower >= 0) {
							// determine costs of external supply
							fitnessValue += timeFactor * currentActivePower * priceSignals.get(VirtualCommodity.ACTIVEPOWEREXTERNAL).getPrice(currenttime);
							// refund: PV-autoconsumption
							fitnessValue += timeFactor * currentActivePowerPv * priceSignals.get(VirtualCommodity.PVACTIVEPOWERAUTOCONSUMPTION).getPrice(currenttime);
						}
						else {
							// no costs for external supply
							// refund: PV-feedin
							double pvFeedin = Math.min(0, Math.max(currentActivePowerPv, currentActivePower));
							fitnessValue += timeFactor * pvFeedin * priceSignals.get(VirtualCommodity.PVACTIVEPOWERFEEDIN).getPrice(currenttime);
							// refund: PV-auto-consumption
							double pvAutoConsumption = Math.max(Math.min(0, currentActivePowerPv - currentActivePower), currentActivePowerPv);
							fitnessValue += timeFactor * pvAutoConsumption * priceSignals.get(VirtualCommodity.PVACTIVEPOWERAUTOCONSUMPTION).getPrice(currenttime);
						}
						
					}
					// no PV available
					else {
						if (currentActivePower > 0) {
							fitnessValue += timeFactor * currentActivePower * priceSignals.get(VirtualCommodity.ACTIVEPOWEREXTERNAL).getPrice(currenttime);
						}
						else {
							// no refund (e.g. CHP)
						}
					}
					
				}
				catch (Exception e) {
//						if (globalLogger != null)
					globalLogger.logError(e);
					e.printStackTrace();
					return 0;
				}
				
			}
			else if (c.equals(Commodity.REACTIVEPOWER)) {
				// NO reactivePower pricing...
			}
			else if (c.equals(Commodity.NATURALGASPOWER)) {
				fitnessValue += EPSCostCalculator.calcNaturalGasPowerCosts(currenttime, chpProfile, otherProfile, priceSignals, timeFactor, globalLogger);
			}
		}
		/* ################################
		 * 3: "ACTIVEPOWEREXTERNAL 
		 * 		+ PVACTIVEPOWERFEEDIN 
		 * 		+ CHPACTIVEPOWERFEEDIN
		 * 		+ NATURALGASPOWEREXTERNAL" : <br>
		 * > if (sum of all activePowers > 0) -> (sum of all activePowers) * ACTIVEPOWEREXTERNAL-Price<br>
		 * > pvPowerToGrid * PVACTIVEPOWERFEEDIN<br>
		 * > chpPowerToGrid * CHPACTIVEPOWERFEEDIN<br>
		 * > gasPower * NATURALGASPOWEREXTERNAL-Price<br>
		 * > IMPORTANT: PV and CHP to grid depending on their power proportionally!<br>
		 * ##################################
		 */
		else if (epsOptimizationObjective == 3) {
			// Calc costs for active power
			if (c.equals(Commodity.ACTIVEPOWER)) {
				try {
					double currentActivePowerExternal = 0;
					double currentActivePowerPv = 0;
					double currentActivePowerChp = 0;
					
					if (pvProfile != null) {
						currentActivePowerPv = pvProfile.getLoadAt(c, currenttime);
						currentActivePowerExternal += currentActivePowerPv;
					}
					
					if (chpProfile != null) {
						currentActivePowerChp = chpProfile.getLoadAt(c, currenttime);
						currentActivePowerExternal += currentActivePowerChp;
					}
					
					if (ecarProfile != null) {
						double currentActivePowerEcar = chpProfile.getLoadAt(c, currenttime);
						currentActivePowerExternal += currentActivePowerEcar;
					}
					
					if (otherProfile != null) {
						double currentActivePowerOther = otherProfile.getLoadAt(c, currenttime);
						currentActivePowerExternal += currentActivePowerOther;
					}
					
					if (currentActivePowerExternal > 0) {
						// costs for external power
						fitnessValue += timeFactor * currentActivePowerExternal * priceSignals.get(VirtualCommodity.ACTIVEPOWEREXTERNAL).getPrice(currenttime);
					}
					else if (currentActivePowerExternal == 0) {
						// NOTHING to add
					}
					else {
						double pvAndChpFeedin = Math.min(0, Math.max(currentActivePowerExternal, currentActivePowerChp + currentActivePowerPv));
						if (pvAndChpFeedin < 0) {
							double pvFeedin = currentActivePowerPv / (currentActivePowerPv + currentActivePowerChp) * pvAndChpFeedin;
							double chpFeedin = currentActivePowerChp / (currentActivePowerPv + currentActivePowerChp) * pvAndChpFeedin;
							// refund: PV-feedin
							fitnessValue += timeFactor * pvFeedin * priceSignals.get(VirtualCommodity.PVACTIVEPOWERFEEDIN).getPrice(currenttime);
							// refund: CHP-feedin
							fitnessValue += timeFactor * chpFeedin * priceSignals.get(VirtualCommodity.CHPACTIVEPOWERFEEDIN).getPrice(currenttime);
						}
					}
					
				}
				catch (Exception e) {
//									if (globalLogger != null)
					globalLogger.logError(e);
					e.printStackTrace();
					return 0;
				}
				
			}
			else if (c.equals(Commodity.REACTIVEPOWER)) {
				// NO reactivePower pricing...
			}
			else if (c.equals(Commodity.NATURALGASPOWER)) {
				fitnessValue += EPSCostCalculator.calcNaturalGasPowerCosts(currenttime, chpProfile, otherProfile, priceSignals, timeFactor, globalLogger);
			}
		}
		/* #################################
		 * 4: "ACTIVEPOWEREXTERNAL 
		 * 		+ PVACTIVEPOWERFEEDIN + PVACTIVEPOWERAUTOCONSUMPTION
		 * 		+ CHPACTIVEPOWERFEEDIN + CHPACTIVEPOWERAUTOCONSUMPTION
		 * 		+ NATURALGASPOWEREXTERNAL"<br>
		 * > if (sum of all activePowers > 0) -> (sum of all activePowers) * ACTIVEPOWEREXTERNAL-Price<br>
		 * > pvPowerToGrid * PVACTIVEPOWERFEEDIN<br>
		 * > pvPowerAutoConsumption * PVACTIVEPOWERAUTOCONSUMPTION<br>
		 * > chpPowerToGrid * CHPACTIVEPOWERFEEDIN<br>
		 * > chpPowerAutoConsumption * CHPACTIVEPOWERAUTOCONSUMPTION<br>
		 * > gasPower * NATURALGASPOWEREXTERNAL-Price<br>
		 * > IMPORTANT: PV and CHP to grid depending on their power proportionally!<br>
		 * ################################## */
		else if (epsOptimizationObjective == 4) {
			// Calc costs for active power
			if (c.equals(Commodity.ACTIVEPOWER)) {
				try {
					double currentActivePowerExternal = 0;
					double currentActivePowerPv = 0;
					double currentActivePowerChp = 0;
					
					if (pvProfile != null) {
						currentActivePowerPv = pvProfile.getLoadAt(c, currenttime);
						currentActivePowerExternal += currentActivePowerPv;
					}
					
					if (chpProfile != null) {
						currentActivePowerChp = chpProfile.getLoadAt(c, currenttime);
						currentActivePowerExternal += currentActivePowerChp;
					}
					
					if (ecarProfile != null) {
						double currentActivePowerEcar = chpProfile.getLoadAt(c, currenttime);
						currentActivePowerExternal += currentActivePowerEcar;
					}
					
					if (otherProfile != null) {
						double currentActivePowerOther = otherProfile.getLoadAt(c, currenttime);
						currentActivePowerExternal += currentActivePowerOther;
					}
					
					if (currentActivePowerExternal >= 0) {
						
						// costs for external power
						fitnessValue += timeFactor * currentActivePowerExternal * priceSignals.get(VirtualCommodity.ACTIVEPOWEREXTERNAL).getPrice(currenttime);
						
						// refund: PV-autoconsumption
						fitnessValue += timeFactor * currentActivePowerPv * priceSignals.get(VirtualCommodity.PVACTIVEPOWERAUTOCONSUMPTION).getPrice(currenttime);
					
						// refund: CHP-autoconsumption
						fitnessValue += timeFactor * currentActivePowerChp * priceSignals.get(VirtualCommodity.CHPACTIVEPOWERAUTOCONSUMPTION).getPrice(currenttime);
						
					}
					else { 
						// ok...we need power from grid
						
						if (currentActivePowerPv + currentActivePowerChp < currentActivePowerExternal) {
							// some power is used, some power goes to grid
							
							double pvAndChpPower = currentActivePowerPv + currentActivePowerChp;
							if (pvAndChpPower != 0) {
								// refund: PV-feedin
								double pvFeedin = currentActivePowerPv / (currentActivePowerPv + currentActivePowerChp) * currentActivePowerExternal;
								fitnessValue += timeFactor * pvFeedin * priceSignals.get(VirtualCommodity.PVACTIVEPOWERFEEDIN).getPrice(currenttime);
								
								// refund: PV-autoconsumption
								double pvAutoconsumption = currentActivePowerPv - pvFeedin;
								fitnessValue += timeFactor * pvAutoconsumption * priceSignals.get(VirtualCommodity.PVACTIVEPOWERAUTOCONSUMPTION).getPrice(currenttime);
								
								// refund: CHP-feedin
								double chpFeedin = currentActivePowerChp / (currentActivePowerPv + currentActivePowerChp) * currentActivePowerExternal;
								fitnessValue += timeFactor * chpFeedin * priceSignals.get(VirtualCommodity.CHPACTIVEPOWERFEEDIN).getPrice(currenttime);
								
								// refund: CHP-autoconsumption
								double chpAutoconsumption = currentActivePowerChp - chpFeedin;
								fitnessValue += timeFactor * chpAutoconsumption * priceSignals.get(VirtualCommodity.CHPACTIVEPOWERAUTOCONSUMPTION).getPrice(currenttime);
								
							}
						}
						
						else { //(currentActivePowerPv + currentActivePowerChp >= currentActivePowerExternal)
							// all power from PV and CHP goes to grid (and there is even more power going to grid)
							// -> there is NO refund because of autoconsumption
							
							// refund: PV-feedin
							fitnessValue += timeFactor * currentActivePowerPv * priceSignals.get(VirtualCommodity.PVACTIVEPOWERFEEDIN).getPrice(currenttime);
						
							// refund: CHP-feedin
							fitnessValue += timeFactor * currentActivePowerChp * priceSignals.get(VirtualCommodity.CHPACTIVEPOWERFEEDIN).getPrice(currenttime);
						}
						
						double pvAndChpFeedin = Math.min(0, Math.max(currentActivePowerExternal, currentActivePowerChp + currentActivePowerPv));
						if (pvAndChpFeedin < 0) {
							double pvFeedin = currentActivePowerPv / (currentActivePowerPv + currentActivePowerChp) * pvAndChpFeedin;
							double chpFeedin = currentActivePowerChp / (currentActivePowerPv + currentActivePowerChp) * pvAndChpFeedin;
							// refund: PV-feedin
							fitnessValue += timeFactor * pvFeedin * priceSignals.get(VirtualCommodity.PVACTIVEPOWERFEEDIN).getPrice(currenttime);
							// refund: CHP-feedin
							fitnessValue += timeFactor * chpFeedin * priceSignals.get(VirtualCommodity.CHPACTIVEPOWERFEEDIN).getPrice(currenttime);
						}
					}
					
				}
				catch (Exception e) {
//									if (globalLogger != null)
					globalLogger.logError(e);
					e.printStackTrace();
					return 0;
				}
				
			}
			else if (c.equals(Commodity.REACTIVEPOWER)) {
				// NO reactivePower pricing...
			}
			else if (c.equals(Commodity.NATURALGASPOWER)) {
				
				fitnessValue += EPSCostCalculator.calcNaturalGasPowerCosts(currenttime, chpProfile, otherProfile, priceSignals, timeFactor, globalLogger);
			}
		}
		else {
			globalLogger.logError("epsOptimizationObjective not implemented");
		}
		
		return fitnessValue;
	}
		
	static private double calcNaturalGasPowerCosts(
			long currenttime, 
			LoadProfile chpProfile, 
			LoadProfile otherProfile, 
			HashMap<VirtualCommodity,PriceSignal> priceSignals, 
			double timeFactor, 
			OSHGlobalLogger globalLogger) {
		try {
			double totalPower = 0;
			
			double currentPowerChp = 0;
			if (chpProfile != null) {
				currentPowerChp = chpProfile.getLoadAt(Commodity.NATURALGASPOWER, currenttime);
			}
			double currentPowerOther = 0;
			if (otherProfile != null) {
				currentPowerOther = otherProfile.getLoadAt(Commodity.NATURALGASPOWER, currenttime);
			}
			
			totalPower = currentPowerChp + currentPowerOther;
			
			double fitnessValue = calcNaturalGasPower(currenttime, timeFactor, priceSignals, totalPower);
			return fitnessValue;
		}
		catch (Exception e) {
			globalLogger.logError(e);
			e.printStackTrace();
			return 0.0;
		}
	}
	
	
	
	static public double calcNaturalGasPower(
			long currenttime,
			double timeFactor,
			HashMap<VirtualCommodity,PriceSignal> priceSignals,
			double totalGasPower) {
		return timeFactor * (totalGasPower * priceSignals.get(VirtualCommodity.NATURALGASPOWEREXTERNAL).getPrice(currenttime));
	}
}
