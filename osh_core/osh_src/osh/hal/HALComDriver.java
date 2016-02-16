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
import osh.configuration.system.ComDeviceTypes;
import osh.core.IOSH;
import osh.core.com.AbstractComManager;
import osh.core.oc.IOCDataSubscriber;
import osh.hal.exchange.IHALExchange;

/**
 * Superclass for all ComDrivers (devices with ComManager but without full O/C-Unit)
 * @author Till Schuberth, Ingo Mauser
 *
 */
public abstract class HALComDriver extends HALDriver implements IDriverDataPublisher, IOCDataSubscriber {

	private AbstractComManager assignedComManager;
	private ComDeviceTypes comDeviceType;
	
	
	public HALComDriver(
			IOSH controllerbox, 
			UUID deviceID, 
			OSHParameterCollection driverConfig) {
		super(controllerbox, deviceID, driverConfig);
	}
	
	/**
	 * @return the assigned ComManager
	 */
	public AbstractComManager getAssignedComManager() {
		return assignedComManager;
	}


	/**
	 * @return the timerDriver
	 */
	public HALRealTimeDriver getTimerDriver() {
		return getOSH().getTimer();
	}


	public ComDeviceTypes getComDeviceType() {
		return comDeviceType;
	}

	public void setComDeviceType(ComDeviceTypes comDeviceType) {
		this.comDeviceType = comDeviceType;
	}

	// HALdataObject
	/**
	 * receive data from ComManager
	 */
	@Override
	public final void onDataFromOcComponent(IHALExchange exchangeObject) {
		updateDataFromComManager(exchangeObject);
	}
	
	public abstract void updateDataFromComManager(IHALExchange exchangeObject);
	
	// HALdataSubject
	
	@Override
	public final void setHalDataSubscriber(IDriverDataSubscriber monitorObject) {
		this.assignedComManager = (AbstractComManager) monitorObject;
	}
	
	@Override
	public final void removeHalDataSubscriber(IDriverDataSubscriber monitorObject) {
		// TODO Auto-generated method stub
		this.assignedComManager = (AbstractComManager) monitorObject;
		//FIXME Is this really correct?
	}
	
	@Override
	public final void updateHalDataSubscriber(IHALExchange halexchange) {
		this.assignedComManager.onDataFromHalDriver(halexchange);
	}
	
	public final void notifyComManager(IHALExchange exchangeObject){
		updateHalDataSubscriber(exchangeObject);
	}

}
