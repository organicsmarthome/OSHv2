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

package osh.core.threads;

import osh.core.EventQueue;
import osh.core.com.interfaces.IEventReceiver;
import osh.core.com.interfaces.IEventTypeReceiver;
import osh.core.exceptions.OSHException;
import osh.datatypes.registry.EventExchange;
import osh.registry.EventReceiverType;
import osh.registry.EventReceiverWrapper;
import osh.registry.ExchangeWrapper;

/**
 * Invokes a {@link IEventReceiver} when new events are available.
 * 
 * A concrete strategy in the strategy pattern.
 * 
 * @author Kaibin Bao
 *
 */
@SuppressWarnings("deprecation")
public class EventQueueSubscriberInvoker extends InvokerEntry<EventReceiverWrapper> {
	
	private EventQueue eventqueue;

	/* CONSTRUCTOR */
	public EventQueueSubscriberInvoker(EventReceiverWrapper eventQueueSubscriber,
			EventQueue eventqueue) {
		super(eventQueueSubscriber);
		
		this.eventqueue = eventqueue;
	}

	@Override
	public boolean shouldInvoke() {
		if( eventqueue.isEmpty() ) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * This function only exists because the compiler cannot infer the type parameters in this situation
	 */
	private <T extends EventExchange> void wildcardHelper(IEventTypeReceiver receiver, ExchangeWrapper<T> wrapper) throws OSHException {
		receiver.onQueueEventTypeReceived(wrapper.getType(), wrapper.getExchange());
	}

	@Override
	public void invoke() throws OSHException {
		ExchangeWrapper<? extends EventExchange> ex;
		while ((ex = eventqueue.getNext()) != null) {
			EventReceiverWrapper sub = getSubscriber();
			synchronized (sub.getSyncObject()) {
				if (sub.getType() == EventReceiverType.IEVENTRECEIVER) {
					sub.getEventReceiver().onQueueEventReceived(ex.getExchange());
				} else if (sub.getType() == EventReceiverType.IEVENTTYPERECEIVER) {
					wildcardHelper(sub.getEventTypeReceiver(), ex);
				} else {
					throw new NullPointerException("type is null"); //cannot happen if you don't changed something
				}
			}
		}
	}
	
	@Override
	public Object getSyncObject() {
		return eventqueue;
	}

	@Override
	public String getName() {
		return "EventQueueSubscriberInvoker for " + getSubscriber().getClass().getName();
	}
	
	/* Delegate to eventQueueSubscriber for HashMap */
	
	@Override
	public int hashCode() {
		return getSubscriber().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if( obj == null )
			return false;
		if( obj instanceof EventQueueSubscriberInvoker )
			return getSubscriber().equals(((EventQueueSubscriberInvoker) obj).getSubscriber());
		else
			return false;
	}
}
