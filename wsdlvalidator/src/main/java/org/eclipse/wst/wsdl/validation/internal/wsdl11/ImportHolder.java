/*******************************************************************************
 * Copyright (c) 2001, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.validation.internal.wsdl11;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.wsdl.Definition;
import javax.wsdl.Import;
import javax.wsdl.WSDLException;

import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xs.XSModel;
import org.eclipse.wst.wsdl.validation.internal.IValidationMessage;
import org.eclipse.wst.wsdl.validation.internal.resolver.IURIResolutionResult;
import org.eclipse.wst.wsdl.validation.internal.util.ErrorMessage;
import org.eclipse.wst.wsdl.validation.internal.util.MessageGenerator;
import org.eclipse.wst.wsdl.validation.internal.wsdl11.xsd.XSDValidator;
import org.eclipse.wst.wsdl.validation.internal.xml.AbstractXMLConformanceFactory;
import org.eclipse.wst.wsdl.validation.internal.xml.DefaultXMLValidator;
import org.eclipse.wst.wsdl.validation.internal.xml.IXMLValidator;
import org.eclipse.wst.wsdl.validation.internal.xml.XMLCatalogResolver;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import com.ibm.wsdl.Constants;
import com.ibm.wsdl.extensions.schema.SchemaConstants;
import com.ibm.wsdl.util.StringUtils;
import com.ibm.wsdl.util.xml.DOMUtils;
import com.ibm.wsdl.util.xml.QNameUtils;

/**
 * A class to hold and parse an import element.
 */
public class ImportHolder implements Comparable
{
  private MessageGenerator messagegenerator;
  
  private WSDLDocument importingWSDLDoc = null;
  private WSDLDocument wsdlDocument = null;
  private Definition importingDef = null;
  private Element importingDocImportElement = null;
  private String namespace = null;
  private String location = null;
  private String classpathURI = null;
  private String contextURI = null;
  private int depth;
  private Element element = null;
  private List schemas = new ArrayList();
  private boolean isWSDLFileImport = true;
  private boolean importInvalid = false;
  private Import importDef = null;
  private IWSDL11ValidationInfo valinfo;
  
  /**
   * Constructor.
   * 
   * @param namespace The namespace of the import.
   * @param location The location of the import.
   * @param contextURI The context URI for resolving the import location.
   * @param wsdlDoc The WSDLDocument that contains the import.
   * @param depth The depth of the import.
   * @param importingDocImportElement The element representing the import in the encapsulating WSDLDocument.
   * @param messagegenerator A messagegenerator for obtaining strings.
   * @param valinfo The WSDL11ValidationInfo for reporting messages.
   */
  public ImportHolder(String namespace, String location, String contextURI, WSDLDocument importingWSDLDoc, int depth, Element importingDocImportElement, MessageGenerator messagegenerator, IWSDL11ValidationInfo valinfo)
  {
    this.messagegenerator = messagegenerator;
    this.valinfo = valinfo;
    this.importingWSDLDoc = importingWSDLDoc;
    if(importingWSDLDoc != null)
    {
      this.importingDef = importingWSDLDoc.getDefinition();
    }
    this.importingDocImportElement = importingDocImportElement;
    this.depth = depth;
    this.namespace = namespace;
    this.location = location;
    
    //  Allow WSDL imports to have no location attribute even though it is required.
    // Schema will normally catch the problem but this allows users to override the
    // schema and have the validator run.
    if (this.location == null)
    {
      this.location = namespace;
    }
    this.contextURI = contextURI;
    
    this.location = this.location.replace('\\','/');
    IURIResolutionResult classpathURI = valinfo.getURIResolver().resolve(this.contextURI, this.namespace, this.location);
    if(classpathURI.getLogicalLocation() != null)
    {
      this.location = classpathURI.getLogicalLocation();
    }
    if(classpathURI.getPhysicalLocation() != null)
    {
      this.classpathURI = classpathURI.getPhysicalLocation();
      this.contextURI = null;
    } 
  }
  
  public void initialize()
  {
    Element documentElement = null;
    try
    {
      documentElement = getElement();
    }
    catch(WSDLException e)
    {
    }
    if(documentElement != null)
    {
  	  // Handle WSDL imports.
      if (QNameUtils.matches(Constants.Q_ELEM_DEFINITIONS, documentElement))
      {
        if(isXMLValid(this.classpathURI))
        {
          try
          {
            wsdlDocument = new WSDLDocument(this.location, documentElement, this.depth, this.messagegenerator, this.valinfo);
            createWSDLImport(wsdlDocument);
          }
          catch(WSDLException e)
          {
            valinfo.addError(messagegenerator.getString("_UNABLE_TO_IMPORT_BAD_LOCATION", "'" + importDef.getLocationURI() + "'"), importingDocImportElement);
          }
        }
      }
      // Handle schema imports.
      else if (QNameUtils.matches(SchemaConstants.Q_ELEM_XSD_2001, documentElement))
      {
        createXSDImport();
      }
    }
  }
  
  protected boolean isXMLValid(String uri)
  {
    IXMLValidator xmlValidator = AbstractXMLConformanceFactory.getInstance().getXMLValidator();
    xmlValidator.setFile(uri);
    xmlValidator.setURIResolver(valinfo.getURIResolver());
    //xmlValidator.setValidationInfo(valInfo);
    if(xmlValidator instanceof DefaultXMLValidator)
    {
    	XMLGrammarPool grammarPool = valinfo.getXMLCache();
        if(grammarPool != null)
          ((DefaultXMLValidator)xmlValidator).setGrammarPool(grammarPool);
    }
    xmlValidator.run();
    // if there are no xml conformance problems go on to check the wsdl stuff
    if (xmlValidator.hasErrors())
    {
      // temp handling of XML errors until validator is updated.
      List errors = xmlValidator.getErrors();
      Iterator errorsIter = errors.iterator();
      while (errorsIter.hasNext())
      {
        IValidationMessage valMes = (IValidationMessage)errorsIter.next();
        valinfo.addError(valMes.getMessage(), valMes.getLine(), valMes.getColumn(), valMes.getURI());
      }
      importInvalid = true;
      return false;
    }
    return true;
  }
  
  /**
   * Get the importing WSDLDocument.
   * 
   * @return The importing WSDLDocument.
   */
  public WSDLDocument getImportingDocument()
  {
    return importingWSDLDoc;
  }
  
  /**
   * Get the WSDL document this import represents.
   * 
   * @return The WSDL document this import represents.
   */
  public WSDLDocument getWSDLDocument()
  {
    return wsdlDocument;
  }
  
  /**
   * Get the namespace.
   * 
   * @return The namespace.
   */
  public String getNamespace()
  {
    return namespace;
  }
  
  /**
   * Get the location.
   * 
   * @return The location.
   */
  public String getLocation()
  {
    return location;
  }
  
  /**
   * Get the context URI.
   * 
   * @return The context URI.
   */
  public String getContextURI()
  {
    return contextURI;
  }
  
  /**
   * Get the depth in the WSDL tree.
   * 
   * @return The depth in the WSDL tree.
   */
  public int getDepth()
  {
    return depth;
  }
  
  /**
   * Get the containing defintions element.
   * 
   * @return The containing definitions element.
   */
  public Definition getImportingDefinition()
  {
    return importingDef;
  }
  
  /**
   * Get the element for this import.
   * 
   * @return The element for this import.
   * @throws WSDLException
   */
  public Element getElement() throws WSDLException
  {
    if(element != null)
    {
      return element;
    }
    
    String locationURI = location;
    //Import importDef = importingDef.createImport();
    
    //  Allow locating WSDL documents using any registered URI resolver.
    //String classpathURI = URIResolver.getInstance().resolve(contextURI, namespaceURI, locationURI);
//    if (!classpathURI.equals(locationURI))
//    {
//      locationURI = classpathURI;
//      contextURI = null;
//    }
    InputStream reader = null;
    if (locationURI != null)
    {
        try
        {
          //String contextURI = def.getDocumentBaseURI();
          //Definition importedDef = null;
          
          InputSource inputSource = null;
          URL url = null;

          
            URL contextURL = (contextURI != null) ? StringUtils.getURL(null, contextURI) : null;

            url = StringUtils.getURL(contextURL, locationURI);
            
            // Handle file:// urls. The correct format should be file:/// or file:/.
            String urlAuthority = url.getAuthority();
            String urlProtocol = url.getProtocol();
            if(urlAuthority !=null && urlProtocol.equalsIgnoreCase("file") && !urlAuthority.equals(""))
             {
              url = new URL(urlProtocol,"","/" + urlAuthority + url.getFile());
            }
            
            String urlString = url.toString();
			// Test for case sensitivity on local files.
			if(urlString.startsWith("file:"))
			{
			  File testfile = new File(url.getFile());
			  String testfileString = testfile.getAbsolutePath();
			  String canonicalfileString = testfile.getCanonicalPath();

              if (!testfileString.equals(canonicalfileString))
              {
				if (!String.valueOf(testfileString.charAt(0)).equalsIgnoreCase
				    (String.valueOf(canonicalfileString.charAt(0)))
				    || !testfileString.substring(1,testfileString.length()).equals
					(canonicalfileString.substring(1,canonicalfileString.length())))
				{
                  urlString = "";
                  url = null;
				}
              }
			}
			if(url != null)
			{
			  try
			  {
                reader = StringUtils.getContentAsInputStream(url);
			  }
			  catch(IOException e)
			  {
			    // No need to do anything here. The error will be handled below.
			  }
			}
            if (reader != null)
            {
              inputSource = new InputSource(reader);
              if(classpathURI != null && !classpathURI.equals(location))
  			  {
  			    inputSource.setByteStream(new URL(classpathURI).openStream());
  			  }
            }

            if (inputSource == null)
            {
              // Get the actual location from the element.
              String actualLocation = DOMUtils.getAttribute(importingDocImportElement, Constants.ATTR_LOCATION);
              if(actualLocation == null)
              {
              	actualLocation = DOMUtils.getAttribute(importingDocImportElement, "schemaLocation");
              }
              if(actualLocation == null)
              {
              	actualLocation = namespace;
              }
              importingWSDLDoc.addReaderWarning(
                  importingDef,
                  importingDocImportElement,
                  messagegenerator.getString("_UNABLE_TO_IMPORT_BAD_LOCATION", "'" + actualLocation + "'"));
              importInvalid = true;
               
              // TODO: modify the reader error to show in all the correct locations.
              throw new WSDLException(
                WSDLException.OTHER_ERROR,
                "Unable to locate imported document "
                  + "at '"
                  + locationURI
                  + "'"
                  + (contextURI == null ? "." : ", relative to '" + contextURI + "'."));
            }
            Document doc = null;
            try
            {
              doc = WSDLReaderImpl.getDocument(inputSource, locationURI);
            }
            catch(WSDLException e)
            {
              // The File is invalid and cannot be read. 
              // Perform XML validation.
              isXMLValid(locationURI);
//              importingWSDLDoc.addReaderError(
//                  importingDef,
//                  importingDocImportElement,
//                  messagegenerator.getString("_UNABLE_TO_IMPORT_INVALID", "'" + location + "'"));
              throw e;
            }
            element = doc.getDocumentElement();
            if(!QNameUtils.matches(Constants.Q_ELEM_DEFINITIONS, element))
            {
              isWSDLFileImport = false;
            }
            // Ensure that the imported document has the same namespace as the import element states.
            String importTargetNS = element.getAttribute(Constants.ATTR_TARGET_NAMESPACE);
            if(!importTargetNS.equals(namespace))
            {
              importingWSDLDoc.addReaderWarning(
                  importingDef,
                  importingDocImportElement,
                  messagegenerator.getString("_WARN_WRONG_NS_ON_IMPORT", "'" + namespace + "'", "'" + importTargetNS + "'"));
              element = null;
              importInvalid = true;
            }
          }
        
        catch(Exception e)
        {
        }
        finally
        {
          if(reader != null)
          {
            try
            {
              reader.close();
            }
            catch(IOException e)
            {}
          }
        }
      
    }
    return element;
  }
  
  /**
   * Create an import element for a WSDL import of a WSDL document.
   * 
   * @param wsdlDocument The document of the import.
   * @return The newly created import element.
   */
  public Import createWSDLImport(WSDLDocument wsdlDocument)
  {
    if(importDef != null)
    {
      return importDef;
    }
    importDef = getNewImport();
  
    if (importDef != null)
    {
      importDef.setDefinition(wsdlDocument.getDefinition());
      schemas.addAll(wsdlDocument.getSchemas());
      importingWSDLDoc.addSchemas(schemas);
    }

    return importDef;
  }
  
  /**
   * Create an import element for a WSDL import of a schema import of a schema document.
   * 
   * @return The newly created import element.
   */
  public Import createXSDImport()
  {
    if(importDef != null)
    {
      return importDef;
    }
    importDef = getNewImport();
    XSDValidator xsdvalidator = new XSDValidator();

    xsdvalidator.validate(location, XMLCatalogResolver.getInstance(), valinfo.getSchemaCache());
    if (xsdvalidator.isValid())
    {
      XSModel schema = xsdvalidator.getXSModel();
      if (schema != null)
      {
        schemas.add(schema);
      }
    }
    else
    {
     // addReaderWarning(
//        def,
//        importDef,
//        messagegenerator.getString("_UNABLE_TO_IMPORT_INVALID", "'" + importDef.getLocationURI() + "'"));
      Iterator errors = xsdvalidator.getErrors().iterator();
      while (errors.hasNext())
      {
        ErrorMessage err = (ErrorMessage) errors.next();
        String uri = err.getURI();
        int line = err.getErrorLine();
        String errmess = err.getErrorMessage();
        valinfo.addError(errmess, line, err.getErrorColumn(), uri);
      }
    }
    importingWSDLDoc.addSchemas(schemas);
    return importDef;
  }
  
  /**
   * Get the import element if it has been created.
   * 
   * @return The import element if it has been created or null.
   */
  public Import getImport()
  {
    return importDef;
  }
  
  /**
   * Get a new import element.
   * 
   * @return A new import element.
   */
  private Import getNewImport()
  {
    if(importInvalid)
    {
      return null;
    }
    Import importDef = importingDef.createImport(); 
    
    if (namespace != null)
    {
      importDef.setNamespaceURI(namespace);
    }

    if (location != null)
    {
      importDef.setLocationURI(location);
    }
    
    if(element != null)
    {
      Element tempEl = DOMUtils.getFirstChildElement(element);

      while (tempEl != null)
      {
        if (QNameUtils.matches(Constants.Q_ELEM_DOCUMENTATION, tempEl))
        {
          importDef.setDocumentationElement(tempEl);
        }

        tempEl = DOMUtils.getNextSiblingElement(tempEl);
      }
    }

    return importDef;
  }
  
  /**
   * Get the schemas corresponding to this import.
   * 
   * @return The schemas corresponding to this import.
   */
  public List getSchemas()
  {
    return schemas;
  }
  
  /**
   * Returns true if this import imports a WSDL document, false otherwise.
   * 
   * @return True if this import imports a WSDL document, false otherwise.
   */
  public boolean isWSDLFileImport()
  {
    return isWSDLFileImport;
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object obj)
  {
    if(obj.getClass() == ImportHolder.class)
    {
      ImportHolder otherImport = (ImportHolder)obj;
      
      if(getNamespace().equals(otherImport.getNamespace()) && getLocation().equals(otherImport.getLocation()))
      {
        return true;
      }
    }
    return false;
  }
  
  /* (non-Javadoc)
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  public int compareTo(Object obj)
  {
    if(obj == null)
    {
      throw new NullPointerException();
    }
    
    ImportHolder otherImport = (ImportHolder)obj;
      
    return (getNamespace()+getLocation()).compareTo((otherImport.getNamespace()+otherImport.getLocation()));
  }
  
  /**
   * Set the messagegenerator for the import holder.
   * 
   * @param mg The message generator to set.
   */
  public void setMessageGenerator(MessageGenerator mg)
  {
    messagegenerator = mg;
  }
  
  /**
   * Return true if the import is invalid, false otherwise.
   * 
   * @return True if the import is invalid, false otherwise.
   */
  public boolean isImportInvalid()
  {
    return importInvalid;
  }
}
