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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

/**
 * This class checks the cookies conform to RFC2965.
 * 
 * @author Baltak
 */
public class SetCookie2Validator
{

  private Vector commentVect = new Vector();
  private Vector commentURLVect = new Vector();
  private Vector domainVect = new Vector();
  private Vector max_AgeVect = new Vector();
  private Vector pathVect = new Vector();
  private Vector portVect = new Vector();
  private Vector versionVect = new Vector();

  private boolean path = false;

  /**
   * Method resetFlags.
   */
  private void resetPath()
  {
    path = false;
  }

  /**
   * Check if the string satisfy the "Set-Cookie2" header value.
   * @param str String 
   * @return boolean
   */
  public boolean isSetCookie2(String str)
  {

    try
    {
      int index = 0;
      int newIndex = 0;
      while (true)
      {
        newIndex = getLastCookie(str, index);
        if (index == newIndex)
          return false;
        // skip spaces
        index = newIndex;
        index = skipSpaces(str, index);
        if (index == str.length())
        {
          return true;
        }
        else
        {
          if (str.charAt(index) != ',')
            return false;
          index++;
          // skip spaces
          index = skipSpaces(str, index);
        }

      }
    }
    catch (Throwable th)
    {
      return false;
    }

  }

  /**
   * Method getLastCookie.
   * @param str
   * @param startIndex
   * @return int
   */
  private int getLastCookie(String str, int startIndex)
  {

    resetPath();

    int index = startIndex;
    // find token
    int newIndex = BasicRules.getLastToken(str, index);
    if (index == newIndex)
      return startIndex;

    index = newIndex;
    if (index == str.length())
      return startIndex;

    // if not '=' - error
    if (str.charAt(index) != '=')
      return startIndex;

    index++;
    if (index == str.length())
      return startIndex;

    // get value
    newIndex = getLastValue(str, index);
    if (index == newIndex)
      return startIndex;

    while (true)
    {
      index = newIndex;
      if (index == str.length())
        return index;

      if (str.charAt(index) != ';')
        return index;
      index++;
      // skip spaces
      index = skipSpaces(str, index);

      if (index == str.length())
        return startIndex;
      newIndex = getLastSetCookieAv(str, index);
      if (index == newIndex)
        return startIndex;
    }
  }

  /**
   * Method getLastValue.
   * @param str
   * @param startIndex
   * @return int
   */
  private int getLastValue(String str, int startIndex)
  {

    int index = BasicRules.getLastToken(str, startIndex);
    if (index != startIndex)
      return index;
    index = BasicRules.getLastQuotedString(str, startIndex);
    if (index != startIndex)
      return index;

    return startIndex;
  }

  /**
   * Method getLastSetCookieAv.
   * @param str
   * @param startIndex
   * @return int
   */
  private int getLastSetCookieAv(String str, int startIndex)
  {

    int index = startIndex;
    int newIndex = 0;

    if (str.startsWith("Comment=", index))
    {
      index += "Comment=".length();
      newIndex = getLastValue(str, index);
      if (index == newIndex)
        return startIndex;
      // debug ->
      String qqq = getValue(str, index, newIndex);
      commentVect.add(qqq);
      // debug <-
      return newIndex;
    }
    else if (str.startsWith("CommentURL=", index))
    {
      index += "CommentURL=".length();

      if (str.charAt(index) != '\"')
        return startIndex;

      newIndex = str.indexOf('\"', index + 1);
      if (newIndex == -1)
        return startIndex;
      try
      {
        new URL(str.substring(index + 1, newIndex));
      }
      catch (MalformedURLException mue)
      {
        return startIndex;
      }
      // debug ->
      String qqq = getValue(str, index + 1, newIndex);
      commentURLVect.add(qqq);
      // debug <-
      newIndex++;
      return newIndex;
    }
    else if (str.startsWith("Discard", index))
    {
      return startIndex + "Discard".length();
    }
    else if (str.startsWith("Domain=", index))
    {
      index += "Domain=".length();

      newIndex = getLastValue(str, index);
      if (index == newIndex)
        return startIndex;

      // debug ->
      String qqq = getValue(str, index, newIndex);
      domainVect.add(qqq);
      // debug <-
      return newIndex;
    }
    else if (str.startsWith("Max-Age=", index))
    {
      index += "Max-Age=".length();

      newIndex = getLastValue(str, index);
      if (index == newIndex)
        return startIndex;

      String qqq = getValue(str, index, newIndex);
      try
      {
        Integer.parseInt(qqq);
      }
      catch (NumberFormatException nfe)
      {
        return startIndex;
      }
      // debug ->
      max_AgeVect.add(qqq);
      // debug <-
      return newIndex;
    }
    else if (str.startsWith("Path=", index))
    {
      index += "Path=".length();

      newIndex = getLastValue(str, index);
      if (index == newIndex)
        return startIndex;

      String qqq = getValue(str, index, newIndex);
      if (path == false)
        pathVect.add(qqq);
      path = true;
      return newIndex;
    }
    else if (str.startsWith("Port", index))
    {
      index += "Port".length();
      if (str.charAt(index) != '=')
        return index;

      index++;
      if (str.charAt(index) != '\"')
        return startIndex;

      index++;
      newIndex = getLastPortList(str, index);
      if (index == newIndex)
        return startIndex;

      // debug ->
      String qqq = getValue(str, index, newIndex);
      portVect.add(qqq);
      // debug <-

      index = newIndex;
      if (str.charAt(index) != '\"')
        return startIndex;
      index++;
      return index;

    }
    else if (str.startsWith("Secure", index))
    {
      return startIndex + "Secure".length();
    }
    else if (str.startsWith("Version=", index))
    {
      index += "Version=".length();

      newIndex = getLastDIGIT(str, index);
      if (index == newIndex)
        return startIndex;

      // debug ->
      String qqq = getValue(str, index, newIndex);
      versionVect.add(qqq);
      // debug <-
      return newIndex;
    }
    else
    {
      return startIndex;
    }
  }

  /**
   * Method getLastPortList.
   * @param str
   * @param startIndex
   * @return int
   */
  private int getLastPortList(String str, int startIndex)
  {
    int index = startIndex;
    int newIndex = 0;

    while (true)
    {
      newIndex = getLastDIGIT(str, index);
      if (index == newIndex)
        return startIndex;

      index = newIndex;
      if (str.charAt(index) != ',')
        return index;
      index++;
    }
  }

  /**
   * Method getLastDIGIT.
   * @param str
   * @param startIndex
   * @return int
   */
  private int getLastDIGIT(String str, int startIndex)
  {
    int index = startIndex;
    for (; index < str.length(); index++)
    {
      if (!BasicRules.isDIGIT(str.charAt(index)))
        return index;
    }
    return index;
  }

  /**
   * Check if the string satisfy the "Cookie" header value.
   * @param str String
   * @return boolean
   */
  public boolean isCookie(String str)
  {
    int index = 0;
    int newIndex = 0;

    try
    {
      newIndex = getLastVersion(str, index);
      if (index == newIndex)
        return false;

      index = newIndex;
      index = skipSpaces(str, index);
      if (str.charAt(index) != ';' && str.charAt(index) != ',')
        return false;

      index++;
      index = skipSpaces(str, index);

      while (true)
      {
        newIndex = getLastCookiesValue(str, index);
        if (index == newIndex)
          return false;

        index = newIndex;
        if (index == str.length())
          return true;

        if (str.charAt(index) != ';' && str.charAt(index) != ',')
          return false;
        index++;
        index = skipSpaces(str, index);
      }
    }
    catch (Throwable th)
    {
      return false;
    }
  }

  /**
   * Method getLastVersion.
   * @param str
   * @param startIndex
   * @return int
   */
  private int getLastVersion(String str, int startIndex)
  {
    int index = startIndex;
    int newIndex = 0;
    if (!str.startsWith("$Version="))
      return startIndex;
    index += "$Version=".length();
    newIndex = getLastValue(str, index);
    if (index == newIndex)
      return startIndex;

    return newIndex;
  }

  /**
   * Method getLastCookiesValue.
   * @param str
   * @param startIndex
   * @return int
   */
  private int getLastCookiesValue(String str, int startIndex)
  {
    int index = startIndex;
    int newIndex = 0;
    int oldIndex = 0;

    // find token
    newIndex = BasicRules.getLastToken(str, index);
    if (index == newIndex)
      return startIndex;

    index = newIndex;
    if (index == str.length())
      return startIndex;

    // if not '=' - error
    if (str.charAt(index) != '=')
      return startIndex;

    index++;
    if (index == str.length())
      return startIndex;

    // get value
    newIndex = getLastValue(str, index);
    if (index == newIndex)
      return startIndex;

    index = newIndex;
    if (index == str.length())
      return index;

    if (str.charAt(index) != ';')
      return index;

    // process [";" path] [";" domain] [";" port] 
    oldIndex = index;
    index++;
    if (index == str.length())
      return startIndex;

    index = skipSpaces(str, index);
    if (index == str.length())
      return startIndex;

    // process path
    newIndex = getLastPath(str, index);
    if (index != newIndex)
    {
      index = newIndex;
      if (index == str.length())
        return index;

      if (str.charAt(index) != ';')
        return index;

      oldIndex = index;
      index++;
      if (index == str.length())
        return startIndex;

      index = skipSpaces(str, index);
      if (index == str.length())
        return startIndex;

    }

    // process domain
    newIndex = getLastDomain(str, index);
    if (index != newIndex)
    {
      index = newIndex;
      if (index == str.length())
        return index;

      if (str.charAt(index) != ';')
        return index;

      oldIndex = index;
      index++;
      if (index == str.length())
        return startIndex;

      index = skipSpaces(str, index);
      if (index == str.length())
        return startIndex;

    }

    // process port
    newIndex = getLastPort(str, index);
    if (index != newIndex)
      return newIndex;
    else
      return oldIndex;
  }

  /**
   * Method getLastPath.
   * @param str
   * @param startIndex
   * @return int
   */
  private int getLastPath(String str, int startIndex)
  {
    int index = startIndex;
    int newIndex = 0;

    if (!str.startsWith("$Path=", index))
      return startIndex;
    index += "$Path=".length();
    newIndex = getLastValue(str, index);
    if (index == newIndex)
      return startIndex;

    return newIndex;
  }

  /**
   * Method getLastDomain.
   * @param str
   * @param startIndex
   * @return int
   */
  private int getLastDomain(String str, int startIndex)
  {
    int index = startIndex;
    int newIndex = 0;

    if (!str.startsWith("$Domain=", index))
      return startIndex;
    index += "$Domain=".length();
    newIndex = getLastValue(str, index);
    if (index == newIndex)
      return startIndex;

    return newIndex;
  }

  /**
   * Method getLastPort.
   * @param str
   * @param startIndex
   * @return int
   */
  private int getLastPort(String str, int startIndex)
  {
    int index = startIndex;
    int newIndex = 0;

    if (!str.startsWith("$Port", index))
      return startIndex;

    index += "$Port".length();
    if (str.charAt(index) != '=')
      return index;

    index++;
    if (str.charAt(index) != '\"')
      return startIndex;

    index++;
    newIndex = getLastPortList(str, index);
    if (index == newIndex)
      return startIndex;

    index = newIndex;
    if (str.charAt(index) != '\"')
      return startIndex;
    index++;
    return index;
  }

  /**
   * Method skipSpaces.
   * @param str
   * @param index
   * @return int
   */
  private int skipSpaces(String str, int index)
  {
    for (; index < str.length(); index++)
      if (!BasicRules.isSP(str.charAt(index)))
        break;
    return index;
  }

  /**
   * Method getValue.
   * @param str
   * @param beg
   * @param end
   * @return String
   */
  private String getValue(String str, int beg, int end)
  {
    String qqq = null;
    if (str.charAt(beg) == '\"')
      qqq = str.substring(beg + 1, end - 1);
    else
      qqq = str.substring(beg, end);
    return qqq;
  }

}
