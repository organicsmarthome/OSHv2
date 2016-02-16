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

import osh.core.OSHGlobalLogger;
import osh.core.exceptions.OSHException;

/**
 * Invoker thread for callback functions.
 * Which function is invoked under which condition is handled
 * via strategy pattern.
 * @see RealtimeSubscriberInvoker
 * 
 * @author Kaibin Bao
 *
 */
public class InvokerThread extends Thread {
	public InvokerEntry<?> entry;
	public OSHGlobalLogger log;
	private boolean dead = false;
	
	public InvokerThread(OSHGlobalLogger log, InvokerEntry<?> entry) throws OSHException {
		super();
		
		setName("InvokerThread for " + entry.getName());
		
		if (log == null) {
			throw new OSHException("CBGlobalLogger log == null");
		}
		this.log = log;
		this.entry = entry;
	}

	@Override
	public void run() {
		try {
			while ( !entry.shouldExit() ) {
				synchronized ( entry.getSyncObject() ) {
					// wait for invocation condition
					while ( !entry.shouldInvoke() ) {
						try {
							entry.getSyncObject().wait();
						} 
						catch (InterruptedException e) {
							e.printStackTrace();
							log.logError("Thread interrupted. Should not happen! Will DIE now.", e);
							return;
						}
					}
				} /* synchronized */

				// invoke callback
				if ( InvokerThreadRegistry.invoke(entry, log, getName()) )
					break; // if a really bad error happens
			}
		} finally {
			//do this in any case
			dead = true;
			entry.threadDied();
		}
	}
	
	public boolean isDead() {
		return dead;
	}
}
