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



import osh.datatypes.registry.StateExchange;


/**
 * StateExchange for communication of DoF details
 * (from device)
 * @author Ingo Mauser
 *
 */

public class GenericApplianceDofDriverDetails extends StateExchange {

	/** DoF for initial scheduling */
	protected int appliance1stDof = 0;
	
	/** DoF for rescheduling */
	protected int appliance2ndDof = 0;
	
	
	/** for JAXB */
	@SuppressWarnings("unused")
	@Deprecated
	private GenericApplianceDofDriverDetails() {
		this(null, 0);
	};


	/**
	 * CONSTRUCTOR
	 */
	public GenericApplianceDofDriverDetails(
			UUID sender, 
			long timestamp) {
		super(sender, timestamp);
	}


	public int getAppliance1stDof() {
		return appliance1stDof;
	}

	public void setAppliance1stDof(int appliance1stDof) {
		this.appliance1stDof = appliance1stDof;
	}

	public int getAppliance2ndDof() {
		return appliance2ndDof;
	}

	public void setAppliance2ndDof(int appliance2ndDof) {
		this.appliance2ndDof = appliance2ndDof;
	}
	
}
