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

package osh.simulation;

import java.util.TreeMap;
import java.util.UUID;

import osh.configuration.OSHParameterCollection;
import osh.core.IOSH;
import osh.core.OSHRandomGenerator;
import osh.core.exceptions.OSHException;
import osh.simulation.exception.SimulationSubjectException;
import osh.simulation.screenplay.ScreenplayType;
import osh.utils.H0Profile;
import osh.utils.time.TimeConversion;


/**
 * 
 * @author Ingo Mauser
 *
 */
public abstract class AbstractApplianceSimulationDriver extends AbstractDeviceSimulationDriver {
	
	// screenplayType specific variables
		// screenplayType = DYNAMIC && DeviceClassification.APPLIANCE
		// all devices
		/** yearly consumption in Wh */
		private TreeMap<Integer,TreeMap<Integer,TreeMap<Double,Integer>>> probabilityMap;
		private double[][] dailyProbabilityCorrectionFactors;
		// screenplayType = DYNAMIC
		/** Max 1stTemporalDoF in ticks for generation of DoF */
		private int deviceMax1stDof;
		/** Max 2ndTemporalDoF in ticks for generation of DoF */
		private int deviceMax2ndDof;
		/** Number of avg daily runs for screenplay generation */
		private double avgYearlyRuns;
	

	/**
	 * CONSTRUCTOR
	 * 
	 * @throws SimulationSubjectException
	 */
	public AbstractApplianceSimulationDriver(
			IOSH controllerbox,
			UUID deviceID, 
			OSHParameterCollection driverConfig)
			throws SimulationSubjectException {
		super(controllerbox, deviceID, driverConfig);
		
		// if DeviceClassification.APPLIANCE (but info at this point not yet available!)
		// all conditions after first && should NOT be necessary (but remain for safety reasons)
		if ( driverConfig.getParameter("screenplaytype") != null ) {
			
			ScreenplayType screenplayType = ScreenplayType.fromValue(driverConfig.getParameter("screenplaytype"));
			
			if (screenplayType == ScreenplayType.STATIC) {
				// screenplay is loaded from file...
			}
			else if (screenplayType == ScreenplayType.DYNAMIC) {
				{
					// max 1st tDoF
					String deviceMax1stDofString = driverConfig.getParameter("devicemax1stdof");
					if (deviceMax1stDofString != null) {
						this.deviceMax1stDof = Integer.valueOf(deviceMax1stDofString);
					}
					else {
						throw new RuntimeException("variable \"screenplaytype\" = DYNAMIC : missing parameter (devicemax1stdof)!");
					}
				}
				{
					// average daily runs for dynamic daily screenplay
					String avgYearlyRunsString = driverConfig.getParameter("averageyearlyruns");
					if (avgYearlyRunsString != null) {
						this.avgYearlyRuns = Double.valueOf(avgYearlyRunsString);
					}
					else {
						throw new RuntimeException("Parameter missing: averageyearlyruns");
					}
				}
				{
					String h0ProfileSourceName = driverConfig.getParameter("h0filename");
					
					if (h0ProfileSourceName != null) {
						double dConsumptionShare = 0.1;
						int intYearlyElectricityConsumptionOfHousehold = 1000;
						
						H0Profile h0Profile = new H0Profile(
								TimeConversion.convertUnixTime2Year(getTimer().getUnixTime()),
								h0ProfileSourceName, 
								intYearlyElectricityConsumptionOfHousehold, 
								dConsumptionShare);
						
						this.probabilityMap = h0Profile.getProbabilityMap();
						this.dailyProbabilityCorrectionFactors = h0Profile.getDailyProbabilityCorrectionFactors();
						
					}
					else {
						throw new RuntimeException("variable \"screenplaytype\" = DYNAMIC : missing parameter!");
					}
				}
			}
			else {
				throw new RuntimeException("variable \"screenplaytype\" " + screenplayType + " : not implemented!");
			}
		}
	}
	
	
	/**
	 * Get random hour of day according to H0-based probability
	 * . Used for dynamic screenplay<br>
	 * . Probability is based on Probability density function f(x)= H0(day)- MIN(H0(day))
	 * @param unixTime
	 * @return
	 * @throws OSHException 
	 */
	private int getRandomHourToRunBasedOnProbabilityMap(long unixTime, OSHRandomGenerator randomGen) throws OSHException {
		double randomDouble = randomGen.getNextDouble();
		if (probabilityMap != null) {
			int hour = 0;
			int month = TimeConversion.convertUnixTime2MonthInt(unixTime);
			int weekday = TimeConversion.convertUnixTime2WeekdayInt(unixTime);
			int reducedWeekday = TimeConversion.convertWeekdayIntTo5Weekdays(weekday);
			Double temp = probabilityMap.get(month).get(reducedWeekday).lowerKey(randomDouble);
			if (temp == null) {
				hour = 0;
			}
			else {
				hour = probabilityMap.get(month).get(reducedWeekday).get(temp) + 1;
			}
			return hour;
		}
		else {
			throw new OSHException("probabilityMap == null");
		}
	}
	
	
	public long getRandomTimestampForRunToday(long unixTime, int middleOfPowerConsumption, double randomValue, OSHRandomGenerator randomGen) throws OSHException {
		int randomHour = getRandomHourToRunBasedOnProbabilityMap(unixTime, randomGen);
		
		double deviation = randomValue;
		if (randomGen.getNextBoolean()) {
			deviation = (-1) * deviation;
		}
		double deviatedHour = randomHour + 0.5 + deviation - (middleOfPowerConsumption / 3600.0);
		if (deviatedHour < 0) {
			deviatedHour = deviatedHour + 24;
		}
		else if (deviatedHour >= 24) {
			deviatedHour = deviatedHour - 24;
		}
		long startTime = (long) (getTimer().getUnixTime()  + deviatedHour * 3600.0);
		
		return startTime;
	}
	
	
	/**
	 * Get calculated dailyProbabilityCorrectionFactor<br>
	 * . Used for dynamic screenplay<br>
	 * . Days with higher consumption should have a higher probability for actions
	 * @param unixTime
	 * @return
	 */
	public double getDailyProbabilityCorrectionFactor(long unixTime) {
		int month = TimeConversion.convertUnixTime2MonthInt(unixTime);
		int weekday = TimeConversion.convertUnixTime2WeekdayInt(unixTime);
		int reducedWeekday = TimeConversion.convertWeekdayIntTo5Weekdays(weekday);
		double value = dailyProbabilityCorrectionFactors[month][reducedWeekday];
		return value;
	}
	
	/**
	 * has to be implemented by every device...
	 * @throws OSHException 
	 */
	protected abstract void generateDynamicDailyScreenplay() throws OSHException;

	/**
	 * in case of use please override
	 */
	protected void generateNewDof(boolean useRandomDof, int noActions, long applianceActionTimeTick, OSHRandomGenerator randomGen) {
		// currently NOTHING
	}

	@Override
	public final void triggerSubject() {
		super.triggerSubject();
		
		//if dynamic screenplay then generate daily screenplay
		if (getSimulationEngine().getScreenplayType() == ScreenplayType.DYNAMIC) {
			//FIXME: this can go very wrong, because the step size can be greater than 1
			if (getTimer().getUnixTime() % 86400 == 0) {
				try {
					generateDynamicDailyScreenplay();
				} catch (OSHException e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected int getDeviceMax1stDof() {
		return deviceMax1stDof;
	}

	protected int getDeviceMax2ndDof() {
		return deviceMax2ndDof;
	}

	protected double getAvgYearlyRuns() {
		return avgYearlyRuns;
	}

}
