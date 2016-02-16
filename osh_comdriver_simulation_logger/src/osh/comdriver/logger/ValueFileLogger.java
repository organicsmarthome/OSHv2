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

package osh.comdriver.logger;

import java.io.IOException;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import osh.core.exceptions.OSHException;


/**
 * 
 * @author Florian Allerding, Ingo Mauser
 *
 */
public class ValueFileLogger extends ValueLogger {
	
	private String prefixForLogs;
	private String suffixForLogs;
	
	//logger
	//TODO make generic
	private Logger powerLogger;
	private Logger powerDetailsLogger;
	private Logger virtualCommodityPowerDetailsLogger;
	private Logger scheduleLogger;
	private Logger temperatureLogger;
	private Logger externalSignalLogger;
	private Logger costDetailedDetailsLogger;
	
	
	/**
	 * CONSTRUCTOR
	 * @param runID
	 * @param packageID
	 * @param configurationID
	 * @param isSimulation
	 */
	public ValueFileLogger(String runID, String packageID, String configurationID, boolean isSimulation) {
		super();
		
		initSimulationLogger(runID, packageID, configurationID);
	}
	

	private void initSimulationLogger(
			String runID,
			String packageID,
			String configurationID) {
		
		prefixForLogs = "logs/" + runID + "/valueLogs/" + packageID + "_" + configurationID;
		suffixForLogs = runID + ".csv";
		
		powerLogger = createFileLogger("Power");
		powerDetailsLogger = createFileLogger("PowerDetails");
		virtualCommodityPowerDetailsLogger = createFileLogger("VirtualCommodityPowerDetails");
		scheduleLogger = createFileLogger("Schedule");
		temperatureLogger = createFileLogger("InhouseTemperatures");
		externalSignalLogger = createFileLogger("ExternalSignals");
		costDetailedDetailsLogger = createFileLogger("DetailedCosts");
	}
	
	
	private Logger createFileLogger(String name) {
		Logger simulationDataLogger = Logger.getLogger(name);
		FileAppender newfileAppender = null;
		try {
			newfileAppender = new FileAppender(
					new PatternLayout(), 
					prefixForLogs + "_" + name + suffixForLogs);
			newfileAppender.setName("logfileappender: " + name);
		}
		catch (IOException e1) {
			throw new RuntimeException("Exception in simulationLogger", e1);
		}
		simulationDataLogger.addAppender(newfileAppender);
		simulationDataLogger.setLevel(Level.INFO);
		
		return simulationDataLogger;
			
	}
	

	//TODO make generic
	public void logPower(String entryLine) {
		powerLogger.log(Level.INFO, entryLine);
	}
	
	public void logPowerDetails(String entryLine) {
		powerDetailsLogger.log(Level.INFO, entryLine);
	}
	
	public void logSchedule(String entryLine) {
		scheduleLogger.log(Level.INFO, entryLine);
	}
	
	public void logTemperature(String entryLine) {
		temperatureLogger.log(Level.INFO, entryLine);
	}
	
	public void logExternalSignals(String entryLine) {
		externalSignalLogger.log(Level.INFO, entryLine);
	}
	
	public void logVirtualCommodityPowerDetails(String entryLine) {
		virtualCommodityPowerDetailsLogger.log(Level.INFO, entryLine);
	}
	
	public void logCostDetailed(String entryLine) {
		costDetailedDetailsLogger.log(Level.INFO, entryLine);
	}


	@Override
	public void log(long timestamp, Object entity) throws OSHException {
		// TODO Auto-generated method stub
	}

}
