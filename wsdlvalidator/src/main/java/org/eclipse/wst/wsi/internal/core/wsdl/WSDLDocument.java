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
package org.eclipse.wst.wsi.internal.core.wsdl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.wsdl.Binding;
import javax.wsdl.Definition;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.xml.WSDLReader;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.wsdl.xsd.InlineSchemaValidator;
import org.eclipse.wst.wsi.internal.core.xml.XMLTags;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Document;

/**
 * This class provides a interface to a single WSDL document.
 * 
 * @version 1.0.1
 * @author Peter Brittenham  (peterbr@us.ibm.com)
 */
public class WSDLDocument
{
  /**
   * WSDL document location.
   */
  protected String fileName = null;

  /**
   * Definition element.
   */
  protected Definition definitions = null;

  /**
   * Document element
   */
  protected Document document = null;

  /**
   * WSDL element list with line and column numbers.
   */
  protected WSDLElementList wsdlElementList = null;

  /**
   * Map of targetNamespaces to the corresponding org.apache.xerces.xs.XSModel of schema elements.
   */
  private Map schemas = new HashMap();

  /**
   * List of schemas validation errors.
   */
  private List schemasValidationErrors = new ArrayList();

  /**
   * Read WSDL document from the specified file.
   * @param fileName WSDL document location.
   * @throws WSDLException if problem reading WSDL document.
   */
  public WSDLDocument(String fileName) throws WSDLException
  {
    this.fileName = fileName;
    ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();   

    try
    {                         
      Thread.currentThread().setContextClassLoader(WSDLDocument.class.getClassLoader());    

      // Get the WSDLReader
      WSDLReader wsdlReader = new WSDLReaderImpl();

      // Set features
      wsdlReader.setFeature(com.ibm.wsdl.Constants.FEATURE_VERBOSE, false);
      // DEBUG:
      //wsdlReader.setFeature(com.ibm.wsdl.Constants.FEATURE_VERBOSE, true);
      wsdlReader.setFeature(
        com.ibm.wsdl.Constants.FEATURE_IMPORT_DOCUMENTS,
        true);
      // DEBUG:
      //wsdlReader.setFeature(com.ibm.wsdl.Constants.FEATURE_IMPORT_DOCUMENTS,true);

      // Parse the WSDL document
      document = XMLUtils.parseXMLDocument(fileName);
      this.definitions = wsdlReader.readWSDL(fileName, document);

      // Since inline schema validator is used by several assertions, validate all
      // schemas right after WSDL validation
      validateSchemas();

      // Get the line and column number references
      this.wsdlElementList = ((WSDLReaderImpl) wsdlReader).getElementList();

      // DEBUG:
      //System.err.println(wsdlElementList.toString());
    }
    catch (Exception cce)
    {
      // Set element list to null
      this.wsdlElementList = null;
    }
    finally
    { 
      Thread.currentThread().setContextClassLoader(currentLoader);
    }    
  }

  /**
   * Create WSDL document from existing Definition object.
   * 
   * @param fileName WSDL document location.
   * @param definitions Definition object.
   */
  public WSDLDocument(String fileName, Definition definitions)
  {
    this.fileName = fileName;
    this.definitions = definitions;
    this.document = null;
    schemas.clear();
    schemasValidationErrors.clear();
  }

  /**
   * Get the WSDL document location.
   * @return he WSDL document location.
   */
  public String getLocation()
  {
    // Return the document location
    return this.fileName;
  }

  /**
   * Get the document element of the WSDL document.
   * @return the document element
   */
  public Document getDocument()
  {
    if (document == null && definitions != null)
    {
      try
      {
        document = XMLUtils.parseXMLDocument(definitions.getDocumentBaseURI());
      }
      catch (IOException ioe)
      {
      }
      catch (WSIException wsie)
      {
      }
    }
    return document;
  }

  /**
   * Get the definition element of the WSDL document.
   * @return the definition element
   */
  public Definition getDefinitions()
  {
    // Return the definition element
    return definitions;
  }

  /**
   * Get all the portType elements from the WSDL document.
   * @return the list of portType elements.
   */
  public PortType[] getPortTypes()
  {
    // Generate an array of PortTypes from the PortType map for this Definition
    return (PortType[]) definitions.getPortTypes().values().toArray(
      new PortType[0]);
  }

  /**
   * Get all the binding elements from the WSDL document.
   * @return the list of binding elements.
   */
  public Binding[] getBindings()
  {
    // Generate an array of Bindings from the Binding map for this Definition
    return (Binding[]) definitions.getBindings().values().toArray(
      new Binding[0]);
  }

  /**
   * Get all the service elements from the WSDL document.
   * @return the list of service elements.
   */
  public Service[] getServices()
  {
    // Generate an array of Services from the Service map for this Definition
    return (Service[]) definitions.getServices().values().toArray(
      new Service[0]);
  }

  /* Return string representation of this object.
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    // Return string
    return (definitions == null)
      ? "WSDL definition element not found."
      : definitions.toString();
  }

  /**
   * Get the element list.
   * @return the element list.
   */
  public WSDLElementList getElementList()
  {
    return this.wsdlElementList;
  }

  /**
   * Returns a schemas map.
   * @return a map of targetNamespace attribute values of the schema elements
   * to the corresponding org.apache.xerces.xs.XSModel object of that schema.
   */
  public Map getSchemas()
  {
    return schemas;
  }

  /**
   * Returns a list of schemas validation errors caused by the validateSchemas() procedure.
   * @return a list of schemas validation errors.
   */
  public List getSchemasValidationErrors()
  {
    return schemasValidationErrors; 
  }

  /**
   * The method invokes the inline schema validator which validates every schema element of the WSDL.
   */
  private void validateSchemas()
  {
    schemas.clear();
    schemasValidationErrors.clear();

    /* INLINE SCHEMA VALIDATOR */
    if (definitions != null
      && definitions.getTypes() != null
      && definitions.getTypes().getExtensibilityElements() != null)
    {
      InlineSchemaValidator schemaValidator = new InlineSchemaValidator();

      // Collecting schema's parent elements
      List parents = new ArrayList(2);
      parents.add(definitions.getTypes());
      parents.add(definitions);
      // Going through all the ExtensibilityElementS of wsdl:types
      // since they are potential schemas
      // See Java APIs for WSDL V1.0 (JSR110), section 4
      Iterator i = this.definitions.getTypes().getExtensibilityElements().iterator();
      while (i.hasNext())
      {
        ExtensibilityElement extEl = (ExtensibilityElement) i.next();
        if (extEl.getElementType().equals(XMLTags.ELEM_XSD_SCHEMA))
        {
          Map map = null;
          try
          {
            // Validating schema element
            map = schemaValidator.validate(extEl, parents, fileName);
          }
          catch (Exception e)
          {
            // Adding an exception to the errors list
            schemasValidationErrors.add(e.getMessage());
          }

          if (map != null)
          {
            // Adding returned XSModel to schemas map
            schemas.putAll(map);
          }
        }
      }
    }    
  }

  /**
   * Command line interface.
   * @param args from command line.
   */
  public static void main(String[] args)
  {
    try
    {
      WSDLDocument wsdlDocument = new WSDLDocument(args[0]);
      System.out.println(wsdlDocument.toString());
    }

    catch (Exception e)
    {
      e.toString();
      e.printStackTrace();
    }

    System.exit(0);
  }
}
