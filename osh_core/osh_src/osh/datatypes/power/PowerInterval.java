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

package osh.datatypes.power;

/**
 * 
 * @author Florian Allerding, Kaibin Bao, Till Schuberth, Ingo Mauser
 *
 */
public class PowerInterval {
	
	private double lowerLimit;
	private double upperLimit;
	
	private final double UNKNOWN_UPPERLIMIT = 43000;
	private final double UNKNOWN_LOWERLIMIT = -43000;
	
	
	/**
	 * CONSTRUCTOR
	 */
	public PowerInterval() {
		this.upperLimit = UNKNOWN_UPPERLIMIT;
		this.lowerLimit = UNKNOWN_LOWERLIMIT;
	}
	
	/**
	 * CONSTRUCTOR
	 * @param powerUpperLimit
	 */
	public PowerInterval(double powerUpperLimit) {
		this.upperLimit = powerUpperLimit;
		this.lowerLimit = UNKNOWN_LOWERLIMIT;
	}
	
	/**
	 * CONSTRUCTOR
	 * @param powerUpperLimit
	 * @param powerLowerLimit
	 */
	public PowerInterval(
			double powerUpperLimit, 
			double powerLowerLimit) {
		this.upperLimit = powerUpperLimit;
		this.lowerLimit = powerLowerLimit;
	}
	

	public double[] getPowerLimits() {
		double[] activeLimits = new double[2];
		activeLimits[0] = this.upperLimit;
		activeLimits[1] = this.lowerLimit;
		return activeLimits;
	}
	
	public double getPowerUpperLimit() {
		return this.upperLimit;
	}
	
	public double getPowerLowerLimit() {
		return this.lowerLimit;
	}
	
	
	public boolean equals(PowerInterval other) {
		if (other == null) {
			return false;
		}
		if (this.lowerLimit == other.lowerLimit 
				&& this.upperLimit == other.upperLimit
				&& this.UNKNOWN_UPPERLIMIT == other.UNKNOWN_UPPERLIMIT
				&& this.UNKNOWN_LOWERLIMIT == other.UNKNOWN_LOWERLIMIT) {
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
	public PowerInterval clone() {
		return new PowerInterval(upperLimit, lowerLimit);
	}
	
	@Override
	public String toString() {
		return "uL=" + upperLimit + " lL=" + lowerLimit;
	}
	
}
