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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import javax.sql.DataSource;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.om.util.StAXUtils;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.databinding.utils.BeanUtil;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.description.AxisService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.wso2.ws.dataservice.adaptors.LDAPJDBCAdaptor;
import org.wso2.ws.dataservice.beans.CSVQuery;
import org.wso2.ws.dataservice.beans.Config;
import org.wso2.ws.dataservice.beans.ExcelQuery;
import org.wso2.ws.dataservice.beans.Param;
import org.wso2.ws.dataservice.beans.Query;
import org.wso2.ws.dataservice.beans.Result;

import au.com.bytecode.opencsv.CSVReader;

public class DBUtils {
	private static final Log log = LogFactory.getLog(DBUtils.class);

	public static OMNode toOM(Reader reader) throws XMLStreamException {
		XMLStreamReader xmlReader = StAXUtils.createXMLStreamReader(reader);
		OMFactory fac = OMAbstractFactory.getOMFactory();
		StAXOMBuilder staxOMBuilder = new StAXOMBuilder(fac, xmlReader);
		return staxOMBuilder.getDocumentElement();
	}
	
	private static OMElement processSQLQuery(OMElement operationElement, AxisService axisService,
			OMElement inputMessage) {
		HashMap inputs = new HashMap();
		for (Iterator iter = inputMessage.getChildElements(); iter.hasNext();) {
			OMElement element = (OMElement) iter.next();
			inputs.put(element.getLocalName().toLowerCase(), element.getText());
		}

		OMElement callQueryElement =
			operationElement.getFirstChildWithName(new QName("call-query"));
		return getRDBMSResult(callQueryElement, axisService, inputs,0);
	}

	private static OMElement getRDBMSResult(OMElement callQueryElement, AxisService axisService,
			HashMap inputs,int queryLevel) {
		OMElement resultElement = null;
		OMElement queryElement ;
		try {
			// Find the corresponding query element
			String href = callQueryElement.getAttributeValue(new QName("href"));
			if (href != null) {
				HashMap queries =
					(HashMap) axisService.getParameterValue(DBConstants.DB_QUERY_ELEMENTS);
				queryElement = (OMElement) queries.get(href);
			} else {
				queryElement = callQueryElement.getFirstChildWithName(new QName("query"));
			}

			HashMap params = new HashMap();
			// TODO : sumedha, we need a mechanism to specify parameter ordering
			// in the spec.As of now order in which it appears in taken.
			HashMap paramOrder = new HashMap();
			//To keep track of parameter names with original case
			HashMap originalParamNames = new HashMap();
			
			//parameter type(IN/OUT/INOUT) for stored procedures
			//For SQL queries, default is IN
			HashMap paramType = new HashMap();
			Iterator iParams = queryElement.getChildrenWithName(new QName("param"));
			int position = 0;
			while (iParams.hasNext()) {				
				OMElement param = (OMElement) iParams.next();
				String inOutType = param.getAttributeValue(new QName("type"));

					String paramName = param.getAttributeValue(new QName("name"));
					String sqlType = param.getAttributeValue(new QName("sqlType"));				
					//default is IN type
					if(inOutType == null || inOutType.trim().length() == 0){
						inOutType = "IN";
					}
					
					params.put(paramName.toLowerCase(), sqlType);
					paramOrder.put(new Integer(position + 1), paramName.toLowerCase());
					originalParamNames.put(new Integer(position + 1), paramName);
					paramType.put(paramName.toLowerCase(), inOutType);
					position++;		
			}

			String query = queryElement.getFirstChildWithName(new QName("sql")).getText();
			// Check the type of SQL statement & get result
			// SELECT,INSERT,UPDATE,DELETE,CREATE all have 6 chars ;-)

			String firstPartOfSQL = query.substring(0, 6);

			if (firstPartOfSQL.equalsIgnoreCase("SELECT")) {
				resultElement =
					getSelectResult(queryElement, inputs, params, paramOrder
							,originalParamNames,paramType, axisService, false,queryLevel);

			} else if (firstPartOfSQL.equalsIgnoreCase("INSERT")
					|| firstPartOfSQL.equalsIgnoreCase("UPDATE")
					|| firstPartOfSQL.equalsIgnoreCase("DELETE")
					|| firstPartOfSQL.equalsIgnoreCase("CREATE")) {
				resultElement = getSelectResult(queryElement, inputs, params
						,paramOrder,originalParamNames,paramType, axisService, true,queryLevel);

			}else {
				//This can be a stored procedure call
				resultElement =
					getStoredProcedureResult(queryElement, inputs, params
							,paramOrder,originalParamNames,paramType, axisService,queryLevel);

			}
		} catch (Exception e) {
			log.error("Error occured", e);
			return generateErrorResponse(axisService.getTargetNamespace(), (AxisFault)e);
	    }
		return resultElement;
	}

	/*
	 * Generates an response containing fault element.
	 * Fault element contains two elements(code & detail) 
	 */
	private static OMElement generateErrorResponse(String targetNamespace,AxisFault e){
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
	
//	private static void getDefaultValuesForResultsWrapper(String wrapperElementName,String rowElementName){
//	    if(wrapperElementName == null || wrapperElementName.trim().length() == 0){
//	        //default value
//	        wrapperElementName = "results";	            
//	    }
//        if(rowElementName == null || rowElementName.trim().length() == 0){
//            //default value
//            rowElementName = "row";             
//        }	    
//	}

	/*
	 * For RDBMS & JNDI connections re-establishing connection, if existing connection is closed.
	 */
	private static Connection checkDBConnectionStatus(AxisService axisService,Connection conn) 
	throws AxisFault{
	    if(log.isDebugEnabled()){
	        log.debug("checking database connection status");
	    }	    
	    if( DBConstants.DATASOURCE_TYPE_RDBMS.equals(
	    		(String)axisService.getParameterValue(DBConstants.DATASOURCE_TYPE))
	            || DBConstants.DATASOURCE_TYPE_JNDI.equals(
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
	
	private static OMElement getStoredProcedureResult(OMElement queryElement,HashMap inputValues,
			HashMap params, HashMap paramOrder,HashMap originalParamNames,HashMap paramType, AxisService axisService
			,int queryLevel)
	throws AxisFault{
		OMElement resultElement = null;
		Connection conn = null;
		boolean noResultSet = false;
		String elementValue;
		boolean hasResponse = true;
		OMNamespace omNs = null;
		String sqlQuery = queryElement.getFirstChildWithName(new QName("sql")).getText();
		try {
			//Parameter dbConnectionParam = axisService.getParameter(DBConstants.DB_CONNECTION);  
			//if(dbConnectionParam == null){
			//	throw new AxisFault("Database connection not found in Axis Configuration");
			//}
			//conn = (Connection)dbConnectionParam.getValue();
			conn = checkDBConnectionStatus(axisService, conn);
			conn.setAutoCommit(false);
			CallableStatement cs = (CallableStatement)getProcessedPreparedStatement
			(inputValues, params, paramOrder,originalParamNames,paramType, conn
					, sqlQuery,"STORED-PROCEDURE",axisService.getName());
			
			ResultSet rs = null;
			try{
				rs = cs.executeQuery();
				if(rs.getMetaData().getColumnCount() == 0){
					noResultSet = true;
				}
			}catch(SQLException e){
				if(e.getMessage().indexOf("java.sql.CallableStatement.executeQuery() " +
						"was called but no result set was returned. " +
						"Use java.sql.CallableStatement.executeUpdate() for non-queries.") > -1){
					noResultSet = true;
					cs.executeUpdate();
				}else{
					log.error("Error occured getting stored procedure result", e);
				}
			}
			

			//check for out parameters			
			OMElement result = queryElement.getFirstChildWithName(new QName("result"));
			String prefix = null;
			String resultElementNS = null;
			Query query = new Query(queryElement.getChildrenWithName(new QName("param")));
			if(result == null){
				hasResponse = false;
			}
			
			if(hasResponse){
				Result resultObj = new Result(result);	
				resultElementNS = result.getAttributeValue(new QName("defaultNamespace"));
				if(resultElementNS == null || resultElementNS.trim().length() == 0){
					resultElementNS = axisService.getTargetNamespace();
				}

				String columnDefalut = result.getAttributeValue(new QName("columnDefault"));
				OMFactory fac = OMAbstractFactory.getOMFactory();
				
				//get prefix for namespace
				HashMap namespacePrefixMap = null;
				HashMap queryLevelNamespaceMap = null;
				HashMap queryLevelPrefixMap = null;
				
				if(axisService.getParameterValue(DBConstants.NAMESPACE_PREFIX_MAP)!= null){
					namespacePrefixMap = (HashMap)axisService.getParameterValue(DBConstants.NAMESPACE_PREFIX_MAP);
					queryLevelNamespaceMap = (HashMap)axisService.getParameterValue(DBConstants.QUERYLEVEL_NAMESPACE_MAP);
					queryLevelPrefixMap = (HashMap)axisService.getParameterValue(DBConstants.QUERYLEVEL_PREFIX_MAP);
					
					prefix = (String)namespacePrefixMap.get(resultElementNS);
					if(prefix == null){
						prefix = DBConstants.RESULT_PREFIX+BeanUtil.getUniquePrefix();
						namespacePrefixMap.put(resultElementNS, prefix);
						queryLevelNamespaceMap.put(new Integer(queryLevel), resultElementNS);
						queryLevelPrefixMap.put(new Integer(queryLevel), prefix);
					}
				}else{
					namespacePrefixMap = new HashMap();
					queryLevelNamespaceMap = new HashMap();
					queryLevelPrefixMap = new HashMap();
					
					prefix = DBConstants.RESULT_PREFIX+BeanUtil.getUniquePrefix();
					namespacePrefixMap.put(resultElementNS, prefix);
					queryLevelNamespaceMap.put(new Integer(queryLevel), resultElementNS);
					queryLevelPrefixMap.put(new Integer(queryLevel), prefix);
					
					axisService.addParameter(DBConstants.NAMESPACE_PREFIX_MAP,namespacePrefixMap);
					axisService.addParameter(DBConstants.QUERYLEVEL_NAMESPACE_MAP,queryLevelNamespaceMap);
					axisService.addParameter(DBConstants.QUERYLEVEL_PREFIX_MAP,queryLevelPrefixMap);					
				}
				
				omNs = fac.createOMNamespace(resultElementNS, prefix);	

				if(queryLevel > 0){
					String previousNS = (String)queryLevelNamespaceMap.get(new Integer(queryLevel - 1));
					String previousNSPrefix = (String)queryLevelPrefixMap.get(new Integer(queryLevel - 1));
					omNs = fac.createOMNamespace(previousNS, previousNSPrefix);
					resultElement = fac.createOMElement(resultObj.getResultWrapper(), omNs);	
				}else{
					resultElement = fac.createOMElement(resultObj.getResultWrapper(), omNs);
				}			

					
					//put result elements into an array
	                Iterator tmpElements = result.getChildElements();
	                ArrayList tmpElementsArrayList = new ArrayList();
	                while(tmpElements.hasNext()){
	                   OMElement element = (OMElement) tmpElements.next();
	                   tmpElementsArrayList.add(element);
	                }
					
	                if(! noResultSet && rs != null){
	                	
	    				while (rs.next()) {
	    					HashMap elementValues = new HashMap();
	    					int columnCount = rs.getMetaData().getColumnCount();

	    					if(queryLevel > 0){
	    						omNs = fac.createOMNamespace(resultElementNS, prefix);	
	    					}
	    					
	    					OMElement row = fac.createOMElement(resultObj.getRowName(), omNs);
	    					if (resultObj.getRowName() == null) {
	    						row = resultElement;
	    					}
	    					for (int i = 1; i <= columnCount; i++) {
	    						String columnName = rs.getMetaData().getColumnLabel(i);
	    						//Some databases return columns in different cases
	    						columnName = columnName.toLowerCase();
	    						String columnValue = rs.getString(columnName);
	    						elementValues.put(columnName, columnValue);
	    					}

	    					boolean useAsParamToNextQuery = false;
	    					for(int a=0;a < resultObj.getDisplayColumnNames().length;a++){
	    						//can be one of 'element','attribute','text','link' or 'header'	
	    						String outPutElementType = resultObj.getElementLocalNames()[a];
	    						
	    						if (outPutElementType.equals("element")
	    								|| outPutElementType.equals("attribute")) {
	    						    String displayTagName = resultObj.getDisplayColumnNames()[a];
	    						    String resultSetFieldName = resultObj.getResultSetColumnNames()[a];
	    						    resultSetFieldName = resultSetFieldName.toLowerCase();

	    							// This means,the parameter is not part of the
	    							// resultset. i.e. it is being passed from user's
	    							// parameters.  						    

	    							if (useAsParamToNextQuery) {
	    								elementValue = (String) params.get(resultSetFieldName);
	    								elementValues.put(resultSetFieldName, elementValue);
	    							} else {
	    								elementValue = (String) elementValues.get(resultSetFieldName);
	    							}
	    							
	    							if(elementValue == null){
	    								//This could be a OUT parameter of a stored procedure
	       								elementValue = setOutparameterValue(cs, query,resultSetFieldName);    								
	    							}

	    							if (outPutElementType.equals("element")) {
	    								OMElement rowElement = fac.createOMElement(displayTagName, omNs);
	    								rowElement.addChild(fac.createOMText(rowElement, elementValue));
	    								row.addChild(rowElement);
	    							} else if (outPutElementType.equals("attribute")) {
	    								row.addAttribute(displayTagName, elementValue, omNs);
	    							}
	    						} else if (resultObj.getElementLocalNames()[a].equals("call-query")) {
	    						    OMElement element = (OMElement)tmpElementsArrayList.get(a);
	    							OMElement rowElement = getRDBMSResult(element, axisService, elementValues,queryLevel+1);
	    							queryLevel = queryLevel - 1;
	    							row.addChild(rowElement);
	    						}
	    					}
	    					if (resultObj.getRowName() != null) {
	    						resultElement.addChild(row);
	    					}
	    				}                	
	                }else{
	                	//No resultset, only out parameters are there.
	                	OMElement row = null;
	                	if(resultObj.getRowName() != null){
	                		//row name is OPTIONAL
	                		row = fac.createOMElement(resultObj.getRowName(), omNs);	
	                	}
						
						//if (resultObj.getRowName() == null) {
						//	row = resultElement;
						//}
	                	
	                	for(int a=0;a < resultObj.getDisplayColumnNames().length;a++){
	                		if (resultObj.getElementLocalNames()[a].equals("element")) {
							    String displayTagName = resultObj.getDisplayColumnNames()[a];
							    String resultSetFieldName = resultObj.getResultSetColumnNames()[a];
							    elementValue = setOutparameterValue(cs, query, resultSetFieldName);
							    
								if (columnDefalut == null || columnDefalut.equals("element")) {
									OMElement rowElement = fac.createOMElement(displayTagName, omNs);
									rowElement.addChild(fac.createOMText(rowElement, elementValue));
									if(row != null){
										row.addChild(rowElement);	
									}else{
										resultElement.addChild(rowElement);
									}
									
								} else if (columnDefalut.equals("attribute")) {
									if(row != null){
										row.addAttribute(displayTagName, elementValue, omNs);	
									}else{
										resultElement.addAttribute(displayTagName, elementValue, omNs);
									}							
								}						    
	                		}                		
	                	}
	                	if(row != null){
	                		resultElement.addChild(row);
	                	}                	
	                }				
			}		
		} catch (SQLException e) {
		    log.error("Exception occurred while trying to execute the SQL statement : "+sqlQuery, e);
			throw new AxisFault("Exception occurred while trying to execute the SQL statement : "+sqlQuery, e);
		} 	
		
		finally {
			try {
				if (conn != null && queryLevel == 0) {
					conn.commit();
					conn.close();
				}
			} catch (SQLException e) {
				log.error(e.getMessage());
				throw new AxisFault(
						"Exception occurred while trying to commit.", e);
			}
		}
		return resultElement;
	}

	private static String setOutparameterValue(CallableStatement cs,
			Query query, String resultSetFieldName)
			throws SQLException,AxisFault {
		// This could be an out parameter
			//Procedure returns both result & out parameters
		String elementValue = "";
		Param param = query.getParam(resultSetFieldName);
		if (param != null) {
			if ("OUT".equals(param.getType()) 
					|| "INOUT".equals(param.getType())) {
				if (param.getSqlType().equals(
						DBConstants.DataTypes.STRING)) {
					elementValue = cs.getString(param.getOrdinal());
				} else if (param.getSqlType().equals(
						DBConstants.DataTypes.DOUBLE)) {
					elementValue = String.valueOf(cs
							.getDouble(param.getOrdinal()));
				} else if (param.getSqlType().equals(
						DBConstants.DataTypes.BIGINT )) {
					elementValue = String.valueOf(cs
							.getLong(param.getOrdinal()));
				} else if (param.getSqlType().equals(
						DBConstants.DataTypes.INTEGER)) {
					elementValue = String.valueOf(cs
							.getInt(param.getOrdinal()));
				} else if (param.getSqlType().equals(
						DBConstants.DataTypes.TIME)) {
					elementValue = String.valueOf(cs
							.getTime(param.getOrdinal()));
				} else if (param.getSqlType().equals(
						DBConstants.DataTypes.DATE)) {
					elementValue = String.valueOf(cs
							.getDate(param.getOrdinal()));
				} else if (param.getSqlType().equals(
						DBConstants.DataTypes.TIMESTAMP)) {
					elementValue = String.valueOf(cs
							.getTimestamp(param.getOrdinal()));
				} else{
					log.error("Unsupported data type : "+param.getSqlType());
					throw new AxisFault("Unsupported data type : "+param.getSqlType());
				}
				
			}
		}
		return elementValue;
	}
	
	
	
	private static OMElement getSelectResult(OMElement queryElement, HashMap inputValues,
			HashMap params, HashMap paramOrder,HashMap originalParamNames,HashMap paramType
			, AxisService axisService, boolean isDML
			,int queryLevel)
	throws AxisFault {
		OMElement resultElement = null;
		Connection conn = null;
		boolean hasResponse = true;
		OMFactory fac = null;
		OMNamespace omNs = null;
		Result resultObj = null;
		//String columnDefault = null; 
		
		String sqlQuery = queryElement.getFirstChildWithName(new QName("sql")).getText();
		try {
			//Parameter dbConnectionParam = axisService.getParameter(DBConstants.DB_CONNECTION);  
			//if(dbConnectionParam == null){
			//	throw new AxisFault("["+axisService.getName()+"] Database connection not found in Axis Configuration");
			//}
			//conn = (Connection)dbConnectionParam.getValue();
			conn = checkDBConnectionStatus(axisService, conn);		
			
			conn.setAutoCommit(false);
			PreparedStatement preparedStatement =
				getProcessedPreparedStatement(inputValues, params, paramOrder,originalParamNames,paramType
						, conn, sqlQuery,"SQL",axisService.getName());
			int responseCode = -1;
			ResultSet rs = null;
			OMElement result = queryElement.getFirstChildWithName(new QName("result"));
			String prefix = null;
			String resultElementNS = null;
			
			if (isDML) {
				responseCode = preparedStatement.executeUpdate();
			} else {
				rs = preparedStatement.executeQuery();
			}

			if(result == null){
				hasResponse = false;
			}
			
			if(hasResponse){
				resultObj = new Result(result);
				resultElementNS = result.getAttributeValue(new QName("defaultNamespace"));
				if(resultElementNS == null || resultElementNS.trim().length() == 0){
					resultElementNS = axisService.getTargetNamespace();
				}			
				
		        //TODO : need to define a way to get values automatically
				//getDefaultValuesForResultsWrapper(wrapperElementName,rowElementName);
				// check ??
				//columnDefault = result.getAttributeValue(new QName("columnDefault"));
				fac = OMAbstractFactory.getOMFactory();

				//get prefix for namespace
				HashMap namespacePrefixMap = null;
				HashMap queryLevelNamespaceMap = null;
				HashMap queryLevelPrefixMap = null;
				
				if(axisService.getParameterValue(DBConstants.NAMESPACE_PREFIX_MAP)!= null){
					namespacePrefixMap = (HashMap)axisService.getParameterValue(DBConstants.NAMESPACE_PREFIX_MAP);
					queryLevelNamespaceMap = (HashMap)axisService.getParameterValue(DBConstants.QUERYLEVEL_NAMESPACE_MAP);
					queryLevelPrefixMap = (HashMap)axisService.getParameterValue(DBConstants.QUERYLEVEL_PREFIX_MAP);
					
					prefix = (String)namespacePrefixMap.get(resultElementNS);
					if(prefix == null){
						prefix = DBConstants.RESULT_PREFIX+BeanUtil.getUniquePrefix();				
						namespacePrefixMap.put(resultElementNS, prefix);
						queryLevelNamespaceMap.put(new Integer(queryLevel), resultElementNS);
						queryLevelPrefixMap.put(new Integer(queryLevel), prefix);
					}
				}else{
					namespacePrefixMap = new HashMap();
					queryLevelNamespaceMap = new HashMap();
					queryLevelPrefixMap = new HashMap();
					

					prefix = DBConstants.RESULT_PREFIX+BeanUtil.getUniquePrefix();				
					namespacePrefixMap.put(resultElementNS, prefix);
					queryLevelNamespaceMap.put(new Integer(queryLevel), resultElementNS);
					queryLevelPrefixMap.put(new Integer(queryLevel), prefix);
					
					axisService.addParameter(DBConstants.NAMESPACE_PREFIX_MAP,namespacePrefixMap);
					axisService.addParameter(DBConstants.QUERYLEVEL_NAMESPACE_MAP,queryLevelNamespaceMap);
					axisService.addParameter(DBConstants.QUERYLEVEL_PREFIX_MAP,queryLevelPrefixMap);					
				}
				
				omNs = fac.createOMNamespace(resultElementNS, prefix);	

				if(queryLevel > 0){
					String previousNS = (String)queryLevelNamespaceMap.get(new Integer(queryLevel - 1));
					String previousNSPrefix = (String)queryLevelPrefixMap.get(new Integer(queryLevel - 1));
					omNs = fac.createOMNamespace(previousNS, previousNSPrefix);
					resultElement = fac.createOMElement(resultObj.getResultWrapper(), omNs);	
				}else{
					resultElement = fac.createOMElement(resultObj.getResultWrapper(), omNs);
				}			
			}			

			if (isDML) {
				if(hasResponse){
					resultElement
					.setText("Your query executed successfully. Return code from the database was "
							+ responseCode);					
				}
			} else {				
				//put result elements into an array
                Iterator tmpElements = result.getChildElements();
                ArrayList tmpElementsArrayList = new ArrayList();
                while(tmpElements.hasNext()){
                   OMElement element = (OMElement) tmpElements.next();
                   tmpElementsArrayList.add(element);
                }
				
				while (rs.next()) {
					HashMap elementValues = new HashMap();
					int columnCount = rs.getMetaData().getColumnCount();

					if(queryLevel > 0){
						omNs = fac.createOMNamespace(resultElementNS, prefix);	
					}
					
					OMElement row = fac.createOMElement(resultObj.getRowName(), omNs);
					if (resultObj.getRowName() == null) {
						row = resultElement;
					}
					for (int i = 1; i <= columnCount; i++) {
						String columnName = rs.getMetaData().getColumnLabel(i);
						//Some database drivers return column names in different cases
						//while some return in all lower cases, this is a work-a-round
						//to deal with all of those.
						columnName = columnName.toLowerCase();
						String columnValue = rs.getString(columnName);
						elementValues.put(columnName, columnValue);
					}

					//Iterator elements = result.getChildElements();
					boolean useAsParamToNextQuery = false;
					//while (elements.hasNext()) {
					for(int a=0;a < resultObj.getDisplayColumnNames().length;a++){
						//can be one of 'element','attribute','text','link' or 'header'	
						String outPutElementType = resultObj.getElementLocalNames()[a];
						
						if (outPutElementType.equals("element")
								|| outPutElementType.equals("attribute")) {
						    String displayTagName = resultObj.getDisplayColumnNames()[a];
						    String resultSetFieldName = resultObj.getResultSetColumnNames()[a];
						    resultSetFieldName = resultSetFieldName.toLowerCase();

							// This means,the parameter is not part of the
							// resultset. i.e. it is being passed from user's
							// parameters.
						    
							//TODO **********
						    //if (resultSetFieldName == null) {
							//	resultSetFieldName = element.getAttributeValue(new QName("param"));
							//	useAsParamToNextQuery = true;
							//}
							String elementValue;

							if (useAsParamToNextQuery) {
								elementValue = (String) params.get(resultSetFieldName);
								elementValues.put(resultSetFieldName, elementValue);
							} else {
								elementValue = (String) elementValues.get(resultSetFieldName);
							}

							if (outPutElementType.equals("element")) {
								OMElement rowElement = fac.createOMElement(displayTagName, omNs);
								rowElement.addChild(fac.createOMText(rowElement, elementValue));
								row.addChild(rowElement);
							} else if (outPutElementType.equals("attribute")) {
								row.addAttribute(displayTagName, elementValue, omNs);
							}
						} else if (resultObj.getElementLocalNames()[a].equals("call-query")) {
						    OMElement element = (OMElement)tmpElementsArrayList.get(a);
							OMElement rowElement = getRDBMSResult(element, axisService, elementValues,queryLevel+1);
							row.addChild(rowElement);
						}
					}
					if (resultObj.getRowName() != null) {
						resultElement.addChild(row);
					}
				}
			}

		} catch (SQLException e) {
		    log.error("["+axisService.getName()+"] Exception occurred while trying to execute the SQL statement : "+sqlQuery, e);
			throw new AxisFault("["+axisService.getName()+"] Exception occurred while trying to execute the SQL statement : "+sqlQuery, e);
		} 
		
		
		finally {
			try {
				if (conn != null && queryLevel == 0) {
					conn.commit();
					conn.close();
				}
			} catch (SQLException e) {
				log.error("Exception occured while trying to close the DB connection.",e);
				throw new AxisFault(
						"Exception occured while trying to close the DB connection.", e);
			}
		}
		
		//Add all namespaces to root level element
		if(!(queryLevel > 0) && hasResponse){
			HashMap allNamespaces = (HashMap)axisService.getParameterValue(DBConstants.NAMESPACE_PREFIX_MAP);
			Set keySet = allNamespaces.keySet();
			Iterator keySetItr = keySet.iterator();
			while(keySetItr.hasNext()){
				String namespaceURI = (String)keySetItr.next();
				String prefix = (String)allNamespaces.get(namespaceURI);
				resultElement.declareNamespace(namespaceURI, prefix);
			}			
		}
		return resultElement;
	}

	public static OMElement invoke(MessageContext msgContext) throws AxisFault{
		AxisOperation axisOp = msgContext.getAxisOperation();
		AxisService axisService = msgContext.getAxisService();
		OMElement inputMessage = msgContext.getEnvelope().getBody().getFirstElement();
		OMElement operationElement =
			(OMElement) (axisOp.getParameterValue(DBConstants.DB_OPERATION_ELEMENT));

		if(DBConstants.DATASOURCE_TYPE_CSV.equals((String)axisService.getParameterValue(
				DBConstants.DATASOURCE_TYPE))){
			return DBUtils.processCSVQuery(operationElement, axisService, inputMessage);
		}
		else if(DBConstants.DATASOURCE_TYPE_EXCEL.equals((String)axisService.getParameterValue(
				DBConstants.DATASOURCE_TYPE))){
			return processExcelQuery(operationElement, axisService, inputMessage);
		}else if(DBConstants.DATASOURCE_TYPE_LDAP_VIA_JDBC.equals((String)axisService.getParameterValue(
				DBConstants.DATASOURCE_TYPE))){
			LDAPJDBCAdaptor ldapJDBCAdaptor = new LDAPJDBCAdaptor();			
			return  ldapJDBCAdaptor.processQuery(operationElement, axisService, inputMessage);
		}else{//default is RDBMS (JNDI Data source falls under this too)
			return DBUtils.processSQLQuery(operationElement, axisService, inputMessage);
		}		
	}

	/*
	 * Check for CSV & Excel reader library availability
	 */

	private static boolean checkLibraryAvailability(String libraryName) throws AxisFault{
		try{
			if("POI".equals(libraryName)){
				Class.forName("org.apache.poi.poifs.filesystem.POIFSFileSystem");
			}else if("OpenCSV".equals(libraryName)){
				Class.forName("au.com.bytecode.opencsv.CSVReader");
			}
			return true;
			
		}catch(ClassNotFoundException e){
		    log.error("Library not found for : "+libraryName,e);
			throw new AxisFault("Library not found for : "+libraryName,e);
		}
	}
	private static OMElement processExcelQuery(OMElement operationElement, AxisService axisService,
			OMElement inputMessage) throws AxisFault{

		String excelFilePath = (String)axisService.getParameterValue(DBConstants.EXCEL_DATASOURCE);
		log.info("Using Excel file from : "+excelFilePath);		
		
		InputStream dataSourceInputStream = null;
		try 
		{
			//check for POI library
			checkLibraryAvailability("POI");
			
			POIFSFileSystem fs;	
			HSSFWorkbook wb;
			
			if(excelFilePath.startsWith("http://")){
				//This is a url file path
				URL url = new URL(excelFilePath);
				dataSourceInputStream = url.openStream();				
			}else{
				dataSourceInputStream = new FileInputStream(excelFilePath);
			}
			fs = new POIFSFileSystem(dataSourceInputStream);
			wb = new HSSFWorkbook(fs);

			OMElement callQueryElement =
				operationElement.getFirstChildWithName(new QName("call-query"));
			// Find the corresponding query element
			String href = callQueryElement.getAttributeValue(new QName("href"));
			OMElement queryElement;
			if (href != null) {
				HashMap queries =
					(HashMap) axisService.getParameterValue(DBConstants.DB_QUERY_ELEMENTS);
				queryElement = (OMElement) queries.get(href);
			} else {
				queryElement = callQueryElement.getFirstChildWithName(new QName("query"));
			}			
			return getExcelResult(wb,queryElement,axisService);

		}catch (FileNotFoundException e1) {
			log.error("Excel file not fould : "+excelFilePath, e1);
			throw new AxisFault("Excel file not fould : "+excelFilePath);
		}
		catch (IOException e) {
		    log.error("Error loading Excel file : "+excelFilePath, e);
		    throw new AxisFault("Error loading Excel file : "+excelFilePath);
		}finally{
			if(dataSourceInputStream != null){
				try {
					dataSourceInputStream.close();
				} catch (IOException e) {
					log.debug("Error occured while close InputStream for : "+excelFilePath, e);
				}
			}
		}
	}


	private static OMElement getExcelResult(HSSFWorkbook wb
			,OMElement queryElement
			, AxisService axisService)throws AxisFault {
		OMElement resultElement = null;

		//OMElement excelElement = queryElement.getFirstChildWithName(new QName("excel"));
		ExcelQuery excelQuery = new ExcelQuery(axisService,queryElement);

		OMElement result = queryElement.getFirstChildWithName(new QName("result"));
		String wrapperElementName = result.getAttributeValue(new QName("element"));
		String rowElementName = result.getAttributeValue(new QName("rowName"));
		String columnDefalut = result.getAttributeValue(new QName("columnDefault"));

		OMFactory fac = OMAbstractFactory.getOMFactory();
		OMNamespace omNs = fac.createOMNamespace(axisService.getTargetNamespace(), "data");
		resultElement = fac.createOMElement(wrapperElementName, omNs);

		if ( !axisService.isElementFormDefault()) {
			omNs = fac.createOMNamespace("", "data"); 
		}

		//Retrieve the sheet name, if user has set it in configuration file
		String sheetName = excelQuery.getWorkBookName();

		//this is used to skip header columns, the spread sheet
		int startReadingFromRow = excelQuery.getStartingRow();
		if(startReadingFromRow >= 0){
		    //rows start from 0
		    startReadingFromRow = startReadingFromRow -1;
		}
		int maxRowCount = excelQuery.getMaxRowCount();

		HSSFSheet sheet = wb.getSheet(sheetName);
		int rowCount  = sheet.getPhysicalNumberOfRows();

		//If hasHeaders is set to true, we need first row object in later stage.
		HSSFRow firstRow = null;
		if(excelQuery.hasHeaders()){
			//assumption : first row is the header row
			firstRow = sheet.getRow(0);					
		}
		
		int processedRowCount = 0;		
		for (int r = 0; r < rowCount; r++)
		{
			if( r >= startReadingFromRow)
			{
				if(processedRowCount == maxRowCount){
					break;
				}
				HSSFRow hssfRow = sheet.getRow(r);
				OMElement row = fac.createOMElement(rowElementName, omNs);
				if (rowElementName == null) {
					row = resultElement;
				}

				Iterator elements = result.getChildElements();
				while (elements.hasNext()) {
					OMElement element = (OMElement) elements.next();
					if (element.getLocalName().equals("element")) {
						String displayTagName = element.getAttributeValue(new QName("name"));
						String columnValue = element.getAttributeValue(new QName("column"));

						short a = 1;
						short columnNumber;
						if(excelQuery.hasHeaders()){
							//if hasHeaders is set to true, column Names should be specified
							//get the column number using specified name
							columnNumber = getExcelColumnNumber(columnValue,firstRow);
						}else{
							try{
								columnNumber = (short)(Short.valueOf(columnValue).shortValue()- a);	
							}catch(NumberFormatException e){
							    log.error("Column value for element : "+displayTagName +" should be a number.",e);
								throw new AxisFault("Column value for element : "+displayTagName +" should be a number.");
							}
														
						}
						
						HSSFCell hssfCell = hssfRow.getCell(columnNumber);
						String elementValue = "";
						if(hssfCell != null){
							if (HSSFCell.CELL_TYPE_STRING == hssfCell.getCellType()){
								elementValue = hssfCell.getRichStringCellValue().getString();
							}else if(HSSFCell.CELL_TYPE_BLANK == hssfCell.getCellType()){
								//do nothing
							}else if(HSSFCell.CELL_TYPE_BOOLEAN == hssfCell.getCellType()){
								elementValue = String.valueOf(hssfCell.getBooleanCellValue());
							}else if(HSSFCell.CELL_TYPE_FORMULA == hssfCell.getCellType()){
								elementValue = "{formula}";
							}else if(HSSFCell.CELL_TYPE_NUMERIC == hssfCell.getCellType()){
								elementValue = String.valueOf(hssfCell.getNumericCellValue());
							}
						}

						if (columnDefalut == null || columnDefalut.equals("element")) {
							OMElement rowElement = fac.createOMElement(displayTagName, omNs);
							rowElement.addChild(fac.createOMText(rowElement, elementValue));
							row.addChild(rowElement);
						} else if (columnDefalut.equals("attribute")) {
							row.addAttribute(displayTagName, elementValue, omNs);
						}
					}
				}
				if (rowElementName != null) {
					resultElement.addChild(row);
					processedRowCount++;
				}							
			}//end of if( k >= startReadingFromRow)
		}//for (int r = 0; r < rowCount; r++)
		return resultElement;
	}
	
	private static short getExcelColumnNumber(String columnName,HSSFRow headerRow)
	throws AxisFault{
		int noOfCells = headerRow.getPhysicalNumberOfCells();
		short columnNo = -1;
		for(int a = 0;a <noOfCells;a++){
			HSSFCell cell = headerRow.getCell((short)a);
			if(HSSFCell.CELL_TYPE_STRING == cell.getCellType()){
				if(columnName.equals(cell.getRichStringCellValue().getString())){
					columnNo = (short)a;
					break;
				}				
			}else if(HSSFCell.CELL_TYPE_NUMERIC == cell.getCellType()){
				try{
					double columnNameInDouble = Double.valueOf(columnName).doubleValue();
					if(columnNameInDouble == cell.getNumericCellValue()){
						columnNo = (short)a;
						break;
					}
				}catch(NumberFormatException e){
					log.error("Numeric value expected for Column Name : "+columnName, e);
					throw new AxisFault("Numeric value expected for Column Name : "+columnName, e);
				}				
			}
		}
		return columnNo;
	}

	private static OMElement processCSVQuery(
			OMElement operationElement, AxisService axisService,
			OMElement inputMessage) throws AxisFault{

		String csvFilePath = (String) axisService
				.getParameterValue(DBConstants.CSV_DATASOURCE);
		log.info("Using CSV file from : " + csvFilePath);

		CSVReader reader = null;
		InputStreamReader inputStreamReader = null;
		try {
			if (csvFilePath.startsWith("http://")) {
				// This is a url file path
				URL url = new URL(csvFilePath);
				inputStreamReader = new InputStreamReader(url.openStream());
				reader = new CSVReader(inputStreamReader);
			} else {
			    if(csvFilePath.startsWith("."+File.separator)
			            || csvFilePath.startsWith(".."+File.separator)){
			        //this is relative path
			        File csvFile = new File(csvFilePath);
			        csvFilePath = csvFile.getAbsolutePath();
			        log.info("relative file path reference found. Using "+csvFilePath+" as absolute path.");
			    }
				// local file
				reader = new CSVReader(new FileReader(csvFilePath));
			}

			OMElement callQueryElement = operationElement
					.getFirstChildWithName(new QName("call-query"));
			// Find the corresponding query element
			String href = callQueryElement.getAttributeValue(new QName("href"));
			OMElement queryElement;
			if (href != null) {
				HashMap queries = (HashMap) axisService
						.getParameterValue(DBConstants.DB_QUERY_ELEMENTS);
				queryElement = (OMElement) queries.get(href);
			} else {
				queryElement = callQueryElement
						.getFirstChildWithName(new QName("query"));
			}

			try {
				return getCSVResult(reader, queryElement, axisService);
			} catch (AxisFault e) {
			    throw e;
			    /*
				OMElement resultElement = null;
				OMFactory fac = OMAbstractFactory.getOMFactory();
				OMNamespace omNs = fac.createOMNamespace(axisService
						.getTargetNamespace(), "data");
				resultElement = fac.createOMElement("csvoutput", omNs);
				resultElement.setText(e.getMessage());
				return resultElement;
				*/
			}
		} catch (FileNotFoundException e) {
		    log.error("CSV file not found : "+csvFilePath,e);
			throw new AxisFault("CSV file not found : "+csvFilePath,e);
		} catch (MalformedURLException e) {
		    log.error("Incorrect URL : "+csvFilePath,e);
		    throw new AxisFault("Incorrect URL : "+csvFilePath,e);
		} 
		catch (IOException e) {
		    log.error("Error opening stream for : "+csvFilePath, e);
		    throw new AxisFault("Error opening stream for : "+csvFilePath, e);
		}
		finally{
			if(inputStreamReader != null){
				try {
					inputStreamReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}		
		}
	}

	private static OMElement getCSVResult(CSVReader reader,OMElement queryElement, AxisService axisService)
	throws AxisFault {
		OMElement resultElement = null;
		//Extract CSV Query Parameters
		CSVQuery csvQuery = new CSVQuery(axisService);			

		//compute number of rows to read
		int start = csvQuery.getStartingRow();
		int end = csvQuery.getMaxRowCount();
		int noOfRowsToRead = 0;

		if(end == -1){
			noOfRowsToRead = -1;
		}else{
			noOfRowsToRead = end;
		}
		
		if(start >= 0){
		    start = start -1;
		}

		//Extract how result to be formatted
		OMElement result = queryElement.getFirstChildWithName(new QName("result"));
		String wrapperElementName = result.getAttributeValue(new QName("element"));
		String rowElementName = result.getAttributeValue(new QName("rowName"));
		String columnDefalut = result.getAttributeValue(new QName("columnDefault"));

		OMFactory fac = OMAbstractFactory.getOMFactory();
		OMNamespace omNs = fac.createOMNamespace(axisService.getTargetNamespace(), "data");
		resultElement = fac.createOMElement(wrapperElementName, omNs);

		if ( !axisService.isElementFormDefault()) {
			omNs = fac.createOMNamespace("", "data"); 
		}
		String [] nextLine;
		int[] columnNumbersToRead = null;

		//if hasHeader parameter is set, read column names
		if(csvQuery.hasHeaders()){
		//if csvQuery.hasHeaders() is true, what is read is column name
		//if not column number should be read
			try {
				nextLine = reader.readNext();
				columnNumbersToRead = new int[csvQuery.getColumnNames().length];
				//iterate through all column headers, if column name specified in the configuration
				//is found, add that column to columnNumbersToRead array.
				//This will be used when mapping columns to result later
				int index = 0;
				for (int b = 0; b < nextLine.length; b++) {
					//'realColumnValue' is the column name specified in CSV file
					String realColumnName = nextLine[b]; 
					if(csvQuery.isColumnNameMentioned(realColumnName)){
						columnNumbersToRead[index] = b;
						index++;
					}
				}				

			} catch (IOException e) {
			    log.error("Error reading header record",e);
				throw new AxisFault("Error reading header record",e);
			}
		}else{
			//if headers are not present, column headers must be specified as
			// numbers
			columnNumbersToRead = new int[csvQuery.getColumnNames().length];
			String colNo = "";
			for(int a = 0;a < csvQuery.getColumnNames().length;a++){
				try{
					colNo = csvQuery.getColumnNames()[a];
					columnNumbersToRead[a] =  Integer.valueOf(colNo).intValue();	
				}catch (NumberFormatException e){
				    log.error("When headers are set to false, you need to have " +
                            "numeric value for column.Incorrect value found : "+colNo,e);
					throw new AxisFault("When headers are set to false, you need to have " +
							"numeric value for column.Incorrect value found : "+colNo,e);
				}
			}
		}

		try {
			int processedRowCount = 0;
			while ((nextLine = reader.readNext()) != null) {
				//work-a-round for lines with spaces
				//Array size for this line is 1 (i.e no separators/commas)
				if(nextLine.length == 1){
					if(nextLine[0] == null || nextLine[0].trim().equals("")){
						continue;						
					}					
				}
				if(noOfRowsToRead != -1){
					if((processedRowCount+start) == (noOfRowsToRead+start)){
						break;
					}
				}
				if(start > (processedRowCount + 1)){
					processedRowCount++;
					continue;
				}				
				
				HashMap elementValues = new HashMap();
				int columnCount = csvQuery.getColumnNames().length;

				OMElement row = fac.createOMElement(rowElementName, omNs);
				if (rowElementName == null) {
					row = resultElement;
				}
				for (int i = 0; i < columnCount; i++) {
					int columnNo = columnNumbersToRead[i];	
					String columnValue = "";
					try{
						columnValue = nextLine[columnNo];						
					}catch(ArrayIndexOutOfBoundsException e){
						log.info("Reading row : "+(processedRowCount+1) +".No value found " +
								"for column no : "+columnNo+".Empty value will be printed.",e);
					}					
					elementValues.put(new Integer(columnNo), columnValue);
				}

				Iterator elements = result.getChildElements();
				while (elements.hasNext()) {
					OMElement element = (OMElement) elements.next();
					if (element.getLocalName().equals("element")) {
						String displayTagName = element.getAttributeValue(new QName("name"));
						String columnReference =
							element.getAttributeValue(new QName("column"));
						
						int columnRef = 0;
						try{
							columnRef = Integer.valueOf(columnReference).intValue();	
						}catch(NumberFormatException e){
						    log.error("Column value specified "+ columnReference
                                    + " for "+displayTagName+" should be a numeric value.",e);
							throw new AxisFault("Column value specified "+ columnReference
									+ " for "+displayTagName+" should be a numeric value.",e);
						}
						
						//in dbs file column number starts from 1, but inside code it starts from 0
						String elementValue = (String) elementValues.get(new Integer(columnRef - 1));

						if (columnDefalut == null || columnDefalut.equals("element")) {
							OMElement rowElement = fac.createOMElement(displayTagName, omNs);
							rowElement.addChild(fac.createOMText(rowElement, elementValue));
							row.addChild(rowElement);
						} else if (columnDefalut.equals("attribute")) {
							row.addAttribute(displayTagName, elementValue, omNs);
						}
					}
					//we cannot support this with single data source
					//else if (element.getLocalName().equals("call-query")) {
					//	OMElement rowElement = getResult(element, axisService, elementValues);
					//	row.addChild(rowElement);
					//}
				}
				if (rowElementName != null) {
					resultElement.addChild(row);
				}
				processedRowCount++;
			}
		}catch (IOException e) {
			log.error("Error reading CSV file.",e);
			throw new AxisFault("Error reading CSV file.",e);
		}

		return resultElement;
	}


	public static Connection createConnection(AxisService axisService,Config config) throws AxisFault {
		try{
			log.debug("Getting database connection for "+axisService.getName());
			Connection conn = null;			
			
			if(config.getPropertyValue(DBConstants.MIN_POOL_SIZE) != null 
					|| config.getPropertyValue(DBConstants.MAX_POOL_SIZE) != null){
				//user has set connection pool size(s). Get connection from pooling manager
				DataSource dataSource =
					(DataSource)axisService.getParameterValue(DBConstants.DB_CONNECTION);
				conn = dataSource.getConnection();				
			}else{
				//Try to load the JDBC driver class. If class not found throw an error.
				Class.forName(config.getPropertyValue(DBConstants.DRIVER)).newInstance();
				Properties props = new Properties();
				props.put("user", config.getPropertyValue(DBConstants.USER));
				props.put("password", config.getPropertyValue(DBConstants.PASSWORD));				
				conn = DriverManager.getConnection((String) config.getPropertyValue(DBConstants.PROTOCOL), props);
			}
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

	public static DBCPConnectionManager initializeDBConnectionManager(
			String serviceName, Config config) throws AxisFault {
		log.debug("Getting database connection for " + serviceName);
		DBCPConnectionManager dbcpConnectionManager = null;
		dbcpConnectionManager = new DBCPConnectionManager(config);
		return dbcpConnectionManager;
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
	private static Timestamp getTimestamp(String value,String paramName)
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
	
	private static Time getTime(String value, String paramName)
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
}
