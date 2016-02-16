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
 * DIN EN 50523:<br>
 * <br>
 * * Byte I ist das i-te uebermittelte Byte des Datenfelds<br>
 * * Big-Endian-Zuordnung<br>
 * <br>
 * * Bei zwei Bytes: signifikanteste Byte ist das erste Feld<br>
 * <br>
 * * Format fuer vorzeichenbehaftete Integer: 2-er-Komplement<br>
 * <br>
 * * Zeichensatz ist ASCII<br>
 * <br>
 * Non-Standard: SAMSUNG 0x5341 (=SA)
 * 
 * @author Ingo Mauser
 *
 */
public enum EN50523Company {
	
	ARCELIK 	((short) 0x4152, "Arcelik"), 		//AR
	BSH 		((short) 0x4253, "BSH"),			//BS
	CANDY 		((short) 0x4341, "Candy"),			//CA
	CLAGE 		((short) 0x434C, "CLAGE"),			//CL
	ELECTROLUX	((short) 0x454C, "Electrolux"),		//EL
	ELCOBRANDT	((short) 0x4542, "ElcoBrandt"),		//EB
	FAGOR 		((short) 0x4641, "Fagor"),			//FA
	LIEBHERR 	((short) 0x4C48, "Liebherr"),		//LH
	GORENJE 	((short) 0x474F, "Gorenje"),		//GO
	INDESIT 	((short) 0x4943, "Indesit Company"),//IC
	MIELE 		((short) 0x4D49, "Miele"),			//MI
	V_ZUG 		((short) 0x565A, "V-ZUG AG"),		//VZ
	WHIRLPOOL 	((short) 0x5748, "Whirlpool"),		//WH
	
	// non-standard:
	SAMSUNG 	((short) 0x5341, "Samsung");		//SA
	
	
	private short companyID;
	private String companyName;
	
	
	/**
	 * CONSTRUCTOR
	 */
	private EN50523Company(
			short id, 
			String companyName) {
		this.companyID = id;
		this.companyName = companyName;
	}

	
	public short getCompanyID() {
		return companyID;
	}
	
	public String getCompanyName() {
		return companyName;
	}
	
}
