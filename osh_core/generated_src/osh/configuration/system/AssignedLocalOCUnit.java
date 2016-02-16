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
 * <p>Java-Klasse für AssignedLocalOCUnit complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="AssignedLocalOCUnit">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="unitDescription" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="localControllerClassName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="localObserverClassName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="localObserverParameters" type="{http://osh/Configuration/System}ConfigurationParameter" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="localControllerParameters" type="{http://osh/Configuration/System}ConfigurationParameter" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AssignedLocalOCUnit", propOrder = {
    "unitDescription",
    "localControllerClassName",
    "localObserverClassName",
    "localObserverParameters",
    "localControllerParameters"
})
public class AssignedLocalOCUnit implements Cloneable
{

    @XmlElement(required = true)
    protected String unitDescription;
    @XmlElement(required = true)
    protected String localControllerClassName;
    @XmlElement(required = true)
    protected String localObserverClassName;
    protected List<ConfigurationParameter> localObserverParameters;
    protected List<ConfigurationParameter> localControllerParameters;

    /**
     * Creates a new {@code AssignedLocalOCUnit} instance.
     * 
     */
    public AssignedLocalOCUnit() {
        // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
        super();
    }

    /**
     * Creates a new {@code AssignedLocalOCUnit} instance by deeply copying a given {@code AssignedLocalOCUnit} instance.
     * 
     * 
     * @param o
     *     The instance to copy.
     * @throws NullPointerException
     *     if {@code o} is {@code null}.
     */
    public AssignedLocalOCUnit(final AssignedLocalOCUnit o) {
        // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
        super();
        if (o == null) {
            throw new NullPointerException("Cannot create a copy of 'AssignedLocalOCUnit' from 'null'.");
        }
        // CBuiltinLeafInfo: java.lang.String
        this.unitDescription = ((o.unitDescription == null)?null:o.getUnitDescription());
        // CBuiltinLeafInfo: java.lang.String
        this.localControllerClassName = ((o.localControllerClassName == null)?null:o.getLocalControllerClassName());
        // CBuiltinLeafInfo: java.lang.String
        this.localObserverClassName = ((o.localObserverClassName == null)?null:o.getLocalObserverClassName());
        // 'LocalObserverParameters' collection.
        if (o.localObserverParameters!= null) {
            copyLocalObserverParameters(o.getLocalObserverParameters(), this.getLocalObserverParameters());
        }
        // 'LocalControllerParameters' collection.
        if (o.localControllerParameters!= null) {
            copyLocalControllerParameters(o.getLocalControllerParameters(), this.getLocalControllerParameters());
        }
    }

    /**
     * Ruft den Wert der unitDescription-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnitDescription() {
        return unitDescription;
    }

    /**
     * Legt den Wert der unitDescription-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnitDescription(String value) {
        this.unitDescription = value;
    }

    /**
     * Ruft den Wert der localControllerClassName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalControllerClassName() {
        return localControllerClassName;
    }

    /**
     * Legt den Wert der localControllerClassName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalControllerClassName(String value) {
        this.localControllerClassName = value;
    }

    /**
     * Ruft den Wert der localObserverClassName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalObserverClassName() {
        return localObserverClassName;
    }

    /**
     * Legt den Wert der localObserverClassName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalObserverClassName(String value) {
        this.localObserverClassName = value;
    }

    /**
     * Gets the value of the localObserverParameters property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the localObserverParameters property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLocalObserverParameters().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ConfigurationParameter }
     * 
     * 
     */
    public List<ConfigurationParameter> getLocalObserverParameters() {
        if (localObserverParameters == null) {
            localObserverParameters = new ArrayList<ConfigurationParameter>();
        }
        return this.localObserverParameters;
    }

    /**
     * Gets the value of the localControllerParameters property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the localControllerParameters property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLocalControllerParameters().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ConfigurationParameter }
     * 
     * 
     */
    public List<ConfigurationParameter> getLocalControllerParameters() {
        if (localControllerParameters == null) {
            localControllerParameters = new ArrayList<ConfigurationParameter>();
        }
        return this.localControllerParameters;
    }

    /**
     * Copies all values of property {@code LocalObserverParameters} deeply.
     * 
     * @param source
     *     The source to copy from.
     * @param target
     *     The target to copy {@code source} to.
     * @throws NullPointerException
     *     if {@code target} is {@code null}.
     */
    @SuppressWarnings("unchecked")
    private static void copyLocalObserverParameters(final List<ConfigurationParameter> source, final List<ConfigurationParameter> target) {
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
                throw new AssertionError((("Unexpected instance '"+ next)+"' for property 'LocalObserverParameters' of class 'osh.configuration.system.AssignedLocalOCUnit'."));
            }
        }
    }

    /**
     * Copies all values of property {@code LocalControllerParameters} deeply.
     * 
     * @param source
     *     The source to copy from.
     * @param target
     *     The target to copy {@code source} to.
     * @throws NullPointerException
     *     if {@code target} is {@code null}.
     */
    @SuppressWarnings("unchecked")
    private static void copyLocalControllerParameters(final List<ConfigurationParameter> source, final List<ConfigurationParameter> target) {
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
                throw new AssertionError((("Unexpected instance '"+ next)+"' for property 'LocalControllerParameters' of class 'osh.configuration.system.AssignedLocalOCUnit'."));
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
    public AssignedLocalOCUnit clone() {
        try {
            {
                // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
                final AssignedLocalOCUnit clone = ((AssignedLocalOCUnit) super.clone());
                // CBuiltinLeafInfo: java.lang.String
                clone.unitDescription = ((this.unitDescription == null)?null:this.getUnitDescription());
                // CBuiltinLeafInfo: java.lang.String
                clone.localControllerClassName = ((this.localControllerClassName == null)?null:this.getLocalControllerClassName());
                // CBuiltinLeafInfo: java.lang.String
                clone.localObserverClassName = ((this.localObserverClassName == null)?null:this.getLocalObserverClassName());
                // 'LocalObserverParameters' collection.
                if (this.localObserverParameters!= null) {
                    clone.localObserverParameters = null;
                    copyLocalObserverParameters(this.getLocalObserverParameters(), clone.getLocalObserverParameters());
                }
                // 'LocalControllerParameters' collection.
                if (this.localControllerParameters!= null) {
                    clone.localControllerParameters = null;
                    copyLocalControllerParameters(this.getLocalControllerParameters(), clone.getLocalControllerParameters());
                }
                return clone;
            }
        } catch (CloneNotSupportedException e) {
            // Please report this at https://apps.sourceforge.net/mantisbt/ccxjc/
            throw new AssertionError(e);
        }
    }

}
