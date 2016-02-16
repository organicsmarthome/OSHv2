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

package osh.datatypes.cruisecontrol;

import java.util.UUID;

import osh.datatypes.cruisecontrol.OptimizedDataStorage.EqualData;
import osh.datatypes.registry.localobserver.WaterStorageStateExchange;


/**
 * 
 * @author Till Schuberth
 *
 */
public class GUIWaterStorageStateExchange 
				extends WaterStorageStateExchange 
				implements EqualData<WaterStorageStateExchange> {

	/**
	 * CONSTRUCTOR 
	 */
	public GUIWaterStorageStateExchange(
			UUID sender, 
			long timestamp,
			double currenttemp, 
			double mintemp, 
			double maxtemp) {
		super(sender, timestamp, currenttemp, mintemp, maxtemp);
	}

	
	public boolean equalData(GUIWaterStorageStateExchange o) {
		return super.equalData(o);
	}
}
