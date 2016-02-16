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

package osh.datatypes.gui;

import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * @author ???
 *
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class DeviceTableEntry implements Comparable<DeviceTableEntry> {
	
	private int entry;
	private UUID id;
	private String name;
	private int bits;
	private String representation;
	
	
	@Deprecated
	public DeviceTableEntry() {
		this(0, null, "", 0, "");
	}
	
	public DeviceTableEntry(int entry, UUID id, String name, int bits, String representation) {
		super();
		
		this.entry = entry;
		this.id = id;
		this.name = name;
		this.bits = bits;
		this.representation = representation;
	}

	
	public int getEntry() {
		return entry;
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public int getBits() {
		return bits;
	}

	public String getRepresentation() {
		return representation;
	}
	
	@Override
	public int compareTo(DeviceTableEntry o) {
		return entry - o.entry;
	}
	
	@Override
	public DeviceTableEntry clone() {
		return new DeviceTableEntry(
				this.entry, this.id, this.name, this.bits, this.representation);
	}
}