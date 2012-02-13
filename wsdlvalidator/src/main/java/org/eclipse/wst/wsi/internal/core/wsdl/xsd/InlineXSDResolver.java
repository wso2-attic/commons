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
package org.eclipse.wst.wsi.internal.core.wsdl.xsd;

import java.io.IOException;
import java.io.StringReader;
import java.util.Hashtable;

import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;

/**
 * An XMLEntityResolver that allows inline schemas to resolve each other through imports.
 * 
 * @author Lawrence Mandel (lmandel@ca.ibm.com)
 */
public class InlineXSDResolver implements XMLEntityResolver
{
  protected Hashtable entities = new Hashtable();
  protected XMLInputSource referringSchemaInputSource = null;
  protected String referringSchemaNamespace = null;

  /**
   * Constuctor.
   */
  public InlineXSDResolver()
  {
  }

  /* (non-Javadoc)
   * @see org.apache.xerces.xni.parser.XMLEntityResolver#resolveEntity(org.apache.xerces.xni.XMLResourceIdentifier)
   */
  public XMLInputSource resolveEntity(XMLResourceIdentifier resourceIdentifier)
		 throws XNIException, IOException {
	String systemId = resourceIdentifier.getExpandedSystemId();
	String publicId = resourceIdentifier.getPublicId();
	String namespace = resourceIdentifier.getNamespace();
	XMLInputSource is = null;
	String schema = null;
	if (systemId == null)
	{
	  if(publicId == null)
	  {
	  	if(namespace == null)
	  	{	
	      return null;
	  	}
	  	else
	  	{
	  		systemId = namespace;
	  	}
	  }
	  else
	  {	
	    systemId = publicId;
	  }
	}
	  
	if(referringSchemaNamespace != null && referringSchemaNamespace.equals(systemId))
	{
		if(referringSchemaInputSource!=null)
		{
			return referringSchemaInputSource;
		}
	}
	else if ((schema = (String) entities.get(systemId)) != null && !schema.equals(""))
	{
		is = new XMLInputSource(publicId, systemId, systemId,new StringReader(schema),null);
	}
	
    //if(is == null)
    //{
    //	throw new IOException();
    //}
	return is;
  }

  /**
   * Add an inline schema.
   * 
   * @param targetNamespace - the target namespace of the schema
   * @param schema - a string representation of the schema
   */
  public void add(String targetNamespace, String schema)
  {
    entities.put(targetNamespace, schema);
  }
  
  /**
   * Add the referring inline schema. 
   * 
   * @param inputSource - a representation of the inline schema
   * @param namespace - the namespace of the inline schema
   */
  public void addReferringSchema(XMLInputSource inputSource, String namespace)
  {
    referringSchemaInputSource = inputSource;
    referringSchemaNamespace = namespace;
  }
}
