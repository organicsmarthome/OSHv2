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

package osh.hal.exchange;

import java.util.UUID;

import osh.datatypes.en50523.EN50523OIDExecutionOfACommandCommands;
import osh.datatypes.hal.interfaces.appliance.IHALGenericApplianceEn50523Command;
import osh.hal.exchange.HALControllerExchange;


/**
 * 
 * @author Florian Allerding, Kaibin Bao, Till Schuberth, Ingo Mauser
 *
 */
public class MieleApplianceControllerExchange extends HALControllerExchange
		implements IHALGenericApplianceEn50523Command {

	private EN50523OIDExecutionOfACommandCommands applianceCommand;
	
	
	/**
	 * CONSTRUCTOR
	 * @param deviceID
	 * @param timestamp
	 */
	public MieleApplianceControllerExchange(
			UUID deviceID, 
			Long timestamp,
			EN50523OIDExecutionOfACommandCommands applianceCommand) {
		super(deviceID, timestamp);
		
		this.applianceCommand = applianceCommand;
	}
	
	@Override
	public EN50523OIDExecutionOfACommandCommands getApplianceCommand() {
		return applianceCommand;
	}
	
	// CLONING not necessary
	
}
