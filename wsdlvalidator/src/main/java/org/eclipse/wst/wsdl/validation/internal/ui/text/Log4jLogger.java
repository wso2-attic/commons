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
package org.eclipse.wst.wsdl.validation.internal.ui.text;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.wst.wsdl.validation.internal.logging.ILogger;

/**
 * A custom WSDL validator logger that passes logging events to log4j.
 */
public class Log4jLogger implements ILogger 
{
  // This class is implemented using reflection to avoid a comilation dependency
  // on log4j.
  protected Object logger = null;
  protected Method error1 = null;
  protected Method warn1 = null;
  protected Method info1 = null;
  protected Method debug1 = null;
  protected Method error2 = null;
  protected Method warn2 = null;
  protected Method info2 = null;
  protected Method debug2 = null;
  
  public Log4jLogger()
  {
	try
    {
	  Class loggerClass = getClass().getClassLoader().loadClass("org.apache.log4j.Logger");
	  Class categoryClass = getClass().getClassLoader().loadClass("org.apache.log4j.Category");
	  Method getLogger = loggerClass.getDeclaredMethod("getLogger" , new Class[]{Class.class});
	  logger = getLogger.invoke(loggerClass, new Object[]{WSDLValidate.class});
	  error1 = categoryClass.getDeclaredMethod("error" , new Class[]{Object.class});
	  warn1 = categoryClass.getDeclaredMethod("warn" , new Class[]{Object.class});
	  info1 = categoryClass.getDeclaredMethod("info" , new Class[]{Object.class});
	  debug1 = categoryClass.getDeclaredMethod("debug" , new Class[]{Object.class});
	  error2 = categoryClass.getDeclaredMethod("error" , new Class[]{Object.class, Throwable.class});
	  warn2 = categoryClass.getDeclaredMethod("warn" , new Class[]{Object.class, Throwable.class});
	  info2 = categoryClass.getDeclaredMethod("info" , new Class[]{Object.class, Throwable.class});
	  debug2 = categoryClass.getDeclaredMethod("debug" , new Class[]{Object.class, Throwable.class});
	}
	catch(ClassNotFoundException e)
	{
	  System.err.println("Unable to create Log4j Logger. Ensure Log4J is on the classpath."); 
	}
	catch(NoSuchMethodException e)
	{
		
	}
	catch(IllegalAccessException e)
	{
		
	}
	catch(InvocationTargetException e)
	{
		
	}
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.validation.internal.logging.ILogger#log(java.lang.String, int)
   */
  public void log(String message, int severity) 
  {
	if(logger != null)
    {
      try
      {
        if(severity == ILogger.SEV_ERROR)
        {
          error1.invoke(logger, new Object[]{message});
		}
        else if(severity == ILogger.SEV_WARNING)
        {
          warn1.invoke(logger, new Object[]{message});
        }
        else if(severity == ILogger.SEV_INFO)
        {
          info1.invoke(logger, new Object[]{message});
        }
        else if(severity == ILogger.SEV_VERBOSE)
        {
          debug1.invoke(logger, new Object[]{message});
        }
      }
      catch(InvocationTargetException e)
      {
        // Do nothing.
      }
      catch(IllegalAccessException e)
      {
        // Do nothing.  
      }
    }
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.validation.internal.logging.ILogger#log(java.lang.String, int, java.lang.Throwable)
   */
  public void log(String message, int severity, Throwable throwable) 
  {
	if(logger != null)
	{
	  try
	  {
	    if(severity == ILogger.SEV_ERROR)
	    {
		  error2.invoke(logger, new Object[]{message, throwable});
	    }
	    else if(severity == ILogger.SEV_WARNING)
	    {
		  warn2.invoke(logger, new Object[]{message, throwable});
	    }
	    else if(severity == ILogger.SEV_INFO)
	    {
		  info2.invoke(logger, new Object[]{message, throwable});
	    }
	    else if(severity == ILogger.SEV_VERBOSE)
	    {
	      debug2.invoke(logger, new Object[]{message, throwable});
	    }
	  }
	  catch(InvocationTargetException e)
	  {
		// Do nothing.
	  }
	  catch(IllegalAccessException e)
	  {
		// Do nothing.  
	  }
	}
  }
}
