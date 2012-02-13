/* This is a sample service to test Reliable messaging in WSO2 WSAS
 * Written by Charitha Kankanamge
 */

package org.wso2.wsas.service;
public class SandeshaService{
	
	//Oneway messaging
	public void Ping(String s){
		System.out.println("*****Received the ping request*****");
	}
	
	//Request-Reply
	public int Add(int a, int b){
		return a+b;
	}
	
	//Ordered delivery
	
	public int echoInt(int x){
		System.out.println(x);
		return x;
	}

}