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

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.persistence.oxm.annotations.XmlPath;

import osh.datatypes.en50523.EN50523DeviceState;


/**
 * A Miele device on the XML homebus
 * 
 * @author Kaibin Bao
 *
 */
@XmlType
public class MieleDeviceHomeBusData {
	@XmlPath("class/text()")
	private int deviceClass;

	@XmlPath("UID/text()")
	private int uid;
	
	@XmlPath("type/text()")
	private String type;
	
	@XmlPath("name/text()")
	private String name;
	
	@XmlPath("state/text()")
	private EN50523DeviceState state;
	
	@XmlPath("additionalName/text()")
	private String additionalName;

	@XmlPath("room/text()")
	private String roomName;
	
	@XmlPath("room[@id]")
	private String roomId;

	@XmlPath("room[@level]")
	private String roomLevel;

	@XmlPath("information/key[@name='State']/@value")
	private String stateName;
	
	@XmlPath("information/key[@name='Phase']/@value")
	private String phaseName;
	
	@XmlPath("information/key[@name='Duration']/@value")
	private MieleDuration duration;

	@XmlPath("information/key[@name='Start Time']/@value")
	private MieleDuration startTime;
	
	@XmlPath("information/key[@name='Remaining Time']/@value")
	private MieleDuration remainingTime;
	
	@XmlPath("actions/action[@name='Details']/@URL")
	private String detailsUrl;

	@XmlTransient
	private MieleApplianceRawData deviceDetails;
	
	/* GETTERS */
	
	public int getDeviceClass() {
		return deviceClass;
	}

	public int getUid() {
		return uid;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public EN50523DeviceState getState() {
		return state;
	}

	public String getAdditionalName() {
		return additionalName;
	}
	
	public String getRoomName() {
		return roomName;
	}

	public String getRoomId() {
		return roomId;
	}

	public String getRoomLevel() {
		return roomLevel;
	}

	public String getStateName() {
		return stateName;
	}

	public String getPhaseName() {
		return phaseName;
	}

	public MieleDuration getDuration() {
		return duration;
	}
	
	public MieleDuration getStartTime() {
		return startTime;
	}

	public MieleDuration getRemainingTime() {
		return remainingTime;
	}

	public String getDetailsUrl() {
		return detailsUrl;
	}
	
	public MieleApplianceRawData getDeviceDetails() {
		return deviceDetails;
	}
	
	/* SETTERS */
	
	public void setDeviceDetails(MieleApplianceRawData deviceDetails) {
		this.deviceDetails = deviceDetails;
	}
	
	@Override
	public String toString() {
		return String.format("miele device %x, class %x, state %s", uid, deviceClass, state.toString());
	}
}
