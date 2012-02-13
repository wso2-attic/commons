package org.wso2.ws.dataservice;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.axis2.AxisFault;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.Parameter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JNDIUtils {
	private static final Log log = LogFactory.getLog(JNDIUtils.class);

	/*
	 * Returns a JDBC Connection using JNDI resource properties specified as 
	 * AxisService parameters
	 */
	public static DataSource getDataSource(AxisService axisService) throws AxisFault{
		Properties properties = null;		
		// username
		Parameter username = axisService.getParameter(DBConstants.JNDI.USERNAME);
		if (username != null) {
			if (properties == null)
				properties = new Properties();
			properties.setProperty(Context.SECURITY_PRINCIPAL,
					((String)username.getValue()).trim());
		}
		// password
		Parameter password = axisService.getParameter(DBConstants.JNDI.PASSWORD);
		if (password != null) {
			if (properties == null)
				properties = new Properties();
			properties.setProperty(Context.SECURITY_CREDENTIALS,
					((String)password.getValue()).trim());
		}
		// factory class
		Parameter factoryClass = axisService.getParameter(DBConstants.JNDI.INITIAL_CONTEXT_FACTORY);
		if (factoryClass != null) {
			if (properties == null)
				properties = new Properties();
			properties.setProperty(Context.INITIAL_CONTEXT_FACTORY,
					((String)factoryClass.getValue()).trim());
		}
		// contextUrl
		Parameter contextUrl = axisService.getParameter(DBConstants.JNDI.PROVIDER_URL);
		if (contextUrl != null) {
			if (properties == null)
				properties = new Properties();
			properties.setProperty(Context.PROVIDER_URL,
					((String)contextUrl.getValue()).trim());
		}
		// resourceName
		Parameter resource = axisService.getParameter(DBConstants.JNDI.RESOURCE_NAME);
		if (resource == null) {
			log.error("JNDI Resource name not specified in Data Service Configuration file");
			return null;
		}		

		//get Datasource using these properties
		try {
			InitialContext context = getContext(properties);
			Object obj = getJNDIResource(context, (String)resource.getValue());
			if(obj != null){
				DataSource dataSource = (DataSource)obj;
				return dataSource;
			}
		} catch (AxisFault e) {
			log.error("Error retrieving properties from AxisService", e);
			throw e;
		} catch (NamingException e) {
			log.error("Naming error occurred while trying to retrieve JDBC Connection " +
					"from JNDI tree.",e);
			throw new AxisFault("Naming error occurred while trying to retrieve JDBC Connection " +
                    "from JNDI tree.",e);
		} 
		return null;
	}

	
	private static InitialContext getContext(Properties properties)
	throws AxisFault, javax.naming.NamingException {
		return new InitialContext(properties);
	}

	private static Object getJNDIResource(InitialContext context, String resourceName)
	throws AxisFault, javax.naming.NamingException {
		if(log.isDebugEnabled()){
			log.debug("Looking up for resource : "+resourceName);	
		}		
        Object obj = context.lookup(resourceName);
        return obj;		
	}

}
