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

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xs.XSModel;
import org.eclipse.wst.wsdl.validation.internal.Constants;
import org.eclipse.wst.wsdl.validation.internal.IValidationInfo;
import org.eclipse.wst.wsdl.validation.internal.ValidationInfoImpl;
import org.eclipse.wst.wsdl.validation.internal.resolver.URIResolver;

/**
 * An implemenation of WSDL11ValidationInfo.
 */
public class WSDL11ValidationInfoImpl implements IWSDL11ValidationInfo
{
  private IValidationInfo valinfo = null;
  private Hashtable elementlocations = null;
  private List schemas = new Vector();
  
  public WSDL11ValidationInfoImpl(IValidationInfo valinfo)
  {
    this.valinfo = valinfo;
  }
  /**
   * @see org.eclipse.wst.wsdl.validation.internal.wsdl11.IWSDL11ValidationInfo#getFileURI()
   */
  public String getFileURI()
  {
    return valinfo.getFileURI();
  }

  /**
   * @see org.eclipse.wst.wsdl.validation.internal.wsdl11.IWSDL11ValidationInfo#addSchema(org.apache.xerces.xs.XSModel)
   */
  public void addSchema(XSModel xsModel)
  {
    if (xsModel != null)
    {
      schemas.add(xsModel);
    }

  }

  /**
   * @see org.eclipse.wst.wsdl.validation.internal.wsdl11.IWSDL11ValidationInfo#getSchemas()
   */
  public XSModel[] getSchemas()
  {
    return (XSModel[])schemas.toArray(new XSModel[schemas.size()]);
  }

  /* (non-Javadoc)
   * @see org.eclipse.wsdl.validate.wsdl11.WSDL11ValidationInfo#cleardSchemas()
   */
  public void clearSchemas()
  {
    schemas.clear();
  }
  /**
   * @see org.eclipse.wst.wsdl.validation.internal.wsdl11.IWSDL11ValidationInfo#setElementLocations(java.util.Hashtable)
   */
  public void setElementLocations(Hashtable elementLocations)
  {
    this.elementlocations = elementLocations;
  }

  /**
   * @see org.eclipse.wst.wsdl.validation.internal.wsdl11.IWSDL11ValidationInfo#addError(java.lang.String, java.lang.Object)
   */
  public void addError(String message, Object element)
  {
    addError(message, element, null, null);
  }
  
  /**
   * @see org.eclipse.wst.wsdl.validation.internal.wsdl11.IWSDL11ValidationInfo#addError(java.lang.String, java.lang.Object, java.lang.String, java.lang.Object[])
   */
  public void addError(String message, Object element, String errorKey, Object[] messageArguments)
  {
    LocationHolder location;
    if (elementlocations.containsKey(element))
    {
      location = (LocationHolder)elementlocations.get(element);
      addError(message, location.getLine(), location.getColumn(), location.getURI(), errorKey, messageArguments);
    }
    // if we give it an element that hasn't been defined we'll set the location
    // at (0,0) so the error shows up but no line marker in the editor
    else
    {
      addError(message, 0, 1, getFileURI());
    }
  }

  /**
   * @see org.eclipse.wst.wsdl.validation.internal.wsdl11.IWSDL11ValidationInfo#addWarning(java.lang.String, java.lang.Object)
   */
  public void addWarning(String message, Object element)
  {
    LocationHolder location;
    if (elementlocations.containsKey(element))
    {
      location = (LocationHolder)elementlocations.get(element);
      addWarning(message, location.getLine(), location.getColumn(), location.getURI());
    }
    // if we give it an element that hasn't been defined we'll set the location
    // at (0,0) so the error shows up but no line marker in the editor
    else
    {
      addWarning(message, 0, 1, getFileURI());
    }

  }

  /**
   * @see org.eclipse.wsdl.validate.wsdl11.WSDL11ValidationInfo#addNamespaceWithNoValidator(java.lang.String)
   */
//  public void addNamespaceWithNoValidator(String namespace)
//  {
//    valinfo.addNamespaceWithNoValidator(namespace);
//
//  }

  /**
   * @see org.eclipse.wst.wsdl.validation.internal.wsdl11.IWSDL11ValidationInfo#addError(java.lang.String, int, int)
   */
  public void addError(String message, int line, int column, String uri)
  {
    addError(message, line, column, uri, null, null);
  }

  public void addError(String message, int line, int column, String uri, String errorKey, Object[] messageArguments)
  { 
    try
    { ((ValidationInfoImpl)valinfo).addError(message, line, column, uri, errorKey, messageArguments);
    }
    catch (ClassCastException e)
    {
      valinfo.addError(message, line, column, uri);
    }
  }
  
  /**
   * @see org.eclipse.wst.wsdl.validation.internal.wsdl11.IWSDL11ValidationInfo#addWarning(java.lang.String, int, int)
   */
  public void addWarning(String message, int line, int column, String uri)
  {
    valinfo.addWarning(message, line, column, uri);
  }

  /* (non-Javadoc)
   * @see org.eclipse.wsdl.validate.wsdl11.WSDL11ValidationInfo#getURIResolver()
   */
  public URIResolver getURIResolver() 
  {
	return valinfo.getURIResolver();
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.validation.internal.wsdl11.IWSDL11ValidationInfo#getSchemaCache()
   */
  public XMLGrammarPool getSchemaCache() 
  {
	XMLGrammarPool grammarPool = (XMLGrammarPool)valinfo.getAttribute(Constants.XMLSCHEMA_CACHE_ATTRIBUTE);
	return grammarPool;
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.validation.internal.wsdl11.IWSDL11ValidationInfo#getXMLCache()
   */
  public XMLGrammarPool getXMLCache() 
  {
	XMLGrammarPool grammarPool = (XMLGrammarPool)valinfo.getAttribute(Constants.XML_CACHE_ATTRIBUTE);
	return grammarPool;
  }
  
}
