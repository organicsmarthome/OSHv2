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

package osh.core;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import osh.datatypes.registry.StateChangedExchange;
import osh.datatypes.registry.StateExchange;


/**
 * 
 * @author Till Schuberth, Ingo Mauser, Kaibin Bao
 *
 */
public class StateChangedEventSet {

	public static final int MAXSIZE = 1024;
	public boolean overfull = false;
	
	private String name;
	private OSHGlobalLogger logger;
	private Set<StateChangedExchange> eventset = new HashSet<StateChangedExchange>();
	
	public StateChangedEventSet(OSHGlobalLogger logger) {
		this(logger, "anonymous");
	}
	
	public StateChangedEventSet(OSHGlobalLogger logger, String name) {
		if (logger == null) throw new NullPointerException("logger is null");
		this.logger = logger;
		this.name = name;
	}
	
	public synchronized void enqueue(StateChangedExchange ex) {
		eventset.add(ex);
		if (overfull == false && eventset.size() > MAXSIZE) {
			
			Class<? extends StateExchange> type = ex.getType();
			
			logger.logWarning(
					"Queue overfull for " 
					+ name 
					+  ", size > MAXSIZE (" 
					+ MAXSIZE 
					+ "). New event "
					+ ex.getClass().getName()
					+ " for "
					+ type
					+ " from " 
					+ ex.getSender());
			//throw away current
			//while (eventset.size() > MAXSIZE) eventset.poll();

			overfull = true;
		} else if ( overfull == true ) {
			logger.logWarning(
					"Queue for " + name + " normalized again.");
			overfull = false;
		}
	}
	
	public synchronized StateChangedExchange getNext() {
		Iterator<StateChangedExchange> first = eventset.iterator();
		if( first.hasNext() ) {
			StateChangedExchange ex = first.next();
			first.remove();
			return ex;
		} else {
			return null;
		}
	}
	
	public synchronized StateChangedExchange peekNext() {
		Iterator<StateChangedExchange> first = eventset.iterator();
		if( first.hasNext() ) {
			StateChangedExchange ex = first.next();
			return ex;
		} else {
			return null;
		}
	}
	
	public synchronized void removeFirst() {
		Iterator<StateChangedExchange> first = eventset.iterator();
		if( first.hasNext() ) {
			first.next();
			first.remove();
		}
	}
	
	public synchronized boolean isEmpty() {
		return eventset.isEmpty();
	}
	
}
