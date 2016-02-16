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

import osh.core.IOSHOC;
import osh.core.com.interfaces.IEventReceiver;
import osh.core.exceptions.OSHException;
import osh.core.oc.LocalController;
import osh.datatypes.registry.EventExchange;
import osh.datatypes.registry.globalcontroller.PvCommandExchange;
import osh.hal.exchange.PvHALControllerExchange;


/**
 * 
 * @author Ingo Mauser
 *
 */
public class PvLocalController extends LocalController implements IEventReceiver {

	
	/**
	 * CONSTRUCTOR
	 * @param controllerbox
	 */
	public PvLocalController(IOSHOC controllerbox) {
		super(controllerbox);
	}

	
	@Override
	public void onNextTimePeriod() throws OSHException {
		super.onNextTimePeriod();
	}
	
	@Override
	public void onSystemIsUp() throws OSHException {
		super.onSystemIsUp();
		
		getTimer().registerComponent(this, 1);
		
		getOCRegistry().register(PvCommandExchange.class, this);
	}

	@Override
	public void onQueueEventReceived(EventExchange ex) throws OSHException {
			if (ex instanceof PvCommandExchange) {
				PvCommandExchange _cmd = (PvCommandExchange) ex;
				if (!_cmd.getReceiver().equals(getDeviceID())) return;
				
				PvHALControllerExchange _cx = new PvHALControllerExchange(
						this.getDeviceID(), 
						getTimer().getUnixTime(), 
						_cmd.getNewPvSwitchedOn(), 
						(int) Math.round(_cmd.getReactivePowerTargetValue()));
				this.updateOcDataSubscriber(_cx);
			}
	}

	@Override
	public UUID getUUID() {
		return getDeviceID();
	}
	
}
