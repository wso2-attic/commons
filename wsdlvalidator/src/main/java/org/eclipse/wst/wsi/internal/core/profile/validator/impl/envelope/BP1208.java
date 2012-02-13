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
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.xml.XMLTraversal;
import org.w3c.dom.Document;
import org.w3c.dom.ProcessingInstruction;


/**
 * BP1208 NOT FULLY TESTED.
 */
public class BP1208 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP1208(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
  }

  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    // Parse log message 
    Document doc = entryContext.getMessageEntryDocument();
    if (doc == null) {
      //message is empty or invalid, the assertion is not applicable
      result = AssertionResult.RESULT_NOT_APPLICABLE;

    } else {

      // Traverse document looking for processing instructions.
      // ISSUE: this inner class needs ... revisiting/replacing
      // EG. Do we want to list all processing instructions, 
      // or is the first sufficient?
      XMLTraversal traversal = new XMLTraversal()
      {
        public void visit(ProcessingInstruction pi)
        {
          try
          {
            result = AssertionResult.RESULT_FAILED;
            failureDetailMessage =
              "Target: " + pi.getTarget() + ", Data: " + pi.getData();
          }
          catch (Exception e)
          {
            // ADD: How should this exception be handled?
          }

          super.visit(pi);
        }
      };

      traversal.visit(doc);

      if (result == AssertionResult.RESULT_FAILED
        && failureDetailMessage != null)
      {
        failureDetail = this.validator.createFailureDetail(failureDetailMessage, entryContext);
      }
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}