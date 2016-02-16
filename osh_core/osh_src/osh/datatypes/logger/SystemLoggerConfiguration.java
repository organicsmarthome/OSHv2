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

package osh.datatypes.logger;

/**
 * 
 * @author Ingo Mauser
 *
 */
public class SystemLoggerConfiguration {
	
	// system logger for system messages etc.
	
	private String globalLoggerLogLevel;
	
	private boolean systemLoggingToConsoleActive;
	
	private boolean systemLoggingToFileActive;
	private boolean createSingleLogfile = false;
	
	private boolean systemLoggingActive = true;
	private boolean showMessageCallerTrace = true;

	private String logDirName;
	
	public SystemLoggerConfiguration(
			String globalLoggerLogLevel,
			boolean systemLoggingToConsoleActive,
			boolean systemLoggingToFileActive, 
			boolean createSingleLogfile,
			boolean systemLoggingActive,
			boolean showMessageCallerTrace,
			String logDirName) {
		super();
		
		this.globalLoggerLogLevel = globalLoggerLogLevel;
		
		this.systemLoggingToConsoleActive = systemLoggingToConsoleActive;
		this.systemLoggingToFileActive = systemLoggingToFileActive;
		
		this.createSingleLogfile = createSingleLogfile;
		
		this.systemLoggingActive = systemLoggingActive;
		this.showMessageCallerTrace = showMessageCallerTrace;
		
		this.logDirName = logDirName;
	}

	
	
	public String getGlobalLoggerLogLevel() {
		return globalLoggerLogLevel;
	}

	public boolean isSystemLoggingToConsoleActive() {
		return systemLoggingToConsoleActive;
	}

	public boolean isSystemLoggingToFileActive() {
		return systemLoggingToFileActive;
	}

	public boolean isCreateSingleLogfile() {
		return createSingleLogfile;
	}

	public boolean isShowMessageCallerTrace() {
		return showMessageCallerTrace;
	}

	public boolean isSystemLoggingActive() {
		return systemLoggingActive;
	}

	public String getLogDirName() {
		return logDirName;
	}

}
