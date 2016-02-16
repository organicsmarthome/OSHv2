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


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import osh.configuration.system.DeviceTypes;
import osh.datatypes.ea.ISolution;
import osh.datatypes.ea.Schedule;
import osh.datatypes.registry.StateExchange;
import osh.datatypes.registry.globalcontroller.EASolutionCommandExchange;

/**
 * 
 * @author Florian Allerding, Ingo Mauser, Till Schuberth
 *
 * @param <PhenotypeType>
 */

public abstract class EAProblemPartExchange<PhenotypeType extends ISolution> extends StateExchange {

	private int bitcount;
	private boolean toBeScheduled;
	
	private long optimizationHorizon;
	
	private DeviceTypes deviceType; // necessary to distinguish PV power / chp power / device power / appliance power for pricing 

	
	/** for JAXB, never use */
	@Deprecated
	private EAProblemPartExchange() { super(null, 0); }
	
	/**
	 * CONSTRUCTOR
	 * @param deviceId
	 * @param timestamp
	 * @param bitcount
	 * @param toBeScheduled
	 * @param deviceType
	 */
	public EAProblemPartExchange(
			UUID deviceId, 
			long timestamp, 
			int bitcount, 
			boolean toBeScheduled,
			long optimizationHorizon,
			DeviceTypes deviceType) {
		super(deviceId, timestamp);
		
		this.bitcount = bitcount;
		this.toBeScheduled = toBeScheduled;
		this.optimizationHorizon = optimizationHorizon;
		this.deviceType = deviceType;
	}

	public EAProblemPartExchange(
			UUID deviceId, 
			long timestamp, 
			int bitcount,
			int optimizationHorizon,
			DeviceTypes deviceType) {
		this(deviceId, timestamp, bitcount, true, optimizationHorizon, deviceType);
	}

	/** returns the number of bits. Don't change this value while scheduling! */
	public final int getBitCount() {
		return bitcount;
	}

	public void setBitCount(int bitcount) {
		this.bitcount = bitcount;
	}

	public UUID getDeviceId() {
		return getSender();
	}

	public boolean isToBeScheduled() {
		return toBeScheduled;
	}
	
	protected void setToBeScheduled(boolean toBeScheduled) {
		this.toBeScheduled = toBeScheduled;
	}
	
	public long getOptimizationHorizon() {
		return optimizationHorizon;
	}

	public DeviceTypes getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceTypes deviceType) {
		this.deviceType = deviceType;
	}

	/**
	 * Schedule with absolute times
	 * @param solution
	 * @return
	 */
	public abstract Schedule getSchedule(BitSet solution);

	public abstract String problemToString();

	@Override
	public String toString() {
		return problemToString();
	}

	/**
	 * transform the given bit string to a valid phenotype
	 * @param solution
	 * @return
	 */
	public abstract PhenotypeType transformToPhenotype(BitSet solution);

	public final EASolutionCommandExchange<PhenotypeType> transformToPhenotype(
			UUID sender, 
			UUID receiver, 
			long timestamp, 
			BitSet solution) {
		return new EASolutionCommandExchange<PhenotypeType>(sender, receiver, timestamp, transformToPhenotype(solution));
	}

	/**
	 * Is invoked before every reschedule. So the EApart can adapt it's current encoding to the current time
	 * @param currentTime
	 */
	public abstract void recalculateEncoding(long currentTime);
}
