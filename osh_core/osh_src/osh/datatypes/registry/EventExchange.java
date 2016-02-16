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

package osh.datatypes.registry;

import java.util.UUID;

import osh.datatypes.AbstractExchange;


/**
 * 
 * @author Till Schuberth
 *
 */
public abstract class EventExchange extends AbstractExchange implements Cloneable {

	public EventExchange(UUID sender, long timestamp) {
		super(sender, timestamp);
	}
	
	@Override
	public EventExchange clone() {
		try {
			return (EventExchange) super.clone();
		} 
		catch (CloneNotSupportedException e) {
			throw new RuntimeException("subclass doesn't provide proper cloning functionality", e);
		}
	}

	/** tries to cast this object to the given event type.
	 * @param type the type of this event
	 * @return This object casted to the event type or null if this is not possible.
	 */
	public <T extends EventExchange> T castToType(Class<T> type) {
		if (type.isAssignableFrom(this.getClass())) {
			return type.cast(this);
		} else {
			return null;
		}
	}

}
