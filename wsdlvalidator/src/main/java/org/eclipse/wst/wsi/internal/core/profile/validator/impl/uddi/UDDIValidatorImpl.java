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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Vector;

import javax.wsdl.Binding;
import javax.wsdl.WSDLException;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.analyzer.AnalyzerContext;
import org.eclipse.wst.wsi.internal.core.analyzer.ServiceReference;
import org.eclipse.wst.wsi.internal.core.analyzer.config.AnalyzerConfig;
import org.eclipse.wst.wsi.internal.core.analyzer.config.UDDIReference;
import org.eclipse.wst.wsi.internal.core.analyzer.config.WSDLElement;
import org.eclipse.wst.wsi.internal.core.analyzer.config.impl.WSDLElementImpl;
import org.eclipse.wst.wsi.internal.core.profile.ProfileArtifact;
import org.eclipse.wst.wsi.internal.core.profile.ProfileAssertions;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.UDDIValidator;
import org.eclipse.wst.wsi.internal.core.profile.validator.WSDLValidator;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseValidatorImpl;
import org.eclipse.wst.wsi.internal.core.report.Entry;
import org.eclipse.wst.wsi.internal.core.report.ReportArtifact;
import org.eclipse.wst.wsi.internal.core.report.Reporter;
import org.eclipse.wst.wsi.internal.core.util.EntryType;
import org.eclipse.wst.wsi.internal.core.util.UDDIUtils;
import org.eclipse.wst.wsi.internal.core.util.Utils;
import org.eclipse.wst.wsi.internal.core.wsdl.WSDLDocument;
import org.uddi4j.client.UDDIProxy;
import org.uddi4j.datatype.binding.BindingTemplate;
import org.uddi4j.datatype.tmodel.TModel;
import org.uddi4j.response.BindingDetail;
import org.uddi4j.response.TModelDetail;
import org.uddi4j.util.CategoryBag;
import org.uddi4j.util.KeyedReference;

import com.ibm.wsdl.util.StringUtils;

/**
 * The UDDI validator will verify that a web service description was published correctly in a UDDI registry.
 *
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public class UDDIValidatorImpl
  extends BaseValidatorImpl
  implements UDDIValidator
{
  /**
   * UDDI reference.
   */
  protected UDDIReference uddiReference;
  /**
   * UDDI proxy.
   */
  protected UDDIProxy uddiProxy;
  private boolean testable;

  /**
   * Get the artifact type that this validator applies to.
   * @return the artifact type (a String)
   */
  public String getArtifactType() {
      return TYPE_DISCOVERY;
  }

  /**
   * Get the collection of entry types that this validator applies to.
   * @return an array of entry types (Strings)
   */
  public String[] getEntryTypes() {
      return new String[] {
              TYPE_DISCOVERY_BINDINGTEMPLATE,
              TYPE_DISCOVERY_TMODEL};
  }
  
  public void init(
          AnalyzerContext analyzerContext,
          ProfileAssertions assertions,
          ReportArtifact reportArtifact,
          AnalyzerConfig analyzerConfig,
          Reporter reporter)
          throws WSIException {
      super.init(analyzerContext,
              assertions.getArtifact(TYPE_DISCOVERY), reportArtifact, reporter);
      this.uddiReference = analyzerConfig.getUDDIReference();
      testable = analyzerConfig.isUDDIReferenceSet();
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.validator.UDDIValidator#init(org.eclipse.wst.wsi.internal.core.analyzer.AnalyzerContext, org.wsi.test.profile.ProfileArtifact, org.wsi.test.report.ReportArtifact, org.wsi.test.analyzer.config.UDDIReference, org.wsi.test.report.Reporter)
   */
  public void init(
    AnalyzerContext analyzerContext,
    ProfileArtifact profileArtifact,
    ReportArtifact reportArtifact,
    UDDIReference uddiReference,
    Reporter reporter)
    throws WSIException
  {
    // BaseValidatorImpl
    super.init(analyzerContext, profileArtifact, reportArtifact, reporter);

    AnalyzerConfig analyzerConfig = Utils.getAnalyzerConfig(reporter);
    testable = ((analyzerConfig != null) && (analyzerConfig.isUDDIReferenceSet()));	

    // Save input references
    this.uddiReference = uddiReference;
  }
  /* (non-Javadoc)
   * @see org.wsi.test.profile.validator.UDDIValidator#validate()
   */
  public void validateArtifact() throws WSIException
  {
    Entry entry = null;

    BindingTemplate bindingTemplate = null;
    TModel tModel = null;

    // it depricated after refactoring 
    // now the inner classes moved out from validator
    //String classPrefix = this.getClass().getName() + "$";
    String classPrefix = this.getClass().getPackage().getName()+".";

    try
    {
      // Set up a reference to the UDDI registry
      uddiProxy = new UDDIProxy();

      //new Socket(new InetAddress(uddiReference.getInquiryURL()));
      uddiProxy.setInquiryURL(uddiReference.getInquiryURL());
      boolean wasEx = false;
      InputStream stream;
      try
      {
        URL url = StringUtils.getURL(null, uddiReference.getInquiryURL());
        stream = url.openStream();
        stream.close();
      }

      catch (UnknownHostException ex)
      {
        wasEx = true;
      }

      catch (IOException ex)
      {
      }

      if (!wasEx)
      {

        // If the UDDI reference is to a bindingTemplate then get it
        if (uddiReference.getKeyType().equals(UDDIReference.BINDING_KEY))
        {
          // Get binding detail which will contain the bindingTemplate
          BindingDetail bindingDetail =
            uddiProxy.get_bindingDetail(uddiReference.getKey());

          // Get bindingTemplate
          bindingTemplate =
            (BindingTemplate) bindingDetail
              .getBindingTemplateVector()
              .elementAt(
              0);

          if (verboseOption)
          {
            System.err.println(
              "    BindingTemplate - "
                + UDDIUtils.bindingTemplateToString(bindingTemplate));
          }

          // Get the wsdlSpec tModel
          tModel = UDDIUtils.findTModel(uddiProxy, bindingTemplate,
                  verboseOption);
        }

        // Else it has to be a tModel
        else
        {
          TModelDetail tModelDetail =
            uddiProxy.get_tModelDetail(uddiReference.getKey());
          tModel = (TModel) tModelDetail.getTModelVector().elementAt(0);
        }

        if (verboseOption)
        {
          System.err.println(
            "    TModel specified or found in bindingTemplate - "
              + UDDIUtils.tModelToString(tModel));
        }
      }

      if (bindingTemplate == null)
      {
        setMissingInput(
          EntryType.getEntryType(TYPE_DISCOVERY_BINDINGTEMPLATE));
      }

      // If there is a bindingTemplate, then process test assertions for it
      else
      {
        // Create entry
        entry = this.reporter.getReport().createEntry();
        entry.setEntryType(
          EntryType.getEntryType(TYPE_DISCOVERY_BINDINGTEMPLATE));
        entry.setReferenceID(bindingTemplate.getBindingKey());
        entry.setEntryDetail(bindingTemplate);

        // Process test assertions
        processAssertions(
          classPrefix,
          new EntryContext(entry, this.analyzerContext));
      }

      // NOTE: From this point forward, if a bindingTemplate does NOT have a wsdlSpec tModel,
      //       the tModel will be NULL.

      // Create entry
      entry = this.reporter.getReport().createEntry();
      entry.setEntryType(
        EntryType.getEntryType(TYPE_DISCOVERY_TMODEL));
      entry.setReferenceID(
        (tModel == null ? "[tModel]" : tModel.getTModelKey()));
      entry.setEntryDetail(tModel);

      // Process test assertions
      processAssertions(
        classPrefix,
        new EntryContext(entry, this.analyzerContext));
    }

    catch (Exception e)
    {
      // Throw WSIException
      if (e instanceof WSIException)
        throw (WSIException) e;
      else
        throw new WSIException(
          "An exception occurred while processing the discovery test assertions.",
          e);
    }

    // Cleanup
    cleanup();
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.validator.UDDIValidator#validate()
   */
  /** @deprecated -- use validateArtifact(). */
  public String validate() throws WSIException
  {
    validateArtifact();
    // Get WSDL location
    return this.analyzerContext.getServiceReference().getWSDLLocation();
  }
  
  /**
   * Set WSDL binding in the service reference portion of the analyzer context.
   */
  protected void setWSDLBinding(Binding binding)
  {
    String bindingName =
      binding == null ? null : binding.getQName().getLocalPart();
    String namespace =
      binding == null ? null : binding.getQName().getNamespaceURI();

    // Set the WSDL document location in analyzerContext
    ServiceReference serviceReference =
      this.analyzerContext.getServiceReference();

    // If the wsdlElement does not exist, then set it
    WSDLElement wsdlElement;
    if ((wsdlElement = serviceReference.getWSDLElement()) == null)
    {
      // Get the WSDL binding from the overviewURL and set in analyzerContext
      wsdlElement = new WSDLElementImpl();
      wsdlElement.setName(bindingName);
      wsdlElement.setNamespace(namespace);
      wsdlElement.setType(WSDLValidator.TYPE_DESCRIPTION_BINDING);
    }

    // Set the wsdlElement in the service reference
    serviceReference.setWSDLElement(wsdlElement);

    // Set the service reference back into the analyzerContext
    analyzerContext.setServiceReference(serviceReference);
  }

  /**
   * Set the WSDL location in the service reference portion of the analyzer context.
   */
  protected void setWSDLLocation(String wsdlURL)
  {
    // Set the WSDL document location in analyzerContext
    ServiceReference serviceReference =
      this.analyzerContext.getServiceReference();
    serviceReference.setWSDLLocation(wsdlURL);

    // Set the service reference back into the analyzerContext
    analyzerContext.setServiceReference(serviceReference);
  }

   /**
   * Determine if this is a wsdlSpec tModel.
   * @deprecated -- use UDDIUtils.isWsdlSpec(String wsdlLocation).
   */
  protected boolean isWsdlSpec(TModel tModel)
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
   * @deprecated -- use UDDIUtils.getOverviewURL(String wsdlLocation).
   */
  protected String getOverviewURL(TModel tModel)
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
   * @deprecated -- use UDDIUtils.getWSDLLocation(String wsdlLocation).
   */
  protected String getWSDLLocation(String wsdlLocation)
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
   * Get WSDL document.
   */
  protected WSDLDocument getWSDLDocument(String wsdlLocation)
    throws MalformedURLException, WSDLException
  {
    return new WSDLDocument(UDDIUtils.getWSDLLocation(wsdlLocation));
  }

  /**
   * Get WSDL binding from the overviewURL in the tModel.
   */
  protected Binding getBinding(String overviewURL, WSDLDocument wsdlDocument)
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

  /**
   * Get string representation of categoryBag.
   * @param categoryBag a CategoryBag object.
   * @return string representation of categoryBag.
   */
  protected String categoryBagToString(CategoryBag categoryBag)
  {
    String toString = "";

    if (categoryBag != null)
    {
      Vector keyedReferenceList = null;
      if (((keyedReferenceList = categoryBag.getKeyedReferenceVector())
        == null)
        || (keyedReferenceList.size() == 0))
      {
        toString += "No KeyedReferences";
      }

      else
      {
        int count = 1;
        KeyedReference keyedReference;
        Iterator iterator = keyedReferenceList.iterator();
        while (iterator.hasNext())
        {
          keyedReference = (KeyedReference) iterator.next();
          toString += "\n  ["
            + count++
            + "] tModelKey: "
            + keyedReference.getTModelKey()
            + ", keyName: "
            + keyedReference.getKeyName()
            + ", keyValue: "
            + keyedReference.getKeyValue();
        }
      }
    }
    else
    {
      toString = "null";
    }

    return toString;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.validator.impl.BaseValidatorImpl#isPrimaryEntryTypeMatch(org.wsi.test.profile.TestAssertion, org.wsi.test.profile.validator.EntryContext)
   */
  protected boolean isPrimaryEntryTypeMatch(
    TestAssertion testAssertion,
    EntryContext targetContext)
  {
    boolean match = false;

    // If the test assertion entry type matches the target context entry type, then contine
    if (testAssertion
      .getEntryTypeName()
      .equals(targetContext.getEntry().getEntryType().getTypeName()))
    {
      match = true;
    }

    return match;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.validator.impl.BaseValidatorImpl#isNotApplicable(org.wsi.test.profile.TestAssertion)
   */
  protected boolean isNotApplicable(TestAssertion testAssertion)
  {
    boolean notApplicable = false;

    // ADD:

    return notApplicable;
  }

  /**
   * Returns true if these tests should be run (depending on the analyzer
   * config)
   */
  public boolean runTests() { return testable; }
}
