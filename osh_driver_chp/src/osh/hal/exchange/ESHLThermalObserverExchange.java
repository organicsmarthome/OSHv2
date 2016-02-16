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

import osh.hal.exchange.HALDeviceObserverExchange;

/**
 * 
 * @author Florian Allerding, Kaibin Bao, Ingo Mauser, Till Schuberth
 *
 */
public class ESHLThermalObserverExchange extends HALDeviceObserverExchange {

	private double currentTempBottom;
	private double currentTempTop;
	
	private boolean electricHeatingState;
	private boolean gasHeatingState;
	
	private long minRuntimeRemaining;
	private double currentElectricPower;
	
	private int currentGasPower;
	private int currentThermalPower;
	private int currentReactivePower;
	

	/**
	 * CONSTRUCTOR
	 */
	public ESHLThermalObserverExchange(UUID deviceID, Long timestamp) {
		super(deviceID, timestamp);
	}
	
	
	public double getCurrentTempBottom() {
		return currentTempBottom;
	}
	
	public double getCurrentTempTop() {
		return currentTempTop;
	}
	
	public long getMinRuntimeRemaining() {
		return minRuntimeRemaining;
	}
	
	public boolean isElectricHeatingState() {
		return electricHeatingState;
	}

	public boolean isGasHeatingState() {
		return gasHeatingState;
	}
	
	public void setCurrentTempBottom(double currentTempBottom) {
		this.currentTempBottom = currentTempBottom;
	}
	
	public void setCurrentTempTop(double currentTempTop) {
		this.currentTempTop = currentTempTop;
	}
	
	public void setElectricHeatingState(boolean electricHeatingState) {
		this.electricHeatingState = electricHeatingState;
	}

	public void setGasHeatingState(boolean gasHeatingState) {
		this.gasHeatingState = gasHeatingState;
	}
	
	public void setMinRuntimeRemaining(long minRuntimeRemaining) {
		this.minRuntimeRemaining = minRuntimeRemaining;
	}
	
	public double getCurrentElectricPower() {
		return currentElectricPower;
	}
	
	public void setCurrentElectricPower(double currentpower) {
		this.currentElectricPower = currentpower;
	}
	
	public int getCurrentGasPower() {
		return currentGasPower;
	}
	
	public void setCurrentGasPower(int currentGasPower) {
		this.currentGasPower = currentGasPower;
	}

	public int getCurrentThermalPower() {
		return currentThermalPower;
	}

	public void setCurrentThermalPower(int currentThermalPower) {
		this.currentThermalPower = currentThermalPower;
	}

	public int getCurrentReactivePower() {
		return currentReactivePower;
	}

	public void setCurrentReactivePower(int currentReactivePower) {
		this.currentReactivePower = currentReactivePower;
	}
	
}
