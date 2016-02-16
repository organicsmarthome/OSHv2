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

import java.util.ArrayList;
import java.util.UUID;

import osh.configuration.OSHParameterCollection;
import osh.core.IOSH;
import osh.datatypes.VirtualCommodity;
import osh.datatypes.limit.PowerLimitSignal;
import osh.datatypes.limit.PriceSignal;
import osh.hal.HALComDriver;
import osh.hal.exchange.IHALExchange;
import osh.simulation.exception.SimulationSubjectException;
import osh.simulation.screenplay.SubjectAction;

import osh.utils.time.TimeConversion;

/**
 * 
 * @author Florian Allerding, Kaibin Bao, Till Schuberth, Ingo Mauser
 *
 */
public abstract class SimulationComDriver extends HALComDriver implements ISimulationSubject {

	private ISimulationActionLogger simlogger = null;
	
	private ArrayList<SubjectAction> actions;
	private SimulationEngine simulationEngine;
	private long currentTimeTick;
	

	
	
	/**
	 * CONSTRUCTOR
	 * @param controllerbox
	 * @param deviceID
	 * @param driverConfig
	 * @throws SimulationSubjectException
	 */
	public SimulationComDriver(
			IOSH controllerbox, 
			UUID deviceID,
			OSHParameterCollection driverConfig)
			throws SimulationSubjectException {
		super(controllerbox, deviceID, driverConfig);
		actions = new ArrayList<SubjectAction>();
		currentTimeTick = controllerbox.getTimer().getUnixTime();
		

	}
	
	
	
	@Override
	public void onSimulationPreTickHook() {
		// do nothing
		
	}
	
	@Override
	public void onSimulationPostTickHook() {
		// do nothing
		
	}



	@Override
	public void flushActions() {
		actions.clear();
	}

	@Override
	public ArrayList<SubjectAction> getActions() {
		return actions;
	}

	@Override
	public void setAction(SubjectAction action) {
		actions.add(action);
		
	}

	@Override
	public ISimulationSubject getAppendingSubject(UUID SubjectID) {
		ISimulationSubject _simSubject = null;
		//ask the simulation engine...
		_simSubject = this.simulationEngine.getSimulationSubjectByID(SubjectID);
		
		return _simSubject;
	}

	@Override
	public void setSimulationEngine(SimulationEngine simulationEngine) {
		this.simulationEngine = simulationEngine;
	}
	
	protected SimulationEngine getSimulationEngine() {
		return simulationEngine;
	}

	@Override
	public void triggerSubject() {
		currentTimeTick = getOSH().getTimer().getUnixTime();
		
		//invoke for announcement...
		onNextTimeTick();
		
		//if we have something to do...
		//...check if you have to perform an action for this timetick
		
		if ( !actions.isEmpty() ) {
			if ( actions.get(0).getTick() == currentTimeTick ) {
				SubjectAction action = actions.get(0);
				if (simlogger != null) {
					simlogger.logAction(action);
				}
				performNextAction(action);
				actions.remove(0);
			}
		}
		
	}
	
	@Override
	public void onSimulationIsUp() {
		//NOTHING
	}

	@Override
	public void onNextTimeTick() {
		//NOTHING
	}

	@Override
	public void performNextAction(SubjectAction nextAction) {
		//NOTHING
	}

	@Override
	public final UUID getDeviceID() {
		return super.getDeviceID();
	}

	@Override
	public void updateDataFromComManager(IHALExchange exchangeObject) {
		//NOTHING
	}
	
	
	/** Please use only for logging stuff */
	public PriceSignal getPriceSignal(VirtualCommodity c) {
		return null;
	}
	
	/** Please use only for logging stuff */
	public PowerLimitSignal getPowerLimitSignal(VirtualCommodity c) {
		return null;
	}
	
	
	protected ISimulationActionLogger getSimlogger() {
		return simlogger;
	}

	public void setSimulationActionLogger(ISimulationActionLogger simulationLogger) {
		this.simlogger = simulationLogger;
	}

}
