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

package osh.mgmt.localobserver.chp;

import java.util.Arrays;

/**
 * 
 * @author Florian Allerding, Till Schuberth
 *
 */
public class NeededEnergyArray implements INeededEnergy {

	private long begin;
	private long duration;
	private long minintegral[], minenergy[];
	private long maxintegral[], maxenergy[];
	
	public static final int SECONDSPERTICK = 60;
	
	
	/**
	 * Array with required Energy and maximum Energy
	 * @param begin
	 * @param energy energy array in Ws, one element per minute
	 */
	public NeededEnergyArray(long begin, long[] minenergy, long[] maxenergy) {
		if (minenergy.length != maxenergy.length) throw new IllegalArgumentException("array lengthes don't match");
		if (minenergy.length == 0) {
			minenergy = new long[] { 0 };
			maxenergy = new long[] { 0 };
		}
		
		this.begin = begin;
		this.duration = minenergy.length * SECONDSPERTICK;
		this.minenergy = minenergy;
		this.maxenergy = maxenergy;
		this.minintegral = getEnergyIntegral(minenergy);
		this.maxintegral = getEnergyIntegral(maxenergy);
	}
	
	private static long[] getEnergyIntegral(long[] energy) {
		long[] integral = new long[energy.length];
		
		//FIXME: use a numerically more stable algorithm (use trees!)
		long sum = 0;
		
		for (int i = 0; i < energy.length; i++) {
			sum += energy[i];
			integral[i] = sum;
		}
		return integral;
	}

	private long getEnergy(long t, long[] integral, long[] energy) {
		if (t <= begin) {
			return 0;
		} else if (t < begin + duration) {
			long time = t - begin;
			int index = (int) (time / SECONDSPERTICK);
			int integralindex = index - 1;
			long ret;
			
			if (integralindex < 0) {
				ret = 0;
			} else {
				ret = integral[integralindex];
			}
			ret += ((long) (time % SECONDSPERTICK)) / SECONDSPERTICK * energy[index];
			
			return ret;
		} else /* (end > begin + duration) */ {
			return integral[integral.length - 1];
		}
	}
	
	@Override
	public long getMaxUsableEnergy(long end) {
		return getEnergy(end, maxintegral, maxenergy);
	}

	@Override
	public long getNeededEnergy(long end) {
		return getEnergy(end, minintegral, minenergy);
	}

	@Override
	public NeededEnergyArray clone() {
		NeededEnergyArray clone;
		
		try {
			clone = (NeededEnergyArray) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
		clone.minenergy = Arrays.copyOf(minenergy, minenergy.length);
		clone.minintegral = Arrays.copyOf(minintegral, minintegral.length);
		clone.maxenergy = Arrays.copyOf(maxenergy, maxenergy.length);
		clone.maxintegral = Arrays.copyOf(maxintegral, maxintegral.length);
		
		return clone;
	}

}
