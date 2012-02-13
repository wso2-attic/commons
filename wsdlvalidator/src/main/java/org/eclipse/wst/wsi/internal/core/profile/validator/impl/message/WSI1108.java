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
package org.eclipse.wst.wsi.internal.core.profile.validator.impl.message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.wsdl.BindingOperation;
import javax.wsdl.Definition;
import javax.wsdl.Message;
import javax.wsdl.Part;
import javax.wsdl.extensions.soap.SOAPBody;
import javax.wsdl.extensions.soap.SOAPFault;
import javax.wsdl.extensions.soap.SOAPHeader;
import javax.wsdl.extensions.soap.SOAPHeaderFault;
import javax.xml.namespace.QName;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.WSITag;
import org.eclipse.wst.wsi.internal.core.log.MessageEntry;
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
 * BPWSI4.
 * Each soapbind extension element with a wsdl:required 
 * attribute of false must appear in a message.
 */
public class WSI1108 extends AssertionProcessVisitor implements WSITag
{

  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public WSI1108(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
  }

  private OperationSignature.OperationMatch match = null;
  private EntryContext entryContext = null;
  private OperationSignature responseSig = null;
  private Vector headerRarts = null;
  private boolean visitFault = false;
  private boolean existSOAPHeader = true;
  private boolean isRequest = false;
  private boolean existSOAPHeaderFault = true;

  /**
   * @see org.eclipse.wst.wsi.wsdl.traversal.WSDLVisitor#visit(SOAPBody, Object, WSDLTraversalContext)
   */
  public void visit(SOAPBody body, Object parent, WSDLTraversalContext ctx)
  {

    // if "wsdl:required" is true then return
    if (body.getRequired() != null
      && body.getRequired().booleanValue() == true)
      return;

    // assert parent instanceof BindingOutput
    BindingOperation bop = ctx.getBindingOperation();
    if (bop.getOperation() == null
      || bop.getOperation().getOutput() == null
      || bop.getOperation().getOutput().getMessage() == null)
      return;
    Message msg = bop.getOperation().getOutput().getMessage();
    List parts =
      WSDLUtil.getParts(
        bop.getOperation(),
        msg,
        body,
        match.getOperationStyle());

    QName additionalName = null;
    // if operation is rpc, add to parts qname qith function name
    if (WSIConstants
      .ATTRVAL_SOAP_BIND_STYLE_RPC
      .equals(match.getOperationStyle()))
    {
      // found out target namespace
      String namespace = body.getNamespaceURI();
      if (namespace == null || namespace.length() == 0)
        // !! ATTENTION
        // namespace should be retrieved from service target nameapce            
        namespace = ctx.getBinding().getQName().getNamespaceURI();

      // insert operation name as first signature part
      additionalName =
        new QName(
          namespace,
          bop
            .getOperation()
            .getOutput()
            .getMessage()
            .getQName()
            .getLocalPart());
    }

    // create the signature of this operation
    OperationSignature op =
      new OperationSignature(
        parts,
        null,
        new TypesRegistry(
            validator.analyzerContext
            .getCandidateInfo()
            .getWsdlDocument()
            .getDefinitions(),
            validator),
        false);
    if (additionalName != null)
      op.getSignature().add(0, additionalName);

    if (op.getSignature().equals(responseSig.getSignature()))
    {
      //            ctx.cancelBindingOperationProcessing();
    }
    else
    {
      // create the signature of this operation
      op =
        new OperationSignature(
          parts,
          null,
          new TypesRegistry(
              validator.analyzerContext
              .getCandidateInfo()
              .getWsdlDocument()
              .getDefinitions(),
              validator),
          true);

      if (additionalName != null)
        op.getSignature().add(0, additionalName);
      if (op.getSignature().equals(responseSig.getSignature()))
      {
        //              ctx.cancelBindingOperationProcessing();
      }
      else
      {
        result = AssertionResult.RESULT_FAILED;
        failureDetail =
          this.validator.createFailureDetail(
            "\nMessage:\n" + entryContext.getMessageEntry().getMessage(),
            entryContext);
        ctx.cancelBindingOperationProcessing();
      }
    }
  }

  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.extensions.soap.SOAPHeader, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(
    SOAPHeader header,
    Object parent,
    WSDLTraversalContext ctx)
  {

    if (existSOAPHeaderFault == false)
    {
      existSOAPHeader = false;
      ctx.cancelBindingOperationProcessing();
      return;
    }
    // if "wsdl:required" is true then return
    if (header.getRequired() != null
      && header.getRequired().booleanValue() == true)
      return;
    // find headr part
    Definition d = (Definition) ctx.getParameter("definition");
    TypesRegistry tReg = (TypesRegistry) ctx.getParameter("TypesRegistry");
    Part mesPart =
      WSDLUtil.getPart(d.getMessage(header.getMessage()), header.getPart());

    // test this part and parts from header        
    QName partQName = mesPart.getTypeName();
    if (partQName == null)
      partQName = tReg.getType(mesPart.getElementName());
    if (partQName == null)
      throw new IllegalArgumentException(
        "Part type can not be null." + mesPart.getElementName().toString());
    String local = partQName.getLocalPart();
    String ns = partQName.getNamespaceURI();

    existSOAPHeader = false;
    for (int i = 0; i < headerRarts.size(); i++)
    {
      Element elem = (Element) headerRarts.get(i);
      if (elem.getLocalName().equals(local)
        && NullUtil.equals(ns, elem.getNamespaceURI()))
      {
        existSOAPHeader = true;
        break;
      }
    }
    if (existSOAPHeader == true)
      ctx.cancelSOAPHeaderProcessing();
    if (isRequest == true)
      ctx.cancelBindingOperationProcessing();
    if (existSOAPHeader == false && isRequest == false)
      existSOAPHeaderFault = false;
  }

  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.extensions.soap.SOAPHeaderFault, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(
    SOAPHeaderFault headerFault,
    Object parent,
    WSDLTraversalContext ctx)
  {
    // find headr part
    Definition d = (Definition) ctx.getParameter("definition");
    TypesRegistry tReg = (TypesRegistry) ctx.getParameter("TypesRegistry");
    Part mesPart =
      WSDLUtil.getPart(
        d.getMessage(headerFault.getMessage()),
        headerFault.getPart());

    // test this part and parts from headerfault
    QName partQName = mesPart.getTypeName();
    if (partQName == null)
      partQName = tReg.getType(mesPart.getElementName());
    if (partQName == null)
      throw new IllegalArgumentException(
        "Part type can not be null." + mesPart.getElementName().toString());
    String local = partQName.getLocalPart();
    String ns = partQName.getNamespaceURI();

    for (int i = 0; i < headerRarts.size(); i++)
    {
      Element elem = (Element) headerRarts.get(i);
      if (elem.getLocalName().equals(local)
        && NullUtil.equals(ns, elem.getNamespaceURI()))
      {
        existSOAPHeaderFault = true;
        break;
      }
    }
    if (existSOAPHeaderFault == true)
      ctx.cancelSOAPHeaderProcessing();
  }

  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.extensions.soap.SOAPFault, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(SOAPFault fault, Object parent, WSDLTraversalContext ctx)
  {
    // set in true if any SOAPFault exist
    visitFault = true;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.validator.impl.BaseValidatorImpl.AssertionProcess#validate(org.wsi.test.profile.TestAssertion, org.wsi.test.profile.validator.EntryContext)
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
      this.entryContext = entryContext;
      // Parse request message
      Document doc = entryContext.getRequestDocument();
      // create request signature
      match =
        OperationSignature.matchOperation(
          doc,
          null,
          validator.analyzerContext.getCandidateInfo().getBindings()[0],
          new TypesRegistry(
              validator.analyzerContext
              .getCandidateInfo()
              .getWsdlDocument()
              .getDefinitions(),
              validator),
          false);

      // if such operation exist      
      if (match != null)
      {
        isRequest =
          MessageEntry.TYPE_REQUEST.equals(
            entryContext.getMessageEntry().getType());
        // Parse message
        doc = entryContext.getMessageEntryDocument();

        if (!isRequest)
        {
          responseSig = new OperationSignature(doc);
          if (WSIConstants
            .ATTRVAL_SOAP_BIND_STYLE_RPC
            .equals(match.getOperationStyle()))
            responseSig.createRPCSignature();
        }
        // extract all headers from message
        Element headerElem =
          XMLUtils.findChildElement(
            doc.getDocumentElement(),
            WSITag.ELEM_SOAP_HEADER);
        headerRarts = new Vector();
        if (headerElem != null)
          headerRarts = XMLUtils.getChildElements(headerElem);

        WSDLTraversal traversal = new WSDLTraversal();
        //VisitorAdaptor.adapt(this);
        traversal.setVisitor(this);
        traversal.visitSOAPBody(true);
        traversal.visitSOAPHeader(true);
        traversal.visitSOAPHeaderFault(true);
        traversal.visitSOAPFault(true);

        traversal.ignoreReferences();
        traversal.ignoreBindingInput2SOAPBody();

        if (isRequest)
          traversal.ignoreBindingOutput();
        else
          traversal.ignoreBindingInput2SOAPHeader();

        if (responseSig != null && responseSig.isFault())
          traversal.ignoreBindingOutput();
        else
          traversal.ignoreBindingFault();

        existSOAPHeaderFault = true;
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

        if (isRequest == false)
          if (existSOAPHeaderFault == true)
            existSOAPHeader = true;

        if (responseSig != null
          && responseSig.isFault()
          && visitFault == false)
        {
          result = AssertionResult.RESULT_FAILED;
          failureDetail =
            this.validator.createFailureDetail(
              "\nMessage:\n" + entryContext.getMessageEntry().getMessage(),
              entryContext);
        }
        if (existSOAPHeader == false)
        {
          result = AssertionResult.RESULT_FAILED;
          failureDetail =
            this.validator.createFailureDetail(
              "\nMessage:\n" + entryContext.getMessageEntry().getMessage(),
              entryContext);
        }
      }
      else
      {
        result = AssertionResult.RESULT_NOT_APPLICABLE;
      }
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}