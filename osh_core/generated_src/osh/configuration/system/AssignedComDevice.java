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
 * <p>Java-Klasse für AssignedComDevice complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="AssignedComDevice">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="comDeviceID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="comDeviceType" type="{http://osh/Configuration/System}ComDeviceTypes"/>
 *         &lt;element name="comDeviceClassification" type="{http://osh/Configuration/System}ComDeviceClassification"/>
 *         &lt;element name="comDeviceDescription" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="comManagerClassName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="comDriverClassName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="comDriverParameters" type="{http://osh/Configuration/System}ConfigurationParameter" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AssignedComDevice", propOrder = {
    "comDeviceID",
    "comDeviceType",
    "comDeviceClassification",
    "comDeviceDescription",
    "comManagerClassName",
    "comDriverClassName",
    "comDriverParameters"
})
public class AssignedComDevice implements Cloneable
{

    @XmlElement(required = true)
    protected String comDeviceID;
    @XmlElement(required = true)
    protected ComDeviceTypes comDeviceType;
    @XmlElement(required = true)
    protected ComDeviceClassification comDeviceClassification;
    @XmlElement(required = true)
    protected String comDeviceDescription;
    @XmlElement(required = true)
    protected String comManagerClassName;
    @XmlElement(required = true)
    protected String comDriverClassName;
    protected List<ConfigurationParameter> comDriverParameters;

    /**
     * Creates a new {@code AssignedComDevice} instance.
     * 
     */
    public AssignedComDevice() {
        // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
        super();
    }

    /**
     * Creates a new {@code AssignedComDevice} instance by deeply copying a given {@code AssignedComDevice} instance.
     * 
     * 
     * @param o
     *     The instance to copy.
     * @throws NullPointerException
     *     if {@code o} is {@code null}.
     */
    public AssignedComDevice(final AssignedComDevice o) {
        // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
        super();
        if (o == null) {
            throw new NullPointerException("Cannot create a copy of 'AssignedComDevice' from 'null'.");
        }
        // CBuiltinLeafInfo: java.lang.String
        this.comDeviceID = ((o.comDeviceID == null)?null:o.getComDeviceID());
        // CEnumLeafInfo: osh.configuration.system.ComDeviceTypes
        this.comDeviceType = ((o.comDeviceType == null)?null:o.getComDeviceType());
        // CEnumLeafInfo: osh.configuration.system.ComDeviceClassification
        this.comDeviceClassification = ((o.comDeviceClassification == null)?null:o.getComDeviceClassification());
        // CBuiltinLeafInfo: java.lang.String
        this.comDeviceDescription = ((o.comDeviceDescription == null)?null:o.getComDeviceDescription());
        // CBuiltinLeafInfo: java.lang.String
        this.comManagerClassName = ((o.comManagerClassName == null)?null:o.getComManagerClassName());
        // CBuiltinLeafInfo: java.lang.String
        this.comDriverClassName = ((o.comDriverClassName == null)?null:o.getComDriverClassName());
        // 'ComDriverParameters' collection.
        if (o.comDriverParameters!= null) {
            copyComDriverParameters(o.getComDriverParameters(), this.getComDriverParameters());
        }
    }

    /**
     * Ruft den Wert der comDeviceID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComDeviceID() {
        return comDeviceID;
    }

    /**
     * Legt den Wert der comDeviceID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComDeviceID(String value) {
        this.comDeviceID = value;
    }

    /**
     * Ruft den Wert der comDeviceType-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ComDeviceTypes }
     *     
     */
    public ComDeviceTypes getComDeviceType() {
        return comDeviceType;
    }

    /**
     * Legt den Wert der comDeviceType-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ComDeviceTypes }
     *     
     */
    public void setComDeviceType(ComDeviceTypes value) {
        this.comDeviceType = value;
    }

    /**
     * Ruft den Wert der comDeviceClassification-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ComDeviceClassification }
     *     
     */
    public ComDeviceClassification getComDeviceClassification() {
        return comDeviceClassification;
    }

    /**
     * Legt den Wert der comDeviceClassification-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ComDeviceClassification }
     *     
     */
    public void setComDeviceClassification(ComDeviceClassification value) {
        this.comDeviceClassification = value;
    }

    /**
     * Ruft den Wert der comDeviceDescription-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComDeviceDescription() {
        return comDeviceDescription;
    }

    /**
     * Legt den Wert der comDeviceDescription-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComDeviceDescription(String value) {
        this.comDeviceDescription = value;
    }

    /**
     * Ruft den Wert der comManagerClassName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComManagerClassName() {
        return comManagerClassName;
    }

    /**
     * Legt den Wert der comManagerClassName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComManagerClassName(String value) {
        this.comManagerClassName = value;
    }

    /**
     * Ruft den Wert der comDriverClassName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComDriverClassName() {
        return comDriverClassName;
    }

    /**
     * Legt den Wert der comDriverClassName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComDriverClassName(String value) {
        this.comDriverClassName = value;
    }

    /**
     * Gets the value of the comDriverParameters property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the comDriverParameters property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComDriverParameters().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ConfigurationParameter }
     * 
     * 
     */
    public List<ConfigurationParameter> getComDriverParameters() {
        if (comDriverParameters == null) {
            comDriverParameters = new ArrayList<ConfigurationParameter>();
        }
        return this.comDriverParameters;
    }

    /**
     * Copies all values of property {@code ComDriverParameters} deeply.
     * 
     * @param source
     *     The source to copy from.
     * @param target
     *     The target to copy {@code source} to.
     * @throws NullPointerException
     *     if {@code target} is {@code null}.
     */
    @SuppressWarnings("unchecked")
    private static void copyComDriverParameters(final List<ConfigurationParameter> source, final List<ConfigurationParameter> target) {
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
                throw new AssertionError((("Unexpected instance '"+ next)+"' for property 'ComDriverParameters' of class 'osh.configuration.system.AssignedComDevice'."));
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
    public AssignedComDevice clone() {
        try {
            {
                // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
                final AssignedComDevice clone = ((AssignedComDevice) super.clone());
                // CBuiltinLeafInfo: java.lang.String
                clone.comDeviceID = ((this.comDeviceID == null)?null:this.getComDeviceID());
                // CEnumLeafInfo: osh.configuration.system.ComDeviceTypes
                clone.comDeviceType = ((this.comDeviceType == null)?null:this.getComDeviceType());
                // CEnumLeafInfo: osh.configuration.system.ComDeviceClassification
                clone.comDeviceClassification = ((this.comDeviceClassification == null)?null:this.getComDeviceClassification());
                // CBuiltinLeafInfo: java.lang.String
                clone.comDeviceDescription = ((this.comDeviceDescription == null)?null:this.getComDeviceDescription());
                // CBuiltinLeafInfo: java.lang.String
                clone.comManagerClassName = ((this.comManagerClassName == null)?null:this.getComManagerClassName());
                // CBuiltinLeafInfo: java.lang.String
                clone.comDriverClassName = ((this.comDriverClassName == null)?null:this.getComDriverClassName());
                // 'ComDriverParameters' collection.
                if (this.comDriverParameters!= null) {
                    clone.comDriverParameters = null;
                    copyComDriverParameters(this.getComDriverParameters(), clone.getComDriverParameters());
                }
                return clone;
            }
        } catch (CloneNotSupportedException e) {
            // Please report this at https://apps.sourceforge.net/mantisbt/ccxjc/
            throw new AssertionError(e);
        }
    }

}
