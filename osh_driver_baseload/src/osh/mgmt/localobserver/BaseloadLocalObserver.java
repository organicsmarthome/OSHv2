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
import osh.datatypes.appliance.LoadProfileCompressionTypes;
import osh.datatypes.appliance.SparseLoadProfile;
import osh.datatypes.mooex.IModelOfObservationExchange;
import osh.datatypes.mooex.ModelOfObservationType;
import osh.datatypes.registry.EventExchange;
import osh.datatypes.registry.globalobserver.CommodityPowerStateExchange;
import osh.datatypes.registry.observer.EAProblemPartExchange;
import osh.hal.exchange.BaseloadObserverExchange;
import osh.mgmt.localobserver.baseload.eapart.BaseloadEAPart;
import osh.utils.time.TimeConversion;


/**
 * 
 * @author Ingo Mauser
 *
 */
public class BaseloadLocalObserver extends LocalObserver implements IHasState, IEventReceiver {
	
	// Profile precision
	private final LoadProfileCompressionTypes compType = LoadProfileCompressionTypes.DISCONTINUITIES;
	
	private final int profileResolutionInSec = 900;	
	
	private SparseLoadProfile lastDayProfile;
	private LoadProfile predictedBaseloadProfile;
	private long timeFromMidnight;
	private int timeRangeCounter;
	
	
	/**
	 * CONSTRUCTOR
	 */
	public BaseloadLocalObserver(IOSHOC controllerbox) {
		super(controllerbox);
		
		lastDayProfile = new SparseLoadProfile();
		predictedBaseloadProfile = lastDayProfile;
	}
	

	//TODO: find a better method for the baseload-prediction
	private void monitorBaseloadProfile(int activeBaseload){
		
		if (this.timeFromMidnight == 0) {
			// a brand new day...let's make a new prediction
			
			//set lastDayProfile as prediction
			SparseLoadProfile lastDayProfile = 
					((SparseLoadProfile) this.lastDayProfile).getCompressedProfile(compType, 1, 0);
			this.predictedBaseloadProfile = lastDayProfile.merge(lastDayProfile, 86400);
			
			//create a new profile for the prediction
			this.lastDayProfile = new SparseLoadProfile();
			this.lastDayProfile.setLoad(
					Commodity.ACTIVEPOWER, 
					this.timeFromMidnight, 
					activeBaseload);
			this.timeRangeCounter = 1;
		}
//		else if (timeFromMidnight == 86400 / 2) {
//			// it's noon...let's make a new prediction
//			
//			// combine lastDayProfile and currentDayProfile
//			SparseLoadProfile currentDayProfile = ((SparseLoadProfile) this.lastDayProfile).getCompressedProfile(1);
//			SparseLoadProfile lastDayProfile = ((SparseLoadProfile) this.predictedBaseloadProfile).getCompressedProfile(1);
//			
//			SparseLoadProfile newPredictedLoadProfile = ...
//		}
		else {
			if (timeFromMidnight == 86400 - 1) {
				lastDayProfile.setLoad(
						Commodity.ACTIVEPOWER, 
						timeFromMidnight, 
						activeBaseload);
			}
		
			if (timeRangeCounter >= profileResolutionInSec) {
				lastDayProfile.setLoad(
						Commodity.ACTIVEPOWER, 
						timeFromMidnight, 
						activeBaseload);
				timeRangeCounter = 1;
			}
			else {
				timeRangeCounter++;
			}
		}
	}

	@Override
	public void onDeviceStateUpdate() {

		BaseloadObserverExchange _ox = (BaseloadObserverExchange) getObserverDataObject();

		CommodityPowerStateExchange cpse = new CommodityPowerStateExchange(
				getUUID(), 
				getTimer().getUnixTime(),
				DeviceTypes.OTHER);
		
		for (Commodity c : _ox.getCommodities()) {
			int power = _ox.getPower(c);
			cpse.addPowerState(c, power);
		}
		
		this.getOCRegistry().setState(
				CommodityPowerStateExchange.class,
				this,
				cpse);
		
		long lastTimeFromMidnight = this.timeFromMidnight;
		this.timeFromMidnight = TimeConversion.calculateTimeSpanFromMidnight(this.getTimer().getUnixTime());
		
		//monitor the baseload
		this.monitorBaseloadProfile(_ox.getPower(Commodity.ACTIVEPOWER));

		// don't invoke at first day (no prediction!!!)
		if (lastTimeFromMidnight > this.timeFromMidnight) {
			//a new day has begun...
			updateEAPart();
		}
	
	}

	
	private void updateEAPart() {
		BaseloadEAPart eaPart = new BaseloadEAPart(
				getDeviceID(), 
				getTimer().getUnixTime(), 
				new SparseLoadProfile().merge(
						predictedBaseloadProfile, 
						getTimer().getUnixTime()));
		this.getOCRegistry().setState(EAProblemPartExchange.class, this, eaPart);
	}


	@Override
	public void onQueueEventReceived(EventExchange event) throws OSHException {
		//NOTHING
	}
	
	@Override
	public IModelOfObservationExchange getObservedModelData(ModelOfObservationType type) {
		return null;
	}

	@Override
	public UUID getUUID() {
		return getDeviceID();
	}
	
}
