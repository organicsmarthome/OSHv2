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

import java.util.Arrays;

/**
 * Dense time series - use it for device profiles
 * 
 */
public class ArrayTimeSeries implements TimeSeries {

	protected double[] values;

	public ArrayTimeSeries() {
		values = new double[0];
	}
	
	@Override
	public double get(long time) {
		if( time < length() )
			return values[(int) time];
		else
			throw new ArrayIndexOutOfBoundsException();
	}

	@Override
	public void add(TimeSeries operand, long offset) {
		long newlength = Math.max(this.length(), offset+operand.length());
		double[] newarray = Arrays.copyOf(values, (int) newlength);

		long maxIdx = Math.min(this.length(), offset+operand.length());
		for( long i = Math.max(0, offset); i < maxIdx; i++ ) {
			newarray[(int) i] += operand.get((int) (i-offset));
		}
		
		values = newarray;
	}

	@Override
	public void sub(TimeSeries operand, long offset) {
		long newlength = Math.max(this.length(), offset+operand.length());
		double[] newarray = Arrays.copyOf(values, (int) newlength);

		long maxIdx = Math.min(this.length(), offset+operand.length());
		for( long i = Math.max(0, offset); i < maxIdx; i++ ) {
			newarray[(int) i] -= operand.get((int) (i-offset));
		}
		
		values = newarray;
	}
	
	@Override
	public void multiply(TimeSeries operand, long offset) {
		long newlength = Math.max(this.length(), offset+operand.length());
		double[] newarray = Arrays.copyOf(values, (int) newlength);

		for( long i = 0; i < newlength; i++ ) {
			if( i < this.length() ) {
				if( i-offset >= 0 && i-offset < operand.length() ) {
					newarray[(int) i] = this.get(i) * operand.get(i-offset);
				} else {
					newarray[(int) i] = 0.0;
				}
			} else {
				newarray[(int) i] = 0.0;
			}
		}
		
		values = newarray;
	}

	@Override
	public double sum(long from, long to) {
		double s = 0.0;
		
		for( long i = from; i < to; i++ ) {
			s += values[(int) i];
		}
		
		return s;
	}

	@Override
	public long length() {
		return values.length;
	}

	@Override
	public void set(long time, double value) {
		if( time < length() )
			values[(int) time] = value;
		else
			throw new ArrayIndexOutOfBoundsException();
	}

	@Override
	public void setLength(long newLength) {
		double[] newarray = Arrays.copyOf(values, (int) newLength);
		
		values = newarray;
	}

	@Override
	public long getNextChange(long time) {
		double val = get(time);
		
		for( long i = time+1; i < length(); i++ ) {
			if( get(i) != val )
				return i;
		}
			
		return -1;
	}

	@Override
	public double sumPositive(long from, long to) {
		double s = 0.0;
		
		for( long i = from; i < to; i++ ) {
			double v = values[(int) i]; 
			if( v > 0.0 )
				s += v;
		}
		
		return s;
	}

	@Override
	public double sumNegative(long from, long to) {
		double s = 0.0;
		
		for( long i = from; i < to; i++ ) {
			double v = values[(int) i]; 
			if( v < 0.0 )
				s += v;
		}
		
		return s;
	}

	@Override
	public TimeSeries cloneMe() {
		ArrayTimeSeries ats = new ArrayTimeSeries();
		
		ats.values = Arrays.copyOf(values, values.length);
		
		return ats;
	}
}
