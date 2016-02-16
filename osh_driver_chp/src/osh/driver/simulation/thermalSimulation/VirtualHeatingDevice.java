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
import osh.hal.exchange.HALControllerExchange;
import osh.simulation.AbstractDeviceSimulationDriver;
import osh.simulation.exception.SimulationSubjectException;
import osh.simulation.screenplay.SubjectAction;

/**
 * 
 * @author Florian Allerding, Till Schuberth
 *
 */
public class VirtualHeatingDevice extends AbstractDeviceSimulationDriver {
	
	private double currentHeatingLoad;
	private double valvePosition;
	private double currentFlowRate;
	private double currentInletTemp;
	private double currentReturnTemp;
	private ThermalSimulationCore thermalSimulationCore;
	
	
	/**
	 * CONSTRUCTOR
	 * @throws SimulationSubjectException
	 */
	public VirtualHeatingDevice(
			IOSH cb, 
			UUID deviceID,
			OSHParameterCollection driverConfig)
			throws SimulationSubjectException {
		super(cb, deviceID, driverConfig);
	}

	
	@Override
	public void onNextTimeTick() {
		//NOTHING
	}

	private void computeFlowRate(double heatingDemand, double inletTemp) {
		/* computes new flow rate from heating load and temperature of the thermal Storage
		 * return temp 60 degree C const., 4.2 KJ/(kg*K), heating demand in KW
		*/ 
		currentInletTemp=inletTemp;
		currentReturnTemp=60;
		currentFlowRate=heatingDemand*3600/((currentInletTemp-currentReturnTemp)*4.2);
	}
	
	public void update (double heatingDemand, double tempTop) {
		this.computeFlowRate(heatingDemand,tempTop);
	}

	@Override
	public void performNextAction(SubjectAction nextAction) {
		//NOTHING
	}
	
	@Override
	protected void onControllerRequest(HALControllerExchange controllerRequest) {
		//NOTHING
	}

	public void setThermalSimulationCore(ThermalSimulationCore thermalSimulationCore) {
		this.thermalSimulationCore = thermalSimulationCore;
	}

	public ThermalSimulationCore getThermalSimulationCore() {
		return thermalSimulationCore;
	}

	public double getCurrentHeatingLoad() {
		return currentHeatingLoad;
	}

	public void setCurrentHeatingLoad(double currentHeatingLoad) {
		this.currentHeatingLoad = currentHeatingLoad;
	}

	public double getValvePosition() {
		return valvePosition;
	}

	public void setValvePosition(double valvePosition) {
		this.valvePosition = valvePosition;
	}

	public double getCurrentFlowRate() {
		return currentFlowRate;
	}

	public void setCurrentFlowRate(double currentFlowRate) {
		this.currentFlowRate = currentFlowRate;
	}

	public double getCurrentInletTemp() {
		return currentInletTemp;
	}

	public void setCurrentInletTemp(double currentInletTemp) {
		this.currentInletTemp = currentInletTemp;
	}

	public double getCurrentReturnTemp() {
		return currentReturnTemp;
	}

	public void setCurrentReturnTemp(double currentReturnTemp) {
		this.currentReturnTemp = currentReturnTemp;
	}

}
