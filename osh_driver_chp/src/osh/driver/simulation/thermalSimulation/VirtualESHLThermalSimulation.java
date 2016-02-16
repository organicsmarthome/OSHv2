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

package osh.driver.simulation.thermalSimulation;

import java.util.ArrayList;
import java.util.UUID;

import osh.configuration.OSHParameterCollection;
import osh.core.IOSH;
import osh.core.exceptions.OSHException;
import osh.datatypes.ComplexPowerUtil;
import osh.driver.simulation.thermalSimulation.model.BasicThermalModel;
import osh.driver.simulation.thermalSimulation.model.HeatingSystemData;
import osh.driver.simulation.thermalSimulation.model.IThermalModel;
import osh.hal.exceptions.HALException;
import osh.hal.exchange.ESHLThermalControllerExchange;
import osh.hal.exchange.ESHLThermalObserverExchange;
import osh.hal.exchange.HALControllerExchange;
import osh.hal.exchange.HALObserverExchange;
import osh.simulation.ISimulationSubject;
import osh.simulation.AbstractDeviceSimulationDriver;
import osh.simulation.exception.SimulationSubjectException;
import osh.simulation.screenplay.SubjectAction;

/**
 * Combined Thermal Simulation of ESHL
 * @author Florian Allerding, Till Schuberth
 *
 */
public class VirtualESHLThermalSimulation extends AbstractDeviceSimulationDriver {

	private double currentTempTop;
	private double currentTempBottom;
	private double currentThermalLosses;
	/** is ElectricalHeating resistant on */
	private boolean electricHeatingState = false;
	/** max temperature of water storage */
	private double maxTemp;
	private double minTemp;
	/** heating power of heating resistant */
	private final double maxAuxHeating;
	private double auxHeatingPower = 0;
	
	private boolean gasHeatingState;
	private long minRuntimeRemaining;

	private ArrayList<ISimulationSubject> appendingThermalDeviceRegister;
	private ThermalSimulationCore thermalSimulationCore;
	/** Thermal Storage Model (DE: Schichtspeichermodell) */
	private IThermalModel thermalModel;
	/** Thermal Model (DE: Statische Informationen über das Heizsystem, z.B. Schichtspeicherkapazität) */
	private HeatingSystemData heatingSystemData;
	
	private String inputHeatingDevicesSourceFile;
	private String inputHotWaterDemandSourceFile;
	

	/**
	 * CONSTRUCTOR
	 * @throws SimulationSubjectException
	 * @throws HALException
	 */
	public VirtualESHLThermalSimulation(
			IOSH cb, 
			UUID deviceID,
			OSHParameterCollection driverConfig)
					throws SimulationSubjectException, HALException {
		
		super(cb, deviceID, driverConfig);

		appendingThermalDeviceRegister = new ArrayList<ISimulationSubject>();

		// initiate thermal model
		thermalModel = new BasicThermalModel();
		
		// FIXME: Please init the heating system only at one point (now both in
		// the Virtual CHP and the VirtualCHPEngine!!!)
		// now initializing the thermal data
		heatingSystemData = new HeatingSystemData(
				this.getDeviceID(), cb.getTimer().getUnixTime());
		
		// init thermal data
		// init heating config
		try {
			heatingSystemData.initConfig(getDriverConfig().getParameter("chpConfigFileName"));
		} 
		catch (Exception e) {
			getGlobalLogger().logError("Can't init the heating system", e);
		}
		currentTempTop = heatingSystemData.getStartTemp();
		currentTempBottom = heatingSystemData.getStartTemp();
		maxTemp = heatingSystemData.getMaxtemp();
		minTemp = heatingSystemData.getMintemp();
		maxAuxHeating = heatingSystemData.getPoweraux();
		gasHeatingState = false;
		minRuntimeRemaining = 0;
		
		this.inputHeatingDevicesSourceFile = driverConfig.getParameter("inputHeatingDevicesSourceFile");
		this.inputHotWaterDemandSourceFile = driverConfig.getParameter("inputHotWaterDemandSourceFile");
		if (this.inputHeatingDevicesSourceFile == null
				|| inputHotWaterDemandSourceFile == null) {
			throw new HALException("Parameters for Thermal ESHL Simulation missing!");
		}
	}

	public boolean checkMaxTempCondition() {
		if ( (this.currentTempTop + 0.1) > this.maxTemp) {
			return false;
		}
		else {
			return true;
		}
	}

	public boolean checkMinTempCondition() {
		if ( this.currentTempTop < (this.minTemp + 0.1) ) {
			return false;
		}
		else {
			return true;
		}
	}

	private void computeNewTemp(
			double removedEnergy, 
			double suppliedEnergy,
			int updateTime) {

		currentThermalLosses = thermalModel.getThermalLosses(currentTempTop,
				currentTempBottom);
		removedEnergy = (removedEnergy + currentThermalLosses) / updateTime;
		if (electricHeatingState) {
			suppliedEnergy = (suppliedEnergy + maxAuxHeating) / updateTime;
		} 
		else {
			suppliedEnergy = suppliedEnergy / updateTime;
		}
		// if the heating system can't cover the heating demand the removed
		// Energy setting is 0
		if (currentTempTop < 60) {
			removedEnergy = 0;
			this.getGlobalLogger()
					.logError("ALARM: simulation failed: heating system can't supply the heating demand");
		}

		currentTempTop = thermalModel.getTempTop(
				currentTempTop,
				currentTempBottom, 
				removedEnergy, 
				suppliedEnergy);
		currentTempBottom = thermalModel.getTempBottom(
				currentTempTop,
				currentTempBottom, 
				removedEnergy, 
				suppliedEnergy);
	}

	public double getAuxHeatingPower() {
		return auxHeatingPower;
	}

	public double getCurrentTempBottom() {
		return currentTempBottom;
	}

	public double getCurrentTempTop() {
		return currentTempTop;
	}

	public double getCurrentThermalLosses() {
		return currentThermalLosses;
	}

	public UUID getDeviceParam(String name) {
		return UUID.fromString(getDriverConfig().getParameter(name));
	}

	public double getMaxAuxHeating() {
		return maxAuxHeating;
	}

	public long getMinRuntimeRemaining() {
		return minRuntimeRemaining;
	}

	private void initMasterSubject() throws SimulationSubjectException {
		// get appending units
		ArrayList<UUID> appendUnits = new ArrayList<UUID>();
		appendUnits.add(this.getDeviceParam("ID_HeatingSystem"));
		appendUnits.add(this.getDeviceParam("ID_HotWater"));
		appendUnits.add(this.getDeviceParam("ID_Temp"));
		appendUnits.add(this.getDeviceParam("ID_Bath"));
		appendUnits.add(this.getDeviceParam("ID_Bed1"));
		appendUnits.add(this.getDeviceParam("ID_Bed2"));
		appendUnits.add(this.getDeviceParam("ID_Living1"));
		appendUnits.add(this.getDeviceParam("ID_Living2"));

		for (UUID uuid : appendUnits) {
			this.appendingThermalDeviceRegister.add(this.getAppendingSubject(uuid));
		}
		this.appendingThermalDeviceRegister.add(this);
		
		this.thermalSimulationCore = new ThermalSimulationCore(
				this.appendingThermalDeviceRegister, 
				this.getTimer().getUnixTime(),
				this.inputHeatingDevicesSourceFile,
				this.inputHotWaterDemandSourceFile);
	}

	public boolean isElectricHeatingState() {
		return electricHeatingState;
	}

	public boolean isGasHeatingState() {
		return gasHeatingState;
	}

	@Override
	protected void onControllerRequest(HALControllerExchange controllerRequest) throws HALException {

		if (controllerRequest instanceof ESHLThermalControllerExchange) {
			ESHLThermalControllerExchange exchangeController = (ESHLThermalControllerExchange) controllerRequest;
			
			// call electric heating
			setElectricHeatingState(exchangeController.isSwitchElectricalHeating());
			if (electricHeatingState) {
				auxHeatingPower = maxAuxHeating;
			} 
			else {
				auxHeatingPower = 0;
			}

			// call the CHP engine
			this.thermalSimulationCore.chpEngineStateChangeRequest(exchangeController.isSwitchGasHeating());
		}
		else {
			throw new HALException("Object NOT instance of ESHLThermalControllerExchange");
		}
	}

	@Override
	public void onNextTimeTick() {
		this.thermalSimulationCore.onNextSimulatedTimeTick();
	}

	@Override
	public void onSimulationIsUp() throws SimulationSubjectException {
		this.initMasterSubject();
	}

	@Override
	public void onSystemIsUp() throws OSHException {
		super.onSystemIsUp();
		
		this.notifyObserver(heatingSystemData);
	}

	@Override
	public void performNextAction(SubjectAction nextAction) {
		//NOTHING
	}

	public void setAuxHeatingPower(double auxHeatingPower) {
		this.auxHeatingPower = auxHeatingPower;
	}

	public void setCurrentTempBottom(double currentTempBottom) {
		this.currentTempBottom = currentTempBottom;
	}

	public void setCurrentTempTop(double currentTempTop) {
		this.currentTempTop = currentTempTop;
	}

	public void setCurrentThermalLosses(double currentThermalLosses) {
		this.currentThermalLosses = currentThermalLosses;
	}

	public void setElectricHeatingState(boolean electricHeatingState) {
		this.electricHeatingState = electricHeatingState;
	}

	public void setGasHeatingState(boolean gasHeatingState) {
		this.gasHeatingState = gasHeatingState;
	}

	public void setMinRuntimeRemaining(long minRuntimeRemaining) {
		this.minRuntimeRemaining = minRuntimeRemaining;
	}

	/**
	 * is invoked after a decided time period by the thermalSimulationCore
	 */
	public void update(double removedEnergy, 
			double suppliedEnergy,
			int updateTime, 
			boolean gasHeatingState, 
			long minRuntimeRemaining) {

		this.gasHeatingState = gasHeatingState;
		this.minRuntimeRemaining = minRuntimeRemaining;
		computeNewTemp(removedEnergy, suppliedEnergy, updateTime);

		ESHLThermalObserverExchange exchangeObserver = new ESHLThermalObserverExchange(
				this.getDeviceID(), this.getTimer().getUnixTime());
		
		// TODO: add: consider two temperatures (as originally)
		exchangeObserver.setCurrentTempTop(currentTempTop);
		exchangeObserver.setCurrentTempTop(currentTempBottom);
		
		exchangeObserver.setElectricHeatingState(electricHeatingState);
		exchangeObserver.setGasHeatingState(gasHeatingState);
		exchangeObserver.setMinRuntimeRemaining(minRuntimeRemaining);
		exchangeObserver.setDeviceID(this.getDeviceID());
		double electricPower = 0.0;
		int gasPower = 0;
		int thermalPower = 0;
		int reactivePower = 0;
		
		if (gasHeatingState) {
			electricPower = (-1) * heatingSystemData.getPowerEL() * 1000;
			gasPower = heatingSystemData.getPowerGas();
			thermalPower = (int) ((-1) * heatingSystemData.getPowerTh() * 1000);
			try {
				reactivePower = (int) ComplexPowerUtil.convertActiveToReactivePower(-1 * electricPower, 0.7, true);
			} 
			catch (Exception e) {
				// should never happen...probably...
			}
		}
		
		//if (electricHeatingState) power += heatingSystemData.getPower??;
		exchangeObserver.setCurrentElectricPower(electricPower);
		exchangeObserver.setCurrentGasPower(gasPower);
		exchangeObserver.setCurrentThermalPower(thermalPower);
		exchangeObserver.setCurrentReactivePower(reactivePower);

		this.notifyObserver((HALObserverExchange) exchangeObserver);
	}
}
