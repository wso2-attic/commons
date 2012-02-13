package org.wso2.ws.dataservice.beans;

/**
 * 
 * Object for query/param element
 *
 */
		
public class Param {
	private String name;
	private String sqlType;
	private String type;
	private int ordinal;
	
	String columnName;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSqlType() {
		return sqlType;
	}
	public void setSqlType(String sqlType) {
		this.sqlType = sqlType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public int getOrdinal() {
		return ordinal;
	}
	public void setOrdinal(int ordinal) {
		this.ordinal = ordinal;
	}
	
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public Param(String name,String sqlType,String type,String ordinal){
		this.name = name;
		this.sqlType = sqlType;
		this.type = type;
		this.ordinal = Integer.valueOf(ordinal).intValue();
	}
	public Param(String name,String sqlType,String type){
		this.name = name;
		this.sqlType = sqlType;
		this.type = type;
	}
	public Param(){		
	}
	
}