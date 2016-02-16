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
import java.util.Comparator;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import osh.configuration.OSHParameterCollection;
import osh.core.IOSH;
import osh.datatypes.Commodity;
import osh.hal.HALDeviceDriver;
import osh.hal.exceptions.HALException;
import osh.hal.exchange.HALControllerExchange;
import osh.simulation.exception.SimulationSubjectException;
import osh.simulation.screenplay.ScreenplayType;
import osh.simulation.screenplay.SubjectAction;

/**
 * Superclass for simulation subjects like simulated appliances or SmartMeters or ....
 * This class inherits from HALDeviceDriver. 
 * This is necessary for the capability of an integration into the controllerbox's HAL 
 * 
 * @author Florian Allerding, Kaibin Bao, Till Schuberth, Ingo Mauser
 * 
 */
public abstract class AbstractDeviceSimulationDriver extends HALDeviceDriver implements ISimulationSubject {

	// INNER CLASSES

	private Comparator<SubjectAction> actionComparator = new Comparator<SubjectAction>() {
		@Override
		public int compare(SubjectAction arg0, SubjectAction arg1) {
			return (int) ((arg0.getTick() - arg1.getTick()));
		}
	};
	
	// VARIABLES

	private SimulationEngine simulationEngine;
	private ISimulationActionLogger simulationActionLogger;
	
	private SortedSet<SubjectAction> actions;

	private HashMap<Commodity,Integer> powerLoadForCommodity;
	
	
	// screenplayType specific variables
		// screenplayType = 1 && DeviceClassification.APPLIANCE
		
	
	// Optimization Objective
	private Integer epsoptimizationobjective;
	
	/**
	 * CONSTRUCTOR
	 * @param controllerbox
	 * @param deviceID
	 * @param driverConfig
	 * @throws SimulationSubjectException
	 */
	public AbstractDeviceSimulationDriver(
			IOSH controllerbox, 
			UUID deviceID,
			OSHParameterCollection driverConfig) 
			throws SimulationSubjectException {
		super(controllerbox, deviceID, driverConfig);
		
		this.actions = new TreeSet<SubjectAction>(actionComparator);
		
		this.powerLoadForCommodity = new HashMap<>();
		for (Commodity c : Commodity.values()) {
			this.powerLoadForCommodity.put(c, 0);
		}
		
		// if DeviceClassification.APPLIANCE (but info at this point not yet available!)
		// all conditions after first && should NOT be necessary (but remain for safety reasons)
		if (driverConfig.getParameter("screenplaytype") != null) {
			
			ScreenplayType screenplayType = ScreenplayType.fromValue(driverConfig.getParameter("screenplaytype"));
			
			if (screenplayType == ScreenplayType.STATIC) {
				// screenplay is loaded from file...
			}
			else if (screenplayType == ScreenplayType.DYNAMIC) {
				// NOTHING here...
			}
			else {
				throw new RuntimeException("value \"screenplayType\" for variable \"screenplaytype\": unknown value!");
			}
		}
		else {
			throw new RuntimeException("variable \"screenplaytype\" : missing!");
		}
		
		// get EpsOptimizationObjective
		if (driverConfig.getParameter("epsoptimizationobjective") != null) {
			epsoptimizationobjective = Integer.valueOf(driverConfig.getParameter("epsoptimizationobjective"));
		}
	}
	
	
	@Override
	public void onSimulationPreTickHook() {
		//do nothing
		
	}
	
	@Override
	public void onSimulationPostTickHook() {
		//do nothing
		
	}
	
	
	@Override
	public void onSimulationIsUp() throws SimulationSubjectException {
		//NOTHING
	}

	@Override
	protected void onControllerRequest(HALControllerExchange controllerRequest) throws HALException {
		//NOTHING
	}
	
	
	
	
	
	/**
	 * Get the Power consumption / Load
	 * @param c Commodity
	 * @return power/load
	 */
	public Integer getPower(Commodity c) {
		return this.powerLoadForCommodity.get(c);
	}
	
	/**
	 * Set the Power consumption / load
	 * @param c Commodity
	 * @param power
	 */
	protected void setPower(Commodity c, int power) {
		this.powerLoadForCommodity.put(c, power);
	}


	/**
	 * delete all actions from the list 
	 */
	@Override
	public void flushActions(){
		actions.clear();
	}
	
	/**
	 * gets all actions for a subject
	 * @return
	 */
	@Override
	public Collection<SubjectAction> getActions() {
		return actions;
	}
	
	/**
	 * Get another subject depending on this subject perhaps to tell him to do something...
	 * @param SubjectID
	 */
	@Override
	public ISimulationSubject getAppendingSubject(UUID SubjectID){
		ISimulationSubject _simSubject = null;
		
		//ask the simulation engine...
		_simSubject = this.simulationEngine.getSimulationSubjectByID(SubjectID);
		
		return _simSubject;
	}

	/**
	 * is invoked on every (new) time tick to announce the subject
	 */
	@Override
	public abstract void onNextTimeTick();
		
	/**
	 * @param nextAction
	 * is invoked when the subject has to do the action "nextAction"
	 */
	@Override
	public abstract void performNextAction(SubjectAction nextAction);
	
	/**
	 * Sets an action for this simulation subject
	 * @param actions
	 */
	@Override
	public void setAction(SubjectAction action) {
		actions.add(action);
	}
	
	
	protected SimulationEngine getSimulationEngine() {
		return this.simulationEngine;
	}
	
	@Override
	public void setSimulationEngine(SimulationEngine simulationEngine){
		this.simulationEngine = simulationEngine;
	}
	
	
	protected ISimulationActionLogger getSimulationActionLogger() {
		return simulationActionLogger;
	}

	@Override
	public void setSimulationActionLogger(ISimulationActionLogger simulationLogger) {
		this.simulationActionLogger = simulationLogger;
	}
	
	
	public Integer getEpsoptimizationobjective() {
		return epsoptimizationobjective;
	}


	@Override
	public void triggerSubject() {
		//invoke for announcement...
		onNextTimeTick();
		
		long currentTimeTick = getTimer().getUnixTime();
		
		while ( actions.size() > 0 && actions.first().getTick() <= currentTimeTick ) {
			// delete earlier entries (tbd)
			if ( actions.first().getTick() < currentTimeTick ) {
				actions.remove(actions.first());
				continue;
			}
			
			
			// perform next action
			SubjectAction action = actions.first();
			if (simulationActionLogger != null) simulationActionLogger.logAction(action);
			performNextAction(action);
			
			// remove action entry
			try {
				actions.remove(actions.first());
			} 
			catch (Exception ex) {
				throw new RuntimeException("actions name: " + actions.getClass().getCanonicalName(), ex);
			}
		}
	}
	

	
	
	
}
