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

package osh.datatypes.logger;

import java.util.HashMap;
import java.util.UUID;

import osh.datatypes.VirtualCommodity;
import osh.datatypes.limit.PowerLimitSignal;
import osh.datatypes.limit.PriceSignal;
import osh.hal.exchange.HALExchange;

/**
 * 
 * @author Ingo Mauser
 *
 */
public class LoggerEpsPlsHALExchange extends HALExchange {

	private HashMap<VirtualCommodity,PriceSignal> ps;
	private HashMap<VirtualCommodity,PowerLimitSignal> pwrLimit;
	
	
	/**
	 * CONSTRUCTOR
	 * @param sender
	 * @param timestamp
	 * @param ps
	 * @param pwrLimit
	 */
	public LoggerEpsPlsHALExchange(
			UUID sender, 
			long timestamp,
			HashMap<VirtualCommodity,PriceSignal> ps,
			HashMap<VirtualCommodity,PowerLimitSignal> pwrLimit) {
		super(sender, timestamp);
		
		this.ps = ps;
		this.pwrLimit = pwrLimit;
	}


	public HashMap<VirtualCommodity, PriceSignal> getPs() {
		return ps;
	}

	public void setPs(HashMap<VirtualCommodity, PriceSignal> ps) {
		this.ps = ps;
	}

	public HashMap<VirtualCommodity,PowerLimitSignal> getPwrLimit() {
		return pwrLimit;
	}

	public void setPwrLimit(HashMap<VirtualCommodity,PowerLimitSignal> pwrLimit) {
		this.pwrLimit = pwrLimit;
	}
	
}
