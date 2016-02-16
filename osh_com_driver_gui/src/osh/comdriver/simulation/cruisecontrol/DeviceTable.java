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

package osh.comdriver.simulation.cruisecontrol;

import java.awt.Dimension;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import osh.datatypes.gui.DeviceTableEntry;

/**
 * 
 * @author Till Schuberth
 *
 */
class DeviceTable extends JPanel {

	/** inner class */
	private class MyModel implements TableModel {
		
		private Set<TableModelListener> modellisteners = new HashSet<TableModelListener>();
		private DeviceTableEntry[] data;
		
		public MyModel() {
			this.data = new DeviceTableEntry[0];
		}
		
		@SuppressWarnings("unused")
		public MyModel(Collection<DeviceTableEntry> data) {
			if (data == null) {
				this.data = new DeviceTableEntry[0];
			} else {
				this.data = new TreeSet<DeviceTableEntry>(data).toArray(this.data);
			}
		}

		@Override
		public int getRowCount() {
			return data.length;
		}

		@Override
		public int getColumnCount() {
			return 5;
		}

		@Override
		public String getColumnName(int columnIndex) {
			switch (columnIndex) {
			case 0:
				return "Index";
			case 1:
				return "UUID";
			case 2:
				return "Name";
			case 3:
				return "Number of Bits";
			case 4:
				return "String representation";
			default:
				return null;
			}
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			switch (columnIndex) {
			case 0:
				return Integer.class;
			case 1:
				return UUID.class;
			case 2:
				return String.class;
			case 3:
				return Integer.class;
			case 4:
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
				return data[rowIndex].getEntry();
			case 1:
				return data[rowIndex].getId();
			case 2:
				return data[rowIndex].getName();
			case 3:
				return data[rowIndex].getBits();
			case 4:
				return data[rowIndex].getRepresentation();
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
		
		@SuppressWarnings("unused")
		public DeviceTableEntry[] getData() {
			return data;
		}

		public void setData(Collection<DeviceTableEntry> data) {
			if (data == null) {
				this.data = new DeviceTableEntry[0];
			} else {
				this.data = new TreeSet<DeviceTableEntry>(data).toArray(this.data);
			}
			notifyTableModelListener();
		}
		
	}


	private static final long serialVersionUID = 1L;
	
	private JTable table;
	private MyModel model;
	
	
	/**
	 * CONSTRUCTOR
	 */
	public DeviceTable() {
		super();
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		model = new MyModel();
		table = new JTable(model);
		table.getColumnModel().getColumn(0).setPreferredWidth(20);
		table.getColumnModel().getColumn(1).setPreferredWidth(150);
		table.getColumnModel().getColumn(2).setPreferredWidth(150);
		table.getColumnModel().getColumn(3).setPreferredWidth(20);
		table.getColumnModel().getColumn(4).setPreferredWidth(400);
		JScrollPane sp = new JScrollPane(table);
		sp.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		sp.setAlignmentX(CENTER_ALIGNMENT);
		add(sp);
		this.setPreferredSize(new Dimension(200, 200));
	}

	public DeviceTable(Set<DeviceTableEntry> entries) {
		this();
		
		model.setData(entries);
	}
	
	public void refreshDeviceTable(Set<DeviceTableEntry> entries) {
		model.setData(entries);
	}

}
