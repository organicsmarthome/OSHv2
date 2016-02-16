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

package osh.simulation.screenplay;

import java.util.List;

/**
 * 
 * @author Ingo Mauser
 *
 */
public class ActionParametersHelper {

	public static String getValueForParameterOfParameters(
			PerformAction action, 
			String parametersName, 
			String parameterName) {
		String s = null;
		
		List<ActionParameters> parametersList = action.getActionParameterCollection();
		for (ActionParameters aps : parametersList) {
			if (aps.getParametersName().equals(parametersName)) {
				for (ActionParameter ap : aps.getParameter()) {
					if (ap.getName().equals(parameterName)) {
						s= ap.getValue();
					}
				}
			}
		}
		
		return s;
	}
	
//	public static void setValueForParameterOfParameters(
//			PerformAction action, 
//			String parametersName, 
//			String parameterName,
//			String value) {
//		
//		ActionParameters configurationActionParameters = new ActionParameters();
//		configurationActionParameters.setParametersName("appliance");
//		ActionParameter configurationActionParameter = new ActionParameter();
//		configurationActionParameter.setName("configuration");
//		configurationActionParameter.setValue("" + configurationForThisRun);
//		configurationActionParameters.getParameter().add(configurationActionParameter);
//		
//		
//	}
}
