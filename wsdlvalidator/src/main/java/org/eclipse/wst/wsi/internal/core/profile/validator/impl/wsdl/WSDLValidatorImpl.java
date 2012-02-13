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
package org.eclipse.wst.wsi.internal.core.profile.validator.impl.wsdl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.wsdl.Binding;
import javax.wsdl.BindingFault;
import javax.wsdl.BindingInput;
import javax.wsdl.BindingOperation;
import javax.wsdl.BindingOutput;
import javax.wsdl.Definition;
import javax.wsdl.Fault;
import javax.wsdl.Import;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Part;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.Types;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.schema.Schema;
import javax.wsdl.extensions.soap.SOAPBinding;
import javax.wsdl.extensions.soap.SOAPBody;
import javax.wsdl.extensions.soap.SOAPFault;
import javax.wsdl.extensions.soap.SOAPHeader;
import javax.wsdl.extensions.soap.SOAPHeaderFault;
import javax.wsdl.extensions.soap.SOAPOperation;
import javax.xml.namespace.QName;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.WSIRuntimeException;
import org.eclipse.wst.wsi.internal.core.analyzer.AnalyzerContext;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionFailException;
import org.eclipse.wst.wsi.internal.core.analyzer.CandidateInfo;
import org.eclipse.wst.wsi.internal.core.analyzer.ServiceReference;
import org.eclipse.wst.wsi.internal.core.analyzer.config.AnalyzerConfig;
import org.eclipse.wst.wsi.internal.core.analyzer.config.WSDLElement;
import org.eclipse.wst.wsi.internal.core.profile.ProfileArtifact;
import org.eclipse.wst.wsi.internal.core.profile.ProfileAssertions;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.WSDLValidator;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseValidatorImpl;
import org.eclipse.wst.wsi.internal.core.report.Entry;
import org.eclipse.wst.wsi.internal.core.report.EntryContainer;
import org.eclipse.wst.wsi.internal.core.report.FailureDetail;
import org.eclipse.wst.wsi.internal.core.report.ReportArtifact;
import org.eclipse.wst.wsi.internal.core.report.Reporter;
import org.eclipse.wst.wsi.internal.core.util.EntryType;
import org.eclipse.wst.wsi.internal.core.wsdl.WSDLDocument;
import org.eclipse.wst.wsi.internal.core.wsdl.WSDLElementList;
import org.eclipse.wst.wsi.internal.core.wsdl.WSDLUtils;
import org.eclipse.wst.wsi.internal.core.xml.dom.ElementLocation;
import org.eclipse.wst.wsi.internal.core.xml.schema.TargetNamespaceProcessor;
import org.eclipse.wst.wsi.internal.core.xml.schema.XMLSchemaValidator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * The WSDL validator will verify that the WSDL and associated XML schema definitions
 * are in conformance with the profile.
 *
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 * @author Graham Turrell 	(gturrell@uk.ibm.com)
 */

public class WSDLValidatorImpl
  extends BaseValidatorImpl
  implements WSDLValidator
{
  /**
   * WSDL URL.
   * @deprecated -- access the wsdl url via wsdlDocument field.
   */
  protected String wsdlURL;

  /**
   * WSDL document.
   */
  protected WSDLDocument wsdlDocument = null;
  private boolean testable;
  
  /**
   * Entry container map.
   */
  protected HashMap containerMap = new HashMap();
  
  protected boolean processDefAssertions = true;

  /**
   * Get the artifact type that this validator applies to.
   * @return the artifact type (a String)
   */
  public String getArtifactType() 
  {  
      return TYPE_DESCRIPTION;
  }

  /**
   * Get the collection of entry types that this validator applies to.
   * @return an array of entry types (Strings)
   */
  public String[] getEntryTypes() 
  {
      return new String[] {
              TYPE_DESCRIPTION_DEFINITIONS,
              TYPE_DESCRIPTION_IMPORT,
              TYPE_DESCRIPTION_TYPES,
              TYPE_DESCRIPTION_MESSAGE,
              TYPE_DESCRIPTION_OPERATION,
              TYPE_DESCRIPTION_PORTTYPE,
              TYPE_DESCRIPTION_BINDING,
              TYPE_DESCRIPTION_PORT
      };
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.validator.WSDLValidator#init(org.eclipse.wst.wsi.internal.core.analyzer.AnalyzerContext, org.wsi.test.profile.ProfileArtifact, org.wsi.test.report.ReportArtifact, java.lang.String, org.wsi.wsdl.WSDLDocument, org.wsi.test.report.Reporter)
   */
  public void init(
    AnalyzerContext analyzerContext,
    ProfileAssertions assertions,
    ReportArtifact reportArtifact,
    AnalyzerConfig analyzerConfig,
    Reporter reporter)
    throws WSIException
  {
    init(analyzerContext, assertions, reportArtifact, analyzerConfig, reporter, true);
  }

  public void init(
    AnalyzerContext analyzerContext,
    ProfileAssertions assertions,
    ReportArtifact reportArtifact,
    AnalyzerConfig analyzerConfig,
    Reporter reporter,
    boolean processDefAssertions)
    throws WSIException
  {
    // BaseValidatorImpl
    super.init(analyzerContext, assertions.getArtifact(TYPE_DESCRIPTION), reportArtifact, reporter);
    this.wsdlDocument = analyzerContext.getWsdlDocument();
    testable = analyzerContext.getWsdlDocument() != null;
    if (this.wsdlDocument != null)
      this.wsdlURL = wsdlDocument.getLocation();
    this.processDefAssertions = processDefAssertions;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.validator.WSDLValidator#init(org.eclipse.wst.wsi.internal.core.analyzer.AnalyzerContext, org.wsi.test.profile.ProfileArtifact, org.wsi.test.report.ReportArtifact, java.lang.String, org.wsi.wsdl.WSDLDocument, org.wsi.test.report.Reporter)
   */
  public void init(
    AnalyzerContext analyzerContext,
    ProfileArtifact profileArtifact,
    ReportArtifact reportArtifact,
    String wsdlURL,
    WSDLDocument wsdlDocument,
    Reporter reporter)
    throws WSIException
  {
    init(analyzerContext, profileArtifact, reportArtifact, wsdlURL, wsdlDocument, reporter, true);
  }

  public void init(
    AnalyzerContext analyzerContext,
    ProfileArtifact profileArtifact,
    ReportArtifact reportArtifact,
    String wsdlURL,
    WSDLDocument wsdlDocument,
    Reporter reporter,
    boolean processDefAssertions)
    throws WSIException
  {
    // BaseValidatorImpl
    super.init(analyzerContext, profileArtifact, reportArtifact, reporter);
    this.wsdlDocument = wsdlDocument;
    testable = (wsdlDocument != null);
    if (wsdlDocument != null)
      this.wsdlURL = wsdlDocument.getLocation();

    if (wsdlURL != null)
      this.wsdlURL = wsdlURL;
    this.processDefAssertions = processDefAssertions;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.validator.WSDLValidator#validate()
   */
  public void validateArtifact() throws WSIException
  {
    //WSDLDocument wsdlDocument = null;
    Service service = null;
    Port port = null;
    Binding binding = null;
    PortType portType = null;
    Operation operation = null;
    Message message = null;

    // it depricated after refactoring 
    // now the inner classes moved out from validator
    //String classPrefix = this.getClass().getName() + "$";
    String classPrefix = this.getClass().getPackage().getName()+".";

    // Get the definition element
    Definition definition = wsdlDocument.getDefinitions();

    // Get service reference from analyzer context
    ServiceReference serviceReference = analyzerContext.getServiceReference();

    // Create normalized data about the service under test.
    CandidateInfo candidate = new CandidateInfo(serviceReference, wsdlDocument);

    analyzerContext.setCandidateInfo(candidate);

    // Set prereq type to entry container
    reporter.setPrereqType(Reporter.PREREQ_TYPE_ENTRY_CONTAINER);

    // always process Import, Definitions & Types assertions 
    // TEMP:
    if (processDefAssertions)
    {
      processDefinitionAssertions(classPrefix, candidate);
      processTypesAssertions(classPrefix, candidate);
      processImportAssertions(classPrefix, candidate);
    }

    // Process the element hierarchy in the WSDL document starting with the one that was specified  
    // FIX: Element finding already completed by CandidateInfo constructor - so use that rather than retest here  

    // ---------------------------
    // wsdl:port
    // ---------------------------
    if (serviceReference.getWSDLElement().isPort())
    {
      // Find the service element
      if ((service =
        definition.getService(
          serviceReference.getWSDLElement().getParentElementQName()))
        == null)
      {
        throw new WSIRuntimeException(
          "Could not locate WSDL service: "
            + serviceReference.getWSDLElement().getParentElementName());
      }

      // Find the port element
      if ((port = service.getPort(serviceReference.getWSDLElement().getName()))
        == null)
      {
        throw new WSIRuntimeException(
          "Could not locate WSDL port: "
            + serviceReference.getWSDLElement().getName());
      }

      // TEMP: Remove until there are port test assertions      
      //processPortAssertions(port, serviceReference, classPrefix, wsdlDocument);	     

      // Next, process the binding
      if (((binding = port.getBinding()) == null) || (binding.isUndefined()))
      {
        //throw new WSIRuntimeException("Could not locate WSDL binding for port: " + port.getName());  
        // Set missingInput for all binding, portType, operation and message test assertions 
        setMissingInput(EntryType.getEntryType(TYPE_DESCRIPTION_BINDING));
        setMissingInput(EntryType.getEntryType(TYPE_DESCRIPTION_PORTTYPE));
        setMissingInput(EntryType.getEntryType(TYPE_DESCRIPTION_OPERATION));
        setMissingInput(EntryType.getEntryType(TYPE_DESCRIPTION_MESSAGE));
      }

      else
      {
        processBindingAssertions(
          binding,
          serviceReference,
          classPrefix,
          wsdlDocument);

        // Next, process the portType
        if (((portType = binding.getPortType()) == null)
          || (portType.isUndefined()))
        {
          //throw new WSIRuntimeException("Could not locate WSDL portType for binding: " + binding.getQName().getLocalPart());
          // Set missingInput for all portType, operation and message test assertions 
          setMissingInput(EntryType.getEntryType(TYPE_DESCRIPTION_PORTTYPE));
          setMissingInput(EntryType.getEntryType(TYPE_DESCRIPTION_OPERATION));
          setMissingInput(EntryType.getEntryType(TYPE_DESCRIPTION_MESSAGE));
        }

        else
        {
          processMessageAssertions(
            binding,
            serviceReference,
            classPrefix,
            wsdlDocument);
          processPortTypeAssertions(
            portType,
            serviceReference,
            classPrefix,
            wsdlDocument);
          processOperationAssertions(
            portType,
            serviceReference,
            classPrefix,
            wsdlDocument);
          processMessageAssertions(
            portType,
            serviceReference,
            classPrefix,
            wsdlDocument);
        }
      }

    }

    // ---------------------------
    // wsdl:binding
    // ---------------------------
    else if (serviceReference.getWSDLElement().isBinding())
    {
      WSDLElement wsdlElement = serviceReference.getWSDLElement();
      // Find the binding element
      if (wsdlElement.getQName() != null
        && wsdlElement.getQName().getLocalPart() != null
        && wsdlElement.getQName().getLocalPart().length() > 0)
      {
        if (((binding =
          definition.getBinding(serviceReference.getWSDLElement().getQName()))
          == null)
          || (binding.isUndefined()))
        {
          throw new WSIRuntimeException(
            "Could not locate WSDL binding: "
              + serviceReference.getWSDLElement().getName());
        }

        processBindingAssertions(
          binding,
          serviceReference,
          classPrefix,
          wsdlDocument);
        processMessageAssertions(
          binding,
          serviceReference,
          classPrefix,
          wsdlDocument);

        // Next, process the portType
        if (((portType = binding.getPortType()) == null)
          || (portType.isUndefined()))
        {
          //throw new WSIRuntimeException("Could not locate WSDL PortType for Binding: " + binding.getQName().getLocalPart());

          // Set missingInput for all portType, operation and message test assertions 
          setMissingInput(EntryType.getEntryType(TYPE_DESCRIPTION_PORTTYPE));
          setMissingInput(EntryType.getEntryType(TYPE_DESCRIPTION_OPERATION));
          setMissingInput(EntryType.getEntryType(TYPE_DESCRIPTION_MESSAGE));
        }

        else
        {
          processPortTypeAssertions(
            portType,
            serviceReference,
            classPrefix,
            wsdlDocument);
          processOperationAssertions(
            portType,
            serviceReference,
            classPrefix,
            wsdlDocument);

          // Process each message within each operation of the portType associated with the binding
          processMessageAssertions(
            portType,
            serviceReference,
            classPrefix,
            wsdlDocument);
        }
      }

      // There was a problem with the binding element specification.  This can 
      // happen when a UDDI tModel did not have a valid binding reference.
      else
      {
        // Set missingInput for all binding, portType, operation and message test assertions 
        setMissingInput(EntryType.getEntryType(TYPE_DESCRIPTION_BINDING));
        setMissingInput(EntryType.getEntryType(TYPE_DESCRIPTION_PORTTYPE));
        setMissingInput(EntryType.getEntryType(TYPE_DESCRIPTION_OPERATION));
        setMissingInput(EntryType.getEntryType(TYPE_DESCRIPTION_MESSAGE));
      }
    }

    // ---------------------------
    // wsdl:portType
    // ---------------------------
    else if (serviceReference.getWSDLElement().isPortType())
    {
      // Find the PortType element
      if (((portType =
        definition.getPortType(serviceReference.getWSDLElement().getQName()))
        == null)
        || (portType.isUndefined()))
      {
        throw new WSIRuntimeException(
          "Could not locate WSDL PortType: "
            + serviceReference.getWSDLElement().getName());
      }

      // Set missingInput for all binding test assertions 
      setMissingInput(EntryType.getEntryType(TYPE_DESCRIPTION_BINDING));

      processPortTypeAssertions(
        portType,
        serviceReference,
        classPrefix,
        wsdlDocument);
      processOperationAssertions(
        portType,
        serviceReference,
        classPrefix,
        wsdlDocument);

      // Process each message within each operation of the portType
      processMessageAssertions(
        portType,
        serviceReference,
        classPrefix,
        wsdlDocument);
    }

    // ---------------------------
    // wsdl:operation
    // ---------------------------
    else if (serviceReference.getWSDLElement().isOperation())
    {
      // Find the operation
      // get portType from config parent element
      if (((portType =
        definition.getPortType(
          serviceReference.getWSDLElement().getParentElementQName()))
        == null)
        || (portType.isUndefined()))
      {
        throw new WSIRuntimeException(
          "Could not locate WSDL portType: "
            + serviceReference.getWSDLElement().getParentElementQName());
      }

      if (((operation =
        getOperationFromPortType(
          portType,
          serviceReference.getWSDLElement().getName()))
        == null)
        || (operation.isUndefined()))
      {
        throw new WSIRuntimeException(
          "Could not locate WSDL Operation: "
            + serviceReference.getWSDLElement().getName()
            + "in portType: "
            + portType.getQName());
      }

      // Set missingInput for all binding and portType test assertions 
      setMissingInput(EntryType.getEntryType(TYPE_DESCRIPTION_BINDING));
      setMissingInput(EntryType.getEntryType(TYPE_DESCRIPTION_PORTTYPE));

      processOperationAssertions(
        operation,
        portType,
        serviceReference,
        classPrefix,
        wsdlDocument);
      processMessageAssertions(
        operation,
        serviceReference,
        classPrefix,
        wsdlDocument);
    }

    // ---------------------------
    // wsdl:message
    // ---------------------------
    else if (serviceReference.getWSDLElement().isMessage())
    {
      // Find the message
      if (((message =
        definition.getMessage(serviceReference.getWSDLElement().getQName()))
        == null)
        || (message.isUndefined()))
      {
        throw new WSIRuntimeException(
          "Could not locate WSDL Message: "
            + serviceReference.getWSDLElement().getName());
      }

      // Set missingInput for all binding, portType, and operation test assertions 
      setMissingInput(EntryType.getEntryType(TYPE_DESCRIPTION_BINDING));
      setMissingInput(EntryType.getEntryType(TYPE_DESCRIPTION_PORTTYPE));
      setMissingInput(EntryType.getEntryType(TYPE_DESCRIPTION_OPERATION));

      processMessageAssertions(
        message,
        serviceReference,
        classPrefix,
        wsdlDocument);
    }

    else
    {
      throw new WSIRuntimeException(
        "The following WSDL type is not supported: "
          + serviceReference.getWSDLElement().getType());
    }

    // Cleanup
    cleanup();
  }

 /* (non-Javadoc)
  * @see org.wsi.test.profile.validator.WSDLValidator#validate()
  */
  /** @deprecated -- use validateArtifact(). */
  public WSDLDocument validate() throws WSIException
  {
    validateArtifact();
    
    // Return WSDL document
    return this.wsdlDocument;
  }
  /**
   * Get entry container using the filename for WSDL document.
   * @param filename a file name.
   * @return entry container using the filename for WSDL document.
   */
  protected EntryContainer getEntryContainer(String filename)
  {
    EntryContainer entryContainer = null;

    // If the entry container already exists, then use it
    if ((entryContainer = (EntryContainer) containerMap.get(filename)) == null)
    {
      // Create new entry container
      entryContainer = this.reporter.createEntryContainer();

      // Set container id using the filename for the WSDL document
      entryContainer.setId(filename);

      // Put the new entry container into the container map
      containerMap.put(filename, entryContainer);
    }

    return entryContainer;
  }

  /**
   * Get operation from port type.
   * @param portType port type.
   * @param operationName operation name.
   * @return operation from port type.
   */
  protected Operation getOperationFromPortType(
    PortType portType,
    String operationName)
  {
    // FIX: wsdl4j available method call below implies that only 
    // name+inputname+outputname uniquely defines operation 
    // Use this instead for now: - get the first operation we find...
    Operation op = null;
    if (portType.getOperations() != null)
    {
      Iterator opIt = portType.getOperations().iterator();

      while (opIt.hasNext())
      {
        op = (Operation) opIt.next();
        if (operationName.equals(op.getName()))
        {
          return op;
        }
      }
    }

    return null; // no matching operation found
  }

  /**
   * Process definition assertions.
   * @param classPrefix class prefix.
   * @param candidate candidate.
   * @throws WSIException if problems occur during processing.
   */
  protected void processDefinitionAssertions(
    String classPrefix,
    CandidateInfo candidate)
    throws WSIException
  {

    Entry entry = null;

    Definition[] wsdlDefinitions = candidate.getDefinitions();

    for (int i = 0; i < wsdlDefinitions.length; i++)
    {
      Definition definition = wsdlDefinitions[i];
      if (definition == null)
        continue;
      // Create entry 
      entry = this.reporter.getReport().createEntry();
      entry.setEntryType(EntryType.getEntryType(TYPE_DESCRIPTION_DEFINITIONS));
      entry.setReferenceID(definition.getDocumentBaseURI());
      entry.setEntryDetail(definition);

      // Set entry container
      entry.setEntryContainer(
        getEntryContainer(definition.getDocumentBaseURI()));

      // Process all of the definition related test assertions
      processAssertions(
        classPrefix,
        new EntryContext(entry, candidate.getWsdlDocument()));
      // ADD: need to use here the specific document corresponding to the definition??

    }
  }

  /**
   * Process types assertions.
   * @param classPrefix class prefix.
   * @param candidate candidate.
   * @throws WSIException if problem occurs during processing 
   *         type assertions.
   */
  protected void processTypesAssertions(
    String classPrefix,
    CandidateInfo candidate)
    throws WSIException
  {
    Entry entry = null;

    Types[] wsdlTypes = candidate.getTypes();
    Definition[] wsdlDefinitions = candidate.getDefinitions();

    // If there are no types elements, then set all results to missingInput   
    if (wsdlTypes == null || wsdlTypes.length == 0)
    {
      // Set missingInput for all test assertions with this entry type
      setMissingInput(EntryType.getEntryType(TYPE_DESCRIPTION_TYPES));
    }

    else
    {
      for (int i = 0; i < wsdlTypes.length; i++)
      {
        Types types = wsdlTypes[i];
        if (types == null)
        {
          // no Types element in i-th document
          continue;
        }

        // Create entry 
        entry = this.reporter.getReport().createEntry();
        entry.setEntryType(EntryType.getEntryType(TYPE_DESCRIPTION_TYPES));
        entry.setReferenceID(
          candidate.getDefinition(types).getDocumentBaseURI() + "-Types");
        entry.setEntryDetail(types);

        // Set entry container
        entry.setEntryContainer(
          getEntryContainer(wsdlDefinitions[i].getDocumentBaseURI()));

        // Process all of the Types related test assertions
        processAssertions(
          classPrefix,
          new EntryContext(entry, candidate.getWsdlDocument()));
      }
    }
  }

  /**
   * Process import assertions. 
   * @param classPrefix class prefix.
   * @param candidate candidate.
   * @throws WSIException if problem occurs during processing 
   *         import assertions.
   */
  protected void processImportAssertions(
    String classPrefix,
    CandidateInfo candidate)
    throws WSIException
  {

    Entry entry = null;

    Import[] wsdlImports = candidate.getImports();

    // If there are no import elements, then set all results to missingInput   
    if (wsdlImports == null || wsdlImports.length == 0)
    {
      // Set missingInput for all test assertions with this entry type
      setMissingInput(EntryType.getEntryType(TYPE_DESCRIPTION_IMPORT));
    }

    else
    {
      for (int i = 0; i < wsdlImports.length; i++)
      {
        Import wsdlImport = wsdlImports[i];
        // Create entry 
        entry = this.reporter.getReport().createEntry();
        entry.setEntryType(EntryType.getEntryType(TYPE_DESCRIPTION_IMPORT));
        entry.setReferenceID(wsdlImport.getNamespaceURI());
        entry.setEntryDetail(wsdlImport);

        // Set entry container
        entry.setEntryContainer(getEntryContainer(wsdlImport.getLocationURI()));

        // Process all of the import related test assertions
        processAssertions(
          classPrefix,
          new EntryContext(entry, candidate.getWsdlDocument()));
        // ADD: need to use here the specific document corresponding to the import!!
      }
    }
  }

  /**
   * Process port assertions.
   * @param port a port.
   * @param serviceReference service reference.
   * @param classPrefix class prefix.
   * @param wsdlDocument WSDL document.
   * @throws WSIException if problem occurs during processing 
   *         port assertions.
   */
  protected void processPortAssertions(
    Port port,
    ServiceReference serviceReference,
    String classPrefix,
    WSDLDocument wsdlDocument)
    throws WSIException
  {
    Entry entry = null;

    // Create entry 	  
    entry = this.reporter.getReport().createEntry();
    entry.setEntryType(EntryType.getEntryType(TYPE_DESCRIPTION_PORT));
    entry.setReferenceID(port.getName());
    entry.setParentElementName(
      serviceReference.getWSDLElement().getParentElementName());
    entry.setEntryDetail(port);

    // Process assertions for this artifact against the target context
    processAssertions(classPrefix, new EntryContext(entry, wsdlDocument));
  }

  /**
   * Process binding assertions.
   * @param binding binding.
   * @param serviceReference service reference.
   * @param classPrefix class prefix.
   * @param wsdlDocument WSDL document.
   * @throws WSIException if problem occurs during processing 
   *         binding assertions.
   */
  protected void processBindingAssertions(
    Binding binding,
    ServiceReference serviceReference,
    String classPrefix,
    WSDLDocument wsdlDocument)
    throws WSIException
  {
    Entry entry = null;
    QName bindingQName = binding.getQName();

    // Create entry 
    entry = this.reporter.getReport().createEntry();
    entry.setEntryType(
      EntryType.getEntryType(TYPE_DESCRIPTION_BINDING));
    entry.setReferenceID(bindingQName.toString());
    entry.setEntryDetail(binding);

    // Set entry container
    Definition definition =
      analyzerContext.getCandidateInfo().getDefinition(binding);
    entry.setEntryContainer(
      getEntryContainer(
        (definition == null ? null : definition.getDocumentBaseURI())));

    // Process binding test assertions
    processAssertions(classPrefix, new EntryContext(entry, wsdlDocument));
  }

  /**
   * Process port type assertions.
   * @param portType port type.
   * @param serviceReference service reference.
   * @param classPrefix class prefix.
   * @param wsdlDocument WSDL document.
   * @throws WSIException if problem occurs during processing 
   *         port type assertions.
  */
  protected void processPortTypeAssertions(
    PortType portType,
    ServiceReference serviceReference,
    String classPrefix,
    WSDLDocument wsdlDocument)
    throws WSIException
  {

    Entry entry = null;
    QName portTypeQName = portType.getQName();

    // Create entry 
    entry = this.reporter.getReport().createEntry();
    entry.setEntryType(EntryType.getEntryType(TYPE_DESCRIPTION_PORTTYPE));
    entry.setReferenceID(portTypeQName.toString());
    entry.setEntryDetail(portType);

    // Set entry container
    Definition definition =
      analyzerContext.getCandidateInfo().getDefinition(portType);
    entry.setEntryContainer(
      getEntryContainer(
        (definition == null ? null : definition.getDocumentBaseURI())));

    // Process test assertions
    processAssertions(classPrefix, new EntryContext(entry, wsdlDocument));
  }

  /**
   * Process operation assertions.
   * @param operation an operation.
   * @param portType port type.
   * @param serviceReference service reference.
   * @param classPrefix class prefix.
   * @param wsdlDocument WSDL document.
   * @throws WSIException if problem occurs during processing 
   *         operation assertions.
  */
  protected void processOperationAssertions(
    Operation operation,
    PortType portType,
    ServiceReference serviceReference,
    String classPrefix,
    WSDLDocument wsdlDocument)
    throws WSIException
  {
    // qualify operation with service location from config.	
    Entry entry = null;

    // Create entry 
    entry = this.reporter.getReport().createEntry();
    entry.setEntryType(EntryType.getEntryType(TYPE_DESCRIPTION_OPERATION));
    entry.setReferenceID(operation.getName());
    entry.setParentElementName(portType.getQName().getLocalPart());
    entry.setEntryDetail(operation);

    // Set entry container
    Definition definition =
      analyzerContext.getCandidateInfo().getDefinition(portType);
    entry.setEntryContainer(
      getEntryContainer(
        (definition == null ? null : definition.getDocumentBaseURI())));

    // Process test assertions
    processAssertions(classPrefix, new EntryContext(entry, wsdlDocument));
  }

  /**
   * Process operation assertions.
   * @param portType port type.
   * @param serviceReference service reference.
   * @param classPrefix class prefix.
   * @param wsdlDocument WSDL document.
   * @throws WSIException if problem occurs during processing 
   *         operation assertions.
   */
  protected void processOperationAssertions(
    PortType portType,
    ServiceReference serviceReference,
    String classPrefix,
    WSDLDocument wsdlDocument)
    throws WSIException
  {
    // For each operation, 
    if (portType.getOperations() == null)
    {
      setMissingInput(EntryType.getEntryType(TYPE_DESCRIPTION_OPERATION));
    }

    else
    {
      Operation operation;
      Iterator opIt = portType.getOperations().iterator();
      while (opIt.hasNext())
      {
        operation = (Operation) opIt.next();
        if (operation == null || operation.isUndefined())
          setMissingInput(EntryType.getEntryType(TYPE_DESCRIPTION_OPERATION));
        else
          processOperationAssertions(
            operation,
            portType,
            serviceReference,
            classPrefix,
            wsdlDocument);
      }
    }
  }

  /**
   * Process message assertions.
   * @param message a message.
   * @param serviceReference service reference.
   * @param classPrefix class prefix.
   * @param wsdlDocument WSDL document.
   * @throws WSIException if problem occurs during processing 
   *         message assertions.
   */
  protected void processMessageAssertions(
    Message message,
    ServiceReference serviceReference,
    String classPrefix,
    WSDLDocument wsdlDocument)
    throws WSIException
  {

    Entry entry = null;
    QName messageQName = message.getQName();

    // Create entry 
    entry = this.reporter.getReport().createEntry();
    entry.setEntryType(EntryType.getEntryType(TYPE_DESCRIPTION_MESSAGE));
    entry.setReferenceID(messageQName.toString());
    entry.setEntryDetail(message);

    // Set entry container
    Definition definition =
      analyzerContext.getCandidateInfo().getDefinition(message);
    entry.setEntryContainer(
      getEntryContainer(
        (definition == null ? null : definition.getDocumentBaseURI())));

    // Process binding test assertions
    processAssertions(classPrefix, new EntryContext(entry, wsdlDocument));
  }

  /**
   * Process message assertions.
   * @param binding a binding.
   * @param serviceReference service reference.
   * @param classPrefix class prefix.
   * @param wsdlDocument WSDL document.
   * @throws WSIException if problem occurs during processing 
   *         message assertions.
   */
  protected void processMessageAssertions(
    Binding binding,
    ServiceReference serviceReference,
    String classPrefix,
    WSDLDocument wsdlDocument)
    throws WSIException
  {
    HashSet messageSet;

    if (binding.getBindingOperations() != null)
    { // can do nothing if have no operations defined
      messageSet =
        WSDLUtils.findMessages(wsdlDocument.getDefinitions(), binding);

      // Process any messages that were found    
      if (messageSet.size() > 0)
      {
        Iterator messageIt = messageSet.iterator();
        while (messageIt.hasNext())
        {
          Message message = (Message) messageIt.next();
          if (!message.isUndefined())
            processMessageAssertions(
              message,
              serviceReference,
              classPrefix,
              wsdlDocument);
        }
      }
    }
  }

  /**
   * Process message assertions.
   * @param portType port type.
   * @param serviceReference service reference.
   * @param classPrefix class prefix.
   * @param wsdlDocument WSDL document.
   * @throws WSIException if problem occurs during processing 
   *         message assertions.
   */
  protected void processMessageAssertions(
    PortType portType,
    ServiceReference serviceReference,
    String classPrefix,
    WSDLDocument wsdlDocument)
    throws WSIException
  {

    HashSet messageSet = new HashSet();

    if (portType.getOperations() != null)
    {
      // can do nothing if have no operations defined

      Iterator opIt = portType.getOperations().iterator();

      while (opIt.hasNext())
      {
        Operation op = (Operation) opIt.next();

        // Since there is no guarantee that we have both and input and output message, 
        // check for its existence before adding it
        if (op.getInput() != null && !op.getInput().getMessage().isUndefined())
          messageSet.add(op.getInput().getMessage());

        if (op.getOutput() != null
          && !op.getOutput().getMessage().isUndefined())
          messageSet.add(op.getOutput().getMessage());

        // also messages from any Faults defined within the operation
        if (op.getFaults() != null)
        {
          Iterator faultIt = op.getFaults().values().iterator();
          Message message;
          while (faultIt.hasNext())
          {
            message = ((Fault) faultIt.next()).getMessage();
            if (!message.isUndefined())
              messageSet.add(message);
          }
        }
      }

      if (messageSet.size() == 0)
      {
        // Set all message test assertion results to missingInput
        setMissingInput(EntryType.getEntryType(TYPE_DESCRIPTION_MESSAGE));
      }

      else
      {
        // now step through each derived Message
        Iterator messageIt = messageSet.iterator();
        while (messageIt.hasNext())
        {
          processMessageAssertions(
            (Message) (messageIt.next()),
            serviceReference,
            classPrefix,
            wsdlDocument);
        }
      }
    }
  }

  /**
   * Process message assertions.
   * @param op - operation.
   * @param serviceReference service reference.
   * @param classPrefix class prefix.
   * @param wsdlDocument WSDL document.
   * @throws WSIException if problem occurs during processing 
   *         message assertions.
   */
  protected void processMessageAssertions(
    Operation op,
    ServiceReference serviceReference,
    String classPrefix,
    WSDLDocument wsdlDocument)
    throws WSIException
  {

    HashSet messageSet = new HashSet();
    if (op.getInput() != null && !op.getInput().getMessage().isUndefined())
      messageSet.add(op.getInput().getMessage());

    if (op.getOutput() != null && !op.getOutput().getMessage().isUndefined())
      messageSet.add(op.getOutput().getMessage());

    // also messages from any Faults defined within the operation
    Iterator faultIt = op.getFaults().values().iterator();
    Message message;
    while (faultIt.hasNext())
    {
      message = ((Fault) faultIt.next()).getMessage();
      if (!message.isUndefined())
        messageSet.add(message);
    }

    if (messageSet.size() == 0)
    {
      // Set all message test assertion results to missingInput
      setMissingInput(EntryType.getEntryType(TYPE_DESCRIPTION_MESSAGE));
    }

    else
    {
      // now step through each derived Message
      Iterator messageIt = messageSet.iterator();
      while (messageIt.hasNext())
      {
        processMessageAssertions(
          (Message) (messageIt.next()),
          serviceReference,
          classPrefix,
          wsdlDocument);
      }
    }
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
   * Method getSoapFaults.
   * 
   * @param inBinding in binding.
   * @return soap faults.
   * @throws WSIException if problems occur while processing binding faults.
   */
  protected SOAPFault[] getSoapFaults(Binding inBinding) throws WSIException
  {

    Vector soapFaults = new Vector();

    // Go through each bindingFault one at a time
    BindingFault[] bindingFaults = getAllBindingFaults(inBinding);
    for (int fault = 0; fault < bindingFaults.length; fault++)
    {
      SOAPFault soapFault = getSoapFault(bindingFaults[fault]);
      if (soapFault != null)
      {
        soapFaults.add(soapFault);
      }
    }

    SOAPFault[] soapFaultArray = new SOAPFault[soapFaults.size()];
    soapFaults.copyInto(soapFaultArray);

    return soapFaultArray;
  }

  /**
  * Method getAllBindingFaults.
  * 
  * @param inBinding binding.
  * @return all binding faults.
  * @throws WSIException if problems occur during processing.
  */
  protected BindingFault[] getAllBindingFaults(Binding inBinding)
    throws WSIException
  {

    Vector faults = new Vector();

    try
    {

      Iterator bindingOperations = inBinding.getBindingOperations().iterator();

      while (bindingOperations.hasNext())
      {

        try
        {
          BindingOperation bo = (BindingOperation) bindingOperations.next();
          Iterator bindingFaults = bo.getBindingFaults().values().iterator();
          while (bindingFaults.hasNext())
          {

            faults.add((BindingFault) bindingFaults.next());
          }
        }
        catch (NullPointerException e)
        {

        }
      }
    }
    catch (NullPointerException e)
    {
      // no binding operations in this binding - ignore & continue
    }

    BindingFault[] faultArray = new BindingFault[faults.size()];
    faults.copyInto(faultArray);

    return faultArray;
  }

  /**
   * Method getWSDLFaults.
   * 
   * @param bindingFault a binding fault.
   * @return WSDL faults.
   * @throws WSIException if problems occur during processing.
   */
  protected SOAPFault getSoapFault(BindingFault bindingFault)
    throws WSIException
  {

    SOAPFault soapFault = null;
    try
    {
      Iterator faultExtensibles =
        bindingFault.getExtensibilityElements().iterator();

      while (faultExtensibles.hasNext() && soapFault == null)
      {
        try
        {
          soapFault = (SOAPFault) faultExtensibles.next();
        }
        catch (ClassCastException e)
        { // ignore everything but SOAP Fault elements.
        }
      }
    }
    catch (NullPointerException e)
    {
    }

    return soapFault;
  }

  /**
   * Method getSoapHeader.
   * 
   * @param inBinding a binding.
   * @return SOAP headers.
   * @throws WSIException if problems occur during processing.
   */
  protected SOAPHeader[] getSoapHeaders(Binding inBinding) throws WSIException
  {
    // Get all bindings
    Binding[] bindingList = new Binding[1];
    bindingList[0] = inBinding;

    Vector soapHeaderList = new Vector();

    // Go through each binding one at a time
    for (int binding = 0; binding < bindingList.length; binding++)
    {
      try
      {
        // get the list of binding Operations
        BindingOperation[] bindingOperations =
          (BindingOperation[]) bindingList[binding]
            .getBindingOperations()
            .toArray(
            new BindingOperation[0]);

        // get references to the extensible elements within the <input> and <output> elements of this binding <operation>. 
        for (int bo = 0; bo < bindingOperations.length; bo++)
        {

          // Iterate over all input/output extensibles, looking for <SOAP:Body> elements.
          try
          {
            BindingInput bindingInput = bindingOperations[bo].getBindingInput();
            BindingOutput bindingOutput =
              bindingOperations[bo].getBindingOutput();

            Iterator extElements =
              bindingInput.getExtensibilityElements().iterator();
            while (extElements.hasNext())
            {
              try
              {
                soapHeaderList.add((SOAPHeader) extElements.next());
              }
              catch (ClassCastException e)
              { // ignore everything but SOAP Header.
              }
            }

            extElements = bindingOutput.getExtensibilityElements().iterator();
            while (extElements.hasNext())
            {
              try
              {
                soapHeaderList.add((SOAPHeader) extElements.next());
              }
              catch (ClassCastException e)
              { // ignore everything but SOAP Header.
              }
            }
          }
          catch (NullPointerException e)
          { // no extensibility elements for <input> or <output> - ignore : not checking this here.
          }
        }
      }
      catch (NullPointerException e)
      {
        // no binding operations in this binding - ignore & continue
      }
    }

    SOAPHeader[] soapHeaderArray = new SOAPHeader[soapHeaderList.size()];
    soapHeaderList.copyInto(soapHeaderArray);

    return soapHeaderArray;
  }

  /**
   * Method getSoapHeaderFaults.
   * 
   * WSDLDocument getter method - maybe better off in class WSDLDocument...
   * 
   * @param inBinding a binding.
   * @return SOAP header faults.
   * @throws WSIException if problems occur during processing.
   */
  protected SOAPHeaderFault[] getSoapHeaderFaults(Binding inBinding)
    throws WSIException
  {
    Vector soapHeaderFaultList = new Vector();

    // Get the list of SOAP headers
    SOAPHeader[] soapHeaderArray = getSoapHeaders(inBinding);

    // Go through the list and get the header faults
    List list = null;
    for (int header = 0; header < soapHeaderArray.length; header++)
    {
      // Get list for this header
      if ((list = soapHeaderArray[header].getSOAPHeaderFaults()) != null)
      {
        // Add to primary list      
        soapHeaderFaultList.addAll(list);
      }
    }

    SOAPHeaderFault[] soapHeaderFaultArray =
      new SOAPHeaderFault[soapHeaderFaultList.size()];
    soapHeaderFaultList.copyInto(soapHeaderFaultArray);

    return soapHeaderFaultArray;
  }

  /**
   * Method getSoapBodies.
   * 
   * WSDLDocument getter method - maybe better off in class WSDLDocument...
   * 
   * @param inBinding a binding.
   * @return SOAP bodies.
   * @throws WSIException if if problems occur during processing.
   */
  protected SOAPBody[] getSoapBodies(Binding inBinding) throws WSIException
  {
    // REMOVE: Get all bindings
    //Binding[] bindingList = wsdlDocument.getBindings();
    Binding[] bindingList = new Binding[1];
    bindingList[0] = inBinding;

    Vector soapBodies = new Vector();

    // Go through each binding one at a time
    for (int binding = 0; binding < bindingList.length; binding++)
    {
      // get the list of binding Operations
      BindingOperation[] bindingOperations =
        (BindingOperation[]) bindingList[binding]
          .getBindingOperations()
          .toArray(
          new BindingOperation[0]);

      // get references to the extensible elements within the <input> and <output> elements of this binding <operation>. 
      for (int bo = 0; bo < bindingOperations.length; bo++)
      {

        // Iterate over all input/output extensibles, looking for <SOAP:Body> elements.
        try
        {
          Iterator inputExtensibles =
            bindingOperations[bo]
              .getBindingInput()
              .getExtensibilityElements()
              .iterator();
          while (inputExtensibles.hasNext())
          {
            try
            {
              soapBodies.add((SOAPBody) inputExtensibles.next());
            }
            catch (ClassCastException e)
            { // ignore everything but SOAP Body elements.
            }
          }
        }
        catch (NullPointerException e)
        { // no extensibility elements for <input> - ignore : not checking this here.
        }

        try
        {
          Iterator outputExtensibles =
            bindingOperations[bo]
              .getBindingOutput()
              .getExtensibilityElements()
              .iterator();
          while (outputExtensibles.hasNext())
          {
            try
            {
              soapBodies.add((SOAPBody) outputExtensibles.next());
            }
            catch (ClassCastException e)
            { // ignore everything but SOAP Body elements.
            }
          }
        }
        catch (NullPointerException e)
        { // no extensibility elements for <output>.
        }
      }
    }

    SOAPBody[] soapBodyArray = new SOAPBody[soapBodies.size()];
    soapBodies.copyInto(soapBodyArray);

    return soapBodyArray;
  }

  /**
   * Method getSoapBody.
   * 
   * @param bindingInput a BindingInput object.
   * @return  body.
   * @throws WSIException if problems occur during processing.
   */
  protected SOAPBody getSoapBody(BindingInput bindingInput) throws WSIException
  {

    SOAPBody soapBody = null;

    Iterator extensibles = bindingInput.getExtensibilityElements().iterator();
    while (extensibles.hasNext())
    {
      Object extensible = extensibles.next();
      if (extensible instanceof SOAPBody)
      {
        soapBody = (SOAPBody) extensible;
        break;
      }
    }
    return soapBody;
  }

  /**
   * Method getSoapBody.
   * 
   * @param bindingOutput a BindingOutput object.
   * @return SOAP body.
   * @throws WSIException if problems occur during processing.
   */
  protected SOAPBody getSoapBody(BindingOutput bindingOutput)
    throws WSIException
  {

    SOAPBody soapBody = null;

    Iterator extensibles = bindingOutput.getExtensibilityElements().iterator();
    while (extensibles.hasNext())
    {
      Object extensible = extensibles.next();
      if (extensible instanceof SOAPBody)
      {
        soapBody = (SOAPBody) extensible;
        break;
      }
    }
    return soapBody;
  }

  /**
   * Get schema used.
   * @param def definition.
   * @return Schema used.
   * @throws AssertionFailException if problem getting WSDL defintions 
   *         namespace.
   */
  protected String getSchemaUsed(Definition def) throws AssertionFailException
  {
    String schemaUsed = "";

    try
    {
      // Need to read the file directly, since WSDL4J always puts in the default WSDL namespace
      Document document = wsdlDocument.getDocument();
      if (document == null)
      {
        document = parseXMLDocumentURL(def.getDocumentBaseURI(), null);
      }

      if (document != null)
      {
        // Get the root element
        Element element = document.getDocumentElement();

        // Get the namespace for this element
        if (element != null)
          schemaUsed = element.getNamespaceURI();
      }
    }

    catch (WSIException we)
    {
      throw new AssertionFailException("problem getting WSDL defintions namespace");
    }

    /*
    // Get the default namespace
    String schemaUsed = def.getNamespace("");
    
    // If the default was set, then process it to get the namespace
    if (schemaUsed == null) {
      // do it the hard way (still better than another DOM parse)... 				
      //WSDLWriter w = new WSDLWriterImpl();
      try {
        WSDLWriter w = WSDLFactory.newInstance().newWSDLWriter();
      	Document doc = w.getDocument(def);
      	Element e = doc.getDocumentElement();
      	schemaUsed = e.getNamespaceURI();
      } 
      catch (NullPointerException e) {
      	throw new AssertionFailException("problem getting WSDL defintions namespace");
      } 
      catch (WSDLException e) {
      	throw new AssertionFailException("problem getting document defintion");
      }      	
    }
    */

    return schemaUsed;
  }

  /**
   * Method getSoapBinding.
   * 
   * Get the SOAP binding for a Binding.
    * 
   * @param binding a binding.
   * @return a SOAP binding.
   * @throws WSIException if problems occur during processing.
   */
  public static SOAPBinding getSoapBinding(Binding binding) throws WSIException
  {
    SOAPBinding soapBinding = null;

    // Get the list of extensibility elements
    List exElements = binding.getExtensibilityElements();
    if (exElements != null)
    {
      Iterator iterator = binding.getExtensibilityElements().iterator();

      //  Check for <soap:binding> element
      while ((iterator.hasNext()) && (soapBinding == null))
      {
        try
        {
          soapBinding = (SOAPBinding) iterator.next();
        }
        catch (ClassCastException e)
        { // ignore everything but SOAP Binding element
        }
      }
    }

    return soapBinding;
  }

  /** 
   * Create XML schema validator.  This is done here because some compilers do not allow
   * the documentList field to be accessed from within an inner class.
   * @param documentBaseURI the base URL.
   * @return newly created XML schema validator.
   */
  protected XMLSchemaValidator createXMLSchemaValidator(String documentBaseURI)
  {
    // Create XML schema validator
    return new XMLSchemaValidator(documentBaseURI);
  }

  /**
   * Search xsd schema or xsd import from node. If node is xsd import it's loading schema.
   * @param definition a Definition object.
   * @return a list of schema target namespaces.
   * @throws WSIException if problem during processing method.
   */
  protected List getSchemaTargetNamespaceList(Definition definition)
    throws WSIException
  {
    List list = null, nextList = null;

    // Get list of extension elements within the types element
    Types types = null;
    if ((types = definition.getTypes()) != null)
    {
      Iterator iterator = types.getExtensibilityElements().iterator();

      ExtensibilityElement extElement = null;
      while (iterator.hasNext())
      {
        // Get next ext. element
        extElement = (ExtensibilityElement) iterator.next();
        // If this is an unknown ext. element, then see if it is a schema element
        TargetNamespaceProcessor tnsProcessor = null;
        if (extElement instanceof Schema)
        {
          tnsProcessor = new TargetNamespaceProcessor(definition.getDocumentBaseURI());


          if ((nextList =
            tnsProcessor.processAllSchema(
              ((Schema) extElement).getElement()))
            != null)
            if (list == null)
              list = new Vector();
          list.addAll(nextList);
        }
      }
    }

    return list;
  }
 /**
   * Search xsd schema or xsd import from node. If node is xsd import it's loading schema.
   * @param definition a Definition object.
   * @return a list of schema target namespaces.
   * @throws WSIException if problem during processing method.
   */
  protected List getSchemaNamespaceList(Definition definition)
    throws WSIException
  {
    List list = new Vector();
    
    // Always add current document targetNamespace
    List targetNamespaceList = getSchemaTargetNamespaceList(definition);
    
    if ((targetNamespaceList != null) && !targetNamespaceList.isEmpty())
      list.addAll(targetNamespaceList);
    
    // Get list of imported WSDL documents
    Map importMap = definition.getImports();

    Import imp;

    // Add each imports targetNamespace to the list
    if (importMap != null && !importMap.isEmpty())
    {
      Iterator values = importMap.values().iterator();
      List importList;
      while (values.hasNext())
      {
        importList = (List) values.next();
        Iterator imports = importList.iterator();
        while (imports.hasNext())
        {
          imp = (Import) imports.next();
          if (imp != null && imp.getDefinition() != null)
          	list.addAll(getSchemaNamespaceList(imp.getDefinition()));
        }
      }
    }

    return list;
  }

  /**
   * Build list of WSDL targetNamespaces.
   * @param definition a Definition object.
   * @return list of WSDL targetNamespaces.
   */
  protected List getWSDLTargetNamespaceList(Definition definition)
  {
    return getWSDLTargetNamespaceList(definition, new ArrayList());
  }

  /**
   * Build list of WSDL targetNamespaces.
   * @param definition a Definition object.
   * @return list of WSDL targetNamespaces.
   */
  protected List getWSDLTargetNamespaceList(Definition definition, List alreadyProcessedDefinitions)
  {
    List list = new ArrayList();
    if ((definition != null) && (!alreadyProcessedDefinitions.contains(definition)))
    {
      alreadyProcessedDefinitions.add(definition);
      
      // Always add current document targetNamespace
      if (definition.getTargetNamespace() != null)
        list.add(definition.getTargetNamespace());

      // Get list of imported WSDL documents
      Map importMap = definition.getImports();

      Import imp;

      // Add each imports targetNamespace to the list
      if (importMap != null && !importMap.isEmpty())
      {
        Iterator values = importMap.values().iterator();
        List importList;
        while (values.hasNext())
        {
          importList = (List) values.next();
          Iterator imports = importList.iterator();
          while (imports.hasNext())
          {
            imp = (Import) imports.next();
            if (imp != null && imp.getDefinition() != null)
            list.addAll(getWSDLTargetNamespaceList(imp.getDefinition(), alreadyProcessedDefinitions));
            // list.add(imp.getDefinition().getTargetNamespace());
          }
        }
      }
    }

    return list;
  }

  protected class BindingMatch
  {
    private Binding binding;
    private BindingOperation bindingOperation;
    private SOAPBinding soapBinding;
    //private Vector bindingArgs; // set of BindingInputs and BindingOutputs
    private BindingInput bindingInput;
    private BindingOutput bindingOutput;

    // ADD: need to include BindingFault support...
    public BindingMatch(
      Binding b,
      BindingOperation bo,
      SOAPBinding sb,
      BindingInput bin,
      BindingOutput bout)
    {
      binding = b;
      bindingOperation = bo;
      soapBinding = sb;
      //bindingArgs = new Vector();
      //if (bin  != null) { bindingArgs.add(bin);  }
      //if (bout != null) { bindingArgs.add(bout); }
      bindingInput = bin;
      bindingOutput = bout;
    }

    public BindingMatch(
      Binding b,
      BindingOperation bo,
      SOAPBinding sb,
      BindingInput bin)
    {
      this(b, bo, sb, bin, null);
    }

    public BindingMatch(
      Binding b,
      BindingOperation bo,
      SOAPBinding sb,
      BindingOutput bout)
    {
      this(b, bo, sb, null, bout);
    }

    /**
     * Returns the soapBinding.
     * @return SOAPBinding
     */
    public SOAPBinding getSoapBinding()
    {
      return soapBinding;
    }

    /**
     * Returns the bindingOperation.
     * @return BindingOperation
     */
    public BindingOperation getBindingOperation()
    {
      return bindingOperation;
    }

    /**
     * Returns the bindingInput.
     * @return BindingInput
     */
    public BindingInput getBindingInput()
    {
      return bindingInput;
    }

    /**
     * Returns the bindingOutput.
     * @return BindingOutput
     */
    public BindingOutput getBindingOutput()
    {
      return bindingOutput;
    }

    public boolean hasBindingInput()
    {
      return (this.bindingInput != null);
    }

    public boolean hasBindingOutput()
    {
      return (this.bindingOutput != null);
    }

    /**
     * Returns the binding.
     * @return Binding
     */
    public Binding getBinding()
    {
      return binding;
    }

  }

  /**
   * Get binding matches.
  * @param binding a binding.
  * @param soapBindingStyle soap binding style.
  * @param soapBodyUse soap body use.
  * @return binding matches.
  * @throws WSIException if problems occur during processing.
  */
  public BindingMatch[] getBindingMatches(
    Binding binding,
    String soapBindingStyle,
    String soapBodyUse)
    throws WSIException
  {

    Vector bindingMatches = new Vector();

    // Check binding
    SOAPBinding soapBinding = getSoapBinding(binding);

    // check that the soap:binding for this WSDL binding is the specified style
    // ADD: check for null pointer
    if (soapBinding != null)
    {
      String defaultStyle = soapBinding.getStyle();

      if (defaultStyle == null)
      {
        defaultStyle = WSIConstants.ATTRVAL_SOAP_BIND_STYLE_DOC;
      }

      // Get the set of operations for this WSDL binding
      List bindingOpsList = binding.getBindingOperations();
      if (bindingOpsList != null)
      {
        Iterator bindingOps = bindingOpsList.iterator();
        // for each binding operation:
        while (bindingOps.hasNext())
        {
          BindingOperation bindingOp = (BindingOperation) bindingOps.next();

          SOAPOperation soapOp = getSoapOperation(bindingOp);

          if ((soapOp == null && defaultStyle.equals(soapBindingStyle))
            || (soapOp != null
              && soapOp.getStyle() == null
              && defaultStyle.equals(soapBindingStyle))
            || (soapOp != null
              && soapOp.getStyle() != null
              && soapOp.getStyle().equals(soapBindingStyle)))
          {
            // check binding input & output
            BindingInput bInput = bindingOp.getBindingInput();
            if (bInput != null)
            {
              SOAPBody inputSoapBody = getSoapBody(bInput);
              if (inputSoapBody == null
                || (inputSoapBody.getUse() != null
                  && !inputSoapBody.getUse().equals(soapBodyUse)))
              {
                bInput = null;
              }
            }

            BindingOutput bOutput = bindingOp.getBindingOutput();
            if (bOutput != null)
            {
              SOAPBody outputSoapBody = getSoapBody(bOutput);

              if (outputSoapBody == null
                || (outputSoapBody.getUse() != null
                  && !outputSoapBody.getUse().equals(soapBodyUse)))
              {
                bOutput = null;
              }
            }

            if ((bOutput != null) || (bInput != null))
            {
              // we have a match, add to the vector
              bindingMatches.add(
                new BindingMatch(
                  binding,
                  bindingOp,
                  soapBinding,
                  bInput,
                  bOutput));
            }
          }
        }
      }
    }

    BindingMatch[] BindingMatchArray = new BindingMatch[bindingMatches.size()];
    bindingMatches.copyInto(BindingMatchArray);
    return BindingMatchArray;
  }

  /**
   * Method getSoapOperation.
   *
   * @param bindingOperation a binding operation.
   * @return a soap operation.
   * @throws WSIException if problems while processing.
   */
  public static SOAPOperation getSoapOperation(BindingOperation bindingOperation)
    throws WSIException
  {

    if (bindingOperation.getExtensibilityElements() == null)
    {
      return null;
    }

    Iterator extensibles =
      bindingOperation.getExtensibilityElements().iterator();
    while (extensibles.hasNext())
    {
      Object extensible = extensibles.next();
      if (extensible instanceof SOAPOperation)
      {
        return (SOAPOperation) extensible;
      }
    }
    return null;
  }

  /* 
   * Returns an array of SOAPOperations corresponding to the wsdl:binding supplied.
   */
  protected HashMap getSoapOperations(Binding binding) throws WSIException
  {
    HashMap soapOperationList = new HashMap();

    if (binding.getBindingOperations() == null)
    {
      return null;
    }

    //Vector soapOpVector = new Vector();

    // Get the list of binding operations
    Iterator operations = binding.getBindingOperations().iterator();

    // Check each binding operation to see if it has a soap operation element
    BindingOperation bindingOperation = null;
    while (operations.hasNext())
    {
      bindingOperation = (BindingOperation) operations.next();
      Iterator extensibles =
        bindingOperation.getExtensibilityElements().iterator();
      while (extensibles.hasNext())
      {
        Object extensible = extensibles.next();
        if (extensible instanceof SOAPOperation)
        {
          soapOperationList.put(extensible, bindingOperation.getName());
        }
      }
    }

    //return (SOAPOperation[])soapOpVector.toArray(new SOAPOperation[] {});
    return soapOperationList;
  }

  /**
   * Check part attributes.
   * @param bindingMatch an array of BindingMatch objects.
   * @param inOrOut a String object.
   * @param attrib attribute.
   * @return a boolean.
   * @throws AssertionFailException if the part is not compliant.
   */
  // GT - rework this method with a better way of parameterizing the getters required for the invocation.
  protected boolean checkPartAttributes(
    BindingMatch[] bindingMatch,
    String inOrOut,
    String attrib)
    throws AssertionFailException
  {

    if (!(inOrOut.equals("useInput") || inOrOut.equals("useOutput"))
      || !(attrib.equals("useType") || attrib.equals("useElement")))
    {
      // invalid argument value supplied by calling method - "internal error"
      return false;
    }

    for (int i = 0; i < bindingMatch.length; i++)
    {
      BindingMatch nextMatch = bindingMatch[i];

      // check the associated parts
      Message msg;
      Map parts;
      Iterator partIteration;

      BindingOperation bindingOp = nextMatch.getBindingOperation();
      if (bindingOp == null)
      {
        continue; // no Binding Operation for some reason
      }

      Operation op = bindingOp.getOperation();

      /*	ADD: handle soap:faults in similar way	   		
      try {
      // check faults - remarkably similar.... (need to retain operation id for failuredetail msg)
      	if (nextMatch.hasBindingFault()) {
      				
      		msg = op.getFault().getMessage();
      		parts = msg.getParts();
      				    					     			
      		//check that each part has an element attribute    			
      		partIteration = parts.values().iterator();
      		while (partIteration.hasNext()) {
      			Part part = (Part)partIteration.next();
      			if (part.getElementName() == null) {
      				throw new AssertionFailException("OPERATION: " + op + "MESSAGE: " + msg);
      			}
      		}	 			
      	}
      }
      catch (NullPointerException n) {
      	// no parts found - this qualifies an assertion failure
      	throw new AssertionFailException(n.getMessage());
      }
      */

      try
      {

        QName attributeName;

        //GT: Do we need to check BindingInput / Output here ??

        if (inOrOut.equals("useInput"))
        {
          if (op.getInput() == null || !nextMatch.hasBindingInput())
          {
            // No Input so nothing to check
            continue;
          }

          msg = op.getInput().getMessage();

        }
        else
        { // Looking for Output
          if (op.getOutput() == null || !nextMatch.hasBindingOutput())
          {
            // No Output so nothing to check
            continue;
          }

          msg = op.getOutput().getMessage();
        }

        if (msg == null)
        {
          continue; // nothing to check from this Binding Match (?)
        }

        // Get the list of parts 
        parts = msg.getParts();

        // If there is a parts attribute, then only process those parts
        List partsNameList = null;
        if ((partsNameList = getPartsList(nextMatch, inOrOut)) != null)
        {
          Vector partsList = new Vector();
          Iterator partsNameIterator = partsNameList.iterator();
          while (partsNameIterator.hasNext())
          {
            partsList.add(parts.get((String) partsNameIterator.next()));
          }
          partIteration = partsList.iterator();
        }

        // Otherwise use the complete list of parts
        else
        {
          partIteration = parts.values().iterator();
        }

        //check that each part has an element or type attribute    			
        while (partIteration.hasNext())
        {
          Part part = (Part) partIteration.next();
          if (attrib.equals("useElement"))
          {
            attributeName = part.getElementName();
          }
          else
          { // "useType"
            attributeName = part.getTypeName();
          }

          if (attributeName == null)
          {
            throw new AssertionFailException(
              "Name of operation that failed: "
                + op.getName()
                + "\n"
                + op.toString()
                + "\n"
                + "\nName of message that failed: "
                + msg.getQName()
                + "\n"
                + msg.toString());
          }
        }
      }
      catch (NullPointerException n)
      {
        // no parts found - this qualifies an assertion failure
        throw new AssertionFailException(n.toString());
      }
    }
    return true; // tests successful
  }

  /**
   * Get parts list from a soapbind:body element.
   */
  private List getPartsList(BindingMatch bindingMatch, String type)
  {
    List partsList = null;
    Iterator iterator = null;

    BindingOperation bindingOp;

    try
    {
      // Get the binding operation
      bindingOp = bindingMatch.getBindingOperation();

      // Determine if the binding operation contains a soapbind:body with a parts attribute
      if (type.equals("useInput"))
      {
        iterator =
          bindingOp.getBindingInput().getExtensibilityElements().iterator();
      }
      else
      {
        iterator =
          bindingOp.getBindingOutput().getExtensibilityElements().iterator();
      }
    }
    catch (NullPointerException e)
    {
      return null;
      // either no binding operation, binding input/output, or SOAP element
    }

    // Determine if the binding operation contains a soapbind:body with a parts attribute
    while ((iterator.hasNext()) && (partsList == null))
    {
      try
      {
        SOAPBody soapBody = (SOAPBody) iterator.next();
        partsList = soapBody.getParts();
      }
      catch (ClassCastException cce)
      { // not a SOAPBody extensibility element so ignore
      }
    }

    return partsList;
  }

  /**
   * Get element location.
   * @param wsdlDocument WSDL document.
   * @param wsdlElement WSDL element.
   * @return element location.
   */
  protected ElementLocation getElementLocation(
    WSDLDocument wsdlDocument,
    Object wsdlElement)
  {
    ElementLocation elementLocation = null;
    WSDLElementList wsdlElementList;

    if ((wsdlElementList = wsdlDocument.getElementList()) != null)
    {
      elementLocation = wsdlElementList.getElementLocation(wsdlElement);
    }

    return elementLocation;
  }

  /**
   * Create failure detail.
   * @param message a message.
   * @param entryContext entry context.
   * @return failure detail.
   */
  protected FailureDetail createFailureDetail(
    String message,
    EntryContext entryContext)
  {
    return createFailureDetail(
      message,
      entryContext,
      entryContext.getEntry().getEntryDetail());
  }

  /**
   * Create failure detail.
   * @param message a message.
   * @param entryContext entry context.
   * @param wsdlElement WSDL element.
   * @return failure detail.
   */
  protected FailureDetail createFailureDetail(
    String message,
    EntryContext entryContext,
    Object wsdlElement)
  {
    FailureDetail failureDetail = reporter.createFailureDetail();
    failureDetail.setFailureMessage(message);
    failureDetail.setElementLocation(
      getElementLocation(entryContext.getWSDLDocument(), wsdlElement));
    return failureDetail;
  }

  /** 
   * SOAPBody, SOAPFault, SOAPHeader and SOAPHeaderFault class 
   * interfaces have compatible getUse() and getNamespaceURI() 
   * methods, but wsdl4j does not declare them at the parent interface. 
   * Therefore use reflection to access these common methods.
   * 
   * @param extElement extensibility element.
   * @return true if namespace is found in SOAP literal.
   * @throws NoSuchMethodException if this method cannot be found.
   * @throws InvocationTargetException if problems occur in an invoked method or constructor
   * @throws IllegalAccessException if there is am attempt to load a
   *         class that it does not have access to.
   */
  protected boolean namespaceFoundInSoapLiteral(ExtensibilityElement extElement)
    throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
  {
    Class c = extElement.getClass();
    Method getUseMethod = c.getMethod("getUse", new Class[0]);
    Method getNamespaceURIMethod = c.getMethod("getNamespaceURI", new Class[0]);

    // (use attribute is mandatory but the null case is checked for since a missing use is not
    // checked with this TA. If its missing its invalid but we don't know whether we have doc-lit).     		
    if (getUseMethod.invoke(extElement, null) == null
      || !getUseMethod.invoke(extElement, null).equals(
        WSIConstants.ATTRVAL_SOAP_BODY_USE_LIT))
    {
      return false;
    }

    // return true if namespace found
    boolean namespaceFound =
      (getNamespaceURIMethod.invoke(extElement, null) != null);

    // return true if namespace found
    return namespaceFound;

  }

  /**
   * Verify extensibility element uses literal.
   * @param extensible - extensibility element
   * @return boolean
   * @throws NoSuchMethodException if this method cannot be found.
   * @throws InvocationTargetException if problems occur in an invoked method or constructor
   * @throws IllegalAccessException if there is am attempt to load a
   *         class that it does not have access to.
   */
  protected boolean isLiteral(ExtensibilityElement extensible)
    throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
  {

    Class c = extensible.getClass();
    Method getUseMethod = c.getMethod("getUse", new Class[0]);

    // (use attribute is mandatory but the null case is checked for since a missing use is not
    // checked with this TA. If its missing its invalid but we don't know whether we have doc-lit).     		
    if (getUseMethod.invoke(extensible, null) == null
      || !getUseMethod.invoke(extensible, null).equals(
        WSIConstants.ATTRVAL_SOAP_BODY_USE_LIT))
    {
      return false;
    }

    // return true if shown to have use="literal"
    return true;

  }
  
  /**
   * Returns true if these tests should be run (depending on the analyzer
   * config)
   */
  public boolean runTests() { return testable; } 
}
