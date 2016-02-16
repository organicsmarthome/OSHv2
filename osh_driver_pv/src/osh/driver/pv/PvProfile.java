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

package osh.driver.pv;

import osh.utils.csv.CSVImporter;
import osh.utils.time.TimeConversion;

/**
 * Resolution = 15 minutes (intervals)
 * @author Ingo Mauser
 */
public class PvProfile {

	//TODO standard deviation with randomness
	private double[] randomDay;
	
	/** [Month][Day(Mo,Tu/We/Th,Fr,Sa,Su)][Hour] */
	private int[][][] pvProfileArray;
	
	private int nominalPower;
	
	
	/**
	 * 
	 * @param pvProfileFilename
	 * @param nominalPower in W
	 */
	public PvProfile(String pvProfileFilename, int nominalPower, double[] randomDay) {
		this.nominalPower = nominalPower;
		init(pvProfileFilename, nominalPower);
		this.randomDay = randomDay;
	}
	
	private void init(String pvProfileFilename, int nominalPower) {
		double[][] pvProfileFile = CSVImporter.readDouble2DimArrayFromFile(pvProfileFilename, ";");
		pvProfileArray = new int[12][1][96];
		for (int j = 0; j < 96; j++) {
			for (int k = 0; k < 12; k++) {
				pvProfileArray[k][0][j] = (int) Math.round((pvProfileFile[j][k] * nominalPower));
			}
		}
	}
	
	/**
	 * IMPORTANT: Value <= 0 (Generating Power!)
	 * @param timeStamp
	 * @return Value <= 0 in W
	 */
	public int getPowerAt(long timeStamp) {
		int month = TimeConversion.convertUnixTime2MonthInt(timeStamp);
		int time = TimeConversion.convertUnixTime2SecondsSinceMidnight(timeStamp);
		
		// Do NOT use Math.round()!!!
		int interval = (int) ((double) time / (60 * 15));
		
		int power = pvProfileArray[month][0][interval];
		
//		// randomize
//		int day = (int) (timeStamp / 86400);
//		day = day % randomDay.length;
//		power = (int) (2 * power * randomDay[day]);
		
		// to be safe...
		if (power > 0) {
			power = (-1) * power;
		}
		
		return power;
	}
	
	public int getNominalPower() {
		return nominalPower;
	}
	
}
