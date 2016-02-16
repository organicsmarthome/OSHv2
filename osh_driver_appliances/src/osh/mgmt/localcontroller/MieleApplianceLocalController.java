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

package osh.mgmt.localcontroller;

import java.util.UUID;

import osh.OSHComponent;
import osh.core.IOSHOC;
import osh.core.com.interfaces.IEventReceiver;
import osh.core.com.interfaces.IHasState;
import osh.core.exceptions.OSHException;
import osh.core.oc.LocalController;
import osh.datatypes.en50523.EN50523DeviceState;
import osh.datatypes.en50523.EN50523OIDExecutionOfACommandCommands;
import osh.datatypes.registry.EventExchange;
import osh.datatypes.registry.globalcontroller.EASolutionCommandExchange;
import osh.datatypes.registry.localcontroller.ExpectedStartTimeExchange;
import osh.hal.exchange.MieleApplianceControllerExchange;
import osh.mgmt.localobserver.MieleApplianceLocalObserver;
import osh.mgmt.localobserver.eapart.MieleSolution;


/**
 * 
 * @author Florian Allerding, Kaibin Bao, Till Schuberth, Ingo Mauser
 *
 */
public class MieleApplianceLocalController extends LocalController implements IEventReceiver, IHasState {
	
	/** use private setter setStartTime() */
	private long startTime = -1;
	
	
	/**
	 * CONSTRUCTOR
	 * @param controllerbox
	 */
	public MieleApplianceLocalController(IOSHOC controllerbox) {
		super(controllerbox);
	}
	
	
	@Override
	public void onSystemIsUp() throws OSHException {
		super.onSystemIsUp();
		
		// register for onNextTimePeriod()
		getTimer().registerComponent(this, 1);
		
		getOCRegistry().register(EASolutionCommandExchange.class, this);
	}

	/**
	 * tell "Erna" that she has to work...
	 */
	private void callDevice() {
		MieleApplianceControllerExchange halControllerExchangeObject
			= new MieleApplianceControllerExchange(
					this.getDeviceID(), 
					getTimer().getUnixTime(), 
					EN50523OIDExecutionOfACommandCommands.START);
		this.updateOcDataSubscriber(halControllerExchangeObject);
	}

	
	@Override
	public void onQueueEventReceived(EventExchange ex) throws OSHException {
		if (ex instanceof EASolutionCommandExchange) {
			@SuppressWarnings("unchecked")
			EASolutionCommandExchange<MieleSolution> solution = (EASolutionCommandExchange<MieleSolution>) ex;
			if (!solution.getReceiver().equals(getDeviceID()) || solution.getPhenotype() == null) return;
			
			setStartTime(solution.getPhenotype().startTime);
			((MieleApplianceLocalObserver) getLocalObserver()).updateDofExchange();

			//System.out.println(getDeviceID() + " got new start time: " + startTime);
		}
	}
	
	@Override
	public void onNextTimePeriod() throws OSHException {

		long now = getTimer().getUnixTime();
		
		EN50523DeviceState curstate = ((MieleApplianceLocalObserver) getLocalObserver()).getCurrentState();
		
		if ( ( curstate == EN50523DeviceState.PROGRAMMED 
				|| curstate == EN50523DeviceState.PROGRAMMEDWAITINGTOSTART )
				&& getStartTime() > 0 ) {
			if (getStartTime() < now) { //already to be started?
				//start device
				callDevice();
				setStartTime(-1);
			}
		}
		else {
			setStartTime(-1);
		}
	}
	
	@Override
	public UUID getUUID() {
		return getDeviceID();
	}

	private long getStartTime() {
		return this.startTime;
	}
	
	private void setStartTime(long startTime) {
		this.startTime = startTime;

		getOCRegistry().setState(
				ExpectedStartTimeExchange.class,
				this,
				new ExpectedStartTimeExchange(
						getUUID(),
						getTimer().getUnixTime(), 
						startTime));
		
		getOCRegistry().sendEvent(
				ExpectedStartTimeChangedExchange.class, 
				new ExpectedStartTimeChangedExchange(
						getUUID(), 
						getTimer().getUnixTime(), 
						startTime));
	}

	@Override
	public OSHComponent getSyncObject() {
		return this;
	}
	
}
