package org.wso2.ws.dataservice.beans;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.description.AxisService;
import org.wso2.ws.dataservice.DBConstants;


public class ExcelQuery extends NonSQLQuery {
	String workBookName = ",";
	
	public String getWorkBookName() {
		return workBookName;
	}
	public void setWorkBookName(String workBookName) {
		this.workBookName = workBookName;
	}


	public ExcelQuery(AxisService axisService,OMElement query){
		//unlike CSV query properties, EXCEL query properties are defined @ query level.
		//Reason : single Excel document can have multiple sheets & we can have different operations
		//per each sheet.
		
		//extracting query properties from query/excel element
		OMElement excelEle = query.getFirstChildWithName(new QName(DBConstants.Query.EXCEL));
		OMElement workBookNameEle = excelEle.getFirstChildWithName(new QName(DBConstants.Query.EXCEL_WORKBOOK_NAME));
		OMElement hasHeaderEle = excelEle.getFirstChildWithName(new QName(DBConstants.Query.HAS_HEADER));
		OMElement startingRowEle = excelEle.getFirstChildWithName(new QName(DBConstants.Query.STARTING_ROW));
		OMElement maxRowCountEle = excelEle.getFirstChildWithName(new QName(DBConstants.Query.MAX_ROW_COUNT));
		
		if(workBookNameEle != null && workBookNameEle.getText() != null && workBookNameEle.getText().trim().length() > 0){
			this.workBookName = workBookNameEle.getText();
		}else{
			this.workBookName = "Sheet1"; //default name
		}
		
		if(hasHeaderEle != null && hasHeaderEle.getText() != null && hasHeaderEle.getText().trim().length() > 0){
			this.hasHeaders = Boolean.valueOf(hasHeaderEle.getText()).booleanValue();
		}else{
			this.hasHeaders = false; //default name
		}
		
		if(startingRowEle != null && startingRowEle.getText() != null && startingRowEle.getText().trim().length() > 0){
			this.startingRow = Integer.valueOf(startingRowEle.getText()).intValue();
		}else{
			this.startingRow = 1;//default name
		}

		if(maxRowCountEle != null && maxRowCountEle.getText() != null && maxRowCountEle.getText().trim().length() > 0){
			this.maxRowCount = Integer.valueOf(maxRowCountEle.getText()).intValue();
		}else{
			this.maxRowCount = -1; //default name
		}
		
	}
}
