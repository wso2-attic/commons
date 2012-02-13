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
package org.eclipse.wst.wsdl.internal.impl.wsdl4j;


import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.xml.WSDLWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.wst.wsdl.internal.impl.DefinitionImpl;
import org.w3c.dom.Document;


public final class WSDLWriterImpl implements WSDLWriter
{
  /**
   * Sets the specified feature to the specified value.
   * <p>
   * There are no minimum features that must be supported.
   * <p>
   * All feature names must be fully-qualified, Java package style. All
   * names starting with javax.wsdl. are reserved for features defined
   * by the JWSDL specification. It is recommended that implementation-
   * specific features be fully-qualified to match the package name
   * of that implementation. For example: com.abc.featureName
   *
   * @param name the name of the feature to be set.
   * @param value the value to set the feature to.
   * @throws IllegalArgumentException if the feature name is not recognized.
   * @see #getFeature(String)
   */
  public void setFeature(String name, boolean value) throws IllegalArgumentException
  {
    throw new IllegalArgumentException("Not Implemented");
  }

  /**
   * Gets the value of the specified feature.
   *
   * @param name the name of the feature to get the value of.
   * @throws IllegalArgumentException if the feature name is not recognized.
   * @see #setFeature(String, boolean)
   */
  public boolean getFeature(String name) throws IllegalArgumentException
  {
    throw new IllegalArgumentException("Not Implemented");
  }

  /**
   * Return a document generated from the specified WSDL model.
   */
  public Document getDocument(Definition wsdlDef) throws WSDLException
  {
    return ((DefinitionImpl)wsdlDef).getDocument();
  }

  /**
   * Write the specified WSDL definition to the specified Writer.
   *
   * @param wsdlDef the WSDL definition to be written.
   * @param sink the Writer to write the xml to.
   */
  public void writeWSDL(Definition wsdlDef, Writer sink) throws WSDLException
  {
    String encoding = null;
    try
    {
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();

      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty(OutputKeys.METHOD, "xml");

      // Unless a width is set, there will be only line breaks but no indentation.
      // The IBM JDK and the Sun JDK don't agree on the property name,
      // so we set them both.
      //
      transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      if (encoding != null)
      {
        transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
      }

      Document document = ((DefinitionImpl)wsdlDef).getDocument();
      if (document == null)
      {
        ((DefinitionImpl)wsdlDef).updateElement(true);
        document = ((DefinitionImpl)wsdlDef).getDocument();
      }

      transformer.transform(new DOMSource(document), new StreamResult(sink));
    }
    catch (TransformerException exception)
    {
      throw new WSDLException(WSDLException.OTHER_ERROR, "Failed to save Definitions.", exception);
    }

  }

  /**
   * Write the specified WSDL definition to the specified OutputStream.
   *
   * @param wsdlDef the WSDL definition to be written.
   * @param sink the OutputStream to write the xml to.
   */
  public void writeWSDL(Definition wsdlDef, OutputStream sink) throws WSDLException
  {

    Resource resource = ((DefinitionImpl)wsdlDef).eResource();
    try
    {
      resource.save(sink, null);
    }
    catch (IOException e)
    {
      throw new WSDLException(WSDLException.OTHER_ERROR, "Failed to save Definitions.", e);
    }
  }
}
