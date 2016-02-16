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
public enum VirtualCommodity {
	
	@XmlEnumValue("activepowerexternal")
	ACTIVEPOWEREXTERNAL		("activepowerexternal", "Wirkleistung Bezug", "active power consumption", "W"),	// 
	
	@XmlEnumValue("reactivepower")
	REACTIVEPOWEREXTERNAL	("reactivepowerexternal", "Blindleistung Bezug", "reactive power consumption", "W"),	// 
	
	@XmlEnumValue("pvactivepowerfeedin")
	PVACTIVEPOWERFEEDIN		("pvactivepowerfeedin", "Wirkleistung Rückspeisung von PV", "active power feedin from PV", "W"),	// 
	
	@XmlEnumValue("pvactivepowerautoconsumption")
	PVACTIVEPOWERAUTOCONSUMPTION ("pvactivepowerautoconsumption", "Wirkleistung Eigenstromnutzung von PV", "active power auto consumption from PV", "W"),	//
	
	@XmlEnumValue("chpactivepowerfeedin")
	CHPACTIVEPOWERFEEDIN		("chpactivepowerfeedin", "Wirkleistung Rückspeisung von CHP", "active power feedin from CHP", "W"),	// 
	
	@XmlEnumValue("chpactivepowerautoconsumption")
	CHPACTIVEPOWERAUTOCONSUMPTION ("chpactivepowerautoconsumption", "Wirkleistung Eigenstromnutzung von CHP", "active power auto consumption from CHP", "W"),	//
	
	@XmlEnumValue("naturalgaspowerexternal")
	NATURALGASPOWEREXTERNAL	("naturalgaspowerexternal", "Leistung Erdgas", "natural gas power consumption", "W");	// 
	
	private final String commodity;
	
	private final String descriptionDE;
	
	private final String descriptionEN;
	
	private final String unit;
	
	/**
	 * CONSTRUCTOR
	 * @param state byte (meaning is UNSIGNED byte (0 - 255))
	 */
	VirtualCommodity(String commodity, String descriptionDE, String descriptionEN, String unit) {
		this.commodity = commodity;
		this.descriptionDE = descriptionDE;
		this.descriptionEN = descriptionEN;
		this.unit = unit;
	}
	
	
	public static VirtualCommodity fromString(String v) {
		for (VirtualCommodity c: VirtualCommodity.values()) {
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
