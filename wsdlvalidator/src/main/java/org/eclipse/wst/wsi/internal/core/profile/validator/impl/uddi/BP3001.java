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

import java.util.Map;

import javax.wsdl.Binding;
import javax.wsdl.Definition;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.UDDIUtils;
import org.eclipse.wst.wsi.internal.core.wsdl.WSDLDocument;
import org.uddi4j.datatype.OverviewDoc;
import org.uddi4j.datatype.tmodel.TModel;


/**
 * BP3001 - The tModel element uses WSDL as the description language:
 * the uddi:overviewDoc/uddi:overviewURL element contains a reference
 * to a WSDL definition, which uses a namespace
 * of http://schemas.xmlsoap.org/wsdl/.
 * The uddi:overviewURL may use the fragment notation to resolve
 * to a wsdl:binding.
 */
public class BP3001 extends AssertionProcess
{
  private final UDDIValidatorImpl validator;

  /**
   * @param UDDIValidatorImpl
   */
  public BP3001(UDDIValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }

  /**
   * Sets the result variable to AssertionResult.RESULT_FAILED value and
   * places a tModel key in the fault detail message.
   * @param tModelKey
   */
  private void setFaultMessage(String tModelKey)
  {
    result = AssertionResult.RESULT_FAILED;
    failureDetailMessage = "The tModel key is: [" + tModelKey + "].";
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

    Binding binding = null;

    // Get the tModel from the entryContext
    TModel tModel = (TModel) entryContext.getEntry().getEntryDetail();

    // If the tModel does not exist, then fail
    if (tModel == null)
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetailMessage = "Could not locate a tModel.";
      return validator.createAssertionResult(
        testAssertion,
        result,
        failureDetailMessage);
    }

    String key = tModel.getTModelKey();

    OverviewDoc doc = tModel.getOverviewDoc();
    if (doc != null)
    {
      String urlText = doc.getOverviewURLString();

      // Try to resolve the URL & check the WSDL
      try
      {
        WSDLDocument wsdlDocument = new WSDLDocument(urlText);
        Definition definition = wsdlDocument.getDefinitions();
        Map namespaces = definition.getNamespaces();
        if (!namespaces.containsValue(WSIConstants.NS_URI_WSDL))
        {
          urlText = null;
        }

        // Get binding
        binding = UDDIUtils.getBinding(urlText, wsdlDocument);

        // See if the binding is in the WSDL document
        if (binding == null
          || definition.getBinding(binding.getQName()) == null)
        {
          urlText = null;
        }
      }

      catch (Exception e)
      {
        urlText = null;
      }

      if (urlText != null)
      {
        String fragmentID = null;
        int index = urlText.indexOf("#");
        if (index > -1)
        {
          fragmentID = urlText.substring(index + 1);
        }

        if ((fragmentID != null)
          && (fragmentID.length() != 0)
          && (fragmentID.toUpperCase().indexOf("xpointer(".toUpperCase()) == -1
            || fragmentID.lastIndexOf(")") != fragmentID.length() - 1))
        {
          setFaultMessage(key);
        }
      }

      else
      {
        setFaultMessage(key);
      }
    }

    else
    {
      setFaultMessage(key);
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetailMessage);
  }
}