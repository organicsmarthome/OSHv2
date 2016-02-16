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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="assignedDevices" type="{http://osh/Configuration/System}AssignedDevice" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="assignedComDevices" type="{http://osh/Configuration/System}AssignedComDevice" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="assignedBusDevices" type="{http://osh/Configuration/System}AssignedBusDevice" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="usingHALdispatcher" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="HALdispatcherClassName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "assignedDevices",
    "assignedComDevices",
    "assignedBusDevices",
    "usingHALdispatcher",
    "haLdispatcherClassName"
})
@XmlRootElement(name = "HALConfiguration")
public class HALConfiguration implements Cloneable
{

    protected List<AssignedDevice> assignedDevices;
    protected List<AssignedComDevice> assignedComDevices;
    protected List<AssignedBusDevice> assignedBusDevices;
    protected Boolean usingHALdispatcher;
    @XmlElement(name = "HALdispatcherClassName")
    protected String haLdispatcherClassName;

    /**
     * Creates a new {@code HALConfiguration} instance.
     * 
     */
    public HALConfiguration() {
        // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
        super();
    }

    /**
     * Creates a new {@code HALConfiguration} instance by deeply copying a given {@code HALConfiguration} instance.
     * 
     * 
     * @param o
     *     The instance to copy.
     * @throws NullPointerException
     *     if {@code o} is {@code null}.
     */
    public HALConfiguration(final HALConfiguration o) {
        // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
        super();
        if (o == null) {
            throw new NullPointerException("Cannot create a copy of 'HALConfiguration' from 'null'.");
        }
        // 'AssignedDevices' collection.
        if (o.assignedDevices!= null) {
            copyAssignedDevices(o.getAssignedDevices(), this.getAssignedDevices());
        }
        // 'AssignedComDevices' collection.
        if (o.assignedComDevices!= null) {
            copyAssignedComDevices(o.getAssignedComDevices(), this.getAssignedComDevices());
        }
        // 'AssignedBusDevices' collection.
        if (o.assignedBusDevices!= null) {
            copyAssignedBusDevices(o.getAssignedBusDevices(), this.getAssignedBusDevices());
        }
        // CBuiltinLeafInfo: java.lang.Boolean
        this.usingHALdispatcher = ((o.usingHALdispatcher == null)?null:o.isUsingHALdispatcher());
        // CBuiltinLeafInfo: java.lang.String
        this.haLdispatcherClassName = ((o.haLdispatcherClassName == null)?null:o.getHALdispatcherClassName());
    }

    /**
     * Gets the value of the assignedDevices property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the assignedDevices property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAssignedDevices().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AssignedDevice }
     * 
     * 
     */
    public List<AssignedDevice> getAssignedDevices() {
        if (assignedDevices == null) {
            assignedDevices = new ArrayList<AssignedDevice>();
        }
        return this.assignedDevices;
    }

    /**
     * Gets the value of the assignedComDevices property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the assignedComDevices property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAssignedComDevices().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AssignedComDevice }
     * 
     * 
     */
    public List<AssignedComDevice> getAssignedComDevices() {
        if (assignedComDevices == null) {
            assignedComDevices = new ArrayList<AssignedComDevice>();
        }
        return this.assignedComDevices;
    }

    /**
     * Gets the value of the assignedBusDevices property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the assignedBusDevices property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAssignedBusDevices().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AssignedBusDevice }
     * 
     * 
     */
    public List<AssignedBusDevice> getAssignedBusDevices() {
        if (assignedBusDevices == null) {
            assignedBusDevices = new ArrayList<AssignedBusDevice>();
        }
        return this.assignedBusDevices;
    }

    /**
     * Ruft den Wert der usingHALdispatcher-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isUsingHALdispatcher() {
        return usingHALdispatcher;
    }

    /**
     * Legt den Wert der usingHALdispatcher-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setUsingHALdispatcher(Boolean value) {
        this.usingHALdispatcher = value;
    }

    /**
     * Ruft den Wert der haLdispatcherClassName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHALdispatcherClassName() {
        return haLdispatcherClassName;
    }

    /**
     * Legt den Wert der haLdispatcherClassName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHALdispatcherClassName(String value) {
        this.haLdispatcherClassName = value;
    }

    /**
     * Copies all values of property {@code AssignedDevices} deeply.
     * 
     * @param source
     *     The source to copy from.
     * @param target
     *     The target to copy {@code source} to.
     * @throws NullPointerException
     *     if {@code target} is {@code null}.
     */
    @SuppressWarnings("unchecked")
    private static void copyAssignedDevices(final List<AssignedDevice> source, final List<AssignedDevice> target) {
        // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
        if ((source!= null)&&(!source.isEmpty())) {
            for (final Iterator<?> it = source.iterator(); it.hasNext(); ) {
                final Object next = it.next();
                if (next instanceof AssignedDevice) {
                    // CClassInfo: osh.configuration.system.AssignedDevice
                    target.add(((AssignedDevice) next).clone());
                    continue;
                }
                // Please report this at https://apps.sourceforge.net/mantisbt/ccxjc/
                throw new AssertionError((("Unexpected instance '"+ next)+"' for property 'AssignedDevices' of class 'osh.configuration.system.HALConfiguration'."));
            }
        }
    }

    /**
     * Copies all values of property {@code AssignedComDevices} deeply.
     * 
     * @param source
     *     The source to copy from.
     * @param target
     *     The target to copy {@code source} to.
     * @throws NullPointerException
     *     if {@code target} is {@code null}.
     */
    @SuppressWarnings("unchecked")
    private static void copyAssignedComDevices(final List<AssignedComDevice> source, final List<AssignedComDevice> target) {
        // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
        if ((source!= null)&&(!source.isEmpty())) {
            for (final Iterator<?> it = source.iterator(); it.hasNext(); ) {
                final Object next = it.next();
                if (next instanceof AssignedComDevice) {
                    // CClassInfo: osh.configuration.system.AssignedComDevice
                    target.add(((AssignedComDevice) next).clone());
                    continue;
                }
                // Please report this at https://apps.sourceforge.net/mantisbt/ccxjc/
                throw new AssertionError((("Unexpected instance '"+ next)+"' for property 'AssignedComDevices' of class 'osh.configuration.system.HALConfiguration'."));
            }
        }
    }

    /**
     * Copies all values of property {@code AssignedBusDevices} deeply.
     * 
     * @param source
     *     The source to copy from.
     * @param target
     *     The target to copy {@code source} to.
     * @throws NullPointerException
     *     if {@code target} is {@code null}.
     */
    @SuppressWarnings("unchecked")
    private static void copyAssignedBusDevices(final List<AssignedBusDevice> source, final List<AssignedBusDevice> target) {
        // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
        if ((source!= null)&&(!source.isEmpty())) {
            for (final Iterator<?> it = source.iterator(); it.hasNext(); ) {
                final Object next = it.next();
                if (next instanceof AssignedBusDevice) {
                    // CClassInfo: osh.configuration.system.AssignedBusDevice
                    target.add(((AssignedBusDevice) next).clone());
                    continue;
                }
                // Please report this at https://apps.sourceforge.net/mantisbt/ccxjc/
                throw new AssertionError((("Unexpected instance '"+ next)+"' for property 'AssignedBusDevices' of class 'osh.configuration.system.HALConfiguration'."));
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
    public HALConfiguration clone() {
        try {
            {
                // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
                final HALConfiguration clone = ((HALConfiguration) super.clone());
                // 'AssignedDevices' collection.
                if (this.assignedDevices!= null) {
                    clone.assignedDevices = null;
                    copyAssignedDevices(this.getAssignedDevices(), clone.getAssignedDevices());
                }
                // 'AssignedComDevices' collection.
                if (this.assignedComDevices!= null) {
                    clone.assignedComDevices = null;
                    copyAssignedComDevices(this.getAssignedComDevices(), clone.getAssignedComDevices());
                }
                // 'AssignedBusDevices' collection.
                if (this.assignedBusDevices!= null) {
                    clone.assignedBusDevices = null;
                    copyAssignedBusDevices(this.getAssignedBusDevices(), clone.getAssignedBusDevices());
                }
                // CBuiltinLeafInfo: java.lang.Boolean
                clone.usingHALdispatcher = ((this.usingHALdispatcher == null)?null:this.isUsingHALdispatcher());
                // CBuiltinLeafInfo: java.lang.String
                clone.haLdispatcherClassName = ((this.haLdispatcherClassName == null)?null:this.getHALdispatcherClassName());
                return clone;
            }
        } catch (CloneNotSupportedException e) {
            // Please report this at https://apps.sourceforge.net/mantisbt/ccxjc/
            throw new AssertionError(e);
        }
    }

}
