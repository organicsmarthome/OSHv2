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

import osh.core.exceptions.OSHException;

/**
 * Interface for life-cycle-methods
 * necessary for the participation on the life-cycle-manager process
 *@author florian.allerding@kit.edu
 *@category smart-home 
 *
 */
public interface ILifeCycleListener  {

	/**
	 * invoked when the system starts running
	 * ...in case of use please override
	 */
	public void onSystemRunning() throws OSHException;
	
	/**
	 * invoked when the system is going down
	 * in case of a simulation it might be usefull when you want to write your data...
	 * ...in case of use please override
	 */
	public void onSystemShutdown() throws OSHException;
	
	/**
	 * invoked when the complete controllerbox, the HAL and all the drivers are up
	 * ...in case of use please override 
	 */
	public void onSystemIsUp() throws OSHException;
	
	/**
	 * invoked when the system has to make a pause...
	 * ...in case of use please override
	 */
	public void onSystemHalt() throws OSHException;
	
	/**
	 * invoked when the system resumes from the pause
	 * ...in case of use please override
	 */
	public void onSystemResume() throws OSHException;
	
	/**
	 * invoked on a critical error to create perhaps a post mortem image for debugging
	 * ...in case of use please override
	 */
	public void onSystemError() throws OSHException;
}
