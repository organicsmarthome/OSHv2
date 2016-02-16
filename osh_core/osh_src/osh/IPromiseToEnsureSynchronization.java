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

package osh;


/**
 * This enforces that the programmer takes care of synchronization.<br>
 * <br>
 * Implement this interface to provide a synchronization object for a synchronization domain.
 * If you cross domains, you have to synchronize your calls and all associated and used objects
 * yourself, normally you use the registry for this stuff. So normally you should not have to implement
 * this interface.
 * 
 * @author Kaibin Bao, Till Schuberth, Ingo Mauser
 *
 */
public interface IPromiseToEnsureSynchronization {
	/**
	 * Must return a common synchronization object for callbacks like:
	 *  - {@link IRealTimeSubscriber}.onNextTimePeriod()
	 *  - {@link IEventReceiver}.onQueueEvent()
	 */
	public Object getSyncObject();
}
