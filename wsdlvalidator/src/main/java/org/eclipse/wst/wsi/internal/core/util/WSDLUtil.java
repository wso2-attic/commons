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
package org.eclipse.wst.wsi.internal.core.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.wsdl.Binding;
import javax.wsdl.Definition;
import javax.wsdl.Import;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Part;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.extensions.soap.SOAPBody;

import org.eclipse.wst.wsi.internal.core.WSIConstants;

/**
 * The utility class to automate WSDL processing.
 * 
 * @author Kulik
 */
public final class WSDLUtil
{

  /**
   * The method extracts list of parts for the given soapbind:body,
   * binding operation, message, and style.
   * 
   * @param op an operation.
   * @param m  a message.
   * @param body a SOAP body.
   * @param style an operation style.
   * @return list of parts for the given soapbind:body,
   *         binding operation, message, and style.
   */
  static public List getParts(
    Operation op,
    Message m,
    SOAPBody body,
    String style)
  {
    //		if null, get parts from message 
    if (body.getParts() == null)
      // if rpc style is used, try to use partOrder attribute from operation
      if (WSIConstants.ATTRVAL_SOAP_BIND_STYLE_RPC.equals(style))
        return m.getOrderedParts(op.getParameterOrdering());
      else
        return m.getOrderedParts(null);
    else
      // converse parts name to parts objects
      return m.getOrderedParts(body.getParts());
  }

  /**
   * The method iterates all imports from the given definition and expands all
   * imported messages, service, portTypes, and bindings to the definition.
   * 
   * @param def a WSDL definition.
   */
  static public void expandDefinition(Definition def)
  {
    if (def != null && def.getImports() != null)
    {
      Iterator it = def.getImports().values().iterator();
      while (it.hasNext())
      {
        List v = (List) it.next();
        if (v != null)
        {
          Iterator it2 = v.iterator();
          while (it2.hasNext())
            expandDefinition(def, (Import) it2.next(), new ArrayList());
        }
      }
    }
  }

  /**
   * The internal method to expand definition.
   * @param target WSDL definition.
   * @param im internal method.
   */
  static private void expandDefinition(Definition target, Import im, List processedDefinitions)
  {
    if (im != null)
    {
      Definition d = im.getDefinition();
      if ((d != null) && (!processedDefinitions.contains(d)))
      {
    	processedDefinitions.add(d);

        if (d.getMessages() != null)
        {
          Iterator it = d.getMessages().values().iterator();
          while (it.hasNext())
            target.addMessage((Message) it.next());
        }
        if (d.getPortTypes() != null)
        {
          Iterator it = d.getPortTypes().values().iterator();
          while (it.hasNext())
            target.addPortType((PortType) it.next());
        }
        if (d.getBindings() != null)
        {
          Iterator it = d.getBindings().values().iterator();
          while (it.hasNext())
            target.addBinding((Binding) it.next());
        }
        if (d.getServices() != null)
        {
          Iterator it = d.getServices().values().iterator();
          while (it.hasNext())
            target.addService((Service) it.next());
        }

        Iterator it = d.getImports().values().iterator();
        while (it.hasNext())
        {
          List v = (List) it.next();
          if (v != null)
          {
            Iterator it2 = v.iterator();
            while (it2.hasNext())
              expandDefinition(target, (Import) it2.next(), processedDefinitions);
          }
        }
      }
    }
  }

  /**
   * The method extracts part from the message.
   * 
   * @param message a SOAP message.
   * @param part a part name.
   * @return part from the specified message.
   */
  static public Part getPart(Message message, String part)
  {
    Part mesPart = null;
    if (part == null)
      mesPart = (Part) message.getOrderedParts(null).get(0);
    else
      mesPart = message.getPart(part);
    return mesPart;
  }
}
