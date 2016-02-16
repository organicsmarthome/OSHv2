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

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.UUID;

import osh.hal.exchange.HALObserverExchange;


/**
 * 
 * @author Florian Allerding, Ingo Mauser
 *
 */
public class HeatingSystemData extends HALObserverExchange {
	
	private double powerTh; // kW
	private double powerEL; // kW
	private double powerAux;
	private int powerGas;   // W
	private double efficiencyTh;
	private double efficiencyEl;
	private int minOperationTime;
	private double minTemp;
	private double maxTemp;
	private double startTemp;
	
	
	/**
	 * CONSTRUCTOR
	 * @param deviceID
	 * @param timestamp
	 */
	public HeatingSystemData(UUID deviceID, Long timestamp) {
		super(deviceID, timestamp);
	}
	
	
	/** 
	 * Unit: kW
	 * @return
	 */
	public double getPowerTh() {
		return powerTh;
	}

	/**
	 * Unit: kW
	 * @return
	 */
	public double getPowerEL() {
		return powerEL;
	}
	
	/**
	 * Unit: W
	 * @return
	 */
	public int getPowerGas() {
		return powerGas;
	}

	public int getMinoperationtime() {
		return minOperationTime;
	}

	public double getPoweraux() {
		return powerAux;
	}

	public double getMintemp() {
		return minTemp;
	}

	public double getMaxtemp() {
		return maxTemp;
	}

	public double getEfficiencyth() {
		return efficiencyTh;
	}

	public double getEfficiency() {
		return efficiencyEl+efficiencyTh;
	}

	public double getEfficiencyel() {
		return efficiencyEl;
	}
	
	public void initConfig(String configFilePath) throws Exception {
		FileReader fr = new FileReader(configFilePath);
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		
		while ( (line = br.readLine()) != null ) {
			if ( line.startsWith("heating power") ){
				powerTh = Double.parseDouble(line.substring(line.indexOf('=') + 1));	    	
			}
			else if(line.startsWith("heating efficiency")){
				efficiencyTh = Double.parseDouble(line.substring(line.indexOf('=')+1));	 
			}
			else if(line.startsWith("electric power")){
				powerEL = Double.parseDouble(line.substring(line.indexOf('=')+1));	
			}
			else if(line.startsWith("electric efficiency")){
				efficiencyEl = Double.parseDouble(line.substring(line.indexOf('=')+1));	    
			}
			else if(line.startsWith("minimum operation time")){
				minOperationTime = Integer.parseInt(line.substring(line.indexOf('=')+1));	
			}
			else if(line.startsWith("minimum temp")){
				minTemp = Double.parseDouble(line.substring(line.indexOf('=')+1));	    
			}
			else if(line.startsWith("maximum temp")){
				maxTemp = Double.parseDouble(line.substring(line.indexOf('=')+1));	   
			}
			else if(line.startsWith("aux")){
				powerAux = Double.parseDouble(line.substring(line.indexOf('=')+1));	 
			}
			else if(line.startsWith("start temp")){
				startTemp = Double.parseDouble(line.substring(line.indexOf('=')+1));	
			}
			else if(line.startsWith("gas power in W")){
				powerGas = Integer.parseInt(line.substring(line.indexOf('=')+1));	
			}
		}
		
		br.close();
	}

	public double getStartTemp() {
		return startTemp;
	}
	
}
