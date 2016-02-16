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

import java.util.List;
import java.util.UUID;

import osh.core.IOSHOC;
import osh.core.com.interfaces.IEventReceiver;
import osh.core.com.interfaces.IHasState;
import osh.core.exceptions.OCUnitException;
import osh.core.exceptions.OSHException;
import osh.core.oc.LocalObserver;
import osh.datatypes.Commodity;
import osh.datatypes.appliance.LoadProfile;
import osh.datatypes.appliance.LoadProfileCompressionTypes;
import osh.datatypes.appliance.SparseLoadProfile;
import osh.datatypes.dof.DofStatesStateExchange;
import osh.datatypes.ea.Schedule;
import osh.datatypes.en50523.EN50523DeviceState;
import osh.datatypes.hal.interfaces.IHALDeviceMetaDetails;
import osh.datatypes.hal.interfaces.appliance.IHALGenericApplianceDetails;
import osh.datatypes.hal.interfaces.appliance.IHALMieleApplianceProgramDetails;
import osh.datatypes.hal.interfaces.electricity.IHALElectricPowerDetails;
import osh.datatypes.mooex.IModelOfObservationExchange;
import osh.datatypes.mooex.ModelOfObservationType;
import osh.datatypes.registry.EventExchange;
import osh.datatypes.registry.StateChangedExchange;
import osh.datatypes.registry.globalobserver.CommodityPowerStateExchange;
import osh.datatypes.registry.globalobserver.PredictionChangedEvent;
import osh.datatypes.registry.globalobserver.PredictionExchange;
import osh.datatypes.registry.localobserver.IAction;
import osh.datatypes.registry.localobserver.LastActionExchange;
import osh.datatypes.registry.localobserver.MieleDofStateExchange;
import osh.datatypes.registry.observer.EAProblemPartExchange;
import osh.datatypes.registry.observer.StaticEAProblemPartExchange;
import osh.datatypes.registry.oc.details.DeviceMetaOCDetails;
import osh.hal.exchange.HALObserverExchange;
import osh.hal.exchange.MieleApplianceObserverExchange;
import osh.mgmt.localcontroller.ExpectedStartTimeChangedExchange;
import osh.mgmt.localobserver.eapart.MieleEAPart;
import osh.mgmt.localobserver.miele.MieleAction;


/**
 * @author Florian Allerding, Ingo Mauser
 */
public class MieleApplianceLocalObserver extends LocalObserver implements IHasState, IEventReceiver {

	/**
	 * SparseLoadProfile containing different profile with different commodities<br>
	 * IMPORATANT: RELATIVE TIMES!
	 */
	private LoadProfile currentProfile;

	// TODO: make individual for devices... (parameter)
//	private int maxPowerDiff = 20;
	LoadProfileCompressionTypes compressionType = LoadProfileCompressionTypes.DISCONTINUITIES;
//	LoadProfileCompressionTypes compressionType = LoadProfileCompressionTypes.TIMESLOTS;

//	private final int maxPowerDiff = 0;
//	private final int maxPowerDiff = 1;
//	private final int maxPowerDiff = 10;
	private final int maxPowerDiff = 50; //default
//	private final int maxPowerDiff = 100;
//	private final int maxPowerDiff = 500;
//	private final int maxPowerDiff = 1000;
	
	private final int timeSlotDuration = 1;
//	private final int timeSlotDuration = 15;
//	private final int timeSlotDuration = 60;
//	private final int timeSlotDuration = 300;
//	private final int timeSlotDuration = 900;
	
	
	private EN50523DeviceState currentState;
	
	// used for EA planning
	private long expectedStarttime = -1;
	
	/** Never change this by hand, use setDof() */
	private int firstDof = 0;
	/** Never change this by hand, use setDof() */
	private int secondDof = 0;
	/** Never change this by hand, use setDof() */
	private long latestStart = 0;
	
	/** latest start time set by device */
	private long deviceStartTime = -1;
	
	private int lastActivePowerLevel = 0;
	private int lastReactivePowerLevel = 0;

	private long profileStarted = -1;
	private long programmedAt = -1;
	
	private MieleAction predictedAction = null;
	
	
	/**
	 * CONSTRUCTOR
	 * @param controllerbox
	 */
	public MieleApplianceLocalObserver(IOSHOC controllerbox) {
		super(controllerbox);
	}
	
	
	@Override
	public void onSystemIsUp() throws OSHException {
		super.onSystemIsUp();
		
		getTimer().registerComponent(this, 1);
		
		this.getOCRegistry().registerStateChangeListener(DofStatesStateExchange.class, this);
		
		this.getOCRegistry().register(ExpectedStartTimeChangedExchange.class, this);
		this.getOCRegistry().register(PredictionChangedEvent.class, this);
		
		updateEAExchange();
	}

	
	@Override
	public IModelOfObservationExchange getObservedModelData(ModelOfObservationType type) {
		return null;
	}

	@Override
	public void onDeviceStateUpdate() throws OCUnitException {
		boolean programUpdated = false;
		HALObserverExchange _hx = (HALObserverExchange) getObserverDataObject();
		
		if (_hx instanceof MieleApplianceObserverExchange) {
			long currentDeviceStartTime = ((MieleApplianceObserverExchange) _hx).getDeviceStartTime(); 
			if( Math.abs(currentDeviceStartTime - deviceStartTime) >= 300 /* 5 Minutes */ )
				programUpdated = true;
			deviceStartTime = currentDeviceStartTime;
			
			//well, well...
		}
		else {
			throw new OCUnitException("Miele Device " + getDeviceType() + " received invalid OX!");
		}

		if (_hx instanceof IHALElectricPowerDetails) {
			IHALElectricPowerDetails idepd = (IHALElectricPowerDetails) _hx;
			
			int currentActivePower = idepd.getActivePower();
			int currentReactivePower = idepd.getReactivePower();
			
			if (Math.abs(currentActivePower - lastActivePowerLevel) > 0 
					|| Math.abs(currentReactivePower - lastReactivePowerLevel) > 0) {
				CommodityPowerStateExchange cpse = new CommodityPowerStateExchange(
						_hx.getDeviceID(), 
						_hx.getTimestamp(),
						getDeviceType());
				cpse.addPowerState(Commodity.ACTIVEPOWER, currentActivePower);
				cpse.addPowerState(Commodity.REACTIVEPOWER, currentReactivePower);
				this.getOCRegistry().setState(
						CommodityPowerStateExchange.class,
						this,
						cpse);
				
				lastActivePowerLevel = idepd.getActivePower();
				lastReactivePowerLevel = idepd.getReactivePower();
			}
		}

		if (_hx instanceof IHALGenericApplianceDetails
				&& _hx instanceof IHALMieleApplianceProgramDetails) {
			IHALGenericApplianceDetails hgad = ((IHALGenericApplianceDetails) _hx);
			IHALMieleApplianceProgramDetails hgapd = ((IHALMieleApplianceProgramDetails) _hx);
			
			EN50523DeviceState newstate = hgad.getEN50523DeviceState();

			if ( currentState != newstate || programUpdated ) {
				if (newstate != null) {
					// set the current state
					this.currentState = newstate;
					
					switch (newstate) {
					case PROGRAMMEDWAITINGTOSTART: {
						this.currentProfile = SparseLoadProfile
								.convertToSparseProfile(
										hgapd.getExpectedLoadProfiles(),
										compressionType,
										maxPowerDiff,
										timeSlotDuration);
						
						this.profileStarted = -1;
						this.programmedAt = _hx.getTimestamp();
						
						getGlobalLogger().logDebug(
								"Appliance " + _hx.getDeviceID().toString()
										+ " programmed at tick:"
										+ this.programmedAt);
					}
						break;
					case PROGRAMMED: {
						this.currentProfile = SparseLoadProfile
								.convertToSparseProfile(
										hgapd.getExpectedLoadProfiles(),
										compressionType,
										maxPowerDiff,
										timeSlotDuration);
						
						this.profileStarted = -1;
						this.programmedAt = _hx.getTimestamp();

						getGlobalLogger().logDebug(
								"Appliance " + _hx.getDeviceID().toString()
										+ " programmed at tick:"
										+ this.programmedAt);
					}
						break;
					case OFF: {
						getGlobalLogger().logDebug(
								"Appliance " + _hx.getDeviceID().toString()
										+ " set -off- at tick:"
										+ getTimer().getUnixTime());

						this.profileStarted = -1;
						this.currentProfile = new SparseLoadProfile();
					}
						break;
					case STANDBY: {
						getGlobalLogger().logDebug(
								"Appliance " + _hx.getDeviceID().toString()
										+ " set -on- at tick:"
										+ getTimer().getUnixTime());

						this.profileStarted = -1;
					}
						break;
					case RUNNING: {
						getGlobalLogger().logDebug(
								"Appliance " + _hx.getDeviceID().toString()
										+ " start running at tick:"
										+ getTimer().getUnixTime());

						this.currentProfile = SparseLoadProfile
								.convertToSparseProfile(
										hgapd.getExpectedLoadProfiles(),
										compressionType,
										maxPowerDiff,
										timeSlotDuration);

						if (profileStarted == -1)
							this.profileStarted = _hx.getTimestamp();
					}
						break;
					case ENDPROGRAMMED: {
						getGlobalLogger().logDebug(
								"Appliance " + _hx.getDeviceID().toString()
										+ " stop running at tick:"
										+ getTimer().getUnixTime());

						this.profileStarted = -1;
					}
						break;
					default:
						this.profileStarted = -1;
						break;
					}

					this.updateEAExchange();
					
				} 
				else { // newstate == null
					//TODO: Decide about state...
					// 1. based on consumption...
					// 2. based on EMP...
				}
				
			}
		}

		if (_hx instanceof IHALDeviceMetaDetails) {
			IHALDeviceMetaDetails ihdmd = (IHALDeviceMetaDetails) _hx;
			
			DeviceMetaOCDetails _devDetails = new DeviceMetaOCDetails(_hx.getDeviceID(), _hx.getTimestamp());
			_devDetails.setName(ihdmd.getName());
			_devDetails.setLocation(ihdmd.getLocation());
			_devDetails.setDeviceType(ihdmd.getDeviceType());
			_devDetails.setDeviceClassification(ihdmd.getDeviceClassification());
			_devDetails.setConfigured(ihdmd.isConfigured());

			this.getOCRegistry().setState(DeviceMetaOCDetails.class, this, _devDetails);
		}

	}

	private MieleEAPart handlePrediction() {
		// handle prediction
		if ( predictedAction != null 
				&& (currentState != EN50523DeviceState.RUNNING || currentState != EN50523DeviceState.PROGRAMMED) ) {
			return predictedAction.getEapart();
		}
		else {
			return null;
		}
	}

	@Override
	public void onNextTimePeriod() throws OSHException {
		super.onNextTimePeriod();
	}

	@Override
	public void onQueueEventReceived(EventExchange ex)
			throws OSHException {
		
		if (ex instanceof StateChangedExchange) {
			StateChangedExchange exsc = (StateChangedExchange) ex;
			
			// 1st DoF and 2nd DoF may be NULL (no DoF for device available yet and the change is because of another device)
			if (exsc.getType().equals(DofStatesStateExchange.class)) {
				DofStatesStateExchange dse = this.getOCRegistry().getState(
						DofStatesStateExchange.class, exsc.getStatefulentity());
				
				if (dse.getDevice1stDegreeOfFreedom().get(this.getUUID()) != null
						&& dse.getDevice2ndDegreeOfFreedom().get(this.getUUID()) != null) {
					this.setDof(
							dse.getDevice1stDegreeOfFreedom().get(this.getUUID()), 
							dse.getDevice2ndDegreeOfFreedom().get(this.getUUID()));
				}
			}
		}

		if (ex instanceof ExpectedStartTimeChangedExchange
				&& ex.getSender().equals(this.getDeviceID())) {
			ExpectedStartTimeChangedExchange exest = (ExpectedStartTimeChangedExchange) ex;

			this.expectedStarttime = exest.getExpectedStartTime();

			updateDofExchange();
		}

		if (ex instanceof PredictionChangedEvent ) {
			PredictionExchange predictionEx = getOCRegistry().getState(PredictionExchange.class, getDeviceID());
			
			predictedAction = null;
			
			if ( predictionEx != null ) {
				List<IAction> actionlist = predictionEx.getPredictedActions();
				for ( IAction action : actionlist ) {
					if ( action instanceof MieleAction ) {
						predictedAction = (MieleAction) action;
						EAProblemPartExchange<?> eapart = handlePrediction();
						
						if ( eapart != null ) {
							updateEAExchange(eapart);
						}
							
						break;
					}
				}
			}
		}
	}


	public void setDof(Integer firstDof, Integer secondDof) {

		boolean dofChanged = false;

		if ((firstDof != null && firstDof < 0)
				|| (secondDof != null && secondDof < 0)) {
			throw new IllegalArgumentException("firstDof or secondDof < 0");
		}

		if (firstDof == null && secondDof == null) {
			throw new IllegalArgumentException("firstDof and secondDof == null");
		}

		if (firstDof != null && this.firstDof != firstDof) {
			this.firstDof = firstDof;
			dofChanged = true;
		}

		if (secondDof != null && this.secondDof != secondDof) {
			this.secondDof = secondDof;
			dofChanged = true;
		}

		if (dofChanged && (currentState != EN50523DeviceState.OFF)) { 
			// don't reschedule if device is OFF
			updateEAExchange();
		}

	}


	public void updateDofExchange() {
		// state for REST and logging
		this.getOCRegistry().setState(
						MieleDofStateExchange.class,
						this,
						new MieleDofStateExchange(
								getDeviceID(),
								getTimer().getUnixTime(), 
								this.firstDof,
								Math.min(getTimer().getUnixTime(), this.latestStart), 
								this.latestStart,
								expectedStarttime));
	}
	

	protected void updateEAExchange() {
		EAProblemPartExchange<?> eapart = null;

		long now = getTimer().getUnixTime();

		if (currentState == EN50523DeviceState.PROGRAMMED 
				|| currentState == EN50523DeviceState.PROGRAMMEDWAITINGTOSTART	) {
			assert programmedAt >= 0;
			
//			if( deviceStartTime != -1 )
//				latestStart = deviceStartTime;
//			else
				latestStart = programmedAt + firstDof;
			
			eapart = new MieleEAPart(
					getDeviceID(), 
					now, // now
					now, //earliest starttime
					latestStart,
					currentProfile,
					false,
					latestStart + currentProfile.getEndingTimeOfProfile(),
					getDeviceType());
			
			IAction mieleAction = new MieleAction(
					this.getDeviceID(), 
					programmedAt, 
					(MieleEAPart) eapart);

			this.getOCRegistry().setState(
					LastActionExchange.class,
					this,
					new LastActionExchange(
							getUUID(),
							getTimer().getUnixTime(), 
							mieleAction));
		} 
		else {
			if (profileStarted > 0) {
				eapart = new StaticEAProblemPartExchange(
						getDeviceID(), 
						now,
						true, // reschedule
						new Schedule(
								new SparseLoadProfile().merge(currentProfile, profileStarted), 0.0),
						"miele appl started @" + profileStarted,
						getDeviceType());
			} 
			else {
				//FIXME: (read below)
				// for a real Smart Home we should reschedule, because the user
				// could have aborted an action.
				// for a simulation we don't need that, because nobody will (at
				// the moment) abort anything
				
				// IMA: StaticEAProblemPartExchange causes rescheduling


				if ( eapart == null ) {
					eapart = new StaticEAProblemPartExchange(
									getDeviceID(), 
									now,
									true, // reschedule
									new Schedule(new SparseLoadProfile(), 0.0),
									"miele appl not running",
									getDeviceType());
				}			
			}
		}
		updateEAExchange(eapart);
	}

	protected void updateEAExchange(EAProblemPartExchange<?> eapart) {
		this.getOCRegistry().setState(EAProblemPartExchange.class, this, eapart);

		updateDofExchange();
	}
	
	public EN50523DeviceState getCurrentState() {
		return currentState;
	}
	

	@Override
	public UUID getUUID() {
		return getDeviceID();
	}
	
	@Override
	public String toString() {
		return this.getClass().getCanonicalName() + " for " + getDeviceID()
				+ " (" + getDeviceType() + ")";
	}

}
