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

package osh.driver.simulation.thermalSimulation;

import java.util.UUID;

import osh.configuration.OSHParameterCollection;
import osh.core.IOSH;
import osh.driver.simulation.thermalSimulation.model.HeatingSystemData;
import osh.hal.exchange.DachsWithElectricHeatingControllerExchange;
import osh.hal.exchange.HALControllerExchange;
import osh.simulation.AbstractDeviceSimulationDriver;
import osh.simulation.exception.SimulationSubjectException;
import osh.simulation.screenplay.SubjectAction;

/**
 * 
 * @author Florian Allerding, Kaibin Bao, Ingo Mauser, Till Schuberth
 *
 */
public class VirtualCHPEngineESHL extends AbstractDeviceSimulationDriver {

	private double powerEl = 0; //kW
	private double powerTh = 0; //kW
	private final double maxPowerTh; //kW
	private final double maxPowerEL; //kW
	
	private int powerGas = 0; // W
	private final int maxPowerGas; // W

	private final double efficiencyTh;
	private final double efficiencyEl;
	
	private int currentOperationTime;
	
	private boolean engineOnState;
	private boolean minTimeActive;
	private boolean requestActive;
	
	private HeatingSystemData heatingSystemData;

	
	/**
	 * CONSTRUCTOR
	 * @param cb
	 * @param deviceID
	 * @param driverConfig
	 * @throws SimulationSubjectException
	 */
	public VirtualCHPEngineESHL(IOSH cb,
			UUID deviceID, OSHParameterCollection driverConfig)
			throws SimulationSubjectException {
		super(cb, deviceID, driverConfig);
		// FIXME: Please init the heating system only at one point (now both in
		// the Virtual CHP and the VirtualCHPEngine!!!)
		// BUT: double init is currently necessary!!!
		
		// init thermal data
		heatingSystemData = new HeatingSystemData(this.getDeviceID(), cb.getTimer().getUnixTime());
		
		// init heating config
		try {
			heatingSystemData.initConfig(driverConfig.getParameter("chpConfigFileName"));
		} catch (Exception e) {
			cb.getLogger().logError("Can't init the heating system", e);

		}
		
		maxPowerTh = heatingSystemData.getPowerTh();
		maxPowerEL = heatingSystemData.getPowerEL();
		maxPowerGas = heatingSystemData.getPowerGas();
		
		efficiencyTh = heatingSystemData.getEfficiencyth();
		efficiencyEl = heatingSystemData.getEfficiencyth();
		
		currentOperationTime = 0;
		
		engineOnState = false;
		minTimeActive = false;
		requestActive = false;
	}

	@Override
	protected void onControllerRequest(HALControllerExchange controllerRequest) {
		DachsWithElectricHeatingControllerExchange exchangeController = (DachsWithElectricHeatingControllerExchange) controllerRequest;
		if (exchangeController.isUseGasHeating()) {
			setOn();
		}
		else {
			setOff(true);
		}
	}

	@Override
	public void onNextTimeTick() {
		
		if (engineOnState) {
			currentOperationTime++;
		}
		
		// has only been running because of minRuntime 
		if (getMinEngineRuntimeRemaining() == 0) {
			minTimeActive = false;
		}
		
		// TODO: check whether necessary (maybe causing spikes)
		if (engineOnState && !minTimeActive && !requestActive) {
			setOff(true);
		}
	}

	@Override
	public void performNextAction(SubjectAction nextAction) {
		// NOTHING
	}

	
	public void setState(boolean state, boolean considerMinTime) {
		if (state) {
			setOn();
		}
		else {
			setOff(considerMinTime);
		}
	}
	
	private void setOn() {
		// is off, switch on
		if (!engineOnState) {
			engineOnState = true;
			minTimeActive = true;
			requestActive = true;
			
			setPowerTh(maxPowerTh);
			setPowerEl(maxPowerEL);
			setPowerGas(maxPowerGas);
			
			sendNewPowerState();
		}
		// is on, switch on
		else {
			//NOTHING
		}
	}
	
	private void sendNewPowerState() {
		//TODO
	}

	private void setOff(boolean considerMinTime) {
		boolean setOff = false;
		
		if (!considerMinTime) {
			setOff = true;
		}
		else if (minTimeActive) {
			requestActive = false;
		}
		else {
			setOff = true;
		}
		
		if (setOff) {
			engineOnState = false;
			minTimeActive = false;
			requestActive = false;
			
			setPowerTh(0);
			setPowerEl(0);
			setPowerGas(0);
			
			currentOperationTime = 0;
		}
	}
	
	public long getMinEngineRuntimeRemaining() {
		long minTime = heatingSystemData.getMinoperationtime() - currentOperationTime;
		if (minTime < 0) {
			minTime = 0;
		}
		return minTime;
	}
	
	// ### Getters / Setters 
	
	public double getEfficiencyEl() {
		return efficiencyEl;
	}

	public double getEfficiencyTh() {
		return efficiencyTh;
	}

	public double getMaxPowerEL() {
		return maxPowerEL;
	}

	public double getMaxPowerTh() {
		return maxPowerTh;
	}
	
	public int getMaxPowerGas() {
		return maxPowerGas;
	}

	public double getPowerEl() {
		return powerEl;
	}

	public double getPowerTh() {
		return powerTh;
	}
	
	public int getPowerGas() {
		return powerGas;
	}
	
	public void setPowerEl(double powerlEl) {
		this.powerEl = powerlEl;
	}

	public void setPowerTh(double powerTh) {
		this.powerTh = powerTh;
	}
	
	public void setPowerGas(int powerGas) {
		this.powerGas = powerGas;
	}

	public boolean isEngineState() {
		return engineOnState;
	}

}
