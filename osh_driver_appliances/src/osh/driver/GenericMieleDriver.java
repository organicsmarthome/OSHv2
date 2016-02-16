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

package osh.driver;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import javax.xml.bind.JAXBException;

import osh.IRealTimeSubscriber;
import osh.configuration.OSHParameterCollection;
import osh.core.IOSH;
import osh.core.com.interfaces.IEventReceiver;
import osh.core.com.interfaces.IHasState;
import osh.core.exceptions.OSHException;
import osh.datatypes.Commodity;
import osh.datatypes.XMLSerialization;
import osh.datatypes.appliance.PowerProfileTick;
import osh.datatypes.en50523.EN50523OIDExecutionOfACommandCommands;
import osh.datatypes.registry.EventExchange;
import osh.datatypes.registry.StateChangedExchange;
import osh.datatypes.registry.commands.StartDeviceRequest;
import osh.datatypes.registry.commands.StopDeviceRequest;
import osh.datatypes.registry.driver.details.appliance.GenericApplianceDriverDetails;
import osh.datatypes.registry.driver.details.appliance.GenericApplianceProgramDriverDetails;
import osh.datatypes.registry.driver.details.appliance.miele.MieleApplianceDriverDetails;
import osh.datatypes.registry.driver.details.energy.ElectricPowerDriverDetails;
import osh.hal.HALDeviceDriver;
import osh.hal.exchange.HALControllerExchange;
import osh.hal.exchange.MieleApplianceControllerExchange;
import osh.hal.exchange.MieleApplianceObserverExchange;
import osh.simulation.virtualdevicesdata.DeviceProfile;
import osh.simulation.virtualdevicesdata.ProfileTick;

/**
 * 
 * @author Florian Allerding, Kaibin Bao, Till Schuberth, Ingo Mauser
 *
 */
public class GenericMieleDriver extends HALDeviceDriver implements IEventReceiver, IHasState {
	
	// uuid of this appliance as posted by the bus driver
	private UUID applianceBusDriverUUID;

	// driverData
	private DeviceProfile deviceProfile;
	private HashMap<Commodity, ArrayList<PowerProfileTick>> currentLoadProfiles;
	private long programStartedTime = -1;
	
	// pending command
	private EN50523OIDExecutionOfACommandCommands pendingCommand = null;
	
	//successive incomplete errors count
	private int incompletedata = 0;
	
	// stored data from bus drivers
	private GenericApplianceDriverDetails currentAppDetails;
	private GenericApplianceProgramDriverDetails appProgramDetails;
	private MieleApplianceDriverDetails mieleApplianceDriverDetails;
	private ElectricPowerDriverDetails currentElectricPowerDriverDetails;

	
	/**
	 * CONSTRUCTOR
	 */
	public GenericMieleDriver(
			IOSH controllerbox, 
			UUID deviceID,
			OSHParameterCollection driverConfig) throws OSHException {
		super(controllerbox, deviceID, driverConfig);
		
		String cfgApplianceUUID = driverConfig.getParameter("applianceuuid");
		if( cfgApplianceUUID == null ) {
			throw new OSHException("Need config parameter applianceuuid");
		}
		this.applianceBusDriverUUID = UUID.fromString(cfgApplianceUUID);
	}
	
	
	@Override
	public void onSystemIsUp() throws OSHException {
		super.onSystemIsUp();
		
		setDataSourcesUsed( this.getMeterUuids() );
		setDataSourcesConfigured(Collections.singleton(applianceBusDriverUUID));

		// at the moment we have only one Power Profile
		this.loadDeviceProfiles();
		this.currentLoadProfiles = this.generatePowerProfiles();

		// register for changes of different details...
		getDriverRegistry().registerStateChangeListener(GenericApplianceDriverDetails.class, this);
		getDriverRegistry().registerStateChangeListener(GenericApplianceProgramDriverDetails.class, this);
		getDriverRegistry().registerStateChangeListener(ElectricPowerDriverDetails.class, this);
		
		getTimer().registerComponent(
				new IRealTimeSubscriber() {
					@Override
					public Object getSyncObject() {
						return GenericMieleDriver.this;
					}
					
					@Override
					public void onNextTimePeriod() throws OSHException {
						synchronized (this) {
							if( pendingCommand == EN50523OIDExecutionOfACommandCommands.START ) {
								StartDeviceRequest req = new StartDeviceRequest(
										getDeviceID(), 
										applianceBusDriverUUID, 
										getTimer().getUnixTime());
								getDriverRegistry().sendCommand(StartDeviceRequest.class, req);
							}
						}
					}
				}, 
			1);
	}


	private ArrayList<PowerProfileTick> shrinkPowerProfile(
			Commodity commodity,
			List<PowerProfileTick> powerProfile, 
			int programDuration){
		ArrayList<PowerProfileTick> _tmpList = new ArrayList<PowerProfileTick>();

		//if it's greater => shrink it!
		if (powerProfile.size() >= programDuration) {
			for( int i = 0; i < programDuration; i++){
				_tmpList.add(powerProfile.get(i));
			}
		}
		else {
			_tmpList.addAll(powerProfile);
			//expand it
			for (int i = 0; i < (programDuration-powerProfile.size()); i++){
				_tmpList.add(powerProfile.get(powerProfile.size()-1));
			}
		}
		
		return _tmpList;
	}
	
	
	private HashMap<Commodity, ArrayList<PowerProfileTick>> generatePowerProfiles() {
		
		HashMap<Commodity, ArrayList<PowerProfileTick>> profiles = new HashMap<>();
		
		int count = 0;
		
		// iterate time ticks
		for ( ProfileTick profileTick : deviceProfile.getProfileTicks().getProfileTick() ) {
			
			// iterate commodities
			for (int i = 0; i < profileTick.getLoad().size(); i++) {
				
				Commodity currentCommodity = Commodity.fromString(profileTick.getLoad().get(i).getCommodity());
				
				ArrayList<PowerProfileTick> _pwrProfileList = profiles.get(currentCommodity);
				
				if ( _pwrProfileList == null ) {
					_pwrProfileList = new ArrayList<PowerProfileTick>();
					profiles.put(currentCommodity, _pwrProfileList);
				}
				
				PowerProfileTick _pwrPro = new PowerProfileTick();
				_pwrPro.commodity = currentCommodity;
				_pwrPro.timeTick = count;
				_pwrPro.load = profileTick.getLoad().get(i).getValue();
				_pwrProfileList.add(_pwrPro);
			}
			
			++count;
		}
		
		return profiles;
	}
	
	private void loadDeviceProfiles() throws OSHException{
		String profileSourceName = getDriverConfig().getParameter("profilesource");
		//load profiles
		try {
			this.deviceProfile = (DeviceProfile)XMLSerialization.file2Unmarshal(profileSourceName, DeviceProfile.class);
		} 
		catch (FileNotFoundException e) {
			throw new OSHException(e);
		} 
		catch (JAXBException e) {
			throw new OSHException(e);
		}
	}
	
	
	@Override
	protected void onControllerRequest(HALControllerExchange controllerRequest) {

		try {
			MieleApplianceControllerExchange controllerExchange = (MieleApplianceControllerExchange) controllerRequest;

			if ( controllerExchange.getApplianceCommand() == EN50523OIDExecutionOfACommandCommands.START ) {
				StartDeviceRequest req = new StartDeviceRequest(getDeviceID(), applianceBusDriverUUID, controllerRequest.getTimestamp());
				getDriverRegistry().sendCommand(StartDeviceRequest.class, req);
				this.pendingCommand = EN50523OIDExecutionOfACommandCommands.START;
			}
			if ( controllerExchange.getApplianceCommand() == EN50523OIDExecutionOfACommandCommands.STOP ) {
				StopDeviceRequest req = new StopDeviceRequest(getDeviceID(), applianceBusDriverUUID, controllerRequest.getTimestamp());
				getDriverRegistry().sendCommand(StopDeviceRequest.class, req);
			}

		} 
		catch (Exception reqEx) {
			getGlobalLogger().logError("Request to Miele Gateway failed!", reqEx);
		}

	}


	@Override
	public void onQueueEventReceived(EventExchange event) throws OSHException {
		// our device? then: build observer exchange
		if ( event instanceof StateChangedExchange ) {
			/** update ox only if there is content */
			boolean updateOx = false;
			// consider only if the own state changed
			// (changed by meter device or BusDriver)
			UUID entity = ((StateChangedExchange) event).getStatefulentity();
			
			if ( applianceBusDriverUUID.equals(entity) ) {
				// get appliance details from registry
				currentAppDetails = getDriverRegistry().getState(
						GenericApplianceDriverDetails.class, applianceBusDriverUUID);
				
				// get appliance program details from registry
				appProgramDetails  = getDriverRegistry().getState(
						GenericApplianceProgramDriverDetails.class, applianceBusDriverUUID);

				// get miele program details from registry
				mieleApplianceDriverDetails = getDriverRegistry().getState(
						MieleApplianceDriverDetails.class, applianceBusDriverUUID);

				updateOx = true;
			}

			if ( this.getMeterUuids().contains(entity) ) {
				// aggregate power data
				{
					ArrayList<ElectricPowerDriverDetails> pdList = new ArrayList<ElectricPowerDriverDetails>();
					
					for ( UUID sourceUUID : this.getMeterUuids() ) {
						ElectricPowerDriverDetails p = getDriverRegistry().getState(
								ElectricPowerDriverDetails.class, sourceUUID);
						if ( p == null )  {
							// unable to fetch state
							break;
						}
						
						pdList.add(p);
					}
					
					if ( pdList.size() == this.getMeterUuids().size() ) {
						currentElectricPowerDriverDetails = ElectricPowerDriverDetails.aggregatePowerDetails(getUUID(), pdList);
						getDriverRegistry().setState(ElectricPowerDriverDetails.class, this, currentElectricPowerDriverDetails);
					} 
					else {
						currentElectricPowerDriverDetails = null;
					}
				}
				
				updateOx = true;
			}

			// generate ox object
			if( updateOx ) {
				MieleApplianceObserverExchange _ox = new MieleApplianceObserverExchange(
						getDeviceID(), getTimer().getUnixTime());
	
				// check for incomplete data
				if (currentAppDetails == null) {
					if( incompletedata == 0 )
						getGlobalLogger().logWarning("appDetails not available. Wait for data... UUID: " + getUUID());
					incompletedata++;
					return;
				}
				if (appProgramDetails == null) {
					if( incompletedata == 0 )
						getGlobalLogger().logWarning("appProgramDetails not available. Wait for data... UUID: " + getUUID());
					incompletedata++;
					return;
				}
				
				if (mieleApplianceDriverDetails == null) {
					if( incompletedata == 0 )
						getGlobalLogger().logWarning("mieleApplianceDriverDetails not available. Wait for data... UUID: " + getUUID());
					incompletedata++;
					return;
				}

				
				_ox.setEn50523DeviceState(currentAppDetails.getState());			
				_ox.setProgramName(appProgramDetails.getProgramName());
				_ox.setPhaseName(appProgramDetails.getPhaseName());
				//don't get Power Profile from Program Details, use stuff from file.
				
				// calculate profile
				if( currentAppDetails.getState() != null && mieleApplianceDriverDetails != null) {
					switch (currentAppDetails.getState()) {
					case PROGRAMMEDWAITINGTOSTART:
					case PROGRAMMED: {
						long maxProgramDuration = mieleApplianceDriverDetails.getExpectedProgramDuration();
						
						// Miele Gateway needs some time before it delivers the correct information about program duration
						if( maxProgramDuration <= 0 )
							return;
						
 						HashMap<Commodity, ArrayList<PowerProfileTick>> expectedLoadProfiles = new HashMap<>();
						
						for ( Entry<Commodity, ArrayList<PowerProfileTick>> e : currentLoadProfiles.entrySet() ) {
							ArrayList<PowerProfileTick> expectedPowerProfile = shrinkPowerProfile(e.getKey(), e.getValue(), (int)maxProgramDuration);
							expectedLoadProfiles.put(e.getKey(), expectedPowerProfile);
						}
						
						_ox.setExpectedLoadProfiles(expectedLoadProfiles);
						_ox.setDeviceStartTime(mieleApplianceDriverDetails.getStartTime());
						
						programStartedTime = -1;
						
						} break;
					case RUNNING: {
						synchronized (this) { // reset pending command
							if ( pendingCommand == EN50523OIDExecutionOfACommandCommands.START ) {
								pendingCommand = null;
							}
						}
						if( programStartedTime == -1 )
							programStartedTime = getTimer().getUnixTime();
						
						long remainingProgramDuration;
						if (isControllable()) {
							remainingProgramDuration = mieleApplianceDriverDetails.getProgramRemainingTime();
						}
						else {
							remainingProgramDuration = currentLoadProfiles.get(Commodity.ACTIVEPOWER).size();
						}
							
						long finishedProgramDuration = getTimer().getUnixTime() - programStartedTime;
						
						HashMap<Commodity, ArrayList<PowerProfileTick>> expectedLoadProfiles = new HashMap<>();
						
						if( remainingProgramDuration > 0 ) { // only makes sense if gateway doen't provide this information
							for ( Entry<Commodity, ArrayList<PowerProfileTick>> e : currentLoadProfiles.entrySet() ) {
								ArrayList<PowerProfileTick> expectedPowerProfile = shrinkPowerProfile(e.getKey(), e.getValue(), (int) (remainingProgramDuration+finishedProgramDuration));
								expectedLoadProfiles.put(e.getKey(), expectedPowerProfile);
							}
						}
						
						_ox.setExpectedLoadProfiles(expectedLoadProfiles);
						_ox.setDeviceStartTime(mieleApplianceDriverDetails.getStartTime());
						
						} break;
					default: {
						programStartedTime = -1;
						} break;
					}
				}
				
				// meta details
				_ox.setName(getName());
				_ox.setLocation(getLocation());
				_ox.setDeviceType(getDeviceType());
				_ox.setDeviceClass(getDeviceClassification());
				_ox.setConfigured(true);
				
				// optional data
				if ( currentElectricPowerDriverDetails != null ) {
					_ox.setActivePower((int) Math.round(currentElectricPowerDriverDetails.getActivePower()));
					_ox.setReactivePower((int) Math.round(currentElectricPowerDriverDetails.getReactivePower()));
				}

				//all data available -> reset error counter
				if ( incompletedata > 0 ) {
					getGlobalLogger().logWarning("data source(s) for device: " + getDeviceID() + " are available again after " + incompletedata + " missing");
				}
				incompletedata = 0;
				
				this.notifyObserver(_ox);
			} /* if updateOx */
			
		} /* if( event instanceof StateChangedExchange ) */
		
	}

	@Override
	public UUID getUUID() {
		return getDeviceID();
	}
	
}
