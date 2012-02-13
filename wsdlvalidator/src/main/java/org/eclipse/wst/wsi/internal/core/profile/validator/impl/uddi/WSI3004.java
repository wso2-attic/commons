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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.wsdl.Binding;
import javax.wsdl.Port;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.WSITag;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcessVisitor;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.UDDIUtils;
import org.eclipse.wst.wsi.internal.core.wsdl.WSDLDocument;
import org.eclipse.wst.wsi.internal.core.wsdl.traversal.WSDLTraversal;
import org.eclipse.wst.wsi.internal.core.wsdl.traversal.WSDLTraversalContext;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.uddi4j.datatype.tmodel.TModel;
import org.uddi4j.util.CategoryBag;
import org.uddi4j.util.KeyedReference;
import org.w3c.dom.Element;


/**
 * WSI3004 - The conformance claims in the uddi:tModel are the same
 * as those in the wsdl:binding which is referenced by the uddi:tModel.
 */
public class WSI3004 extends AssertionProcessVisitor implements WSITag
{
  private final UDDIValidatorImpl validator;

  /**
   * @param UDDIValidatorImpl
   */
  public WSI3004(UDDIValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }

  private static final String PORTS_PARAM = "ports";
  private static final String BINDING_PARAM = "binding";

  /**
   * Gets WSI conformance claims from a tModel.
   * @param tModel
   * @return HashSet
   * @throws WSIException
   */
  private HashSet getConformanceClaimsFromUDDI(TModel tModel)
    throws WSIException
  {
    String conformanceKey = UDDIUtils.getWSIConformanceTModelKey(validator.uddiProxy);

    HashSet result = new HashSet();

    CategoryBag bag = tModel.getCategoryBag();
    if (bag != null)
    {
      Vector references = bag.getKeyedReferenceVector();
      for (int i = 0; i < references.size(); i++)
      {
        KeyedReference ref = (KeyedReference) references.get(i);

        if (ref.getTModelKey().equalsIgnoreCase(conformanceKey))
        {
          result.add(ref.getKeyValue());
        }
      }
    }

    return result;
  }

  /**
   * Gets WSI conformance claims from WSDL binding
   * that references specified tModel. Additionally,
   * WSDL ports, which uses the binding, are processed.
   *
   * @param tModel
   * @return HashSet
   */
  private HashSet getConformanceClaimsFromWSDL(TModel tModel)
  {
    HashSet result = new HashSet();

    try
    {
      // Read the WSDL document
      String overviewURL = UDDIUtils.getOverviewURL(tModel);
      WSDLDocument wsdlDocument = validator.getWSDLDocument(overviewURL);

      // Get binding
      Binding binding = UDDIUtils.getBinding(overviewURL, wsdlDocument);
      String bindingName = binding.getQName().getLocalPart();

      // Get claims
      result =
        getClaimsFromElement(binding.getDocumentationElement(), result);

      // Get ports, which reference this binding, because:
      // "A claim on a wsdl:port is inherited by the referenced wsdl:bindings."
      Vector ports = getPorts(wsdlDocument, bindingName);
      for (int i = 0; i < ports.size(); i++)
      {
        Port port = (Port) ports.get(i);

        result = getClaimsFromElement(port.getDocumentationElement(), result);
      }
    }
    catch (Exception e)
    {
      // ADD:
    }

    return result;
  }

  /**
   * Gets WSI conformance claims from the 'documentation' XML element
   * and puts them into the HashSet.
   * @param docElement
   * @param result
   * @return HashSet
   */
  private HashSet getClaimsFromElement(Element docElement, HashSet result)
  {
    if (result == null)
    {
      result = new HashSet();
    }

    if (docElement != null)
    {
      Element claimElement = XMLUtils.findChildElement(docElement, WSI_CLAIM);
      while (claimElement != null)
      {
        /*
        Node attr = XMLUtils.getAttribute(claimElement, ATTR_CLAIM_CONFORMSTO);
        if (attr != null)
        	result.add(attr.getNodeValue());
        */
        String value =
          claimElement.getAttribute(ATTR_CLAIM_CONFORMSTO.getLocalPart());
        if (value != null)
          result.add(value);

        claimElement = XMLUtils.findElement(claimElement, WSI_CLAIM);
      }
    }

    return result;
  }

  /**
   * Gets wsdl:ports, which use specified binding.
   * @param wsdlDocument
   * @param bindingName
   * @return Vector
   */
  private Vector getPorts(WSDLDocument wsdlDocument, String bindingName)
  {
    Vector ports = new Vector();

    Map params = new HashMap();
    params.put(PORTS_PARAM, ports);
    params.put(BINDING_PARAM, bindingName);

    WSDLTraversal traversal = new WSDLTraversal();
    //VisitorAdaptor.adapt(this);
    traversal.setVisitor(this);
    traversal.visitPort(true);
    traversal.ignoreReferences();
    traversal.ignoreImport();
    traversal.traverse(wsdlDocument.getDefinitions(), params);

    return ports;
  }

  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.Port, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(Port port, Object parent, WSDLTraversalContext ctx)
  {
    if (port
      .getBinding()
      .getQName()
      .getLocalPart()
      .equals(ctx.getParameter(BINDING_PARAM).toString()))
    {
      ((Vector) ctx.getParameter(PORTS_PARAM)).add(port);
    }
  }

  /* (non-Javadoc)
  * @see org.wsi.test.profile.validator.impl.BaseValidatorImpl.AssertionProcess#validate(org.wsi.test.profile.TestAssertion, org.wsi.test.profile.validator.EntryContext)
  */
  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    result = AssertionResult.RESULT_PASSED;

    // Get the tModel from the entryContext
    TModel tModel = (TModel) entryContext.getEntry().getEntryDetail();

    // If the tModel does not exist, then fail
    if (tModel == null)
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetailMessage = "Could not locate a tModel.";
    }

    // If there is a tModel
    else
    {
      try
      {
        HashSet claimsFromUDDI = getConformanceClaimsFromUDDI(tModel);
        HashSet claimsFromWSDL = getConformanceClaimsFromWSDL(tModel);

        if (claimsFromUDDI.size() == 0)
        {
          result = AssertionResult.RESULT_PASSED;
        }
        else if (claimsFromUDDI.size() == claimsFromWSDL.size())
        {
          int counter = 0;
          for (Iterator iter = claimsFromWSDL.iterator(); iter.hasNext();)
          {
            String claim = (String) iter.next();

            for (Iterator iter2 = claimsFromUDDI.iterator();
              iter2.hasNext();
              )
            {
              String uddiClaim = (String) iter2.next();
              if (claim.equalsIgnoreCase(uddiClaim))
              {
                counter++;
                break;
              }
            }
          }

          if (counter != claimsFromWSDL.size())
          {
            // failed
            result = AssertionResult.RESULT_FAILED;
            failureDetailMessage =
              "The tModel key is: [" + tModel.getTModelKey() + "].";
          }
        }
        else
        {
          // failed
          result = AssertionResult.RESULT_FAILED;
          failureDetailMessage =
            "The tModel key is: [" + tModel.getTModelKey() + "].";
        }
      }
      catch (IllegalStateException e)
      {
        result = AssertionResult.RESULT_FAILED;
        failureDetailMessage =
          "The tModel key is: [" + tModel.getTModelKey() + "].";
      }

    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetailMessage);
  }
}