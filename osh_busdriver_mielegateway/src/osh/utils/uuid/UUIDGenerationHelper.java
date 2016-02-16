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

package osh.utils.uuid;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;


import osh.datatypes.en50523.EN50523Brand;
import osh.datatypes.en50523.EN50523Company;


/**
 * Generation of UUIDs
 * @author Kaibin Bao, Ingo Mauser
 *
 */
public class UUIDGenerationHelper {

	/* MIELE@HOME AND DIN EN 50523 STUFF */

	/**
	 * Generates the higher part of Miele-UUIDs
	 * @param mieleUID
	 * @param brandID
	 * @param manufacturerID
	 * @return
	 */
	public static long getMieleUUIDHigherPart( int mieleUID ) {
		return getHomeApplianceUUIDHigherPart( 
								mieleUID,
								EN50523Company.MIELE.getCompanyID(),
								EN50523Brand.MIELE.getBrandID() );
	}
	
	public static long getMieleUUIDLowerPart( int deviceType50523, InetAddress address ) throws Exception {
		return getHomeApplianceUUIDLowerPart( deviceType50523, address );
	}
	
	
	/* GENERAL HOME / HOUSEHOLD APPLIANCE */
	
	public static long getHomeApplianceUUIDHigherPart( int first32Bit, short companyID, short brandID ) {
		return ( ( (((long)first32Bit) & 0xFFFFFFFFL) << 32 ) |
			     ( (((long)companyID) & 0xFFFFL) << 16 ) |
			     ( (((long)brandID) & 0xFFFFL) << 0 ));
	}
	
	
	public static long getHomeApplianceUUIDLowerPart( int deviceType50523, InetAddress address ) throws Exception {
		byte[] b_addr = address.getAddress();

		if ( address instanceof Inet4Address || address instanceof Inet6Address )
			return getUUIDLowerPart(b_addr, deviceType50523);
		else {
			throw new Exception("Unknown IP version");
		}
	}

	/* GENERIC STUFF */

	private static long getUUIDLowerPart( byte[] low_array, int high_part ) {
		// keep brackets and 0xff because of negative numbers
		if ( low_array.length >= 8 ) {
			return	(((long)high_part&0xffffffff) << 32) ^ // just don't ask why...
					((((long)low_array[0]&0xff) << 48) |
					 (((long)low_array[1]&0xff) << 32) |
					 (((long)low_array[2]&0xff) << 24) | // EUI-64 ... byte 4..5 are skipped
					 (((long)low_array[5]&0xff) << 16) |
					 (((long)low_array[6]&0xff) <<  8) |
					 (((long)low_array[7]&0xff) <<  0));
		} else
		if ( low_array.length >= 4 ) {
			return	(((long)high_part&0xffffffff) << 32) |
					(((long)low_array[0]&0xff) << 24) |
					(((long)low_array[1]&0xff) << 16) |
					(((long)low_array[2]&0xff) <<  8) |
					(((long)low_array[3]&0xff) <<  0);
		} else {
			return	(((long)high_part&0xffffffff) << 32);
		}
	}
	
	
}
