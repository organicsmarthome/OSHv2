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

package osh.hal;

import java.util.ArrayList;
import java.util.UUID;

import osh.ILifeCycleListener;
import osh.IRealTimeSubscriber;
import osh.OSHComponent;
import osh.configuration.OSHParameterCollection;
import osh.core.IOSHDriver;
import osh.core.IOSH;
import osh.core.exceptions.OSHException;
import osh.registry.DriverRegistry;
import osh.utils.uuid.UUIDLists;


/**
 * 
 * @author Florian Allerding, Kaibin Bao, Till Schuberth, Ingo Mauser
 *
 */
public class HALDriver extends OSHComponent implements IRealTimeSubscriber, ILifeCycleListener {

	private final UUID deviceID;
	private OSHParameterCollection driverConfig;
	
	private DriverRegistry driverRegistry;
	
	/**
	 * CONSTRUCTOR
	 * @param controllerbox
	 * @param deviceID
	 * @param driverConfig
	 */
	public HALDriver(
			IOSH controllerbox, 
			UUID deviceID,
			OSHParameterCollection driverConfig) {
		super(controllerbox);
		
		this.deviceID = deviceID;
		this.driverConfig = driverConfig;
	}
	
	
	@Override
	protected IOSHDriver getOSH() {
		return (IOSHDriver) super.getOSH();
	}
	
	/**
	 * The UUID of the device.
	 * @return Device-UUID
	 */
	public UUID getDeviceID() {
		return deviceID;
	}
	
	protected DriverRegistry getDriverRegistry() {
		return driverRegistry;
	}
	
	/**
	 * @return the driverConfig
	 */
	public OSHParameterCollection getDriverConfig() {
		return driverConfig;
	}

	/**
	 * @param driverConfig the driverConfig to set
	 */
	public void setDriverConfig(OSHParameterCollection driverConfig) {
		this.driverConfig = driverConfig;
	}

	@Override
	public OSHComponent getSyncObject() {
		return this;
	}

	@Override
	public void onSystemRunning() throws OSHException {
		//...in case of use please override
	}

	@Override
	public void onSystemShutdown() throws OSHException {
		//...in case of use please override
	}

	@Override
	public void onSystemIsUp() throws OSHException {
		this.driverRegistry = getOSH().getDriverRegistry();
	}

	@Override
	public void onSystemHalt() throws OSHException {
		//...in case of use please override
	}

	@Override
	public void onSystemResume() throws OSHException {
		//...in case of use please override
	}

	@Override
	public void onSystemError() throws OSHException {
		//...in case of use please override
	}

	@Override
	public void onNextTimePeriod() throws OSHException {
		//...in case of use please override
	}


	// HELPER METHODS
	
	protected ArrayList<UUID> parseUUIDArray(String parameter) throws OSHException {
		try {
			ArrayList<UUID> list = UUIDLists.parseUUIDArray(parameter);
			return list;
		} catch( IllegalArgumentException e ) {
			throw new OSHException(e);
		}
	}
	
}
