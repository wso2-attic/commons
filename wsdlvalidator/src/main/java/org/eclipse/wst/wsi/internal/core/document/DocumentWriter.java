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
package org.eclipse.wst.wsi.internal.core.document;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.w3c.dom.Document;

/**
 * Defines the interface used to write the Conformance XML documents.
 * 
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public interface DocumentWriter
{
  /**
   * Get the Document object associated with this Conformance document.
   * @return the Document object associated with this Conformance document.
   * @throws UnsupportedOperationException if operation is not supported.
   */
  public Document getDocument() throws UnsupportedOperationException;

  /**
   * Set writer.
   * @param documentLocation the location of the document.
   * @throws IOException if failed or interrupted I/O operations. 
   */
  public void setWriter(String documentLocation) throws IOException;

  /**
   * Write out only a part of a document to the location
   * that was previously set using the setLocation method.
   * @param reader a Reader object.
   * @throws WSIException if there is trouble writing out the document.
   * @throws IllegalStateException if this operation has been invoked 
   *         at an illegal or inappropriate time.
   */
  public void write(Reader reader) throws WSIException, IllegalStateException;

  /**
   * Write out only a part of a document.
   * @param reader a Reader object.
   * @param writer a Writer object.
   * @throws WSIException if there is trouble writing out the document.
   */
  public void write(Reader reader, Writer writer) throws WSIException;

  /**
   * Close writer.
   * @throws WSIException if there is trouble closing the writer.
   * @throws IllegalStateException if this operation has been invoked 
   *         at an illegal or inappropriate time.
   */
  public void close() throws WSIException, IllegalStateException;
}
