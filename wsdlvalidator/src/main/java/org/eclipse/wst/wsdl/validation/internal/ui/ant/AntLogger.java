/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.validation.internal.ui.ant;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.eclipse.wst.wsdl.validation.internal.logging.ILogger;

/**
 * A logger that logs using Ant's log method.
 */
public class AntLogger implements ILogger 
{
  private Task task = null;
	
  /**
   * Constructor.
   * 
   * @param antTask
   * 		The Ant task this logger will act for.
   */
  public AntLogger(Task antTask)
  {
    task = antTask;
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.validation.internal.logging.ILogger#log(java.lang.String, int)
   */
  public void log(String message, int severity) 
  {
    int msgLevel = Project.MSG_ERR;
    if(severity == ILogger.SEV_WARNING)
    {
      msgLevel = Project.MSG_WARN;
    }
    else if(severity == ILogger.SEV_INFO)
    {
      msgLevel = Project.MSG_INFO;
    }
    else if(severity == ILogger.SEV_VERBOSE)
    {
      msgLevel = Project.MSG_VERBOSE;
    }
    task.log(message, msgLevel);
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.validation.internal.logging.ILogger#log(java.lang.String, int, java.lang.Throwable)
   */
  public void log(String message, int severity, Throwable throwable) 
  {
	log(message, severity);
	log(throwable.toString(), severity);
  }

}
