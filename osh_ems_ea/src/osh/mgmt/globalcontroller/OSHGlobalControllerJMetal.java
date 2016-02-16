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

package osh.mgmt.globalcontroller;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import osh.configuration.OSHParameterCollection;
import osh.core.IOSHOC;
import osh.core.OSHRandomGenerator;
import osh.core.com.interfaces.IEventReceiver;
import osh.core.com.interfaces.IHasState;
import osh.core.exceptions.OSHException;
import osh.core.oc.GlobalController;
import osh.core.oc.LocalController;
import osh.datatypes.VirtualCommodity;
import osh.datatypes.ea.Schedule;
import osh.datatypes.limit.PowerLimitSignal;
import osh.datatypes.limit.PriceSignal;
import osh.datatypes.registry.EventExchange;
import osh.datatypes.registry.StateChangedExchange;
import osh.datatypes.registry.globalcontroller.EASolutionCommandExchange;
import osh.datatypes.registry.globalobserver.EpsPlsStateExchange;
import osh.datatypes.registry.globalobserver.ScheduleStateExchange;
import osh.datatypes.registry.observer.ControllableEAProblemPartExchange;
import osh.datatypes.registry.observer.EAProblemPartExchange;
import osh.datatypes.registry.oc.details.utility.EpsStateExchange;
import osh.datatypes.registry.oc.details.utility.PlsStateExchange;
import osh.hal.exchange.xmlannot.ScheduleDebugExchange;
import osh.mgmt.globalcontroller.JMetal.Fitness;
import osh.mgmt.globalcontroller.JMetal.IFitness;
import osh.mgmt.globalcontroller.JMetal.JMetalSolverGA;
import osh.mgmt.globalcontroller.JMetal.JMetalSolverGA.GAParameters;
import osh.mgmt.globalobserver.OSHGlobalObserver;

/**
 * 
 * @author Florian Allerding, Kaibin Bao, Ingo Mauser, Till Schuberth
 *
 */
public class OSHGlobalControllerJMetal extends GlobalController implements IEventReceiver, IHasState {

	private OSHGlobalObserver oshGlobalObserver;
	
	private HashMap<VirtualCommodity,PriceSignal> priceSignals;
	private HashMap<VirtualCommodity,PowerLimitSignal> powerLimitSignals;
	
	private int epsOptimizationObjective;
	private int plsOptimizationObjective;
	private double overLimitFactor;
	
	private long lasttimeSchedulingStarted;
	private OSHRandomGenerator eaRandomGenerator = new OSHRandomGenerator();
	private GAParameters gaparameters = new GAParameters();
	
	
	/**
	 * CONSTRUCTOR
	 * @param controllerbox
	 * @param configurationParameters
	 */
	public OSHGlobalControllerJMetal(
			IOSHOC controllerbox,
			OSHParameterCollection configurationParameters) {
		super(controllerbox, configurationParameters);

		this.priceSignals = new HashMap<>();
		this.powerLimitSignals = new HashMap<>();
		
		try {
			this.overLimitFactor = Double.valueOf(this.configurationParameters.getParameter("overlimitfactor"));
		}
		catch (Exception e) {
			this.overLimitFactor = 5;
			getGlobalLogger().logWarning("Can't get overlimitFactor, using the default value: " + this.overLimitFactor);
		}
		
		try {
			this.epsOptimizationObjective = Integer.valueOf(this.configurationParameters.getParameter("epsoptimizationobjective"));
		}
		catch (Exception e) {
			this.epsOptimizationObjective = 0;
			getGlobalLogger().logWarning("Can't get epsOptimizationObjective, using the default value: " + this.epsOptimizationObjective);
		}		
		
		try {
			this.plsOptimizationObjective = Integer.valueOf(this.configurationParameters.getParameter("plsoptimizationobjective"));
		}
		catch (Exception e) {
			this.plsOptimizationObjective = 1;
			getGlobalLogger().logWarning("Can't get plsOptimizationObjective, using the default value: " + this.plsOptimizationObjective);
		}	
		
		try {
			this.gaparameters.numEvaluations = Integer.valueOf(this.configurationParameters.getParameter("gaNumEvaluations"));
		}
		catch (Exception e) {
			getGlobalLogger().logError("Can't get parameter gaNumEvaluations, using the default value " + this.gaparameters.numEvaluations);
		}
		
		try {
			this.gaparameters.popSize = Integer.valueOf(this.configurationParameters.getParameter("gaPopulationSize"));
		}
		catch (Exception e) {
			getGlobalLogger().logError("Can't get parameter gaPopulationSize, using the default value " + this.gaparameters.popSize);
		}
		
		try {
			this.gaparameters.crossoverProbability = Double.valueOf(this.configurationParameters.getParameter("gaCrossoverProbability"));
		} 
		catch (Exception e) {
			getGlobalLogger().logError("Can't get parameter gaCrossoverProbability, using the default value: " + this.gaparameters.crossoverProbability);
		}
		
		try {
			this.gaparameters.mutationProbability = Double.valueOf(this.configurationParameters.getParameter("gaMutationProbability"));
		} 
		catch (Exception e) {
			getGlobalLogger().logError("Can't get parameter gaMutationProbability, using the default value: " + this.gaparameters.mutationProbability);
		}
		
	}
	
	
	@Override
	public void onSystemIsUp() throws OSHException {
		super.onSystemIsUp();
		
		// safety first...
		if ( getGlobalObserver() instanceof OSHGlobalObserver ) {
			oshGlobalObserver = (OSHGlobalObserver) getGlobalObserver();
		} 
		else {
			throw new OSHException("this global controller only works with global observers of type " + OSHGlobalObserver.class.getName());
		}
		
		this.getOSH().getTimer().registerComponent(this, 1);
		
		this.getOCRegistry().registerStateChangeListener(EpsStateExchange.class, this);
		this.getOCRegistry().registerStateChangeListener(PlsStateExchange.class, this);
		
		lasttimeSchedulingStarted = 0;
	}
	
	@Override
	public void onQueueEventReceived(EventExchange ex) throws OSHException {
		if (ex instanceof StateChangedExchange) {
			StateChangedExchange exsc = (StateChangedExchange) ex;
			if (exsc.getType().equals(EpsStateExchange.class)) {
				EpsStateExchange eee = this.getOCRegistry().getState(EpsStateExchange.class, exsc.getStatefulentity());
				this.priceSignals = eee.getPriceSignals();
			}
			else if (exsc.getType().equals(PlsStateExchange.class)) {
				PlsStateExchange eee = this.getOCRegistry().getState(PlsStateExchange.class, exsc.getStatefulentity());
				this.powerLimitSignals = eee.getPowerLimitSignals();
			}
		}
		else {
			getGlobalLogger().logError("ERROR in " + this.getClass().getCanonicalName() + ": UNKNOWN EventExchange from UUID " + ex.getSender());
		}
	}

	
	@Override
	public void onNextTimePeriod() {
		long now = getTimer().getUnixTime();
		
		// check whether rescheduling is required and if so do rescheduling
		handleScheduling();
		
		// save current EPS and PLS to registry for logger
		{
			EpsPlsStateExchange epse = new EpsPlsStateExchange(
					getUUID(), 
					now, 
					priceSignals, 
					powerLimitSignals);
			
			this.getOCRegistry().setState(
					EpsPlsStateExchange.class,
					this,
					epse);
		}
	}
	
	/**
	 * decide if a (re-)scheduling is necessary
	 */
	private void handleScheduling() {
		
		boolean reschedulingRequired = false;
		
		//check if something has been changed:
		for (EAProblemPartExchange<?> problemPart : getOshGlobalObserver().getProblemParts()) {
			if (problemPart.getTimestamp() >= lasttimeSchedulingStarted) {
				reschedulingRequired = true;
			}
		}
		
		if (reschedulingRequired) {
			lasttimeSchedulingStarted = getTimer().getUnixTime();
			startScheduling();
		}
	}
	
	/**
	 * is triggered to 
	 */
	public void startScheduling() {
		HashMap<VirtualCommodity,PriceSignal> tempPriceSignals = new HashMap<>();
		HashMap<VirtualCommodity,PowerLimitSignal> tempPowerLimitSignals = new HashMap<>();
		
		//TODO: Check if necessary to synchronize full object (this)
		//TODO: Check why keySet and not entrySet
		
		// Cloning necessary, because of possible price signal changes during optimization
		synchronized (priceSignals) {
			for (VirtualCommodity vc : this.priceSignals.keySet()) {
				tempPriceSignals.put(vc, this.priceSignals.get(vc).clone());
			}
		}
		if ( tempPriceSignals.size() == 0 ) {
			getGlobalLogger().logError("No valid price signal available. Cancel scheduling!");
			return;
		}
		
		synchronized (powerLimitSignals) {
			for (VirtualCommodity vc : this.powerLimitSignals.keySet()) {
				tempPowerLimitSignals.put(vc, this.powerLimitSignals.get(vc).clone());
			}
		}
		if ( tempPowerLimitSignals.size() == 0 ) {
			getGlobalLogger().logError("No valid power limit signal available. Cancel scheduling!");
			return;
		}
		
		boolean showSolverDebugMessages = !getControllerBoxStatus().getShowSolverDebugMessages();
		
		// it is a good idea to use a specific random Generator for the EA, 
		// to make it comparable with other optimizers...
		JMetalSolverGA solver = new JMetalSolverGA(
				getGlobalLogger(), 
				eaRandomGenerator, 
				showSolverDebugMessages, 
				gaparameters,
				getTimer().getUnixTime());
		
		List<EAProblemPartExchange<?>> problemparts = getOshGlobalObserver().getProblemParts();
		List<BitSet> solutions;
		
		if (!getOshGlobalObserver().getAndResetProblempartChangedFlag()) {
			return; //nothing new, return
		}
		
		// debug print
		getGlobalLogger().logDebug("=== scheduling... ===");
		long now = getTimer().getUnixTime();

		for (EAProblemPartExchange<?> problem : problemparts) {
			problem.recalculateEncoding(now);
		}
		
		try {
			
			IFitness fitnessFunction = new Fitness(
					this.getGlobalLogger(),
					this.epsOptimizationObjective,
					this.plsOptimizationObjective,
					this.overLimitFactor);
			
			solutions = solver.getSolution(
					problemparts, 
					tempPriceSignals,
					tempPowerLimitSignals,
					getTimer().getUnixTime(),
					fitnessFunction);
			
			// Send current Schedule to GUI (via Registry to Com)
			this.getOCRegistry().setState(
					ScheduleStateExchange.class, this,
						new ScheduleStateExchange(
								getUUID(), 
								getTimer().getUnixTime(), 
								debugGetSchedules(problemparts, solutions)));
		} 
		catch (Exception e) {
			e.printStackTrace();
			getGlobalLogger().logError(e);
			return;
		}
		

		int min = Math.min(solutions.size(), problemparts.size());
		if (solutions.size() != problemparts.size()) getGlobalLogger().logDebug("jmetal: problem list and solution list don't have the same size");
		
		ScheduleDebugExchange debug = new ScheduleDebugExchange(
				getUUID(), getTimer().getUnixTime());
		for (int i = 0; i < min; i++) {
			EAProblemPartExchange<?> part = problemparts.get(i);
			LocalController lc = getLocalController(part.getDeviceId());
			BitSet bits = solutions.get(i);

			if (lc != null) {
				this.getOCRegistry().sendCommand(
							EASolutionCommandExchange.class, 
							part.transformToPhenotype(
									null, 
									part.getDeviceId(), 
									getTimer().getUnixTime(), 
									bits));
			} 
			else if (/* lc == null && */ part.getBitCount() > 0) {
				throw new NullPointerException("got a local part with used bits but without controller! (UUID: " + part.getDeviceId() + ")");
			}
			
			StringBuilder debugstr = new StringBuilder();
			debugstr.append(getTimer().getUnixTime() + ";");
			debugstr.append(part.getSender() + ";");
			debugstr.append(part.problemToString() + ";");
			if (part instanceof ControllableEAProblemPartExchange<?>) {
				debugstr.append(((ControllableEAProblemPartExchange<?>) part).solutionToString(bits));
			}
			debug.addString(part.getSender(), debugstr.toString());
		}
		
		getOCRegistry().sendEvent(ScheduleDebugExchange.class, debug);
		
		getGlobalLogger().logDebug("===    EA done    ===");
		
		//lasttimeScheduled = getTimer().getUnixTime();
	}
	
	private List<Schedule> debugGetSchedules(List<EAProblemPartExchange<?>> problemparts, List<BitSet> result) {
		int i = 0;
		List<Schedule> schedules = new ArrayList<Schedule>();
		
		for (EAProblemPartExchange<?> part : problemparts) {
			schedules.add(part.getSchedule(result.get(i)));
			i++;
		}
		return schedules;
	}

	@Override
	public UUID getUUID() {
		return getGlobalObserver().getAssignedOCUnit().getUnitID();
	}
	
	public OSHGlobalObserver getOshGlobalObserver() {
		return oshGlobalObserver;
	}

}
