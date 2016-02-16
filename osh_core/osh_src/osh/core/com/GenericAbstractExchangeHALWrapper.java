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

package osh.core.com;

import java.util.UUID;

import osh.datatypes.AbstractExchange;
import osh.hal.exchange.IHALExchange;


/**
 * 
 * @author Till Schuberth
 *
 */
@Deprecated
public class GenericAbstractExchangeHALWrapper implements IHALExchange {

	private AbstractExchange ex;
	private UUID commdev;
	private long timestamp;
	
	
	public GenericAbstractExchangeHALWrapper(UUID commdev, long timestamp, AbstractExchange ex) {
		this.ex = ex;
		this.commdev = commdev;
		this.timestamp = timestamp;
	}
	
	
	@Override
	public UUID getDeviceID() {
		return commdev;
	}

	@Override
	public Long getTimestamp() {
		return timestamp;
	}
	
	public AbstractExchange getAbstractExchange() {
		return ex;
	}

}
