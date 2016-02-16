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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für AssignedBusDevice complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="AssignedBusDevice">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="busDeviceID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="busDeviceType" type="{http://osh/Configuration/System}BusDeviceTypes"/>
 *         &lt;element name="busDeviceClassification" type="{http://osh/Configuration/System}BusDeviceClassification"/>
 *         &lt;element name="busDeviceDescription" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="busManagerClassName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="busDriverClassName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="busDriverParameters" type="{http://osh/Configuration/System}ConfigurationParameter" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AssignedBusDevice", propOrder = {
    "busDeviceID",
    "busDeviceType",
    "busDeviceClassification",
    "busDeviceDescription",
    "busManagerClassName",
    "busDriverClassName",
    "busDriverParameters"
})
public class AssignedBusDevice implements Cloneable
{

    @XmlElement(required = true)
    protected String busDeviceID;
    @XmlElement(required = true)
    protected BusDeviceTypes busDeviceType;
    @XmlElement(required = true)
    protected BusDeviceClassification busDeviceClassification;
    @XmlElement(required = true)
    protected String busDeviceDescription;
    @XmlElement(required = true)
    protected String busManagerClassName;
    @XmlElement(required = true)
    protected String busDriverClassName;
    protected List<ConfigurationParameter> busDriverParameters;

    /**
     * Creates a new {@code AssignedBusDevice} instance.
     * 
     */
    public AssignedBusDevice() {
        // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
        super();
    }

    /**
     * Creates a new {@code AssignedBusDevice} instance by deeply copying a given {@code AssignedBusDevice} instance.
     * 
     * 
     * @param o
     *     The instance to copy.
     * @throws NullPointerException
     *     if {@code o} is {@code null}.
     */
    public AssignedBusDevice(final AssignedBusDevice o) {
        // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
        super();
        if (o == null) {
            throw new NullPointerException("Cannot create a copy of 'AssignedBusDevice' from 'null'.");
        }
        // CBuiltinLeafInfo: java.lang.String
        this.busDeviceID = ((o.busDeviceID == null)?null:o.getBusDeviceID());
        // CEnumLeafInfo: osh.configuration.system.BusDeviceTypes
        this.busDeviceType = ((o.busDeviceType == null)?null:o.getBusDeviceType());
        // CEnumLeafInfo: osh.configuration.system.BusDeviceClassification
        this.busDeviceClassification = ((o.busDeviceClassification == null)?null:o.getBusDeviceClassification());
        // CBuiltinLeafInfo: java.lang.String
        this.busDeviceDescription = ((o.busDeviceDescription == null)?null:o.getBusDeviceDescription());
        // CBuiltinLeafInfo: java.lang.String
        this.busManagerClassName = ((o.busManagerClassName == null)?null:o.getBusManagerClassName());
        // CBuiltinLeafInfo: java.lang.String
        this.busDriverClassName = ((o.busDriverClassName == null)?null:o.getBusDriverClassName());
        // 'BusDriverParameters' collection.
        if (o.busDriverParameters!= null) {
            copyBusDriverParameters(o.getBusDriverParameters(), this.getBusDriverParameters());
        }
    }

    /**
     * Ruft den Wert der busDeviceID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBusDeviceID() {
        return busDeviceID;
    }

    /**
     * Legt den Wert der busDeviceID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBusDeviceID(String value) {
        this.busDeviceID = value;
    }

    /**
     * Ruft den Wert der busDeviceType-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BusDeviceTypes }
     *     
     */
    public BusDeviceTypes getBusDeviceType() {
        return busDeviceType;
    }

    /**
     * Legt den Wert der busDeviceType-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BusDeviceTypes }
     *     
     */
    public void setBusDeviceType(BusDeviceTypes value) {
        this.busDeviceType = value;
    }

    /**
     * Ruft den Wert der busDeviceClassification-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BusDeviceClassification }
     *     
     */
    public BusDeviceClassification getBusDeviceClassification() {
        return busDeviceClassification;
    }

    /**
     * Legt den Wert der busDeviceClassification-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BusDeviceClassification }
     *     
     */
    public void setBusDeviceClassification(BusDeviceClassification value) {
        this.busDeviceClassification = value;
    }

    /**
     * Ruft den Wert der busDeviceDescription-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBusDeviceDescription() {
        return busDeviceDescription;
    }

    /**
     * Legt den Wert der busDeviceDescription-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBusDeviceDescription(String value) {
        this.busDeviceDescription = value;
    }

    /**
     * Ruft den Wert der busManagerClassName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBusManagerClassName() {
        return busManagerClassName;
    }

    /**
     * Legt den Wert der busManagerClassName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBusManagerClassName(String value) {
        this.busManagerClassName = value;
    }

    /**
     * Ruft den Wert der busDriverClassName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBusDriverClassName() {
        return busDriverClassName;
    }

    /**
     * Legt den Wert der busDriverClassName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBusDriverClassName(String value) {
        this.busDriverClassName = value;
    }

    /**
     * Gets the value of the busDriverParameters property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the busDriverParameters property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBusDriverParameters().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ConfigurationParameter }
     * 
     * 
     */
    public List<ConfigurationParameter> getBusDriverParameters() {
        if (busDriverParameters == null) {
            busDriverParameters = new ArrayList<ConfigurationParameter>();
        }
        return this.busDriverParameters;
    }

    /**
     * Copies all values of property {@code BusDriverParameters} deeply.
     * 
     * @param source
     *     The source to copy from.
     * @param target
     *     The target to copy {@code source} to.
     * @throws NullPointerException
     *     if {@code target} is {@code null}.
     */
    @SuppressWarnings("unchecked")
    private static void copyBusDriverParameters(final List<ConfigurationParameter> source, final List<ConfigurationParameter> target) {
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
                throw new AssertionError((("Unexpected instance '"+ next)+"' for property 'BusDriverParameters' of class 'osh.configuration.system.AssignedBusDevice'."));
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
    public AssignedBusDevice clone() {
        try {
            {
                // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
                final AssignedBusDevice clone = ((AssignedBusDevice) super.clone());
                // CBuiltinLeafInfo: java.lang.String
                clone.busDeviceID = ((this.busDeviceID == null)?null:this.getBusDeviceID());
                // CEnumLeafInfo: osh.configuration.system.BusDeviceTypes
                clone.busDeviceType = ((this.busDeviceType == null)?null:this.getBusDeviceType());
                // CEnumLeafInfo: osh.configuration.system.BusDeviceClassification
                clone.busDeviceClassification = ((this.busDeviceClassification == null)?null:this.getBusDeviceClassification());
                // CBuiltinLeafInfo: java.lang.String
                clone.busDeviceDescription = ((this.busDeviceDescription == null)?null:this.getBusDeviceDescription());
                // CBuiltinLeafInfo: java.lang.String
                clone.busManagerClassName = ((this.busManagerClassName == null)?null:this.getBusManagerClassName());
                // CBuiltinLeafInfo: java.lang.String
                clone.busDriverClassName = ((this.busDriverClassName == null)?null:this.getBusDriverClassName());
                // 'BusDriverParameters' collection.
                if (this.busDriverParameters!= null) {
                    clone.busDriverParameters = null;
                    copyBusDriverParameters(this.getBusDriverParameters(), clone.getBusDriverParameters());
                }
                return clone;
            }
        } catch (CloneNotSupportedException e) {
            // Please report this at https://apps.sourceforge.net/mantisbt/ccxjc/
            throw new AssertionError(e);
        }
    }

}
