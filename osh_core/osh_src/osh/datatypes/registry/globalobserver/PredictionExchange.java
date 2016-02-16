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

package osh.datatypes.registry.globalobserver;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import osh.datatypes.registry.StateExchange;
import osh.datatypes.registry.localobserver.IAction;


/**
 * This exchange stores the predicted actions for each device
 * 
 * @author Florian Allerding, Kaibin Bao
 *
 */
@XmlAccessorType( XmlAccessType.FIELD )
@XmlType(name = "prediction")
@XmlRootElement
public class PredictionExchange extends StateExchange {

	/** for JAXB */
	@SuppressWarnings("unused")
	@Deprecated
	private PredictionExchange() { this(null, 0); }

	public PredictionExchange(UUID sender, long timestamp) {
		super(sender, timestamp);
	}

	@XmlAnyElement
	private List<IAction> predictedActions = new ArrayList<IAction>();
	
	public List<IAction> getPredictedActions() {
		return predictedActions;
	}
	
	public void setPredictedActions(List<IAction> predictedActions) {
		this.predictedActions = predictedActions;
	}
}
