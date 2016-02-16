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

package osh.mgmt.globalobserver;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import osh.configuration.OSHParameterCollection;
import osh.configuration.system.DeviceTypes;
import osh.core.IOSHOC;
import osh.core.com.interfaces.IEventReceiver;
import osh.core.com.interfaces.IHasState;
import osh.core.exceptions.OSHException;
import osh.core.oc.GlobalObserver;
import osh.core.oc.LocalOCUnit;
import osh.core.oc.LocalObserver;
import osh.datatypes.Commodity;
import osh.datatypes.VirtualCommodity;
import osh.datatypes.gui.DeviceTableEntry;
import osh.datatypes.limit.PowerLimitSignal;
import osh.datatypes.limit.PriceSignal;
import osh.datatypes.mooex.IModelOfObservationExchange;
import osh.datatypes.mooex.ModelOfObservationType;
import osh.datatypes.registry.EventExchange;
import osh.datatypes.registry.StateChangedExchange;
import osh.datatypes.registry.globalobserver.CommodityPowerStateExchange;
import osh.datatypes.registry.globalobserver.DetailedCostsLoggingStateExchange;
import osh.datatypes.registry.globalobserver.DeviceListStateExchange;
import osh.datatypes.registry.globalobserver.DevicesPowerStateExchange;
import osh.datatypes.registry.globalobserver.VirtualCommodityPowerStateExchange;
import osh.datatypes.registry.observer.EAProblemPartExchange;
import osh.datatypes.registry.oc.details.utility.EpsStateExchange;
import osh.datatypes.registry.oc.details.utility.PlsStateExchange;
import osh.utils.uuid.UUIDLists;


/**
 * 
 * @author Florian Allerding, Kaibin Bao, Till Schuberth, Ingo Mauser
 *
 */
public class OSHGlobalObserver extends GlobalObserver implements IEventReceiver, IHasState {

	
	private class ProblemPartComparator implements Comparator<EAProblemPartExchange<?>> {
		@Override
		public int compare(EAProblemPartExchange<?> o1, EAProblemPartExchange<?> o2) {
			return o1.getDeviceId().compareTo(o2.getDeviceId());
		}
	}
	
	
	private Map<UUID, EAProblemPartExchange<?>> eaproblempart;
	private boolean reschedule = false;
	
	private HashMap<UUID,HashMap<Commodity,Double>> deviceCommodityMap = new HashMap<>();
	private HashMap<Commodity,Double> commodityTotalPowerMap = new HashMap<>();
	
	private HashMap<VirtualCommodity,PriceSignal> priceSignals;
	private HashMap<VirtualCommodity,PowerLimitSignal> powerLimitSignals;
	
	List<UUID> totalPowerMeter = null;
	
	
	/**
	 * CONSTRUCTOR
	 * @param controllerbox
	 * @param configurationParameters
	 * @throws OSHException 
	 */
	public OSHGlobalObserver(
			IOSHOC controllerbox, 
			OSHParameterCollection configurationParameters) throws OSHException {
		super(controllerbox, configurationParameters);

		this.eaproblempart = Collections.synchronizedMap(new HashMap<UUID, EAProblemPartExchange<?>>());
		
		String strTotalPowerMeter = configurationParameters.getParameter("totalpowermeter");
		if( strTotalPowerMeter != null ) {
			totalPowerMeter = parseUUIDArray(strTotalPowerMeter);
		}
	}

	private List<UUID> parseUUIDArray(String parameter) throws OSHException {
		try {
			List<UUID> list = UUIDLists.parseUUIDArray(parameter);
			return list;
		} catch( IllegalArgumentException e ) {
			throw new OSHException(e);
		}
	}
	
	@Override
	public void onSystemIsUp() throws OSHException {
		super.onSystemIsUp();
		
		this.getTimer().registerComponent(this, 1);
		
		this.getOCRegistry().registerStateChangeListener(EAProblemPartExchange.class, this);
		this.getOCRegistry().register(StateChangedExchange.class, this);
		
		this.getOCRegistry().registerStateChangeListener(EpsStateExchange.class, this);
		this.getOCRegistry().registerStateChangeListener(PlsStateExchange.class, this);
		
		//FIXME: get all states that are already registered (EAProblemPart,
		//with function getStates()), and put them in the map "eaproblempart".
	}

	@Override
	public void onSystemShutdown() {
		// finalize everything
			//currently nothing
	}

	@Override
	public void onQueueEventReceived(EventExchange ex) throws OSHException {
		boolean problemPartListChanged = false;
		
		if (ex instanceof StateChangedExchange) {
			StateChangedExchange sce = (StateChangedExchange) ex;
			
			if (sce.getType().equals(EpsStateExchange.class)) {
				EpsStateExchange eee = this.getOCRegistry().getState(EpsStateExchange.class, sce.getStatefulentity());
				this.priceSignals = eee.getPriceSignals();
			}
			else if (sce.getType().equals(PlsStateExchange.class)) {
				PlsStateExchange eee = this.getOCRegistry().getState(PlsStateExchange.class, sce.getStatefulentity());
				this.powerLimitSignals = eee.getPowerLimitSignals();
			}
			else if (sce.getType().equals(EAProblemPartExchange.class)) {
				UUID entity = sce.getStatefulentity();
				EAProblemPartExchange<?> ppex = (EAProblemPartExchange<?>) this.getOCRegistry().getState(EAProblemPartExchange.class, entity);
				this.eaproblempart.put(entity, ppex);
				if (ppex.isToBeScheduled())  {
					reschedule = true;
				}
				problemPartListChanged = true;
			}
		}
		if (problemPartListChanged) {
			this.getOCRegistry().setState(
					DeviceListStateExchange.class, this,
					new DeviceListStateExchange(
							getUUID(), 
							getTimer().getUnixTime(),
							getDeviceList(getProblemParts())
					)
			);
		}
	}

	
	private Set<DeviceTableEntry> getDeviceList(List<EAProblemPartExchange<?>> problemparts) {
		Set<DeviceTableEntry> entries = new HashSet<DeviceTableEntry>();
		int i = 1;
		for (EAProblemPartExchange<?> p : problemparts) {
			String type = null;
			try {
				LocalObserver lo = getLocalObserver(p.getDeviceId());
				LocalOCUnit ocunit = lo.getAssignedOCUnit();
				type = ocunit.getDeviceType().toString() + "(" + ocunit.getDeviceClassification().toString() + ")";
			} catch (NullPointerException e) {}
			DeviceTableEntry e = new DeviceTableEntry(i, p.getDeviceId(), type, p.getBitCount(), p.problemToString());
			entries.add(e);
			i++;
		}
		return entries;
	}

	/**
	 * Collect all EAProblemParts from local observers
	 */
	final public List<EAProblemPartExchange<?>> getProblemParts() {
		EAProblemPartExchange<?> [] array = eaproblempart.values().toArray(new EAProblemPartExchange<?>[0]);
		Arrays.sort(array,new ProblemPartComparator());
		return Arrays.asList(array);
	}
	
	public boolean getAndResetProblempartChangedFlag() {
		boolean tmp = reschedule;
		reschedule = false;
		return tmp;
	}

	@Override
	public UUID getUUID() {
		return getAssignedOCUnit().getUnitID();
	}
	
	private void getCurrentPowerOfDevices() {
		// get new power values
		Map<UUID, CommodityPowerStateExchange> powerStatesMap = 
				this.getOCRegistry().getStates(CommodityPowerStateExchange.class);
				
		commodityTotalPowerMap = new HashMap<>();
		
		if ( totalPowerMeter != null ) {
			for (Entry<UUID, CommodityPowerStateExchange> e : powerStatesMap.entrySet()) {
				if (e.getKey().equals(getUUID())) { // ignore own CommodityPowerStateExchange
					continue;
				}
				
				if (totalPowerMeter.contains(e.getKey())) {
					for (Commodity c : Commodity.values()) {
						Double value = (e.getValue()).getPowerState(c);
						if (value != null) {
							HashMap<Commodity,Double> deviceCommodityPowerMap = deviceCommodityMap.get(c);
							if (deviceCommodityPowerMap == null) {
								deviceCommodityPowerMap = new HashMap<>();
								deviceCommodityMap.put(e.getKey(), deviceCommodityPowerMap);
							}
							deviceCommodityPowerMap.put(c, value);
							
							// calculate new total power
							Double commodityTotalPower = commodityTotalPowerMap.get(c);
							if (commodityTotalPower == null) {
								commodityTotalPower = 0.0;
							}
							commodityTotalPower += value;
							commodityTotalPowerMap.put(c, commodityTotalPower);
						}
					}
				}
			}
		}  
		else {
			for (Entry<UUID, CommodityPowerStateExchange> e : powerStatesMap.entrySet()) {
				if (e.getKey().equals(getUUID())) // ignore own CommodityPowerStateExchange
					continue;
				
				for (Commodity c : Commodity.values()) {
					Double value = (e.getValue()).getPowerState(c);
					if (value != null) {
						HashMap<Commodity,Double> deviceCommodityPowerMap = deviceCommodityMap.get(c);
						if (deviceCommodityPowerMap == null) {
							deviceCommodityPowerMap = new HashMap<>();
							deviceCommodityMap.put(e.getKey(), deviceCommodityPowerMap);
						}
						deviceCommodityPowerMap.put(c, value);
						
						// calculate new total power
						Double commodityTotalPower = commodityTotalPowerMap.get(c);
						if (commodityTotalPower == null) {
							commodityTotalPower = 0.0;
						}
						commodityTotalPower += value;
						commodityTotalPowerMap.put(c, commodityTotalPower);
					}
				}
			}
		}
		
		long now = getTimer().getUnixTime();
		
		// Export Commodity powerStates
		CommodityPowerStateExchange cpse = new CommodityPowerStateExchange(
				getUUID(), 
				now,
				DeviceTypes.OTHER);
		for (Entry<Commodity,Double> e : commodityTotalPowerMap.entrySet()) {
			cpse.addPowerState(e.getKey(), e.getValue());
		}
		
		this.getOCRegistry().setState(
				CommodityPowerStateExchange.class, 
				this, 
				cpse);

		// Export Device powerStates
		DevicesPowerStateExchange dpse = new DevicesPowerStateExchange(getUUID(), now);
		
		if( totalPowerMeter != null ) {
			for (Entry<Commodity, Double> e : commodityTotalPowerMap.entrySet()) {
				dpse.addPowerState(getUUID(), e.getKey(), e.getValue());
			}
		} 
		else { /* ( totalPowerMeter == null ) */
			for (Entry<UUID, CommodityPowerStateExchange> e : powerStatesMap.entrySet()) {
				if (!e.getKey().equals(getUUID())) {
					for (Commodity c : Commodity.values()) {
						Double value = (e.getValue()).getPowerState(c);
						if (value != null) {
							dpse.addPowerState(e.getKey(), c, value);
						}
					}
				} /* if (!e.getKey().equals(getUUID())) */
			} /* for */
		} /* if( totalPowerMeter != null ) */ 
		
		// save as state
		this.getOCRegistry().setState(
				DevicesPowerStateExchange.class,
				this,
				dpse);
		
		// save current VirtualCommodity power states to registry (for logger and maybe more in the future)
		{
			HashMap<VirtualCommodity,Integer> vcMap = new HashMap<>();
			
			HashMap<DeviceTypes,Integer> devMapActivePower = new HashMap<>();
			devMapActivePower.put(DeviceTypes.PVSYSTEM, 0);
			devMapActivePower.put(DeviceTypes.CHPPLANT, 0);
			devMapActivePower.put(DeviceTypes.ECAR, 0);
			devMapActivePower.put(DeviceTypes.OTHER, 0);
			
			int totalGasPower = 0;
			
			// calc current VirtualCommodity power state
			for (Entry<UUID, CommodityPowerStateExchange> e : powerStatesMap.entrySet()) {
				
				// check whether UUID is global OC unit and exclude then...
				if (e.getKey().equals(this.getUUID())) {
					continue;
				}
				
				// Active Power
				if (e.getValue().getDeviceType() == DeviceTypes.PVSYSTEM) {
					Integer pvPower = devMapActivePower.get(DeviceTypes.PVSYSTEM);
					Double addPowerDouble = e.getValue().getPowerState(Commodity.ACTIVEPOWER);
					if (addPowerDouble != null) {
						Integer additionalPower = (int) Math.round(addPowerDouble);
						pvPower = pvPower + additionalPower;
						devMapActivePower.put(DeviceTypes.PVSYSTEM, pvPower);
					}
				}
				else if (e.getValue().getDeviceType() == DeviceTypes.CHPPLANT) {
					Integer chpPower = devMapActivePower.get(DeviceTypes.CHPPLANT);
					Double addPowerDouble = e.getValue().getPowerState(Commodity.ACTIVEPOWER);
					if (addPowerDouble != null) {
						Integer additionalPower = (int) Math.round(addPowerDouble);
						chpPower = chpPower + additionalPower;
						devMapActivePower.put(DeviceTypes.CHPPLANT, chpPower);
					}
				}
				else if (e.getValue().getDeviceType() == DeviceTypes.ECAR) {
					Integer ecarPower = devMapActivePower.get(DeviceTypes.ECAR);
					Double addPowerDouble = e.getValue().getPowerState(Commodity.ACTIVEPOWER);
					if (addPowerDouble != null) {
						Integer additionalPower = (int) Math.round(addPowerDouble);
						ecarPower = ecarPower + additionalPower;
						devMapActivePower.put(DeviceTypes.ECAR, ecarPower);
					}
				}
				else {
					Integer otherPower = devMapActivePower.get(DeviceTypes.OTHER);
					Double addPowerDouble = e.getValue().getPowerState(Commodity.ACTIVEPOWER);
					if (addPowerDouble != null) {
						Integer additionalPower = (int) Math.round(addPowerDouble);
						otherPower = otherPower + additionalPower;
						devMapActivePower.put(DeviceTypes.OTHER, otherPower);
					}
				}
				
				// Gas power
				Double gasPowerDouble = e.getValue().getPowerState(Commodity.NATURALGASPOWER);
				if (gasPowerDouble != null) {
					Integer gasPower = (int) Math.round(gasPowerDouble);
					totalGasPower = totalGasPower + gasPower;
				}
				 
			}
			
			double shareofPV = 0;
			double shareofCHP = 0;
			
			Integer pvPower = devMapActivePower.get(DeviceTypes.PVSYSTEM);
			Integer chpPower = devMapActivePower.get(DeviceTypes.CHPPLANT);
			Integer ecarPower = devMapActivePower.get(DeviceTypes.ECAR);
			Integer otherPower = devMapActivePower.get(DeviceTypes.OTHER);
			
			if (pvPower != 0 && chpPower != 0) {
				shareofPV = (double) pvPower / (pvPower + chpPower);
				shareofCHP = (double) chpPower / (pvPower + chpPower);
			}
			else if (pvPower != 0 && chpPower == 0) {
				shareofPV = 1;
			}
			else if (pvPower == 0 && chpPower != 0) {
				shareofCHP = 1;
			}
			
			if (pvPower == null) {
				pvPower = 0;
			}
			if (chpPower == null) {
				chpPower = 0;
			}
			if (ecarPower == null) {
				ecarPower = 0;
			}
			if (otherPower == null) {
				otherPower = 0;
			}
			
			int totalPower = pvPower + chpPower + ecarPower + otherPower;
			
			// net consumption
			if (totalPower >= 0) {
				vcMap.put(VirtualCommodity.PVACTIVEPOWERAUTOCONSUMPTION, pvPower);
				vcMap.put(VirtualCommodity.PVACTIVEPOWERFEEDIN, 0);
				vcMap.put(VirtualCommodity.CHPACTIVEPOWERAUTOCONSUMPTION, chpPower);
				vcMap.put(VirtualCommodity.CHPACTIVEPOWERFEEDIN, 0);
				vcMap.put(VirtualCommodity.ACTIVEPOWEREXTERNAL, totalPower);
			}
			// net production / feed-in
			else {
				int pvExternal = (int) Math.round(shareofPV * totalPower);
				int pvInternal = pvPower - pvExternal;
				
				int chpExternal = (int) Math.round(shareofCHP * totalPower);
				int chpInternal = chpPower - chpExternal;
				
				vcMap.put(VirtualCommodity.PVACTIVEPOWERAUTOCONSUMPTION, pvInternal);
				vcMap.put(VirtualCommodity.PVACTIVEPOWERFEEDIN, pvExternal);
				vcMap.put(VirtualCommodity.CHPACTIVEPOWERAUTOCONSUMPTION, chpInternal);
				vcMap.put(VirtualCommodity.CHPACTIVEPOWERFEEDIN, chpExternal);
				vcMap.put(VirtualCommodity.ACTIVEPOWEREXTERNAL, 0);
			}
			
			vcMap.put(VirtualCommodity.NATURALGASPOWEREXTERNAL, totalGasPower);
			
			VirtualCommodityPowerStateExchange vcpse = new VirtualCommodityPowerStateExchange(
					getUUID(), 
					now, 
					vcMap);
			
			this.getOCRegistry().setState(
					VirtualCommodityPowerStateExchange.class,
					this,
					vcpse);
			
			DetailedCostsLoggingStateExchange dclse = new DetailedCostsLoggingStateExchange(
					getUUID(), 
					now,
					vcMap, 
					priceSignals, 
					powerLimitSignals);
			this.getOCRegistry().setState(
					DetailedCostsLoggingStateExchange.class,
					this,
					dclse);
		}
	}
	
	@Override
	public void onNextTimePeriod() throws OSHException {
		super.onNextTimePeriod();
		
		// get current power states
		//  and also send them to logger
		getCurrentPowerOfDevices();
		
	}

	@Override
	public IModelOfObservationExchange getObservedModelData(ModelOfObservationType type) {
		return null;
	}

}
