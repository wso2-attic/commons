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
package org.eclipse.wst.wsi.internal.core.profile.validator.impl.wsdl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.wsdl.Operation;
import javax.wsdl.PortType;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionFailException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;


/**
* BP2010.
* "Name" attributes of Operations are unique across the wsdl:portType definition 
*
* @version 1.0.1 27.06.2003
* @author Vitali Fedosenko
**/
public class BP2010 extends AssertionProcess
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2010(WSDLValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }

  /* Validates the test assertion.
  * @see org.wsi.test.profile.validator.impl.BaseValidatorImpl.AssertionProcess#validate(org.wsi.test.profile.TestAssertion, org.wsi.test.profile.validator.EntryContext)
  */
  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {

    // Get the portType from the entry context
    PortType portType = (PortType) entryContext.getEntry().getEntryDetail();

    // Check the operations names for uniqueness within the candidate Port Type in the WSDL 
    Iterator ops;
    HashSet namesSeen = new HashSet();
    HashSet duplicates = new HashSet();
    try
    {
      List opsList = portType.getOperations();
      if (opsList == null)
      {
        result = AssertionResult.RESULT_NOT_APPLICABLE;
        // no operations found in portType
      }
      else
      {
        ops = opsList.iterator(); // will use Candidate.getOperations()
        while (ops.hasNext())
        {
          Operation op = (Operation) ops.next();
          String opName = op.getName();
          if (namesSeen.contains(opName))
          {
            duplicates.add(opName);
          }
          else
          {
            namesSeen.add(opName);
          }
        }
      }

      if (!duplicates.isEmpty())
      {
        StringBuffer failInfo =
          new StringBuffer(
            "Duplicate operation names in portType "
              + portType.getQName()
              + ": ");
        Iterator i = duplicates.iterator();
        while (i.hasNext())
        {
          failInfo.append((String) i.next() + "; ");
        }
        throw new AssertionFailException(failInfo.toString());
      }
    }
    catch (NullPointerException e)
    { // ?? no operations found, but does not fail the assertion.
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    catch (AssertionFailException e)
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetail =
        this.validator.createFailureDetail(e.getMessage(), entryContext, portType);
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}