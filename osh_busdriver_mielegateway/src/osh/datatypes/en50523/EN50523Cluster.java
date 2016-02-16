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
 * 3-bit HEX as BYTE (0 to 7)
 * @author Ingo Mauser
 *
 */
public enum EN50523Cluster {
	
	ALL			((byte) 0x3, "all clusters", "allen Clustern gemeinsam"),
	HOUSEHOLD	((byte) 0x6, "household cluster", "Haushaltscluster");
	
	private byte clusterID;
	private String descriptionEN;
	private String descriptionDE;
	
	
	/**
	 * CONSTRUCTOR
	 */
	private EN50523Cluster(byte clusterID, String descriptionEN, String descriptionDE) {
		this.clusterID = clusterID;
		this.descriptionEN = descriptionEN;
		this.descriptionDE = descriptionDE;
	}


	public byte getClusterID() {
		return clusterID;
	}

	public String getDescriptionEN() {
		return descriptionEN;
	}

	public String getDescriptionDE() {
		return descriptionDE;
	}
	
}
