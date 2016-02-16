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

import osh.datatypes.ea.ISolution;


/**
 * 
 * @author Florian Allerding, Till Schuberth
 *
 */

public class MieleSolution implements ISolution {
	
	public long startTime;
	public boolean isPredicted;
	
	
	/** for JAXB */
	@SuppressWarnings("unused")
	@Deprecated
	private MieleSolution() {}
	
	/**
	 * CONSTRUCTOR
	 * @param startTime
	 * @param isPredicted
	 */
	public MieleSolution(long startTime, boolean isPredicted) {
		super();
		
		this.startTime = startTime;
		this.isPredicted = isPredicted;
	}

	
	@Override
	public boolean equals(Object obj) {
		if ( obj == null ) {
			return false;
		}
		else if (obj instanceof MieleSolution) {
			MieleSolution ms = (MieleSolution) obj;
			if (startTime == ms.startTime && isPredicted == ms.isPredicted) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	@Override
	public MieleSolution clone() throws CloneNotSupportedException {
		MieleSolution clonedSolution = new MieleSolution(this.startTime, this.isPredicted);
		return clonedSolution;
	}

}