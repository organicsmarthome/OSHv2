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

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.UUID;

import osh.datatypes.registry.CommandExchange;
import osh.datatypes.registry.EventExchange;
import osh.datatypes.registry.StateChangedExchange;
import osh.datatypes.registry.StateExchange;
import osh.registry.ExchangeWrapper;


/**
 * 
 * @author Till Schuberth, Ingo Mauser
 *
 */
public class EventQueue {
	
	public static final int MAXSIZE = 1024;
	
	private String name;
	private OSHGlobalLogger logger;
	private Queue<ExchangeWrapper<? extends EventExchange>> queue = new ArrayDeque<>();
	
	public EventQueue(OSHGlobalLogger logger) {
		this(logger, "anonymous");
	}
	
	public EventQueue(OSHGlobalLogger logger, String name) {
		if (logger == null) throw new NullPointerException("logger is null");
		this.logger = logger;
		this.name = name;
	}
	
	public synchronized <T extends EventExchange, U extends T> void enqueue(Class<T> type, U ex) {
		queue.add(new ExchangeWrapper<T>(type, ex));
		if (queue.size() > MAXSIZE) {
			
			String append = "";
			if (StateChangedExchange.class.isAssignableFrom(type)) {
				UUID uuid = ((StateChangedExchange) ex).getStatefulentity();
				Class<? extends StateExchange> statetype = ((StateChangedExchange) ex).getType();
				append = " Statefulentity: " + uuid + ", state type: " + statetype;
			} else if (CommandExchange.class.isAssignableFrom(type)) {
				append = " Receiver: " + ((CommandExchange) ex).getReceiver();
			}
			
			logger.logError(
					"Queue overflow for " 
					+ name 
					+  ", size > MAXSIZE (" 
					+ MAXSIZE 
					+ "), throwing away old events. New event "
					+ ex.getClass().getName()
					+ " from " 
					+ ex.getSender()
					+ append);
			//throw away
			while (queue.size() > MAXSIZE) queue.poll();
		}
	}
	
	public synchronized ExchangeWrapper<? extends EventExchange> getNext() {
		return queue.poll();
	}
	
	public synchronized ExchangeWrapper<? extends EventExchange> peekNext() {
		return queue.peek();
	}
	
	public synchronized void removeFirst() {
		queue.poll();
	}
	
	public synchronized boolean isEmpty() {
		return queue.isEmpty();
	}
	
}
