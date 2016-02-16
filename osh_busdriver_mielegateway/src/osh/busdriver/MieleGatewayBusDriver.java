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

package osh.busdriver;

import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;

import osh.busdriver.mielegateway.MieleGatewayDispatcher;
import osh.busdriver.mielegateway.data.MieleApplianceRawData;
import osh.busdriver.mielegateway.data.MieleDeviceHomeBusData;
import osh.busdriver.mielegateway.data.MieleDuration;
import osh.configuration.OSHParameterCollection;
import osh.core.IOSH;
import osh.core.com.interfaces.IEventReceiver;
import osh.core.exceptions.OSHException;
import osh.datatypes.en50523.EN50523DeviceState;
import osh.datatypes.registry.CommandExchange;
import osh.datatypes.registry.EventExchange;
import osh.datatypes.registry.commands.StartDeviceRequest;
import osh.datatypes.registry.commands.StopDeviceRequest;
import osh.datatypes.registry.commands.SwitchRequest;
import osh.datatypes.registry.details.common.BusDeviceStatusDetails;
import osh.datatypes.registry.details.common.BusDeviceStatusDetails.ConnectionStatus;
import osh.datatypes.registry.details.common.StartTimeDetails;
import osh.datatypes.registry.driver.details.appliance.GenericApplianceDriverDetails;
import osh.datatypes.registry.driver.details.appliance.GenericApplianceProgramDriverDetails;
import osh.datatypes.registry.driver.details.appliance.miele.MieleApplianceDriverDetails;
import osh.hal.HALBusDriver;
import osh.hal.exchange.IHALExchange;
import osh.utils.uuid.UUIDGenerationHelper;


/**
 * Busdriver for Miele Homebus Gateway at KIT
 * @author Ingo Mauser, Kaibin Bao
 *
 */
//FIXME: Error handling, if device is (temporarily not found by Miele GW)
public class MieleGatewayBusDriver extends HALBusDriver implements Runnable {
	
	private MieleGatewayDispatcher mieleGatewayDispatcher = null;
	
	private String mieleGatewayHost;
	private InetAddress mieleGatewayAddr;
	
	private String mieleGatewayUsername;
	private String mieleGatewayPassword;
	
	private Map<UUID, Map<String, String>> deviceProperties;

	/**
	 * CONSTRUCTOR
	 * @param controllerbox
	 * @param deviceID
	 * @param driverConfig
	 * @throws UnknownHostException 
	 */
	public MieleGatewayBusDriver(
			IOSH controllerbox,
			UUID deviceID, 
			OSHParameterCollection driverConfig) throws UnknownHostException {
		super(controllerbox, deviceID, driverConfig);
		
		this.mieleGatewayHost = driverConfig.getParameter("mielegatewayhost");
		this.mieleGatewayAddr = InetAddress.getByName(mieleGatewayHost);
		
		this.mieleGatewayUsername = driverConfig.getParameter("mielegatewayusername");
		this.mieleGatewayPassword = driverConfig.getParameter("mielegatewaypassword");
		
		deviceProperties = new HashMap<UUID, Map<String,String>>();
	}
	
	
	@Override
	public void onSystemIsUp() throws OSHException {
		super.onSystemIsUp();
		
		// connect to Miele gateway
		mieleGatewayDispatcher = new MieleGatewayDispatcher(
				this.mieleGatewayHost,
				this.mieleGatewayUsername,
				this.mieleGatewayPassword,
				getGlobalLogger());
		
		new Thread(this, "push proxy of Miele bus driver " + mieleGatewayHost).start();
	}
	
	@Override
	public void run() {
		while (true) {
			synchronized (mieleGatewayDispatcher) {
				try { // wait for new data
					mieleGatewayDispatcher.wait();
				} 
				catch (InterruptedException e) {
					getGlobalLogger().logError("should not happen", e);
					break;
				}
				
				long timestamp = getTimer().getUnixTime();
				
				if ( mieleGatewayDispatcher.getDeviceData().isEmpty() ) { // an error has occurred
					for( UUID uuid : deviceProperties.keySet() ) {
						BusDeviceStatusDetails bs = new BusDeviceStatusDetails(uuid, timestamp);
						bs.setState(ConnectionStatus.ERROR);
						getDriverRegistry().setStateOfSender(BusDeviceStatusDetails.class, bs);
					}
				}
				
				for (MieleDeviceHomeBusData dev : mieleGatewayDispatcher.getDeviceData()) {
					// build UUID
					long uuidHigh = UUIDGenerationHelper.getMieleUUIDHigherPart( dev.getUid() );
					long uuidLow;
					try {
						uuidLow = UUIDGenerationHelper.getHomeApplianceUUIDLowerPart((short)dev.getDeviceClass(), mieleGatewayAddr);
					} 
					catch (Exception e) {
						getGlobalLogger().logError("should not happen: UUID generation failed", e);
						continue;
					}
					final UUID devUUID = new UUID(uuidHigh, uuidLow);
					
					// register UUID as command receiver to the registry
					if( !deviceProperties.containsKey(devUUID) ) { // device already known?
						IEventReceiver eventReceiver = new IEventReceiver() {
							@Override
							public Object getSyncObject() {
								return MieleGatewayBusDriver.this;
							}
							
							@Override
							public void onQueueEventReceived(EventExchange event)
									throws OSHException {
								MieleGatewayBusDriver.this.onQueueEventReceived(event);
							}
							
							@Override
							public UUID getUUID() {
								return devUUID;
							}
						};
						
						// register device
						try {
							getDriverRegistry().register(StartDeviceRequest.class, eventReceiver);
							getDriverRegistry().register(StopDeviceRequest.class, eventReceiver);
							getDriverRegistry().register(SwitchRequest.class, eventReceiver);
							deviceProperties.put(devUUID, new HashMap<String, String>());
						} catch (OSHException e) {
							// nop. happens.
							getGlobalLogger().logError("should not happen", e);
						}
					}

					// check if all data is available
					BusDeviceStatusDetails bs = new BusDeviceStatusDetails(devUUID, timestamp);
					if( dev.getDeviceDetails() == null ) { 
						bs.setState(ConnectionStatus.ERROR);
						getDriverRegistry().setStateOfSender(BusDeviceStatusDetails.class, bs);
						continue;
					} else {
						bs.setState(ConnectionStatus.ATTACHED);
						getDriverRegistry().setStateOfSender(BusDeviceStatusDetails.class, bs);
					}
					
					// create program details
					GenericApplianceProgramDriverDetails programdetails = new GenericApplianceProgramDriverDetails(devUUID, timestamp);
					programdetails.setLoadProfiles(null);
					programdetails.setProgramName(dev.getDeviceDetails().getProgramName());
					programdetails.setPhaseName(dev.getDeviceDetails().getPhaseName());
					
					// create Miele specific details
					  // duration
					MieleApplianceDriverDetails mieledetails = new MieleApplianceDriverDetails(devUUID, timestamp);
					if( dev.getDuration() != null )
						mieledetails.setExpectedProgramDuration(dev.getDuration().duration()*60);
					else
						mieledetails.setExpectedProgramDuration(-1);

					  // remaining time
					if( dev.getRemainingTime() != null )
						mieledetails.setProgramRemainingTime(dev.getRemainingTime().duration()*60);
					else
						mieledetails.setProgramRemainingTime(-1);
					
					  // start time
					if( dev.getStartTime() != null ) {
						Calendar cal = Calendar.getInstance();
						long nowInMillies = getTimer().getUnixTime() * 1000L;
						
						cal.setTimeInMillis(nowInMillies);
						
						cal.setTimeZone(getTimer().getHostTimeZone());
						
						cal.set(Calendar.HOUR_OF_DAY, dev.getStartTime().hour());
						cal.set(Calendar.MINUTE, dev.getStartTime().minute());
						cal.set(Calendar.SECOND, 0);

						if( cal.getTimeInMillis() <= nowInMillies )
							cal.add(Calendar.DAY_OF_YEAR, 1);
					
						mieledetails.setStartTime( cal.getTimeInMillis() / 1000L );
					} else
						mieledetails.setStartTime(-1);

					
					// set state of the UUID
					try {
						getDriverRegistry().setStateOfSender(
								GenericApplianceDriverDetails.class,
								createApplianceDetails(devUUID, timestamp, dev));
						getDriverRegistry().setStateOfSender(
								StartTimeDetails.class,
								createStartTimeDetails(devUUID, timestamp, dev));
						getDriverRegistry().setStateOfSender(
								GenericApplianceProgramDriverDetails.class,
								programdetails);
						getDriverRegistry().setStateOfSender(
								MieleApplianceDriverDetails.class,
								mieledetails);
						
					} catch (OSHException e1) {
						bs.setState(ConnectionStatus.ERROR);
						getDriverRegistry().setStateOfSender(BusDeviceStatusDetails.class, bs);

						getGlobalLogger().logError(e1);
					}
					
					// extract additional information for invoking commands
					String detailsUrl = dev.getDetailsUrl();
					// extract type and id from details url
					if( detailsUrl != null ) {
						Map<String, String> devProps = deviceProperties.get(devUUID);
						try {
							URIBuilder uri = new URIBuilder( detailsUrl );
							for( NameValuePair pair : uri.getQueryParams() ) {
								if( "type".equals(pair.getName()) || "id".equals(pair.getName()) ) {
									devProps.put(pair.getName(), pair.getValue());
								}
							}
						} catch (URISyntaxException e) {
							// nop. shit happens.
							getGlobalLogger().logError("should not happen", e);
						}
					}
				}
			}
		}		
	}


	@Override
	public void updateDataFromBusManager(IHALExchange exchangeObject) {
		// NOTHING
	}

	static private StartTimeDetails createStartTimeDetails(
			final UUID devUUID, 
			long timestamp,
			MieleDeviceHomeBusData dev) {
		
		StartTimeDetails startDetails = new StartTimeDetails(devUUID, timestamp);
		startDetails.setStartTime(-1);

		if ( dev.getDeviceDetails() != null ) {
			MieleDuration mieleStartTime = dev.getDeviceDetails().getStartTime();
			
			if ( mieleStartTime != null ) {
				int starttime = mieleStartTime.duration();
				
				if ( starttime >= 0 ) {
					Calendar calNow = Calendar.getInstance();
					Calendar calStartTime = (Calendar) calNow.clone();
					
					calStartTime.set(Calendar.MINUTE, starttime % 60);
					calStartTime.set(Calendar.HOUR_OF_DAY, starttime / 60);
					
					if ( calStartTime.before(calNow) ) {
						calStartTime.add(Calendar.DAY_OF_YEAR, 1);
					}
					
					startDetails.setStartTime(calStartTime.getTimeInMillis()/1000L);
				}
			}
		}
		
		return startDetails;
	}
	
	static private GenericApplianceDriverDetails createApplianceDetails( 
			UUID uuid, 
			long timestamp, 
			MieleDeviceHomeBusData dev ) throws OSHException {
		
		GenericApplianceDriverDetails details = new GenericApplianceDriverDetails(uuid, timestamp); 
		MieleApplianceRawData devDetails = dev.getDeviceDetails();
		
		if ( devDetails == null ) {
			throw new OSHException("can't get device details");
		}
		
		if (devDetails.getStartCommandUrl() == null && dev.getState() == EN50523DeviceState.PROGRAMMED) {
			details.setState(EN50523DeviceState.STANDBY);
		}
		else {
			details.setState(dev.getState());
		}
		
		//FIXME
//		if( devDetails.getStartCommandUrl() != null ) {
//			details.setAction("Start", devDetails.getStartCommandUrl().toString());
//		}
//			
//		if ( devDetails.getStopCommandUrl() != null ) {
//			details.setAction("Stop", devDetails.getStopCommandUrl().toString());
//		}
			
		
		//FIXME
//		details.setProgramName(devDetails.getProgramName());
//		details.setPhaseName(devDetails.getPhaseName());
//		
//		if ( devDetails.getRemainingTime() != null ) {
//			details.setProgramRemainingTime(devDetails.getRemainingTime().duration());
//		}
//		else {
//			details.setProgramRemainingTime(-1);
//		}
		
		return details;
	}

	public void onQueueEventReceived(EventExchange event) throws OSHException {
		if ( event instanceof CommandExchange ) {
			UUID devUUID = ((CommandExchange) event).getReceiver();
			Map<String, String> devProps = deviceProperties.get(devUUID);
			
			if ( devProps != null ) { // known device?
				if ( devProps.containsKey("type") && devProps.containsKey("id") ) {
					URIBuilder builder = new URIBuilder();
					if ( event instanceof StartDeviceRequest ) {
						builder
							.setScheme("http")
							.setHost(this.mieleGatewayHost)
							.setPath("/homebus/device")
							.setParameter("type", devProps.get("type"))
							.setParameter("id", devProps.get("id"))
							.setParameter("action", "start");
		//						.setParameter("actionId", "start")
		//						.setParameter("argumentCount", "0");
					} else {
						if ( event instanceof StopDeviceRequest ) {
							builder
								.setScheme("http")
								.setHost(this.mieleGatewayHost)
								.setPath("/homebus/device")
								.setParameter("type", devProps.get("type"))
								.setParameter("id", devProps.get("id"))
								.setParameter("action", "stop");
						} else {
							if ( event instanceof SwitchRequest ) {
								builder
									.setScheme("http")
									.setHost(this.mieleGatewayHost)
									.setPath("/homebus/device")
									.setParameter("type", devProps.get("type"))
									.setParameter("id", devProps.get("id"))
									.setParameter("action", ((SwitchRequest) event).isTurnOn()?"switchOn":"switchOff");
							} else {
								return;
							}
						}
					}
					
					try {
						mieleGatewayDispatcher.sendCommand(builder.build().toString());
					} 
					catch (URISyntaxException e) {
						getGlobalLogger().logWarning("miele gateway disconnected?", e);
					}
				}
			}
		}
	}

	/* CURRENTLY NOT IN USE (BUT KEEP IT!)
	private final int MIELE_GW_UID_HOMEBUS = 0x48425553; // "HBUS" for HOMEBUS
	private final short MIELE_GW_APPLIANCE_TYPE = 0x4757; // "GW"
	public UUID getUUID() {
		long uuidHigh = getUUIDHigherPart(MIELE_GW_UID_HOMEBUS, MIELE_BRAND_AND_MANUFACTURER_ID, MIELE_BRAND_AND_MANUFACTURER_ID);
		long uuidLow;
		try {
			uuidLow = getUUIDLowerPart(MIELE_GW_APPLIANCE_TYPE, mieleGatewayAddr);
		} catch (ControllerBoxException e) {
			getGlobalLogger().logError("should not happen", e);
			return null;
		}
		
		return new UUID( uuidHigh, uuidLow );
	}
	*/
	
	
}
