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

package osh.core;

import java.util.ArrayList;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;

import osh.ILifeCycleListener;
import osh.LifeCycleStates;
import osh.OSHComponent;
import osh.configuration.OSHParameterCollection;
import osh.configuration.controllerbox.OCConfiguration;
import osh.configuration.system.HALConfiguration;
import osh.core.bus.AbstractBusManager;
import osh.core.com.AbstractComManager;
import osh.core.exceptions.OCManagerException;
import osh.core.exceptions.OSHException;
import osh.core.oc.GlobalController;
import osh.core.oc.GlobalOCUnit;
import osh.core.oc.GlobalObserver;
import osh.core.oc.LocalController;
import osh.core.oc.LocalOCUnit;
import osh.core.oc.LocalObserver;
import osh.datatypes.XMLSerialization;
import osh.datatypes.logger.SystemLoggerConfiguration;
import osh.hal.HALDeviceDriver;
import osh.hal.HALManager;
import osh.registry.DriverRegistry;
import osh.registry.IRegistry;
import osh.registry.OCRegistry;

/**
 * for initialization an management of the controllerbox
 * 
 * @author Florian Allerding, Kaibin Bao, Till Schuberth, Ingo Mauser
 * @category smart-home ControllerBox
 * 
 */
public class OCManager extends OSHComponent {
	
	private OCConfiguration ocConfig;

	private UUID controllerBoxID;

	private OSHParameterCollection globalObserverParameterCollection;
	private OSHParameterCollection globalControllerParameterCollection;

	private HALManager halManager;

	private GlobalOCUnit globalOCunit;

	private ArrayList<LocalOCUnit> localOCUnits;
	private ArrayList<AbstractComManager> comManagers;
	private ArrayList<AbstractBusManager> busManagers;


	private ArrayList<HALDeviceDriver> deviceDrivers;

	
	/**
	 * Real House CB
	 */
	public OCManager(SystemLoggerConfiguration systemLoggerConfiguration) {
		this(new OSH(), systemLoggerConfiguration);
	}

	/**
	 * Simulation CB
	 * @param theOrganicSmartHome
	 * @param randomGenerator
	 */
	public OCManager(
			OSH theOrganicSmartHome,
			SystemLoggerConfiguration systemLoggerConfiguration) {
		// create new ControllerBox object
		super(theOrganicSmartHome);

		// create a new Logger with default LogLevel: error
		OSHGlobalLogger globalLogger;
		globalLogger = new OSHGlobalLogger(theOrganicSmartHome, systemLoggerConfiguration);
		getControllerBoxObj().setLogger(globalLogger);

	}

	@Override
	public IOSHFull getOSH() {
		return (IOSHFull) super.getOSH();
	}

	protected OSH getControllerBoxObj() {
		return (OSH) super.getOSH();
	}

	/**
	 * creates local o/c-unit based on the driver information. Only for devices
	 * witch are at least "Observable" such an instance will be created
	 * 
	 * @param deviceDrivers
	 * @return
	 */
	private ArrayList<LocalOCUnit> createLocalOCUnits(
			ArrayList<HALDeviceDriver> deviceDrivers)
			throws OCManagerException {

		ArrayList<LocalOCUnit> _localOCUnits = new ArrayList<LocalOCUnit>();

		getLogger().logInfo("...creating local units");

		for (HALDeviceDriver deviceDriver : deviceDrivers) {

			// is this device able to be observed or controlled?
			// ...then build an o/c-unit
			// otherwise do nothing ;-)
			LocalObserver localObserver = null;

			if (deviceDriver.isObservable()) {
				// getting the class for the local oc unit
				try {
					localObserver = (LocalObserver) deviceDriver
							.getRequiredLocalObserverClass()
							.getConstructor(IOSHOC.class)
							.newInstance(getOSH());
				} catch (Exception ex) {
					throw new OCManagerException(ex);
				}

				LocalController localController = null;

				if (deviceDriver.isControllable()) {
					try {
						localController = (LocalController) deviceDriver
								.getRequiredLocalControllerClass()
								.getConstructor(IOSHOC.class)
								.newInstance(getOSH());
					} catch (Exception ex) {
						throw new OCManagerException(ex);
					}

				}
				// init localunit and refer the realtime module

				// UUID id = _driver.getDeviceID();

				LocalOCUnit _localOCUnit = new LocalOCUnit(getOSH(),
						deviceDriver.getDeviceID(), localObserver,
						localController);

				// assign the type of the device an it's classification
				_localOCUnit.setDeviceType(deviceDriver.getDeviceType());
				_localOCUnit.setDeviceClassification(deviceDriver
						.getDeviceClassification());

				// register the local observer at the specific HAL-driver
				// (Observer Pattern/publish-subscribe)
				deviceDriver.setHalDataSubscriber(_localOCUnit.localObserver);

				// do the same with the local controller
				// Before check if the controller is available
				// (it's not available when the device is not controllable)
				if (_localOCUnit.localController != null) {
					_localOCUnit.localController.setOcDataSubscriber(deviceDriver);
				}

				// add the new unit
				_localOCUnits.add(_localOCUnit);
			}
		}

		return _localOCUnits;
	}

	/**
	 * will shutdown the controllerbox and announce the components, that the
	 * system is going down...
	 */
	public void doSystemShutdown() throws OCManagerException {
		// the local units
		try {
			getControllerBoxObj().getCbstatusObj().getLifeCycleManager()
					.switchToLifeCycleState(LifeCycleStates.ON_SYSTEM_SHUTDOWN);
		} catch (Exception ex) {
			throw new OCManagerException(ex);
		}
		getLogger().printSystemMessage("ControllerBox is going down...");
	}

	/**
	 * get the unique ID for the controllerbox or the household (one
	 * controllerbox per household...))
	 * 
	 * @return
	 */
	public UUID getControllerBoxID() {
		return controllerBoxID;
	}

	/**
	 * get the assigned drivers to the controllerbox
	 * 
	 * @return
	 */
	public ArrayList<HALDeviceDriver> getDeviceDrivers() {
		return deviceDrivers;
	}

	/**
	 * get the HALManager of the controllerbox
	 * 
	 * @return
	 */
	public HALManager getHalManager() {
		return halManager;
	}
	
	public OCConfiguration getOcConfig() {
		return ocConfig;
	}

	/**
	 * get all members of the lifecycle-process. Used by the lifecycle-manager
	 * 
	 * @return
	 */
	protected ArrayList<ILifeCycleListener> getLifeCycleMembers() {

		ArrayList<ILifeCycleListener> boxLifeCycleMembers = new ArrayList<ILifeCycleListener>();

		// device drivers
		for (HALDeviceDriver halDevicedriver : this.deviceDrivers) {
			boxLifeCycleMembers.add(halDevicedriver);
		}

		// com drivers
		for (AbstractComManager comManager : this.comManagers) {
			boxLifeCycleMembers.add(comManager);
			boxLifeCycleMembers.add(comManager.getComDriver());
		}
		
		// bus drivers
		for (AbstractBusManager busManager : this.busManagers) {
			boxLifeCycleMembers.add(busManager);
			boxLifeCycleMembers.add(busManager.getBusDriver());
		}

		// OC-units for device drivers
		for (LocalOCUnit localOCUnit : localOCUnits) {

			if (localOCUnit.localObserver != null) {
				boxLifeCycleMembers
						.add(localOCUnit.localObserver);
			}
			if (localOCUnit.localController != null) {
				boxLifeCycleMembers
						.add(localOCUnit.localController);
			}
		}

		boxLifeCycleMembers.add(halManager);
		boxLifeCycleMembers.add(globalOCunit.getObserver());
		boxLifeCycleMembers.add(globalOCunit.getController());

		return boxLifeCycleMembers;
	}

	/**
	 * Gets all device-drivers and com-drivers. Necessary for the
	 * simulation-engine to get the connection between the controllerbox and the
	 * simulation-layer
	 * 
	 * @return
	 */
	public ArrayList<OSHComponent> getAllDrivers() {

		ArrayList<OSHComponent> simulationSubjects = new ArrayList<OSHComponent>();

		// add the device drivers (appliances, car, ...)
		for (HALDeviceDriver halDeviceDriver : this.deviceDrivers) {
			simulationSubjects.add(halDeviceDriver);
		}
		// add the com drivers (communication: electric utility, ...)
		for (AbstractComManager acc : comManagers) {
			simulationSubjects.add((OSHComponent) acc.getComDriver());
		}
		// add the bus drivers (busses with single connection to OSH: EEBus, WAGO controller, ...)
		for (AbstractBusManager abm : busManagers) {
			simulationSubjects.add((OSHComponent) abm.getBusDriver());
		}

		return simulationSubjects;
	}

	/**
	 * initialize the ControllerBox based on the given configuration files
	 * 
	 * @param halConfigFile
	 * @param controllerBoxConfigFile
	 * @param loggingConfiguration
	 * @param forcedStartTime
	 * @param randomSeed
	 * @param runID
	 * @param packageID
	 * @param configurationID
	 * @throws Exception 
	 */
	public void initControllerBox(
			String halConfigFile,
			String controllerBoxConfigFile,
			TimeZone hostTimeZone,
			long forcedStartTime,
			Long randomSeed, 
			String runID,
			String configID)
			throws OCManagerException {
		initControllerBox(
				halConfigFile, 
				controllerBoxConfigFile,
				hostTimeZone,
				forcedStartTime, 
				randomSeed, 
				runID,
				configID,
				null);
	}

	/**
	 * initialize the ControllerBox based on the given configuration files
	 * 
	 * @param halConfigFile
	 * @param controllerBoxConfigFile
	 * @param forcedStartTime
	 * @param randomSeed
	 * @param runID
	 * @param configurationID
	 * @param hhUUID
	 * @throws Exception 
	 */
	public void initControllerBox(
			String halConfigFile,
			String controllerBoxConfigFile, 
			TimeZone hostTimeZone,
			long forcedStartTime,
			Long randomSeed, 
			String runID,
			String configurationID,
			IRegistry dsmRegistry) throws OCManagerException {

		// load from files:
		HALConfiguration halconfig = null;
		try {
			halconfig = (HALConfiguration) XMLSerialization.file2Unmarshal(
					halConfigFile, HALConfiguration.class);
		} catch (Exception ex) {
			getLogger().logError("can't load HAL-configuration", ex);
			throw new OCManagerException(ex);
		}
		this.ocConfig = null;
		try {
			ocConfig = (OCConfiguration) XMLSerialization
					.file2Unmarshal(
							controllerBoxConfigFile,
							OCConfiguration.class);
		} 
		catch (Exception ex) {
			getLogger().logError("can't load ControllerBox-configuration", ex);
			throw new OCManagerException(ex);
		}

		if (randomSeed == null && ocConfig.getRandomSeed() != null) {
			randomSeed = Long.valueOf(ocConfig.getRandomSeed());
		}
		if (randomSeed == null) {
			getLogger()
					.logError(
							"No randomSeed available: neither in CBConfig nor as Startup parameter - using default random seed!");
			getLogger()
					.printDebugMessage(
							"No randomSeed available: Using default seed \"0xd1ce5bL\"");
			randomSeed = 0xd1ce5bL;
		}

		getControllerBoxObj().setRandomGenerator(
				new OSHRandomGenerator(new Random(randomSeed)));
		getLogger().logInfo("Using random seed 0x" + Long.toHexString(randomSeed));

		initControllerBox(
				halconfig, 
				ocConfig, 
				hostTimeZone,
				forcedStartTime, 
				runID,
				configurationID);
	}

	/**
	 * initialize the ControllerBox based on the given configuration objects
	 * 
	 * @param halConfig
	 * @param ocConfig
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initControllerBox(
			HALConfiguration halConfig,
			OCConfiguration ocConfig,
			TimeZone hostTimeZone,
			long forcedStartTime, 
			String runID,
			String configurationID) throws OCManagerException {


		getLogger().logInfo("...initializing OC Manager (ControllerBox)");
		
		if (ocConfig.isSimulation() && ocConfig.isRunnigVirtual()) {
			throw new OCManagerException("Setting the configuration variable runningVirtual does only make sense when running in the real scenario.");
		}

		// set some status variables...
		getControllerBoxObj().getCbstatusObj().setIsSimulation(ocConfig.isSimulation());
		getControllerBoxObj().getCbstatusObj().setVirtual(ocConfig.isRunnigVirtual());
		// info: record simulation is already set in the constructor
		getControllerBoxObj().getCbstatusObj().setRunID(runID);
		getControllerBoxObj().getCbstatusObj().setConfigurationID(configurationID);
		

		// create a HAL manager first
		this.halManager = new HALManager(getControllerBoxObj());

		// init life-cycle-manager
		getLogger().logInfo("...setting up lifecyle-management");
		getControllerBoxObj().getCbstatusObj().setLifeCycleManager(
				new OSHLifeCycleManager(this, halManager));

		// init the HAL
		try {
			getLogger().logInfo("...starting HAL-layer");
			halManager.loadConfiguration(halConfig, hostTimeZone, forcedStartTime);
		} 
		catch (Exception ex) {
			throw new OCManagerException(ex);
		}
		
		// load drivers
		deviceDrivers = halManager.getConnectedDevices();
		
		// then start the OC layer
		comManagers = halManager.getConnectedComManagers();
		busManagers = halManager.getConnectedBusManagers();

		// create local OC-Unit connected with the specific HAL-driver
		localOCUnits = createLocalOCUnits(deviceDrivers);

		// load global o/c-unit
		ocConfig.getGlobalControllerClass();
		GlobalObserver globalObserver = null;
		GlobalController globalController = null;
		getLogger().logInfo("...creating global O/C-units");
		Class globalObserverClass = null;
		Class globalControllerClass = null;

		globalObserverParameterCollection = new OSHParameterCollection();
		globalObserverParameterCollection.loadCollection(ocConfig
				.getGlobalObserverParameters());

		globalControllerParameterCollection = new OSHParameterCollection();
		globalControllerParameterCollection.loadCollection(ocConfig
				.getGlobalControllerParameters());

		try {
			globalObserverClass = Class.forName(ocConfig.getGlobalObserverClass());
			globalControllerClass = Class.forName(ocConfig.getGlobalControllerClass());
		} catch (Exception ex) {
			throw new OCManagerException(ex);
		}

		try {
			globalObserver = (GlobalObserver) globalObserverClass
					.getConstructor(IOSHOC.class,
							OSHParameterCollection.class).newInstance(
							getOSH(),
							globalObserverParameterCollection);
		} catch (Exception ex) {
			throw new OCManagerException(ex);
		}

		try {
			globalController = (GlobalController) globalControllerClass
					.getConstructor(IOSHOC.class,
							OSHParameterCollection.class).newInstance(
							getOSH(),
							globalControllerParameterCollection);
		} catch (Exception ex) {
			throw new OCManagerException(ex);
		}
		getControllerBoxObj().setGlobalObserver(globalObserver);
		getControllerBoxObj().setGlobalController(globalController);

		// assign OCRegistry (O/C communication above HAL)
		OCRegistry ocRegistry = new OCRegistry(getControllerBoxObj());
		getControllerBoxObj().setOCRegistry(ocRegistry);

		// assign DriverRegistry (DeviceDriver, ComDriver and BusDriver communication below HAL)
		DriverRegistry driverRegistry = new DriverRegistry(getControllerBoxObj());
		getControllerBoxObj().setDriverRegistry(driverRegistry);
		
		// create global ControllerBox
		this.globalOCunit = new GlobalOCUnit(
				UUID.fromString(ocConfig.getGlobalOcUuid()),
				getOSH(), 
				globalObserver, 
				globalController);

		registerLocalUnits();

		
		// notify the units, that the system is up
		try {
			getControllerBoxObj().getCbstatusObj().getLifeCycleManager()
					.switchToLifeCycleState(LifeCycleStates.ON_SYSTEM_IS_UP);
			getLogger().logInfo("...ControllerBox is up!");
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(0);
			throw new OCManagerException(ex);
		}

		//start all component threads (do not start earlier, otherwise components
		//get callbacks before the system is up. This can lead to evil race conditions.
		getControllerBoxObj().getDriverRegistry().startQueueProcessingThreads();
		getControllerBoxObj().getOCRegistry().startQueueProcessingThreads();
		
		getControllerBoxObj().getTimer().startTimerProcessingThreads();

	}

	/**
	 * register local o/c-units at the controllerbox (global o/c-unit)
	 */
	private void registerLocalUnits() {
		for (LocalOCUnit _localunit : localOCUnits) {
			try {
				globalOCunit.registerLocalUnit(_localunit);
			} catch (OSHException e) {
				getGlobalLogger().logError("", e);
			}
		}
	}

	/**
	 * set the unique ID for the controllerbox or the household (one
	 * controllerbox per household...))
	 * 
	 * @param controllerBoxID
	 */
	public void setControllerBoxID(UUID controllerBoxID) {
		this.controllerBoxID = controllerBoxID;
	}

	/**
	 * Switch to life-cycle-state "error"
	 */
	public void setSystemError() throws OCManagerException {
		try {
			getControllerBoxObj().getCbstatusObj().getLifeCycleManager()
					.switchToLifeCycleState(LifeCycleStates.ON_SYSTEM_ERROR);
		} catch (OSHException ex) {
			throw new OCManagerException(ex);
		}
	}

	/**
	 * Switch to life-cycle-state "halt"
	 */
	public void setSystemHalt() throws OCManagerException {
		try {
			getControllerBoxObj().getCbstatusObj().getLifeCycleManager()
					.switchToLifeCycleState(LifeCycleStates.ON_SYSTEM_HALT);
		} catch (OSHException ex) {
			throw new OCManagerException(ex);
		}
	}

	/**
	 * Switch to life-cycle-state "resume"
	 */
	public void setSystemResume() throws OCManagerException {
		try {
			getControllerBoxObj().getCbstatusObj().getLifeCycleManager()
					.switchToLifeCycleState(LifeCycleStates.ON_SYSTEM_RESUME);
		} catch (OSHException ex) {
			throw new OCManagerException(ex);
		}
	}

	/**
	 * Switch to life-cycle-state "running"
	 */
	public void setSystemRunning() throws OCManagerException {
		try {
			getControllerBoxObj().getCbstatusObj().getLifeCycleManager()
					.switchToLifeCycleState(LifeCycleStates.ON_SYSTEM_RUNNING);
		} catch (OSHException ex) {
			throw new OCManagerException(ex);
		}
	}

	/**
	 * start the controllerbox in the "real smart-home mode"
	 */
	public void startSystem() throws OCManagerException {

		new Thread(this.halManager.getRealTimeDriver()).start();
		try {
			getControllerBoxObj().getCbstatusObj().getLifeCycleManager()
					.switchToLifeCycleState(LifeCycleStates.ON_SYSTEM_RUNNING);
		} catch (OSHException ex) {
			throw new OCManagerException(ex);
		}
		getLogger().printSystemMessage("...System started");

	}

	/**
	 * get the global logger => here the configuration of the logger can be done
	 * during the runtime.
	 * 
	 * @return
	 */
	public OSHGlobalLogger getLogger() {
		return getOSH().getLogger();
	}

}
