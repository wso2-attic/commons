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
package org.eclipse.wst.wsi.internal.core.log;

import java.util.Collection;

/**
 * Mime parts interface.
 *
 * @version 1.0.1
 * @author David Lauzon (lauzond@ca.ibm.com)
 */
public interface MimeParts
{
  /**
   * Returns the start attribute value from the .
   * @return the he start attribute value.
   * @see #setRootPart
   */
  /**
   * Returns the root part, otherwise null.
   * @return the root part.
   * @see #setRootPart
   */
  public MimePart getRootPart();

  /**
   * Sets the root part.
   * @param rootPart the root part.
   * @see #getRootPart
   */
  public void setRootPart(MimePart rootPart);

  /**
   * Returns the number of parts.
   * @return the number of parts.
   */
  public int count();

  /**
   * Gets the conversation id.
   * @return the conversation id.
   * @see #setConversationId
   */
  public void addPart(MimePart part);

  /**
   * Returns a list of mime parts.
   * @return a list of mime parts.
   */
  public Collection getParts();
}
