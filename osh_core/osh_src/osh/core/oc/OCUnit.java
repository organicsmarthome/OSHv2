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

import osh.core.IOSHOC;
import osh.hal.HALRealTimeDriver;


/**
 * abstract superclass for the O/C-Unit container
 * @author florian.allerding@kit.edu
 * @category smart-home ControllerBox
 */
public abstract class OCUnit {
	
	private IOSHOC controllerbox;
	protected final UUID unitID;
	
	
	public OCUnit(UUID unitID, IOSHOC controllerbox) {
		this.controllerbox = controllerbox;
		this.unitID = unitID;
	}
	
	
	protected HALRealTimeDriver getSystemTimer() {
		return controllerbox.getTimer();
	}
	
	public UUID getUnitID() {
		return unitID;
	}

}
