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

package osh.driver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import osh.configuration.OSHParameterCollection;
import osh.core.IOSH;
import osh.core.com.interfaces.IHasState;
import osh.core.exceptions.OSHException;
import osh.datatypes.registry.EventExchange;
import osh.datatypes.registry.commands.ChpElectricityRequest;
import osh.datatypes.registry.driver.details.chp.raw.DachsDriverDetails;
import osh.driver.dachs.DachsInformationRequestThread;
import osh.hal.exchange.HALControllerExchange;


/**
 * 
 * @author Florian Allerding, Kaibin Bao, Till Schuberth, Ingo Mauser
 *
 */
public abstract class DachsDriver extends ChpDriver implements IHasState {

	protected String dachsURL;
	protected String loginName;
	protected String loginPwd;
	
	private DachsInformationRequestThread reqRunnable;
	private Thread reqThread;
	
	protected Double waterStorageTemperature = null;
	
	private ArrayList<String> parametersToGet = new ArrayList<String>(Arrays.asList(new String[] {
			// Betriebsdaten Dachs
			"Hka_Bd.Anforderung.ModulAnzahl",
			"Hka_Bd.Anforderung.UStromF_Anf.bFlagSF",
			"Hka_Bd.UStromF_Frei.bFreigabe",
			"Hka_Bd.bStoerung",
			"Hka_Bd.bWarnung",
			"Hka_Bd.UHka_Anf.Anforderung.fStrom",
			"Hka_Bd.UHka_Anf.usAnforderung",
			"Hka_Bd.UHka_Frei.usFreigabe",
			"Hka_Bd.ulArbeitElektr",
			"Hka_Bd.ulArbeitThermHka",
//			"Hka_Bd.ulArbeitThermKon", // n.a.
			"Hka_Bd.ulBetriebssekunden",
			"Hka_Bd.ulAnzahlStarts",
//			"Hka_Bd_Stat.uchSeriennummer,"  // static
//			"Hka_Bd_Stat.uchTeilenummer", // static
//			"Hka_Bd_Stat.ulInbetriebnahmedatum", // static
			
			// Betriebsdaten 31.12.
			"BD3112.Hka_Bd.ulBetriebssekunden",
			"BD3112.Hka_Bd.ulAnzahlStarts",
			"BD3112.Hka_Bd.ulArbeitElektr",
			"BD3112.Hka_Bd.ulArbeitThermHka",
//			"BD3112.Hka_Bd.ulArbeitThermKon", // n.a.
			"BD3112.Ww_Bd.ulWwMengepA",
			
			// Daten 2. Wärmeerzeuger (SEplus) // KIT
			"Brenner_Bd.bIstStatus",
			"Brenner_Bd.bWarnung",
			"Brenner_Bd.UBrenner_Anf.usAnforderung",
			"Brenner_Bd.UBrenner_Frei.bFreigabe",
			"Brenner_Bd.ulAnzahlStarts",
			"Brenner_Bd.ulBetriebssekunden",
			
			// Hydraulik Schema
//			"Hka_Ew.HydraulikNr.bSpeicherArt", // static
//			"Hka_Ew.HydraulikNr.bWW_Art", // static
//			"Hka_Ew.HydraulikNr.b2_Waermeerzeuger", // static
//			"Hka_Ew.HydraulikNr.bMehrmodul", // static
			
			// Temperaturen
			"Hka_Mw1.Temp.sAbgasHKA",
			"Hka_Mw1.Temp.sAbgasMotor",
			"Hka_Mw1.Temp.sKapsel",
			"Hka_Mw1.Temp.sbAussen",
			"Hka_Mw1.Temp.sbFreigabeModul",
			"Hka_Mw1.Temp.sbFuehler1",
			"Hka_Mw1.Temp.sbFuehler2",
			"Hka_Mw1.Temp.sbGen",
			"Hka_Mw1.Temp.sbMotor",
			"Hka_Mw1.Temp.sbRegler",
			"Hka_Mw1.Temp.sbRuecklauf",
			"Hka_Mw1.Temp.sbVorlauf",
			"Hka_Mw1.Temp.sbZS_Fuehler3",
			"Hka_Mw1.Temp.sbZS_Fuehler4",
			"Hka_Mw1.Temp.sbZS_Vorlauf1",
			"Hka_Mw1.Temp.sbZS_Vorlauf2",
			"Hka_Mw1.Temp.sbZS_Warmwasser",
			"Hka_Mw1.Solltemp.sbRuecklauf",
			"Hka_Mw1.Solltemp.sbVorlauf",
			
			// Aktoren
//			"Hka_Mw1.Aktor.bWwPumpe",
//			"Hka_Mw1.Aktor.fFreiAltWaerm",
//			"Hka_Mw1.Aktor.fMischer1Auf",
			 // ...
			"Hka_Mw1.sWirkleistung",
			"Hka_Mw1.ulMotorlaufsekunden",
			"Hka_Mw1.usDrehzahl",
			
			// Tageslauf
			 // ...
			
			// Informationen über Wartung
			"Wartung_Cache.fStehtAn",
//			"Wartung_Cache.ulBetriebssekundenBei", // quasi-static
//			"Wartung_Cache.ulZeitstempel", // quasi-static
//			"Wartung_Cache.usIntervall" // quasi-static
			}));
	
	
	/**
	 * CONSTRUCTOR
	 * @param controllerbox
	 * @param deviceID
	 * @param driverConfig
	 * @throws OSHException 
	 */
	public DachsDriver(
			IOSH controllerbox, 
			UUID deviceID,
			OSHParameterCollection driverConfig) throws OSHException {
		super(controllerbox, deviceID, driverConfig);
		
		String dachsHost = driverConfig.getParameter("dachshost");
		String dachsPort = driverConfig.getParameter("dachsport");
		if ( dachsHost == null 
				|| dachsPort == null 
				|| dachsHost.length() == 0 
				|| dachsPort.length() == 0) {
			throw new OSHException("Invalid Dachs Host or Port");
		}
		this.dachsURL = "http://" + dachsHost + ":" + dachsPort + "/";
		
		this.loginName = driverConfig.getParameter("dachsloginname");
		this.loginPwd = driverConfig.getParameter("dachsloginpwd");
		
		this.setMinimumRuntime(30 * 60); // 30 minutes
	}
	
	
	@Override
	public void onSystemIsUp() throws OSHException {
		super.onSystemIsUp();
		
		getTimer().registerComponent(this, 300);
		
		// init request thread
		reqRunnable = new DachsInformationRequestThread(
				getGlobalLogger(), 
				this, dachsURL,
				this.loginName,
				this.loginPwd, 
				this.parametersToGet);
		reqThread = new Thread(reqRunnable, "DachsInformationRequestThread");
		reqThread.start();
	}
	
	@Override
	public void onNextTimePeriod() throws OSHException {
		super.onNextTimePeriod();
		
		// still alive message
		if (getTimer().getUnixTime() % 60 == 0) {
			getGlobalLogger().logDebug("onNextTimePeriod() (getTimer().getUnixTime() % 60 == 0) - I'm still alive");
		}
		
		// re-init request thread if dead
		if (reqThread == null || !reqThread.isAlive()) {
			reqRunnable = new DachsInformationRequestThread(
					getGlobalLogger(), 
					this, dachsURL,
					this.loginName,
					this.loginPwd,
					this.parametersToGet);
			reqThread = new Thread(reqRunnable, "DachsInformationRequestThread");
			reqThread.start();
		}
	} 
	
	@Override
	public void onSystemShutdown() throws OSHException {
		super.onSystemShutdown();
		
		reqRunnable.shutdown();
	}
	
	@Override
	protected void onControllerRequest(HALControllerExchange controllerRequest) {
		super.onControllerRequest(controllerRequest);
		
		// in ChpDriver
	}

	// for callback of DachsInformationRequestThread
	public abstract void processDachsDetails(DachsDriverDetails dachsDetails);
	
	protected Integer parseIntegerStatus(String value) {
		if (value == null || value.equals("")) return null;
		
		int i;
		try {
			i = Integer.parseInt(value);
			return i;
		} 
		catch (NumberFormatException e) {
			return null;
		}
	}
	
	protected Double parseDoubleStatus(String value) {
		if (value == null || value.equals("")) return null;
		
		double i;
		try {
			i = Double.parseDouble(value);
			return i;
		} 
		catch (NumberFormatException e) {
			return null;
		}
	}

	@Override
	public void onQueueEventReceived(EventExchange event)
			throws OSHException {
		
		if (event instanceof ChpElectricityRequest) {
			ChpElectricityRequest ceq = (ChpElectricityRequest) event;
			
			getGlobalLogger().logDebug("onQueueEventReceived(ChpElectricityRequest)");
			getGlobalLogger().logDebug("sendPowerRequestToChp(" + ceq.isOn() + ")");
			
			setElectricityRequest( ceq.isOn() );
			
			sendPowerRequestToChp();
		}
	}
	
}
