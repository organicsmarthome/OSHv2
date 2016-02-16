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

package osh.comdriver.simulation;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import osh.configuration.OSHParameterCollection;
import osh.core.IOSH;
import osh.core.com.interfaces.IEventReceiver;
import osh.core.exceptions.OSHException;
import osh.datatypes.dof.ApplianceDofStateExchange;
import osh.datatypes.registry.EventExchange;
import osh.datatypes.registry.StateChangedExchange;
import osh.hal.exchange.DofComExchange;
import osh.simulation.SimulationComDriver;
import osh.simulation.exception.SimulationSubjectException;
import osh.simulation.screenplay.ActionParameter;
import osh.simulation.screenplay.SubjectAction;

/**
 * 
 * @author Ingo Mauser
 *
 */
public class DofProviderSimulationComDriver 
				extends SimulationComDriver
				implements IEventReceiver {
	
	HashMap<UUID, Integer> device1stDegreeOfFreedom;
	HashMap<UUID, Integer> device2ndDegreeOfFreedom;

	
	/**
	 * CONSTRUCTOR
	 * @param controllerbox
	 * @param deviceID
	 * @param driverConfig
	 * @throws SimulationSubjectException
	 */
	public DofProviderSimulationComDriver(
			IOSH controllerbox, UUID deviceID,
			OSHParameterCollection driverConfig)
			throws SimulationSubjectException {
		super(controllerbox, deviceID, driverConfig);
		
		device1stDegreeOfFreedom = new HashMap<>();
		device2ndDegreeOfFreedom = new HashMap<>();
	}
	
	
	@Override
	public void onSystemIsUp() throws OSHException {
		super.onSystemIsUp();
		this.getDriverRegistry().registerStateChangeListener(ApplianceDofStateExchange.class, this);
	}
	
	@Override
	public void onQueueEventReceived(EventExchange ex)
			throws OSHException {
		if (ex instanceof StateChangedExchange) {
			StateChangedExchange exsc = (StateChangedExchange) ex;
			if (exsc.getType().equals(ApplianceDofStateExchange.class)) {
				ApplianceDofStateExchange dse = this.getDriverRegistry().getState(
						ApplianceDofStateExchange.class, exsc.getStatefulentity());
				int new1stDof = dse.getFirstDof();
				int new2ndDof = dse.getSecondDof();
				
				boolean initalDof = false;
				// default value is 0, but HashMap is empty at startup
				if (this.device1stDegreeOfFreedom.get(dse.getSender()) == null) {
					this.device1stDegreeOfFreedom.put(dse.getSender(), 0);
					initalDof = true;
				}
				// default value is 0, but HashMap is empty at startup
				if (this.device2ndDegreeOfFreedom.get(dse.getSender()) == null) {
					this.device2ndDegreeOfFreedom.put(dse.getSender(), 0);
					initalDof = true;
				}
				
				// check if DOF has changed
				if ( new1stDof != this.device1stDegreeOfFreedom.get(dse.getSender())
						|| new2ndDof != this.device2ndDegreeOfFreedom.get(dse.getSender())
						|| initalDof ) {
					
					this.device1stDegreeOfFreedom.put(dse.getSender(), new1stDof);
					this.device2ndDegreeOfFreedom.put(dse.getSender(), new2ndDof);
					
					DofComExchange dofcex = new DofComExchange(getDeviceID(), getTimer().getUnixTime());
					dofcex.setDevice1stDegreeOfFreedom(this.device1stDegreeOfFreedom);
					dofcex.setDevice2ndDegreeOfFreedom(this.device2ndDegreeOfFreedom);
					
					this.notifyComManager(dofcex);
				}
			}
		}
	}
	
	@Override
	public void performNextAction(SubjectAction nextAction) {
		List<ActionParameter> dofParam = nextAction.getPerformAction().get(0).getActionParameterCollection().get(0).getParameter();
		for (ActionParameter parameter: dofParam){
			String[] value = parameter.getValue().split(";");
			int value1stDof = Integer.parseInt(value[0]);
			int value2ndDof = 0;
			if (value.length == 2) {
				value2ndDof = Integer.parseInt(value[1]);
			}
			this.device1stDegreeOfFreedom.put(UUID.fromString(parameter.getName()), value1stDof);
			this.device2ndDegreeOfFreedom.put(UUID.fromString(parameter.getName()), value2ndDof);
		}
		
		DofComExchange dofcex = new DofComExchange(getDeviceID(), getTimer().getUnixTime());
		dofcex.setDevice1stDegreeOfFreedom(this.device1stDegreeOfFreedom);
		dofcex.setDevice2ndDegreeOfFreedom(this.device2ndDegreeOfFreedom);
		this.notifyComManager(dofcex);
	}

	@Override
	public UUID getUUID() {
		return getDeviceID();
	}
	
}
