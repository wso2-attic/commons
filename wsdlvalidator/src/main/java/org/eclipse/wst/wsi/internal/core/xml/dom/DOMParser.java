/*******************************************************************************
 * Copyright (c) 2002-2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.core.xml.dom;

import org.apache.xerces.dom.ElementImpl;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XNIException;
import org.w3c.dom.Element;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 * This class specializes org.apache.xerces.parsers.DOMParser.
 * 
 * @author Peter  Brittenham (peterbr@us.ibm.com)
 * @version 1.0.1
 */
public class DOMParser extends org.apache.xerces.parsers.DOMParser
{
  /**
   * Locator.
   */
  XMLLocator locator = null;

  /* (non-Javadoc)
   * @see org.apache.xerces.xni.XMLDocumentHandler#startDocument(org.apache.xerces.xni.XMLLocator, java.lang.String, org.apache.xerces.xni.NamespaceContext, org.apache.xerces.xni.Augmentations)
   */
  public void startDocument(
    XMLLocator locator,
    String encoding,
    NamespaceContext context,
    Augmentations augs)
    throws XNIException
  {
    this.locator = locator;
    super.startDocument(locator, encoding, context, augs);
  }

  /* (non-Javadoc)
   * @see org.apache.xerces.xni.XMLDocumentHandler#startElement(org.apache.xerces.xni.QName, org.apache.xerces.xni.XMLAttributes, org.apache.xerces.xni.Augmentations)
   */
  public void startElement(
    QName qname,
    XMLAttributes attributes,
    Augmentations augs)
    throws XNIException
  {
    Element element;
    ElementImpl elementImpl;

    // DEBUG:
    //System.err.println("1-line: " + locator.getLineNumber() + ", column: " + locator.getColumnNumber());

    super.startElement(qname, attributes, augs);

    // DEBUG:
    //System.err.println("2-line: " + locator.getLineNumber() + ", column: " + locator.getColumnNumber());    

    try
    {
      element = (Element) getProperty(CURRENT_ELEMENT_NODE);

      elementImpl = (ElementImpl) element;
      // Setting the user data with an identifier such as ElementLocation.KEY_NAME
      // may be a long term good idea. The setUserData method with no id is used 
      // to support JVMs with alternate versions of Xerces.
      elementImpl.setUserData(new ElementLocation(locator.getLineNumber(), locator.getColumnNumber()));
    }
    catch (ClassCastException cce)
    {
    }
    catch (SAXNotSupportedException snse)
    {
      // DEBUG:
      //System.err.println(snse.toString());
    }
    catch (SAXNotRecognizedException snre)
    {
    }
  }
}
