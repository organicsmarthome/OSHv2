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

package osh.hal;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.TimeZone;
import java.util.UUID;

import osh.ILifeCycleListener;
import osh.OSHComponent;
import osh.configuration.OSHParameterCollection;
import osh.configuration.system.AssignedBusDevice;
import osh.configuration.system.AssignedComDevice;
import osh.configuration.system.AssignedDevice;
import osh.configuration.system.HALConfiguration;
import osh.core.IOSH;
import osh.core.IOSHOC;
import osh.core.OSH;
import osh.core.OSHRandomGenerator;
import osh.core.bus.AbstractBusManager;
import osh.core.com.AbstractComManager;
import osh.core.exceptions.OSHException;
import osh.core.oc.LocalController;
import osh.core.oc.LocalObserver;
import osh.core.threads.InvokerThreadRegistry;
import osh.hal.exceptions.HALManagerException;

/**
 * represents the manager of the HAL-layer
 * 
 * @author Florian Allerding
 * @category smart-home ControllerBox HAL
 * 
 */
public class HALManager extends OSHComponent implements ILifeCycleListener  {

	private HALConfiguration halConfig;
	
	private ArrayList<HALDeviceDriver> connectedDevices;
	private ArrayList<AbstractComManager> connectedComManagers;
	private ArrayList<AbstractBusManager> connectedBusManagers;
	
	public HALManager(OSH controllerbox) {
		super(controllerbox);
		
		connectedDevices = new ArrayList<HALDeviceDriver>();
		connectedComManagers = new ArrayList<AbstractComManager>();
		connectedBusManagers = new ArrayList<AbstractBusManager>();
	}
	
	protected OSH getControllerBoxObj() {
		return (OSH) getOSH();
	}

	public HALRealTimeDriver getRealTimeDriver() {
		return getOSH().getTimer();
	}

	public ArrayList<HALDeviceDriver> getConnectedDevices() {
		return connectedDevices;
	}


	// if you want to do 'real' things ;-)
	public void loadConfiguration(
			HALConfiguration halConfig,
			TimeZone hostTimeZone,
			long forcedStartTime) throws HALManagerException {
		
		boolean isSimulation = getOSH().getCbstatus().isSimulation();
		boolean runningVirtual = getOSH().getCbstatus().isRunningVirtual();
		getGlobalLogger().logInfo("...loading HAL configuration");
		this.halConfig = halConfig;
		if (halConfig == null) throw new NullPointerException("halConfig is null");
		
		
		// init real time driver and set the mode
		HALRealTimeDriver realTimeDriver = new HALRealTimeDriver(
				getGlobalLogger(),
				hostTimeZone,
				isSimulation,
				runningVirtual,
				1,
				forcedStartTime);
		getControllerBoxObj().setTimer(realTimeDriver);
		realTimeDriver.setThreadRegistry(new InvokerThreadRegistry(getControllerBoxObj()));

		getGlobalLogger().logInfo("...creating HAL-BUS-devices...");
		this.processBusDeviceConfiguration();
		getGlobalLogger().logInfo("...creating HAL-BUS-devices... [DONE]");

		getGlobalLogger().logInfo("...creating HAL-device-drivers");
		this.processDeviceDriverConfiguration(getOSH().getRandomGenerator());
		getGlobalLogger().logInfo("...creating HAL-device-drivers... [DONE]");

		getGlobalLogger().logInfo("...creating HAL-COM-devices...");
		this.processComDeviceConfiguration();
		getGlobalLogger().logInfo("...creating HAL-COM-devices... [DONE]");
		
		getGlobalLogger().logInfo("...HAL-layer is up!");
	}

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void processDeviceDriverConfiguration(OSHRandomGenerator halRandomGenerator) throws HALManagerException {
		
		for (int i = 0; i < this.halConfig.getAssignedDevices().size(); i++) {

			AssignedDevice _device = this.halConfig.getAssignedDevices().get(i);

			if( _device == null )
				throw new HALManagerException("configuration fail: assigned device is null!");
			
			// load driver parameter
			OSHParameterCollection drvParams = new OSHParameterCollection();
			drvParams.loadCollection(_device.getDriverParameters());

			// get the class of the driver an make an instance
			Class driverClass = null;
			try {
				driverClass = Class.forName(_device.getDriverClassName());
			} catch (ClassNotFoundException ex) {
				throw new HALManagerException(ex);
			}

			HALDeviceDriver _driver = null;
			try {
				Constructor<HALDeviceDriver> constructor = driverClass.getConstructor(
						IOSH.class, 
						UUID.class,
						OSHParameterCollection.class);
				_driver = (HALDeviceDriver) constructor.newInstance(
						getOSH(), 
						UUID.fromString(_device.getDeviceID()),
						drvParams);
				getGlobalLogger().logInfo("" + _driver.getClass().getSimpleName() + " - UUID - " + _device.getDeviceID() + " - Driver loaded ...... [OK]");
			}
			catch (InstantiationException iex) {
				throw new HALManagerException("Instantiation of " + driverClass + " failed!", iex);
			}
			catch (Exception ex) {
				throw new HALManagerException(ex);
			}

			_driver.setControllable(_device.isControllable());
			_driver.setObservable(_device.isObservable());
			_driver.setDeviceType(_device.getDeviceType());
			_driver.setDeviceClassification(_device.getDeviceClassification());
			// add driver to the list of connected devices
			connectedDevices.add(_driver);

			// assign the dispatcher
			//_driver.assignDispatcher(halDispatcher);

			// get the class to the controller and the observer and refer it for
			// the cbox-layer
			if (_device.isControllable()) {
				// ...the controller class
				String controllerClassName = _device.getAssignedLocalOCUnit()
						.getLocalControllerClassName();

				try {
					_driver
					.setRequiredLocalControllerClass((Class<LocalController>) Class
					.forName(controllerClassName));

				} catch (ClassNotFoundException ex) {
					throw new HALManagerException(ex);
				}
				getGlobalLogger().logInfo("" + _driver.getClass().getSimpleName() + " - UUID - " + _device.getDeviceID() + " - LocalController loaded ...... [OK]");
			}

			if (_device.isObservable()) {
				// ...and the observer class
				String observerClassName = _device.getAssignedLocalOCUnit()
						.getLocalObserverClassName();

				try {
					_driver
					.setRequiredLocalObserverClass((Class<LocalObserver>) Class
					.forName(observerClassName));
				} catch (ClassNotFoundException ex) {
					throw new HALManagerException(ex);
				}
				getGlobalLogger().logInfo("" + _driver.getClass().getSimpleName() + " - UUID - " + _device.getDeviceID() + " - LocalObserver loaded ...... [OK]");
			}
		}
	}
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void processComDeviceConfiguration() throws HALManagerException {
		for (int i = 0; i < this.halConfig.getAssignedComDevices().size(); i++){
			AssignedComDevice _device = this.halConfig.getAssignedComDevices().get(i);
			
			// load driver parameter
			OSHParameterCollection drvParams = new OSHParameterCollection();
			drvParams.loadCollection(_device.getComDriverParameters());
			
			// get the class of the driver an make an instance
			
			Class controllerClass = null;
			String controllerclassname = _device.getComManagerClassName();
			if (controllerclassname == null || controllerclassname.isEmpty()) {
				throw new HALManagerException("no com manager for driver " + _device.getComDeviceID() + " available.");
			}
			try {
				controllerClass = Class.forName(controllerclassname);
				}
			catch (ClassNotFoundException ex) {
				throw new HALManagerException(ex);
			}
			
			Class comDriverClass = null;
			try {
				comDriverClass = Class.forName(_device.getComDriverClassName());
			}
			catch (ClassNotFoundException ex) {
				throw new HALManagerException(ex);
			}
			
			AbstractComManager _comManager = null;
			try {
				_comManager = (AbstractComManager) controllerClass.getConstructor(
						IOSHOC.class, 
						UUID.class).newInstance(
								getOSH(), 
								UUID.fromString(_device.getComDeviceID()));
				
				getGlobalLogger().logInfo("" + _device.getClass().getSimpleName() + " - UUID - " + _device.getComDeviceID() + " - ComManager loaded ...... [OK]");
				
			}
			catch (Exception ex) {
				getGlobalLogger().logError("ERROR: initializing " + _device.getClass().getSimpleName() + " - UUID - " + _device.getComDeviceID() + " - ComManager loaded ...... [OK]");
				throw new HALManagerException(ex);
			}
			
			HALComDriver _comDriver = null;
			try {
				Constructor<HALComDriver> constructor = comDriverClass.getConstructor(
						IOSH.class, 
						UUID.class,
						OSHParameterCollection.class);
						
				_comDriver =  (HALComDriver) constructor.newInstance(
								getOSH(), 
								UUID.fromString(_device.getComDeviceID()),
								drvParams);
				getGlobalLogger().logInfo("" + _device.getClass().getSimpleName() + " - UUID - " + _device.getComDeviceID() + " - ComDriver loaded ...... [OK]");
			}
			catch (Exception ex) {
				throw new HALManagerException(ex);
			}
			
			_comManager.setOcDataSubscriber(_comDriver);
			_comDriver.setHalDataSubscriber(_comManager);
			
			_comDriver.setComDeviceType(_device.getComDeviceType());
		
			connectedComManagers.add(_comManager);
			
		}
	}

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void processBusDeviceConfiguration() throws HALManagerException {
		for (int i = 0; i < this.halConfig.getAssignedBusDevices().size(); i++){
			AssignedBusDevice _device = this.halConfig.getAssignedBusDevices().get(i);
			
			// load driver parameter
			OSHParameterCollection drvParams = new OSHParameterCollection();
			drvParams.loadCollection(_device.getBusDriverParameters());
			
			// get the class of the driver an make an instance
			
			Class controllerClass = null;
			String controllerclassname = _device.getBusManagerClassName();
			if (controllerclassname == null || controllerclassname.isEmpty()) {
				throw new HALManagerException("no com manager for driver " + _device.getBusDeviceID() + " available.");
			}
			try {
				controllerClass = Class.forName(controllerclassname);
			}
			catch (ClassNotFoundException ex) {
				throw new HALManagerException(ex);
			}
			
			Class busDriverClass = null;
			try {
				busDriverClass = Class.forName(_device.getBusDriverClassName());
			}
			catch (ClassNotFoundException ex) {
				throw new HALManagerException(ex);
			}
			
			AbstractBusManager _busManager = null;
			try {
				_busManager = (AbstractBusManager) controllerClass.getConstructor(
						IOSHOC.class, 
						UUID.class).newInstance(
								getOSH(), 
								UUID.fromString(_device.getBusDeviceID()));
				getGlobalLogger().logInfo("" + _device.getClass().getSimpleName() + " - UUID - " + _device.getBusDeviceID() + " - BusManager loaded ...... [OK]");
			}
			catch (Exception ex) {
				throw new HALManagerException(ex);
			}
			
			HALBusDriver _busDriver = null;
			try {
				Constructor<HALBusDriver> constructor = busDriverClass.getConstructor(
						IOSH.class, 
						UUID.class,
						OSHParameterCollection.class);
						
				_busDriver =  (HALBusDriver) constructor.newInstance(
								getOSH(), 
								UUID.fromString(_device.getBusDeviceID()),
								drvParams);
				getGlobalLogger().logInfo("" + _device.getClass().getSimpleName() + " - UUID - " + _device.getBusDeviceID() + " - BusDriver loaded ...... [OK]");
			}
			catch (Exception ex) {
				throw new HALManagerException(ex);
			}
			
			_busManager.setOcDataSubscriber(_busDriver);
			_busDriver.setHalDataSubscriber(_busManager);
			
			_busDriver.setBusDeviceType(_device.getBusDeviceType());
		
			connectedBusManagers.add(_busManager);
			
		}
	}
	

	public void startHAL() {
		//TODO: why is nothing here?
	}

	public void addDevice(HALDeviceDriver driver, String deviceDescription) {
		//TODO: why is nothing here?
	}

	public void removeDevice(HALDeviceDriver driver) {
		//TODO: why is nothing here?
	}
	
	public ArrayList<AbstractComManager> getConnectedComManagers() {
		return connectedComManagers;
	}
	
	public ArrayList<AbstractBusManager> getConnectedBusManagers() {
		return connectedBusManagers;
	}
	
	@Override
	public void onSystemError() throws OSHException {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSystemHalt() throws OSHException {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSystemRunning() throws OSHException {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSystemIsUp() throws OSHException {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSystemResume() throws OSHException {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSystemShutdown() throws OSHException {
		// TODO Auto-generated method stub
	}

}
