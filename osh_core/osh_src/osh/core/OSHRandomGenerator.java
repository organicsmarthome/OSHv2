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

package osh.core;

import java.util.Random;

/**
 * 
 * @author Florian Allerding, Kaibin Bao, Till Schuberth, Ingo Mauser
 *
 */
public class OSHRandomGenerator {

	private Random random;

	public OSHRandomGenerator(Random random) {
		this.random = random;
	}

	public OSHRandomGenerator() {
		//see: Seeed ;-)
		this.random = new Random(0xd1ce5bL);
	}
	
	public synchronized int getNextInt(){
		return this.random.nextInt();
	}
	
	public synchronized int getNextInt(int max){
		return this.random.nextInt(max);
	}
	
	/**
	 * random.nextDouble()
	 * @return [0, 1)
	 */
	public synchronized double getNextDouble(){
		return this.random.nextDouble();
	}
	
	/**
	 * 
	 * @param max > 0
	 * @return
	 */
	public synchronized double getNextDouble(double max){
		return max * this.random.nextDouble();
	}
	
	public synchronized float getNextFloat(){
		return this.random.nextFloat();
	}
	
	public synchronized long getNextLong(){
		return this.random.nextLong();
	}
	
	public synchronized boolean getNextBoolean(){
		return this.random.nextBoolean();
	}
	
	public synchronized double getNextGaussian(){
		return this.random.nextGaussian();
	}
	
}
