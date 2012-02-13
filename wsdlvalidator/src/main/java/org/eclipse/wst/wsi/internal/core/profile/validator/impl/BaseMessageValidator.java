/*******************************************************************************
 * Copyright (c) 2002, 2009 IBM Corporation, Parasoft and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM      - Initial API and implementation
 *   Parasoft - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.core.profile.validator.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.wsdl.Binding;
import javax.wsdl.BindingOperation;
import javax.wsdl.Definition;
import javax.wsdl.Message;
import javax.wsdl.Part;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.mime.MIMEMultipartRelated;
import javax.wsdl.extensions.mime.MIMEPart;
import javax.wsdl.extensions.soap.SOAPBinding;
import javax.wsdl.extensions.soap.SOAPBody;
import javax.wsdl.extensions.soap.SOAPHeader;
import javax.wsdl.extensions.soap.SOAPOperation;
import javax.xml.namespace.QName;

import org.apache.xerces.util.URI;
import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSTypeDefinition;
import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.WSITag;
import org.eclipse.wst.wsi.internal.core.analyzer.AnalyzerContext;
import org.eclipse.wst.wsi.internal.core.analyzer.CandidateInfo;
import org.eclipse.wst.wsi.internal.core.analyzer.ServiceReference;
import org.eclipse.wst.wsi.internal.core.analyzer.config.AnalyzerConfig;
import org.eclipse.wst.wsi.internal.core.document.DocumentFactory;
import org.eclipse.wst.wsi.internal.core.log.Log;
import org.eclipse.wst.wsi.internal.core.log.LogReader;
import org.eclipse.wst.wsi.internal.core.log.MessageEntry;
import org.eclipse.wst.wsi.internal.core.log.MessageEntryHandler;
import org.eclipse.wst.wsi.internal.core.profile.ProfileArtifact;
import org.eclipse.wst.wsi.internal.core.profile.ProfileAssertions;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.LogValidator;
import org.eclipse.wst.wsi.internal.core.profile.validator.MessageValidator;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.wsdl.WSDLValidatorImpl;
import org.eclipse.wst.wsi.internal.core.report.ArtifactReference;
import org.eclipse.wst.wsi.internal.core.report.FailureDetail;
import org.eclipse.wst.wsi.internal.core.report.ReportArtifact;
import org.eclipse.wst.wsi.internal.core.report.Reporter;
import org.eclipse.wst.wsi.internal.core.util.EntryType;
import org.eclipse.wst.wsi.internal.core.util.HTTPUtils;
import org.eclipse.wst.wsi.internal.core.util.Utils;
import org.eclipse.wst.wsi.internal.core.wsdl.WSDLDocument;
import org.eclipse.wst.wsi.internal.core.wsdl.WSDLUtils;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ibm.icu.util.StringTokenizer;
import com.ibm.wsdl.Constants;
import com.ibm.wsdl.util.xml.DOM2Writer;
import com.ibm.wsdl.util.xml.DOMUtils;

/**
 * The WSDL validator will verify that the WSDL and associated XML schema definitions
 * are in conformance with the profile.
 *
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 * @author Jim Clune
 * @author Graham Turrell (gturrell@uk.ibm.com)
 * @author Neil Delima (nddelima@ca.ibm.com) 
 */
public abstract class BaseMessageValidator
  extends BaseValidatorImpl
  implements LogValidator
{
  /**
  * WSDL document.
  */
  private WSDLDocument wsdlDocument;

  /**
   * Log entry.
   */
  protected MessageEntry logEntry;
  protected Log log;
  
  private boolean testable;
  private AnalyzerConfig analyzerConfig;

  public void init(AnalyzerContext analyzerContext,
          ProfileAssertions assertions,
          ReportArtifact reportArtifact,
          AnalyzerConfig analyzerConfig,
          Reporter reporter)
          throws WSIException {

      super.init(analyzerContext, assertions.getArtifact(getArtifactType()),
              reportArtifact, reporter);

      testable = analyzerConfig.getLogLocation() != null;
      this.analyzerConfig = analyzerConfig;
      this.wsdlDocument = (WSDLDocument) analyzerContext.getWsdlDocument();
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.validator.MessageValidator#init(org.eclipse.wst.wsi.internal.core.analyzer.AnalyzerContext, org.wsi.test.profile.ProfileArtifact, org.wsi.test.report.ReportArtifact, org.wsi.wsdl.WSDLDocument, org.wsi.test.report.Reporter)
   */
  public void init(
    AnalyzerContext analyzerContext,
    ProfileArtifact profileArtifact,
    ReportArtifact reportArtifact,
    WSDLDocument wsdlDocument,
    Reporter reporter)
    throws WSIException
  {
    // BaseValidatorImpl
    super.init(analyzerContext, profileArtifact, reportArtifact, reporter);

    this.analyzerConfig = Utils.getAnalyzerConfig(reporter);
    testable = ((analyzerConfig != null) && (analyzerConfig.getLogLocation() != null));	

    // Save input references
    this.wsdlDocument = wsdlDocument;
  }

  /**
   * Returns wsdlDocument
   * @return wsdlDocument
   */
  public WSDLDocument getWSDLDocument()
  {
    return wsdlDocument;
  }
  /* (non-Javadoc)
   * @see org.wsi.test.profile.validator.MessageValidator#validate(org.wsi.test.profile.validator.EntryContext)
   */
  public void validate(EntryContext entryContext) throws WSIException
  {
    //Entry entry = null;

    // Save log entry to be referenced by other methods
    this.logEntry = entryContext.getMessageEntry();

    // Get reference to the analyzer config object
    AnalyzerConfig analyzerConfig =
      reporter.getReport().getReportContext().getAnalyzer().getAnalyzerConfig();

    /* If Service Description (WSDL/UDDI) NOT supplied in analyzer config OR
     * Service Description IS supplied and the current message correlates to it...
     */
    if ((!analyzerConfig.isWSDLReferenceSet())
      || messageCorrelatesToService(
        entryContext,
        analyzerConfig.getCorrelationType()))
    {
      // now inner classes moved out from the validator
      //String classPrefix = this.getClass().getName() + "$";
      String classPrefix = this.getClass().getPackage().getName()+".";

      // Process assertions for this artifact against the target context
      processAssertions(classPrefix, entryContext);
    }
  }

  /**
   * Check whether the message correlates to the service under test.
   * Use the request part of the request-response pair, from which the correlation of the response is implied.
   * Entities from the Service under test are determined once for all.
   * @param entryContext    an entry context.
   * @param correlationType a correlation type.
   * @return true if the message correlates to the service under test.
   * @throws WSIException if correlation type is not appropriate.
   */
  private boolean messageCorrelatesToService(
    EntryContext entryContext,
    String correlationType)
    throws WSIException
  {

    URI[] endpoints = null;
    boolean correlation = false;

    /* TEMP: Get it from analyzer config passed in on init method
    CandidateInfo candidate = entryContext
                              .getAnalyzerContext()
                              .getCandidateInfo();
    */
    CandidateInfo candidate = analyzerContext.getCandidateInfo();

    // Service reference 
    ServiceReference serviceReference = analyzerContext.getServiceReference();

    // Get the definition element
    Definition definition = candidate.getWsdlDocument().getDefinitions();

    Binding binding = null;
    if (serviceReference.getWSDLElement().isPort())
    {
      // Get service
      Service service =
        definition.getService(
          serviceReference.getWSDLElement().getParentElementQName());

      // Get port
      Port port = service.getPort(serviceReference.getWSDLElement().getName());

      // Get binding
      binding = port.getBinding();
    }
    else if (serviceReference.getWSDLElement().isBinding())
    {
      // Get binding
      binding =
        definition.getBinding(serviceReference.getWSDLElement().getQName());
    }

    if (binding == null)
    {
      return false;
    }

    if ((endpoints = hostAndPortCorrelation(entryContext)) != null
      && urlPathCorrelation(entryContext, endpoints))
    {

      /* If correlation type is "endpoint", this is all the correlation that can be done.
       * (Note - this is incomplete correlation since >1 service could be associated with the endpoint.
       *  Therefore , if messages for different services on the same endpoint appear in the log file
       *  and correlation type is "endpoint", all those messages will be analyzed).
       */
      if (correlationType
        .equals(WSIConstants.ATTRVAL_CORRELATION_TYPE_ENDPOINT))
      {
        correlation = true;
      }
      else
      {
        // always allow GET requests right through if they've passed Endpoint Correlation
        String requestType = getHttpRequestType(entryContext);
        if ((requestType != null) && requestType.equalsIgnoreCase("GET"))
        {
          correlation = true;
        }
        else
        {
          // The correlationType is not "endpoint" so continue on to processing for at least 
          // "namespace" correlation...

          // get the operation signature (input & output) from request & response messages...	   
          // and do a quick DOM parse 
          Document requestMessage = entryContext.getRequestDocument();

          // Check if namespace is found in request message only
          if (namespaceCorrelation(binding, requestMessage))
          {
            // If namespace found and the correlation type is namespace, then process messages
            if (correlationType
              .equals(WSIConstants.ATTRVAL_CORRELATION_TYPE_NAMESPACE))
            {
              correlation = true;
            }

            // If operation is found and correlation type is operation, then process messages
            else if (
              (operationCorrelation(binding, requestMessage))
                && (correlationType
                  .equals(WSIConstants.ATTRVAL_CORRELATION_TYPE_OPERATION)))
            {
              correlation = true;
            }
            else
            {
              // this should never happen if config schema was followed correctly
              throw new WSIException(
                "Bad correlation type found in config: " + correlationType);
            }
          }
        }
      }
    }

    return correlation;
  }

  /** 
   *  Correlation Check 1: Service Description endpoint vs HTTP Header (test 1)
   *  Check if receiverHostAndPort from request message log matches host & port from WSDL SOAP binding for
   *  the Service under test.
   *  If it does not, stop processing this message pair.
   * @param entryContext an entry context.
   * @return if receiverHostAndPort from request message log matches 
   *         host & port from WSDL SOAP binding for the Service.
   * @throws WSIException if problem occurs during correlation check.
   */
  private URI[] hostAndPortCorrelation(EntryContext entryContext)
    throws WSIException
  {

    // get <receiverHostAndPort> from request message
    String httpHostAndPort = entryContext.getRequest().getReceiverHostAndPort();
    // Search endpoint list for a match with <receiverHostAndPort>

    /* TEMP: Get it from analyzer config passed in on init method
    return entryContext.getAnalyzerContext()
                       .getCandidateInfo()
                       .getEndPoints(httpHostAndPort);
    */
    return analyzerContext.getCandidateInfo().getEndPoints(httpHostAndPort);
  }

  /** 
   * Correlation Check 2: Service Description endpoint vs HTTP Header (test 2)
   * 
   * Use analyzer config host & port, plus URL path (from request message header HTTP POST)
   * to look for the corresponding endpoint in the Service Definition.
   * If it does not, stop processing this message pair.
   * @param entryContext an entry context.
   * @param endpoints an array of endpoints.
   * @return true if corresponding endpoints are found in the Service Definition.
   * @throws WSIException if problem occurs during correlation check.
   */
  private boolean urlPathCorrelation(
    EntryContext entryContext,
    URI[] endpoints)
    throws WSIException
  {

    // compares: protocol (must be http for POST), host, port and path.

    // get POST URI path from message using Tokenizer	
    Vector requestLine =
      getHttpRequest(entryContext.getRequest().getHTTPHeaders());
    if (requestLine == null)
    {
      return false; // an unexpected HTTP request type !
    }

    String requestPath = (String) requestLine.get(1);
    // Path immediately follows POST

    // compare path with those in the previous generated URI list	
    Vector filteredEndpoints = new Vector();
    for (int i = 0; i < endpoints.length; i++)
    {
      if (endpoints[i].toString().endsWith(requestPath))
      {
        filteredEndpoints.add(endpoints[i]);
      }
    }

    if (filteredEndpoints.size() == 0)
      return false;

    // this URI should be unique in the WSDL - there should be at most one match
    // but if we have at least one, that's sufficient to accept the message

    return true;
  }

  /** 
   * Correlation Check 3: Service Description namespace.
   * 
   * Determine if the Service Description declares the namespace that appears in the (request?) message soap body.
   * Location of relevent namespace :
   * Operation type                    	- Message Namespace: (compare with)  Service Definition Namespace:
   * (from message? endpoint? where?)
   * rpc-literal                           &lt;soap:Body&gt; child namespace.        &lt;soapbind:body&gt; namespace.
   * doc-literal			                 &lt;soap:Body&gt; child namespace.       targetNameSpace of schema that
   *                                                                          defines  &lt;soap:body&gt; child.
   * If it doesn't match, stop processing this message pair.
   * @param binding a binding.
   * @param request a request.
   * @return true if description declared in the description are the 
   *         same as those that appear in the message soap body.
   * @throws WSIException if problem occurs during correlationcheck.                                                                       
   */
  private boolean namespaceCorrelation(Binding binding, Document request)
    throws WSIException
  {
    boolean namespaceFound = false;
    String bindingStyle = WSIConstants.ATTRVAL_SOAP_BIND_STYLE_DOC;

    // Get operation namespace from the request soap message
    String soapOpNS = getSoapBodyChild(request).getNamespaceURI();

    // Get soapbind:binding
    SOAPBinding soapBinding = null;
    if ((soapBinding = WSDLUtils.getSoapBinding(binding)) != null)
    {
      bindingStyle = soapBinding.getStyle();
    }

    // Go through each operation in the binding and check for namespace match
    Iterator operations = binding.getBindingOperations().iterator();
    while (operations.hasNext() && !(namespaceFound))
    {
      BindingOperation bindingOperation = (BindingOperation) operations.next();

      // If rpc-literal, then look at namespace attribute on soapbind:body
      if (WSDLUtils.isRpcLiteral(bindingStyle, bindingOperation))
      {
        // Get soapbind:body element 
        SOAPBody soapBody = WSDLUtils.getInputSoapBody(bindingOperation);

        if ((soapBody.getNamespaceURI() != null)
          && (soapBody.getNamespaceURI().equals(soapOpNS)))
        {
          namespaceFound = true;
        }
      }

      // If doc-literal, then check namespace
      else if (WSDLUtils.isDocLiteral(bindingStyle, bindingOperation))
      {
        int partCount;
        Iterator partsIterator = null;

        // Get first part name from soapbind:body element parts attribute
        String partName = getFirstPartName(bindingOperation);

        // Get the list of parts from the message           
        Map partsMap = null;
        if ((partsMap =
          bindingOperation.getOperation().getInput().getMessage().getParts())
          != null)
        {
          partsIterator = partsMap.values().iterator();

          // If the part name wasn't specified on the soapbind:body element, then process just the first one
          if (partName == null)
            partCount = 1;
          else
            partCount = partsMap.size();

          for (int i = 0; i < partCount && !namespaceFound; i++)
          {
            // Get next part
            Part part = (Part) partsIterator.next();

            // If part name matches or there is no part name, then check namespace
            if ((partName == null)
              || ((partName != null) && (part.getName().equals(partName))))
            {
              if ((part.getElementName().getNamespaceURI() != null)
                && (part.getElementName().getNamespaceURI().equals(soapOpNS)))
              {
                namespaceFound = true;
              }
            }
          }
        }
      }
    }

    return namespaceFound;
  }

  /** 
  * Correlation Check 4 : Service Description "operation"
  *
  * Check if the message content matches any of the candidate operation definitions 
  * for request and response in the Service Description.
  *  
  * For both request and response, look for the soap body child element name
  * in the list of candidate operation names.
  * @param binding        a binding.
  * @param requestMessage a request message.
  * @return true if the message content matches any of the candidate 
  *         operation definitions for request and response in the 
  *         Service Description.
  * @throws WSIException if problem occurs during correlationcheck.
  */
  private boolean operationCorrelation(
    Binding binding,
    Document requestMessage)
    throws WSIException
  {
    boolean operationFound = false;

    String bindingStyle = WSIConstants.ATTRVAL_SOAP_BIND_STYLE_DOC;

    // Get soapbind:binding
    SOAPBinding soapBinding = null;
    if ((soapBinding = WSDLUtils.getSoapBinding(binding)) != null)
    {
      bindingStyle = soapBinding.getStyle();
    }

    // Get the first child element from the soap body
    Element soapBodyChild = getSoapBodyChild(requestMessage);

    // If there is a child element, then check it
    if (soapBodyChild != null)
    {
      // Get operation name from the soap:body
      String operationName = soapBodyChild.getLocalName();

      // Get operation QName
      QName operationQName =
        new QName(soapBodyChild.getNamespaceURI(), operationName);

      // Go through each operation in the binding and check for namespace match
      Iterator operations = binding.getBindingOperations().iterator();
      while (operations.hasNext() && !(operationFound))
      {
        BindingOperation bindingOperation =
          (BindingOperation) operations.next();

        // If rpc-literal, then look at namespace attribute on soapbind:body
        if (WSDLUtils.isRpcLiteral(bindingStyle, bindingOperation))
        {
          // Get soapbind:body element 
          if (bindingOperation.getName().equals(operationName))
            operationFound = true;
        }

        // If doc-literal, then check namespace
        else if (WSDLUtils.isDocLiteral(bindingStyle, bindingOperation))
        {
          int partCount;
          Iterator partsIterator = null;

          // Get first part name from soapbind:body element parts attribute
          String partName = getFirstPartName(bindingOperation);

          // Get the list of parts from the message           
          Map partsMap = null;
          if ((partsMap =
            bindingOperation.getOperation().getInput().getMessage().getParts())
            != null)
          {
            partsIterator = partsMap.values().iterator();

            // If the part name wasn't specified on the soapbind:body element, then process just the first one
            if (partName == null)
              partCount = 1;
            else
              partCount = partsMap.size();

            for (int i = 0; i < partCount && !operationFound; i++)
            {
              // Get next part
              Part part = (Part) partsIterator.next();

              // If part name matches or there is no part name, then check namespace
              if ((partName == null)
                || ((partName != null) && (part.getName().equals(partName))))
              {
                if ((part.getElementName() != null)
                  && (part.getElementName().equals(operationQName)))
                {
                  operationFound = true;
                }
              }
            }
          }
        }
      }
    }

    // Go through each operation to find a match
    return operationFound;
  }

  /**
   * Create failure detail.
   * @param message       a message.
   * @param entryContext  an entry context.
   * @return failure detail.
   */
  public FailureDetail createFailureDetail(
    String message,
    EntryContext entryContext)
  {
    FailureDetail failureDetail = reporter.createFailureDetail();
    failureDetail.setFailureMessage(message);
    failureDetail.setElementLocation(
      entryContext.getMessageEntry().getElementLocation());
    return failureDetail;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.validator.impl.BaseValidatorImpl#isNotApplicable(org.wsi.test.profile.TestAssertion)
   */
  protected boolean isNotApplicable(TestAssertion testAssertion)
  {
    boolean notApplicable = false;

    // If the additional entry is not available, then set notApplicable
    if (testAssertion.getAdditionalEntryTypeList().getWSDLInput() != null
      && !testAssertion.getAdditionalEntryTypeList().getWSDLInput().equals("none")
      && analyzerContext.getServiceReference().getWSDLLocation() == null)
    {
      notApplicable = true;
    }

    return notApplicable;
  }


  /**
   * Get POST request.
   * @param httpHeader an HTTP 
   * @return POST request.
   */
  public Vector getPostRequest(String httpHeader)
  {
    //Request-Line   = Method SP Request-URI SP HTTP-Version CRLF
    Vector requestLine = new Vector();
    String startLine = null;

    StringTokenizer httpMessageTokenizer =
      new StringTokenizer(httpHeader, "\n\r\f");

    if (httpMessageTokenizer.hasMoreTokens())
    {
      startLine = httpMessageTokenizer.nextToken();
    }

    if (startLine.startsWith("POST"))
    {
      StringTokenizer startLineTokenizer =
        new StringTokenizer(startLine, "\u0020");
      while (startLineTokenizer.hasMoreTokens())
      {
        requestLine.add(startLineTokenizer.nextToken());
      }
    }
    return requestLine;
  }

  /**
   * Get HTTP request.
   * @param httpHeader an HTTP 
   * @return HTTP request.
  */
  private Vector getHttpRequest(String httpHeader)
  {
    //Request-Line   = Method SP Request-URI SP HTTP-Version CRLF
    Vector requestLine = new Vector();
    String startLine = null;

    StringTokenizer httpMessageTokenizer =
      new StringTokenizer(httpHeader, "\n\r\f");

    if (httpMessageTokenizer.hasMoreTokens())
    {
      startLine = httpMessageTokenizer.nextToken();
    }

    if (startLine.startsWith("POST") || startLine.startsWith("GET"))
    {
      StringTokenizer startLineTokenizer =
        new StringTokenizer(startLine, "\u0020");
      while (startLineTokenizer.hasMoreTokens())
      {
        requestLine.add(startLineTokenizer.nextToken());
      }
    }
    else
    {
      requestLine = null; // signify not POST or GET
    }

    return requestLine;
  }

  /**
   * Get HTTP request type.
   * @param entryContext an entry context.
   * @return HTTP request type.
   */
  private String getHttpRequestType(EntryContext entryContext)
  {
    //Request-Line   = Method SP Request-URI SP HTTP-Version CRLF

    String httpHeader = entryContext.getRequest().getHTTPHeaders();
    String httpRequestType = null;
    String startLine = null;

    StringTokenizer httpMessageTokenizer =
      new StringTokenizer(httpHeader, "\n\r\f");

    if (httpMessageTokenizer.hasMoreTokens())
    {
      startLine = httpMessageTokenizer.nextToken();
      StringTokenizer startLineTokenizer =
        new StringTokenizer(startLine, "\u0020");
      if (startLineTokenizer.hasMoreTokens())
      {
        httpRequestType = startLineTokenizer.nextToken();
      }
    }
    return httpRequestType;
  }

  /**
   * Determine if the message is a response for a one-way operation.
   * @param entryContext an entry context.
   * @return true if the message is a response for a one-way operation.
   */
  public boolean isOneWayResponse(EntryContext entryContext)
  {
    boolean oneway = false;

    // TEMP: If this is a response message and there is no content, then set one-way to true
    // TEMP: Need to find a way to determine if the response is for a one-way message
    if (entryContext
      .getMessageEntry()
      .getType()
      .equalsIgnoreCase(MessageEntry.TYPE_RESPONSE)
      && entryContext.getMessageEntry().getMessage().length() == 0)
    {
      oneway = true;
    }

    return oneway;
  }

  /**
   * messageIsDocLitSchemaValid.
   *
   * Validates the doc-lit messages against the schema found in a candidate wsdl document.
   *
   * Identify (or be given) a reference in the wsdl to elements (found in &lt;wsdl:types&gt;&lt;schema&gt;...)
   * that are immediate children elements in the soap body.
   * @param entryContext an entry context.
   * @return true if the document literal message is compliant to the 
   *         schema found in a candidate WSDL document.
   * @throws WSIException if there is a parsing problem during validation.
   */
  public boolean messageIsDocLitSchemaValid(EntryContext entryContext)
    throws WSIException
  {

    // This method should use a validating parser that is capable of acceppting multiple schema references
    // programmatically. Then, every schema in candidate wsdl can be passed in to the validator. This avoids the
    // need to pinpoint a particular schema element from all candidate wsdl:types, corresponding to the 
    // wsdl:operation being used. The pinpointing is an incomplete workaround for parsers not capable 
    // of accepting arrays of scehma references 
    // [ i.e. the DOM factory.setAttribute(JAXP_SCHEMA SOURCE, Object[] {...}) ]
    // get all xs:schema from all candidate wsdl:types                    
    // add each <schema> to an array and pass this array to the validating parser
    // when validating the/each soap body child.

    // use the referenced WSDL file and its imported files to find schemas
    List schemaWSDLs = new ArrayList();
    List inlineSchemas = new ArrayList();
    List schemaStrings = new ArrayList();

    Definition[] allDefs =
      analyzerContext.getCandidateInfo().getDefinitions();
    for (int thisDef = 0; thisDef < allDefs.length; thisDef++)
    {
      schemaWSDLs.add(allDefs[thisDef].getDocumentBaseURI());
    }
    Iterator fileIterator = schemaWSDLs.iterator();
    while (fileIterator.hasNext())
    {
      // parse file if possible
      Document wsdlDoc = null;
      String wsdlURI = (String) fileIterator.next();
      try
      {
        wsdlDoc = XMLUtils.parseXMLDocumentURL(wsdlURI, null);
      }
      catch (Exception e)
      {
        continue;
      }
      Element root = wsdlDoc.getDocumentElement();
      // find the schema
      NodeList schemaElements = root.getElementsByTagNameNS(
        WSITag.ELEM_XSD_SCHEMA.getNamespaceURI(),
        WSITag.ELEM_XSD_SCHEMA.getLocalPart());

      for (int elem = 0; elem < schemaElements.getLength(); elem++)
      {     
        Element schema = (Element) schemaElements.item(elem);
        // copying all the NS declarations from wsdl:definitions wsdl:types
        copyNSDeclarations(root, schema);
        copyNSDeclarations((Element) schema.getParentNode(), schema);
        // Replacing all relative schemaLocation URIs with absolute ones
        replaceRelativeURIs(schema, wsdlURI);
        inlineSchemas.add(schema);
      }
    }

    // Note that the Xerces parser ONLY accepts an array
    // of schemas with unique namespaces.
    if (!duplicateNamespacesDetected(inlineSchemas))
    {
      // Serialize the schema elements inside the Types, then use this as
      // the schema string for the validation
      Iterator i = inlineSchemas.iterator();
      while (i.hasNext())
      {
    	Element schema = (Element)i.next(); 
        String schemaString = DOM2Writer.nodeToString(schema);
        schemaStrings.add(schemaString);
      }

      NodeList elementList = entryContext.getMessageEntryDocument()
        .getElementsByTagNameNS(WSITag.ELEM_SOAP_BODY.getNamespaceURI(),
            WSITag.ELEM_SOAP_BODY.getLocalPart());
      if (elementList == null || elementList.getLength() != 1)
      {
        // should only be a single soap body !
        return false; // probably an error condition though
      }

      NodeList soapBodyChildList = ((Element)elementList.item(0)).getChildNodes();
      for (int child = 0; child < soapBodyChildList.getLength(); child++)
      {
        Node soapBodyChild = soapBodyChildList.item(child);
        if (soapBodyChild.getNodeType() == Node.ELEMENT_NODE)
        {
          // do all for now
          try
          {
            // Write out element tree to String
            String messageContent = DOM2Writer.nodeToString(soapBodyChild);
            // parse the child element, validating against the schema 
            XMLUtils.parseXML(messageContent, schemaStrings);
          }
          catch (WSIException e)
          {
            if (e.getTargetException() instanceof SAXException)
            {
              // validation failed
              throw new WSIException(e.getTargetException().getMessage());
            }
            throw e;
          }
          catch (Exception e)
          {
            throw new WSIException("Validating Parsing problem", e);
            // Bad things have happened
          }
        }
      }   
    }
    return true;
  }

  /**
   * Copy the namespace declarations.
   * @param parent a message.
   * @param child a stripped message.
   */
  private void copyNSDeclarations(Element parent, Element child)
  {
    NamedNodeMap nodeMap = parent.getAttributes();
    for (int nodeId = 0; nodeId < nodeMap.getLength(); nodeId++)
    {
      Node node = nodeMap.item(nodeId);

      if ((node.getNodeType() == Node.ATTRIBUTE_NODE)
        && (node.getNodeName().startsWith("xmlns:")))
      {
        String nodeName = node.getNodeName();
        // If an NS being copied is not the same as the child element has, copy it
        if (!child.getNodeName().startsWith(
          nodeName.substring(nodeName.indexOf(":") + 1) + ":"))
        {
          child.setAttribute(nodeName, node.getNodeValue());
        }
      }
    }
  }

  /**
   * Replaces all relative URIs for schemaLocation attributes.
   * @param schema an xsd:schema element.
   * @param wsdlURI an URI of WSDL that contains xsd:schema being processed.
   */
  private void replaceRelativeURIs(Element schema, String wsdlURI)
  {
    // Retrieving all xsd:import elements
    NodeList imports = schema.getElementsByTagNameNS(
      WSITag.ELEM_XSD_IMPORT.getNamespaceURI(),
      WSITag.ELEM_XSD_IMPORT.getLocalPart());
    // Going through the elements
    for (int i = 0; i < imports.getLength(); i++)
    {
      Element imp = (Element) imports.item(i);
      // Getting the schemaLocation attribute
      Attr schemaLocation =
        XMLUtils.getAttribute(imp, WSITag.ATTR_XSD_SCHEMALOCATION);
      // If the attribute is present
      if (schemaLocation != null)
      {
        // Trying to create an URI object using attribute's value
        URI uri = null;
        try
        {
          uri = new URI(schemaLocation.getValue());
        }
        catch (Exception e) {}
        // If the value is not an absolute URI (the URI constructor throws the
        // MalformedURIException), creating the absolute URI using wsdlURI
        if (uri == null)
        {
          String newURI = wsdlURI.substring(0, wsdlURI.lastIndexOf("/") + 1)
            + schemaLocation.getValue();
          try
          {
            uri = new URI(newURI);
            // Setting a new URI as a value for the schemaLocation attribute
            schemaLocation.setValue(uri.toString());
          }
          catch (Exception e) {}
        }
      }
    }
  }

  /**
   * Returns binding operation matched for SOAP message.
   * @param entryType message entry type.
   * @param doc a message.
   * @return any binding operation matched, null if it is not found.
   */
  public BindingOperation getOperationMatch(EntryType entryType, Document doc) 
  {
    BindingOperation bindingOperation = null;
    try
    {
      // Getting the name of the first SOAP Body child element
      QName operation = getOperationFromMessage(doc);
      if (operation != null)
      {
        // Retrieving all the RPC binding operations from wsdl:binding
        BindingOperation[] rpcBindingOps = getMatchingBindingOps(
          WSIConstants.ATTRVAL_SOAP_BIND_STYLE_RPC,
          analyzerContext.getCandidateInfo().getBindings());
        // Retrieving binding operation by the given operation name
        bindingOperation = getOperationMatch(
          entryType, operation, rpcBindingOps);
        // If no one RPC operation matched
        if(bindingOperation == null)
        {
          // Retrieving all the document binding operations from wsdl:binding
          BindingOperation[] docBindingOperations = getMatchingBindingOps(
            WSIConstants.ATTRVAL_SOAP_BIND_STYLE_DOC,
            analyzerContext.getCandidateInfo().getBindings());
          // Retrieving binding operation by given element name
          BindingOperation[] potentialDocLitOps =
            getDocLitOperations(entryType, operation, docBindingOperations);
          // If there is exactly one operation matched
          if (potentialDocLitOps.length == 1)
            bindingOperation = potentialDocLitOps[0];
        }
      }
    }
    catch (Exception e) {}

    return bindingOperation;
  }

  /**
   * Match either Input or Output.
   * @param messageEntryType message entry type.
   * @param soapMessage soap message.
   * @param op binding operations.
   * @return matched operation.
   */
  public BindingOperation getOperationMatch(
    EntryType messageEntryType,
    Document soapMessage,
    BindingOperation[] op)
  {
    // Get operation from message
    QName operation = getOperationFromMessage(soapMessage);
    // list of QNames

    return getOperationMatch(messageEntryType, operation, op);
  }

  /**
   * Match either Input or Output.
   * @param messageEntryType message entry type.
   * @param operation SOAP operation name.
   * @param op binding operations.
   * @return matched operation.
   */
  public BindingOperation getOperationMatch(
    EntryType messageEntryType,
    QName operation,
    BindingOperation[] op)
  {
    QName checkOperation;

    // Look for a candidate operation in the wsdl that matches this signature.
    for (int k = 0; k < op.length; k++)
    {
      String name = null;
      List extensibles = null;
      BindingOperation bindingOp = op[k];

      if (messageEntryType.isType(MessageValidator.TYPE_MESSAGE_REQUEST)
        && (bindingOp.getOperation().getInput() != null))
      {
        name = bindingOp.getOperation().getName();
        extensibles = bindingOp.getBindingInput().getExtensibilityElements();
      }
      else if (
        messageEntryType.isType(MessageValidator.TYPE_MESSAGE_RESPONSE)
          && (bindingOp.getOperation().getOutput() != null))
      {
        name = bindingOp.getOperation().getName() + "Response";
        extensibles = bindingOp.getBindingOutput().getExtensibilityElements();
      }

      // Get soap:body
      SOAPBody soapBody = getSoapBody(extensibles);
      if (soapBody == null)
        checkOperation = new QName(name);
      else
        checkOperation = new QName(soapBody.getNamespaceURI(), name);

      if (operation != null && operation.equals(checkOperation))
      {
        return (bindingOp);
      }
    }
    return null;
  }

  /**
   * Match either Input or Output.
   * @param messageEntryType a message entry type.
   * @param soapMessage a soap message.
   * @param op binsing operations.
   * @return matched operation.
   */
  public BindingOperation getOperationPartsMatch(
    EntryType messageEntryType,
    Document soapMessage,
    BindingOperation[] op)
  {
    Map parts = getPartListFromMessage(soapMessage); // list of QNames

    // Look for a candidate operation in the wsdl that matches this signature.
    for (int k = 0; k < op.length; k++)
    {

      Message wsdlMessage = null;
      List extensibles = null;
      BindingOperation bindingOp = op[k];

      if (messageEntryType.isType(MessageValidator.TYPE_MESSAGE_REQUEST)
        && (bindingOp.getOperation().getInput() != null))
      {
        wsdlMessage = bindingOp.getOperation().getInput().getMessage();
        extensibles = bindingOp.getBindingInput().getExtensibilityElements();
      }
      else if (
        messageEntryType.isType(MessageValidator.TYPE_MESSAGE_RESPONSE)
          && (bindingOp.getOperation().getOutput() != null))
      {
        wsdlMessage = bindingOp.getOperation().getOutput().getMessage();
        extensibles = bindingOp.getBindingOutput().getExtensibilityElements();
      }
      //wsdlFaultParts = op[k].getFaults();
      // ADD: check for case when response message is a fault

      if (sameParts(parts, wsdlMessage, getSoapHeader(extensibles)))
      {
        return (bindingOp);
      }
    }
    return null;
  }

  /** 
   * BindingOperation getOperationPartsMatch(..).
   * 
   * Find one or more matching binding operations from the WSDL corresponding to the
   * given request-response signature.
   * 
   * This overloaded version is intended for possible Correlation only (not assertions),
   * since it checks for an operation signature on a request-response pair.
   * Assertions now check request & response messages independently. 
   * @param requestMessage a request message.
   * @param responseMessage a response message.
   * @param op binding operations.
   * @return matched operation.
   */
  //private BindingOperation getOperationPartsMatch(
  //  Document requestMessage,
  //  Document responseMessage,
  //  BindingOperation[] op)
  //{
  //  Map inParts = getPartListFromMessage(requestMessage); // list of QNames
  //  Map outParts = getPartListFromMessage(responseMessage);
  //  // list of QNames
  //
  //  // Look for a candidate operation in the wsdl that matches this signature.
  //  for (int k = 0; k < op.length; k++)
  //  {
  //
  //    //Map wsdlFaultParts = null;
  //    Message wsdlInMessage = null;
  //    Message wsdlOutMessage = null;
  //
  //    BindingOperation bindingOp = op[k];
  //    if (bindingOp.getOperation().getInput() != null)
  //    {
  //      wsdlInMessage = bindingOp.getOperation().getInput().getMessage();
  //    }
  //
  //    if (bindingOp.getOperation().getOutput() != null)
  //    {
  //      wsdlOutMessage = bindingOp.getOperation().getOutput().getMessage();
  //    }
  //    //wsdlFaultParts = op[k].getFaults();
  //    // ADD: check for case when response message is a fault
  //
  //    if (sameParts(inParts,
  //      wsdlInMessage,
  //      getSoapHeader(bindingOp.getBindingInput().getExtensibilityElements())))
  //    {
  //      // match on the request - now check response if its not a fault
  //      if (responseMessage != null && isFault(responseMessage))
  //      {
  //        return (bindingOp);
  //      }
  //      else if (
  //        sameParts(
  //          outParts,
  //          wsdlOutMessage,
  //          getSoapHeader(
  //            bindingOp.getBindingOutput().getExtensibilityElements())))
  //      {
  //        // It does match so treat this as a relevant message pair. 
  //        // Let the message through for validation.
  //        //ADD: could pass the operations list back to the entryContext for the message.
  //        return (bindingOp);
  //      }
  //    }
  //  }
  //  return null;
  //}

  /**
   * Get the soap:body from a List of extensibility elements.
   * @param extElems a list of extensibility elements.
   * @return the soap:body from a List of extensibility elements.
   */
  private SOAPBody getSoapBody(List extElems)
  {
    // Find the soap body
    if (extElems != null)
    {
      for (int i = 0; i < extElems.size(); i++)
      {
        ExtensibilityElement extElem = (ExtensibilityElement) extElems.get(i);
        if (extElem.getElementType().equals(WSITag.WSDL_SOAP_BODY))
        {
          return (SOAPBody) extElem;
        }
        // If the element is mime:multipartRelated
        else if (extElem.getElementType().equals(WSITag.WSDL_MIME_MULTIPART))
        {
          // Getting the mime:part elements of the mime:multipartRelated
          List mimeParts = ((MIMEMultipartRelated) extElem).getMIMEParts();
          // Going through all the mime:part elements
          for (int j = 0; j < mimeParts.size(); j++)
          {
            // Collecting all the mime:content elements of this mime:part
            SOAPBody soapBody = getSoapBody(
              ((MIMEPart) mimeParts.get(j)).getExtensibilityElements());
            if (soapBody != null)
              return soapBody;
          }
        }
      }
    }

    return null;
  }

  /**
   * Get the SOAPHeader from a List of extensibility elements.
   * @param extensibles a list of extensibility elements.
   * @return the SOAPHeader from a List of extensibility elements.
   */
  private SOAPHeader getSoapHeader(List extensibles)
  {
    SOAPHeader soapHeader = null;

    // find the soap header
    if (extensibles != null)
    {
      Iterator i = extensibles.iterator();
      while (i.hasNext() && soapHeader == null)
      {
        try
        {
          soapHeader = (SOAPHeader) i.next();
        }
        catch (ClassCastException c)
        {
        }
      }
    }
    return soapHeader;
  }

  /**
   * Get first part name from soapbind:body element.
   */
  private String getFirstPartName(BindingOperation bindingOperation)
  {
    String partName = null;
    List extList = null;
    Iterator partsIterator = null;

    // Determine if there is a list of parts by finding the soapbind:body element
    if ((extList =
      bindingOperation.getBindingInput().getExtensibilityElements())
      != null)
    {
      List partsList = null;
      Iterator extIterator = extList.iterator();
      while (extIterator.hasNext() && (partName == null))
      {
        Object extElement = extIterator.next();
        if (extElement instanceof SOAPBody)
        {
          if ((partsList = ((SOAPBody) extElement).getParts()) != null)
          {
            partsIterator = partsList.iterator();

            // Since this is a doc literal binding there should be only one part name
            if (partsIterator.hasNext())
              partName = (String) partsIterator.next();
          }
        }
      }
    }

    return partName;
  }

  public BindingOperation[] getDocLitOperations(
    EntryType messageType,
    QName partElementQName,
    BindingOperation[] wsdlOperations)
  {

    if (messageType.isType(EntryType.getEntryType(MessageValidator.TYPE_MESSAGE_REQUEST)))
    {
      return getInputDocLitOperations(partElementQName, wsdlOperations);
    }
    else if (messageType.isType(EntryType.getEntryType(MessageValidator.TYPE_MESSAGE_RESPONSE)))
    {
      return getOutputDocLitOperations(partElementQName, wsdlOperations);
    }
    else
    {
      return null; // should be one or the other
    }
  }

  private BindingOperation[] getInputDocLitOperations(
    QName partElementQName,
    BindingOperation[] wsdlOperations)
  {

    Vector potentialOps = new Vector();

    for (int i = 0; i < wsdlOperations.length; i++)
    {
      if (wsdlOperations[i].getOperation().getInput() != null)
      {
        Message message = wsdlOperations[i].getOperation().getInput().getMessage();
        // If SOAP Body child element is not present and wsdl:message does not have any wsdl:parts, that is the match 
        if (partElementQName == null)
        {
          if (message.getParts().isEmpty())
            potentialOps.add(wsdlOperations[i]);
          continue;
        }

        Iterator partsIt = message.getParts().values().iterator();
        while (partsIt.hasNext())
        {
          Part nextPart = (Part) partsIt.next();
          if (partElementQName.equals(nextPart.getElementName()))
          {
            // matching part found (doc-lit) - add to list of possible operation matches
            potentialOps.add(wsdlOperations[i]);
            break;
          }
        }
      }
    }
    return (BindingOperation[]) potentialOps.toArray(new BindingOperation[0]);
  }

  private BindingOperation[] getOutputDocLitOperations(
    QName partElementQName,
    BindingOperation[] wsdlOperations)
  {

    Vector potentialOps = new Vector();

    for (int i = 0; i < wsdlOperations.length; i++)
    {
      if (wsdlOperations[i].getOperation().getOutput() != null)
      {
        Message message = wsdlOperations[i].getOperation().getOutput().getMessage();
        // If SOAP Body child element is not present and wsdl:message does not have any wsdl:parts, that is the match
        if (partElementQName == null)
        {
          if (message.getParts().isEmpty())
            potentialOps.add(wsdlOperations[i]);
          continue;
        }

        Iterator partsIt = message.getParts().values().iterator();
        while (partsIt.hasNext())
        {
          Part nextPart = (Part) partsIt.next();
          if (partElementQName.equals(nextPart.getElementName()))
          {
            // matching part found (doc-lit) - add to list of possible operation matches
            potentialOps.add(wsdlOperations[i]);
            break;
          }
        }
      }
    }
    return (BindingOperation[]) potentialOps.toArray(new BindingOperation[0]);
  }

  /**
   * Compare soap message element part names with Parts from specified wsdl Operation
   */
  //private boolean sameParts(HashSet messageParts, Map wsdlParts)
  //{
  //
  //  // look for the soap-message operation signature
  //  Iterator i = wsdlParts.values().iterator();
  //  // build a set of Part names
  //  HashSet h = new HashSet();
  //  while (i.hasNext())
  //  {
  //    Part p = (Part) i.next();
  //    h.add(p.getName());
  //  }
  //
  //  // compare with the parts list from the message	(unordered)
  //  return (h.equals(messageParts));
  //}

  /**
   * Compare soap message element part names with Parts from specified wsdl Operation
   */
  private boolean sameParts(
    Map messageParts,
    Message wsdlMessage,
    SOAPHeader soapHeader)
  {

    String soapHeaderPart = null;
    if (soapHeader != null)
      soapHeaderPart = soapHeader.getPart();

    // check null conditions
    if (messageParts == null && wsdlMessage == null)
    {
      return true; // simple equality test
    }
    else if (messageParts == null || wsdlMessage == null)
    {
      return false;
    }

    Vector v = new Vector();
    //List wsdlParts = wsdlMessage.getOrderedParts(null);
    Map wsdlParts = null;
    if (wsdlMessage.getParts() != null)
    {
      wsdlParts = wsdlMessage.getParts();
      // look for the soap-message operation signature
      Iterator i = wsdlParts.values().iterator();
      // build a set of Part names    
      while (i.hasNext())
      {
        Part p = (Part) i.next();
        // do not include the part for the soap:header (if any)
        if (!p.getName().equals(soapHeaderPart))
        {
          // check that the part is associated with the soap:body
          v.add(new QName(p.getName()));
        }
      }
      // if one of the parts is associated with a soap:header in the binding
      // (by <soap:header part="{partname}">), remove this from the set

    }

    // compare with the parts list from the message	(ordered)
    if (v.isEmpty() && messageParts.isEmpty())
    {
      return true;
    }

    // PB: Changed to containsAll() since equals() fails when using JRE 1.4
    if (v.containsAll(messageParts.keySet())
      && (v.size() == messageParts.size()))
    {
      // Check for xsi:type mismatch
      Iterator parts = messageParts.keySet().iterator();
      QName partName, xsiType;
      while (parts.hasNext())
      {
        partName = (QName) parts.next();

        // Get xsi:type QName
        if ((xsiType = (QName) messageParts.get(partName)) != null
          && wsdlParts != null)
        {
          // Get the WSDL part definition
          Part part = (Part) wsdlParts.get(partName.getLocalPart());

          // If xsiType is NOT derived from the type of the corresponding WSDL part
          if (!isDerivedType(xsiType, part.getTypeName()))
          {
            // return false
            return false;
          }
        }
      }

      return true;
    }
    else
      return false;
    //return (v.equals(messageParts));
  }

  /**
   * Checks whether one schema type is derived from another.
   * @param extType an assumed derived type.
   * @param type an assumed base type.
   * @return true if extType is derived from type, false otherwise
   */
  public boolean isDerivedType(QName extType, QName type)
  {
    // If either of types is null, return false
    if (extType == null || type == null)
      return false;
    // If the types are equal, return true
    if (extType.equals(type))
      return true;

    // Going through all schemas 
    Iterator i = wsdlDocument.getSchemas().values().iterator();
    while (i.hasNext())
    {
      XSModel xsModel = (XSModel) i.next();
      // Retrieving the derived type definition
      XSTypeDefinition xsType = xsModel.getTypeDefinition(
        extType.getLocalPart(), extType.getNamespaceURI());
      // If it is found and derived from the base type, return true
      if (xsType != null && xsType.derivedFrom(type.getNamespaceURI(),
        type.getLocalPart(), XSConstants.DERIVATION_NONE))
      {
        return true;
      }
    }
    // extType is not derived from type, return false
    return false;
  }

  /**
  	* Get a list of QNames of parts from the soap body of the specified message
  	*/
  public Element getSoapBodyChild(Document doc)
  {

    Element opElem = null;
    if (doc != null)
    {
      Element root = doc.getDocumentElement();
      NodeList bodies =
        root.getElementsByTagNameNS(WSIConstants.NS_URI_SOAP, "Body");
      // Get the list of soap:body child element names from the request message
      if (bodies != null && bodies.getLength() > 0)
      {
        Element body = (Element) bodies.item(0);
        NodeList children = body.getChildNodes();
        for (int i = 0; i < children.getLength() && opElem == null; ++i)
        {
          Node n = children.item(i);
          if (n instanceof Element)
          {
            opElem = (Element) n;
          }
        }
      }
    }
    return opElem;
  }

  /** Check whether this message is a soap fault
   */
  public boolean isFault(Document doc)
  {
    boolean isFault = false;

    if (doc != null)
    {
      Element root = doc.getDocumentElement();
      isFault =
        (root
          .getElementsByTagNameNS(WSIConstants.NS_URI_SOAP, "Fault")
          .getLength()
          > 0);
    }

    return isFault;
  }

  /** 
   * Check whether this message has a soap body with a child element.
   */
  public boolean containsSoapBodyWithChild(Document doc)
  {
    boolean contains = false;

    if (doc != null)
    {
      contains = ((getSoapBodyChild(doc) == null) ? false : true);
    }

    return contains;
  }

  /**
   * Get SOAPAction value from the HTTP headers.
   * @param headers HTTP headers
   * @return SOAPAction value
   */
  public String getSoapAction(String headers) throws WSIException
  {
    // get SOAPAction
    String action = null;
    if (headers != null)
      action = (String) HTTPUtils.getHttpHeaderTokens(headers, ":").get("SOAPAction".toUpperCase());
    return action;
  }

  /**
   * Get a list of QNames of parts from the soap body of the specified message.
   * This method assumes RPC style message content
   */
  private QName getOperationFromMessage(Document doc)
  {
    QName operation = null;
    if (doc != null)
    {
      Element root = doc.getDocumentElement();
      NodeList bodies =
        root.getElementsByTagNameNS(WSIConstants.NS_URI_SOAP, "Body");
      if (bodies != null && bodies.getLength() > 0)
      {
        Element body = (Element) bodies.item(0);
        NodeList children = body.getChildNodes();
        for (int i = 0; i < children.getLength(); ++i)
        {
          Node n = children.item(i);
          // If element, then this is the operation name
          if (n instanceof Element)
          {
            operation = new QName(n.getNamespaceURI(), n.getLocalName());
          }
        }
      }
    }

    return operation;
  }

  /**
   * Get a list of QNames of parts from the soap body of the specified message.
   * This method assumes RPC style message content
   */
  private Map getPartListFromMessage(Document doc)
  {
    Map parts = new HashMap();
    if (doc != null)
    {
      Element root = doc.getDocumentElement();
      NodeList bodies =
        root.getElementsByTagNameNS(WSIConstants.NS_URI_SOAP, "Body");
      // Get the list of soap:body grand-child element names from the request message
      // (immediate child is the message name)
      if (bodies != null && bodies.getLength() > 0)
      {
        Element body = (Element) bodies.item(0);
        NodeList children = body.getChildNodes();
        for (int i = 0; i < children.getLength(); ++i)
        {
          Node n = children.item(i);
          if (n instanceof Element)
          {
            // this is the operation name. Its children are the parts
            NodeList grandChildren = n.getChildNodes();
            for (int j = 0; j < grandChildren.getLength(); j++)
            {
              Node m = grandChildren.item(j);
              if (m instanceof Element)
              {
                // Determine if the part has an xsi:type
                Attr attr =
                  XMLUtils.getAttribute(
                    (Element) m,
                    new QName(WSIConstants.NS_URI_XSI, "type"));

                QName xsiType = null;

                // If there is an xsi:type attribute, then get the value as a QName 
                try
                {
                  if (attr != null)
                    xsiType =
                      DOMUtils.getQName(attr.getNodeValue(), (Element) m, wsdlDocument.getDefinitions());
                }
                catch (javax.wsdl.WSDLException we)
                {
                }

                // add to the child element list
                parts.put(
                  new QName(m.getNamespaceURI(), m.getLocalName()),
                  xsiType);
              }
            }
          }
        }
      }
    }
    return parts;
  }

  public BindingOperation[] getMatchingBindingOps(
    String bindingStyle,
    Binding[] bindings)
    throws WSIException
  {

    HashSet bindingOperationsSet = new HashSet();

    // whizz through the bindings, checking for a bindingOperation matching the message
    for (int i = 0; i < bindings.length; i++)
    {

      Binding tryBinding = bindings[i];
      List bindingOps = tryBinding.getBindingOperations();

      if (bindingOps != null)
      {

        // search through binding Operations
        Iterator bindingOpIt = tryBinding.getBindingOperations().iterator();
        while (bindingOpIt.hasNext())
        {

          BindingOperation bindingOp = (BindingOperation) bindingOpIt.next();
          // check depends on which binding style is declared in the wsdl
          SOAPOperation soapOp = WSDLValidatorImpl.getSoapOperation(bindingOp);
          //GT: move this method to utils

          String style;
          if ((soapOp == null) || (style = soapOp.getStyle()) == null)
          {
            // use the style of the parent bindingOp

            SOAPBinding soapBind = WSDLValidatorImpl.getSoapBinding(tryBinding);
            //GT: move this method to utils
            if ((style = soapBind.getStyle()) == null)
            {
              style = WSIConstants.ATTRVAL_SOAP_BIND_STYLE_DOC;
              //default
            }
          }
          if (style.equals(bindingStyle))
          {

            bindingOperationsSet.add(bindingOp);

          }
        }
      } // current binding has no bindingOperations, ignore
    }
    return (BindingOperation[]) bindingOperationsSet.toArray(
      new BindingOperation[0]);
  }

  /**
   * Checks whether soap:body element is literal.
   * @param extElements extensibility elements of wsdl:input or wsdl:output
   * of a binding
   */
  public boolean isLiteral(List extElems)
  {
    SOAPBody soapBody = getSOAPBody(extElems);
    if (soapBody != null
      && soapBody.getUse().equals(WSIConstants.ATTRVAL_SOAP_BODY_USE_LIT))
      return true;
    else
      return false;
  }

  /**
   * Orders wsdl:part names keeping in mind the "parts" attribute of WSDL soap:body
   * @param messageParts a list of wsdl:partS
   * @param extElems extensibility elements in the binding
   * @return the ordered list of part names
   */
  public List orderPartNames(List messageParts, List extElems)
  {
    List orderedPartNames = new ArrayList();
    List bodyParts = null;

    SOAPBody soapBody = getSOAPBody(extElems);
    if (soapBody != null)
      bodyParts = soapBody.getParts();

    Iterator i = messageParts.iterator();
    while (i.hasNext())
    {
      String partName = ((Part) i.next()).getName();
      // If the parts attribute is not specified or contains
      // the wsdl:part name, then adding part name to the ordered list
      if (bodyParts == null || bodyParts.contains(partName))
        orderedPartNames.add(partName);
    }

    return orderedPartNames;
  }

  /**
   * Retrieves the SOAPBody object from a list of extensibility elments in binding
   * @param extElems extensibility elements
   * @return the SOAPBody element
   */
  public SOAPBody getSOAPBody(List extElems)
  {
    if (extElems == null)
      return null;
    for (Iterator i = extElems.iterator(); i.hasNext();)
    {
      Object obj = i.next();
      if (obj instanceof SOAPBody)
        return (SOAPBody) obj;
    }

    return null;
  }
 
  public List resolveSameNamespaces(List schemaElements)
  {
  	List namespaces = new ArrayList();
  	List result = new ArrayList();
  	Iterator i = schemaElements.iterator();
    while (i.hasNext())
    {
      Element schema = (Element) i.next();
      String targetNamespace = schema.getAttribute(Constants.ATTR_TARGET_NAMESPACE);
      if (!namespaces.contains(targetNamespace))
      {
        namespaces.add(targetNamespace);
    	List schemas = getSchemasWithSameNamespace(schemaElements, targetNamespace);
    	if (schemas.size() == 1)
      	{
      	  result.add(schema);
      	}
    	else
    	{
    		// copying all the NS declarations from wsdl:definitions wsdl:types
      //copyNSDeclarations(root, schema);
      copyNSDeclarations((Element) schema.getParentNode(), schema);
      // Replacing all relative schemaLocation URIs with absolute ones
      //replaceRelativeURIs(schema, wsdlURI);
      // Serialize the schema elements inside the Types, then use this as
      // the schema string for the validation
      //String schemaString =
      //  DOM2Writer.nodeToString(schema);
      // schemaStrings.add(schemaString);
      	}
      }
    }
  	return null;
  }
  	
  public List getSchemasWithSameNamespace(List schemaElements, String targetNamespace)
  { 
  	List result = new ArrayList();
    Iterator i = schemaElements.iterator();
    while (i.hasNext())
    {
      Element schema = (Element)i.next();
      String schemaTargetNamespace = schema.getAttribute(Constants.ATTR_TARGET_NAMESPACE);
      if (schemaTargetNamespace == null)
       	schemaTargetNamespace = "";
        
      if (schemaTargetNamespace.equals(targetNamespace))
      {
       	result.add(schema);
      }
    }
    return result;
  }

  public boolean duplicateNamespacesDetected(List schemaElements)
  { 
  	boolean result = false;
    List namespaces = new ArrayList();
    Iterator i = schemaElements.iterator();
    while (i.hasNext())
    {
    	Element schema = (Element)i.next();
        String targetNamespace = schema.getAttribute(Constants.ATTR_TARGET_NAMESPACE);
        if (targetNamespace == null)
        	targetNamespace = "";
        
        if (namespaces.contains(targetNamespace))
        {
        	result = true;
        	break;
        }
        else
        {
        	namespaces.add(targetNamespace);
        }
    }
    return result;
  }

  /**
   * Returns true if these tests should be run (depending on the analyzer
   * config)
   */
  public boolean runTests() { return testable; }
  
  public void validateArtifact() throws WSIException {
      // Get the log file reader
      LogReader logReader = DocumentFactory.newInstance().newLogReader();

      // Create log reader callback
      LogProcessor envelopeProcessor = new LogProcessor(this);

      // Start reading the log file
      logReader.readLog(analyzerConfig.getLogLocation(), envelopeProcessor);
  }

  protected class LogProcessor implements MessageEntryHandler {
      LogValidator validator = null;

    /**
     * Create message processor as a log reader callback function.
     */
      LogProcessor(LogValidator validator) {
          this.validator = validator;
      }

    /**
     * Process artifact reference.
     */
    public void processArtifactReference(ArtifactReference artifactReference)
            throws WSIException {
        reporter.addArtifactReference(artifactReference);
    }

    /**
     * Process a single log entry.
     */
    public void processLogEntry(EntryContext entryContext) throws WSIException {
        validator.validate(entryContext);
    }
  }
}