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

package osh.comdriver.simulation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import osh.configuration.OSHParameterCollection;
import osh.core.IOSH;
import osh.datatypes.VirtualCommodity;
import osh.datatypes.limit.PriceSignal;
import osh.hal.exchange.EpsComExchange;
import osh.simulation.SimulationComDriver;
import osh.simulation.exception.SimulationSubjectException;


/**
 * 
 * @author Florian Allerding
 *
 */
public class CsvEpsProviderSimulationComDriver extends SimulationComDriver {
	
	private static final long newSignalAfterThisPeriod = 12 * 60 * 60; //new Signal every 12 hours
	private static final int accuracyPriceSignal = 1; //One hour
	//private static final int accuracyPriceSignal = 4; //Quarter of an hour
	private static String filePathPriceSignal = "configfiles/externalSignal/priceDynamic.csv";
	private List<Double> priceSignalYear;
	private int priceKnownHoursAdvance = 36;
	

	/**
	 * CONSTRUCTOR
	 * @throws SimulationSubjectException
	 */
	public CsvEpsProviderSimulationComDriver(IOSH controllerbox,
			UUID deviceID, OSHParameterCollection driverConfig)
			throws SimulationSubjectException {
		super(controllerbox, deviceID, driverConfig);
		priceSignalYear = new ArrayList<Double>();
	}

	
	private void readCsvPriceSignal() {
		try {
			BufferedReader csvReader = new BufferedReader(new FileReader(new File(filePathPriceSignal)));
			String priceSignalLine;
			while ((priceSignalLine = csvReader.readLine()) != null){
				String[] splitLine = priceSignalLine.split(";");
				priceSignalYear.add(new Double(splitLine[0]));
			}
			csvReader.close();
		} 
		catch (Exception e) { // TODO: rethrow and handle
			e.printStackTrace();
		}
	}

	@Override
	public void onSimulationIsUp() {
		super.onSimulationIsUp();
		readCsvPriceSignal();
		generateNewPriceSignal();
	}

	private void generateNewPriceSignal() {		//gibt Preissignal für die nächsten 36 Stunden 
		int priceSignalFrom = (int) ((this.getTimer().getUnixTime()/3600)*accuracyPriceSignal);
		int priceSignalTo = priceKnownHoursAdvance  * accuracyPriceSignal + priceSignalFrom;
		PriceSignal powerPriceSignal = new PriceSignal(VirtualCommodity.ACTIVEPOWEREXTERNAL);
		PriceSignal gasPriceSignal = new PriceSignal(VirtualCommodity.NATURALGASPOWEREXTERNAL);
		for (int i = priceSignalFrom; i < priceSignalTo; i++){
			if(priceSignalYear.size() <= i){
				powerPriceSignal.setPrice((i/accuracyPriceSignal)*3600, 0.0);
			}
			else{
				powerPriceSignal.setPrice((i/accuracyPriceSignal)*3600, priceSignalYear.get(i));
			}
		}
		powerPriceSignal.setKnownPriceInterval((priceSignalFrom/accuracyPriceSignal)*3600, (priceSignalTo/accuracyPriceSignal)*3600);
		powerPriceSignal.compress();
		gasPriceSignal.setKnownPriceInterval((priceSignalFrom/accuracyPriceSignal)*3600, (priceSignalTo/accuracyPriceSignal)*3600);
		gasPriceSignal.setPrice((priceSignalFrom/accuracyPriceSignal)*3600, 0);
		
		//now sending priceSignal
		Map<VirtualCommodity,PriceSignal> priceSignals = new HashMap<>();
		
		priceSignals.put(powerPriceSignal.getCommodity(), powerPriceSignal);
		priceSignals.put(gasPriceSignal.getCommodity(), gasPriceSignal);
	
		EpsComExchange ex = new EpsComExchange(
				this.getDeviceID(), 
				this.getTimer().getUnixTime(), 
				priceSignals);
		this.updateHalDataSubscriber(ex);
	}
	
	//Backup - falls was doch nicht so funktioniert...
	//gibt Preissignal für die nächsten 36 Stunden - Stundengenaues Signal
	
//	private void generateNewPriceSignalHour() {										
																					
//		int priceSignalFrom = (int) ((this.getTimer().getUnixTime()/3600));
//		int priceSignalTo = 36 + priceSignalFrom;
//		PriceSignal priceSignal = new PriceSignal(VirtualCommodity.ACTIVEPOWEREXTERNAL);
//		for (int i = priceSignalFrom; i < priceSignalTo; i++){
//			if(priceSignalYear.size() <= i){
//				priceSignal.setPrice((i*3600), 0.0);
//			}
//			else{
//				priceSignal.setPrice((i*3600), priceSignalYear.get(i));
//			}
//		}
//		priceSignal.setKnownPriceInterval((priceSignalFrom*3600), (priceSignalTo*3600));
//		priceSignal.compress();
//		
//		//now sending priceSignal
//		Map<VirtualCommodity,PriceSignal> priceSignals = new HashMap<>();
//		priceSignals.put(priceSignal.getCommodity(),priceSignal);
//	
//		EpsComExchange ex = new EpsComExchange(
//				this.getDeviceID(), 
//				this.getTimer().getUnixTime(), 
//				priceSignals);
//		this.updateHalDataSubscriber(ex);
//	}

	@Override
	public void onNextTimeTick() {
		super.onNextTimeTick();
		if (getTimer().getUnixTime() % newSignalAfterThisPeriod == 0){
			generateNewPriceSignal();
		}
	}
	
}
