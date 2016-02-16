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

package osh.datatypes.registry.details.common;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;




import osh.datatypes.registry.StateExchange;


/**
 * 
 * @author Florian Allerding, Kaibin Bao, Till Schuberth, Ingo Mauser
 *
 */

public class TemperatureDetails extends StateExchange {
	
	private Double temperature;
	

	private HashMap<String, Double> auxiliaryTemperatures = new HashMap<>();
	
	/** JAXB */
	@Deprecated
	private TemperatureDetails() {
		super(null, 0);
	}
	
	public TemperatureDetails(UUID deviceId, long timestamp) {
		super(deviceId, timestamp);
	}
	
	public TemperatureDetails(UUID deviceId, long timestamp, Double temperature) {
		super(deviceId, timestamp);
		this.temperature = temperature;
	}

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature; 
	}

	public Set<String> getAuxiliaryTemperatureKeys() {
		return auxiliaryTemperatures.keySet();
	}

	public HashMap<String, Double> getAuxiliaryTemperatures() {
		return auxiliaryTemperatures;
	}
	
	public void setAuxiliaryTemperatures(HashMap<String, Double> auxiliaryTemperatures) {
		this.auxiliaryTemperatures = auxiliaryTemperatures;
	}

	public void addAuxiliaryTemperatures(String key, Double temperature) {
		this.auxiliaryTemperatures.put(key, temperature);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[ temperature: ");
		if (temperature != null) builder.append(temperature.toString()); else builder.append("null");
		builder.append(", auxiliaryTemperatures: [ \n");
		
		for (Entry<String, Double> e : auxiliaryTemperatures.entrySet()) {
			builder.append('\t').append(e.getKey()).append(": ").append(e.getValue()).append('\n');
		}
		builder.append("] ]\n");
		
		return builder.toString();
	}
	
	/**
	 * Calculates a pseudo distance between two measurements
	 * A value >= 1.0 is considered significant ( which is approx. 1 K difference )
	 * 
	 * @param other
	 * @return
	 */
	public double distance(TemperatureDetails other) {
		if( other == null )
			return Double.MAX_VALUE;
		
		if( this.temperature == null || other.temperature == null )
			return Double.NaN;
		
		double dist = Math.abs( this.temperature - other.temperature );

		/*
		Set<String> keys = new HashSet<String>();
		keys.addAll(auxiliaryTemperatures.keySet());
		keys.addAll(other.auxiliaryTemperatures.keySet());
		for( String key : keys ) {
			Double d1 = this.auxiliaryTemperatures.get(key);
			Double d2 = other.auxiliaryTemperatures.get(key);
			
			if( d1 == null ) d1 = 0.0;
			if( d2 == null ) d2 = 0.0;
			
			dist += Math.abs(d1 - d2);
		}
		*/

		return dist;
	}
	
	@Override
	public boolean equals(Object obj) {
		if( obj == null )
			return false;
		if( !( obj instanceof TemperatureDetails ) )
			return false;
		
		TemperatureDetails other = (TemperatureDetails) obj;
		
		return this.temperature.equals(other.temperature)
				&& this.auxiliaryTemperatures
						.equals(other.auxiliaryTemperatures);
	}
}

