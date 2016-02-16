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

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.UUID;

/**
 * 
 * @author Kaibin Bao, Ingo Mauser
 *
 */
public class UUIDLists {
	
	public static ArrayList<UUID> parseUUIDArray( String str ) throws IllegalArgumentException {
		while ( str.startsWith("[") )
			str = str.substring(1);
		
		while ( str.endsWith("]") )
			str = str.substring(0, str.length()-1);
		
		StringTokenizer strtok = new StringTokenizer(str, ",");
		ArrayList<UUID> uuidList = new ArrayList<UUID>();
		
		while ( strtok.hasMoreElements() ) {
			UUID uuid = UUID.fromString( strtok.nextToken() );
			uuidList.add(uuid);
		}
		
		return uuidList;
	}
}
