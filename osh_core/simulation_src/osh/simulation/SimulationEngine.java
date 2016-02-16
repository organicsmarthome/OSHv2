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
import java.util.HashMap;
import java.util.UUID;

import osh.OSHComponent;
import osh.datatypes.XMLSerialization;
import osh.simulation.exception.SimulationEngineException;
import osh.simulation.exception.SimulationSubjectException;
import osh.simulation.screenplay.Screenplay;
import osh.simulation.screenplay.ScreenplayType;

/**
 * Simulation engine for the smart-home-lab
 * @author Florian Allerding, Ingo Mauser
 * @category smart-home ControllerBox Simulation
 */
public class SimulationEngine extends AbstractSimulationEngine {

	
	private ArrayList<ISimulationSubject> simulationSubjects;
	private HashMap<UUID, ISimulationSubject> simulationMap;


	private ScreenplayType screenplayType = null;


	/**
	 * CONSTRUCTOR<br>
	 * constructor with a given array of devices to simulate...yes everything is a device!
	 * @param deviceList
	 * @param simlogger 
	 */
	public SimulationEngine(
			ArrayList<? extends OSHComponent> deviceList,
			ScreenplayType screenplayType,
			ISimulationActionLogger simlogger) throws SimulationEngineException {
		
		
		this.screenplayType = screenplayType;

		this.simulationSubjects = new ArrayList<ISimulationSubject>();
		this.simulationMap = new HashMap<UUID, ISimulationSubject>();

		//cast to simulation subject
		try {
			for ( OSHComponent _driver : deviceList ) {
				if ( _driver instanceof ISimulationSubject ) {
					ISimulationSubject _simSubj = (ISimulationSubject) _driver;

					//assign the simulation engine
					_simSubj.setSimulationEngine(this);

					//assign logger
					_simSubj.setSimulationActionLogger(simlogger);
					
					//add subject
					this.simulationSubjects.add(_simSubj);
					
					//do the same for the HashMap (better direct Access)
					this.simulationMap.put(_simSubj.getDeviceID(),_simSubj);			
				}
			}	
		}
		catch (Exception ex){
			throw new SimulationEngineException(ex);
		}
	}

	
	/**
	 * get a simulationSubject by his (device) ID.
	 * This can be called from another subject to get an appending subject
	 * @param subjectID
	 * @return
	 */
	protected ISimulationSubject getSimulationSubjectByID(UUID subjectID){
		ISimulationSubject _simSubj = null;
		_simSubj = simulationMap.get(subjectID);

		return _simSubj;
	}

	/**
	 * load the actions for the devices from a screenplay-object for a timespan
	 * @param currentScreenplay
	 */
	public void loadSingleScreenplay(Screenplay currentScreenplay){
		for ( ISimulationSubject _simSubj : simulationSubjects ){
			for(int i = 0; i < currentScreenplay.getSIMActions().size(); i++){
				//Search for an action for a specific device
				if (currentScreenplay.getSIMActions().get(i).getDeviceID().compareTo(_simSubj.getDeviceID().toString()) == 0) {
					_simSubj.setAction(currentScreenplay.getSIMActions().get(i));
				}
			}
		}
	}

	/**
	 * @param load the actions for the devices for a timespan or a cycle from a file
	 */
	public void loadSingleScreenplay(String screenPlaySource) throws SimulationEngineException{

		Screenplay currentScreenplaySet;

		try {
			currentScreenplaySet = (Screenplay) XMLSerialization.file2Unmarshal(screenPlaySource, Screenplay.class);
		}
		catch (Exception ex){
			currentScreenplaySet = null;
			throw new SimulationEngineException(ex);
		}

		this.loadSingleScreenplay(currentScreenplaySet);
	}

	/**
	 * You can load more than one day at the same time
	 * @param screenplays: An Arraylist of the one-day-screenplay
	 * @param ticksPerDay: The duration of a day
	 * @param resetTimerAfterEachDay: you want to do this?
	 */
	public void runMultipleDaySimulation(ArrayList<Screenplay> screenplays, int ticksPerDay, boolean resetTimerAfterEachDay) throws SimulationEngineException{

		for (Screenplay _screenplay:screenplays) {

			this.loadSingleScreenplay(_screenplay);

			runSimulation(ticksPerDay);

			if (resetTimerAfterEachDay){
				this.resetSimulationTimer();
			}

		}

	}

	/**
	 * simulate the next timeTick, increment the real-time driver 
	 * @param currentTick
	 * @throws SimulationEngineException
	 */
	@Override
	protected void simulateNextTimeTick(int currentTick) throws SimulationEngineException {
		



		//notify the subject that the next sim tick begins
		for (ISimulationSubject _simSubject : simulationSubjects) {

			//general
			_simSubject.onSimulationPreTickHook();

		}


		try {
			for (ISimulationSubject _simSubject : simulationSubjects) {

				//general
				_simSubject.triggerSubject();

				//for SimulationDevices

				//looking for some special devices
				//currently NONE
			}
		} 
		catch (SimulationSubjectException ex){
			throw new SimulationEngineException(ex);
		}
		
		//notify the subject that the current sim tick ended 
		for (ISimulationSubject _simSubject : simulationSubjects) {

			//general
			_simSubject.onSimulationPostTickHook();

		}

	}


	public ScreenplayType getScreenplayType() {
		return screenplayType;
	}

	@Override
	protected void notifyLocalEngineOnSimulationIsUp() throws SimulationEngineException {
		try {
			for (ISimulationSubject simulationSubject: this.simulationSubjects) {
				simulationSubject.onSimulationIsUp();
			}
		} 
		catch (SimulationSubjectException ex ) {
			throw new SimulationEngineException(ex);
		}
		
	}
	
}
