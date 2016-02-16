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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import osh.configuration.system.ConfigurationParameter;


/**
 * <p>Java-Klasse für anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="simulation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="runnigVirtual" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="configfilePathes" type="{http://osh/Configuration/ControllerBox}FilePathes"/>
 *         &lt;element name="globalOcUuid" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="globalControllerClass" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="globalControllerParameters" type="{http://osh/Configuration/System}ConfigurationParameter" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="globalObserverClass" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="globalObserverParameters" type="{http://osh/Configuration/System}ConfigurationParameter" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="randomSeed" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "simulation",
    "runnigVirtual",
    "configfilePathes",
    "globalOcUuid",
    "globalControllerClass",
    "globalControllerParameters",
    "globalObserverClass",
    "globalObserverParameters",
    "randomSeed"
})
@XmlRootElement(name = "OCConfiguration")
public class OCConfiguration implements Cloneable
{

    protected boolean simulation;
    protected boolean runnigVirtual;
    @XmlElement(required = true)
    protected FilePathes configfilePathes;
    @XmlElement(required = true)
    protected String globalOcUuid;
    @XmlElement(required = true)
    protected String globalControllerClass;
    protected List<ConfigurationParameter> globalControllerParameters;
    @XmlElement(required = true)
    protected String globalObserverClass;
    protected List<ConfigurationParameter> globalObserverParameters;
    @XmlElement(required = true)
    protected String randomSeed;

    /**
     * Creates a new {@code OCConfiguration} instance.
     * 
     */
    public OCConfiguration() {
        // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
        super();
    }

    /**
     * Creates a new {@code OCConfiguration} instance by deeply copying a given {@code OCConfiguration} instance.
     * 
     * 
     * @param o
     *     The instance to copy.
     * @throws NullPointerException
     *     if {@code o} is {@code null}.
     */
    public OCConfiguration(final OCConfiguration o) {
        // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
        super();
        if (o == null) {
            throw new NullPointerException("Cannot create a copy of 'OCConfiguration' from 'null'.");
        }
        // CBuiltinLeafInfo: java.lang.Boolean
        this.simulation = o.isSimulation();
        // CBuiltinLeafInfo: java.lang.Boolean
        this.runnigVirtual = o.isRunnigVirtual();
        // CClassInfo: osh.configuration.controllerbox.FilePathes
        this.configfilePathes = ((o.configfilePathes == null)?null:((o.getConfigfilePathes() == null)?null:o.getConfigfilePathes().clone()));
        // CBuiltinLeafInfo: java.lang.String
        this.globalOcUuid = ((o.globalOcUuid == null)?null:o.getGlobalOcUuid());
        // CBuiltinLeafInfo: java.lang.String
        this.globalControllerClass = ((o.globalControllerClass == null)?null:o.getGlobalControllerClass());
        // 'GlobalControllerParameters' collection.
        if (o.globalControllerParameters!= null) {
            copyGlobalControllerParameters(o.getGlobalControllerParameters(), this.getGlobalControllerParameters());
        }
        // CBuiltinLeafInfo: java.lang.String
        this.globalObserverClass = ((o.globalObserverClass == null)?null:o.getGlobalObserverClass());
        // 'GlobalObserverParameters' collection.
        if (o.globalObserverParameters!= null) {
            copyGlobalObserverParameters(o.getGlobalObserverParameters(), this.getGlobalObserverParameters());
        }
        // CBuiltinLeafInfo: java.lang.String
        this.randomSeed = ((o.randomSeed == null)?null:o.getRandomSeed());
    }

    /**
     * Ruft den Wert der simulation-Eigenschaft ab.
     * 
     */
    public boolean isSimulation() {
        return simulation;
    }

    /**
     * Legt den Wert der simulation-Eigenschaft fest.
     * 
     */
    public void setSimulation(boolean value) {
        this.simulation = value;
    }

    /**
     * Ruft den Wert der runnigVirtual-Eigenschaft ab.
     * 
     */
    public boolean isRunnigVirtual() {
        return runnigVirtual;
    }

    /**
     * Legt den Wert der runnigVirtual-Eigenschaft fest.
     * 
     */
    public void setRunnigVirtual(boolean value) {
        this.runnigVirtual = value;
    }

    /**
     * Ruft den Wert der configfilePathes-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link FilePathes }
     *     
     */
    public FilePathes getConfigfilePathes() {
        return configfilePathes;
    }

    /**
     * Legt den Wert der configfilePathes-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link FilePathes }
     *     
     */
    public void setConfigfilePathes(FilePathes value) {
        this.configfilePathes = value;
    }

    /**
     * Ruft den Wert der globalOcUuid-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGlobalOcUuid() {
        return globalOcUuid;
    }

    /**
     * Legt den Wert der globalOcUuid-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGlobalOcUuid(String value) {
        this.globalOcUuid = value;
    }

    /**
     * Ruft den Wert der globalControllerClass-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGlobalControllerClass() {
        return globalControllerClass;
    }

    /**
     * Legt den Wert der globalControllerClass-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGlobalControllerClass(String value) {
        this.globalControllerClass = value;
    }

    /**
     * Gets the value of the globalControllerParameters property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the globalControllerParameters property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGlobalControllerParameters().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ConfigurationParameter }
     * 
     * 
     */
    public List<ConfigurationParameter> getGlobalControllerParameters() {
        if (globalControllerParameters == null) {
            globalControllerParameters = new ArrayList<ConfigurationParameter>();
        }
        return this.globalControllerParameters;
    }

    /**
     * Ruft den Wert der globalObserverClass-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGlobalObserverClass() {
        return globalObserverClass;
    }

    /**
     * Legt den Wert der globalObserverClass-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGlobalObserverClass(String value) {
        this.globalObserverClass = value;
    }

    /**
     * Gets the value of the globalObserverParameters property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the globalObserverParameters property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGlobalObserverParameters().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ConfigurationParameter }
     * 
     * 
     */
    public List<ConfigurationParameter> getGlobalObserverParameters() {
        if (globalObserverParameters == null) {
            globalObserverParameters = new ArrayList<ConfigurationParameter>();
        }
        return this.globalObserverParameters;
    }

    /**
     * Ruft den Wert der randomSeed-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRandomSeed() {
        return randomSeed;
    }

    /**
     * Legt den Wert der randomSeed-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRandomSeed(String value) {
        this.randomSeed = value;
    }

    /**
     * Copies all values of property {@code GlobalControllerParameters} deeply.
     * 
     * @param source
     *     The source to copy from.
     * @param target
     *     The target to copy {@code source} to.
     * @throws NullPointerException
     *     if {@code target} is {@code null}.
     */
    @SuppressWarnings("unchecked")
    private static void copyGlobalControllerParameters(final List<ConfigurationParameter> source, final List<ConfigurationParameter> target) {
        // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
        if ((source!= null)&&(!source.isEmpty())) {
            for (final Iterator<?> it = source.iterator(); it.hasNext(); ) {
                final Object next = it.next();
                if (next instanceof ConfigurationParameter) {
                    // CClassInfo: osh.configuration.system.ConfigurationParameter
                    target.add(((ConfigurationParameter) next).clone());
                    continue;
                }
                // Please report this at https://apps.sourceforge.net/mantisbt/ccxjc/
                throw new AssertionError((("Unexpected instance '"+ next)+"' for property 'GlobalControllerParameters' of class 'osh.configuration.controllerbox.OCConfiguration'."));
            }
        }
    }

    /**
     * Copies all values of property {@code GlobalObserverParameters} deeply.
     * 
     * @param source
     *     The source to copy from.
     * @param target
     *     The target to copy {@code source} to.
     * @throws NullPointerException
     *     if {@code target} is {@code null}.
     */
    @SuppressWarnings("unchecked")
    private static void copyGlobalObserverParameters(final List<ConfigurationParameter> source, final List<ConfigurationParameter> target) {
        // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
        if ((source!= null)&&(!source.isEmpty())) {
            for (final Iterator<?> it = source.iterator(); it.hasNext(); ) {
                final Object next = it.next();
                if (next instanceof ConfigurationParameter) {
                    // CClassInfo: osh.configuration.system.ConfigurationParameter
                    target.add(((ConfigurationParameter) next).clone());
                    continue;
                }
                // Please report this at https://apps.sourceforge.net/mantisbt/ccxjc/
                throw new AssertionError((("Unexpected instance '"+ next)+"' for property 'GlobalObserverParameters' of class 'osh.configuration.controllerbox.OCConfiguration'."));
            }
        }
    }

    /**
     * Creates and returns a deep copy of this object.
     * 
     * 
     * @return
     *     A deep copy of this object.
     */
    @Override
    public OCConfiguration clone() {
        try {
            {
                // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
                final OCConfiguration clone = ((OCConfiguration) super.clone());
                // CBuiltinLeafInfo: java.lang.Boolean
                clone.simulation = this.isSimulation();
                // CBuiltinLeafInfo: java.lang.Boolean
                clone.runnigVirtual = this.isRunnigVirtual();
                // CClassInfo: osh.configuration.controllerbox.FilePathes
                clone.configfilePathes = ((this.configfilePathes == null)?null:((this.getConfigfilePathes() == null)?null:this.getConfigfilePathes().clone()));
                // CBuiltinLeafInfo: java.lang.String
                clone.globalOcUuid = ((this.globalOcUuid == null)?null:this.getGlobalOcUuid());
                // CBuiltinLeafInfo: java.lang.String
                clone.globalControllerClass = ((this.globalControllerClass == null)?null:this.getGlobalControllerClass());
                // 'GlobalControllerParameters' collection.
                if (this.globalControllerParameters!= null) {
                    clone.globalControllerParameters = null;
                    copyGlobalControllerParameters(this.getGlobalControllerParameters(), clone.getGlobalControllerParameters());
                }
                // CBuiltinLeafInfo: java.lang.String
                clone.globalObserverClass = ((this.globalObserverClass == null)?null:this.getGlobalObserverClass());
                // 'GlobalObserverParameters' collection.
                if (this.globalObserverParameters!= null) {
                    clone.globalObserverParameters = null;
                    copyGlobalObserverParameters(this.getGlobalObserverParameters(), clone.getGlobalObserverParameters());
                }
                // CBuiltinLeafInfo: java.lang.String
                clone.randomSeed = ((this.randomSeed == null)?null:this.getRandomSeed());
                return clone;
            }
        } catch (CloneNotSupportedException e) {
            // Please report this at https://apps.sourceforge.net/mantisbt/ccxjc/
            throw new AssertionError(e);
        }
    }

}
