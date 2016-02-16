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
public class VirtualTemperature extends AbstractDeviceSimulationDriver {
	
	private double currentOutsideTemp;
	private double currentBed1Temp;
	private double currentBed2Temp;
	private double currentBathTemp;
	private double currentLivingTemp;
	
	
	/**
	 * CONSTRUCTOR
	 * @throws SimulationSubjectException
	 */
	public VirtualTemperature(
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
	
	@Override
	public void performNextAction(SubjectAction nextAction) {
		//NOTHING
	}

	@Override
	protected void onControllerRequest(HALControllerExchange controllerRequest) {
		//NOTHING
	}

	public double getCurrentOutsideTemp() {
		return currentOutsideTemp;
	}

	public void setCurrentOutsideTemp(double currentOutsideTemp) {
		this.currentOutsideTemp = currentOutsideTemp;
	}

	public double getCurrentBed1Temp() {
		return currentBed1Temp;
	}

	public void setCurrentBed1Temp(double currentBed1Temp) {
		this.currentBed1Temp = currentBed1Temp;
	}

	public double getCurrentBed2Temp() {
		return currentBed2Temp;
	}

	public void setCurrentBed2Temp(double currentBed2Temp) {
		this.currentBed2Temp = currentBed2Temp;
	}

	public double getCurrentBathTemp() {
		return currentBathTemp;
	}

	public void setCurrentBathTemp(double currentBathTemp) {
		this.currentBathTemp = currentBathTemp;
	}

	public double getCurrentLivingTemp() {
		return currentLivingTemp;
	}

	public void setCurrentLivingTemp(double currentLivingTemp) {
		this.currentLivingTemp = currentLivingTemp;
	}

}
