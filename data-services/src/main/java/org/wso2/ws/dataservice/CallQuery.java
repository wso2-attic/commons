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

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.java2wsdl.Java2WSDLConstants;
import org.wso2.ws.dataservice.beans.Param;

public class CallQuery {

    private ArrayList inputParms;
    private ArrayList outputParms;
    private String sql;
    private String elementName;
    private String rawName;
    private ArrayList queryRefList;
    private String defaultNamespace;
    private String columnDefault;
    private String nsPrefix;


    public CallQuery() {
        inputParms = new ArrayList();
        outputParms = new ArrayList();
        queryRefList = new ArrayList();
    }

    public ArrayList getInputParms() {
        return inputParms;
    }

    public void addInputParms(Param param) {
        inputParms.add(param);
    }

    public ArrayList getOutputParms() {
        return outputParms;
    }

    public void addOutputParms(Param param) {
        outputParms.add(param);
    }


    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }


    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getRawName() {
        return rawName;
    }

    public void setRawName(String rawName) {
        this.rawName = rawName;
    }


    public ArrayList getQueryRefList() {
        return queryRefList;
    }
    
    

    public String getDefaultNamespace() {
		return defaultNamespace;
	}

	public void setDefaultNamespace(String defaultNamespace) {
		this.defaultNamespace = defaultNamespace;
	}

	public String getColumnDefault() {
		return columnDefault;
	}

	public void setColumnDefault(String columnDefault) {
		this.columnDefault = columnDefault;
	}	

	public String getNsPrefix() {
		return nsPrefix;
	}

	public void setNsPrefix(String nsPrefix) {
		this.nsPrefix = nsPrefix;
	}

	public void populateCallQuery(OMElement queryElement,AxisService axisService) throws AxisFault{
        Iterator inputsIterator = queryElement.getChildrenWithName(new QName("param"));
        while (inputsIterator.hasNext()) {
            OMElement input = (OMElement) inputsIterator.next();
            String name = input.getAttributeValue(new QName("name"));
            String sqlType = input.getAttributeValue(new QName("sqlType"));
            String dataType = DBDeployer.getJavaTypeFromSQLType(sqlType);
            String paramType = input.getAttributeValue(new QName("type"));
            String ordinal = input.getAttributeValue(new QName("ordinal"));
            //QueryElement inputElement = new QueryElement();
            Param param = new Param();
            param.setSqlType(dataType);
            
            //set Parameter type
            if(paramType != null && paramType.trim().length() > 0){
            	param.setType(paramType);
            }else{
            	param.setType("IN");
            }
            
            //if ordinal is set, set it
            if(ordinal != null && ordinal.trim().length() > 0){
            	try{
            		param.setOrdinal(Integer.valueOf(ordinal).intValue());	
            	}catch(NumberFormatException e){
            		throw new AxisFault("["+axisService.getName()
            				+"] Non-numeric value("+ordinal
            				+") found for ordinal for parameter :"+name);
            	}
            	
            }
            
            //inputElement.setClassType(type);
            
            //TODO : complete
            //String withParamName = getExternalParameterName(queryElement, paramsPerCallQueryMap, name);
            //if(withParamName != null){
            //    inputElement.setName(withParamName);
            //}else{
                //inputElement.setName(name);
                param.setName(name);
            //}
            addInputParms(param);
        }
        OMElement sqlQuery = queryElement.getFirstChildWithName(new QName("sql"));
        if (sqlQuery != null) {
            sql = sqlQuery.getText();
        }

        OMElement resultElement = queryElement.getFirstChildWithName(new QName("result"));
        if (resultElement != null) {
            elementName = resultElement.getAttributeValue(new QName("element"));      
            rawName = resultElement.getAttributeValue(new QName("rowName"));
            defaultNamespace  = resultElement.getAttributeValue(new QName("defaultNamespace"));
            //if not mentioned, set the default one
            if(defaultNamespace == null){
            	defaultNamespace = axisService.getTargetNamespace();
            	nsPrefix = axisService.getTargetNamespacePrefix();
            }else{
            	//namespace prefix must be set
            	int index = rawName.indexOf(":");
				if(index > -1){
            		nsPrefix = rawName.substring(1, index);
            		rawName = rawName.substring(index+1);
            	}else{
            		//TODO : throw exception
            		nsPrefix = Java2WSDLConstants.AXIS2_NAMESPACE_PREFIX;
            	}
            }
            columnDefault  = resultElement.getAttributeValue(new QName("columnDefault"));      


            
            //processing elements
            Iterator elements = resultElement.getChildrenWithName(new QName("element"));
            while (elements.hasNext()) {
                OMElement element = (OMElement) elements.next();
                Param param = new Param();
                param.setSqlType("java.lang.String");
                param.setColumnName(element.getAttributeValue(new QName("column")));
                param.setName(element.getAttributeValue(new QName("name")));
                addOutputParms(param);
            }
            Iterator queryElements = resultElement.getChildrenWithName(new QName("call-query"));
            while (queryElements.hasNext()) {
                OMElement query = (OMElement) queryElements.next();
                queryRefList.add(query.getAttributeValue(new QName("href")));
            }
        }else{//resultElement == null
        	//for services with no return types, there will not be a result
        	//element.
           	defaultNamespace = axisService.getTargetNamespace();
           	nsPrefix = axisService.getTargetNamespacePrefix();
        }

    }

	/*
    private String getExternalParameterName(OMElement queryElement,
            HashMap paramsPerCallQueryMap, String name) {
        String withParamName = null;
        String queryId = queryElement.getAttributeValue(new QName("id"));
        HashMap callQueryParamNames = (HashMap)paramsPerCallQueryMap.get(queryId);
        if(callQueryParamNames != null){
            withParamName = (String) callQueryParamNames.get(name);
        }
        return withParamName;
    }*/
    
    
    
}
