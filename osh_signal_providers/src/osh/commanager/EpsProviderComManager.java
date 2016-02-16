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

import java.util.Map.Entry;
import java.util.UUID;

import osh.core.IOSHOC;
import osh.core.com.AbstractComManager;
import osh.core.com.interfaces.IHasState;
import osh.core.exceptions.OSHException;
import osh.datatypes.VirtualCommodity;
import osh.datatypes.limit.PriceSignal;
import osh.datatypes.registry.oc.details.utility.EpsStateExchange;
import osh.hal.exchange.EpsComExchange;
import osh.hal.exchange.IHALExchange;


/**
 * 
 * @author Ingo Mauser
 *
 */
public class EpsProviderComManager 
				extends AbstractComManager 
				implements IHasState {

	
	/**
	 * CONSTRUCTOR
	 * @param controllerbox
	 * @param uuid
	 */
	public EpsProviderComManager(IOSHOC controllerbox, UUID uuid) {
		super(controllerbox, uuid);
	}
	
	
	/**
	 * Receive data from ComDriver
	 */
	@Override
	public void onDriverUpdate(IHALExchange exchangeObject) {
		
		// receive signal from ComDriver as OX-object
		if (exchangeObject instanceof EpsComExchange) {
			
			getGlobalLogger().logInfo("SmartHome received new EPS signal...");
			EpsComExchange ox = (EpsComExchange) exchangeObject;
			
			// set states in oc registry
			EpsStateExchange pricedetails = new EpsStateExchange(
					getUUID(), 
					ox.getTimestamp());
			
			for (Entry<VirtualCommodity,PriceSignal> e : ox.getPriceSignals().entrySet()) {
				pricedetails.setPriceSignal(e.getKey(), e.getValue());
			}
			
			getOCRegistry().setState(EpsStateExchange.class, this, pricedetails);
		}
		else {
			try {
				throw new OSHException("Signal unknown");
			} 
			catch (OSHException e) {
				e.printStackTrace();
			}
		}
	}
	
}
