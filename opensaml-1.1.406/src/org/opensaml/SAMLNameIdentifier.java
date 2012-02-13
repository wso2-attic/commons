/*
 *  Copyright 2001-2005 Internet2
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensaml;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Hashtable;

import org.apache.log4j.Category;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 *  Represents a SAML Subject
 *
 * @author     Scott Cantor
 * @created    March 25, 2002
 */
public class SAMLNameIdentifier extends SAMLObject implements Cloneable
{
    protected String name = null;
    protected String nameQualifier = null;
    protected String format = null;

    /**  Maps formats to Java class implementations */
    protected static Hashtable /*<String,String>*/ formatMap = new Hashtable();


    /**  Unspecified Format Identifier */    
    public final static String FORMAT_UNSPECIFIED = "urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified";

    /**  Email Format Identifier */    
    public final static String FORMAT_EMAIL = "urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress";

    /**  X.509 Subject Format Identifier */    
    public final static String FORMAT_X509 = "urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName";

    /**  Windows Domain Format Identifier */    
    public final static String FORMAT_WINDOWS = "urn:oasis:names:tc:SAML:1.1:nameid-format:WindowsDomainQualifiedName";

    /**
     *  Registers a class to handle a specific NameID format when parsing XML
     * 
     * @param format          The format URI to register
     * @param className     The Java class that handles this format
     */
    public static void regFactory(String format, String className)
    {
    	formatMap.put(format, className);
    }

    /**
     *  Unregisters a class to handle a specific NameID format when parsing XML
     * 
     * @param type          The format URI to unregister
     */
    public static void unregFactory(String format)
    {
    	formatMap.remove(format);
    }
    
    /**
     *  Locates an implementation class for a NameIdentifier based on Format and constructs it based
     *  on the DOM provided.
     * 
     * @param e     The root of a DOM containing the SAML condition
     * @return SAMLNameIdentifier    A constructed NameIdentifier object
     * 
     * @throws SAMLException    Thrown if an error occurs while constructing the object
     */
    public static SAMLNameIdentifier getInstance(Element e)
        throws SAMLException
    {
        if (e == null)
            throw new MalformedException("SAMLNameIdentifier.getInstance() given an empty DOM");
       
        try {
            String className = (String)formatMap.get(e.getAttributeNS(null,"Format"));
            if (className == null)
                return new SAMLNameIdentifier(e);
            Class implementation = Class.forName(className);
            Class[] paramtypes = {Element.class};
            Object[] params = {e};
            Constructor ctor = implementation.getDeclaredConstructor(paramtypes);
            return (SAMLNameIdentifier)ctor.newInstance(params);
        }
        catch (ClassNotFoundException ex) {
            throw new SAMLException(SAMLException.REQUESTER, "SAMLNameIdentifier.getInstance() unable to locate implementation class for NameIdentifier", ex);
        }
        catch (NoSuchMethodException ex) {
            throw new SAMLException(SAMLException.REQUESTER, "SAMLNameIdentifier.getInstance() unable to bind to constructor for NameIdentifier", ex);
        }
        catch (InstantiationException ex) {
            throw new SAMLException(SAMLException.REQUESTER, "SAMLNameIdentifier.getInstance() unable to build implementation object for NameIdentifier", ex);
        }
        catch (IllegalAccessException ex) {
            throw new SAMLException(SAMLException.REQUESTER, "SAMLNameIdentifier.getInstance() unable to access implementation of NameIdentifier", ex);
        }
        catch (java.lang.reflect.InvocationTargetException ex) {
            ex.printStackTrace();
            Throwable e2 = ex.getTargetException();
            if (e2 instanceof SAMLException)
                throw (SAMLException)e2;
            else
                throw new SAMLException(SAMLException.REQUESTER, "SAMLNameIdentifier.getInstance() caught unknown exception while building NameIdentifier object: " + e2.getMessage());
        }
    }

    /**
     *  Locates an implementation class for a NameIdentifier based on Format and constructs it based
     *  on the stream provided.
     * 
     * @param in     The stream to deserialize from
     * @return SAMLNameIdentifier    A constructed NameIdentifier object
     * 
     * @throws SAMLException    Thrown if an error occurs while constructing the object
     */
    public static SAMLNameIdentifier getInstance(InputStream in)
        throws SAMLException
    {
        try {
            Document doc = XML.parserPool.parse(in);
            return getInstance(doc.getDocumentElement());
        }
        catch (SAXException e) {
            Category.getInstance("SAMLNameIdentifier").error("caught an exception while parsing a stream:\n" + e.getMessage());
            throw new MalformedException("SAMLNameIdentifier.getInstance() caught exception while parsing a stream",e);
        }
        catch (java.io.IOException e) {
            Category.getInstance("SAMLNameIdentifier").error("caught an exception while parsing a stream:\n" + e.getMessage());
            throw new MalformedException("SAMLNameIdentifier.getInstance() caught exception while parsing a stream",e);
        }
    }

    /**
     *  Locates an implementation class for a NameIdentifier based on Format and constructs an empty instance.
     * 
     * @param format     The format, indicating which implementation class to use
     * @return SAMLNameIdentifier    A constructed NameIdentifier object
     * 
     * @throws SAMLException    Thrown if an error occurs while constructing the object
     */
    public static SAMLNameIdentifier getInstance(String format)
        throws SAMLException
    {
        try {
            if (format == null)
                return new SAMLNameIdentifier();
            String className = (String)formatMap.get(format);
            if (className == null)
                return new SAMLNameIdentifier(null, null, format);
            Class implementation = Class.forName(className);
            Class[] paramtypes = {String.class, String.class, String.class};
            Object[] params = {null, null, format};
            Constructor ctor = implementation.getDeclaredConstructor(paramtypes);
            return (SAMLNameIdentifier)ctor.newInstance(params);
        }
        catch (ClassNotFoundException ex) {
            throw new SAMLException(SAMLException.REQUESTER, "SAMLNameIdentifier.getInstance() unable to locate implementation class for NameIdentifier", ex);
        }
        catch (NoSuchMethodException ex) {
            throw new SAMLException(SAMLException.REQUESTER, "SAMLNameIdentifier.getInstance() unable to bind to constructor for NameIdentifier", ex);
        }
        catch (InstantiationException ex) {
            throw new SAMLException(SAMLException.REQUESTER, "SAMLNameIdentifier.getInstance() unable to build implementation object for NameIdentifier", ex);
        }
        catch (IllegalAccessException ex) {
            throw new SAMLException(SAMLException.REQUESTER, "SAMLNameIdentifier.getInstance() unable to access implementation of NameIdentifier", ex);
        }
        catch (java.lang.reflect.InvocationTargetException ex) {
            ex.printStackTrace();
            Throwable e2 = ex.getTargetException();
            if (e2 instanceof SAMLException)
                throw (SAMLException)e2;
            else
                throw new SAMLException(SAMLException.REQUESTER, "SAMLNameIdentifier.getInstance() caught unknown exception while building NameIdentifier object: " + e2.getMessage());
        }
    }
    
    /**
     *  Default constructor
     */
    public SAMLNameIdentifier() {
    }
    
    /**
     *  Builds a name identifier out of its component parts
     *
     * @param  name                 Name of subject (optional)
     * @param  nameQualifier        Federates or qualifies subject name (optional)
     * @param  format               URI describing name semantics and format (optional)
     * @exception  SAMLException    Raised if a name cannot be constructed
     *      from the supplied information
     */
    public SAMLNameIdentifier(String name, String nameQualifier, String format) throws SAMLException {
        this.name = XML.assign(name);
        this.nameQualifier = XML.assign(nameQualifier);
        this.format = XML.assign(format);
    }

    /**
     *  Reconstructs a name identifier from a DOM tree
     *
     * @param  e                  The root of a DOM tree
     * @exception  SAMLException  Thrown if the object cannot be constructed
     */
    public SAMLNameIdentifier(Element e) throws SAMLException {
        fromDOM(e);
    }

    /**
     *  Reconstructs a name identifier from a stream
     *
     * @param  in                   A stream containing XML
     * @exception  SAMLException  Raised if an exception occurs while constructing
     *                              the object.
     */
    public SAMLNameIdentifier(InputStream in) throws SAMLException {
        fromDOM(fromStream(in));
    }

    /**
     * @see org.opensaml.SAMLObject#fromDOM(org.w3c.dom.Element)
     */
    public void fromDOM(Element e) throws SAMLException {
        super.fromDOM(e);

        if (config.getBooleanProperty("org.opensaml.strict-dom-checking") && !XML.isElementNamed(e,XML.SAML_NS,"NameIdentifier"))
            throw new MalformedException("SAMLNameIdentifier.fromDOM() requires saml:NameIdentifier at root");

        nameQualifier = XML.assign(e.getAttributeNS(null,"NameQualifier"));
        format = XML.assign(e.getAttributeNS(null,"Format"));
        name = XML.assign(e.getFirstChild().getNodeValue());
        
        checkValidity();
    }

    /**
     *  Gets the name of the Subject
     *
     * @return    The Subject name
     */
    public String getName() {
        return name;
    }
    
    /**
     *  Sets the name of the Subject
     * 
     * @param   name    The name
     */
    public void setName(String name) {
        if (XML.isEmpty(name))
            throw new IllegalArgumentException("name cannot be empty");
        this.name = name;
        setDirty(true);
    }

    /**
     *  Gets the name qualifier
     *
     * @return    The name qualifier
     */
    public String getNameQualifier() {
        return nameQualifier;
    }

    /**
     *  Sets the name qualifier
     * 
     * @param   nameQualifier    The name qualifier
     */
    public void setNameQualifier(String nameQualifier) {
        this.nameQualifier = XML.assign(nameQualifier);
        setDirty(true);
    }

    /**
     *  Gets the format of the name
     *
     * @return    The name format URI
     */
    public String getFormat() {
        return format;
    }

    /**
     *  Sets the format of the name
     * 
     * @param   format    The name format URI
     */
    public void setFormat(String format) {
        this.format = XML.assign(format);
        setDirty(true);
    }

    /**
     *  @see org.opensaml.SAMLObject#buildRoot(org.w3c.dom.Document,boolean)
     */
    protected Element buildRoot(Document doc, boolean xmlns) {
        Element e = doc.createElementNS(XML.SAML_NS, "NameIdentifier");
        if (xmlns)
            e.setAttributeNS(XML.XMLNS_NS, "xmlns", XML.SAML_NS);
        return e;
    }

    /**
     *  @see org.opensaml.SAMLObject#toDOM(org.w3c.dom.Document,boolean)
     */
    public Node toDOM(Document doc, boolean xmlns) throws SAMLException {
        // Let the base build/verify the DOM root. 
        super.toDOM(doc, xmlns);
        Element nameid = (Element)root;
        
        if (dirty) {
            if (!XML.isEmpty(nameQualifier))
                nameid.setAttributeNS(null,"NameQualifier", nameQualifier);
            if (!XML.isEmpty(format))
                nameid.setAttributeNS(null,"Format", format);
            nameid.appendChild(doc.createTextNode(name));
            setDirty(false);
        }
        else if (xmlns) {
            nameid.setAttributeNS(XML.XMLNS_NS, "xmlns", XML.SAML_NS);
        }
        return root;
    }

    /**
     * @see org.opensaml.SAMLObject#checkValidity()
     */
    public void checkValidity() throws SAMLException {
        if (XML.isEmpty(name))
            throw new MalformedException("NameIdentifier is invalid, requires name");
    }
    
    /**
     *  Copies a SAML object such that no dependencies exist between the original
     *  and the copy
     * 
     * @return      The new object
     * @see java.lang.Object#clone()
     */
    public Object clone() throws CloneNotSupportedException {
        return (SAMLNameIdentifier)super.clone();
    }
}
