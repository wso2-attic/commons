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

public final class MIMEUtils
{

  /**
   * Utility to present MIME header information as attribute-value pairs, 
   * based on ':' as attribute-value separator.
   * @param mimeHeaderString an MIME header string.
   * @param separators attribute-value separator list.
   * @return MIME header information as attribute-value pairs.
   * @throws WSIException if unable to create MIME header information 
   *         as attribute-value pairs.
   */
  public static Map getMimeHeaderTokens(String mimeHeaderString, String separators)
    throws WSIException
  {
    StringTokenizer tokenizer = new StringTokenizer(mimeHeaderString, "\n\r\f");
    Map map = new HashMap();
    while (tokenizer.hasMoreTokens())
    {
      String line = tokenizer.nextToken();
      int index = line.indexOf(separators);
      if (index > 0 && index < line.length() - 1)
      {
        map.put(line.substring(0, index).toUpperCase(), line.substring(index + 1).trim());
      }
    }
    return map;
  }
  
  /**
   * Utility to take sub attribute value from MIME header
   * @param mimeHeaderString an MIME header string.
   * @param attributeName attribute name.
   * @param subAttributeName sub attribute name.
   * @return sub attribute value from MIME header.
   * @throws WSIException if unable to get sub attribute value from MIME header. 
   */
  public static String getMimeHeaderSubAttribute(String mimeHeaderString, 
      String attributeName, String subAttributeName)
    throws WSIException
  {
    // get attribute value
    String value = 
      (String) getMimeHeaderTokens(mimeHeaderString,":").get(attributeName.toUpperCase());
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
      // if attribute is not quoted
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
   * Utility to take attribute value from MIME header
   * @param mimeHeaderString an MIME header string.
   * @param attributeName attribute name.
   * @return attribute value from MIME header.
   * @throws WSIException if unable to get attribute value from MIME header. 
   */
  public static String getMimeHeaderAttribute(String mimeHeaderString, 
      String attributeName)
    throws WSIException
  {
    String attributeValue = 
        (String) getMimeHeaderTokens(mimeHeaderString,":").get(attributeName.toUpperCase());
    // get first token
    if((attributeValue != null) && (attributeValue.indexOf(";") != -1)) {
      attributeValue = attributeValue.substring(0, attributeValue.indexOf(";"));
    }
    return attributeValue;
  }
}
