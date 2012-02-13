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
package org.eclipse.wst.wsi.internal.core.profile.validator.impl;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.analyzer.AnalyzerContext;
import org.eclipse.wst.wsi.internal.core.profile.ProfileArtifact;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.BaseValidator;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.EnvelopeValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.report.Entry;
import org.eclipse.wst.wsi.internal.core.report.FailureDetail;
import org.eclipse.wst.wsi.internal.core.report.PrereqFailedList;
import org.eclipse.wst.wsi.internal.core.report.ReportArtifact;
import org.eclipse.wst.wsi.internal.core.report.Reporter;
import org.eclipse.wst.wsi.internal.core.report.impl.PrereqFailedListImpl;
import org.eclipse.wst.wsi.internal.core.util.EntryType;
import org.eclipse.wst.wsi.internal.core.xml.XMLDocumentCacheUser;
import org.eclipse.wst.wsi.internal.core.xml.dom.ElementLocation;

/**
 * The WSDL validator will verify that the WSDL and associated XML schema definitions
 * are in conformance with the profile.
 *
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 * @author Graham Turrell   (gturrell@uk.ibm.com)
 */
public abstract class BaseValidatorImpl
  extends XMLDocumentCacheUser
  implements BaseValidator
{

  /**
   * A hashtable that maps assertion results to their priorities, 0 is lowest
   */
  private static final Hashtable resultPriority = new Hashtable(6);
  static {
    resultPriority.put(AssertionResult.RESULT_PASSED, new Integer(0));
    resultPriority.put(AssertionResult.RESULT_NOT_APPLICABLE, new Integer(1));
    resultPriority.put(AssertionResult.RESULT_MISSING_INPUT, new Integer(2));
    resultPriority.put(AssertionResult.RESULT_WARNING, new Integer(3));
    resultPriority.put(AssertionResult.RESULT_PREREQ_FAILED, new Integer(4));
    resultPriority.put(AssertionResult.RESULT_FAILED, new Integer(5));
  }

  /**
   * The highest priority value being passed when processing pre-requisites
   */
  private static final int MAX_VALID_PRIORITY = 1;

  /**
   * Analyzer context.
   */
  public AnalyzerContext analyzerContext;

  /**
   * Profile artifact.
   */
  protected ProfileArtifact profileArtifact;

  /**
   * Reporter.
   */
  protected Reporter reporter;

  /**
   * Entry.
   */
  // protected Entry entry;

  /**
   * Report artifact.
   */
  protected ReportArtifact reportArtifact;

  /**
   * Verbose option.
   */
  public boolean verboseOption = false;

  /**
   * Test assertion processed count.
   */
  protected int assertionCount = 0;

  /**
   * Constructor.
   */
  public BaseValidatorImpl()
  {
  }

  /**
   * Initiailize validation test procedure.
   * @param analyzerContext the analyzerContext.
   * @param profileArtifact the profile artifiact.
   * @param reportArtifact  the report artifact.
   * @param reporter        a Reporter object.
   * @throws WSIException if validator could not be initialized.
   */
  public void init(
    AnalyzerContext analyzerContext,
    ProfileArtifact profileArtifact,
    ReportArtifact reportArtifact,
    Reporter reporter)
    throws WSIException
  {
    // Save input references
    this.analyzerContext = analyzerContext;
    this.profileArtifact = profileArtifact;
    this.reportArtifact = reportArtifact;
    this.reporter = reporter;

    // Get verbose option    
    verboseOption =
      reporter
        .getReport()
        .getReportContext()
        .getAnalyzer()
        .getAnalyzerConfig()
        .getVerboseOption();
  }

  /**
   * Process all of the test assertions for one entry.
   * @param classPrefix a class prefix.
   * @param entryContext an entry context.
   * @throws WSIException if serious problems occur while processing 
   *         all of the test assertions for an entry.
   */
  protected void processAssertions(
    String classPrefix,
    EntryContext entryContext)
    throws WSIException
  {
    long startTime = 0;

    HashMap processList = new HashMap();
    TestAssertion testAssertion = null;
    AssertionProcess assertionProcess;
    AssertionResult assertionResult;

    TreeMap preReqResults;
    PrereqFailedList prereqFailedList;
    String preReqId;

    Class[] paramTypes = new Class[1];
    // If this is an instance of MessageValidator or EnvelopeValidator
    // then use BaseMessageValidator as a param type of assertions constructor
    paramTypes[0] = this instanceof BaseMessageValidator ?
      BaseMessageValidator.class : this.getClass();

    Object[] params = new Object[1];
    params[0] = this;

    // Get entry from context
    Entry entry = entryContext.getEntry();

    // Add entry to artifact
    this.reportArtifact.addEntry(entry);

    // Add entry to report
    if (this instanceof EnvelopeValidator)
     	this.reporter.setCurrentEnvelopeEntry(entry); 
	else this.reporter.setCurrentEntry(entry);

    try
    {
      // Go through the list of test assertions for the artifact
      Iterator iterator = profileArtifact.getTestAssertionList().iterator();
      while (iterator.hasNext())
      {
        // Get next test assertion
        testAssertion = (TestAssertion) iterator.next();

        // Reset variables
        assertionResult = null;

        // If the test assertion is enabled, then continue
        //        if ((testAssertion.isEnabled()) && 
        //            testAssertion.getEntryTypeName().equals(targetContext.getEntry().getEntryType().getTypeName())) { 
        if (testAssertion.isEnabled()
          && isPrimaryEntryTypeMatch(testAssertion, entryContext))
        {
          try
          {
            // Add 1 to assertion processed count
            assertionCount++;

            // If the assertion process hasn't been created yet, then create it
            if ((assertionProcess =
              (AssertionProcess) processList.get(testAssertion.getId()))
              == null)
            {

              // Create a new assertion process
              Class assertionClass =
                Class.forName(classPrefix + testAssertion.getId());

              assertionProcess =
                (AssertionProcess) assertionClass.getConstructor(
                  paramTypes).newInstance(
                  params);

              // Add it to the list
              processList.put(testAssertion.getId(), assertionProcess);
            }

            // Check whether entry is null
            if (entry.getEntryDetail() == null)
            {
              assertionResult =
                createAssertionResult(
                  testAssertion,
                  AssertionResult.RESULT_MISSING_INPUT,
                  (String) null);
            }

            // Check to see if the test assertion should be run
            //if (isNotApplicable(testAssertion, entryContext)) {
            else if (isNotApplicable(testAssertion))
            {
              assertionResult =
                createAssertionResult(
                  testAssertion,
                  AssertionResult.RESULT_NOT_APPLICABLE,
                  (String) null);
            }
            else
            {
              // Processing all the assertion pre-requisites

              preReqResults = null;
              prereqFailedList = null;
              // Get list of pre-reqs for specified assertion
              Iterator i = testAssertion.getPrereqIdList().iterator();
              while (i.hasNext())
              {
                if (preReqResults == null)
                  preReqResults = new TreeMap();
                // Get next id
                preReqId = (String) i.next();
                // Get the prereq result
                AssertionResult preReqResult = reporter.getAssertionResult(preReqId);

                // Adding a result priority value to preReqResults
                preReqResults.put(
                  resultPriority.get(preReqResult.getResult()),
                  preReqResult.getResult());

                // If the prereq failed, then add it to the list
                if (((Integer)resultPriority.get(preReqResult.getResult())).intValue()
                  > MAX_VALID_PRIORITY)
                {
                  // Adding failed prereq ID to the list
                  if (prereqFailedList == null)
                    prereqFailedList = new PrereqFailedListImpl();
                  prereqFailedList.addTestAssertionID(preReqId);
                }
              }

              if (prereqFailedList != null) {
                // Getting the highest pre-requisites' result
                Integer maxRes = (Integer)preReqResults.lastKey();
                String resString = (String)preReqResults.get(maxRes);
                if (resString.equals(AssertionResult.RESULT_FAILED)
                  ||resString.equals(AssertionResult.RESULT_WARNING)) {
                  resString = AssertionResult.RESULT_PREREQ_FAILED;
                }
                // Create assertion result based on pre-requisites which are not passed
                assertionResult = createAssertionResult(testAssertion, resString, prereqFailedList);
              }

              // Pre-requisites are passed, validate current assertion
              else {

                // Reset test assertion
                assertionProcess.reset();

                //  VERBOSE
                if (verboseOption)
                {
                  System.err.println(
                    "  Processing "
                      + testAssertion.getId()
                      + " for entry reference ID ["
                      + entry.getReferenceID()
                      + "] ...");
                  startTime = System.currentTimeMillis();
                }

                // Validate the target
                assertionResult =
                  assertionProcess.validate(testAssertion, entryContext);

                //  VERBOSE
                if (verboseOption)
                {
                  System.err.println(
                    "    Elapsed time: "
                      + (System.currentTimeMillis() - startTime)
                      + "ms");
                }                
              }
            }

            // Add result to the report
            reporter.addAssertionResult(assertionResult);
          }

          catch (ClassNotFoundException cnfe)
          {
            // DEBUG:
            if (testAssertion != null)
            {
              reporter
                .getReport()
                .getReportContext()
                .getAnalyzer()
                .printMessage(
                "WARNING: "
                  + testAssertion.getId()
                  + " is not supported currently.");
            }
          }
        }
      }
    }

    catch (Exception e)
    {
      System.err.println("EXECEPTION: " + e.getMessage());
      if (verboseOption)
        e.printStackTrace();

      if (e instanceof WSIException)
        throw (WSIException) e;
      else
        throw new WSIException(e.getMessage(), e);
    }

    finally
    {
      // Indicate that we are done with this assertion target
      this.reporter.endCurrentEntry();
    }
  }

  /**
   * Determine if the test assertion should be processed for the current entry.
   * @param testAssertion a test assertion.
   * @param entryContext  the entry context.
   * @return true if the test assertion should be processed for the current entry.
   */
  protected abstract boolean isPrimaryEntryTypeMatch(
    TestAssertion testAssertion,
    EntryContext entryContext);

  /**
   * Determine if the test assertion should be processed for the current entry.
   * @param testAssertion  a test assertion.
   * @param entry          an Entry object
   * @return true if the test assertion should be processed for the current entry.
   */
  protected boolean isPrimaryEntryTypeMatch(
    TestAssertion testAssertion,
    Entry entry)
  {
    boolean match = false;

    // If the test assertion entry type matches the target context entry type, then contine
    if (testAssertion
      .getEntryTypeName()
      .equals(entry.getEntryType().getTypeName()))
    {
      match = true;
    }

    return match;
  }

  /**
   * Determine if the test assertion is not applicable.
   * @param testAssertion a test assertion.
   * @return true if the test assertion is not applicable.
   */
  protected abstract boolean isNotApplicable(TestAssertion testAssertion);

  /* (non-Javadoc)
   * @see org.wsi.test.profile.validator.BaseValidator#cleanup()
   */
  public void cleanup() throws WSIException
  {
    // If no assertions were processed, then set all missingInput
    if (assertionCount == 0)
      setAllMissingInput();
  }

  /**
   * Create assertion result.
   * @param testAssertion a test assertion.
   * @param result        a String object.
   * @param failureDetail a FailureDetail object.
   * @return newly created test result.
   */
  public AssertionResult createAssertionResult(
    TestAssertion testAssertion,
    String result,
    FailureDetail failureDetail)
  {
    Vector failureDetailList = null;

    // Create failure detail list
    if (failureDetail != null)
    {
      failureDetailList = new Vector();
      failureDetailList.add(failureDetail);
    }

    return createAssertionResult(testAssertion, result, failureDetailList);
  }

  /**
   * Create assertion result.
   * @param testAssertion         a test assertion.
   * @param result                a String object.
   * @param failureDetailMessage  a failure detail message.
   * @return newly created test result.
   */
  public AssertionResult createAssertionResult(
    TestAssertion testAssertion,
    String result,
    String failureDetailMessage)
  {
    return createAssertionResult(
      testAssertion,
      result,
      failureDetailMessage,
      null);
  }

  /**
   * Create assertion result.
   * @param testAssertion         a test assertion.
   * @param result                a String object.
   * @param failureDetailMessage  a failure detail message.
   * @param elementLocation       element location.
   * @return newly created test result.
   */
  public AssertionResult createAssertionResult(
    TestAssertion testAssertion,
    String result,
    String failureDetailMessage,
    ElementLocation elementLocation)
  {
    // Create failure detail list
    Vector failureDetailList = null;
    if (failureDetailMessage != null)
    {
      failureDetailList = new Vector();
      FailureDetail failureDetail = reporter.createFailureDetail();
      failureDetail.setFailureMessage(failureDetailMessage);
      failureDetail.setElementLocation(elementLocation);
      failureDetailList.add(failureDetail);
    }

    return createAssertionResult(testAssertion, result, failureDetailList);
  }

  /**
   * Create assertion result.
   * @param testAssertion     a test assertion.
   * @param result            a String object.
   * @param failureDetailList a failure detail list.
   * @return newly created test result.
   */
  public AssertionResult createAssertionResult(
    TestAssertion testAssertion,
    String result,
    Vector failureDetailList)
  {
    // Create assertion result
    AssertionResult assertionResult = reporter.createAssertionResult();

    // Set values in assertion result
    assertionResult.setAssertion(testAssertion);
    assertionResult.setResult(result);
    assertionResult.setEntry(reporter.getReport().getCurrentEntry());

    // Set failure detail list
    assertionResult.setFailureDetailList(failureDetailList);

    return assertionResult;
  }

  /**
   * Create assertion result.
   * @param testAssertion     a test assertion.
   * @param result            a String object.
   * @param prereqFailedList  a list failed pre-requisite assertions.
   * @return newly created test result.
   */
  public AssertionResult createAssertionResult(
    TestAssertion testAssertion,
    String result,
    PrereqFailedList prereqFailedList)
  {
    // Create assertion result
    AssertionResult assertionResult = reporter.createAssertionResult();

    // Set values in assertion result
    assertionResult.setAssertion(testAssertion);
    assertionResult.setResult(result);
    assertionResult.setEntry(reporter.getReport().getCurrentEntry());
    assertionResult.setPrereqFailedList(prereqFailedList);

    return assertionResult;
  }

  /**
   * Add assertion result to report.
   * @param assertionId  assertion id.
   * @param result       assertion result.
   * @throws WSIException if the assertion result cannot be added to the report.
   */
  protected void addAssertionResult(String assertionId, String result)
    throws WSIException
  {
    // Create test assertion result
    AssertionResult assertionResult = reporter.createAssertionResult();

    // Set assertion, result and target
    assertionResult.setAssertion(profileArtifact.getTestAssertion(assertionId));
    assertionResult.setResult(result);
    assertionResult.setEntry(reporter.getReport().getCurrentEntry());

    // Add it to the report
    reporter.addAssertionResult(assertionResult);
  }

  /**
   * Add assertion result to report with an error detail message.
   * @param assertionId          assertion id.
   * @param result               assertion result.
   * @param failureDetailMessage an error detail message.
   * @throws WSIException if the assertion result cannot be added to the report.
   */
  protected void addAssertionResult(
    String assertionId,
    String result,
    String failureDetailMessage)
    throws WSIException
  {
    // Convert message to vector
    Vector failureDetailList = null;
    if (failureDetailMessage != null)
    {
      failureDetailList = new Vector();
      FailureDetail failureDetail = reporter.createFailureDetail();
      failureDetailList.add(failureDetail);
    }

    // Add assertion result
    addAssertionResult(assertionId, result, failureDetailList);
  }

  /**
   * Add assertion result to report.
   * @param assertionId        assertion id.
   * @param result             assertion result.
   * @param failureDetailList  a list of error detail messages.
   * @throws WSIException if the assertion result cannot be added to the report.
   */
  protected void addAssertionResult(
    String assertionId,
    String result,
    Vector failureDetailList)
    throws WSIException
  {
    // Create test assertion result
    AssertionResult assertionResult = reporter.createAssertionResult();

    // Set assertion, result and target
    assertionResult.setAssertion(profileArtifact.getTestAssertion(assertionId));
    assertionResult.setResult(result);
    assertionResult.setEntry(reporter.getReport().getCurrentEntry());
    assertionResult.setFailureDetailList(failureDetailList);

    // Add it to the report
    reporter.addAssertionResult(assertionResult);
  }

  /**
   * Add missingInput assertion result to report.
   * @param testAssertion a test assertion.
   * @throws WSIException if there is any problem adding missingInput 
   * 		 assertion result to report.
   */
  protected void addMissingInputResult(TestAssertion testAssertion)
    throws WSIException
  {
    // Create test assertion result
    AssertionResult assertionResult = reporter.createAssertionResult();

    // Set assertion, result and target
    assertionResult.setAssertion(testAssertion);
    assertionResult.setResult(AssertionResult.RESULT_MISSING_INPUT);
    assertionResult.setEntry(reporter.getReport().getCurrentEntry());

    // Add it to the report
    reporter.addAssertionResult(assertionResult);
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.validator.BaseValidator#setAllMissingInput()
   */
  public void setAllMissingInput() throws WSIException
  {
    // If profile artifact is set
    if (profileArtifact != null)
    {
      TestAssertion testAssertion;
  
      // Create entry
      Entry entry = this.reporter.getReport().createEntry();
      entry.setArtifactName(this.reportArtifact.getType().getTypeName());
  
      // Add entry to artifact
      this.reportArtifact.addEntry(entry);
  
      // Add entry to report
      this.reporter.setCurrentEntry(entry);
  
      // Go through the list of test assertions for the artifact
      Iterator iterator = profileArtifact.getTestAssertionList().iterator();
      while (iterator.hasNext())
      {
        // Get next test assertion
        testAssertion = (TestAssertion) iterator.next();
  
        // Add missingInput result
        if (testAssertion.isEnabled())
          addMissingInputResult(testAssertion);
      }
  
      // Indicate that we are done with this assertion target
      this.reporter.endCurrentEntry();
    }
  }

  /**
   * Set all results for an entry type to missingInput.
   * @param entryType an EntryType object.
   * @throws WSIException if there is any problem while processing.
   */
  protected void setMissingInput(EntryType entryType) throws WSIException
  {
    TestAssertion testAssertion;

    // Create entry 
    Entry entry = this.reporter.getReport().createEntry();
    entry.setEntryType(entryType);
    entry.setReferenceID("[" + entryType.getTypeName() + "]");

    // Add entry to artifact
    this.reportArtifact.addEntry(entry);

    // Add entry to report
    this.reporter.setCurrentEntry(entry);

    //try {
    // Go through the list of test assertions for the artifact
    Iterator iterator = profileArtifact.getTestAssertionList().iterator();
    while (iterator.hasNext())
    {
      // Get next test assertion
      testAssertion = (TestAssertion) iterator.next();
      if (testAssertion.isEnabled()
        && isPrimaryEntryTypeMatch(testAssertion, entry))
      {
        addMissingInputResult(testAssertion);
      }
    }

    // Indicate that we are done with this entry
    this.reporter.endCurrentEntry();
  }
}
