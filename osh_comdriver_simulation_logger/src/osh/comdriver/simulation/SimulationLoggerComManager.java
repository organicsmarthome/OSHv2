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

import java.util.UUID;

import osh.comdriver.LoggerComManager;
import osh.core.IOSHOC;
import osh.core.exceptions.OSHException;
import osh.datatypes.logger.LoggerDetailedCostsHALExchange;
import osh.datatypes.logger.LoggerEpsPlsHALExchange;
import osh.datatypes.logger.LoggerVirtualCommoditiesHALExchange;
import osh.datatypes.registry.EventExchange;
import osh.datatypes.registry.globalobserver.CommodityPowerStateExchange;
import osh.datatypes.registry.globalobserver.DetailedCostsLoggingStateExchange;
import osh.datatypes.registry.globalobserver.EpsPlsStateExchange;
import osh.datatypes.registry.globalobserver.VirtualCommodityPowerStateExchange;
import osh.hal.exchange.IHALExchange;
import osh.hal.exchange.LoggerCommodityPowerHALExchange;


/**
 * 
 * @author Ingo Mauser
 *
 */
public class SimulationLoggerComManager extends LoggerComManager {

	/**
	 * CONSTRUCTOR
	 */
	public SimulationLoggerComManager(IOSHOC controllerbox, UUID uuid) {
		super(controllerbox, uuid);
	}

	
	@Override
	public void onSystemIsUp() throws OSHException {
		super.onSystemIsUp();
		
		getTimer().registerComponent(this, 1);
	}

	@Override
	public void onQueueEventReceived(EventExchange ex) throws OSHException {
		super.onQueueEventReceived(ex);
		
		//TODO
	}

	@Override
	public void onDriverUpdate(IHALExchange exchangeObject) {
		super.onDriverUpdate(exchangeObject);
		
		//TODO
	}

	@Override
	public void onNextTimePeriod() throws OSHException {
		super.onNextTimePeriod();
		
		// get overall power state and communicate to driver for logging
		{
			CommodityPowerStateExchange cpse = 
					this.getOCRegistry().getState(
							CommodityPowerStateExchange.class, 
							getGlobalOCUnitUUID());

			if (cpse != null) {
				LoggerCommodityPowerHALExchange cphe = 
						new LoggerCommodityPowerHALExchange(
								cpse.getSender(), 
								cpse.getTimestamp(), 
								cpse.getPowerState());
				updateOcDataSubscriber(cphe);
			}
		}
		
		// get power states of all devices and communicate to logger
		{
//			DevicesPowerStateExchange dpse = 
//					this.getOCRegistry().getState(
//							DevicesPowerStateExchange.class, 
//							getGlobalOCUnitUUID());
//			
//			if (dpse != null) {
//				LoggerDevicesPowerHALExchange dphe = 
//						new LoggerDevicesPowerHALExchange(
//								dpse.getSender(), 
//								dpse.getTimestamp(), 
//								dpse.getPowerStateMap());
//				updateOcDataSubscriber(dphe);
//			}
		}
		
		// get VirtualCommodity Power states and communicate to logger
		{
			VirtualCommodityPowerStateExchange vcse = 
					this.getOCRegistry().getState(
							VirtualCommodityPowerStateExchange.class, 
							getGlobalOCUnitUUID());
			
			if (vcse != null) {
				LoggerVirtualCommoditiesHALExchange vche =
						new LoggerVirtualCommoditiesHALExchange(
								vcse.getSender(), 
								vcse.getTimestamp(), 
								vcse.getMap());
				updateOcDataSubscriber(vche);
			}
		}
		
		// get EPS and PLS states and communicate to logger
		{
			EpsPlsStateExchange epse = 
					this.getOCRegistry().getState(
							EpsPlsStateExchange.class, 
							getGlobalOCUnitUUID());
			
			if (epse != null) {
				LoggerEpsPlsHALExchange ephe =
						new LoggerEpsPlsHALExchange(
								epse.getSender(), 
								epse.getTimestamp(), 
								epse.getPs(), 
								epse.getPwrLimit());
				updateOcDataSubscriber(ephe);
			}
		}
		
		// get VirtualCommodity Power states and EPS and PLS states and communicate to logger
		{
			DetailedCostsLoggingStateExchange vcse = 
					this.getOCRegistry().getState(
							DetailedCostsLoggingStateExchange.class, 
							getGlobalOCUnitUUID());
			
			if (vcse != null) {
				LoggerDetailedCostsHALExchange vche =
						new LoggerDetailedCostsHALExchange(
								vcse.getSender(), 
								vcse.getTimestamp(), 
								vcse.getMap(),
								vcse.getPs(),
								vcse.getPwrLimit());
				updateOcDataSubscriber(vche);
			}
		}
	}

}
