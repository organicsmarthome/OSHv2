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

package osh.simulation;

import java.util.Collection;
import java.util.UUID;

import osh.simulation.exception.SimulationSubjectException;
import osh.simulation.screenplay.SubjectAction;

/**
 *@author florian.allerding@kit.edu
 *@category smart-home ControllerBox Simulation
 *
 */
public interface ISimulationSubject {
	
	/**
	 * is invoked by the simulation Engine on every time tick to synchronize the subjects
	 */
	public void triggerSubject() throws SimulationSubjectException;
	
	/**
	 * delete all actions from the list 
	 */
	public void flushActions();
	
	/**
	 * Sets an action for this simulation subject
	 * @param actions
	 */
	public void setAction(SubjectAction action);
	
	/**
	 * gets all actions for a subject
	 * @return
	 */
	public Collection<SubjectAction> getActions();
	
	/**
	 * is invoked by triggerSubject() on every (new) time tick to announce the subject
	 */
	public void onNextTimeTick() throws SimulationSubjectException;
	
	/**
	 * is invoked when the complete simulation environment has been set up 
	 */
	public void onSimulationIsUp() throws SimulationSubjectException;
	
	/**
	 * @param nextAction
	 * is invoked when the subject has to do the action "nextAction"
	 */
	public void performNextAction(SubjectAction nextAction);
	
	public void setSimulationEngine(SimulationEngine simulationEngine);

	public UUID getDeviceID();
	
	public ISimulationSubject getAppendingSubject(UUID SubjectID);

	public void setSimulationActionLogger(ISimulationActionLogger simulationLogger);
	
	/**
	 * is invoked before every simulation tick by the simulation engine
	 */
	public void onSimulationPreTickHook();

	/**
	 * is invoked after every simulation tick by the simulation engine
	 */
	public void onSimulationPostTickHook();

}
