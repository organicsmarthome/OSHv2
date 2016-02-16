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

package osh.datatypes.en50523;

/**
 * 5-bit HEX [DIN EN 50523-2 p.12] / 4-bit HEX (0 to F) [DIN EN 50523-2 p.13]
 * @author Ingo Mauser
 *
 */
public enum EN50523Category {
	
	COMMON			((byte) 0x1, "common household appliance", "allgemeines Haushaltsgeraet"),
	VENTILATION 	((byte) 0x2, "ventilation", "Lueftung"),
	WET				((byte) 0xA, "wet appliance", "Nass"),
	HOT				((byte) 0xB, "hot apliance", "Heiss"),
	COLD			((byte) 0xC, "cold appliance", "Kaelte"),
	HEAT			((byte) 0xD, "warm appliance", "Waerme");
	
	private byte categoryID;
	private String descriptionEN;
	private String descriptionDE;
	
	
	/**
	 * CONSTRUCTOR
	 */
	private EN50523Category(byte categoryID, String descriptionEN, String descriptionDE) {
		this.categoryID = categoryID;
		this.descriptionEN = descriptionEN;
		this.descriptionDE = descriptionDE;
	}


	public byte getCategoryID() {
		return categoryID;
	}

	public String getDescriptionEN() {
		return descriptionEN;
	}

	public String getDescriptionDE() {
		return descriptionDE;
	}
	
}
