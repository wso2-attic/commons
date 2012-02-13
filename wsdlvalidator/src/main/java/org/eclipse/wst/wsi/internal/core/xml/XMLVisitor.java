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
package org.eclipse.wst.wsi.internal.core.xml;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Entity;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Notation;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

/**
 * We don't have access to the node source code, so this does
 * our double-dispatch the hard way.
 */
public class XMLVisitor
{
  /**
  * Visit XML CDATASection node.
  * @param s  an XML CDATASection node.
  */
  public void visit(CDATASection s)
  {
  }

  /**
  * Visit XML Document node.
  * @param e  an XML Document node.
  */
  public void visit(Document e)
  {
  }

  /**
  * Visit XML DocumentType node.
  * @param e  an ML DocumentType node.
  */
  public void visit(DocumentType e)
  {
  }

  /**
  * Visit XML Attribute node.
  * @param e  an XML Atrribute node.
  */
  public void visit(Attr e)
  {
  }

  /**
  * Visit XML Element node.
  * @param e  an XML Element node.
  */
  public void visit(Element e)
  {
  }

  /**
  * Visit XML Comment node.
  * @param e  an XML Comment node.
  */
  public void visit(Comment e)
  {
  }

  /**
   * Visit XML Text node.
  * @param e  an XML Text node.
  */
  public void visit(Text e)
  {
  }

  /**
   * Visit XML EntityReference node. 
  * @param e  an XML EntityReference node.
  */
  public void visit(EntityReference e)
  {
  }

  /**
   * Visit XML Entity node. 
  * @param e  an XML Entity node.
  */
  public void visit(Entity e)
  {
  }

  /**
   * Visit XML Notation node. 
  * @param n  an XML Notation node.
  */
  public void visit(Notation n)
  {
  }

  /**
   * Visit XML ProcessingInstruction node. 
  * @param i  an XML ProcessingInstruction node.
  */
  public void visit(ProcessingInstruction i)
  {
  }

  /**
   * Visit XML NamedNodeMap node. 
  * @param i  an XML NamedNodeMap node.
  */
  public void visit(NamedNodeMap i)
  {
  }

  /**
   * Visit XML node. 
  * @param node  an XML node.
  */
  public void doVisit(Object node)
  {
    if (node instanceof Element)
    {
      visit((Element) node);
    }
    else if (node instanceof Comment)
    {
      visit((Comment) node);
    }
    else if (node instanceof CDATASection)
    {
      // CDATASection extends Text, so it must come first
      visit((CDATASection) node);
    }
    else if (node instanceof Text)
    {
      visit((Text) node);
    }
    else if (node instanceof Attr)
    {
      visit((Attr) node);
    }
    else if (node instanceof Document)
    {
      visit((Document) node);
    }
    else if (node instanceof EntityReference)
    {
      visit((EntityReference) node);
    }
    else if (node instanceof Entity)
    {
      visit((Entity) node);
    }
    else if (node instanceof DocumentType)
    {
      visit((DocumentType) node);
    }
    else if (node instanceof Notation)
    {
      visit((Notation) node);
    }
    else if (node instanceof ProcessingInstruction)
    {
      visit((ProcessingInstruction) node);
    }
    else if (node instanceof NamedNodeMap)
    {
      visit((NamedNodeMap) node);
    }
  }
}

// END OF FILE
