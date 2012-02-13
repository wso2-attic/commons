/*******************************************************************************
 *
 * Copyright (c) 2002-2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.core.xml;

import java.io.FileOutputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * This class converts a XML document using an XSL file.
 * 
 * @version 1.0.1
 * @author Peter Brittenham  (peterbr@us.ibm.com)
 */
public class XMLConvertor
{

  /**
   * Transforms an XML document using an XSL file.
   * @param args  arguments for main.
   */
  public static void main(String[] args)
  {
    try
    {
      if (args.length < 3)
      {
        System.out.println(
          "Usage: XMLConvertor <xslFile> <inXMLFile> <outFile>");
      }

      else
      {
        // Get transformer 
        Transformer transformer =
          TransformerFactory.newInstance().newTransformer(
            new StreamSource(args[0]));

        // Transform the XML document using the specificed XSL file
        transformer.transform(
          new StreamSource(args[1]),
          new StreamResult(new FileOutputStream(args[2])));

        // Display results
        System.out.println(
          "Created " + args[2] + " from " + args[0] + " and " + args[1] + ".");
      }
    }

    catch (Exception e)
    {
      e.printStackTrace();
    }

    System.exit(0);
  }
}
