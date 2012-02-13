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

package org.eclipse.wst.wsdl.validation.internal.xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.parsers.SAXParser;
import org.apache.xerces.parsers.StandardParserConfiguration;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.eclipse.wst.wsdl.validation.internal.ValidationMessageImpl;
import org.eclipse.wst.wsdl.validation.internal.logging.ILogger;
import org.eclipse.wst.wsdl.validation.internal.logging.LoggerFactory;
import org.eclipse.wst.wsdl.validation.internal.resolver.IURIResolutionResult;
import org.eclipse.wst.wsdl.validation.internal.resolver.URIResolver;
import org.eclipse.wst.wsdl.validation.internal.wsdl11.xsd.XSDValidator;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.ibm.wsdl.Constants;
import com.ibm.wsdl.extensions.schema.SchemaConstants;

/**
 * The default implementation of IXMLValidator.
 */
public class DefaultXMLValidator implements IXMLValidator
{
  protected String uri;
  protected URIResolver uriResolver = null;
  protected List errors = new ArrayList();
  //protected StringBuffer schemaLocationString = new StringBuffer();
  protected List ignoredNamespaceList = new ArrayList();
  
  protected InputStream inputStream = null;
  
  protected String currentErrorKey = null;
  protected Object[] currentMessageArguments = null;
  
  protected boolean isChildOfDoc = false;
  protected XMLGrammarPool grammarPool = null; 
  
  /**
   * A stack of start tag locations, used to move errors
   * reported at the close tag to be reported at the start tag.
   */
  protected Stack startElementLocations = new Stack();
  protected Set adjustLocationErrorKeySet = new TreeSet();

/**
   * Constructor.
   */
  public DefaultXMLValidator()
  {
    super();
    
    ignoredNamespaceList.add(SchemaConstants.NS_URI_XSD_1999);
    ignoredNamespaceList.add(SchemaConstants.NS_URI_XSD_2000);
    ignoredNamespaceList.add(SchemaConstants.NS_URI_XSD_2001);
    
    adjustLocationErrorKeySet.add("cvc-complex-type.2.4.b");
    adjustLocationErrorKeySet.add("cvc-complex-type.2.3");
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.validation.internal.xml.IXMLValidator#setFile(java.lang.String)
   */
  public void setFile(String uri)
  {
    this.uri = uri;
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.validation.internal.xml.IXMLValidator#setURIResolver(org.eclipse.wst.wsdl.validation.internal.resolver.URIResolver)
   */
  public void setURIResolver(URIResolver uriResolver)
  {
  	this.uriResolver = uriResolver;
  }
  
  /**
   * @param grammarPool
   */
  public void setGrammarPool(XMLGrammarPool grammarPool)
  {
	this.grammarPool = grammarPool;
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.validation.internal.xml.IXMLValidator#run()
   */
  public void run()
  {
    // Validate the XML file.
    try
    {
      Reader reader1 = null; // Used for validation parse.
      
      InputSource validateInputSource; 
     
      validateInputSource = new InputSource(uri);
      if (this.inputStream != null)
      {    
        String string = createStringForInputStream(inputStream);
        reader1 = new StringReader(string);
          
        validateInputSource.setByteStream(inputStream);
        validateInputSource.setCharacterStream(reader1);
      }
      
      XMLReader reader = createXMLReader();
      reader.parse(validateInputSource);
    }
    catch (SAXParseException e)
    {
      // No need to add an error here. SAXParseExceptions are reported by the error reporter.
    }
    catch (IOException e)
    {
      // No need to log this error. The WSDL validator will catch this error when creating the dom model.
      //LoggerFactory.getInstance().getLogger().log("The WSDL validator was unable to read file " + uri + ".", ILogger.SEV_ERROR, e);
    }
    catch (Exception e)
    {
      LoggerFactory.getInstance().getLogger().log("An exception occurred while performing WSDL XML conformance validation for file " + uri + ".", ILogger.SEV_ERROR, e);
    }
  }
  
  /**
   * @param inputStream
   * @return
   */
  final String createStringForInputStream(InputStream inputStream)
  {
    // Here we are reading the file and storing to a stringbuffer.
    StringBuffer fileString = new StringBuffer();
    try
    {
      InputStreamReader inputReader = new InputStreamReader(inputStream);
      BufferedReader reader = new BufferedReader(inputReader);
      char[] chars = new char[1024];
      int numberRead = reader.read(chars);
      while (numberRead != -1)
      {
        fileString.append(chars, 0, numberRead);
        numberRead = reader.read(chars);
      }
    }
    catch (Exception e)
    {
      LoggerFactory.getInstance().getLogger().log("The WSDL valdiator was unable to create to create a string representation of an input stream. WSDL XML validation may not have run correctly.", ILogger.SEV_ERROR, e);
    }
    return fileString.toString();
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.validation.internal.xml.IXMLValidator#hasErrors()
   */
  public boolean hasErrors()
  {
    return !errors.isEmpty();
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.validation.internal.xml.IXMLValidator#getErrors()
   */
  public List getErrors()
  {
    return errors;
  }
  
  /**
   * @param message
   * @param line
   * @param column
   * @param uri
   */
  public void addError(String message, int line, int column, String uri)
  {
	// For the following errors the line number will be modified to use that of the start
  	// tag instead of the end tag.
  	if (currentErrorKey != null && adjustLocationErrorKeySet.contains(currentErrorKey))  
  	{
  	  LocationCoordinate adjustedCoordinates = (LocationCoordinate)startElementLocations.peek();
  	  line = adjustedCoordinates.getLineNumber();
  	  column = adjustedCoordinates.getColumnNumner();
  	}
  	
	errors.add(new ValidationMessageImpl(message, line, column, ValidationMessageImpl.SEV_WARNING, uri, currentErrorKey, currentMessageArguments));
  }

  /**
   * The handler for the SAX parser. This handler will obtain the WSDL
   * namespace, handle errors and resolve entities.
   */
  protected class XMLConformanceDefaultHandler extends DefaultHandler
  {
	private Locator locator = null;
	
    public void setDocumentLocator(Locator locator) 
    {
		this.locator = locator;
		super.setDocumentLocator(locator);
	}

	/**
     * @see org.xml.sax.ErrorHandler#error(SAXParseException)
     */
    public void error(SAXParseException arg0) throws SAXException
    {
     String tempURI = arg0.getSystemId();
     if (inputStream!= null && arg0.getSystemId() == null)
     {
       //mh: In this case we are validating a stream so the URI may be null in this exception
       tempURI = uri;       
     }
     addError(arg0.getMessage(), arg0.getLineNumber(), arg0.getColumnNumber(), tempURI);
    }

    /**
     * @see org.xml.sax.ErrorHandler#fatalError(SAXParseException)
     */
    public void fatalError(SAXParseException arg0) throws SAXException
    {
      addError(arg0.getMessage(), arg0.getLineNumber(), arg0.getColumnNumber(), arg0.getSystemId());
    }

    /**
     * @see org.xml.sax.ErrorHandler#warning(SAXParseException)
     */
    public void warning(SAXParseException arg0) throws SAXException
    {
      addError(arg0.getMessage(), arg0.getLineNumber(), arg0.getColumnNumber(), arg0.getSystemId());
    }
	
	/**
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if(localName.equals("documentation") && (uri.equals(Constants.NS_URI_WSDL) || uri.equals(SchemaConstants.NS_URI_XSD_2001)|| uri.equals(SchemaConstants.NS_URI_XSD_1999) || uri.equals(SchemaConstants.NS_URI_XSD_2000)))
		{
		  isChildOfDoc = false;
		}
		super.endElement(uri, localName, qName);
		startElementLocations.pop();
	}
	/**
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		startElementLocations.push(new LocationCoordinate(locator.getLineNumber(), locator.getColumnNumber()));
		if(localName.equals("documentation") && (uri.equals(Constants.NS_URI_WSDL) || uri.equals(SchemaConstants.NS_URI_XSD_2001)|| uri.equals(SchemaConstants.NS_URI_XSD_1999) || uri.equals(SchemaConstants.NS_URI_XSD_2000)))
		{
		  isChildOfDoc = true;
		}
		super.startElement(uri, localName, qName, atts);
	}
  }
  
  /**
   * @param inputStream - set the inputStream to validate
   */
  public void setInputStream(InputStream inputStream)
  {
      this.inputStream = inputStream;
  }
  
  protected class XMLValidatorParserConfiguration extends StandardParserConfiguration
  {
	/* (non-Javadoc)
     * @see org.apache.xerces.parsers.DTDConfiguration#createErrorReporter()
     */
    protected XMLErrorReporter createErrorReporter()
    {
      return new XMLErrorReporter()
      {
        /* (non-Javadoc)
         * @see org.apache.xerces.impl.XMLErrorReporter#reportError(java.lang.String, java.lang.String, java.lang.Object[], short)
         */
        public void reportError(String domain, String key, Object[] arguments,
            short severity) throws XNIException
        {
          currentErrorKey = key;
          currentMessageArguments = arguments;
          super.reportError(domain, key, arguments, severity);
        }
      };
    }
  }
  
  /**
   * Create an XML Reader.
   * 
   * @return The newly created XML reader or null if unsuccessful.
   * @throws Exception
   */
  protected XMLReader createXMLReader() throws Exception
  {     
    SAXParser reader = null;
    try
    {
      reader = new org.apache.xerces.parsers.SAXParser(new XMLValidatorParserConfiguration());
      
      XMLConformanceDefaultHandler conformanceDefaultHandler = new XMLConformanceDefaultHandler();
      reader.setErrorHandler(conformanceDefaultHandler);
      reader.setContentHandler(conformanceDefaultHandler);
      
      // Older Xerces versions will thrown a NPE if a null grammar pool is set.
      if(grammarPool != null)
      {
        reader.setProperty(org.apache.xerces.impl.Constants.XERCES_PROPERTY_PREFIX + org.apache.xerces.impl.Constants.XMLGRAMMAR_POOL_PROPERTY, grammarPool);
      }
      reader.setProperty(org.apache.xerces.impl.Constants.XERCES_PROPERTY_PREFIX + org.apache.xerces.impl.Constants.ENTITY_RESOLVER_PROPERTY, new MyEntityResolver(uriResolver));
      reader.setFeature(org.apache.xerces.impl.Constants.XERCES_FEATURE_PREFIX + org.apache.xerces.impl.Constants.CONTINUE_AFTER_FATAL_ERROR_FEATURE, false);
      reader.setFeature(org.apache.xerces.impl.Constants.SAX_FEATURE_PREFIX + org.apache.xerces.impl.Constants.NAMESPACES_FEATURE, true);
      reader.setFeature(org.apache.xerces.impl.Constants.SAX_FEATURE_PREFIX + org.apache.xerces.impl.Constants.NAMESPACE_PREFIXES_FEATURE, true);
	  reader.setFeature(org.apache.xerces.impl.Constants.SAX_FEATURE_PREFIX + org.apache.xerces.impl.Constants.VALIDATION_FEATURE, true);
	  reader.setFeature(org.apache.xerces.impl.Constants.XERCES_FEATURE_PREFIX + org.apache.xerces.impl.Constants.SCHEMA_VALIDATION_FEATURE, true);
    } 
    catch(Exception e)
    { 
      LoggerFactory.getInstance().getLogger().log("Error creating XML reader for WSDL XML conformance validation.", ILogger.SEV_ERROR, e);
    }
    return reader;
  } 
  
  /**
   * A custom entity resolver that uses the URI resolver specified to resolve entities.
   */
  protected class MyEntityResolver implements XMLEntityResolver 
  {
    private URIResolver uriResolver;
    
    /**
     * Constructor.
     * 
     * @param uriResolver The URI resolver to use with this entity resolver.
     */
    public MyEntityResolver(URIResolver uriResolver)
    {
      this.uriResolver = uriResolver;
    }
    
    /* (non-Javadoc)
     * @see org.apache.xerces.xni.parser.XMLEntityResolver#resolveEntity(org.apache.xerces.xni.XMLResourceIdentifier)
     */
    public XMLInputSource resolveEntity(XMLResourceIdentifier rid) throws XNIException, IOException
    {
      // If we're currently examining a subelement of the 
	  // WSDL or schema documentation element we don't want to resolve it
	  // as anything is allowed as a child of documentation.
	  if(isChildOfDoc)
	  {
	    return new XMLInputSource(rid);
	  }
	  
	  boolean nsUsed = false;
	  
	  String ns = rid.getNamespace();
	  if(ns != null && ignoredNamespaceList.contains(ns))
	  {
		return new XMLInputSource(rid);
	  }
	  
	  String systemId = rid.getLiteralSystemId();
	  if(systemId == null)
	  {
		systemId = ns;
		nsUsed = true;
	  }
	  String publicId = rid.getPublicId();
      if(publicId == null)
      {
        publicId = systemId;
      }
      
      // Xerces tends to try to resolve locations with no information.
      // No need to do any processing if we have no information.
      if(publicId != null || systemId != null)
      {
        IURIResolutionResult result = uriResolver.resolve("", publicId, systemId);
        String uri = result.getPhysicalLocation();
        if (uri != null && !uri.equals(""))
        {
          // If the namespace was used to resolve this reference ensure a schema
          // has been returned. Namespaces tend to point to Web resources that
          // may or may not be schemas.
          boolean createEntityResult = true;
          if(nsUsed)
          {
        	XSDValidator xsdVal = new XSDValidator();
        	xsdVal.validate(uri, uriResolver, null);
        	if(!xsdVal.isValid())
        	  createEntityResult = false;
          }
          
          if(createEntityResult)
          {
	        try
	        {
		      URL entityURL = new URL(uri);
              XMLInputSource is = new XMLInputSource(rid.getPublicId(), systemId, result.getLogicalLocation());
		      is.setByteStream(entityURL.openStream());
              if (is != null)
              {
                return is;
              }
	        }
	        catch(Exception e)
	        {
		      // No need to report this error. Simply continue below.
	        }
          }
        }
      }
      return null;
    }
  }  
  
  /** 
   * A line and column number coordinate.
   */
  protected class LocationCoordinate
  {	
	private int lineNo = -1;
    private int columnNo = -1;
    
    public LocationCoordinate(int lineNumber, int columnNumber)
    {
      this.lineNo = lineNumber;
      this.columnNo = columnNumber;
    }
    	
    public int getLineNumber()
    { 
      return this.lineNo;
    }
    	
    public int getColumnNumner()
    { 
      return this.columnNo;
    } 
  }
}
