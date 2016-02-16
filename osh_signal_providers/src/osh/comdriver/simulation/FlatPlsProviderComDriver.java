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

import osh.configuration.OSHParameterCollection;
import osh.core.IOSH;
import osh.core.exceptions.OSHException;
import osh.datatypes.VirtualCommodity;
import osh.datatypes.limit.PowerLimitSignal;
import osh.hal.HALComDriver;
import osh.hal.exchange.IHALExchange;
import osh.hal.exchange.PlsComExchange;
import osh.simulation.exception.SimulationSubjectException;

/**
 * 
 * @author Ingo Mauser
 *
 */
public class FlatPlsProviderComDriver  extends HALComDriver {

	private HashMap<VirtualCommodity,PowerLimitSignal> powerLimitSignals;
	
	/** Time after which a signal is send */
	private int newSignalAfterThisPeriod = 43200; // 12 hours
	
	/** Maximum time the signal is available in advance (24h) */
	private int signalPeriod = 86400;  // 24 hours
//	/** Maximum time the signal is available in advance (36h) */
//	private int signalPeriod = 129600; // 36 hours
	
	/* Minimum time the signal is available in advance (24h) 
	 * atLeast = signalPeriod - newSignalAfterThisPeriod */
//	private int signalAvailableFor = 129600 - 43200;
	
	/** Signal is constant for 15 minutes */
	private int signalConstantPeriod = 900; // change every 15 minutes

	
	/**
	 * CONSTRUCTOR
	 * @param controllerbox
	 * @param deviceID
	 * @param driverConfig
	 * @throws SimulationSubjectException
	 */
	public FlatPlsProviderComDriver(
			IOSH controllerbox,
			UUID deviceID, 
			OSHParameterCollection driverConfig)
			throws SimulationSubjectException {
		super(controllerbox, deviceID, driverConfig);
		
		this.powerLimitSignals = new HashMap<>();
	}
	
	
	@Override
	public void onSystemIsUp() throws OSHException {
		super.onSystemIsUp();
		
		long now = getTimer().getUnixTime();
		powerLimitSignals = generateNewPowerLimitSignal();
		PlsComExchange ex = new PlsComExchange(
				this.getDeviceID(), 
				now, 
				powerLimitSignals);
		this.notifyComManager(ex);
	}
	
	
	//TODO: better signal
	private HashMap<VirtualCommodity,PowerLimitSignal> generateNewPowerLimitSignal() {
		HashMap<VirtualCommodity,PowerLimitSignal> newPls = new HashMap<>();
		PowerLimitSignal activePowerLimitSignal = new PowerLimitSignal();
		activePowerLimitSignal.setPowerLimit(0, 3000, -10000);
		newPls.put(VirtualCommodity.ACTIVEPOWEREXTERNAL, activePowerLimitSignal);
		return newPls;
	}

	@Override
	public void onNextTimePeriod() throws OSHException {
		super.onNextTimePeriod();
		
		// generate new PriceSignal and send it
		if (getTimer().getUnixTime() % newSignalAfterThisPeriod == 0) {
			long now = getTimer().getUnixTime();
			// PLS
			powerLimitSignals = generateNewPowerLimitSignal();
			PlsComExchange ex = new PlsComExchange(
					this.getDeviceID(), 
					now, 
					powerLimitSignals);
			this.notifyComManager(ex);
		}
	}

	@Override
	public void updateDataFromComManager(IHALExchange exchangeObject) {
		//NOTHING
	}

}
