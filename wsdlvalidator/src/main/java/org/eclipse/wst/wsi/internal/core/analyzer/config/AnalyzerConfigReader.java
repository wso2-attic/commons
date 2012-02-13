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
package org.eclipse.wst.wsi.internal.core.analyzer.config;

import java.io.Reader;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.document.DocumentReader;
import org.eclipse.wst.wsi.internal.core.util.MessageList;


/**
 * Defines the interface used to read the analyzer config documents.
 * 
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public interface AnalyzerConfigReader extends DocumentReader {

  /**
   * Initialize analyzer config reader.
   * @param messageList a MessageList object.
   */
  public void init(MessageList messageList);
  
  /**
   * Read the analyzer config file.
   * @param analyzerConfigURI the analyzer config file location.
   * @return the AnalyzerConfig object.
   * @throws WSIException if unable to read the analyzer config file.
   */
  public AnalyzerConfig readAnalyzerConfig(String analyzerConfigURI) throws WSIException;
  
  /**
   * Read the analyzer config file.
   * @param reader a Reader object.
   * @return the AnalyzerConfig object.
   * @throws WSIException if unable to parse the analyzer config file.
   */
  public AnalyzerConfig readAnalyzerConfig(Reader reader) throws WSIException;
}


