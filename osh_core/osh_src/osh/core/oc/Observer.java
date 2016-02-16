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

import osh.ILifeCycleListener;
import osh.IRealTimeSubscriber;
import osh.OSHComponent;
import osh.core.IOSHOC;
import osh.core.OCComponent;
import osh.core.exceptions.OSHException;
import osh.datatypes.mooex.IModelOfObservationExchange;
import osh.datatypes.mooex.ModelOfObservationType;

/**
 * superclass for all observer
 * @author florian
 * abstract superclass for all observers
 */
public abstract class Observer extends OCComponent implements IRealTimeSubscriber, ILifeCycleListener { 
	
	protected ModelOfObservationType modelOfObservationType; 
	
	/**
	 * CONSTRUCTOR
	 * @param controllerbox
	 */
	public Observer(IOSHOC controllerbox) {
		super(controllerbox);
	}
	
	/**
	 * For the communication between the observer and the controller
	 * The observer (controller?) can invoke this method to get some observed data.
	 * Only an interface will be communicated, so feel free to create some own classes...
	 * @return
	 */
	public abstract IModelOfObservationExchange getObservedModelData(ModelOfObservationType type);
	
	public final IModelOfObservationExchange getObservedModelData() {
		synchronized (getSyncObject()) {
			return getObservedModelData(modelOfObservationType);
		}
	}

	public ModelOfObservationType getModelOfObservationType() {
		return modelOfObservationType;
	}

	public void setModelOfObservationType(
			ModelOfObservationType modelOfObservationType) {
		this.modelOfObservationType = modelOfObservationType;
	}

	@Override
	public void onNextTimePeriod() throws OSHException {
		//...in case of use please override
	}

	@Override
	public void onSystemError() throws OSHException {
		//...in case of use please override
	}

	@Override
	public void onSystemHalt() throws OSHException {
		//...in case of use please override
	}

	@Override
	public void onSystemRunning() throws OSHException {
		//...in case of use please override
	}

	@Override
	public void onSystemIsUp() throws OSHException {
		//...in case of use please override
	}

	@Override
	public void onSystemResume() throws OSHException {
		//...in case of use please override
	}

	@Override
	public void onSystemShutdown() throws OSHException {
		//...in case of use please override
	}

	
	@Override
	public OSHComponent getSyncObject() {
		return this;
	}

	
}
