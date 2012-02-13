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
package org.eclipse.wst.wsi.internal.core.common.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.TreeMap;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.common.AddStyleSheet;

/**
 * The implementation for AddStyleSheet element. 
 * 
 * @version 1.0.1
 * @author Peter Brittenham  (peterbr@us.ibm.com)
 */
public class AddStyleSheetImpl implements AddStyleSheet
{
  /**
   * Attribute values.
   */
  protected TreeMap attributeValueList = new TreeMap();

  /**
   * List of attributes.
   */
  protected String[] attributeNameList =
    {
      WSIConstants.ATTR_HREF,
      WSIConstants.ATTR_TYPE,
      WSIConstants.ATTR_TITLE,
      WSIConstants.ATTR_MEDIA,
      WSIConstants.ATTR_CHARSET,
      WSIConstants.ATTR_ALTERNATE };

  /**
   * Set if the style sheet declaration should be a comment.
   */
  protected boolean comment = false;

  /* (non-Javadoc)
   * @see org.wsi.test.common.AddStyleSheet#getHref()
   */
  public String getHref()
  {
    return (String) this.attributeValueList.get(WSIConstants.ATTR_HREF);
  }

  /* (non-Javadoc)
   * @see org.wsi.test.common.AddStyleSheet#setHref(String)
   */
  public void setHref(String href)
  {
    this.attributeValueList.put(WSIConstants.ATTR_HREF, href);
  }

  /* (non-Javadoc)
   * @see org.wsi.test.common.AddStyleSheet#getType()
   */
  public String getType()
  {
    return (String) this.attributeValueList.get(WSIConstants.ATTR_TYPE);
  }

  /* (non-Javadoc)
   * @see org.wsi.test.common.AddStyleSheet#setType(String)
   */
  public void setType(String type)
  {
    this.attributeValueList.put(WSIConstants.ATTR_TYPE, type);
  }

  /* (non-Javadoc)
   * @see org.wsi.test.common.AddStyleSheet#getTitle()
   */
  public String getTitle()
  {
    return (String) this.attributeValueList.get(WSIConstants.ATTR_TITLE);
  }

  /* (non-Javadoc)
   * @see org.wsi.test.common.AddStyleSheet#setTitle(String)
   */
  public void setTitle(String title)
  {
    this.attributeValueList.put(WSIConstants.ATTR_TITLE, title);
  }

  /* (non-Javadoc)
   * @see org.wsi.test.common.AddStyleSheet#getMedia()
   */
  public String getMedia()
  {
    return (String) this.attributeValueList.get(WSIConstants.ATTR_MEDIA);
  }

  /* (non-Javadoc)
   * @see org.wsi.test.common.AddStyleSheet#setMedia(String)
   */
  public void setMedia(String media)
  {
    this.attributeValueList.put(WSIConstants.ATTR_MEDIA, media);
  }

  /* (non-Javadoc)
   * @see org.wsi.test.common.AddStyleSheet#getCharset()
   */
  public String getCharset()
  {
    return (String) this.attributeValueList.get(WSIConstants.ATTR_CHARSET);
  }

  /* (non-Javadoc)
   * @see org.wsi.test.common.AddStyleSheet#setCharset(String)
   */
  public void setCharset(String charset)
  {
    this.attributeValueList.put(WSIConstants.ATTR_CHARSET, charset);
  }

  /* (non-Javadoc)
   * @see org.wsi.test.common.AddStyleSheet#getAlternate()
   */
  public String getAlternate()
  {
    return (String) this.attributeValueList.get(WSIConstants.ATTR_ALTERNATE);
  }

  /* (non-Javadoc)
   * @see org.wsi.test.common.AddStyleSheet#setAlternate(String)
   */
  public void setAlternate(String alternate)
  {
    this.attributeValueList.put(WSIConstants.ATTR_ALTERNATE, alternate);
  }

  /* (non-Javadoc)
   * Set if the style sheet declaration should be a comment.
   */
  public void setComment(boolean comment)
  {
    this.comment = comment;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.common.AddStyleSheet#getStyleSheetString()
   */
  public String getStyleSheetString()
  {
    StringBuffer styleSheet = new StringBuffer();

    if (comment)
      styleSheet.append("<!-- ");

    styleSheet.append("<?xml-stylesheet");

    String value;
    for (int i = 0; i < attributeNameList.length; i++)
    {
      if ((value = (String) attributeValueList.get(attributeNameList[i]))
        != null)
        styleSheet.append(" " + attributeNameList[i] + "=\"" + value + "\"");
    }

    // End element
    styleSheet.append(" ?>");

    if (comment)
      styleSheet.append(" -->");

    return styleSheet.toString();
  }

  /**
   * Get string representation of this object.
   */
  public String toString()
  {
    return getStyleSheetString();
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

    // Config options
    pw.print("      <" + nsName + ELEM_NAME + " ");
    pw.print(WSIConstants.ATTR_HREF + "=\"" + getHref() + "\" ");
    pw.print(WSIConstants.ATTR_TYPE + "=\"" + getType() + "\" ");
    if (getTitle() != null)
      pw.print(WSIConstants.ATTR_TITLE + "=\"" + getTitle() + "\" ");
    if (getMedia() != null)
      pw.print(WSIConstants.ATTR_MEDIA + "=\"" + getMedia() + "\" ");
    if (getCharset() != null)
      pw.print(WSIConstants.ATTR_CHARSET + "=\"" + getCharset() + "\" ");
    if (getAlternate() != null)
      pw.print(WSIConstants.ATTR_ALTERNATE + "=\"" + getAlternate() + "\" ");
    pw.println("/>");

    return sw.toString();
  }

}
