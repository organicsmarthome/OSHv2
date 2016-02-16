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

package osh.datatypes.registry.localobserver;

import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import osh.datatypes.registry.StateExchange;


@XmlAccessorType( XmlAccessType.FIELD )
@XmlRootElement
@XmlType
public class LastActionExchange extends StateExchange {

	@XmlAnyElement
	private IAction lastAction;

	public LastActionExchange(UUID sender, long timestamp) {
		super(sender, timestamp);
	}

	/** for JAXB, do not use */
	@Deprecated
	private LastActionExchange() {
		super(null, 0);
	}
	
	public LastActionExchange(UUID sender, long timestamp, IAction lastAction) {
		super(sender, timestamp);
		this.lastAction = lastAction;
	}

	public IAction getLastAction() {
		return lastAction;
	}

	public void setLastAction(IAction lastAction) {
		this.lastAction = lastAction;
	}
	
}
