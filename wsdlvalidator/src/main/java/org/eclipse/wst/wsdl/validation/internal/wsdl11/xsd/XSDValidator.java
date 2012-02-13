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

package org.eclipse.wst.wsdl.validation.internal.wsdl11.xsd;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.xerces.impl.Constants;
import org.apache.xerces.impl.xs.XSDDescription;
import org.apache.xerces.parsers.XMLGrammarPreparser;
import org.apache.xerces.util.XMLGrammarPoolImpl;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.grammars.XSGrammar;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSModel;


/**
 * Validate schemas from files or String.
 */
public class XSDValidator
{
  protected final String HONOUR_ALL_SCHEMA_LOCATIONS_FEATURE_ID = "http://apache.org/xml/features/honour-all-schemaLocations";
  protected final String FILE_PREFIX = "file:";
  protected final String XMLNS = "xmlns";
  protected final String TARGETNAMESPACE = "targetNamespace";
  protected final String NAMESPACE = "namespace";
  protected final String IMPORT = "import";
  protected final String SCHEMALOCATION = "schemaLocation";
  protected final String TYPE = "type";
  protected final String[] ignoreNamespaces =
    { "http://www.w3.org/2001/XMLSchema", "http://www.w3.org/1999/XMLSchema" };

  protected XSModel xsModel;
  protected boolean isValidXSD;
  protected List errors;
  protected String filelocation;

  /**
   * Constructor.
   */
  public XSDValidator()
  {
    xsModel = null;
    isValidXSD = false;
    errors = null;
  }
  /**
   * Validate an inline schema.
   * 
   * @param schema A schema represented as a string.
   * @param targetNamespace The target namespace of the schema.
   * @param fileloc The uri of the file that contains the schema.
   */
  public void validateInlineSchema(String schema, String targetNamespace, String fileloc)
  {
    validateInlineSchema(schema, targetNamespace, fileloc, null, null, null);
  }
  
  /**
   * Validate an inline schema and specify an entity resolver.
   * 
   * @param schema This schema represented as a string.
   * @param targetNamespace The target namespace of the schema.
   * @param fileloc The uri of the file that contains this schema.
   * @param entityResolverChain The entity resolver chain.
   * @param inlineSchemaEntityResolver An inline schema resolver for this schema.
   * @param grammarPool A Xerces XMLGrammarPool that holds precached schemas.
   */
  public void validateInlineSchema(
    String schema,
    String targetNamespace,
    String fileloc,
    XMLEntityResolver entityResolverChain,
    XMLEntityResolver inlineSchemaEntityResolver,
    XMLGrammarPool grammarPool)
  {
    filelocation = fileloc;
    
    validateXSD(schema, true, entityResolverChain,targetNamespace, inlineSchemaEntityResolver, grammarPool);
  }

  /**
   * Validate the file located at the uri specified with the given entity resolver.
   * 
   * @param uri An absolute uri for the schema location.
   * @param entityResolver An entity resolver to be used.
   * @param grammarPool A Xerces XMLGrammarPool that holds precached schemas.
   */
  public void validate(String uri, XMLEntityResolver entityResolver, XMLGrammarPool grammarPool)
  {
    validateXSD(uri, false, entityResolver, null, null, grammarPool);
  }
  
  /**
   * Validate the schema.
   * 
   * @param schema The schema or it's location.
   * @param inlineXSD True if an inline schema, false otherwise.
   * @param entityResolver An entity resolver to use.
   * @param targetNamespace The target namespace of the schema being validated.
   * @param inlineSchemaEntityResolver An inline schema entity resolver.
   * @param grammarPool A Xerces XMLGrammarPool that holds precached schemas.
   */
  protected void validateXSD(String schema, boolean inlineXSD, XMLEntityResolver entityResolver, String targetNamespace, XMLEntityResolver inlineSchemaEntityResolver, XMLGrammarPool grammarPool)
  {
	ValidateErrorHandler errorHandler = new ValidateErrorHandler();
	//errorHandler.getErrorMessages().clear();
	try
	{
	  XMLGrammarPreparser grammarPreparser = new XMLGrammarPreparser();
	  grammarPreparser.registerPreparser(XMLGrammarDescription.XML_SCHEMA,null/*schemaLoader*/);
		
	  grammarPreparser.setFeature(Constants.XERCES_FEATURE_PREFIX + Constants.CONTINUE_AFTER_FATAL_ERROR_FEATURE, false);
      grammarPreparser.setFeature(Constants.XERCES_FEATURE_PREFIX + Constants.NAMESPACES_FEATURE, true);
      grammarPreparser.setFeature(Constants.XERCES_FEATURE_PREFIX + Constants.NAMESPACE_PREFIXES_FEATURE, true);
	  grammarPreparser.setFeature(Constants.XERCES_FEATURE_PREFIX + Constants.VALIDATION_FEATURE, true);
	  grammarPreparser.setFeature(Constants.XERCES_FEATURE_PREFIX + Constants.SCHEMA_VALIDATION_FEATURE, true);
      grammarPreparser.setFeature(Constants.XERCES_FEATURE_PREFIX + Constants.SCHEMA_FULL_CHECKING, false);
	  grammarPreparser.setFeature(Constants.XERCES_FEATURE_PREFIX + Constants.EXTERNAL_GENERAL_ENTITIES_FEATURE, true);
	  grammarPreparser.setFeature(Constants.XERCES_FEATURE_PREFIX + Constants.EXTERNAL_PARAMETER_ENTITIES_FEATURE, true);
	  grammarPreparser.setFeature(Constants.XERCES_FEATURE_PREFIX + Constants.WARN_ON_DUPLICATE_ATTDEF_FEATURE, true);
	      
	  try
	  {
		grammarPreparser.setFeature(HONOUR_ALL_SCHEMA_LOCATIONS_FEATURE_ID, true);
	  }
	  catch (Exception e)
	  {
	    // Catch the exception and ignore if we can't set this feature.
	  }

	  // cs : toggle the comments on these 2 lines to easily disable caching
      // grammarPreparser.setGrammarPool(new XMLGrammarPoolImpl()); 		
	  grammarPreparser.setGrammarPool(grammarPool != null ? grammarPool : new XMLGrammarPoolImpl()); 
	  grammarPreparser.setErrorHandler(errorHandler);
	  if (entityResolver != null)
	  {
	    grammarPreparser.setEntityResolver(entityResolver);
	  }

	  try
	  {
	    XMLInputSource is = null;
	    List oldGrammars = null;
	    XMLGrammarPoolImpl pool = null;
	    // This allows support for the inline schema in WSDL documents.
	    if (inlineXSD)
	    {       
	      Reader reader = new StringReader(schema);
		  is = new XMLInputSource(null,filelocation,filelocation,reader,null);
				
		  ((InlineXSDResolver)inlineSchemaEntityResolver).addReferringSchema(is,targetNamespace);
		  // In the case that 'shared' grammar pool is used we need to consider
		  // that we might have already added a schema to the pool with the same target namespace
		  // as the inline schema we're hoping to construct.  In this case we need to remove
		  // the schema from the pool before constructing our inline schema.  We can add it 
		  // back when we're all done.
		  pool = (XMLGrammarPoolImpl)grammarPreparser.getGrammarPool();        	      
		  oldGrammars = new ArrayList();
		  // Remove the inline schema namespace if it is listed directly
		  // in the pool. If it is indirectly listed as an import of a grammar listed
		  // directly in the pool hide the grammar to continue to get caching benefits
		  // from the cached grammar.
		  Grammar[] grammars = pool.retrieveInitialGrammarSet(XMLGrammarDescription.XML_SCHEMA);
		  int numGrammars = grammars.length;
		  for(int i = 0; i < numGrammars; i++)
		  {
		    XMLGrammarDescription desc = grammars[i].getGrammarDescription();
		    if(targetNamespace.equals(desc.getNamespace()))
		    {
		      oldGrammars.add(pool.removeGrammar(desc));
		    }
		    else
		    {
		      if(grammars[i] instanceof XSGrammar)
		      {
		    	XSGrammar grammar = (XSGrammar)grammars[i];
		    	  	  
		    	StringList namespaces = grammar.toXSModel().getNamespaces();
		    	if(namespaces.contains(targetNamespace))
		    	{
		    	  oldGrammars.add(pool.removeGrammar(desc));
		    	  	    //pool.putGrammar(new XSGrammarHider(grammar, targetNamespace));
		    	}
		      }
		    }
		  }
		    	
		  Set inlineNSs = ((InlineXSDResolver)inlineSchemaEntityResolver).getInlineSchemaNSs();
		  Iterator nsiter = inlineNSs.iterator();
		  while(nsiter.hasNext())
		  {
		    XSDDescription desc = new XSDDescription();
		    desc.setNamespace((String)nsiter.next());      
		    Grammar oldGrammar = pool.removeGrammar(desc);
		    if(oldGrammar != null)
		      oldGrammars.add(oldGrammar);
		  }

	    }
	    // get the input source for an external schema file
	    else
	    {
	      is = new XMLInputSource(null,schema,schema);
	    }

	    XSGrammar grammar = (XSGrammar)grammarPreparser.preparseGrammar(XMLGrammarDescription.XML_SCHEMA,is);
	    xsModel = grammar.toXSModel();
			
	    // Here we add the temporiliy removed schema back.
		if (inlineXSD && oldGrammars != null)
		{
		  XSDDescription description = new XSDDescription();
		  description.setNamespace(targetNamespace); 
		  pool.removeGrammar(description);
				
		  Set inlineNSs = ((InlineXSDResolver)inlineSchemaEntityResolver).getInlineSchemaNSs();
		  Iterator nsiter = inlineNSs.iterator();
		  while(nsiter.hasNext())
		  {
			XSDDescription desc = new XSDDescription();
		    desc.setNamespace((String)nsiter.next());   
		    pool.removeGrammar(desc);
		  }
		  
		  Iterator oldGIter = oldGrammars.iterator();
	      while(oldGIter.hasNext())
		  {
			 Grammar oldGrammar = (Grammar)oldGIter.next();
			 if(oldGrammar != null)
			   pool.putGrammar(oldGrammar);
		  }
		}  
	  }
	  catch (Exception e)
	  {
	        // Parser will return null pointer exception if the document is structurally invalid.
	      	// In this case we simply ignore the error.
	      	//System.out.println(e);
	  }
	  errors = errorHandler.getErrorMessages();
	}
	catch (Exception e)
	{
	  System.out.println(e);
	}
	if (errors.isEmpty())
	{
	  isValidXSD = true;
	}
  }

  /**
   * Returns the XSModel created.
   * 
   * @return The XSModel created.
   */

  public XSModel getXSModel()
  {
  	return xsModel;
  }
  /**
   * Returns true if the schema is valid, false otherwise.
   * 
   * @return True if the schema is valid, false otherwise.
   */
  public boolean isValid()
  {
    return isValidXSD;
  }

  /**
   * Return the error list.
   * 
   * @return A list of error from the schema.
   */
  public List getErrors()
  {
    return errors;
  }
}
