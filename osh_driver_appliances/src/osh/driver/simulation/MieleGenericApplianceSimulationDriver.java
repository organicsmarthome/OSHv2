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

package osh.driver.simulation;

import java.util.List;
import java.util.UUID;

import osh.simulation.AbstractApplianceSimulationDriver;
import osh.simulation.exception.SimulationSubjectException;
import osh.simulation.screenplay.SubjectAction;
import osh.simulation.virtualdevicesdata.DeviceProfile;
import osh.simulation.virtualdevicesdata.ProfileTick.Load;
import osh.configuration.OSHParameterCollection;
import osh.core.IOSH;
import osh.datatypes.Commodity;
import osh.datatypes.XMLSerialization;

/**
 * 
 * @author Florian Allerding, Kaibin Bao, Till Schuberth, Ingo Mauser
 *
 */
public abstract class MieleGenericApplianceSimulationDriver extends AbstractApplianceSimulationDriver {
	
	// single load profile...
	protected DeviceProfile deviceProfile;
	
	protected boolean hasProfile;
	protected boolean isIntelligent;
	private int progamDuration;
	
	/** middle in sense of consumption */
	protected int middleOfPowerConsumption = -1;
	protected long programStart = -1;
	
	/** in Ws (Watt-seconds)*/
	protected int activePowerSumPerRun = -1;
	
	protected int device2ndDof = 0;
	
	private boolean systemState;
	
	
	/**
	 * CONSTRUCTOR
	 * @param controllerbox
	 * @param deviceID
	 * @param driverConfig
	 * @throws SimulationSubjectException
	 */
	public MieleGenericApplianceSimulationDriver(
			IOSH controllerbox, 
			UUID deviceID,
			OSHParameterCollection driverConfig) throws SimulationSubjectException {
		super(controllerbox, deviceID, driverConfig);
		
		String profileSourceName = driverConfig.getParameter("profilesource");
		try {
			this.deviceProfile = (DeviceProfile)XMLSerialization.file2Unmarshal(profileSourceName, DeviceProfile.class);
		}
		catch (Exception ex){
			getGlobalLogger().logError("An error ouccurd while loading the device profile: " +ex.getMessage());
		}
		
		this.device2ndDof = Integer.valueOf(driverConfig.getParameter("device2nddof"));
		
		this.progamDuration = this.deviceProfile.getProfileTicks().getProfileTick().size();
		
	}

	
	/*
	 * PLZ use onProcessingTimeTick() or onActiveTimeTick()
	 */
	@Override
	final public void onNextTimeTick() {
		
		this.onProcessingTimeTick();
		
		if (systemState) {
					
			//next tick
			if (programStart < 0) {
				getGlobalLogger().logError("systemState is true, but programStart < 0", new Exception());
				turnOff();
				return;
			}
			
			long currentTime = getTimer().getUnixTime();
			int currentDurationSinceStart = (int) (currentTime - programStart);
			if (currentDurationSinceStart < 0) {
				getGlobalLogger().logError("timewarp: currentDurationSinceStart is negative", new Exception());
			}
			
			
			if (progamDuration > currentDurationSinceStart) {
				
				// iterate commodities
				List<Load> loadList = deviceProfile.getProfileTicks().getProfileTick().get(currentDurationSinceStart).getLoad();
				for (Load load : loadList) {
					Commodity currentCommodity = Commodity.fromString(load.getCommodity());
					
					if (currentCommodity.equals(Commodity.ACTIVEPOWER)) {
						this.setPower(Commodity.ACTIVEPOWER, load.getValue());
					}
					else if (currentCommodity.equals(Commodity.REACTIVEPOWER)) {
						this.setPower(Commodity.REACTIVEPOWER, load.getValue());
					}
				}
				
				this.onActiveTimeTick();
			}
			else {
				// turn off the device
				turnOff();
			}
		}
	}
	
	private void turnOff() {
		systemState = false;
		this.setPower(Commodity.ACTIVEPOWER, 0);
		this.setPower(Commodity.REACTIVEPOWER, 0);
		programStart = -1;
		this.onProgramEnd();
	}
	
	
	/**
	 * is always invoked while processing a time tick (when onNextTimeTick() has been invoked)
	 */
	protected abstract void onProcessingTimeTick();
	
	/**
	 * is invoked while processing a time tick AND the appliance is running 
	 */
	protected abstract void onActiveTimeTick();
	
	/**
	 * is invoked when the program stops at the end of a work-item
	 */
	protected abstract void onProgramEnd();
		
	@Override
	public void performNextAction(SubjectAction nextAction) {
		//check if the appliance is running 
		setSystemState(nextAction.isNextState());
	}
	
	public void setHasProfile(boolean profile) {
		hasProfile = profile;
	}
	
	
	public void setSystemState(boolean systemState) {
		if (!this.systemState && systemState) {
			programStart = getTimer().getUnixTime();
		}
		
		this.systemState = systemState;
	}
	
	protected int getProgamDuration() {
		return progamDuration;
	}
	
	protected int getMiddleOfDuration() {
		return middleOfPowerConsumption;
	}

	
	@Override
	public boolean isIntelligent() {
		return isIntelligent;
	}
	
	public boolean hasProfile() {
		return hasProfile;
	}
	
}
