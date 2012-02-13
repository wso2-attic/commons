package org.wso2.ws.dataservice.beans;

import java.util.StringTokenizer;

import org.apache.axis2.description.AxisService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.ws.dataservice.DBConstants;

public class NonSQLQuery {
	protected boolean hasHeaders = false;
	protected int startingRow;
	protected int maxRowCount;
	protected String[] columnNames; 
	protected String[] columnOrder;
	protected String queryType = "";
	private static final Log log = LogFactory.getLog(NonSQLQuery.class);

	public boolean hasHeaders() {
		return hasHeaders;
	}
	public void setHasHeaders(String value) {
		if(value == null || value.trim().length() == 0){
			this.hasHeaders = false;
		}else{
			this.startingRow = Integer.valueOf(value).intValue();
		}		
	}
	public int getStartingRow() {
		return startingRow;
	}

	public void setStartingRow(String value) {
		if(value == null || value.trim().length() == 0){
			this.startingRow = 0;
		}else{
			this.startingRow = Integer.valueOf(value).intValue();
		}		
	}
	public int getMaxRowCount() {
		return maxRowCount;
	}
	public void setMaxRowCount(String value) {
		if(value == null || value.trim().length() == 0){
			this.maxRowCount = -1;
		}else{
			this.maxRowCount = Integer.valueOf(value).intValue();
		}		
	}
	
	public String[] getColumnNames() {
		return columnNames;
	}
	public void setColumnNames(String[] columnNamesArray) {
		this.columnNames = columnNamesArray;
	}
	public String[] getColumnOrder() {
		return columnOrder;
	}
	public void setColumnOrder(String[] columnOrderArray) {
		this.columnOrder = columnOrderArray;
	}
	
	public NonSQLQuery(AxisService axisService,String queryType){
		Object value = "";
		value = axisService.getParameterValue(queryType + "_" + DBConstants.Query.HAS_HEADER);
		if( value != null){
			this.hasHeaders = Boolean.valueOf((String)value).booleanValue();	
		}else{
			this.hasHeaders = false; //default value
		}

		value = axisService.getParameterValue(queryType + "_" + DBConstants.Query.STARTING_ROW);
		if(value != null){
		    try{
		        this.startingRow = Integer.valueOf((String)value).intValue();
		    }catch(NumberFormatException e){
		        log.info("Found non-numeric value for "
		                +queryType + "_" + DBConstants.Query.STARTING_ROW+ " : "+value);
		        this.startingRow = 0;
		    }			
		}else{
			this.startingRow = 0; //default value
		}

		value = axisService.getParameterValue(queryType + "_" + DBConstants.Query.MAX_ROW_COUNT);
		if(value != null){
		    try{
		        this.maxRowCount = Integer.valueOf((String)value).intValue();
		    }catch(NumberFormatException e){
                log.info("Found non-numeric value for "
                        +queryType + "_" + DBConstants.Query.MAX_ROW_COUNT+ " : "+value);		        
		        this.maxRowCount = -1;
		    }
			
		}else{
			this.maxRowCount = -1; //default value
		}
		
		value = axisService.getParameterValue(queryType + "_" + DBConstants.Query.COLUMNS);
		if(value != null){
			String columnNames = (String)value;
			//Assumption : , will not be used as text in Column header :-) 
			StringTokenizer stringTokenizer = new StringTokenizer(columnNames,",");
			this.columnNames = new String[stringTokenizer.countTokens()];
			int tokenCount = stringTokenizer.countTokens();
			for(int a = 0; a < tokenCount;a++){
				this.columnNames[a] = (String)stringTokenizer.nextElement();
			}			
		}
		
		value = axisService.getParameterValue(queryType + "_" + DBConstants.Query.CSV_COLUMN_ORDER);
		if(value != null){
			String columnOrder = (String)value;
			StringTokenizer stringTokenizer = new StringTokenizer(columnOrder,",");
			this.columnOrder = new String[stringTokenizer.countTokens()];
			int tokenCount = stringTokenizer.countTokens();
			for(int a = 0; a < tokenCount;a++){
				this.columnOrder[a] = (String)stringTokenizer.nextElement();
			}			
		}
		
	}
	
	public NonSQLQuery(){
		
	}
}
