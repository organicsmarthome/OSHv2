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

import osh.core.StateChangedEventSet;
import osh.core.exceptions.OSHException;
import osh.datatypes.registry.StateChangedExchange;
import osh.registry.EventReceiverType;
import osh.registry.EventReceiverWrapper;

/**
 * Invokes a {@link IEventReceiver} when new events are available.
 * 
 * A concrete strategy in the strategy pattern.
 * 
 * @author Kaibin Bao
 *
 */
public class StateSubscriberInvoker extends InvokerEntry<EventReceiverWrapper> {
	
	private StateChangedEventSet eventset;

	/* CONSTRUCTOR */
	public StateSubscriberInvoker(EventReceiverWrapper eventQueueSubscriber,
			StateChangedEventSet eventset) {
		super(eventQueueSubscriber);
		
		this.eventset = eventset;
	}

	@Override
	public boolean shouldInvoke() {
		if( eventset.isEmpty() ) {
			return false;
		} else {
			return true;
		}
	}

	@SuppressWarnings("deprecation") //usage of legacy component IEventReceiver
	@Override
	public void invoke() throws OSHException {
		StateChangedExchange ex;
		while ((ex = eventset.getNext()) != null) {
			EventReceiverWrapper receiver = getSubscriber();
			synchronized (receiver.getSyncObject()) {
				if (receiver.getType() == EventReceiverType.IEVENTRECEIVER) {
					receiver.getEventReceiver().onQueueEventReceived(ex);
				} else if (receiver.getType() == EventReceiverType.IEVENTTYPERECEIVER) {
					receiver.getEventTypeReceiver().onQueueEventTypeReceived(StateChangedExchange.class, ex);
				} else {
					throw new IllegalStateException("type is not known");
				}
			}
		}
	}
	
	@Override
	public Object getSyncObject() {
		return eventset;
	}

	@Override
	public String getName() {
		return "StateSubscriberInvoker for " + getSubscriber().getClass().getName();
	}
	
	/* Delegate to eventQueueSubscriber for HashMap */
	
	@Override
	public int hashCode() {
		return getSubscriber().hashCode() ^ 0x50000000;
	}
	
	@Override
	public boolean equals(Object obj) {
		if( obj == null )
			return false;
		if( obj instanceof StateSubscriberInvoker )
			return getSubscriber().equals(((StateSubscriberInvoker) obj).getSubscriber());
		else
			return false;
	}
}
