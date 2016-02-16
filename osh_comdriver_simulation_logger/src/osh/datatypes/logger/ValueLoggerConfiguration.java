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
public class ValueLoggerConfiguration {
	
	// value logger for power values etc.
	//  console
	private Boolean valueLoggingToConsoleActive;
	private Integer valueLoggingToConsoleResolution;
	//  file
	private Boolean valueLoggingToFileActive;
	private Integer valueLoggingToFileResolution;
	//  database
	private Boolean valueLoggingToDatabaseActive;
	private Integer valueLoggingToDatabaseResolution;
	//  RRD database
	private Boolean valueLoggingToRrdDatabaseActive;
	private Integer valueLoggingToRrdDatabaseResolution;
	
	
	public ValueLoggerConfiguration(Boolean valueLoggingToConsoleActive,
			Integer valueLoggingToConsoleResolution,
			Boolean valueLoggingToFileActive,
			Integer valueLoggingToFileResolution,
			Boolean valueLoggingToDatabaseActive,
			Integer valueLoggingToDatabaseResolution,
			Boolean valueLoggingToRrdDatabaseActive,
			Integer valueLoggingToRrdDatabaseResolution) {
		super();
		
		this.valueLoggingToConsoleActive = valueLoggingToConsoleActive;
		this.valueLoggingToConsoleResolution = valueLoggingToConsoleResolution;
		
		this.valueLoggingToFileActive = valueLoggingToFileActive;
		this.valueLoggingToFileResolution = valueLoggingToFileResolution;
		
		this.valueLoggingToDatabaseActive = valueLoggingToDatabaseActive;
		this.valueLoggingToDatabaseResolution = valueLoggingToDatabaseResolution;
		
		this.valueLoggingToRrdDatabaseActive = valueLoggingToRrdDatabaseActive;
		this.valueLoggingToRrdDatabaseResolution = valueLoggingToRrdDatabaseResolution;
	}
	
	
	
	public Boolean getIsValueLoggingToConsoleActive() {
		return valueLoggingToConsoleActive;
	}
	
	public void setIsValueLoggingToConsoleActive(Boolean isValueLoggingToConsoleActive) {
		this.valueLoggingToConsoleActive = isValueLoggingToConsoleActive;
	}
	
	public Boolean getIsValueLoggingToFileActive() {
		return valueLoggingToFileActive;
	}
	
	public void setIsValueLoggingToFileActive(Boolean isValueLoggingToFileActive) {
		this.valueLoggingToFileActive = isValueLoggingToFileActive;
	}
	
	public Integer getValueLoggingToFileResolution() {
		return valueLoggingToFileResolution;
	}
	
	public void setValueLoggingToFileResolution(Integer valueLoggingToFileResolution) {
		this.valueLoggingToFileResolution = valueLoggingToFileResolution;
	}
	
	public Boolean getIsValueLoggingToDatabaseActive() {
		return valueLoggingToDatabaseActive;
	}
	
	public void setIsValueLoggingToDatabaseActive(Boolean isValueLoggingToDatabaseActive) {
		this.valueLoggingToDatabaseActive = isValueLoggingToDatabaseActive;
	}
	
	public Integer getValueLoggingToDatabaseResolution() {
		return valueLoggingToDatabaseResolution;
	}
	
	public void setValueLoggingToDatabaseResolution(Integer valueLoggingToDatabaseResolution) {
		this.valueLoggingToDatabaseResolution = valueLoggingToDatabaseResolution;
	}
	
	// Console
	
	public Integer getValueLoggingToConsoleResolution() {
		return valueLoggingToConsoleResolution;
	}
	
	public void setValueLoggingToConsoleResolution(
			Integer valueLoggingToConsoleResolution) {
		this.valueLoggingToConsoleResolution = valueLoggingToConsoleResolution;
	}

	// RRD Database
	
	public Boolean getValueLoggingToRrdDatabaseActive() {
		return valueLoggingToRrdDatabaseActive;
	}

	public Integer getValueLoggingToRrdDatabaseResolution() {
		return valueLoggingToRrdDatabaseResolution;
	}

	public void setValueLoggingToRrdDatabaseResolution(
			Integer valueLoggingToRrdDatabaseResolution) {
		this.valueLoggingToRrdDatabaseResolution = valueLoggingToRrdDatabaseResolution;
	}
	
}
