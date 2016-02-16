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

package osh.mgmt.localobserver.chp.eapart;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.UUID;

import osh.datatypes.Commodity;
import osh.datatypes.appliance.SparseLoadProfile;
import osh.datatypes.ea.Schedule;
import osh.mgmt.localobserver.chp.CHPActivation;


//current time horizon: 10 * 30 min = 5 hours

/**
 * 
 * @author Florian Allerding
 *
 */
public class CHPEAPartFixedRaster extends AbstractCHPEAPart {

	private long now;
	private int power;
	
	
	/**
	 * CONSTRUCTOR
	 */
	public CHPEAPartFixedRaster(UUID deviceId, long timestamp, int power, long now) {
		super(deviceId, timestamp, 10, 10 * 30 * 60, true);
		
		this.power = power;
		this.now = now;
	}

	
	@Override
	public Schedule getSchedule(BitSet solution) {
		double cervisia = 0.0;
		boolean laststate = false;
		SparseLoadProfile pr = new SparseLoadProfile();
		pr.setEndingTimeOfProfile(10 * 30 * 60);
		
		for (int i = 0; i < 10; i++) {
			long begin = now + i * 30 * 60;

			if (solution.get(i)) {
				cervisia += 0.01; //random value
			}
			
			if (solution.get(i) && laststate == false) {
				pr.setLoad(Commodity.ACTIVEPOWER, begin + 30, -power);
				laststate = true;
			} else if (!solution.get(i) && laststate == true) {
				pr.setLoad(Commodity.ACTIVEPOWER, begin, 0);
				laststate = false;
			}
		}
		return new Schedule(pr, cervisia);
	}
	
	@Override
	public String problemToString() {
		//TODO: return description of problem
		return "dachs problem: ";
	}

	@Override
	public String solutionToString(BitSet bits) {
		//TODO: return description of solution
		return "dachs solution";
	}

	@Override
	public void recalculateEncoding(long currentTime) {
		this.now = currentTime;
	}

	@Override
	public CHPPhenotype transformToPhenotype(BitSet solution) {
		ArrayList<CHPActivation> starttimes = new ArrayList<CHPActivation>();
		
		CHPActivation currentactivation = null;
		for (int i = 0; i < 10; i++) {
			if (solution.get(i)) {
				//turn on
				if (currentactivation == null) {
					currentactivation = new CHPActivation();
					currentactivation.startTime = now + i * 30 * 60;
					currentactivation.duration = 30 * 60;
				} else {
					currentactivation.duration += 30 * 60;
				}
			} else {
				//turn off
				if (currentactivation != null) {
					starttimes.add(currentactivation);
					currentactivation = null;
				}
			}
		}
		
		//turn off after last entry
		if (currentactivation != null) {
			starttimes.add(currentactivation);
			currentactivation = null;
		}
		
		CHPPhenotype phenotype = new CHPPhenotype();
		phenotype.setList(starttimes);
		
		return phenotype;
	}
		
}