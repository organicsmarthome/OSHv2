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

package osh.commanager;

import java.util.UUID;

import osh.core.IOSHOC;
import osh.core.com.AbstractComManager;
import osh.datatypes.dof.CarConfigurationExchange;
import osh.datatypes.dof.DofStatesStateExchange;
import osh.hal.exchange.CarComExchange;
import osh.hal.exchange.DofComExchange;
import osh.hal.exchange.IHALExchange;


/**
 * 
 * @author Till Schuberth, Ingo 
 *
 */
public class DoFProviderComManager extends AbstractComManager {

	
	/**
	 * CONSTRUCTOR
	 * @param controllerbox
	 * @param uuid
	 */
	public DoFProviderComManager(IOSHOC controllerbox,
			UUID uuid) {
		super(controllerbox, uuid);
	}

	
	@Override
	public void onDriverUpdate(IHALExchange exchangeObject) {
		
		if ( exchangeObject instanceof DofComExchange ) {
			DofComExchange dce = (DofComExchange) exchangeObject;
			DofStatesStateExchange dofsex = new DofStatesStateExchange(getUUID(), dce.getTimestamp());
			dofsex.setDevice1stDegreeOfFreedom(dce.getDevice1stDegreeOfFreedom());
			dofsex.setDevice2ndDegreeOfFreedom(dce.getDevice2ndDegreeOfFreedom());
			getOCRegistry().setStateOfSender(DofStatesStateExchange.class, dofsex);
		}
		
		if ( exchangeObject instanceof CarComExchange ) {
			CarComExchange cce = (CarComExchange) exchangeObject;
			CarConfigurationExchange cconf = new CarConfigurationExchange(getUUID(), cce.getTimestamp());
			cconf.setDepatureTime(cce.getDepatureTime());
			cconf.setMinimumRange(cce.getMinimumRange());
			getOCRegistry().setStateOfSender(CarConfigurationExchange.class, cconf);
		}
	}

}
