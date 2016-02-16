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

package osh.driver.simulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.math3.distribution.BinomialDistribution;

import osh.simulation.exception.SimulationSubjectException;
import osh.simulation.screenplay.ActionParameter;
import osh.simulation.screenplay.ActionParameters;
import osh.simulation.screenplay.ActionType;
import osh.simulation.screenplay.PerformAction;
import osh.simulation.screenplay.ScreenplayType;
import osh.simulation.screenplay.SubjectAction;
import osh.simulation.virtualdevicesdata.ProfileTick;
import osh.configuration.OSHParameterCollection;
import osh.core.IOSH;
import osh.core.OSHRandomGenerator;
import osh.core.exceptions.OSHException;
import osh.datatypes.Commodity;
import osh.datatypes.appliance.LoadProfileCompressionTypes;
import osh.datatypes.appliance.PowerProfileTick;
import osh.datatypes.appliance.SparseLoadProfile;
import osh.datatypes.dof.ApplianceDofStateExchange;
import osh.datatypes.en50523.EN50523DeviceState;
import osh.datatypes.en50523.EN50523OIDExecutionOfACommandCommands;
import osh.hal.exchange.HALControllerExchange;
import osh.hal.exchange.MieleApplianceControllerExchange;
import osh.hal.exchange.MieleApplianceObserverExchange;

/**
 * 
 * @author Florian Allerding, Kaibin Bao, Till Schuberth, Ingo Mauser
 *
 */
public class MieleApplianceSimulationDriver extends MieleGenericApplianceSimulationDriver {
	
	private LoadProfileCompressionTypes compressionType = LoadProfileCompressionTypes.DISCONTINUITIES;
	private EN50523DeviceState en50523State;
	private HashMap<Commodity,ArrayList<PowerProfileTick>> currentPowerProfiles;
	private boolean useRandomDof;
	private int totalNumberOfRuns = 0;

	
	/**
	 * CONSTRUCTOR
	 * @param controllerbox
	 * @param deviceID
	 * @param driverConfig
	 * @throws SimulationSubjectException
	 */
	public MieleApplianceSimulationDriver(
			IOSH controllerbox,
			UUID deviceID, 
			OSHParameterCollection driverConfig) 
			throws SimulationSubjectException {
		super(controllerbox, deviceID, driverConfig);

		setSystemState(false);
		
		isIntelligent = deviceProfile.isIntelligent();
		hasProfile = deviceProfile.isHasProfile();
		en50523State = EN50523DeviceState.OFF;
		
		SparseLoadProfile temp = SparseLoadProfile.convertToSparseProfile(
				this.generatePowerProfile(),
				compressionType,
				1);
		middleOfPowerConsumption = (int) temp.getMiddleOfPowerConsumption(Commodity.ACTIVEPOWER);
		activePowerSumPerRun = (int) temp.getWork(Commodity.ACTIVEPOWER);
		
		// get parameter about random dof
		if (driverConfig.getParameter("randomdof") != null) {
			this.useRandomDof = Boolean.valueOf(driverConfig.getParameter("randomdof"));
		}
		else {
			throw new RuntimeException("parameter \"randomdof\" : missing!");
		}
	}


	@Override
	protected void onProcessingTimeTick() {
		//notify the controllerbox
		long now = getTimer().getUnixTime();

		MieleApplianceObserverExchange observerObj
			= new MieleApplianceObserverExchange(
					this.getDeviceID(), 
					now);

		observerObj.setEn50523DeviceState(en50523State);
		
		switch (en50523State) {
		case PROGRAMMED: {
			observerObj.setActivePower(0);
			observerObj.setReactivePower(0);
			observerObj.setExpectedLoadProfiles(currentPowerProfiles);
		}
			break;

		case RUNNING: {
			observerObj.setActivePower(this.getPower(Commodity.ACTIVEPOWER));
			observerObj.setReactivePower(this.getPower(Commodity.REACTIVEPOWER));
			observerObj.setExpectedLoadProfiles(currentPowerProfiles);
			observerObj.setProgramRemainingTime(
					currentPowerProfiles.get(Commodity.ACTIVEPOWER).size() - (int) (getTimer().getUnixTime() - programStart));
		}
			break;

		default: {
			observerObj.setActivePower(this.getPower(Commodity.ACTIVEPOWER));
			observerObj.setReactivePower(this.getPower(Commodity.REACTIVEPOWER));
			observerObj.setDeviceID(getDeviceID());
			observerObj.setProgramRemainingTime(0);
		}
			break;
		}


		this.notifyObserver(observerObj);
	}

	@Override
	protected void onControllerRequest(HALControllerExchange controllerRequest) {
		MieleApplianceControllerExchange controllerExchange = (MieleApplianceControllerExchange) controllerRequest;
		
		if (controllerExchange.getApplianceCommand() == EN50523OIDExecutionOfACommandCommands.START){
			setSystemState(true);
			en50523State = EN50523DeviceState.RUNNING;
		}
		
		if (controllerExchange.getApplianceCommand() == EN50523OIDExecutionOfACommandCommands.STOP){
			setSystemState(false);
			en50523State = EN50523DeviceState.OFF;
		}

	}

	
	private HashMap<Commodity,ArrayList<PowerProfileTick>> generatePowerProfile() {
		
		if (deviceProfile == null) {
			return null;
		}
		
		HashMap<Commodity,ArrayList<PowerProfileTick>> profiles = new HashMap<>();
		
		int count = 0;
		
		// iterate time ticks
		for ( ProfileTick profileTick : deviceProfile.getProfileTicks().getProfileTick() ) {
			
			// iterate commodities
			for (int i = 0; i < profileTick.getLoad().size(); i++) {
				
				Commodity currentCommodity = Commodity.fromString(profileTick.getLoad().get(i).getCommodity());
				ArrayList<PowerProfileTick> _pwrProfileList = profiles.get(currentCommodity);
				
				if (_pwrProfileList == null) {
					_pwrProfileList = new ArrayList<PowerProfileTick>();
					profiles.put(currentCommodity, _pwrProfileList);
				}
				
				PowerProfileTick _pwrPro = new PowerProfileTick();
				_pwrPro.commodity = currentCommodity;
				_pwrPro.timeTick = count;
				_pwrPro.load = profileTick.getLoad().get(i).getValue();
				_pwrProfileList.add(_pwrPro);
			}
			
			++count;
		}
		
		return profiles;
	}
	
	@Override
	public void performNextAction(SubjectAction nextAction){
		// IMPORTANT: DO DoF-Action FIRST!
		
		// DoF-Action
		if (nextAction.getActionType() == ActionType.USER_ACTION) {
			int newDof = 0;
			
			newDof = Integer.valueOf(nextAction.getPerformAction().get(0).getActionParameterCollection().get(0).getParameter().get(0).getValue());
			
			ApplianceDofStateExchange adse = new ApplianceDofStateExchange(getDeviceID(), getTimer().getUnixTime());
			adse.setFirstDof(newDof);
			adse.setSecondDof(device2ndDof);
			getDriverRegistry().setStateOfSender(ApplianceDofStateExchange.class, adse);
		}
		
		//if the next state is true the i-appliance is filled up an is now able to run
		else if (nextAction.isNextState() && nextAction.getActionType() == ActionType.I_DEVICE_ACTION) {
			String oldState = en50523State.toString();
			
			if (this.isControllable()) {
				en50523State = EN50523DeviceState.PROGRAMMED;
			}
			else {
				setSystemState(true);
				en50523State = EN50523DeviceState.RUNNING;
			}
			
			getGlobalLogger().logDebug("VirtualMieleDevice " + getDeviceType() + " performAction: change from " + oldState + " to " + en50523State.toString() + " (" + getDeviceID() + ")");
			
			currentPowerProfiles = generatePowerProfile();
			
			//  Workaround to reduce calculation time (by IMA)
			if (getEpsoptimizationobjective() != null) {
				if (getEpsoptimizationobjective() == 0
						|| getEpsoptimizationobjective() == 1
						|| getEpsoptimizationobjective() == 2
						|| getEpsoptimizationobjective() == 3
						|| getEpsoptimizationobjective() == 4) {
					currentPowerProfiles.remove(Commodity.REACTIVEPOWER);
				}
			}
			
		}
		else if (!nextAction.isNextState() && nextAction.getActionType() == ActionType.I_DEVICE_ACTION) {
			getGlobalLogger().logDebug("VirtualMieleDevice " + getDeviceType() + " performAction: change from " + en50523State.toString() + " to OFF (" + getDeviceID() + ")");
			en50523State = EN50523DeviceState.OFF;
			setSystemState(false);
		}
	}
	

	@Override
	protected void onActiveTimeTick() {
		//NOTHING
	}

	@Override
	protected void onProgramEnd() {
		en50523State = EN50523DeviceState.OFF;
	}

	@Override
	protected void generateDynamicDailyScreenplay() throws OSHException {
		// get current Random-Number and construct new Random-generator
		// (reason: there might be a varying number of random values necessary for one day,
		//          resulting in deviations of the optimization using different parameters for
		//          the optimization)
		// Author: IMA
		
		long initialNumber = getRandomGenerator().getNextLong();
		OSHRandomGenerator newRandomGen = new OSHRandomGenerator(new Random(initialNumber));
		
		
		//calc runs per day
		int dailyRuns = 0;
		double avgDailyRuns = getAvgYearlyRuns() / 365.0;
		// days with higher consumption explained by run of devices have more runs
		avgDailyRuns = avgDailyRuns * getDailyProbabilityCorrectionFactor(getTimer().getUnixTime());
		
		int dailyRunsMin = (int) Math.floor(avgDailyRuns);
		int dailyRunsMax = (int) Math.ceil(avgDailyRuns);
		double probMax = avgDailyRuns - dailyRunsMin;
		double r = newRandomGen.getNextDouble();
		if (r < probMax) {
			dailyRuns = dailyRunsMax;
		}
		else {
			dailyRuns = dailyRunsMin;
		}
		
		totalNumberOfRuns += dailyRuns;
		
		if (dailyRuns > 0) {

			SubjectAction[] actions = new SubjectAction[dailyRuns];
			for (int i = 0; i < dailyRuns; i++) {
				// select one program randomly
				double randomForProgramChoice = newRandomGen.getNextDouble(); //DO NOT REMOVE!
				//TODO implement
				
				//calc start time for run
				double randomValueForTimestamp = newRandomGen.getNextDouble();
				long startTime = getRandomTimestampForRunToday(
						getTimer().getUnixTime(), 
						middleOfPowerConsumption, 
						randomValueForTimestamp, 
						newRandomGen);
				
				// better do not generate action for the beginning of the simulation (no signal...)
				if (startTime < getTimer().getUnixTimeAtStart() + 100) {
					startTime = 100;
				}
				
				actions[i] = new SubjectAction();
				actions[i].setTick(startTime);		      
				actions[i].setDeviceID(getDeviceID().toString());
				actions[i].setNextState(true);
				actions[i].setActionType(ActionType.I_DEVICE_ACTION);
				
				// check whether new action would overwrite other existing action
				boolean generateNew = false;
				int currentMax1stDof = calcMaxDof(dailyRuns, 86400);
				long currentFirst = actions[i].getTick();
				long currentLast = actions[i].getTick() + currentMax1stDof + getProgamDuration();
				
				// overlapping with PLANNED (only in simulation) action from this day 
				// (actions from the other day are already scheduled)
				Collection<SubjectAction> existingActions = getActions();
				for (SubjectAction a : existingActions) {					
					long otherFirst = a.getTick() - device2ndDof;
//					long otherLast = a.getTick() + device2ndDof + getProgamDuration();
//					long otherLast = a.getTick() + deviceMaxDof + device2ndDof + getProgamDuration();
					long otherLast = a.getTick() + currentMax1stDof + getProgamDuration();
					
					if ((currentFirst >= otherFirst && currentFirst <= otherLast)
							|| (currentLast >= otherFirst && currentLast <= otherLast)) {
						generateNew = true;
					}
					
				}
				
				//overlapping with scheduled/running action from the other day
				if (programStart != -1) {
					long otherFirst = programStart;
					long otherLast = programStart + getProgamDuration();
					if (!en50523State.equals(EN50523DeviceState.RUNNING)) {
						otherFirst -= device2ndDof;
						otherLast += device2ndDof;
					}

					if ((currentFirst >= otherFirst && currentFirst <= otherLast)
							|| (currentLast >= otherFirst && currentLast <= otherLast)) {
						generateNew = true;
					}
				}
				
				if (generateNew) {
					i--; // generated Run is not possible (overlapping with other Runs)
				}
				else {
					setAction(actions[i]);
					
					//now create the Dof for the specific action (send it one second in advance...
					this.generateNewDof(useRandomDof, dailyRuns, actions[i].getTick(), newRandomGen);
				}
			}
		}
	}

	/**
	 * Please don't ask why it works...but it should be fine!
	 * @param actionCount
	 * @return
	 */
	protected int calcMaxDof(int actionCount, int availableTime) {
		int maxDof = getDeviceMax1stDof();
		// + 1 to be safe...
		while (Math.floor(availableTime / (2 * (maxDof + getProgamDuration() + 1))) + 1 < actionCount) {
			maxDof = maxDof / 2;
		}
		
		// run on last day immediately
		if (getTimer().getUnixTime() % 86400 == (getSimulationEngine().getSimulationDuration() / 86400 - 1)) {
			maxDof = 0;
		}
		
		return maxDof;
	}
	
	@Override
	protected void generateNewDof(boolean useRandomDof, int actionCountPerDay, long applianceActionTimeTick, OSHRandomGenerator randomGen) {
		super.generateNewDof(useRandomDof, actionCountPerDay, applianceActionTimeTick, randomGen);
		
		// generate DOFs with binary distribution
		if (getSimulationEngine().getScreenplayType() == ScreenplayType.DYNAMIC) {
			if (86400 / ((getProgamDuration() + 1) * actionCountPerDay) < 1 && actionCountPerDay > 1) {
				throw new RuntimeException("Program duration to long for multiple runs per day");
			}
			
			// (there could be a scheduled/running action from the other day)
			// this could cause an endless loop
			int maxDof = calcMaxDof(actionCountPerDay, 86400);
			
			int newValue = maxDof;
			if (useRandomDof) {
				// in 15 minutes steps only
				int stepSize = 900;
				maxDof = maxDof / stepSize;
				//deviate
				//E(X)=0.5*max=28800s=8h or E(X)=0.5*max=14400s=4h
				BinomialDistribution binDistribution = new BinomialDistribution(maxDof, 0.5);
				double rand = randomGen.getNextDouble();
				
				for (int i = 0; i < maxDof; i++) {
					if (binDistribution.cumulativeProbability(i) > rand) {
						newValue = i;
						break;
					}
				}
				newValue = newValue * stepSize;
			}
			
			
			//create now the new action

			SubjectAction dofAction = new SubjectAction();
			dofAction.setTick(applianceActionTimeTick - 1); // Do 1 sec in advance!
			dofAction.setDeviceID(this.getDeviceID().toString());
			dofAction.setActionType(ActionType.USER_ACTION);
			dofAction.setNextState(false);		
			PerformAction dofAction2Perform = new PerformAction();
			ActionParameters dofActionParameters = new ActionParameters();
			dofActionParameters.setParametersName("dof");
			ActionParameter dofActionParameter = new ActionParameter();
			dofActionParameter.setName("tdof");
			dofActionParameter.setValue("" + newValue);
			dofActionParameters.getParameter().add(dofActionParameter);
			dofAction2Perform.getActionParameterCollection().add(dofActionParameters);
			dofAction.getPerformAction().add(dofAction2Perform);
			
			this.setAction(dofAction);
		}
	}
	
	@Override
	public void onSystemShutdown() throws OSHException {
		super.onSystemShutdown();
		
		//DEBUG
		try {
			String fileName = "logs/" 
					+ getDeviceType() 
					+ "_" + (System.currentTimeMillis() / 1000)
					+ ".txt";
			PrintWriter pwr = new PrintWriter(new File(fileName));
			pwr.println(totalNumberOfRuns);
			pwr.close();
			System.out.println("Output: " + fileName);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		//DEBUG END
	}

}
