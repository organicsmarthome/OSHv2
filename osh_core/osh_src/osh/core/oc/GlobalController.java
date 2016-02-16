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

import java.util.UUID;

import osh.configuration.OSHParameterCollection;
import osh.core.IOSHOC;



/**
 *Superclass for the global controller unit
 *
 *@author florian.allerding@kit.edu
 *@category smart-home ControllerBox
 *
 */
public abstract class GlobalController extends Controller {

	private GlobalOCUnit assignedOCUnit;
	
	protected OSHParameterCollection configurationParameters;
	
	public GlobalController(IOSHOC controllerbox, OSHParameterCollection configurationParameters) {
		super(controllerbox);
		this.configurationParameters = configurationParameters;
	}

	
	protected void assignControllerBox(GlobalOCUnit assignedOCUnit){
		this.assignedOCUnit = assignedOCUnit;
	}
	
	/**
	 * get the local o/c-unit to which thing controller belongs...
	 * @return
	 */
	public final GlobalOCUnit getAssignedOCUnit() {
		return assignedOCUnit;
	}
	
	/**
	 * get a local controller unit from a specific local o/c-unit
	 * @param deviceID
	 * @return
	 */
	public LocalController getLocalController(UUID deviceID) {
		LocalOCUnit _localOC = assignedOCUnit.getLocalUnits().get(deviceID);
		
		if( _localOC != null )
			return _localOC.localController;
		else
			return null;
	}
	
	/**
	 * return the according global observer unit
	 * @return
	 */
	public GlobalObserver getGlobalObserver(){
		return this.assignedOCUnit.getObserver();
	}
	

//	/** legacy interface */
//	@Deprecated
//	public abstract void onComAction(IComAction commAction);

}
