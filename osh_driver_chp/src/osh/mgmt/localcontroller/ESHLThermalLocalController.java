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

import java.util.List;
import java.util.UUID;

import osh.core.IOSHOC;
import osh.core.com.interfaces.IEventReceiver;
import osh.core.com.interfaces.IHasState;
import osh.core.exceptions.OSHException;
import osh.core.oc.LocalController;
import osh.datatypes.registry.EventExchange;
import osh.datatypes.registry.globalcontroller.EASolutionCommandExchange;
import osh.hal.exchange.ESHLThermalControllerExchange;
import osh.mgmt.localobserver.chp.CHPActivation;
import osh.mgmt.localobserver.chp.ExpectedStateExchange;
import osh.mgmt.localobserver.chp.eapart.CHPPhenotype;

/**
 * 
 * @author Florian Allerding, Till Schuberth, Ingo Mauser
 *
 */
public class ESHLThermalLocalController extends LocalController implements IEventReceiver, IHasState {
	@SuppressWarnings("unused")
	private final long TURNOFFLAG = 600; // 10 minutes
	private List<CHPActivation> starttimes;
	private CHPActivation currentactivation;
	
	
	/**
	 * CONSTRUCTOR
	 * @param controllerbox
	 */
	public ESHLThermalLocalController(IOSHOC controllerbox) {
		super(controllerbox);
	}
	
	
	@Override
	public void onQueueEventReceived(EventExchange ex) throws OSHException {
		if (ex instanceof EASolutionCommandExchange) {
			EASolutionCommandExchange<CHPPhenotype> exs = ((EASolutionCommandExchange<CHPPhenotype>) ex);
			starttimes = exs.getPhenotype().getList();
			StringBuilder builder = new StringBuilder();
			
			builder.append("starttimes: {");
			boolean first = true;
			for (CHPActivation a : starttimes) {
				if (!first) builder.append(", ");
				builder.append(a.startTime).append(" for ").append(a.duration);
				first = false;
			}
			builder.append("}");
			
			getGlobalLogger().logDebug(builder.toString());
			this.getGlobalLogger().logDebug(builder.toString());
		}
	}

	@Override
	public void onSystemIsUp() throws OSHException {
		getTimer().registerComponent(this, 10);
		getOCRegistry().register(EASolutionCommandExchange.class, this);
	}

	@Override
	public void onNextTimePeriod() throws OSHException {
		ESHLThermalControllerExchange dachsControllerExchange = null;
		
		if (starttimes != null 
				&& starttimes.size() > 0 
				&& starttimes.get(0).startTime < getTimer().getUnixTime()) {
			//turn on
			dachsControllerExchange = new ESHLThermalControllerExchange(
					getDeviceID(), 
					getTimer().getUnixTime());
			dachsControllerExchange.setSwitchGasHeating(true);
			currentactivation = starttimes.get(0);
			starttimes.remove(0);
		}
		
		if (currentactivation != null 
				&& currentactivation.startTime + currentactivation.duration < getTimer().getUnixTime()) {
			//turn off
			dachsControllerExchange = new ESHLThermalControllerExchange(
					getDeviceID(), 
					getTimer().getUnixTime());
			dachsControllerExchange.setSwitchGasHeating(false);
			currentactivation = null;
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
