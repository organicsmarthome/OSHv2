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

package osh.hal.exchange;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import osh.datatypes.registry.StateExchange;
import osh.hal.exchange.HALComExchange;


/**
 * 
 * @author inspired by Ingo Mauser
 *
 */
public class GUIStatesComExchange extends HALComExchange {

	private Set<Class<? extends StateExchange>> types;
	private Map<UUID, ? extends StateExchange> states;
	private Class<? extends StateExchange> driverstatetype;
	private boolean ocmode;

	
	/**
	 * CONSTRUCTOR
	 * @param deviceID
	 * @param timestamp
	 */
	public GUIStatesComExchange(
			UUID deviceID, 
			Long timestamp,
			Set<Class<? extends StateExchange>> types,
			Map<UUID, ? extends StateExchange> states) {
		super(deviceID, timestamp);

		ocmode = true;
		if (types == null) {
			this.types = new HashSet<>();
		} else {
			synchronized ( types ) {
				@SuppressWarnings("unchecked")
				Class<? extends StateExchange>[] dte = (Class<? extends StateExchange>[]) types.toArray(new Class<?>[0]);

				Set<Class<? extends StateExchange>> clonedTypes = new HashSet<>();

				for (Class<? extends StateExchange> e : dte) {
					clonedTypes.add(e); //no cloning for class available
				}

				this.types = clonedTypes;
			}
		}

		if (states == null) {
			this.states = new HashMap<>();
		} else {
			synchronized ( states ) {
				@SuppressWarnings("unchecked")
				Entry<UUID, ? extends StateExchange>[] dte = (Entry<UUID, ? extends StateExchange>[]) states.entrySet().toArray(new Entry<?, ?>[0]);

				Map<UUID, StateExchange> clonedStates = new HashMap<>();

				for (Entry<UUID, ? extends StateExchange> e : dte) {
					clonedStates.put(e.getKey(), e.getValue().clone()); //no cloning for key
				}

				this.states = clonedStates;
			}
		}
	}
	
	public GUIStatesComExchange(
			UUID deviceID, 
			Long timestamp,
			Class<? extends StateExchange> driverstatetype) {
		super(deviceID, timestamp);

		ocmode = false;
		this.driverstatetype = driverstatetype; //cloning not possible
	}
	
	public Set<Class<? extends StateExchange>> getTypes() {
		return types;
	}

	public Map<UUID, ? extends StateExchange> getStates() {
		return states;
	}

	public Class<? extends StateExchange> getDriverstatetype() {
		return driverstatetype;
	}

	public boolean isOcMode() {
		return ocmode;
	}

}
