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
import org.w3c.dom.Node;
import org.w3c.dom.Notation;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

/**
 * We don't have access to the node source code, so this does
 * our double-dispatch the hard way.
 */
public abstract class XMLTraversal extends XMLVisitor
{
  /**
     * Returning false from action would terminates traversal.
     * However, this always returns true. -- a no-op.
     * @param n - an XML node.
     * @return always true.
     */
  public boolean action(Node n)
  {
    return true;
  }

  public void visit(CDATASection s)
  {
    action(s);
  }

  public void visit(Document e)
  {
    if (action(e))
    {
      // This does visit DocumentType, which is considered a child Node.
      for (Node n = e.getFirstChild(); n != null; n = n.getNextSibling())
      {
        doVisit(n);
      }
    }
  }

  public void visit(DocumentType type)
  {
    if (action(type))
    {
      visit(type.getEntities());
      visit(type.getNotations());
    }
  }

  public void visit(Attr e)
  {
    action(e);
  }

  public void visit(Element node)
  {
    if (action(node))
    {
      visit(node.getAttributes());
      for (Node n = node.getFirstChild(); n != null; n = n.getNextSibling())
      {
        doVisit(n);
      }
    }
  }

  public void visit(Comment e)
  {
    action(e);
  }

  public void visit(Text e)
  {
    action(e);
  }

  public void visit(EntityReference e)
  {
    action(e);
  }

  public void visit(Entity e)
  {
    action(e);
  }

  public void visit(Notation n)
  {
    action(n);
  }

  public void visit(ProcessingInstruction i)
  {
    action(i);
  }

  public void visit(NamedNodeMap map)
  {
    if (map != null)
    {
      for (int i = 0; i < map.getLength(); ++i)
      {
        doVisit(map.item(i));
      }
    }
  }
}

// END OF FILE
