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
package org.eclipse.wst.wsi.internal.core.report.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Vector;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.report.PrereqFailedList;

/**
 * This interface is used to maintain and access the Prerequisite Failed List.
 * 
 * @version 1.0.1
 * @author Peter Brittenham  (peterbr@us.ibm.com)
 */
public class PrereqFailedListImpl implements PrereqFailedList
{
  /**
   * List of test assertion IDs.
   */
  protected Vector idList = new Vector();

  /* (non-Javadoc)
   * @see org.wsi.test.report.PrereqFailedList#addTestAssertionID(java.lang.String)
   */
  public void addTestAssertionID(String assertionID)
  {
    idList.add(assertionID);
  }

  /* (non-Javadoc)
   * @see org.wsi.test.document.DocumentElement#toXMLString(String)
   */
  public String toXMLString(String namespaceName)
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    String nsName = namespaceName;
    if ((!nsName.equals("") && (!nsName.endsWith(":"))))
      nsName += ":";

    if (this.idList.size() > 0)
    {
      // Create element 
      pw.println("        <" + nsName + ELEM_NAME + ">");

      // Include each test assertion ID
      Iterator iterator = idList.iterator();
      while (iterator.hasNext())
      {
        pw.print(
          "          <" + nsName + WSIConstants.ELEM_TEST_ASSERTION_ID + ">");
        pw.print((String) iterator.next());
        pw.println("</" + nsName + WSIConstants.ELEM_TEST_ASSERTION_ID + ">");
      }

      // End element
      pw.println("        </" + nsName + ELEM_NAME + ">");
    }

    return sw.toString();
  }

}
