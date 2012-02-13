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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.wsdl.BindingOperation;
import javax.wsdl.Message;
import javax.wsdl.Part;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.UnknownExtensibilityElement;
import javax.wsdl.extensions.mime.MIMEMultipartRelated;
import javax.wsdl.extensions.mime.MIMEPart;
import javax.wsdl.extensions.soap.SOAPBody;
import javax.wsdl.extensions.soap.SOAPHeader;
import javax.xml.namespace.QName;

import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSModelGroup;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSParticle;
import org.apache.xerces.xs.XSTerm;
import org.apache.xerces.xs.XSTypeDefinition;
import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.WSITag;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionFailException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionNotApplicableException;
import org.eclipse.wst.wsi.internal.core.log.MessageEntry;
import org.eclipse.wst.wsi.internal.core.log.MimePart;
import org.eclipse.wst.wsi.internal.core.log.MimeParts;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.MIMEConstants;
import org.eclipse.wst.wsi.internal.core.util.MIMEUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * AP1928
 *
 * <context>For a candidate envelope containing a URI reference
 * that is typed using the ref:swaRef schema type</context>
 * <assertionDescription>In an envelope, a URI reference that is typed using
 * the ref:swaRef schema type resolves to a MIME part in the same message
 * as the envelope.</assertionDescription>
 */
public class AP1928 extends AssertionProcess implements WSITag
{
  private final BaseMessageValidator validator;

  /**
   * @param WSDLValidatorImpl
   */
  public AP1928(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
  }

  /* Validates the test assertion.
  * @see org.wsi.test.profile.validator.impl.BaseValidatorImpl.AssertionProcess#validate(org.wsi.test.profile.TestAssertion, org.wsi.test.profile.validator.EntryContext)
  */
  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    try
    {
      if (!entryContext.getMessageEntry().isMimeContent())
      {
        throw new AssertionNotApplicableException();
      }
      // get the rrot part of a multipart/related message
      Document root = entryContext.getMessageEntryDocument();
      MimeParts mimeParts = entryContext.getMessageEntry().getMimeParts();

      // get an operation matched for the message
      BindingOperation bindingOperation = validator.getOperationMatch(
        entryContext.getEntry().getEntryType(), root);
 
      // if no one operation matches, the assertion is not applicable
      if (bindingOperation == null)
        throw new AssertionNotApplicableException();

      // get the corresponding extensibility elements and message
      List extElems;
      Message message;
      if (MessageEntry.TYPE_REQUEST
        .equals(entryContext.getEntry().getEntryType()))
      {
        extElems = bindingOperation.getBindingInput() == null ? null
          : bindingOperation.getBindingInput().getExtensibilityElements();
        message = bindingOperation.getOperation().getInput() == null ? null
          : bindingOperation.getOperation().getInput().getMessage();
      }
      else
      {
        extElems = bindingOperation.getBindingOutput() == null ? null
          : bindingOperation.getBindingOutput().getExtensibilityElements();
        message = bindingOperation.getOperation().getOutput() == null ? null
          : bindingOperation.getOperation().getOutput().getMessage();
      }

      // If there are no extensibility elements in the WSDL binding operation,
      // the assertion is not applicable
      if (extElems == null || extElems.size() < 1)
        throw new AssertionNotApplicableException();

      // Collecting the names of schema elements that are defined
      // with the ref:swaRef schema type
      List swaRefs = getSwaRefs((ExtensibilityElement)extElems.get(0), message);
      // If such schema elements are not found, the assertion is not applicable
      if (swaRefs.isEmpty())
        throw new AssertionNotApplicableException();

      // Going through all the schema element names
      for (int i = 0; i < swaRefs.size(); i++)
      {
        QName elemName = (QName) swaRefs.get(i);
        // Looking for such elements in the root MIME part
        NodeList elems = root.getDocumentElement().getElementsByTagNameNS(
            elemName.getNamespaceURI(), elemName.getLocalPart());
        // Going through all the ref:swaRef references
        for (int j = 0; j < elems.getLength(); j++)
        {
          Node elem = elems.item(j);
          // Getting a value of this reference
          String ref = elem.getFirstChild() == null ? null
            : elem.getFirstChild().getNodeValue();
          // If the value is invalid, the assertion failed
          if (ref == null)
            throw new AssertionFailException("The element " + elem.getNodeName()
              + " of the ref:swaRef schema type is specified, but its vaule"
              + " is invalid.");
          // If the URI does not start with the "cid:" content-id prefix
          // The assertion failed
          if (!ref.startsWith("cid:"))
          {
            throw new AssertionFailException("The URI " + ref
              + " of the ref:swaRef schema type must use the cid: prefix.");
          }
          boolean refFound = false;
          Iterator iMimeParts = mimeParts.getParts().iterator();
          // Going through all the MIME parts of the SOAP message
          while (iMimeParts.hasNext())
          {
            MimePart mimePart = (MimePart)iMimeParts.next();
            // Getting part's Content-ID header
            String cid = MIMEUtils.getMimeHeaderAttribute(
                mimePart.getHeaders(), MIMEConstants.HEADER_CONTENT_ID);
            if (cid != null)
            {
              // If the header value equals the reference URI,
              // the corresponding MIME part is found
              if (cid.equals('<' + ref.substring(4) + '>'))
              {
                refFound = true;
                break;
              }
            }
          }
          // If the corresponding MIME part is not found
          // the assertion failed
          if (!refFound)
            throw new AssertionFailException("The SOAP message does not contain"
              + " a MIME part with the <" + ref.substring(4) + "> Content-ID.");
        }
      }
    }
    catch (AssertionNotApplicableException anae)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    catch (AssertionFailException afe)
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetail = validator.createFailureDetail(
        afe.getMessage(), entryContext);
    }
    // Return assertion result
    return validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }

  /**
   * Collects names of schema elements that are defined with the ref:swaRef
   * schema type.
   * @param extElem a list of extensibility elements
   * @param message the corresponding message element.
   * @return a list of such names found.
   */
  private List getSwaRefs(ExtensibilityElement extElem, Message message)
  {
    List swaRefs = new ArrayList();
    if (extElem.getElementType().equals(WSDL_MIME_MULTIPART))
    {
      // Getting the root mime:part
      List mimeParts = ((MIMEMultipartRelated) extElem).getMIMEParts();
      if (mimeParts.size() > 0)
      {
        // Getting extensibility elements of the first mime:part
        List elems = ((MIMEPart) mimeParts.get(0)).getExtensibilityElements();
        List boundParts = new ArrayList();
        // Going through all the MIME part's extensibility elements
        for (int k = 0; k < elems.size(); k ++)
        {
          ExtensibilityElement elem = (ExtensibilityElement) elems.get(k);
          // If that is a soap:body
          if (elem.getElementType().equals(WSDL_SOAP_BODY) && message != null)
          {
            // Getting parts specified by the parts attribute
            List bodyParts = ((SOAPBody) elem).getParts();
            // Collecting all the message parts bound by this soapbind:body
            Iterator i = message.getParts().values().iterator();
            while (i.hasNext())
            {
              Part part = (Part) i.next();
              if (bodyParts == null || bodyParts.contains(part.getName()))
              {
                boundParts.add(part);
              }
            }
          }
          // else if that is a soap:header
          else if (elem.getElementType().equals(WSDL_SOAP_HEADER))
          {
            QName msgName;
            String partName;
            if (elem instanceof SOAPHeader)
            {
              SOAPHeader header = (SOAPHeader) elem;
              // If the part is bound by this element
              msgName = header.getMessage();
              partName = header.getPart();
            }
            // WSDL4J 1.4 does not recognize soap:header elements that
            // are enclosed in mime:multipartRelated, so using a workaround
            else
            {
              Element header =
                ((UnknownExtensibilityElement) elem).getElement();
              // Getting the element's message attribute
              String msgVal = header.getAttribute("message");
              // finding the colon delimiter
              int colonPos = msgVal.indexOf(":");
              String ns = null;
              // Getting a local part
              String lp = colonPos > -1 ? msgVal.substring(colonPos + 1) : msgVal;
              // If the delimiter is found
              if (colonPos > -1)
              {
                // Retrieving a namespace URI
                ns = validator.getWSDLDocument()
                .getDefinitions().getNamespace(msgVal.substring(0, colonPos));
              }
              msgName = new QName(ns, lp);
              partName = header.getAttribute("part");
            }
            // Getting a message referenced by this soapbind:header element
            Message msg = 
              validator.getWSDLDocument().getDefinitions().getMessage(msgName);
            if (msg != null)
            {
              // Adding the part to the list of bound parts
              Part part = msg.getPart(partName);
              if (part != null)
                boundParts.add(part);
            }
          }
        }

        // All the bound parts from soapbind:body and soapbind:header
        // are collected, finding swaRef references.
        for (int i = 0; i < boundParts.size(); i++)
        {
          Part part = (Part) boundParts.get(i);
          QName ref;
          short type;
          // Getting either part's element reference or type reference
          if ((ref = part.getTypeName()) != null)
          {
            type = XSConstants.TYPE_DEFINITION;
          }
          else if ((ref = part.getElementName()) != null)
          {
            type = XSConstants.ELEMENT_DECLARATION;
          }
          // The part conatins neither element nor type attribute,
          // proceeding with the next part
          else
          {
            continue;
          }
          // Getting a list of schemas defined
          Map schemas = validator.getWSDLDocument().getSchemas();
          // Going through the schemas
          Iterator it = schemas.values().iterator();
          while (it.hasNext())
          {
            XSModel xsModel = (XSModel) it.next();
            XSTypeDefinition xsType = null;
            // Getting the corresponding part type
            if (type == XSConstants.ELEMENT_DECLARATION)
            {
              // Getting schema element
              XSElementDeclaration elem = xsModel.getElementDeclaration(
                ref.getLocalPart(), ref.getNamespaceURI());
              if (elem != null)
              {
                // Getting element's type
                xsType = elem.getTypeDefinition();
                // If it is ref:swaRef
                if (WSIConstants.NS_URI_SWA_REF.equals(xsType.getName())
                  && WSIConstants.SCHEMA_TYPE_SWA_REF.equals(
                    xsType.getNamespace()))
                {
                  // Adding the name of the element to the list
                  swaRefs.add(
                    new QName(elem.getNamespace(), elem.getName()));
                }
              }
            }
            else
            {
              xsType = xsModel.getTypeDefinition(
                ref.getLocalPart(), ref.getNamespaceURI());
            }
            // Collecting all the element names,adding element names to the list
            swaRefs.addAll(collectSwaRefs(xsType));
          }
        }
      }
    }
    return swaRefs;
  }

  /**
   * Collects names of schema elements that are defined with the ref:swaRef
   * schema type.
   * @param xsType a schema type.
   * @return a list of names found.
   */
  private List collectSwaRefs(XSTypeDefinition xsType)
  {
    List swaRefs = new ArrayList();
    if (xsType != null)
    {
      // If this is a complex type
      if (xsType.getTypeCategory() == XSTypeDefinition.COMPLEX_TYPE)
      {
        XSComplexTypeDefinition xsComplexType =
          (XSComplexTypeDefinition) xsType;
        // If it contains mixed context
        if (xsComplexType.getParticle() != null)
        {
          // collecting element names 
          swaRefs.addAll(collectSwaRefs(xsComplexType.getParticle().getTerm()));
        }
      }
    }
    return swaRefs;
  }

  /**
   * Collects names of schema elements that are defined with the ref:swaRef
   * schema type.
   * @param term a schema term.
   * @return a list of names found.
   */
  private List collectSwaRefs(XSTerm term)
  {
    List swaRefs = new ArrayList(); 
    // If a term is an element declaration
    if (term.getType() == XSConstants.ELEMENT_DECLARATION)
    {
      XSElementDeclaration elem = (XSElementDeclaration) term;
      XSTypeDefinition xsType = elem.getTypeDefinition();
      // If element's type is ref:swaRef
      if (WSIConstants.NS_URI_SWA_REF.equals(xsType.getNamespace())
        && WSIConstants.SCHEMA_TYPE_SWA_REF.equals(xsType.getName()))
      {
        // Add element's name to the list
        swaRefs.add(
          new QName(elem.getNamespace(), elem.getName()));
      }
      else
      {
        // else collecting element names from element's type
        swaRefs.addAll(collectSwaRefs(xsType));
      }
    }
    // else if a term is a model group
    else if(term.getType() == XSConstants.MODEL_GROUP)
    {
      // Getting a list of Particle schema components
      XSObjectList list = ((XSModelGroup) term).getParticles();
      for (int i = 0; i < list.getLength(); i++)
      {
        // Collecting element names
        swaRefs.addAll(collectSwaRefs(((XSParticle) list.item(i)).getTerm()));
      }
    }
    return swaRefs;
  }
}