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
import org.uddi4j.datatype.tmodel.TModel;
import org.uddi4j.util.CategoryBag;
import org.uddi4j.util.KeyedReference;


/**
 * WSI3005_OBSOLETE - A uddi:tModel which claims conformance with a WS-I profile
 * must be categorized using the ws-i-org:conformsTo taxonomy.
 */
public class WSI3005_OBSOLETE extends AssertionProcess
{
  private final UDDIValidatorImpl validator;

  /**
   * @param UDDIValidatorImpl
   */
  public WSI3005_OBSOLETE(UDDIValidatorImpl impl)
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

    // Get the tModel from the entryContext
    TModel tModel = (TModel) entryContext.getEntry().getEntryDetail();

    // If the tModel does not exist, then fail
    if (tModel == null)
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetailMessage = "Could not locate a tModel.";
    }

    // If there is a tModel
    else
    {
      CategoryBag bag = tModel.getCategoryBag();

      try
      {
        if (!checkCategoryBag(this.validator.uddiProxy, bag))
        {
          // failed
          result = AssertionResult.RESULT_FAILED;
          failureDetailMessage =
            "The tModel key is: [" + tModel.getTModelKey() + "].";
        }
      }
      catch (IllegalStateException e)
      {
        result = AssertionResult.RESULT_FAILED;
        failureDetailMessage =
          "The UDDI registry does not contain WSI conformance taxonomy"
            + " tModel (\"ws-i-org:conformsTo:2002_12\").";
      }
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetailMessage);
  }

  /**
   * Checks a category bag.
   * @param proxy
   * @param bag
   * @return boolean
   */
  private boolean checkCategoryBag(UDDIProxy proxy, CategoryBag bag)
  {
    String conformanceKey = UDDIUtils.getWSIConformanceTModelKey(proxy);

    boolean result = false;

    if (bag != null)
    {
      Vector references = bag.getKeyedReferenceVector();
      for (int i = 0; i < references.size() && !result; i++)
      {
        KeyedReference ref = (KeyedReference) references.get(i);

        result = ref.getTModelKey().equalsIgnoreCase(conformanceKey);
      }
    }

    return result;
  }
}