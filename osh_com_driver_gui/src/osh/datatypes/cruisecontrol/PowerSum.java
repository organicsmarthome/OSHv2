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

package osh.datatypes.cruisecontrol;

import osh.datatypes.cruisecontrol.OptimizedDataStorage.EqualData;


/**
 * 
 * @author Ingo Mauser
 *
 */
public class PowerSum implements EqualData<PowerSum> {

	public double posSum, negSum, sum;

	
	/**
	 * CONSTRUCTOR
	 */
	public PowerSum(double posSum, double negSum, double sum) {
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
	public boolean equalData(PowerSum o) {
		return (Math.abs(posSum - o.posSum) < 4 &&
				Math.abs(negSum - o.negSum) < 4 &&
				Math.abs(sum - o.sum) < 4);
			
	}
	
}
