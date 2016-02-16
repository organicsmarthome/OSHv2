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


package osh.configuration.controllerbox;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für FilePathes complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="FilePathes">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="halConfigPath" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="localObserverDataStoragePath" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="simulationEngineConfigurationPath" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="logFileDirectory" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FilePathes", propOrder = {
    "halConfigPath",
    "localObserverDataStoragePath",
    "simulationEngineConfigurationPath",
    "logFileDirectory"
})
public class FilePathes implements Cloneable
{

    @XmlElement(required = true)
    protected String halConfigPath;
    @XmlElement(required = true)
    protected String localObserverDataStoragePath;
    @XmlElement(required = true)
    protected String simulationEngineConfigurationPath;
    @XmlElement(required = true)
    protected String logFileDirectory;

    /**
     * Creates a new {@code FilePathes} instance.
     * 
     */
    public FilePathes() {
        // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
        super();
    }

    /**
     * Creates a new {@code FilePathes} instance by deeply copying a given {@code FilePathes} instance.
     * 
     * 
     * @param o
     *     The instance to copy.
     * @throws NullPointerException
     *     if {@code o} is {@code null}.
     */
    public FilePathes(final FilePathes o) {
        // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
        super();
        if (o == null) {
            throw new NullPointerException("Cannot create a copy of 'FilePathes' from 'null'.");
        }
        // CBuiltinLeafInfo: java.lang.String
        this.halConfigPath = ((o.halConfigPath == null)?null:o.getHalConfigPath());
        // CBuiltinLeafInfo: java.lang.String
        this.localObserverDataStoragePath = ((o.localObserverDataStoragePath == null)?null:o.getLocalObserverDataStoragePath());
        // CBuiltinLeafInfo: java.lang.String
        this.simulationEngineConfigurationPath = ((o.simulationEngineConfigurationPath == null)?null:o.getSimulationEngineConfigurationPath());
        // CBuiltinLeafInfo: java.lang.String
        this.logFileDirectory = ((o.logFileDirectory == null)?null:o.getLogFileDirectory());
    }

    /**
     * Ruft den Wert der halConfigPath-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHalConfigPath() {
        return halConfigPath;
    }

    /**
     * Legt den Wert der halConfigPath-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHalConfigPath(String value) {
        this.halConfigPath = value;
    }

    /**
     * Ruft den Wert der localObserverDataStoragePath-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalObserverDataStoragePath() {
        return localObserverDataStoragePath;
    }

    /**
     * Legt den Wert der localObserverDataStoragePath-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalObserverDataStoragePath(String value) {
        this.localObserverDataStoragePath = value;
    }

    /**
     * Ruft den Wert der simulationEngineConfigurationPath-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSimulationEngineConfigurationPath() {
        return simulationEngineConfigurationPath;
    }

    /**
     * Legt den Wert der simulationEngineConfigurationPath-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSimulationEngineConfigurationPath(String value) {
        this.simulationEngineConfigurationPath = value;
    }

    /**
     * Ruft den Wert der logFileDirectory-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogFileDirectory() {
        return logFileDirectory;
    }

    /**
     * Legt den Wert der logFileDirectory-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogFileDirectory(String value) {
        this.logFileDirectory = value;
    }

    /**
     * Creates and returns a deep copy of this object.
     * 
     * 
     * @return
     *     A deep copy of this object.
     */
    @Override
    public FilePathes clone() {
        try {
            {
                // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
                final FilePathes clone = ((FilePathes) super.clone());
                // CBuiltinLeafInfo: java.lang.String
                clone.halConfigPath = ((this.halConfigPath == null)?null:this.getHalConfigPath());
                // CBuiltinLeafInfo: java.lang.String
                clone.localObserverDataStoragePath = ((this.localObserverDataStoragePath == null)?null:this.getLocalObserverDataStoragePath());
                // CBuiltinLeafInfo: java.lang.String
                clone.simulationEngineConfigurationPath = ((this.simulationEngineConfigurationPath == null)?null:this.getSimulationEngineConfigurationPath());
                // CBuiltinLeafInfo: java.lang.String
                clone.logFileDirectory = ((this.logFileDirectory == null)?null:this.getLogFileDirectory());
                return clone;
            }
        } catch (CloneNotSupportedException e) {
            // Please report this at https://apps.sourceforge.net/mantisbt/ccxjc/
            throw new AssertionError(e);
        }
    }

}
