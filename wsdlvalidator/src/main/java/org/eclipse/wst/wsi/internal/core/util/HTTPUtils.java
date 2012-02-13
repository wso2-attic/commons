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
package org.eclipse.wst.wsi.internal.core.util;

import java.util.HashMap;
import java.util.Map;
import com.ibm.icu.util.StringTokenizer;

import org.eclipse.wst.wsi.internal.core.WSIException;

/**
 * Set of HTTPL related utilities.
 *
 * @version 1.0.1
 * @author Peter Brittenham
 */

public final class HTTPUtils
{

  /**
   * Find the URL string in a HTTP POST header.
   * @param httpPostHeader  a HTTP POST header. 
   * @return the URL string in a HTTP POST header.
   */
  public static String getURLString(String httpPostHeader)
  {
    String urlString = null;
    int start, end;

    // If POST, then continue
    if (httpPostHeader.startsWith("POST") || httpPostHeader.startsWith("GET"))
    {
      // Start after first space
      start = httpPostHeader.indexOf(' ') + 1;

      // Look for next non-space character
      while (httpPostHeader.charAt(start) == ' ')
        start++;

      // Find next space character
      end = httpPostHeader.indexOf(' ', start);

      // Get URL string which is located in between start and end
      urlString = httpPostHeader.substring(start, end);
    }

    // Else throw exception
    else
    {
      throw new IllegalArgumentException(
        "HTTP header does not contain POST data (was: " + httpPostHeader + ")");
    }

    // Return URL string
    return urlString;
  }
  /**
   * Utility to present HTTP header information as attribute-value pairs, 
   * based on ':' as attribute-value separator.
   * @param httpHeaderString an HTTP header string.
   * @param separators attribute-value separator list.
   * @return HTTP header information as attribute-value pairs.
   * @throws WSIException if unable to create HTTP header information 
   *         as attribute-value pairs.
   */
  public static Map getHttpHeaderTokens(String httpHeaderString, String separators)
    throws WSIException
  {
    StringTokenizer tokenizer = new StringTokenizer(httpHeaderString, "\n\r\f");
    Map map = new HashMap();
    String name = null;
    while (tokenizer.hasMoreTokens())
    {
      String line = tokenizer.nextToken();
      int index = line.indexOf(separators);
      if (index > 0 && index < line.length() - 1)
      {
      	name = line.substring(0, index).toUpperCase();
        map.put(name, line.substring(index + 1).trim());
      }
      else
      {
        if ((name != null) && (line.length()>0) && Character.isWhitespace(line.charAt(0)))
           map.put(name, map.get(name) + " " + line.trim());
      }
    }
    return map;
  }
  
  /**
   * Utility to take sub attribute value from HTTP header
   * @param httpHeaderString an HTTP header string.
   * @param attributeName attribute name.
   * @param subAttributeName sub attribute name.
   * @return sub attribute value from HTTP header.
   * @throws WSIException if unable to get sub attribute value from HTTP header. 
   */
  public static String getHttpHeaderSubAttribute(String httpHeaderString, 
      String attributeName, String subAttributeName)
    throws WSIException
  {
    // get attribute value
    String value = 
      (String) getHttpHeaderTokens(httpHeaderString,":").get(attributeName.toUpperCase());
    if(value != null)
    {
      // search sub attribute token
      int idxQ = value.toUpperCase().indexOf(subAttributeName.toUpperCase()+"=\"");
      int idx = value.toUpperCase().indexOf(subAttributeName.toUpperCase()+"=");
      // if attribute is quoted
      if (idxQ != -1) 
      {
        idxQ += (subAttributeName+"=\"").length();
        int eIdxQ = value.indexOf("\"", idxQ);
        if (eIdxQ != -1)
        {
          return value.substring(idxQ, eIdxQ);
        }
        else 
        {
          return null;
        }
      }
      // if attribute do not quoted
      else if (idx != -1)
      {
        idx += (subAttributeName+"=").length();
        int eIdx = -1;
        // find end space separator
        if ((eIdx = value.indexOf(" ", idx)) != -1)
        {
          return value.substring(idx, eIdx);
        }
        // find coma separator
        else if ((eIdx = value.indexOf(";", idx)) != -1)
        {
          return value.substring(idx, eIdx);
        }
        // this is last attribute
        else 
        {
          return value.substring(idx);
        }
      }
      // if attribute do not found
      else 
      {
        return null;
      }
    }
    return value;
  }

  /**
   * Utility to take attribute value from HTTP header
   * @param httpHeaderString an HTTP header string.
   * @param attributeName attribute name.
   * @return attribute value from HTTP header.
   * @throws WSIException if unable to get attribute value from HTTP header. 
   */
  public static String getHttpHeaderAttribute(String httpHeaderString, 
      String attributeName)
    throws WSIException
  {
    String attributeValue = 
        (String) getHttpHeaderTokens(httpHeaderString,":").get(attributeName.toUpperCase());
    // get first token
    if((attributeValue != null) && (attributeValue.indexOf(";") != -1)) {
      attributeValue = attributeValue.substring(0, attributeValue.indexOf(";"));
    }
    return attributeValue;
  }
}
