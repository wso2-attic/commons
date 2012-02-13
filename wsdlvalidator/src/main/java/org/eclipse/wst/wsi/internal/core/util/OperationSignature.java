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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
import javax.xml.namespace.QName;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSITag;
import org.eclipse.wst.wsi.internal.core.wsdl.traversal.WSDLTraversal;
import org.eclipse.wst.wsi.internal.core.wsdl.traversal.WSDLTraversalContext;
import org.eclipse.wst.wsi.internal.core.wsdl.traversal.WSDLVisitor;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * The class implements mechanism for creating unique signature of operation.
 * 
 * @author Kulik
 */
public final class OperationSignature
{
  // the flag indicates whether the messages is represented as document style
  // vice versa is not generally true
  private boolean isDocumentStyle = false;
  private boolean isCreated = false;
  private boolean isFault = false;

  protected List signature = new LinkedList();
  private Element firstBody = null;

  /**
   * Class operates as container for matched operation.
  * @author Kulik
   */
  public final static class OperationMatch
  {
    final private String style;
    final private BindingOperation operation;
    final private List signature;

    /**
     * Constructor.
     * @param style operation style.
     * @param signature a signature.
     * @param operation a binding operation.
     */
    protected OperationMatch(
      String style,
      List signature,
      BindingOperation operation)
    {
      this.style = style;
      this.signature = signature;
      this.operation = operation;
    }
    /**
     * Gets operation style.
     * @return operation style.
     */
    public String getOperationStyle()
    {
      return style;
    }
    /**
     * Gets WSDL binding operation object.
     * @return WSDL binding operation object.
     */
    public BindingOperation getOperation()
    {
      return operation;
    }

    /**
     * Gets operation signature as list.
     * @return operation signature as list.
     */
    public List getSignature()
    {
      return signature;
    }
  }

  /**
   * The class searches BindingOperation by the given OperationSigbnature. 
  * @author Kulik
   */
  public final class Visitor implements WSDLVisitor
  {
    /* (non-Javadoc)
     * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.Part, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
     */
    public void visit(Part obj, Object parent, WSDLTraversalContext ctx)
    {
    }

    /* (non-Javadoc)
     * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.Service, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
     */
    public void visit(Service obj, Object parent, WSDLTraversalContext ctx)
    {
    }

    /* (non-Javadoc)
     * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.Types, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
     */
    public void visit(Types obj, Object parent, WSDLTraversalContext ctx)
    {
    }

    /* (non-Javadoc)
     * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.Operation, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
     */
    public void visit(Operation obj, Object parent, WSDLTraversalContext ctx)
    {
    }

    /* (non-Javadoc)
     * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.Input, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
     */
    public void visit(Input obj, Object parent, WSDLTraversalContext ctx)
    {
    }

    /* (non-Javadoc)
     * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.Output, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
     */
    public void visit(Output obj, Object parent, WSDLTraversalContext ctx)
    {
    }

    /* (non-Javadoc)
     * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.Fault, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
     */
    public void visit(Fault obj, Object parent, WSDLTraversalContext ctx)
    {
    }

    /* (non-Javadoc)
     * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.Binding, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
     */
    public void visit(Binding obj, Object parent, WSDLTraversalContext ctx)
    {
    }

    /* (non-Javadoc)
     * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.BindingOperation, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
     */
    public void visit(
      BindingOperation obj,
      Object parent,
      WSDLTraversalContext ctx)
    {
    }

    /* (non-Javadoc)
     * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.BindingInput, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
     */
    public void visit(
      BindingInput obj,
      Object parent,
      WSDLTraversalContext ctx)
    {
    }

    /* (non-Javadoc)
     * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.BindingOutput, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
     */
    public void visit(
      BindingOutput obj,
      Object parent,
      WSDLTraversalContext ctx)
    {
    }

    /* (non-Javadoc)
     * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(BindingFault, Object, WSDLTraversalContext)
     */
    public void visit(
      BindingFault obj,
      Object parent,
      WSDLTraversalContext ctx)
    {
    }

    /* (non-Javadoc) 
     * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(Import, Object, WSDLTraversalContext)
     */
    public void visit(Import obj, Object parent, WSDLTraversalContext ctx)
    {
    }

    /* (non-Javadoc) 
     * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(Element, Object, WSDLTraversalContext)
     */
    public void visit(Element obj, Object parent, WSDLTraversalContext ctx)
    {
    }

    /* (non-Javadoc)
     * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(Message, Object, WSDLTraversalContext)
     */
    public void visit(Message obj, Object parent, WSDLTraversalContext ctx)
    {
    }

    /* (non-Javadoc)
     * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(Port, Object, WSDLTraversalContext)
     */
    public void visit(Port obj, Object parent, WSDLTraversalContext ctx)
    {
    }

    /* (non-Javadoc)
     * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(PortType, Object, WSDLTraversalContext)
     */
    public void visit(PortType obj, Object parent, WSDLTraversalContext ctx)
    {
    }

    /* (non-Javadoc)
     * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(Definition, Object, WSDLTraversalContext)
     */
    public void visit(Definition obj, Object parent, WSDLTraversalContext ctx)
    {
    }

    /* (non-Javadoc) 
     * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(ExtensibilityElement, Object, WSDLTraversalContext)
     */
    public void visit(
      ExtensibilityElement obj,
      Object parent,
      WSDLTraversalContext ctx)
    {
    }

    /* (non-Javadoc) 
     * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(SOAPHeader, Object, WSDLTraversalContext)
     */
    public void visit(SOAPHeader obj, Object parent, WSDLTraversalContext ctx)
    {
    }

    /* (non-Javadoc) 
     * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(SOAPHeaderFault, Object, WSDLTraversalContext)
     */
    public void visit(
      SOAPHeaderFault obj,
      Object parent,
      WSDLTraversalContext ctx)
    {
    }

    /* (non-Javadoc) 
     * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(SOAPFault, Object, WSDLTraversalContext)
     */
    public void visit(SOAPFault obj, Object parent, WSDLTraversalContext ctx)
    {
    }

    private String requiredStyle = null;
    private String soapAction = null;
    private BindingOperation operation = null;
    private TypesRegistry registry = null;
    private boolean isSOAPActionRequired = false;

    /**
    * Constructor.
    */
    public Visitor()
    {
      super();
      // TODO Auto-generated constructor stub
    }

    /**
     * Constructor.
     * @param requiredStyle required style.
     * @param registry types registry.
     */
    public Visitor(String requiredStyle, TypesRegistry registry)
    {
      this.requiredStyle = requiredStyle;
      this.registry = registry;
    }

    /**
     * The method returns matched binding operation.
     * @return BindingOperation
     */
    public BindingOperation getMatchedOperation()
    {
      return operation;
    }

    /**
     * The method sets up required operation to be found.
     * The possible values is rpc and document.
     * @param requiredStyle operation style.
     */
    public void setRequiredStyle(String requiredStyle)
    {
      this.requiredStyle = requiredStyle;
    }

    /**
     * The method indicates whether SOAP Action be included into operation.
     * signature.
     * @return true if SOAP Action isd required.
     */
    public boolean isSOAPActionRequired()
    {
      return isSOAPActionRequired;
    }

    /**
     * Sets SOAP action to be included into signature.
     */
    public void setSOAPActionRequired()
    {
      isSOAPActionRequired = true;
    }

    /**
     * internal method.
     */
    public void visit(
      SOAPBinding binding,
      Object parent,
      WSDLTraversalContext ctx)
    {
      String style =
        (binding.getStyle() == null)
          ? WSIConstants.ATTRVAL_SOAP_BIND_STYLE_DOC
          : binding.getStyle();
      ctx.addParameter("style", style);
    }

    /**
     * internal method.
     */
    public void visit(
      SOAPOperation operation,
      Object parent,
      WSDLTraversalContext ctx)
    {
      soapAction = null;
      String style = operation.getStyle();
      if (style == null)
        style = (String) ctx.getParameter("style");
      if (!requiredStyle.equals(style))
        ctx.cancelBindingOperationProcessing();
      else
        //if (isSOAPActionRequired)
        soapAction = operation.getSoapActionURI();
    }

    /**
     * internal method.
     */
    public void visit(SOAPBody body, Object parent, WSDLTraversalContext ctx)
    {
      // assert parent instanceof BindingInput
      BindingOperation bop = ctx.getBindingOperation();
      if (bop.getOperation() == null
        || bop.getOperation().getInput() == null
        || bop.getOperation().getInput().getMessage() == null)
        return;

      Message msg = bop.getOperation().getInput().getMessage();
      List parts =
        WSDLUtil.getParts(bop.getOperation(), msg, body, requiredStyle);

      QName additionalName = null;
      // if operation is rpc, add to parts qname qith function name
      if (WSIConstants.ATTRVAL_SOAP_BIND_STYLE_RPC.equals(requiredStyle))
      {
        // found out target namespace
        String namespace = body.getNamespaceURI();
        if (namespace == null || namespace.length() == 0)
          // !! ATTENTION
          // namespace should be retrieved from service target nameapce            
          namespace = ctx.getBinding().getQName().getNamespaceURI();

        // insert operation name as first signature part
        additionalName =
          new QName(namespace, ctx.getBindingOperation().getName());
      }

      OperationSignature op =
        new OperationSignature(parts, null, registry, false);

      if (additionalName != null)
        op.getSignature().add(0, additionalName);
      //if (isSOAPActionRequired)
      //if (soapAction != null)
      if (isSOAPActionRequired && soapAction != null)
        op.getSignature().add(0, soapAction);
      if (op.getSignature().equals(signature))
      {
        // required operation is found
        operation = ctx.getBindingOperation();
        ctx.cancelBindingOperationProcessing();
        ctx.cancelBindingProcessing();
      }

      else
      {
        op = new OperationSignature(parts, null, registry, true);
        if (additionalName != null)
          op.getSignature().add(0, additionalName);

        //if (isSOAPActionRequired)
        //if (soapAction != null)
        if (isSOAPActionRequired && soapAction != null)
          op.getSignature().add(0, soapAction);

        if (op.getSignature().equals(signature))
        {
          // required operation is found
          operation = ctx.getBindingOperation();
          ctx.cancelBindingOperationProcessing();
          ctx.cancelBindingProcessing();
        }
      }
    }
  }

  /**
   * The constructor creates OperationSignature for SOAP message.
   * @param doc a Document object.
   */
  public OperationSignature(Document doc)
  {
    Element body =
      XMLUtils.findChildElement(
        doc.getDocumentElement(),
        WSITag.ELEM_SOAP_BODY);
    processParts(body, false);
    isDocumentStyle |= (signature.size() > 1);
  }

  /**
   * The constructor creates OperationSIgnature for 
   * list of <code>Part</code> objects and given WSDL document within these
   * parts are defined.
   * 
   * @param parts a list of Part objects.
   * @param wsdlDocument a WSDL document.
   * @param registry a types registry.
   * @param partial a partial status.
   */
  public OperationSignature(
    List parts,
    Document wsdlDocument,
    TypesRegistry registry,
    boolean partial)
  {
    if (parts != null)
    {
      Iterator it = parts.iterator();

      while (it.hasNext())
      {
        Part part = (Part) it.next();
        String localName = (partial) ? part.getName() : null;
        QName typeName = part.getTypeName();
        QName elementName = part.getElementName();

        String namespace = "";

        // If type name is not null, then a type element was used so it should be an RPC style signature 
        if (typeName != null)
        {
          // Part wrappers do not have namespaces 
          namespace = "";

          if (localName == null)
            localName = typeName.getLocalPart();
        }

        // If element name is not null, then it should be a document style signature
        else if (elementName != null)
        {
          namespace = elementName.getNamespaceURI();

          if (localName == null)
            localName = elementName.getLocalPart();
        }

        signature.add(new QName(namespace, localName));
      }
    }

    isCreated = true;
  }

  /**
   * The method recreates OperationSignature with assumption that operation is
   * RPC.
   */
  public void createRPCSignature()
  {
    isCreated = false;
    if (firstBody == null)
      return;
    processParts(firstBody, true);
    isDocumentStyle = false;
  }

  /**
   * Internal method processes WSDL parts and creates operation signature.
   * @param parent
   */
  private void processParts(Node parent, boolean processWrapper)
  {
    if (parent != null)
    {
      Node n = parent.getFirstChild();

      // variable indicates that first tag is processed
      boolean isFirst = true;
      // iterate all bodies
      while (n != null)
      {
        if (Node.ELEMENT_NODE == n.getNodeType())
        {
          if (isFirst && firstBody == null)
          {
            isFirst = false;
            firstBody = (Element) n;
            // check soapenv:fault
            if (XMLUtils.equals(n, WSITag.ELEM_SOAP_FAULT))
            {
              isFault = true;
              isCreated = false;
              return;
            }
          }

          String namespace = n.getNamespaceURI();
          String localName = n.getLocalName();

          /*I have no idea why this code is in here since it makes no sense at all, so I am commenting it out
          // try to get xsi:tag
          if (XMLUtils.getAttribute((Element) n, WSITag.ATTR_XSI_TYPE) != null) {
              // we suppose that RPC style does not use xsi:type attribute
              isDocumentStyle = true;
              Attr xsiType = XMLUtils.getAttribute((Element) n, WSITag.ATTR_XSI_TYPE);
              
              String xsiValue = xsiType.getNodeValue();
              int i = xsiValue.indexOf(':');
              if (i != -1)
              {
                  namespace = XMLUtils.findNamespaceURI(n, xsiValue.substring(0, i));
                  localName = xsiValue.substring(i+1);
              } 
          }
          */

          // If this is rpc-literal and processing a part (not the wrapper), then the part should not have a namespace
          // And if it does, then one of the test assertions will detect it
          if (!isDocumentStyle && processWrapper)
          {
            namespace = "";
          }

          if (namespace == null || namespace.length() == 0)
          {
            if (isDocumentStyle)
            {
              //Element firstChild  = null;
              Node it = n.getFirstChild();
              while (it != null && !(it instanceof Element))
                it = it.getNextSibling();
              if (it != null)
                namespace = it.getNamespaceURI();
            }
          }

          // normalize namespaces. All empty namespaces -> null 
          if (namespace != null && namespace.length() == 0)
            namespace = null;

          // put part into signature
          signature.add(new QName(namespace, localName));
        }

        n = n.getNextSibling();
      }
    }
    else
    {
      isCreated = false; // not found ?
      return;
    }

    isCreated = true;
  }

  /**
   * Indicates whether operation has document style or RPC. 
   * @return true if operation has document style.
   */
  public boolean isDocumentStyle()
  {
    return isDocumentStyle;
  }

  /**
   * Indicates whether WSDL fault parts should be included into signature or
   * not.
   * @return true if WSDL fault parts should be included into signature.
   */
  public boolean isFault()
  {
    return isFault;
  }

  /**
   * Indicates that operation signature was created.
   * @return true if operation signature was created.
   */
  public boolean isCreated()
  {
    return isCreated;
  }

  /**
   * Gets operation signature created for SOAP message.
   * @return operation signature created for SOAP message.
   */
  public List getSignature()
  {
    return signature;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  public int hashCode()
  {
    return signature.hashCode();
  }

  /**
   * @see java.lang.Object#equals(Object)
   */
  public boolean equals(Object o)
  {
    if (o instanceof OperationSignature)
      return signature.equals(((OperationSignature) o).getSignature());
    return false;
  }

  /**
   * The method matches operation based on the given SOAP message request,
   * WSDL binding, and soapAction.
   * 
   * @param inputMessage an SOAP message request.
   * @param soapAction a soap action.
   * @param binding a WSDL binding.
   * @param registry a types registry.
   * @return OperationMatch object.
   */
  public static OperationMatch matchOperation(
    Document inputMessage,
    String soapAction,
    Binding binding,
    TypesRegistry registry)
  {
    return matchOperation(inputMessage, soapAction, binding, registry, true);
  }

  /**
     * The method matches operation based on the given SOAP message request,
     * WSDL binding, and soapAction.
     * 
     * @param inputMessage SOAP message request.
     * @param soapAction a soap action.
     * @param binding a WSDL binding.
     * @param registry a types registry.
     * @param soapActionRequired must process the soapAction value even if it is null 
     * @return OperationMatch object.
     */
  public static OperationMatch matchOperation(
    Document inputMessage,
    String soapAction,
    Binding binding,
    TypesRegistry registry,
    boolean soapActionRequired)
  {
    // Parse request message
    OperationSignature signature = new OperationSignature(inputMessage);
    if (!signature.isCreated())
      return null;

    // first of all looking for document style
    // because rpc style is subset of document style  
    OperationSignature.Visitor resolver =
      signature.new Visitor(WSIConstants.ATTRVAL_SOAP_BIND_STYLE_DOC, registry);

    // normalize SOAPAction
    if (soapAction != null
      && soapAction.length() > 1
      && soapAction.charAt(0) == '"'
      && soapAction.charAt(soapAction.length() - 1) == '"')
      soapAction = soapAction.substring(1, soapAction.length() - 1);

    //      if soap action URI processing required
    // if soap action URI processing required 
    // analyze SOAPAction after parts being processed
    // put SOAPAction into signature
    //if (soapAction != null && soapAction.length() > 0) {
    if (soapAction != null)
    {
      signature.getSignature().add(0, soapAction);
    }

    if (soapActionRequired)
      resolver.setSOAPActionRequired();

    WSDLTraversal traversal = new WSDLTraversal();
    //VisitorAdaptor.adapt(resolver);
    traversal.setVisitor(resolver);
    traversal.visitSOAPBinding(true);
    traversal.visitSOAPBody(true);
    traversal.visitSOAPOperation(true);

    traversal.ignoreReferences();
    traversal.ignoreBindingOutput();
    traversal.traverse(binding);

    BindingOperation operation = resolver.getMatchedOperation();
    if (operation == null && !signature.isDocumentStyle())
    {
      resolver.setRequiredStyle(WSIConstants.ATTRVAL_SOAP_BIND_STYLE_RPC);
      signature.createRPCSignature();
      traversal.traverse(binding);
      operation = resolver.getMatchedOperation();
      if (operation == null)
        return null;
    }

    return new OperationMatch(
      resolver.requiredStyle,
      signature.getSignature(),
      resolver.getMatchedOperation());
  }
}
