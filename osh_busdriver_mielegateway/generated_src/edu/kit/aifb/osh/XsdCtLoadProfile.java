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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java-Klasse für xsdCt.LoadProfile complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="xsdCt.LoadProfile">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="LoadProfileSegment" type="{http://www.aifb.kit.edu/osh}xsdCt.LoadProfileSegment" maxOccurs="unbounded"/>
 *         &lt;element name="LoadProfileFinishedPause" type="{http://www.aifb.kit.edu/osh}xsdCt.LoadProfilePause"/>
 *       &lt;/sequence>
 *       &lt;attribute name="loadProfileID" use="required" type="{http://www.aifb.kit.edu/osh}xsdSt.NonNegativeInt" />
 *       &lt;attribute name="loadProfileName" type="{http://www.aifb.kit.edu/osh}xsdSt.Name" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "xsdCt.LoadProfile", propOrder = {
    "loadProfileSegment",
    "loadProfileFinishedPause"
})
public class XsdCtLoadProfile implements Cloneable
{

    @XmlElement(name = "LoadProfileSegment", required = true)
    protected List<XsdCtLoadProfileSegment> loadProfileSegment;
    @XmlElement(name = "LoadProfileFinishedPause", required = true)
    protected XsdCtLoadProfilePause loadProfileFinishedPause;
    @XmlAttribute(name = "loadProfileID", required = true)
    protected int loadProfileID;
    @XmlAttribute(name = "loadProfileName")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String loadProfileName;

    /**
     * Creates a new {@code XsdCtLoadProfile} instance.
     * 
     */
    public XsdCtLoadProfile() {
        // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
        super();
    }

    /**
     * Creates a new {@code XsdCtLoadProfile} instance by deeply copying a given {@code XsdCtLoadProfile} instance.
     * 
     * 
     * @param o
     *     The instance to copy.
     * @throws NullPointerException
     *     if {@code o} is {@code null}.
     */
    public XsdCtLoadProfile(final XsdCtLoadProfile o) {
        // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
        super();
        if (o == null) {
            throw new NullPointerException("Cannot create a copy of 'XsdCtLoadProfile' from 'null'.");
        }
        // 'LoadProfileSegment' collection.
        if (o.loadProfileSegment!= null) {
            copyLoadProfileSegment(o.getLoadProfileSegment(), this.getLoadProfileSegment());
        }
        // CClassInfo: edu.kit.aifb.osh.XsdCtLoadProfilePause
        this.loadProfileFinishedPause = ((o.loadProfileFinishedPause == null)?null:((o.getLoadProfileFinishedPause() == null)?null:o.getLoadProfileFinishedPause().clone()));
        // CBuiltinLeafInfo: java.lang.Integer
        this.loadProfileID = o.getLoadProfileID();
        // CBuiltinLeafInfo: java.lang.String
        this.loadProfileName = ((o.loadProfileName == null)?null:o.getLoadProfileName());
    }

    /**
     * Gets the value of the loadProfileSegment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the loadProfileSegment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLoadProfileSegment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XsdCtLoadProfileSegment }
     * 
     * 
     */
    public List<XsdCtLoadProfileSegment> getLoadProfileSegment() {
        if (loadProfileSegment == null) {
            loadProfileSegment = new ArrayList<XsdCtLoadProfileSegment>();
        }
        return this.loadProfileSegment;
    }

    /**
     * Ruft den Wert der loadProfileFinishedPause-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XsdCtLoadProfilePause }
     *     
     */
    public XsdCtLoadProfilePause getLoadProfileFinishedPause() {
        return loadProfileFinishedPause;
    }

    /**
     * Legt den Wert der loadProfileFinishedPause-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XsdCtLoadProfilePause }
     *     
     */
    public void setLoadProfileFinishedPause(XsdCtLoadProfilePause value) {
        this.loadProfileFinishedPause = value;
    }

    /**
     * Ruft den Wert der loadProfileID-Eigenschaft ab.
     * 
     */
    public int getLoadProfileID() {
        return loadProfileID;
    }

    /**
     * Legt den Wert der loadProfileID-Eigenschaft fest.
     * 
     */
    public void setLoadProfileID(int value) {
        this.loadProfileID = value;
    }

    /**
     * Ruft den Wert der loadProfileName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLoadProfileName() {
        return loadProfileName;
    }

    /**
     * Legt den Wert der loadProfileName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLoadProfileName(String value) {
        this.loadProfileName = value;
    }

    /**
     * Copies all values of property {@code LoadProfileSegment} deeply.
     * 
     * @param source
     *     The source to copy from.
     * @param target
     *     The target to copy {@code source} to.
     * @throws NullPointerException
     *     if {@code target} is {@code null}.
     */
    @SuppressWarnings("unchecked")
    private static void copyLoadProfileSegment(final List<XsdCtLoadProfileSegment> source, final List<XsdCtLoadProfileSegment> target) {
        // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
        if ((source!= null)&&(!source.isEmpty())) {
            for (final Iterator<?> it = source.iterator(); it.hasNext(); ) {
                final Object next = it.next();
                if (next instanceof XsdCtLoadProfileSegment) {
                    // CClassInfo: edu.kit.aifb.osh.XsdCtLoadProfileSegment
                    target.add(((XsdCtLoadProfileSegment) next).clone());
                    continue;
                }
                // Please report this at https://apps.sourceforge.net/mantisbt/ccxjc/
                throw new AssertionError((("Unexpected instance '"+ next)+"' for property 'LoadProfileSegment' of class 'edu.kit.aifb.osh.XsdCtLoadProfile'."));
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
    public XsdCtLoadProfile clone() {
        try {
            {
                // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
                final XsdCtLoadProfile clone = ((XsdCtLoadProfile) super.clone());
                // 'LoadProfileSegment' collection.
                if (this.loadProfileSegment!= null) {
                    clone.loadProfileSegment = null;
                    copyLoadProfileSegment(this.getLoadProfileSegment(), clone.getLoadProfileSegment());
                }
                // CClassInfo: edu.kit.aifb.osh.XsdCtLoadProfilePause
                clone.loadProfileFinishedPause = ((this.loadProfileFinishedPause == null)?null:((this.getLoadProfileFinishedPause() == null)?null:this.getLoadProfileFinishedPause().clone()));
                // CBuiltinLeafInfo: java.lang.Integer
                clone.loadProfileID = this.getLoadProfileID();
                // CBuiltinLeafInfo: java.lang.String
                clone.loadProfileName = ((this.loadProfileName == null)?null:this.getLoadProfileName());
                return clone;
            }
        } catch (CloneNotSupportedException e) {
            // Please report this at https://apps.sourceforge.net/mantisbt/ccxjc/
            throw new AssertionError(e);
        }
    }

}
