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

package osh.busdriver.mielegateway.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.persistence.oxm.annotations.XmlPath;

@XmlType
@XmlAccessorType( XmlAccessType.PUBLIC_MEMBER )
public class MieleDuration {
	/**
	 * Duration in minutes
	 */
	private int duration;
	
	@XmlPath("text()")
	public void setDuration(String mieleTimeString) {
		this.duration = parseString(mieleTimeString);
	}
	
	/**
	 * Returns MieleTime interpreted as duration in minutes
	 * 
	 * @return
	 */
	public int duration() {
		return duration;
	}
	
	public int hour() {
		return duration / 60;
	}
	
	public int minute() {
		return duration % 60;
	}

	static private int parseString(String mieleTimeString) {
		// strip "h" suffix and spaces at end
		while ( mieleTimeString.endsWith("h") || mieleTimeString.endsWith(" ") ) {
			// strip one character
			mieleTimeString = mieleTimeString.substring(0, mieleTimeString.length()-1);
		}

		String[] timeParts = mieleTimeString.split(":");
		if ( timeParts.length == 2 ) {
			return Integer.valueOf(timeParts[0]) * 60 + Integer.valueOf(timeParts[1]);
		} else {
			return -1;
		}
	}
	
	@Override
	public String toString() {
		if ( duration >= 0 )
			return Integer.toString(duration) + "m";
		else
			return "invalid duration";
	}
}
