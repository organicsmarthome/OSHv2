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

package osh.datatypes;

import javax.xml.bind.annotation.XmlEnumValue;

/**
 * 
 * @author Ingo Mauser
 *
 */
public enum Commodity {
	
	@XmlEnumValue("activepower")
	ACTIVEPOWER				("activepower", "Wirkleistung", "active power", "W"),	// 
	
	@XmlEnumValue("reactivepower")
	REACTIVEPOWER			("reactivepower", "Blindleistung", "reactive power", "var"),	// 
	
	@XmlEnumValue("naturalgas")
	NATURALGASPOWER			("naturalgas", "Erdgas", "natural gas", "W"),	// 

	@XmlEnumValue("warmwater")
	WARMWATERPOWER			("warmwaterpower", "Warmwasser", "warm water", "W"),	// 
	
	@XmlEnumValue("coldwater")
	COLDWATERPOWER			("coldwaterpower", "Kaltwasser", "cold water", "W");	// 
	
	private final String commodity;
	
	private final String descriptionDE;
	
	private final String descriptionEN;
	
	private final String unit;
	
	/**
	 * CONSTRUCTOR
	 * @param state byte (meaning is UNSIGNED byte (0 - 255))
	 */
	Commodity(String commodity, String descriptionDE, String descriptionEN, String unit) {
		this.commodity = commodity;
		this.descriptionDE = descriptionDE;
		this.descriptionEN = descriptionEN;
		this.unit = unit;
	}
	
	
	public static Commodity fromString(String v) {
        for (Commodity c: Commodity.values()) {
            if (c.commodity.equalsIgnoreCase(v)) {
                return c;
            }
        }
        
        throw new IllegalArgumentException(v);
    }

	@Override
	public String toString() {
		return this.commodity;
	}
	
	public String getCommodity() {
		return commodity;
	}

	public String getDescriptionDE() {
		return descriptionDE;
	}

	public String getDescriptionEN() {
		return descriptionEN;
	}
	
	
	public String getUnit() {
		return unit;
	}
	
}
