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

package osh.hal.exchange;

import java.util.UUID;

import osh.datatypes.appliance.LoadProfile;
import osh.datatypes.appliance.LoadProfileCompressionTypes;
import osh.datatypes.appliance.SparseLoadProfile;
import osh.datatypes.appliance.generic.ApplianceConfigurationProfile;
import osh.datatypes.en50523.EN50523DeviceState;
import osh.datatypes.hal.interfaces.appliance.IHALGenericApplianceDetails;
import osh.datatypes.hal.interfaces.appliance.IHALGenericApplianceIsCurrentlyControllable;
import osh.datatypes.hal.interfaces.appliance.IHALGenericApplianceProgramDetails;
import osh.datatypes.hal.interfaces.electricity.IHALElectricPowerDetails;
import osh.datatypes.hal.interfaces.gas.IHALGasPowerDetails;

/**
 * 
 * @author Ingo Mauser
 *
 */
public class GenericApplianceObserverExchange 
				extends HALDeviceObserverExchange
				implements  IHALElectricPowerDetails,
							IHALGasPowerDetails,
							IHALGenericApplianceDetails,
							IHALGenericApplianceProgramDetails,
							IHALGenericApplianceIsCurrentlyControllable {

	// ### IHALElectricPowerDetails ###
	private int activePower;
	private int reactivePower;
	
	// ### IHALGasPowerDetails ###
	private int gasPower;
	
	// ### IHALGenericApplianceDetails ###
	private EN50523DeviceState en50523DeviceState;
	
	// ### IHALGenericApplianceProgramDetails ###
	private ApplianceConfigurationProfile applianceConfigurationProfile;
	private UUID acpID;
	private int currentProfileID;
	private int currentSegment;
	private boolean currentlyRunningPhase;
	private int currentTickCounter;
	
	// ### IHALGenericApplianceIsCurrentlyControllable ###
	private boolean currentlyControllable;
	
	
	/**
	 * CONSTRUCTOR
	 * @param deviceID
	 * @param timestamp
	 * @param device1stDoF
	 * @param originalLoadProfile
	 */
	public GenericApplianceObserverExchange(
			UUID deviceID, 
			Long timestamp) {
		super(deviceID, timestamp);
	}


	public int getActivePower() {
		return activePower;
	}

	public void setActivePower(int activePower) {
		this.activePower = activePower;
	}


	public int getReactivePower() {
		return reactivePower;
	}

	public void setReactivePower(int reactivePower) {
		this.reactivePower = reactivePower;
	}

	public int getGasPower() {
		return gasPower;
	}

	public void setGasPower(int gasPower) {
		this.gasPower = gasPower;
	}

	@Override
	public EN50523DeviceState getEN50523DeviceState() {
		return en50523DeviceState;
	}
	
	public EN50523DeviceState getEn50523DeviceState() {
		return en50523DeviceState;
	}

	public void setEn50523DeviceState(EN50523DeviceState en50523DeviceState) {
		this.en50523DeviceState = en50523DeviceState;
	}

	@Override
	public ApplianceConfigurationProfile getApplianceConfigurationProfile() {
		return applianceConfigurationProfile;
	}

	public void setApplianceConfigurationProfile(
			ApplianceConfigurationProfile applianceConfigurationProfile,
			LoadProfileCompressionTypes profileType,
			final int powerEps,
			final int timeSlotDuration) {
		// clone and compress
		
		LoadProfile[][][] dynamicLoadProfiles = applianceConfigurationProfile.getDynamicLoadProfiles();
		LoadProfile[][][] compressedDynamicLoadProfiles = 
				SparseLoadProfile.getCompressedProfile(
						profileType, 
						(SparseLoadProfile[][][]) dynamicLoadProfiles, 
						powerEps, 
						timeSlotDuration);
		
		LoadProfile[] finishedProfile = applianceConfigurationProfile.getFinishedProfile();
		LoadProfile[] compressedFinishedProfile = 
				SparseLoadProfile.getCompressedProfile(
						profileType, 
						(SparseLoadProfile[]) finishedProfile, 
						powerEps,
						timeSlotDuration);
		
		ApplianceConfigurationProfile acp = new ApplianceConfigurationProfile(
				applianceConfigurationProfile.getAcpID(), 
				compressedDynamicLoadProfiles, 
				compressedFinishedProfile);
		
		this.applianceConfigurationProfile = acp;
	}


	@Override
	public int getCurrentProfileID() {
		return currentProfileID;
	}

	public void setCurrentProfileID(int currentProfileID) {
		this.currentProfileID = currentProfileID;
	}

	@Override
	public int getCurrentSegment() {
		return currentSegment;
	}

	public void setCurrentSegment(int currentSegment) {
		this.currentSegment = currentSegment;
	}

	@Override
	public boolean isCurrentlyRunningPhase() {
		return currentlyRunningPhase;
	}

	public void setCurrentlyRunningPhase(boolean currentlyRunningPhase) {
		this.currentlyRunningPhase = currentlyRunningPhase;
	}

	@Override
	public int getCurrentTickCounter() {
		return currentTickCounter;
	}

	public void setCurrentTickCounter(int currentTickCounter) {
		this.currentTickCounter = currentTickCounter;
	}

	@Override
	public UUID getAcpID() {
		return acpID;
	}
	
	public void setAcpID(UUID acpID) {
		this.acpID = acpID;
	}

	@Override
	public boolean isCurrentlyControllable() {
		return currentlyControllable;
	}

	public void setCurrentlyControllable(boolean currentlyControllable) {
		this.currentlyControllable = currentlyControllable;
	}
	
}
