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


/**
 * <p>Java-Klasse für xsdCt.GenericApplianceProfile complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="xsdCt.GenericApplianceProfile">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DeviceUUID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DeviceType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DeviceDescription" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="HasProfile" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="Intelligent" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
@XmlType(name = "xsdCt.GenericApplianceProfile", propOrder = {
    "deviceUUID",
    "deviceType",
    "deviceDescription",
    "hasProfile",
    "intelligent",
    "loadProfiles"
})
public class XsdCtGenericApplianceProfile implements Cloneable
{

    @XmlElement(name = "DeviceUUID", required = true)
    protected String deviceUUID;
    @XmlElement(name = "DeviceType", required = true)
    protected String deviceType;
    @XmlElement(name = "DeviceDescription", required = true)
    protected String deviceDescription;
    @XmlElement(name = "HasProfile")
    protected boolean hasProfile;
    @XmlElement(name = "Intelligent")
    protected boolean intelligent;
    @XmlElement(name = "LoadProfiles", required = true)
    protected XsdCtLoadProfiles loadProfiles;

    /**
     * Creates a new {@code XsdCtGenericApplianceProfile} instance.
     * 
     */
    public XsdCtGenericApplianceProfile() {
        // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
        super();
    }

    /**
     * Creates a new {@code XsdCtGenericApplianceProfile} instance by deeply copying a given {@code XsdCtGenericApplianceProfile} instance.
     * 
     * 
     * @param o
     *     The instance to copy.
     * @throws NullPointerException
     *     if {@code o} is {@code null}.
     */
    public XsdCtGenericApplianceProfile(final XsdCtGenericApplianceProfile o) {
        // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
        super();
        if (o == null) {
            throw new NullPointerException("Cannot create a copy of 'XsdCtGenericApplianceProfile' from 'null'.");
        }
        // CBuiltinLeafInfo: java.lang.String
        this.deviceUUID = ((o.deviceUUID == null)?null:o.getDeviceUUID());
        // CBuiltinLeafInfo: java.lang.String
        this.deviceType = ((o.deviceType == null)?null:o.getDeviceType());
        // CBuiltinLeafInfo: java.lang.String
        this.deviceDescription = ((o.deviceDescription == null)?null:o.getDeviceDescription());
        // CBuiltinLeafInfo: java.lang.Boolean
        this.hasProfile = o.isHasProfile();
        // CBuiltinLeafInfo: java.lang.Boolean
        this.intelligent = o.isIntelligent();
        // CClassInfo: edu.kit.aifb.osh.XsdCtLoadProfiles
        this.loadProfiles = ((o.loadProfiles == null)?null:((o.getLoadProfiles() == null)?null:o.getLoadProfiles().clone()));
    }

    /**
     * Ruft den Wert der deviceUUID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeviceUUID() {
        return deviceUUID;
    }

    /**
     * Legt den Wert der deviceUUID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeviceUUID(String value) {
        this.deviceUUID = value;
    }

    /**
     * Ruft den Wert der deviceType-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeviceType() {
        return deviceType;
    }

    /**
     * Legt den Wert der deviceType-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeviceType(String value) {
        this.deviceType = value;
    }

    /**
     * Ruft den Wert der deviceDescription-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeviceDescription() {
        return deviceDescription;
    }

    /**
     * Legt den Wert der deviceDescription-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeviceDescription(String value) {
        this.deviceDescription = value;
    }

    /**
     * Ruft den Wert der hasProfile-Eigenschaft ab.
     * 
     */
    public boolean isHasProfile() {
        return hasProfile;
    }

    /**
     * Legt den Wert der hasProfile-Eigenschaft fest.
     * 
     */
    public void setHasProfile(boolean value) {
        this.hasProfile = value;
    }

    /**
     * Ruft den Wert der intelligent-Eigenschaft ab.
     * 
     */
    public boolean isIntelligent() {
        return intelligent;
    }

    /**
     * Legt den Wert der intelligent-Eigenschaft fest.
     * 
     */
    public void setIntelligent(boolean value) {
        this.intelligent = value;
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
    public XsdCtGenericApplianceProfile clone() {
        try {
            {
                // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
                final XsdCtGenericApplianceProfile clone = ((XsdCtGenericApplianceProfile) super.clone());
                // CBuiltinLeafInfo: java.lang.String
                clone.deviceUUID = ((this.deviceUUID == null)?null:this.getDeviceUUID());
                // CBuiltinLeafInfo: java.lang.String
                clone.deviceType = ((this.deviceType == null)?null:this.getDeviceType());
                // CBuiltinLeafInfo: java.lang.String
                clone.deviceDescription = ((this.deviceDescription == null)?null:this.getDeviceDescription());
                // CBuiltinLeafInfo: java.lang.Boolean
                clone.hasProfile = this.isHasProfile();
                // CBuiltinLeafInfo: java.lang.Boolean
                clone.intelligent = this.isIntelligent();
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
