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
package org.eclipse.wst.wsi.internal.core.util;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.wsdl.Binding;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.wsdl.WSDLDocument;
import org.uddi4j.UDDIException;
import org.uddi4j.client.UDDIProxy;
import org.uddi4j.datatype.binding.BindingTemplate;
import org.uddi4j.datatype.binding.TModelInstanceInfo;
import org.uddi4j.datatype.business.BusinessEntity;
import org.uddi4j.datatype.service.BusinessService;
import org.uddi4j.datatype.tmodel.TModel;
import org.uddi4j.response.BusinessDetail;
import org.uddi4j.response.ServiceDetail;
import org.uddi4j.response.TModelDetail;
import org.uddi4j.response.TModelInfo;
import org.uddi4j.response.TModelList;
import org.uddi4j.transport.TransportException;
import org.uddi4j.util.CategoryBag;
import org.uddi4j.util.FindQualifier;
import org.uddi4j.util.FindQualifiers;
import org.uddi4j.util.KeyedReference;

/**
 * This class provide some service functions used by UDDIValidator.
 *
 * @version 1.0.1
 * @author Peter Brittenham
 */
public final class UDDIUtils
{
  static public final FindQualifiers EXACT_NAME_MATCH_QUALIFIER;
  static {
    Vector vector = new Vector();
    EXACT_NAME_MATCH_QUALIFIER = new FindQualifiers();
    vector.add(new FindQualifier(FindQualifier.exactNameMatch));
    vector.add(new FindQualifier(FindQualifier.sortByNameAsc));
    EXACT_NAME_MATCH_QUALIFIER.setFindQualifierVector(vector);
  }

  static final String WS_I_CONFORMANCE_TMODEL_NAME =
    "ws-i-org:conformsTo:2002_12";

  static private Hashtable wsiConformanceTable = new Hashtable();

  /**
   * Gets a business service by key.
   * @param proxy  a UDDI proxy.
   * @param key    a key to a business service.
   * @return a business service specified by the given key.
   * @throws TransportException if error originating within the SOAP transport.
   * @throws UDDIException if the return value of a UDDI API cannot indicate an error condition.
   */
  public static BusinessService getBusinessServiceByKey(
    UDDIProxy proxy,
    String key)
    throws TransportException, UDDIException
  {
    BusinessService result = null;

    ServiceDetail sd = proxy.get_serviceDetail(key);

    if (sd != null)
    {
      Vector v = sd.getBusinessServiceVector();

      if (v != null && v.size() > 0)
      {
        result = (BusinessService) v.firstElement();
      }
    }

    return result;
  }

  /**
   * Gets business entity by key.
   * @param proxy  a UDDI proxy.
   * @param key    a key to a business entiy.
   * @return a business entity specified by the given key.
   * @throws TransportException if error originating within the SOAP transport.
   * @throws UDDIException if the return value of a UDDI API cannot indicate 
   *         an error condition.
   */
  public static BusinessEntity getBusinessByKey(UDDIProxy proxy, String key)
    throws TransportException, UDDIException
  {
    BusinessEntity result = null;

    BusinessDetail bd = proxy.get_businessDetail(key);

    if (bd != null)
    {
      Vector v = bd.getBusinessEntityVector();

      if (v != null && v.size() > 0)
      {
        result = (BusinessEntity) v.firstElement();
      }
    }

    return result;
  }

  /**
   * Gets a tModel by key.
   * @param proxy  a UDDI proxy.
   * @param key    a key to a tModel.
   * @return a tModel specified by the given key.
   * @throws TransportException if error originating within the SOAP transport.
   * @throws UDDIException if the return value of a UDDI API cannot indicate 
   *         an error condition.
   */
  public static TModel getTModelByKey(UDDIProxy proxy, String key)
  {
    TModel result = null;

    try
    {
      TModelDetail bd = proxy.get_tModelDetail(key);

      if (bd != null)
      {
        Vector v = bd.getTModelVector();

        if (v != null && v.size() > 0)
        {
          result = (TModel) v.firstElement();
        }
      }
    }
    catch (Throwable e)
    {
    }

    return result;
  }

  /**
   * Gets key of the WSI Conformance tModel.
   * @param proxy  a UDDI proxy.
   * @return the key of the WSI Conformance tModel.
   */
  public static String getWSIConformanceTModelKey(UDDIProxy proxy)
  {
    if (proxy == null)
      throw new IllegalArgumentException("UDDI proxy cannot be null.");

    String result = null;

    if (wsiConformanceTable.containsKey(proxy))
    {
      result = wsiConformanceTable.get(proxy).toString();
    }
    else
    {
      result = getTModelKeyByName(proxy, WS_I_CONFORMANCE_TMODEL_NAME);

      if (result == null)
      {
        throw new IllegalStateException("WS-I conformance taxonomy tModel was not found");
      }

      wsiConformanceTable.put(proxy, result);
    }

    return result;
  }

  /**
   * Gets a tModel key by tModel name.
   * @param proxy  a UDDI proxy.
   * @param name   a tModel name.
   * @return a tModel key specified by the given tModel name.
   */
  public static String getTModelKeyByName(UDDIProxy proxy, String name)
  {
    String result = null;
    try
    {
      TModelList list =
        proxy.find_tModel(name, null, null, EXACT_NAME_MATCH_QUALIFIER, 1);
      TModelInfo info =
        (TModelInfo) list.getTModelInfos().getTModelInfoVector().firstElement();
      result = info.getTModelKey();
    }
    catch (Throwable e)
    {
    }

    return result;
  }

  /**
   * Get string representation of bindingTemplate.
   * @param bindingTemplate  a BindingTemplate object.
   * @return a string representation of bindingTemplate.
   */
  public static String bindingTemplateToString(BindingTemplate bindingTemplate)
  {
    String returnString = "";

    if (bindingTemplate == null)
      returnString = "null";

    else
    {
      returnString =
        "accessPoint: "
          + (bindingTemplate.getAccessPoint() == null
            ? "null"
            : bindingTemplate.getAccessPoint().getText());

      if (bindingTemplate.getTModelInstanceDetails() == null
        || bindingTemplate
          .getTModelInstanceDetails()
          .getTModelInstanceInfoVector()
          == null)
      {
        returnString += ", [no tModel reference]";
      }

      else
      {
        Iterator iterator =
          bindingTemplate
            .getTModelInstanceDetails()
            .getTModelInstanceInfoVector()
            .iterator();

        int infoCount = 1;
        TModelInstanceInfo info;
        while (iterator.hasNext())
        {
          info = (TModelInstanceInfo) iterator.next();
          returnString += ", ["
            + infoCount++
            + "] tModelKey: "
            + info.getTModelKey();
        }
      }
    }

    return returnString;
  }

  /**
   * Get string representation of tModel.
   * @param tModel  a TModel object.
   * @return a string representation of tModel.
   */
  public static String tModelToString(TModel tModel)
  {
    String returnString = "";

    if (tModel == null)
      returnString = "null";

    else
    {
      returnString =
        "name: "
          + tModel.getNameString()
          + ", categoryBag: "
          + (tModel.getCategoryBag() == null
            ? "null"
            : categoryBagToString(tModel.getCategoryBag()))
          + ", overviewURL: "
          + (tModel.getOverviewDoc() == null
            ? "null"
            : tModel.getOverviewDoc().getOverviewURLString());
    }

    return returnString;
  }

  /**
   * Get string representation of categoryBag.
   * @param categoryBag  a CategoryBag object.
   * @return a tring representation of categoryBag.
   */
  public static String categoryBagToString(CategoryBag categoryBag)
  {
    String returnString = "";

    if (categoryBag == null)
    {
      returnString += "null";
    }

    else
    {
      returnString += "KeyedReferenceList: ";

      Vector krList = null;
      if ((krList = categoryBag.getKeyedReferenceVector()) == null)
      {
        returnString += "null";
      }

      else if (krList.size() == 0)
      {
        returnString += "empty";
      }

      else
      {
        KeyedReference kr = null;
        Iterator iterator = krList.iterator();
        while (iterator.hasNext())
        {
          kr = (KeyedReference) iterator.next();
          returnString += "tModelKey: "
            + kr.getTModelKey()
            + ", keyName: "
            + kr.getKeyName()
            + ", keyValue: "
            + kr.getKeyValue();
        }
      }
    }

    return returnString;
  }

  /**
   * Find the wsdlSpec tModel associated with a binding.
   */
  public static TModel findTModel(
    UDDIProxy uddiProxy,
    BindingTemplate bindingTemplate,
    boolean verboseOption)
    throws WSIException
  {
    TModel tModel = null;

    // Get the list of tModel references associated with this bindingTemplate
    Iterator iterator =
      bindingTemplate
        .getTModelInstanceDetails()
        .getTModelInstanceInfoVector()
        .iterator();

    // Process each tModel reference
    Vector tModelKeyList = new Vector();
    while (iterator.hasNext())
    {
      // Get tModelInstanceInfo
      TModelInstanceInfo tModelInstanceInfo =
        (TModelInstanceInfo) iterator.next();

      // Add key to list
      tModelKeyList.add(tModelInstanceInfo.getTModelKey());
    }

    // Get the tModels associated with the bindingTemplate
    if (tModelKeyList.size() > 0)
    {
      try
      {
        // Get the tModel details
        TModelDetail tModelDetail = uddiProxy.get_tModelDetail(tModelKeyList);

        // Get the list of tModels
        Iterator tModelIterator = tModelDetail.getTModelVector().iterator();

        //boolean tModelFound = false;
        TModel nextTModel = null;

        // Go through the list of tModels
        while ((tModelIterator.hasNext()) && (tModel == null))
        {
          // Get next tModel in list
          nextTModel = (TModel) tModelIterator.next();

          if (verboseOption)
          {
            System.err.println(
              "      TModel referenced from bindingTemplate - "
                + UDDIUtils.tModelToString(nextTModel));
          }

          // If this is a wsdlSpec tModel, then this is the tModel we want
          if (isWsdlSpec(nextTModel))
            tModel = nextTModel;
        }
      }

      catch (Exception e)
      {
        // Throw WSIException
        throw new WSIException("Could not get tModel details.", e);
      }
    }

    else
    {
      // Throw exception
      //throw new WSIException("UDDI bindingTemplate did not contain any tModel references.");
    }

    return tModel;
  }
  
  /**
   * Determine if this is a wsdlSpec tModel.
   */
  public static boolean isWsdlSpec(TModel tModel)
  {
    boolean tModelFound = false;
    CategoryBag categoryBag = null;
    Iterator categoryBagIterator = null;

    // Determine if the catetgoryBag contains wsdlSpec
    if ((categoryBag = tModel.getCategoryBag()) != null)
    {
      // Get the list of keyed references
      categoryBagIterator = categoryBag.getKeyedReferenceVector().iterator();

      KeyedReference keyedReference = null;

      // Go through the list of keyed references
      while (categoryBagIterator.hasNext() && !(tModelFound))
      {
        // Get next keyed reference
        keyedReference = (KeyedReference) categoryBagIterator.next();

        // If this is a types taxonomy tModel and the value is wsdlSpec, then this is the tModel we want
        // REMOVE: It is not necessary to check the key name
        //if (keyedReference.getTModelKey().equalsIgnoreCase(TModel.TYPES_TMODEL_KEY) &&
        //   "wsdlSpec".equals(keyedReference.getKeyValue()) &&
        //   ("types".equals(keyedReference.getKeyName()) ||
        //    "uddi-org:types".equals(keyedReference.getKeyName()))) {
        //  tModelFound = true;
        //}
        if (keyedReference
          .getTModelKey()
          .equalsIgnoreCase(TModel.TYPES_TMODEL_KEY)
          && "wsdlSpec".equals(keyedReference.getKeyValue()))
        {
          tModelFound = true;
        }
      }
    }

    return tModelFound;
  }
  /**
   * Get an OverviewURL from tModel.
   */
  public static String getOverviewURL(TModel tModel)
  {
    if (tModel != null
      && tModel.getOverviewDoc() != null
      && tModel.getOverviewDoc().getOverviewURL() != null)
    {
      return tModel.getOverviewDoc().getOverviewURL().getText();
    }
    return null;
  }
  

  /**
   * Get WSDL document.
   */
  public static String getWSDLLocation(String wsdlLocation)
  {
    int index;

    // Check if the overviewURL contains a fragment identifier
    if ((index = wsdlLocation.indexOf("#")) > -1)
    {
      wsdlLocation = wsdlLocation.substring(0, index);
    }
    return wsdlLocation;
  }
  /**
   * Get WSDL binding from the overviewURL in the tModel.
   */
  public static Binding getBinding(String overviewURL, WSDLDocument wsdlDocument)
  {
    int index;
    int nameIndex;

    Binding[] bindings = wsdlDocument.getBindings();
    if (bindings == null || bindings.length == 0)
      return null;

    if (overviewURL != null)
    {
      // Check if the overviewURL contains a fragment identifier
      if ((index = overviewURL.indexOf("#")) > -1)
      {
        // TEMP: Need to use a real XPath evaluator like Xalan
        String nameAttribute = "@name=";

        // Locate name reference
        if ((nameIndex =
          overviewURL.substring(index + 1).indexOf(nameAttribute))
          > -1)
        {
          // Get the next character which should be a quote
          int firstQuoteIndex = index + 1 + nameIndex + nameAttribute.length();
          String quote =
            overviewURL.substring(firstQuoteIndex, firstQuoteIndex + 1);

          // Get the part of the URL which should contain the binding name
          String urlPart = overviewURL.substring(firstQuoteIndex + 1);

          // Find the next quote
          int nextQuoteIndex;
          if ((nextQuoteIndex = urlPart.indexOf(quote)) > -1)
          {
            String bindingName = urlPart.substring(0, nextQuoteIndex);
            //look for binding with the specified name
            for (int i = 0; i < bindings.length; i++)
            {
              if (bindingName.equals(bindings[i].getQName().getLocalPart()))
                return bindings[i];
            }
          }
        }
      }
    }

    return bindings[0];
  }
}
