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
package org.eclipse.wst.wsi.internal.core.profile;

import java.io.Reader;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.document.DocumentReader;

/**
 * Defines the interface used to read a profile assertions documents.
 * 
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public interface ProfileAssertionsReader extends DocumentReader
{
  /**
   * Read the profile assertions file.
   * @param assertionsURI he profile assertions file location.
   * @return the ProfileAssertions object.
   * @throws WSIException if problem occur while reading the file.
   */
  public ProfileAssertions readProfileAssertions(String assertionsURI)
    throws WSIException;

  /**
   * Read the profile assertions  file.
   * @param reader a Reader object.
   * @return the ProfileAssertions object.
   * @throws WSIException  if problem occur while reading the file.
   */
  public ProfileAssertions readProfileAssertions(Reader reader)
    throws WSIException;
}
