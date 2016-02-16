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
 * <p>Java-Klasse für xsdCt.LoadProfilePhase complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="xsdCt.LoadProfilePhase">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="LoadProfileTick" type="{http://www.aifb.kit.edu/osh}xsdCt.LoadProfileTick" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="loadProfilePhaseID" use="required" type="{http://www.aifb.kit.edu/osh}xsdSt.NonNegativeInt" />
 *       &lt;attribute name="loadProfilePhaseName" type="{http://www.aifb.kit.edu/osh}xsdSt.Name" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "xsdCt.LoadProfilePhase", propOrder = {
    "loadProfileTick"
})
public class XsdCtLoadProfilePhase implements Cloneable
{

    @XmlElement(name = "LoadProfileTick", required = true)
    protected List<XsdCtLoadProfileTick> loadProfileTick;
    @XmlAttribute(name = "loadProfilePhaseID", required = true)
    protected int loadProfilePhaseID;
    @XmlAttribute(name = "loadProfilePhaseName")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String loadProfilePhaseName;

    /**
     * Creates a new {@code XsdCtLoadProfilePhase} instance.
     * 
     */
    public XsdCtLoadProfilePhase() {
        // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
        super();
    }

    /**
     * Creates a new {@code XsdCtLoadProfilePhase} instance by deeply copying a given {@code XsdCtLoadProfilePhase} instance.
     * 
     * 
     * @param o
     *     The instance to copy.
     * @throws NullPointerException
     *     if {@code o} is {@code null}.
     */
    public XsdCtLoadProfilePhase(final XsdCtLoadProfilePhase o) {
        // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
        super();
        if (o == null) {
            throw new NullPointerException("Cannot create a copy of 'XsdCtLoadProfilePhase' from 'null'.");
        }
        // 'LoadProfileTick' collection.
        if (o.loadProfileTick!= null) {
            copyLoadProfileTick(o.getLoadProfileTick(), this.getLoadProfileTick());
        }
        // CBuiltinLeafInfo: java.lang.Integer
        this.loadProfilePhaseID = o.getLoadProfilePhaseID();
        // CBuiltinLeafInfo: java.lang.String
        this.loadProfilePhaseName = ((o.loadProfilePhaseName == null)?null:o.getLoadProfilePhaseName());
    }

    /**
     * Gets the value of the loadProfileTick property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the loadProfileTick property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLoadProfileTick().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XsdCtLoadProfileTick }
     * 
     * 
     */
    public List<XsdCtLoadProfileTick> getLoadProfileTick() {
        if (loadProfileTick == null) {
            loadProfileTick = new ArrayList<XsdCtLoadProfileTick>();
        }
        return this.loadProfileTick;
    }

    /**
     * Ruft den Wert der loadProfilePhaseID-Eigenschaft ab.
     * 
     */
    public int getLoadProfilePhaseID() {
        return loadProfilePhaseID;
    }

    /**
     * Legt den Wert der loadProfilePhaseID-Eigenschaft fest.
     * 
     */
    public void setLoadProfilePhaseID(int value) {
        this.loadProfilePhaseID = value;
    }

    /**
     * Ruft den Wert der loadProfilePhaseName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLoadProfilePhaseName() {
        return loadProfilePhaseName;
    }

    /**
     * Legt den Wert der loadProfilePhaseName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLoadProfilePhaseName(String value) {
        this.loadProfilePhaseName = value;
    }

    /**
     * Copies all values of property {@code LoadProfileTick} deeply.
     * 
     * @param source
     *     The source to copy from.
     * @param target
     *     The target to copy {@code source} to.
     * @throws NullPointerException
     *     if {@code target} is {@code null}.
     */
    @SuppressWarnings("unchecked")
    private static void copyLoadProfileTick(final List<XsdCtLoadProfileTick> source, final List<XsdCtLoadProfileTick> target) {
        // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
        if ((source!= null)&&(!source.isEmpty())) {
            for (final Iterator<?> it = source.iterator(); it.hasNext(); ) {
                final Object next = it.next();
                if (next instanceof XsdCtLoadProfileTick) {
                    // CClassInfo: edu.kit.aifb.osh.XsdCtLoadProfileTick
                    target.add(((XsdCtLoadProfileTick) next).clone());
                    continue;
                }
                // Please report this at https://apps.sourceforge.net/mantisbt/ccxjc/
                throw new AssertionError((("Unexpected instance '"+ next)+"' for property 'LoadProfileTick' of class 'edu.kit.aifb.osh.XsdCtLoadProfilePhase'."));
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
    public XsdCtLoadProfilePhase clone() {
        try {
            {
                // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
                final XsdCtLoadProfilePhase clone = ((XsdCtLoadProfilePhase) super.clone());
                // 'LoadProfileTick' collection.
                if (this.loadProfileTick!= null) {
                    clone.loadProfileTick = null;
                    copyLoadProfileTick(this.getLoadProfileTick(), clone.getLoadProfileTick());
                }
                // CBuiltinLeafInfo: java.lang.Integer
                clone.loadProfilePhaseID = this.getLoadProfilePhaseID();
                // CBuiltinLeafInfo: java.lang.String
                clone.loadProfilePhaseName = ((this.loadProfilePhaseName == null)?null:this.getLoadProfilePhaseName());
                return clone;
            }
        } catch (CloneNotSupportedException e) {
            // Please report this at https://apps.sourceforge.net/mantisbt/ccxjc/
            throw new AssertionError(e);
        }
    }

}
