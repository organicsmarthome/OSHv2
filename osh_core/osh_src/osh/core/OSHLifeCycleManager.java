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

package osh.core;

import osh.ILifeCycleListener;
import osh.LifeCycleStates;
import osh.core.exceptions.OSHException;
import osh.hal.HALManager;

public class OSHLifeCycleManager {

	private OCManager controllerBoxManager;
	private LifeCycleStates currentState;
	private OSHGlobalLogger globalLogger;
	
	public OSHLifeCycleManager(OCManager controllerBoxManager, HALManager halManager ) {
		this.controllerBoxManager = controllerBoxManager;
		this.globalLogger = controllerBoxManager.getGlobalLogger();
		this.currentState = LifeCycleStates.ON_SYSTEM_INIT;
		
	}

	public void switchToLifeCycleState(LifeCycleStates nextState) throws OSHException{
		this.currentState = nextState;
		switch (nextState)
		{
			case ON_SYSTEM_INIT:
			{
				//NOTHING
				break;
			}
			case ON_SYSTEM_RUNNING:
			{
				for(ILifeCycleListener boxLifeCycle : this.controllerBoxManager.getLifeCycleMembers()){
					boxLifeCycle.onSystemRunning();
				}
				this.globalLogger.logInfo("...switching to SYSTEM_RUNNING");
				break;
			
			}
			case ON_SYSTEM_IS_UP:
			{
				for(ILifeCycleListener boxLifeCycle : this.controllerBoxManager.getLifeCycleMembers()){
					boxLifeCycle.onSystemIsUp();
				}
				this.globalLogger.logInfo("...switching to SYSTEM_IS_UP");
				break;
			}
			case ON_SYSTEM_SHUTDOWN:
			{
				for(ILifeCycleListener boxLifeCycle: this.controllerBoxManager.getLifeCycleMembers()){
					boxLifeCycle.onSystemShutdown();
				}
				this.globalLogger.logInfo("...switching to SYSTEM_SHUTDOWN");
				break;
			}
			case ON_SYSTEM_HALT:
			{
				for(ILifeCycleListener boxLifeCycle: this.controllerBoxManager.getLifeCycleMembers()){
					boxLifeCycle.onSystemHalt();
				}
				this.globalLogger.logInfo("...switching to SYSTEM_HALT");
				break;
			}
			case ON_SYSTEM_RESUME:
			{
				for(ILifeCycleListener boxLifeCycle: this.controllerBoxManager.getLifeCycleMembers()){
					boxLifeCycle.onSystemResume();
				}
				this.globalLogger.logInfo("...switching to SYSTEM_RESUME");
				break;
			}
			case ON_SYSTEM_ERROR:
			{
				for(ILifeCycleListener boxLifeCycle: this.controllerBoxManager.getLifeCycleMembers()){
					boxLifeCycle.onSystemError();
				}
				this.globalLogger.logInfo("...switching to SYSTEM_ERROR");
				break;
			}
		}
			
	}

	/**
	 * @return the currentState
	 */
	protected LifeCycleStates getCurrentState() {
		return currentState;
	}
}
