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

import org.eclipse.wst.wsi.internal.core.document.DocumentElement;

/**
 * This interface is used to maintain a profile EntryTypeList.
 * 
 * @version 1.0.1
 * @author Peter Brittenham  (peterbr@us.ibm.com)
 */
public interface EntryTypeList extends DocumentElement
{
  /**
   * Log input.
   */
  public static final String LOG_INPUT_NONE = "none";
  public static final String LOG_INPUT_REQUEST = "request";
  public static final String LOG_INPUT_RESPONSE = "response";

  /**
   * WSDL input.
   */
  public static final String WSDL_INPUT_NONE = "none";

  /**
   * UDDI input.
   */
  public static final String UDDI_INPUT_NONE = "none";

  /**
   * Get log input.
   * @return log input.
   * @see #setLogInput
   */
  public String getLogInput();

  /**
   * Set log input.
   * @param logInput log input.
   * @see #getLogInput
   */
  public void setLogInput(String logInput);

  /**
   * Get WSDL input.
   * @return WSDL input.
   * @see #setWSDLInput
   */
  public String getWSDLInput();

  /**
   * Set WSDL input.
   * @param wsdlInput WSDL input.
   * @see #getWSDLInput
   */
  public void setWSDLInput(String wsdlInput);

  /**
   * Get UDDI input.
   * @return UDDI input.
   * @see #setUDDIInput
   */
  public String getUDDIInput();

  /**
   * Set UDDI input.
   * @param uddiInput UDDI input.
   * @see #getUDDIInput
   */
  public void setUDDIInput(String uddiInput);

}
