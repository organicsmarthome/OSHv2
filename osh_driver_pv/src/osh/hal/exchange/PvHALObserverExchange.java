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

import osh.datatypes.hal.interfaces.electricity.IHALElectricCurrentDetails;
import osh.datatypes.hal.interfaces.electricity.IHALElectricPowerDetails;
import osh.datatypes.hal.interfaces.electricity.IHALElectricVoltageDetails;
import osh.datatypes.registry.driver.details.energy.ElectricPowerDriverDetails;
import osh.hal.exchange.HALObserverExchange;


/**
 * 
 * @author Ingo Mauser
 *
 */
public class PvHALObserverExchange 
				extends HALObserverExchange
				implements 	IHALElectricCurrentDetails,
							IHALElectricPowerDetails,
							IHALElectricVoltageDetails {
	
	// ### IHALElectricCurrentDetails ###
	private double current;
	
	// ### IHALElectricPowerDetails ###
	private int activePower;
	private int reactivePower;

	// ### IHALElectricVoltageDetails ###
	private double voltage;
	
	
	/**
	 * CONSTRUCTOR 1
	 * @param deviceID
	 * @param timestamp
	 */
	public PvHALObserverExchange(UUID deviceID, Long timestamp) {
		this(deviceID, timestamp, null);
	}
	
	/**
	 * CONSTRUCTOR 2
	 * @param deviceID
	 * @param timestamp
	 * @param powerDetails
	 */
	public PvHALObserverExchange(UUID deviceID, Long timestamp, ElectricPowerDriverDetails powerDetails) {
		super(deviceID, timestamp);
		
		setPowerDetails(powerDetails);
	}

	
	public void setPowerDetails(ElectricPowerDriverDetails powerDetails) {
		if (powerDetails != null) {
			synchronized (powerDetails) {
				this.activePower = (int) Math.round(powerDetails.getActivePower());
				this.reactivePower = (int) Math.round(powerDetails.getReactivePower());
			}
		}
		else {
			this.activePower = 0;
			this.reactivePower = 0;
		}
	}

	public double getVoltage() {
		return voltage;
	}

	public void setVoltage(double voltage) {
		this.voltage = voltage;
	}

	public double getCurrent() {
		return current;
	}

	public void setCurrent(double current) {
		this.current = current;
	}

	public int getActivePower() {
		return activePower;
	}

	public void setActivePower(int activePower) {
		this.activePower = activePower;
	}

	public int getReactivePower() {
		return reactivePower;
	}

	public void setReactivePower(int reactivePower) {
		this.reactivePower = reactivePower;
	}

}
