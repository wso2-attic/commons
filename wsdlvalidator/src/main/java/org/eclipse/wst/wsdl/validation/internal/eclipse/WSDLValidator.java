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

package org.eclipse.wst.wsdl.validation.internal.eclipse;



/**
 * An Eclipse WSDL validator. This validator is the default validator
 * used in the validation framework. There is only a single instance of
 * this validator. When created, this validator registers all extension
 * URI resolvers.
 */
public class WSDLValidator extends org.eclipse.wst.wsdl.validation.internal.WSDLValidator
{
	private static WSDLValidator instance = null;

	/**
	 * The constructor registers all of the URI resolvers defined via the
	 * WSDL URI resolver extension point with the WSDL validator. 
	 * 
	 */
	protected WSDLValidator()
	{
      super();
      URIResolverWrapper resolver = new URIResolverWrapper();
      addURIResolver(resolver);
	}
	
	/**
	 * Get the one and only instance of this Eclipse WSDL validator.
	 * 
	 * @return The one and only instance of this Eclipse WSDL validator.
	 */
	public static WSDLValidator getInstance()
	{
		if(instance == null)
		{
			instance = new WSDLValidator();
		}
		return instance;
	}

}
