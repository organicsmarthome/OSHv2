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
public class VirtualHotWaterDemand extends AbstractDeviceSimulationDriver {
	
	@SuppressWarnings("unused")
	private double currentFlowRate = 0;
	private double currentTemp = 60;
	
	/**
	 * CONSTRUCTOR
	 * @param cb
	 * @param deviceID
	 * @param driverConfig
	 * @throws SimulationSubjectException
	 */
	public VirtualHotWaterDemand(IOSH cb, UUID deviceID,
			OSHParameterCollection driverConfig)
			throws SimulationSubjectException {
		super(cb, deviceID, driverConfig);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onNextTimeTick() {
		//NOTHING
	}
	
	public void computeFlowRate(double hotWaterDemand){
		double deltaTemp = currentTemp - 10;
		this.currentFlowRate = hotWaterDemand * 3.6 / (deltaTemp * 4.2);
	}
	
	public void update(double hotWaterDemand){
		computeFlowRate (hotWaterDemand);

	}
	@Override
	public void performNextAction(SubjectAction nextAction) {
		//NOTHING
	}

	@Override
	protected void onControllerRequest(HALControllerExchange controllerRequest) {
		//NOTHING
	}

}
