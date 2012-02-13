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
package org.eclipse.wst.wsi.internal.core.log.impl;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.log.MimePart;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;

/**
 * @author lauzond
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MimePartImpl implements MimePart 
{
  private String mimeContent;
  private String mimeHeaders;
  private String[] boundaryStrings = new String[0];
  
  public MimePartImpl ()
  {
  }
  
  /* (non-Javadoc)
   * @see org.wsi.test.log.MimePart#getContent()
   */
  public String getContent()
  {
    return this.mimeContent;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.log.MimePart#setContent(String)
   */
  public void setContent(String mimeContent)
  {
  	this.mimeContent = mimeContent;
  }

  /**
  /* (non-Javadoc)
   * @see org.eclipse.wst.wsi.internal.core.log.MimePart#getHeaders()
   */
  public String getHeaders()
  {
  	return this.mimeHeaders;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.log.MimePart#setHeaders(String)
   */
  public void setHeaders(String mimeHeaders)
  {
  	this.mimeHeaders = mimeHeaders;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.log.MimePart#getBoundaryStrings()
   */
  public String[] getBoundaryStrings()
  {
  	return this.boundaryStrings;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.log.MimePart#setBoundaryStrings(String[])
   */
  public void setBoundaryStrings(String[] boundaryStrings)
  {
  	this.boundaryStrings = boundaryStrings;
  }

  /**
   * Get string representation of this object.
   */
  public String toString()
  {
    return toXMLString(WSIConstants.NS_NAME_WSI_LOG);
  }

  /* (non-Javadoc)
   * @see org.wsi.test.document.DocumentElement#toXMLString(String)
   */
  public String toXMLString(String namespaceName)
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    String nsName = namespaceName;
    if ((!nsName.equals("") && (!nsName.endsWith(":"))))
      nsName += ":";

    // Add mimePart element
    pw.println("<" + nsName + WSIConstants.ELEM_MIME_PART  + ">");
    
    // Add boundary string element
     for (int i = 0; i < boundaryStrings.length; i++)
    {
        pw.print("<" + nsName + WSIConstants.ELEM_BOUNDARY_STRING + ">");
        pw.print(boundaryStrings[i]);
        pw.println("</" + nsName + WSIConstants.ELEM_BOUNDARY_STRING + ">");
    }
   
    // Add mimeHeaders element
    pw.print("<" + nsName + WSIConstants.ELEM_MIME_HEADERS + ">");
    pw.print(XMLUtils.xmlEscapedString(mimeHeaders));
    pw.println("</" + nsName + WSIConstants.ELEM_MIME_HEADERS + ">");

    // Add encoded content
    pw.print("<" + nsName + WSIConstants.ELEM_MIME_CONTENT + ">");
    if ((mimeContent.indexOf("<") != -1)  || 
        (mimeContent.indexOf(">") != -1)  || 
		(mimeContent.indexOf("\"") != -1) ||
		(mimeContent.indexOf("\'") != -1))
    	pw.print(XMLUtils.xmlEscapedString(mimeContent));
    else
        pw.print(getContent());
    pw.println("</" + nsName + WSIConstants.ELEM_MIME_CONTENT + ">");
 
    // Add end message element
    pw.println("</" + nsName + WSIConstants.ELEM_MIME_PART + ">");

    // Return string
    return sw.toString();
  }

}
