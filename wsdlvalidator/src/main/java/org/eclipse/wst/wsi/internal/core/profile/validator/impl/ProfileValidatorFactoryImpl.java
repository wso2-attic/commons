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
import java.util.Map;

import org.eclipse.wst.wsi.internal.WSITestToolsPlugin;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.profile.validator.BaseValidator;
import org.eclipse.wst.wsi.internal.core.profile.validator.EnvelopeValidator;
import org.eclipse.wst.wsi.internal.core.profile.validator.MessageValidator;
import org.eclipse.wst.wsi.internal.core.profile.validator.ProfileValidatorFactory;
import org.eclipse.wst.wsi.internal.core.profile.validator.UDDIValidator;
import org.eclipse.wst.wsi.internal.core.profile.validator.WSDLValidator;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.envelope.EnvelopeValidatorImpl;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.message.MessageValidatorImpl;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.uddi.UDDIValidatorImpl;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.wsdl.WSDLValidatorImpl;

/**
 * This class is an implementation of the WSILDocumentFactory.
 *
 * @version 1.0.1
 * @author: Peter Brittenham
 */
public class ProfileValidatorFactoryImpl extends ProfileValidatorFactory
{
  private static Map validatorRegistry;

  public static void addToValidatatorRegistry(String artifactName,
          BaseValidator validatorClass) {
      if (validatorRegistry == null)
          validatorRegistry = new HashMap();
      validatorRegistry.put(artifactName, validatorClass);
  }

  public BaseValidator getValidatorForArtifact(String artifactName) 
          throws WSIException {
      if (validatorRegistry == null) {
          BaseValidator validators[] = WSITestToolsPlugin.getPlugin().
                  getBaseValidators();
          for (int i = 0; i < validators.length; i++)
              addToValidatatorRegistry(validators[i].getArtifactType(),
              validators[i]);
          
      }
      if (validatorRegistry == null) {
          return null;
      }
      return (BaseValidator) validatorRegistry.get(artifactName);
  }
  
    /* (non-Javadoc)
   * @see org.wsi.test.profile.validator.ProfileValidatorFactory#newUDDIValidator()
   */
  public UDDIValidator newUDDIValidator() throws WSIException
  {
    // Create new UDDI validator 
    UDDIValidator uddiValidator = new UDDIValidatorImpl();
    // Return validator
    return uddiValidator;
  }
  /* (non-Javadoc)
   * @see org.wsi.test.profile.validator.ProfileValidatorFactory#newWSDLValidator()
   */
  public WSDLValidator newWSDLValidator() throws WSIException
  {
    // Create new WSDL validator 
    WSDLValidator wsdlValidator = new WSDLValidatorImpl();

    // Return validator
    return wsdlValidator;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.validator.ProfileValidatorFactory#newMessageValidator()
   */
  public MessageValidator newMessageValidator() throws WSIException
  {
    // Create new message validator 
    MessageValidator messageValidator = new MessageValidatorImpl();
    // Return validator
    return messageValidator;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.validator.ProfileValidatorFactory#newEnvelopeValidator()
   */
  public EnvelopeValidator newEnvelopeValidator() throws WSIException
  {
    // Create new envelope validator 
    EnvelopeValidator envelopeValidator = new EnvelopeValidatorImpl();
    // Return validator
    return envelopeValidator;
  }
}