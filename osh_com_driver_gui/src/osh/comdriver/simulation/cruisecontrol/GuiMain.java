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

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import javax.swing.JFrame;

import osh.comdriver.simulation.cruisecontrol.stateviewer.StateViewer;
import osh.comdriver.simulation.cruisecontrol.stateviewer.StateViewerListener;
import osh.datatypes.Commodity;
import osh.datatypes.VirtualCommodity;
import osh.datatypes.cruisecontrol.GUIWaterStorageStateExchange;
import osh.datatypes.cruisecontrol.PowerSum;
import osh.datatypes.ea.Schedule;
import osh.datatypes.gui.DeviceTableEntry;
import osh.datatypes.limit.PowerLimitSignal;
import osh.datatypes.limit.PriceSignal;
import osh.datatypes.registry.StateExchange;
import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.CLocation;
import bibliothek.gui.dock.common.DefaultSingleCDockable;
import bibliothek.gui.dock.common.SingleCDockable;
import bibliothek.gui.dock.common.location.CContentAreaCenterLocation;
import bibliothek.gui.dock.common.location.TreeLocationRoot;
import bibliothek.gui.dock.common.theme.ThemeMap;


/**
 * 
 * @author Till Schuberth
 *
 */
public class GuiMain {

	private JFrame rootframe;
	
	private CruiseControl cruisecontrol;
	private ScheduleDrawer scheduledrawer;
	private WaterTemperatureDrawer waterdrawer;
	private PowerDrawer powersumsdrawer;
	private DeviceTable devicetable;
	private StateViewer stateviewer;
	
	private final boolean ismultithread;
	/**
	 * CONSTRUCTOR
	 */
	public GuiMain(boolean ismultithread) {
		this.ismultithread = ismultithread;
		
		this.rootframe = new JFrame("Cruise Control");
		this.rootframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		CControl control = new CControl( this.rootframe );
		this.rootframe.setLayout(new BorderLayout());
		this.rootframe.add( control.getContentArea(), BorderLayout.CENTER );
        control.setTheme(ThemeMap.KEY_ECLIPSE_THEME);
		
		this.scheduledrawer = createScheduledrawer();
		this.waterdrawer = createWaterdrawer();
		this.powersumsdrawer = createPowerSumsdrawer();
		this.devicetable = createDevicetable();
		this.stateviewer = createStateViewer();
		// IMPORTANT: CruiseControl must be the last one to be in front of the others!
		this.cruisecontrol = createCruisecontrol();
		
		SingleCDockable scheduledock = new DefaultSingleCDockable("scheduledrawer", "Schedule", this.scheduledrawer);
		SingleCDockable waterdock = new DefaultSingleCDockable("waterdrawer", this.waterdrawer.getName(), this.waterdrawer);
		SingleCDockable powersumsdock = new DefaultSingleCDockable("powersumsdrawer", this.powersumsdrawer.getName(), this.powersumsdrawer);
		SingleCDockable devicetabledock = new DefaultSingleCDockable("devicetable", "Device Table", this.devicetable);
		SingleCDockable stateviewerdock = new DefaultSingleCDockable("stateviewer", "Registry State Viewer", this.stateviewer);
		SingleCDockable cruisecontroldock = new DefaultSingleCDockable("cruisecontrol", "Cruise Control", this.cruisecontrol);
		
		control.addDockable( scheduledock );
		control.addDockable( waterdock );
		control.addDockable( powersumsdock );
		control.addDockable( devicetabledock );
		control.addDockable( stateviewerdock );
		control.addDockable( cruisecontroldock );
		
		CContentAreaCenterLocation normal = CLocation.base().normal();
		
		scheduledock.setLocation(normal);
		waterdock.setLocation(normal.stack());
        powersumsdock.setLocation(normal.stack());
        
        TreeLocationRoot south = CLocation.base().normalSouth(0.4);
        
        devicetabledock.setLocation(south);
        stateviewerdock.setLocation(south.stack());

        TreeLocationRoot north = CLocation.base().normalNorth(0.1);

        cruisecontroldock.setLocation(north);
        
		scheduledock.setVisible(true);
		waterdock.setVisible(true);
		powersumsdock.setVisible(true);
		devicetabledock.setVisible(true);
		stateviewerdock.setVisible(true);
		cruisecontroldock.setVisible(true);

		this.rootframe.pack();
		this.rootframe.setBounds( 50, 50, 1000, 700 );
		this.rootframe.setVisible(true);
	}
	
	ScheduleDrawer getScheduledrawer() {
		return scheduledrawer;
	}
	
	WaterTemperatureDrawer getWaterdrawer() {
		return waterdrawer;
	}

	private PowerDrawer getPowerSumsDrawer() {
		return powersumsdrawer;
	}
	
	CruiseControl getCruisecontrol() {
		return cruisecontrol;
	}
	
	DeviceTable getDevicetable() {
		return devicetable;
	}
	StateViewer getStateViewer() {
		return stateviewer;
	}
	
	private static ScheduleDrawer createScheduledrawer() {
		ScheduleDrawer drawer = new ScheduleDrawer();
    	return drawer;
	}
	
	private static WaterTemperatureDrawer createWaterdrawer() {
		WaterTemperatureDrawer drawer = new WaterTemperatureDrawer();
    	return drawer;
	}
	
	private PowerDrawer createPowerSumsdrawer() {
		PowerDrawer drawer = new PowerDrawer();
    	return drawer;
	}

	private CruiseControl createCruisecontrol() {
		CruiseControl showcontrol = new CruiseControl(!ismultithread);
    	return showcontrol;
	}
	
	private static DeviceTable createDevicetable() {
		DeviceTable devlist = new DeviceTable();
		return devlist;
	}
	
	private static StateViewer createStateViewer() {
		return new StateViewer();
	}
	
	public void waitForUserIfRequested() {
		if (cruisecontrol.isWait()) {
			cruisecontrol.waitForGo();
		}
	}

	public void updateTime(long timestamp) {
		cruisecontrol.updateTime(timestamp);
	}
	
	public void refreshDiagram(
			List<Schedule> schedules, 
			HashMap<VirtualCommodity,PriceSignal> ps, 
			HashMap<VirtualCommodity,PowerLimitSignal> pls,
			long time,
			boolean saveGraph) {

		if (getCruisecontrol().isUpdate()) {
			getScheduledrawer().refreshDiagram(
					schedules, 
					ps, 
					pls,
					time,
					saveGraph);
		}
	}
	
	public void refreshDeviceTable(Set<DeviceTableEntry> entries) {
		getDevicetable().refreshDeviceTable(entries);
	}
	
	public void refreshStateTable(Set<Class<? extends StateExchange>> types, Map<UUID, ? extends StateExchange> states) {
		getStateViewer().showTypes(types);
		getStateViewer().showStates(states);
	}
	
	public void refreshWaterDiagram(TreeMap<Long, GUIWaterStorageStateExchange> waterstoragehistory) {
		if (waterstoragehistory.size() > 0) {
			getWaterdrawer().refreshDiagram(waterstoragehistory);
		}
	}

	public void refreshPowerSumDiagram(long now, HashMap<Commodity, TreeMap<Long, PowerSum>> commodityPowerSum) {
		if (commodityPowerSum.size() > 0) {
			getPowerSumsDrawer().refreshDiagram(now, commodityPowerSum);
		}
	}

	public void registerListener(StateViewerListener l) {
		stateviewer.registerListener(l);
	}
	
}
