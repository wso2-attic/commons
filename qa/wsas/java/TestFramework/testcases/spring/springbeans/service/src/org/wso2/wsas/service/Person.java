package org.wso2.wsas.service;
/* This is a sample service to verify spring bean 
 * handling capability of WSO2 WSAS.
 * Written by Charitha Kankanamge 
 */

public class Person {
	
	private String name;
	private String address;
	private int age;

	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

}
