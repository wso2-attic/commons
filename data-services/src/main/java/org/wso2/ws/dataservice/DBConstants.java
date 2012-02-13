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

public class DBConstants {
	public static final String DRIVER = "org.wso2.ws.dataservice.driver";
	public static final String PROTOCOL = "org.wso2.ws.dataservice.protocol";
	public static final String USER = "org.wso2.ws.dataservice.user";
	public static final String PASSWORD = "org.wso2.ws.dataservice.password";
	public static final String MIN_POOL_SIZE = "org.wso2.ws.dataservice.minpoolsize";
	public static final String MAX_POOL_SIZE = "org.wso2.ws.dataservice.maxpoolsize";
	
	//Keys for parameters in AxisService
	public static final String CONNECTION_CONFIG = "org.wso2.ws.dataservice.config";
	
	public static final String CSV_DATASOURCE = "csv_datasource";
	public static final String EXCEL_DATASOURCE = "excel_datasource";
	public static final String JNDI_DATASOURCE = "jndi_datasource";
	public static final String DB_OPERATION_ELEMENT = "org.wso2.ws.dataservice.db_operation_element";
	public static final String CALL_QUERY_ELEMENT = "CallQuery";
	public static final String DB_CONFIG_ELEMENT =  "org.wso2.ws.dataservice.db_config_element";
	public static final String DB_QUERY_ELEMENTS =  "org.wso2.ws.dataservice.db_query_elements";
	public static final String AXIS2_HOME = "/work/axis2/target/dist/axis2-SNAPSHOT";
	public static final String DB_SERVICE_TYPE = "data_service";
	public static final String DB_SERVICE_CONFIG_FILE = "org.wso2.ws.dataservice.db_service_config_file";
	public static final String DB_SERVICE_REPO = "local_org.wso2.ws.dataservice.db_service_repo";
	public static final String DB_SERVICE_EXTENSION = "local_org.wso2.ws.dataservice.db_service_extension";
	public static final String DB_CONNECTION = "org.wso2.ws.dataservice.dbconnection";
	public static final String NAMESPACE_PREFIX_MAP = "org.wso2.ws.dataservice.namespaceprefixmap";
	public static final String QUERYLEVEL_PREFIX_MAP = "org.wso2.ws.dataservice.querylevelprefixmap";
	public static final String QUERYLEVEL_NAMESPACE_MAP = "org.wso2.ws.dataservice.querylevelnamespacemap";
	public static final String RESULT_PREFIX = "data"; 
	
    
	public interface DataTypes{
	    public static final String CHAR = "CHAR";
	    public static final String STRING = "STRING";
	    public static final String VARCHAR = "VARCHAR";
	    public static final String TEXT = "TEXT";
	    public static final String NUMERIC = "NUMERIC";
	    public static final String DECIMAL = "DECIMAL";
	    public static final String MONEY = "MONEY";
	    public static final String SMALLMONEY = "SMALLMONEY";
	    public static final String BIT = "BIT";
	    public static final String TINYINT = "TINYINT";
	    public static final String SMALLINT = "SMALLINT";
	    public static final String INTEGER = "INTEGER";
	    public static final String BIGINT = "BIGINT";
	    public static final String REAL = "REAL";
	    public static final String FLOAT = "FLOAT";
	    public static final String DOUBLE = "DOUBLE";
	    public static final String BINARY = "BINARY";
	    public static final String VARBINARY = "VARBINARY";
	    public static final String LONG_VARBINARY = "LONG VARBINARY";
	    public static final String IMAGE = "IMAGE";
	    public static final String DATE = "DATE";
	    public static final String TIME = "TIME";
	    public static final String TIMESTAMP = "TIMESTAMP";		
	}
    public static final String WSO2_NAMESPACE = "http://ws.wso2.org/dataservice";
    
    //Datasource Type
    public static final String DATASOURCE_TYPE = "DATASOURCE_TYPE";
    public static final String DATASOURCE_TYPE_RDBMS= "RDBMS";
    public static final String DATASOURCE_TYPE_CSV= "CSV";
    public static final String DATASOURCE_TYPE_EXCEL= "EXCEL";
    public static final String DATASOURCE_TYPE_JNDI= "JNDI";
    public static final String DATASOURCE_TYPE_LDAP_VIA_JDBC= "LDAP_VIA_JDBC";
    

    //same values are present in data_service.js & data_service.xsl.If you change any value here, 
    //make sure you change those files as well.
    public interface Query{
        //CSV query parameters
    	String CSV = "csv";
        String EXCEL = "excel";    	
    	String HAS_HEADER = "hasheader";
    	String STARTING_ROW = "startingrow";
    	String MAX_ROW_COUNT = "maxrowcount";
    	String COLUMNS = "columns";
    	String COLUMN = "column";

    	//CSV query parameters
    	String CSV_COLUMN_SEPERATOR = "columnseperator";
    	String CSV_COLUMN_ORDER = "columnordinal";
    	//excel query parameters
    	String EXCEL_WORKBOOK_NAME = "workbookname";
    }
    
    public interface JNDI{
        String INITIAL_CONTEXT_FACTORY = "jndi_context_class";   	
        String PROVIDER_URL = "jndi_provider_url";
    	String RESOURCE_NAME = "jndi_resource_name";
    	String USERNAME = "jndi_username";
    	String PASSWORD = "jndi_password";
    }
}

