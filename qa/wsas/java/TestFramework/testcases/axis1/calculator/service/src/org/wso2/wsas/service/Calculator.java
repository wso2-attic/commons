package org.wso2.wsas.service;

/*Sample service to test Axis1 support of WSO2 WSAS
 * Written by Charitha Kankanamge
 */


public class Calculator{
	
	public int addition(int x, int y){
		return x+y;
	}
	
	public int multiplication(int a, int b){
		return a*b;
	}
	
	public int subtraction(int c, int d){
		return c-d;
	}
	
	public double division(double m, double n){
		return m/n;
	}
}