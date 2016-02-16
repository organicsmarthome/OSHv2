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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author Florian Allerding, Till Schuberth
 *
 */
public class ThermalDemandData {
	
	private double [] HeatingDemandBed1 = new double [8760];
	private double [] HeatingDemandBed2 = new double [8760];
	private double [] HeatingDemandLiving1 = new double [8760];
	private double [] HeatingDemandLiving2 = new double [8760];
	private double [] HeatingDemandBath = new double [8760];
	private double [] hotWaterDemand = new double [8760];
	private double [] sumDemand= new double [8760];
	
	
	/**
	 * CONSTRUCTOR
	 * @param inputHeatingDevicesSourceFile
	 * @param inputHotWaterDemandSourceFile
	 * 
	 * @throws IOException
	 */
	public ThermalDemandData(
			String inputHeatingDevicesSourceFile, 
			String inputHotWaterDemandSourceFile) 
					throws IOException {
		
		FileReader fr = new FileReader(inputHeatingDevicesSourceFile);
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		String [] cell;
		
		for (int i = 0; (line = br.readLine()) != null; i++) {
			List<String> tmpcells = new ArrayList<String>(Arrays.asList(line.split("	")));
			List<String> tmpcells2 = new ArrayList<String>();
			List<String> cells = new ArrayList<String>();
			for (String s : tmpcells) {
				tmpcells2.addAll(Arrays.asList(s.split(" ")));
			}
			for (String s : tmpcells2) {
				if (!s.trim().isEmpty()) {
					if (s.startsWith("+")) s = s.substring(2);
					cells.add(s);
				}
			}
			cell = cells.toArray(new String[0]);
			
			if ( i > 1 ) {
				HeatingDemandBed1[i-2] = Double.parseDouble(cell[3]);
				HeatingDemandBed2[i-2] = Double.parseDouble(cell[4]);
				HeatingDemandLiving1[i-2] = Double.parseDouble(cell[5])/2;
				HeatingDemandLiving2[i-2] = Double.parseDouble(cell[5])/2;
				HeatingDemandBath[i-2] = Double.parseDouble(cell[6]);
				sumDemand[i-2] = 
						HeatingDemandBed1[i-2]
						+ HeatingDemandBed2[i-2]
						+ HeatingDemandLiving1[i-2]
						+ HeatingDemandLiving2[i-2]
						+ HeatingDemandBath[i-2];
			}
		}
		br.close();
		fr = new FileReader(inputHotWaterDemandSourceFile);
		br = new BufferedReader(fr);
		line="";
		
		for(int i=0; (line = br.readLine()) != null; i++) {
			if ( i > 0 ) {
				this.hotWaterDemand[i-1]=Double.parseDouble(line)/1000;
				sumDemand[i-1]=sumDemand[i-1]+this.hotWaterDemand[i-1];
			}	
		}
		br.close();
	}

	public double[] getHeatingDemandBed1() {
		return HeatingDemandBed1;
	}

	public double[] getHeatingDemandBed2() {
		return HeatingDemandBed2;
	}

	public double[] getHeatingDemandLiving1() {
		return HeatingDemandLiving1;
	}

	public double[] getHeatingDemandLiving2() {
		return HeatingDemandLiving2;
	}

	public double[] getHeatingDemandBath() {
		return HeatingDemandBath;
	}

	public double[] getHotWaterDemand() {
		return hotWaterDemand;
	}
	
	public double[] getThermalDemand(){	
		return sumDemand;
	}
	
}
