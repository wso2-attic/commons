package org.wso2.ws.dataservice.beans;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.namespace.QName;
import org.apache.axiom.om.OMElement;

public class Query {
	private String id;
	private Param[] params;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Param[] getParams() {
		return params;
	}
	public void setParams(Param[] paramsArray) {
		params = new Param[paramsArray.length];
		for (int a = 0; a < paramsArray.length; a++) {
			params[a] = paramsArray[a];
		}
	}
	
	public Param getParam(String name){
		for (int a = 0; a < this.params.length; a++) {
			if(params[a].getName().equals(name)){
				return params[a]; 
			}
		}
		return null;
	}
	
	public Query(Iterator paramItr){
		Param param;
		ArrayList paramList = new ArrayList();		
		int ordinal = 0; 
		while (paramItr.hasNext()) {
			ordinal++;
			OMElement paramElement = (OMElement) paramItr.next();

			//start: work-a-round to maintain backward compatibility
			String userSetOrdinalValue = paramElement.getAttributeValue(new QName("ordinal"));
			if(userSetOrdinalValue == null || userSetOrdinalValue.trim().length() == 0){
				userSetOrdinalValue = String.valueOf(ordinal);
			}
			//end: work-a-round to maintain backward compatibility
			
			param = new Param(paramElement.getAttributeValue(new QName("name")),
						paramElement.getAttributeValue(new QName("sqlType")),
						paramElement.getAttributeValue(new QName("type")),
						userSetOrdinalValue);
			paramList.add(param);
		}
		try{
			Param[] params = new Param[paramList.size()];
			paramList.toArray(params);
			//Param[] params = (Param[])paramList.toArray(); 
			setParams(params);	
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	

}
