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
import osh.driver.simulation.thermalSimulation.model.HeatingSystemData;
import osh.hal.exchange.ESHLThermalObserverExchange;
import osh.mgmt.localobserver.chp.WaterStorageModel;


/**
 * 
 * @author Florian Allerding, Till Schuberth
 *
 */
public class StupidAdvancedESHLThermalLocalObserver extends LocalObserver implements IHasState, IEventReceiver {

	private WaterStorageModel model;
	private HeatingSystemData heatingSystemData;


	/**
	 * CONSTRUCTOR
	 */
	public StupidAdvancedESHLThermalLocalObserver(IOSHOC controllerbox) {
		super(controllerbox);
	}

	
	@Override
	public void onDeviceStateUpdate() throws OCUnitException{
		if (this.getObserverDataObject() instanceof HeatingSystemData) {
			System.out.println("DEBUG: HeatingSystemData exchange");
			heatingSystemData = (HeatingSystemData) this.getObserverDataObject();
			if (model == null) {
				model = new WaterStorageModel(heatingSystemData.getMintemp(), heatingSystemData.getMaxtemp(), kW2W(heatingSystemData.getPowerTh()));
				
			}
		} 
		else if (this.getObserverDataObject() instanceof ESHLThermalObserverExchange) {
			ESHLThermalObserverExchange chpex = (ESHLThermalObserverExchange) this.getObserverDataObject();
	
			if (heatingSystemData != null){
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
			
			if (model != null) {
				model.temperatureUpdate(chpex.getTimestamp(), chpex.getCurrentTempTop(), chpex.isGasHeatingState());
			}
			
//			ExpectedStateExchange expected = getOCRegistry().getState(ExpectedStateExchange.class, getUUID());
			
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

		} else {
			throw new OCUnitException("got unknown HALExchange object");
		}		
	}
	
	private static int kW2W(double kW) {
		return (int) (kW * 1000);
	}

	@Override
	public void onQueueEventReceived(EventExchange event)
			throws OSHException {
		//NOTHING
	}

	@Override
	public UUID getUUID() {
		return getDeviceID();
	}

	@Override
	public IModelOfObservationExchange getObservedModelData(
			ModelOfObservationType type) {
		return null;
	}
	
}
