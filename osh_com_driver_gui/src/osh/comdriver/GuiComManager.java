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

package osh.comdriver;

import java.util.Map;
import java.util.UUID;

import osh.comdriver.simulation.cruisecontrol.stateviewer.StateViewerRegistryEnum;
import osh.core.IOSHOC;
import osh.core.com.AbstractComManager;
import osh.core.com.interfaces.IEventReceiver;
import osh.core.exceptions.OSHException;
import osh.datatypes.registry.EventExchange;
import osh.datatypes.registry.StateChangedExchange;
import osh.datatypes.registry.StateExchange;
import osh.datatypes.registry.globalobserver.DeviceListStateExchange;
import osh.datatypes.registry.globalobserver.DevicesPowerStateExchange;
import osh.datatypes.registry.globalobserver.ScheduleStateExchange;
import osh.datatypes.registry.localobserver.WaterStorageStateExchange;
import osh.datatypes.registry.oc.details.utility.EpsStateExchange;
import osh.datatypes.registry.oc.details.utility.PlsStateExchange;
import osh.hal.exchange.DevicesPowerComExchange;
import osh.hal.exchange.GUIDeviceListComExchange;
import osh.hal.exchange.GUIEpsComExchange;
import osh.hal.exchange.GUIPlsComExchange;
import osh.hal.exchange.GUIScheduleComExchange;
import osh.hal.exchange.GUIStateRegistrySelectedComExchange;
import osh.hal.exchange.GUIStateSelectedComExchange;
import osh.hal.exchange.GUIStatesComExchange;
import osh.hal.exchange.GUIWaterStorgaComExchange;
import osh.hal.exchange.IHALExchange;
import osh.hal.exchange.xmlannot.ScheduleDebugExchange;


/**
 * 
 * @author Till Schuberth, Ingo Mauser
 *
 */
public class GuiComManager extends AbstractComManager implements IEventReceiver {
	
	private UUID chpsource = null;
	private Class<? extends StateExchange> stateviewertype = null;
	private StateViewerRegistryEnum stateviewerregistry = StateViewerRegistryEnum.OC;
	
	/**
	 * CONSTRUCTOR
	 * @param controllerbox
	 * @param uuid
	 */
	public GuiComManager(IOSHOC controllerbox, UUID uuid) {
		super(controllerbox, uuid);
	}

	@Override
	public void onSystemIsUp() throws OSHException {
		super.onSystemIsUp();

		// signals from utility or the like
		getOCRegistry().registerStateChangeListener(EpsStateExchange.class, this);
		getOCRegistry().registerStateChangeListener(PlsStateExchange.class, this);
		
		// states to visualize
		getOCRegistry().registerStateChangeListener(ScheduleStateExchange.class, this);
		getOCRegistry().registerStateChangeListener(DeviceListStateExchange.class, this);
		getOCRegistry().registerStateChangeListener(WaterStorageStateExchange.class, this);
		getOCRegistry().registerStateChangeListener(DevicesPowerStateExchange.class, this);
		
		// schedule to visualize
		getOCRegistry().register(ScheduleDebugExchange.class, this);
		
		getTimer().registerComponent(this, 1);
	}

	@Override
	public void onQueueEventReceived(EventExchange ex) throws OSHException {

		if (ex instanceof StateChangedExchange) {
			StateChangedExchange exsc = (StateChangedExchange) ex;
			
			if (exsc.getType().equals(ScheduleStateExchange.class)) {
				ScheduleStateExchange se = 
						(ScheduleStateExchange) getOCRegistry().getState(ScheduleStateExchange.class, exsc.getStatefulentity());
				GUIScheduleComExchange gsce = new GUIScheduleComExchange(
						getUUID(), se.getTimestamp(), se.getDebugGetSchedules());
				updateOcDataSubscriber(gsce);
				
			} 
			else if (exsc.getType().equals(DeviceListStateExchange.class)) {
				DeviceListStateExchange se = 
						(DeviceListStateExchange) getOCRegistry().getState(DeviceListStateExchange.class, exsc.getStatefulentity());
				GUIDeviceListComExchange gdlce = new GUIDeviceListComExchange(
						getUUID(), se.getTimestamp(), se.getDeviceList());
				updateOcDataSubscriber(gdlce);
			} 
			else if (exsc.getType().equals(WaterStorageStateExchange.class)) {
				//prevent two CHPs to interleave data. 
				//This solution is not very clever, but implemented in 5 lines
				if (chpsource == null) {
					chpsource = exsc.getStatefulentity();
				}
				if (chpsource.equals(exsc.getStatefulentity())) {
					WaterStorageStateExchange sx = 
							(WaterStorageStateExchange) getOCRegistry().getState(WaterStorageStateExchange.class, exsc.getStatefulentity());
					GUIWaterStorgaComExchange gwsce = new GUIWaterStorgaComExchange(
							getUUID(), sx.getTimestamp(), sx.getCurrenttemp(), sx.getMintemp(), sx.getMaxtemp());
					updateOcDataSubscriber(gwsce);
				}
			}
			else if (exsc.getType().equals(EpsStateExchange.class)) {
				EpsStateExchange eee = getOCRegistry().getState(EpsStateExchange.class, exsc.getStatefulentity());
				GUIEpsComExchange gece = new GUIEpsComExchange(getUUID(), eee.getTimestamp());
				gece.setPriceSignals(eee.getPriceSignals());
				updateOcDataSubscriber(gece);
			}
			else if (exsc.getType().equals(PlsStateExchange.class)) {
				PlsStateExchange pse = getOCRegistry().getState(PlsStateExchange.class, exsc.getStatefulentity());
				GUIPlsComExchange gpce = new GUIPlsComExchange(getUUID(), pse.getTimestamp());
				gpce.setPowerLimitSignals(pse.getPowerLimitSignals());
				updateOcDataSubscriber(gpce);
			}
			else if (exsc.getType().equals(DevicesPowerStateExchange.class)) {
				DevicesPowerStateExchange dpsex = getOCRegistry().getState(DevicesPowerStateExchange.class, exsc.getStatefulentity());
				DevicesPowerComExchange gpce = new DevicesPowerComExchange(getUUID(), dpsex.getTimestamp(), dpsex);
				updateOcDataSubscriber(gpce);
			}
		} 
		
	}

	@Override
	public void onDriverUpdate(IHALExchange exchangeObject) {
		if (exchangeObject instanceof GUIStateSelectedComExchange) {
			GUIStateSelectedComExchange gssce = (GUIStateSelectedComExchange) exchangeObject;
			synchronized (this) {
				stateviewertype = gssce.getSelected();
			}
		} else if (exchangeObject instanceof GUIStateRegistrySelectedComExchange) {
			osh.hal.exchange.GUIStateRegistrySelectedComExchange gssrce = (GUIStateRegistrySelectedComExchange) exchangeObject;
			synchronized (this) {
				stateviewerregistry = gssrce.getSelected();
			}
		}
	}

	@Override
	public void onNextTimePeriod() throws OSHException {
		super.onNextTimePeriod();
		
		// TODO build clean sum object (NOT WaterStoragSumDetails...)...maybe somewhere else
		// Best place would be GlobalObserver!

		synchronized (this) {
			if (stateviewerregistry == StateViewerRegistryEnum.OC) {
				Map<UUID, ? extends StateExchange> states = null;
				if (stateviewertype != null) {
					states = getOCRegistry().getStates(stateviewertype);
				}

				updateOcDataSubscriber(
						new GUIStatesComExchange(
								getUUID(), 
								getTimer().getUnixTime(), 
								getOCRegistry().getTypes(), 
								states));
			} else if (stateviewerregistry == StateViewerRegistryEnum.DRIVER) {
				updateOcDataSubscriber(
						new GUIStatesComExchange(
								getUUID(), 
								getTimer().getUnixTime(), 
								stateviewertype));
			}
		}
	}
	
}
