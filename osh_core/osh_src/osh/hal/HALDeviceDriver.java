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

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import osh.configuration.OSHParameterCollection;
import osh.configuration.system.DeviceClassification;
import osh.configuration.system.DeviceTypes;
import osh.core.IOSH;
import osh.core.exceptions.OSHException;
import osh.core.oc.IOCDataSubscriber;
import osh.core.oc.LocalController;
import osh.core.oc.LocalObserver;
import osh.datatypes.registry.details.common.ConfigurationDetails;
import osh.datatypes.registry.details.common.ConfigurationDetails.ConfigurationStatus;
import osh.datatypes.registry.details.common.DeviceMetaDriverDetails;
import osh.hal.exceptions.HALDriverException;
import osh.hal.exceptions.HALException;
import osh.hal.exchange.HALControllerExchange;
import osh.hal.exchange.HALExchange;
import osh.hal.exchange.IHALExchange;

/**
 * superclass for the driver between the controllerbox and the IDevice each
 * IDevice driver has to extend it!
 * 
 * @author Florian Allerding, Ingo Mauser
 * @category smart-home ControllerBox HAL
 */
public abstract class HALDeviceDriver extends HALDriver implements IDriverDataPublisher, IOCDataSubscriber {

	private HALControllerExchange controllerDataObject;
	
	private Class<LocalObserver> requiredLocalObserverClass;
	private Class<LocalController> requiredLocalControllerClass;
	private IDriverDataSubscriber assignedLocalObserver;
	
	private boolean observable;
	private boolean controllable;
	
	// ### DeviceMetaDriverDetails ###
	private String name;
	private String location;
	private String icon;
	
	private DeviceClassification deviceClassification;
	private DeviceTypes deviceType;
	private boolean configured;
	
	
	private boolean intelligent;
	
	
	private ArrayList<UUID> meterUuids = null;
	private ArrayList<UUID> switchUuids = null;
	
	
	/**
	 * CONSTRUCTOR
	 * @param controllerbox
	 * @param deviceID
	 * @param driverConfig
	 */
	public HALDeviceDriver(
			IOSH controllerbox, 
			UUID deviceID,
			OSHParameterCollection driverConfig) {
		super(controllerbox, deviceID, driverConfig);
		
		this.name = driverConfig.getParameter("name");
		this.location = driverConfig.getParameter("location");
		this.icon = driverConfig.getParameter("icon");
		
		this.meterUuids = new ArrayList<>();
		this.switchUuids = new ArrayList<>();
		
		// Add meter uuids (sources for metering/measurement
		String cfgMeterSources = driverConfig.getParameter("metersources");
		if( cfgMeterSources != null ) {
			ArrayList<UUID> meterSources;
			try {
				meterSources = parseUUIDArray( cfgMeterSources );
				for (UUID uuid : meterSources) {
					this.getMeterUuids().add(uuid);
				}
			} 
			catch (OSHException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void onSystemIsUp() throws OSHException {
		super.onSystemIsUp();
		
		// set data sources as configured
		DeviceMetaDriverDetails deviceMetaDriverDetails = new DeviceMetaDriverDetails(
				getDeviceID(), 
				getTimer().getUnixTime());
		deviceMetaDriverDetails.setName(name);
		deviceMetaDriverDetails.setLocation(location);
		deviceMetaDriverDetails.setIcon(icon);
		deviceMetaDriverDetails.setDeviceClassification(deviceClassification);
		deviceMetaDriverDetails.setDeviceType(deviceType);
		deviceMetaDriverDetails.setConfigured(configured);
		
		getDriverRegistry().setStateOfSender(DeviceMetaDriverDetails.class, deviceMetaDriverDetails);
	}


	/**
	 * Simplify the notification to the local observer
	 * @param observerExchange
	 * @throws HALDriverException 
	 */
	public void notifyObserver(IHALExchange observerExchange) {
		try {
			this.updateHalDataSubscriber((HALExchange) observerExchange);
		} 
		catch (HALDriverException e) {
			this.getGlobalLogger().logError("HAL communication error", e);
		}
	}

	/**
	 * invoked from the local controller when the device has to something
	 * @param controllerRequest contains what the device has to do...
	 * @throws HALException 
	 */
	protected abstract void onControllerRequest(HALControllerExchange controllerRequest) throws HALException;


	// HALdataObject (from former HALcontrollerDriver)
	
	@Override
	public void onDataFromOcComponent(IHALExchange controllerObject) {
		
		if (controllerObject instanceof HALControllerExchange) {
			this.controllerDataObject = (HALControllerExchange) controllerObject;
		}
		try {
			changeDeviceState();
		} catch (HALException e) {

			throw new RuntimeException("Uncaught HAL-Exception...Bad boy (...girl...transsexual...) !!! You will a big time out !!!",e);
		}
	
	}
	
	private void changeDeviceState() throws HALException{
		onControllerRequest(this.controllerDataObject);
	}
	
	
	// HALdataSubject (from former HALobserverDriver)
	
	@Override
	public void setHalDataSubscriber(IDriverDataSubscriber observerObject) {
		this.assignedLocalObserver = observerObject;
	}

	@Override
	public void removeHalDataSubscriber(IDriverDataSubscriber observerObject) {
		//FIXME add removal routine
	}
	
	@Override
	public void updateHalDataSubscriber(IHALExchange observerExchange) throws HALDriverException {
		if ( this.assignedLocalObserver != null ) {
			try {
				this.assignedLocalObserver.onDataFromHalDriver(observerExchange);
			} 
			catch (OSHException e) {
				throw new HALDriverException(e);
			}
		}
	}

	
	/* HELPER FUNCTIONS */
	public void setDataSourcesConfigured(Collection<UUID> uuids) {
		for( UUID uuid : uuids ) {
			ConfigurationDetails cd = new ConfigurationDetails(uuid, getTimer().getUnixTime());
			cd.setConfigurationStatus(ConfigurationStatus.CONFIGURED);
			getDriverRegistry().setStateOfSender(ConfigurationDetails.class, cd);
		}
	}
	
	public void setDataSourcesUsed(Collection<UUID> uuids) {
		for( UUID uuid : uuids ) {
			ConfigurationDetails cd = new ConfigurationDetails(uuid, getTimer().getUnixTime());
			cd.setConfigurationStatus(ConfigurationStatus.USED);
			cd.setUsedBy(getDeviceID());
			getDriverRegistry().setStateOfSender(ConfigurationDetails.class, cd);
		}
	}
	
	
	public DeviceClassification getDeviceClassification() {
		return deviceClassification;
	}
	
	protected void setDeviceClassification(
			DeviceClassification deviceClassification) {
		this.deviceClassification = deviceClassification;
	}


	public DeviceTypes getDeviceType() {
		return deviceType;
	}
	
	protected void setDeviceType(DeviceTypes deviceType) {
		this.deviceType = deviceType;
	}


	public Class<LocalController> getRequiredLocalControllerClass() {
		return requiredLocalControllerClass;
	}
	
	protected void setRequiredLocalControllerClass(
			Class<LocalController> requiredLocalControllerClass) {
		this.requiredLocalControllerClass = requiredLocalControllerClass;
	}
	

	public Class<LocalObserver> getRequiredLocalObserverClass() {
		return requiredLocalObserverClass;
	}
	
	protected void setRequiredLocalObserverClass(
			Class<LocalObserver> requiredLocalObserverClass) {
		this.requiredLocalObserverClass = requiredLocalObserverClass;
	}
	

	public boolean isControllable() {
		return controllable;
	}
	
	protected void setControllable(boolean controllable) {
		this.controllable = controllable;
	}
	

	public boolean isIntelligent() {
		return intelligent;
	}
	
	protected void setIntelligent(boolean isIntelligent) {
		this.intelligent = isIntelligent;
	}

	
	public boolean isObservable() {
		return observable;
	}
	
	protected void setObservable(boolean observable) {
		this.observable = observable;
	}


	public ArrayList<UUID> getMeterUuids() {
		return meterUuids;
	}
	
	protected void addMeterUUID(UUID meterUUID) {
		this.meterUuids.add(meterUUID);
	}


	public ArrayList<UUID> getSwitchUuids() {
		return switchUuids;
	}

	protected void addSwitchUUID(UUID switchUUID) {
		this.switchUuids.add(switchUUID);
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}
	
}
