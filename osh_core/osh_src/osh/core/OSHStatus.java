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

import osh.LifeCycleStates;

/**
 * 
 * @author Florian Allerding, Kaibin Bao, Till Schuberth, Ingo Mauser
 *
 */
public class OSHStatus implements IOSHStatus {
	
	private OSHLifeCycleManager lifeCycleManager;
	
	private String runID;
	private String configurationID;
	
	private Boolean isSimulation;
	
	private Boolean isVirtual;
	
	boolean showSolverDebugMessages;
	
	
	//TODO [IMA]: find better solution (this is a bad hack)
	private int voluntarySignalAcceptationCounter = 0;
	private int voluntarySignalRejectionCounter = 0;
	

	@Override
	public String getRunID() {
		return runID;
	}
	
	public void setRunID(String runID) {
		this.runID = runID;
	}
	
	@Override
	public String getConfigurationID() {
		return configurationID;
	}

	public void setConfigurationID(String configurationID) {
		this.configurationID = configurationID;
	}

	@Override
	public boolean isSimulation() {
		return isSimulation;
	}
	
	public void setIsSimulation(boolean isSimulation) {
		this.isSimulation = isSimulation;
	}
	
	@Override
	public boolean isRunningVirtual() {
		return isVirtual;
	}

	public void setVirtual(boolean virtual) {
		this.isVirtual = virtual;
	}


	
	@Override
	public boolean getShowSolverDebugMessages() {
		return showSolverDebugMessages;
	}
	
	
	/**
	 * @return the currentLifeCycleState
	 */
	@Override
	public LifeCycleStates getCurrentLifeCycleState() {
		return lifeCycleManager.getCurrentState();
	}

	/**
	 * @param lifeCycleManager the lifeCycleManager to set
	 */
	public OSHLifeCycleManager getLifeCycleManager() {
		return lifeCycleManager;
	}

	/**
	 * @param lifeCycleManager the lifeCycleManager to set
	 */
	public void setLifeCycleManager(
			OSHLifeCycleManager lifeCycleManager) {
		this.lifeCycleManager = lifeCycleManager;
	}
	
	public void voluntarySignalAccepted() {
		voluntarySignalAcceptationCounter++;
	}
	
	public void voluntarySignalRejected() {
		voluntarySignalRejectionCounter++;
	}

	public int getVoluntarySignalAcceptationCounter() {
		return voluntarySignalAcceptationCounter;
	}

	public int getVoluntarySignalRejectionCounter() {
		return voluntarySignalRejectionCounter;
	}

	


}
