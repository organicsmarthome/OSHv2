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

/**
 * 
 * @author Florian Allerding, Kaibin Bao, Till Schuberth, Ingo Mauser
 *
 */
public class StateChangedExchange extends EventExchange {

	private Class<? extends StateExchange> type;
	private UUID statefulentity;
	
	
	/**
	 * CONSTRUCTOR
	 * @param timestamp
	 * @param type
	 * @param statefulentity
	 */
	public StateChangedExchange(long timestamp,
			Class<? extends StateExchange> type, UUID statefulentity) {
		super(null, timestamp);
		
		this.type = type;
		this.statefulentity = statefulentity;
	}
	

	public Class<? extends StateExchange> getType() {
		return type;
	}

	public UUID getStatefulentity() {
		return statefulentity;
	}

	// needed for StateChangedEventSet
	@Override
	public int hashCode() {
		return statefulentity.hashCode() ^ type.hashCode();
	}
	
	// needed for StateChangedEventSet
	@Override
	public boolean equals(Object obj) {
		if( obj == null )
			return false;
		if( !(obj instanceof StateChangedExchange) )
			return false;

		StateChangedExchange other = (StateChangedExchange) obj;
		
		return statefulentity.equals(other.statefulentity)
			&& type.equals(other.type);
	}
}
