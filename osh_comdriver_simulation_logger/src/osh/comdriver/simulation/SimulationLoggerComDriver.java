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

package osh.comdriver.simulation;

import java.util.HashMap;
import java.util.UUID;

import osh.comdriver.LoggerComDriver;
import osh.configuration.OSHParameterCollection;
import osh.configuration.system.DeviceTypes;
import osh.core.IOSH;
import osh.core.exceptions.OSHException;
import osh.datatypes.Commodity;
import osh.datatypes.VirtualCommodity;
import osh.datatypes.limit.PowerLimitSignal;
import osh.datatypes.limit.PriceSignal;
import osh.datatypes.logger.LoggerDetailedCostsHALExchange;
import osh.datatypes.logger.LoggerEpsPlsHALExchange;
import osh.datatypes.logger.LoggerVirtualCommoditiesHALExchange;
import osh.datatypes.registry.EventExchange;
import osh.datatypes.registry.globalobserver.CommodityPowerStateExchange;
import osh.datatypes.registry.globalobserver.DevicesPowerStateExchange;
import osh.hal.exchange.IHALExchange;
import osh.hal.exchange.LoggerCommodityPowerHALExchange;
import osh.hal.exchange.LoggerDevicesPowerHALExchange;
import osh.utils.EPSCostCalculator;


/**
 * 
 * @author Ingo Mauser
 *
 */
public class SimulationLoggerComDriver extends LoggerComDriver {
	
	private boolean firstLineSum = true;
	private boolean firstLineDetails = true;
	private boolean firstLineVirtualCommodities = true;
	private boolean firstLineEpsPls = true;
	

	/**
	 * CONSTRUCTOR
	 * @param controllerbox
	 * @param deviceID
	 * @param driverConfig
	 */
	public SimulationLoggerComDriver(IOSH controllerbox,
			UUID deviceID, OSHParameterCollection driverConfig) {
		super(controllerbox, deviceID, driverConfig);

	}
	
	
	/**
	 * Register to Timer for timed logging operations (logger gets data to log by itself)<br>
	 * Register to DriverRegistry for logging operations trigger by Drivers
	 */
	@Override
	public void onSystemIsUp() throws OSHException {
		super.onSystemIsUp();
		
		getTimer().registerComponent(this, 1);
	}

	/**
	 * Get things to log from O/C-layer
	 * @param exchangeObject
	 */
	@Override
	public void updateDataFromComManager(IHALExchange exchangeObject) {
		long now = getTimer().getUnixTime();
		
		if ( valueLoggerConfiguration.getIsValueLoggingToFileActive() ) {
			if (exchangeObject instanceof LoggerCommodityPowerHALExchange) {
				
				LoggerCommodityPowerHALExchange lcphe = (LoggerCommodityPowerHALExchange) exchangeObject;
				
				CommodityPowerStateExchange cpse = new CommodityPowerStateExchange(
						lcphe.getDeviceID(), 
						lcphe.getTimestamp(),
						lcphe.getPowerState(),
						DeviceTypes.OTHER);
				
				if (firstLineSum) {
					String firstEntry = "";
					for (Commodity c : Commodity.values()) {
						firstEntry += c + ";";
					}
					fileLog.logPower(firstEntry);
					firstLineSum = false;
				}
				
				String entryLine = "";
				
				for (Commodity c : Commodity.values()) {
					Double power = cpse.getPowerState(c);
					if (power != null) {
						entryLine += power + ";";
					}
					else {
						entryLine += "0;";
					}
				}
					
				fileLog.logPower(entryLine);
			}
		
			else if (exchangeObject instanceof LoggerDevicesPowerHALExchange) {
				
				LoggerDevicesPowerHALExchange ldphe = (LoggerDevicesPowerHALExchange) exchangeObject;
				
				DevicesPowerStateExchange dpse = new DevicesPowerStateExchange(
						ldphe.getDeviceID(), 
						ldphe.getTimestamp(), 
						ldphe.getPowerStates());
				
				if (firstLineDetails) {
					String entryLine = "";
					
					// induction cooktop
					for (Commodity c : Commodity.values()) {
						entryLine += "HB_" + c + ";";
					}
					
					// dishwasher
					for (Commodity c : Commodity.values()) {
						entryLine += "DW_" + c + ";";
					}
					
					// oven
					for (Commodity c : Commodity.values()) {
						entryLine += "OV_" + c + ";";
					}
			        
			        // dryer
					for (Commodity c : Commodity.values()) {
						entryLine += "DR_" + c + ";";
					}
			        
			        // washer
					for (Commodity c : Commodity.values()) {
						entryLine += "WM_" + c + ";";
					}
			        
			        // PV
					for (Commodity c : Commodity.values()) {
						entryLine += "PV_" + c + ";";
					}
					
					// baseload
					for (Commodity c : Commodity.values()) {
						entryLine += "BL_" + c + ";";
					}
					
					// CHP
					for (Commodity c : Commodity.values()) {
						entryLine += "CHP_ESHL_" + c + ";";
					}
					
					fileLog.logPowerDetails(entryLine);
					firstLineDetails = false;
				}	
				
				HashMap<UUID,HashMap<Commodity,Double>> map = dpse.getPowerStateMap();
				String entryLine = "";

				// induction cooktop
				UUID hb = UUID.fromString("e2ef0d13-61b3-4188-b32a-1570dcbab4d1");
				for (Commodity c : Commodity.values()) {
					if (map.get(hb) == null) {
						entryLine += "0;";
					}
					else
					entryLine += map.get(hb).get(c) + ";";
				}
				// dishwasher
				UUID dw = UUID.fromString("ab9519db-7a14-4e43-ac3a-ade723802194");
				for (Commodity c : Commodity.values()) {
					if (map.get(dw) == null) {
						entryLine += "0;";
					}
					else
					entryLine += map.get(dw).get(c) + ";";
				}
				// oven
				UUID ov = UUID.fromString("cef732b1-04ba-49e1-8189-818468a0d98e");
				for (Commodity c : Commodity.values()) {
					if (map.get(ov) == null) {
						entryLine += "0;";
					}
					else
					entryLine += map.get(ov).get(c) + ";";
				}
				
				// dryer
				UUID dr = UUID.fromString("1468cc8a-dfdc-418a-8df8-96ba8c146156");
				for (Commodity c : Commodity.values()) {
					if (map.get(dr) == null) {
						entryLine += "0;";
					}
					else
					entryLine += map.get(dr).get(c) + ";";
				}
				
				// washer
				UUID wm = UUID.fromString("e7b3f13d-fdf6-4663-848a-222303d734b8");
				for (Commodity c : Commodity.values()) {
					if (map.get(wm) == null) {
						entryLine += "0;";
					}
					else
					entryLine += map.get(wm).get(c) + ";";
				}
				
				// PV
				UUID pv = UUID.fromString("f72c8ecb-7b82-4c8d-89db-751c9a139075");
				for (Commodity c : Commodity.values()) {
					if (map.get(pv) == null) {
						entryLine += "0;";
					}
					else
					entryLine += map.get(pv).get(c) + ";";
				}
				
				// baseload
				UUID bl = UUID.fromString("d313ddbb-b991-46d7-8572-26ae9359a621");
				for (Commodity c : Commodity.values()) {
					if (map.get(bl) == null) {
						entryLine += "0;";
					}
					else
					entryLine += map.get(bl).get(c) + ";";
				}

				// CHP_ESHL
				{
					UUID chp_eshl = UUID.fromString("34693aa1-4a15-4504-b6d8-931658b81f09");
					for (Commodity c : Commodity.values()) {
						if (map.get(chp_eshl) == null) {
							entryLine += "0;";
						}
						else
						entryLine += map.get(chp_eshl).get(c) + ";";
					}
				}
				
				fileLog.logPowerDetails(entryLine);
			}
			else if (exchangeObject instanceof LoggerVirtualCommoditiesHALExchange) {
				LoggerVirtualCommoditiesHALExchange lvche = (LoggerVirtualCommoditiesHALExchange) exchangeObject;
				HashMap<VirtualCommodity,Integer> map = lvche.getMap();
				
				if (firstLineVirtualCommodities) {
//					String entryLine = "";
					String entryLine = "timestamp;";
					
					for (VirtualCommodity vc : VirtualCommodity.values()) {
						entryLine = entryLine + vc.toString() + ";";
					}
					
					fileLog.logVirtualCommodityPowerDetails(entryLine);
					firstLineVirtualCommodities = false;
				}
//				String entryLine = "";
				String entryLine = now + ";";
				
				for (VirtualCommodity vc : VirtualCommodity.values()) {
					Integer value = map.get(vc);
					if (value == null) {
						value = 0;
					}
					entryLine = entryLine + value + ";";
				}
				
				fileLog.logVirtualCommodityPowerDetails(entryLine);
			}
			
			else if (exchangeObject instanceof LoggerEpsPlsHALExchange) {
				LoggerEpsPlsHALExchange lephe = (LoggerEpsPlsHALExchange) exchangeObject;
				
				if (firstLineEpsPls) {
					String entryLine = "";
					
					for (VirtualCommodity vc : VirtualCommodity.values()) {
						entryLine = entryLine + "EPS_" + vc.toString() + ";";
					}
					
					for (VirtualCommodity vc : VirtualCommodity.values()) {
						entryLine = entryLine + "PLS_upper_" + vc.toString() + ";";
						entryLine = entryLine + "PLS_lower_" + vc.toString() + ";";
					}
					
					fileLog.logExternalSignals(entryLine);
					firstLineEpsPls = false;
				}
				
				String entryLine = "";
				
				for (VirtualCommodity vc : VirtualCommodity.values()) {
					HashMap<VirtualCommodity,PriceSignal> pss = lephe.getPs();
					
					double value = 0.0;
					if (pss != null) {
						PriceSignal ps = pss.get(vc);
						if (ps != null) {
							value = ps.getPrice(now);
						}
					}
					
					entryLine = entryLine + value + ";";
				}
				
				for (VirtualCommodity vc : VirtualCommodity.values()) {
					HashMap<VirtualCommodity, PowerLimitSignal> plss = lephe.getPwrLimit();
					
					double uvalue = 0.0;
					double lvalue = 0.0;
					if (plss != null) {
						PowerLimitSignal pls = plss.get(vc);
						if (pls != null) {
							uvalue = pls.getPowerUpperLimit(now);
							lvalue = pls.getPowerLowerLimit(now);
						}
					}
					
					entryLine = entryLine + uvalue + ";" + lvalue + ";";
				}
				
				fileLog.logExternalSignals(entryLine);
			}
			
			else if (exchangeObject instanceof LoggerDetailedCostsHALExchange) {
				LoggerDetailedCostsHALExchange lvche = (LoggerDetailedCostsHALExchange) exchangeObject;
				HashMap<VirtualCommodity,Integer> map = lvche.getPowerValueMap();
				
				if (firstLineVirtualCommodities) {
					String entryLine = "timestamp;";
					
					for (VirtualCommodity vc : VirtualCommodity.values()) {
						entryLine = entryLine + vc.toString() + ";";
					}
					
					for (VirtualCommodity vc : VirtualCommodity.values()) {
						entryLine = entryLine + "EPS_" + vc.toString() + ";";
					}
					
					for (VirtualCommodity vc : VirtualCommodity.values()) {
						entryLine = entryLine + "PLS_upper_" + vc.toString() + ";";
						entryLine = entryLine + "PLS_lower_" + vc.toString() + ";";
					}
					
					// total costs
					entryLine = entryLine + "totalEPScosts";
					
					fileLog.logCostDetailed(entryLine);
					firstLineVirtualCommodities = false;
				}
				
				String entryLine = now + ";";
				
				for (VirtualCommodity vc : VirtualCommodity.values()) {
					Integer value = map.get(vc);
					if (value == null) {
						value = 0;
					}
					entryLine = entryLine + value + ";";
				}
				
				for (VirtualCommodity vc : VirtualCommodity.values()) {
					HashMap<VirtualCommodity,PriceSignal> pss = lvche.getPs();
					
					double value = 0.0;
					if (pss != null) {
						PriceSignal ps = pss.get(vc);
						if (ps != null) {
							value = ps.getPrice(now);
						}
					}
					
					entryLine = entryLine + value + ";";
				}
				
				for (VirtualCommodity vc : VirtualCommodity.values()) {
					HashMap<VirtualCommodity, PowerLimitSignal> plss = lvche.getPwrLimit();
					
					double uvalue = 0.0;
					double lvalue = 0.0;
					if (plss != null) {
						PowerLimitSignal pls = plss.get(vc);
						if (pls != null) {
							uvalue = pls.getPowerUpperLimit(now);
							lvalue = pls.getPowerLowerLimit(now);
						}
					}
					
					entryLine = entryLine + uvalue + ";" + lvalue + ";";
				}
				
				// total costs
				double costs = 0.0;
				
				{
					// EPS
					double additional = 0;
					
					double currentActivePowerExternal = 0;
					if (lvche.getPowerValueMap().get(VirtualCommodity.ACTIVEPOWEREXTERNAL) != null
							&& lvche.getPowerValueMap().get(VirtualCommodity.PVACTIVEPOWERFEEDIN) != null
							&& lvche.getPowerValueMap().get(VirtualCommodity.CHPACTIVEPOWERFEEDIN) != null) {
						currentActivePowerExternal = lvche.getPowerValueMap().get(VirtualCommodity.ACTIVEPOWEREXTERNAL)
								+ lvche.getPowerValueMap().get(VirtualCommodity.PVACTIVEPOWERFEEDIN)
								+ lvche.getPowerValueMap().get(VirtualCommodity.CHPACTIVEPOWERFEEDIN);
					}
					
					double currentActivePowerPv = 0;
					if (lvche.getPowerValueMap().get(VirtualCommodity.PVACTIVEPOWERFEEDIN) != null
							&& lvche.getPowerValueMap().get(VirtualCommodity.PVACTIVEPOWERAUTOCONSUMPTION) != null) {
						currentActivePowerPv = lvche.getPowerValueMap().get(VirtualCommodity.PVACTIVEPOWERFEEDIN)
								+ lvche.getPowerValueMap().get(VirtualCommodity.PVACTIVEPOWERAUTOCONSUMPTION);
					}
					
					double currentActivePowerChp = 0;
					if (lvche.getPowerValueMap().get(VirtualCommodity.CHPACTIVEPOWERFEEDIN) != null
							&& lvche.getPowerValueMap().get(VirtualCommodity.CHPACTIVEPOWERAUTOCONSUMPTION) != null) {
						currentActivePowerPv = lvche.getPowerValueMap().get(VirtualCommodity.CHPACTIVEPOWERFEEDIN)
								+ lvche.getPowerValueMap().get(VirtualCommodity.CHPACTIVEPOWERAUTOCONSUMPTION);
					}
					
					// FIXME
					additional = additional + EPSCostCalculator.calcEpsOptimizationObjective3(
							now, 
							1.0 / 3600000.0, 
							lvche.getPs(), 
							currentActivePowerExternal, 
							currentActivePowerPv, 
							currentActivePowerChp);
					
					double currentGasPower = 0;
					if (lvche.getPowerValueMap().get(VirtualCommodity.NATURALGASPOWEREXTERNAL) != null) {
						currentGasPower = lvche.getPowerValueMap().get(VirtualCommodity.NATURALGASPOWEREXTERNAL);
					}
					
					// gas
					additional = additional + EPSCostCalculator.calcNaturalGasPower(
							now, 
							1.0 / 3600000.0, 
							lvche.getPs(), 
							currentGasPower);
					
					costs = costs + additional;
					
					//PLS
					//FIXME
					
					
				}
				
				entryLine = entryLine + costs;
				
				fileLog.logCostDetailed(entryLine);
			}
			
		}
		
	}
	
	/**
	 * Received logging events from devices (push-logging)
	 */
	@Override
	public void onQueueEventReceived(EventExchange event) throws OSHException {
		//TODO
	}

	@Override
	public UUID getUUID() {
		return getDeviceID();
	}

}
