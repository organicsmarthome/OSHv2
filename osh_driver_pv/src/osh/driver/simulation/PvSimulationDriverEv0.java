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

import java.util.UUID;

import osh.configuration.OSHParameterCollection;
import osh.core.IOSH;
import osh.datatypes.Commodity;
import osh.datatypes.ComplexPowerUtil;
import osh.driver.pv.PvProfile;
import osh.hal.exchange.HALControllerExchange;
import osh.hal.exchange.PvHALControllerExchange;
import osh.hal.exchange.PvHALObserverExchange;
import osh.simulation.AbstractDeviceSimulationDriver;
import osh.simulation.screenplay.SubjectAction;

/**
 * 
 * @author Ingo Mauser
 *
 */
public class PvSimulationDriverEv0 extends AbstractDeviceSimulationDriver {
	
	private boolean pvSwitchedOn;
	private int reactivePowerTarget;	
	
	/** nominal power of PV (e.g. 4.6kWpeak) */
	private int nominalPower;
	/** according to inverter (technical, due to VAmax) */
	private int complexPowerMax;
	@SuppressWarnings("unused")
	private int reactivePowerMax;
	/** according to inverter (technical, due to cosPhi: e.g. 0.8ind...0.8cap) */
	private double cosPhiMax;

	private PvProfile profile;
	

	/**
	 * CONSTRUCTOR
	 * @throws Exception
	 */
	public PvSimulationDriverEv0(
			IOSH controllerbox, 
			UUID deviceID,
			OSHParameterCollection driverConfig)
			throws Exception {
		super(controllerbox, deviceID, driverConfig);
		
		this.pvSwitchedOn = true;
		this.reactivePowerTarget = 0;
		
		String profileSourceName = driverConfig.getParameter("profilesource");
		this.nominalPower = Integer.valueOf(driverConfig.getParameter("nominalpower"));
		this.complexPowerMax = Integer.valueOf(driverConfig.getParameter("complexpowermax"));
		this.cosPhiMax = Double.valueOf(driverConfig.getParameter("cosphimax"));
		
		double randomDay[] = new double[366];
		for (int i = 0; i < randomDay.length; i++) {
			randomDay[i] = getRandomGenerator().getNextDouble();
		}
		
		this.profile = new PvProfile(profileSourceName, this.nominalPower, randomDay);
		this.reactivePowerMax = 
				(int) ComplexPowerUtil.convertComplexToReactivePower(
						this.complexPowerMax, this.cosPhiMax, true);
	}

	@Override
	protected void onControllerRequest(HALControllerExchange controllerRequest) {
		PvHALControllerExchange controllerExchange = (PvHALControllerExchange) controllerRequest;
		Boolean newPvSwitchedOn = controllerExchange.getNewPvSwitchedOn();
		
		// check whether to switch pv on or off
		if (newPvSwitchedOn != null) {
			this.pvSwitchedOn = newPvSwitchedOn;
			if (this.pvSwitchedOn == false) {
				this.setPower(Commodity.ACTIVEPOWER, 0);
				this.setPower(Commodity.REACTIVEPOWER, 0);
				this.reactivePowerTarget = 0;
			}
		}
	}

	@Override
	public void onNextTimeTick() {
		if (this.pvSwitchedOn) {
			long now = getTimer().getUnixTime();
			this.setPower(Commodity.ACTIVEPOWER, profile.getPowerAt(now));
			
			if (getPower(Commodity.ACTIVEPOWER) != 0) {
				double newCosPhi;
				try {
					newCosPhi = ComplexPowerUtil.convertActiveAndReactivePowerToCosPhi(
							getPower(Commodity.ACTIVEPOWER), this.reactivePowerTarget);
					
					if (newCosPhi > this.cosPhiMax) {
						this.setPower(Commodity.REACTIVEPOWER, (int) ComplexPowerUtil.convertActiveToReactivePower(
								getPower(Commodity.ACTIVEPOWER), 
								this.cosPhiMax, 
								(this.reactivePowerTarget >= 0)));
					}
					else if (newCosPhi < -1) {
						newCosPhi = -1;
						this.setPower(Commodity.REACTIVEPOWER, 0);
					}
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			else {
				this.setPower(Commodity.REACTIVEPOWER, 0);
			}
		}
		
		PvHALObserverExchange _ox = new PvHALObserverExchange(this.getDeviceID(), getTimer().getUnixTime());
		_ox.setActivePower(this.getPower(Commodity.ACTIVEPOWER));
		_ox.setReactivePower(this.getPower(Commodity.REACTIVEPOWER));
		this.notifyObserver(_ox);
	}

	@Override
	public void performNextAction(SubjectAction nextAction) {
		//NOTHING
	}

}
