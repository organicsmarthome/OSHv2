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

package osh.comdriver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import osh.comdriver.logger.ValueFileLogger;
import osh.comdriver.logger.ValueLogger;
import osh.configuration.OSHParameterCollection;
import osh.core.IOSH;
import osh.core.com.interfaces.IEventReceiver;
import osh.core.exceptions.OSHException;
import osh.datatypes.logger.ValueLoggerConfiguration;
import osh.datatypes.registry.StateExchange;
import osh.hal.HALComDriver;


/**
 * 
 * @author Florian Allerding, Kaibin Bao, Till Schuberth, Ingo Mauser
 *
 */
public abstract class LoggerComDriver extends HALComDriver implements IEventReceiver {
	
	protected ValueFileLogger fileLog;
	protected static boolean logAll = true;
	
	protected ValueLoggerConfiguration valueLoggerConfiguration;
	protected HashMap<UUID,List<String>> loggerUuidAndClassesToLogMap;
	
	protected long lastLoggingToConsoleAt;
	protected long lastLoggingToFileAt;
	protected long lastLoggingToDatabaseAt;
	protected long lastLoggingToRrdDatabaseAt;

	
	/**
	 * CONSTRUCTOR
	 */
	public LoggerComDriver(
			IOSH controllerbox, 
			UUID deviceID,
			OSHParameterCollection driverConfig) {
		super(controllerbox, deviceID, driverConfig);

		// get parameters
		if ( valueLoggerConfiguration.getIsValueLoggingToFileActive() ) {
			fileLog = new ValueFileLogger(
					getOSH().getCbstatus().getRunID(),
					"0",
					getOSH().getCbstatus().getConfigurationID(),
					getOSH().getCbstatus().isSimulation());
		}
		
		//TODO: add option for "log all"
		String loggerUuidAndClassesToLogMapString = getDriverConfig().getParameter("loggeruuidandclassestolog");
		if (loggerUuidAndClassesToLogMapString != null 
				&& !loggerUuidAndClassesToLogMapString.equals("")) {
			String[] splittedLoggerUuidAndClassesToLogMapString = loggerUuidAndClassesToLogMapString.split(";");
			if (splittedLoggerUuidAndClassesToLogMapString != null 
					&& splittedLoggerUuidAndClassesToLogMapString.length > 0) {
				
				loggerUuidAndClassesToLogMap = new HashMap<>();
				
				for (int i = 0; i < splittedLoggerUuidAndClassesToLogMapString.length; i++) {
					UUID uuid = UUID.fromString(splittedLoggerUuidAndClassesToLogMapString[i].split(":")[0]);
					String classes = splittedLoggerUuidAndClassesToLogMapString[i].split(":")[1];
					String[] splittedClasses = classes.split(",");
					List<String> listedClasses = new ArrayList<String>();
					for (int j = 0; j < splittedClasses.length; j++) {
						listedClasses.add(splittedClasses[j]);
					}
					loggerUuidAndClassesToLogMap.put(uuid, listedClasses);
				}
				
			}
		}
	}
	
	/**
	 * Pull-logging
	 */
	@Override
	public void onNextTimePeriod() throws OSHException {
		super.onNextTimePeriod();
		
		ArrayList<ValueLogger> activeLoggers = new ArrayList<>();
		
		long currentTime = getTimer().getUnixTime();
		
		if (fileLog != null
				&& (currentTime - lastLoggingToFileAt) >= valueLoggerConfiguration.getValueLoggingToFileResolution()) {
			activeLoggers.add(fileLog);
			lastLoggingToFileAt = currentTime;
		}
		
		//TODO: log all (allow partial logging)
		if (!activeLoggers.isEmpty()) {
			if ( logAll ) {
				for ( Class<? extends StateExchange> type : getDriverRegistry().getTypes() ) {
					for ( Entry<UUID, StateExchange> ent : getDriverRegistry().getStates(type).entrySet() ) {
						for ( ValueLogger vlog : activeLoggers ) {
							vlog.log( currentTime, ent.getValue() );
						}
					}
				}
			} 
			else {
				for (Entry<UUID,List<String>> e : loggerUuidAndClassesToLogMap.entrySet()) {
					UUID uuid = e.getKey();
					for (String className : e.getValue()) {
						try {
							@SuppressWarnings("rawtypes")
							Class realClass = Class.forName(className);
							
							@SuppressWarnings("unchecked")
							StateExchange a = getDriverRegistry().getState(realClass, uuid);
							
							for( ValueLogger vlog : activeLoggers ) {
								vlog.log( currentTime, a );
							}
							
						}
						catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				} /* for */
			} /* if( logAll )  */
		} /* if (updateNecessary) */
	}

}
