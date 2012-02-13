package org.wso2.ws.dataservice.adaptors;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.description.AxisService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.ws.dataservice.DBConstants;
import org.wso2.ws.dataservice.beans.Config;

public abstract class DataSourceAdaptor {
	private static final Log log = LogFactory.getLog(LDAPJDBCAdaptor.class);
	/**
	 * @param operationElement
	 * @param axisService
	 * @param inputMessage
	 * @return - OMElement constructed according to how user has specified
	 */
	//TODO : this signature(esp. parameters) needs to be changed.
	abstract public OMElement processQuery(
			OMElement operationElement, 
			AxisService axisService
			,OMElement inputMessage);
	

	/*
	 * Generates an response containing fault element.
	 * Fault element contains two elements(code & detail) 
	 */
	protected static OMElement generateErrorResponse(String targetNamespace,AxisFault e){
		//get stackTrace into a String
	    StringWriter sw = new StringWriter();
	    PrintWriter pw = new PrintWriter(sw);
	    e.printStackTrace(pw);
		
		OMFactory fac = OMAbstractFactory.getOMFactory();
		OMNamespace omNs = fac.createOMNamespace(targetNamespace, "data");
		
		OMElement resultElement = fac.createOMElement("data", omNs);
		//TODO : read from query/fault
		OMElement faultElement = fac.createOMElement("fault",null);		
		
		OMElement codeElement = fac.createOMElement("code", omNs);
		codeElement.setText(e.getMessage());
		OMElement detailElement = fac.createOMElement("detail", omNs);
		detailElement.setText(sw.toString());
		
		faultElement.addChild(codeElement);
		faultElement.addChild(detailElement);
		resultElement.addChild(faultElement);
		return resultElement;				
	}
	
	
	public static PreparedStatement getProcessedPreparedStatement(HashMap inputs, HashMap params,
			HashMap paramOrder,HashMap originalParamNames,HashMap paramTypes, Connection conn
			, String sqlStatement
			,String callee
			,String serviceName) throws AxisFault {
	    
        String paramName = null;
        String originalParamName = null;
        String sqlType = null;
        String value = null;
        String paramType = null;
        
		log.debug("["+serviceName+"] Processing prepared statement for SQL " + sqlStatement);
		Set paramNames = params.keySet();
		Object pramNameArray[] = paramNames.toArray();        

	    try{
	    	PreparedStatement sqlQuery = null;
	    if("SQL".equals(callee)){
	    	sqlQuery = conn.prepareStatement(sqlStatement);
			//SQL expects parameters, but not params set in config file
			if(sqlStatement.indexOf("?") > -1 && pramNameArray.length == 0){
				throw new AxisFault("["+serviceName+"]  SQL : "+sqlStatement+" expects one or more parameters. " +
						"But none is mentioned in the configuration file.");
			}	    	
	    }else if("STORED-PROCEDURE".equals(callee)){
	    	sqlQuery = conn.prepareCall(sqlStatement);
	    }

		for (int i = 0; i < pramNameArray.length; i++) {
			paramName = (String) paramOrder.get(new Integer(i + 1));
			originalParamName = (String) originalParamNames.get(new Integer(i + 1));
			sqlType = (String) params.get(paramName);
			paramType = (String)paramTypes.get(paramName);
			value = (String) inputs.get(paramName);
			log.debug("["+serviceName+"]  Param name : "+paramName
			        +" SQL Type : "+sqlType
			        +" Value : "+value);
			
			if("IN".equals(paramType)
					|| "INOUT".equals(paramType)){
			    if(value == null || value.trim().length() == 0){
		            log.error("["+serviceName+"]  Empty value found for parameter : "+originalParamName);
		            throw new AxisFault("["+serviceName+"]  Empty value found for parameter : "+originalParamName);			        
			    }				
			}
		    //work-a-round for setting NULL
		    if("NULL".equalsIgnoreCase(value)){
		    	value = null;
		    }
		    //TODO : work-a-round for setting space
			
			if (sqlType == null) {
				// Defaults to string
				if("IN".equals(paramType)){
					sqlQuery.setString(i + 1, value);
				}else if("INOUT".equals(paramType)){
					sqlQuery.setString(i + 1, value);
					((CallableStatement)sqlQuery).registerOutParameter(i+1, java.sql.Types.VARCHAR);
				}else{
					((CallableStatement)sqlQuery).registerOutParameter(i+1, java.sql.Types.VARCHAR);
				}				
			} else if (DBConstants.DataTypes.INTEGER.equals(sqlType)) {				
				if("IN".equals(paramType)){
					if("SQL".equals(callee)){
						sqlQuery.setInt(i + 1, Integer.parseInt(value));	
					}else{
						((CallableStatement)sqlQuery).setInt(i + 1, Integer.parseInt(value));
					}					
				}else if("INOUT".equals(paramType)){
					((CallableStatement)sqlQuery).setInt(i + 1, Integer.parseInt(value));
					((CallableStatement)sqlQuery).registerOutParameter(i+1, java.sql.Types.INTEGER);
				}else{
					((CallableStatement)sqlQuery).registerOutParameter(i+1, java.sql.Types.INTEGER);
				}				
			} else if (DBConstants.DataTypes.STRING.equals(sqlType)) {				
				if("IN".equals(paramType)){
					if("SQL".equals(callee)){
						sqlQuery.setString(i + 1, value);	
					}else{
						((CallableStatement)sqlQuery).setString(i + 1, value);
					}
				}else if("INOUT".equals(paramType)){
					((CallableStatement)sqlQuery).setString(i + 1, value);
					((CallableStatement)sqlQuery).registerOutParameter(i+1, java.sql.Types.VARCHAR);
				}else{
					((CallableStatement)sqlQuery).registerOutParameter(i+1, java.sql.Types.VARCHAR);
				}				
			} else if (DBConstants.DataTypes.DOUBLE.equals(sqlType)) {				
				if("IN".equals(paramType)){
					if("SQL".equals(callee)){
						sqlQuery.setDouble(i + 1, Double.parseDouble(value));	
					}else{
						((CallableStatement)sqlQuery).setDouble(i + 1, Double.parseDouble(value));
					}					
				}else if("INOUT".equals(paramType)){
					((CallableStatement)sqlQuery).setDouble(i + 1, Double.parseDouble(value));
					((CallableStatement)sqlQuery).registerOutParameter(i+1, java.sql.Types.DOUBLE);
				}else{
					((CallableStatement)sqlQuery).registerOutParameter(i+1, java.sql.Types.DOUBLE);
				}
			}else if(DBConstants.DataTypes.DATE.equals(sqlType)){
			    try{	
					//Only yyyy-MM-dd part is needed
			    	String modifiedValue = value.substring(0, 10);
					if("IN".equals(paramType)){
						if("SQL".equals(callee)){
							sqlQuery.setDate(i+1, Date.valueOf(modifiedValue));	
						}else{
							((CallableStatement)sqlQuery).setDate(i+1, Date.valueOf(modifiedValue));
						}
					}else if("INOUT".equals(paramType)){
						((CallableStatement)sqlQuery).setDate(i+1, Date.valueOf(modifiedValue));
						((CallableStatement)sqlQuery).registerOutParameter(i+1, java.sql.Types.DATE);
					}else{
						((CallableStatement)sqlQuery).registerOutParameter(i+1, java.sql.Types.DATE);
					}			        
			    }catch(IllegalArgumentException e){
		            log.error("Incorrect date format("+value+") for parameter : "+paramName, e);
		            throw new AxisFault("Incorrect date format for parameter  : "
		                    +paramName+".Date should be in yyyy-mm-dd format.", e);			        
			    }			    
			}else if(DBConstants.DataTypes.TIMESTAMP.equals(sqlType)){
				Timestamp timestamp = getTimestamp(value,paramName);
				if("IN".equals(paramType)){
					if("SQL".equals(callee)){
						sqlQuery.setTimestamp(i+1,timestamp);	
					}else{
						((CallableStatement)sqlQuery).setTimestamp(i+1,timestamp);
					}
				}else if("INOUT".equals(paramType)){
					((CallableStatement)sqlQuery).setTimestamp(i+1,timestamp);
					((CallableStatement)sqlQuery).registerOutParameter(i+1, java.sql.Types.TIMESTAMP);					
				}else{
					((CallableStatement)sqlQuery).registerOutParameter(i+1, java.sql.Types.TIMESTAMP);
				}
			}else if(DBConstants.DataTypes.TIME.equals(sqlType)){
				Time time = getTime(value,paramName);
				if("IN".equals(paramType)){					
					if("SQL".equals(callee)){
						sqlQuery.setTime(i+1,time);	
					}else{
						((CallableStatement)sqlQuery).setTime(i+1,time);
					}				
				}else if("INOUT".equals(paramType)){
					((CallableStatement)sqlQuery).setTime(i+1,time);
					((CallableStatement)sqlQuery).registerOutParameter(i+1, java.sql.Types.TIME);
				}else{
					((CallableStatement)sqlQuery).registerOutParameter(i+1, java.sql.Types.TIME);
				}				
			}else{
				log.error("["+serviceName+"]  Unsupported data type : "+sqlType+" as input parameter.");
				throw new AxisFault("["+serviceName+"]  Found Unsupported data type : "+sqlType+" as input parameter.");
			}
		}
		return sqlQuery;
	    }
	    catch(NumberFormatException e){
	        log.error("["+serviceName+"]  Incorrect value found for parameter : "+originalParamName, e);
	        throw new AxisFault("["+serviceName+"]  Incorrect value found for parameter : "+originalParamName, e);
	    }catch(SQLException e){
	        log.error("["+serviceName+"]  Error occurred while preparing prepared statement for sql : "+sqlStatement, e);
	        throw new AxisFault("["+serviceName+"]  Error occurred while preparing prepared statement for sql : "+sqlStatement, e); 
	    }
	}
	
	/**
	 * @param value - String value to be converted to timestamp.
	 * Should be either in format "yyyy-MM-dd hh:mm:ss" or "yyyy-MM-dd hh:mm:ss.SSSSSS"
	 * @param paramName - name given to parameter in data service configuration file
	 * @return Timestamp object containing passed value
	 */
	protected static Timestamp getTimestamp(String value,String paramName)
	throws AxisFault{
		java.sql.Timestamp timestamp = null;
		SimpleDateFormat sdf;
	    try{
	    	sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSSSSS");
	    	java.util.Date date = sdf.parse(value);
	    	timestamp = new java.sql.Timestamp(date.getTime());		    	
	    }catch(ParseException e){
    	    try{
    	    	sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    	    	java.util.Date date = sdf.parse(value);
    	    	timestamp = new java.sql.Timestamp(date.getTime());  	
    	    }catch(ParseException e1){
    	    	sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.sss'Z'");
    	    	try {					
        	    	java.util.Date date = sdf.parse(value);
        	    	timestamp = new java.sql.Timestamp(date.getTime());  	
				} catch (ParseException e2) {	    	    	
	    	    	try {
	    	    		sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.sss'+'hh:mm");
	        	    	java.util.Date date = sdf.parse(value);
	        	    	timestamp = new java.sql.Timestamp(date.getTime());  	
					} catch (ParseException e3) {
						try{
		    	    		sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.sss'-'hh:mm");
		        	    	java.util.Date date = sdf.parse(value);
		        	    	timestamp = new java.sql.Timestamp(date.getTime());	
						} catch (ParseException e4) {
							try{
			    	    		sdf = new SimpleDateFormat("yyyy-MM-dd");
			        	    	java.util.Date date = sdf.parse(value);
			        	    	timestamp = new java.sql.Timestamp(date.getTime());	
							} catch (ParseException e5) {
				                log.error("Incorrect Timestamp format for parameter : "+paramName
				                		+".Timestamp should be in one of following formats " +
				                				"yyyy-MM-dd'T'hh:mm:ss.sss'+'hh:mm, " +
				                				"yyyy-MM-dd'T'hh:mm:ss.sss'-'hh:mm, " +
				                				"yyyy-MM-dd'T'hh:mm:ss.sss'Z', " +
				                				"yyyy-MM-dd hh:mm:ss.SSSSSS or " +
				                				"yyyy-MM-dd hh:mm:ss", e5);
						                throw new AxisFault("Incorrect Timestamp format for parameter : "+paramName
				                		+".Timestamp should be in one of following formats " +
				                				"yyyy-MM-dd'T'hh:mm:ss.sss'+'hh:mm, " +
				                				"yyyy-MM-dd'T'hh:mm:ss.sss'-'hh:mm, " +
				                				"yyyy-MM-dd'T'hh:mm:ss.sss'Z', " +
				                				"yyyy-MM-dd hh:mm:ss.SSSSSS or " +
				                				"yyyy-MM-dd hh:mm:ss", e5);           
							}
						}
					}
				}
    	    }            
	    }
	    return timestamp;
	}
	
	/**
	 * @param value - String value to be converted to time.Should be either in format "hh:mm:ss"
	 * @param paramName - name given to parameter in data service configuration file
	 * @return Time object containing passed value
	 */
	
	protected static Time getTime(String value, String paramName)
			throws AxisFault {
		java.sql.Time time;
		try {			
			SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
			java.util.Date date = sdf.parse(value);
			time = new java.sql.Time(date.getTime());
		} catch (ParseException e) {
			log.error("Incorrect Time format for parameter : "+ paramName
									+ ".Time should be in the format hh:mm:ss",e);
			throw new AxisFault("Incorrect Time format for parameter : "+ paramName
									+ ".Time should be in the format hh:mm:ss",e);
		}
		return time;
	}		
	
	protected static Connection checkDBConnectionStatus(AxisService axisService,Connection conn) 
	throws AxisFault{
	    if(log.isDebugEnabled()){
	        log.debug("checking database connection status");
	    }	    
	    if( DBConstants.DATASOURCE_TYPE_RDBMS.equals(
	    		(String)axisService.getParameterValue(DBConstants.DATASOURCE_TYPE))
	            || DBConstants.DATASOURCE_TYPE_JNDI.equals(
	            		(String)axisService.getParameterValue(DBConstants.DATASOURCE_TYPE))
	            		 || DBConstants.DATASOURCE_TYPE_LDAP_VIA_JDBC.equals(
	     	            		(String)axisService.getParameterValue(DBConstants.DATASOURCE_TYPE))){
	        try {
                if(conn == null || conn.isClosed()){
                    if(log.isDebugEnabled()){
                        log.debug("Database connection is closed.Trying to re-establish.");
                    }
                    Config config = (Config)axisService.getParameterValue(DBConstants.CONNECTION_CONFIG);
                    return createConnection(axisService,config);
                }else{
                    //existing connection is not closed. Return it.
                    return conn;
                }
            } catch (SQLException e) {
                log.error("Error occurred while trying to re-establish the database connection.", e);
                throw new AxisFault("Error occurred while trying to re-establish the database connection.",e);
            }
	    }
	    return null;
	}

	protected static Connection createConnection(AxisService axisService,Config config) throws AxisFault {
		try{
			log.debug("Getting database connection for "+axisService.getName());
			Connection conn = null;			
			
//			if(config.getPropertyValue(DBConstants.MIN_POOL_SIZE) != null 
//					|| config.getPropertyValue(DBConstants.MAX_POOL_SIZE) != null){
//				//user has set connection pool size(s). Get connection from pooling manager
//				DataSource dataSource =
//					(DataSource)axisService.getParameterValue(DBConstants.DB_CONNECTION);
//				conn = dataSource.getConnection();				
//			}else{
				//Try to load the JDBC driver class. If class not found throw an error.
				Class.forName(config.getPropertyValue(DBConstants.DRIVER)).newInstance();
				Properties props = new Properties();
				String userName = config.getPropertyValue(DBConstants.USER);
				String password = config.getPropertyValue(DBConstants.PASSWORD);
				if(userName != null && userName.trim().length() > 0){
					props.put("user", userName);	
				}
				if(password != null && password.trim().length() > 0){
					props.put("password", password);
				}								
				conn = DriverManager.getConnection((String) config.getPropertyValue(DBConstants.PROTOCOL), props);
//			}
			return conn;
		} catch (SQLException e) {
			log.error("Error occured while connecting to database",e);
			throw new AxisFault("Error occured while connecting to database",e);
			
		} catch (ClassNotFoundException e) {
			log.error("JDBC driver not available in classpath : "+e.getMessage());
			throw new AxisFault("JDBC driver not available in classpath : "+e.getMessage());
		}
		catch (InstantiationException e) {
			log.error("Error occurred during instantiating "+config.getPropertyValue(DBConstants.DRIVER), e);
			throw new AxisFault("Error occurred during instantiating "+config.getPropertyValue(DBConstants.DRIVER), e);
		} 
		catch (IllegalAccessException e) {
			log.error("Illegal Access error during loading "+config.getPropertyValue(DBConstants.DRIVER), e);
			throw new AxisFault("Illegal Access error during loading "+config.getPropertyValue(DBConstants.DRIVER), e);
		}
	}
	
}
