/*******************************************************************************
 * Copyright (c) 2002, 2009 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.core.profile.validator.impl.envelope;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.wsdl.Binding;
import javax.wsdl.BindingOperation;
import javax.wsdl.Message;
import javax.wsdl.Part;
import javax.wsdl.Types;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.schema.Schema;
import javax.xml.namespace.QName;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionFailException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionNotApplicableException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionPassException;
import org.eclipse.wst.wsi.internal.core.log.MessageEntry;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.HTTPUtils;
import org.eclipse.wst.wsi.internal.core.util.OperationSignature;
import org.eclipse.wst.wsi.internal.core.util.TypesRegistry;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * BP1204.
 * The serialized array form in the message does not contain the 
 * soapenc:arrayType attribute.
 * 
 */
public class BP1204 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP1204(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
  }

  /**
   * @see org.eclipse.wst.wsi.test.profile.validator.impl.BaseValidatorImpl.AssertionProcess#validate(TestAssertion, EntryContext)
   */
  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    try
    {

      if (this.validator.isOneWayResponse(entryContext))
        throw new AssertionNotApplicableException();

      // Parse message
      Document doc =
        XMLUtils.parseXML(entryContext.getMessageEntry().getMessage());

      if (this.validator.isFault(doc))
      {
        throw new AssertionPassException();
      }

      // Parse request message
      Document docRequest =
        XMLUtils.parseXML(entryContext.getRequest().getMessage());

      // get SOAPAction
      String headers = entryContext.getRequest().getHTTPHeaders();
      String action = null;
      if (headers != null)
        action = (String) HTTPUtils.getHttpHeaderTokens(headers, ":").get("SOAPAction".toUpperCase());

      Binding binding = validator.analyzerContext.getCandidateInfo().getBindings()[0];
      TypesRegistry registry =
        new TypesRegistry(
          this.validator.getWSDLDocument().getDefinitions(),
          validator);
      OperationSignature.OperationMatch match =
        OperationSignature.matchOperation(
          docRequest,
          action,
          binding,
          registry);

      if (match == null)
        throw new AssertionPassException();

      BindingOperation bindingOperation = match.getOperation();

      Message operationMessage = null;
      if (MessageEntry
        .TYPE_REQUEST
        .equals(entryContext.getMessageEntry().getType())
        && (bindingOperation.getOperation().getInput() != null))
      {
        operationMessage =
          bindingOperation.getOperation().getInput().getMessage();
      }
      else
      {
        if (MessageEntry
          .TYPE_RESPONSE
          .equals(entryContext.getMessageEntry().getType())
          && (bindingOperation.getOperation().getOutput() != null))
        {
          operationMessage =
            bindingOperation.getOperation().getOutput().getMessage();
        }
      }

      if (operationMessage == null)
        throw new AssertionPassException();

      boolean isContainArray = false;

      Collection parts = operationMessage.getParts().values();
      for (Iterator iter = parts.iterator(); iter.hasNext();)
      {
        Part part = (Part) iter.next();

        QName type = null;

        if (part.getTypeName() == null)
        {
          type = registry.getType(part.getElementName());
        }
        else
        {
          type = part.getTypeName();
        }

        isContainArray =
          registry.isExtendsArray(type)
            || registry.isUsesWSDLArrayType(type)
            || isArrayType(type);
        if (isContainArray)
          break;
      }

      if (isContainArray)
      {
        // Gets body
        NodeList soapBodyList =
          doc.getElementsByTagNameNS(
            WSIConstants.NS_URI_SOAP,
            XMLUtils.SOAP_ELEM_BODY);
        if (soapBodyList.getLength() == 0 || soapBodyList.getLength() > 1)
        {
          throw new AssertionFailException();
        }

        Element soapBodyElem = (Element) soapBodyList.item(0);

        NodeList soapBodyCildrenList =
          soapBodyElem.getElementsByTagNameNS("*", "*");
        for (int indexChild = 0;
          indexChild < soapBodyCildrenList.getLength();
          indexChild++)
        {
          Element elem = (Element) soapBodyCildrenList.item(indexChild);
          if (elem
            .hasAttributeNS(
              WSIConstants.NS_URI_SOAP_ENCODING,
              WSIConstants.ATTR_ARRAY_TYPE))
          {
            throw new AssertionFailException();
          }
        }

        throw new AssertionPassException();
      }
      else
      {
        throw new AssertionPassException();
      }

    }
    catch (AssertionNotApplicableException e)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    catch (AssertionFailException e)
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetail =
        this.validator.createFailureDetail(
          entryContext.getMessageEntry().getMessage(),
          entryContext);
    }
    catch (AssertionPassException e)
    {
      result = AssertionResult.RESULT_PASSED;
    }
    catch (Exception e)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }

  /**
   * Checks whether type uses an array or not.
   * @param type
   * @return boolean
   */
  private boolean isArrayType(QName type)
  {
    boolean arrayType = false;

    Element typeElement = getTypeElement(type);
    if (typeElement != null)
    {
      NodeList list =
        typeElement.getElementsByTagNameNS(
          WSIConstants.NS_URI_XSD,
          "sequence");
      if (list.getLength() > 0)
        arrayType = true;
    }

    return arrayType;
  }

  /**
   * Gets XML element defining the type.
   * @param type
   * @return Element
   */
  private Element getTypeElement(QName type)
  {
    Types[] allTypes = validator.analyzerContext.getCandidateInfo().getTypes();
    for (int i = 0; allTypes != null && i < allTypes.length; i++)
    {
      Element typesElement = null;
      if (allTypes[i] != null)
      {
        typesElement = allTypes[i].getDocumentationElement();
        if (typesElement == null)
        {
          List extList = allTypes[i].getExtensibilityElements();
          for (Iterator iter = extList.iterator(); iter.hasNext();)
          {
            ExtensibilityElement extElem = (ExtensibilityElement) iter.next();

            if (extElem
              .getElementType()
              .equals(new QName(WSIConstants.NS_URI_XSD, "schema")))
            {
              typesElement =
                ((Schema) extElem).getElement();
              break;
            }
          }
        }
        if (typesElement != null)
        {
          NodeList complexTypesList =
            typesElement.getElementsByTagNameNS(
              WSIConstants.NS_URI_XSD,
              "complexType");
          for (int j = 0; j < complexTypesList.getLength(); j++)
          {
            Element typeElem = (Element) complexTypesList.item(j);
            if (type.getLocalPart().equals(typeElem.getAttribute("name")))
            {
              return typeElem;
            }
          }

        }
      }
    }
    return null;
  }
}