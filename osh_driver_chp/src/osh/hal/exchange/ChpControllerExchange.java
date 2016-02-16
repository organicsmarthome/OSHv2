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

import osh.hal.exchange.HALControllerExchange;

/**
 * 
 * @author Ingo Mauser
 *
 */
public class ChpControllerExchange 
				extends HALControllerExchange {
	
	private boolean stopGenerationFlag;
	private boolean electricityRequest;
	private boolean heatingRequest;

	
	/**
	 * Constructor
	 */
	public ChpControllerExchange(UUID deviceID, Long timestamp, boolean stopGenerationFlag, boolean electricityRequest, boolean heatingRequest) {
		super(deviceID, timestamp);
		
		this.stopGenerationFlag = stopGenerationFlag;
		this.electricityRequest = electricityRequest;
		this.heatingRequest = heatingRequest;
	}

	
	public boolean isStopGenerationFlag() {
		return stopGenerationFlag;
	}

	public void setStopGenerationFlag(boolean stopGenerationFlag) {
		this.stopGenerationFlag = stopGenerationFlag;
	}

	public boolean isElectricityRequest() {
		return electricityRequest;
	}

	public void setElectricityRequest(boolean electricityRequest) {
		this.electricityRequest = electricityRequest;
	}

	public boolean isHeatingRequest() {
		return heatingRequest;
	}

	public void setHeatingRequest(boolean heatingRequest) {
		this.heatingRequest = heatingRequest;
	}
	
}
