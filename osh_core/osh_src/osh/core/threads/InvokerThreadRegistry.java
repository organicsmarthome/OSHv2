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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import osh.IRealTimeSubscriber;
import osh.OSHComponent;
import osh.core.EventQueue;
import osh.core.IOSHFull;
import osh.core.OSHGlobalLogger;
import osh.core.StateChangedEventSet;
import osh.core.exceptions.OSHException;
import osh.core.threads.exceptions.InvokerThreadException;
import osh.core.threads.exceptions.SubscriberNotFoundException;
import osh.registry.EventReceiverWrapper;

/**
 * Manages threads which are used to trigger callback functions
 * 
 * @author Kaibin Bao, Till Schuberth
 *
 */
public class InvokerThreadRegistry extends OSHComponent {

	/** don't give away iterators, otherwise you will get concurrent modification exceptions */
	private Map<InvokerEntry<?>, InvokerEntry<?>> invokers = new HashMap<>();
	private List<InvokerEntry<?>> invokersAsList = new ArrayList<InvokerEntry<?>>();
	private List<InvokerThread> invokerThreads = new ArrayList<InvokerThread>();
	private boolean threadsStarted = false;
	
	/* CONSTRUCTOR */
	public InvokerThreadRegistry(IOSHFull controllerbox) {
		super(controllerbox);
	}

	/* (DE-) REGISTER */	
	
	public void addRealtimeSubscriber(IRealTimeSubscriber subscriber, long refreshInterval, int priority) throws OSHException {
		if (subscriber == null) throw new NullPointerException("subscriber is null");
		
		RealtimeSubscriberInvoker subscriberInvoker
			= new RealtimeSubscriberInvoker(subscriber, refreshInterval, getTimer());
		
		synchronized (this) {
			if( invokers.containsKey(subscriberInvoker) )
				throw new OSHException("RealTimeSubscriber is already registered");

			createThread(subscriberInvoker, priority);
		}
	}

	public void removeRealtimeSubscriber(IRealTimeSubscriber subscriber) {
		if (subscriber == null) throw new NullPointerException("subscriber is null");
		
		RealtimeSubscriberInvoker subscriberInvoker
			= new RealtimeSubscriberInvoker(subscriber, 0, getTimer());

		synchronized (this) {
			invokers.remove(subscriberInvoker);
			invokersAsList.remove(subscriberInvoker);
		}
	}

	public void addQueueSubscriber(EventReceiverWrapper subscriber, EventQueue queue) throws OSHException {
		if (subscriber == null) throw new NullPointerException("subscriber is null");

		EventQueueSubscriberInvoker subscriberInvoker
			= new EventQueueSubscriberInvoker(subscriber, queue);
		
		synchronized (this) {
			if( invokers.containsKey(subscriberInvoker) )
				throw new OSHException("IEventReceiver is already registered");

			createThread(subscriberInvoker, Thread.NORM_PRIORITY);
		}
	}
	
	/**
	 * Invokes one specific {@link IEventReceiver}
	 * 
	 * @param receiver
	 * @return true iff subscriber was found
	 * @throws SubscriberNotFoundException 
	 * @throws InvokerThreadException 
	 */
	public void invoke(EventReceiverWrapper subscriber) throws SubscriberNotFoundException, InvokerThreadException {
		if (subscriber == null) throw new SubscriberNotFoundException("argument is null");

		EventQueueSubscriberInvoker subscriberInvoker
			= new EventQueueSubscriberInvoker(subscriber, null);
		
		synchronized (this) {
			InvokerEntry<?> realSubscriberInvoker = invokers.get(subscriberInvoker);
			
			if( realSubscriberInvoker == null ) throw new SubscriberNotFoundException("subscriber not in list. UUID: " + subscriber.getUUID());
			if (realSubscriberInvoker.isThreadDead()) throw new InvokerThreadException("queue thread for subscriber " + subscriber.getUUID() + " died some time ago.");
			
			notifyInvokerThread( realSubscriberInvoker );
		}
	}
	
	
	public void addStateSubscriber(EventReceiverWrapper subscriber, StateChangedEventSet eventset) throws OSHException {
		if (subscriber == null) throw new NullPointerException("subscriber is null");

		StateSubscriberInvoker subscriberInvoker
			= new StateSubscriberInvoker(subscriber, eventset);
		
		synchronized (this) {
			if( invokers.containsKey(subscriberInvoker) )
				throw new OSHException("IEventReceiver is already registered");

			createThread(subscriberInvoker, Thread.NORM_PRIORITY);
		}
	}

	public void notifyStateSubscriber(EventReceiverWrapper subscriber) throws SubscriberNotFoundException, InvokerThreadException {
		if (subscriber == null) throw new SubscriberNotFoundException("argument is null");

		InvokerEntry<?> realSubscriberInvoker;

		synchronized (this) {
			realSubscriberInvoker = invokers.get(new StateSubscriberInvoker(subscriber, null));
			
			if( realSubscriberInvoker == null ) throw new SubscriberNotFoundException("subscriber not in list. UUID: " + subscriber.getUUID());
			if (realSubscriberInvoker.isThreadDead()) throw new InvokerThreadException("queue thread for subscriber " + subscriber.getUUID() + " died some time ago.");
		}
			
		notifyInvokerThread( realSubscriberInvoker );
	}

	
	/* ********* */

	private InvokerThread createThread(InvokerEntry<?> subscriberInvoker, int priority) throws OSHException {
		InvokerThread thread = new InvokerThread(getGlobalLogger(), subscriberInvoker);
		
		// start a real thread if this is not a simulation
		if (isConcurrent()) {
			thread.setName(subscriberInvoker.getName());
			thread.setPriority(priority);
		}
		
		synchronized (this) {
			invokers.put(subscriberInvoker, subscriberInvoker);
			invokersAsList.add(subscriberInvoker);
		}
		
		// start a real thread if this is not a simulation
		if (isConcurrent()) {
			synchronized (invokerThreads) {
				invokerThreads.add(thread);
				if (threadsStarted) {
					//thread came after a call to startThreads, start thread immediately (e.g. new driver loaded into a running system)
					thread.start();
				}
			}
		}
		else {
			thread = null;
		}
		
		return thread;
	}
	
	private boolean isConcurrent() {
		return !getOSH().isSimulation();
	}
	
	/* INVOCATION */
	
	/**
	 * Common method for {@link InvokerThread} and triggerInvokersSequentially()
	 * 
	 * @param invokerEntry
	 * @param log
	 * @param name
	 * @return true iff a really bad error happens
	 */
	static /* default */ boolean invoke( InvokerEntry<?> invokerEntry, OSHGlobalLogger log, String name ) {
		try {
			invokerEntry.invoke();
		} 
		catch (OSHException e) {
			e.printStackTrace();
			log.logError("ControllerBoxException in InvokerThread " + name, e);
		} 
		catch (Exception e) {
			e.printStackTrace();
			log.logError("Really bad runtime exception. InvokerThread " + name + " will DIE now.", e);
			return true;
		}
		
		return false;
	}
	
	static private void notifyInvokerThread( InvokerEntry<?> invokerEntry ) {
		synchronized (invokerEntry.getSyncObject()) {
			invokerEntry.getSyncObject().notifyAll();
		}
	}
	
	/* USED BY SIMULATION / TIMER DRIVER */
	
	public void triggerInvokers() {
		// notify real threads concurrently if this is not a simulation
		if (isConcurrent()) {
			triggerInvokersConcurrently();
		} 
		else {
			triggerInvokersSequentially();
		}
	}
	
	private void triggerInvokersSequentially() {
		synchronized (this) {
			for( InvokerEntry<?> invokerEntry : invokersAsList ) {
				if( invokerEntry.shouldInvoke() ) {
					invoke(invokerEntry, getGlobalLogger(), "Sequential Invoker");
				}
			}
		}
	}
	
	private void triggerInvokersConcurrently() {
		synchronized (this) {
			for( InvokerEntry<?> invokerEntry : invokersAsList ) {
				notifyInvokerThread( invokerEntry );
			}
		}
	}
	
	
	public void startThreads() {
//		getGlobalLogger().logError(invokerthreads.toString());
		
		synchronized (invokerThreads) {
			for (InvokerThread thread : invokerThreads) {
				thread.start();
			}
			threadsStarted = true;
		}
	}
	
}
