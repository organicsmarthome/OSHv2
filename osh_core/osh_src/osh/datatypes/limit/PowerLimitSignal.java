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
import java.util.TreeMap;
import java.util.Map.Entry;

import osh.datatypes.power.PowerInterval;


/**
 * Representation of the Complex Power Limitation Signal for Shot Term Optimization 
 * @author Ingo Mauser
 *
 */
public class PowerLimitSignal {
	
	private boolean isCompressed = true;
	
	private long limitUnknownBefore = 0;
	private long limitUnknownAtAndAfter = 0;

	//private TreeMap<Long, ComplexPower> powerLimits;
	private TreeMap<Long, PowerInterval> powerLimits;
	
	// means the limit of a 64A three phase supply in complex power with cosPhi = 0.95 (inductive)
	//private final ComplexPower UNKNOWN_LIMIT = new ComplexPower(430000, 2, 0.95, true); 
	private final PowerInterval UNKNOWN_LIMIT = new PowerInterval();
	
	
	public PowerLimitSignal() {
		powerLimits = new TreeMap<Long, PowerInterval>();
	}
	
	/**
	 * Sets the interval during which the power limit is known
	 * 
	 */
	public void setKnownPowerLimitInterval( long start, long end ) {
		this.limitUnknownBefore = start;
		this.limitUnknownAtAndAfter = end;
	}
	
	/**
	 * Removes redundant entries
	 */
	public void compress() {
		if( isCompressed )
			return;
		
		Iterator<Entry<Long, PowerInterval>> i = powerLimits.entrySet().iterator();
		PowerInterval last = null;
		
		while( i.hasNext() ) {
			Entry<Long, PowerInterval> e = i.next();
			if( e.getValue().equals(last)) {
				i.remove();
			} else {
				last = e.getValue();
			}
		}
		
		isCompressed = true;
	}
	
//	public boolean getIsCompressed() {
//		return isCompressed;
//	}
	

	public void setPowerLimit(long time, PowerInterval limit) {
		if (limit == null) throw new NullPointerException("limit is null");
		powerLimits.put(time, limit);
		isCompressed = false;
	}
	
	public void setPowerLimit(
			long time, 
			double activePowerUpperLimit,
			double activePowerLowerLimit) {
		PowerInterval limit = new PowerInterval(
				activePowerUpperLimit,
				activePowerLowerLimit);
		setPowerLimit(time, limit);
	} 
	
	public void setPowerLimit(long time, double powerUpperLimit) {
		PowerInterval limit = new PowerInterval(powerUpperLimit);
		setPowerLimit(time, limit);
	}
	
	
	public PowerInterval getPowerLimitClone(long time) {
		
		Entry<Long, PowerInterval> entry = powerLimits.floorEntry(time);
		
		if (entry != null) {
			return entry.getValue().clone();
		}
		else {
			return UNKNOWN_LIMIT.clone();
		}
	}
	
	public PowerInterval getPowerLimitInterval(Long time) {
		return powerLimits.floorEntry(time).getValue();
	}
	
	public double getPowerUpperLimit(long time) {
		
		Entry<Long, PowerInterval> entry = powerLimits.floorEntry(time);
		
		if (entry != null) {
			return entry.getValue().getPowerUpperLimit();
		}
		else {
			return UNKNOWN_LIMIT.getPowerUpperLimit();
		}
	}
	
	public double getPowerLowerLimit(long time) {
		
		Entry<Long, PowerInterval> entry = powerLimits.floorEntry(time);
		
		if (entry != null) {
			return entry.getValue().getPowerLowerLimit();
		}
		else {
			return UNKNOWN_LIMIT.getPowerLowerLimit();
		}
	}
	
	
	/**
	 * 
	 * @param ps old power limit signal
	 * @param base offset time for new power limit signal 
	 * @param resolution New resolution in seconds
	 * @return
	 */
	public PowerLimitSignal scalePowerLimitSignal(long base, long resolution) 
	{
		PowerLimitSignal newPS = new PowerLimitSignal();

		for( Entry<Long, PowerInterval> e : powerLimits.entrySet() ) {
			newPS.setPowerLimit((e.getKey() - base) / resolution, e.getValue());
		}
		
		newPS.setKnownPowerLimitInterval(
				(limitUnknownBefore - base) / resolution, 
				(limitUnknownAtAndAfter - base) / resolution);
		
		return newPS;
	}
	
	/**
	 * Returns the time the power limit changes after t
	 * 
	 * @param t time after power limit will change
	 * @return null if there is no next power limit change
	 */
	public Long getNextPowerLimitChange( long t ) {
		if( t >= limitUnknownAtAndAfter ) {
			return null;
		}
			
		compress();

		Long key = powerLimits.higherKey(t);
		
		if( key == null /* && t < priceUnknownAfter */ )
			return limitUnknownAtAndAfter;
		else
			return key;
	}
	
	@Deprecated
	public Long getNextActivePowerLimitChange(long t) {
		if( t >= limitUnknownAtAndAfter ) {
			return null;
		}
			
		compress();

		Long nextChange = powerLimits.higherKey(t);
		Long prevChange = powerLimits.higherKey(t);
		
		boolean flag = true;
		Long returnValue = 0L;
		
		do {
			if( nextChange == null /* && t < priceUnknownAfter */ )
				returnValue = limitUnknownAtAndAfter;
			else
				returnValue = nextChange;
			
			if (nextChange == null || prevChange == null) {
				flag = false;
			}
			else if (powerLimits.get(prevChange).getPowerUpperLimit() 
					== powerLimits.get(nextChange).getPowerUpperLimit()) {
				flag = false;
			}
			else {
				flag = true;
			}
			
		} while (flag);
		
		return returnValue;
	}
	
	@Override
	public PowerLimitSignal clone() {
		PowerLimitSignal clone = new PowerLimitSignal();
		
		clone.isCompressed = this.isCompressed;
		clone.limitUnknownBefore = this.limitUnknownBefore;
		clone.limitUnknownAtAndAfter = this.limitUnknownAtAndAfter;
		
		//deep clone tree map
		for (Entry<Long, PowerInterval> e : powerLimits.entrySet()) {
			clone.powerLimits.put(e.getKey(), e.getValue().clone());
		}
		
		return clone;
	}
	
	/** returned value is the first time tick which has no limit.*/
	public long getPowerLimitUnknownAtAndAfter() {
		return limitUnknownAtAndAfter;
	}
	
	/** returned value is the first time tick which has a limit.*/
	public long getPowerLimitUnknownBefore() {
		return limitUnknownBefore;
	}
	
}
