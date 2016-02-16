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

package osh.busdriver;

import java.util.UUID;

import osh.core.IOSHOC;
import osh.core.bus.AbstractBusManager;
import osh.core.exceptions.OSHException;
import osh.hal.exchange.IHALExchange;


/**
 * Dummy busdriver manager for future use
 * @author Ingo Mauser
 *
 */
public class MieleGatewayBusManager extends AbstractBusManager {

	/**
	 * CONSTRUCTOR
	 * @param controllerbox
	 * @param uuid
	 */
	public MieleGatewayBusManager(IOSHOC controllerbox, UUID uuid) {
		super(controllerbox, uuid);
		// NOTHING
	}
	
	
	@Override
	public void onSystemIsUp() throws OSHException {
		super.onSystemIsUp();
		
//		getTimer().registerComponent(this, 1);
		
//		this.ocRegistry.register(NAME.class, this);
//		this.ocRegistry.registerStateChangeListener(NAME.class, this);
	}

	@Override
	public void onDriverUpdate(IHALExchange exchangeObject) {
		// NOTHING
	}

}
