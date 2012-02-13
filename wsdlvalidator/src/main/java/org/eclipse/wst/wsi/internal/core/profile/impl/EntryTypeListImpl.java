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
package org.eclipse.wst.wsi.internal.core.profile.impl;

import org.eclipse.wst.wsi.internal.core.profile.EntryTypeList;

/**
 * This implementation is used to maintain a profile EntryTypeList.
 * 
 * @version 1.0.1
 * @author Peter Brittenham  (peterbr@us.ibm.com)
 */
public class EntryTypeListImpl implements EntryTypeList
{
  /**
   * Log input.
   */
  protected String logInput = null;

  /**
   * WSDL input.
   */
  protected String wsdlInput = null;

  /**
   * UDDI input.
   */
  protected String uddiInput = null;

  /* (non-Javadoc)
   * @see org.wsi.test.profile.EntryTypeList#getLogInput()
   */
  public String getLogInput()
  {
    return this.logInput;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.EntryTypeList#setLogInput(String)
   */
  public void setLogInput(String logInput)
  {
    this.logInput = logInput;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.EntryTypeList#getWSDLInput()
   */
  public String getWSDLInput()
  {
    return this.wsdlInput;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.EntryTypeList#setWSDLInput(String)
   */
  public void setWSDLInput(String wsdlInput)
  {
    this.wsdlInput = wsdlInput;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.EntryTypeList#getUDDIInput()
   */
  public String getUDDIInput()
  {
    return this.uddiInput;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.EntryTypeList#setUDDIInput(String)
   */
  public void setUDDIInput(String uddiInput)
  {
    this.uddiInput = uddiInput;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.document.DocumentElement#toXMLString(String)
   */
  public String toXMLString(String namespaceName)
  {
    return null;
  }

}
