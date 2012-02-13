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

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;


/**
 * A registry of IWSDLValidator validators. This holds the top level WSDL and WS-I validators.
 * Validators are registered by the namespace they validate.
 */
public class ValidatorRegistry
{
  /**
   * The WSDL validator type.
   */
  public final static Integer WSDL_VALIDATOR = new Integer(0);
  /**
   * The WS-I validator type.
   */
  public final static Integer EXT_VALIDATOR = new Integer(1);
  protected static ValidatorRegistry verInstance;
  /*
    This is a Map of Maps. The top-level Map is keyed by (Class)parentType,
    and the inner Maps are keyed by (QName)elementType.
    This idea is the same as is done in WSDL4J
  */
  protected Map validatorReg = new Hashtable();

  protected Iterator defaultValidatorIterator;

  /**
   * Constructor.
   */
  protected ValidatorRegistry()
  {
  }

  /**
   * Returns the instance of the registry.
   * 
   * @return The instance of the registry.
   */
  public static ValidatorRegistry getInstance()
  {
    if (verInstance == null)
    {
      verInstance = new ValidatorRegistry();
    }
    return verInstance;
  }
  /**
   * Register this validator of the given type with the given namespace.
   * 
   * @param namespace The namespace the validator validates.
   * @param valDelegate The validator delegate to register.
   * @param type The type of validator.
   */
  public void registerValidator(String namespace, WSDLValidatorDelegate valDelegate, Integer type)
  {
    // allow the null namespace but make it the empty string
    if (namespace == null)
    {
      namespace = "";
    }

    // get the hastable for the type of validator we want
    Hashtable typeValidatorReg = (Hashtable)validatorReg.get(type);
    // if it's null if means we haven't defined this type of validator yet
    if (typeValidatorReg == null)
    {
      typeValidatorReg = new Hashtable();
      validatorReg.put(type, typeValidatorReg);
    }
    
      List namespacevals = (List)typeValidatorReg.get(namespace);
      if(namespacevals == null)
      {
        namespacevals = new Vector();
        typeValidatorReg.put(namespace, namespacevals);
      }
      namespacevals.add(valDelegate);

  }
  /**
   * Ask for the validator associated with this namespace. If none is found
   * return null.
   * 
   * @param namespace The namespace for the validator that is being requested.
   * @param type The type of validator that is being requested.
   * @return An array of validator delegates if at least one is registered, null otherwise.
   */
  public WSDLValidatorDelegate[] queryValidatorRegistry(String namespace, Integer type)
  {
    // if the namespace is null allow it and treat it as the empty string
    if (namespace == null)
    {
      namespace = "";
    }
    Hashtable typeValidatorReg = (Hashtable)validatorReg.get(type);
    if (typeValidatorReg == null)
    {
      return null;

    }

    List valdels = (List)typeValidatorReg.get(namespace);
    if(valdels != null)
    {
      return (WSDLValidatorDelegate[])valdels.toArray(new WSDLValidatorDelegate[valdels.size()]);
    }

    return null;
  }

  /**
   * Returns true if there is a validator registered of the given type with the given namespace.
   * 
   * @param namespace The namespace of the validator.
   * @param type The type of the validator.
   * @return True if there is a validator registered for the namespace, false otherwise.
   */
  public boolean hasRegisteredValidator(String namespace, Integer type)
  {
    if (queryValidatorRegistry(namespace, type) != null)
    {
      return true;
    }
    return false;
  }
}
