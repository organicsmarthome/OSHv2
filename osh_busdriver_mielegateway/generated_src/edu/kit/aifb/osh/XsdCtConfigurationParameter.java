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
 * <p>Java-Klasse für xsdCt.ConfigurationParameter complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="xsdCt.ConfigurationParameter">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ParameterName" type="{http://www.aifb.kit.edu/osh}xsdSt.Name"/>
 *         &lt;element name="ParameterValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "xsdCt.ConfigurationParameter", propOrder = {
    "parameterName",
    "parameterValue"
})
public class XsdCtConfigurationParameter implements Cloneable
{

    @XmlElement(name = "ParameterName", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String parameterName;
    @XmlElement(name = "ParameterValue", required = true)
    protected String parameterValue;

    /**
     * Creates a new {@code XsdCtConfigurationParameter} instance.
     * 
     */
    public XsdCtConfigurationParameter() {
        // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
        super();
    }

    /**
     * Creates a new {@code XsdCtConfigurationParameter} instance by deeply copying a given {@code XsdCtConfigurationParameter} instance.
     * 
     * 
     * @param o
     *     The instance to copy.
     * @throws NullPointerException
     *     if {@code o} is {@code null}.
     */
    public XsdCtConfigurationParameter(final XsdCtConfigurationParameter o) {
        // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
        super();
        if (o == null) {
            throw new NullPointerException("Cannot create a copy of 'XsdCtConfigurationParameter' from 'null'.");
        }
        // CBuiltinLeafInfo: java.lang.String
        this.parameterName = ((o.parameterName == null)?null:o.getParameterName());
        // CBuiltinLeafInfo: java.lang.String
        this.parameterValue = ((o.parameterValue == null)?null:o.getParameterValue());
    }

    /**
     * Ruft den Wert der parameterName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParameterName() {
        return parameterName;
    }

    /**
     * Legt den Wert der parameterName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParameterName(String value) {
        this.parameterName = value;
    }

    /**
     * Ruft den Wert der parameterValue-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParameterValue() {
        return parameterValue;
    }

    /**
     * Legt den Wert der parameterValue-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParameterValue(String value) {
        this.parameterValue = value;
    }

    /**
     * Creates and returns a deep copy of this object.
     * 
     * 
     * @return
     *     A deep copy of this object.
     */
    @Override
    public XsdCtConfigurationParameter clone() {
        try {
            {
                // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
                final XsdCtConfigurationParameter clone = ((XsdCtConfigurationParameter) super.clone());
                // CBuiltinLeafInfo: java.lang.String
                clone.parameterName = ((this.parameterName == null)?null:this.getParameterName());
                // CBuiltinLeafInfo: java.lang.String
                clone.parameterValue = ((this.parameterValue == null)?null:this.getParameterValue());
                return clone;
            }
        } catch (CloneNotSupportedException e) {
            // Please report this at https://apps.sourceforge.net/mantisbt/ccxjc/
            throw new AssertionError(e);
        }
    }

}
