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
package org.eclipse.wst.wsi.internal.core.log.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.wst.wsi.internal.core.log.MimePart;
import org.eclipse.wst.wsi.internal.core.log.MimeParts;

/**
 * MimePartsImpl class.
 *
 * @version 1.0.1
 * @author David Lauzon (lauzond@ca.ibm.com)
 */
public class MimePartsImpl implements MimeParts
{
	
  protected MimePart rootPart;
  protected Collection mimeParts = new ArrayList();

  public MimePartsImpl()
  {
  }

  /* (non-Javadoc)
   * @see org.wsi.test.log.MimeParts#getRootPart()
   */
  public MimePart getRootPart()
  {
	return rootPart;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.log.MimeParts#setRootPart(MimePart)
   */
  public void setRootPart(MimePart rootPart)
  {
  	this.rootPart = rootPart;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.log.MimeParts#count()
   */
  public int count()
  {
	return mimeParts.size();
  }

  /* (non-Javadoc)
   * @see org.wsi.test.log.MimeParts#addPart(MimePart)
   */
  public void addPart(MimePart part)
  {
  	if (part != null)
  	  mimeParts.add(part);
  }

  /* (non-Javadoc)
   * @see org.wsi.test.log.MimeParts#getParts()
   */
  public Collection getParts()
  {
  	return mimeParts;
  }
}
