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

package osh.mgmt.localobserver.miele;

import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import osh.datatypes.registry.localobserver.IAction;
import osh.mgmt.localobserver.eapart.MieleEAPart;


/**
 * 
 * @author Florian Allerding, Kaibin Bao, Till Schuberth, Ingo Mauser
 *
 */
@XmlAccessorType( XmlAccessType.FIELD )
@XmlRootElement
public class MieleAction implements IAction {

	private UUID deviceID;
	private long programmedAt;
	private MieleEAPart eapart;

	
	/** for JAXB */
	@SuppressWarnings("unused")
	@Deprecated
	private MieleAction() {}

	/**
	 * CONSTRUCTOR
	 * @param deviceID
	 * @param programmedAt
	 * @param eapart
	 */
	public MieleAction(
			UUID deviceID, 
			long programmedAt, 
			MieleEAPart eapart) {
		this.deviceID = deviceID;
		this.programmedAt = programmedAt;
		this.eapart = eapart;
	}

	@Override
	public UUID getDeviceId() {
		return deviceID;
	}

	@Override
	public long getTimestamp() {
		return programmedAt;
	}

	public MieleEAPart getEapart() {
		return eapart;
	}
	
	@Override
	public boolean equals(IAction other) {
		if( !(other instanceof MieleAction) )
			return false;
		
		MieleAction otherMieleAction = (MieleAction) other;
		
		if( !(deviceID.equals(otherMieleAction.getDeviceId()) ) )
			return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		return deviceID.hashCode();
	}

	// TODO: keep for prediction
//	@Override
//	public IAction createAction(long newTimestamp) {
//		MieleEAPart newEAPart = new MieleEAPart(deviceID,newTimestamp,newTimestamp,newTimestamp+eapart.getOriginalDof(),eapart.getProfile(), true);
//
//		return new MieleAction(deviceID, newTimestamp, newEAPart);
//	}

}
