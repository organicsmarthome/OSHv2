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

package osh.comdriver.simulation.cruisecontrol;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import osh.datatypes.Commodity;
import osh.datatypes.VirtualCommodity;
import osh.datatypes.cruisecontrol.GUIWaterStorageStateExchange;
import osh.datatypes.cruisecontrol.OptimizedDataStorage;
import osh.datatypes.cruisecontrol.PowerSum;
import osh.datatypes.ea.Schedule;
import osh.datatypes.gui.DeviceTableEntry;
import osh.datatypes.limit.PowerLimitSignal;
import osh.datatypes.limit.PriceSignal;
import osh.datatypes.registry.StateExchange;
import osh.datatypes.registry.localobserver.WaterStorageStateExchange;


/**
 * 
 * @author Till Schuberth, Kaibin Bao, Ingo Mauser
 *
 */
public class GuiDataCollector {

	private GuiMain driver;
	
	private List<Schedule> schedules;
	private HashMap<VirtualCommodity,PriceSignal> ps;
	private HashMap<VirtualCommodity,PowerLimitSignal> pwrLimit;
	private HashMap<UUID, HashMap<Commodity, Double>> powerStates;
	private OptimizedDataStorage<GUIWaterStorageStateExchange> waterstoragehistory = 
			new OptimizedDataStorage<GUIWaterStorageStateExchange>();
	private HashMap<Commodity,OptimizedDataStorage<PowerSum>> powerhistory = new HashMap<>();
	private boolean saveGraph;

	private long lastWaterRefresh = 0;
	private final int removeOlderThan = 2 * 86400;
	
	
	/**
	 * CONSTRUCTOR
	 */
	public GuiDataCollector(GuiMain driver, boolean saveGraph) {
		if (driver == null) {
			throw new NullPointerException("driver is null");
		}
		
		this.saveGraph = saveGraph;
		this.driver = driver;
	}
	
	public void updateEADeviceList(Set<DeviceTableEntry> deviceList) {
		driver.refreshDeviceTable(deviceList);
	}
	
	public void updateStateView(Set<Class<? extends StateExchange>> types, Map<UUID, ? extends StateExchange> states) {
		driver.refreshStateTable(types, states);
	}
	
	private void waitforuser(long timestamp) {
		//in case we are waiting deliver an updated water diagram
		//this is a dirty hack, but who cares
		doRealPastUpdate(timestamp);
		driver.waitForUserIfRequested();
	}

	public void updateGlobalSchedule(
			HashMap<VirtualCommodity,PriceSignal> ps,
			long timestamp) {
		this.ps = ps;
		driver.refreshDiagram(this.schedules, ps, this.pwrLimit, timestamp, this.saveGraph);
		waitforuser(timestamp);

	}
	
	public void updateGlobalSchedule(
			long timestamp,
			HashMap<VirtualCommodity,PowerLimitSignal> pwrLimit) {
		this.pwrLimit = pwrLimit;
		driver.refreshDiagram(schedules, ps, pwrLimit, timestamp, saveGraph);
		waitforuser(timestamp);
	}
	
	public void updateGlobalSchedule(
			List<Schedule> schedules, 
			long timestamp) {
		this.schedules = schedules;
		driver.refreshDiagram(schedules, ps, pwrLimit, timestamp, saveGraph);
		waitforuser(timestamp);
	}
	
	public void updateWaterStorageData(WaterStorageStateExchange exws) {
		GUIWaterStorageStateExchange guiWaterStorageStateExchange = 
				new GUIWaterStorageStateExchange(	
						exws.getSender(),
						exws.getTimestamp(), 
						exws.getCurrenttemp(), 
						exws.getMintemp(), 
						exws.getMaxtemp());
		waterstoragehistory.add(exws.getTimestamp(), guiWaterStorageStateExchange);
		checkForPastUpdate(exws.getTimestamp());
	}

	public void updatePowerStates(
			Long timestamp,
			HashMap<UUID, HashMap<Commodity, Double>> powerStates) {
		HashMap<Commodity,PowerSum> commodityPowerSum = new HashMap<>();
		
		this.powerStates = powerStates;

		if ( powerStates != null ) {
			for (Entry<UUID, HashMap<Commodity, Double>> e : powerStates.entrySet()) {
				HashMap<Commodity, Double> comMap = e.getValue();
				
				for (Entry<Commodity,Double> f : comMap.entrySet()) {
					PowerSum existingSum = commodityPowerSum.get(f.getKey());
					if (existingSum == null) {
						existingSum = new PowerSum(0, 0, 0);
					}
					double posSum = existingSum.getPosSum();
					double negSum = existingSum.getNegSum();
					double sum = existingSum.getSum();
					
					Double value = f.getValue();
					if (value == null) {
						value = 0.0;
					}
					
					// add to totalSum
					sum += value;
					
					if (value > 0) {
						posSum += value;
					}
					else if (value < 0) {
						negSum += value;
					}
					
					PowerSum newSum = new PowerSum(posSum, negSum, sum);
					commodityPowerSum.put(f.getKey(), newSum);
				} /* for( ... Commodity ... ) */
			} /* for( ... UUID ... ) */
			
			for( Entry<Commodity, PowerSum> ent : commodityPowerSum.entrySet() ) {
				OptimizedDataStorage<PowerSum> timeseries = powerhistory.get(ent.getKey());
				if( timeseries == null ) {
					timeseries = new OptimizedDataStorage<>();
					powerhistory.put(ent.getKey(), timeseries);
				}
				timeseries.add(timestamp, ent.getValue());
			}
		}

		checkForPastUpdate(timestamp);
		checkForPowerHistoryCleanup(timestamp);
	}

	private void checkForPastUpdate(long now) {
		//refresh only every hour
		if (Math.abs(lastWaterRefresh - now) > 3600) {
			lastWaterRefresh = now;
			doRealPastUpdate(now);
		}
	}

	private void doRealPastUpdate(long timestamp) {
		// WaterStorage
		driver.refreshWaterDiagram(waterstoragehistory.getMap());
		
		// PowerSum History
		HashMap<Commodity,TreeMap<Long,PowerSum>> commodityPowerSum = new HashMap<>();
		
		for( Commodity c : powerhistory.keySet() ) {
			TreeMap<Long,PowerSum> powersumseries = powerhistory.get(c).getMap();
			commodityPowerSum.put(c,powersumseries);
		}
		
		driver.refreshPowerSumDiagram(timestamp, commodityPowerSum);
	}
	
	private void checkForPowerHistoryCleanup(long timestamp) {
		if ( timestamp % 3600 == 0 ) {
			synchronized (powerhistory) {
				for( Commodity c : powerhistory.keySet() ) {
					Map<Long,PowerSum> map = powerhistory.get(c).getMap();
					
					for (Iterator<Map.Entry<Long, PowerSum>> it = map.entrySet().iterator(); it.hasNext();) {
						Entry<Long,PowerSum> e = it.next();
						if ( e.getKey() < (timestamp - this.removeOlderThan) ) {
							it.remove();
						}
					}
				}
			}
		}
	}

}
