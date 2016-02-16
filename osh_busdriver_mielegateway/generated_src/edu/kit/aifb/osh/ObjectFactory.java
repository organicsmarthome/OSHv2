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


package edu.kit.aifb.osh;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the edu.kit.aifb.osh package. 
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

    private final static QName _GenericApplianceProfile_QNAME = new QName("http://www.aifb.kit.edu/osh", "GenericApplianceProfile");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: edu.kit.aifb.osh
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link XsdCtApplianceProgramConfigurations }
     * 
     */
    public XsdCtApplianceProgramConfigurations createXsdCtApplianceProgramConfigurations() {
        return new XsdCtApplianceProgramConfigurations();
    }

    /**
     * Create an instance of {@link XsdCtApplianceProgramConfiguration }
     * 
     */
    public XsdCtApplianceProgramConfiguration createXsdCtApplianceProgramConfiguration() {
        return new XsdCtApplianceProgramConfiguration();
    }

    /**
     * Create an instance of {@link XsdCtGenericApplianceProfile }
     * 
     */
    public XsdCtGenericApplianceProfile createXsdCtGenericApplianceProfile() {
        return new XsdCtGenericApplianceProfile();
    }

    /**
     * Create an instance of {@link XsdCtLoadProfiles }
     * 
     */
    public XsdCtLoadProfiles createXsdCtLoadProfiles() {
        return new XsdCtLoadProfiles();
    }

    /**
     * Create an instance of {@link XsdCtLoadProfilePause }
     * 
     */
    public XsdCtLoadProfilePause createXsdCtLoadProfilePause() {
        return new XsdCtLoadProfilePause();
    }

    /**
     * Create an instance of {@link XsdCtProgram }
     * 
     */
    public XsdCtProgram createXsdCtProgram() {
        return new XsdCtProgram();
    }

    /**
     * Create an instance of {@link XsdCtLoadProfileSegment }
     * 
     */
    public XsdCtLoadProfileSegment createXsdCtLoadProfileSegment() {
        return new XsdCtLoadProfileSegment();
    }

    /**
     * Create an instance of {@link XsdCtLoadProfile }
     * 
     */
    public XsdCtLoadProfile createXsdCtLoadProfile() {
        return new XsdCtLoadProfile();
    }

    /**
     * Create an instance of {@link XsdCtConfigurationParameter }
     * 
     */
    public XsdCtConfigurationParameter createXsdCtConfigurationParameter() {
        return new XsdCtConfigurationParameter();
    }

    /**
     * Create an instance of {@link XsdCtLoadProfilePhase }
     * 
     */
    public XsdCtLoadProfilePhase createXsdCtLoadProfilePhase() {
        return new XsdCtLoadProfilePhase();
    }

    /**
     * Create an instance of {@link XsdCtDescriptions }
     * 
     */
    public XsdCtDescriptions createXsdCtDescriptions() {
        return new XsdCtDescriptions();
    }

    /**
     * Create an instance of {@link XsdCtLoadParameters }
     * 
     */
    public XsdCtLoadParameters createXsdCtLoadParameters() {
        return new XsdCtLoadParameters();
    }

    /**
     * Create an instance of {@link XsdCtConfigurationParameters }
     * 
     */
    public XsdCtConfigurationParameters createXsdCtConfigurationParameters() {
        return new XsdCtConfigurationParameters();
    }

    /**
     * Create an instance of {@link XsdCtLoad }
     * 
     */
    public XsdCtLoad createXsdCtLoad() {
        return new XsdCtLoad();
    }

    /**
     * Create an instance of {@link XsdCtDescription }
     * 
     */
    public XsdCtDescription createXsdCtDescription() {
        return new XsdCtDescription();
    }

    /**
     * Create an instance of {@link XsdCtLoadProfileTick }
     * 
     */
    public XsdCtLoadProfileTick createXsdCtLoadProfileTick() {
        return new XsdCtLoadProfileTick();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XsdCtGenericApplianceProfile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.aifb.kit.edu/osh", name = "GenericApplianceProfile")
    public JAXBElement<XsdCtGenericApplianceProfile> createGenericApplianceProfile(XsdCtGenericApplianceProfile value) {
        return new JAXBElement<XsdCtGenericApplianceProfile>(_GenericApplianceProfile_QNAME, XsdCtGenericApplianceProfile.class, null, value);
    }

}
