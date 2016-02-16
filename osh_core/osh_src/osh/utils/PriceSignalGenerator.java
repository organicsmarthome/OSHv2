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

package osh.utils;

import osh.datatypes.VirtualCommodity;
import osh.datatypes.limit.PriceSignal;

/**
 * 
 * @author Ingo Mauser
 *
 */
public class PriceSignalGenerator {
	
	public static PriceSignal getConstantPriceSignal(
			VirtualCommodity commodity,
			long startTime, 
			long endTime, 
			long constantPeriod,
			double price) {
		
		PriceSignal priceSignal = new PriceSignal(commodity);
		
		for (long i = 0; i < ((endTime - startTime) / constantPeriod); i++){
			
			long timestamp = startTime + i * constantPeriod;

			priceSignal.setPrice(timestamp, price);
		}
		priceSignal.setKnownPriceInterval(startTime, endTime);
		
		priceSignal.compress();
		
		return priceSignal;
	}
	

	public static PriceSignal getStepPriceSignal(
			long startTime, 
			long endTime, 
			long constantPeriod,
			int periodsToSwitch,
			VirtualCommodity commodity,
			double priceMin,
			double priceMax,
			boolean startWithMinPrice) {
		
		boolean lowPricePeriod = startWithMinPrice;
		
		PriceSignal priceSignal = new PriceSignal(commodity);
		
		int counter = 0;
		
		for (long i = 0; i < ((endTime - startTime) / constantPeriod); i++){
			
			long timestamp = startTime + i * constantPeriod;

			if (lowPricePeriod) {
				priceSignal.setPrice(timestamp, priceMin);
			}
			else {
				priceSignal.setPrice(timestamp, priceMax);
			}
			
			counter++;
			
			if (counter >= periodsToSwitch) {
				counter = 0;
				lowPricePeriod = !lowPricePeriod;
			}
		}
		
		priceSignal.setKnownPriceInterval(startTime, endTime);
		priceSignal.compress();
		
		return priceSignal;
	}
	
}
