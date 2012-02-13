package org.wso2.esb.service;

public class Adding {
	//Request-Reply
	public static int add(int x, int y){
				
		int z= x + y;
		System.out.println("The response is :" + z);
		return z;
	}
	
	//Oneway messaging
	public void Ping(String s){
		System.out.println("*****Received the ping request*****");
	}
}
