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

import osh.core.oc.GlobalController;
import osh.core.oc.GlobalObserver;
import osh.hal.HALRealTimeDriver;
import osh.registry.DriverRegistry;
import osh.registry.OCRegistry;

/**
 * 
 * @author Florian Allerding, Kaibin Bao, Till Schuberth, Ingo Mauser
 *
 */
public class OSH implements IOSHFull {

	/** OCRegistry (O/C communication above HAL) */
	OCRegistry ocRegistry;
	
	/** DriverRegistry (HALDriver and BusDriver communication below HAL) */
	DriverRegistry driverRegistry;
	
	
	/* default */ OSHGlobalLogger logger;
	/* default */ OSHStatus oshstatus;
	/* default */ HALRealTimeDriver timer;
	/* default */ OSHRandomGenerator randomGenerator;
	/* default */ GlobalController globalcontroller;
	/* default */ GlobalObserver globalobserver;
	
	
	/**
	 * CONSTRUCTOR
	 */
	public OSH() {
		//init cbstatus
		this.oshstatus = new OSHStatus();
	}
	
	
	@Override
	public OCRegistry getOCRegistry() {
		return ocRegistry;
	}

	public void setOCRegistry(OCRegistry ocRegistry) {
		this.ocRegistry = ocRegistry;
	}
	
	
	@Override
	public DriverRegistry getDriverRegistry() {
		return driverRegistry;
	}

	public void setDriverRegistry(DriverRegistry driverRegistry) {
		this.driverRegistry = driverRegistry;
	}
	
	
	@Override
	public OSHGlobalLogger getLogger() {
		return logger;
	}


	@Override
	public IOSHStatus getCbstatus() {
		return oshstatus;
	}

	public OSHStatus getCbstatusObj() {
		return oshstatus;
	}

	@Override
	public HALRealTimeDriver getTimer() {
		return timer;
	}


	@Override
	public OSHRandomGenerator getRandomGenerator() {
		return randomGenerator;
	}


	public void setLogger(OSHGlobalLogger logger) {
		this.logger = logger;
	}


	public void setTimer(HALRealTimeDriver timer) {
		this.timer = timer;
	}


	public void setRandomGenerator(OSHRandomGenerator randomGenerator) {
		this.randomGenerator = randomGenerator;
	}
	
	
	public void setControllerBoxStatus(OSHStatus cbs) {
		this.oshstatus = cbs;
	}
	
	@Override
	public GlobalController getGlobalController() {
		return globalcontroller;
	}
	
	@Override
	public GlobalObserver getGlobalObserver() {
		return globalobserver;
	}
	
	public void setGlobalController(GlobalController globalcontroller) {
		this.globalcontroller = globalcontroller;
	}
	
	public void setGlobalObserver(GlobalObserver globalobserver) {
		this.globalobserver = globalobserver;
	}
	
	@Override
	public boolean isSimulation() {
		return oshstatus.isSimulation();
	}

}
