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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java-Klasse für xsdCt.ApplianceProgramConfiguration complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="xsdCt.ApplianceProgramConfiguration">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ConfigurationID" type="{http://www.aifb.kit.edu/osh}xsdSt.NonNegativeInt"/>
 *         &lt;element name="ConfigurationName" type="{http://www.aifb.kit.edu/osh}xsdSt.Name" minOccurs="0"/>
 *         &lt;element name="Program" type="{http://www.aifb.kit.edu/osh}xsdCt.Program"/>
 *         &lt;element name="Parameters" type="{http://www.aifb.kit.edu/osh}xsdCt.ConfigurationParameters" minOccurs="0"/>
 *         &lt;element name="LoadProfiles" type="{http://www.aifb.kit.edu/osh}xsdCt.LoadProfiles"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "xsdCt.ApplianceProgramConfiguration", propOrder = {
    "configurationID",
    "configurationName",
    "program",
    "parameters",
    "loadProfiles"
})
public class XsdCtApplianceProgramConfiguration implements Cloneable
{

    @XmlElement(name = "ConfigurationID")
    protected int configurationID;
    @XmlElement(name = "ConfigurationName")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String configurationName;
    @XmlElement(name = "Program", required = true)
    protected XsdCtProgram program;
    @XmlElement(name = "Parameters")
    protected XsdCtConfigurationParameters parameters;
    @XmlElement(name = "LoadProfiles", required = true)
    protected XsdCtLoadProfiles loadProfiles;

    /**
     * Creates a new {@code XsdCtApplianceProgramConfiguration} instance.
     * 
     */
    public XsdCtApplianceProgramConfiguration() {
        // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
        super();
    }

    /**
     * Creates a new {@code XsdCtApplianceProgramConfiguration} instance by deeply copying a given {@code XsdCtApplianceProgramConfiguration} instance.
     * 
     * 
     * @param o
     *     The instance to copy.
     * @throws NullPointerException
     *     if {@code o} is {@code null}.
     */
    public XsdCtApplianceProgramConfiguration(final XsdCtApplianceProgramConfiguration o) {
        // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
        super();
        if (o == null) {
            throw new NullPointerException("Cannot create a copy of 'XsdCtApplianceProgramConfiguration' from 'null'.");
        }
        // CBuiltinLeafInfo: java.lang.Integer
        this.configurationID = o.getConfigurationID();
        // CBuiltinLeafInfo: java.lang.String
        this.configurationName = ((o.configurationName == null)?null:o.getConfigurationName());
        // CClassInfo: edu.kit.aifb.osh.XsdCtProgram
        this.program = ((o.program == null)?null:((o.getProgram() == null)?null:o.getProgram().clone()));
        // CClassInfo: edu.kit.aifb.osh.XsdCtConfigurationParameters
        this.parameters = ((o.parameters == null)?null:((o.getParameters() == null)?null:o.getParameters().clone()));
        // CClassInfo: edu.kit.aifb.osh.XsdCtLoadProfiles
        this.loadProfiles = ((o.loadProfiles == null)?null:((o.getLoadProfiles() == null)?null:o.getLoadProfiles().clone()));
    }

    /**
     * Ruft den Wert der configurationID-Eigenschaft ab.
     * 
     */
    public int getConfigurationID() {
        return configurationID;
    }

    /**
     * Legt den Wert der configurationID-Eigenschaft fest.
     * 
     */
    public void setConfigurationID(int value) {
        this.configurationID = value;
    }

    /**
     * Ruft den Wert der configurationName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConfigurationName() {
        return configurationName;
    }

    /**
     * Legt den Wert der configurationName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConfigurationName(String value) {
        this.configurationName = value;
    }

    /**
     * Ruft den Wert der program-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XsdCtProgram }
     *     
     */
    public XsdCtProgram getProgram() {
        return program;
    }

    /**
     * Legt den Wert der program-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XsdCtProgram }
     *     
     */
    public void setProgram(XsdCtProgram value) {
        this.program = value;
    }

    /**
     * Ruft den Wert der parameters-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XsdCtConfigurationParameters }
     *     
     */
    public XsdCtConfigurationParameters getParameters() {
        return parameters;
    }

    /**
     * Legt den Wert der parameters-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XsdCtConfigurationParameters }
     *     
     */
    public void setParameters(XsdCtConfigurationParameters value) {
        this.parameters = value;
    }

    /**
     * Ruft den Wert der loadProfiles-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XsdCtLoadProfiles }
     *     
     */
    public XsdCtLoadProfiles getLoadProfiles() {
        return loadProfiles;
    }

    /**
     * Legt den Wert der loadProfiles-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XsdCtLoadProfiles }
     *     
     */
    public void setLoadProfiles(XsdCtLoadProfiles value) {
        this.loadProfiles = value;
    }

    /**
     * Creates and returns a deep copy of this object.
     * 
     * 
     * @return
     *     A deep copy of this object.
     */
    @Override
    public XsdCtApplianceProgramConfiguration clone() {
        try {
            {
                // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
                final XsdCtApplianceProgramConfiguration clone = ((XsdCtApplianceProgramConfiguration) super.clone());
                // CBuiltinLeafInfo: java.lang.Integer
                clone.configurationID = this.getConfigurationID();
                // CBuiltinLeafInfo: java.lang.String
                clone.configurationName = ((this.configurationName == null)?null:this.getConfigurationName());
                // CClassInfo: edu.kit.aifb.osh.XsdCtProgram
                clone.program = ((this.program == null)?null:((this.getProgram() == null)?null:this.getProgram().clone()));
                // CClassInfo: edu.kit.aifb.osh.XsdCtConfigurationParameters
                clone.parameters = ((this.parameters == null)?null:((this.getParameters() == null)?null:this.getParameters().clone()));
                // CClassInfo: edu.kit.aifb.osh.XsdCtLoadProfiles
                clone.loadProfiles = ((this.loadProfiles == null)?null:((this.getLoadProfiles() == null)?null:this.getLoadProfiles().clone()));
                return clone;
            }
        } catch (CloneNotSupportedException e) {
            // Please report this at https://apps.sourceforge.net/mantisbt/ccxjc/
            throw new AssertionError(e);
        }
    }

}
