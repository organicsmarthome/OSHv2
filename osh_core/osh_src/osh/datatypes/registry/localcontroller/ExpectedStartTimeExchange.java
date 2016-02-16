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

package osh.datatypes.registry.localcontroller;

import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import osh.datatypes.registry.StateExchange;


@XmlAccessorType( XmlAccessType.PUBLIC_MEMBER )
@XmlRootElement
public class ExpectedStartTimeExchange extends StateExchange {
	private long expectedStartTime;

	/** for JAXB */
	@SuppressWarnings("unused")
	@Deprecated
	private ExpectedStartTimeExchange() { this(null, 0); }
	
	public ExpectedStartTimeExchange(UUID sender, long timestamp) {
		super(sender, timestamp);
	}
	
	public ExpectedStartTimeExchange(UUID sender, long timestamp,
			long startTime) {
		super(sender, timestamp);
		setExpectedStartTime(startTime);
	}

	public long getExpectedStartTime() {
		return expectedStartTime;
	}
	
	public void setExpectedStartTime(long expectedStartTime) {
		this.expectedStartTime = expectedStartTime;
	}

	@Override
	public String toString() {
		return "ExpectedStartTimeExchange [expectedStartTime="
				+ expectedStartTime + "]";
	}
}
