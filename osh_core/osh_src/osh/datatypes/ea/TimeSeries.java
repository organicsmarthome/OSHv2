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

package osh.datatypes.ea;

/**
 * Represents a discrete function mapping from time domain to real values.
 * The domain of the function ranges from 0 to length().
 * Function value outside the domain is implicitly considered as 0.0.
 * 
 */
public interface TimeSeries {
	/**
	 * returns function value at specified time
	 * 
	 * @param time
	 * @return
	 */
	public double get(long time);
	
	/**
	 * finds the next time the value changes
	 * 
	 * @param time
	 * @return
	 */
	public long getNextChange(long time);
	
	/**
	 * sets one function value at the specified time 
	 * 
	 */
	public void set(long time, double value);
	
	/**
	 * 
	 * @return domain of function / time series
	 */
	public long length();

	/**
	 * Augments the length of the time series (crops or fill series with 0.0)
	 * 
	 */
	public void setLength(long newLength);
	
	/**
	 * Function addition of two time series.
	 * Result stored in this object.
	 * Operand is shifted by offset first before operation. 
	 * 
	 * @param operand
	 * @param offset
	 */
	public void add(TimeSeries operand, long offset);
	
	/**
	 * Function subtraction of two time series.
	 * Result stored in this object.
	 * Operand is shifted by offset first before operation. 
	 * 
	 * @param operand
	 * @param offset
	 */
	public void sub(TimeSeries operand, long offset);
	
	/**
	 * Function multiplication of two time series.
	 * Result stored in this object 
	 * Operand is shifted by offset first before operation. 
	 * 
	 * @param operand
	 * @param offset
	 */
	public void multiply(TimeSeries operand, long offset);
	
	/**
	 * calculates discrete integral (sum) between two times
	 * 
	 * @param from
	 * @param to
	 */
	public double sum(long from, long to);
	
	/**
	 * calculates discrete integral (sum) between two times, but only for positive values
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public double sumPositive(long from, long to);

	/**
	 * calculates discrete integral (sum) between two times, but only for negative values
	 * 
	 * @param from
	 * @return
	 */
	public double sumNegative(long from, long to);
	
	public TimeSeries cloneMe();
}
