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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.wsdl.Binding;
import javax.wsdl.BindingFault;
import javax.wsdl.BindingOperation;
import javax.wsdl.extensions.ExtensibilityElement;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.WSITag;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionFailException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionNotApplicableException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;

/**
 * AP2930
 *
 * <context>For a candidate wsdl:binding</context>
 * <assertionDescription>A wsdl:fault element in a description does not have
 * mime:multipartRelated element as its child element.</assertionDescription>
 */
public class AP2930 extends AssertionProcess implements WSITag
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public AP2930(WSDLValidatorImpl impl)
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
    try
    {
      // A variable that indicates a binding contains at least one
      // wsdl:fault element
      boolean faultsFound = false;

      // Getting a wsdl:binding
      Binding binding = (Binding) entryContext.getEntry().getEntryDetail();

      // Getting its wsdl:operation elements
      List ops = binding.getBindingOperations();

      // Going through the operation elements
      for (int i = 0; i < ops.size(); i++)
      {
        BindingOperation bindingOperation = (BindingOperation) ops.get(i);

        // Getting wsdl:fault elements
        Collection faults = bindingOperation.getBindingFaults().values();
        if (!faults.isEmpty())
        {
          faultsFound = true;
          // If there is at least one mime:multipartRelated element,
          // the assertion failed
          if (containsMimeMultiparts(faults))
            throw new AssertionFailException("The binding operation is \""
              + bindingOperation.getName() + "\".");
        }
      }

      // If the binding does not contain wsdl:fault elements,
      // the assertion is not applicable
      if (!faultsFound)
        throw new AssertionNotApplicableException();
    }
    catch (AssertionNotApplicableException anae)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    catch (AssertionFailException afe)
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetail = validator.createFailureDetail(
        afe.getMessage(), entryContext);
    }
    // Return assertion result
    return validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }

  /**
   * Validates wsdl:fault elements.
   * @param faults a collection of wsdl:fault elements.
   * @return true if any wsdl:fault has a mime:multipartRelated child
   * element, false otherwise.
   */
  private boolean containsMimeMultiparts(Collection faults)
  {
    // Going through all the wsdl:faultS
    Iterator i = faults.iterator();
    while (i.hasNext())
    {
      // Getting wsdl:fault's extensibility elements
      List extElems = ((BindingFault) i.next()).getExtensibilityElements();
      for (int j = 0; j < extElems.size(); j++)
      {
        // If there is a mime:multipartRelated element, return true
        if (((ExtensibilityElement)extElems.get(j))
          .getElementType().equals(WSDL_MIME_MULTIPART))
        {
          return true;
        }
      }
    }
    // There are no mime:multipartRelated elements, return false
    return false;
  }
}