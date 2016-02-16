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

package osh.boot;

import java.util.TimeZone;

import osh.core.OCManager;
import osh.core.exceptions.OCManagerException;
import osh.datatypes.logger.SystemLoggerConfiguration;


/**
 * Real building loader
 * 
 * @author Florian Allerding, Kaibin Bao, Till Schuberth, Ingo Mauser
 *
 */
public class OSHLoader {
	
	static private String configID = "oshrealconfig";
	static private String configFilesPath = "configfiles";
	
	public static void main(String[] args) { 
		
		String runID = "" + (System.currentTimeMillis() / 1000L);
		
		// ### Start Initialization ###
		TimeZone hostTimeZone = TimeZone.getDefault();
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		
		String logDirName = "logs/" + configID + "/" + runID;
		
		SystemLoggerConfiguration systemLoggerConfiguration = new SystemLoggerConfiguration(
				"WARNING", 					//logLevel
				true,  						//systemLoggingToConsoleActive
				true, 						//systemLoggingToFileActive
				false, 
				true, 
				true,
				logDirName);
		
		String configrootPath = configFilesPath + "/osh/" + configID + "/";

		//initialize the controllerbox
		OCManager oshOCManager = new OCManager(systemLoggerConfiguration);
		
		try {
			oshOCManager.initControllerBox(
					configrootPath + "/system/HALConfig.xml", 
					configrootPath + "/system/CBConfig.xml", 
					hostTimeZone,
					0L,		//forced start time
					null,	//random seed
					runID,
					configID);
		} 
		catch (OCManagerException e) {
			oshOCManager.getGlobalLogger().logError("ERROR: initializing OSH", e);
			return;
		}

		//now everything should be up...
		
		oshOCManager.getGlobalLogger().printSystemMessage("OSH is up...now starting system...");
		
		try {
			oshOCManager.startSystem();
		} catch (OCManagerException e) {
			oshOCManager.getGlobalLogger().logError("ERROR: switching runlevel to *RUNNING*", e);
		}
	}

}
