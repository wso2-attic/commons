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
package org.eclipse.wst.wsi.internal.core.profile.validator;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.util.WSIProperties;

/**
* This class is used to access an implementation of a ProfileValidatorFactory abstract class.
 *
 * @version 1.0.1
 * @author Peter Brittenham
 */
public abstract class ProfileValidatorFactory
{
  /**
   * Create a new instance of a validator depending on the given artifact name
   * @param artifactName - String containing the artifact name from the TAD
   * @return an object that implements the BaseValidator interface.
   * @throws WSIException if the validator cannot be instantiated
   */
  public abstract BaseValidator getValidatorForArtifact(String artifactName)
          throws WSIException;

  /** 
   * Create a new instance of a UDDI validator.
   * @return  an object that implements the WSDLValidator interface.
   * @throws WSIException if UDDI validator cannot be instantiated.
   * @deprecated -- use getValidatorForArtifact(String artifactName).
   */
  public abstract UDDIValidator newUDDIValidator() throws WSIException;

  /** 
   * Create a new instance of a WSDL validator.
   * @return an object that implements the WSDLValidator interface. 
   * @throws WSIException if WSDL validator cannot be instantiated.
   * @deprecated -- use getValidatorForArtifact(String artifactName).
   */
  public abstract WSDLValidator newWSDLValidator() throws WSIException;

  /** 
   * Create a new instance of a message validator.
   * NOTE: Do we need to differentiate between the tranport and SOAP 
   * message validator?
   *
   * @return an object that implements the MessageValidator interface.
   * @throws WSIException if message validatorcannot be instantiated.
   * @deprecated -- use getValidatorForArtifact(String artifactName).
   */
  public abstract MessageValidator newMessageValidator() throws WSIException;

  /** 
   * Create a new instance of an envelope validator.
   * @return an object that implements the EnvelopeValidator interface.
   * @throws WSIException if message validator cannot be instantiated. 
   * @deprecated -- use getValidatorForArtifact(String artifactName).
   */
  public abstract EnvelopeValidator newEnvelopeValidator() throws WSIException;

  /**
   * Instantiate the implementation of the ProfileValidatorFactory class.
   * The implementation class for this interface is specified in the following 
   * Java system property:
   * <UL>
   *   <LI>wsi.profile.validator.factory
   * </UL>
   *
   * @return the ProfileValidatorFactory object.
   * @throws WSIException if factory class cannot be instantiated.
   */
  public static ProfileValidatorFactory newInstance() throws WSIException
  {
    ProfileValidatorFactory factory = null;
    String factoryClassName = null;

    try
    {
      // Get factory class name
      factoryClassName =
        WSIProperties.getProperty(
          WSIProperties.PROP_VALIDATOR_FACTORY,
          WSIProperties.DEF_VALIDATOR_FACTORY);

      // Create the factory class
      Class factoryClass = Class.forName(factoryClassName);

      // Instantiate the factory 
      factory = (ProfileValidatorFactory) factoryClass.newInstance();
    }

    catch (Exception e)
    {
      throw new WSIException(
        "Could not instantiate factory class: " + factoryClassName + ".",
        e);
    }

    // Return factory
    return factory;
  }
}