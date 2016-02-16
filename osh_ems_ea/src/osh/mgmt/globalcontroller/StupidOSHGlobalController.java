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

package osh.mgmt.globalcontroller;

import java.util.BitSet;
import java.util.List;
import java.util.UUID;

import osh.configuration.OSHParameterCollection;
import osh.core.IOSHOC;
import osh.core.com.interfaces.IEventReceiver;
import osh.core.com.interfaces.IHasState;
import osh.core.exceptions.OSHException;
import osh.core.oc.GlobalController;
import osh.datatypes.VirtualCommodity;
import osh.datatypes.limit.PowerLimitSignal;
import osh.datatypes.limit.PriceSignal;
import osh.datatypes.registry.EventExchange;
import osh.datatypes.registry.globalcontroller.EASolutionCommandExchange;
import osh.datatypes.registry.observer.ControllableEAProblemPartExchange;
import osh.datatypes.registry.observer.EAProblemPartExchange;
import osh.mgmt.globalobserver.OSHGlobalObserver;


//FIXME Not up to date...
/**
 * 
 * @author Florian Allerding, Ingo Mauser
 *
 */
@Deprecated
public class StupidOSHGlobalController extends GlobalController implements IEventReceiver, IHasState {
	
	private VirtualCommodity virtualCommodity;
	
	private PriceSignal priceSignal;
	private PowerLimitSignal powerLimitSignal;
	private int optimizationObjective;
	private double overLimitFactor;
	
	private long lasttimeScheduled;

	public StupidOSHGlobalController(IOSHOC controllerbox,
			OSHParameterCollection configurationParameters) {
		super(controllerbox, configurationParameters);
		
		this.virtualCommodity = VirtualCommodity.ACTIVEPOWEREXTERNAL;
		
		this.priceSignal = new PriceSignal(virtualCommodity);
		this.powerLimitSignal = new PowerLimitSignal();

		try {
			this.overLimitFactor = Double.valueOf(this.configurationParameters
					.getParameter("overLimitFactor"));
		} catch (Exception e) {
			getGlobalLogger().logWarning(
					"Can't get overlimitFactor, using the default value");
			this.overLimitFactor = 5;
		}

		try {
			this.optimizationObjective = Integer
					.valueOf(this.configurationParameters
							.getParameter("optimizationObjective"));
		} catch (Exception e) {
			getGlobalLogger().logWarning(
					"Can't get optimizationObjective, using the default value");
			this.optimizationObjective = 0; // "ActivePowerPrice"
		}
	}

	@Override
	public void onSystemIsUp() throws OSHException {
		super.onSystemIsUp();
		
		lasttimeScheduled = 0;
		
		getOSH().getTimer().registerComponent(this, 1);

	}

	@Override
	public void onQueueEventReceived(EventExchange ex)
			throws OSHException {
		getGlobalLogger().logError("ERROR in " + this.getClass().getCanonicalName() + ": UNKNOWN CommandExchange");
	}

	@Override
	public void onNextTimePeriod() {
		handleScheduling();
		
	}

	/**
	 * decide if a (re-)scheduling is necessary
	 */
	private void handleScheduling() {

		boolean reschedulingRequired = false;

		// check if something has been changed:
		for (EAProblemPartExchange<?> problemPart : ((OSHGlobalObserver) getGlobalObserver())
				.getProblemParts()) {
			if (problemPart.getTimestamp() >= lasttimeScheduled) {
				reschedulingRequired = true;

			}

		}

		if (reschedulingRequired) {
			startScheduling();
		}

	}

	/**
	 * is triggered to
	 */
	public void startScheduling() {

		List<EAProblemPartExchange<?>> problemparts = ((OSHGlobalObserver) getGlobalObserver())
				.getProblemParts();

		getGlobalLogger().logDebug("=== scheduling... ===");

		for (EAProblemPartExchange<?> problem : problemparts) {
			// TODO: make better
			if (problem instanceof ControllableEAProblemPartExchange<?>) {
				this.getOCRegistry().sendCommand(
						EASolutionCommandExchange.class,
						problem.transformToPhenotype(
								null,
								problem.getDeviceId(),
								getTimer().getUnixTime(), 
								new BitSet()));
			}
		}
	}

	@Override
	public UUID getUUID() {
		return getGlobalObserver().getAssignedOCUnit().getUnitID();
	}

}
