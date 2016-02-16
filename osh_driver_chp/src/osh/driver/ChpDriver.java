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

package osh.driver;

import java.util.UUID;

import osh.configuration.OSHParameterCollection;
import osh.core.IOSH;
import osh.core.com.interfaces.IEventReceiver;
import osh.core.com.interfaces.IHasState;
import osh.datatypes.registry.driver.details.chp.ChpDriverDetails;
import osh.hal.HALDeviceDriver;
import osh.hal.exchange.ChpControllerExchange;
import osh.hal.exchange.ChpObserverExchange;
import osh.hal.exchange.HALControllerExchange;


/**
 * 
 * @author Ingo Mauser	
 *
 */
public abstract class ChpDriver extends HALDeviceDriver implements IEventReceiver, IHasState {

	protected int minimumRuntime;
	protected int runtime;
	
	private boolean electricityRequest;
	private boolean heatingRequest;
	
	protected ChpDriverDetails chpDriverDetails = null;
	
	
	/**
	 * CONSTRUCTOR
	 * @param controllerbox
	 * @param deviceID
	 * @param driverConfig
	 */
	public ChpDriver(
			IOSH controllerbox, 
			UUID deviceID,
			OSHParameterCollection driverConfig) {
		super(controllerbox, deviceID, driverConfig);
		
	}

	@Override
	protected void onControllerRequest(HALControllerExchange controllerRequest) {
		if (controllerRequest instanceof ChpControllerExchange) {
			ChpControllerExchange chpControllerExchange = (ChpControllerExchange) controllerRequest;
			getGlobalLogger().logDebug("onControllerRequest(ChpControllerExchange)");
			electricityRequest = chpControllerExchange.isElectricityRequest(); 
			sendPowerRequestToChp();
		}
	}
	
	protected abstract void sendPowerRequestToChp();
	
	public synchronized void processChpDetailsAndNotify(ChpDriverDetails chpDetails) {
		ChpObserverExchange _ox = new ChpObserverExchange(getDeviceID(), getTimer().getUnixTime());
		_ox.setActivePower((int) Math.round(chpDetails.getCurrentElectricalPower()));
		_ox.setThermalPower((int) Math.round(chpDetails.getCurrentThermalPower()));
		_ox.setElectricityRequest(electricityRequest);
		_ox.setHeatingRequest(heatingRequest);
		_ox.setMinRuntime(minimumRuntime);
		_ox.setMinRuntimeRemaining(getMinimumRuntimeRemaining());
		
		this.notifyObserver(_ox);
	}

	public int getMinimumRuntime() {
		return minimumRuntime;
	}

	protected void setMinimumRuntime(int minimumRuntime) {
		this.minimumRuntime = minimumRuntime;
	}

	public int getMinimumRuntimeRemaining() {
		int returValue = getMinimumRuntime() - getRuntime();
		if (returValue < 0) returValue = 0;
		return returValue;
	}
	
	public int getRuntime() {
		return runtime;
	}

	public boolean isElectricityRequest() {
		return electricityRequest;
	}

	protected void setElectricityRequest(boolean electricityRequest) {
		if ( chpDriverDetails != null ) {
			chpDriverDetails.setPowerGenerationRequest(electricityRequest);
			getDriverRegistry().setState(ChpDriverDetails.class, this, chpDriverDetails);			
		}

		this.electricityRequest = electricityRequest;
	}

	public boolean isHeatingRequest() {
		return heatingRequest;
	}

	protected void setHeatingRequest(boolean heatingRequest) {
		if ( chpDriverDetails != null ) {
			chpDriverDetails.setHeatingRequest(heatingRequest);
			getDriverRegistry().setState(ChpDriverDetails.class, this, chpDriverDetails);			
		}
		this.heatingRequest = heatingRequest;
	}

	public boolean isOperationRequest() {
		return heatingRequest || electricityRequest;
	}
	
	@Override
	public UUID getUUID() {
		return getDeviceID();
	}

}
