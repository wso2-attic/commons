/*******************************************************************************
 * Copyright (c) 2002-2003 IBM Corporation, Beacon Information Technology Inc. and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM      - Initial API and implementation
 *   BeaconIT - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.core.document.impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.document.DocumentWriter;
import org.w3c.dom.Document;

/**
 * Defines the implementation used to write the Conformance XML 
 * documents.
 * 
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public abstract class DocumentWriterImpl implements DocumentWriter
{
  /**
   * Document writer.
   */
  protected Writer writer = null;

  /* (non-Javadoc)
   * @see org.wsi.test.document.DocumentWriter#getDocument()
   */
  public Document getDocument() throws UnsupportedOperationException
  {
    // ADD:
    return null;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.document.DocumentWriter#setWriter(String)
   */
  public void setWriter(String documentLocation)
    throws UnsupportedEncodingException, FileNotFoundException
  {
    // I18N: 2003.02.26 modified by K.Nakagome@BeaconIT 
    this.writer =
      new PrintWriter(
        new OutputStreamWriter(
          new FileOutputStream(documentLocation),
          "UTF-8"));
  }

  /* (non-Javadoc)
   * @see org.wsi.test.document.DocumentWriter#write(Reader)
   */
  public synchronized void write(Reader reader)
    throws WSIException, IllegalStateException
  {
    // If the writer was not specified, then throw an exception
    if (writer == null)
      throw new IllegalStateException("Could not write document since writer was not set.");

    write(reader, writer);
  }

  /* (non-Javadoc)
   * @see org.wsi.test.document.DocumentWriter#write(Reader, Writer)
   */
  public synchronized void write(Reader reader, Writer writer)
    throws WSIException
  {
    // Create buffered reader
    BufferedReader bufReader = new BufferedReader(reader);

    // Create print writer
    PrintWriter printWriter = new PrintWriter(writer);

    String nextLine = null;

    try
    {
      // Read each line from the reader
      while ((nextLine = bufReader.readLine()) != null)
      {
        // Write text and flush stream
        printWriter.println(nextLine);
      }
    }

    catch (Exception e)
    {
      throw new WSIException("Could not write document.", e);
    }

    // Flush and close the writer
    printWriter.flush();
    //printWriter.close();    
  }

  /**
   * Close writer.
   */
  public void close() throws WSIException, IllegalStateException
  {
    // If the writer was not specified, then throw an exception
    if (writer == null)
      throw new IllegalStateException("Could not write log file since writer was not set.");

    try
    {
      // Close the writer
      writer.close();
    }

    catch (IOException ioe)
    {
      throw new WSIException(ioe.toString(), ioe);
    }
  }

}
