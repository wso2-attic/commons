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
import org.uddi4j.datatype.tmodel.TModel;
import org.uddi4j.util.CategoryBag;
import org.uddi4j.util.KeyedReference;


/**
* WSI3021 - The uddi:tModel has a "name" element of value "ws-i-org:conformsTo:2002_12" ,
* has an overviewURL value of "http://ws-i.org/schemas/conformanceClaim/", and has
* a keyedReference element with keyName attribute value of "uddi-org:types:categorization".
*/
public class WSI3021 extends AssertionProcess
{
  private final UDDIValidatorImpl validator;

  /**
   * @param UDDIValidatorImpl
   */
  public WSI3021(UDDIValidatorImpl impl)
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
    result = AssertionResult.RESULT_FAILED;

    TModel tModel =
      UDDIUtils.getTModelByKey(
        this.validator.uddiProxy,
        UDDIUtils.getWSIConformanceTModelKey(this.validator.uddiProxy));

    // If the tModel does not exist, then fail
    if (tModel == null)
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetailMessage = "Could not locate a tModel.";
    }
    else
    {
      boolean validURL =
        tModel.getOverviewDoc() != null
          && "http://ws-i.org/schemas/conformanceClaim/".equals(
            tModel.getOverviewDoc().getOverviewURLString());
      boolean validCategory = checkCategoryBag(tModel.getCategoryBag());

      if (validURL && validCategory)
      {
        result = AssertionResult.RESULT_PASSED;
      }
      else
      {
        result = AssertionResult.RESULT_FAILED;
        failureDetailMessage = this.validator.uddiReference.getInquiryURL();
      }
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetailMessage);
  }

  /**
   * Checks whether the category bag meets the WSI's requirements.
   * @param bag
   * @return
   */
  private boolean checkCategoryBag(CategoryBag bag)
  {
    boolean result = false;

    if (bag != null)
    {
      Vector references = bag.getKeyedReferenceVector();
      for (int i = 0; i < references.size() && !result; i++)
      {
        KeyedReference ref = (KeyedReference) references.get(i);

        result =
          "uddi-org:types:categorization".equals(ref.getKeyName())
            && "categorization".equals(ref.getKeyValue())
            && "uuid:C1ACF26D-9672-4404-9D70-39B756E62AB4".equals(
              ref.getTModelKey());
      }
    }

    return result;
  }
}