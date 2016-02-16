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

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import osh.datatypes.Commodity;
import osh.datatypes.hal.interfaces.electricity.IHALElectricPowerDetails;
import osh.datatypes.hal.interfaces.gas.IHALGasPowerDetails;
import osh.hal.exchange.HALDeviceObserverExchange;


/**
 * 
 * @author Ingo Mauser
 *
 */
public class BaseloadObserverExchange 
				extends HALDeviceObserverExchange
				implements IHALElectricPowerDetails, IHALGasPowerDetails {
	
	private HashMap<Commodity,Integer> powerMap;
	
	
	/**
	 * CONSTRUCTOR
	 * @param deviceID
	 * @param timestamp
	 * @param complexPower
	 */
	public BaseloadObserverExchange(
			UUID deviceID, 
			Long timestamp) {
		super(deviceID, timestamp);
		
		this.powerMap = new HashMap<>();
	}
	
	
	public Integer getPower(Commodity c) {
		return powerMap.get(c);
	}
	
	public void setPower(Commodity c, int power) {
		powerMap.put(c, power);
	}
	
	public Set<Commodity> getCommodities() {
		return powerMap.keySet();
	}
	
	@Override
	public int getGasPower() {
		return powerMap.get(Commodity.NATURALGASPOWER);
	}

	@Override
	public int getActivePower() {
		return powerMap.get(Commodity.ACTIVEPOWER);
	}

	@Override
	public int getReactivePower() {
		return powerMap.get(Commodity.REACTIVEPOWER);
	}

}
