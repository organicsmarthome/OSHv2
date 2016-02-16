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

package osh.simulation.exception;

import osh.core.OSHLoggerCore;

/**
 * Exception superclass for the simulation core
 *@author florian.allerding@kit.edu
 *@category smart-home ControllerBox Simulation
 *
 */
public class SimulationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SimulationException() {
		OSHLoggerCore.cb_Main_Logger.error("An unknown error near the Simulation occured...");
	}

	public SimulationException(String message) {
		super(message);
		OSHLoggerCore.cb_Main_Logger.error("An error near the Simulation occured: " +message);
	}

	public SimulationException(Throwable cause) {
		super(cause);
		OSHLoggerCore.cb_Main_Logger.error("An error near the Simulation occured: " +cause.getStackTrace());
	}

	public SimulationException(String message, Throwable cause) {
		super(message, cause);
		OSHLoggerCore.cb_Main_Logger.error("An error near the Simulation occured: " +cause.getStackTrace());
	}

}
