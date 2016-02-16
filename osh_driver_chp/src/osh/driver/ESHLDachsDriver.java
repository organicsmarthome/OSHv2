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

import java.util.HashMap;
import java.util.UUID;

import osh.configuration.OSHParameterCollection;
import osh.core.IOSH;
import osh.core.com.interfaces.IHasState;
import osh.core.exceptions.OSHException;
import osh.datatypes.registry.details.common.TemperatureDetails;
import osh.datatypes.registry.driver.details.chp.ChpDriverDetails;
import osh.datatypes.registry.driver.details.chp.raw.DachsDriverDetails;
import osh.driver.dachs.DachsPowerRequestThread;
import osh.driver.simulation.thermalSimulation.model.HeatingSystemData;
import osh.hal.exchange.ESHLThermalControllerExchange;
import osh.hal.exchange.ESHLThermalObserverExchange;
import osh.hal.exchange.HALControllerExchange;


/**
 * 
 * @author Florian Allerding, Kaibin Bao, Till Schuberth, Ingo Mauser
 *
 */
public class ESHLDachsDriver extends DachsDriver implements IHasState {

//	public static final String TEMPERATUREKEY_OUTSIDE = "outside";
//	public static final String TEMPERATUREKEY_SENSOR1 = "sensor1";
//	public static final String TEMPERATUREKEY_SENSOR2 = "sensor2";
//	public static final String TEMPERATUREKEY_DACHSIN = "dachsin";
//	public static final String TEMPERATUREKEY_DACHSOUT = "dachsout";
//	public static final String TEMPERATUREKEY_OUTGOING = "outgoing";
//	public static final String TEMPERATUREKEY_INCOMING = "incoming";
//	public static final String TEMPERATUREKEY_OUTGOINGCIRCUIT1 = "outgoingCircuit1";
//	public static final String TEMPERATUREKEY_HOTWATER = "hotwater";
	
	private long lastElectricityRequestTime = 0;
	private HeatingSystemData heatingSystemData;
	private int temperatureTop;
	
	
	/**
	 * CONSTRUCTOR
	 * @param controllerbox
	 * @param deviceID
	 * @param driverConfig
	 * @throws OSHException 
	 */
	public ESHLDachsDriver(
			IOSH controllerbox, 
			UUID deviceID,
			OSHParameterCollection driverConfig) throws OSHException {
		super(controllerbox, deviceID, driverConfig);
		
	}
	
	
	@Override
	public void onNextTimePeriod() throws OSHException {
		super.onNextTimePeriod();
		
		long now = getTimer().getUnixTime();
		
		if ( (now - lastElectricityRequestTime >= 60) ) {
			this.lastElectricityRequestTime = now;
			
			DachsPowerRequestThread powerRequestThread = new DachsPowerRequestThread(
					isOperationRequest(),
					dachsURL,
					getGlobalLogger());
			new Thread(powerRequestThread, "DachsPowerRequestThread").start();
		}
	}
	
	
	@Override
	protected void sendPowerRequestToChp() {
		//Start new thread and send power request (on OR off) to CHP
		
//		this.firstElectricityRequestTime = getTimer().getUnixTime();
		this.lastElectricityRequestTime = getTimer().getUnixTime();
		
		DachsPowerRequestThread powerRequestThread = new DachsPowerRequestThread(
				isOperationRequest(),
				dachsURL,
				getGlobalLogger());
		new Thread(powerRequestThread, "DachsPowerRequestThread").start();
	}

	
	@Override
	public synchronized void processChpDetailsAndNotify(ChpDriverDetails chpDetails) {
		
		ESHLThermalObserverExchange exchangeObserver = new ESHLThermalObserverExchange(
				this.getDeviceID(), this.getTimer().getUnixTime());
		exchangeObserver.setCurrentTempTop(temperatureTop);
		exchangeObserver.setElectricHeatingState(false);
		double power = chpDetails.getCurrentElectricalPower();
		boolean isOn = false;
		if (power >= 500) {
			isOn = true;
		}
		
		exchangeObserver.setGasHeatingState(isOn);
		exchangeObserver.setCurrentElectricPower(power);
		exchangeObserver.setMinRuntimeRemaining(isOn ? getMinimumRuntimeRemaining() : 0);
		exchangeObserver.setDeviceID(this.getDeviceID());

		this.notifyObserver(exchangeObserver);
	}


	@Override
	public void onSystemIsUp() throws OSHException {
		super.onSystemIsUp();
		
		heatingSystemData = new HeatingSystemData(this.getDeviceID(), this.getTimer().getUnixTime());
		// init thermal data
		// init heating config
		try {
			heatingSystemData.initConfig(getDriverConfig().getParameter("chpConfigFileName"));
		} catch (Exception e) {
			this.getGlobalLogger().logError("Can't init the heating system", e);
		}
		
		this.notifyObserver(heatingSystemData);
	}
	
	// for callback of DachsInformationRequestThread
	public void processDachsDetails(DachsDriverDetails dachsDetails){
		
		// ### save DachsDetails into DB ###
		getDriverRegistry().setStateOfSender(DachsDriverDetails.class, dachsDetails);
		
		// ### transform DachsDetails to ChpDetails ###
		HashMap<String,String> values = dachsDetails.getValues();
		
		// convert Dachs Details to general CHP details
		ChpDriverDetails chpDetails = new ChpDriverDetails(getDeviceID(), getTimer().getUnixTime());
		
		// Heating request or power request? Or both?
		chpDetails.setPowerGenerationRequest(isElectricityRequest());
		chpDetails.setHeatingRequest(isHeatingRequest());

		// current power
		Double currentElectricalPower = parseDoubleStatus(values.get("Hka_Mw1.sWirkleistung"));
		if (currentElectricalPower == null) {
			currentElectricalPower = -1.0;
		}
		else {
			currentElectricalPower = currentElectricalPower * 1000;
		}
		chpDetails.setCurrentElectricalPower(currentElectricalPower);
		
		Double currentThermalPower = -1.0;
		if (Math.round(currentElectricalPower) > 1000) {
			currentThermalPower = 12500.0;
		}
		chpDetails.setCurrentThermalPower(currentThermalPower);
		
		// total energy
		Double generatedElectricalWork = parseDoubleStatus(values.get("Hka_Bd.ulArbeitElektr"));
		if (generatedElectricalWork == null) {
			generatedElectricalWork = -1.0;
		}
		chpDetails.setGeneratedElectricalWork(generatedElectricalWork);
		
		Double generatedThermalWork = parseDoubleStatus(values.get("Hka_Bd.ulArbeitThermHka"));
		if (generatedThermalWork != null) {
			generatedThermalWork = -1.0;
		}
		chpDetails.setGeneratedThermalWork(generatedThermalWork);
		
		// priorities
		Integer electicalPowerPriorizedControl = parseIntegerStatus(values.get("Hka_Bd.UStromF_Frei.bFreigabe"));
		if (electicalPowerPriorizedControl != null) {
			if (electicalPowerPriorizedControl == 255) {
				chpDetails.setElecticalPowerPriorizedControl(true);
			}
			else {
				chpDetails.setElecticalPowerPriorizedControl(false);
			}
		}
		// always with thermal priority
		chpDetails.setThermalPowerPriorizedControl(true);
		
		// temperature
		Integer temperatureIn = parseIntegerStatus(values.get("Hka_Mw1.Temp.sbGen"));
		if (temperatureIn == null) {
			temperatureIn = -1;
		}
		chpDetails.setTemperatureIn(temperatureIn);
		
		Integer temperatureOut = parseIntegerStatus(values.get("Hka_Mw1.Temp.sbMotor"));
		if (temperatureOut == null) {
			temperatureOut = -1;
		}
		chpDetails.setTemperatureOut(temperatureOut);
		
//			Boolean statusSEplus = parseBooleanStatus(values.get("Brenner_Bd.bIstStatus"));
//			if (statusSEplus != null) {
//				dachsDetails.setStatusSEplus(statusSEplus);
//			}
		
		// convert to TemperatureDetails
		TemperatureDetails td = new TemperatureDetails(getDeviceID(), getTimer().getUnixTime());
		waterStorageTemperature = parseDoubleStatus(values.get("Hka_Mw1.Temp.sbFuehler1"));
		if (waterStorageTemperature != null) {
			td.setTemperature(waterStorageTemperature);
			getDriverRegistry().setState(TemperatureDetails.class, this, td);
		}
		
		Integer temperatureTop = parseIntegerStatus(values.get("Hka_Mw1.Temp.sbVorlauf"));
		if (temperatureTop == null) {
			temperatureTop = 60;
		}
		this.temperatureTop = temperatureTop;
		
		Double parsedRuntime = parseDoubleStatus(values.get("Hka_Mw1.ulMotorlaufsekunden"));
		int runtime = 0;
		if (parsedRuntime != null) {
			runtime = (int) (parsedRuntime * 3600);
		} else {
			runtime = 0;
		}
		this.runtime = runtime;
		
		this.chpDriverDetails = chpDetails;
		getDriverRegistry().setState(ChpDriverDetails.class, this, chpDriverDetails);

		processChpDetailsAndNotify(chpDriverDetails);
	}


	@Override
	protected void onControllerRequest(
			HALControllerExchange controllerRequest) {
		if (controllerRequest instanceof ESHLThermalControllerExchange) {
			ESHLThermalControllerExchange exchangeControllerEx = (ESHLThermalControllerExchange) controllerRequest;
			
			setElectricityRequest(exchangeControllerEx.isSwitchGasHeating());
			// call the chp engine
			sendPowerRequestToChp();
		}
		else {
			getGlobalLogger().logError("Object NOT instance of ESHLThermalControllerExchange");
		}
	}
	
}
