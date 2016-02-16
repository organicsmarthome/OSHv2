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

package osh.utils.time;

public enum Months {
	JAN("Januar"),
	FEB("Februar"),
	MAR("März"),
	APR("April"),
	MAY("Mai"),
	JUN("Juni"),
	JUL("Juli"),
	AUG("August"),
	SEP("September"),
	OCT("Oktober"),
	NOV("November"),
	DEC("Dezember");
	
	private String name;
	
	private Months(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
