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
 * 8-bit HEX (00 to FF)<br>
 * Source: DIN EN 50523-2:2010-05, p.13: "80 to BF are (somewhere) defined"<br>
 * <br>
 * new standardized OIDs (6, -, 90 to AF)<br>
 * copyrighted OIDs (6, –, B0 to BF)
 * 
 * @author Ingo Mauser
 * 
 */
public enum EN50523OID {

	EXECUTIONOFACOMMAND			((byte) 0x80, EN50523Cluster.HOUSEHOLD, EN50523Category.COMMON, 		"execute command", "Ausfuehren eines Befehls"),
	WASHINGPARAMETERS	 		((byte) 0x81, EN50523Cluster.HOUSEHOLD, EN50523Category.WET,		 	"washing parameters", "Waschparameter"),
	COOKINGPARAMETERS 			((byte) 0x81, EN50523Cluster.HOUSEHOLD, EN50523Category.HOT, 			"", "Kochparameter"),
	REFRIGERATIONPARAMETERS		((byte) 0x81, EN50523Cluster.HOUSEHOLD, EN50523Category.COLD, 			"", "Kuehlparameter"),
	AIRCONDITIONINGPARAMETERS	((byte) 0x81, EN50523Cluster.HOUSEHOLD, EN50523Category.VENTILATION, 	"", "Klimatisierungsparameter"),
	WATERWARMINGPARAMETERS 		((byte) 0x81, EN50523Cluster.HOUSEHOLD, EN50523Category.HEAT, 	"", "Wassererwaermungsparameter"),
	STARTTIME			 		((byte) 0x82, EN50523Cluster.HOUSEHOLD, EN50523Category.COMMON, "", "Startzeit"),
	ENDTIME				 		((byte) 0x83, EN50523Cluster.HOUSEHOLD, EN50523Category.COMMON, "", "Endzeit"),
	APPLIANCESTATE		 		((byte) 0x84, EN50523Cluster.HOUSEHOLD, EN50523Category.COMMON, "", "Geraetezustand"),
	REMAININGTIME		 		((byte) 0xD2, EN50523Cluster.HOUSEHOLD, EN50523Category.COMMON, "", "Verbleibende Zeit"),
	CURRENTPHASE		 		((byte) 0x85, EN50523Cluster.HOUSEHOLD, EN50523Category.COMMON, "", "Gegenwaertige Phase"),
	SELECTEDTEMPERATURE 		((byte) 0x87, EN50523Cluster.HOUSEHOLD, EN50523Category.COMMON, "", "Einstelltemperatur"),
	DISPLAYEDTEMPERATURE 		((byte) 0x88, EN50523Cluster.HOUSEHOLD, EN50523Category.COMMON, "", "Angezeigte Temperatur"),
	CURRENTTEMPERATURE	 		((byte) 0x89, EN50523Cluster.HOUSEHOLD, EN50523Category.COMMON, "", "Aktuelle Temperatur"),
	NORMALEVENTS		 		((byte) 0x8A, EN50523Cluster.HOUSEHOLD, EN50523Category.COMMON, "", "Normalereignisse"),
	ALARMEVENTS 				((byte) 0x86, EN50523Cluster.HOUSEHOLD, EN50523Category.COMMON, "", "Alarmereignisse"),
	REDUCTION			 		((byte) 0x8B, EN50523Cluster.HOUSEHOLD, EN50523Category.COMMON, "", "Reduktion"),
	EXTENDEDIDENTIFICATION 		((byte) 0x8C, EN50523Cluster.HOUSEHOLD, EN50523Category.COMMON, "", "Aufforderung zur erweiterten Identifizierung"),
	STANDARDIDENTIFICATION 		((byte) 0x8D, EN50523Cluster.HOUSEHOLD, EN50523Category.COMMON, "", "Aufforderung zur Standardidentifizierung"),
	DIAGNOSISOPERATION 			((byte) 0x8E, EN50523Cluster.HOUSEHOLD, EN50523Category.COMMON, "", "Diagnoseoperation"),
	DIAGNOSISDATA		 		((byte) 0x8F, EN50523Cluster.HOUSEHOLD, EN50523Category.COMMON, "", "Diagnosedaten"),
	TIME 						((byte) 0xC0, EN50523Cluster.ALL, 		EN50523Category.COMMON, "", "Uhrzeit"),
	DATE				 		((byte) 0xC1, EN50523Cluster.ALL, 		EN50523Category.COMMON, "", "Datum");
	
	private byte oid;
	private String descriptionEN;
	private String descriptionDE;
	private EN50523Cluster cluster;
	private EN50523Category category;
	
	
	/**
	 * CONSTRUCTOR
	 * @param oid
	 * @param descriptionEN
	 * @param descriptionDE
	 * @param cluster
	 * @param category
	 */
	private EN50523OID(byte oid, EN50523Cluster cluster, EN50523Category category, String descriptionEN, String descriptionDE) {
		this.oid = oid;
		this.descriptionEN = descriptionEN;
		this.descriptionDE = descriptionDE;
		this.cluster = cluster;
		this.category = category;
	}

	
	public byte getOid() {
		return oid;
	}
	
	public String getDescriptionEN() {
		return descriptionEN;
	}

	public String getDescriptionDE() {
		return descriptionDE;
	}

	public EN50523Cluster getCluster() {
		return cluster;
	}

	public EN50523Category getCategory() {
		return category;
	}
	
}
