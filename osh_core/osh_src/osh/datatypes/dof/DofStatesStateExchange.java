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

package osh.datatypes.dof;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import osh.datatypes.registry.StateExchange;


/**
 * 
 * @author Ingo Mauser
 *
 */
public class DofStatesStateExchange extends StateExchange {


	private HashMap<UUID, Integer> device1stDegreeOfFreedom;
	private HashMap<UUID, Integer> device2ndDegreeOfFreedom;
	
	
	
	/**
	 * CONSTRUCTOR
	 * @param sender
	 * @param timestamp
	 */
	public DofStatesStateExchange(UUID sender, long timestamp) {
		super(sender, timestamp);
		
		this.device1stDegreeOfFreedom = new HashMap<UUID, Integer>();
		this.device2ndDegreeOfFreedom = new HashMap<UUID, Integer>();
	}

	
	public long getDegreeOfFreedomByDeviceId(UUID deviceId){
		return  this.device1stDegreeOfFreedom.get(deviceId).longValue();
	}
	
	public void addDevice1stDof(UUID deviceId, int device1stDof) {
		this.device1stDegreeOfFreedom.put(deviceId, Integer.valueOf(device1stDof));
	}
	
	public void addDevice2ndDof(UUID deviceId, int device2ndDof) {
		this.device2ndDegreeOfFreedom.put(deviceId, Integer.valueOf(device2ndDof));
	}

	/**
	 * @return the deviceDegreeOfFreedom
	 */
	public HashMap<UUID, Integer> getDevice1stDegreeOfFreedom() {
		return device1stDegreeOfFreedom;
	}
	
	public HashMap<UUID, Integer> getDevice2ndDegreeOfFreedom() {
		return device2ndDegreeOfFreedom;
	}
	
	
	
	
	public void setDevice1stDegreeOfFreedom(
			HashMap<UUID, Integer> device1stDegreeOfFreedom) {
		this.device1stDegreeOfFreedom = device1stDegreeOfFreedom;
	}


	public void setDevice2ndDegreeOfFreedom(
			HashMap<UUID, Integer> device2ndDegreeOfFreedom) {
		this.device2ndDegreeOfFreedom = device2ndDegreeOfFreedom;
	}


	@Override
	public DofStatesStateExchange clone() {
		DofStatesStateExchange cloned = new DofStatesStateExchange(this.getSender(), this.getTimestamp());
		
		for (Entry<UUID,Integer> e : device1stDegreeOfFreedom.entrySet()) {
			cloned.addDevice1stDof(e.getKey(), e.getValue());
		}
		
		for (Entry<UUID,Integer> e : device2ndDegreeOfFreedom.entrySet()) {
			cloned.addDevice2ndDof(e.getKey(), e.getValue());
		}
		
		return cloned;
	}
	
	@Override
	public String toString() {
		return "FirstDoF: " + device1stDegreeOfFreedom.toString() + "\n SecondDoF: " + device2ndDegreeOfFreedom.toString() + " }";
	}

}
