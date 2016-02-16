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

package osh.datatypes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;

public class WaterStorageOptimizedDataStorage<Value extends WaterStorageOptimizedDataStorage.EqualData<? super Value>> {

	public interface EqualData<T> {
		public boolean equalData(T o);
	}
	
	private TreeMap<Long, Value> storage = new TreeMap<Long, Value>();
	
	public TreeMap<Long, Value> getMap() {
		return storage;
	}
	
	public void add(long timestamp, Value value) {
		storage.put(timestamp, value);
		
		//check if this datapoint is the same as the two before and throw out the middle one if possible
		if (storage.size() >= 3) {
			List<Entry<Long, Value>> lastEntries = new ArrayList<Entry<Long, Value>>(3);
			Iterator<Entry<Long, Value>> it = storage.descendingMap().entrySet().iterator();
			for (int i = 0; i < 3; i++) lastEntries.add(i, it.next());
			
			if (lastEntries.get(0).getValue().equalData(lastEntries.get(1).getValue()) && lastEntries.get(1).getValue().equalData(lastEntries.get(2).getValue())) {
				storage.remove(lastEntries.get(1).getKey());
			}
		}
	}
}
