/*******************************************************************************
 * Copyright (c) 2001, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.util;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.Types;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.WSDLPlugin;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.wsdl.internal.impl.DefinitionImpl;
import org.eclipse.wst.wsdl.internal.util.WSDLResourceFactoryImpl;
import org.eclipse.wst.wsdl.internal.util.XSDSchemaLocatorAdapterFactory;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.DefaultJAXPConfiguration;
import org.eclipse.xsd.util.JAXPConfiguration;
import org.eclipse.xsd.util.JAXPPool;
import org.eclipse.xsd.util.XSDSchemaLocator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


/**
 * <!-- begin-user-doc -->
 * The <b>Resource</b> implementation for the model.
 * This specialized resource implementation supports it's own way of making keys and 
 * hrefs, and it's own serialization. This class is not intended for subclassing 
 * outside of the model implementation; it is intended to be used as is with the 
 * Resource framework.
 * <p>
 * <b>Note:</b> This class/interface is part of an interim API that is still under development and expected to
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken
 * (repeatedly) as the API evolves.
 * </p>
 * @since 1.0
 * @ignore
 * <!-- end-user-doc -->
 * @see org.eclipse.wst.wsdl.internal.util.WSDLResourceFactoryImpl
 * @generated
 */
public class WSDLResourceImpl extends ResourceImpl
{

  private boolean useExtensionFactories = true;

  private boolean continueOnLoadError = true;

  public static final String USE_EXTENSION_FACTORIES = "USE_EXTENSION_FACTORIES"; //$NON-NLS-1$

  public static final String CONTINUE_ON_LOAD_ERROR = "CONTINUE_ON_LOAD_ERROR"; //$NON-NLS-1$

  public static final String WSDL_ENCODING = "WSDL_ENCODING"; //$NON-NLS-1$

  public static final String WSDL_PROGRESS_MONITOR = "WSDL_PROGRESS_MONITOR"; //$NON-NLS-1$

  /**
   * Add this option with a value of Boolean.TRUE to the options map when
   * loading a resource to instruct the loader to track source code location for
   * each node in the source document.
   * 
   * @see WSDLParser#getUserData(org.w3c.dom.Node)
   * @see WSDLParser#getStartColumn(org.w3c.dom.Node)
   * @see WSDLParser#getEndColumn(org.w3c.dom.Node)
   * @see WSDLParser#getStartLine(org.w3c.dom.Node)
   * @see WSDLParser#getEndLine(org.w3c.dom.Node)
   */
  public static final String TRACK_LOCATION = "TRACK_LOCATION"; //$NON-NLS-1$

  /**
   * This option can be used as an option on Resource#load methods to specify JAXP pool to be used
   * for loading and serializing XML Schemas.
   * @see Resource#load(InputStream, Map)
   * @see Resource#load(Map)
   */
  public static final String WSDL_JAXP_POOL = "WSDL_JAXP_POOL"; //$NON-NLS-1$
  
  /**
   * This option can be used as an option on Resource#load methods to specify JAXP configuration
   * that creates and configures SAX, DOM parsers and Transformer.
   * @see Resource#load(InputStream, Map)
   * @see Resource#load(Map)
   */
  public static final String WSDL_JAXP_CONFIG = "WSDL_JAXP_CONFIG"; //$NON-NLS-1$
  
  /**
   * Creates an instance of the resource. 
   * <!-- begin-user-doc --> 
   * <!-- end-user-doc -->
   * 
   * @param uri the URI of the new resource. 
   * @generated
   */
  public WSDLResourceImpl(URI uri)
  {
    super(uri);
  }

  protected void doSave(OutputStream os, Map options) throws IOException
  {
    Definition definition = getDefinition();
    if (definition != null)
    {
      Document document = definition.getDocument();
      if (document == null)
      {
        ((DefinitionImpl)definition).updateDocument();
        document = definition.getDocument();
      }

      if (definition.getElement() == null)
      {
        ((DefinitionImpl)definition).updateElement();
      }

      doSerialize(os, document, options);
    }
  }

  /**
   * Returns the resource's Definition.
   */
  public Definition getDefinition()
  {
    return getContents().size() == 1 && getContents().get(0) instanceof Definition ? (Definition)getContents().get(0) : null;
  }

  private static void doSerialize(OutputStream outputStream, Document document, String encoding)
  {
    try
    {
      Transformer transformer = new DefaultJAXPConfiguration().createTransformer(encoding);
      // Be sure to use actual encoding of the transformer which might be non-null even if encoding started as null.
      encoding = transformer.getOutputProperty(OutputKeys.ENCODING);
      Writer writer = encoding == null ? new OutputStreamWriter(outputStream) : new OutputStreamWriter(outputStream, encoding);
      transformer.transform(new DOMSource(document), new StreamResult(writer));
    }
    catch (Exception exception)
    {
      WSDLPlugin.INSTANCE.log(exception);
    }
  }
  
  private static void doSerialize(OutputStream outputStream, Document document, Map options) throws IOException
  {
    JAXPPool jaxpPool = null;
    JAXPConfiguration config = null;
    String encoding = null;
    
    if (options != null)
    {
      jaxpPool = (JAXPPool)options.get(WSDL_JAXP_POOL);
      config = (JAXPConfiguration)options.get(WSDL_JAXP_CONFIG);
      encoding = (String)options.get(WSDL_ENCODING);
    }
    
    if (jaxpPool == null)
    {
      if (config == null)
      {
        doSerialize(outputStream, document, encoding);
      }
      else
      {
        try
        {
          Transformer transformer = config.createTransformer(encoding);
          // Be sure to use actual encoding of the transformer which might be non-null even if encoding started as null.
          encoding = transformer.getOutputProperty(OutputKeys.ENCODING);
          Writer writer = encoding == null ? new OutputStreamWriter(outputStream) : new OutputStreamWriter(outputStream, encoding);
          transformer.transform(new DOMSource(document), new StreamResult(writer));
        }
        catch (TransformerException exception)
        {
          WSDLPlugin.getPlugin().log(exception);
        }
      }
    }
    else
    {
      Transformer transformer = null;
      try
      {
        transformer = jaxpPool.getTransformer(encoding);
        Writer writer = encoding == null ? new OutputStreamWriter(outputStream) : new OutputStreamWriter(outputStream, encoding);
        transformer.transform(new DOMSource(document), new StreamResult(writer));
      }
      catch (TransformerException exception)
      {
        WSDLPlugin.INSTANCE.log(exception);
      }
      finally
      {
        jaxpPool.releaseTransformer(transformer);
      }
    }
  }

  /**
   * Loads a new {@link WSDLResourceImpl} into the resource set.
   * @param inputSource the contents of the new resource.
   * @param options any options to influence loading behavior.
   */
  protected void doLoad(InputSource inputSource, Map options) throws IOException
  {
    // This pattern avoids loading the IProgressMonitor class when there is no progress monitor.
    // This is important for stand-alone execution to work correctly.
    //
    IProgressMonitor progressMonitor = null;
    Object monitor = options == null ? null : options.get(WSDL_PROGRESS_MONITOR);
    if (monitor != null)
    {
      progressMonitor = (IProgressMonitor)monitor;
      progressMonitor.setTaskName(WSDLPlugin.INSTANCE.getString("_UI_ResourceLoad_progress")); //$NON-NLS-1$
      progressMonitor.subTask(getURI().toString());
    }

    Object bindings = options == null ? null : options.get(USE_EXTENSION_FACTORIES);
    if (bindings != null && bindings instanceof Boolean)
      // true by default
      useExtensionFactories = ((Boolean)bindings).booleanValue();

    Object continueOnError = options == null ? null : options.get(CONTINUE_ON_LOAD_ERROR);
    if (continueOnError != null && continueOnError instanceof Boolean)
      // true by default
      continueOnLoadError = ((Boolean)continueOnError).booleanValue();

    Document doc = null;
    try
    {
      boolean trackLocation = options != null && Boolean.TRUE.equals(options.get(TRACK_LOCATION));

      if (trackLocation)
      {
        doc = getDocumentUsingSAX(inputSource, options);
      }
      else
      {
        // Create a DOM document
        doc = getDocument(inputSource, new InternalErrorHandler(), options);
      }

      if (doc != null && doc.getDocumentElement() != null)
      {
        if (!findDefinition(doc.getDocumentElement()))
        {
          if (continueOnLoadError)
            handleDefinitionElement(doc.getDocumentElement());
          else
            throw new IOException(WSDLPlugin.getPlugin().getString("_ERROR_INVALID_WSDL")); //$NON-NLS-1$
        }
      }
      else
      {
        handleDefinitionElement(null);
      }
    }
    catch (IOException exception)
    {
      if (continueOnLoadError)
      {
        WSDLPlugin.INSTANCE.log(exception);
        handleDefinitionElement(null);
      }
      else
        throw exception;
    }

    Definition definition = null;

    for (Iterator i = getContents().iterator(); i.hasNext();)
    {
      definition = (Definition)i.next();

      // Initialize the inline schemas location 
      Types types = definition.getETypes();
      if (types != null)
      {
        XSDSchemaExtensibilityElement el = null;
        for (Iterator j = types.getEExtensibilityElements().iterator(); j.hasNext();)
        {
          ExtensibilityElement extensibilityElement = (ExtensibilityElement)j.next();
          if (!(extensibilityElement instanceof XSDSchemaExtensibilityElement))
          {
            continue;
          }
          
          el = (XSDSchemaExtensibilityElement) extensibilityElement;
          XSDSchema schema = el.getSchema();
          if (schema != null)
            schema.setSchemaLocation(getURI().toString());
        }
      }
    }

    if (progressMonitor != null)
    {
      progressMonitor.worked(1);
    }
  }

  /**
   * Loads a new {@link WSDLResourceImpl} into the resource set.
   * @param inputStream the contents of the new resource.
   * @param options any options to influence loading behavior.
   */
  protected void doLoad(InputStream inputStream, Map options) throws IOException
  {
    InputSource inputSource = inputStream instanceof URIConverter.ReadableInputStream ? new InputSource(
      ((URIConverter.ReadableInputStream)inputStream).asReader()) : new InputSource(inputStream);

    if (getURI() != null)
    {
      String id = getURI().toString();
      inputSource.setPublicId(id);
      inputSource.setSystemId(id);
    }
    doLoad(inputSource, options);
  }

  /**
   * Use a custom SAX parser to allow us to track the source location of 
   * each node in the source XML document.
   * @param inputSource the parsing source. Must not be null.
   * @param options the loading options. 
   * @return the DOM document created by parsing the input stream. 
   */
  private Document getDocumentUsingSAX(InputSource inputSource, Map options)
  {
    WSDLParser wsdlParser = new WSDLParser(options);
    wsdlParser.parse(inputSource);

    Collection errors = wsdlParser.getDiagnostics();

    if (errors != null)
    {
      Iterator iterator = errors.iterator();

      while (iterator.hasNext())
      {
        WSDLDiagnostic wsdlDiagnostic = (WSDLDiagnostic)iterator.next();
        switch (wsdlDiagnostic.getSeverity().getValue())
        {
          case WSDLDiagnosticSeverity.FATAL:
          case WSDLDiagnosticSeverity.ERROR:
          {
            getErrors().add(wsdlDiagnostic);
            break;
          }
          case WSDLDiagnosticSeverity.WARNING:
          case WSDLDiagnosticSeverity.INFORMATION:
          {
            getWarnings().add(wsdlDiagnostic);
            break;
          }
        }
      }
    }

    Document doc = wsdlParser.getDocument();
    
    if (wsdlParser.getEncoding() != null)
    {
      getDefaultSaveOptions().put(WSDL_ENCODING, wsdlParser.getEncoding());
    }
    
    return doc;
  }

  /**
   * Builds a document using Xerces.
   * @param inputSource the contents to parse.
   * @param errorHandler the handled used by the parser.
   * @return a document.
   */
  private static Document getDocument(InputSource inputSource, ErrorHandler errorHandler) throws IOException
  {
    ClassLoader previousClassLoader = Thread.currentThread().getContextClassLoader();
    try
    {
      Thread.currentThread().setContextClassLoader(WSDLResourceFactoryImpl.class.getClassLoader());

      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
      documentBuilderFactory.setNamespaceAware(true);
      documentBuilderFactory.setValidating(false);

      try
      {
        // Using a deferred DOM document in the WSDL model may cause a
        // performance problem in terms of memory consumption in particular.
        // We're attempting to use the feature which instructs the Xerces parser
        // to not use deferred DOM trees.
        // TODO Convert to use setFeature when it becomes available. The Xerces
        // versions < 2.7.1 do not fully support setFeature, so we have to use
        // setAttribute.
        documentBuilderFactory.setAttribute("http://apache.org/xml/features/dom/defer-node-expansion", Boolean.FALSE); //$NON-NLS-1$
      }
      catch (IllegalArgumentException e)
      {
        // Ignore, as the code will have to run with parsers other than Xerces.
      }

      DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

      // TBD - Revisit
      //EntityResolver entityResolver = createEntityResolver();
      //documentBuilder.setEntityResolver(entityResolver);

      documentBuilder.setErrorHandler(errorHandler);

      Document document = documentBuilder.parse(inputSource);
      return document;
    }
    catch (ParserConfigurationException exception)
    {
      throw new IOWrappedException(exception);
    }
    catch (SAXException exception)
    {
      throw new IOWrappedException(exception);
    }
    finally
    {
      Thread.currentThread().setContextClassLoader(previousClassLoader);
    }
  }
  
  /**
   * Builds DOM document using JAXP DocumentBuilder
   * @see #WSDL_JAXP_POOL
   * @see #WSDL_JAXP_CONFIG
   * @param inputSource the content to parser
   * @param errorHandler error handler for recording any loading errors
   * @param options loading options
   * @return document DOM document
   * @throws IOException
   */
  private static Document getDocument(InputSource inputSource, ErrorHandler errorHandler, Map options) throws IOException
  {
    JAXPPool jaxpPool = null;
    JAXPConfiguration config = null;
    if (options != null)
    {
      jaxpPool = (JAXPPool)options.get(WSDL_JAXP_POOL);
      config = (JAXPConfiguration)options.get(WSDL_JAXP_CONFIG);
    }

    if (jaxpPool == null)
    {
      if (config == null)
      {
        return getDocument(inputSource, errorHandler);
      }
      else
      {
        try
        {
          DocumentBuilder documentBuilder = config.createDocumentBuilder(errorHandler);
          Document document = documentBuilder.parse(inputSource);
          return document;
        }
        catch (ParserConfigurationException exception)
        {
          throw new IOWrappedException(exception);
        }
        catch (SAXException exception)
        {
          throw new IOWrappedException(exception);
        }
      }
    }
    else
    {
      DocumentBuilder documentBuilder = null;
      try
      {
        documentBuilder = jaxpPool.getDocumentBuilder(errorHandler);
        Document document = documentBuilder.parse(inputSource);
        return document;
      }
      catch (ParserConfigurationException exception)
      {
        throw new IOWrappedException(exception);
      }
      catch (SAXException exception)
      {
        throw new IOWrappedException(exception);
      }
      finally
      {
        jaxpPool.releaseDocumentBuilder(documentBuilder);
      }
    }
  }

  private boolean findDefinition(Element element)
  {
    if (WSDLConstants.nodeType(element) == WSDLConstants.DEFINITION)
    {
      handleDefinitionElement(element);
      return true;
    }
    else
    {
      boolean result = false;
      /*
       for (Node child = element.getFirstChild(); child != null; child = child.getNextSibling())
       {
       if (child instanceof Element)
       {
       if (findDefinition((Element) child))
       {
       result = true;
       }
       }
       }
       */
      return result;
    }
  }

  private void handleDefinitionElement(Element element)
  {
    Definition definition = null;
    if (element == null)
    {
      definition = WSDLFactory.eINSTANCE.createDefinition();
      ((DefinitionImpl)definition).setUseExtensionFactories(useExtensionFactories);
    }
    else
    {
      definition = DefinitionImpl.createDefinition(element, getURI().toString(), useExtensionFactories);
    }
    getContents().add(definition);
    // Do we need the next line?
    ((DefinitionImpl)definition).reconcileReferences(true);
  }

  public static void serialize(OutputStream outputStream, Document document)
  {
    serialize(outputStream, document, null);
  }

  public static void serialize(OutputStream outputStream, Document document, String encoding)
  {
    doSerialize(outputStream, document, encoding);
  }

  public static void serialize(OutputStream outputStream, Element element)
  {
    serialize(outputStream, element, null);
  }

  public static void serialize(OutputStream outputStream, Element element, String encoding)
  {
    try
    {
      doSerialize(outputStream, element, encoding);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
  }

  private static void doSerialize(OutputStream outputStream, Element element, String encoding) throws IOException
  {
    try
    {
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();

      transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$
      transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
      if (encoding != null)
      {
        transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
      }

      transformer.transform(new DOMSource(element), new StreamResult(outputStream));
    }
    catch (TransformerException exception)
    {
      WSDLPlugin.INSTANCE.log(exception);
    }
  }

  private class InternalErrorHandler implements ErrorHandler
  {
    public void error(SAXParseException e)
    {
      System.out.println("WSDL PARSE ERROR: " + e);
    }

    public void fatalError(SAXParseException e)
    {
      System.out.println("WSDL PARSE FATAL ERROR: " + e);
    }

    public void warning(SAXParseException e)
    {
      System.out.println("WSDL PARSE WARNING: " + e);
    }
  }

  public void attached(EObject eObject)
  {
    super.attached(eObject);

    // we need to attach a XSDSchemaLocator in order to resolve inline schema locations
    // if there's not already one attached
    XSDSchemaLocator xsdSchemaLocator = (XSDSchemaLocator)EcoreUtil.getRegisteredAdapter(this, XSDSchemaLocator.class);
    if (xsdSchemaLocator == null)
    {
      getResourceSet().getAdapterFactories().add(new XSDSchemaLocatorAdapterFactory());
    }

    if (eObject instanceof DefinitionImpl)
    {
      DefinitionImpl definition = (DefinitionImpl)eObject;
      definition.setInlineSchemaLocations(this);
    }
  }
  /*
   public void setInlineSchemaLocations(Definition definition)
   {
   // Initialize the inline schemas location 
   Types types = definition.getETypes();
   if (types != null)
   {
   for (Iterator j = types.getEExtensibilityElements().iterator(); j.hasNext();)
   {
   XSDSchemaExtensibilityElement el = (XSDSchemaExtensibilityElement) j.next();
   XSDSchema schema = el.getSchema();
   if (schema != null)
   {  
   schema.setSchemaLocation(getURI().toString());
   }  
   }        
   }      
   }*/

  public Map getDefaultSaveOptions()
  {
    if (defaultSaveOptions == null)
    {
      defaultSaveOptions = new HashMap();
    }

    return defaultSaveOptions;
  }
} //WSDLResourceFactoryImpl
