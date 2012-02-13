/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.util;


import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryRegistryImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

// NOTICE: This class has been edited to fix compilation failures (lines: 68, 116-163) - Senaka

/**
 * This class provides support for the loading of WSDL or XSD resource from
 * URI's that don't end with .xsd or .wsdl extensions.
 * 
 * These URI's foil EMF's standard resource loading mechanism wich assumes that
 * a resource type can always be deduced from the file extension.
 * 
 *  Code Example:
 *    ResourceSet resourceSet = new ResourceSetImpl();
 *    WSDLResourceFactoryRegistry registry = new WSDLResourceFactoryRegistry(Resource.Factory.Registry.INSTANCE);
 *    resourceSet.setResourceFactoryRegistry(registry);
 * <p>
 * <b>Note:</b> This class/interface is part of an interim API that is still under development and expected to
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken
 * (repeatedly) as the API evolves.
 * </p>
 * @since 1.0
 */
public class WSDLResourceFactoryRegistry extends ResourceFactoryRegistryImpl
{
  protected Resource.Factory.Registry parent;

  public WSDLResourceFactoryRegistry(Resource.Factory.Registry parent)
  {
    this.parent = parent;
  }

  public Map getExtensionToFactoryMap()
  {
    return parent.getExtensionToFactoryMap();
  }

  public Map getProtocolToFactoryMap()
  {
    return parent.getProtocolToFactoryMap();
  }

  public Factory getFactory(URI uri)
  {
    /*return getFactory(uri, null);*/
    String uriString = uri.toString();

    Factory factory = parent.getFactory(uri);
    Factory defaultFactory = (Factory)INSTANCE.getFactory(URI.createURI(DEFAULT_EXTENSION));

    // give the parent the first crack at getting the factory
    // if the factory is null or the known 'default' factory then we'll
    // try to compute the factory ourselves
    if (factory == null || factory == defaultFactory)
    {
      if (uriString.endsWith("wsdl") || uriString.endsWith("WSDL"))
      {
        // handle cases like "http://xxx/Distance.jws?wsdl"
        //
        factory = parent.getFactory(URI.createURI("*.wsdl"));
      }
      else if (uriString.endsWith("xsd") || uriString.endsWith("XSD"))
      {
        // handle cases like "http://xxx/Distance.jws?xsd"
        //
        factory = parent.getFactory(URI.createURI("*.xsd"));
      }
      else
      //if (uri.fileExtension() == null)
      {
        // handle cases like "http://xxx/example?getQuote"
        //
        // finally we must resort to opening the uri's stream and
        // peeking at the content to determine if its an xsd or wsdl
        //
        String rootElementName = getRootElementName(uriString);
        if (rootElementName != null)
        {
          if (rootElementName.equals("schema"))
          {
            factory = parent.getFactory(URI.createURI("*.xsd"));
          }
          else if (rootElementName.equals("definitions"))
          {
            factory = parent.getFactory(URI.createURI("*.wsdl"));
          }
        }
      }
    }
    return factory;
  }

  /*public Resource.Factory getFactory(URI uri, String contentType)
  {
    String uriString = uri.toString();

    Factory factory = parent.getFactory(uri, contentType);
    Factory defaultFactory = (Factory)INSTANCE.getFactory(URI.createURI(DEFAULT_EXTENSION), contentType);

    // give the parent the first crack at getting the factory
    // if the factory is null or the known 'default' factory then we'll
    // try to compute the factory ourselves
    if (factory == null || factory == defaultFactory)
    {
      if (uriString.endsWith("wsdl") || uriString.endsWith("WSDL"))
      {
        // handle cases like "http://xxx/Distance.jws?wsdl"
        //          	
        factory = parent.getFactory(URI.createURI("*.wsdl"), contentType);
      }
      else if (uriString.endsWith("xsd") || uriString.endsWith("XSD"))
      {
        // handle cases like "http://xxx/Distance.jws?xsd"
        //        	
        factory = parent.getFactory(URI.createURI("*.xsd"), contentType);
      }
      else
      //if (uri.fileExtension() == null)
      {
        // handle cases like "http://xxx/example?getQuote"
        //              	
        // finally we must resort to opening the uri's stream and
        // peeking at the content to determine if its an xsd or wsdl
        //         	
        String rootElementName = getRootElementName(uriString);
        if (rootElementName != null)
        {
          if (rootElementName.equals("schema"))
          {
            factory = parent.getFactory(URI.createURI("*.xsd"), contentType);
          }
          else if (rootElementName.equals("definitions"))
          {
            factory = parent.getFactory(URI.createURI("*.wsdl"), contentType);
          }
        }
      }
    }
    return factory;
  }*/

  // this method uses a SAX Parser to parser a stream and determine the root element name
  // of any xml content
  private String getRootElementName(String uri)
  {
    RootElementNameContentHandler handler = new RootElementNameContentHandler();
    try
    {
      XMLReader reader = XMLReaderFactory.createXMLReader();
      reader.setContentHandler(handler);
      reader.parse(uri);
    }
    catch (Exception e)
    {
    }

    return handler.rootElementName;
  }

  protected static class RootElementNameContentHandler extends DefaultHandler
  {
    public String rootElementName;

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
      rootElementName = localName;

      // here I throw an exception to make the SAX Parser to stop parsing
      throw new SAXException("SAXParser intentonally stopped");
    }
  }
}
