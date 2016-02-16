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

package osh.mgmt.localcontroller;

import java.util.UUID;

import osh.core.IOSHOC;
import osh.core.com.interfaces.IEventReceiver;
import osh.core.com.interfaces.IHasState;
import osh.core.exceptions.OSHException;
import osh.core.oc.LocalController;
import osh.datatypes.registry.EventExchange;
import osh.hal.exchange.ESHLThermalControllerExchange;
import osh.hal.exchange.ESHLThermalObserverExchange;
import osh.mgmt.localobserver.chp.ExpectedStateExchange;

/**
 * 
 * @author Florian Allerding
 *
 */
public class StupidESHLThermalLocalController extends LocalController implements IEventReceiver, IHasState {

	
	/**
	 * CONSTRUCTOR
	 */
	public StupidESHLThermalLocalController(IOSHOC controllerbox) {
		super(controllerbox);
	}
	
	
	@Override
	public void onQueueEventReceived(EventExchange ex) throws OSHException {
		//NOTHING
	}

	@Override
	public void onSystemIsUp() throws OSHException {
		getTimer().registerComponent(this, 10);
	}

	@Override
	public void onNextTimePeriod() throws OSHException {
		ESHLThermalControllerExchange dachsControllerExchange = null;
		
		if (getLocalObserver().getObserverDataObject() instanceof ESHLThermalObserverExchange) {
			double currentWaterTemp =  ((ESHLThermalObserverExchange) getLocalObserver().getObserverDataObject()).getCurrentTempTop();
			
			if (currentWaterTemp <= 60.0) {
				dachsControllerExchange = new ESHLThermalControllerExchange(
						getDeviceID(), 
						getTimer().getUnixTime());
				dachsControllerExchange.setSwitchGasHeating(true);
			}
			else if (currentWaterTemp >= 85.0) {
				dachsControllerExchange = new ESHLThermalControllerExchange(
						getDeviceID(), 
						getTimer().getUnixTime());
				dachsControllerExchange.setSwitchGasHeating(false);
			}
		}		
		
		if (dachsControllerExchange != null) {
			getOCRegistry().setState(
					ExpectedStateExchange.class, 
					this, 
					new ExpectedStateExchange(
							getUUID(), 
							getTimer().getUnixTime(), 
							dachsControllerExchange.isSwitchGasHeating()));
			this.updateOcDataSubscriber(dachsControllerExchange);
		}
	}

	@Override
	public UUID getUUID() {
		return getDeviceID();
	}

}
