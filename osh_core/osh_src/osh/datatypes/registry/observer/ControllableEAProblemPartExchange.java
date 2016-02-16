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

package osh.datatypes.registry.observer;

import java.util.BitSet;
import java.util.UUID;


import osh.configuration.system.DeviceTypes;
import osh.datatypes.ea.ISolution;

/**
 * 
 * @author Florian Allerding, Ingo Mauser, Till Schuberth
 *
 * @param <PhenotypeType>
 */

public abstract class ControllableEAProblemPartExchange<PhenotypeType extends ISolution> 
							extends EAProblemPartExchange<PhenotypeType> {

	/** for JAXB, never use */
	@Deprecated
	private ControllableEAProblemPartExchange() { 
		super(null, 0, 0, 0, DeviceTypes.OTHER); 
	}
	
	/**
	 * CONSTRUCTOR
	 * @param deviceId
	 * @param timestamp
	 * @param bitcount
	 * @param toBeScheduled
	 */
	public ControllableEAProblemPartExchange(
			UUID deviceId, 
			long timestamp,
			int bitcount, 
			boolean toBeScheduled,
			long optimizationHorizon,
			DeviceTypes deviceType) {
		super(deviceId, timestamp, bitcount, toBeScheduled, optimizationHorizon, deviceType);
	}


	/**
	 * converts a bitset to a long value. 
	 * @param bitset to be converted
	 * @return long value from bitset
	 */
	//tested
	public static long bitset2long(BitSet bitset) {
		if (bitset.length() > Long.SIZE - 1) throw new IllegalArgumentException("bitset is too big");
		long ret = 0;
		int bitsize = bitset.length();
		for (int i = bitsize; i >= 0; i--) {
			ret <<= 1;
			if (bitset.get(i)) ret |= 1L;
		}
		
		return ret;
	}
	
	//tested
	public static long gray2long(BitSet gray) {
		BitSet binary = new BitSet();
		int bitsize = gray.length();

		if (bitsize > 0) binary.set(bitsize - 1, gray.get(bitsize - 1));

		for (int i = bitsize - 2; i >= 0; i--) {
			if (binary.get(i + 1) != gray.get(i)) //xor
				binary.set(i);
		}

		return bitset2long(binary);
	}

	public static BitSet long2bitset(long n) {
		if (n < 0) throw new IllegalArgumentException("n is negative");
		
		BitSet ret = new BitSet();
		long mask = 0x01L;

		for (int i = 0; i < 64; i++) {
			if ((n & mask) != 0L) ret.set(i);
			mask <<= 1;
		}

		return ret;
	}


	public abstract String solutionToString(BitSet bits);
}
