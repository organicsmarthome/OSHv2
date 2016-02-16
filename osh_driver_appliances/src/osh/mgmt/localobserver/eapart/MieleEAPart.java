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

package osh.mgmt.localobserver.eapart;

import java.util.BitSet;
import java.util.UUID;




import osh.configuration.system.DeviceTypes;
import osh.datatypes.appliance.LoadProfile;
import osh.datatypes.appliance.SparseLoadProfile;
import osh.datatypes.ea.Schedule;
import osh.datatypes.registry.observer.ControllableEAProblemPartExchange;

/**
 * 
 * @author Till Schuberth
 *
 */

public class MieleEAPart extends ControllableEAProblemPartExchange<MieleSolution> {

	private long earliestStarttime;
	private long latestStarttime;
	private long originaldof;
	private boolean predicted = false;

	/** copied from controller (exchange) */

	private LoadProfile profile;

	/** device is already scheduled, use short time dof stuff now */
	private boolean scheduled = false;
	
	
	/** for JAXB, never use that */
	@Deprecated
	private MieleEAPart() { 
		super(null, 0, 0, true, 0, DeviceTypes.OTHER); 
	}
	
	public MieleEAPart(
			UUID deviceId, 
			long timestamp,
			long earliestStarttime, 
			long latestStarttime,
			LoadProfile profile, 
			boolean predicted,
			long optimizationHorizon,
			DeviceTypes deviceType) {
		this(deviceId, timestamp, earliestStarttime, latestStarttime,
				profile, false, true, predicted, optimizationHorizon, deviceType);
	}

	public MieleEAPart(
			UUID deviceId, 
			long timestamp,
			long earliestStarttime, 
			long latestStarttime,
			LoadProfile profile, 
			boolean isScheduled, 
			boolean toBeScheduled, 
			boolean predicted,
			long optimizationHorizon,
			DeviceTypes deviceType) {
		super(	deviceId, 
				timestamp, 
				calculateBitCount(earliestStarttime, latestStarttime), 
				toBeScheduled,
				optimizationHorizon,
				deviceType);
		
		this.earliestStarttime = earliestStarttime;
		this.latestStarttime = latestStarttime;
		this.originaldof = latestStarttime - earliestStarttime;
		this.profile = profile;
		this.scheduled = isScheduled;
		this.predicted = predicted;

		if (profile == null) {
			throw new NullPointerException("profile is null");
		}
	}
	
	/**
	 * returns the needed amount of bits for the EA
	 * 
	 * @param earliestStarttime
	 * @param latestStarttime
	 */
	private static int calculateBitCount(
			long earliestStarttime,
			long latestStarttime) {
		if (earliestStarttime > latestStarttime) {
			return 0;
		}

		long diff = latestStarttime - earliestStarttime + 1;
		int bits = (int) Math.ceil(Math.log(diff) / Math.log(2));

		return bits;
	}


	@Override
	public Schedule getSchedule(BitSet solution) {
		LoadProfile pr = new SparseLoadProfile();
		pr = pr.merge(profile, earliestStarttime + getStartOffset(solution));
		return new Schedule(pr, 0);
	}

	private long getStartOffset(BitSet solution) {
		long maxoffset = latestStarttime - earliestStarttime;
		return (long) Math.floor(Double.valueOf(gray2long(solution))
				/ Math.pow(2, getBitCount()) * maxoffset);
	}

	public long getStartTime(BitSet solution) {
		return earliestStarttime + getStartOffset(solution);
	}

	public boolean isPredicted() {
		return predicted;
	}

	public boolean isScheduled() {
		return scheduled;
	}

	@Override
	public String problemToString() {
		return "Miele Profile: " + profile.toStringShort() + " DoF:" + earliestStarttime + "-" + latestStarttime
				+ "(" + (latestStarttime - earliestStarttime) + ")" + (predicted ? " (predicted)" : "");
	}

	@Override
	public void recalculateEncoding(long currentTime) {
		if (earliestStarttime < currentTime) {
			if (currentTime > latestStarttime) {
				earliestStarttime = latestStarttime;
			} else {
				earliestStarttime = currentTime;
			}
			setBitCount(calculateBitCount(
					earliestStarttime,
					latestStarttime));
		}
	}

	@Override
	public String solutionToString(BitSet bits) {
		if (bits == null)
			return "ERROR: no solution bits";
		return "start time: " + getStartTime(bits);
	}

	@Override
	public MieleSolution transformToPhenotype(BitSet solution) {
		return new MieleSolution(getStartTime(solution), predicted);
	}

	public long getEarliestStarttime() {
		return earliestStarttime;
	}

	public long getLatestStarttime() {
		return latestStarttime;
	}

	public long getOriginalDof() {
		return originaldof;
	}
	
	/* default */ LoadProfile getClonedProfile() {
		return new SparseLoadProfile().merge(profile, 0); //clone
	}
}
