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

import java.util.Map;
import java.util.UUID;

import osh.comdriver.simulation.cruisecontrol.GuiDataCollector;
import osh.comdriver.simulation.cruisecontrol.GuiMain;
import osh.comdriver.simulation.cruisecontrol.stateviewer.StateViewerListener;
import osh.comdriver.simulation.cruisecontrol.stateviewer.StateViewerRegistryEnum;
import osh.configuration.OSHParameterCollection;
import osh.core.IOSH;
import osh.core.exceptions.OSHException;
import osh.datatypes.registry.StateExchange;
import osh.datatypes.registry.localobserver.WaterStorageStateExchange;
import osh.hal.exchange.DevicesPowerComExchange;
import osh.hal.exchange.GUIDeviceListComExchange;
import osh.hal.exchange.GUIEpsComExchange;
import osh.hal.exchange.GUIPlsComExchange;
import osh.hal.exchange.GUIScheduleComExchange;
import osh.hal.exchange.GUIStateRegistrySelectedComExchange;
import osh.hal.exchange.GUIStateSelectedComExchange;
import osh.hal.exchange.GUIStatesComExchange;
import osh.hal.exchange.GUIWaterStorgaComExchange;
import osh.hal.exchange.IHALExchange;
import osh.simulation.SimulationComDriver;
import osh.simulation.exception.SimulationSubjectException;


/**
 * 
 * @author Till Schuberth
 *
 */
public class GuiComDriver extends SimulationComDriver implements StateViewerListener {

	private GuiMain driver;
	private GuiDataCollector collector;
	
	boolean saveGraph = false;
	
	
	/**
	 * CONSTRUCTOR
	 * @throws SimulationSubjectException
	 */
	public GuiComDriver(
			IOSH controllerbox, 
			UUID deviceID,
			OSHParameterCollection driverConfig) throws SimulationSubjectException {
		super(controllerbox, deviceID, driverConfig);
		
		driver = new GuiMain(!getOSH().isSimulation());
		driver.registerListener(this);
		collector = new GuiDataCollector(driver, saveGraph);
	}

	
	@Override
	public void onSystemIsUp() throws OSHException {
		super.onSystemIsUp();
		
		getTimer().registerComponent(this, 1);
	}
	
	@Override
	public void onNextTimePeriod() throws OSHException {
		super.onNextTimePeriod();		
		driver.updateTime(getTimerDriver().getUnixTime());
	}
	
	@Override
	public void updateDataFromComManager(IHALExchange exchangeObject) {
		
		if (exchangeObject instanceof GUIScheduleComExchange) {
			GUIScheduleComExchange exgs = (GUIScheduleComExchange) exchangeObject;
			collector.updateGlobalSchedule(exgs.getSchedules(), exchangeObject.getTimestamp());
		} 
		else if (exchangeObject instanceof GUIDeviceListComExchange) {
			GUIDeviceListComExchange exgdl = (GUIDeviceListComExchange) exchangeObject;
			collector.updateEADeviceList(exgdl.getDeviceList());
		}
		else if (exchangeObject instanceof GUIStatesComExchange) {
			GUIStatesComExchange exgse = (GUIStatesComExchange) exchangeObject;
			if (exgse.isOcMode()) {
				collector.updateStateView(exgse.getTypes(), exgse.getStates());
			} else {
				Map<UUID, ? extends StateExchange> states = null;
				if (exgse.getDriverstatetype() != null) {
					states = getDriverRegistry().getStates(exgse.getDriverstatetype());
				}

				collector.updateStateView(getDriverRegistry().getTypes(), states);
			}
		}
		else if (exchangeObject instanceof GUIEpsComExchange) {
			GUIEpsComExchange gece = (GUIEpsComExchange) exchangeObject;
			collector.updateGlobalSchedule(gece.getPriceSignals(), gece.getTimestamp());
		}
		else if (exchangeObject instanceof GUIPlsComExchange) {
			GUIPlsComExchange gpce = (GUIPlsComExchange) exchangeObject;
			collector.updateGlobalSchedule(gpce.getTimestamp(), gpce.getPowerLimitSignals());
		}
		else if (exchangeObject instanceof GUIWaterStorgaComExchange) {
			GUIWaterStorgaComExchange gwsce = (GUIWaterStorgaComExchange) exchangeObject;
			WaterStorageStateExchange gwsse = new WaterStorageStateExchange(
					gwsce.getDeviceID(), gwsce.getTimestamp(), gwsce.getCurrenttemp(), gwsce.getMintemp(), gwsce.getMaxtemp());
			collector.updateWaterStorageData(gwsse);
		}
		else if (exchangeObject instanceof DevicesPowerComExchange) {
			DevicesPowerComExchange dpsex = (DevicesPowerComExchange) exchangeObject;
			collector.updatePowerStates(dpsex.getTimestamp(), dpsex.getPowerStates());
		}
		else {
			getGlobalLogger().logError("unknown exchange data type: " + exchangeObject.getClass().getName());
			return;
		}
			
	}

	@Override
	public void stateViewerClassChanged(Class<? extends StateExchange> cls) {
		notifyComManager(
				new GUIStateSelectedComExchange(
						getDeviceID(), 
						getTimer().getUnixTime(), 
						cls));
	}


	@Override
	public void stateViewerRegistryChanged(StateViewerRegistryEnum registry) {
		notifyComManager(
				new GUIStateRegistrySelectedComExchange(
						getDeviceID(), 
						getTimer().getUnixTime(), 
						registry));
	}
	
}
