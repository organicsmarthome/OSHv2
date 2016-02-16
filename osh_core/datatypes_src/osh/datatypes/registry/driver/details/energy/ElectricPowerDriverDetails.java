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

package osh.datatypes.registry.driver.details.energy;

import java.util.Collection;
import java.util.UUID;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


import osh.datatypes.registry.StateExchange;


/**
 * 
 * @author Florian Allerding, Kaibin Bao, Till Schuberth, Ingo Mauser
 *
 */
@XmlAccessorType( XmlAccessType.PUBLIC_MEMBER )
@XmlType

public class ElectricPowerDriverDetails extends StateExchange {
	

	protected UUID meterUuid;


	protected double activePower;
	

	protected double reactivePower;
	
	
	/** for JAXB */
	@SuppressWarnings("unused")
	@Deprecated
	private ElectricPowerDriverDetails() {
		this(null, 0);
	}
	
	/**
	 * CONSTRUCTOR
	 * @param sender
	 * @param timestamp
	 */
	public ElectricPowerDriverDetails(UUID sender, long timestamp) {
		super(sender, timestamp);
	}

	
	public UUID getMeterUuid() {
		return meterUuid;
	}
	
	public void setMeterUuid(UUID meterUuid) {
		this.meterUuid = meterUuid;
	}
	
	

	
	public double getActivePower() {
		return activePower;
	}

	public void setActivePower(double activePower) {
		this.activePower = activePower;
	}

	
	public double getReactivePower() {
		return reactivePower;
	}

	public void setReactivePower(double reactivePower) {
		this.reactivePower = reactivePower;
	}
	
	
	
	@Override
	public boolean equals(Object obj) {
		if( obj == null ) {
			return false;
		}
		if( !(obj instanceof ElectricPowerDriverDetails) ) {
			return false;
		}
		
		ElectricPowerDriverDetails other = (ElectricPowerDriverDetails) obj;
		
		return  (this.meterUuid.equals(other.meterUuid)) &&
				(this.activePower == other.activePower) &&
				(this.reactivePower == other.reactivePower);
	}
	
	@Override
	public String toString() {
		return "Electric Power: { " +
				"MeterUUID=" + getMeterUuid() + ", " +
				"P=" + getActivePower() + "W " +
				"Q=" + getReactivePower() + "var, " +
				"}" ;
	}
	
	
	static public ElectricPowerDriverDetails aggregatePowerDetails(UUID sender, Collection<ElectricPowerDriverDetails> details) {
		int _pdCount = 0;
		long timestamp = 0;
		double activesum = 0, reactivesum = 0;
		
		for ( ElectricPowerDriverDetails p : details ) {
			activesum = activesum + p.getActivePower();
			reactivesum = reactivesum + p.getReactivePower();
			timestamp = p.getTimestamp(); //why?
			_pdCount++;
		}
		
		ElectricPowerDriverDetails _pd = new ElectricPowerDriverDetails(sender, timestamp);
		_pd.setActivePower( activesum );
		_pd.setReactivePower( reactivesum );

		if ( _pdCount == details.size() && _pdCount > 0 ) {
			return _pd;
		}
		else {
			// ERROR: undefined state due to missing data
			return null;
		}
	}

	
}
