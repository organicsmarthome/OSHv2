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

package osh.hal.exchange;

import java.util.UUID;

import osh.datatypes.WaterStorageOptimizedDataStorage.EqualData;


/**
 * 
 * @author Florian Allerding, Kaibin Bao, Ingo Mauser, Till Schuberth
 *
 */
public class WaterStorageSumDetailsComExchange extends HALComExchange implements EqualData<WaterStorageSumDetailsComExchange> {
	
	private int maxDeltaForEquality = 4;
	private double posSum, negSum, sum;
	

	/**
	 * CONSTRUCTOR
	 */
	public WaterStorageSumDetailsComExchange(UUID deviceID, Long timestamp, double posSum, double negSum, double sum) {
		super(deviceID, timestamp);
		this.posSum = posSum;
		this.negSum = negSum;
		this.sum = sum;
	}
	
	
	public double getPosSum() {
		return posSum;
	}

	public double getNegSum() {
		return negSum;
	}

	public double getSum() {
		return sum;
	}

	@Override
	public boolean equalData(WaterStorageSumDetailsComExchange o) {
		return (Math.abs(posSum - o.posSum) < maxDeltaForEquality &&
				Math.abs(negSum - o.negSum) < maxDeltaForEquality &&
				Math.abs(sum - o.sum) < maxDeltaForEquality);
			
	}
	
}
