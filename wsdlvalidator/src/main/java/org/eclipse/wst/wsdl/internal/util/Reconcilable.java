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
package org.eclipse.wst.wsdl.internal.util;


import org.w3c.dom.Element;


// TODO... why do we need this?
// 
public interface Reconcilable
{
  public void setElement(Element element);

  public Element getElement();

  public void reconcileAttributes(Element changedElement);

  public void reconcileReferences(boolean deep);
}
