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

package osh.core.bus;

import java.util.UUID;

import osh.ILifeCycleListener;
import osh.IRealTimeSubscriber;
import osh.OSHComponent;
import osh.core.IOSHOC;
import osh.core.OCComponent;
import osh.core.exceptions.OSHException;
import osh.core.oc.IOCDataPublisher;
import osh.core.oc.IOCDataSubscriber;
import osh.hal.HALBusDriver;
import osh.hal.IDriverDataSubscriber;
import osh.hal.exceptions.HALDriverException;
import osh.hal.exchange.IHALExchange;


public abstract class AbstractBusManager extends OCComponent 
							implements	IRealTimeSubscriber, 
										ILifeCycleListener, 
										IDriverDataSubscriber, 
										IOCDataPublisher  {

	private HALBusDriver busDriver;
	private UUID uuid;
	
	
	/**
	 * CONSTRUCTOR
	 * @param controllerbox
	 */
	public AbstractBusManager(IOSHOC controllerbox, UUID uuid) {
		super(controllerbox);
		this.uuid = uuid;
	}
	
	
	@Override
	public IOSHOC getOSH() {
		return (IOSHOC) super.getOSH();
	}

	
	@Override
	public OSHComponent getSyncObject() {
		return this;
	}

	@Override
	public void setOcDataSubscriber(IOCDataSubscriber monitorObject) {
		this.busDriver = (HALBusDriver) monitorObject;
	}

	@Override
	public void removeOcDataSubscriber(IOCDataSubscriber monitorObject) {
		this.busDriver = null;
	}

	
	public HALBusDriver getBusDriver() {
		return this.busDriver;
	}
	
	public UUID getUUID() {
		return this.uuid;
	}
	
	
	@Override
	public void updateOcDataSubscriber(IHALExchange halexchange) throws HALDriverException,
			OSHException {
		if (this.busDriver != null) {
			this.busDriver.onDataFromOcComponent(halexchange);
		}
		else {
			//NOTHING
			//TODO: error message/exception
		}
	}

	@Override
	public final void onDataFromHalDriver(IHALExchange exchangeObject) {
		synchronized(getSyncObject()) {
			onDriverUpdate(exchangeObject);
		}
	}
	
	public abstract void onDriverUpdate(IHALExchange exchangeObject);

	@Override
	public void onSystemRunning() throws OSHException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSystemShutdown() throws OSHException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSystemIsUp() throws OSHException {
	}

	@Override
	public void onSystemHalt() throws OSHException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSystemResume() throws OSHException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSystemError() throws OSHException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNextTimePeriod() throws OSHException {
		// TODO Auto-generated method stub
		
	}

}
