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

package osh.core.oc;

import java.util.HashMap;
import java.util.Map;

import osh.core.IOSHOC;


@Deprecated
public abstract class LocalParameterizedController extends LocalController {

	private Map<String,String> runtimeParameters = new HashMap<String, String>();
	
	public LocalParameterizedController(IOSHOC controllerbox) {
		super(controllerbox);
	}

	/**
	 * Get all runtime parameters for local unit (like dof)
	 * 
	 * @return
	 */
	public Map<String, String> getRuntimeParameters() {
		return runtimeParameters;
	}

	/**
	 * Get one runtime parameter for local unit (like dof)
	 * 
	 * @return
	 */
	public String getRuntimeParameter(String key) {
		return runtimeParameters.get(key);
	}

	/**
	 * Set all runtime parameters for local unit
	 * 
	 * @return
	 */
	public void setRuntimeParameters(Map<String, String> runtimeParameters) {
		this.runtimeParameters = runtimeParameters;
	}

	/**
	 * Set one runtime parameter for local unit
	 * 
	 * @return
	 */
	public void setRuntimeParameters(String key, String value) {
		this.runtimeParameters.put(key, value);
	}
	

}
