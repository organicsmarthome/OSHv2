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

package osh.datatypes.registry.driver.details.appliance;

import java.util.UUID;

import osh.datatypes.en50523.EN50523DeviceState;
import osh.datatypes.registry.StateExchange;

/**
 * Generic Appliance driver details
 * (communication of device state)
 * @author Kaibin Bao, Ingo Mauser
 *
 */

public class GenericApplianceDriverDetails extends StateExchange {

	protected EN50523DeviceState state;
	protected String stateTextDE;
	
	
	/** for JAXB */
	@SuppressWarnings("unused")
	@Deprecated
	private GenericApplianceDriverDetails() {
		this(null, 0);
	};

	
	/**
	 * CONSTRUCTOR
	 * @param sender
	 * @param timestamp
	 */
	public GenericApplianceDriverDetails(
			UUID sender, 
			long timestamp) {
		super(sender, timestamp);
	}

	
	// ### GETTERS and SETTERS ###

	public EN50523DeviceState getState() {
		return state;
	}

	public void setState(EN50523DeviceState state) {
		this.state = state;
	}

	public String getStateTextDE() {
		return stateTextDE;
	}

	public void setStateTextDE(String stateTextDE) {
		this.stateTextDE = stateTextDE;
	}
	
	// ### OTHER STUFF ###

	@Override
	public GenericApplianceDriverDetails clone() {
		GenericApplianceDriverDetails _new = new GenericApplianceDriverDetails(getSender(), getTimestamp());
		_new.setState(state);
		_new.setStateTextDE(stateTextDE + "");
		return _new;
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( obj == null ) {
			return false;
		}
		if ( !(obj instanceof GenericApplianceDriverDetails) ) {
			return false;
		}
		
		GenericApplianceDriverDetails other = (GenericApplianceDriverDetails) obj;

		if (this.state == null) {
			if (other.state != null) {
				return false;
			}
		} else if ( !this.state.equals(other.state) ) {
			return false;
		}

		//TODO details are currently not relevant
		/*
		if(this.programExtras == null) {
			if(other.programExtras != null)
				return false;
			else
				return true;
		}
		
		return (this.programExtras.equals(other.programExtras)); */
		return true;
	}
	
	@Override
	public String toString() {
		return "ApplianceState: " + ((state==null)?"null":state.name());
	}
}
