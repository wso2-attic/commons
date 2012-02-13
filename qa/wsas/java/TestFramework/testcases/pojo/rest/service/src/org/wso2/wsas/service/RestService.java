package org.wso2.wsas.service;

public class RestService {
	
	public String echoString(String param1, String param2) {
		
		return param1 + " " + param2;
	}
	
	public int addNumbers(int x, int y){
		return x+y;
	}
	

}
