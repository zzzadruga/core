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

import com.sun.xacml.ParsingException;

import java.net.URI;

import java.util.Arrays;

import org.w3c.dom.Node;

import com.sun.xacml.attr.xacmlv3.AttributeValue;

/**
 * Representation of an xsi:hexBinary value. This class supports parsing
 * xsi:hexBinary values. All objects of this class are immutable and
 * all methods of the class are thread-safe.
 *
 * @since 1.0
 * @author Steve Hanna
 */
public class HexBinaryAttribute extends AttributeValue
{
    /**
     * Official name of this type
     */
    public static final String identifier =
        "http://www.w3.org/2001/XMLSchema#hexBinary";
 
    /**
     * URI version of name for this type
     */
    public static final URI identifierURI = URI.create(identifier);

    /**
     * The actual binary value that this object represents.
     */
    private byte [] value;

    /**
     * The value returned by toString(). Cached, but only
     * generated if needed.
     */
    private String strValue;

    /**
     * Creates a new <code>HexBinaryAttribute</code> that represents
     * the byte [] value supplied.
     *
     * @param value the <code>byte []</code> value to be represented
     */
    public HexBinaryAttribute(byte [] value) {
        super(identifierURI);

        // This will throw a NullPointerException if value == null.
        // That's what we want in that case.
        this.value = (byte []) value.clone();
    }

    /**
     * Returns a new <code>HexBinaryAttribute</code> that represents
     * the xsi:hexBinary at a particular DOM node.
     *
     * @param root the <code>Node</code> that contains the desired value
     * @return a new <code>HexBinaryAttribute</code> representing the
     *         appropriate value
     * @exception ParsingException if a parsing error occurs
     */
    public static HexBinaryAttribute getInstance(Node root)
        throws ParsingException {
        return getInstance(root.getFirstChild().getNodeValue());
    }

    /**
     * Returns a new <code>HexBinaryAttribute</code> that represents
     * the xsi:hexBinary value indicated by the string provided.
     *
     * @param value a string representing the desired value
     * @return a new <code>HexBinaryAttribute</code> representing the
     *         desired value
     * @exception ParsingException if a parsing error occurs
     */
    public static HexBinaryAttribute getInstance(String value)
        throws ParsingException {
        byte [] bytes = hexToBin(value);

        if (bytes == null)
            throw new ParsingException("Couldn't parse purported " +
                                       "hex string: " + value);

        return new HexBinaryAttribute(bytes);
    }

    /**
     * Returns the <code>byte []</code> value represented by this object.
     * Note that this value is cloned before returning to prevent
     * unauthorized modifications.
     *
     * @return the <code>byte []</code> value
     */
    public byte [] getValue() {
        return (byte []) value.clone();
    }

    /**
     * Returns the hashcode value used to index and compare this object with
     * others of the same type. Typically this is the hashcode of the backing
     * data object.
     *
     * @return the object's hashcode value
     */
    public int hashCode() {
        int code = (int)(value[0]);

        for (int i = 1; i < value.length; i++) {
            code *= 31;
            code += (int)(value[i]);
        }

        return code;
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
        if (! (o instanceof HexBinaryAttribute))
            return false;

        HexBinaryAttribute other = (HexBinaryAttribute)o;

        return Arrays.equals(value, other.value);
    }

    /**
     * Return the int value of a hex character. Return -1 if the
     * character is not a valid hex character.
     */
    private static int hexToBinNibble(char c) {
        int result = -1;

        if ((c >= '0') && (c <= '9'))
            result = (c - '0');
        else {
            if ((c >= 'a') && (c <= 'f'))
                result = (c - 'a') + 10;
            else {
                if ((c >= 'A') && (c <= 'F'))
                    result = (c - 'A') + 10;
                // else pick up the -1 value set above
            }
        }
        return result;
    }

    /**
     * Parse a hex string, returning a new byte array containing the
     * value. Return null in case of a parsing error.
     *
     * @param hex the hex string
     * @return a new byte array containing the value (or null)
     */
    private static byte [] hexToBin(String hex) {
        int len = hex.length();
        // Must have an even number of hex digits
        if (len % 2 != 0)
            return null;
        int byteCount = len / 2;
        byte [] bytes = new byte [byteCount];

        int charIndex = 0;
        for (int byteIndex = 0; byteIndex < byteCount; byteIndex++) {
            int hiNibble = hexToBinNibble(hex.charAt(charIndex++));
            int loNibble = hexToBinNibble(hex.charAt(charIndex++));
            if ((hiNibble < 0) || (loNibble < 0))
                return null;
            bytes[byteIndex] = (byte) (hiNibble * 16 + loNibble);
        }
        return bytes;
    }

    /**
     * Return the hex character for a particular nibble (half a byte).
     *
     * @param nibble a value 0-15
     * @return hex character for that nibble (using A-F for 10-15)
     */
    private static char binToHexNibble(int nibble) {
        char result = (char) 0;

        if (nibble < 10)
            result = (char) (nibble + '0');
        else
            result = (char) ((nibble - 10) + 'A');

        return result;
    }

    /**
     * Return a straight hexadecimal conversion of a byte array.
     * This is a String containing only hex digits.
     *
     * @param bytes the byte array
     * @return the hex version
     */
    private static String binToHex(byte [] bytes) {
        int byteLength = bytes.length;
        char [] chars = new char [byteLength * 2];
        int charIndex = 0;
        
        for (int byteIndex = 0; byteIndex < byteLength; byteIndex++) {
            byte b = bytes[byteIndex];
            chars[charIndex++] = binToHexNibble((b >> 4) & 0xf);
            chars[charIndex++] = binToHexNibble(b & 0xf);
        }

        return new String(chars);
    }

    /**
     * Returns a String representation.
     *
     * @return the String representation
     */
    public String toString() {
        if (strValue == null)
            strValue = binToHex(value);

        return "HexBinaryAttribute: [\n" + strValue + "]\n";
    }

    /**
     *
     */
    public String encode() {
        if (strValue == null)
            strValue = binToHex(value);

        return strValue;
    }

}
