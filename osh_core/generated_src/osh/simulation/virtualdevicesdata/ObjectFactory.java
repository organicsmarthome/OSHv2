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
// Generiert: 2014.07.17 um 10:39:31 AM CEST 
//


package osh.simulation.virtualdevicesdata;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the osh.simulation.virtualdevicesdata package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: osh.simulation.virtualdevicesdata
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ProfileTick }
     * 
     */
    public ProfileTick createProfileTick() {
        return new ProfileTick();
    }

    /**
     * Create an instance of {@link DeviceProfile }
     * 
     */
    public DeviceProfile createDeviceProfile() {
        return new DeviceProfile();
    }

    /**
     * Create an instance of {@link ProfileTicks }
     * 
     */
    public ProfileTicks createProfileTicks() {
        return new ProfileTicks();
    }

    /**
     * Create an instance of {@link ProfileTick.Load }
     * 
     */
    public ProfileTick.Load createProfileTickLoad() {
        return new ProfileTick.Load();
    }

    /**
     * Create an instance of {@link ProfileTick.Parameters }
     * 
     */
    public ProfileTick.Parameters createProfileTickParameters() {
        return new ProfileTick.Parameters();
    }

}