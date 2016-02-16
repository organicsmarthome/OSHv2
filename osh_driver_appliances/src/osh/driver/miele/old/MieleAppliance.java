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

package osh.driver.miele.old;

import java.util.LinkedList;
import java.util.UUID;


/**
 * 
 * @author Kaibin Bao
 *
 */
public class MieleAppliance {

	// timeStamp for Device, for every cycle
	private long timestamp;
	// unique cycle ID, is initialized once every program selection.
	private long cycleID;
	
	//homebus URL aus Application GUI
	private String homebusURL;
		
	// class aus Homebus XML
	private int classe;
	// UID aus Homebus XML
	private int uid;
	// type aus Homebus XML
	private String type;
	// name aus Homebus XML
	private String name;
	// stateID aus Homebus XML
	private int stateID;
	// addName aus Homebus XML
	private String addName;
	// roomID aus Homebus XML
	private int roomID;
	// roomLevel aus Homebus XML
	private String roomLevel;
	// roomName aus Homebus XML
	private String roomName;
	// detailURL aus Homebus XML (f�r add. Info)
	private String detailURL;
	// detailName aus Homebus XML (actions/action/name)
	private String detailName;
	
	private UUID applianceId;
			
	// Liste mit DeviceDetails Objekten jeweils mit Sring .name und String .value aus Details-XML
	private LinkedList<MieleApplianceDetails> addInfoList; 
	// Liste mit DeviceDetails Objekten f�r Action wie Fernstart jeweils mit Sring .name und String .value(als URL) aus Details-XML
	private LinkedList<MieleApplianceDetails> addActionList; 

	// PowerURL
	private String powerURL;
	// ActivePower-double
	private double activePower;
	// ReactivePower-double
	private double reactivePower;
	// pID represents the power plug in iDevice
	private int pId;
	
		
	public String getPowerURL() {
		return powerURL;
	}

	public void setPowerURL(String powerURL) {
		this.powerURL = powerURL;
	}
	
	/** returns the current Active Power information */
	public double getActivePower() {
		return activePower;
	}
	
	public void setActivePower(double activePower) {
		this.activePower = activePower;
	}
	
	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public int getpId() {
		return pId;
	}

	public void setpId(int pId) {
		this.pId = pId;
	}

	public String getDetailName() {
		return detailName;
	}

	public void setDetailName(String detailName) {
		this.detailName = detailName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getClasse() {
		return classe;
	}

	public void setClasse(int classe) {
		this.classe = classe;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public int getStateID() {
		return stateID;
	}

	public void setStateID(int stateID) {
		this.stateID = stateID;
	}

	public String getAddName() {
		return addName;
	}

	public void setAddName(String addName) {
		this.addName = addName;
	}

	public int getRoomID() {
		return roomID;
	}

	public void setRoomID(int roomID) {
		this.roomID = roomID;
	}

	public String getRoomName() {
		return roomName;
	}
	
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	
	public String getRoomLevel() {
		return roomLevel;
	}

	public void setRoomLevel(String roomLevel) {
		this.roomLevel = roomLevel;
	}

	public String getDetailURL() {
		return detailURL;
	}

	public void setDetailURL(String detailURL) {
		this.detailURL = detailURL;
	}

	public LinkedList<MieleApplianceDetails> getAddInfoList() {
		return addInfoList;
	}

	public void setAddInfoList(LinkedList<MieleApplianceDetails> addInfoList) {
		this.addInfoList = addInfoList;
	}

	public long getCycleID() {
		return cycleID;
	}

	public void setCycleID(long cycleID) {
		this.cycleID = cycleID;
	}
	
	public String getHomebusURL() {
		return homebusURL;
	}

	public void setHomebusURL(String homebusURL) {
		this.homebusURL = homebusURL;
	}
	
	public LinkedList<MieleApplianceDetails> getAddActionList() {
		return addActionList;
	}

	public void setAddActionList(LinkedList<MieleApplianceDetails> addActionList) {
		this.addActionList = addActionList;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Device ["+"\n" + "  addActionList="
				+ addActionList + "\n" 
				+ "  addInfoList=" + addInfoList + "\n" 
				+ " addName=" + addName + "\n" 
				+ " classe=" + classe + "\n" 
				+ " cycleID=" + cycleID	+ "\n" 
				+ " detailName=" + detailName + "\n" 
				+ " detailURL=" + detailURL	+ "\n" 
				+ " homebusURL=" + homebusURL + "\n" 
				+ " name=" + name + "\n" 
				+ " pId=" + pId + "\n" 
				+ " activePower=" + activePower + "\n" 
				+ " pId=" + pId + "\n" 
				+ " reactivePower=" + reactivePower + "\n" 
				+ " powerURL=" + powerURL + "\n" 
				+ " roomID=" + roomID + "\n" 
				+ " roomLevel=" + roomLevel + "\n" 
				+ " roomName=" + roomName + "\n" 
				+ " stateID=" + stateID + "\n" 
				+ " timestamp=" + timestamp	+ "\n" 
				+ " type=" + type + "\n" 
				+ " uid=" + uid + "]");
				return sb.toString();
	}

	/**
	 * @return the applianceId
	 */
	public UUID getApplianceId() {
		return applianceId;
	}

	/**
	 * @param applianceId the applianceId to set
	 */
	public void setApplianceId(UUID applianceId) {
		this.applianceId = applianceId;
	}
	
}

