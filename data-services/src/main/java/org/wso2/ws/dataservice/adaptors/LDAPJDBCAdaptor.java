package org.wso2.ws.dataservice.adaptors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.databinding.utils.BeanUtil;
import org.apache.axis2.description.AxisService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.ws.dataservice.DBConstants;
import org.wso2.ws.dataservice.beans.Result;

public class LDAPJDBCAdaptor extends DataSourceAdaptor {
	private static final Log log = LogFactory.getLog(LDAPJDBCAdaptor.class);

	public OMElement processQuery(OMElement operationElement,
			AxisService axisService, OMElement inputMessage) {
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

			resultElement =
					getSelectResult(queryElement, inputs, params, paramOrder
							,originalParamNames,paramType, axisService, false,queryLevel);

		} catch (Exception e) {
			log.error("Error occured", e);
			return generateErrorResponse(axisService.getTargetNamespace(), (AxisFault)e);
	    }
		return resultElement;
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
		String dataSourceType = (String)axisService.getParameterValue(DBConstants.DATASOURCE_TYPE);
		try {
			//Parameter dbConnectionParam = axisService.getParameter(DBConstants.DB_CONNECTION);  
			//if(dbConnectionParam == null){
			//	throw new AxisFault("["+axisService.getName()+"] Database connection not found in Axis Configuration");
			//}
			//conn = (Connection)dbConnectionParam.getValue();
			conn = checkDBConnectionStatus(axisService, conn);		
			if(!(dataSourceType.equalsIgnoreCase(DBConstants.DATASOURCE_TYPE_LDAP_VIA_JDBC))){
				conn.setAutoCommit(false);
			}
			
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
					if(!(dataSourceType.equalsIgnoreCase(DBConstants.DATASOURCE_TYPE_LDAP_VIA_JDBC))){
						conn.commit();
					}			
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
	
	

	
}
