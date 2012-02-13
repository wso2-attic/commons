/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     
 *******************************************************************************/

package org.eclipse.wst.wsdl.validation.internal.wsdl11.xsd;

/**
 * This interface is used to signify error levels that are the result of
 * XMLParseException. They conceptually correspond to org.w3c.dom.DOMError but
 * we use our own interface, since DOMError is not in Java 1.4, and its
 * package changes from Xerces 2.6.2 and Xerces 2.7.0.
 */

public interface DOMError {
	int SEVERITY_WARNING = 1;
	int SEVERITY_ERROR = 2;
	int SEVERITY_FATAL_ERROR = 3;
}
