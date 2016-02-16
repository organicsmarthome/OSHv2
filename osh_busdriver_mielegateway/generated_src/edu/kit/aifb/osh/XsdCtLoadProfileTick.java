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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für xsdCt.LoadProfileTick complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="xsdCt.LoadProfileTick">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Load" type="{http://www.aifb.kit.edu/osh}xsdCt.Load" maxOccurs="unbounded"/>
 *         &lt;element name="LoadParameters" type="{http://www.aifb.kit.edu/osh}xsdCt.LoadParameters" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "xsdCt.LoadProfileTick", propOrder = {
    "load",
    "loadParameters"
})
public class XsdCtLoadProfileTick implements Cloneable
{

    @XmlElement(name = "Load", required = true)
    protected List<XsdCtLoad> load;
    @XmlElement(name = "LoadParameters")
    protected XsdCtLoadParameters loadParameters;

    /**
     * Creates a new {@code XsdCtLoadProfileTick} instance.
     * 
     */
    public XsdCtLoadProfileTick() {
        // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
        super();
    }

    /**
     * Creates a new {@code XsdCtLoadProfileTick} instance by deeply copying a given {@code XsdCtLoadProfileTick} instance.
     * 
     * 
     * @param o
     *     The instance to copy.
     * @throws NullPointerException
     *     if {@code o} is {@code null}.
     */
    public XsdCtLoadProfileTick(final XsdCtLoadProfileTick o) {
        // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
        super();
        if (o == null) {
            throw new NullPointerException("Cannot create a copy of 'XsdCtLoadProfileTick' from 'null'.");
        }
        // 'Load' collection.
        if (o.load!= null) {
            copyLoad(o.getLoad(), this.getLoad());
        }
        // CClassInfo: edu.kit.aifb.osh.XsdCtLoadParameters
        this.loadParameters = ((o.loadParameters == null)?null:((o.getLoadParameters() == null)?null:o.getLoadParameters().clone()));
    }

    /**
     * Gets the value of the load property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the load property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLoad().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XsdCtLoad }
     * 
     * 
     */
    public List<XsdCtLoad> getLoad() {
        if (load == null) {
            load = new ArrayList<XsdCtLoad>();
        }
        return this.load;
    }

    /**
     * Ruft den Wert der loadParameters-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XsdCtLoadParameters }
     *     
     */
    public XsdCtLoadParameters getLoadParameters() {
        return loadParameters;
    }

    /**
     * Legt den Wert der loadParameters-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XsdCtLoadParameters }
     *     
     */
    public void setLoadParameters(XsdCtLoadParameters value) {
        this.loadParameters = value;
    }

    /**
     * Copies all values of property {@code Load} deeply.
     * 
     * @param source
     *     The source to copy from.
     * @param target
     *     The target to copy {@code source} to.
     * @throws NullPointerException
     *     if {@code target} is {@code null}.
     */
    @SuppressWarnings("unchecked")
    private static void copyLoad(final List<XsdCtLoad> source, final List<XsdCtLoad> target) {
        // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
        if ((source!= null)&&(!source.isEmpty())) {
            for (final Iterator<?> it = source.iterator(); it.hasNext(); ) {
                final Object next = it.next();
                if (next instanceof XsdCtLoad) {
                    // CClassInfo: edu.kit.aifb.osh.XsdCtLoad
                    target.add(((XsdCtLoad) next).clone());
                    continue;
                }
                // Please report this at https://apps.sourceforge.net/mantisbt/ccxjc/
                throw new AssertionError((("Unexpected instance '"+ next)+"' for property 'Load' of class 'edu.kit.aifb.osh.XsdCtLoadProfileTick'."));
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
    public XsdCtLoadProfileTick clone() {
        try {
            {
                // CC-XJC Version 2.0.1 Build 2012-03-02T12:09:12+0000
                final XsdCtLoadProfileTick clone = ((XsdCtLoadProfileTick) super.clone());
                // 'Load' collection.
                if (this.load!= null) {
                    clone.load = null;
                    copyLoad(this.getLoad(), clone.getLoad());
                }
                // CClassInfo: edu.kit.aifb.osh.XsdCtLoadParameters
                clone.loadParameters = ((this.loadParameters == null)?null:((this.getLoadParameters() == null)?null:this.getLoadParameters().clone()));
                return clone;
            }
        } catch (CloneNotSupportedException e) {
            // Please report this at https://apps.sourceforge.net/mantisbt/ccxjc/
            throw new AssertionError(e);
        }
    }

}
