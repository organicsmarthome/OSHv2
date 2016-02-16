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

package osh.driver.simulation.thermalSimulation.model;


/**
 * simple model of a thermal storage, should be improved by implementing a stratified thermal storage
 * 
 * @author Till Schuberth
 *
 */
public class BasicThermalModel implements IThermalModel {
	
	/** unit [dm^3] (a.k.a. liter) */
	public static final int volume = 750;
	/** unit [kg / dm^3] */
	public static final int density = 1;
	/** [m] */
	private static final double height= 1.750;
	/** [m] */
	private static final double radius = 0.375;
	private static final double uValue = 0.3;
	/** [degree C] */
	private static final int roomTemp = 25;
	/** unit: [kJ/kg] */
	public static final double heatingCapacity = 4.2;
	
	
	@Override
	public double getTempTop(
			double tempTop, 
			double tempBottom, 
			double removedEnergy, 
			double suppliedEnergy){
		return tempTop - (((removedEnergy - suppliedEnergy) * 3600) / (heatingCapacity * volume * density));
	}
	
	@Override
	public double getTempBottom (double tempTop, double tempBottom,double removedEnergy, double suppliedEnergy){
		return tempBottom - (((removedEnergy - suppliedEnergy) * 3600) / (heatingCapacity * volume * density));
	}
	
	@Override
	public double getThermalLosses(double tempTop, double tempBottom){
		return (radius * radius * Math.PI * 2 + height * 2 * radius * Math.PI) 
				* uValue * ((tempTop + tempBottom) / 2 - roomTemp) / 1000;
	}

}
