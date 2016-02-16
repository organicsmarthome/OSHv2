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

package osh.datatypes.registry.globalcontroller;

import java.util.UUID;

import osh.datatypes.registry.CommandExchange;


/**
 * 
 * @author Till Schuberth, Ingo Mauser
 *
 */
public class DofSetCommandExchange extends CommandExchange {

	private Integer firstDof;
	private Integer secondDoF;
	
	public DofSetCommandExchange(
			UUID sender, 
			UUID receiver, 
			Long timestamp, 
			Integer firstDof,
			Integer secondDoF) {
		super(sender, receiver, timestamp);
		
		if (firstDof != null) {
			this.firstDof = firstDof;
		}
		if (secondDoF != null) {
			this.secondDoF = secondDoF;
		}
		
		if (firstDof != null && firstDof < 0 ) throw new IllegalArgumentException("First Dof is less than zero");
		if (secondDoF != null && secondDoF < 0 ) throw new IllegalArgumentException("Second Dof is less than zero");
	}

	public Integer getDof() {
		return firstDof;
	}
	
	public Integer getSecondDof() {
		return secondDoF;
	}
}
