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

package osh.registry;

import java.util.UUID;

import osh.IPromiseToEnsureSynchronization;
import osh.core.com.interfaces.IEventReceiver;
import osh.core.com.interfaces.IEventTypeReceiver;

/**
 * FOR INTERNAL USE ONLY
 */
@SuppressWarnings("deprecation")
public class EventReceiverWrapper implements IPromiseToEnsureSynchronization {
	private EventReceiverType type;
	private IEventReceiver eventreceiver = null;
	private IEventTypeReceiver eventtypereceiver = null;
	
	public EventReceiverWrapper(IEventReceiver eventreceiver) {
		super();
		this.type = EventReceiverType.IEVENTRECEIVER;
		this.eventreceiver = eventreceiver;
	}
	
	public EventReceiverWrapper(IEventTypeReceiver eventtypereceiver) {
		super();
		this.type = EventReceiverType.IEVENTTYPERECEIVER;
		this.eventtypereceiver = eventtypereceiver;
	}

	public EventReceiverType getType() {
		return type;
	}
	
	public IEventReceiver getEventReceiver() {
		return eventreceiver;
	}
	
	public IEventTypeReceiver getEventTypeReceiver() {
		return eventtypereceiver;
	}

	public UUID getUUID() {
		if (type == EventReceiverType.IEVENTRECEIVER) {
			return eventreceiver.getUUID();
		} else if (type == EventReceiverType.IEVENTTYPERECEIVER) {
			return eventtypereceiver.getUUID();
		} else {
			throw new NullPointerException("type is null");//should never happen
		}
	}
	
	public Object getSyncObject() {
		if (type == EventReceiverType.IEVENTRECEIVER) {
			return eventreceiver.getSyncObject();
		} else if (type == EventReceiverType.IEVENTTYPERECEIVER) {
			return eventtypereceiver.getSyncObject();
		} else {
			throw new NullPointerException("type is null");//should never happen
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		if (type == EventReceiverType.IEVENTRECEIVER) {
			result = prime * result
					+ ((eventreceiver == null) ? 0 : eventreceiver.hashCode());
		} else if (type == EventReceiverType.IEVENTTYPERECEIVER) {
			result = prime
					* result
					+ ((eventtypereceiver == null) ? 0 : eventtypereceiver
							.hashCode());
		}
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventReceiverWrapper other = (EventReceiverWrapper) obj;
		if (type == EventReceiverType.IEVENTRECEIVER) {
			if (eventreceiver == null) {
				if (other.eventreceiver != null)
					return false;
			} else if (!eventreceiver.equals(other.eventreceiver))
				return false;
		}
		if (type == EventReceiverType.IEVENTTYPERECEIVER) {
			if (eventtypereceiver == null) {
				if (other.eventtypereceiver != null)
					return false;
			} else if (!eventtypereceiver.equals(other.eventtypereceiver))
				return false;
		}
		if (type != other.type)
			return false;
		return true;
	}

}