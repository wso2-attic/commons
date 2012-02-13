/*******************************************************************************
 * Copyright (c) 2006, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.util;


import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.xsd.XSDDiagnostic;
import org.eclipse.xsd.XSDDiagnosticSeverity;
import org.w3c.dom.Node;


/**
 * Default implementation of the WSDLDiagnostic interface. This class is based
 * on XSDDiagnosticImpl
 * <p>
 * <b>Note:</b> This class/interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is being made available at this early stage to solicit feedback
 * from pioneering adopters on the understanding that any code that uses this
 * API will almost certainly be broken (repeatedly) as the API evolves.
 * </p>
 */
public class WSDLDiagnosticImpl extends EObjectImpl implements WSDLDiagnostic
{
  /**
   * The default value of the '{@link #getColumn() <em>Column</em>}'
   * attribute.
   * 
   * @see #getColumn()
   */
  protected static final int COLUMN_EDEFAULT = 1;

  /**
   * The default value of the '{@link #getLine() <em>Line</em>}' attribute.
   * 
   * 
   * @see #getLine()
   */
  protected static final int LINE_EDEFAULT = 1;

  /**
   * The default value of the '{@link #getLocationURI() <em>Location URI</em>}'
   * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @see #getLocationURI()
   */
  protected static final String LOCATION_URI_EDEFAULT = null;

  /**
   * The default value of the '{@link #getMessage() <em>Message</em>}'
   * attribute.
   * 
   * @see #getMessage()
   */
  protected static final String MESSAGE_EDEFAULT = null;

  /**
   * The default value of the '{@link #getSeverity() <em>Severity</em>}'
   * attribute.
   * 
   * @see #getSeverity()
   */
  protected static final WSDLDiagnosticSeverity SEVERITY_EDEFAULT = WSDLDiagnosticSeverity.FATAL_LITERAL;

  /**
   * The cached value of the '{@link #getColumn() <em>Column</em>}' attribute.
   * 
   * 
   * @see #getColumn()
   * 
   * @ordered
   */
  protected int column = COLUMN_EDEFAULT;

  /**
   * The cached value of the '{@link #getLine() <em>Line</em>}' attribute.
   * 
   * 
   * @see #getLine()
   */
  protected int line = LINE_EDEFAULT;

  /**
   * The cached value of the '{@link #getLocationURI() <em>Location URI</em>}'
   * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @see #getLocationURI()
   */
  protected String locationURI = LOCATION_URI_EDEFAULT;

  /**
   * The cached value of the '{@link #getMessage() <em>Message</em>}'
   * attribute.
   * 
   * @see #getMessage()
   */
  protected String message = MESSAGE_EDEFAULT;

  /**
   * The cached value of the '{@link #getSeverity() <em>Severity</em>}'
   * attribute.
   * 
   * @see #getSeverity()
   */
  protected WSDLDiagnosticSeverity severity = SEVERITY_EDEFAULT;

  /**
   * The original message key.
   */
  protected String key;

  /**
   * The original message parameters.
   */
  protected EList substitutions;

  /**
   * The Node where the error occurs
   */
  protected Node node;

  /**
   * The WSDLElement or XSDConcreteComponent object where the error occurs
   */
  protected Object container;

  /**
   * Default constructor.
   */
  public WSDLDiagnosticImpl()
  {
    super();
  }

  /**
   * Wraps an XSD diagnostic into a WSDL diagnostic. Used to report problems in
   * inline schemas.
   * 
   * @param xsdDiagnostic
   *          the XSD diagnostic object to wrap. Must not be null.
   */
  public WSDLDiagnosticImpl(XSDDiagnostic xsdDiagnostic)
  {
    this();
    setMessage(xsdDiagnostic.getMessage());
    setSeverity(getSeverity(xsdDiagnostic.getSeverity()));
    setLine(xsdDiagnostic.getLine());
    setColumn(xsdDiagnostic.getColumn());
    setNode(xsdDiagnostic.getNode());
    setContainer(xsdDiagnostic.getContainer());
    setLocation(xsdDiagnostic.getLocation());
    setKey(xsdDiagnostic.getKey());
    getSubstitutions().addAll(xsdDiagnostic.getSubstitutions());
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.emf.ecore.resource.Resource$Diagnostic#getColumn()
   */
  public int getColumn()
  {
    return column;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.emf.ecore.resource.Resource$Diagnostic#getLine()
   */
  public int getLine()
  {
    return line;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.emf.ecore.resource.Resource$Diagnostic#getLocation()
   */
  public String getLocation()
  {
    return locationURI;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.emf.ecore.resource.Resource$Diagnostic#getMessage()
   */
  public String getMessage()
  {
    return message;
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.wst.wsdl.util.WSDLDiagnostic#getSeverity()
   */
  public WSDLDiagnosticSeverity getSeverity()
  {
    return severity;
  }

  /**
   * Converts an XSD diagnostic severity level into a WSDL diagnostic severity
   * level.
   * 
   * @param xsdDiagnosticSeverity
   *          the source XSD diagnostic severity.
   * @return the equivalent WSDL diagnostic severity.
   */
  private WSDLDiagnosticSeverity getSeverity(XSDDiagnosticSeverity xsdDiagnosticSeverity)
  {
    switch (xsdDiagnosticSeverity.getValue())
    {
      case XSDDiagnosticSeverity.FATAL:
      return WSDLDiagnosticSeverity.FATAL_LITERAL;
      case XSDDiagnosticSeverity.ERROR:
      return WSDLDiagnosticSeverity.ERROR_LITERAL;
      case XSDDiagnosticSeverity.WARNING:
      return WSDLDiagnosticSeverity.WARNING_LITERAL;
      case XSDDiagnosticSeverity.INFORMATION:
      return WSDLDiagnosticSeverity.INFORMATION_LITERAL;
      default:
      break;
  }
  return SEVERITY_EDEFAULT;
}

  /**
   * Sets the new value of the column attribute.
   * 
   * @param newColumn
   *          the new column value.
   */
  public void setColumn(int newColumn)
  {
    column = newColumn;
  }

  public void setLine(int newLine)
  {
    line = newLine;
  }

  /**
   * Sets the value of the loaction attribute.
   * 
   * @param location
   *          a String with the new value for the location attribute
   */
  public void setLocation(String location)
  {
    locationURI = location;
  }

  /**
   * Sets a new value for the message attribute.
   * 
   * @param newMessage
   *          a String with the new message.
   */
  public void setMessage(String newMessage)
  {
    message = newMessage;
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.wst.wsdl.util.WSDLDiagnostic#setSeverity(org.eclipse.wst.wsdl.util.WSDLDiagnosticSeverity)
   */
  public void setSeverity(WSDLDiagnosticSeverity newSeverity)
  {
    severity = newSeverity == null ? SEVERITY_EDEFAULT : newSeverity;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.wst.wsdl.util.WSDLDiagnostic#getKey()
   */
  public String getKey()
  {
    return key;
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.wst.wsdl.util.WSDLDiagnostic#setKey(java.lang.String)
   */
  public void setKey(String value)
  {
    key = value;
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.wst.wsdl.util.WSDLDiagnostic#getSubstitutions()
   */
  public EList getSubstitutions()
  {
    if (substitutions == null)
    {
      substitutions = new BasicEList();
    }
    return substitutions;
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.util.WSDLDiagnostic#getNode()
   */
  public Node getNode()
  {
    return node;
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.util.WSDLDiagnostic#setNode(org.w3c.dom.Node)
   */
  public void setNode(Node node)
  {
    this.node = node;
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.util.WSDLDiagnostic#getContainer()
   */
  public Object getContainer()
  {
    return container;
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.util.WSDLDiagnostic#setContainer(java.lang.Object)
   */
  public void setContainer(Object container)
  {
    this.container = container;
  }
}
