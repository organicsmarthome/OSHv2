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

package osh.comdriver;

import java.util.UUID;

import osh.core.IOSHOC;
import osh.core.com.AbstractComManager;
import osh.core.com.interfaces.IEventReceiver;
import osh.core.exceptions.OSHException;
import osh.datatypes.registry.EventExchange;
import osh.hal.exchange.IHALExchange;


/**
 * 
 * @author Florian Allerding, Ingo Mauser
 *
 */
public abstract class LoggerComManager extends AbstractComManager implements IEventReceiver {

	/**
	 * CONSTRUCTOR
	 * @param controllerbox
	 * @param uuid
	 */
	public LoggerComManager(IOSHOC controllerbox, UUID uuid) {
		super(controllerbox, uuid);
	}

	
	@Override
	public void onSystemIsUp() throws OSHException {
		super.onSystemIsUp();
	}

	@Override
	public void onQueueEventReceived(EventExchange ex) throws OSHException {
		//NOTHING
	}

	@Override
	public void onDriverUpdate(IHALExchange exchangeObject) {
		//NOTHING
	}

	@Override
	public void onNextTimePeriod() throws OSHException {
		super.onNextTimePeriod();
	}
	
}
