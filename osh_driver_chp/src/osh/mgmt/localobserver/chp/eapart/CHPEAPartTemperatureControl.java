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
import osh.mgmt.localobserver.chp.INeededEnergy;


/**
 * 
 * @author Florian Allerding, Till Schuberth
 *
 */
public class CHPEAPartTemperatureControl extends AbstractCHPEAPart {

	private class AdditionalInfo {
		@SuppressWarnings("unused")
		public int noForcedOffs;
		public int noForcedOns;
	}

	/**
	 * electrical power in W
	 */
	private final int electricalpower;
	
	/** 
	 * thermal power in W 
	 */
	private final int thermalpower;
	
	/**
	 * gas power in W
	 */
	private final int gasPower;
	
	private INeededEnergy neededEnergy;

	/** prediction horizon in seconds */
	public final static int HORIZON = 3 * 3600; // 3 hours
	/** minimum running time of CHP in seconds */
	public final static long MINRUNTIME = 300;
	private final static int BITSPERACTIVATION = 3;

	private long remainingRunningTime;
	private long now;

	
	/**
	 * CONSTRUCTOR
	 */
	public CHPEAPartTemperatureControl(
			UUID deviceId, 
			long now,
			int electricalpower, 
			int thermalpower,
			int gasPower,
			INeededEnergy neededEnergy, 
			long remainingRunningTime) {
		super(deviceId, now, getBitsForDachs(neededEnergy), HORIZON, true);

		this.electricalpower = electricalpower;
		this.thermalpower = thermalpower;
		this.gasPower = gasPower;
		this.neededEnergy = neededEnergy;
		this.remainingRunningTime = remainingRunningTime;
		this.now = now;
		if (remainingRunningTime < 0)
			throw new IllegalArgumentException(
					"remainingRunningTime is less than 0");
	}

	private static int getBitsForDachs(INeededEnergy neededEnergy) {
		return (int) (HORIZON / MINRUNTIME) * BITSPERACTIVATION;
	}

	private boolean[] getActivationBits(long now, BitSet solution,
			AdditionalInfo ai) {
		double thEnergyGenerated = 0.0;

		if (ai != null) {
			ai.noForcedOffs = 0;
			ai.noForcedOns = 0;
		}

		int bitcount = getBitsForDachs(neededEnergy);
		boolean ret[] = new boolean[bitcount / BITSPERACTIVATION];

		boolean laststate = false;

		for (int i = 0; i < bitcount; i += BITSPERACTIVATION) {
			boolean chpOn;
			long timeStartSlot = now + i * MINRUNTIME;
			long timeEndSlot = timeStartSlot + MINRUNTIME;
			double minenergy = neededEnergy.getNeededEnergy(timeEndSlot);
			double maxenergy = neededEnergy.getMaxUsableEnergy(timeEndSlot);
			long thEnergyPerMinRuntime = thermalpower * MINRUNTIME; // in Ws

			if (minenergy > thEnergyGenerated) {
				chpOn = true; // temperature below min temperature
				if (ai != null)
					ai.noForcedOns++;
			} 
			else if (maxenergy < thEnergyGenerated + thEnergyPerMinRuntime) {
				chpOn = false;
				if (ai != null)
					ai.noForcedOffs++;
			} 
			else {
				boolean anded = true, ored = false; // and / or
				for (int j = 0; j < BITSPERACTIVATION; j++) {
					anded &= solution.get(i + j);
					ored |= solution.get(i + j);
				}
				if (anded == false && ored == true) { // bits are not all equal
					chpOn = laststate; // keep last state
				} else {
					chpOn = solution.get(i); // all 1 -> on, all 0 -> off
				}
			}

			laststate = chpOn;
			if (chpOn)
				thEnergyGenerated += thEnergyPerMinRuntime;
			ret[i / BITSPERACTIVATION] = chpOn;
		}

		return ret;
	}

	@Override
	public Schedule getSchedule(BitSet solution) {
		SparseLoadProfile pr = new SparseLoadProfile();
		double cervisia = 0.0;
		
		long timeoffirstbit = now + remainingRunningTime;
		boolean laststate;
		boolean activationbits[] = getActivationBits(timeoffirstbit, solution,
				null);

		if (remainingRunningTime > 0) {
			pr.setLoad(Commodity.ACTIVEPOWER, now, -electricalpower);
			pr.setLoad(Commodity.NATURALGASPOWER, now, gasPower);
			laststate = true;
		} else {
			laststate = false;
		}
		for (int i = 0; i < activationbits.length; i++) {
			boolean chpOn = activationbits[i];
			long timeStartSlot = timeoffirstbit + i * MINRUNTIME;

			if (chpOn) {
				cervisia += 1 + 0.001 * (activationbits.length - i); // the later the better // TODO: really?
			}

			if (chpOn == true && laststate == false) {
				pr.setLoad(Commodity.ACTIVEPOWER, timeStartSlot + 30, -electricalpower);
				pr.setLoad(Commodity.NATURALGASPOWER, timeStartSlot + 30, gasPower);
				laststate = true;
				// cervisia += 0.05 * 1e2; //costs to turn on the CHP (not the
				// variable costs for letting the CHP run) (random value)
//				cervisia += 7.5;
				cervisia += 20;
			} 
			else if (chpOn == false && laststate == true) {
				pr.setLoad(Commodity.ACTIVEPOWER, timeStartSlot, 0);
				pr.setLoad(Commodity.NATURALGASPOWER, timeStartSlot, 0);
				laststate = false;
			}
		}
		return new Schedule(pr, cervisia);
	}

	@Override
	public CHPPhenotype transformToPhenotype(BitSet solution) {
		ArrayList<CHPActivation> starttimes = new ArrayList<CHPActivation>();

		long timeoffirstbit = now + remainingRunningTime;
		boolean[] activationBits = getActivationBits(timeoffirstbit, solution, null);

		CHPActivation currentactivation = null;

		if (remainingRunningTime > 0) {
			currentactivation = new CHPActivation();
			currentactivation.startTime = now;
			currentactivation.duration = remainingRunningTime;
		}

		for (int i = 0; i < activationBits.length; i++) {
			if (activationBits[i]) {
				// turn on
				if (currentactivation == null) {
					currentactivation = new CHPActivation();
					currentactivation.startTime = timeoffirstbit + i * MINRUNTIME;
					currentactivation.duration = MINRUNTIME;
				} 
				else {
					currentactivation.duration += MINRUNTIME;
				}
			} else {
				// turn off
				if (currentactivation != null) {
					starttimes.add(currentactivation);
					currentactivation = null;
				}
			}

		}

		if (currentactivation != null) {
			starttimes.add(currentactivation);
			currentactivation = null;
		}
		
		CHPPhenotype chpPhenotype = new CHPPhenotype();
		chpPhenotype.setList(starttimes);
		
		return chpPhenotype;
	}

	@Override
	public String problemToString() {
		AdditionalInfo ai = new AdditionalInfo();
		getActivationBits(now + remainingRunningTime, new BitSet(), ai);

		return "forced ons:" + ai.noForcedOns;
	}

	@Override
	public String solutionToString(BitSet bits) {
		return "chp solution";
	}

	@Override
	public void recalculateEncoding(long currentTime) {
		this.now = currentTime;
	}

}