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
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für DeviceClassification.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="DeviceClassification">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="HVAC"/>
 *     &lt;enumeration value="APPLIANCE"/>
 *     &lt;enumeration value="LUMINAIRE"/>
 *     &lt;enumeration value="ELECTRICVEHICLE"/>
 *     &lt;enumeration value="METERING"/>
 *     &lt;enumeration value="SENSOR"/>
 *     &lt;enumeration value="SMARTPLUG"/>
 *     &lt;enumeration value="VIRTUALSWITCH"/>
 *     &lt;enumeration value="CHPPLANT"/>
 *     &lt;enumeration value="PVSYSTEM"/>
 *     &lt;enumeration value="HEATPUMP"/>
 *     &lt;enumeration value="BASELOAD"/>
 *     &lt;enumeration value="REMOTECONTROL"/>
 *     &lt;enumeration value="CUSTOMER"/>
 *     &lt;enumeration value="OTHER"/>
 *     &lt;enumeration value="N/A"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DeviceClassification")
@XmlEnum
public enum DeviceClassification {

    HVAC("HVAC"),
    APPLIANCE("APPLIANCE"),
    LUMINAIRE("LUMINAIRE"),
    ELECTRICVEHICLE("ELECTRICVEHICLE"),
    METERING("METERING"),
    SENSOR("SENSOR"),
    SMARTPLUG("SMARTPLUG"),
    VIRTUALSWITCH("VIRTUALSWITCH"),
    CHPPLANT("CHPPLANT"),
    PVSYSTEM("PVSYSTEM"),
    HEATPUMP("HEATPUMP"),
    BASELOAD("BASELOAD"),
    REMOTECONTROL("REMOTECONTROL"),
    CUSTOMER("CUSTOMER"),
    OTHER("OTHER"),
    @XmlEnumValue("N/A")
    N_A("N/A");
    private final String value;

    DeviceClassification(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DeviceClassification fromValue(String v) {
        for (DeviceClassification c: DeviceClassification.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
