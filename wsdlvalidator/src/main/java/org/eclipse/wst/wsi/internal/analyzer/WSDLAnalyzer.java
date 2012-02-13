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
package org.eclipse.wst.wsi.internal.analyzer;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.eclipse.wst.common.uriresolver.internal.util.URIEncoder;
import org.eclipse.wst.wsi.internal.WSIPreferences;
import org.eclipse.wst.wsi.internal.WSITestToolsEclipseProperties;
import org.eclipse.wst.wsi.internal.WSITestToolsProperties;
import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.analyzer.config.AnalyzerConfig;
import org.eclipse.wst.wsi.internal.core.analyzer.config.AssertionResultType;
import org.eclipse.wst.wsi.internal.core.analyzer.config.AssertionResultsOption;
import org.eclipse.wst.wsi.internal.core.analyzer.config.WSDLElement;
import org.eclipse.wst.wsi.internal.core.analyzer.config.WSDLReference;
import org.eclipse.wst.wsi.internal.core.analyzer.config.impl.AssertionResultsOptionImpl;
import org.eclipse.wst.wsi.internal.core.analyzer.config.impl.WSDLElementImpl;
import org.eclipse.wst.wsi.internal.core.analyzer.config.impl.WSDLReferenceImpl;
import org.eclipse.wst.wsi.internal.core.common.AddStyleSheet;
import org.eclipse.wst.wsi.internal.core.common.impl.AddStyleSheetImpl;
import org.eclipse.wst.wsi.internal.core.document.DocumentFactory;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.report.Entry;
import org.eclipse.wst.wsi.internal.core.report.impl.FailureDetailImpl;
import org.eclipse.wst.wsi.internal.core.report.impl.ReportImpl;
import org.eclipse.wst.wsi.internal.core.xml.dom.ElementLocation;
import org.eclipse.wst.wsi.internal.report.AssertionError;

/**
 * WSDLAnalyzer
 * The interface to the WS-I Test Tools for analyzing a WSDL file.  Allows a WSDL file to be analyzed
 * at any of the following levels.
 *  port
 *  binding
 *  portType
 *  operation
 *  message
 */

public class WSDLAnalyzer
{
  public final static String PLUGIN_ID = "org.wsi.test.tools";
  
  public static final String PORT = "port";
  public static final String BINDING = "binding";
  public static final String PORTTYPE = "portType";
  public static final String OPERATION = "operation";
  public static final String MESSAGE = "message";

  protected final String NOTPASSED = "notPassed";
  protected final String WARNING = "warning";
  protected final String FAILED = "failed";
  
  protected List analyzerConfigs;
  protected String wsdlURI;
  // assertions to ignore - used for assertions that the tools have not implemented properly yet
  //protected String[] ignoreAssertions = { /*"WSI2105" , "WSI2119"*/};
  protected List assertionerrors;
  protected List assertionwarnings;
  protected WSIPreferences wsiPreferences = null;

  /**
   * Constructor.
   * Given the file name, parent element name, the element to analyze and the type of element
   * sets up the WSDLAnalyzer to analyze the file.
   * @param fileName log file name.
   * @param parentName parent element name.
   * @param namespace  a namespace.
   * @param elementName the element to analyze.
   * @param elementType the type of the element.
   * @param wsiPreferences the preferences.
   * @throws WSIAnalyzerException if unable to add configuration information.
   */
  public WSDLAnalyzer(String fileName, String parentName, String namespace, String elementName, String elementType, WSIPreferences wsiPreferences) throws WSIAnalyzerException
  {
    this(fileName, wsiPreferences);
    addConfigurationToTest(parentName, namespace, elementName, elementType);

  }

  /**
   * Constructor.
   * Given the file name, parent element name, the element to analyze and the type of element
   * sets up the WSDLAnalyzer to analyze the file.
   * @param fileName log file name.
   * @param parentName parent element name.
   * @param namespace  a namespace.
   * @param elementName the element to analyze.
   * @param elementType the type of the element.
   * @throws WSIAnalyzerException if unable to add configuration information.
   */
  public WSDLAnalyzer(String fileName, String parentName, String namespace, String elementName, String elementType) throws WSIAnalyzerException
  {
    this(fileName);
    addConfigurationToTest(parentName, namespace, elementName, elementType);

  }

  /**
   * Constructor.
   * @param fileName  log file name.
   */
  public WSDLAnalyzer(String fileName, WSIPreferences wsiPreferences)
  {
    reset(fileName);
    this.wsiPreferences = wsiPreferences;
  }

  /**
   * Constructor.
   * @param fileName  log file name.
   */
  public WSDLAnalyzer(String fileName)
  {
    reset(fileName);
    if(WSITestToolsProperties.getEclipseContext())
    {
      this.wsiPreferences = WSITestToolsEclipseProperties.checkWSIPreferences(fileName);
    }
    else
    {
  	  this.wsiPreferences = WSITestToolsProperties.checkWSIPreferences(fileName);
    }
  }

  public WSIPreferences getWSIPreferences()
  {
	  return this.wsiPreferences;
  }
	  
  /**
   * validateConformance.
   * Validate the WSDL file as setup in the constructor.
   * @throws WSIAnalyzerException if unable to validate the given WSDL File.
   */
  public void validateConformance() throws WSIAnalyzerException
  {
    try
    {
	  if (wsiPreferences.getComplianceLevel() != WSITestToolsProperties.IGNORE_NON_WSI)
	  {
        // here's where the validation actually starts happening
        WSIBasicProfileAnalyzer bpanalyzer = new WSIBasicProfileAnalyzer(analyzerConfigs,wsdlURI);
        bpanalyzer.validateAll();
        ReportImpl report = (ReportImpl) bpanalyzer.getReport();

        List entries = report.getEntries();

        // if there are report entries we iterate through them and add all
        // error and warning messages to the error list. 
        if (entries != null)
        {
          Iterator ientry = entries.iterator();
          while (ientry.hasNext())
          {
            Entry entry = (Entry) ientry.next();
            Map assertionResults = entry.getAssertionResultList();

            if (assertionResults != null)
            {
              Iterator iassertionResults = assertionResults.values().iterator();

              while (iassertionResults.hasNext())
              {
                AssertionResult ar = (AssertionResult) iassertionResults.next();
                TestAssertion ta = ar.getAssertion();
                // add in to skip errors for assertions that are reporting incorrect problems.
                // This allows us to not report incorrect errors reported in the tools without disabling them
                //              String assertionId = ta.getId();
                //              boolean ignoreAssertion = false;
                //              for(int i = ignoreAssertions.length-1; i >=0;i--)
                //              {
                //              	if(ignoreAssertions[i].equalsIgnoreCase(assertionId))
                //              	{
                //              		ignoreAssertion = true;
                //              	}
                //              }
                //              if(ignoreAssertion)
                //              {
                //              	continue;
                //              }
                if (ar.getResult().equalsIgnoreCase(FAILED) && 
                	wsiPreferences.getComplianceLevel().equals(WSITestToolsProperties.STOP_NON_WSI))
                {
                  if (ta.isEnabled())
                  {
                    List errorMessageList = ar.getFailureDetailList();
                    if (errorMessageList != null)
                    {
                      Iterator errorMessages = errorMessageList.iterator();

                      while (errorMessages.hasNext())
                      {
                        FailureDetailImpl fdi = (FailureDetailImpl) errorMessages.next();
                        int lineNumber = 0;
                        int columnNumber = 0;

                        ElementLocation el = fdi.getElementLocation();

                        if (el != null)
                        {
                          lineNumber = el.getLineNumber();
                          columnNumber = el.getColumnNumber();
                        }

                        assertionerrors.add(
                          new AssertionError(ta.getId(), ta.getFailureMessage(), lineNumber, columnNumber));
                      }
                    }
                    // the errorMessageList is null but there is an error so assign it to line 0 for now
                    else
                    {
                      assertionerrors.add(new AssertionError(ta.getId(), ta.getFailureMessage(), 0, 0));
                    }
                  }
                }
                else if (ar.getResult().equalsIgnoreCase(FAILED) && 
                         wsiPreferences.getComplianceLevel().equals(WSITestToolsProperties.WARN_NON_WSI))
                {
                  if (ta.isEnabled())
                  {
                    List errorMessageList = ar.getFailureDetailList();
                    if (errorMessageList != null)
                    {
                      Iterator errorMessages = errorMessageList.iterator();
                      while (errorMessages.hasNext())
                      {
                        FailureDetailImpl fdi = (FailureDetailImpl) errorMessages.next();
                        int lineNumber = 0;
                        int columnNumber = 0;

                        ElementLocation el = fdi.getElementLocation();

                        if (el != null)
                        {
                          lineNumber = el.getLineNumber();
                          columnNumber = el.getColumnNumber();
                        }
                        assertionwarnings.add(
                          new AssertionError(ta.getId(), ta.getFailureMessage(), lineNumber, columnNumber));
                      }
                    }
                    // the errorMessageList is null but there is an error so assign it to line 0 for now
                    else
                    {
                      assertionwarnings.add(new AssertionError(ta.getId(), ta.getFailureMessage(), 0, 0));
                    }
                  }
                }
              }
            }
          }
        }
	  }
    }

    catch (Exception e)
    {
      throw new WSIAnalyzerException(e + "The WSDLAnalyzer was unable to validate the given WSDL File.");
    }

  }

  /**
   * Add configuration information.
   * @param parentName parent element name.
   * @param namespace  a namespace.
   * @param elementName the element to analyze.
   * @param elementType the type of the element.
   * @throws WSIAnalyzerException if unable to add configuration information.
   */
  public void addConfigurationToTest(
    String parentName,
    String namespace,
    String elementName,
    String elementType) throws WSIAnalyzerException
  {
  	try
  	{
      WSDLReference wsdlReference = new WSDLReferenceImpl();

      WSDLElement wsdlElement = (WSDLElement) new WSDLElementImpl();
      wsdlElement.setType(elementType);
      if (parentName != null)
      {
        wsdlElement.setParentElementName(parentName);
      }
      wsdlElement.setNamespace(namespace);
      wsdlElement.setName(elementName);
      wsdlReference.setWSDLElement(wsdlElement);

      wsdlReference.setWSDLLocation(wsdlURI);
      DocumentFactory documentFactory = DocumentFactory.newInstance();
      // Initialize the BasicProfileAnalyzer using an analyzerconfig object
      AnalyzerConfig analyzerconfig = documentFactory.newAnalyzerConfig();

      AssertionResultsOption aro = new AssertionResultsOptionImpl();
      aro.setAssertionResultType(AssertionResultType.newInstance(NOTPASSED));
      aro.setShowAssertionDescription(false);
      aro.setShowFailureDetail(false);
      aro.setShowFailureMessage(false);
      aro.setShowMessageEntry(false);

      // although we don't use a stylesheet for our application it is required or the 
      // WS-I tools will fail
      AddStyleSheet addstylesheet = new AddStyleSheetImpl();

      analyzerconfig.setAssertionResultsOption(aro);
      analyzerconfig.setAddStyleSheet(addstylesheet);
      analyzerconfig.setReplaceReport(true);
      //analyzerconfig.setLogLocation(filename);
      analyzerconfig.setTestAssertionsDocumentLocation(wsiPreferences.getTADFile());
      analyzerconfig.setVerboseOption(false);

      // set the wsdlReference
      analyzerconfig.setWSDLReference(wsdlReference);
      analyzerConfigs.add(analyzerconfig);
    }
    catch (Exception e)
    {
      throw new WSIAnalyzerException("Unable to add AnalyzerConfig to list. " + e);
    }
  }
  /**
   * Method getAssertionFailures.
   * Return a list of the assertion failures.
   * @return a list of the assertion failures.
   */
  public List getAssertionErrors()
  {
    return assertionerrors;
  }

  /**
  	 * Method getAssertionWarnings.
  	 * Returns a list of any assertion warnings generated from validateConformance.
  	 * @return a list of any assertion warnings generated from validateConformance.
  	 */
  public List getAssertionWarnings()
  {
    return assertionwarnings;
  }
  
  /**
   * Reset.
   * @param filename log file name.
   */
  public void reset(String filename)
  {
	if (filename != null)
	{
	  String tmp = filename.replace('\\', '/');
	  if ((!tmp.startsWith(WSIConstants.HTTP_PREFIX)) &&
          (!tmp.startsWith(WSIConstants.FILE_PREFIX)))
	  {
	    tmp = WSIConstants.FILE_PROTOCOL + tmp;
	  }
	  try
	  {
        if (tmp.indexOf("?") > 0) {
	      wsdlURI = URIEncoder.encode(tmp.substring(0, tmp.indexOf("?")), "UTF8") +
                  tmp.substring(tmp.indexOf("?"));
        } else {
          wsdlURI = URIEncoder.encode(tmp, "UTF8");
        }
	  }
	  catch (UnsupportedEncodingException uee)
	  {
	  }
	}
  	analyzerConfigs = new Vector();
  	assertionerrors = new Vector();
  	assertionwarnings = new Vector();
  }
}
