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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.wsdl.Binding;
import javax.wsdl.BindingInput;
import javax.wsdl.BindingOperation;
import javax.wsdl.BindingOutput;
import javax.wsdl.Input;
import javax.wsdl.Message;
import javax.wsdl.Output;
import javax.wsdl.Part;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.UnknownExtensibilityElement;
import javax.wsdl.extensions.mime.MIMEMultipartRelated;
import javax.wsdl.extensions.mime.MIMEPart;
import javax.wsdl.extensions.soap.SOAPBody;
import javax.wsdl.extensions.soap.SOAPHeader;
import javax.xml.namespace.QName;

import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.xs.XSAttributeUse;
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
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.w3c.dom.Element;

/**
 * AP2940
 *
 * <context>For a candidate wsdl:binding</context>
 * <assertionDescription>In a description, a wsdl:part defined with the
 * ref:swaRef schema type is bound to a soapbind:body, or a soapbind:header
 * in a MIME binding.</assertionDescription>
 */
public class AP2940 extends AssertionProcess implements WSITag
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public AP2940(WSDLValidatorImpl impl)
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
      // A variable that indicates wsdl:portType references at least one
      // wsdl:part defined with the ref:swaRef schema type
      boolean swaRefFound = false;

      // Getting a wsdl:binding
      Binding binding = (Binding) entryContext.getEntry().getEntryDetail();

      // Getting its wsdl:operation elements
      List ops = binding.getBindingOperations();

      // Going through the operation elements
      for (int i = 0; i < ops.size(); i++)
      {
        BindingOperation bindingOperation = (BindingOperation) ops.get(i);

        // Getting wsdl:input and wsdl:output elements of an operation
        BindingInput bindingInput = bindingOperation.getBindingInput();
        BindingOutput bindingOutput = bindingOperation.getBindingOutput();

        Input portTypeInput = bindingOperation.getOperation().getInput();
        Output portTypeOutput = bindingOperation.getOperation().getOutput();
        // If the corresponding wsdl:input exists in wsdl:portType
        // and includes the message attribute
        if (portTypeInput != null && portTypeInput.getMessage() != null)
        {
          // Collecting all the message's parts defined with ref:swaRef
          List swaRefParts = getSwaRefParts(portTypeInput.getMessage());
          if (!swaRefParts.isEmpty())
          {
            swaRefFound = true;
            // Getting a wsdl:part that is unbound
            String unboundPart = getUnboundPart(swaRefParts,
              portTypeInput.getMessage().getQName(),
              bindingInput == null ? null : bindingInput.getExtensibilityElements());
            // If such wsdl:part exists, assertion failed
            if (unboundPart != null)
              throw new AssertionFailException("The part \"" + unboundPart
                + "\" is not bound properly to the wsdl:input of the \""
                + bindingOperation.getName() + "\" binding operation.");
          }
        }

        // If the corresponding wsdl:output exists in wsdl:portType
        // and includes the message attribute
        if (portTypeOutput != null && portTypeOutput.getMessage() != null)
        {
          // Collecting all the message's parts defined with ref:swaRef
          List swaRefParts = getSwaRefParts(portTypeOutput.getMessage());
          if (!swaRefParts.isEmpty())
          {
            swaRefFound = true;
            // Getting a wsdl:part that is unbound
            String unboundPart = getUnboundPart(swaRefParts,
              portTypeOutput.getMessage().getQName(),
              bindingOutput == null ? null : bindingOutput.getExtensibilityElements());
            // If such wsdl:part exists, assertion failed
            if (unboundPart != null)
              throw new AssertionFailException("The part \"" + unboundPart
                + "\" is not bound properly to the wsdl:input of the \""
                + bindingOperation.getName() + "\" binding operation.");
          }
        }
      }

      // If there is no wsdl:partS defined with the ref:swaRef
      // schema type, the assertion is not applicable
      if (!swaRefFound)
        throw new AssertionNotApplicableException();
    }
    catch (AssertionNotApplicableException anae)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    catch (AssertionFailException afe)
    {
      // The assertion is "recommended", using the "warning" result
      result = AssertionResult.RESULT_WARNING;
      failureDetail = validator.createFailureDetail(
        afe.getMessage(), entryContext);
    }

    // Return assertion result
    return validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }

  /**
   * Looks for a wsdl:part that is not bound to either soap:body or soap:header
   * in a MIME binding.
   * @param swaRefParts a list of wsdl:part names that are defined with the
   * ref:swaRef schema type.
   * @param messageName the qualified name of the wsdl:message being validated.
   * @param extElems a list of extensibility elements of either wsdl:input or
   * wsdl:output of the corresponding binding operation.
   * @return a wsdl:part name that is not bound properly, or null.
   */
  private String getUnboundPart(List swaRefParts, QName messageName, List extElems) {
    // Going through wsdl:part names
    for (int i = 0; i < swaRefParts.size(); i++)
    {
      String part = (String) swaRefParts.get(i);
      boolean boundProperly = false;
      if (extElems != null)
      {
        // Going through extensibility elements
        for (int j = 0; j < extElems.size() && !boundProperly; j++)
        {
          ExtensibilityElement extElem = (ExtensibilityElement) extElems.get(j);
          // If the element is mime:multipartRelated
          if (extElem.getElementType().equals(WSDL_MIME_MULTIPART))
          {
            // Getting the mime:part elements of the mime:multipartRelated
            List mimeParts = ((MIMEMultipartRelated) extElem).getMIMEParts();
            // Getting the first mime:part element where soap:body
            // and soap:header could appear
            if (mimeParts.size() > 0)
            {
              List elems = ((MIMEPart) mimeParts.get(0)).getExtensibilityElements();
              // Going through all the MIME part's extensibility elements
              for (int k = 0; k < elems.size(); k ++)
              {
                ExtensibilityElement elem = (ExtensibilityElement) elems.get(k);
                // If that is a soap:body
                if (elem.getElementType().equals(WSDL_SOAP_BODY))
                {
                  List pts = ((SOAPBody) elem).getParts();
                  // If the part is bound by this element
                  if (pts == null || pts.contains(part))
                  {
                    boundProperly = true;
                    break;
                  }
                }
                // else if that is a soap:header
                else if (elem.getElementType().equals(WSDL_SOAP_HEADER))
                {
                  if (elem instanceof SOAPHeader)
                  {
                    SOAPHeader header = (SOAPHeader) elem;
                    // If the part is bound by this element
                    if (messageName.equals(header.getMessage())
                      && header.getPart() != null
                      && header.getPart().equals(part))
                    {
                      boundProperly = true;
                      break;
                    }
                  }
                  // WSDL4J 1.4 does not recognize soap:header elements that
                  // are enclosed in mime:multipartRelated, so using a workaround
                  else
                  {
                    Element header =
                      ((UnknownExtensibilityElement) elem).getElement();
                    // If a header references the corresponding message
                    // and the part is bound by this element
                    if (referencesMessage(header, messageName)
                      && header.getAttribute("part").equals(part))
                    {
                      boundProperly = true;
                      break;
                    }
                  }
                }
              }
            }
          }
        }
      }
      // If this part is unbound properly, return it
      if (!boundProperly)
        return part;
    }
    // All the parts are bound properly, return null
    return null;
  }

  /**
   * Validates whether an element contains a message attribute that references
   * a message that have the qualified name specified.
   * @param elem an element to be validated.
   * @param messageName the qualified name of a message.
   * @return true if an element is valid, false otherwise.
   */
  private boolean referencesMessage(Element elem, QName messageName)
  {
    // Getting the element's message attribute
    String message = elem.getAttribute("message");
    // finding the colon delimiter
    int colonPos = message.indexOf(":");
    String ns = null;
    // Getting a local part
    String lp = colonPos > -1 ? message.substring(colonPos + 1) : message;
    // If the delimiter is found
    if (colonPos > -1)
    {
      // Retrieving a namespace URI
      ns = validator.wsdlDocument.getDefinitions()
        .getNamespace(message.substring(0, colonPos));
    }
    // If the local part and the namespace URI are the same as a message have
    if (messageName.getLocalPart().equals(lp)
      && messageName.getNamespaceURI().equals(ns))
    {
      // element is valid, return true
      return true;
    }
    // element is not valid, return false
    return false;
  }

  /**
   * Collects all the wsdl:part defined with the ref:swaRef schema type.
   * @param message a message containig wsdl:part elements.
   * @return a list of wsdl:part names.
   */
  private List getSwaRefParts(Message message)
  {
    List swaRefParts = new ArrayList();

    // Going through message's parts
    Iterator it = message.getParts().values().iterator();
    while (it.hasNext())
    {
      Part part = (Part) it.next();
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
      // The part does conatins neither element nor type attribute,
      // proceeding with the next part
      else
      {
        continue;
      }
      // Getting a list of schemas defined
      Map schemas = validator.wsdlDocument.getSchemas();
      // Going through the schemas
      Iterator it2 = schemas.values().iterator();
      while (it2.hasNext())
      {
        XSModel xsModel = (XSModel) it2.next();
        XSTypeDefinition partType = null;
        // Getting the corresponding part type
        if (type == XSConstants.ELEMENT_DECLARATION)
        {
          XSElementDeclaration elem = xsModel.getElementDeclaration(
            ref.getLocalPart(), ref.getNamespaceURI());
          if (elem != null)
            partType = elem.getTypeDefinition();
        }
        else
        {
          partType = xsModel.getTypeDefinition(
            ref.getLocalPart(), ref.getNamespaceURI());
        }
        // If the part type is defined using the ref:swaRef schema type
        if (referencesSwaRef(partType, new ArrayList()))
        {
          // Adding the part's name to the list being returned
          swaRefParts.add(part.getName());
        }
      }
    }
    // Return the list
    return swaRefParts;
  }

  /**
   * Check schema type whether it contains ref:swaRef simple schema type
   * or has an attribute of this schema type.
   * @param xsType a schema type definition element
   * @return true, if schema type contains ref:swaRef, false otherwise.
   */
  private boolean referencesSwaRef(XSTypeDefinition xsType, List types)
  {
    if ((xsType != null) && (!types.contains(xsType)))
    {
      types.add(xsType);
      // If this is a complex type
      if (xsType.getTypeCategory() == XSTypeDefinition.COMPLEX_TYPE)
      {
        XSComplexTypeDefinition xsComplexType =
          (XSComplexTypeDefinition) xsType;
        // If it contains mixed context
        // check whether the context contains ref:swaRef
        if (xsComplexType.getParticle() != null
          && referencesSwaRef(xsComplexType.getParticle().getTerm(), types))
        {
          return true;
        }
        // Getting the type's attributes
        XSObjectList attrList = xsComplexType.getAttributeUses();
        for (int i = 0; i < attrList.getLength(); i++)
        {
          XSAttributeUse xsAttrUse = (XSAttributeUse) attrList.item(i);
          // If an attribute is defined using ref:swaRef, return true
          if (referencesSwaRef(
            xsAttrUse.getAttrDeclaration().getTypeDefinition(), types))
          {
            return true;
          }
        }
      }
      // else if this is a simple type
      else if (xsType.getTypeCategory() == XSTypeDefinition.SIMPLE_TYPE)
      {
        XSSimpleType xsSimpleType = (XSSimpleType) xsType;
        // If this type is ref:swaRef, return true
        if (xsSimpleType.getNamespace().equals(WSIConstants.NS_URI_SWA_REF)
          && xsSimpleType.getName().equals(WSIConstants.SCHEMA_TYPE_SWA_REF))
        {
          return true;
        }
      }
    }
    // The schema type does not contain any element defined with the ref:swaRef
    // return false
    return false;
  }

  /**
   * Checks a term whether it is defined with ref:swaRef.
   * @param term a term that can be one of a model group or an
   * element declaration.
   * @return true if a term is defined with ref:swaRef, false otherwise.
   */
  private boolean referencesSwaRef(XSTerm term, List types)
  {
    // If a term is an element declaration
    if (term.getType() == XSConstants.ELEMENT_DECLARATION)
    {
      // If element's type is defined with the ref:swaRef, return true
      if (referencesSwaRef(
        ((XSElementDeclaration) term).getTypeDefinition(), types))
      {
          return true;
      }
    }
    // else if a term is a model group
    else if(term.getType() == XSConstants.MODEL_GROUP)
    {
      // Getting a list of Particle schema components
      XSObjectList list = ((XSModelGroup) term).getParticles();
      for (int i = 0; i < list.getLength(); i++)
      {
        // If the term of a particle is defined with the ref:swaRef,
        // return true
        if (referencesSwaRef(((XSParticle) list.item(i)).getTerm(), types))
        {
          return true;
        }
      }
    }
    return false;
  }
}