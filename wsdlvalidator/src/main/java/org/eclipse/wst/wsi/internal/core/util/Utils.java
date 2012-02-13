/*******************************************************************************
 *
 * Copyright (c) 2002, 2008 IBM Corporation, Beacon Information Technology Inc. and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM      - Initial API and implementation
 *   BeaconIT - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.core.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.wst.wsi.internal.WSITestToolsPlugin;
import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.analyzer.config.AnalyzerConfig;
import org.eclipse.wst.wsi.internal.core.log.MimePart;
import org.eclipse.wst.wsi.internal.core.log.MimeParts;
import org.eclipse.wst.wsi.internal.core.log.impl.MimePartImpl;
import org.eclipse.wst.wsi.internal.core.log.impl.MimePartsImpl;
import org.eclipse.wst.wsi.internal.core.profile.ProfileAssertions;
import org.eclipse.wst.wsi.internal.core.report.Reporter;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.StringTokenizer;
import com.ibm.icu.util.TimeZone;

/**
 * General set of utilities.
 */
public final class Utils
{
  public static final byte CR = (byte) '\r';
  public static final byte LF = (byte) '\n';

  
  private static Map validProfileTADVersions;

  /**
   * Common timestamp format.
   */
  //	public static final SimpleDateFormat timestampFormat = new SimpleDateFormat ("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
  public static final SimpleDateFormat timestampFormat =
    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

  /**
   * Basic date format.
   */
  public static final SimpleDateFormat dateFormat =
    new SimpleDateFormat("yyyy-MM-dd");

  /**
   * Wrapper method for error logging;
   * for now it just goes to stderr.
   * @param inError  an error message.
   */
  public final static void logError(String inError)
  {
    System.err.println("Error: " + inError);
  }

  /** 
   * Get exception information as a string.
   * @param throwable  a Throwable object.
   * @return exception information as a string.
   */
  public final static String getExceptionDetails(Throwable throwable)
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    pw.println("Exception: ");
    throwable.printStackTrace(pw);

    return sw.toString();
  }

  /**
   * Get current date and time as a timestamp.
   * @return urrent date and time as a timestamp.
   */
  public static String getTimestamp()
  {
    // Use GMT timezone
    //timestampFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    timestampFormat.setTimeZone(TimeZone.getDefault());

    // Return timestamp
    return timestampFormat.format(new Date());
  }

  /**
   * Get current date for default time zone.
   * @return current date for default time zone.
   */
  public static String getDate()
  {
    // Use GMT timezone
    dateFormat.setTimeZone(TimeZone.getDefault());

    // Return date
    return dateFormat.format(new Date());
  }

  /**
   * Get HTTP status code.
   * @param httpHeaders  HTTP headers.
   * @return HTTP status code.
   * @throws WSIException if the status code in http headers was not found.
   */
  public static String getHTTPStatusCode(String httpHeaders)
    throws WSIException
  {
    String statusCode = null;

    if (httpHeaders.startsWith("HTTP"))
    {
      // Get location of status code
      int index = httpHeaders.indexOf(" ");
      int index2 = httpHeaders.indexOf(" ", index + 1);

      if ((index == -1) || (index2 == -1))
      {
        throw new WSIException(
          "Could not find status code in http headers: [" + httpHeaders + "].");
      }

      else
      {
        statusCode = httpHeaders.substring(index + 1, index2);
      }
    }

    else
    {
      throw new WSIException(
        "Could not find status code in http headers: [" + httpHeaders + "].");
    }

    // Return status code
    return statusCode;
  }

  /**
   * Get HTTP headers from a full message.
   * @param fullMessage  a message.
   * @return HTTP headers from a full message.
   */
  public static String getHTTPHeaders(String fullMessage)
  {
    //String httpHeaders = null;

    // Try looking for the double newline
    int index = fullMessage.indexOf("\r\n\r\n");
    if (index != -1)
    {
      index += 4;
    }
    else
    {
      // check for case "\r\r...\r\n\r\r...\n"
      // Note the index that is returned points to the first character 
      // immediatedly following the first occurence of the CRLFCRLF. 
      index = getFirstCRLFCRLF(fullMessage);
      if (index == -1)
      {
        logError(
          "Unable to parse HTTP message to find headers.  Full message: "
          + fullMessage);
        return "x-WSI-Test-Tool-Error: Couldn't find headers.  Full message: ["
          + fullMessage
          + "].";
      }
    }

    // Return HTTP headers
    return fullMessage.substring(0, index);
  }

  /**
   * Get SOAP message from a full message.
   * @param fullMessage  a message.
   * @return SOAP message from a full message.
   */
  public static String getContent(String fullMessage)
  {
    String message = "";

    // Find start of message
    int index = fullMessage.indexOf("\r\n\r\n");
    if (index != -1)
    {
      index += 4;
    }
    else
    {
      // check for case "\r\r...\r\n\r\r...\n"
      // Note the index that is returned points to the first character 
      // immediatedly following the first occurence of the CRLFCRLF. 
      index = getFirstCRLFCRLF(fullMessage);
    }    	

    if (index < 0)
    {
    	// If we couldn't find the end of the HTTP headers or the start of the message, then show error
      logError(
        "Unable to parse message to get content.  Full message: "
          + fullMessage);
      message =
        "x-WSI-Test-Tool-Error: Couldn't find message content.  Full message: ["
          + fullMessage
          + "].";
    }
    // If the index is greater than the length, then there is no message content
    else if (index >= fullMessage.length())
    {
      message = "";
    }

    // Else get the message content
    else
    {
      message = fullMessage.substring(index);
    }

    // Return SOAP message
    return message;
  }
 
  /**
   * Get HTTP headers from a full message.
   * @param fullMessage  a message.
   * @return HTTP headers from a full message.
   */
  public static byte[] getHTTPHeaders(byte[] fullMessage)
  {
    //String httpHeaders = null;
    int index = getFirstCRLFCRLF(fullMessage, 0);
    if (index == -1)
    {
      logError(
        "Unable to parse HTTP message to find headers.  Full message: "
          + fullMessage);
      return ("x-WSI-Test-Tool-Error: Couldn't find headers.  Full message: ["
        + fullMessage + "].").getBytes();
    }

    // Return HTTP headers
	byte[] b = new byte[index];
	System.arraycopy(fullMessage, 0, b, 0, index);

    return b;
  }

  /**
   * Get HTTP headers from a full message.
   * @param fullMessage  a message.
   * @return HTTP headers from a full message.
   */
  public static String getHTTPHeadersAsString(byte[] fullMessage, String encoding)
  {
    //String httpHeaders = null;
    int index = getFirstCRLFCRLF(fullMessage, 0);
    if (index == -1)
    {
      logError(
        "Unable to parse HTTP message to find headers.  Full message: "
          + fullMessage);
      return "x-WSI-Test-Tool-Error: Couldn't find headers.  Full message: ["
        + fullMessage + "].";
    }

    // Return HTTP headers
    try
    {
      return new String(fullMessage, 0, index, encoding);
    }
    catch (UnsupportedEncodingException e)
    {
        logError(
                "Unsupported Encoding: " + encoding + ".  Full message: "
                  + fullMessage);
              return "x-WSI-Test-Tool-Error: Unsupported Encoding \"" + encoding + "\".  Full message: ["
                + fullMessage + "].";
    }
  }

  /**
   * Get SOAP message from a full message.
   * @param fullMessage  a message.
   * @return SOAP message from a full message.
   */
  public static String getContentAsString(byte[] message)
  {
  	String content = "";
    //String httpHeaders = null;
    int index = getFirstCRLFCRLF(message, 0);

    // If we couldn't find the end of the HTTP headers or the start of the message, then show error
    if (index < 0)
    {
      logError(
        "Unable to parse message to get content.  Full message: "
          + message);
      content =
        "x-WSI-Test-Tool-Error: Couldn't find message content.  Full message: ["
          + message.toString()
          + "].";
    }

    // Else get the message content
    else if (index < message.length)
    {
      try
      {
        content = new String(message, index, message.length - index);
       }
      catch (Exception e)
      {
      	logError(
      	        "Unable to parse message to get content.  Full message: "
      	          + message);
      	content =
      	        "x-WSI-Test-Tool-Error: Couldn't find message content.  Full message: ["
      	          + message.toString()
      	          + "].";
      }
    }

    // Return SOAP message
    return content;
  }

  /**
   * Get SOAP message from a full message.
   * @param fullMessage  a message.
   * @return SOAP message from a full message.
   */
  public static byte[] getContent(byte[] message)
  {
    byte[] content = new byte [0];

    //String httpHeaders = null;
    int index = getFirstCRLFCRLF(message, 0);
 
    // If we couldn't find the end of the HTTP headers or the start of the message, then show error
    if (index < 0)
    {
      logError(
        "Unable to parse message to get content.  Full message: "
          + message);
      message =
        ("x-WSI-Test-Tool-Error: Couldn't find message content.  Full message: ["
          + message.toString()
          + "].").getBytes();
    }

    // If the index is greater than the length, then there is no message content
    //else if (index >= fullMessage.length())
    //{
    //  message = "";
    //}

    // Else get the message content
    else if (index < message.length)
    {
    	byte[] b = new byte[message.length - index];
    	System.arraycopy(message, index, b, 0, message.length - index);

        content = b;
    }

    // Return SOAP message
    return content;
  }

  
  /**
	 * Returns the first location of a CRLF.
	 *
	 * @return int
	 */
	public static int getFirstCRLF(byte[] buffer, int index) 
	{
		int size = buffer.length;
		int i = index;
		while (i < size - 1) {
			if (buffer[i] == CR && buffer[i+1] == LF)
				return i;
			i++;
		}
		return -1;
	}

	/**
	 * Returns the first location of a CRLF followed imediately by another CRLF.
	 *
	 * @return int
	 */
	public static int getFirstCRLFCRLF(byte[] buffer, int index) 
	{
		int size = buffer.length;
		int i = index;
		while (i < size - 3) 
		{
		  if (buffer[i] == CR && buffer[i+1] == LF && buffer[i+2] == CR)
		  {
			if (buffer[i+3] == LF)
			{
			  return i+4;
			}
			else
			{
			  int j = i + 3;
			  while (j < buffer.length && buffer[j] == CR)
			  {
				j++;
			  }
			  if (j < buffer.length && buffer[j] == LF)
			  {
			  	return j + 1;
			  }
			}
		  }
		  i++;
		}
		return -1;
	}

	/**
	 * Returns the first location of a CRLF followed imediately by another CRLF.
	 *
	 * @return int
	 */
	public static int getFirstCRLFCRLF(String buffer) 
	{
		int index = buffer.indexOf("\r\n\r");
		if (index != -1)
		{
		  int i = index +3;
		  while (i < buffer.length() && buffer.startsWith("\r", i))
		     i++;
		
		  if (i < buffer.length() && buffer.startsWith("\n", i))
			return i+1;
		  else
			return getFirstCRLFCRLF(buffer.substring(index + 3));
		}
		else
		{
		  return -1;
		}
	}

	/**
     * Returns the list of indices which marks the separation of parts.
     */
	public static int[] getBoundaryIndices(byte[] message, String boundaryStr)
	{
	  int[] indices = new int[256];
	  int indicesIndex = 0;
	  try
	  {
	    byte[] boundary = ("\r\n--" + boundaryStr).getBytes("US-ASCII");

	    int index = 0;
	    int start = 0;
	    while (index != -1)
	    {
	      index = indexOf(message, boundary, start);
	      
	      if (index != -1) 
	      {
		    start = index + boundary.length;
	    	indices[indicesIndex] = index;
	    	indicesIndex++;
	      }
	    }
      	int[] b = new int[indicesIndex];
    	System.arraycopy(indices, 0, b, 0, indicesIndex);
    	indices = b;
	  }
	  catch (Exception e)
	  {}
	  return indices;
	}
 
    /**
     * Returns the index of the first occurrence of key in the buffer.
     */
	public static int indexOf(byte[] buffer, byte[] key, int start)
	{
		int bufferLen = buffer.length;
		int keyLen = key.length;
		int i,j,k = 0;
		
		if (keyLen > bufferLen - start)
		{
			return -1;
		}
	
		for (k = start + keyLen - 1; k < bufferLen; k++)
		{
           for (j = keyLen - 1, i = k; (j >= 0) && (buffer[i] == key[j]); j--) 
           {
               i--;
           }

           if (j == (-1)) {
               return i + 1;
           }
		}

		return -1;
	}
	/**
   * Get contents of a resource and return as a input stream.
   *
   * @param resourceName the name of the resource to get and return as
   *                      an input stream.
   * @return contents of a resource as an input stream.
   * @throws IOException if the resource could not be located.
   */
  public static InputStream getInputStream(String resourceName)
    throws IOException
  {
    InputStream is = null;

    // If resource reference is a URL, then input stream from URL
    try
    {
      // Try to create URL
      URL urlResource = new URL(resourceName);

      // If successful, then get URL input stream
      is = getInputStream(urlResource);
    }

    // Else try to read resource directly
    catch (MalformedURLException mue)
    {
      boolean bTryClassLoader = false;

      try
      {
        // Open file input stream
        is = new BufferedInputStream(new FileInputStream(resourceName));
      }
      catch (FileNotFoundException fnfe)
      {
        // Set try class loader flag
        bTryClassLoader = true;
      }
      catch (SecurityException se)
      {
        // Set try class loader flag
        bTryClassLoader = true;
      }
      catch (Exception e)
      {
        // DEBUG:
        System.out.println("Exception in getInputStream :" + e.toString());
      }

      // If try class loader, then use it to get input stream
      if (bTryClassLoader)
      {
        // Use class loader to load resource
        is = ClassLoader.getSystemResourceAsStream(resourceName);
      }
    }

    // If the input stream is null, then throw FileNotFoundException
    if (is == null)
    {
      //try this
      is =
        Thread.currentThread().getContextClassLoader().getResourceAsStream(
          resourceName);
    }

    // If the input stream is null, then throw FileNotFoundException
    if (is == null)
    {
      //try this
      URL aURL =
        Thread.currentThread().getContextClassLoader().getResource(
          resourceName);
      if (aURL != null)
        is = getInputStream(aURL);
    }

    if (is == null)
      // Throw execption
      throw new FileNotFoundException(
        "Could not locate resource file: " + resourceName);

    // Return input stream
    return is;
  }

  /**
   * Get the input stream from a URL.
   * @param urlFile the URL to get the input stream from.
   * @return the input stream corresponding to the given URL.
   * @throws IOException if attempt to open the file denoted by URL has failed.
   * @throws ConnectException if trouble connecting to URL.
   */
  public static InputStream getInputStream(URL urlFile)
    throws IOException, ConnectException
  {
    InputStream is = null;

    // ADD: how are URLs that are password protected handled????

    try
    {
      // Open file input stream
      is = new BufferedInputStream(urlFile.openStream());
    }

    catch (ConnectException e)
    {
      // Re-throw this excpetion with additional information
      throw new java.net.ConnectException(
        "Could not connect to URL: " + urlFile.toExternalForm() + ".");
    }

    // Return input stream
    return is;
  }

  /**
   * Get contents of a resource and return as a input stream.
   * @param fileLocation the location of the file.
   * @return contents of a resource as a input stream.
   */
  public static boolean fileExists(String fileLocation)
  {
    boolean fileExists = false;

    // If resource reference is a URL, then input stream from URL
    try
    {
      // Try to create URL
      URL url = new URL(fileLocation);

      // If successful, then try to open connection
      url.openStream();
      fileExists = true;
    }

    // Else try to read resource directly
    catch (MalformedURLException mue)
    {
      try
      {
        File file = new File(fileLocation);

        fileExists = file.exists();
      }

      catch (Exception e2)
      {
        fileExists = false;
      }
    }

    catch (FileNotFoundException fnfe)
    {
      fileExists = false;
    }

    catch (Exception e)
    {
      fileExists = false;
    }

    // Return file exists indicator
    return fileExists;
  }

  /**
   * Get local host name.
   * @return the local host name.
   */
  public static String getLocalHostName()
  {
    String sLocalHostName;

    try
    {
      // Get local host name
      sLocalHostName = InetAddress.getLocalHost().getHostName();
    }
    catch (Exception e)
    {
      // Set default local host name
      sLocalHostName = "127.0.0.1";
    }

    // Return local host name
    return sLocalHostName;
  }

  /**
   * Build a URL string from hostname, port and URN.
   *
   * @param hostname the hostname.
   * @param port the port.
   * @param urn the URN.
   * @return formatted URL string.
   */
  public static String formatURL(String hostname, String port, String urn)
  {
    // Build URN
    String formatURN = urn;

    // If URN doesn't start with "/", then add it
    if (!(formatURN.startsWith("/")))
    {
      // Add "/" to beginning of the string
      formatURN = "/" + urn;
    }

    // Return URL string
    return "http://" + hostname + ":" + port + formatURN;
  }

  /**
   * This method will replace all of the occurances of a string
   * with a substitution string.
   *
   * @param sText String to udpate.
   * @param sFind String to find.
   * @param sReplace String to use for substitution.
   * @return updated string.
   */
  public static String replaceString(
    String sText,
    String sFind,
    String sReplace)
  {
    int iPrevIndex = 0;

    int iFindLen = sFind.length();
    int iReplaceLen = sReplace.length();

    String sUpdatedText = sText;

    // Replace all occurances of the find string
    for (int iIndex = sUpdatedText.indexOf(sFind);
      iIndex < (sUpdatedText.length() - 1) && iIndex != -1;
      iIndex = sUpdatedText.indexOf(sFind, iPrevIndex + iReplaceLen))
    {
      // Set updated text from the front portion + replacement text + back portion
      sUpdatedText =
        sUpdatedText.substring(0, iIndex)
          + sReplace
          + sUpdatedText.substring(iIndex + iFindLen);

      // Set the previous index field
      iPrevIndex = iIndex;
    }

    // Return updated text string
    return sUpdatedText;
  }

  /**
   * Convert string to hex string.
   * @param data  a String object.
   * @return hex string.
   */
  public static String toHexString(String data)
  {
    char[] HEX_CHARS =
      {
        '0',
        '1',
        '2',
        '3',
        '4',
        '5',
        '6',
        '7',
        '8',
        '9',
        'A',
        'B',
        'C',
        'D',
        'E',
        'F' };

    // Get string as byte array
    byte[] byteData = data.getBytes();

    // Get length
    int length = byteData.length;

    // Create Char buffer
    char[] charBuffer = new char[length * 2];

    int next;
    for (int byteCnt = 0, charCnt = 0; byteCnt < length;)
    {
      next = byteData[byteCnt++];
      charBuffer[charCnt++] = HEX_CHARS[(next >>> 4) & 0x0F];
      charBuffer[charCnt++] = HEX_CHARS[next & 0x0F];
    }

    return new String(charBuffer);
  }

  /**
   * Convert byte buffer to hex string.
   * @param data  a byte array.
   * @return hex string.
   */
  public static String toHexString(byte[] byteData)
  {
    char[] HEX_CHARS =
      {
        '0',
        '1',
        '2',
        '3',
        '4',
        '5',
        '6',
        '7',
        '8',
        '9',
        'A',
        'B',
        'C',
        'D',
        'E',
        'F' };


    // Get length
    int length = byteData.length;

    // Create Char buffer
    char[] charBuffer = new char[length * 2];

    int next;
    for (int byteCnt = 0, charCnt = 0; byteCnt < length;)
    {
      next = byteData[byteCnt++];
      charBuffer[charCnt++] = HEX_CHARS[(next >>> 4) & 0x0F];
      charBuffer[charCnt++] = HEX_CHARS[next & 0x0F];
    }

    return new String(charBuffer);
  }

  // I18N: 2003.02.26 modified by K.Nakagome@BeaconIT
  /**
   * Get MIME charset from a HTTP headers.
   * @param httpHeaders String of HTTP header.
   * @return the MIME charset string.
   * @author K.Nakagome@BeaconIT Japan SIG 
   */
  public static String getHTTPCharset(String httpHeaders)
  {
    String mimeCharset = null;

    mimeCharset = httpHeaders.toUpperCase();
    int[] index = { -1, -1, -1 };
    int indexS = mimeCharset.indexOf("CHARSET");
    int indexE = Integer.MAX_VALUE;
    if (indexS < 17)
    {
      return "";
    }
    indexS = mimeCharset.indexOf("=", indexS + 7);
    if (indexS == -1)
    {
      return "";
    }
    indexS++;
    index[0] = mimeCharset.indexOf("'", indexS);
    index[1] = mimeCharset.indexOf("\r\n", indexS);
    index[2] = mimeCharset.indexOf("\"", indexS);
    for (int i = 0; i < 3; i++)
    {
      if (index[i] != -1 & indexE > index[i])
      {
        indexE = index[i];
      }
    }

    if (indexE != Integer.MAX_VALUE)
    {
      mimeCharset = httpHeaders.substring(indexS, indexE);
      mimeCharset.trim();
    }
    else
    {
      mimeCharset = "";
    }
    return mimeCharset;
  }

  /**
   * Checks to see if the message is a simple SOAP message or whether it is a SOAP messagwe with attachments.
   */
  public static boolean isMultipartRelatedMessage(String httpHeaders)
  {
  	boolean result = false;
    try 
    {
      // check header for mime version and boundary
      String contentType = HTTPUtils.getHttpHeaderAttribute(httpHeaders, HTTPConstants.HEADER_CONTENT_TYPE);
      if (contentType == null)
      {
        // there is no contentType, check if there is a boundary attribute
        String boundary = Utils.getHttpHeaderSubAttribute(httpHeaders, HTTPConstants.HEADER_CONTENT_TYPE, "boundary");
        if ((boundary != null) && (!boundary.equals("")))
           result = true;
      }
      else
      {
        result = contentType.equalsIgnoreCase("multipart/related");
      }
    } 
    catch (WSIException e)
	{ 
    	result = false; 
    }
    return result;
  }

  public static String getHttpHeaderAttribute(String httpHeaders, String attributeName)
  { String result = null;
  	try
  	{
     result = HTTPUtils.getHttpHeaderAttribute(httpHeaders, attributeName);
  	}
    catch (WSIException e)
	{ 
    	result = null; 
    }
    return result;
 }

  public static String getHttpHeaderSubAttribute(String httpHeaders, String attributeName, String subAttributeName)
  { String result = null;
  	try
  	{
     result = HTTPUtils.getHttpHeaderSubAttribute(httpHeaders, attributeName, subAttributeName);
  	}
    catch (WSIException e)
	{ 
    	result = null; 
    }
    return result;
 }

  public static String getMimeHeaderAttribute(String mimeHeaders, String attributeName)
  { String result = null;
  	try
  	{
     result = MIMEUtils.getMimeHeaderAttribute(mimeHeaders, attributeName);
  	}
    catch (WSIException e)
	{ 
    	result = null; 
    }
    return result;
 }

  public static String getMimeHeaderSubAttribute(String mimeHeaders, String attributeName, String subAttributeName)
  { String result = null;
  	try
  	{
     result = MIMEUtils.getMimeHeaderSubAttribute(mimeHeaders, attributeName, subAttributeName);
  	}
    catch (WSIException e)
	{ 
    	result = null; 
    }
    return result;
 }
  // I18N: 2003.02.26 modified by K.Nakagome@BeaconIT
  /**
   * Get XML encoding from a SOAP Messages.
   * @param message  SOAP Message String. 
   * @return character encoding of XML.
   * @author K.Nakagome@BeaconIT Japan SIG 
   */
  public static String getXMLEncoding(String message)
  {
    String xmlDef = null;

    int indexS = message.indexOf("<?xml");
    int indexE = -1;
    if (indexS != -1)
    {
      indexE = message.indexOf("?>", indexS);
      if (indexE > indexS)
      {
        xmlDef = message.substring(indexS, indexE);
      }
    }

    if (xmlDef != null)
    {
      indexS = xmlDef.indexOf("encoding");
      if (indexS == -1)
      {
        xmlDef = "";
      }
      else
      {
        xmlDef = xmlDef.substring(indexS + 8);
        xmlDef = xmlDef.trim();
      }
    }
    else
    {
      return "";
    }

    if (xmlDef.length() > 3)
    {
      indexS = xmlDef.indexOf("=");
      if (indexS == 0)
      {
        xmlDef = xmlDef.substring(1);
      }
      else
      {
        return "";
      }
    }

    if (xmlDef.length() > 3)
    {
      String end = "\"";
      indexS = xmlDef.indexOf(end);
      if (indexS != 0)
      {
        indexS = xmlDef.indexOf((end = "'"));
      }
      if (indexS == 0)
      {
        indexE = xmlDef.indexOf(end, 3);
        if (indexE != -1)
        {
          xmlDef = xmlDef.substring(1, indexE);
        }
        else
        {
          xmlDef = "";
        }
      }
      else
      {
        xmlDef = "";
      }
    }
    return xmlDef;
  }

  // I18N: 2003.02.26 modified by K.Nakagome@BeaconIT
  private static ResourceBundle javaEncodingResource = null;
  private static final String JAVA_ENCODING_RESOURCE =
    "org.wsi.test.util.JavaEncoding";
  private static final String JAVA_ENCODING_DEFAULT = "UTF-8";

  // I18N: 2003.02.26 modified by K.Nakagome@BeaconIT
  /**
   * Get Java VM supported character encoding.
   * 
   * @param mimeEncoding  string of MIME(IANA) character encoding. 
   * @return string of character encoding supported by Java VM.
   * @author K.Nakagome@BeaconIT Japan SIG 
   */
  public static String getJavaEncoding(String mimeEncoding)
  {
    if (mimeEncoding == null || mimeEncoding.length() == 0)
    {
      return JAVA_ENCODING_DEFAULT;
    }
    try
    {
      if (javaEncodingResource == null)
      {
        javaEncodingResource = ResourceBundle.getBundle(JAVA_ENCODING_RESOURCE);
      }
      return javaEncodingResource.getString(mimeEncoding);
    }
    catch (Throwable t)
    {
      return mimeEncoding;
    }
  }

  /**
   * Convert an array to a Vector.
   * 
   * @param array  the array to be converted .
   * @return converted Vector (null if array is null, empty if empty).
   * @author Graham Turrell IBM
   */
  public static Vector arrayToVector(Object[] array)
  {
    if (array == null)
      return null;
    Vector v = new Vector(array.length);
    for (int i = 0; i < array.length; i++)
      v.add(array[i]);
    return v;
  }

  /**
   * Designates legal versions for the profile test assertion document
   * @param name - the TAD name
   * @param version - the legal version
   */
  public static void registerValidProfileTADVersion(String name, String version)
  {
      if (validProfileTADVersions == null)
          validProfileTADVersions = new HashMap();
      validProfileTADVersions.put(name, version);
  }
  
  /**
   * Checks to ensure that version of the profile test assertion 
   * document is supported in this version of the test tools.
   * @param profileAssertions - a profile TAD.
   * @return true if the version of the profile test assertion
   *         docuement is supported in this version of the test tools.
   */
  public static boolean isValidProfileTADVersion(ProfileAssertions profileAssertions)
  {

    String name = profileAssertions.getTADName();
    String version = profileAssertions.getTADVersion();
    
    if (validProfileTADVersions == null) {
       String versions[][] = WSITestToolsPlugin.getPlugin().getAllTADVersions();
       for (int i = 0; i < versions.length; i++)
           registerValidProfileTADVersion(versions[i][0], versions[i][1]);
    }

    // Unable to determine validity, and hence treating as valid.
    if (validProfileTADVersions == null) {
        return true;
    }

    if (validProfileTADVersions.containsKey(name))
        return checkVersionNumber((String) validProfileTADVersions.get(name),
                version);
    else
        return false;
  }

  /**
   * Checks to ensure that version number of the actual profile test assertion 
   * document is supported in this version of the test tools.
   * @param supportedVersion - supported version number of profile TAD.
   * @param actualVersion    - actual version number of profile TAD. 
   * @return true if the version number of the actual profile test assertion
   *         document is supported in this version of the test tools.
   */
  private static boolean checkVersionNumber(
    String supportedVersion,
    String actualVersion)
  {
    boolean validVersion = true;

    try
    {
      StringTokenizer supportedVersionTokenizer =
        new StringTokenizer(supportedVersion, ".");
      StringTokenizer actualVersionTokenizer =
        new StringTokenizer(actualVersion, ".");

      while (supportedVersionTokenizer.hasMoreTokens() && validVersion)
      {
        int supportedVersionToken =
          Integer.parseInt(supportedVersionTokenizer.nextToken());
        if (actualVersionTokenizer.hasMoreTokens())
        {
          int actualVersionToken =
            Integer.parseInt(actualVersionTokenizer.nextToken());
          if (supportedVersionToken > actualVersionToken) break;
          else validVersion = (supportedVersionToken >= actualVersionToken);
        }
      }
    }
    catch (Exception e)
    {
      validVersion = false;
    }
    return validVersion;
  }
  
  /**
   * Identifies the root part in the list using the "start" attribute.
   * If the "start" attribute does not exist then the first part is designated the root.
   */
  public static MimePart findRootPart(String httpHeaders, Collection parts)
  {
  	MimePart root = null;
    String start = Utils.getHttpHeaderSubAttribute(httpHeaders, HTTPConstants.HEADER_CONTENT_TYPE, "start");
    if (!parts.isEmpty())
    {
      // default to the first part in the collection
      root = (MimePart)parts.iterator().next();
    	
      if ((start != null) && (!start.equals("")))
      {
    	Iterator i = parts.iterator();
    	boolean rootNotFound = true;
    	while (i.hasNext() && rootNotFound)
    	{
    	  MimePart part = (MimePart)i.next();
    	  String headers = part.getHeaders();
    	  if (headers != null)
    	  {
            String contentId = Utils.getMimeHeaderAttribute(headers, MIMEConstants.HEADER_CONTENT_ID);
            if (start.equals(contentId))
            {
              root = part;
			  rootNotFound = false;
            }
    	  }
    	}
      }
    }
  	return root;
  }
  
  /**
   * Decodes the given encoded string.
   */
  public static byte[] decodeBase64(String str)
  {
  	try
  	{
    	Base64 decoder = new Base64();
  	    return decoder.decode(str.getBytes());
  	}
  	catch (Exception e)
  	{
  		return new byte[0];
  	}
  }

  /**
   * Encodes the given byte array.
   */
  public static String encodeBase64(byte[] buffer)
  {    
  	Base64 encoder = new Base64();
  	return new String(encoder.encode(buffer));
  }

  public static MimeParts parseMultipartRelatedMessage(String message, String httpHeaders, String encoding)
  {
  	byte[] buffer = null;
  	try
	{ 
  	  buffer = message.getBytes(encoding);
	}
  	catch (Exception e)
	{
  		return null;
	}
  	return parseMultipartRelatedMessage(buffer, httpHeaders, encoding);
  }
  public static MimeParts parseMultipartRelatedMessage(byte[] message, String httpHeaders, String encoding)
  {
    MimeParts mimeParts = new MimePartsImpl();
    String boundary = Utils.getHttpHeaderSubAttribute(httpHeaders, HTTPConstants.HEADER_CONTENT_TYPE, "boundary");
    ArrayList parts = new ArrayList();
    
    if (boundary == null)
    {
    	// assume it is a simple SOAP message
    	return null;
    }
    else
    {
      String start = Utils.getHttpHeaderSubAttribute(httpHeaders, HTTPConstants.HEADER_CONTENT_TYPE, "start");
      int[] indices = Utils.getBoundaryIndices(message, boundary);
      boolean rootNotFound = true;
      
      for (int i= indices.length - 2; i>=0; i--)
      {
        try
    	{
          MimePart part = new MimePartImpl();
          int index = Utils.getFirstCRLFCRLF(message, indices[i]);
          if ((index > indices[i]) && (index < indices[i+1]))
          {
            // the boundary string & mime headers (include the trailing CRLF CRLF)
        	String str = new String(message, indices[i], (index - indices[i]), "US-ASCII");
        	String delimiter = str.substring(0, str.indexOf("\r\n", 2) + 2);

        	if (i == indices.length -2)
        	{
        	  String endDelimiter = new String(message, indices[i + 1], message.length - indices[i + 1], "US-ASCII");
        	  int j = str.indexOf("\r\n", 2);
        	  if (j != -1)
        	    endDelimiter = str.substring(0, str.indexOf("\r\n", 2) + 2);
              part.setBoundaryStrings(new String[]{delimiter, endDelimiter});
            }
        	else
        	  part.setBoundaryStrings(new String[]{delimiter});
       
        	// the headers
        	String headers = str.substring(delimiter.length());
            if (headers.startsWith("\r\n"))
            {
          	  // no headers present
          	  part.setHeaders("");
            }
            else
            {
          	  part.setHeaders(headers);
            }
            
            // the content
            String contentId = Utils.getMimeHeaderAttribute(headers, MIMEConstants.HEADER_CONTENT_ID);
            int size = indices[i+1] - (index);
         	byte[] content = new byte[size];
      	    System.arraycopy(message, index, content, 0, size);
      	  
            if ((rootNotFound && (i == 0)) ||
            	((start != null) && (!start.equals("")) && (start.equals(contentId))))
            {
              // root part -- do not encode
              part.setContent(new String(content, encoding));
              mimeParts.setRootPart(part);
            }
            else
            {
              String transferEncoding =  Utils.getMimeHeaderAttribute(headers, MIMEConstants.HEADER_CONTENT_TRANSFER_ENCODING);
   
              if ((transferEncoding != null) && transferEncoding.equalsIgnoreCase("base64"))
              	part.setContent(new String(content, encoding));
              else
                part.setContent(Utils.encodeBase64(content));
            }
           parts.add(part);
         }
    	}
    	catch (Exception e)
    	{
    	  return null;
    	}
      }
      int size = parts.size();
      for (int i = size-1; i>=0; i--)
         mimeParts.addPart((MimePart)parts.get(i));
    }
    return mimeParts;
  }

  public static String toXMLString(MimeParts mimeParts)
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    // Add message content with attachments element
    pw.print("<" + WSIConstants.ELEM_MESSAGE_CONTENT_WITH_ATTACHMENTS);
    pw.print(">");

    Collection partList = mimeParts.getParts();
    if (!partList.isEmpty())
        {
         	Iterator iMimeParts = partList.iterator();
        	while (iMimeParts.hasNext())
            {
        	  MimePart mimePart = (MimePart)iMimeParts.next();
         	  pw.print(mimePart.toXMLString(""));
            }
        }

        // Add end message element
        pw.println("</" + WSIConstants.ELEM_MESSAGE_CONTENT_WITH_ATTACHMENTS + ">");
    // Return string
    return sw.toString();
  }

  public static void main (String[] args)
  {
  	try
	{
  	  FileInputStream inputStream = new FileInputStream("d:\\b.xml");
  	  int i = inputStream.available();
  	  byte[] buffer = new byte[i];
  	  inputStream.read(buffer);
  	  String message = new String(buffer);
  	  message = XMLUtils.xmlRemoveEscapedString(message);
  	  String headers = Utils.getHTTPHeaders(message);
  	  MimeParts parts = Utils.parseMultipartRelatedMessage(message, headers, Utils.JAVA_ENCODING_DEFAULT);
  	  System.out.println(Utils.toXMLString(parts));
	}
  	catch (Exception e){}
  }

  public static AnalyzerConfig getAnalyzerConfig(Reporter reporter) 
  {
	AnalyzerConfig result = null;
	if (reporter != null)
	{
	  try
	  {
		result = reporter.getReport().getReportContext().getAnalyzer().getAnalyzerConfig();
	  }
	  catch (Exception e)
	  {
		result = null;
	  }
	}
	return result;
  }
}
