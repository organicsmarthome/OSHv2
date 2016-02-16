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

package osh.hal.exchange;

import java.util.UUID;

import osh.datatypes.hal.interfaces.chp.IHALChpDetails;
import osh.datatypes.hal.interfaces.chp.IHALChpStaticDetails;
import osh.datatypes.hal.interfaces.electricity.IHALElectricPowerDetails;
import osh.datatypes.hal.interfaces.thermal.IHALThermalPowerDetails;
import osh.hal.exchange.HALDeviceObserverExchange;


/**
 * 
 * @author Ingo Mauser
 *
 */
public class ChpObserverExchange 
				extends HALDeviceObserverExchange
				implements 	IHALChpDetails,
							IHALChpStaticDetails,
							IHALElectricPowerDetails,
							IHALThermalPowerDetails {
	
	// ### IHALChpDetails ###
	private boolean heatingRequest;
	private boolean electricityRequest;
	private int minRuntimeRemaining;
//	private int temperatureIn;
//	private int temperatureOut;
	
	// ### IHALChpStaticDetails ###
	private int minRuntime;
	
	// ### IHALElectricPowerDetails ###
	private int activePower;
	private int reactivePower;
	
	// ### IHALThermalPowerDetails ###
	private int thermalPower;
	
	
	/**
	 * CONSTRUCTOR
	 * @param deviceID
	 * @param timestamp
	 */
	public ChpObserverExchange(
			UUID deviceID, 
			Long timestamp) {
		super(deviceID, timestamp);
	}

	@Override
	public boolean isHeatingRequest() {
		return heatingRequest;
	}

	public void setHeatingRequest(boolean heatingRequest) {
		this.heatingRequest = heatingRequest;
	}

	@Override
	public boolean isElectricityRequest() {
		return electricityRequest;
	}

	public void setElectricityRequest(boolean electricityRequest) {
		this.electricityRequest = electricityRequest;
	}

	@Override
	public int getMinRuntime() {
		return minRuntime;
	}

	public void setMinRuntime(int minRuntime) {
		this.minRuntime = minRuntime;
	}

	@Override
	public int getMinRuntimeRemaining() {
		return minRuntimeRemaining;
	}

	public void setMinRuntimeRemaining(int minRuntimeRemaining) {
		this.minRuntimeRemaining = minRuntimeRemaining;
	}

	@Override
	public int getActivePower() {
		return activePower;
	}

	public void setActivePower(int activePower) {
		this.activePower = activePower;
	}

	@Override
	public int getReactivePower() {
		return reactivePower;
	}

	public void setReactivePower(int reactivePower) {
		this.reactivePower = reactivePower;
	}

	@Override
	public int getThermalPower() {
		return thermalPower;
	}

	public void setThermalPower(int thermalPower) {
		this.thermalPower = thermalPower;
	}

}
