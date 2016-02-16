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
import osh.core.exceptions.OCUnitException;
import osh.core.exceptions.OSHException;
import osh.core.oc.LocalObserver;
import osh.datatypes.Commodity;
import osh.datatypes.mooex.IModelOfObservationExchange;
import osh.datatypes.mooex.ModelOfObservationType;
import osh.datatypes.registry.EventExchange;
import osh.datatypes.registry.globalobserver.CommodityPowerStateExchange;
import osh.datatypes.registry.localobserver.WaterStorageStateExchange;
import osh.datatypes.registry.observer.EAProblemPartExchange;
import osh.driver.simulation.thermalSimulation.model.HeatingSystemData;
import osh.hal.exchange.ESHLThermalObserverExchange;
import osh.mgmt.localobserver.chp.ExpectedStateExchange;
import osh.mgmt.localobserver.chp.WaterStorageModel;
import osh.mgmt.localobserver.chp.eapart.AbstractCHPEAPart;
import osh.mgmt.localobserver.chp.eapart.AdvancedCHPEAPartTemperatureControl;
import osh.mgmt.localobserver.chp.eapart.CHPEAPartDoNothing;

/**
 * 
 * @author Florian Allerding, Till Schuberth
 *
 */
public class AdvancedESHLThermalLocalObserver extends LocalObserver implements IHasState, IEventReceiver {
	private long lastTimeReschedulingTriggered;

	private WaterStorageModel model;
	private HeatingSystemData heatingSystemData;
	private ESHLThermalObserverExchange lastExchange;
	
	private boolean debugElHeat;
	private boolean oldState;

	
	/**
	 * CONSTRUCTOR
	 */
	public AdvancedESHLThermalLocalObserver(IOSHOC controllerbox) {
		super(controllerbox);
	}
	

	@Override
	public void onDeviceStateUpdate() throws OCUnitException {
		if (this.getObserverDataObject() instanceof HeatingSystemData) {
			heatingSystemData = (HeatingSystemData) this.getObserverDataObject();
			if (model == null) {
				model = new WaterStorageModel(heatingSystemData.getMintemp(), heatingSystemData.getMaxtemp(), kW2W(heatingSystemData.getPowerTh()));
				createNewEaPart(false, 0);
			}
		} else if (this.getObserverDataObject() instanceof ESHLThermalObserverExchange){
			ESHLThermalObserverExchange chpex = (ESHLThermalObserverExchange) this.getObserverDataObject();
			lastExchange = chpex;
			
			if ( heatingSystemData != null ) {
				getOCRegistry().setState(
						WaterStorageStateExchange.class, 
						this, 
						new WaterStorageStateExchange(
								getDeviceID(), 
								chpex.getTimestamp(), 
								Math.round(chpex.getCurrentTempTop() * 10.0) / 10.0, 
								heatingSystemData.getMintemp(), 
								heatingSystemData.getMaxtemp()));
			}
			
			if ( model != null )  {
				model.temperatureUpdate(chpex.getTimestamp(), chpex.getCurrentTempTop(), chpex.isGasHeatingState());
			}
			
			ExpectedStateExchange expected = getOCRegistry().getState(ExpectedStateExchange.class, getUUID());
			if (oldState != chpex.isGasHeatingState() 
					&& expected != null 
					&& chpex.isGasHeatingState() != expected.getExpectedState()) {
				//chp changed its state, but we didn't expect that, reschedule
				createNewEaPart(chpex.isGasHeatingState(), chpex.getMinRuntimeRemaining());
				getGlobalLogger().logInfo("chp changed its state, but we didn't say so, reschedule");
			}
			
			oldState = chpex.isGasHeatingState();
			
//			getOCRegistry().setState(
//					DevicesPowerStateExchange.class, 
//					this, 
//					new ElectricPowerOCDetails(
//							getDeviceID(), 
//							getTimer().getUnixTime(), 
//							(int) Math.round(chpex.getCurrentpower()), 
//							0));
			
			CommodityPowerStateExchange cpse = new CommodityPowerStateExchange(
					getDeviceID(), 
					getTimer().getUnixTime(),
					DeviceTypes.CHPPLANT);
			cpse.addPowerState(Commodity.ACTIVEPOWER, chpex.getCurrentElectricPower());
			cpse.addPowerState(Commodity.REACTIVEPOWER, chpex.getCurrentReactivePower());
			cpse.addPowerState(Commodity.NATURALGASPOWER, chpex.getCurrentGasPower());
			cpse.addPowerState(Commodity.WARMWATERPOWER,chpex.getCurrentThermalPower());
			
			this.getOCRegistry().setState(
					CommodityPowerStateExchange.class,
					this,
					cpse);
			
			//debug message
			if (debugElHeat != chpex.isElectricHeatingState()) {
				getGlobalLogger().logDebug("electrical heater is " + (chpex.isElectricHeatingState() ? "on" : "off"));
				debugElHeat = chpex.isElectricHeatingState();
			}
		} else {
			throw new OCUnitException("got unknown HALExchange object");
		}		
	}
	
	private void createNewEaPart(boolean currentState, long remainingRunningTime) {
		AbstractCHPEAPart ex;
		if (model != null && heatingSystemData != null) {
			long remaining = remainingRunningTime;
			if (currentState == false) {
				remaining = 0; // CHP was shut down because the water is too hot, no remaining time
			}
			ex = new AdvancedCHPEAPartTemperatureControl(
					getDeviceID(), 
					getTimer().getUnixTime(), 
					kW2W(heatingSystemData.getPowerEL()), 
					kW2W(heatingSystemData.getPowerTh()),
					heatingSystemData.getPowerGas(),
					model.getNeededEnergy(), 
					remaining);
		} 
		else {
			ex = new CHPEAPartDoNothing(
					getDeviceID(), 
					getTimer().getUnixTime());
		}
		
		getOCRegistry().setState(
				EAProblemPartExchange.class, this, ex);
		lastTimeReschedulingTriggered = getTimer().getUnixTime();
	}
	
	@Override
	public void onSystemIsUp() throws OSHException {
		super.onSystemIsUp();
		
		//init before registering for timer messages
		createNewEaPart(false, 0);

		getTimer().registerComponent(this, 1);
	}
	
	@Override
	public void onNextTimePeriod() throws OSHException {
		long diff = getTimer().getUnixTime() - lastTimeReschedulingTriggered;
		if (diff < 0 || diff > 1800) {
			if (lastExchange != null) {
				createNewEaPart(
						lastExchange.isGasHeatingState(), 
						lastExchange.getMinRuntimeRemaining());
			} else {
				createNewEaPart(false, 0);
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
	
	private static int kW2W(double kW) {
		return (int) (kW * 1000);
	}

	@Override
	public void onQueueEventReceived(EventExchange event)
			throws OSHException {
		//NOTHING
	}

}
