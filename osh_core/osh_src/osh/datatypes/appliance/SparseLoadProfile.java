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

package osh.datatypes.appliance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import osh.datatypes.Commodity;

/**
 * 
 * @author Florian Allerding, Kaibin Bao, Ingo Mauser, Till Schuberth
 *
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class SparseLoadProfile implements LoadProfile, PowerSumPrecalculated {

	@XmlType
	@XmlAccessorType(XmlAccessType.FIELD)
	private class Tick {
		public int value;
		public long positiveIntegral;
		public long negativeIntegral;

		public Tick() {
		}

		public Tick(Tick other) {
			this.value = other.value;
			this.positiveIntegral = other.positiveIntegral;
			this.negativeIntegral = other.negativeIntegral;
		}

		@Override
		public String toString() {
			return value + "W  P/Q+" + positiveIntegral + "Ws  P/Q-" + negativeIntegral
					+ "Ws";
		}

		@Override
		public Tick clone() {
			return new Tick(this);
		}
	}

	private HashMap<Commodity, TreeMap<Long, Tick>> commodities;

	private long endingTimeOfProfile;

	/**
	 * Just for internal use (cloning)
	 * 
	 * @param commodities
	 * @param endingTimeOfProfile
	 */
	@Deprecated
	private SparseLoadProfile(
			HashMap<Commodity,TreeMap<Long, Tick>> commodities,
			long endingTimeOfProfile) {
		this.commodities = commodities;
		this.endingTimeOfProfile = endingTimeOfProfile;
	}

	public SparseLoadProfile() {
		commodities = new HashMap<>();
		for (Commodity commodity : Commodity.values()) {
			TreeMap<Long, Tick> profile = new TreeMap<>();
			commodities.put(commodity, profile);
		}

		this.endingTimeOfProfile = 0;
	}

	/**
	 * EndingTime is defined as the point in time (timestamp) where
	 * the profile stops having a value other than 0, NOT the length of the profile.
	 * See {@link LoadProfile#getEndingTimeOfProfile()}
	 */
	public void setEndingTimeOfProfile(long endingTimeOfProfile) {
		this.endingTimeOfProfile = endingTimeOfProfile;
	}

	private TreeMap<Long, Tick> getLoadProfile(Commodity commodity) {
		return commodities.get(commodity);
	}

	public void setLoad(Commodity commodity, long t, int power) {
		Tick v = new Tick();
		v.value = power;

		TreeMap<Long, Tick> loadProfile = getLoadProfile(commodity);
		if (loadProfile == null) {
			loadProfile = new TreeMap<>();
			commodities.put(commodity, loadProfile);
		}
		
		Entry<Long, Tick> floor = loadProfile.floorEntry(t);

		// sum up from -infinity to t
		if (floor != null) {
			Tick f = floor.getValue();
			v.positiveIntegral = f.positiveIntegral;
			v.negativeIntegral = f.negativeIntegral;
			long deltaT = (t - floor.getKey());
			if (f.value > 0) {
				v.positiveIntegral = v.positiveIntegral + deltaT * (f.value);
			} else {
				v.negativeIntegral = v.negativeIntegral + deltaT * (f.value);
			}
		} else {
			v.negativeIntegral = 0;
			v.positiveIntegral = 0;
		}

		loadProfile.put(t, v);
		if (this.endingTimeOfProfile < t + 1) {
			if (this.endingTimeOfProfile == t 
					&& power == 0) {
				//do nothing
				// KEEP this: only 1 tick with value 0 -> no tick
			} 
			else {
				this.endingTimeOfProfile = t + 1;
			}
		}

		// update from t to +infinity
		SortedMap<Long, Tick> ticksRightOfT = loadProfile.tailMap(t, false);

		if (ticksRightOfT != null && !ticksRightOfT.isEmpty()) {
			long firstKey = ticksRightOfT.firstKey();
			assert (firstKey > t);
			long partialsumpos = 0, partialsumneg = 0;
			if (v.value > 0) {
				partialsumpos = (firstKey - t) * v.value;
			} else {
				partialsumneg = (firstKey - t) * v.value;
			}
			if (floor != null) {
				if (floor.getValue().value >= 0) {
					partialsumpos = partialsumpos - (firstKey - t) * floor.getValue().value;
				} else {
					partialsumneg = partialsumneg - (firstKey - t) * floor.getValue().value;
				}
			}
			
			for (Entry<Long, Tick> entry : ticksRightOfT.entrySet()) {
				Tick tr = entry.getValue();
				tr.positiveIntegral = tr.positiveIntegral + partialsumpos;
				tr.negativeIntegral = tr.negativeIntegral + partialsumneg;
			}
		}
	}

	private <T> Entry<Long, T> getNext(
			Iterator<Entry<Long, T>> it,
			long duration) {
		if (it.hasNext()) {
			Entry<Long, T> e = it.next();
			if (e.getKey() < duration)
				return e;
			else
				return null;
		} else
			return null;
	}

	@Override
	public LoadProfile merge(LoadProfile other, long offset) {
		if (other instanceof SparseLoadProfile) {
			LoadProfile merged = merge((SparseLoadProfile) other, offset);
			if (this.getEndingTimeOfProfile() != 0 || other.getEndingTimeOfProfile() != 0) {
				if (merged.getEndingTimeOfProfile() == 0) {
					// still possible because of offset, but not normal
					System.out.println("ERROR: merged.getEndingTimeOfProfile() == 0, although one profile not 0");
				}
			}

			return merged;
		} 
		else {
			if (other == null) {
				throw new NullPointerException("other is null");
			}
			throw new UnsupportedOperationException();
		}
	}

	public SparseLoadProfile merge(
			SparseLoadProfile other, 
			long offset) {
		
		SparseLoadProfile merged = new SparseLoadProfile();

		merged.setEndingTimeOfProfile(Math.max(this.endingTimeOfProfile, other.endingTimeOfProfile + offset));

		for (Commodity commodity : Commodity.values()) {
			TreeMap<Long, Tick> loadProfile1 = this.getLoadProfile(commodity);
			TreeMap<Long, Tick> loadProfile2 = other.getLoadProfile(commodity);

			Iterator<Entry<Long, Tick>> iSet1 = loadProfile1.entrySet()
					.iterator();
			Iterator<Entry<Long, Tick>> iSet2 = loadProfile2.entrySet()
					.iterator();

			Entry<Long, Tick> entry1 = null;
			Entry<Long, Tick> entry2 = null;

			int activeValue1 = 0;
			int activeValue2 = 0;

			entry1 = getNext(iSet1, this.endingTimeOfProfile);
			entry2 = getNext(iSet2, other.endingTimeOfProfile);

			while (entry1 != null && entry2 != null) {

				if (entry1.getKey() < entry2.getKey() + offset) {
					merged.setLoad(commodity, entry1.getKey(),
							entry1.getValue().value + activeValue2);

					activeValue1 = entry1.getValue().value;

					entry1 = getNext(iSet1, this.endingTimeOfProfile);
				} else if (entry1.getKey() > entry2.getKey() + offset) {
					merged.setLoad(commodity, entry2.getKey() + offset,
							activeValue1 + entry2.getValue().value);

					activeValue2 = entry2.getValue().value;

					entry2 = getNext(iSet2, other.endingTimeOfProfile);
				} else /* (entry1.getKey() == entry2.getKey() + offset) */{
					merged.setLoad(commodity, entry2.getKey() + offset,
							entry1.getValue().value + entry2.getValue().value);

					activeValue1 = entry1.getValue().value;
					activeValue2 = entry2.getValue().value;

					entry1 = getNext(iSet1, this.endingTimeOfProfile);
					entry2 = getNext(iSet2, other.endingTimeOfProfile);
				}
			}

			while (entry1 != null) { // 1st profile still has data points
				if (entry1.getKey() < other.endingTimeOfProfile + offset) {
					merged.setLoad(commodity, entry1.getKey(),
							entry1.getValue().value + activeValue2);
					activeValue1 = entry1.getValue().value;
				} else { // 2nd profile has ended
					if (activeValue2 != 0) {
						merged.setLoad(commodity, other.endingTimeOfProfile + offset,
								activeValue1);
						activeValue2 = 0;
					}
					merged.setLoad(commodity, entry1.getKey(),
							entry1.getValue().value + activeValue2);
				}

				entry1 = getNext(iSet1, this.endingTimeOfProfile);
			}
			while (entry2 != null) {
				if (entry2.getKey() + offset < this.endingTimeOfProfile) {
					merged.setLoad(commodity, entry2.getKey() + offset,
							entry2.getValue().value + activeValue1);
					activeValue2 = entry2.getValue().value;
				} else {
					if (activeValue1 != 0) {
						merged.setLoad(commodity, endingTimeOfProfile, activeValue2);
						activeValue1 = 0;
					}
					merged.setLoad(commodity, entry2.getKey() + offset,
							entry2.getValue().value + activeValue1);
				}

				entry2 = getNext(iSet2, other.endingTimeOfProfile);
			}

			// handling the end of profiles
			if (activeValue1 != 0 && activeValue2 != 0) {
				if (this.endingTimeOfProfile > other.endingTimeOfProfile + offset) {
					merged.setLoad(commodity, other.endingTimeOfProfile + offset,
							activeValue1);
				} else if (this.endingTimeOfProfile < other.endingTimeOfProfile + offset) {
					merged.setLoad(commodity, this.endingTimeOfProfile, activeValue2);
				} else { /* == */
					assert (this.endingTimeOfProfile == merged.endingTimeOfProfile);
				}
			} else if (activeValue2 != 0) {
				merged.setLoad(commodity, other.endingTimeOfProfile + offset, activeValue1);
			} else if (activeValue1 != 0) {
				merged.setLoad(commodity, endingTimeOfProfile, activeValue2);
			}
		}

		return merged;
	}

	/**
	 * EndingTimeOfProfile is defined as the point in time where
	 * the profile stops having a value other than 0, NOT the length of the profile.
	 * See {@link LoadProfile#getEndingTimeOfProfile()}
	 */
	@Override
	public long getEndingTimeOfProfile() {
		return endingTimeOfProfile;
	}

	@Override
	public int getLoadAt(Commodity commodity, long t) {
		TreeMap<Long, Tick> loadProfile = getLoadProfile(commodity);
		
		if (t >= endingTimeOfProfile) {
//			throw new IndexOutOfBoundsException();
			return 0;
		}

		Entry<Long, Tick> entry = loadProfile.floorEntry(t);
		return (entry == null) ? 0 : entry.getValue().value;
	}
	
	
	@Override
	public long getMiddleOfPowerConsumption(Commodity c) {
		long powerTotal = getWork(c, endingTimeOfProfile);
		long time = 0;
		for ( Entry<Long, Tick> e : getLoadProfile(c).entrySet() ) {
			if (getWork(c, e.getKey()) >= powerTotal / 2.0) {
				time = e.getKey();
				break;
			}
		}
		return time;
	}

	/**
	 * 
	 * @param commodity
	 * @return Work in [CommodityUnit * TimeUnit]
	 */
	public long getWork(Commodity commodity) {
		return getWork(commodity, endingTimeOfProfile);
	}
	
	
	@Override
	public long getWork(Commodity commodity, long t) {
		TreeMap<Long, Tick> loadProfile = getLoadProfile(commodity);
		
		if (t > endingTimeOfProfile)
			throw new IndexOutOfBoundsException();

		Entry<Long, Tick> e = loadProfile.floorEntry(t);

		if (e != null) {
			int power = e.getValue().value;

			return e.getValue().positiveIntegral + e.getValue().negativeIntegral
					+ (t - e.getKey()) * power;
		} 
		else {
			return 0;
		}
	}
	
	@Override
	public long getPositiveWork(Commodity commodity, long t) {
		TreeMap<Long, Tick> loadProfile = getLoadProfile(commodity);
		
		if (t > endingTimeOfProfile)
			throw new IndexOutOfBoundsException();

		Entry<Long, Tick> e = loadProfile.floorEntry(t);

		if (e != null) {
			long power = e.getValue().value;

			if (power >= 0) {
				assert (t >= e.getKey());
				return e.getValue().positiveIntegral + (t - e.getKey()) * power;
			} else
				return e.getValue().positiveIntegral;
		} 
		else {
			return 0;
		}
	}
	
	@Override
	public long getNegativeWork(Commodity commodity, long t) {
		TreeMap<Long, Tick> loadProfile = getLoadProfile(commodity);
		
		if (t > endingTimeOfProfile)
			throw new IndexOutOfBoundsException();

		Entry<Long, Tick> e = loadProfile.floorEntry(t);

		if (e != null) {
			int power = e.getValue().value;

			if (power <= 0) {
				return e.getValue().negativeIntegral + (t - e.getKey()) * power;
			} else
				return e.getValue().negativeIntegral;
		} else {
			return 0;
		}
	}
	
	@Override
	public Long getNextLoadChange(Commodity commodity, long t) {
		return getLoadProfile(commodity).higherKey(t);
	}
	
	/**
	 * cuts off all ticks with a negative time and inserts a tick at time 0
	 * if necessary. This function changes the current object.
	 * @author tisu
	 */
	@Override
	public void cutOffNegativeTimeValues() {
		boolean modified = false;
		
		for (TreeMap<Long, Tick> ticks : commodities.values()) {
			Entry<Long, Tick> lastProcessed = null;
			while (ticks.size() > 0 && ticks.firstKey() < 0) {
				lastProcessed = ticks.firstEntry();
				ticks.remove(lastProcessed.getKey());
				modified = true;
			}
			if (lastProcessed != null && ticks.size() > 0 && ticks.firstKey() > 0) {
				ticks.put(0L, lastProcessed.getValue());
			}
		}
		
		if (modified) recalculateIntegrals();
	}
	
	
	private void recalculateIntegrals() {
		for (Entry<Commodity, TreeMap<Long, Tick> > ticks : commodities.entrySet()) {
			//we could really recalculate all integrals in here, but I prefer
			//to do all the calculations in one place to minimize errors
			//this method is a bit slower, but won't take too much more
			//processing time.
			
			TreeMap<Long, Tick> newticks = new TreeMap<>();
			for (Entry<Long, Tick> t : ticks.getValue().entrySet()) {
				newticks.put(t.getKey(), t.getValue());
			}
			commodities.put(ticks.getKey(), newticks);
		}
	}

	
	/***********************************************************
	 * static functions
	 **********************************************************/

	/**
	 * Convert and do NOT compress
	 * @param powerProfile
	 * @return
	 */
	public static SparseLoadProfile convertToSparseProfile(
			HashMap<Commodity, ArrayList<PowerProfileTick>> powerProfile,
			LoadProfileCompressionTypes ct,
			final int timeSlotDuration) {
		// conversion without compression
		return convertToSparseProfile(
				powerProfile, 
				ct,
				-1,
				timeSlotDuration);
	}

	/**
	 * Convert and compress
	 * @param powerProfiles
	 * @param powerEps max delta value for new point
	 * @return
	 */
	public static SparseLoadProfile convertToSparseProfile(
			HashMap<Commodity,ArrayList<PowerProfileTick>> powerProfiles,
			LoadProfileCompressionTypes ct,
			int powerEps,
			final int timeSlotDuration) {
		
		SparseLoadProfile profile = new SparseLoadProfile();
		
		if (ct == LoadProfileCompressionTypes.TIMESLOTS) {
			powerEps = -1;
		}
		
		for (Entry<Commodity, ArrayList<PowerProfileTick>> e : powerProfiles.entrySet()) {
			
			int powerLastAvg = 0;
			long powerAvgSum = 0;
			long powerAvgCnt = 0;

			long i = 0;
			long prevI = 0;
			
			List<PowerProfileTick> powerProfile = e.getValue();
			Commodity currentCommodity = e.getKey();
			
			for (PowerProfileTick p : powerProfile) {
				if (i == 0) {
					powerAvgSum = powerAvgSum + p.load;
					powerAvgCnt++;
					powerLastAvg = (int) Math.round(powerAvgSum / powerAvgCnt);

				} 
				else if (i == (powerProfile.size() - 1)) {
					profile.setLoad(currentCommodity, prevI, powerLastAvg);

					profile.setLoad(currentCommodity, i, p.load);
				} 
				else {
					if (Math.abs(powerLastAvg - p.load) > powerEps) {

						profile.setLoad(currentCommodity, prevI, powerLastAvg);
						powerLastAvg = p.load;
						powerAvgSum = p.load;
						powerAvgCnt = 1;

						prevI = i;
					} 
					else {
						powerAvgSum = powerAvgSum + p.load;
						powerAvgCnt++;
						powerLastAvg = (int) Math.round(powerAvgSum / powerAvgCnt);
					}
				}
				i++;
			}
			
		}
		
		if (ct == LoadProfileCompressionTypes.TIMESLOTS) {
			profile = profile.getCompressedProfile(ct, powerEps, timeSlotDuration);
		}

		return profile;
	}
	

	
	// ###########
	// COMPRESSION
	// ###########
	
	// general
	public SparseLoadProfile getCompressedProfile(LoadProfileCompressionTypes ct, final int powerEps, final int time) {
		if (ct == LoadProfileCompressionTypes.DISCONTINUITIES) {
			return getCompressedProfileByDiscontinuities(powerEps);
		}
		else if (ct == LoadProfileCompressionTypes.TIMESLOTS) {
			return getCompressedProfileByTimeSlot(time);
		}
		else {
			return null;
		}
	}
	
	public static SparseLoadProfile[][][] getCompressedProfile(LoadProfileCompressionTypes ct, SparseLoadProfile[][][] original, final int powerEps, final int time) {
		if (ct == LoadProfileCompressionTypes.DISCONTINUITIES) {
			return getCompressedLoadProfilesByDiscontinuities(original, powerEps);
		}
		else if (ct == LoadProfileCompressionTypes.TIMESLOTS) {
			return getCompressedLoadProfilesByTimeSlot(original, time);
		}
		else {
			return null;
		}
	}
	
	public static SparseLoadProfile[] getCompressedProfile(LoadProfileCompressionTypes ct, SparseLoadProfile[] original, final int powerEps, final int time) {
		if (ct == LoadProfileCompressionTypes.DISCONTINUITIES) {
			return getCompressedLoadProfilesByDiscontinuities(original, powerEps);
		}
		if (ct == LoadProfileCompressionTypes.TIMESLOTS) {
			return getCompressedLoadProfilesByTimeSlot(original, time);
		}
		else {
			return null;
		}
	}
	
	
	// ByDiscontinuities

	public SparseLoadProfile getCompressedProfileByDiscontinuities(final double powerEps) {
		
		SparseLoadProfile compressed = new SparseLoadProfile();

		for (Commodity c : commodities.keySet()) {
			TreeMap<Long, Tick> map = commodities.get(c);
			
			
			double lastAvgToSave = Double.MAX_VALUE;
			long lastKeyToSave = Long.MIN_VALUE;
			Tick lastValueTickToSave = null;
			int lastValueTickValueToSave = 0;
			
			long lastKey = Long.MIN_VALUE;
			int lastTickValue = 0;
			
			long counter = 0;
			
			for (Iterator<Map.Entry<Long, Tick>> it = map.entrySet().iterator(); it.hasNext();) {
				Entry<Long,Tick> e = it.next();
				// if last -> set value
				if (it.hasNext() == false) {
					compressed.setLoad(c, e.getKey(), e.getValue().value);
					
					if (lastValueTickToSave != null) {
						// write previous average value...
						compressed.setLoad(c, lastKeyToSave, (int) Math.round(lastAvgToSave));
					}
				}
				// first value
				else if (lastValueTickToSave == null) {
					compressed.setLoad(c, e.getKey(), e.getValue().value);

					lastKeyToSave = e.getKey();
					lastValueTickToSave = e.getValue();
					lastValueTickValueToSave = e.getValue().value;
					lastAvgToSave = lastValueTickValueToSave;
					
					lastKey = lastKeyToSave;
					lastTickValue = lastValueTickValueToSave;
					counter = 0;
				}
				// difference to previous value is too small -> update avg
//				else if (Math.abs(e.getValue().value - lastValueTickValueToSave) < powerEps) {
//					long diffToLastKey = e.getKey() - lastKeyToSave;
//					int currentValue = e.getValue().value;
//					lastAvg = (lastAvg * diffToLastKey + currentValue) / (diffToLastKey + 1);
					
				else if (Math.abs(e.getValue().value - lastAvgToSave) < powerEps) {
					long diffToLastKey = e.getKey() - lastKey;
					lastAvgToSave = (lastTickValue * diffToLastKey + lastAvgToSave * counter) / (diffToLastKey + counter);

					lastKey = e.getKey();
					lastTickValue = e.getValue().value;
					
					counter = counter + diffToLastKey;
				}
				// difference to previous is big enough -> set avg and add new datapoint
				else {
//					long diffToLastKey = e.getKey() - lastKeyToSave;
//					lastAvgToSave = (lastValueTickValueToSave * diffToLastKey + lastAvgToSave * counter) / (diffToLastKey + counter);
					
					long diffToLastKey = e.getKey() - lastKey;
					lastAvgToSave = (lastTickValue * diffToLastKey + lastAvgToSave * counter) / (diffToLastKey + counter);
					
					compressed.setLoad(c, lastKeyToSave, (int) Math.round(lastAvgToSave));
					
					lastKeyToSave = e.getKey();
					lastValueTickToSave = e.getValue();
					lastValueTickValueToSave = e.getValue().value;
					lastAvgToSave = lastValueTickValueToSave;
					
					lastKey = e.getKey();
					lastTickValue = e.getValue().value;
					counter = 0;
				}
			}
		}
		
		compressed.setEndingTimeOfProfile(this.getEndingTimeOfProfile());
		
		return compressed;
	}
	
	
	public static SparseLoadProfile[][][] getCompressedLoadProfilesByDiscontinuities(SparseLoadProfile[][][] original, final int powerEps) {
		SparseLoadProfile[][][] compressed = null;
		if (original != null) {
			compressed = new SparseLoadProfile[original.length][][];
			for (int i = 0; i < original.length; i++) {
				compressed[i] = new SparseLoadProfile[original[i].length][];
				for (int j = 0; j < original[i].length; j++) {
					compressed[i][j] = SparseLoadProfile.getCompressedLoadProfilesByDiscontinuities(original[i][j], powerEps);
				}
			}
		}
		return compressed;
	}
	
	public static SparseLoadProfile[] getCompressedLoadProfilesByDiscontinuities(SparseLoadProfile[] original, final int powerEps) {
		SparseLoadProfile[] compressed = null;
		if (original != null) {
			compressed = new SparseLoadProfile[original.length];
			for (int i = 0; i < original.length; i++) {
				compressed[i] = original[i].getCompressedProfileByDiscontinuities(powerEps);
			}
		}
		return compressed;
	}
	
	
	// by TimeSlot


	public SparseLoadProfile getCompressedProfileByTimeSlot(final int time) {
		SparseLoadProfile compressed = new SparseLoadProfile();

		for (Commodity c : commodities.keySet()) {
			TreeMap<Long, Tick> map = commodities.get(c);
			
			Tick lastTick = null;
			double lastAvg = Double.MAX_VALUE;
			long lastKey = Long.MIN_VALUE;
			
			for (Iterator<Map.Entry<Long, Tick>> it = map.entrySet().iterator(); it.hasNext();) {
				Entry<Long,Tick> e = it.next();
				// if last -> set value
				if (it.hasNext() == false) {
					compressed.setLoad(c, e.getKey(), e.getValue().value);
					
					if (lastTick != null) {
						// write last value...
						compressed.setLoad(c, lastKey, (int) Math.round(lastAvg));
					}
				}
				// first value
				else if (lastTick == null) {
					compressed.setLoad(c, e.getKey(), e.getValue().value);

					lastKey = e.getKey();
					lastTick = e.getValue();
					lastAvg = e.getValue().value;
				}
				// difference to previous value is too small -> update avg
				else if (Math.abs(e.getKey() - lastKey) < time) {
					long diffToLastKey = e.getKey() - lastKey;
					int currentValue = e.getValue().value;
					lastAvg = (lastAvg * diffToLastKey + currentValue) / (diffToLastKey + 1);
				}
				// difference to previous is big enough -> set avg and add new datapoint
				else {
					compressed.setLoad(c, lastKey, (int) Math.round(lastAvg));
					
					lastKey = e.getKey();
					lastTick = e.getValue();
					lastAvg = e.getValue().value;
				}
			}
		}
		
		return compressed;
	}
	
	
	public static SparseLoadProfile[][][] getCompressedLoadProfilesByTimeSlot(SparseLoadProfile[][][] original, final int time) {
		SparseLoadProfile[][][] compressed = null;
		if (original != null) {
			compressed = new SparseLoadProfile[original.length][][];
			for (int i = 0; i < original.length; i++) {
				compressed[i] = new SparseLoadProfile[original[i].length][];
				for (int j = 0; j < original[i].length; j++) {
					compressed[i][j] = SparseLoadProfile.getCompressedLoadProfilesByTimeSlot(original[i][j], time);
				}
			}
		}
		return compressed;
	}
	
	public static SparseLoadProfile[] getCompressedLoadProfilesByTimeSlot(SparseLoadProfile[] original, final int time) {
		SparseLoadProfile[] compressed = null;
		if (original != null) {
			compressed = new SparseLoadProfile[original.length];
			for (int i = 0; i < original.length; i++) {
				compressed[i] = original[i].getCompressedProfileByTimeSlot(time);
			}
		}
		return compressed;
	}
	
	
	
	
	/***********************************************************
	 * utility functions (cloning, toString, ...)
	 **********************************************************/

	@Override
	public SparseLoadProfile clone() {
		
		SparseLoadProfile newSparseLoadProfile = new SparseLoadProfile();

		for( Entry<Commodity, TreeMap<Long, Tick>> es : commodities.entrySet() ) {
			TreeMap<Long, Tick> originalLoadProfile = es.getValue();
			TreeMap<Long, Tick> newLoadProfile = new TreeMap<>();
			
			for (Entry<Long, Tick> entry : originalLoadProfile.entrySet()) {
				newLoadProfile.put(entry.getKey(), entry.getValue().clone());
			}
			
			newSparseLoadProfile.commodities.put(es.getKey(), newLoadProfile);
		}

		newSparseLoadProfile.endingTimeOfProfile = this.endingTimeOfProfile;

		return newSparseLoadProfile;
	}

	public SparseLoadProfile cloneOnlyDuration() {
		return new SparseLoadProfile(commodities, endingTimeOfProfile);
	}
	
	
	// toString
	
	@Override
	public String toString() {
		String returnValue = "";
		
		for ( Entry<Commodity, TreeMap<Long, Tick>> es : commodities.entrySet() ) {
			returnValue += "Profile for Commodity " + es.getKey() + ": " + es.getValue().toString();
		}
		
		return returnValue;
	}

	@Override
	public String toStringShort() {
		String returnValue = "[ ";
		
		for ( Entry<Commodity, TreeMap<Long, Tick>> es : commodities.entrySet() ) {
			TreeMap<Long, Tick> map = es.getValue();
			if( map != null && map.lastEntry() != null ) {
				Tick lastTick = map.lastEntry().getValue();
				returnValue += es.getKey() + ": S+ " + lastTick.positiveIntegral + " S- " + lastTick.negativeIntegral + ", ";
			}
		}
		
		return returnValue + "]";
	}
	
	
	
	
}
