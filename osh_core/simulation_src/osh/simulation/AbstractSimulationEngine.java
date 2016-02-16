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

import java.util.HashSet;
import java.util.Set;

import osh.hal.HALRealTimeDriver;
import osh.hal.exceptions.HALException;
import osh.registry.DriverRegistry;
import osh.registry.OCRegistry;
import osh.registry.Registry;
import osh.simulation.exception.SimulationEngineException;


/**
 * 
 * @author Florian Allerding, Till Schuberth, Ingo Mauser
 *
 */
public abstract class AbstractSimulationEngine {

	protected HALRealTimeDriver timerdriver;
	
	protected OCRegistry ocRegistry;
	private DriverRegistry driverRegistry;
	
	private int currentSimulationTick = -1;
	private int simulationDuration;

	private Set<AbstractSimulationEngine> subengines = new HashSet<AbstractSimulationEngine>();
	
	
	// ## IMPORTANT GETTERS AND SETTERS ##

	// # TIMER DRIVER #
	public void assignTimerDriver(HALRealTimeDriver timerDriver) {
		this.timerdriver = timerDriver;
	}

	// # REGISTRIES #
	// OC REGISTRY
	public void assignOCRegistry(OCRegistry ocRegistry){
		this.ocRegistry = ocRegistry;
	}
	// DRIVER REGISTRY
	public void assignDriverRegistry(DriverRegistry driverRegistry){
		this.driverRegistry = driverRegistry;
	}
	
	// # SUB SIMULATION ENGINES #
	// add engine that depends on this engine
	public void addSubSimulationEngine(AbstractSimulationEngine simengine) {
		subengines.add(simengine);
	}
	// remove engine that depends on this engine
	public void removeSubSimulationEngine(AbstractSimulationEngine simengine) {
		subengines.remove(simengine);
	}
	
	
	// ## LOGIC ##
	
	
	/**
	 * will call every simulation Engine that the simulation setup is complete.
	 * So every ISimulationSubject will be notified, too
	 * @throws SimulationEngineException 
	 */
	public void notifySimulationIsUp() throws SimulationEngineException{
		// notify main engine that simulation is up
		this.notifyLocalEngineOnSimulationIsUp();
		// notify sub engines that simulation is up
		for(AbstractSimulationEngine simulationEngine: subengines){
			simulationEngine.notifySimulationIsUp();
		}
	}
	
	abstract protected void notifyLocalEngineOnSimulationIsUp() throws SimulationEngineException;
 
	/**
	 * 
	 * @param currentTick
	 * @param doSimulation if true, do a full simulation with a call to
	 * simulateNextTimeTick, if false, only update timerdriver and empty all
	 * queues.
	 * @throws SimulationEngineException
	 */
	private void internalSimulateNextTimeTick(int currentTick, boolean doSimulation) throws SimulationEngineException {
		//update realtimeDriver
		//doNextSimulationTickTimer:
		try {
			if (this.timerdriver != null) this.timerdriver.updateTimer(currentTick);
		} 
		catch (HALException e) {
			throw new SimulationEngineException(e);
		}

		//do the simulation in subengines
		if (doSimulation) {
			simulateNextTimeTick(currentTick);
			for (AbstractSimulationEngine e : subengines) {
				e.triggerEngine();
			}
		}

		//empty all queues
		boolean queueswereempty;
		do {
			queueswereempty = true;
			queueswereempty &= doSimulateNextTimeTickQueues(currentTick); // &= is AND
			for (AbstractSimulationEngine e : subengines) {
				queueswereempty &= e.doSimulateNextTimeTickQueues(currentTick);
			}
		} while (!queueswereempty);
	}
	
	
	/**
	 * this function is to be called from inside internalSimulateNextTimeTick.
	 * 
	 * @param currentTick
	 * @throws SimulationEngineException 
	 */
	private boolean doSimulateNextTimeTickQueues(int currentTick) throws SimulationEngineException {
		boolean allqueueswereempty = true;
		
		allqueueswereempty &= processRegistry(this.ocRegistry);
		allqueueswereempty &= processRegistry(this.driverRegistry);
		
		return allqueueswereempty;
	}

	private boolean processRegistry(Registry registry) throws SimulationEngineException {
		// empty all event/state queues
		if (registry != null) {
			registry.flushAllQueues();
		}
		else {
			System.out.println("ERROR: No Registry available!");
			System.exit(0); // shutdown
			return true;
		}
		
		return registry.areAllQueuesEmpty();
	}
	
	/**
	 * Simulate next time tick
	 * @throws SimulationEngineException
	 */
	protected abstract void simulateNextTimeTick(int currentTick) throws SimulationEngineException;

	/**
	 * start the simulation based on an external clock
	 * This function is deprecated, because I can't see that the function does
	 * what it should do. If I'm wrong, feel free to delete the tag.
	 * IMA: Simulation with external clock (timerDriver), e.g. combination of real house and simulated houses
	 * FIXME: unclear implementation.
	 * @throws SimulationEngineException 
	 */
	@Deprecated
	public void runSimulationByExternalClock(int startTime) throws SimulationEngineException {
		internalSimulateNextTimeTick(startTime, false);
	}
	
	/**
	 * Trigger the simulation by an external clock to simulate the next step
	 * @throws SimulationEngineException
	 */
	public void triggerEngine() throws SimulationEngineException {
		//next tick
		++this.currentSimulationTick;

		//simulate it
		internalSimulateNextTimeTick(currentSimulationTick, true);
	}
	
	/**
	 * start the simulation with a given numberOfTicks based on the internal clock
	 * @param numberOfTicks
	 * @throws SimulationEngineException
	 */
	public void runSimulation(int numberOfTicks) throws SimulationEngineException {
		simulationDuration = numberOfTicks;
		
		for (int currentTick = 0; currentTick < numberOfTicks; currentTick++ ) {
			internalSimulateNextTimeTick(currentTick, true);
		}
	
	}

	/**
	 * Reset the simulation timer to zero. Necessary when you want to run several simulations
	 * beginning at the same start time '0'
	 * @throws SimulationEngineException 
	 */
	public void resetSimulationTimer() throws SimulationEngineException {
		internalSimulateNextTimeTick(0, false);
	}
	
	/**
	 * You can set a specific start time for the simulation.
	 * Normally you don't need that
	 * @param startTimeTick
	 * @throws SimulationEngineException 
	 */
	public void setSimulationTimerTo(int startTimeTick) throws SimulationEngineException{
		internalSimulateNextTimeTick(startTimeTick, false);
	}
	
	public int getSimulationDuration() {
		return simulationDuration;
	}

}
