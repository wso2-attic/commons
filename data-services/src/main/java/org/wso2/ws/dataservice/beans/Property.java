package org.wso2.ws.dataservice.beans;

/*
 * Represents property elements in config section.
 * eg :
 *   <config>
 *       <property name="org.wso2.ws.dataservice.driver">com.mysql.jdbc.Driver</property>
 *       <property name="org.wso2.ws.dataservice.protocol">jdbc:mysql://localhost:3306/classicmodels</property>
 *       <property name="org.wso2.ws.dataservice.user">sumedha</property>
 *       <property name="org.wso2.ws.dataservice.password">sumedha</property>
 *   </config>  
 */
public class Property {
	private String name;
	private String value;
	
	public Property(String name, String value) {
		this.name = name;
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
