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
package org.eclipse.wst.wsi.internal.core.monitor.config;

import java.util.Vector;

import javax.xml.namespace.QName;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.document.DocumentElement;

/**
 * The interface for the monitor config ManInTheMiddle element.
 * 
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public interface ManInTheMiddle extends DocumentElement
{
  /**
   * Element name.
   */
  public static final String ELEM_NAME = WSIConstants.ELEM_MAN_IN_THE_MIDDLE;

  /**
   * QName.
   */
  public static final QName QNAME =
    new QName(WSIConstants.NS_URI_WSI_MONITOR_CONFIG, ELEM_NAME);

  /**
   * Get comment.
   * @return comment.
   * @see #setComment
   */
  public Comment getComment();

  /**
   * Set comment.
   * @param comment the Comment object.
   * @see #getComment
   */
  public void setComment(Comment comment);

  /**
   * Get redirects.
   * @return the redirirects.
   * @see #setRedirectList
   */
  public Vector getRedirectList();

  /**
   * Add redirect.
   * @param redirect the Redirect object.
   */
  public void addRedirect(Redirect redirect);

  /**
   * Set redirects.
   * @param redirectList a list of the redirects.
   * @see #getRedirectList 
   */
  public void setRedirectList(Vector redirectList);
}
