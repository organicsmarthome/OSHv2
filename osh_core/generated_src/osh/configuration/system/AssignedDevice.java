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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für AssignedDevice complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="AssignedDevice">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="deviceType" type="{http://osh/Configuration/System}DeviceTypes"/>
 *         &lt;element name="deviceClassification" type="{http://osh/Configuration/System}DeviceClassification"/>
 *         &lt;element name="deviceDescription" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="driverClassName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="driverParameters" type="{http://osh/Configuration/System}ConfigurationParameter" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="controllable" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="observable" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="assignedLocalOCUnit" type="{http://osh/Configuration/System}AssignedLocalOCUnit"/>
 *       &lt;/sequence>
 *       &lt;attribute name="deviceID" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AssignedDevice", propOrder = {
    "deviceType",
    "deviceClassification",
    "deviceDescription",
    "driverClassName",
    "driverParameters",
    "controllable",
    "observable",
    "assignedLocalOCUnit"
})
public class AssignedDevice implements Cloneable
{

    @XmlElement(required = true)
    protected DeviceTypes deviceType;
    @XmlElement(required = true, defaultValue = "N/A")
    protected DeviceClassification deviceClassification;
    @XmlElement(required = true)
    protected String deviceDescription;
    @XmlElement(required = true)
    protected String driverClassName;
    protected List<ConfigurationParameter> driverParameters;
    protected boolean controllable;
    protected boolean observable;
    @XmlElement(required = true)
    protected AssignedLocalOCUnit assignedLocalOCUnit;
    @XmlAttribute(name = "deviceID")
    protected String deviceID;

    /**
     * Creates a new {@code AssignedDevice} instance.
     * 
     */
    public AssignedDevice() {
        // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
        super();
    }

    /**
     * Creates a new {@code AssignedDevice} instance by deeply copying a given {@code AssignedDevice} instance.
     * 
     * 
     * @param o
     *     The instance to copy.
     * @throws NullPointerException
     *     if {@code o} is {@code null}.
     */
    public AssignedDevice(final AssignedDevice o) {
        // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
        super();
        if (o == null) {
            throw new NullPointerException("Cannot create a copy of 'AssignedDevice' from 'null'.");
        }
        // CEnumLeafInfo: osh.configuration.system.DeviceTypes
        this.deviceType = ((o.deviceType == null)?null:o.getDeviceType());
        // CEnumLeafInfo: osh.configuration.system.DeviceClassification
        this.deviceClassification = ((o.deviceClassification == null)?null:o.getDeviceClassification());
        // CBuiltinLeafInfo: java.lang.String
        this.deviceDescription = ((o.deviceDescription == null)?null:o.getDeviceDescription());
        // CBuiltinLeafInfo: java.lang.String
        this.driverClassName = ((o.driverClassName == null)?null:o.getDriverClassName());
        // 'DriverParameters' collection.
        if (o.driverParameters!= null) {
            copyDriverParameters(o.getDriverParameters(), this.getDriverParameters());
        }
        // CBuiltinLeafInfo: java.lang.Boolean
        this.controllable = o.isControllable();
        // CBuiltinLeafInfo: java.lang.Boolean
        this.observable = o.isObservable();
        // CClassInfo: osh.configuration.system.AssignedLocalOCUnit
        this.assignedLocalOCUnit = ((o.assignedLocalOCUnit == null)?null:((o.getAssignedLocalOCUnit() == null)?null:o.getAssignedLocalOCUnit().clone()));
        // CBuiltinLeafInfo: java.lang.String
        this.deviceID = ((o.deviceID == null)?null:o.getDeviceID());
    }

    /**
     * Ruft den Wert der deviceType-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DeviceTypes }
     *     
     */
    public DeviceTypes getDeviceType() {
        return deviceType;
    }

    /**
     * Legt den Wert der deviceType-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DeviceTypes }
     *     
     */
    public void setDeviceType(DeviceTypes value) {
        this.deviceType = value;
    }

    /**
     * Ruft den Wert der deviceClassification-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DeviceClassification }
     *     
     */
    public DeviceClassification getDeviceClassification() {
        return deviceClassification;
    }

    /**
     * Legt den Wert der deviceClassification-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DeviceClassification }
     *     
     */
    public void setDeviceClassification(DeviceClassification value) {
        this.deviceClassification = value;
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
     * Ruft den Wert der driverClassName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDriverClassName() {
        return driverClassName;
    }

    /**
     * Legt den Wert der driverClassName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDriverClassName(String value) {
        this.driverClassName = value;
    }

    /**
     * Gets the value of the driverParameters property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the driverParameters property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDriverParameters().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ConfigurationParameter }
     * 
     * 
     */
    public List<ConfigurationParameter> getDriverParameters() {
        if (driverParameters == null) {
            driverParameters = new ArrayList<ConfigurationParameter>();
        }
        return this.driverParameters;
    }

    /**
     * Ruft den Wert der controllable-Eigenschaft ab.
     * 
     */
    public boolean isControllable() {
        return controllable;
    }

    /**
     * Legt den Wert der controllable-Eigenschaft fest.
     * 
     */
    public void setControllable(boolean value) {
        this.controllable = value;
    }

    /**
     * Ruft den Wert der observable-Eigenschaft ab.
     * 
     */
    public boolean isObservable() {
        return observable;
    }

    /**
     * Legt den Wert der observable-Eigenschaft fest.
     * 
     */
    public void setObservable(boolean value) {
        this.observable = value;
    }

    /**
     * Ruft den Wert der assignedLocalOCUnit-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AssignedLocalOCUnit }
     *     
     */
    public AssignedLocalOCUnit getAssignedLocalOCUnit() {
        return assignedLocalOCUnit;
    }

    /**
     * Legt den Wert der assignedLocalOCUnit-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AssignedLocalOCUnit }
     *     
     */
    public void setAssignedLocalOCUnit(AssignedLocalOCUnit value) {
        this.assignedLocalOCUnit = value;
    }

    /**
     * Ruft den Wert der deviceID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeviceID() {
        return deviceID;
    }

    /**
     * Legt den Wert der deviceID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeviceID(String value) {
        this.deviceID = value;
    }

    /**
     * Copies all values of property {@code DriverParameters} deeply.
     * 
     * @param source
     *     The source to copy from.
     * @param target
     *     The target to copy {@code source} to.
     * @throws NullPointerException
     *     if {@code target} is {@code null}.
     */
    @SuppressWarnings("unchecked")
    private static void copyDriverParameters(final List<ConfigurationParameter> source, final List<ConfigurationParameter> target) {
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
                throw new AssertionError((("Unexpected instance '"+ next)+"' for property 'DriverParameters' of class 'osh.configuration.system.AssignedDevice'."));
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
    public AssignedDevice clone() {
        try {
            {
                // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
                final AssignedDevice clone = ((AssignedDevice) super.clone());
                // CEnumLeafInfo: osh.configuration.system.DeviceTypes
                clone.deviceType = ((this.deviceType == null)?null:this.getDeviceType());
                // CEnumLeafInfo: osh.configuration.system.DeviceClassification
                clone.deviceClassification = ((this.deviceClassification == null)?null:this.getDeviceClassification());
                // CBuiltinLeafInfo: java.lang.String
                clone.deviceDescription = ((this.deviceDescription == null)?null:this.getDeviceDescription());
                // CBuiltinLeafInfo: java.lang.String
                clone.driverClassName = ((this.driverClassName == null)?null:this.getDriverClassName());
                // 'DriverParameters' collection.
                if (this.driverParameters!= null) {
                    clone.driverParameters = null;
                    copyDriverParameters(this.getDriverParameters(), clone.getDriverParameters());
                }
                // CBuiltinLeafInfo: java.lang.Boolean
                clone.controllable = this.isControllable();
                // CBuiltinLeafInfo: java.lang.Boolean
                clone.observable = this.isObservable();
                // CClassInfo: osh.configuration.system.AssignedLocalOCUnit
                clone.assignedLocalOCUnit = ((this.assignedLocalOCUnit == null)?null:((this.getAssignedLocalOCUnit() == null)?null:this.getAssignedLocalOCUnit().clone()));
                // CBuiltinLeafInfo: java.lang.String
                clone.deviceID = ((this.deviceID == null)?null:this.getDeviceID());
                return clone;
            }
        } catch (CloneNotSupportedException e) {
            // Please report this at https://apps.sourceforge.net/mantisbt/ccxjc/
            throw new AssertionError(e);
        }
    }

}
