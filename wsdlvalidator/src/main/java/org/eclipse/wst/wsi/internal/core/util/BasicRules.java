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

/**
 * This class validates basic constructs of RFC 2616.
 * 
 * @author Volodin
 */
public class BasicRules
{

  public static final char CR = 13;
  public static final char LF = 10;
  public static final char SP = 32;
  public static final char HT = 9;
  public static final char DQ = 34;
  public static final String CRLF = "" + CR + LF;

  public static final char[] SEPARATORS =
    {
      '(',
      ')',
      '<',
      '>',
      '@',
      ',',
      ';',
      ':',
      '\\',
      '\"',
      '/',
      '[',
      ']',
      '?',
      '=',
      '{',
      '}',
      SP,
      HT };

  /**
   * Checking rule.
   * 		OCTET = &lt;any 8-bit sequence of data&gt;
   * @param ch character.
   * @return boolean true if ch is conform to rule, false otherwise.
   */
  public static boolean isOCTET(char ch)
  {
    return true;
  }

  /**
   * Checking rule:
   * 		CHAR = &lt;any US-ASCII character (octets 0 - 127)&gt;.
   * @param ch character.
   * @return boolean true if ch is conform to rule, false otherwise.
   */
  public static boolean isCHAR(char ch)
  {
    return (ch >= 0 && ch <= 127) ? true : false;
  }

  /**
   * Checking rule:
   * 		UPALPHA = &lt;any US-ASCII uppercase letter "A".."Z"&gt;.
   * @param ch character.
   * @return boolean true if ch is conform to rule, false otherwise.
   */
  public static boolean isUPALPHA(char ch)
  {
    return (ch >= 'A' && ch <= 'Z') ? true : false;
  }

  /**
   * Checking rule:
   * 		LOALPHA = &lt;any US-ASCII lowercase letter "a".."z"&gt;.
   * @param ch character.
   * @return boolean true if ch is conform to rule, false otherwise.
   */
  public static boolean isLOALPHA(char ch)
  {
    return (ch >= 'a' && ch <= 'z') ? true : false;
  }

  /**
   * Checking rule:
   * 		ALPHA = UPALPHA | LOALPHA.
   * @param ch character.
   * @return boolean true if ch is conform to rule, false otherwise.
   */
  public static boolean isALPHA(char ch)
  {
    return (isLOALPHA(ch) || isUPALPHA(ch)) ? true : false;
  }

  /**
   * Checking rule:
   * 		DIGIT = &lt;any US-ASCII digit "0".."9"&gt;.
   * @param ch character.
   * @return boolean true if ch is conform to rule, false otherwise.
   */
  public static boolean isDIGIT(char ch)
  {
    return (ch >= '0' && ch <= '9') ? true : false;
  }

  /**
   * Checking rule:
   * 		CTL = &lt;any US-ASCII control character (octets 0 - 31) and DEL (127)&gt;.
   * @param ch character.
   * @return boolean true if ch is conform to rule, false otherwise.
   */
  public static boolean isCTL(char ch)
  {
    return ((ch >= 0 && ch <= 31) || ch == 127) ? true : false;
  }

  /**
   * Checking rule:
   * 		CR = &lt;US-ASCII CR, carriage return (13)&gt;.
   * @param ch character.
   * @return boolean true if ch is conform to rule, false otherwise.
   */
  public static boolean isCR(char ch)
  {
    return (ch == CR) ? true : false;
  }

  /**
   * Checking rule:
   * 		LF = &lt;US-ASCII LF, linefeed (10)&gt;.
   * @param ch character
   * @return boolean true if ch is conform to rule, false otherwise
   */
  public static boolean isLF(char ch)
  {
    return (ch == LF) ? true : false;
  }

  /**
   * Checking rule:
   * 		SP = &lt;US-ASCII SP, space (32)&gt;.
   * @param ch character.
   * @return boolean true if ch is conform to rule, false otherwise.
   */
  public static boolean isSP(char ch)
  {
    return (ch == SP) ? true : false;
  }

  /**
   * Checking rule:
   * 		HT = &lt;US-ASCII HT, horizontal-tab (9)&gt;.
   * @param ch character
   * @return boolean true if ch is conform to rule, false otherwise
   */
  public static boolean isHT(char ch)
  {
    return (ch == HT) ? true : false;
  }

  /**
   * Checking rule:
   * 		&lt;"&gt; = &lt;US-ASCII double-quote mark (34)&gt;.
   * @param ch character.
   * @return boolean true if ch is conform to rule, false otherwise.
   */
  public static boolean isDoubleQuote(char ch)
  {
    return (ch == DQ) ? true : false;
  }

  /**
   * Checking rule:
   * 		CRLF = CR LF.
   * @param str string.
   * @return boolean true if ch is conform to rule, false otherwise.
   */
  public static boolean isCRLF(String str)
  {
    return CRLF.equals(str);
  }

  /**
   * Checking rule:
   * 		LWS = [CRLF] 1*( SP | HT ).
   * @param str string.
   * @return boolean true if str is conform to rule, false otherwise.
   */
  public static boolean isLWS(String str)
  {

    int index = getLastIndexLWS(str, 0);
    if (index == -1 || index != str.length())
      return false;

    return true;
  }

  /**
   * Gets last index of the LWS string.
   * @param str string.
   * @param startIndex start index.
   * @return int an index of the first symbol which isn't LWS.
   */
  public static int getLastIndexLWS(String str, int startIndex)
  {
    int index = str.indexOf(CRLF, startIndex);
    if (index == -1)
      index = startIndex;
    else if (index == startIndex)
      index += CRLF.length();
    else
      return -1;

    if (!isSP(str.charAt(index)) && isHT(str.charAt(index)))
      return -1;

    index++;
    for (; index < str.length(); index++)
      if (!isSP(str.charAt(index)) && isHT(str.charAt(index)))
        return index;
    return index;
  }

  /**
   * Gets last index of the TEXT string.
   * TEXT = &lt;any OCTET except CTLs, but including LWS&gt;
   * @param str string.
   * @param startIndex start index.
   * @return int an index of the first symbol which isn't TEXT.
   */
  public static int getLastTEXT(String str, int startIndex)
  {
    int index;
    for (index = startIndex; index < str.length(); index++)
    {
      if (!isOCTET(str.charAt(index)))
        return index;
      if (isCTL(str.charAt(index)))
      {
        int lastLWS = getLastIndexLWS(str, index);
        if (lastLWS == -1)
          return index;
        index = lastLWS - 1;
      }
    }
    return index;
  }

  /**
   * Checking rule:
   * 		HEX = "A" | "B" | "C" | "D" | "E" | "F" | "a" | "b" | "c" | "d" | "e" | "f" | DIGIT.
   * @param ch character.
   * @return boolean true if ch is conform to rule, false otherwise.
   */
  public static boolean isHEX(char ch)
  {
    return (
      (ch >= 'A' && ch <= 'F') || (ch >= 'a' && ch <= 'f') || isDIGIT(ch))
      ? true
      : false;
  }

  /**
   * Checking rule:
   * 		token = 1*&lt;any CHAR except CTLs or separators&gt;.
   * @param str string.
   * @return boolean true if str is conform to rule, false otherwise.
   */
  public static boolean isToken(String str)
  {
    if (str.length() == 0)
      return false;

    for (int index = 0; index < str.length(); index++)
    {
      char ch = str.charAt(index);
      if (!isCHAR(ch) || isSEPARATOR(ch) || isCTL(ch))
        return false;

    }

    return true;
  }

  /**
   * Gets last index of the "token" string.
   * @param str string.
   * @param startIndex start index.
   * @return int an index of the first symbol which isn't "token".
   */
  public static int getLastToken(String str, int startIndex)
  {
    int index = startIndex;
    for (; index < str.length(); index++)
    {
      char ch = str.charAt(index);
      if (!isCHAR(ch) || isSEPARATOR(ch) || isCTL(ch))
        return index;

    }
    return index;
  }

  /**
   * Checking rule:
   * 		separators = "(" | ")" | "<" | ">" | "@"
     *                 | "," | ";" | ":" | "\" | <">
     *                 | "/" | "[" | "]" | "?" | "="
     *                 | "{" | "}" | SP | HT.
   * @param ch character.
   * @return boolean true if ch is conform to rule, false otherwise.
   */
  public static boolean isSEPARATOR(char ch)
  {
    for (int index = 0; index < SEPARATORS.length; index++)
    {
      if (ch == SEPARATORS[index])
        return true;
    }
    return false;
  }

  /**
   * Gets last index of the "comment" string.
   * 		comment = "(" *( ctext | quoted-pair | comment ) ")".
   * @param str string.
   * @param startIndex start index.
   * @return int an index of the first symbol which isn't "comment".
   */
  public static int getLastComment(String str, int startIndex)
  {

    int index = startIndex;
    if (str.length() <= startIndex)
      return startIndex;
    if (str.charAt(index) != '(')
      return startIndex;

    boolean bExit = false;
    boolean bQuotedPair = false;
    int idx = startIndex + 1;
    while (bExit == false)
    {
      while (bQuotedPair == false)
      {
        idx = getLastCtext(str, idx);
        if (idx == str.length())
          return idx;
        if (!isQuotedPair(str, idx - 1))
          bQuotedPair = true;
        else
          idx++;
      }
      if (str.charAt(idx) == '(')
      {
        getLastComment(str, idx);
      }
      else if (str.charAt(idx) == ')')
      {
        return idx + 1;
      }
    }

    return idx;
  }

  /**
   * Gets last index of the "ctext" string.
   * 		ctext = &lt;any TEXT excluding "(" and ")"&gt;
   * @param str string.
   * @param startIndex start index.
   * @return int an index of the first symbol which isn't "ctext".
   */
  private static int getLastCtext(String str, int startIndex)
  {
    int idx = getLastTEXT(str, startIndex);
    int iBracket = startIndex;
    for (; iBracket < idx; iBracket++)
    {
      if (str.charAt(iBracket) == '(' || str.charAt(iBracket) == ')')
        break;
    }
    if (iBracket < idx)
      idx = iBracket;
    return idx;
  }

  /**
   * Gets last index of the "qdtext" string.
   * 		qdtext = &lt;any TEXT except &lt;"&gt;&gt;
   * @param str string.
   * @param startIndex start index.
   * @return int an index of the first symbol which isn't "qdtext".
   */
  private static int getLastQdtext(String str, int startIndex)
  {
    int idx = getLastTEXT(str, startIndex);
    int iBracket = startIndex;
    for (; iBracket < idx; iBracket++)
    {
      if (str.charAt(iBracket) == '\"')
        break;
    }
    if (iBracket < idx)
      idx = iBracket;
    return idx;
  }

  /**
   * Checking rule:
   * 		quoted-pair = "\" CHAR.
   * @param str string.
   * @param startIndex start index.
   * @return boolean true if str is conform to rule, false otherwise.
   */
  public static boolean isQuotedPair(String str, int startIndex)
  {
    if (str.length() >= startIndex + 2
      && str.charAt(startIndex) == '\\'
      && isCHAR(str.charAt(startIndex + 1)))
      return true;
    else
      return false;
  }

  /**
   * Gets last index of the "quoted-string" string.
   * 		quoted-string  = ( &lt;"&gt; *(qdtext | quoted-pair ) &lt;"&gt; )
   * @param str string.
   * @param startIndex start index.
   * @return int an index of the first symbol which isn't "quoted-string".
   */
  public static int getLastQuotedString(String str, int startIndex)
  {
    int index = startIndex;

    // if end if the string
    if (startIndex == str.length())
      return startIndex;
    // if the begin is not '"'
    if (str.charAt(index) != DQ)
      return startIndex;

    index++;
    while (true)
    {
      index = getLastQdtext(str, index);
      if (index == str.length())
        return startIndex;
      if (isQuotedPair(str, index - 1))
      {
        index++;
        if (index == str.length())
          return startIndex;
      }
      else
        break;
    }
    // if the end is not '"'
    if (str.charAt(index) != DQ)
      return startIndex;

    index++;
    return index;
  }
}
