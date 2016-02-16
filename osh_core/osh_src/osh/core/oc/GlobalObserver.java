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

package osh.core.oc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import osh.configuration.OSHParameterCollection;
import osh.core.IOSHOC;


/**
 * represents the observer of the global o/c-unit
 * @author florian.allerding@kit.edu
 *
 */
public abstract class GlobalObserver extends Observer {
	
	private GlobalOCUnit assignedOCUnit;
	
	protected OSHParameterCollection configurationParameters;
	
	public GlobalObserver(IOSHOC controllerbox, OSHParameterCollection configurationParameters){
		super(controllerbox);
		this.configurationParameters = configurationParameters;
	}
	
	/**
	 * assign the controllerbox o/c-unit
	 * @param assignedOCUnit
	 */
	protected void assignControllerBox(GlobalOCUnit assignedOCUnit){
		this.assignedOCUnit = assignedOCUnit;
	}

	/**
	 * gets a local observer based on it's id
	 * @param deviceID
	 * @return
	 */
	public LocalObserver getLocalObserver(UUID deviceID){
		
		LocalOCUnit _localOC = assignedOCUnit.getLocalUnits().get(deviceID);
		if( _localOC != null )
			return _localOC.localObserver;
		else
			return null;
	}	
	
	/**
	 * Returns all assigned local Observer
	 * @return
	 */
	public ArrayList<LocalObserver> getAllLocalObservers(){
		
		ArrayList<LocalObserver> _localObserver = new ArrayList<LocalObserver>();
		Collection<LocalOCUnit> _ocCollection = assignedOCUnit.getLocalUnits().values();
		ArrayList<LocalOCUnit>  _localOCUnits = new ArrayList<LocalOCUnit>();
		_localOCUnits.addAll(_ocCollection);
		
		for (int i = 0; i < _localOCUnits.size(); i++){
			_localObserver.add(_localOCUnits.get(i).localObserver);
		}
		
		return _localObserver;
	}
	
	/**
	 * returns the list of all ids form the assignd devices/local units
	 * @return
	 */
	public ArrayList<UUID> getAssigndDeviceIDs(){
		ArrayList<UUID> _devIDs = new ArrayList<UUID>();
		
		for (int i = 0; i < _devIDs.size(); i++){
			_devIDs.add((UUID) assignedOCUnit.getLocalUnits().keySet().toArray()[i]);
		}
		return _devIDs;
	}
	
	
	public GlobalOCUnit getAssignedOCUnit() {
		return assignedOCUnit;
	}
	
}
