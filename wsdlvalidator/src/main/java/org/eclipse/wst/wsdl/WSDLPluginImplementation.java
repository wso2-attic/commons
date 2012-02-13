/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl;


import org.eclipse.emf.common.EMFPlugin.EclipsePlugin;


/**
 * The actual implementation of the Eclipse <b>Plugin</b>.
 * <p>
 * <b>Note:</b> This class/interface is part of an interim API that is still under development and expected to
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken
 * (repeatedly) as the API evolves.
 * </p>
 * @since 1.0
 */
public class WSDLPluginImplementation extends EclipsePlugin
{
  /**
   * Creates an instance.
   */
  public WSDLPluginImplementation()
  {
    super();

    // Remember the static instance.
    //
    WSDLPlugin.plugin = this;
  }
}
