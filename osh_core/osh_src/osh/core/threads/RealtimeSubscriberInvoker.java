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

import osh.IRealTimeSubscriber;
import osh.core.exceptions.OSHException;
import osh.hal.HALRealTimeDriver;

/**
 * Invokes a {@link IRealTimeSubscriber} when the time has come.
 * 
 * A concrete strategy in the strategy pattern.
 * 
 * @author Kaibin Bao
 *
 */
public class RealtimeSubscriberInvoker extends InvokerEntry<IRealTimeSubscriber> {
	private HALRealTimeDriver realTimeDriver;

	private long invokeInterval;
	private long lastInvokeTimestamp; //usually 0 (iff: 1.1.1970)

	/* CONSTRUCTOR */
	/**
	 * 
	 * @param realTimeSubscriber
	 * @param invokeInterval
	 * @param realTimeDriver
	 */
	public RealtimeSubscriberInvoker(
			IRealTimeSubscriber realTimeSubscriber,
			long invokeInterval, 
			HALRealTimeDriver realTimeDriver) {
		super(realTimeSubscriber);
		
		this.invokeInterval = invokeInterval;
		this.realTimeDriver = realTimeDriver;
		this.lastInvokeTimestamp = realTimeDriver.getUnixTime();
	}

	@Override
	public boolean shouldInvoke() {
		long now = realTimeDriver.getUnixTime();
		if( now >= (lastInvokeTimestamp + invokeInterval) )
			return true;
		else
			return false;
	}

	@Override
	public void invoke() throws OSHException {
		lastInvokeTimestamp = realTimeDriver.getUnixTime();
		synchronized (getSubscriber().getSyncObject()) {
			getSubscriber().onNextTimePeriod();
		}
	}

	@Override
	public String getName() {
		return "RealtimeSubscriberInvoker for " + getSubscriber().getClass().getName();
	}
	
	/* Delegate to realTimeSubscriber for HashMap */
	
	@Override
	public int hashCode() {
		return getSubscriber().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		return getSubscriber().equals(obj);
	}
}
