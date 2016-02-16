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

/**
 * Exception class for the global simulation engine
 * @author ???
 * @category smart-home ControllerBox Simulation
 */
public class GlobalSimulationEngineException extends SimulationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GlobalSimulationEngineException() {
		super();
	}

	public GlobalSimulationEngineException(String message, Throwable cause) {
		super(message, cause);
	}

	public GlobalSimulationEngineException(String message) {
		super(message);
	}

	public GlobalSimulationEngineException(Throwable cause) {
		super(cause);
	}

}
