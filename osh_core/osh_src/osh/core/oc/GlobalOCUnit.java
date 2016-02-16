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

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import osh.core.IOSHOC;
import osh.core.exceptions.OSHException;


/**
 * containerclass for the virtual controllerbox-unit it represents the central controlling unit
 * @author Florian Allerding, Kaibin Bao, Till Schuberth, Ingo Mauser
 * @category smart-home ControllerBox
 */
public class GlobalOCUnit extends OCUnit {

	private GlobalObserver observer;
	private GlobalController controller;
	//private ArrayList<LocalOCUnit> localUnits;
	private HashMap<UUID, LocalOCUnit> localUnits;
	

	public GlobalOCUnit(
			UUID unitID, 
			IOSHOC controllerbox, 
			GlobalObserver globalObserver, 
			GlobalController globalController){
		super(unitID, controllerbox);
		
		this.localUnits =  new HashMap<UUID, LocalOCUnit>();
		this.observer = globalObserver;
		this.controller = globalController;
		
		this.observer.assignControllerBox(this);
		this.controller.assignControllerBox(this);
		
	}
	public GlobalObserver getObserver() {
		return observer;
	}

	public Controller getController() {
		return controller;
	}
	
	public void registerLocalUnit(LocalOCUnit localunit) throws OSHException{
		
		// put and check if it already exists
		LocalOCUnit old;
		if ((old = localUnits.put(localunit.getUnitID(), localunit)) != null) {
			throw new OSHException("UUID " + localunit.getUnitID() + " already registered!" + old.toString());
		}
		
		//assign globalObserver (UUID)
		localunit.localObserver.registerGlobalObserver(this.getUnitID());
		
		//TODO
		//add to the List
	}
	
	protected HashMap<UUID, LocalOCUnit> getLocalUnits() {
		return localUnits;
	}

	public UUID[] getLocalUnitsUUIDs() {
		UUID[] uuids = new UUID[localUnits.size()];
		int i = 0;
		for (Entry<UUID, LocalOCUnit> localUnit : localUnits.entrySet()) {
			uuids[i] = localUnit.getKey();
			i++;
		}
		
		return uuids;
	}

	
}