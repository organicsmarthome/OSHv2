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

package osh;

import osh.core.IOSH;
import osh.core.OSHGlobalLogger;
import osh.core.OSHRandomGenerator;
import osh.hal.HALRealTimeDriver;

/**
 * Super class for all components managed by the OCManager or HALManager
 * @author Florian Allerding
 */
public abstract class OSHComponent {
	
	protected IOSH theOrganicSmartHome;
	
	
	/**
	 * CONSTRUCTOR
	 * @param controllerbox
	 */
	public OSHComponent(IOSH controllerbox) {
		super();
		this.theOrganicSmartHome = controllerbox;
	}
	

	public OSHGlobalLogger getGlobalLogger() {
		return theOrganicSmartHome.getLogger();
	}

	public OSHRandomGenerator getRandomGenerator() {
		return theOrganicSmartHome.getRandomGenerator();
	}
	
	protected IOSH getOSH() {
		return theOrganicSmartHome;
	}

	public HALRealTimeDriver getTimer() {
		return theOrganicSmartHome.getTimer();
	}
	
}
