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
package org.eclipse.wst.wsi.internal.core.profile.validator.impl.envelope;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionFailException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionNotApplicableException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionPassException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;


/**
 * BP1031.
 * 
 * @author: Graham Turrell, IBM UK
 */
public class BP1031 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP1031(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
  }

  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    failureDetail = null;
    try
    {
      Document doc;
      // Check if this is one way response
      // or message is mepty or invalid
      if (this.validator.isOneWayResponse(entryContext)
          || (doc = entryContext.getMessageEntryDocument()) == null)
      {
        throw new AssertionNotApplicableException();
      }

      if (!this.validator.isFault(doc))
      {
        throw new AssertionNotApplicableException();
      }

      NodeList faultCodes = doc.getElementsByTagName("faultcode");
      for (int i = 0; i < faultCodes.getLength(); i++)
      {
        NodeList faultCodeNodes = (faultCodes.item(i)).getChildNodes();
        for (int j = 0; j < faultCodeNodes.getLength(); j++)
        {
          if (faultCodeNodes.item(j) instanceof Text)
          {
            Text faultCodeText = (Text) faultCodeNodes.item(j);
            if (faultCodeText.getData().indexOf('.') > 0)
            {
              throw new AssertionFailException();
              // dotted notation used.
            }
          }
        }
      }

      throw new AssertionPassException();
      // no dotted notation used in a faultcode element
    }
    catch (AssertionPassException e)
    {
      result = AssertionResult.RESULT_PASSED;
    }
    catch (AssertionNotApplicableException e)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    catch (AssertionFailException e)
    {
      result = AssertionResult.RESULT_WARNING;
      failureDetail =
        this.validator.createFailureDetail(
          entryContext.getMessageEntry().getMessage(),
          entryContext);
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}