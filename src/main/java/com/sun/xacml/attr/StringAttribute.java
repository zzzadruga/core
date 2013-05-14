/**
 * Copyright (C) 2011-2013 Thales Services - ThereSIS - All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.sun.xacml.attr;

import java.net.URI;

import org.w3c.dom.Node;
import com.sun.xacml.attr.xacmlv3.AttributeValue;


/**
 * Representation of an xs:string value. This class supports parsing
 * xs:string values. All objects of this class are immutable and
 * all methods of the class are thread-safe.
 * <p>
 * Note that there was some confusion in the XACML specification
 * about whether this datatype should be able to handle XML elements (ie,
 * whether &lt;AttributeValue DataType="...string"&gt;&lt;foo/&gt;
 * &lt;/AttributeValue&gt; is valid). This has been clarified to provide
 * the correct requirement that a string may not contain mixed content (ie,
 * the example provided here is invalid). If you need to specify something
 * like this with the string datatype, then you must escape the
 * <code>&lt;</code> and <code>&gt;</code> characters.
 *
 * @since 1.0
 * @author Marco Barreno
 * @author Seth Proctor
 * @author Steve Hanna
 */
public class StringAttribute extends AttributeValue
{
    /**
     * Official name of this type
     */
    public static final String identifier =
        "http://www.w3.org/2001/XMLSchema#string";
 
    /**
     * URI version of name for this type
     */
    public static final URI identifierURI = URI.create(identifier);

    /**
     * The actual String value that this object represents.
     */
    private String value;

    /**
     * Creates a new <code>StringAttribute</code> that represents
     * the String value supplied.
     *
     * @param value the <code>String</code> value to be represented
     */
    public StringAttribute(String value) {
        super(identifierURI);
        this.content.add(value);

        // TODO: document why a null pointer is allowed here and not for other
        // attributes.
        if (value == null) {
            this.value = "";
        } else {
            this.value = value;
        }
    }

    /**
     * Returns a new <code>StringAttribute</code> that represents
     * the xs:string at a particular DOM node.
     *
     * @param root the <code>Node</code> that contains the desired value
     * @return a new <code>StringAttribute</code> representing the
     *         appropriate value (null if there is a parsing error)
     */
    public static StringAttribute getInstance(Node root) {
        Node node = root.getFirstChild();

        // Strings are allowed to have an empty AttributeValue element and are
        // just treated as empty strings...we have to handle this case
        if (node == null)
            return new StringAttribute("");

        // get the type of the node
        short type = node.getNodeType();

        // now see if we have (effectively) a simple string value
        if ((type == Node.TEXT_NODE) || (type == Node.CDATA_SECTION_NODE) ||
            (type == Node.COMMENT_NODE)) {
            return getInstance(node.getNodeValue());
        }

        // there is some confusion in the specifications about what should
        // happen at this point, but the strict reading of the XMLSchema
        // specification suggests that this should be an error
        return null;
    }

    /**
     * Returns a new <code>StringAttribute</code> that represents
     * the xs:string value indicated by the <code>String</code> provided.
     *
     * @param value a string representing the desired value
     * @return a new <code>StringAttribute</code> representing the
     *         appropriate value
     */
    public static StringAttribute getInstance(String value) {
        return new StringAttribute(value);
    }

    /**
     * Returns the <code>String</code> value represented by this object.
     *
     * @return the <code>String</code> value
     */
    public String getValue() {
        return value;
    }

    /**
     * Returns true if the input is an instance of this class and if its
     * value equals the value contained in this class.
     *
     * @param o the object to compare
     *
     * @return true if this object and the input represent the same value
     */
    public boolean equals(Object o) {
        if (! (o instanceof StringAttribute))
            return false;

        StringAttribute other = (StringAttribute)o;

        return value.equals(other.value);
    }

    /**
     * Returns the hashcode value used to index and compare this object with
     * others of the same type. Typically this is the hashcode of the backing
     * data object.
     *
     * @return the object's hashcode value
     */
    public int hashCode() {
        return value.hashCode();
    }

    /**
     * Converts to a String representation.
     *
     * @return the String representation
     */
    public String toString() {
        return "StringAttribute: \"" + value + "\"";
    }

    /**
     *
     */
    public String encode() {
        return value;
    }

}
