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
package org.eclipse.wst.wsi.internal.core.profile.validator.impl.uddi;

import java.util.Vector;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.UDDIUtils;
import org.uddi4j.client.UDDIProxy;
import org.uddi4j.datatype.binding.BindingTemplate;
import org.uddi4j.datatype.business.BusinessEntity;
import org.uddi4j.datatype.service.BusinessService;
import org.uddi4j.util.CategoryBag;
import org.uddi4j.util.KeyedReference;


/**
 * WSI3007 - A uddi:businessEntity or uddi:businessService associated
 * with this uddi:bindingTemplate is not categorized using
 * the ws-i-org:conformsTo:2002_12 taxonomy.
 */
public class WSI3007 extends AssertionProcess
{
  private final UDDIValidatorImpl validator;

  /**
   * @param UDDIValidatorImpl
   */
  public WSI3007(UDDIValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }

  /**
   * Validates the test assertion.
   * @see org.eclipse.wst.wsi.test.profile.validator.impl.BaseValidatorImpl.AssertionProcess#validate(org.wsi.test.profile.TestAssertion, org.wsi.test.profile.validator.EntryContext)
   */
  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    result = AssertionResult.RESULT_PASSED;

    // Get the bindingTemplate from the entryContext
    BindingTemplate bindingTemplate =
      (BindingTemplate) entryContext.getEntry().getEntryDetail();

    boolean validService = false;
    boolean validBusiness = false;

    String serviceKey = null;
    String businessKey = null;
    try
    {
      serviceKey = bindingTemplate.getServiceKey();
      BusinessService service =
        UDDIUtils.getBusinessServiceByKey(this.validator.uddiProxy, serviceKey);

      businessKey = service.getBusinessKey();
      BusinessEntity business =
        UDDIUtils.getBusinessByKey(this.validator.uddiProxy, businessKey);

      validService = checkCategoryBag(this.validator.uddiProxy, service.getCategoryBag());
      validBusiness = checkCategoryBag(this.validator.uddiProxy, business.getCategoryBag());

      if (!validBusiness || !validService)
      {
        result = AssertionResult.RESULT_FAILED;

        if (!validBusiness)
        {
          failureDetailMessage =
            "The businessEntity key is: [" + businessKey + "].";
        }

        if (!validService)
        {
          if (!validBusiness)
            failureDetailMessage += " ";
          else
            failureDetailMessage = "";

          failureDetailMessage += "The businessService key is: ["
            + serviceKey
            + "].";
        }
      }
    }
    catch (IllegalStateException e)
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetailMessage =
        "The UDDI registry does not contain WSI conformance taxonomy"
          + " tModel (\"ws-i-org:conformsTo:2002_12\").";
    }
    catch (Throwable e)
    {
      throw new WSIException(
        "An exception occurred while processing the discovery test assertions.",
        e);
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetailMessage);
  }

  /**
   * Checks whether the category bag contains the conformance claim.
   * Returns <b>true</b> if the bag does not contain the conformance claim.
   * @param proxy
   * @param bag
   * @return boolean
   */
  private boolean checkCategoryBag(UDDIProxy proxy, CategoryBag bag)
  {
    String conformanceKey = null;
    try
    {
      conformanceKey = UDDIUtils.getWSIConformanceTModelKey(proxy);
    }
    catch (IllegalStateException ex)
    {
      return false;
    }

    boolean result = true;

    if (bag != null)
    {
      Vector references = bag.getKeyedReferenceVector();
      for (int i = 0; i < references.size() && result; i++)
      {
        KeyedReference ref = (KeyedReference) references.get(i);

        result = !ref.getTModelKey().equalsIgnoreCase(conformanceKey);
      }
    }

    return result;
  }
}