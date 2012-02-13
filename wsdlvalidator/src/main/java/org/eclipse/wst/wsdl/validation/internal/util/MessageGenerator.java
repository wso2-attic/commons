/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.validation.internal.util;

import java.util.ResourceBundle;
import java.text.MessageFormat;

/**
 * A convenience class for working with resources in a resource bundle.
 */
public class MessageGenerator
{
  protected ResourceBundle resourcebundle;

  /**
   * Constructor.
   * 
   * @param rb The resource bundle to work with.
   */
  public MessageGenerator(ResourceBundle rb)
  {
    resourcebundle = rb;
  }

  /**
  * Set the resourcebundle to the one specified.
  * 
  * @param rb The resource bundle to set.
  */
  protected void setResourceBundle(ResourceBundle rb)
  {
    resourcebundle = rb;
  }

  /**
  * Gets the string resource for the given key.
  * 
  * @param key The key for the resource.
  * @return The string from the resource bundle.
  */
  public String getString(String key)
  {
    return resourcebundle.getString(key);
  }

  /**
   * Gets the string resource for the given key and does one substitution.
   * 
   * @param key The key for the resource.
   * @param s1 The substitution to perform.
   * @return The string from the resource bundle.
   */
  public String getString(String key, Object s1)
  {
    return MessageFormat.format(getString(key), new Object[] { s1 });
  }

  /**
   * Gets the string resource for the given key and does two substitutions.
   * 
   * @param key The key for the resource.
   * @param s1 The first substitution to perform.
   * @param s2 The second substitution to perform.
   * @return The string from the resource bundle.
   */
  public String getString(String key, Object s1, Object s2)
  {
    return MessageFormat.format(getString(key), new Object[] { s1, s2 });
  }

  /**
   *  Gets the string resource for the given key and does three substitutions.
   * 
   * @param key The key for the resource.
   * @param s1 The first substitution to perform.
   * @param s2 The second substitution to perform.
   * @param s3 The third substitution to perform.
   * @return The string from the resource bundle.
   */
  public String getString(String key, Object s1, Object s2, Object s3)
  {
    return MessageFormat.format(getString(key), new Object[] { s1, s2, s3 });
  }
}
