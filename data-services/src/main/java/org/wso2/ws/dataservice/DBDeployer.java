/*
 * Copyright 2005,2006 WSO2, Inc. http://www.wso2.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.ws.dataservice;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import javax.sql.DataSource;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.deployment.Deployer;
import org.apache.axis2.deployment.DeploymentConstants;
import org.apache.axis2.deployment.DeploymentEngine;
import org.apache.axis2.deployment.DeploymentErrorMsgs;
import org.apache.axis2.deployment.DeploymentException;
import org.apache.axis2.deployment.repository.util.DeploymentFileData;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.AxisServiceGroup;
import org.apache.axis2.description.InOutAxisOperation;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.description.java2wsdl.Java2WSDLConstants;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.i18n.Messages;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ws.commons.schema.utils.NamespaceMap;
import org.wso2.registry.Registry;
import org.wso2.registry.RegistryException;
import org.wso2.registry.Resource;
import org.wso2.registry.app.RemoteRegistry;
import org.wso2.ws.dataservice.beans.Config;

public class DBDeployer implements Deployer, DeploymentConstants {
    private static final Log log = LogFactory.getLog(DBDeployer.class);
    private AxisConfiguration axisConfig;
    private ConfigurationContext configCtx;
    private DBMessageReceiver dbMessageReceiver;
    private String repoDir = null;
    private String extension = null;
    private Config config; 
    private static HashMap conversionType = null;
    private AxisService axisService = null;

    private static HashMap getConversionTable() {
        if (conversionType == null) {
            conversionType = new HashMap();
            conversionType.put(DBConstants.DataTypes.CHAR, "java.lang.String");
            conversionType.put("STRING", "java.lang.String");
            conversionType.put(DBConstants.DataTypes.VARCHAR, "java.lang.String");
            conversionType.put(DBConstants.DataTypes.TEXT, "java.lang.String");
            conversionType.put(DBConstants.DataTypes.NUMERIC, "java.math.BigDecimal");
            conversionType.put(DBConstants.DataTypes.DECIMAL, "java.math.BigDecimal");
            conversionType.put(DBConstants.DataTypes.MONEY, "java.math.BigDecimal");
            conversionType.put(DBConstants.DataTypes.SMALLMONEY, "java.math.BigDecimal");
            conversionType.put(DBConstants.DataTypes.BIT, "boolean");
            conversionType.put(DBConstants.DataTypes.TINYINT, "byte");
            conversionType.put(DBConstants.DataTypes.SMALLINT, "short");
            conversionType.put(DBConstants.DataTypes.INTEGER, "int");
            conversionType.put(DBConstants.DataTypes.BIGINT, "long");
            conversionType.put(DBConstants.DataTypes.REAL, "float");
            conversionType.put(DBConstants.DataTypes.FLOAT, "double");
            conversionType.put(DBConstants.DataTypes.DOUBLE, "double");
            conversionType.put(DBConstants.DataTypes.BINARY, "base64Binary"); //byte []
            conversionType.put(DBConstants.DataTypes.VARBINARY, "base64Binary"); // byte []
            conversionType.put(DBConstants.DataTypes.LONG_VARBINARY, "base64Binary"); // byte []
            conversionType.put(DBConstants.DataTypes.IMAGE, "base64Binary"); // byte []
            conversionType.put(DBConstants.DataTypes.DATE, "java.sql.Date");
            conversionType.put(DBConstants.DataTypes.TIME, "java.sql.Time");
            conversionType.put(DBConstants.DataTypes.TIMESTAMP, "java.sql.Timestamp");
        }
        return conversionType;
    }
    public void deploy(DeploymentFileData deploymentFileData) {
        StringWriter errorWriter;
        String serviceStatus;
        errorWriter = new StringWriter();
        serviceStatus = "";
        try {
            deploymentFileData.setClassLoader(axisConfig.getSystemClassLoader());
            AxisServiceGroup serviceGroup = new AxisServiceGroup(axisConfig);
            serviceGroup.setServiceGroupClassLoader(deploymentFileData.getClassLoader());
            ArrayList serviceList =
                    processService(deploymentFileData, serviceGroup, configCtx);
            DeploymentEngine.addServiceGroup(serviceGroup, serviceList, deploymentFileData
                    .getFile().toURL(), deploymentFileData, axisConfig);
            log.info(Messages.getMessage("deployingws", deploymentFileData.getName()));
        } catch (DeploymentException de) {
            log.error(Messages.getMessage(DeploymentErrorMsgs.INVALID_SERVICE, deploymentFileData
                    .getName(), de.getMessage()), de);
            PrintWriter error_ptintWriter = new PrintWriter(errorWriter);
            de.printStackTrace(error_ptintWriter);
            serviceStatus = "Error:\n" + errorWriter.toString();
        } catch (AxisFault axisFault) {
            log.error(Messages.getMessage(DeploymentErrorMsgs.INVALID_SERVICE, deploymentFileData
                    .getName(), axisFault.getMessage()), axisFault);
            PrintWriter errorPrintWriter = new PrintWriter(errorWriter);
            axisFault.printStackTrace(errorPrintWriter);
            serviceStatus = "Error:\n" + errorWriter.toString();
        } catch (Exception e) {
            if (log.isInfoEnabled()) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                log.info(Messages.getMessage(DeploymentErrorMsgs.INVALID_SERVICE,
                        deploymentFileData.getName(), sw.getBuffer().toString()));
            }
            PrintWriter errorPrintWriter = new PrintWriter(errorWriter);
            e.printStackTrace(errorPrintWriter);
            serviceStatus = "Error:\n" + errorWriter.toString();
        } catch (Throwable t) {
            if (log.isInfoEnabled()) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                t.printStackTrace(pw);
                log.info(Messages.getMessage(DeploymentErrorMsgs.INVALID_SERVICE,
                        deploymentFileData.getName(), sw.getBuffer().toString()));
            }
            PrintWriter errorPrintWriter = new PrintWriter(errorWriter);
            t.printStackTrace(errorPrintWriter);
            serviceStatus = "Error:\n" + errorWriter.toString();
        } finally {
            if (serviceStatus.startsWith("Error:")) {
                axisConfig.getFaultyServices().put(deploymentFileData.getFile().getAbsolutePath(),
                        serviceStatus);
                //Registering a timer task to re-establish the database connection 
                //& deploy the service again
        		TimerTask faultyServiceRectifier = new FaultyServiceRectifier(axisService,
        				deploymentFileData,configCtx,config);
        		Timer timer = new Timer();
        		//Retry in 1 minute
        		long retryIn = new Long(1000*60).longValue();
        		timer.scheduleAtFixedRate(faultyServiceRectifier, new Date(),retryIn);
            }
        }
    }

    public void init(ConfigurationContext configCtx) {
        this.configCtx = configCtx;
        axisConfig = this.configCtx.getAxisConfiguration();
        // init is called after the setDirectory is called so setting the
        // repoDir and the extension here.
        configCtx.setProperty(DBConstants.DB_SERVICE_REPO, this.repoDir);
        configCtx.setProperty(DBConstants.DB_SERVICE_EXTENSION, this.extension);
    }

    public void setDirectory(String repoDir) {
        this.repoDir = repoDir;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    
    public void unDeploy(String fileName) {
        try {
            File file = new File(fileName);
            fileName = file.getName();
            fileName = DeploymentEngine.getAxisServiceName(fileName);
            AxisServiceGroup serviceGroup = axisConfig.removeServiceGroup(fileName);
            configCtx.removeServiceGroupContext(serviceGroup);
            log.info(Messages.getMessage("serviceremoved", fileName));
        } catch (AxisFault axisFault) {
            axisConfig.removeFaultyService(fileName);
        }
    }

    private AxisService createDBService(String configFilePath,
                                        AxisConfiguration axisConfiguration)
            throws AxisFault {
        AxisService axisService;
        OMElement configElement;
        String baseURI = null;
        DBMessageReceiver dbMessageReceiver = getDbMessageReceiver();
        HashMap callQueryMap = new HashMap();
        FileInputStream fis = null;
        String dataSourceType = DBConstants.DATASOURCE_TYPE_RDBMS; //RDBMS is the default
        try {
            fis = new FileInputStream(configFilePath);
            
            configElement = (new StAXOMBuilder(fis)).getDocumentElement();
            configElement.build();
            
            Config config;
            
            //If configuration file is hosted on wso2 registry
            //get it from there
			configElement = checkForConfigurationFileOnRegistry(configElement);		
            
            String serviceName = configElement.getAttributeValue(new QName("name"));
            baseURI = configElement.getAttributeValue(new QName("baseURI"));
            axisService = new AxisService(serviceName);

            OMElement connection = configElement.getFirstChildWithName(new QName("config"));

            // This method is used as opposed to adding all the parameters to
            // limit errors on run time. Now if some
            // thing is missing we will see an error at Deployment time.
            HashMap props = getProperties(connection);
            ArrayList reqProps = new ArrayList();
            //TODO : Do we really need to populate this list ?
            reqProps.add(DBConstants.DRIVER);
            reqProps.add(DBConstants.PROTOCOL);
            reqProps.add(DBConstants.USER);
            reqProps.add(DBConstants.PASSWORD);
            reqProps.add(DBConstants.DB_CONFIG_ELEMENT);
            
            //load parameters in <config> section 
            config = getConfigProperties(connection);
            axisService.addParameter(DBConstants.CONNECTION_CONFIG , config);

            for (int i = 0; i < reqProps.size(); i++) {
                String propName = (String) reqProps.get(i);
                String propValue = (String) props.get(propName);
                if(propValue != null){
                	axisService.addParameter(new Parameter(propName, propValue));	
                }
            }

            // If is not one of the required properties then add it in now.
            for (Iterator it = props.keySet().iterator(); it.hasNext();) {
                String propName = (String) it.next();
                if (!reqProps.contains(propName)) {
                	if(propName.equals(DBConstants.CSV_DATASOURCE)){
                		dataSourceType = DBConstants.DATASOURCE_TYPE_CSV;
                	}else if(propName.equals(DBConstants.EXCEL_DATASOURCE)){
                		dataSourceType = DBConstants.DATASOURCE_TYPE_EXCEL;
                	}else if(propName.equals(DBConstants.JNDI.INITIAL_CONTEXT_FACTORY)){
                		dataSourceType = DBConstants.DATASOURCE_TYPE_JNDI;
                	}
                    String propValue = (String) props.get(propName);
                    axisService.addParameter(new Parameter(propName, propValue));                }
            }
            
            //check for LDAP Datasource access via JDBC driver
            String protocol = config.getPropertyValue(DBConstants.PROTOCOL);
            if(protocol.toLowerCase().startsWith("jdbc:ldap")){
            	dataSourceType = DBConstants.DATASOURCE_TYPE_LDAP_VIA_JDBC;
            }
            
            
            //connection properties are added to Axis Service.
            //Lets create the connect & put it to AxisConfiguration
            if(dataSourceType.equals(DBConstants.DATASOURCE_TYPE_RDBMS)){
            	DBCPConnectionManager dbcpConnectionManager = 
            		DBUtils.initializeDBConnectionManager(axisService.getName(),config);
            	if(dbcpConnectionManager != null){
            		try {
						dbcpConnectionManager.getDatasource().getConnection();
					} catch (SQLException e) {
						log.error("Pooling manager could not establish a connection to database.", e);
						throw new AxisFault("Pooling manager could not establish a connection to database.",e);
					}
            		axisService.addParameter(DBConstants.DB_CONNECTION, dbcpConnectionManager.getDatasource());
            	}else {
                  throw new AxisFault("Pooling manager could not establish a connection to database.");
                }            	
            }else if(dataSourceType.equals(DBConstants.DATASOURCE_TYPE_JNDI)){
            	DataSource dataSource = JNDIUtils.getDataSource(axisService);
                if (dataSource != null) {
                    axisService.addParameter(DBConstants.DB_CONNECTION, dataSource);
                } else {
                    throw new AxisFault("Could not establish a connection to database.");
                }	
            }

            // Used by the container to find out what kind of a service
            // this is.
            axisService.addParameter(new Parameter("serviceType", DBConstants.DB_SERVICE_TYPE));
            axisService.addParameter(new Parameter(DBConstants.DB_SERVICE_CONFIG_FILE,
                    configFilePath));

            // Get all query elements & put them into a hashmap with id as the
            // key
            Iterator queries = configElement.getChildrenWithName(new QName("query"));
            
            //This contains map of parameters passed along with query href
            //HashMap paramsPerCallQueryMap = new HashMap();
            //extractParamsPerCallQuery(configElement, queries,
            //        paramsPerCallQueryMap);
            
            HashMap queryMap = new HashMap();
            while (queries.hasNext()) {
                OMElement queryElement = (OMElement) queries.next();
                String queryId = queryElement.getAttributeValue(new QName("id"));
                queryMap.put(queryId, queryElement);
                CallQuery callQuery = new CallQuery();
                //callQuery.populateCallQuery(queryElement,paramsPerCallQueryMap);
                callQuery.populateCallQuery(queryElement,axisService);
                callQueryMap.put(queryId,callQuery);
            }
            axisService.addParameter(new Parameter(DBConstants.DB_QUERY_ELEMENTS, queryMap));
            if(baseURI == null){
            	axisService.setTargetNamespace(DBConstants.WSO2_NAMESPACE);            	
            }else{
            	axisService.setTargetNamespace(baseURI);
            }
            
            axisService.setElementFormDefault(false);
            
            //set the datasource type as a Axis parameter
            if(DBConstants.DATASOURCE_TYPE_CSV.equals(dataSourceType)){
            	axisService.addParameter(new Parameter(DBConstants.DATASOURCE_TYPE,DBConstants.DATASOURCE_TYPE_CSV));
            }else if(DBConstants.DATASOURCE_TYPE_EXCEL.equals(dataSourceType)){
            	axisService.addParameter(new Parameter(DBConstants.DATASOURCE_TYPE,DBConstants.DATASOURCE_TYPE_EXCEL));
            }else if(DBConstants.DATASOURCE_TYPE_JNDI.equals(dataSourceType)){
            	axisService.addParameter(new Parameter(DBConstants.DATASOURCE_TYPE,DBConstants.DATASOURCE_TYPE_JNDI));
            }else if(DBConstants.DATASOURCE_TYPE_LDAP_VIA_JDBC.equals(dataSourceType)){
            	axisService.addParameter(new Parameter(DBConstants.DATASOURCE_TYPE,DBConstants.DATASOURCE_TYPE_LDAP_VIA_JDBC));
            }else{//default is RDBMS
            	axisService.addParameter(new Parameter(DBConstants.DATASOURCE_TYPE,DBConstants.DATASOURCE_TYPE_RDBMS));
            }


            Iterator ops = configElement.getChildrenWithName(new QName("operation"));
            while (ops.hasNext()) {
                OMElement operation = (OMElement) ops.next();
                String opName = operation.getAttributeValue(new QName("name"));
                int index = opName.indexOf(":");
                if(index > -1){
                	opName = opName.substring(index+1);
                }
                //is it always InOutAxisOperation ??
                AxisOperation axisOperation = new InOutAxisOperation(new QName(opName));
                // axisOperation.
                axisOperation.setMessageReceiver(dbMessageReceiver);
                axisOperation.setStyle("document");
                axisConfig.getPhasesInfo().setOperationPhases(axisOperation);
                axisOperation.addParameter(new Parameter(DBConstants.DB_OPERATION_ELEMENT,
                        operation));
                axisService.addOperation(axisOperation);

                //getting the query element
                OMElement callQuery = operation.getFirstChildWithName(new QName("call-query"));
                String hrefVal = callQuery.getAttributeValue(new QName("href"));
                OMElement query = (OMElement) queryMap.get(hrefVal);
                CallQuery callQueryElement = new CallQuery();
                //callQueryElement.populateCallQuery(query,paramsPerCallQueryMap);
                callQueryElement.populateCallQuery(query,axisService);

                //creating a parameter to hold query object
                Parameter callQueryParameter = new Parameter();
                callQueryParameter.setName(DBConstants.CALL_QUERY_ELEMENT);
                callQueryParameter.setValue(callQueryElement);
                axisOperation.addParameter(callQueryParameter);
            }

        } catch (FileNotFoundException e) {
            throw new AxisFault("Error reading service configuration file.", e);
        } catch (XMLStreamException e) {
            throw new AxisFault("Error while parsing the service configuration file.", e);
        }finally{
        	try {
				fis.close();
			} catch (IOException e) {
				throw new AxisFault(e.getMessage());
			}
        }
        DataServiceDocLitWrappedSchemaGenerator schemaGenerator =
                new DataServiceDocLitWrappedSchemaGenerator(axisService,callQueryMap);
        //schemaGenerator.setElementFormDefault(Java2WSDLConstants.FORM_DEFAULT_UNQUALIFIED);
        //schemaGenerator.setAttrFormDefault(Java2WSDLConstants.FORM_DEFAULT_UNQUALIFIED);
        schemaGenerator.setElementFormDefault(Java2WSDLConstants.FORM_DEFAULT_QUALIFIED);
        schemaGenerator.setAttrFormDefault(Java2WSDLConstants.FORM_DEFAULT_QUALIFIED);
        NamespaceMap map = new NamespaceMap();
        if(baseURI == null){
            map.put(Java2WSDLConstants.AXIS2_NAMESPACE_PREFIX,
                    DBConstants.WSO2_NAMESPACE);        	
        }else{
            map.put(Java2WSDLConstants.AXIS2_NAMESPACE_PREFIX,
                    baseURI);
        }
        map.put(Java2WSDLConstants.DEFAULT_SCHEMA_NAMESPACE_PREFIX,
                Java2WSDLConstants.URI_2001_SCHEMA_XSD);
        axisService.setNameSpacesMap(map);
        //axisService.setElementFormDefault(false);
        schemaGenerator.generateSchema();
        return axisService;
    }
	private OMElement checkForConfigurationFileOnRegistry(
			OMElement configElement) throws XMLStreamException {
		//check for configuration file hosted on wso2registry
		OMElement configEle = configElement.getFirstChildWithName(new QName("config"));
		Config config = getConfigProperties(configEle);
		String registryUrl = config.getPropertyValue("registry_url");
		String contentPath = config.getPropertyValue("registry_contentpath");
		if(registryUrl != null){
			//Get configuration file from registry
			String registryUserName = config.getPropertyValue("registry_url");
			if(registryUserName != null && registryUserName.trim().length() == 0){
				registryUserName = null;
			}
			String registryPassword = config.getPropertyValue("registry_url");
			if(registryPassword != null && registryPassword.trim().length() == 0){
				registryPassword = null;
			}
			Registry registry;
			try {
				registry = new RemoteRegistry(new URL(registryUrl));
				Resource resource = registry.get(contentPath);
				byte[] content = (byte[])resource.getContent();
				ByteArrayInputStream ins = new ByteArrayInputStream(content);
				configElement = (new StAXOMBuilder(ins)).getDocumentElement();
				
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (RegistryException e) {
				e.printStackTrace();
			}			
		}
		return configElement;
	}
    
    
    /*
     * map populated here (paramsPerCallQueryMap) will be used to get user 
     * defined parameter name for web service operation
     */
    /*
    private void extractParamsPerCallQuery(OMElement configElement,
            Iterator queries, HashMap paramsPerCallQueryMap) {
        Iterator operations = configElement.getChildrenWithName(new QName("operation"));
        while(operations.hasNext()){
            OMElement operationElement = (OMElement) operations.next();
            Iterator callQueries = operationElement.getChildrenWithName(new QName("call-query"));
            if(callQueries != null){
                //there can be only one call-query per operation
                Object obj = null;
                while(callQueries.hasNext()){
                    obj = callQueries.next();
                    if(obj instanceof OMElement){
                        break;
                    }
                }
                if(obj != null){
                    OMElement callQuery = (OMElement)obj;
                    
                    Iterator withParams = callQuery.getChildrenWithName(new QName("with-param"));
                    
                    //this hashmap holds following key, value pairs
                    //<with-param name="id" query-param="deptId"/>
                    HashMap paramNameWithQueryParam = new HashMap();
                    while(withParams.hasNext()){
                        OMElement withParam = (OMElement)withParams.next();
                        String paramName = withParam.getAttributeValue(new QName("name"));
                        String queryParam = withParam.getAttributeValue(new QName("query-param"));
                        paramNameWithQueryParam.put(paramName,queryParam);
                    }
                    String callQueryRef = callQuery.getAttributeValue(new QName("href"));
                    paramsPerCallQueryMap.put(callQueryRef,paramNameWithQueryParam);                    
                }
            }
        }
    }
    */

    public static String getJavaTypeFromSQLType(String sqlType) {
        HashMap convertionMap = getConversionTable();
        return (String) convertionMap.get(sqlType);
    }


    /**
     * @deprecated : use getConfigProperties() instead
     *  
     */
    private HashMap getProperties(OMElement confiObj) {
        HashMap props = new HashMap();
        for (Iterator iter = confiObj.getChildrenWithName(new QName("property")); iter.hasNext();) {
            OMElement element = (OMElement) iter.next();
            props.put((element.getAttributeValue(new QName("name"))),
                    element.getText());
        }
        return props;
    }

    private Config getConfigProperties(OMElement confiObj) {
        Config config = new Config();
        for (Iterator iter = confiObj.getChildrenWithName(new QName("property")); iter.hasNext();) {
            OMElement element = (OMElement) iter.next();
            config.addProperty((element.getAttributeValue(new QName("name"))), element.getText());
        }
        return config;
    }
    
    private ArrayList processService(DeploymentFileData currentFile,
                                     AxisServiceGroup axisServiceGroup, ConfigurationContext configCtx)
            throws AxisFault {
    	String dataServiceFileName = currentFile.getName();
    	//service group name is taken from the file name, where as
    	//service name is taken from name provided inside configuration
    	String serviceGroupName = dataServiceFileName.substring(0, dataServiceFileName.lastIndexOf("."));
        axisService = createDBService(currentFile.getAbsolutePath(), 
        				configCtx.getAxisConfiguration());
        axisServiceGroup.setServiceGroupName(serviceGroupName);
        axisService.setParent(axisServiceGroup);
        axisService.setClassLoader(axisConfig.getServiceClassLoader());
        ArrayList serviceList = new ArrayList();
        serviceList.add(axisService);
        return serviceList;
    }

    private DBMessageReceiver getDbMessageReceiver() {
        if (dbMessageReceiver == null)
            dbMessageReceiver = new DBMessageReceiver();
        return dbMessageReceiver;
    }

}
