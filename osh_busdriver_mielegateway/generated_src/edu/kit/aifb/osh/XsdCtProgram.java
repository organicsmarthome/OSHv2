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
 * <p>Java-Klasse für xsdCt.Program complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="xsdCt.Program">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ProgramID" type="{http://www.aifb.kit.edu/osh}xsdSt.ProgramID"/>
 *         &lt;element name="ProgramName" type="{http://www.aifb.kit.edu/osh}xsdSt.Name" minOccurs="0"/>
 *         &lt;element name="Descriptions" type="{http://www.aifb.kit.edu/osh}xsdCt.Descriptions" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "xsdCt.Program", propOrder = {
    "programID",
    "programName",
    "descriptions"
})
public class XsdCtProgram implements Cloneable
{

    @XmlElement(name = "ProgramID")
    protected int programID;
    @XmlElement(name = "ProgramName")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String programName;
    @XmlElement(name = "Descriptions")
    protected XsdCtDescriptions descriptions;

    /**
     * Creates a new {@code XsdCtProgram} instance.
     * 
     */
    public XsdCtProgram() {
        // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
        super();
    }

    /**
     * Creates a new {@code XsdCtProgram} instance by deeply copying a given {@code XsdCtProgram} instance.
     * 
     * 
     * @param o
     *     The instance to copy.
     * @throws NullPointerException
     *     if {@code o} is {@code null}.
     */
    public XsdCtProgram(final XsdCtProgram o) {
        // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
        super();
        if (o == null) {
            throw new NullPointerException("Cannot create a copy of 'XsdCtProgram' from 'null'.");
        }
        // CBuiltinLeafInfo: java.lang.Integer
        this.programID = o.getProgramID();
        // CBuiltinLeafInfo: java.lang.String
        this.programName = ((o.programName == null)?null:o.getProgramName());
        // CClassInfo: edu.kit.aifb.osh.XsdCtDescriptions
        this.descriptions = ((o.descriptions == null)?null:((o.getDescriptions() == null)?null:o.getDescriptions().clone()));
    }

    /**
     * Ruft den Wert der programID-Eigenschaft ab.
     * 
     */
    public int getProgramID() {
        return programID;
    }

    /**
     * Legt den Wert der programID-Eigenschaft fest.
     * 
     */
    public void setProgramID(int value) {
        this.programID = value;
    }

    /**
     * Ruft den Wert der programName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProgramName() {
        return programName;
    }

    /**
     * Legt den Wert der programName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProgramName(String value) {
        this.programName = value;
    }

    /**
     * Ruft den Wert der descriptions-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XsdCtDescriptions }
     *     
     */
    public XsdCtDescriptions getDescriptions() {
        return descriptions;
    }

    /**
     * Legt den Wert der descriptions-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XsdCtDescriptions }
     *     
     */
    public void setDescriptions(XsdCtDescriptions value) {
        this.descriptions = value;
    }

    /**
     * Creates and returns a deep copy of this object.
     * 
     * 
     * @return
     *     A deep copy of this object.
     */
    @Override
    public XsdCtProgram clone() {
        try {
            {
                // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
                final XsdCtProgram clone = ((XsdCtProgram) super.clone());
                // CBuiltinLeafInfo: java.lang.Integer
                clone.programID = this.getProgramID();
                // CBuiltinLeafInfo: java.lang.String
                clone.programName = ((this.programName == null)?null:this.getProgramName());
                // CClassInfo: edu.kit.aifb.osh.XsdCtDescriptions
                clone.descriptions = ((this.descriptions == null)?null:((this.getDescriptions() == null)?null:this.getDescriptions().clone()));
                return clone;
            }
        } catch (CloneNotSupportedException e) {
            // Please report this at https://apps.sourceforge.net/mantisbt/ccxjc/
            throw new AssertionError(e);
        }
    }

}
