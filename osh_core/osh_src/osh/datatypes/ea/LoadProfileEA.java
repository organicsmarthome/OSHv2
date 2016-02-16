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

package osh.datatypes.ea;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import osh.datatypes.Commodity;
import osh.datatypes.appliance.PowerProfileTick;
import osh.datatypes.ea.TimeSeries;


/**
 * Representation of the power consumption (predicted or recorded)
 * 
 */
public class LoadProfileEA {

	protected HashMap<Commodity, TimeSeries> profiles;
	
	protected TimeSeries voltageProfile;
	protected TimeSeries currentProfile;

	/**
	 * default constructor
	 * 
	 */
	public LoadProfileEA() {
		this.profiles = new HashMap<>();
	}
	
	/**
	 * convenience constructor
	 * 
	 * @param powerProfile
	 */
	public LoadProfileEA(HashMap<Commodity,List<PowerProfileTick>> powerProfiles) {
		this();
		
		
		for (Entry<Commodity,List<PowerProfileTick>> e : powerProfiles.entrySet()) {
			List<PowerProfileTick> list = e.getValue();
			Commodity commodity = e.getKey();
			
			TimeSeries currentSeries = new ArrayTimeSeries();
			currentSeries.setLength(list.size());
			
			long t = 0;
			for( PowerProfileTick _p : list ) {
				currentSeries.set(t, _p.load);
				t++;
			}
			
			this.profiles.put(commodity, currentSeries);
		}
		
	}
	
	/* OPERATIONS */
	
	/**
	 * Function addition of two load profiles.
	 * Result stored in this object.
	 * Operand is shifted by offset first before operation. 
	 * 
	 * @param operand
	 * @param offset
	 */
	public void add(LoadProfileEA operand, long offset) {
		
		if( this.profiles.get(Commodity.ACTIVEPOWER) != null && operand.profiles.get(Commodity.ACTIVEPOWER) != null )
			this.profiles.get(Commodity.ACTIVEPOWER).add(operand.profiles.get(Commodity.ACTIVEPOWER), offset);
		
		if( this.profiles.get(Commodity.REACTIVEPOWER) != null && operand.profiles.get(Commodity.REACTIVEPOWER) != null )
			this.profiles.get(Commodity.REACTIVEPOWER).add(operand.profiles.get(Commodity.REACTIVEPOWER), offset);
		
		if( this.voltageProfile != null && operand.voltageProfile != null )
			this.voltageProfile.add(operand.voltageProfile, offset);
		if( this.currentProfile != null && operand.currentProfile != null )
			this.currentProfile.add(operand.currentProfile, offset);
	}
	
	/**
	 * Function multiplication of two load profiles.
	 * Result stored in this object 
	 * Operand is shifted by offset first before operation. 
	 * 
	 * @param operand
	 * @param offset
	 */
	public void multiply(LoadProfileEA operand, long offset) {
		
		if( this.profiles.get(Commodity.ACTIVEPOWER) != null && operand.profiles.get(Commodity.ACTIVEPOWER) != null )
			this.profiles.get(Commodity.ACTIVEPOWER).multiply(operand.profiles.get(Commodity.ACTIVEPOWER), offset);
		
		if( this.profiles.get(Commodity.REACTIVEPOWER) != null && operand.profiles.get(Commodity.REACTIVEPOWER) != null )
			this.profiles.get(Commodity.REACTIVEPOWER).multiply(operand.profiles.get(Commodity.REACTIVEPOWER), offset);
		
		if( this.voltageProfile != null && operand.voltageProfile != null )
			this.voltageProfile.multiply(operand.voltageProfile, offset);
		if( this.currentProfile != null && operand.currentProfile != null )
			this.currentProfile.multiply(operand.currentProfile, offset);
	}
	
	/* GETTERS / SETTERS */
	
	public TimeSeries getActivePowerProfile() {
		return this.profiles.get(Commodity.ACTIVEPOWER);
	}
	public void setActivePowerProfile(TimeSeries activePowerProfile) {
		this.profiles.put(Commodity.ACTIVEPOWER, activePowerProfile);
	}
	public TimeSeries getReactivePowerProfile() {
		return this.profiles.get(Commodity.REACTIVEPOWER);
	}
	public void setReactivePowerProfile(TimeSeries reactivePowerProfile) {
		this.profiles.put(Commodity.REACTIVEPOWER, reactivePowerProfile);
	}
	public TimeSeries getVoltageProfile() {
		return voltageProfile;
	}
	public void setVoltageProfile(TimeSeries voltageProfile) {
		this.voltageProfile = voltageProfile;
	}
	public TimeSeries getCurrentProfile() {
		return currentProfile;
	}
	public void setCurrentProfile(TimeSeries currentProfile) {
		this.currentProfile = currentProfile;
	}
}
