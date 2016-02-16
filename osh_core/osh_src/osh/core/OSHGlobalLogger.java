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

package osh.core;

import org.apache.log4j.Level;

import osh.datatypes.logger.SystemLoggerConfiguration;



/**
 * Global logger for the OSH. Please use this logger for all OSH logging issues
 * 
 * @author Florian Allerding, Kaibin Bao, Till Schuberth, Ingo Mauser
 */
public class OSHGlobalLogger {
	
	private IOSHFull controllerbox;
	
//	// if true please check for required changes
	private boolean createSingleLogfile = false;

	private boolean consoleSystemMessagesEnabled = true;
	private boolean logSystemMessagesEnabled = true;

	private boolean systemLoggingActive = true;
	private boolean messageCallerTrace = true;

	/**
	 * CONSTRUCTOR
	 * @param controllerbox
	 */
	public OSHGlobalLogger(IOSHFull controllerbox, SystemLoggerConfiguration systemLoggerConfiguration) {
		
		if (controllerbox ==  null) throw new NullPointerException("controllerbox is null");
		this.controllerbox = controllerbox;
		
		this.createSingleLogfile = systemLoggerConfiguration.isCreateSingleLogfile();
		
		this.consoleSystemMessagesEnabled = systemLoggerConfiguration.isSystemLoggingToConsoleActive();
		this.logSystemMessagesEnabled = systemLoggerConfiguration.isSystemLoggingToFileActive();
		
		this.systemLoggingActive = systemLoggerConfiguration.isSystemLoggingActive();
		this.messageCallerTrace = systemLoggerConfiguration.isShowMessageCallerTrace();
		
		initGlobalLogger(systemLoggerConfiguration.getLogDirName(), systemLoggerConfiguration.getGlobalLoggerLogLevel());
	}
	
	private void initGlobalLogger(String logDirName, String logLevel) {
		if (!createSingleLogfile){
			String timeStamp = String.valueOf(System.currentTimeMillis()/1000L);
			OSHLoggerCore.initLoggers(logDirName, "controllerBoxLog_" + timeStamp , logLevel, true, consoleSystemMessagesEnabled);
		}
		else {
			
			OSHLoggerCore.initLoggers(logDirName, "controllerBoxLog", logLevel, false, consoleSystemMessagesEnabled);
		}
	}
	

	public boolean isSystemLoggingActive() {
		return systemLoggingActive;
	}

	public void setSystemLoggingActive(boolean systemLogingActive) {
		this.systemLoggingActive = systemLogingActive;
	}
	

	private long currentTime(){
		if (controllerbox.getTimer() == null) return -1; //timer is not initialized yet
		return controllerbox.getTimer().getUnixTime();
	}

	/**
	 * create a single file for logging and override it every restart
	 * disable this variable and a logfile with timestamp will be created every restart
	 * @return the createSingleLogfile
	 */
	public boolean isCreateSingleLogfile() {
		return createSingleLogfile;
	}

	/**
	 * log system messages in the logfile
	 * @return the logSystemMessages
	 */
	public boolean isLogSystemMessages() {
		return logSystemMessagesEnabled;
	}

	/**
	 * report the caller class of the log-message while logging 
	 * @return the messageCallerTrace
	 */
	public boolean isMessageCallerTrace() {
		return messageCallerTrace;
	}
	/**
	 * print system messages on the console
	 * @return the systemMessagesEnable
	 */
	public boolean isSystemMessagesEnable() {
		return consoleSystemMessagesEnabled;
	}
	
	
	/**
	 * create a single file for logging and override it every restart
	 * disable this variable and a logfile with timestamp will be created every restart
	 * @param createSingleLogfile the createSingleLogfile to set
	 */
	public void setCreateSingleLogfile(boolean createSingleLogfile) {
		this.createSingleLogfile = createSingleLogfile;
	}
	
	
	public String getLogLevel() {
		return OSHLoggerCore.getLogLevel();
	}
	
	public void setLogLevel(String logLevel) {
		OSHLoggerCore.setLogLevel(logLevel);
	}
	
	
	/**
	 * print system messages on the console
	 * @param consoleSystemMessagesEnabled the systemMessagesEnable to set
	 */
	public void setConsoleSystemMessagesEnabled(boolean consoleSystemMessagesEnabled) {
		this.consoleSystemMessagesEnabled = consoleSystemMessagesEnabled;
	}
	
	/**
	 * log system messages in the logfile
	 * @param logSystemMessagesEnabled the logSystemMessages to set
	 */
	public void setLogSystemMessagesEnabled(boolean logSystemMessagesEnabled) {
		this.logSystemMessagesEnabled = logSystemMessagesEnabled;
	}
	
	
	/**
	 *  report the caller class of the log-message while logging 
	 * @param messageCallerTrace the messageCallerTrace to set
	 */
	public void setMessageCallerTrace(boolean messageCallerTrace) {
		this.messageCallerTrace = messageCallerTrace;
	}
	
	
	// ALL > TRACE > DEBUG > INFO > WARN > ERROR > FATAL > OFF
	
	public void logError(Object message){
		if (systemLoggingActive) {
			if (messageCallerTrace) {
				String[] callerClassNameSpace = Thread.currentThread().getStackTrace()[2].getClassName().split("\\.");
				String callerClassName = callerClassNameSpace[callerClassNameSpace.length-1];
				OSHLoggerCore.cb_Main_Logger.error("[LOGGING] ["+currentTime()+ "] [ERROR] [" + callerClassName + "] : [" + message + "]");
			}
			else {
				OSHLoggerCore.cb_Main_Logger.error(message);
			}
		}
	}
	
	public void logError(Object message, Throwable throwable){
		if (systemLoggingActive) {
			if (messageCallerTrace) {
				String[] callerClassNameSpace = Thread.currentThread().getStackTrace()[2].getClassName().split("\\.");
				String callerClassName = callerClassNameSpace[callerClassNameSpace.length-1];
				OSHLoggerCore.cb_Main_Logger.error("[LOGGING] ["+currentTime()+ "] [ERROR] [" + callerClassName + "] : [" + message + "]", throwable);
			}
			else {
				OSHLoggerCore.cb_Main_Logger.error(message, throwable);
			}
		}
	}
	
	public void logWarning(Object message){
		if (systemLoggingActive) {
			if (messageCallerTrace) {
				String[] callerClassNameSpace = Thread.currentThread().getStackTrace()[2].getClassName().split("\\.");
				String callerClassName = callerClassNameSpace[callerClassNameSpace.length-1];
				OSHLoggerCore.cb_Main_Logger.warn("[LOGGING] ["+currentTime()+ "] [WARN] [" + callerClassName + "] : [" + message + "]");
			}
			else {
				OSHLoggerCore.cb_Main_Logger.warn(message);
			}
		}
	}
	
	public void logWarning(Object message, Throwable throwable){
		if (systemLoggingActive) {
			if (messageCallerTrace) {
				String[] callerClassNameSpace = Thread.currentThread().getStackTrace()[2].getClassName().split("\\.");
				String callerClassName = callerClassNameSpace[callerClassNameSpace.length-1];
				OSHLoggerCore.cb_Main_Logger.warn("[LOGGING] ["+currentTime()+ "] [WARN] [" + callerClassName + "] : [" + message + "]", throwable);
			}
			else {
				OSHLoggerCore.cb_Main_Logger.warn(message, throwable);
			}
		}
	}
	
	public void logInfo(Object message){
		if (systemLoggingActive) {
			if (messageCallerTrace) {
				String[] callerClassNameSpace = Thread.currentThread().getStackTrace()[2].getClassName().split("\\.");
				String callerClassName = callerClassNameSpace[callerClassNameSpace.length-1];
				OSHLoggerCore.cb_Main_Logger.info("[LOGGING] ["+currentTime()+ "] [INFO] [" + callerClassName + "] : [" + message + "]");
			}
			else {
				OSHLoggerCore.cb_Main_Logger.info(message);
			}
		}
	}

	public void logInfo(Object message, Throwable throwable){
		if (systemLoggingActive) {
			if (messageCallerTrace) {
				String[] callerClassNameSpace = Thread.currentThread().getStackTrace()[2].getClassName().split("\\.");
				String callerClassName = callerClassNameSpace[callerClassNameSpace.length-1];
				OSHLoggerCore.cb_Main_Logger.info("[LOGGING] ["+currentTime()+ "] [INFO] [" + callerClassName + "] : [" + message + "]", throwable);
			}
			else {
				OSHLoggerCore.cb_Main_Logger.info(message, throwable);
			}
		}
	}
		
	
	public void logDebug(Object message) {
		if (systemLoggingActive) {
			if (messageCallerTrace) {
				String[] callerClassNameSpace = Thread.currentThread().getStackTrace()[2].getClassName().split("\\.");
				String callerClassName = callerClassNameSpace[callerClassNameSpace.length-1];
				OSHLoggerCore.cb_Main_Logger.debug("[LOGGING] ["+currentTime()+ "] [DEBUG] [" + callerClassName + "] : [" + message + "]");
			}
			else {
				OSHLoggerCore.cb_Main_Logger.debug(message);
			}
		}
	}
	public void logDebug(Object message, Throwable throwable) {
		if (systemLoggingActive) {
			if (messageCallerTrace) {
				String[] callerClassNameSpace = Thread.currentThread().getStackTrace()[2].getClassName().split("\\.");
				String callerClassName = callerClassNameSpace[callerClassNameSpace.length-1];
				OSHLoggerCore.cb_Main_Logger.debug("[LOGGING] ["+ currentTime() + "] [DEBUG] [" + callerClassName + "] : [" + message + "]", throwable);
			}
			else {
				OSHLoggerCore.cb_Main_Logger.debug(message, throwable);
			}
		}
	}
	
	/**
	 * System messages can be printed at the console to show the current state of the contollerbox
	 * @param message
	 */
	public void printSystemMessage(Object message){
		if (systemLoggingActive) {
			if (consoleSystemMessagesEnabled){
				if (messageCallerTrace) {
					String[] callerClassNameSpace = Thread.currentThread().getStackTrace()[2].getClassName().split("\\.");
					String callerClassName = callerClassNameSpace[callerClassNameSpace.length-1];
					System.out.println("[CONSOLE] [" + currentTime() + "] [SYSTEM] [" + callerClassName + "] : [" + message + "]");
				}
				else {
					System.out.println("[ControllerBox] " + message);
				}
			}
			if (logSystemMessagesEnabled) {
				this.logInfo("[ControllerBox] : " + message);	
			}
		}
	}
	
	/**
	 * System messages which will be printed out but never logged. Messages are only printed out in DEBUG level!
	 * @param message
	 */
	public void printDebugMessage(Object message){
		// print only in debug level
		if (OSHLoggerCore.cb_Main_Logger.getLevel() == Level.DEBUG){
			String[] callerClassNameSpace = Thread.currentThread().getStackTrace()[2].getClassName().split("\\.");
			String callerClassName = callerClassNameSpace[callerClassNameSpace.length-1];
			System.out.println("[CONSOLE] [" + currentTime() + "] [DEBUG] [" + callerClassName + "] : [" + message + "]");
		}
	}
	
	

}
