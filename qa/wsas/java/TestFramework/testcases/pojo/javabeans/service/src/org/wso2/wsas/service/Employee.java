package org.wso2.wsas.service;
/* This is a sample service to verify the java bean 
 * handling capability of WSO2 WSAS. This service has to be deployed on 
 * Application scope.
 * Written by Charitha Kankanamge 
 */

public class Employee {
	
	private String name;
	private String dept;
	private String empid;
	private int age;
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getEmpid() {
		return empid;
	}
	public void setEmpid(String empid) {
		this.empid = empid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

}
