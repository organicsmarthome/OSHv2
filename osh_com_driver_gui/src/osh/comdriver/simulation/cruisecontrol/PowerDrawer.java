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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.jfree.data.xy.XYSeries;

import osh.datatypes.Commodity;
import osh.datatypes.cruisecontrol.PowerSum;


/**
 * 
 * @author Florian Allerding, Kaibin Bao, Ingo Mauser, Till Schuberth
 *
 */
@SuppressWarnings("serial")
class PowerDrawer extends AbstractDrawer {

	private List<XYSeries> currentSeries = null;
	private long numberOfEntries = 0;
	
	public PowerDrawer() {
		super("Power", true);
	}
	
	@Override
	protected String getAxisName() {
		return "power";
	}

	@Override
	protected List<XYSeries> getSeries(long begin, long end) {
		return (currentSeries == null ? new LinkedList<XYSeries>() : currentSeries);
	}

	@Override
	protected long getNumberOfEntries() {
		return numberOfEntries;
	}

	public void refreshDiagram(
			long timestamp,
			HashMap<Commodity, 
			TreeMap<Long, 
			PowerSum>> commodityPowerSum) {
		List<XYSeries> series = new ArrayList<XYSeries>();

		TreeMap<Long, PowerSum> electricPowerSeries = 
				commodityPowerSum.get(Commodity.ACTIVEPOWER);

		// FIXME

		XYSeries positive = new XYSeries("positive");
		XYSeries negative = new XYSeries("negative");
		XYSeries sum = new XYSeries("sum");
		
		// retain only two days (there should only be data newer than two days being feed-in)
		positive.setMaximumItemCount(2 * 86400);
		negative.setMaximumItemCount(2 * 86400);
		sum.setMaximumItemCount(2 * 86400);

		for( Entry<Long,PowerSum> entry : electricPowerSeries.entrySet() ) {
			positive.add(entry.getKey().doubleValue() * 1000, entry.getValue().getPosSum());
			negative.add(entry.getKey().doubleValue() * 1000, entry.getValue().getNegSum());
			sum.add(entry.getKey().doubleValue() * 1000, entry.getValue().getSum());
		}

		series.add(positive);
		series.add(negative);
		series.add(sum);

		this.currentSeries = series;
		if (commodityPowerSum.size() > 0) {
			this.numberOfEntries = electricPowerSeries.lastKey();
		}
		super.refreshDiagram();
	}
	
}