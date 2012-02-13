/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.validation.internal.wsdl11;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;

import org.apache.xerces.xs.XSModel;
import org.eclipse.wst.wsdl.validation.internal.IWSDLValidator;
import org.eclipse.wst.wsdl.validation.internal.IValidationInfo;
import org.eclipse.wst.wsdl.validation.internal.exception.ValidateWSDLException;
import org.eclipse.wst.wsdl.validation.internal.util.MessageGenerator;
import org.w3c.dom.Document;

import com.ibm.wsdl.Constants;

/**
 * The validator controller is the head of validation. 
 */
public class WSDL11ValidatorController implements IWSDLValidator
{
  private static String VALIDATOR_RESOURCE_BUNDLE_NAME = "validatewsdl";
  protected final String _WARN_NO_VALDIATOR = "_WARN_NO_VALDIATOR";
//  protected final int ERROR_MESSAGE = 0;
//  protected final int WARNING_MESSAGE = 1;
//  protected String fileURI;
//  protected List schemas = new Vector();
//  protected Definition wsdlDefinition;
  protected MessageGenerator messagegenerator = null;
  //protected ValidationController validationController;
  protected ValidatorRegistry ver = ValidatorRegistry.getInstance();

  /**
   * Constructor.
   */
  public WSDL11ValidatorController()
  {
    ResourceBundle rb = ResourceBundle.getBundle(VALIDATOR_RESOURCE_BUNDLE_NAME, Locale.getDefault());
	messagegenerator = new MessageGenerator(rb);
  }


  /* (non-Javadoc)
   * @see org.eclipse.wsdl.validate.IWSDLValidator#validate(org.w3c.dom.Document, org.eclipse.wsdl.validate.ValidationInfo)
   */
  public void validate(Document domModel, IValidationInfo valInfo) throws ValidateWSDLException
  {
    // reset the variables
//    reset();
//    fileURI = valInfo.getFileURI();
    //this.validationController = validationcontroller;

    IWSDL11ValidationInfo wsdlvalinfo = new WSDL11ValidationInfoImpl(valInfo);
    WSDLDocument[] wsdlDocs = readWSDLDocument(domModel, valInfo.getFileURI(), getMessageGenerator(), wsdlvalinfo);
    // Don't validate an null definitions element. Either the file is emtpy and valid or
    // had an error when reading.
    if(wsdlDocs != null)
    {  
      int numWSDLDocs = wsdlDocs.length;
      for(int i = 0; i < numWSDLDocs; i++)
      {
        WSDLDocument tempDoc = wsdlDocs[i];
        Definition wsdlDefinition = tempDoc.getDefinition();
        // Register the schemas.
        List xsdList = tempDoc.getSchemas();
        Iterator xsdIter = xsdList.iterator();
        while (xsdIter.hasNext())
        {
          wsdlvalinfo.addSchema((XSModel)xsdIter.next());
        }
        // Set the element locations table.
        wsdlvalinfo.setElementLocations(tempDoc.getElementLocations());
        // Set any reader errors. This needs to be done after the element locations table is set.
        List readerErrors = tempDoc.getReaderErrors();
        if (readerErrors != null)
        {
          Iterator readerErrorsI = readerErrors.iterator();
          while (readerErrorsI.hasNext())
          {
            ReaderError re = (ReaderError)readerErrorsI.next();
            wsdlvalinfo.addError(re.getError(), re.getObject());
          }
        }
        List readerWarnings = tempDoc.getReaderWarnings();
        if (readerWarnings != null)
        {
          Iterator readerWarningsI = readerWarnings.iterator();
          while (readerWarningsI.hasNext())
          {
            ReaderError re = (ReaderError)readerWarningsI.next();
            wsdlvalinfo.addWarning(re.getError(), re.getObject());
          }
        }
        validateWSDLElement(Constants.NS_URI_WSDL, wsdlDefinition, new Vector(), wsdlvalinfo);
        wsdlvalinfo.clearSchemas();
      }
    }
    
  }
  
  /**
   * Read in the WSDL document and set the model and imported schemas.
   * 
   * @param domModel A DOM model of the document to be read.
   * @param file The file to read.
   * @param messagegenerator The messagegenerator the reader should use for any messages produced.
   * @param wsdlvalinfo The validation information for the current validation.
   * @return The definitions element for the WSDL document.
   * @throws ValidateWSDLException
   */
  protected WSDLDocument[] readWSDLDocument(Document domModel, String file, MessageGenerator messagegenerator, IWSDL11ValidationInfo wsdlvalinfo) throws ValidateWSDLException
  {
    WSDLDocument[] wsdlDocs = null;
    try
    {

      WSDLReaderImpl wsdlReader = new WSDLReaderImpl(wsdlvalinfo);
      wsdlReader.setMessageGenerator(messagegenerator);
      if(domModel != null)
      {
        wsdlDocs = wsdlReader.readWSDL(file, domModel);
      }
      else
      {  
        wsdlDocs = wsdlReader.readWSDL(file);
      }
      //wsdlvalinfo.setElementLocations(wsdlReader.getElementLocationsHashtable());
//      List readerErrors = wsdlReader.getReaderErrors();
//      if (readerErrors != null)
//       {
//        Iterator readerErrorsI = readerErrors.iterator();
//        while (readerErrorsI.hasNext())
//         {
//          ReaderError re = (ReaderError)readerErrorsI.next();
//          wsdlvalinfo.addError(re.getError(), re.getObject());
//        }
//      }
//      if (wsdlReader.hasImportSchemas())
//      {
//        List xsdList = wsdlReader.getImportSchemas();
//        Iterator xsdIter = xsdList.iterator();
//        while (xsdIter.hasNext())
//        {
//          wsdlvalinfo.addSchema((XSModel)xsdIter.next());
//        }
//
//      }

    }
    catch (WSDLException e)
    {
      throw new ValidateWSDLException(e.getMessage() + " " + e.getFaultCode());
    }

    catch (Exception e)
    {
      throw new ValidateWSDLException("unable to read file" + e.getMessage() + " " + e.toString());
    }
    return wsdlDocs;
  }

  /**
   * Given a WSDL element, call ValidateElement for it.
   * 
   * @param namespace The namespace of the element to validate.
   * @param element The element to validate.
   * @param parents The list of parents for this element.
   */
  public void validateWSDLElement(String namespace, Object element, List parents, IWSDL11ValidationInfo wsdlvalinfo)
  {
    IWSDL11Validator val = ver.queryValidatorRegistry(namespace);
    if (val != null)
    {
      val.validate(element, parents, wsdlvalinfo);
    }
    else
    {
	  //TODO: Add this as a preference.
      //wsdlvalinfo.addWarning(messagegenerator.getString(_WARN_NO_VALDIATOR, namespace), element);
    }
  }
  
  /**
   * Set the message generator for this controller.
   * 
   * @param mesgen The message generator to set for this controller.
   */
  public void setMessageGenerator(MessageGenerator mesgen)
  {
    messagegenerator = mesgen;
  }

  /**
   * Get the message generator registered for this controller.
   * 
   * @return The message generator registered for this controller.
   */
  public MessageGenerator getMessageGenerator()
  {
    return messagegenerator;
  }

  /**
   * Return the filename for the file currently being validated. Some validators require this.
   * 
   * @return The filename for the file being validated.
   */
//  public String getFilename()
//  {
//    return fileURI;
//  }

  /**
   * Convenience method for extensibly validators to add error messages.
   * 
   * @param object The object to add the error for.
   * @param error The error to add.
   */
//  public void addErrorMessage(Object object, String error)
//  {
//    addValidationMessage(ERROR_MESSAGE, object, error);
//    errors = true;
//  }

  /**
   * Method for extensibly validators to add error messages when they know
   * line and column numbers.
   * 
   * @param line The line where the error message is located.
   * @param column The column where the error message is located.
   * @param error The error message.
   */
//  public void addErrorMessage(int line, int column, String error)
//  {
//    addValidationMessage(ERROR_MESSAGE, line, column, error);
//    errors = true;
//  }

  /**
   * Convenience method for extensibly validators to add warning messages.
   * 
   * @param object The object to add the warning message.
   * @param warning The warning message.
   */
//  public void addWarningMessage(Object object, String warning)
//  {
//    addValidationMessage(WARNING_MESSAGE, object, warning);
//  }

  /**
   * Method for extensibly validators to add warning messages when they know
   * line and column numbers.
   * 
   * @param line The line where the error message is located.
   * @param column The column where the error message is located.
   * @param warning The warning message.
   */
//  public void addWarningMessage(int line, int column, String warning)
//  {
//    addValidationMessage(WARNING_MESSAGE, line, column, warning);
//  }

  /**
   * If you have an object read in by the reader for this
   * validatorcontroller the object can be passed in here and the line and column
   * information will be abstracted from it.
   * 
   * @param type The type of message to add.
   * @param o The object that has the error (used to get the location).
   * @param message The message to add.
   */
//  protected void addValidationMessage(int type, Object o, String message)
//  {
//    int[] location;
//    if (elementLocations.containsKey(o))
//    {
//      location = (int[])elementLocations.get(o);
//    }
//    // if we give it an element that hasn't been defined we'll set the location
//    // at (0,0) so the error shows up but no line marker in the editor
//    else
//    {
//      location = new int[] { 0, 0 };
//    }
//    addValidationMessage(type, location[0], location[1], message);
//  }

  /**
    * Creates a validation message of the specified type.
    * 
  	* @param type The type of validation message to add.
  	* @param line The line where the error message is located.
  	* @param column The line where the column message is located.
  	* @param message The message to add.
  	*/
//  protected void addValidationMessage(int type, int line, int column, String message)
//  {
//    if (message != null)
//    {
//      if (type == ERROR_MESSAGE)
//      {
//        validationController.addErrorMessage(line, column, message);
//      }
//      else if (type == WARNING_MESSAGE)
//      {
//        validationController.addWarningMessage(line, column, message);
//      }
//    }
//  }

  /**
   * @see org.eclipse.wsdl.validate.controller.IWSDLValidator#isValid()
   */
//  public boolean isValid()
//  {
//    return !errors;
//  }

  /**
   * Reset the validator controller.
   */
//  protected void reset()
//  {
//    schemas = new Vector();
//    fileURI = "";
//    wsdlDefinition = null;
//    elementLocations = null;
//    resourcebundle = null;
//    //validationController = null;
//    errors = false;
//  }
}
