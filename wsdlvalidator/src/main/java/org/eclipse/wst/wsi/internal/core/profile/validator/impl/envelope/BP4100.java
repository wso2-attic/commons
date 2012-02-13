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

import java.util.Iterator;
import java.util.List;

import javax.wsdl.Binding;
import javax.wsdl.BindingInput;
import javax.wsdl.BindingOperation;
import javax.wsdl.BindingOutput;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.soap.SOAPHeader;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionNotApplicableException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionPassException;
import org.eclipse.wst.wsi.internal.core.log.MessageEntry;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



/**
 * BP4100
 * <context>For a candidate envelope containing a header block that is either mandatory or is not described in the wsdl:binding.</context>
 * <assertionDescription>An envelope contains a header block that is either mandatory or is not described in the wsdl:binding.</assertionDescription>
 */
public class BP4100 extends AssertionProcess {

  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP4100(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
  }

  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    try
    {
      // Parsing the message
      Document doc = entryContext.getMessageEntryDocument();

      // If the message is empty or invalid, the assertion is not applicable
      if (doc == null)
      {
        throw new AssertionNotApplicableException();
      }

      // Getting header elements from envelope
      Element root = doc.getDocumentElement();
      NodeList headerList = root.getElementsByTagNameNS(
        WSIConstants.NS_URI_SOAP, XMLUtils.SOAP_ELEM_HEADER);

      // If there is no header, the assertion is not applicable
      if (headerList == null || headerList.getLength() == 0)
      {
        throw new AssertionNotApplicableException();
      }

      // Getting the header element
      Node header = headerList.item(0);

      // Getting the immediate child elements of the header
      NodeList elems = header.getChildNodes();

      // If there are no child elements of the header
      // the assertion is not applicable
      if (elems == null || elems.getLength() == 0)
      {
        throw new AssertionNotApplicableException();
      }

      // Walking through child elements
      for (int i = 0; i < elems.getLength(); i++)
      {

        if (elems.item(i).hasAttributes())
        {
          // Getting the mustUnderstand attribute
          Node muNode = elems.item(i).getAttributes().getNamedItem(
            root.getPrefix() + ":" + XMLUtils.SOAP_ATTR_MUST_UNDERSTAND);
          // If a header block is mandatory, then the assertion passed
          if (muNode != null && muNode.getNodeValue().equals("1"))
          {
            throw new AssertionPassException();
          }
        }

        // Getting header block name
        String blockName = elems.item(i).getLocalName();
        // If the name is not presented (occurs when element is empty string)
        // then continue with the next element
        if (blockName == null)
        {
          continue;
        }

        boolean blockNameExists = false;

        // Getting WSDL bindings
        Binding[] bindings = validator.getWSDLDocument().getBindings();
        for (int j = 0; j < bindings.length; j++) 
        {
          // Getting wsdl:operations
          List operations = bindings[j].getBindingOperations();
          Iterator k = operations.iterator();
          while (k.hasNext() && !blockNameExists)
          {
            BindingOperation operation = (BindingOperation) k.next();

            // If this is a request message,
            // then getting wsdl:input for an operation 
            if (entryContext.getMessageEntry().getType().equals(
              MessageEntry.TYPE_REQUEST))
            {
              BindingInput opInput = operation.getBindingInput();
              if (opInput != null)
              {
                // If wsdl:input is presented then checking
                // whether the block name is described there or not
                blockNameExists = blockNameExists(
                  opInput.getExtensibilityElements(), blockName);
              }
            }
            // If this is a response message,
            // then getting wsdl:output for an operation
            else
            {
              BindingOutput opOutput = operation.getBindingOutput();
              if (opOutput != null)
              {
                // If wsdl:output is presented then checking
                // whether the block name is described there or not
                blockNameExists = blockNameExists(
                  opOutput.getExtensibilityElements(), blockName);
              }
            }
          }
        }

        // If a header block is not described in the appropriate wsdl:binding
        // then the assertion passed
        if (!blockNameExists)
        {
          throw new AssertionPassException();
        }
      }

      // No one header block is either mandatory or is not described in the
      // appropriate wsdl:binding, the assertion is not applicable
      result = AssertionResult.RESULT_NOT_APPLICABLE;

    }
    catch (AssertionPassException ape)
    {
      failureDetail = validator.createFailureDetail(
        testAssertion.getDetailDescription(), entryContext);
    }
    catch (AssertionNotApplicableException anae)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }

    return validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }

  /**
   * Checks the existense of blockName in wsdlsoap:header, if it is found in
   * a list of ExtensibilityElement objects
   * 
   * @param elems A list of ExtensibilityElement objects
   * @param blockName The name that will be checked for existence
   * @return true if blockName is described in the wsdlsoap:header extensibility element
   */
  private boolean blockNameExists(List elems, String blockName)
  {
    if (elems == null)
    {
      return false;
    }

    Iterator i = elems.iterator();
    while (i.hasNext())
    {
      ExtensibilityElement elem = (ExtensibilityElement) i.next();
      String elemName = elem.getElementType().getLocalPart();
      if (elemName.equals("header"))
      {
        SOAPHeader soapHeader = (SOAPHeader) elem;
        if (soapHeader.getPart().equals(blockName))
        {
          return true;
        }
      }
    }

    return false;
  }
}