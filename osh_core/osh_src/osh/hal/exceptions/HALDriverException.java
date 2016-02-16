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

package osh.hal.exceptions;

/**
 * Exception class for the HAL-driver
 * @author florian.allerding@kit.edu
 * @category smart-home ControllerBox HAL
 */
public class HALDriverException extends HALException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HALDriverException() {
		super();
	}

	public HALDriverException(String message) {
		super(message);
	}

	public HALDriverException(Throwable cause) {
		super(cause);
	}

	public HALDriverException(String message, Throwable cause) {
		super(message, cause);
	}

}
