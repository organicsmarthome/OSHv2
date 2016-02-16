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

package osh.core.oc;

import java.util.UUID;

import osh.configuration.system.DeviceClassification;
import osh.configuration.system.DeviceTypes;
import osh.core.IOSHOC;
import osh.core.exceptions.OCUnitException;
import osh.hal.HALRealTimeDriver;
import osh.hal.IDriverDataSubscriber;
import osh.hal.exchange.IHALExchange;


/**
 * superclass for the local observer at the controllerbox
 * 
 * @author Florian Allerding, Kaibin Bao, Till Schuberth, Ingo Mauser
 * @category smart-home ControllerBox
 */
public abstract class LocalObserver extends Observer implements IDriverDataSubscriber {
	
	// the local O/C-Unit
	private LocalOCUnit assignedOCUnit;
	
	// the GlobalObserver
	/** legacy stuff, get reference over the OSH object */
	@Deprecated
	protected UUID globalOCUnitUUID;

	// current representation of the state from the observed device
	private volatile IHALExchange observerDataObject;
	

	/**
	 * CONSTRUCTOR
	 * @param controllerbox
	 */
	public LocalObserver(IOSHOC controllerbox) {
		super(controllerbox);
	}

	
	protected void assignLocalOCUnit(LocalOCUnit localOCUnit) {
		this.assignedOCUnit = localOCUnit;
		// this.deviceID = this.assignedOCUnit.getDeviceID();
	}

	public LocalOCUnit getAssignedOCUnit() {
		return assignedOCUnit;
	}
	
	/**
	 * returns the local controllerUnit according to this observer
	 * @return
	 */
	public LocalController getLocalController() {
		return this.getAssignedOCUnit().localController;
	}

	public UUID getDeviceID() {
		return this.assignedOCUnit.getUnitID();
	}
	
	public DeviceTypes getDeviceType () {
		return this.assignedOCUnit.getDeviceType();
	}
	
	public DeviceClassification getDeviceClassification () {
		return this.assignedOCUnit.getDeviceClassification();
	}
	

	/**
	 * get the current representation of the state from the observed device
	 * @return current representation of the state from the observed device
	 */
	public IHALExchange getObserverDataObject() {
		return observerDataObject;
	}

	public HALRealTimeDriver getSystemTimer() {
		return getOSH().getTimer();
	}

	/**
	 * is invoked every time when the state of the device changes
	 */
	public abstract void onDeviceStateUpdate() throws OCUnitException;

	/** Use the OSH object to get the global observer UUID */
	@Deprecated
	protected void registerGlobalObserver(UUID globalOCUnitUUID) {
		this.globalOCUnitUUID = globalOCUnitUUID;
	}

	@Override
	public final void onDataFromHalDriver(IHALExchange exchangeObject) throws OCUnitException {
		// cast to the observer object
		synchronized (getSyncObject()) {
			this.observerDataObject = exchangeObject;
			this.onDeviceStateUpdate();
		}
	}


	
}
