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
package org.eclipse.wst.wsi.internal.core.analyzer.config.impl;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.WSIFileNotFoundException;
import org.eclipse.wst.wsi.internal.core.analyzer.config.AnalyzerConfig;
import org.eclipse.wst.wsi.internal.core.analyzer.config.AnalyzerConfigReader;
import org.eclipse.wst.wsi.internal.core.analyzer.config.AssertionResultType;
import org.eclipse.wst.wsi.internal.core.analyzer.config.AssertionResultsOption;
import org.eclipse.wst.wsi.internal.core.analyzer.config.UDDIReference;
import org.eclipse.wst.wsi.internal.core.analyzer.config.WSDLElement;
import org.eclipse.wst.wsi.internal.core.analyzer.config.WSDLReference;
import org.eclipse.wst.wsi.internal.core.common.AddStyleSheet;
import org.eclipse.wst.wsi.internal.core.common.impl.AddStyleSheetImpl;
import org.eclipse.wst.wsi.internal.core.profile.validator.WSDLValidator;
import org.eclipse.wst.wsi.internal.core.util.MessageList;
import org.eclipse.wst.wsi.internal.core.util.TestUtils;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * This class is the implementation used to read the analyzer config documents.
 * 
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public class AnalyzerConfigReaderImpl implements AnalyzerConfigReader
{
  /**
   * Message list.
   */
  protected MessageList messageList;

  /**
   * Document location.
   */
  protected String documentURI;

  /**
   * Initialize analyzer config reader.
   */
  public void init(MessageList messageList)
  {
    this.messageList = messageList;
  }

  /**
   * @see org.eclipse.wst.wsi.internal.core.analyzer.config.AnalyzerConfigReader#readAnalyzerConfig(String)
   */
  public AnalyzerConfig readAnalyzerConfig(String analyzerConfigURI)
    throws WSIException
  {
    FileReader fileReader = null;
    try
    {
      fileReader = new FileReader(analyzerConfigURI);
    }

    catch (FileNotFoundException fnfe)
    {
      throw new WSIFileNotFoundException(
        getMessage(
          "config01",
          analyzerConfigURI,
          "Could not find analyzer config file: "),
        fnfe);
    }

    catch (Exception e)
    {
      throw new WSIException(
        getMessage(
          "config08",
          analyzerConfigURI,
          "Could not read analyzer config file: "),
        e);
    }

    return readAnalyzerConfig(fileReader);
  }

  /**
   * @see org.eclipse.wst.wsi.internal.core.analyzer.config.AnalyzerConfigReader#readAnalyzerConfig(Reader)
   */
  public AnalyzerConfig readAnalyzerConfig(Reader reader) throws WSIException
  {
    AnalyzerConfig analyzerConfig = new AnalyzerConfigImpl();

    // Parse XML
    Document doc = XMLUtils.parseXML(reader);

    // Parse elements in the config document
    parseConfigElement(analyzerConfig, doc.getDocumentElement());

    return analyzerConfig;
  }

  /**
   * @see org.eclipse.wst.wsi.internal.core.document.DocumentReader#getLocation()
   */
  public String getLocation()
  {
    return this.documentURI;
  }

  /**
   * @see org.eclipse.wst.wsi.internal.core.document.DocumentReader#setLocation(String)
   */
  public void setLocation(String documentURI)
  {
    this.documentURI = documentURI;
  }

  /**
   * Parse config element.
   */
  private void parseConfigElement(
    AnalyzerConfig analyzerConfig,
    Element element)
    throws WSIException
  {
    // ADD: Get name and version

    // ADD: Verify that this is the config element

    // Get first child element
    Element nextElement = XMLUtils.getFirstChild(element);

    // DEBUG:
    //System.out.println("Element name: " + nextElement.getClass().getName());

    // Process each child element
    while (nextElement != null)
    {
      // <description>
      if (isElement(nextElement, WSIConstants.ELEM_DESCRIPTION))
      {
        analyzerConfig.setDescription(XMLUtils.getText(nextElement));
      }

      // <verbose>
      else if (isElement(nextElement, WSIConstants.ELEM_VERBOSE))
      {
        analyzerConfig.setVerboseOption(
          XMLUtils.getBooleanValue(nextElement, false));
      }

      // <assertionResults>
      else if (isElement(nextElement, WSIConstants.ELEM_ASSERTION_RESULTS))
      {
        // Create assertion results option object
        AssertionResultsOption assertionResultsOption =
          new AssertionResultsOptionImpl();
        analyzerConfig.setAssertionResultsOption(assertionResultsOption);

        // Set result type
        String resultType =
          XMLUtils.getAttributeValue(nextElement, WSIConstants.ATTR_TYPE);
        assertionResultsOption.setAssertionResultType(
          AssertionResultType.newInstance(resultType));

        // Set show log entry
        assertionResultsOption.setShowMessageEntry(
          XMLUtils.getBooleanValue(
            nextElement,
            WSIConstants.ATTR_MESSAGE_ENTRY,
            assertionResultsOption.getShowMessageEntry()));

        // Set show failure message
        assertionResultsOption.setShowFailureMessage(
          XMLUtils.getBooleanValue(
            nextElement,
            WSIConstants.ATTR_FAILURE_MESSAGE,
            assertionResultsOption.getShowFailureMessage()));

        // Set show failure detail
        assertionResultsOption.setShowFailureDetail(
          XMLUtils.getBooleanValue(
            nextElement,
            WSIConstants.ATTR_FAILURE_DETAIL,
            assertionResultsOption.getShowFailureDetail()));

        // REMOVE: Set show warning message
        //assertionResultsOption.setShowWarningMessage(XMLUtils.getBooleanValue(nextElement, 
        //      WSIConstants.ATTR_WARNING_MESSAGE, assertionResultsOption.getShowWarningMessage()));
      }

      // <reportFile>
      else if (isElement(nextElement, WSIConstants.ELEM_REPORT_FILE))
      {
        parseReportFileElement(analyzerConfig, nextElement);
      }

      // <testAssertionFile>
      else if (isElement(nextElement, WSIConstants.ELEM_TEST_ASSERTIONS_FILE))
      {
        analyzerConfig.setTestAssertionsDocumentLocation(
          XMLUtils.getText(nextElement));
      }

      // <logFile>
      else if (isElement(nextElement, WSIConstants.ELEM_LOG_FILE))
      {
        String correlationType =
          XMLUtils.getAttributeValue(
            nextElement,
            WSIConstants.ATTR_CORRELATION_TYPE);
        analyzerConfig.setCorrelationType(
          correlationType == null ? "operation" : correlationType);
        analyzerConfig.setLogLocation(XMLUtils.getText(nextElement));
      }

      // <wsdlReference>
      else if (isElement(nextElement, WSIConstants.ELEM_WSDL_REFERENCE))
      {
        parseWsdlReference(analyzerConfig, nextElement);
      }

      // <uddiReference>
      else if (isElement(nextElement, WSIConstants.ELEM_UDDI_REFERENCE))
      {
        parseUddiReference(analyzerConfig, nextElement);
      }

      else
      {
        // Throw exception
        throw new IllegalArgumentException(
          getMessage(
            "config06",
            nextElement.getLocalName(),
            "Invalid analyzer config element:"));
      }

      // Get next element
      nextElement = XMLUtils.getNextSibling(nextElement);
    }
  }

  /**
   * Parse reportFile element.
   */
  private void parseReportFileElement(
    AnalyzerConfig analyzerConfig,
    Element element)
    throws WSIException
  {
    // Get report location and replace indicator
    analyzerConfig.setReplaceReport(
      XMLUtils.getBooleanValue(element, WSIConstants.ATTR_REPLACE, false));
    analyzerConfig.setReportLocation(
      XMLUtils.getAttributeValue(
        element,
        WSIConstants.ATTR_LOCATION,
        WSIConstants.DEFAULT_REPORT_URI));

    // ADD: If the report location wasn't specified, then throw an exception
    //if (analyzerConfig.getReportLocation() == null)
    //  throw new WSIException("The analyzer configuration file must contain the report file location.");

    // Get first child element
    Element nextElement = XMLUtils.getFirstChild(element);

    // Process each child element
    while (nextElement != null)
    {
      // <addStyleSheet>
      if (isElement(nextElement, WSIConstants.ELEM_ADD_STYLE_SHEET))
      {
        AddStyleSheet addStyleSheet = new AddStyleSheetImpl();

        // Parse the element  
        TestUtils.parseAddStyleSheet(
          nextElement,
          addStyleSheet,
          WSIConstants.DEFAULT_REPORT_XSL);

        // Set add style sheet
        analyzerConfig.setAddStyleSheet(addStyleSheet);
      }

      else
      {
        // Throw exception
        throw new IllegalArgumentException(
          getMessage(
            "config06",
            nextElement.getLocalName(),
            "Invalid analyzer config element:"));
      }

      // Get next element
      nextElement = XMLUtils.getNextSibling(nextElement);
    }
  }

  /**
   * Parse wsdl reference element.
   */
  private void parseWsdlReference(
    AnalyzerConfig analyzerConfig,
    Element element)
    throws WSIException
  {
    // Create UDDI reference
    WSDLReference wsdlReference = new WSDLReferenceImpl();

    // Set WSDL reference
    analyzerConfig.setWSDLReference(wsdlReference);

    // Get first child element
    Element nextElement = XMLUtils.getFirstChild(element);

    // Process each child element
    while (nextElement != null)
    {
      // <wsdlURI>
      if (isElement(nextElement, WSIConstants.ELEM_WSDL_URI))
      {
        // Set WSDL location
        wsdlReference.setWSDLLocation(XMLUtils.getText(nextElement));
      }

      // <serviceLocation>
      else if (isElement(nextElement, WSIConstants.ELEM_SERVICE_LOCATION))
      {
        // Set service location
        wsdlReference.setServiceLocation(XMLUtils.getText(nextElement));
      }

      // <wsdlElement>
      else if (isElement(nextElement, WSIConstants.ELEM_WSDL_ELEMENT))
      {
        // Set WSDL element
        wsdlReference.setWSDLElement(parseWsdlElement(nextElement));
      }

      else
      {
        // Throw exception
        throw new IllegalArgumentException(
          getMessage(
            "config06",
            nextElement.getLocalName(),
            "Invalid analyzer config element:"));
      }

      // Get next element
      nextElement = XMLUtils.getNextSibling(nextElement);
    }

    // If WSDL element or WSL URI not specified, then throw exception
    if ((wsdlReference.getWSDLElement() == null)
      || (wsdlReference.getWSDLLocation() == null))
    {
      throw new IllegalArgumentException(
        getMessage(
          "config07",
          "Both the <wsdlElement> and <wsdlURI> elements must be specified."));
    }

    // If type is port or operation, then parent element name must be specified
    if (((wsdlReference
      .getWSDLElement()
      .getType()
      .equalsIgnoreCase(WSDLValidator.TYPE_DESCRIPTION_PORT))
      || (wsdlReference
        .getWSDLElement()
        .getType()
        .equalsIgnoreCase(WSDLValidator.TYPE_DESCRIPTION_OPERATION)))
      && (wsdlReference.getWSDLElement().getParentElementName() == null))
    {
      throw new IllegalArgumentException(
        getMessage(
          "config09",
          "The parentElementName attribute must be specified with a WSDL type of "
            + WSDLValidator.TYPE_DESCRIPTION_PORT
            + " or "
            + WSDLValidator.TYPE_DESCRIPTION_OPERATION
            + "."));
    }
  }

  /**
   * Parse uddi reference.
   */
  private void parseUddiReference(
    AnalyzerConfig analyzerConfig,
    Element element)
    throws WSIException
  {
    // Create UDDI reference
    UDDIReference uddiReference = new UDDIReferenceImpl();

    // Set UDDI reference
    analyzerConfig.setUDDIReference(uddiReference);

    // Get first child element
    Element nextElement = XMLUtils.getFirstChild(element);

    // Process each child element
    while (nextElement != null)
    {
      // <inquiryURL>
      if (isElement(nextElement, WSIConstants.ELEM_INQUIRY_URL))
      {
        // Set inquiry URL
        uddiReference.setInquiryURL(XMLUtils.getText(nextElement));
      }

      // <uddiKey>
      else if (isElement(nextElement, WSIConstants.ELEM_UDDI_KEY))
      {
        // Set UDDI key and key type
        uddiReference.setKey(XMLUtils.getText(nextElement));
        uddiReference.setKeyType(
          XMLUtils.getAttributeValue(nextElement, WSIConstants.ATTR_TYPE));
      }

      // <wsdlElement>
      else if (isElement(nextElement, WSIConstants.ELEM_WSDL_ELEMENT))
      {
        // Set WSDL element
        uddiReference.setWSDLElement(parseWsdlElement(nextElement));
      }

      // <serviceLocation>
      else if (isElement(nextElement, WSIConstants.ELEM_SERVICE_LOCATION))
      {
        // Set service location
        uddiReference.setServiceLocation(XMLUtils.getText(nextElement));
      }

      /* REMOVE:
      // <wsdlElement>
      else if (isElement(nextElement, WSIConstants.ELEM_WSDL_ELEMENT)) {
        // Set service location
        parseWsdlElement(analyzerConfig, nextElement);
       }
       */

      else
      {
        // Throw exception
        throw new IllegalArgumentException(
          getMessage(
            "config06",
            nextElement.getLocalName(),
            "Invalid analyzer config element:"));
      }

      // Get next element
      nextElement = XMLUtils.getNextSibling(nextElement);
    }
  }

  /**
   * Parse WSDL element.
   */
  private WSDLElement parseWsdlElement(Element element) throws WSIException
  {
    WSDLElement wsdlElement = new WSDLElementImpl();

    // Set type, namespace, parent element name and name
    wsdlElement.setType(
      XMLUtils.getAttributeValue(element, WSIConstants.ATTR_TYPE));
    wsdlElement.setNamespace(
      XMLUtils.getAttributeValue(element, WSIConstants.ATTR_NAMESPACE));
    wsdlElement.setParentElementName(
      XMLUtils.getAttributeValue(
        element,
        WSIConstants.ATTR_PARENT_ELEMENT_NAME));

    String wsdlElementName = XMLUtils.getText(element);
    if (wsdlElementName.equals(""))
    {
      // Throw exception
      throw new IllegalArgumentException(
        getMessage("config19", "The WSDL element name must be specified."));
    }
    wsdlElement.setName(wsdlElementName);

    return wsdlElement;
  }

  /**
   * Determine if this element matches specified local name in the analyzer config namespace.
   */
  private boolean isElement(Element element, String localName)
  {
    return isElement(
      element,
      getValidWSIAnalyzerConfigNamespaces(),
      localName);
  }

  
  static public List getValidWSIAnalyzerConfigNamespaces()
  {
  	ArrayList list = new ArrayList();
  	list.add(WSIConstants.NS_URI_WSI_ANALYZER_CONFIG_2003);
  	list.add(WSIConstants.NS_URI_WSI_ANALYZER_CONFIG);
  	return list;
  }
  
  /**
   * Determine if this element matches specified local name in the analyzer config namespace.
   */
  private boolean isElement(
    Element element,
    List namespaces,
    String localName)
  {
    return XMLUtils.isElement(element, namespaces, localName);
  }

  /**
   * Get message from resource bundle.
   */
  private String getMessage(String messageID, String defaultMessage)
  {
    return getMessage(messageID, null, defaultMessage);
  }

  /**
   * Get message from resource bundle.
   */
  private String getMessage(
    String messageID,
    String messageData,
    String defaultMessage)
  {
    String message = defaultMessage;
    if (messageList != null)
      message = messageList.getMessage(messageID, messageData, defaultMessage);
    else
      message += " " + messageData + ".";

    return message;
  }
}
