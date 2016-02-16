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

package osh.driver.simulation;

import java.util.UUID;

import osh.configuration.OSHParameterCollection;
import osh.core.IOSH;
import osh.datatypes.Commodity;
import osh.datatypes.ComplexPowerUtil;
import osh.driver.simulation.data.H0Profile;
import osh.hal.exchange.BaseloadObserverExchange;
import osh.hal.exchange.HALControllerExchange;
import osh.simulation.AbstractDeviceSimulationDriver;
import osh.simulation.exception.SimulationSubjectException;
import osh.simulation.screenplay.SubjectAction;
import osh.utils.time.TimeConversion;

/**
 * 
 * @author Florian Allerding, Ingo Mauser
 *
 */
public class VirtualBaseload extends AbstractDeviceSimulationDriver {
	
	private H0Profile baseload;
	private double cosPhi;
	private boolean isInductive;
	
	
	/**
	 * CONSTRUCTOR
	 *
	 * @throws SimulationSubjectException
	 */
	public VirtualBaseload(IOSH controllerbox, UUID deviceID,
			OSHParameterCollection driverConfig)
			throws SimulationSubjectException {
		super(controllerbox, deviceID, driverConfig);

		String h0Filename = driverConfig.getParameter("h0filename");
		String consumptionShare = driverConfig.getParameter("consumptionshare");
		String yearlyElectricityConsumptionOfHousehold = driverConfig.getParameter("yearlyelectricityconsumptionofhousehold");
		
		this.cosPhi = Double.valueOf(driverConfig.getParameter("baseloadcosphi"));
		this.isInductive = Boolean.valueOf(driverConfig.getParameter("baseloadisinductive"));
		
		this.baseload = new H0Profile(
				TimeConversion.convertUnixTime2Year(getTimer().getUnixTime()),
				h0Filename, 
				Integer.valueOf(yearlyElectricityConsumptionOfHousehold), 
				Double.valueOf(consumptionShare));
	}
	
	
	@Override
	public void onNextTimeTick() {
		this.setPower(Commodity.ACTIVEPOWER, baseload.getActivePowerAt(getTimer().getUnixTime()));
		try {
			this.setPower(
					Commodity.REACTIVEPOWER,
					(int) ComplexPowerUtil.convertActiveToReactivePower(
							this.getPower(Commodity.ACTIVEPOWER), this.cosPhi, this.isInductive));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		BaseloadObserverExchange blox = 
				new BaseloadObserverExchange(
						getDeviceID(), 
						getTimer().getUnixTime());
		
		blox.setPower(Commodity.ACTIVEPOWER, this.getPower(Commodity.ACTIVEPOWER));
		blox.setPower(Commodity.REACTIVEPOWER, this.getPower(Commodity.REACTIVEPOWER));
		
		this.notifyObserver(blox);
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
