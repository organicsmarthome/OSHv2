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

import java.util.UUID;

import osh.configuration.OSHParameterCollection;
import osh.configuration.system.BusDeviceTypes;
import osh.core.IOSH;
import osh.core.bus.AbstractBusManager;
import osh.core.exceptions.OSHException;
import osh.core.oc.IOCDataSubscriber;
import osh.hal.exchange.IHALExchange;

/**
 * 
 * @author Ingo Mauser
 *
 */
public abstract class HALBusDriver extends HALDriver implements IDriverDataPublisher, IOCDataSubscriber {

	private AbstractBusManager assignedBusManager;
	private BusDeviceTypes busDeviceType;
	
	/**
	 * CONSTRUCTOR
	 * @param controllerbox
	 * @param deviceID
	 * @param driverConfig
	 */
	public HALBusDriver(IOSH controllerbox, UUID deviceID,
			OSHParameterCollection driverConfig) {
		super(controllerbox, deviceID, driverConfig);
		// currently NOTHING
	}

	
	/**
	 * @return the assigned ComManager
	 */
	public AbstractBusManager getAssignedBusManager() {
		return assignedBusManager;
	}
	
	public void setBusDeviceType(BusDeviceTypes busDeviceType) {
		this.busDeviceType = busDeviceType;
	}

	public BusDeviceTypes getBusDeviceType() {
		return busDeviceType;
	}
	
	// HALdataObject
		/**
		 * receive data from BusManager
		 */
	@Override
	public void onDataFromOcComponent(IHALExchange exchangeObject)
			throws OSHException {
		updateDataFromBusManager(exchangeObject);
	}
	
	public abstract void updateDataFromBusManager(IHALExchange exchangeObject);

	// HALdataSubject
	
	@Override
	public final void setHalDataSubscriber(IDriverDataSubscriber monitorObject) {
		this.assignedBusManager = (AbstractBusManager) monitorObject;
	}
	
	@Override
	public final void removeHalDataSubscriber(IDriverDataSubscriber monitorObject) {
		this.assignedBusManager = null;
	}
	
	@Override
	public final void updateHalDataSubscriber(IHALExchange halexchange) {
		this.assignedBusManager.onDataFromHalDriver(halexchange);
	}
	
	public final void notifyBusManager(IHALExchange exchangeObject){
		updateHalDataSubscriber(exchangeObject);
	}

}
