package org.wso2.wsas.client;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.apache.ws.axis.Axis1CalculatorStub;
import org.jaxen.function.SubstringAfterFunction;

/* This is a sample client to verify the java bean 
 * handling capability of WSO2 WSAS. 
 * Written by Charitha Kankanamge 
 */

public class Axis1client {

public static void main(String args[])throws AxisFault{
	
	Axis1CalculatorStub stub = new Axis1CalculatorStub();
	Axis1CalculatorStub.Addition addrequest = new Axis1CalculatorStub.Addition();
	Axis1CalculatorStub.Multiplication multiplyrequest = new Axis1CalculatorStub.Multiplication();
	Axis1CalculatorStub.Division divisionrequest = new Axis1CalculatorStub.Division();
	Axis1CalculatorStub.Subtraction subtractrequest = new Axis1CalculatorStub.Subtraction();
	
	//ToDo: Read the inputs from console
	addrequest.setX(40);
	addrequest.setY(20);
	
	multiplyrequest.setA(50);
	multiplyrequest.setB(60);
	
	divisionrequest.setM(84.0);
	divisionrequest.setN(4.0);
	
	subtractrequest.setC(32);
	subtractrequest.setD(23);
	
	try {
		Axis1CalculatorStub.AdditionResponse addresponse = stub.addition(addrequest);
		System.out.println(addresponse.getAdditionReturn());
		Axis1CalculatorStub.MultiplicationResponse multiplyresponse = stub.multiplication(multiplyrequest);
		System.out.println(multiplyresponse.getMultiplicationReturn());
		Axis1CalculatorStub.DivisionResponse divisionresponse = stub.division(divisionrequest);
		System.out.println(divisionresponse.getDivisionReturn());
		Axis1CalculatorStub.SubtractionResponse subtractresponse = stub.subtraction(subtractrequest);
		System.out.println(subtractresponse.getSubtractionReturn());
	} catch (RemoteException e) {
		e.printStackTrace();
	}
	
	}
}