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

package osh.runsimulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.TimeZone;

import osh.core.OCManager;
import osh.core.OSH;
import osh.core.OSHLoggerCore;
import osh.core.exceptions.OCManagerException;
import osh.datatypes.logger.SystemLoggerConfiguration;
import osh.simulation.ActionSimulationLogger;
import osh.simulation.ISimulationActionLogger;
import osh.simulation.SimulationEngine;
import osh.simulation.exception.SimulationEngineException;
import osh.simulation.screenplay.ScreenplayType;


/**
 * 
 * @author Ingo Mauser, Florian Allerding
 *
 */
public class runSimulationPackage {

	/* #########################
	 * # General configuration #
	 * ######################### */


	static private String configID = "oshsimconfig";

	// logger for exceptions etc.
	/** Logger log level
	 * "DEBUG"  : nearly everything
	 * "INFO"   : only important stuff (default)
	 * "ERROR"  : errors only -> used for simulation recording
	 * "OFF"    : nothing */
	static private String globalLoggerLogLevel = "DEBUG";


	static private String configFilesPath = "configfiles";
	static private long forcedStartTime = 0;
	static private int simulationDuration = 864000; //simulate ten days

	/* ########################
	 * # System configuration #
	 * ########################	*/

	public static void main(String[] args) {

		long simStartTime = System.currentTimeMillis();
		String runID = "" + (simStartTime / 1000);

		String logDirName = "logs/" + configID + "/" + runID;
		
		SystemLoggerConfiguration systemLoggingConfiguration = new SystemLoggerConfiguration(
				globalLoggerLogLevel, 
				true, //systemLoggingToConsoleActive
				true, //systemLoggingToFileActive
				false, 
				true, 
				true,
				logDirName);

		File simulationFolder = new File(configFilesPath + "/osh");

		if ( !simulationFolder.exists() ) {
			System.out.println("[ERROR] Simulation folder does not exist: " + simulationFolder.getAbsolutePath());
			System.exit(1);
		}

		System.out.println("[INFO] Simulation running from time " + forcedStartTime + " for " + simulationDuration + " ticks");

		ScreenplayType currentScreenplayType = ScreenplayType.DYNAMIC;

		Long randomSeed = 0xd1ce5bL;


		String configrootPath = configFilesPath + "/osh/" + configID + "/";

		String currentScreenplayFileName = configrootPath + "simulation/Screenplay.xml";
		String currentHalconfigFileName = configrootPath + "system/HALConfig.xml";
		String currentControllerBoxConfigFileName = configrootPath + "system/CBConfig.xml";

		File file1 = new File(currentScreenplayFileName);
		File file2 = new File(currentHalconfigFileName);
		File file3 = new File(currentControllerBoxConfigFileName);

		// check if files exist
		if (!file1.exists() || !file2.exists() || !file3.exists()) {
			System.out.println("[ERROR] One ore more of the required files is missing");
			if (!file1.exists()) {
				System.out.println("[ERROR] Screenplay file is missing : " + currentScreenplayFileName);
			}
			if (!file2.exists()) {
				System.out.println("[ERROR] HALConfigFile is missing : " + currentHalconfigFileName);
			}
			if (!file3.exists()) {
				System.out.println("[ERROR] ControllerBoxConfigFile is missing : " + currentControllerBoxConfigFileName);
			}
			return;
		}

		//then initialize the ControllerBox
		OCManager ocManager = 
				new OCManager(new OSH(), systemLoggingConfiguration);
		try {
			ocManager.initControllerBox(
					currentHalconfigFileName, 
					currentControllerBoxConfigFileName,
					TimeZone.getTimeZone("UTC"),
					forcedStartTime,
					randomSeed,
					runID,
					configID);
		}
		catch (OCManagerException e) {
			e.printStackTrace();
			return;
		}

		// everything should be up now...
		// now: load simulation

		ISimulationActionLogger simlogger = null;
		try {
			String folderName = logDirName;
			File theDir = new File(folderName);

			// if the directory does not exist, create it
			if (!theDir.exists()) {
				theDir.mkdir();  
			}

			simlogger = new ActionSimulationLogger( 
					ocManager.getGlobalLogger(), 
					folderName + "/" + configID + "_" + randomSeed + "_actionlog.mxml");
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		osh.simulation.SimulationEngine myEngine = null;
		try {
			myEngine = new SimulationEngine(
					ocManager.getAllDrivers(),
					currentScreenplayType,
					simlogger);
		}
		catch (SimulationEngineException e) {
			e.printStackTrace();
			return;
		}

		//assign time base
		myEngine.assignTimerDriver(ocManager.getHalManager().getRealTimeDriver());

		//assign OC-Registry
		myEngine.assignOCRegistry(ocManager.getOSH().getOCRegistry());

		//assign Driver-Registry
		myEngine.assignDriverRegistry(ocManager.getOSH().getDriverRegistry());

		try {

			myEngine.loadSingleScreenplay(currentScreenplayFileName);
			myEngine.notifySimulationIsUp();
			ocManager.setSystemRunning();
			myEngine.runSimulation(simulationDuration);
			ocManager.getGlobalLogger().logInfo("[main] sim started");
		}
		catch (SimulationEngineException e1) {
			e1.printStackTrace();
			return;
		} 
		catch (OCManagerException e) {
			e.printStackTrace();
			return;
		}

		//shutdown the controllerBox
		try {
			ocManager.doSystemShutdown();
		}
		catch (OCManagerException e) {
			e.printStackTrace();
		}

		OSHLoggerCore.removeAllAppenders();

		System.out.println("[main] sim stopped");

		long simFinishTime = System.currentTimeMillis();

		System.out.println("[main] Simulation runtime: " + (simFinishTime - simStartTime) / 1000 + " sec");
	}

}
