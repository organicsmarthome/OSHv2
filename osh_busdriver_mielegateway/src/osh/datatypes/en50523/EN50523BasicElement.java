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
 * Source: DIN EN 50523-1:2010-05, p.26
 * @author Ingo Mauser
 *
 */
public enum EN50523BasicElement {
	
	CHANGE 		(1, "Aenderung"),
	SEND 		(2, "Senden"),
	REQUEST 	(3, "Abfrage"),
	RESPONSE	(4, "Rueckgabe");
	
	private byte elementID;
	private String descriptionDE;
	
	
	private EN50523BasicElement( int elementID, String descriptionDE) {
		this.elementID = (byte) elementID;
		this.descriptionDE = descriptionDE;
	}


	public byte getElementID() {
		return elementID;
	}

	public String getDescriptionDE() {
		return descriptionDE;
	}

}
