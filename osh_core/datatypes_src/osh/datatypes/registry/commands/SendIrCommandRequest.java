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

package osh.datatypes.registry.commands;

import java.util.UUID;

import osh.datatypes.registry.CommandExchange;


/**
 * 
 * @author Ingo Mauser
 *
 */
public class SendIrCommandRequest extends CommandExchange {

	private String remote;
	private String led;
	private String command;
	
	/**
	 * CONSTRUCTOR
	 * @param sender
	 * @param receiver
	 * @param timestamp
	 * @param command
	 */
	public SendIrCommandRequest(
			UUID sender, 
			UUID receiver, 
			long timestamp,
			String remote,
			String led,
			String command) {
		super(sender, receiver, timestamp);
		
		this.remote = remote;
		this.led = led;
		this.command = command;
	}
	

	public String getRemote() {
		return remote;
	}

	public String getLed() {
		return led;
	}



	public String getCommand() {
		return command;
	}
	
}
