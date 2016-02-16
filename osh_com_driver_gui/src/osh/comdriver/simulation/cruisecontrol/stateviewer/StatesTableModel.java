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

package osh.comdriver.simulation.cruisecontrol.stateviewer;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import osh.datatypes.registry.StateExchange;


/**
 * 
 * @author Till Schuberth
 *
 */
class StatesTableModel implements TableModel {
			
	private Set<TableModelListener> modellisteners = new HashSet<TableModelListener>();
	private Entry<UUID, StateExchange>[] data;
	
	
	/**
	 * CONSTRUCTOR
	 */
	@SuppressWarnings("unchecked")
	public StatesTableModel() {
		this.data = new Entry[0];
	}
	
	
	@Override
	public int getRowCount() {
		return data.length;
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "Sender";
		case 1:
			return "Timestamp";
		case 2:
			return "Value";
		default:
			return null;
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return UUID.class;
		case 1:
			return Long.class;
		case 2:
			return String.class;
		default:
			return null;
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex < 0 || rowIndex >= data.length) return null;
		switch (columnIndex) {
		case 0:
			return data[rowIndex].getKey();
		case 1:
			return data[rowIndex].getValue().getTimestamp();
		case 2:
			return data[rowIndex].getValue().toString();
		default:
			return null;
		}
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}

	@Override
	public synchronized void addTableModelListener(TableModelListener l) {
		if (l != null) modellisteners.add(l);
	}

	@Override
	public synchronized void removeTableModelListener(TableModelListener l) {
		if (l != null) modellisteners.remove(l);
	}
	
	private void notifyTableModelListener() {
		HashSet<TableModelListener> listeners;
		synchronized (this) {
			listeners = new HashSet<TableModelListener>(modellisteners);
		}
		
		for (TableModelListener l : listeners) {
			l.tableChanged(new TableModelEvent(this));
		}
	}
	
	@SuppressWarnings("unchecked")
	public void setData(Map<UUID, ? extends StateExchange> data) {
		
		if (data == null) {
			this.data = new Entry[0];
		} else {
			Map<UUID, StateExchange> strdata = new HashMap<UUID, StateExchange>();
			for (Entry<UUID, ? extends StateExchange> e : data.entrySet()) {
				strdata.put(e.getKey(), e.getValue());
			}
			this.data = strdata.entrySet().toArray(new Entry[0]);
			Arrays.sort(this.data, new Comparator<Entry<UUID, ?>>() {

				@Override
				public int compare(Entry<UUID, ?> o1,
						Entry<UUID, ?> o2) {
					return o1.getKey().compareTo(o2.getKey());
				}
				
			});
		}
		notifyTableModelListener();
	}
}