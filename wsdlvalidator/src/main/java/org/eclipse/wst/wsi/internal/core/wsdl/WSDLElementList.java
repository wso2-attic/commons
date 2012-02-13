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
package org.eclipse.wst.wsi.internal.core.wsdl;

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.wst.wsi.internal.core.xml.dom.ElementLocation;

/**
 * This class maintains a WSDL element list.
 * 
 * @version 1.0.1
 * @author Peter Brittenham  (peterbr@us.ibm.com)
 */
public class WSDLElementList
{
  /**
   * List of WSDL elements with their location.
   */
  HashMap wsdlElementList = new HashMap();

  /**
   * WSDL element locator.
   */
  public WSDLElementList()
  {
  }

  /**
   * Add element with location information.
   * @param wsdlElement      a WSDL element artifact.
   * @param elementLocation  the location information for the element.
   */
  public void addElement(Object wsdlElement, ElementLocation elementLocation)
  {
    this.wsdlElementList.put(wsdlElement, elementLocation);
  }

  /**
   * Get element location.
   * @param wsdlElement  a WSDL element artifact.
   * @return the location information for the element.
   */
  public ElementLocation getElementLocation(Object wsdlElement)
  {
    return (ElementLocation) this.wsdlElementList.get(wsdlElement);
  }

  /* Return string representation of this object.
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    String string = "WSDLElementList: \n";

    // Get list entries (values?)
    Iterator iterator = this.wsdlElementList.keySet().iterator();

    // Add each entry in the list to the string
    ElementLocation wsdlElementLocation;
    while (iterator.hasNext())
    {
      Object wsdlElement = iterator.next();

      if (wsdlElement != null)
      {
        string += "  " + wsdlElement.getClass().getName() + ": ";
        if ((wsdlElementLocation =
          (ElementLocation) wsdlElementList.get(wsdlElement))
          == null)
        {
          string += "null\n";
        }

        else
        {
          string += "  "
            + wsdlElement.getClass().getName()
            + ": "
            + wsdlElementLocation.getLineNumber()
            + ", "
            + wsdlElementLocation.getColumnNumber()
            + "\n";
        }
      }
    }

    return string;
  }
}
