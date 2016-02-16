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

package osh.datatypes.limit;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

import osh.datatypes.VirtualCommodity;


/**
 * 
 * @author Ingo Mauser
 *
 */
public class PriceSignal {
	
	private VirtualCommodity commodity;

	private TreeMap<Long, Double> prices;
	private final double UNKNOWN_PRICE = 100;
	
	/** Flag whether the redundant entries have been removed */
	private boolean isCompressed = true;
	
	private long priceUnknownBefore = 0;
	private long priceUnknownAtAndAfter = 0;
	
	
	/**
	 * CONSTRUCTOR
	 * @param commodity
	 */
	public PriceSignal(VirtualCommodity commodity) {
		this.commodity = commodity;
		this.prices = new TreeMap<Long, Double>();
	}
	
	
	public void setPrice(long time, double price) {
		prices.put(time, price);
		isCompressed = false;
	}
	
	
	/**
	 * Sets the interval during which the price is known
	 * 
	 */
	public void setKnownPriceInterval(long start, long end) {
		this.priceUnknownBefore = start;
		this.priceUnknownAtAndAfter = end;
	}
	
	/**
	 * Removes redundant entries
	 */
	public void compress() {
		if (isCompressed)
			return;
		
		Iterator<Entry<Long, Double>> i = prices.entrySet().iterator();
		Double last = null;
		
		while( i.hasNext() ) {
			Entry<Long, Double> e = i.next();
			if(e.getValue().equals(last)) {
				i.remove();
			} else {
				last = e.getValue();
			}
		}
		
		isCompressed = true;
	}
	
	/**
	 * Returns the current price<br>
	 * If there's no price available: return UNKNOWN_PRICE (100 cents)
	 * @param t timeStamp (UnixTime) */
	public double getPrice( long t ) {
		if( t < priceUnknownBefore || t > priceUnknownAtAndAfter ) {
			return UNKNOWN_PRICE;
		}
		
		// Return most recent price
		Entry<Long, Double> entry = prices.floorEntry(t);
		
		if( entry != null ) {
			return entry.getValue();
		}
		else {
			return UNKNOWN_PRICE;
		}
	}
	
	
	/**
	 * Returns the time the price changes after t
	 * 
	 * @param t time after price will change
	 * @return null if there is no next price change
	 */
	public Long getNextPriceChange( long t ) {
		if ( t >= priceUnknownAtAndAfter ) {
			return null;
		}

		compress();

		Long key = prices.higherKey(t);
		if( key == null /* && t < priceUnknownAfter */ ) {
			return priceUnknownAtAndAfter;
		}
		else {
			return key;
		}
	}
	
	
	
	/** returned value is the first time tick which has no price.*/
	public long getPriceUnknownAtAndAfter() {
		return priceUnknownAtAndAfter;
	}
	
	public long getPriceUnknownBefore() {
		return priceUnknownBefore;
	}
	
	public VirtualCommodity getCommodity() {
		return commodity;
	}
	
	public TreeMap<Long, Double> getPrices() {
		return prices;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public PriceSignal clone() {
		PriceSignal clone = new PriceSignal(this.getCommodity());
		
		clone.isCompressed = this.isCompressed;
		clone.priceUnknownBefore = this.priceUnknownBefore;
		clone.priceUnknownAtAndAfter = this.priceUnknownAtAndAfter;
		
		//clone TreeMap
//		for (Entry<Long, Double> e : prices.entrySet()) {
//			double originalValue = e.getValue();
//			clone.prices.put(e.getKey(), originalValue);
//		}
		// clone only map, not the keys and values (not necessary)
		clone.prices = (TreeMap<Long, Double>) this.prices.clone();
		
		return clone;
	}
}
