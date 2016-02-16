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

package osh.core.exceptions;

import osh.core.OSHLoggerCore;

/**
 * superclass for exceptions near the controllerbox/OSH
 *@author florian.allerding@kit.edu
 *@category smart-home ControllerBox
 *
 */
public class OSHException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OSHException() {
		super(" I'm sorry, Dave. I'm afraid I can't do that. ");
		if (OSHLoggerCore.cb_Main_Logger != null) {
			OSHLoggerCore.cb_Main_Logger.error("An unknown error near the Controller occured..." + "I'm sorry, Dave. I'm afraid I can't do that. ");
		}
	}

	public OSHException(String message) {
		super(" I'm sorry, Dave. I'm afraid I can't do that..... " + message);
		if (OSHLoggerCore.cb_Main_Logger != null) {
			OSHLoggerCore.cb_Main_Logger.error("An error near the Controller occured: " +message);
		}
	}

	public OSHException(Throwable cause) {
		super(" I'm sorry, Dave. I'm afraid I can't do that. ", cause);
		if (OSHLoggerCore.cb_Main_Logger != null) {
			OSHLoggerCore.cb_Main_Logger.error("An error near the Controller occured...");
		}
	}

	public OSHException(String message, Throwable cause) {
		super(" I'm sorry, Dave. I'm afraid I can't do that..... " + message, cause);
		if (OSHLoggerCore.cb_Main_Logger != null) {
			OSHLoggerCore.cb_Main_Logger.error("An error near the Controller occured: " +message);
		}
	}

}
