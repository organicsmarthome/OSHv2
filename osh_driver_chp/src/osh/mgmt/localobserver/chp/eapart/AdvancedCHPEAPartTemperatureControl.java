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
 * @author Florian Allerding, Till Schuberth, Ingo Mauser
 *
 */
public class AdvancedCHPEAPartTemperatureControl extends AbstractCHPEAPart {

	private class AdditionalInfo {
		public int noForcedOffs;
		public int noForcedOns;
	}
	
	// VARIABLES 
	private long remainingRunningTime;
	private long referenceTime;

	// information about the outcome of the thermal model
	private INeededEnergy neededEnergy;

	// CONSTANTS
	
	// CHP
	
	/** electrical power in W */
	private final int ELECTRICALPOWER;
	
	/** thermal power in W */
	private final int THERMALPOWER;
	
	/** gas power in W */
	private final int GASPOWER;
	
	// EAP
	
	/** prediction horizon in seconds */
	public final static int HORIZON = 3 * 3600; // 3 hours
	
	/** duration of a time slot */
	public final static long SLOT_DURATION = 300; // >= 60 (!)
	
	/** minimum running time of CHP in seconds */
	public final static long MIN_RUNTIME = 1800;
	
	private final static int BITSPERACTIVATION = 3;

	
	/**
	 * CONSTRUCTOR
	 * @param deviceId
	 * @param now
	 * @param electricalpower
	 * @param thermalpower
	 * @param neededEnergy
	 * @param remainingRunningTime
	 */
	public AdvancedCHPEAPartTemperatureControl(
			UUID deviceId, 
			long now,
			int electricalpower, 
			int thermalpower,
			int gasPower,
			INeededEnergy neededEnergy, 
			long remainingRunningTime) {
		super(deviceId, now, getBitsForDachs(), now + HORIZON, false);

		this.ELECTRICALPOWER = electricalpower;
		this.THERMALPOWER = thermalpower;
		this.GASPOWER = gasPower;
		this.neededEnergy = neededEnergy;
		this.remainingRunningTime = remainingRunningTime;
		this.referenceTime = now;
		
		if (remainingRunningTime < 0) {
			throw new IllegalArgumentException("remainingRunningTime is less than 0");
		}
		
	}

	
	private static int getBitsForDachs() {
		return (int) (HORIZON / SLOT_DURATION) * BITSPERACTIVATION;
	}

	private boolean[] getActivationBits(long now, BitSet solution, AdditionalInfo ai) {
		double thEnergyGenerated = 0.0;

		if (ai != null) {
			ai.noForcedOffs = 0;
			ai.noForcedOns = 0;
		}

		int bitcount = getBitsForDachs();
		boolean ret[] = new boolean[bitcount / BITSPERACTIVATION];

		boolean laststate = false;

		for (int i = 0; i < bitcount; i += BITSPERACTIVATION) {
			
			boolean chpOn;
			long timeStartSlot = now + i * SLOT_DURATION;
			long timeEndSlot = timeStartSlot + SLOT_DURATION;
			
			double minenergy = neededEnergy.getNeededEnergy(timeEndSlot);
			double maxenergy = neededEnergy.getMaxUsableEnergy(timeEndSlot);
			long thEnergyPerMinRuntime = THERMALPOWER * SLOT_DURATION; // in Ws

			// check: force on
			if (minenergy > thEnergyGenerated) {
				chpOn = true; // temperature below min temperature
				
				if (ai != null) {
					ai.noForcedOns++;
				}
			}
			// check: force off
			else if (maxenergy < thEnergyGenerated + thEnergyPerMinRuntime) {
				chpOn = false;
				
				if (ai != null) {
					ai.noForcedOffs++;
				}
			} 
			else {
				boolean anded = true, ored = false; // and / or
				for (int j = 0; j < BITSPERACTIVATION; j++) {
					anded &= solution.get(i + j);
					ored |= solution.get(i + j);
				}
				
				if (anded == false && ored == true) { // bits are not all equal
					chpOn = laststate; // keep last state
				} 
				else {
					chpOn = solution.get(i); // all 1 -> on, all 0 -> off
				}
			}
			
			// consider min runtime
			if (remainingRunningTime > 0) {
				int remainingSlots = (int) Math.ceil((double) remainingRunningTime / SLOT_DURATION);
				
				int currentRetBit = (int) (i / BITSPERACTIVATION);
				
				if (currentRetBit < remainingSlots) {
					chpOn = true;
				}
				else {
					if (currentRetBit > remainingSlots + 1) {
						if (chpOn == false && laststate == true) {
							// check min runtime
							int minSlots = (int) Math.ceil((double) MIN_RUNTIME / SLOT_DURATION);
							int count = 0;
							for (int x = 1; x < minSlots + 1; x++) {
								
								if (currentRetBit - x < 0) {
									count = minSlots; // running since the beginning of slot (with CHP has been on)
									break;
								}
								
								if (ret[currentRetBit - x] == true) {
									count++; // was on in this slot
								}
								else  {
									break; // was off. stop counting.
								}
							}
							if (count < minSlots) {
								chpOn = true;
							}
						}
					}
				}
			}
			else {
				if (chpOn == false && laststate == true) {
					int minSlots = (int) Math.ceil(MIN_RUNTIME / SLOT_DURATION);
					int currentRetBit = (int) (i / BITSPERACTIVATION);
					
					if (currentRetBit < minSlots) {
						chpOn = true;
					}
					else {
						//check min runtime
						int count = 0;
						for (int x = 0; x < minSlots; x++) {
							if (ret[currentRetBit - x - 1] == true) {
								count++;
							}
						}
						if (count < minSlots) {
							chpOn = true;
						}
						else {
							// there are enough slots with activation
						}
					}
				}
			}
			
			laststate = chpOn;
			
			if (chpOn) {
				thEnergyGenerated += thEnergyPerMinRuntime;
			}
				
			ret[i / BITSPERACTIVATION] = chpOn;
		}

		return ret;
	}

	@Override
	public Schedule getSchedule(BitSet solution) {
		SparseLoadProfile pr = new SparseLoadProfile();
		double cervisia = 0.0;
		
		long timeoffirstbit = referenceTime + remainingRunningTime;
		boolean laststate;
		boolean activationbits[] = getActivationBits(timeoffirstbit, solution, null);

		if (remainingRunningTime > 0) {
			pr.setLoad(Commodity.ACTIVEPOWER, referenceTime, -ELECTRICALPOWER);
			pr.setLoad(Commodity.NATURALGASPOWER, referenceTime, GASPOWER);
			pr.setLoad(Commodity.WARMWATERPOWER, referenceTime, -THERMALPOWER);
			laststate = true;
		} else {
			laststate = false;
		}
		for (int i = 0; i < activationbits.length; i++) {
			boolean chpOn = activationbits[i];
			long timeStartSlot = timeoffirstbit + i * SLOT_DURATION;

			if (chpOn) {
//				cervisia += 1 + 0.001 * (activationbits.length - i); // the later the better
			}
				
			if (chpOn == true && laststate == false) {
				// !: 60 seconds to get electric power...
				pr.setLoad(Commodity.ACTIVEPOWER, timeStartSlot + 60, -ELECTRICALPOWER);
				// !: 30 seconds to get electric power...
				pr.setLoad(Commodity.NATURALGASPOWER, timeStartSlot + 30, GASPOWER);
				// !: 60 seconds to get warm water...
				pr.setLoad(Commodity.WARMWATERPOWER, timeStartSlot + 60, -THERMALPOWER);
				
				laststate = true;
				// cervisia += 0.05 * 1e2; //costs to turn on the CHP (not the
				// variable costs for letting the CHP run) (random value)
				cervisia += 7.5;
			} 
			else if (chpOn == false && laststate == true) {
				pr.setLoad(Commodity.ACTIVEPOWER, timeStartSlot, 0);
				pr.setLoad(Commodity.NATURALGASPOWER, timeStartSlot, 0);
				pr.setLoad(Commodity.WARMWATERPOWER, timeStartSlot, 0);
				laststate = false;
			}
		}
		return new Schedule(pr, cervisia);
	}

	@Override
	public CHPPhenotype transformToPhenotype(BitSet solution) {
		ArrayList<CHPActivation> starttimes = new ArrayList<CHPActivation>();

		long timeoffirstbit = referenceTime + remainingRunningTime;
		boolean[] activationBits = getActivationBits(timeoffirstbit, solution, null);

		CHPActivation currentactivation = null;

		if (remainingRunningTime > 0) {
			currentactivation = new CHPActivation();
			currentactivation.startTime = referenceTime;
			currentactivation.duration = remainingRunningTime;
		}

		for (int i = 0; i < activationBits.length; i++) {
			if (activationBits[i]) {
				// turn on
				if (currentactivation == null) {
					currentactivation = new CHPActivation();
					currentactivation.startTime = timeoffirstbit + i * SLOT_DURATION;
					currentactivation.duration = SLOT_DURATION;
				} 
				else {
					currentactivation.duration += SLOT_DURATION;
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
		getActivationBits(referenceTime + remainingRunningTime, new BitSet(), ai);

		return "forced ons:" + ai.noForcedOns + " forced offs:" + ai.noForcedOffs;
	}

	@Override
	public String solutionToString(BitSet bits) {
		return "chp solution";
	}

	@Override
	public void recalculateEncoding(long currentTime) {
		this.referenceTime = currentTime;
		
		AdditionalInfo ai = new AdditionalInfo();
		getActivationBits(referenceTime + remainingRunningTime, new BitSet(), ai);
		
		if (ai.noForcedOns > 0
				|| ai.noForcedOffs > 0) {
			this.setToBeScheduled(true);
		}
		else {
			this.setToBeScheduled(false);
		}
	}

}