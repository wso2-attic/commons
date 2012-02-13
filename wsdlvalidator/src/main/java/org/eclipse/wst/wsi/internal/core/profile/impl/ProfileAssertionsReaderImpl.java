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
package org.eclipse.wst.wsi.internal.core.profile.impl;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import org.eclipse.wst.common.uriresolver.internal.provisional.URIResolver;
import org.eclipse.wst.wsi.internal.WSITestToolsEclipseProperties;
import org.eclipse.wst.wsi.internal.WSITestToolsProperties;
import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.profile.EntryTypeList;
import org.eclipse.wst.wsi.internal.core.profile.ProfileArtifact;
import org.eclipse.wst.wsi.internal.core.profile.ProfileAssertions;
import org.eclipse.wst.wsi.internal.core.profile.ProfileAssertionsReader;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.util.ArtifactType;
import org.eclipse.wst.wsi.internal.core.util.Utils;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.xml.sax.Attributes;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class ...
 * 
 * @version 1.0.1
 * @author Peter Brittenham  (peterbr@us.ibm.com)
 */
public class ProfileAssertionsReaderImpl implements ProfileAssertionsReader
{
  /**
   * Profile assertions.
   */
  protected ProfileAssertions profileAssertions;

  /**
   * ReportArtifact list.
   */
  protected TreeMap artifactList = new TreeMap();

  /**
   * Create new reader.
   */
  public ProfileAssertionsReaderImpl()
  {
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.ProfileAssertionsReader#readProfileAssertions(String)
   */
  public ProfileAssertions readProfileAssertions(String assertionsURI)
    throws WSIException
  {
    return readProfileAssertions(new InputSource(assertionsURI));
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.ProfileAssertionsReader#readProfileAssertions(Reader)
   */
  public ProfileAssertions readProfileAssertions(Reader reader)
    throws WSIException
  {
    return readProfileAssertions(new InputSource(reader));
  }

  /**
   * Read profile assertions.
   */
  private ProfileAssertions readProfileAssertions(InputSource inputSource)
    throws WSIException
  {
    // Create profile assertions
    profileAssertions = new ProfileAssertionsImpl();

    // Profile parts are put into a vector
    artifactList = new TreeMap();

    try
    {
      // Create xml reader
      XMLReader reader = XMLUtils.getXMLReader();

      // Set content handler to inner class
      reader.setContentHandler(new ProfileAssertionsHandler());
      
      if(WSITestToolsProperties.getEclipseContext())
      {
    	  EntityResolver resolver = new EntityResolver(){

			public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
				URIResolver resolver = WSITestToolsEclipseProperties.getURIResolver();
				String uri = resolver.resolve("", publicId, systemId);
				String physicalLocation = resolver.resolvePhysicalLocation("", publicId, uri);
				InputSource is = null;
				try
				{
					URL url = new URL(physicalLocation);
					is = new InputSource(uri);
					is.setByteStream(url.openStream());
				}
				catch(Exception e)
				{
					// Do nothing if opening the stream fails.
				}
				return is;
			}
    		  
    	  };
    	  reader.setEntityResolver(resolver);
      }
      // Parse profile definition file
      reader.parse(inputSource);
    }

    catch (Exception e)
    {
      throw new WSIException("Could not read and parse profile definition.", e);
    }
    finally
    {
      //Check to see if the version of test asssertion document is supported
      if (!Utils.isValidProfileTADVersion(profileAssertions))
      {
    	String tadVersion = profileAssertions.getTADVersion();
    	if(tadVersion != null)
    	{
    	  String tadName = profileAssertions.getTADName();
          throw new WSIException(
          "\nVersion "
            + tadVersion
            + " of the \""
            + tadName
            + "\"\n"
            + "document is not compatible with this version of"
            + "\n"
            + "the test tools.");
    	}
    	else
    	{
    	  throw new WSIException("WS-I validation was unable to run. Unable to read the test assertion document.");
    	}
      }
    }

    // The assertions from the TAD are reordered for purposes of processing. All
    // assertions of an artifact are reordered such that prerequisites occur
    // before the assertions they prereq. The assumption is that  prerequisites do
    // not cross artifact types.
    //
    Iterator it = artifactList.keySet().iterator();
    // for each artifact type do reordering assertion 
    while (it.hasNext())
    {
      ProfileArtifact art = (ProfileArtifact) artifactList.get(it.next());
      // result list of the reordering assertions 
      LinkedList res = new LinkedList();
      Iterator it2 = art.getTestAssertionList().iterator();
      // sort the artifact assertions into result list 
      while (it2.hasNext())
      {
        // call for each assertion , 
        // if the assertion has the prereq, the prereq will added first into res list
        sortTestAssertions(art, (TestAssertion) it2.next(), res);
      }
      // replace the original assertions list to the prereq reordering assertions list
      art.getTestAssertionList().clear();
      art.getTestAssertionList().addAll(res);
    }
    // Put the profile parts into the profile definition
    profileAssertions.setArtifactList(artifactList);

    return profileAssertions;
  }
  
  /* (non-Javadoc)
   * @see org.wsi.test.document.DocumentReader#getLocation()
   */
  public String getLocation()
  {
    return null;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.document.DocumentReader#setLocation(String)
   */
  public void setLocation(String documentURI)
  {
  }

  /**
   * Recursive walk to order assertions so that assertions are 
   * processed following the ones that prereq them.
   * If assertion has prereq, then add it to the result list first.
   *  
   * @param art profile artifact for call @link ProfileArtifact#getTestAssertion(String)
   * @param asrt test assertion
   * @param list result list to sort
   */
  private void sortTestAssertions(ProfileArtifact art, TestAssertion asrt, List list)
  {
    // if assertion is null or does not exist, then exit
    if((asrt == null) || list.contains(asrt)) 
      return;
    
    // if it has not prereq, add the asertion and exit
    if(asrt.getPrereqIdList().isEmpty()) 
    {
      list.add(asrt);
      return;
    }
    // get list prereq assertion and reqursive call sortTestAssertions this each    
    Iterator it = asrt.getPrereqIdList().iterator();
    while(it.hasNext()) {
      String id = (String) it.next();
      sortTestAssertions(art, art.getTestAssertion(id), list);
    }
    list.add(asrt);
  }

  /**
   * Inner class: ProfileAssertionsHandler
   */
  class ProfileAssertionsHandler extends DefaultHandler
  {
    private ProfileArtifact artifact = profileAssertions.createArtifact();
    private StringBuffer context = null;
    private StringBuffer assertionDescription = null;
    private StringBuffer failureMessage = null;
    private StringBuffer failureDetailDescription = null;
    private StringBuffer detailDescription = null;
    private StringBuffer testAssertionId = null;
    private String currentElement = null;
    private TestAssertion testAssertion = null;
    private StringBuffer logInput = null;
    private StringBuffer wsdlInput = null;
    private EntryTypeList entryTypeList = null;

    // Process start element event
    public void startElement(
      String namespaceURI,
      String localName,
      String qName,
      Attributes atts)
    {
      if (namespaceURI != null
        && (namespaceURI.equals(WSIConstants.NS_URI_WSI_ASSERTIONS_2003)
        ||  namespaceURI.equals(WSIConstants.NS_URI_WSI_ASSERTIONS)))
      {
        // Save element name
        currentElement = localName;

        // <artifact>
        if (localName.equals(WSIConstants.ELEM_ARTIFACT))
        {
          // Create a new profile artifact object
          artifact = profileAssertions.createArtifact();

          // Set type
          artifact.setType(
            ArtifactType.getArtifactType(
              atts.getValue(WSIConstants.ATTR_TYPE)));

          // ADD: Get specification list

          // Add artifact to list of artifact list
          artifactList.put(artifact.getType().getTypeName(), artifact);
        }

        // <testAssertion>
        else if (localName.equals(WSIConstants.ELEM_TEST_ASSERTION))
        {
          // Create test assertion
          testAssertion = new TestAssertionImpl();

          // Get attribute values
          String id = atts.getValue("", WSIConstants.ATTR_ID);
          testAssertion.setId(id);
          testAssertion.setType(atts.getValue("", WSIConstants.ATTR_TYPE));
          testAssertion.setEntryTypeName(
            atts.getValue("", WSIConstants.ATTR_ENTRY_TYPE));
          
          // if assertion is not required or it has already been checked by core validator then disable it
          if (alreadyChecked(id) || !(testAssertion.getType().equals(TestAssertion.TYPE_REQUIRED)))
        	  testAssertion.setEnabled(false);
          else
        	  testAssertion.setEnabled(Boolean.valueOf(atts.getValue("", WSIConstants.ATTR_ENABLED)).booleanValue());

          // Add test assertion to artifact
          artifact.addTestAssertion(testAssertion);
        }

        // <context>
        else if (localName.equals(WSIConstants.ELEM_CONTEXT))
        {
          context = new StringBuffer();
        }

        // <assertionDescription>
        else if (localName.equals(WSIConstants.ELEM_ASSERTION_DESCRIPTION))
        {
          assertionDescription = new StringBuffer();
        }

        // <failureMessage>
        else if (localName.equals(WSIConstants.ELEM_FAILURE_MESSAGE))
        {
          failureMessage = new StringBuffer();
        }

        // <failureDetailDescription>
        else if (localName.equals(WSIConstants.ELEM_FAILURE_DETAIL_DESCRIPTION))
        {
          failureDetailDescription = new StringBuffer();
        }

        // <detailDescription>
        else if (localName.equals(WSIConstants.ELEM_DETAIL_DESCRIPTION))
        {
          detailDescription = new StringBuffer();
        }

        // <testAssertionID>
        else if (localName.equals(WSIConstants.ELEM_TEST_ASSERTION_ID))
        {
          testAssertionId = new StringBuffer();
        }

        // <additionalEntryTypeList>
        else if (
          localName.equals(WSIConstants.ELEM_ADDITIONAL_ENTRY_TYPE_LIST))
        {
          // Create entry type list
          entryTypeList = new EntryTypeListImpl();
        }

        // <messageInput>
        else if (localName.equals(WSIConstants.ELEM_MESSAGE_INPUT))
        {
          logInput = new StringBuffer();
        }

        // <wsdlInput>
        else if (localName.equals(WSIConstants.ELEM_WSDL_INPUT))
        {
          wsdlInput = new StringBuffer();
        }
        // <profileAssertions>
        else if (localName.equals(WSIConstants.ELEM_PROFILE_ASSERTIONS))
        {
          profileAssertions.setTADName(
            atts.getValue("", WSIConstants.ATTR_NAME));
          profileAssertions.setTADVersion(
            atts.getValue("", WSIConstants.ATTR_VERSION));
        }

        /* REMOVE:     
        // <uddiInput>
        else if (localName.equals(WSIConstants.ELEM_UDDI_INPUT)) {
          uddiInput = new StringBuffer();
        } 
        */
      }
    } // END startElement

    public void endElement(String namespaceURI, String localName, String qname)
    {
      // If this is the profile namespace, then check for and process selected elements
      if (namespaceURI != null
        && (namespaceURI.equals(WSIConstants.NS_URI_WSI_ASSERTIONS_2003)
        || namespaceURI.equals(WSIConstants.NS_URI_WSI_ASSERTIONS)))
      {
        // <context>
        if (localName.equals(WSIConstants.ELEM_CONTEXT))
        {
          // Set context in test assertion
          testAssertion.setContext(context.toString());
          context = null;
        }

        // <assertionDescription>
        else if (localName.equals(WSIConstants.ELEM_ASSERTION_DESCRIPTION))
        {
          // Set assertion description in test assertion
          testAssertion.setAssertionDescription(
            assertionDescription.toString());
          assertionDescription = null;
        }

        // <failureMessage>
        else if (localName.equals(WSIConstants.ELEM_FAILURE_MESSAGE))
        {
          // Set failure message in test assertion
          testAssertion.setFailureMessage(failureMessage.toString());
          failureMessage = null;
        }

        // <failureDetailDescription>
        else if (localName.equals(WSIConstants.ELEM_FAILURE_DETAIL_DESCRIPTION))
        {
          // Set failure detail description in test assertion
          testAssertion.setFailureDetailDescription(
            failureDetailDescription.toString());
          failureDetailDescription = null;
        }

        // <detailDescription>
        else if (localName.equals(WSIConstants.ELEM_DETAIL_DESCRIPTION))
        {
          // Set detail description in test assertion
          testAssertion.setDetailDescription(detailDescription.toString());
          detailDescription = null;
        }

        // <testAssertionID>
        else if (localName.equals(WSIConstants.ELEM_TEST_ASSERTION_ID))
        {
          // Set test assertion id in prereq list
          testAssertion.addPrereqId(testAssertionId.toString());
          testAssertionId = null;
        }

        // <addtionalEntryTypeList>
        else if (
          localName.equals(WSIConstants.ELEM_ADDITIONAL_ENTRY_TYPE_LIST))
        {
          // Set target list
          testAssertion.setAdditionalEntryTypeList(entryTypeList);
          entryTypeList = null;
        }

        // <messageInput>
        else if (localName.equals(WSIConstants.ELEM_MESSAGE_INPUT))
        {
          // Set test assertion id in prereq list
          entryTypeList.setLogInput(logInput.toString());
          logInput = null;
        }

        // <wsdlInput>
        else if (localName.equals(WSIConstants.ELEM_WSDL_INPUT))
        {
          // Set test assertion id in prereq list
          entryTypeList.setWSDLInput(wsdlInput.toString());
          wsdlInput = null;
        }

        // <profileAssertions>
        else if (localName.equals(WSIConstants.ELEM_PROFILE_ASSERTIONS))
        {
          // a no-op
        }

        /* REMOVE:
        // <uddiInput>
        else if (localName.equals(WSIConstants.ELEM_UDDI_INPUT)) {
          // Set test assertion id in prereq list
           entryTypeList.setUDDIInput(uddiInput.toString());         
          uddiInput = null;
        }
        */
      }
    } // END endElement

    public void characters(char[] ch, int start, int length)
    {
      // <context>
      if ((context != null)
        && (currentElement.equals(WSIConstants.ELEM_CONTEXT)))
      {
        context.append(ch, start, length);
      }

      // <assertionDescription>
      else if (
        (assertionDescription != null)
          && (currentElement.equals(WSIConstants.ELEM_ASSERTION_DESCRIPTION)))
      {
        assertionDescription.append(ch, start, length);
      }

      // <failureMessage>
      else if (
        (failureMessage != null)
          && (currentElement.equals(WSIConstants.ELEM_FAILURE_MESSAGE)))
      {
        failureMessage.append(ch, start, length);
      }

      // <failureDetailDescription>
      else if (
        (failureDetailDescription != null) && (
          currentElement.equals(WSIConstants.ELEM_FAILURE_DETAIL_DESCRIPTION)))
      {
        failureDetailDescription.append(ch, start, length);
      }

      // <detailDescription>
      else if (
        (detailDescription != null)
          && (currentElement.equals(WSIConstants.ELEM_DETAIL_DESCRIPTION)))
      {
        detailDescription.append(ch, start, length);
      }

      // <testAssertionID>
      else if (
        (testAssertionId != null)
          && (currentElement.equals(WSIConstants.ELEM_TEST_ASSERTION_ID)))
      {
        testAssertionId.append(ch, start, length);
      }

      // <messageInput>
      else if (
        (logInput != null)
          && (currentElement.equals(WSIConstants.ELEM_MESSAGE_INPUT)))
      {
        logInput.append(ch, start, length);
      }

      // <wsdlInput>
      else if (
        (wsdlInput != null)
          && (currentElement.equals(WSIConstants.ELEM_WSDL_INPUT)))
      {
        wsdlInput.append(ch, start, length);
      }

      /* REMOVE:
      // <uddiInput>
      else if ((uddiInput != null) && (currentElement.equals(WSIConstants.ELEM_UDDI_INPUT))) {
        uddiInput.append(ch, start, length);
      }
      */
    }
  }

  /**
   * NOTE that this api assumes that base wsdl validator has already validated
   * the wsdl document and corresponding schemas against the wsdl 1.1 specification.
   * Note also that there are a number of duplicate checks between the base validator
   * and the ws-i validator. Since some of these checks are cpu intensive, we disable 
   * those assertions here. 
   *
   * Returns true if the assertion has already been checked by the base validator. 
   * @param testAssertion
   * @return true if the assertion has already been checked by the base validator. 
   */
  protected boolean alreadyChecked(String id)
  {
	 boolean result = false;
	 if (id != null)
	 {
       result = (id.equals("BP2110") ||
    		   	 id.equals("BP2115") ||
		         id.equals("BP2417") ||
		         id.equals("BP2114") ||
		         id.equals("BP2123") ||
		         id.equals("SSBP2209") ||
		         id.equals("AP2209") ||
		         id.equals("BP2700") ||
		         id.equals("BP2011")
		         );
	 }
	 return result;
}


}
