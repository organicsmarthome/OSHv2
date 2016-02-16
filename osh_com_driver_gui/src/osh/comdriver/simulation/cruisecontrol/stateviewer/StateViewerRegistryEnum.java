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

package osh.comdriver.simulation.cruisecontrol.stateviewer;

/**
 * 
 * @author Till Schuberth
 *
 */
public enum StateViewerRegistryEnum {
	
	OC("OC Registry"), DRIVER("driver registry");
	
	private String str;
	
	private StateViewerRegistryEnum(String str) {
		this.str = str;
	}
	
	
	/**
	 * CONSTRUCTOR
	 * @param str
	 * @return
	 */
	public static StateViewerRegistryEnum findByString(String str) {
		if (str == null) return null;
		
		for (StateViewerRegistryEnum e : StateViewerRegistryEnum.values()) {
			if (e.str.equals(str)) return e;
		}
		
		return null;
	}

	@Override
	public String toString() {
		return str;
	}
	
}
