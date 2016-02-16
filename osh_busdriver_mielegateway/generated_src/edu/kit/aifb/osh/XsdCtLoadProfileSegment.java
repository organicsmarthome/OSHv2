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
 * <p>Java-Klasse für xsdCt.LoadProfileSegment complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="xsdCt.LoadProfileSegment">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="LoadProfilePause" type="{http://www.aifb.kit.edu/osh}xsdCt.LoadProfilePause"/>
 *         &lt;element name="LoadProfilePhase" type="{http://www.aifb.kit.edu/osh}xsdCt.LoadProfilePhase"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "xsdCt.LoadProfileSegment", propOrder = {
    "loadProfilePause",
    "loadProfilePhase"
})
public class XsdCtLoadProfileSegment implements Cloneable
{

    @XmlElement(name = "LoadProfilePause", required = true)
    protected XsdCtLoadProfilePause loadProfilePause;
    @XmlElement(name = "LoadProfilePhase", required = true)
    protected XsdCtLoadProfilePhase loadProfilePhase;

    /**
     * Creates a new {@code XsdCtLoadProfileSegment} instance.
     * 
     */
    public XsdCtLoadProfileSegment() {
        // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
        super();
    }

    /**
     * Creates a new {@code XsdCtLoadProfileSegment} instance by deeply copying a given {@code XsdCtLoadProfileSegment} instance.
     * 
     * 
     * @param o
     *     The instance to copy.
     * @throws NullPointerException
     *     if {@code o} is {@code null}.
     */
    public XsdCtLoadProfileSegment(final XsdCtLoadProfileSegment o) {
        // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
        super();
        if (o == null) {
            throw new NullPointerException("Cannot create a copy of 'XsdCtLoadProfileSegment' from 'null'.");
        }
        // CClassInfo: edu.kit.aifb.osh.XsdCtLoadProfilePause
        this.loadProfilePause = ((o.loadProfilePause == null)?null:((o.getLoadProfilePause() == null)?null:o.getLoadProfilePause().clone()));
        // CClassInfo: edu.kit.aifb.osh.XsdCtLoadProfilePhase
        this.loadProfilePhase = ((o.loadProfilePhase == null)?null:((o.getLoadProfilePhase() == null)?null:o.getLoadProfilePhase().clone()));
    }

    /**
     * Ruft den Wert der loadProfilePause-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XsdCtLoadProfilePause }
     *     
     */
    public XsdCtLoadProfilePause getLoadProfilePause() {
        return loadProfilePause;
    }

    /**
     * Legt den Wert der loadProfilePause-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XsdCtLoadProfilePause }
     *     
     */
    public void setLoadProfilePause(XsdCtLoadProfilePause value) {
        this.loadProfilePause = value;
    }

    /**
     * Ruft den Wert der loadProfilePhase-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XsdCtLoadProfilePhase }
     *     
     */
    public XsdCtLoadProfilePhase getLoadProfilePhase() {
        return loadProfilePhase;
    }

    /**
     * Legt den Wert der loadProfilePhase-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XsdCtLoadProfilePhase }
     *     
     */
    public void setLoadProfilePhase(XsdCtLoadProfilePhase value) {
        this.loadProfilePhase = value;
    }

    /**
     * Creates and returns a deep copy of this object.
     * 
     * 
     * @return
     *     A deep copy of this object.
     */
    @Override
    public XsdCtLoadProfileSegment clone() {
        try {
            {
                // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
                final XsdCtLoadProfileSegment clone = ((XsdCtLoadProfileSegment) super.clone());
                // CClassInfo: edu.kit.aifb.osh.XsdCtLoadProfilePause
                clone.loadProfilePause = ((this.loadProfilePause == null)?null:((this.getLoadProfilePause() == null)?null:this.getLoadProfilePause().clone()));
                // CClassInfo: edu.kit.aifb.osh.XsdCtLoadProfilePhase
                clone.loadProfilePhase = ((this.loadProfilePhase == null)?null:((this.getLoadProfilePhase() == null)?null:this.getLoadProfilePhase().clone()));
                return clone;
            }
        } catch (CloneNotSupportedException e) {
            // Please report this at https://apps.sourceforge.net/mantisbt/ccxjc/
            throw new AssertionError(e);
        }
    }

}
