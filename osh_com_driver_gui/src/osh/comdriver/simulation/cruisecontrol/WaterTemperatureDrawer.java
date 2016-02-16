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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.jfree.data.xy.XYSeries;

import osh.datatypes.cruisecontrol.GUIWaterStorageStateExchange;


/**
 * 
 * @author Till Schuberth
 *
 */
@SuppressWarnings("serial")
class WaterTemperatureDrawer extends AbstractDrawer {

	private List<XYSeries> currentSeries = null;
	private long lastentry = 0;
	
	public WaterTemperatureDrawer() {
		super("Watertemperature", true);
	}
	
	@Override
	protected String getAxisName() {
		return "temperature";
	}

	@Override
	protected List<XYSeries> getSeries(long begin, long end) {
		return (currentSeries == null ? new LinkedList<XYSeries>() : currentSeries);
	}

	@Override
	protected long getNumberOfEntries() {
		return lastentry;
	}

	public void refreshDiagram(TreeMap<Long, GUIWaterStorageStateExchange> waterstoragehistory) {
		List<XYSeries> series = new ArrayList<XYSeries>();
		
		XYSeries minTemp = new XYSeries("minTemp");
		XYSeries maxTemp = new XYSeries("maxTemp");
		XYSeries temp = new XYSeries("temp");
		
		for (Entry<Long, GUIWaterStorageStateExchange> ex : waterstoragehistory.entrySet()) {
			minTemp.add(ex.getKey().doubleValue() * 1000, ex.getValue().getMintemp());
			maxTemp.add(ex.getKey().doubleValue() * 1000, ex.getValue().getMaxtemp());
			temp.add(ex.getKey().doubleValue() * 1000, ex.getValue().getCurrenttemp());
		}
		
		series.add(minTemp);
		series.add(maxTemp);
		series.add(temp);

		this.currentSeries = series;
		if (waterstoragehistory.size() > 0) {
			this.lastentry = waterstoragehistory.lastKey();
		}
		super.refreshDiagram();
	}
		
}