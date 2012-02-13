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

package org.eclipse.wst.wsdl.validation.internal;

import java.io.InputStream;
import java.util.ResourceBundle;

import org.eclipse.wst.wsdl.validation.internal.resolver.IExtensibleURIResolver;
import org.eclipse.wst.wsdl.validation.internal.resolver.URIResolver;
import org.eclipse.wst.wsdl.validation.internal.wsdl11.ClassloaderWSDL11ValidatorDelegate;
import org.eclipse.wst.wsdl.validation.internal.wsdl11.WSDL11BasicValidator;
import org.eclipse.wst.wsdl.validation.internal.wsdl11.WSDL11ValidatorController;
import org.eclipse.wst.wsdl.validation.internal.wsdl11.WSDL11ValidatorDelegate;
import org.eclipse.wst.wsdl.validation.internal.wsdl11.http.HTTPValidator;
import org.eclipse.wst.wsdl.validation.internal.wsdl11.mime.MIMEValidator;
import org.eclipse.wst.wsdl.validation.internal.wsdl11.soap.SOAPValidator;

import com.ibm.wsdl.Constants;

/**
 * An main WSDL validator class. The WSDL validator validates WSDL documents.
 */
public class WSDLValidator
{
  private static String VALIDATOR_RESOURCE_BUNDLE = "validatewsdl";
  private ValidationController validationController;
  protected URIResolver uriResolver;
  
  /**
   * Constructor.
   */
  public WSDLValidator()
  {
    ResourceBundle rb = ResourceBundle.getBundle(VALIDATOR_RESOURCE_BUNDLE);
    uriResolver = new URIResolver();
    validationController = new ValidationController(rb, uriResolver);
    
    //Register the default validators.
    ValidatorRegistry registry = ValidatorRegistry.getInstance();
    // Register the WSDL 1.1 validator controller and validators.
    WSDLValidatorDelegate delegate = new ClassloaderWSDLValidatorDelegate(WSDL11ValidatorController.class.getName(), getClass().getClassLoader());
    registry.registerValidator(Constants.NS_URI_WSDL, delegate, ValidatorRegistry.WSDL_VALIDATOR);
    WSDL11ValidatorDelegate delegate1 = new ClassloaderWSDL11ValidatorDelegate(WSDL11BasicValidator.class.getName(), getClass().getClassLoader());
    registerWSDL11Validator(Constants.NS_URI_WSDL, delegate1);
    delegate1 = new ClassloaderWSDL11ValidatorDelegate(HTTPValidator.class.getName(), getClass().getClassLoader());
    registerWSDL11Validator(org.eclipse.wst.wsdl.validation.internal.Constants.NS_HTTP, delegate1);
    delegate1 = new ClassloaderWSDL11ValidatorDelegate(SOAPValidator.class.getName(), getClass().getClassLoader());
    registerWSDL11Validator(org.eclipse.wst.wsdl.validation.internal.Constants.NS_SOAP11, delegate1);
    delegate1 = new ClassloaderWSDL11ValidatorDelegate(MIMEValidator.class.getName(), getClass().getClassLoader());
    registerWSDL11Validator(org.eclipse.wst.wsdl.validation.internal.Constants.NS_MIME, delegate1);
   
    // The WSDL 1.1 schema validator is a special case as it is registered for three namespaces.
//    delegate1 = new WSDL11ValidatorDelegate(InlineSchemaValidator.class.getName(), VALIDATOR_RESOURCE_BUNDLE, getClass().getClassLoader());
//    registerWSDL11Validator(Constants.NS_URI_XSD_1999, delegate1);
//    registerWSDL11Validator(Constants.NS_URI_XSD_2000, delegate1);
//    registerWSDL11Validator(Constants.NS_URI_XSD_2001, delegate1);
  }
  
  /**
   * Validate the WSDL file at the given location.
   * 
   * @param uri The location of the WSDL file to validate.
   * @return A validation report summarizing the results of the validation.
   */
  public IValidationReport validate(String uri)
  {
   return validate(uri, null, new WSDLValidationConfiguration());
  }
  
  /**
   * 
   * Validate the inputStream
   * @param uri The location of the WSDL file being validated
   * @param inputStream The stream to validate
   * @return A Validation report summarizing the results of the validation
   */
  public IValidationReport validate(String uri, InputStream inputStream, WSDLValidationConfiguration configuration)
  {
    if(uri == null) 
      return null;
    if(configuration == null)
      configuration = new WSDLValidationConfiguration();
    return validationController.validate(uri, inputStream, configuration);
  }
  
  /**
   * Add a URI resolver to the WSDL validator.
   * 
   * @param uriResolver The URI resolver to add to the WSDL validator.
   */
  public void addURIResolver(IExtensibleURIResolver uriResolver)
  {
  	this.uriResolver.addURIResolver(uriResolver);
  }
  
  /**
   * Register an extension WSDL validator delegate with this validator.
   * 
   * @param namespace The namespace the validator validates for. This is the WSDL namespace.
   * @param delegate The delegate that holds the validator.
   */
  public void registerWSDLExtensionValidator(String namespace, WSDLValidatorDelegate delegate)
  {
    ValidatorRegistry.getInstance().registerValidator(namespace, delegate, ValidatorRegistry.EXT_VALIDATOR);
  }
  
  /**
   * Register a WSDL 1.1 validator delegate with this validator.
   * 
   * @param namespace The namespace the validator validates for.
   * @param delegate The delegate that holds the validator.
   */
  public void registerWSDL11Validator(String namespace, WSDL11ValidatorDelegate delegate)
  {
    org.eclipse.wst.wsdl.validation.internal.wsdl11.ValidatorRegistry.getInstance().registerValidator(namespace, delegate);
  }
}
