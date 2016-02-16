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

package osh.mgmt.localobserver;

import java.util.UUID;

import osh.configuration.system.DeviceTypes;
import osh.core.IOSHOC;
import osh.core.com.interfaces.IEventReceiver;
import osh.core.com.interfaces.IHasState;
import osh.core.exceptions.OSHException;
import osh.core.oc.LocalObserver;
import osh.datatypes.Commodity;
import osh.datatypes.appliance.LoadProfile;
import osh.datatypes.appliance.SparseLoadProfile;
import osh.datatypes.mooex.IModelOfObservationExchange;
import osh.datatypes.mooex.ModelOfObservationType;
import osh.datatypes.registry.EventExchange;
import osh.datatypes.registry.globalobserver.CommodityPowerStateExchange;
import osh.datatypes.registry.observer.EAProblemPartExchange;
import osh.datatypes.registry.oc.details.energy.ElectricCurrentOCDetails;
import osh.datatypes.registry.oc.details.energy.ElectricPowerOCDetails;
import osh.datatypes.registry.oc.details.energy.ElectricVoltageOCDetails;
import osh.hal.exchange.PvHALObserverExchange;
import osh.mgmt.localobserver.pv.eapart.PvEAPart;
import osh.utils.time.TimeConversion;


/**
 * 
 * @author Florian Allerding, Ingo Mauser
 *
 */
public class PvLocalObserver extends LocalObserver implements IHasState, IEventReceiver {

	private double lastActivePowerLevel = 0.0;
	private double lastReactivePowerLevel = 0.0;
	private SparseLoadProfile lastDayProfile;
	private LoadProfile predictedPVProfile;
	private int timeRangeCounter = 0;
	private long timeFromMidnight;
	
	// Configuration variables
	private static final int profileResolutionInSec = 900;

	
	/**
	 * CONSTRUCTOR
	 * @param controllerbox
	 */
	public PvLocalObserver(IOSHOC controllerbox) {
		super(controllerbox);
		lastDayProfile = new SparseLoadProfile();
		
		//start with an empty prediction
		predictedPVProfile = lastDayProfile;
	}

	@Override
	public void onDeviceStateUpdate() {

		PvHALObserverExchange _ox = (PvHALObserverExchange) getObserverDataObject();
		
		ElectricCurrentOCDetails _currentDetails = new ElectricCurrentOCDetails(_ox.getDeviceID(), _ox.getTimestamp());
		_currentDetails.setCurrent(_ox.getCurrent());
		
		ElectricPowerOCDetails _powDetails = new ElectricPowerOCDetails(_ox.getDeviceID(), _ox.getTimestamp());
		_powDetails.setActivePower(_ox.getActivePower());
		_powDetails.setReactivePower(_ox.getReactivePower());
		
		ElectricVoltageOCDetails _voltageDetails = new ElectricVoltageOCDetails(_ox.getDeviceID(), _ox.getTimestamp());
		_voltageDetails.setVoltage(_ox.getVoltage());
			
		if (_powDetails != null) {
			if (Math.abs(lastActivePowerLevel -_powDetails.getActivePower()) > 1) {
				this.lastActivePowerLevel = _powDetails.getActivePower();
				this.lastReactivePowerLevel = _powDetails.getReactivePower();

				CommodityPowerStateExchange cpse = new CommodityPowerStateExchange(
						getUUID(), 
						getTimer().getUnixTime(),
						DeviceTypes.PVSYSTEM);
				cpse.addPowerState(Commodity.ACTIVEPOWER, lastActivePowerLevel);
				cpse.addPowerState(Commodity.REACTIVEPOWER, lastReactivePowerLevel);
				
				this.getOCRegistry().setState(
						CommodityPowerStateExchange.class,
						this,
						cpse);
			}
		}
		
		//refresh time from Midnight
		long lastTimeFromMidnight = timeFromMidnight;
		timeFromMidnight = TimeConversion.calculateTimeSpanFromMidnight(this.getTimer().getUnixTime());
		
		//monitor the load profile
		runPvProfilePredictor(_powDetails);
		
		//refresh the EApart
		if (lastTimeFromMidnight > timeFromMidnight) {
			//a new day has begun...
			updateEAPart();
		}
		
	}

	
	private void updateEAPart() {
		PvEAPart eaPart = new PvEAPart(
				getDeviceID(), 
				getTimer().getUnixTime(), 
				new SparseLoadProfile().merge(
						predictedPVProfile, 
						getTimer().getUnixTime()));

		this.getOCRegistry().setState(EAProblemPartExchange.class, this, eaPart);
	}

	//TODO: find a better method for the pv-prediction
	private void runPvProfilePredictor(ElectricPowerOCDetails powerDetails){
		
		if (timeFromMidnight == 0) {
			//hooray a brand new day...let's make a new prediction
			
			//set lastDayProfile as prediction
			predictedPVProfile = lastDayProfile.clone();
			//create a new profile for the prediction
			lastDayProfile = new SparseLoadProfile();
		}
		else {
		
			if (timeRangeCounter >= profileResolutionInSec) {
				lastDayProfile.setLoad(Commodity.ACTIVEPOWER, timeFromMidnight, powerDetails.getActivePower());
				timeRangeCounter = 0;
			}
			else {
				++timeRangeCounter;
			}
		}
	}
	

	@Override
	public IModelOfObservationExchange getObservedModelData(ModelOfObservationType type) {
		return null;
	}
	
	@Override
	public UUID getUUID() {
		return getDeviceID();
	}

	@Override
	public void onQueueEventReceived(EventExchange event)
			throws OSHException {
		//NOTHING
	}

}
