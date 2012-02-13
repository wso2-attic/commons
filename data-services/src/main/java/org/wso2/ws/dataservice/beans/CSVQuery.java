package org.wso2.ws.dataservice.beans;

import org.apache.axis2.description.AxisService;
import org.wso2.ws.dataservice.DBConstants;


public class CSVQuery extends NonSQLQuery {
	String columnSeperator = ",";
	

	public String getColumnSeperator() {
		return columnSeperator;
	}

	public void setColumnSeperator(String columnSeperator) {
		this.columnSeperator = columnSeperator;
	}
	
	public CSVQuery(AxisService axisService){		
		super(axisService,DBConstants.Query.CSV);
		
		Object value = axisService.getParameterValue(DBConstants.Query.CSV_COLUMN_SEPERATOR);
		if( value != null){
			this.columnSeperator = (String)value;	
		}else{
			this.columnSeperator = ","; //default value
		}
	}
	
	/*
	 * Iterates through the columnNames mentioned in the configuration file.
	 * <property name="csv_columns">ID,Name,Price</property>
	 * 
	 *  Returns true, if passed 'columnName' is found within this column names
	 *  false, otherwise
	 */	
	public boolean isColumnNameMentioned(String columnName){
		for(int a = 0;a < this.columnNames.length;a++){
			if(this.columnNames[a].equals(columnName)){
				return true;
			}
		}
		return false;
	}
}
