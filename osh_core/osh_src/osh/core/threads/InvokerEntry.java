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

import osh.IPromiseToEnsureSynchronization;
import osh.core.exceptions.OSHException;


/**
 * Stores information about the callback function.
 * 
 * Strategy pattern.
 * 
 * @author Kaibin Bao
 *
 */
public abstract class InvokerEntry<T extends IPromiseToEnsureSynchronization> {

	private T subscriber;
	private boolean threaddead = false;
	
	public InvokerEntry(T subscriber) {
		if (subscriber == null) throw new NullPointerException();
		if (subscriber.getSyncObject() == null) throw new NullPointerException("synchronization object is null");
		
		this.subscriber = subscriber;
	}
	
	protected T getSubscriber() {
		return subscriber;
	}
	
	/**
	 * Checks if condition for invocation is met
	 * 
	 * @return true iff condition is met
	 */
	public abstract boolean shouldInvoke();

	/**
	 * {@link InvokerThread} calls getSyncObject().wait() if shouldInvoke() returns false.
	 * 
	 * Default synchronization object is this {@link InvokerEntry}.
	 * 
	 * @return 
	 */
	public Object getSyncObject() {
		return this;
	}
	
	/**
	 * Calls the callback method
	 * 
	 * @throws OSHException
	 */
	public abstract void invoke() throws OSHException;

	
	/**
	 * exit thread?
	 */
	/* default */ boolean exit = false;
	
	/**
	 * should exit thread?
	 */
	/* default */ boolean shouldExit() {
		return exit;
	}
	
	public void setExit(boolean exit) {
		this.exit = exit;
	}
	
	/* default */ void threadDied() {
		threaddead = true;
	}
	
	/**
	 * Returns a canonical name for debugging
	 * 
	 * @return
	 */
	public abstract String getName();
	
	
	
	/**
	 * Needed for HashMap to work
	 */
	@Override
	public abstract int hashCode();
	
	/**
	 * Needed for HashMap to work
	 */
	@Override
	public abstract boolean equals(Object obj);
	
	public boolean isThreadDead() {
		return threaddead;
	}
}
