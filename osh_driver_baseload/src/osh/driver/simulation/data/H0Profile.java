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

package osh.driver.simulation.data;

import java.util.TreeMap;

import osh.utils.csv.CSVImporter;
import osh.utils.time.TimeConversion;


/**
 * 
 * @author Ingo Mauser
 *
 */
public class H0Profile {
	
	private int year = 0;
	private String h0ProfileFileName; 
	private double yearlyKWhTotal;
	private double consumptionShare;
	
	
	/** [Month][Day(Mo,Tu/We/Th,Fr,Sa,Su)][Hour] in W */
	private double[][][] h0ProfileArray; //Wh
	private double[][][] h0ProfileArrayWithoutMin; //Wh

	private int[][][] finalReducedH0ProfileArray; //Wh
	
	private double[][][] reducedH0ProfileArrayWithoutMin; //Wh
	private double[][][] reducedH0ProfileArray; //Wh
	private double[][] dailyMinValue; //in W
	
	private double[][] dailyProbabilityCorrectionFactor;
	
	private double yearlyKWh = 0; // in kWh/a
	
	private int[] daysPerMonth;

	
	/**
	 * CONSTRUCTOR
	 * @param h0ProfileFileName
	 * @param yearlyKWhTotal
	 * @param consumptionShare
	 */
	public H0Profile(
			int year, 
			String h0ProfileFileName, 
			double yearlyKWhTotal, 
			double consumptionShare) {
		
		this.year = year;
		this.h0ProfileFileName = h0ProfileFileName;
		this.yearlyKWhTotal = yearlyKWhTotal;
		this.consumptionShare = consumptionShare;
		
		this.yearlyKWh = yearlyKWhTotal * consumptionShare;
		
		
		if ( ( (year % 4 == 0) && (year % 100 != 0) ) || (year % 400 == 0) ) {
			// leap year
			int[] daysPerMonth = {31,29,31,30,31,30,31,31,30,31,30,31};
			this.daysPerMonth = daysPerMonth;
		}
		else {
			// normal year
			int[] daysPerMonth = {31,28,31,30,31,30,31,31,30,31,30,31};
			this.daysPerMonth = daysPerMonth;
		}
		
		init(h0ProfileFileName, yearlyKWhTotal, consumptionShare);
	}
	
	
	private void init(String h0ProfileFileName, double yearlyKWh, double consumptionShare) {
		double[][] h0ProfileFile = CSVImporter.readDouble2DimArrayFromFile(h0ProfileFileName, ";");
		double[][][]h0ProfileArrayTemp = new double[12][5][24];
		double[][] averageDayTemp = new double[12][24];
		
		h0ProfileArray = new double[12][5][24];
		h0ProfileArrayWithoutMin = new double[12][5][24];
		finalReducedH0ProfileArray = new int[12][5][24];
		
		reducedH0ProfileArrayWithoutMin = new double[12][5][24];
		reducedH0ProfileArray = new double[12][5][24];
		dailyMinValue = new double[12][5];
		dailyProbabilityCorrectionFactor = new double[12][5];
		
		
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 24; j++) {
				for (int k = 0; k < 12; k++) {
					h0ProfileArrayTemp[k][i][j] = h0ProfileFile[24*i+j][k];
				}
			}
		}
		// calculate average day
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 24; j++) {
				for (int k = 0; k < 12; k++) {
					if (i != 1) {
						averageDayTemp[k][j] += h0ProfileArrayTemp[k][i][j] / 7;
					}
					else {
						// Tue, Wed, Thu
						averageDayTemp[k][j] += 3 * h0ProfileArrayTemp[k][i][j] / 7;
					}
				}
			}
		}
		// calculate yearly consumption in original profile
		double kwh = 0;
		for (int i = 0; i < averageDayTemp.length; i++) {
			for (int j = 0; j < averageDayTemp[i].length; j++) {
				kwh += averageDayTemp[i][j] * daysPerMonth[i];
			}
		}
		// calculate divisor for scaling
		double correctionDivisor = kwh / yearlyKWh;
		// scale new profile
		for (int i = 0; i < h0ProfileArrayTemp.length; i++) {
			for (int j = 0; j < h0ProfileArrayTemp[i].length; j++) {
				double min = Double.MAX_VALUE;
				for (int k = 0; k < h0ProfileArrayTemp[i][j].length; k++) {
					h0ProfileArray[i][j][k] = h0ProfileArrayTemp[i][j][k] * 1000 / correctionDivisor;
					min = Math.min(min, h0ProfileArray[i][j][k]);
				}
				dailyMinValue[i][j] = min;
				for (int k = 0; k < h0ProfileArrayTemp[i][j].length; k++) {
					h0ProfileArrayWithoutMin[i][j][k] = Math.max(0, h0ProfileArray[i][j][k] - dailyMinValue[i][j]);
				}
			}
		}
		
		//DEBUG
//		double testConsumption = 0;
//		for (int i = 0; i < 12; i++) {
//			for (int j = 0; j < 5; j++) {
//				for (int k = 0; k < 24; k++) {
//					if (j == 1) {
//						testConsumption += 3 * h0ProfileArray[i][j][k] * daysPerMonth[i] / 7;
//					}
//					else {
//						testConsumption += h0ProfileArray[i][j][k] * daysPerMonth[i] / 7;
//					}
//				}
//			}
//		}
		//DEBUG END
		
		// ### reduce by reduction ###
		double reduction = 1.0 - consumptionShare;
		if (reduction < 0) {
			reduction = 0;
		}
		// calculate daily sums
		double[][] dailySum = new double[12][5];
		for (int i = 0; i < h0ProfileArray.length; i++) {
			for (int j = 0; j < h0ProfileArray[i].length; j++) {
				for (int k = 0; k < h0ProfileArray[i][j].length; k++) {
					dailySum[i][j] += h0ProfileArray[i][j][k];
				}
			}
		}
		// calculate daily sums above min
		double[][] dailySumAboveDailyMin = new double[12][5];
		double yearlySumAboveDailyMins = 0;
		for (int i = 0; i < h0ProfileArrayWithoutMin.length; i++) {
			for (int j = 0; j < h0ProfileArrayWithoutMin[i].length; j++) {
				for (int k = 0; k < h0ProfileArrayWithoutMin[i][j].length; k++) {
					dailySumAboveDailyMin[i][j] += h0ProfileArrayWithoutMin[i][j][k];
				}
			}
		}
		// calculate average day sum
		double[] averageDailySumAboveMinTemp = new double[12];
		for (int i = 0; i < 12; i++) {
			for (int j = 0; j < 5; j++) {
				if (j != 1) {
					averageDailySumAboveMinTemp[i] += dailySumAboveDailyMin[i][j] / 7.0;
				}
				else {
					averageDailySumAboveMinTemp[i] += 3.0 * dailySumAboveDailyMin[i][j] / 7.0;
				}
			}
		}
		// calculate yearly sum above min
		for (int i = 0; i < averageDailySumAboveMinTemp.length; i++) {
			yearlySumAboveDailyMins += averageDailySumAboveMinTemp[i] * daysPerMonth[i];
		}
		// calculate probability correction factor (some days have higher probability for runs of devices)
		for (int i = 0; i < dailyProbabilityCorrectionFactor.length; i++) {
			for (int j = 0; j < dailyProbabilityCorrectionFactor[i].length; j++) {
				dailyProbabilityCorrectionFactor[i][j] = dailySumAboveDailyMin[i][j] * 365.0 / yearlySumAboveDailyMins;

			}
		}
		
		//TODO UNKNOWN REASON FIX
		for (int i = 0; i < dailyProbabilityCorrectionFactor.length; i++) {
			for (int j = 0; j < dailyProbabilityCorrectionFactor[i].length; j++) {
				double tempFIX = 0;
				for (int k = 0; k < 5; k++) {
					if (k != 1) {
						tempFIX += dailyProbabilityCorrectionFactor[i][k] / 7.0;
					}
					else {
						tempFIX += 3.0 * dailyProbabilityCorrectionFactor[i][k] / 7.0;
					}
				}
				double correc = 0;
				if (i==0) {
//					correc = tempFIX / 1.222841549;
					correc = tempFIX / 1.198786141;
				}
				else if (i==1) {
//					correc = tempFIX / 1.072109647;
					correc = tempFIX / 1.163628617;
				}
				else if (i==2) {
//					correc = tempFIX / 1.112723687;
					correc = tempFIX / 1.090834488;
				}
				else if (i==3) {
//					correc = tempFIX / 1.005536731;
					correc = tempFIX / 1.018614621;
				}
				else if (i==4) {
//					correc = tempFIX / 0.938523966;
					correc = tempFIX / 0.920061577;
				}
				else if (i==5) {
//					correc = tempFIX / 0.857919114;
					correc = tempFIX / 0.869077107;
				}
				else if (i==6) {
//					correc = tempFIX / 0.836896216;
					correc = tempFIX / 0.82043302;
				}
				else if (i==7) {
//					correc = tempFIX / 0.852956922;
					correc = tempFIX / 0.836177785;
				}
				else if (i==8) {
//					correc = tempFIX / 0.879259443;
					correc = tempFIX / 0.890694986;
				}
				else if (i==9) {
//					correc = tempFIX / 0.996472625;
					correc = tempFIX / 0.976870285;
				}
				else if (i==10) {
//					correc = tempFIX / 1.035147682;
					correc = tempFIX / 1.048610688;
				}
				else if (i==11) {
//					correc = tempFIX / 1.189612418;
					correc = tempFIX / 1.166210684;
				}
				dailyProbabilityCorrectionFactor[i][j] = dailyProbabilityCorrectionFactor[i][j] / correc;
				
			}
		}
		
		//TODO UNKNOWN REASON FIX
		double sum = 0;
		for (int i = 0; i < 365; i++) {
			sum += this.getDailyProbabilityCorrectionFactor(i * 86400);
		}

		//TODO UNKNOWN REASON FIX
		for (int i = 0; i < dailyProbabilityCorrectionFactor.length; i++) {
			for (int j = 0; j < dailyProbabilityCorrectionFactor[i].length; j++) {
				dailyProbabilityCorrectionFactor[i][j] = dailyProbabilityCorrectionFactor[i][j] * 365.0 / sum;
			}
		}
		
//		//DEBUG
//		double sum2 = 0;
//		for (int i = 0; i < 365; i++) {
//			sum2 += this.getDailyProbabilityCorrectionFactor(i * 86400);
//		}
//		//DEBUG END
		
		// reduction depends on ratio of sum below and sum above daily minimum
		double[][] dailyReductionFactor = new double[12][5];
		
//		//DEBUG
//		double testFactor = 0;
//		double[] testFactor2 = new double[12];
//		for (int i = 0; i < dailyProbabilityCorrectionFactor.length; i++) {
//			for (int j = 0; j < dailyProbabilityCorrectionFactor[i].length; j++) {
//				if (j == 1) {
//					testFactor += dailyProbabilityCorrectionFactor[i][j] / 7 / 12;
//					testFactor += dailyProbabilityCorrectionFactor[i][j] / 7 / 12;
//					testFactor += dailyProbabilityCorrectionFactor[i][j] / 7 / 12;
//					testFactor2[i] += dailyProbabilityCorrectionFactor[i][j] / 7;
//					testFactor2[i] += dailyProbabilityCorrectionFactor[i][j] / 7;
//					testFactor2[i] += dailyProbabilityCorrectionFactor[i][j] / 7;
//				}
//				else {
//					testFactor += dailyProbabilityCorrectionFactor[i][j] / 7 / 12;
//					testFactor2[i] += dailyProbabilityCorrectionFactor[i][j] / 7;
//				}
//			}
//		}
		
		for (int i = 0; i < dailySum.length; i++) {
			for (int j = 0; j < dailySum[i].length; j++) {
				dailyReductionFactor[i][j] = reduction * dailySum[i][j] / dailySumAboveDailyMin[i][j];
			}
		}
		// finally reduced array
		for (int i = 0; i < h0ProfileArrayWithoutMin.length; i++) {
			for (int j = 0; j < h0ProfileArrayWithoutMin[i].length; j++) {
				for (int k = 0; k < h0ProfileArrayWithoutMin[i][j].length; k++) {
					reducedH0ProfileArrayWithoutMin[i][j][k] = ((1 - dailyReductionFactor[i][j]) * h0ProfileArrayWithoutMin[i][j][k]);
					reducedH0ProfileArray[i][j][k] = reducedH0ProfileArrayWithoutMin[i][j][k] + dailyMinValue[i][j];
					finalReducedH0ProfileArray[i][j][k] = Math.max(0, (int) Math.round(reducedH0ProfileArray[i][j][k]));
				}
			}
		}

	}
	
	public double[][] getDailyProbabilityCorrectionFactors() {
		return dailyProbabilityCorrectionFactor;
	}
	
	public double getDailyProbabilityCorrectionFactor(long unixTime) {
		int month = TimeConversion.convertUnixTime2MonthInt(unixTime);
		int weekday = TimeConversion.convertUnixTime2WeekdayInt(unixTime);
		int reducedWeekday = TimeConversion.convertWeekdayIntTo5Weekdays(weekday);
		double value = dailyProbabilityCorrectionFactor[month][reducedWeekday];
		return value;
	}
	

	public TreeMap<Integer,TreeMap<Integer,TreeMap<Double,Integer>>> getProbabilityMap() {
		double[][][] diffTemp = new double[12][5][24]; //Wh
		double[][] dailySumTemp = new double[12][5];
		double[][][] normalizedDiffTemp = new double[12][5][24];
		double[][][] distributionFunction = new double[12][5][24];
		
		// calc diff between hour value and daily min
		// calc daily sum above daily min
		for (int i = 0; i < h0ProfileArrayWithoutMin.length; i++) {
			for (int j = 0; j < h0ProfileArrayWithoutMin[i].length; j++) {
				for (int k = 0; k < h0ProfileArrayWithoutMin[i][j].length; k++) {
					diffTemp[i][j][k] = h0ProfileArrayWithoutMin[i][j][k] - reducedH0ProfileArrayWithoutMin[i][j][k];
					dailySumTemp[i][j] += diffTemp[i][j][k];
				}
			}
		}
		// calc daily distribution function
		for (int i = 0; i < h0ProfileArrayWithoutMin.length; i++) {
			for (int j = 0; j < h0ProfileArrayWithoutMin[i].length; j++) {
				double temp = 0;
				for (int k = 0; k < h0ProfileArrayWithoutMin[i][j].length; k++) {
					normalizedDiffTemp[i][j][k] = diffTemp[i][j][k] / dailySumTemp[i][j];
					temp += normalizedDiffTemp[i][j][k];
					distributionFunction[i][j][k] = temp;
				}
			}
		}
		
		//fill tree maps
		TreeMap<Integer,TreeMap<Integer,TreeMap<Double,Integer>>> map1 = new TreeMap<Integer,TreeMap<Integer,TreeMap<Double,Integer>>>();
		for (int i = 0; i < distributionFunction.length; i++) {
			TreeMap<Integer,TreeMap<Double,Integer>> map2 = new TreeMap<Integer,TreeMap<Double,Integer>>();
			for (int j = 0; j < distributionFunction[i].length; j++) {
				TreeMap<Double,Integer> map3 = new TreeMap<Double,Integer>();
				double lastKey = 0;
				for (int k = 0; k < distributionFunction[i][j].length; k++) {
					if (k < distributionFunction[i][j].length - 1
							&& lastKey < distributionFunction[i][j][k]) {
						if (distributionFunction[i][j][k] > 1) {
							distributionFunction[i][j][k] = 1;
						}
						map3.put(distributionFunction[i][j][k], k);
						lastKey = distributionFunction[i][j][k];
					}
					else if (k == distributionFunction[i][j].length - 1
							&& lastKey < distributionFunction[i][j][k]) {
						map3.put(Double.valueOf(1), k);
					}
				}
				map2.put(j, map3);
			}
			map1.put(i, map2);
		}
		
		return map1;
	}
	
	public int getActivePowerAt(long timeStamp) {
		int month = TimeConversion.convertUnixTime2MonthInt(timeStamp);
		int weekday = TimeConversion.convertUnixTime2WeekdayInt(timeStamp);
		int time = TimeConversion.convertUnixTime2SecondsSinceMidnight(timeStamp);
		int hour = (int) ((double) time / (60.0 * 60.0));
		int i = TimeConversion.convertWeekdayIntTo5Weekdays(weekday);
		
		return finalReducedH0ProfileArray[month][i][hour];
	}
	
	public double getDoubleActivePowerAt(long timeStamp) {
		int month = TimeConversion.convertUnixTime2MonthInt(timeStamp);
		int weekday = TimeConversion.convertUnixTime2WeekdayInt(timeStamp);
		int time = TimeConversion.convertUnixTime2SecondsSinceMidnight(timeStamp);
		int hour = (int) ((double) time / (60.0 * 60.0));
		int i = TimeConversion.convertWeekdayIntTo5Weekdays(weekday);
		
		return reducedH0ProfileArray[month][i][hour];
	}
	
	public int getYearlyKWh() {
//		return yearlyKWh;
		return (int) Math.round(yearlyKWh);
	}
	
	public double[][][] getH0ProfileArrayWithoutMin() {
		return h0ProfileArrayWithoutMin;
	}
	
	public double getProbabilityAtHour(long timeStamp) {
		int month = TimeConversion.convertUnixTime2MonthInt(timeStamp);
		int weekday = TimeConversion.convertUnixTime2WeekdayInt(timeStamp);
		int time = TimeConversion.convertUnixTime2SecondsSinceMidnight(timeStamp);
		int hour = (int) ((double) time / 3600.0);
		int convertedDay = TimeConversion.convertWeekdayIntTo5Weekdays(weekday);
		
		double sum = 0;
		for (int i = 0; i < 24; i++) {
			sum += h0ProfileArrayWithoutMin[month][convertedDay][i];
		}
		return h0ProfileArrayWithoutMin[month][convertedDay][hour] / sum;
	}
	
	public double getPercentOfDailyMaxWithoutDailyMin(long timeStamp) {
		int month = TimeConversion.convertUnixTime2MonthInt(timeStamp);
		int weekday = TimeConversion.convertUnixTime2WeekdayInt(timeStamp);
		int time = TimeConversion.convertUnixTime2SecondsSinceMidnight(timeStamp);
		int hour = (int) ((double) time / 3600.0);
		int convertedDay = TimeConversion.convertWeekdayIntTo5Weekdays(weekday);
		
		double max = 0;
		for (int i = 0; i < 24; i++) {
			max = Math.max(max, h0ProfileArrayWithoutMin[month][convertedDay][i]);
		}
		return h0ProfileArrayWithoutMin[month][convertedDay][hour] / max;
	}
	
	public double getAvgPercentOfDailyMaxWithoutDailyMin(long timestamp) {
		int month = TimeConversion.convertUnixTime2MonthInt(timestamp);
		int weekday = TimeConversion.convertUnixTime2WeekdayInt(timestamp);
		int convertedWeekday = TimeConversion.convertWeekdayIntTo5Weekdays(weekday);
		
		double max = 0;
		for (int i = 0; i < 24; i++) {
			max = Math.max(max, h0ProfileArrayWithoutMin[month][convertedWeekday][i]);
		}
		
		double avgPercent = 0;
		for (int i = 0; i < 24; i++) {
			avgPercent += h0ProfileArrayWithoutMin[month][convertedWeekday][i] / max / 24;
		}
		
		return avgPercent;
	}
	
	public double getPercentOfDailyConsumption(long timestamp) {
		int month = TimeConversion.convertUnixTime2MonthInt(timestamp);
		int weekday = TimeConversion.convertUnixTime2WeekdayInt(timestamp);
		int convertedWeekday = TimeConversion.convertWeekdayIntTo5Weekdays(weekday);
		int time = TimeConversion.convertUnixTime2SecondsSinceMidnight(timestamp);
		int hour = (int) ((double) time / 3600.0);
		// calculate daily sums
		double dailySum = 0;
		for (int i = 0; i < h0ProfileArray[month][convertedWeekday].length; i++) {
			dailySum += h0ProfileArray[month][convertedWeekday][i];
		}
		return h0ProfileArray[month][convertedWeekday][hour] / dailySum;
	}


	public int getYear() {
		return year;
	}
	
	
	public String getH0ProfileFileName() {
		return h0ProfileFileName;
	}


	public double getYearlyKWhTotal() {
		return yearlyKWhTotal;
	}


	public double getConsumptionShare() {
		return consumptionShare;
	}
	
}
