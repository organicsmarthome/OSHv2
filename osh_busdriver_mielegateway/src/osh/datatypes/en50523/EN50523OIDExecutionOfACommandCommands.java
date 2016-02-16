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
 * 
 * Source: DIN EN 50523-1:2010-05, p.42
 * 
 * @author Ingo Mauser, Julian Rothenbacher
 *
 */
public enum EN50523OIDExecutionOfACommandCommands {
	
	//TODO add description
	RESERVED			((byte) 0, ""),
	START				((byte) 1, ""),
	STOP				((byte) 2, ""),
	PAUSE				((byte) 3, ""),
	STARTDEEPFREEZE		((byte) 4, ""),
	STOPDEEPFREEZE		((byte) 5, ""),
	STARTSUPERCOOL		((byte) 6, ""),
	STOPSUPERCOOL		((byte) 7, ""),
	STARTOVERHEAT		((byte) 8, ""),
	STOPOVERHEAT		((byte) 9, ""),
	ACTIVATEGAS			((byte) 10, ""),
	DEACTIVATEGAS		((byte) 11, "");

	
	private byte dinEN50523Command;
	private String descriptionEN;
	
	/**
	 * CONSTRUCTOR
	 */
	private EN50523OIDExecutionOfACommandCommands(byte dinEN50523Command, String descriptionEN) {
		this.dinEN50523Command = dinEN50523Command;
		this.descriptionEN = descriptionEN;
	}
	
	
	public byte getEN50523Command () {
		return dinEN50523Command;
	}
	
	public String getdescriptionEN() {
		return descriptionEN;
	}
	
	//TODO getSignedValue(){}, getUnsignedValue(){}...
	//TODO fromString(String v){}...
	
}
