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

package osh.mgmt.localobserver.chp;

import java.util.Map.Entry;
import java.util.TreeMap;

import osh.driver.simulation.thermalSimulation.model.BasicThermalModel;


/**
 * 
 * @author Florian Allerding
 *
 */
public class WaterStorageModel {

	/**
	 * INNER CLASS
	 */
	private class TemperatureDataPoint{
		public TemperatureDataPoint(double temperature, boolean running) {
			this.temperature = temperature;
			this.running = running;
		}
		double temperature;
		boolean running;
	}
	
	private NeededEnergyArray neededEnergy = new NeededEnergyArray(0, new long[0], new long[0]);
	private TreeMap<Long, TemperatureDataPoint> lastValues = new TreeMap<Long, TemperatureDataPoint>();
	private double mintemp, maxtemp;
	/** energy necessary to heat the storage 1 K, in Ws/K */ 
	private double energyperkelvin;
	private double gasHeatingGradient;
	
	private static final long HISTORYLENGTH = 600;
	private static final long NEEDEDENERGYTIME = 3600 * 3; //3 hours
	
	
	/**
	 * CONSTRUCTOR
	 */
	public WaterStorageModel(double mintemp, double maxtemp, double chpPowerTh /* in W */) {
		super();
		this.mintemp = mintemp;
		this.maxtemp = maxtemp;
		this.energyperkelvin = BasicThermalModel.volume * BasicThermalModel.density * kJperkg2Jperkg(BasicThermalModel.heatingCapacity);
		this.gasHeatingGradient = 1.0 / energyperkelvin * chpPowerTh;
	}
	

	public INeededEnergy getNeededEnergy() {
		return neededEnergy.clone();
	}
	
	public void temperatureUpdate(long timestamp, double currentTemp, boolean chpRunning) {
		addLastValue(timestamp, currentTemp, chpRunning);
		double gradient = getGradient();
		
		// one value per minute
		int arraysize = (int) (NEEDEDENERGYTIME / 60);
		long[] minenergy = new long[arraysize];
		long[] maxenergy = new long[arraysize];
		
		if (mintemp > currentTemp + gradient * 60) {
			minenergy[0] = (long) ((mintemp - (currentTemp + gradient * 60)) * energyperkelvin);
		}
		else {
			minenergy[0] = 0;
		}
		
		maxenergy[0] = (long) ((maxtemp - (currentTemp + gradient * 60)) * energyperkelvin);
		
		if (maxenergy[0] < 0) maxenergy[0] = 0;
		for (int i = 1; i < arraysize; i++) {
			if (mintemp > currentTemp + gradient * 60 * i) {
				minenergy[i] = (long) (-gradient * 60 * energyperkelvin);
			}
			else {
				minenergy[i] = 0;
			}
			maxenergy[i] = (long) (-gradient * 60 * energyperkelvin);
		}
		
		neededEnergy = new NeededEnergyArray(timestamp, minenergy, maxenergy);
	}
	
	private void addLastValue(long now, double currentTemperature, boolean chpRunning) {
		lastValues.put(now, new TemperatureDataPoint(currentTemperature, chpRunning));
		
		long key;
		while ((key = lastValues.firstKey()) < now - HISTORYLENGTH) {
			lastValues.remove(key);
		}
	}
	
	private double getGradient() {
		if (lastValues.size() >= 2) {
			//calculate the gradient, but don't use values when the water gets hotter.
			//this algorithm isn't very intuitive but seems to work
			double lowerTempCorrection = 0.0;
			Entry<Long, TemperatureDataPoint> last = null;
			for (Entry<Long, TemperatureDataPoint> e : lastValues.entrySet()) {
				if (e.getValue().running && last != null) {
					long timediff = e.getKey() - last.getKey();
					lowerTempCorrection += timediff * gasHeatingGradient;
				}
				last = e;
			}
			
			Entry<Long, TemperatureDataPoint> firstEntry = lastValues.firstEntry(), lastEntry = lastValues.lastEntry();
			long timespan = lastEntry.getKey() - firstEntry.getKey();
			double temperatureDiff = (lastEntry.getValue().temperature - lowerTempCorrection) - firstEntry.getValue().temperature;
			double gradient = temperatureDiff / timespan;
			
//			System.out.println("###");
//			System.out.println(lastEntry.getValue().temperature);
//			System.out.println(lowerTempCorrection);
//			System.out.println(firstEntry.getValue().temperature);
//			System.out.println(temperatureDiff);
//			System.out.println(timespan);
//			System.out.println(gradient);
			
			return gradient;
		} 
		else {
			return 0.0;
		}
	}
	
	private double kJperkg2Jperkg(double kJperkg) {
		return kJperkg * 1000;
	}

}
