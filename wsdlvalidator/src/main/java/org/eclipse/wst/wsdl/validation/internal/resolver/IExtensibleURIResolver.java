/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.validation.internal.resolver;

/**
 * An interface for the WSDL validator's URI resolver mechanism. The URI resolver
 * is used to find the location of an entity.
 */
public interface IExtensibleURIResolver
{
 /**
  * Resolve the location of the entity described by the publicId and systemId.
  * 
  * @param baseLocation The location of the resource that contains the uri.
  * @param publicId An optional public identifier (i.e. namespace name), or null if none.
  * @param systemId An absolute or relative URI, or null if none.
  * @param result The result of the resolution.
  */
  public void resolve(String baseLocation, String publicId, String systemId, IURIResolutionResult result);
}
