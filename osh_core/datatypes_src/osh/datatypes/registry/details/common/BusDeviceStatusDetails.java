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

package osh.datatypes.registry.details.common;

import java.util.UUID;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

import osh.datatypes.registry.StateExchange;


/**
 * 
 * @author Kaibin Bao, Ingo Mauser
 *
 */
@XmlAccessorType( XmlAccessType.PUBLIC_MEMBER )
@XmlType
public class BusDeviceStatusDetails extends StateExchange {

	@XmlType
	public enum ConnectionStatus {
		@XmlEnumValue("ATTACHED")
		ATTACHED,
		@XmlEnumValue("DETACHED")
		DETACHED,
		@XmlEnumValue("ERROR")
		ERROR
	}
	

	protected ConnectionStatus state;
	
	/** for JAXB */
	@SuppressWarnings("unused")
	@Deprecated
	private BusDeviceStatusDetails() {
		this(null, 0);
	}

	public BusDeviceStatusDetails(UUID sender, long timestamp) {
		super(sender, timestamp);
	}

	public ConnectionStatus getState() {
		return state;
	}

	public void setState(ConnectionStatus state) {
		this.state = state;
	}
	
	@Override
	public String toString() {
		return "BusDeviceStatus: " + state.name();
	}
}
