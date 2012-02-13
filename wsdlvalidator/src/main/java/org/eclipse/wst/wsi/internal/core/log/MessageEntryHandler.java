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
package org.eclipse.wst.wsi.internal.core.log;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.report.ArtifactReference;

/**
 * Defines the interface for the MessageEntry handler.
 * 
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 * @author Graham Turrell (gturrell@uk.ibm.com)
 */
public interface MessageEntryHandler
{
  /**
   * Process artifact reference.
   * @param artifactReference artifact reference.
   * @throws WSIException if problems occur while processing artifact reference.
   */
  public void processArtifactReference(ArtifactReference artifactReference)
    throws WSIException;

  /**
   * Process a single log entry.
   * @param entryContext a single log entry.
   * @throws WSIException if problems occur while processing a single log entry.
  
   */
  public void processLogEntry(EntryContext entryContext) throws WSIException;
}
