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

import javax.wsdl.Definition;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.WSITag;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.Utils;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * BP2018. 
 * <context>For a candidate Web service definition</context>
 * <assertionDescription>The wsdl:types element occurs either as the first child in the WSDL namespace of the wsdl:definitions element if no wsdl:documentation or wsdl:import element is present; or immediately following the wsdl:documentation element(s) if they are present but wsdl:import(s) are not, or immediately following both the wsdl:documentation and wsdl:import elemen(s) if present.</assertionDescription>
 */
public class BP2018 extends AssertionProcess implements WSITag
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2018(WSDLValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }

  /*
   * @param el - xml element
   * @return if element is extensibility element returns true.
   */
  private boolean isExtensibilityElement(Element el)
  {
    boolean isEx = true;
    isEx = isEx && !XMLUtils.equals(el, WSDL_BINDING);
    isEx = isEx && !XMLUtils.equals(el, WSDL_DEFINITIONS);
    isEx = isEx && !XMLUtils.equals(el, WSDL_DOCUMENTATION);
    isEx = isEx && !XMLUtils.equals(el, WSDL_FAULT);
    isEx = isEx && !XMLUtils.equals(el, WSDL_IMPORT);
    isEx = isEx && !XMLUtils.equals(el, WSDL_INPUT);
    isEx = isEx && !XMLUtils.equals(el, WSDL_MESSAGE);
    isEx = isEx && !XMLUtils.equals(el, WSDL_OPERATION);
    isEx = isEx && !XMLUtils.equals(el, WSDL_OUTPUT);
    isEx = isEx && !XMLUtils.equals(el, WSDL_PART);
    isEx = isEx && !XMLUtils.equals(el, WSDL_PORT);
    isEx = isEx && !XMLUtils.equals(el, WSDL_PORTTYPE);
    isEx = isEx && !XMLUtils.equals(el, WSDL_SERVICE);
    isEx = isEx && !XMLUtils.equals(el, WSDL_TYPES);

    return isEx;
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

    // Get the location of the WSDL document
    Definition definition =
      (Definition) entryContext.getEntry().getEntryDetail();
    try
    {
      // Parse the WSDL document as an XML file
      Document doc =
        validator.parseXMLDocumentURL(definition.getDocumentBaseURI(), null);

      Element root = doc.getDocumentElement(); // get definition

      Element types = XMLUtils.findChildElement((Element) root, WSDL_TYPES);

      if (types != null)
      {
        Element el = XMLUtils.findPreviousSibling(types);
        while (el != null)
		{
          if (!isExtensibilityElement(el) &&
			   ((!XMLUtils.equals(el, WSDL_IMPORT)) &&
			    (!XMLUtils.equals(el, WSDL_DOCUMENTATION))))
		  {
		    result = AssertionResult.RESULT_FAILED;
			String message = "The " + el.getLocalName() + " element must not precede the types element.";
		    failureDetail = this.validator.createFailureDetail(message, entryContext);
  		    break;
		  }
          el = XMLUtils.findPreviousSibling(el);
		}
      }
    }

    catch (Throwable t)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
      failureDetail =
        this.validator.createFailureDetail(
          "An error occurred while processing the document at "
            + definition.getDocumentBaseURI()
            + ".\n\n"
            + Utils.getExceptionDetails(t),
          entryContext);
    }

    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}