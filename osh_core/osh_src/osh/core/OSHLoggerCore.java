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

import java.io.File;
import java.io.IOException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;


/**
 * central static logger class for the OSH. will be initialized at the controllerboxManager's ctor
 * 
 *@author florian.allerding@kit.edu
 *@category smart-home ControllerBox
 *
 */
public class OSHLoggerCore {

	/**
	 * For creating a log-message use this logger like:
	 * root_logger.warn("message");
	 */

	public static Logger cb_Main_Logger = null;
	
	public static void removeAllAppenders() {
		if (cb_Main_Logger != null) {
			cb_Main_Logger.removeAllAppenders();
		}
	}
	
	/**
	 * initialize a root Logger; for logging always use the root_logger !!!
	 * @param logFileName
	 * @param logLevel
	 */
	public static void initRootLogger(String logDirName, String logFileName, String logLevel, boolean useConsoleAppender) {
		initLoggers(logDirName, logFileName, logLevel, false, useConsoleAppender);
	}
	
	/**
	 * initialize a root Logger; for logging always use the root_logger !!!
	 * @param logFileName
	 * @param logLevel
	 * @param createSingleLogfile
	 */
	public static void initLoggers(String logDirName, String logFileName, String logLevel, boolean createSingleLogfile, boolean useConsoleAppender) {	
		
			logFileName = logFileName +".log";
			
			if (!createSingleLogfile) {
				// delete any existing log files
//				deleteLogDirectory(new File("logs"));
				createLogFile(logDirName, logFileName);
			}
			else {
				createLogFile(logDirName, logFileName);
			}
			
			FileAppender fileAppender = null;
			try {
				fileAppender = new FileAppender(new PatternLayout(), logDirName + "/" + logFileName);
				fileAppender.setName("logfileappender: " + logFileName);
			}
			catch (IOException e1) {
				e1.printStackTrace();
			}
		
			
			cb_Main_Logger = Logger.getLogger("Main Logger");
			
//			Appender appender = null;
			
			cb_Main_Logger.addAppender(fileAppender);
			
			if (useConsoleAppender) {
				ConsoleAppender consoleAppender = new ConsoleAppender(new PatternLayout(), "System.out");
				consoleAppender.setName("console appender");
				
				cb_Main_Logger.addAppender(consoleAppender);
			}
			
			try {
				cb_Main_Logger.setLevel(Level.toLevel(logLevel));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
			
		


	}
	
	public static String getLogLevel(){
		return cb_Main_Logger.getLevel().toString();
	}
	
	/**
	 * sets a specific loglevel like "warn", "info", ...
	 * @param logLevel
	 */
	public static void setLogLevel(String logLevel){
		cb_Main_Logger.setLevel(Level.toLevel(logLevel));
	}
	
	
	private static void createLogFile(String dirName, String fileName) {
		File logfile = null;
		
		File logdir = new File(dirName);
		logdir.mkdirs();
		if (!logdir.exists() || !logdir.isDirectory()) throw new RuntimeException("log file directory or parent is a file");
		
		logfile = new File (dirName + "/" + fileName);
		
		try {
//			System.out.println("file name: " + logfile.getAbsolutePath());
			if (!logfile.exists()) {
				logfile.createNewFile();
			}
		} catch (IOException e) {
			//e.printStackTrace();
		}
		
	}
	
	public static boolean deleteLogDirectory(File path) {	
		if (path.exists()) {
			File[] files = path.listFiles();
			@SuppressWarnings("unused")
			boolean deleted = false;
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteLogDirectory(files[i]);
				} else {
					 deleted = files[i].delete();
				}
			}
		}
		return (path.delete());
	}
	

}
