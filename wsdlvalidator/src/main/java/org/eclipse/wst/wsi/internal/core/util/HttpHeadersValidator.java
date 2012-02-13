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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Locale;
import com.ibm.icu.util.StringTokenizer;

import sun.net.www.MessageHeader;

import com.ibm.icu.text.SimpleDateFormat;

/**
 * This class checks HTTP request headers about RFC 2616.
 *
 * @author Volodin 
 */
public class HttpHeadersValidator
{

  private static final String HEADER_ALLOW = "Allow";
  private static final String HEADER_CONTENT_TYPE = "Content-Type";
  private static final String HEADER_CONTENT_ENCODING = "Content-Encoding";
  private static final String HEADER_CONTENT_LANGUAGE = "Content-Language";
  private static final String HEADER_CONTENT_LENGHT = "Content-Length";
  private static final String HEADER_CONTENT_LOCATION = "Content-Location";
  private static final String HEADER_CONTENT_RANGE = "Content-Range";
  private static final String HEADER_EXPIRES = "Expires";
  private static final String HEADER_LAST_MODIFIED = "Last-Modified";
  private static final String HEADER_CACHE_CONTROL = "Cache-Control";
  private static final String HEADER_CONNECTION = "Connection";
  private static final String HEADER_DATE = "Date";
  private static final String HEADER_PRAGMA = "Pragma";
  private static final String HEADER_TRAILER = "Trailer";
  private static final String HEADER_TRANSFER_ENCODING = "Transfer-Encoding";

  private static final String HEADER_UPGRADE = "Upgrade";
  private static final String HEADER_VIA = "Via";
  private static final String HEADER_WARNING = "Warning";

  private static final String HEADER_ACCEPT = "Accept";
  private static final String HEADER_ACCEPT_CHARSET = "Accept-Charset";
  private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
  private static final String HEADER_ACCEPT_LANGUAGE = "Accept-Language";
  private static final String HEADER_AUTHORIZATION = "Authorization";
  private static final String HEADER_EXPECT = "Expect";
  private static final String HEADER_FROM = "From";
  private static final String HEADER_HOST = "Host";
  private static final String HEADER_IF_MATCH = "If-Match";
  private static final String HEADER_IF_MODIFIED_SINCE = "If-Modified-Since";
  private static final String HEADER_IF_NONE_MATCH = "If-None-Match";
  private static final String HEADER_IF_RANGE = "If-Range";
  private static final String HEADER_IF_UNMODIFIED_SINCE =
    "If-Unmodified-Since";
  private static final String HEADER_MAX_FORWARDS = "Max-Forwards";
  private static final String HEADER_PROXY_AUTHORIZATION =
    "Proxy-Authorization";
  private static final String HEADER_RANGE = "Range";
  private static final String HEADER_REFERER = "Referer";
  private static final String HEADER_TE = "TE";
  private static final String HEADER_USER_AGENT = "User-Agent";

  /**
   * This class checks e-mail string about RFC 822.
   */
  public static class MailboxValidator
  {
    private static final char[] SPECIAL =
      { '(', ')', '<', '>', '@', ',', ';', ':', '\\', '\"', '.', '[', ']' };

    /**
    	* Validates e-mail string.
    * @param mailbox e-mail string
    * @return boolean  true if e-mail string is conform to RFC 822, false otherwise.
    */

    public static boolean validateMailbox(String mailbox)
    {

      if (!isFullAddr(mailbox) && !isShortAddr(mailbox))
        return false;

      return true;
    }

    /**
     * Method isSimpleAddr.
     * @param mailbox
     * @return boolean
     */
    private static boolean isShortAddr(String mailbox)
    {
      int indexAt = mailbox.indexOf('@');
      if (indexAt == -1)
        return false;

      if (!isLocalPart(mailbox.substring(0, indexAt))
        || !isDomain(mailbox.substring(indexAt + 1)))
        return false;

      return true;
    }

    /**
     * Method isDomaim.
     * @param string
     * return boolean
     */
    private static boolean isDomain(String string)
    {
      if (string.trim().length() == 0)
        return false;
      StringTokenizer st = new StringTokenizer(string, ".");
      while (st.hasMoreTokens())
      {
        String subDomain = st.nextToken();
        if (!isAtom(subDomain) && !isDomainLiteral(subDomain))
          return false;

      }
      return true;
    }

    /**
     * Method isDomainLiteral.
     * @param subDomain
     * @return boolean
     */
    private static boolean isDomainLiteral(String subDomain)
    {
      if (getLastDomainLiteral(subDomain, 0) == subDomain.length())
      {
        return true;
      }
      else
      {
        return false;
      }
    }

    private static int getLastDomainLiteral(String str, int startIndex)
    {

      int index = startIndex;
      //int newIndex = 0;

      // if end if the string
      if (startIndex == str.length())
        return startIndex;
      // if the begin is not '['
      if (str.charAt(index) != '[')
        return startIndex;

      index++;
      while (true)
      {
        index = getLastDtext(str, index);
        if (index == str.length())
          return startIndex;
        if (BasicRules.isQuotedPair(str, index - 1))
        {
          index++;
          if (index == str.length())
            return startIndex;
        }
        else
          break;
      }
      // if the end is not ']'
      if (str.charAt(index) != ']')
        return startIndex;

      index++;
      return index;
    }

    private static int getLastDtext(String str, int startIndex)
    {
      if (str.length() == startIndex)
        return startIndex;
      int i = 0;
      for (; i < str.length(); i++)
      {
        char ch = str.charAt(i);
        i = BasicRules.getLastIndexLWS(str, i);
        if (!BasicRules.isCHAR(ch)
          || str.charAt(i) == '['
          || str.charAt(i) == ']'
          || str.charAt(i) == '\\'
          || str.charAt(i) == BasicRules.CR)
        {
          return i + 1;
        }
      }
      return i + 1;
    }

    /**
     * Method isLocalPart.
     * @param string
     * @return boolean
     */
    private static boolean isLocalPart(String string)
    {
      if (string.trim().length() == 0)
        return false;
      StringTokenizer st = new StringTokenizer(string, ".");
      while (st.hasMoreTokens())
      {
        if (!isWord(st.nextToken()))
          return false;
      }
      return true;
    }

    /**
     * Method isWord.
     * @param string
     * @return boolean
     */
    private static boolean isWord(String string)
    {
      if (!isAtom(string) && !isQuotedString(string))
        return false;
      return true;
    }

    /**
     * Method isAtom.
     * @param string
     * @return boolean
     */
    private static boolean isAtom(String string)
    {
      if (string.length() == 0)
        return false;
      for (int i = 0; i < string.length(); i++)
      {
        if (!BasicRules.isCHAR(string.charAt(i))
          || isSpecial(string.charAt(i))
          || string.charAt(i) == ' '
          || BasicRules.isCTL(string.charAt(i)))
        {
          return false;
        }
      }
      return true;
    }

    /**
     * Method isSpecial.
     * @param c
     * @return boolean
     */
    private static boolean isSpecial(char ch)
    {
      for (int index = 0; index < SPECIAL.length; index++)
      {
        if (ch == SPECIAL[index])
          return true;
      }
      return false;
    }

    /**
     * Method isFullAddr.
     * @param mailbox
     * @return boolean
     */
    private static boolean isFullAddr(String mailbox)
    {
      if (mailbox.length() == 0)
        return false;
      int idxLT = mailbox.indexOf('<');
      if (idxLT == -1)
        return false;

      //is phrase
      String phrase = mailbox.substring(0, idxLT);
      StringTokenizer st = new StringTokenizer(phrase, " ");
      if (st.countTokens() == 0)
        return false;
      while (st.hasMoreTokens())
      {
        if (!isWord(st.nextToken()))
          return false;
      }
      if (phrase.charAt(phrase.length() - 1) != BasicRules.SP)
      {
        return false;
      }

      //is route-addr
      String routeAddr = mailbox.substring(idxLT + 1);
      // is route
      int idxTwoSpot = routeAddr.indexOf(':');
      if (idxTwoSpot != -1)
      {
        StringTokenizer stRouteAddr =
          new StringTokenizer(routeAddr.substring(0, idxTwoSpot), ",");
        if (!stRouteAddr.hasMoreTokens())
          return false;
        while (stRouteAddr.hasMoreTokens())
        {
          if (!isDomain(stRouteAddr.nextToken()))
            return false;
        }
      }

      //is addr spec
      int idxGT = routeAddr.indexOf('>');
      if (idxGT == -1 || idxGT != (routeAddr.length() - 1))
        return false;

      if (!isShortAddr(routeAddr.substring(idxTwoSpot + 1, idxGT)))
        return false;

      return true;
    }

  }

  /**
   * Validates HTTP request headers.
   * @param headers HTTP request headers
   * @return boolean  true if all HTTP headers string is conform to RFC 2616, false otherwise.
   */

  public static boolean validateHttpRequestHeaders(String headers)
  {

    MessageHeader mh = new MessageHeader();
    try
    {
      mh.parseHeader(new ByteArrayInputStream(headers.getBytes()));
    }
    catch (IOException e)
    {
      return false;
    }

    String header = null;
    String value = null;

    header = mh.getKey(0);
    if (header != null)
      return false;

    value = mh.getValue(0);
    if (value == null)
      return false;

    //method
    StringTokenizer st = new StringTokenizer(value, " ");
    if (!st.hasMoreElements())
      return false;
    String str = st.nextToken();
    if (!isToken(str))
      return false;

    if (!st.hasMoreElements())
      return false;
    str = st.nextToken();
    if (!isURI(str) && !str.equals("*"))
      return false;

    if (!st.hasMoreElements())
      return false;
    str = st.nextToken();
    if (!isHTTPVersion(str))
      return false;

    int i = 1;
    try
    {
      while ((header = mh.getKey(i)) != null)
      {
        value = mh.getValue(i);
        i++;

        // is message-header token
        if (!isToken(header))
          return false;

        //---- entity-headers

        if (header.equals(HEADER_ALLOW))
        {
          if (!isValidAllow(value))
            return false;
          else
            continue;
        }

        if (header.equals(HEADER_CONTENT_TYPE))
        {
          if (!isMediaType(value))
            return false;
          else
            continue;
        }
        if (header.equals(HEADER_CONTENT_ENCODING))
        {
          if (!isToken(value))
            return false;
          else
            continue;
        }
        if (header.equals(HEADER_CONTENT_LANGUAGE))
        {
          if (!isLanguageTag(value))
            return false;
          else
            continue;
        }
        if (header.equals(HEADER_CONTENT_LENGHT))
        {
          if (!isDidgit(value))
            return false;
          else
            continue;
        }
        if (header.equals(HEADER_CONTENT_LOCATION))
        {
          if (!isURI(value))
            return false;
          else
            continue;
        }

        if (header.equals(HEADER_CONTENT_RANGE))
        {
          if (!isValidContentRange(value))
            return false;
          else
            continue;
        }

        if (header.equals(HEADER_EXPIRES)
          || header.equals(HEADER_LAST_MODIFIED))
        {
          if (!isHTTPDate(value))
            return false;
          else
            continue;
        }

        //---- general-headers
        if (header.equals(HEADER_CACHE_CONTROL))
        {
          if (!isValidCacheControl(value))
            return false;

          continue;
          //return true;
        }

        if (header.equals(HEADER_CONNECTION))
        {
          if (!isToken(value))
            return false;
          else
            continue;
        }

        if (header.equals(HEADER_DATE))
        {
          if (!isHTTPDate(value))
            return false;
          else
            continue;
        }

        if (header.equals(HEADER_PRAGMA))
        {
          if (!isPragmaDerective(value))
            return false;
          else
            continue;
        }

        if (header.equals(HEADER_TRAILER))
        {
          if (!isToken(value))
            return false;
          else
            continue;
        }

        if (header.equals(HEADER_TRANSFER_ENCODING))
        {
          if (!isTransferCoding(value))
            return false;
          else
            continue;
        }

        if (header.equals(HEADER_UPGRADE))
        {
          if (!isValidUpgrade(value))
            return false;
          else
            continue;
        }

        if (header.equals(HEADER_VIA))
        {
          if (!isValidVia(value))
            return false;
          else
            continue;
        }

        if (header.equals(HEADER_WARNING))
        {
          if (!isValidWarning(value))
            return false;
          else
            continue;
        }

        if (header.equals(HEADER_ACCEPT))
        {
          if (!isValidAccept(value))
            return false;
          else
            continue;
        }

        if (header.equals(HEADER_ACCEPT_CHARSET))
        {
          if (!isValidAcceptCharSet(value))
            return false;
          else
            continue;
        }

        if (header.equals(HEADER_ACCEPT_ENCODING))
        {
          if (!isValidAcceptEncoding(value))
            return false;
          else
            continue;
        }

        if (header.equals(HEADER_ACCEPT_LANGUAGE))
        {
          if (!isValidAcceptLanguage(value))
            return false;
          else
            continue;
        }

        if (header.equals(HEADER_AUTHORIZATION)
          || header.equals(HEADER_PROXY_AUTHORIZATION))
        {
          if (!isCredentials(value))
            return false;
          else
            continue;
        }
        if (header.equals(HEADER_EXPECT))
        {
          if (!isExpectation(value))
            return false;
          else
            continue;
        }

        if (header.equals(HEADER_FROM))
        {
          if (!MailboxValidator.validateMailbox(value))
            return false;
          else
            continue;
        }

        if (header.equals(HEADER_IF_MATCH)
          || header.equals(HEADER_IF_NONE_MATCH))
        {
          if (!isValidIfMatch(value))
            return false;
          else
            continue;
        }
        if (header.equals(HEADER_IF_RANGE))
        {
          if (!isEntityTag(value) && !isHTTPDate(value))
            return false;
          else
            continue;
        }

        //---			
        if (header.equals(HEADER_IF_MODIFIED_SINCE))
        {
          if (!isHTTPDate(value))
            return false;
          else
            continue;
        }
        if (header.equals(HEADER_IF_UNMODIFIED_SINCE))
        {
          if (!isHTTPDate(value))
            return false;
          else
            continue;
        }
        if (header.equals(HEADER_MAX_FORWARDS))
        {
          if (!isDidgit(value))
            return false;
          else
            continue;
        }

        if (header.equals(HEADER_USER_AGENT))
        {
          if (!isValidUserAgent(value))
            return false;
          else
            continue;
        }

        if (header.equals(HEADER_TE))
        {
          if (!isValidTE(value))
            return false;
          else
            continue;
        }

        if (header.equals(HEADER_RANGE))
        {
          if (!isRange(value))
            return false;
          else
            continue;
        }
        if (header.equals(HEADER_HOST))
        {
          if (!isHost(value))
            return false;
          else
            continue;
        }
        if (header.equals(HEADER_REFERER))
        {
          if (!isURI(value))
            return false;
          else
            continue;
        }

      }
    }
    catch (Exception e)
    {
      return false;
    }
    return true;
  }

  /**
   * Method isValidIfMatch.
   * @param value
   * @return boolean
   */
  private static boolean isValidIfMatch(String value)
  {
    if (value.trim().length() == 0)
      return true;
    if ("*".equals(value.trim()))
      return true;

    StringTokenizer st = new StringTokenizer(value, ",");
    while (st.hasMoreElements())
    {
      String str = st.nextToken().trim();
      if (!isEntityTag(str))
        return false;

    }

    return true;
  }

  /**
   * Method isValidUpgrade.
   * @param value
   * @return boolean
   */
  private static boolean isValidUpgrade(String value)
  {
    if (value.trim().length() == 0)
      return false;
    StringTokenizer st = new StringTokenizer(value, ",");
    while (st.hasMoreElements())
    {
      String str = st.nextToken().trim();
      if (!isProduct(str))
        return false;

    }
    return true;
  }

  /**
   * Method isValidCacheControl.
   * @param value
   * @return boolean
   */
  private static boolean isValidCacheControl(String value)
  {

    if (value.trim().length() == 0)
      return false;

    StringTokenizer st = new StringTokenizer(value, ",");
    while (st.hasMoreElements())
    {
      String str = st.nextToken().trim();
      int index = str.indexOf('=');
      if (index == -1)
      {

      }
      else
      {
        if (!isToken(str.substring(0, index)))
          return false;
        String strAfterEq = str.substring(index + 1);
        if (!isToken(strAfterEq) && !isQuotedString(strAfterEq))
        {
          return false;
        }
      }
    }

    return true;
  }

  /**
   * Method isHTTPVersion.
   * @param str
   * @return boolean
   */
  private static boolean isHTTPVersion(String str)
  {
    if (!str.startsWith("HTTP/"))
      return false;
    int idx = "HTTP/".length();

    int idx2 = str.indexOf(".");

    // 1*DIGIT
    String s = str.substring(idx, idx2);
    if (!isDidgit(s))
      return false;

    s = str.substring(idx2 + 1);
    if (!isDidgit(s))
      return false;

    return true;
  }

  /**
   * Method isValidWarning.
   * @param value
   * @return boolean
   */
  private static boolean isValidWarning(String value)
  {
    if (value.length() == 0)
      return false;
    value = value.trim();
    StringTokenizer st = new StringTokenizer(value, " ");
    String str = st.nextToken();

    if (str.length() > 3 || !isDidgit(str))
      return false;

    if (!st.hasMoreTokens())
      return false;
    str = st.nextToken();
    if (!isHost(str) && !isToken(str))
      return false;

    //if(!st.hasMoreTokens()) return false;
    str = st.nextToken("").trim();
    //???

    int lastQuotedString = BasicRules.getLastQuotedString(str, 0);
    if (lastQuotedString == str.length())
    {
      return true;
    }
    else
    {
      String data = str.substring(lastQuotedString);
      if (data.charAt(data.length()) != '\"')
        return false;
      if (str.charAt(0) != '\"')
        return false;
      if (!isHTTPDate(str.substring(1, data.length() - 1)))
        return false;
    }

    return true;
  }

  /**
   * Method isValidVia.
   * @param value
   * @return boolean
   */
  private static boolean isValidVia(String value)
  {
    if (value.trim().length() == 0)
      return false;

    StringTokenizer st = new StringTokenizer(value, ",");
    while (st.hasMoreElements())
    {

      String str = st.nextToken().trim();

      StringTokenizer st2 = new StringTokenizer(str, " ");

      // protocol/version
      str = st2.nextToken();
      int idx = str.indexOf("/");
      if (idx == -1)
      {
        if (!isToken(str))
          return false;
      }
      else
      {
        if (!isToken(str.substring(0, idx))
          || !isToken(str.substring(idx + 1)))
          return false;
      }

      //host
      str = st2.nextToken();
      if (!isHost(str) && !isToken(str))
        return false;

      //comment
      if (st2.hasMoreTokens())
      {
        str = st2.nextToken("");
        if (!isComment(str.trim()))
          return false;
      }
    }
    return true;
  }

  /**
   * Method isHost.
   * @param value
   * @return boolean
   */
  private static boolean isHost(String value)
  {

    try
    {
      new URL("http://" + value);
    }
    catch (MalformedURLException e)
    {
      return false;
    }
    return true;
  }

  /**
   * Method isValidAllow.
   * @param value
   * @return boolean
   */
  private static boolean isValidAllow(String value)
  {
    if (value.trim().length() == 0)
      return true;

    StringTokenizer st = new StringTokenizer(value, ",");
    while (st.hasMoreElements())
    {
      String str = st.nextToken().trim();
      if (!isToken(str))
        return false;
    }
    return true;
  }

  /**
   * Method isValidContentRange.
   * @param value
   * @return boolean
   */
  private static boolean isValidContentRange(String value)
  {
    if (value.length() == 0)
      return false;
    if (!value.startsWith("bytes"))
      return false;
    String str = value.substring("bytes".length()).trim();

    int idx = str.indexOf("/");
    if (idx == -1)
      return false;

    String byteRange = str.substring(0, idx);
    int idx2 = byteRange.indexOf("-");
    if (idx2 == -1)
    {
      if (!byteRange.equals("*"))
        return false;
    }
    else
    {
      if (!isDidgit(byteRange.substring(0, idx2))
        || !isDidgit(byteRange.substring(idx2 + 1)))
        return false;
    }

    if (!isDidgit(str.substring(idx + 1))
      && !str.substring(idx + 1).equals("*"))
      return false;

    return true;
  }

  /**
   * Method isRange.
   * @param value
   * @return boolean
   */
  private static boolean isRange(String value)
  {
    if (value.length() == 0)
      return false;
    if (!value.startsWith("bytes="))
      return false;
    String strByteRange = value.substring("bytes=".length());

    StringTokenizer st = new StringTokenizer(strByteRange, ",");
    while (st.hasMoreElements())
    {
      String str = st.nextToken();
      int idx = str.indexOf("-");
      if (idx == -1)
        return false;
      if (idx == 0)
      {
        if (!isDidgit(str.substring(1)))
          return false;
      }
      else
      {
        if (idx == (str.length() - 1))
        {
          if (!isDidgit(str.substring(0, str.length() - 1)))
            return false;
        }
        else
        {
          if (!isDidgit(str.substring(0, idx))
            || !isDidgit(str.substring(idx + 1)))
            return false;
        }

      }

    }
    return true;
  }

  /**
   * Method isValidTE.
   * @param value
   * @return boolean
   */
  private static boolean isValidTE(String value)
  {
    if (value.trim().length() == 0)
      return true;

    StringTokenizer st = new StringTokenizer(value, ",");
    while (st.hasMoreElements())
    {
      String str = st.nextToken().trim();
      if (st.equals("trailers"))
        return true;

      int idx = str.indexOf(";");
      if (idx == -1)
      {
        if (!isLanguageRange(str))
          return false;
      }
      else
      {
        String _1 = str.substring(0, idx).trim();
        String _2 = str.substring(idx + 1).trim();
        if (!isLanguageRange(_1))
          return false;
        if (!isQAndQValue(_2))
          return false;
      }

    }

    return true;
  }

  /**
   * Method isValidUserAgent.
   * @param value
   * @return boolean
   */
  private static boolean isValidUserAgent(String value)
  {
    if (value.length() == 0)
      return false;
    StringTokenizer st = new StringTokenizer(value, " ");
    while (st.hasMoreElements())
    {
      String str = st.nextToken();
      if (!isProduct(str) && !isComment(str))
        return false;

    }

    return true;
  }

  /**
   * Method isComment.
   * @param str
   * @return boolean
   */
  private static boolean isComment(String str)
  {
    if (BasicRules.getLastComment(str, 0) != str.length())
      return false;
    return true;
  }

  /**
   * Method isValidAcceptLanguage.
   * @param value
   * @return boolean
   */
  private static boolean isValidAcceptLanguage(String value)
  {
    if (value.trim().length() == 0)
      return false;

    StringTokenizer st = new StringTokenizer(value, ",");
    while (st.hasMoreElements())
    {
      String str = st.nextToken().trim();
      int idx = str.indexOf(";");
      if (idx == -1)
      {
        if (!isToken(str))
          return false;
      }
      else
      {
        String _1 = str.substring(0, idx).trim();
        String _2 = str.substring(idx + 1).trim();
        if (!isToken(_1))
          return false;
        if (!isAcceptParams(_2))
          return false;
      }

    }

    return true;
  }

  /**
   * Method isLanguageRange.
   * @param str
   * @return boolean
   */
  private static boolean isLanguageRange(String str)
  {
    if (str.trim().equals("*"))
      return true;
    StringTokenizer st = new StringTokenizer(str, "-");
    while (st.hasMoreElements())
    {
      if (!is8ALPHA(st.nextToken()))
        return false;
    }
    return true;
  }

  /**
   * Method isValidAcceptEncoding.
   * @param value
   * @return boolean
   */
  private static boolean isValidAcceptEncoding(String value)
  {
    if (value.trim().length() == 0)
      return false;

    StringTokenizer st = new StringTokenizer(value, ",");
    while (st.hasMoreElements())
    {
      String str = st.nextToken().trim();
      int idx = str.indexOf(";");
      if (idx == -1)
      {
        if (!isToken(str) && !str.equals("*"))
          return false;
      }
      else
      {
        String _1 = str.substring(0, idx).trim();
        String _2 = str.substring(idx + 1).trim();

        if ((!isToken(_1) && !_1.equals("*")))
          return false;
        if (!isQAndQValue(_2))
          return false;
      }

    }

    return true;
  }

  /**
   * Method isValidAcceptCharSet.
   * @param value
   * @return boolean
   */
  private static boolean isValidAcceptCharSet(String value)
  {
    if (value.trim().length() == 0)
      return false;

    StringTokenizer st = new StringTokenizer(value, ",");
    while (st.hasMoreElements())
    {
      String str = st.nextToken().trim();
      int idx = str.indexOf(";");
      if (idx == -1)
      {
        if (!isToken(str) && !str.equals("*"))
          return false;
      }
      else
      {
        String _1 = str.substring(0, idx).trim();
        String _2 = str.substring(idx + 1).trim();

        if ((!isToken(_1) && !_1.equals("*")))
          return false;
        if (!isQAndQValue(_2))
          return false;
      }

    }

    return true;
  }

  /**
   * Method isValidAccept.
   * @param value
   * @return boolean
   */
  private static boolean isValidAccept(String value)
  {
    if (value.trim().length() == 0)
      return true;

    StringTokenizer st = new StringTokenizer(value, ",");
    while (st.hasMoreElements())
    {
      String str = st.nextToken().trim();
      int idx = str.indexOf(";");
      if (idx == -1)
      {
        if (!isMediaRange(str))
          return false;
      }
      else
      {
        if (!isMediaRange(str.substring(0, idx).trim())
          || !isAcceptParams(str.substring(idx + 1).trim()))
          return false;

      }

    }
    return true;
  }

  /**
   * Method isAcceptParams.
   * @param string
   * @return boolean
   */
  private static boolean isAcceptParams(String string)
  {
    if (string.trim().length() == 0)
      return false;

    StringTokenizer st = new StringTokenizer(string, ";");
    String str = st.nextToken();

    int idx = str.indexOf("=");
    if (idx == -1)
    {
      if (str.equals("q") || !isToken(str))
        return false;
    }
    else
    {
      if (str.substring(0, idx).equals("q"))
      {
        if (!isQValue(str.substring(idx + 1)))
          return false;
      }
      else
      {
        if (!isParameterWithoutValue(str))
          return false;
      }
    }

    while (st.hasMoreElements())
    {
      str = st.nextToken();
      if (!isParameterWithoutValue(str))
        return false;
    }
    return true;
  }

  /**
   * Method isQAndQValue.
   * @param str
   * @return boolean
   */
  private static boolean isQAndQValue(String str)
  {
    str = str.trim();
    if (!str.trim().startsWith("q="))
      return false;
    if (!isQValue(str.substring("q=".length())))
      return false;
    return true;
  }

  /**
   * Method isQValue.
   * @param string
   * @return boolean
   */
  private static boolean isQValue(String string)
  {
    if (string.trim().length() == 0)
      return false;

    int idx = string.indexOf(".");
    if (idx == -1)
    {
      if (!"0".equals(string) && !"1".equals(string))
        return false;
    }
    else
    {
      String strDig = string.substring(idx + 1);
      if (strDig.length() > 3)
        return false;

      if (string.substring(0, idx).equals("0"))
      {
        if (!isDidgit(strDig))
          return false;

      }
      else
      {
        if (!string.substring(0, idx).equals("1"))
          return false;

        for (int i = 0; i < strDig.length(); i++)
        {
          if (strDig.charAt(i) != '0')
            return false;
        }
      }

    }
    return true;
  }

  /**
   * Method isMediaRange.
   * @param str
   * @return boolean
   */
  private static boolean isMediaRange(String str)
  {
    if (str.trim().length() == 0)
      return false;

    int idx = str.indexOf("/");
    if (idx == -1)
      return false;

    if (!isToken(str.substring(0, idx)) && !str.substring(0, idx).equals("*"))
      return false;
    if (!isToken(str.substring(idx + 1))
      && !str.substring(idx + 1).equals("*"))
      return false;

    return true;
  }

  /**
   * Method isEntityTag.
   * @param value
   * @return boolean
   */
  private static boolean isEntityTag(String value)
  {

    int idx = 0;
    if (value.startsWith("W/"))
      idx = 2;
    if (!isQuotedString(value.substring(idx)))
      return false;
    return true;
  }

  /**
   * Method isExpectation.
   * @param value
   * @return boolean
   */
  private static boolean isExpectation(String value)
  {
    if (value.equals("100-continue"))
      return true;

    StringTokenizer st = new StringTokenizer(value, ";");
    while (st.hasMoreElements())
    {
      if (!isParameterWithoutValue(st.nextToken()))
        return false;
    }

    return true;
  }

  /**
   * Method isCredentials.
   * @param value
   * @return boolean
   */
  private static boolean isCredentials(String value)
  {
    StringTokenizer st = new StringTokenizer(value, " ");
    if (!isToken(st.nextToken()))
      return false;

    while (st.hasMoreElements())
    {
      String param = st.nextToken(",");
      if (!isParameter(param))
        return false;
    }
    return true;
  }

  /**
   * Method isProduct.
   * @param value
   * @return boolean
   */
  private static boolean isProduct(String value)
  {
    int idx = value.indexOf("/");
    if (idx == -1)
    {
      if (!isToken(value))
        return false;
    }
    else
    {
      if (!isToken(value.substring(0, idx))
        || !isToken(value.substring(idx + 1)))
        return false;
    }
    return true;
  }

  /**
   * Method isTransferCoding.
   * @param value
   * @return boolean
   */
  private static boolean isTransferCoding(String value)
  {
    if (value.equals("chunked"))
    {
      return true;
    }
    else
    {
      StringTokenizer st = new StringTokenizer(value, ";");
      if (!isToken(st.nextToken()))
        return false;

      while (st.hasMoreElements())
      {
        if (!isParameter(st.nextToken()))
          return false;
      }

    }

    return true;
  }

  /**
   * Method isParameter.
   * @param string
   * @return boolean
   */
  private static boolean isParameter(String string)
  {

    // check parameter	
    int idx = string.indexOf("=");
    if (!isToken(string.substring(0, idx)))
      return false;

    String parValue = string.substring(idx + 1);
    if (!isToken(parValue) && !isQuotedString(parValue))
      return false;

    return true;
  }

  /**
   * Method isParameterWithoutValue.
   * @param string
   * @return boolean
   */
  private static boolean isParameterWithoutValue(String string)
  {

    // check parameter	
    int idx = string.indexOf("=");
    if (idx != -1)
    {
      if (!isToken(string.substring(0, idx)))
        return false;
      String parValue = string.substring(idx + 1);
      if (!isToken(parValue) && !isQuotedString(parValue))
        return false;

    }
    else
    {
      if (!isToken(string))
        return false;
    }

    return true;
  }

  /**
   * Method isPragmaDerective.
   * @param value
   * @return boolean
   */
  private static boolean isPragmaDerective(String value)
  {
    if (value.equals("no-cache"))
      return true;
    else
    {
      int idx = value.indexOf("=");
      if (idx == -1)
      {
        if (isToken(value))
          return true;
      }
      else
      {
        String str = value.substring(idx + 1);
        if (isToken(value.substring(0, idx))
          && (isToken(str) || isQuotedString(str)))
          return true;
      }
    }
    return true;
  }

  /**
   * Method isHTTPDate.
   * @param value
   * @return boolean
   */
  private static boolean isHTTPDate(String value)
  {

    String rfc1123_date = "EEE, dd MMM yyyy hh:mm:ss 'GMT'";
    String rfc850_date = "EEEE, dd-MMM-yy hh:mm:ss 'GMT'";
    String asctime_date = "EEE MMM d hh:mm:ss yyyy";

    try
    {
      SimpleDateFormat sdf = new SimpleDateFormat(rfc1123_date, Locale.US);
      if (sdf.parse(value) != null)
        return true;
    }
    catch (ParseException e)
    {
    }

    try
    {
      SimpleDateFormat sdf = new SimpleDateFormat(rfc850_date, Locale.US);
      if (sdf.parse(value) != null)
        return true;
    }
    catch (ParseException e)
    {
    }

    try
    {
      SimpleDateFormat sdf = new SimpleDateFormat(asctime_date, Locale.US);
      if (sdf.parse(value) != null)
        return true;
    }
    catch (ParseException e)
    {
    }

    return false;

  }

  /**
   * Method isURI.
   * @param value
   * @return boolean
   */
  private static boolean isURI(String value)
  {
    try
    {
      new URL(value);
    }
    catch (MalformedURLException e)
    {
      try
      {
        new URL("http://localhost" + value);
      }
      catch (MalformedURLException e2)
      {
        return false;
      }
    }

    return true;
  }

  /**
   * Method isLanguageTag.
   * @param value
   * @return boolean
   */
  private static boolean isLanguageTag(String value)
  {
    int idx = value.indexOf("-");
    if (idx == -1)
    {
      return is8ALPHA(value);
    }
    else
    {
      if (!is8ALPHA(value.substring(0, idx))
        || !is8ALPHA(value.substring(idx + 1)))
        return false;
      else
        return true;

    }
  }

  /**
   * Method is8ALPHA.
   * @param string
   * @return boolean
   */
  private static boolean is8ALPHA(String string)
  {
    if (string.length() > 8 || !isALPHA(string))
      return false;
    else
      return true;
  }

  /**
   * Method isALPHA.
   * @param string
   * @return boolean
   */
  private static boolean isALPHA(String string)
  {
    for (int i = 0; i < string.length(); i++)
    {
      if (!BasicRules.isCHAR(string.charAt(i)))
        return false;
    }

    return true;
  }

  /**
   * Method isDidgit.
   * @param value
   * @return boolean
   */
  private static boolean isDidgit(String value)
  {
    if (value.length() == 0)
      return false;
    char[] chs = value.toCharArray();
    for (int i = 0; i < chs.length; i++)
    {
      if (chs[i] < '0' || chs[i] > '9')
        return false;
    }

    return true;
  }

  /**
   * Method isMediaType.
   * @param value
   * @return boolean
   */
  private static boolean isMediaType(String value)
  {
    StringTokenizer st = new StringTokenizer(value, ";");
    String mediaType = st.nextToken();

    int idx = mediaType.indexOf("/");
    if (!isToken(mediaType.substring(0, idx))
      || !isToken(mediaType.substring(idx + 1)))
    {
      return false;
    }

    while (st.hasMoreElements())
    {
      if (!isParameter(st.nextToken(";").trim()))
        return false;
    }
    return true;
  }

  /**
   * Method isQuotedString.
   * @param parValue
   * @return boolean
   */
  private static boolean isQuotedString(String parValue)
  {
    if (BasicRules.getLastQuotedString(parValue, 0) != parValue.length())
    {
      return false;
    }
    return true;
  }

  /**
   * Method isToken.
   * @param value
   * @return boolean
   */
  private static boolean isToken(String value)
  {
    return BasicRules.isToken(value);
  }
}
