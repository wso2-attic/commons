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
package org.eclipse.wst.wsi.internal.core.analyzer.config;

import javax.xml.namespace.QName;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.common.AddStyleSheet;
import org.eclipse.wst.wsi.internal.core.document.WSIDocument;
import org.eclipse.wst.wsi.internal.core.util.MessageList;

/**
 * This is the interface for the analzyer configuration file.
 * 
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public interface AnalyzerConfig extends WSIDocument
{
  /**
   * Element name.
   */
  public static final String ELEM_NAME = WSIConstants.ELEM_CONFIG;

  /**
   * QName.
   */
  public static final QName QNAME =
    new QName(WSIConstants.NS_URI_WSI_ANALYZER_CONFIG, ELEM_NAME);

  /**
   * Correlation types.
   */
  public static final String CORRELATION_TYPE_OPERATION = "operation";
  public static final String CORRELATION_TYPE_NAMESPACE = "namespace";
  public static final String CORRELATION_TYPE_ENDPOINT = "endpoint";

  /**
   * Initialize analyzer config.
   * @param messageList a MessageList object.
   */
  public void init(MessageList messageList);

  /**
   * Get optional description.
   * @return optional description.
   * @see #setDescription
   */
  public String getDescription();

  /**
   * Set optional description.
   * @param description  an optional description.
   * @see #getDescription
   */
  public void setDescription(String description);

  /**
   * Get verbose option.
   * @return true if verbose.
   * @see #setVerboseOption
   */
  public boolean getVerboseOption();

  /**
   * Set verbose option.
   * @param verbose verbose option.
   * @see #getVerboseOption
   */
  public void setVerboseOption(boolean verbose);

  /**
   * Get assertion results option.
   * @return assertion results option.
   * @see #setAssertionResultsOption
   */
  public AssertionResultsOption getAssertionResultsOption();

  /**
   * Set assertion results option.
   * @param assertionResultsOption  assertion results option.
   * @see #getAssertionResultsOption
   */
  public void setAssertionResultsOption(AssertionResultsOption assertionResultsOption);

  /**
   * Get replace report file option.
   * @return replace report file option.
   * @see #setReplaceReport
   */
  public boolean getReplaceReport();

  /**
   * Set replace report file option.
   * @param replaceReport  eplace report file option.
   * @see #getReplaceReport
   */
  public void setReplaceReport(boolean replaceReport);

  /**
   * Get report file location.
   * @return report file location.
   * @see #setReportLocation
   */
  public String getReportLocation();

  /**
   * Set replace report file option.
   * @param reportURI report location.
   * @see #getReportLocation
   */
  public void setReportLocation(String reportURI);

  /**
   * Get style sheet.
   * @return style sheet.
   * @see #setAddStyleSheet
   */
  public AddStyleSheet getAddStyleSheet();

  /**
   * Set style sheet.
   * @param addStyleSheet a style sheet.
   * @see #getAddStyleSheet
   */
  public void setAddStyleSheet(AddStyleSheet addStyleSheet);

  /**
   * Get profile test assertions document location.
   * @return rofile test assertions document location.
   * @see #setTestAssertionsDocumentLocation
   */
  public String getTestAssertionsDocumentLocation();

  /**
   * Set profile test assertions document location.
   * @param testAssertionsDocumentURI profile test assertions document location.
   * @see #getTestAssertionsDocumentLocation
   */
  public void setTestAssertionsDocumentLocation(String testAssertionsDocumentURI);

  /**
   * Get mesage log location.
   * @return mesage log location.
   * @see #setLogLocation
   */
  public String getLogLocation();

  /**
   * Set message log location.
   * @param logURI message log location.
   * @see #getLogLocation
   */
  public void setLogLocation(String logURI);

  /**
   * Is message log location specified.
   * @return true if message log location is set.
   */
  public boolean isLogSet();

  /**
   * Get correlation type.
   * @return correlation type.
   * @see #setCorrelationType
   */
  public String getCorrelationType();

  /**
   * Set correlation type.
   * @param correlationType correlation type.
   * @see #getCorrelationType
   */
  public void setCorrelationType(String correlationType);

  /**
   * Is WSDL reference set.
   * @return true if WSDL reference is set.
   */
  public boolean isWSDLReferenceSet();

  /**
   * Get WSDL reference.
   * @return WSDL reference.
   * @see #setWSDLReference
   */
  public WSDLReference getWSDLReference();

  /**
   * Set WSDL reference.
   * @param wsdlReference WSDL reference.
   * @see #getWSDLReference
   */
  public void setWSDLReference(WSDLReference wsdlReference);

  /**
   * Get WSDL element.
   * @return WSDL element.
   */
  public WSDLElement getWSDLElement();

  /**
   * Get WSDL document location.
   * @return WSDL document location.
   */
  public String getWSDLLocation();

  /**
   * Get service location.
   * @return service location.
   */
  public String getServiceLocation();

  /**
   * Is UDDI reference set.
   * @return true if UDDI reference is set.
   */
  public boolean isUDDIReferenceSet();

  /**
   * Get UDDI reference.
   * @return UDDI reference.
   * @see #setUDDIReference
   */
  public UDDIReference getUDDIReference();

  /**
   * Set UDDI reference.
   * @param uddiReference UDDI reference.
   * @see #getUDDIReference
   */
  public void setUDDIReference(UDDIReference uddiReference);

  /**
   * Parse the command line arguments.
   * @param args the command line arguments.
   * @param validate flag for command line validation.
   * @throws WSIException if problems parsing the command line arguments.
   */
  public void parseArgs(String[] args, boolean validate) throws WSIException;

  /**
   * Get string representation of this object.
   * @return string representation of this object.
   */
  public String toString();
}
