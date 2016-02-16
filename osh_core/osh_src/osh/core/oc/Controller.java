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

import osh.ILifeCycleListener;
import osh.IRealTimeSubscriber;
import osh.OSHComponent;
import osh.core.IOSHOC;
import osh.core.OCComponent;
import osh.core.exceptions.OSHException;
import osh.registry.OCRegistry;


/**
 * abstract superclass for all controllers
 * @author florian.allerding@kit.edu
 * @category smart-home ControllerBox
 */
public abstract class Controller extends OCComponent implements IRealTimeSubscriber, ILifeCycleListener {
	
	
	/**
	 * CONSTRUCTOR
	 * @param controllerbox
	 */
	public Controller(IOSHOC controllerbox) {
		super(controllerbox);
	}
	
	
	@Override
	public IOSHOC getOSH() {
		return (IOSHOC) super.getOSH();
	}
	
	
	protected OCRegistry getOCRegistry() {
		return getOSH().getOCRegistry();
	}

	@Override
	public void onSystemError() throws OSHException {
		//...in case of use please override
	}

	@Override
	public void onSystemHalt() throws OSHException {
		//...in case of use please override
	}

	@Override
	public void onSystemRunning() throws OSHException {
		//...in case of use please override
	}

	@Override
	public void onSystemIsUp() throws OSHException {
		//...in case of use please override
	}

	@Override
	public void onSystemResume() throws OSHException {
		//...in case of use please override
	}

	@Override
	public void onSystemShutdown() throws OSHException {
		//...in case of use please override
	}

	@Override
	public void onNextTimePeriod() throws OSHException {
		//...in case of use please override
	}

	@Override
	public OSHComponent getSyncObject() {
		return this;
	}
	
}
