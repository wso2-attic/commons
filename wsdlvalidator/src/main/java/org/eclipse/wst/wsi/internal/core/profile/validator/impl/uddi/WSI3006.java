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

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionResultException;
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
 * WSI3006 - The uddi:tModel uses the ws-i-org:conformsTo:2002_12 categorization
 * value of "http://ws-i.org/profiles/base/1.0". The categoryBag in the tModel
 * contains a keyedReference with a tModelKey that contains the key value
 * uuid:65719168-72c6-3f29-8c20-62defb0961c0 for the ws-i-org:conformsTo:2002_12
 * tModel.
 */
public class WSI3006 extends AssertionProcess
{
  private final UDDIValidatorImpl validator;
  /**
   * @param UDDIValidatorImpl
   */
  public WSI3006(UDDIValidatorImpl impl)
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
    failureDetailMessage = null;
    KeyedReference claimKeyedRef = null;

    // check whether a UDDI conformance claim is being made...

    // Get the tModel from the entryContext
    TModel tModel = (TModel) entryContext.getEntry().getEntryDetail();
    try
    {
      if ((claimKeyedRef = conformanceClaimMade(tModel)) == null)
      {
        throw new AssertionResultException(
          AssertionResult.RESULT_NOT_APPLICABLE);
      }

      // we have a conformance claim, so check the keyedReference keyValue
      if (!claimKeyedRef
        .getKeyValue()
        .equals(WSIConstants.ATTRVAL_UDDI_CLAIM_KEYVALUE))
      {
        throw new AssertionResultException(
          AssertionResult.RESULT_FAILED,
          "The tModel key is: ["
            + tModel.getTModelKey()
            + "].\nThe categoryBag is: ["
            + tModel.getCategoryBag()
            + "].");
      }
    }
    catch (AssertionResultException e)
    {
      result = e.getMessage();
      failureDetailMessage = e.getDetailMessage();
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetailMessage);
  }

  /**
   * Checks a category bag and extracts KeyedReference containing
   * the conformance claim.
   * @param proxy
   * @param bag
   * @return boolean
   */
  private KeyedReference checkCategoryBag(UDDIProxy proxy, CategoryBag bag)
  {
    String conformanceKey = null;
    try
    {
      conformanceKey = UDDIUtils.getWSIConformanceTModelKey(proxy);
    }
    catch (IllegalStateException ex)
    {
      return null;
    }

    boolean result = true;
    KeyedReference conformanceRef = null;

    if (bag != null)
    {
      Vector references = bag.getKeyedReferenceVector();
      for (int i = 0; i < references.size() && result; i++)
      {
        KeyedReference ref = (KeyedReference) references.get(i);

        if (ref
          .getTModelKey()
          .equalsIgnoreCase(conformanceKey) //&& ref.getKeyValue().equals(WSIConstants.ATTRVAL_UDDI_CLAIM_KEYVALUE)
        // we need only tModel key
        )
        {
          result = true;
          conformanceRef = ref;
        }
      }
    }

    return conformanceRef;
  }
  /**
   * Gets KeyedReference containing the conformance claim.
   * @param tModel
   * @return
   * @throws WSIException
   */
  private KeyedReference conformanceClaimMade(TModel tModel)
    throws WSIException
  {

    // If the tModel does not exist, then fail
    if (tModel == null)
    {
      throw new IllegalArgumentException("tModel cannot be null");
    }

    // If there is a tModel
    else
    {
      CategoryBag bag = tModel.getCategoryBag();
      return checkCategoryBag(this.validator.uddiProxy, bag);
    }

  }
}