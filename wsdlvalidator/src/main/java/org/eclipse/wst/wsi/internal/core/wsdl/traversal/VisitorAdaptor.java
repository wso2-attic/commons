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
package org.eclipse.wst.wsi.internal.core.wsdl.traversal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import javax.wsdl.Binding;
import javax.wsdl.BindingFault;
import javax.wsdl.BindingInput;
import javax.wsdl.BindingOperation;
import javax.wsdl.BindingOutput;
import javax.wsdl.Definition;
import javax.wsdl.Fault;
import javax.wsdl.Import;
import javax.wsdl.Input;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Output;
import javax.wsdl.Part;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.Types;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.soap.SOAPBinding;
import javax.wsdl.extensions.soap.SOAPBody;
import javax.wsdl.extensions.soap.SOAPFault;
import javax.wsdl.extensions.soap.SOAPHeader;
import javax.wsdl.extensions.soap.SOAPHeaderFault;
import javax.wsdl.extensions.soap.SOAPOperation;

import org.w3c.dom.Element;

/**
 * The class adapts the given object with the implemented
 * <code>WSDLVisitor</code> methods.
 * 
 * @author Kulik
 */
public class VisitorAdaptor implements InvocationHandler
{

  // optimization hint
  static final private Class[] visitorClass = new Class[] { WSDLVisitor.class };

  /**
   * The array is used for determination of actual javax.wsdl class
   */
  private static Class[] wsdl =
    new Class[] {
      Part.class,
      Service.class,
      Types.class,
      Operation.class,
      Input.class,
      Output.class,
      Fault.class,
      Binding.class,
      BindingOperation.class,
      BindingInput.class,
      BindingOutput.class,
      BindingFault.class,
      Import.class,
      Element.class,
      Message.class,
      Port.class,
      PortType.class,
      Definition.class,
      ExtensibilityElement.class,
      SOAPBinding.class,
      SOAPBody.class,
      SOAPHeader.class,
      SOAPHeaderFault.class,
      SOAPFault.class,
      SOAPOperation.class };

  final private Object visitor;

  private Map methods = new HashMap();

  /**
   * Constructor.
   * @param o  a visitor object.
   */
  private VisitorAdaptor(Object o)
  {
    this.visitor = o;
  }

  /**
   * Adds method to method binding.
   * @param wsdlMethod    a WSDL method.
   * @param targetMethod  a  target method.
   */
  private void addMethodBinding(Method wsdlMethod, Method targetMethod)
  {
    methods.put(wsdlMethod, targetMethod);
  }

  /**
   * The method proxies all "visit(XXX)" methods to the corresponding
   * "visit(XXX)" methods of the target visitor object. 
   * @see java.lang.reflect.InvocationHandler#invoke(Object, Method, Object[])
   */
  public Object invoke(Object proxy, Method m, Object[] params)
  {
    try
    {
      Method target = (Method) methods.get(m);
      // assert target != null
      return target.invoke(visitor, params);
    }
    catch (Throwable t)
    {
      t.printStackTrace();
    }
    return null;
  }

  /**
   * The method extract the short class name. 
   * @param c  a class.
   * @return the short class name.
   */
  static private String getName(Class c)
  {
    if (c != null)
    {
      String name = c.getName();
      return name.substring(name.lastIndexOf(".") + 1);
    }
    return null;
  }

  /**
   * The method generates proxy for the given visitor.
   * Proxy redirects all visit(XXX) callback to the implemented in visitor
   * object.
   * @param visitor  a vistor object.
   * @return a proxy for the given visitor.
   */
  public static WSDLTraversal adapt(Object visitor)
  {
    WSDLTraversal traversal = new WSDLTraversal();

    if (visitor == null)
      throw new IllegalArgumentException("Visitor object can not be NULL");

    VisitorAdaptor adaptor = new VisitorAdaptor(visitor);

    // check whether methods are implemented
    // get real class of the object
    Class c = visitor.getClass();
    // iterates through wsdl artifacts and looking for
    // void visit(XXX) implemented methods.  
    for (int i = 0; i < wsdl.length; i++)
      try
      {
        // if method is not found the exception will be thrown
        Method m =
          c.getMethod(
            "visit",
            new Class[] { wsdl[i], Object.class, WSDLTraversalContext.class });
        // register binding WSDLVisitor method -> target visitor method
        adaptor.addMethodBinding(
          WSDLVisitor.class.getMethod(
            "visit",
            new Class[] { wsdl[i], Object.class, WSDLTraversalContext.class }),
          m);
        // register visitXXX in WSDLTraversalBuilder
        m =
          WSDLTraversal.class.getMethod(
            "visit" + getName(wsdl[i]),
            new Class[] { boolean.class });
        m.invoke(traversal, new Object[] { Boolean.TRUE });
      }
      catch (Exception e)
      {
      }

    // construct the WSDLVisitor by using java.lang.reflect.Proxy
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    WSDLVisitor wsdlVisitor =
      (WSDLVisitor) Proxy.newProxyInstance(loader, visitorClass, adaptor);
    // traverse WSDL document
    traversal.setVisitor(wsdlVisitor);
    return traversal;
  }
}
