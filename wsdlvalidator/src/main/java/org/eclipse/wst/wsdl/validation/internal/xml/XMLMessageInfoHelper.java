/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.validation.internal.xml;

public class XMLMessageInfoHelper
{
  
  //see org.eclipse.wst.xml.ui.reconcile.DelegatingSourceValidator
  protected static final String ALL_ATTRIBUTES = "ALL_ATTRIBUTES";
  protected static final String ATTRIBUTE_NAME ="ATTRIBUTE_NAME";
  protected static final String ATTRIBUTE_VALUE = "ATTRIBUTE_VALUE";
  protected static final String START_TAG = "START_TAG";
  protected static final String TEXT = "TEXT";
  protected static final String FIRST_NON_WHITESPACE_TEXT = "FIRST_NON_WHITESPACE_TEXT";
  protected static final String TEXT_ENTITY_REFERENCE = "TEXT_ENTITY_REFERENCE"; 
  protected static final String VALUE_OF_ATTRIBUTE_WITH_GIVEN_VALUE = "VALUE_OF_ATTRIBUTE_WITH_GIVEN_VALUE";
  
  
  public XMLMessageInfoHelper()
  {
    super();
  }

  /**
   *  returns an array containing information about what should be underlined with the red "squiggles"
   *  using the errorKey, and the messageArguments
   *  <br>Position 0 of the array returned contains the selection Strategy,or what DOM Element to underline.  
   *  For example "ATTRIBUTE_NAME"  
   *  <br>Position 1 contains the name or value to squiggle.
   *  <p>For example, if we wanted to squiggle the attribute name of an attribute name
   *  foo this method would return {"ATTRIBUTE_NAME", "foo"}
   *  </p>
   *  @param errorKey the error key given by the Xerces parser
   *  @param messageArguments the arguments used by Xerces to "fill in the blanks" of their messages
   *  @return an array containing the squiggle information
   *  @see org.eclipse.wst.xml.ui.reconcile.DelegatingReconcileValidator
   *  
   */
  public String[] createMessageInfo(String errorKey, Object[] messageArguments)
  { 
    String selectionStrategy = null;
    String nameOrValue = null;

    //XML Errors
    if (errorKey.equals("cvc-complex-type.2.4.a") || errorKey.equals("cvc-complex-type.2.4.d") || errorKey.equals("cvc-complex-type.2.4.b") || errorKey.equals("MSG_CONTENT_INVALID")
        | errorKey.equals("MSG_CONTENT_INCOMPLETE") || errorKey.equals("MSG_REQUIRED_ATTRIBUTE_NOT_SPECIFIED") || errorKey.equals("cvc-complex-type.4"))
    {
      selectionStrategy = START_TAG;
    }
    else if (errorKey.equals("cvc-type.3.1.3"))
    {
      selectionStrategy = TEXT;
    }
    else if (errorKey.equals("cvc-complex-type.2.3"))
    {
    	selectionStrategy = FIRST_NON_WHITESPACE_TEXT;
    }
    else if (errorKey.equals("cvc-type.3.1.1"))
    {
      selectionStrategy = ALL_ATTRIBUTES;
    }
    else if (errorKey.equals("cvc-complex-type.3.2.2") || errorKey.equals("MSG_ATTRIBUTE_NOT_DECLARED"))
    {
      selectionStrategy = ATTRIBUTE_NAME;
	  //in this case we need nameOrValue to be the name of the attribute to underline
	  nameOrValue = (String)messageArguments[1];
    }
    else if (errorKey.equals("cvc-attribute.3") || errorKey.equals("MSG_ATTRIBUTE_VALUE_NOT_IN_LIST"))
    {	
    	selectionStrategy = ATTRIBUTE_VALUE;  
		//in this case we need nameOrValue to be the name of the Attribute
    	if (errorKey.equals("cvc-attribute.3"))
    	{ nameOrValue = (String)messageArguments[1];
    	}
    	else if (errorKey.equals("MSG_ATTRIBUTE_VALUE_NOT_IN_LIST"))
    	{	nameOrValue = (String)messageArguments[0];
    	}
    }
    else if (errorKey.equals("cvc-elt.4.2"))
    {	selectionStrategy=VALUE_OF_ATTRIBUTE_WITH_GIVEN_VALUE;
		//in this case we need nameOrValue to be the value of the attribute we want to unerline
    	nameOrValue = (String)messageArguments[1];	
    }
    else if (errorKey.equals("EntityNotDeclared"))
    {  	selectionStrategy=TEXT_ENTITY_REFERENCE;
    }
    
    
    //WSDL Errors
    else if (errorKey.equals("_MESSAGE_UNDEFINED_FOR_OUTPUT") ||
               errorKey.equals("_MESSAGE_UNDEFINED_FOR_INPUT") ||
               errorKey.equals("_MESSAGE_UNDEFINED_FOR_FAULT"))
    {
      selectionStrategy=ATTRIBUTE_VALUE;
      nameOrValue= "message";
    }
    else if (errorKey.equals("_PORTTYPE_UNDEFINED_FOR_BINDING") ||
               errorKey.equals("_NO_PORTTYPE_DEFINED_FOR_BINDING"))
    {
      selectionStrategy=ATTRIBUTE_VALUE;
      nameOrValue="type";
    }
    else if (errorKey.equals("_OPERATION_UNDEFINED_FOR_PORTTYPE"))
    {
      selectionStrategy=ATTRIBUTE_VALUE;
      nameOrValue="name";
    }
    else if (errorKey.equals("_PART_INVALID_ELEMENT"))
    {
      selectionStrategy=ATTRIBUTE_VALUE;
      nameOrValue="element";
    }
    else if (errorKey.equals("_NO_BINDING_FOR_PORT"))
    {
      selectionStrategy=ATTRIBUTE_VALUE;
      nameOrValue="binding";
    }
    
    String messageInfo[] = new String[2];
    messageInfo[0] = selectionStrategy;
    messageInfo[1] = nameOrValue;
    return messageInfo;
  }
}
