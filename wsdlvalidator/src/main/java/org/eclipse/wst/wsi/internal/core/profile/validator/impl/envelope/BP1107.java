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

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.wsdl.Fault;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Part;
import javax.wsdl.extensions.soap.SOAPFault;
import javax.xml.namespace.QName;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.WSITag;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcessVisitor;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.NullUtil;
import org.eclipse.wst.wsi.internal.core.util.OperationSignature;
import org.eclipse.wst.wsi.internal.core.util.TypesRegistry;
import org.eclipse.wst.wsi.internal.core.util.WSDLUtil;
import org.eclipse.wst.wsi.internal.core.wsdl.traversal.WSDLTraversal;
import org.eclipse.wst.wsi.internal.core.wsdl.traversal.WSDLTraversalContext;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * BP1107.
 * The contained soapbind:fault is defined in the wsdl:binding.
 */
public class BP1107 extends AssertionProcessVisitor
{

  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP1107(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
  }

  private OperationSignature responseSig = null;
  private Vector faults = null;

  /**
   * @see org.eclipse.wst.wsi.wsdl.traversal.WSDLVisitor#visit(SOAPFault, Object, WSDLTraversalContext)
   */
  public void visit(SOAPFault fault, Object parent, WSDLTraversalContext ctx)
  {
    String faultName = fault.getName();

    if (faultName == null)
      faultName = ctx.getBindingFault().getName();

    Operation op = ctx.getBindingOperation().getOperation();
    if (op == null /* || faultName == null*/
      ) // may be it's possible to have legal fault with null name
      return;

    // we suppose that SOAPFault.getName() corresponds to the abstract operation's fault name           
    Fault f = op.getFault(faultName);
    if (f == null)
      return;

    Message m = f.getMessage();
    // message should have only one part
    if (m == null || m.getParts() == null || m.getParts().size() != 1)
      return;

    //Part faultPart = WSDLUtil.getPart(m, faultName);
    Part faultPart = (Part) m.getParts().values().iterator().next();

    TypesRegistry tReg = (TypesRegistry) ctx.getParameter("TypesRegistry");
    QName elemQName = faultPart.getElementName();
    QName typeQName = faultPart.getTypeName();
    if (typeQName == null)
      typeQName = tReg.getType(faultPart.getElementName());
    if (typeQName == null)
      throw new IllegalArgumentException("Part type can not be null.");

    // for all faults; if it presents in the definition remove it from list
    for (int i = 0; i < faults.size();)
    {
      Element elem = (Element) faults.get(i);

      // TODO: I don't understand why this is here.  Only the element setting should be checked. 
      //boolean matchByType =
      //  elem.getLocalName().equals(typeQName.getLocalPart()) && NullUtil.equals(elem.getNamespaceURI(), typeQName.getNamespaceURI());

      boolean matchByElement =
        elemQName != null
          && elem.getLocalName().equals(elemQName.getLocalPart())
          && NullUtil.equals(
            elem.getNamespaceURI(),
            elemQName.getNamespaceURI());

      //if (matchByType || matchByElement)
      if (matchByElement)
        faults.remove(i);
      else
        i++;
    }
  }

  /**
   * @see org.eclipse.wst.wsi.test.profile.validator.impl.BaseValidatorImpl.AssertionProcess#validate(TestAssertion, EntryContext)
   */
  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {

    if (this.validator.isOneWayResponse(entryContext))
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    else
    {
      // Parse request and response message
      Document docRequest = entryContext.getRequestDocument();
      Document docResponse = entryContext.getMessageEntryDocument();

      if ((docRequest == null) || (docResponse == null))
      {
        result = AssertionResult.RESULT_NOT_APPLICABLE;
      }
      else if (!this.validator.isFault(docResponse))
      {
        result = AssertionResult.RESULT_NOT_APPLICABLE;
      }
      else
      {
        // create request signature
        OperationSignature.OperationMatch match =
          OperationSignature.matchOperation(
            docRequest,
            this.validator.getSoapAction(entryContext.getRequest().getHTTPHeaders()),
            validator.analyzerContext.getCandidateInfo().getBindings()[0],
            new TypesRegistry(
                validator.analyzerContext
                .getCandidateInfo()
                .getWsdlDocument()
                .getDefinitions(),
                validator));

        result = AssertionResult.RESULT_NOT_APPLICABLE;

        if (match != null)
        {
          // MOVED: Parse response message
          //doc = entryContext.getMessageEntryDocument();

          responseSig = new OperationSignature(docResponse);
          if (WSIConstants
            .ATTRVAL_SOAP_BIND_STYLE_RPC
            .equals(match.getOperationStyle()))
            responseSig.createRPCSignature();

          WSDLTraversal traversal = new WSDLTraversal();
          //VisitorAdaptor.adapt(this);
          traversal.setVisitor(this);
          traversal.visitSOAPFault(true);
          traversal.ignoreBindingInput();
          traversal.ignoreBindingOutput();

          if (responseSig == null || !responseSig.isFault())
          {
            result = AssertionResult.RESULT_NOT_APPLICABLE;
          }
          else if (responseSig != null && responseSig.isFault())
          {
            // extract all faults and try find them in the definition
            // extracts only faults with namespace
            Element body =
              XMLUtils.findChildElement(
                docResponse.getDocumentElement(),
                WSITag.ELEM_SOAP_BODY);
            Element fault =
              XMLUtils.findChildElement(body, WSITag.ELEM_SOAP_FAULT);
            Element detail = XMLUtils.getElement("detail", fault);
            if (detail == null)
            {
              result = AssertionResult.RESULT_NOT_APPLICABLE;
            }
            else
            {
              result = AssertionResult.RESULT_PASSED;

              faults = XMLUtils.getChildElements(detail);

              // REMOVE: Why do the faults have to be namespaced qualified?
              //XMLUtils.removeAllElementsWithoutNS(faults);

              // if faults exist try to validate it
              if (faults.size() > 0)
              {
                Map m = new HashMap();
                WSDLUtil.expandDefinition(
                    validator.analyzerContext
                    .getCandidateInfo()
                    .getWsdlDocument()
                    .getDefinitions());
                m.put(
                  "definition",
                  validator.analyzerContext
                    .getCandidateInfo()
                    .getWsdlDocument()
                    .getDefinitions());
                TypesRegistry tReg =
                  new TypesRegistry(
                      validator.analyzerContext
                      .getCandidateInfo()
                      .getWsdlDocument()
                      .getDefinitions(),
                      validator);
                m.put("TypesRegistry", tReg);
                traversal.traverse(match.getOperation(), m);
                if (faults.size() > 0)
                {
                  result = AssertionResult.RESULT_WARNING;
                  StringWriter sw = new StringWriter();
                  for (int i = 0; i < faults.size(); i++)
                  {
                    try
                    {
                      XMLUtils.serializeElement((Element) faults.get(i), sw);
                    }
                    catch (Exception e)
                    {
                    }
                  }
                  try
                  {
                    sw.close();
                  }
                  catch (Exception e)
                  {
                  }
                  failureDetail =
                    this.validator.createFailureDetail(
                      "\nFaults:\n" + sw.toString(),
                      entryContext);
                }
                else
                  result = AssertionResult.RESULT_PASSED;
              }
            }
          }
        }
      }
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}