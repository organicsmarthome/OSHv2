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

import osh.core.IOSHOC;
import osh.core.exceptions.OSHException;
import osh.datatypes.mooex.IModelOfObservationExchange;
import osh.hal.exchange.IHALExchange;


/**
 * superclass for all local controllers
 * @author florian.allerding@kit.edu
 */
public abstract class LocalController extends Controller implements IOCDataPublisher {
	
	private LocalOCUnit assignedOCUnit;

	private IOCDataSubscriber monitorObject;
	
	
	/**
	 * CONSTRUCTOR
	 * @param controllerbox
	 */
	public LocalController(IOSHOC controllerbox){
		super(controllerbox);
	}

	
	/**
	 * get the local o/c-unit to which thing controller belongs...
	 * @return
	 */
	public final LocalOCUnit getAssignedOCUnit() {
		return assignedOCUnit;
	}
	
	/**
	 * returns the local observerUnit according to this controller
	 * @return
	 */
	public final LocalObserver getLocalObserver(){
		return this.getAssignedOCUnit().localObserver;
	}

	/**
	 * For the communication between the observer and the controller
	 * The observer can invoke this method to get some observed data.
	 * Only an interface will be communicated, so feel free to create some own classes...
	 * @return
	 */
	public IModelOfObservationExchange getDataFromLocalObserver(){
			return this.getLocalObserver().getObservedModelData();
	}
	
	@Override
	public void setOcDataSubscriber(IOCDataSubscriber monitorObject) {
		this.monitorObject = monitorObject;
		
	}

	@Override
	public final void removeOcDataSubscriber(IOCDataSubscriber monitorObject) {
		//TODO: WHY IS NOTHING HERE???
	}

	/**
	 * Calls onControllerRequest() of the Driver
	 */
	@Override
	public final void updateOcDataSubscriber(IHALExchange halexchange) {
		try {
			this.monitorObject.onDataFromOcComponent(halexchange);
		} catch (OSHException e) {
			this.getGlobalLogger().logError("HAL communnication error", e);
		}
	}
	
	protected final void assignLocalOCUnit(LocalOCUnit localOCUnit){
		this.assignedOCUnit = localOCUnit;
	}

	public UUID getDeviceID() {
		return (assignedOCUnit != null) ? assignedOCUnit.getUnitID() : null;
	}
	
}
