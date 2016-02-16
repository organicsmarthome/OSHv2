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

//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2014.07.17 um 09:44:44 AM CEST 
//


package osh.configuration.system;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für DeviceTypes.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="DeviceTypes">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="HVAC"/>
 *     &lt;enumeration value="COMBINATIONAPPARATUS"/>
 *     &lt;enumeration value="AIRCONDITION"/>
 *     &lt;enumeration value="FANHEATER"/>
 *     &lt;enumeration value="DISHWASHER"/>
 *     &lt;enumeration value="DRYER"/>
 *     &lt;enumeration value="WASHERDRYER"/>
 *     &lt;enumeration value="WASHINGMACHINE"/>
 *     &lt;enumeration value="ELECTRICOVEN"/>
 *     &lt;enumeration value="ELECTRICSTOVE"/>
 *     &lt;enumeration value="ELECTRICCOOKTOP"/>
 *     &lt;enumeration value="INDUCTIONCOOKTOP"/>
 *     &lt;enumeration value="GASOVEN"/>
 *     &lt;enumeration value="GASSTOVE"/>
 *     &lt;enumeration value="GASCOOKTOP"/>
 *     &lt;enumeration value="COOKERHOOD"/>
 *     &lt;enumeration value="MICROWAVE"/>
 *     &lt;enumeration value="STEAMER"/>
 *     &lt;enumeration value="BREADMAKER"/>
 *     &lt;enumeration value="COFFEESYSTEM"/>
 *     &lt;enumeration value="COFFEEMAKER"/>
 *     &lt;enumeration value="FREEZERREFRIGERATOR"/>
 *     &lt;enumeration value="FREEZER"/>
 *     &lt;enumeration value="REFRIGERATOR"/>
 *     &lt;enumeration value="WINECOOLER"/>
 *     &lt;enumeration value="ELECTRICWATERBOILER"/>
 *     &lt;enumeration value="WATERHEATERINSTANTANEOUS"/>
 *     &lt;enumeration value="WATERHEATERSTORAGE"/>
 *     &lt;enumeration value="LUMINAIRE"/>
 *     &lt;enumeration value="ECAR"/>
 *     &lt;enumeration value="EBIKE"/>
 *     &lt;enumeration value="SEGWAY"/>
 *     &lt;enumeration value="SMARTMETER"/>
 *     &lt;enumeration value="TEMPERATURESENSOR"/>
 *     &lt;enumeration value="GASSENSOR"/>
 *     &lt;enumeration value="SMOKESENSOR"/>
 *     &lt;enumeration value="MOTIONSENSOR"/>
 *     &lt;enumeration value="CONTACTSENSOR"/>
 *     &lt;enumeration value="METERPLUG"/>
 *     &lt;enumeration value="SWITCHPLUG"/>
 *     &lt;enumeration value="METERSWITCHPLUG"/>
 *     &lt;enumeration value="VIRTUALSWITCH"/>
 *     &lt;enumeration value="CHPPLANT"/>
 *     &lt;enumeration value="PVSYSTEM"/>
 *     &lt;enumeration value="HEATPUMP"/>
 *     &lt;enumeration value="IR"/>
 *     &lt;enumeration value="BASELOAD"/>
 *     &lt;enumeration value="HOUSEHOLD"/>
 *     &lt;enumeration value="OTHER"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DeviceTypes")
@XmlEnum
public enum DeviceTypes {

    HVAC,
    COMBINATIONAPPARATUS,
    AIRCONDITION,
    FANHEATER,
    DISHWASHER,
    DRYER,
    WASHERDRYER,
    WASHINGMACHINE,
    ELECTRICOVEN,
    ELECTRICSTOVE,
    ELECTRICCOOKTOP,
    INDUCTIONCOOKTOP,
    GASOVEN,
    GASSTOVE,
    GASCOOKTOP,
    COOKERHOOD,
    MICROWAVE,
    STEAMER,
    BREADMAKER,
    COFFEESYSTEM,
    COFFEEMAKER,
    FREEZERREFRIGERATOR,
    FREEZER,
    REFRIGERATOR,
    WINECOOLER,
    ELECTRICWATERBOILER,
    WATERHEATERINSTANTANEOUS,
    WATERHEATERSTORAGE,
    LUMINAIRE,
    ECAR,
    EBIKE,
    SEGWAY,
    SMARTMETER,
    TEMPERATURESENSOR,
    GASSENSOR,
    SMOKESENSOR,
    MOTIONSENSOR,
    CONTACTSENSOR,
    METERPLUG,
    SWITCHPLUG,
    METERSWITCHPLUG,
    VIRTUALSWITCH,
    CHPPLANT,
    PVSYSTEM,
    HEATPUMP,
    IR,
    BASELOAD,
    HOUSEHOLD,
    OTHER;

    public String value() {
        return name();
    }

    public static DeviceTypes fromValue(String v) {
        return valueOf(v);
    }

}
