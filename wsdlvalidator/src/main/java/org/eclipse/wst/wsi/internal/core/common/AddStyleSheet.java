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
package org.eclipse.wst.wsi.internal.core.common;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.document.DocumentElement;

/**
 * The interface for AddStyleSheet element. 
 * 
 * @version 1.0.1
 * @author Peter Brittenham  (peterbr@us.ibm.com)
 */
public interface AddStyleSheet extends DocumentElement
{
  /**
   * Element name.
   */
  public static final String ELEM_NAME = WSIConstants.ELEM_ADD_STYLE_SHEET;

  /**
   * Get href.
   * @return href.
   * @see #setHref
   */
  public String getHref();

  /**
   * Set href.
   * @param href a href.
   * @see #getHref
   */
  public void setHref(String href);

  /**
   * Get type.
   * @return type.
   * @see #setType
   */
  public String getType();

  /**
   * Set type.
   * @param type a type.
   * @see #getType
   */
  public void setType(String type);

  /**
   * Get title.
   * @return title.
   * @see #setTitle
   */
  public String getTitle();

  /**
   * Set title.
   * @param title a title.
   * @see #getTitle
   */
  public void setTitle(String title);

  /**
   * Get media.
   * @return media.
   * @see #setMedia
   */
  public String getMedia();

  /**
   * Set title.
   * @param media media.
   * @see #getMedia
   */
  public void setMedia(String media);

  /**
   * Get charset.
   * @return charset.
   * @see #setCharset
   */
  public String getCharset();

  /**
   * Set charset.
   * @param charset charset.
   * @see #getCharset
   */
  public void setCharset(String charset);

  /**
   * Get alternate.
   * @return alternate.
   * @see #setAlternate
   */
  public String getAlternate();

  /**
   * Set alternate.
   * @param alternate alternate.
   * @see #getAlternate
   */
  public void setAlternate(String alternate);

  /**
   * Set if the style sheet declaration should be a comment.
   * @param comment a boolean.
   */
  public void setComment(boolean comment);

  /**
   * Get style sheet declaration.
   * @return style sheet declaration.
   */
  public String getStyleSheetString();
}
