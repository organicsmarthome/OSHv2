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
 * @author Florian Allerding, Kaibin Bao, Ingo Mauser, Till Schuberth
 *
 */
public class ESHLThermalControllerExchange extends HALControllerExchange {
	
	private boolean switchElectricalHeating;
	private boolean switchGasHeating;
	
	
	/**
	 * CONSTRUCTOR
	 */
	public ESHLThermalControllerExchange(UUID deviceID, Long timestamp) {
		super(deviceID, timestamp);
		//NOTHING
	}

	
	public boolean isSwitchElectricalHeating() {
		return switchElectricalHeating;
	}
	
	public void setSwitchElectricalHeating(boolean switchElectricalHeating) {
		this.switchElectricalHeating = switchElectricalHeating;
	}
	
	public boolean isSwitchGasHeating() {
		return switchGasHeating;
	}
	
	public void setSwitchGasHeating(boolean switchGasHeating) {
		this.switchGasHeating = switchGasHeating;
	}

}
