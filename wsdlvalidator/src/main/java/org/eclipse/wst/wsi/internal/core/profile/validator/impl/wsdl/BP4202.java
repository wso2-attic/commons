/*******************************************************************************
 * Copyright (c) 2002, 2009 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.core.profile.validator.impl.wsdl;

import javax.wsdl.Message;
import javax.wsdl.Types;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.schema.Schema;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.WSITag;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcessVisitor;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.ErrorList;
import org.eclipse.wst.wsi.internal.core.wsdl.traversal.WSDLTraversal;
import org.eclipse.wst.wsi.internal.core.wsdl.traversal.WSDLTraversalContext;
import org.eclipse.wst.wsi.internal.core.xml.schema.XMLSchemaProcessor;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * BP4202. 
 * <context>For an XML schema definition defined in the wsdl:types element, or imported directly or indirectly by a schema definition defined in the wsdl:types element, which contains any schema annotation elements.</context>
 * <assertionDescription>An XML schema definition defined in the wsdl:types element, or imported directly or indirectly by a schema definition defined in the wsdl:types element, may use schema annotation elements as an extensibility mechanism.</assertionDescription>
 */
 public class BP4202 extends AssertionProcessVisitor implements WSITag 
 {
   private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP4202(WSDLValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }

  private static final String ANNOTATION_KEY = "annotation";
   
   private ErrorList errorList = new ErrorList();
   private AnnotationProcessor processor;

   /** 
    * (non-Javadoc)
    * @see org.eclipse.wst.wsi.wsdl.traversal.WSDLVisitor#visit(Message, Object, WSDLTraversalContext)
    */
   public void visit(ExtensibilityElement obj, Object parent, WSDLTraversalContext ctx)
   {
     if((obj != null) && (obj instanceof Schema))
     {
    	 Schema el = (Schema) obj;
       try {
         processor.processAllSchema(el.getElement());
       } catch (WSIException e) {}
     }
   }

   /**
    *  (non-Javadoc)
    *  @see org.eclipse.wst.wsi.test.profile.validator.impl.BaseValidatorImpl.AssertionProcess#validate(org.wsi.test.profile.TestAssertion, org.wsi.test.profile.validator.EntryContext)
    */
   public AssertionResult validate(
     TestAssertion testAssertion,
     EntryContext entryContext)
     throws WSIException
   {
     result = AssertionResult.RESULT_PASSED;

     // Get the types from the entry context
     Types types =
       (Types) entryContext.getEntry().getEntryDetail();
     
     processor = new AnnotationProcessor(
           entryContext.getWSDLDocument().getDefinitions().getDocumentBaseURI());
     
     WSDLTraversal traversal = new WSDLTraversal();
     traversal.setVisitor(this);
     traversal.visitExtensibilityElement(true);
     traversal.ignoreReferences();
     traversal.traverse(types);
     
     if (errorList.isEmpty()) 
     {
        return validator.createAssertionResult(testAssertion, 
              AssertionResult.RESULT_NOT_APPLICABLE, (String) null);
     }
     
     failureDetail = this.validator.createFailureDetail(testAssertion.getFailureMessage() + 
         "\n\n" + errorList.toString(), entryContext);
     // Return assertion result
     return validator.createAssertionResult(testAssertion, result, failureDetail);
   }
   
   /**
    * Class for parse schema and search annotation elements 
    */
   class AnnotationProcessor extends XMLSchemaProcessor
   {
     public AnnotationProcessor(String context)
     {
       super(context, false);
     }

     protected void processSchema(Element element)
     {
       checkForAnnotation(element);
     }
     
     private void checkForAnnotation(Node node) 
     {
       if((node.getLocalName() != null) && (node.getLocalName().equals(ANNOTATION_KEY))) {
         errorList.add(node.toString());
       }
       for (int i = 0; i < node.getChildNodes().getLength(); i++)
       {
         checkForAnnotation(node.getChildNodes().item(i));
       }
     }
   }
 }