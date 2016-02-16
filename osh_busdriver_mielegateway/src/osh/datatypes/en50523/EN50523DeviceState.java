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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * DIN EN 50523 Device State as BYTE (unsigned)
 * @author Kaibin Bao, Ingo Mauser
 *
 */
@XmlType
public enum EN50523DeviceState {
	
	@XmlEnumValue("99")
	UNKNOWN (99, "UNBEKANNT"),
	
	/**
	 * Devices:	???<br>
	 * State:	Device OFF, but communication module may still have power<br>
	 * DE: 		AUS<br>
	 * Meaning:	???
	 */
	@XmlEnumValue("1")
	OFF				(1, "AUS"), 
	
	/**
	 * Devices:	???<br>
	 * State:	???<br>
	 * DE:		STANDBY<br>
	 * Meaning:	???
	 */
	@XmlEnumValue("2")
	STANDBY			( 2, "STANDBY"),
	
	/**
	 * Devices:	???<br>
	 * State:	???<br>
	 * DE: 		PROGRAMMIERT<br>
	 * Meaning:	???
	 */
	@XmlEnumValue("3")
	PROGRAMMED		( 3, "PROGRAMMIERT"),
	
	/**
	 * Devices:	???<br>
	 * State:	Device PROGRAMMED, now WAITING TO START<br>
	 * DE:		PROGRAMMIERT, AUF START WARTEND<br>
	 * Meaning:	Program has been selected and confirmed (by user)
	 */
	@XmlEnumValue("4")
	PROGRAMMEDWAITINGTOSTART	( 4, "PROGRAMMIERT, AUF START WARTEND"),

	/**
	 * Devices:	???<br>
	 * State:	???<br>
	 * DE:		IN BETRIEB<br>
	 * Meaning:	???
	 */
	@XmlEnumValue("5")
	RUNNING					( 5, "IN BETRIEB"),
	
	/**
	 * Devices:	???<br>
	 * State:	Device: current program run PAUSED<br>
	 * DE:		PAUSE<br>
	 * Meaning:	???
	 */
	@XmlEnumValue("6")
	PAUSE					( 6, "PAUSE"),
	
	/**
	 * Devices:	???<br>
	 * State:	Device: current program run finished<br>
	 * DE:		PROGRAMMIERTES ENDE<br>
	 * Meaning:	???
	 */
	@XmlEnumValue("7")
	ENDPROGRAMMED			( 7, "PROGRAMMIERTES ENDE"),
	
	/**
	 * Devices:	???<br>
	 * State:	???<br>
	 * DE:		STÖRUNG<br>
	 * Meaning:	???
	 */
	@XmlEnumValue("8")
	FAILURE					( 8, "STÖRUNG"),
	
	/**
	 * Devices:	???<br>
	 * State:	INTERRUPT current program<br>
	 * DE:		PROGRAMM UNTERBROCHEN<br>
	 * Meaning:	???
	 */
	@XmlEnumValue("9")
	PROGRAMMEINTERRUPTED	( 9, "PROGRAMM UNTERBROCHEN"),
	
	/**
	 * Devices:	???<br>
	 * State:	???<br>
	 * DE:		AUSSER BETRIEB<br>
	 * Meaning:	Machine is idle/disabled
	 */
	@XmlEnumValue("10")
	IDLE					( 10, "AUSSER BETRIEB"),
	
	/**
	 * Devices:	WM<br>
	 * State:	Current program state RINSEHOLD<br>
	 * DE:		SPÜLVERZÖGERUNG<br>
	 * Meaning:	Clothes have been washed, but not spin-dryed (DE: Spülstopp/Spülverzögerung)
	 */
	@XmlEnumValue("11")
	RINSEHOLD				( 11, "SPÜLVERZÖGERUNG"),
	
	/**
	 * Devices:	???<br>
	 * State:	Current program state MAINTENANCE<br>
	 * DE:		WARTUNG<br>
	 * Meaning:	Service mode (maintenance)
	 */
	@XmlEnumValue("12")
	SERVICE					( 12, "WARTUNG"),
	
	/**
	 * Devices:	FR, Freezer-Fridge<br>
	 * State:	???<br>
	 * DE:		TIEFGEFRIEREN<br>
	 * Meaning:	???
	 */
	@XmlEnumValue("13")
	SUPERFREEZING			( 13, "TIEFGEFRIEREN"),
	
	/**
	 * Devices:	???<br>
	 * State:	UNTERKUEHLEN<br>
	 * DE:		AUSSER BETRIEB<br>
	 * Meaning:	???
	 */
	@XmlEnumValue("14")
	SUPERCOOLING			( 14, "UNTERKÜHLEN"),
	
	/**
	 * Devices:	???<br>
	 * State:	???<br>
	 * DE:		ÜBERHITZEN<br>
	 * Meaning:	???
	 */
	@XmlEnumValue("15")
	SUPERHEATING			( 15, "ÜBERHITZEN"),
	
	/**
	 * ### NOT STANDARDIZED ###
	 * Devices:	???<br>
	 * State:	???<br>
	 * DE:		PHASENVERZÖGERUNG<br>
	 * Meaning:	???
	 */
	@XmlEnumValue("111")
	PHASEHOLD		( 111, "PHASENVERZÖGERUNG");
	
	//TODO: Find more values...
	
	/*
	 * Values  64 to 127 are not standardized
	 * Values 128 to 255 are copyrighted (DE: "eigentumsrechtlich geschützt")
	 */
	

	@XmlElement
	private final int stateInt;
	@XmlAttribute
	private final String descriptionDE;
	
	
	/**
	 * CONSTRUCTOR
	 * @param stateInt 0 - 255
	 */
	EN50523DeviceState(int stateInt, String descriptionDE) {
		this.stateInt = stateInt;
		this.descriptionDE = descriptionDE;
	}
	

	
	/**
	 * @return -128 to 127
	 */
	public byte getByteValue() {
		return (byte) stateInt;
	}
	
    public static EN50523DeviceState fromString(String v) {
        for (EN50523DeviceState c: EN50523DeviceState.values()) {
            if (Integer.toString(c.stateInt).equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
    
    public static EN50523DeviceState fromInt(int v) {
        for (EN50523DeviceState c: EN50523DeviceState.values()) {
            if (c.getStateInt() == v) {
                return c;
            }
        }
        throw new IllegalArgumentException(v + "");
    }

    
	public int getStateInt() {
		return stateInt;
	}

	public String getDescriptionDE() {
		return descriptionDE;
	}
    
}
