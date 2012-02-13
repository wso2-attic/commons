/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.validation.internal.eclipse;

import org.apache.xerces.util.XMLGrammarPoolImpl;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;

/**
 * A grammar pool for inline schemas. This grammar pool restricts inline schemas
 * from being cached.
 */
public class InlineSchemaModelGrammarPoolImpl extends XMLGrammarPoolImpl 
{
  public void putGrammar(Grammar grammar) 
  {
	if (grammar == null)
	  return;
	// we overide this method to perform 'selective' caching of schemas
	XMLGrammarDescription description = grammar.getGrammarDescription();
	if (!fPoolIsLocked && !containsGrammar(grammar.getGrammarDescription())) 
	{
	  // in order to avoid caching the inline schemas
	  // we ensure the literal system id does not end with wsdl
	  // before we attempt to 'put' the grammar
	  String litSysId = description.getLiteralSystemId();
	  String basSysId = description.getBaseSystemId();
	  if (litSysId != null && litSysId.endsWith("xsd")
			&& basSysId != null && basSysId.endsWith("wsdl")) 
	  {
				/*
				 * System.out.println("putGramamr : " +
				 * schemaDescription.getNamespace() + ", " +
				 * schemaDescription.getExpandedSystemId() + ", " +
				 * schemaDescription.getBaseSystemId());
				 */
	    super.putGrammar(grammar);
	  }
	}
  }
}