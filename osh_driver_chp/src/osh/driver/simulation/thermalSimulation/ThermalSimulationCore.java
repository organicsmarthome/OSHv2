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

import java.io.IOException;
import java.util.ArrayList;

import osh.driver.simulation.thermalSimulation.model.ThermalDemandData;
import osh.simulation.ISimulationSubject;
import osh.simulation.exception.SimulationSubjectException;

/**
 * 
 * @author Florian Allerding, Till Schuberth
 *
 */
public class ThermalSimulationCore {

	private VirtualCHPEngineESHL virtualCHPEngine;
	private VirtualESHLThermalSimulation virtualESHLThermalSim;
	private VirtualHotWaterDemand virtualHotWaterDemand;
	private VirtualHeatingDevice virtualHeatingDevice_Bath;
	private VirtualHeatingDevice virtualHeatingDevice_Bed1;
	private VirtualHeatingDevice virtualHeatingDevice_Bed2;
	private VirtualHeatingDevice virtualHeatingDevice_Living1;
	private VirtualHeatingDevice virtualHeatingDevice_Living2;
	//private VirtualCar virtualCar;
	private double [] HeatingDemandBed1 = new double [8760];
	private double [] HeatingDemandBed2 = new double [8760];
	private double [] HeatingDemandLiving1 = new double [8760];
	private double [] HeatingDemandLiving2 = new double [8760];
	private double [] HeatingDemandBath = new double [8760];
	private double [] hotWaterDemand = new double [8760];
	private ThermalDemandData thermalDemand;
	private int currentTimeTickInHours = 0;
	private long currentTimeTickInS = 0;
	private double sumPowerThHour, sumPowerElHour, sumPowerAuxHour, sumThermalLosses;


	/**the meaning of the update time is the possibility for a quick change of the update time to increase the performance
		but the function has to be checked if it is still working, after making a lot of changes
	 	value is in s and has to be >=1
	 */
	private int updateTime = 1;
//	public ThermalSimulationCore(ArrayList<ISimulationSubject> appendingThermalDeviceRegister) throws SimulationSubjectException{
//		this(appendingThermalDeviceRegister, 0);
//	}
	
	public ThermalSimulationCore(
			ArrayList<ISimulationSubject> appendingThermalDeviceRegister, 
			long startTimeOffset,
			String inputHeatingDevicesSourceFile,
			String inputHotWaterDemandSourceFile) 
					throws SimulationSubjectException {
	
		this.currentTimeTickInHours = (int) (startTimeOffset/3600);
		this.currentTimeTickInS = startTimeOffset;
		try {
			thermalDemand = new ThermalDemandData(
					inputHeatingDevicesSourceFile,
					inputHotWaterDemandSourceFile);
			initThermalDemand();
		} catch (IOException e) {
			throw new SimulationSubjectException(e);
		}
		
		for (ISimulationSubject appendUnits: appendingThermalDeviceRegister) {
			if (appendUnits instanceof VirtualCHPEngineESHL) {
				virtualCHPEngine = (VirtualCHPEngineESHL) appendUnits;
			}
			else if (appendUnits instanceof VirtualESHLThermalSimulation) {
				virtualESHLThermalSim = (VirtualESHLThermalSimulation) appendUnits;
			}
			else if(appendUnits instanceof VirtualHotWaterDemand) {
				virtualHotWaterDemand = (VirtualHotWaterDemand) appendUnits;
			}
			else if( appendUnits instanceof VirtualTemperature) {
				//NOTHING
			} 
			else if (appendUnits instanceof VirtualHeatingDevice) {
				VirtualHeatingDevice heatingDevice = (VirtualHeatingDevice) appendUnits;
				if (heatingDevice.getDriverConfig().getParameter("location").equals("bath")) {
					virtualHeatingDevice_Bath = heatingDevice; 
				}
				else if (heatingDevice.getDriverConfig().getParameter("location").equals("bed1")) {
					virtualHeatingDevice_Bed1 = heatingDevice;
				}
				else if (heatingDevice.getDriverConfig().getParameter("location").equals("bed2")) {
					virtualHeatingDevice_Bed2 = heatingDevice;
				}
				else if (heatingDevice.getDriverConfig().getParameter("location").equals("living1")) {
					virtualHeatingDevice_Living1 = heatingDevice;
				}
				else if (heatingDevice.getDriverConfig().getParameter("location").equals("living2")) {
					virtualHeatingDevice_Living2 = heatingDevice;
				}
			}

		}
	}
	
	public void chpEngineStateChangeRequest(boolean nextState) {
		virtualCHPEngine.setState(nextState, true);
	}
	
	/**
	 * is invoked by the thermal storage on every time tick
	 */
	public void onNextSimulatedTimeTick(){
		
		//check CHP temperature min. and max conditions
		if (!this.virtualESHLThermalSim.checkMinTempCondition()) {
			virtualCHPEngine.setState(true, true);
		}
		
		if (!this.virtualESHLThermalSim.checkMaxTempCondition()) {
			virtualCHPEngine.setState(false, false);
		}
		
		currentTimeTickInS++;
		if (currentTimeTickInS % 3600 == 1){
			currentTimeTickInHours++;
			updateInputData();
		}
		
		if(currentTimeTickInS == 1){
			updateThermalStorage();
			computePower();
		}
		else if (currentTimeTickInS % updateTime == 0){	
			updateThermalStorage();
			computePower();
		}
		
		if (currentTimeTickInS % 3600 == 1){
			sumPowerThHour = 0;
			sumPowerElHour = 0;
			sumPowerAuxHour = 0;
		}
	}

	/**
     * initialize the heating demand and the hot water demand in KW for every hour
     */
	private void initThermalDemand(){
		HeatingDemandBed1 = thermalDemand.getHeatingDemandBed1();
		HeatingDemandBed2 = thermalDemand.getHeatingDemandBed2();
		HeatingDemandLiving1 = thermalDemand.getHeatingDemandLiving1();
		HeatingDemandLiving2 = thermalDemand.getHeatingDemandLiving2();
		HeatingDemandBath =thermalDemand.getHeatingDemandBath();
		hotWaterDemand = thermalDemand.getHotWaterDemand();
	}
	 
	/**
	 * is invoked every hour to update the input data
	 */
	public void updateInputData(){
		try {
		virtualHeatingDevice_Bath.update(getHeatingDemandBath(currentTimeTickInHours),virtualESHLThermalSim.getCurrentTempTop());
		virtualHeatingDevice_Bed1.update(getHeatingDemandBed1(currentTimeTickInHours),virtualESHLThermalSim.getCurrentTempTop());
		virtualHeatingDevice_Bed2.update(getHeatingDemandBed2(currentTimeTickInHours),virtualESHLThermalSim.getCurrentTempTop());
		virtualHeatingDevice_Living1.update(getHeatingDemandLiving1(currentTimeTickInHours),virtualESHLThermalSim.getCurrentTempTop());
		virtualHeatingDevice_Living2.update(getHeatingDemandLiving2(currentTimeTickInHours),virtualESHLThermalSim.getCurrentTempTop());
		virtualHotWaterDemand.update(getHotWaterDemand(currentTimeTickInHours));
		}
		catch (Exception ex) {
			// FIXME: The thermal simulation only works in the year 1970 
			//  - due to the used array - at least a wrapper for that should be build.
			throw new RuntimeException(""
					+ "Yo man we're livin' in the year 1970 ... "
					+ "so let it be ooooh ... "
					+ "whisper words of wisdom ... "
					+ "let it be (Lennon/McCartney 1970)",ex);
		}
	}

	/**
	 * is invoked after a decided time period to update the thermal storage temperatures
	 */
	public void updateThermalStorage(){
		virtualESHLThermalSim.update(
				getRemovedEnergy(currentTimeTickInHours), 
				virtualCHPEngine.getPowerTh(),
				3600 / updateTime, 
				virtualCHPEngine.isEngineState(),
				virtualCHPEngine.getMinEngineRuntimeRemaining());
	}
		
	public double getRemovedEnergy(int timeTick) {
		return (getHeatingDemandBed1(timeTick) 
				+ getHeatingDemandBed2(timeTick)
				+ getHeatingDemandBath(timeTick) 
				+ getHeatingDemandLiving1(timeTick)
				+ getHeatingDemandLiving2(timeTick)
				+ getHotWaterDemand(timeTick));		
	}
	
	public double getHeatingDemandBed1(int timeTick){
		return HeatingDemandBed1[timeTick-1];
	}
	
	public double getHeatingDemandBed2(int timeTick){
		return HeatingDemandBed2[timeTick-1];
	}
	
	public double getHeatingDemandBath(int timeTick){
		return HeatingDemandBath[timeTick-1];
	}
	
	public double getHeatingDemandLiving1(int timeTick){
		return HeatingDemandLiving1[timeTick-1];
	}
	
	public double getHeatingDemandLiving2(int timeTick){
		return HeatingDemandLiving2[timeTick-1];
	}
	
	public double getHotWaterDemand(int timeTick){
		return hotWaterDemand[timeTick-1];
	}

	public VirtualCHPEngineESHL getVirtualHeatingSystem() {
		return virtualCHPEngine;
	}
	
	private void computePower(){
		sumPowerThHour = sumPowerThHour + virtualCHPEngine.getPowerTh() / (3600/updateTime);
		sumPowerElHour = sumPowerElHour + virtualCHPEngine.getPowerEl() / (3600/updateTime);
		sumPowerAuxHour = sumPowerAuxHour + virtualESHLThermalSim.getAuxHeatingPower() / (3600/updateTime);
		sumThermalLosses = sumThermalLosses + virtualESHLThermalSim.getCurrentThermalLosses() / (3600/updateTime);
	}

}
