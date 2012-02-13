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
package org.eclipse.wst.wsi.internal.core.report;

import org.eclipse.wst.wsi.internal.core.WSIException;

/**
 * This class ..
 * 
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public interface WriteReport
{
  /**
   * Start conformance report.
   * @throws WSIException the base WS-I exception.
   */
  public void startReport() throws WSIException;

  /**
   * Finish the conformance report by adding the summary and test coverage information.
   * @throws WSIException the base WS-I exception.
   */
  public void finishReport() throws WSIException;

  /**
   * Finish the conformance report because of an analyzer error.
   * @param errorDetail analyzer error.
   * @throws WSIException the base WS-I exception.
   */
  public void finishReportWithError(String errorDetail) throws WSIException;

  /**
   * Get report.
   * @return report.
   */
  public Report getReport();
}
