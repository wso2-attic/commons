package org.wso2.wsas.client;

import java.math.BigDecimal;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;

import org.apache.axis2.AxisFault;
import org.apache.axis2.databinding.types.UnsignedByte;

import org.tempuri.BaseDataTypesDocLitWServiceStub;

import com.microsoft.schemas._2003._10.serialization._char;



/* This is a sample client to test Base Data Types defined in a
 * Document - literal WSDL.   
 * Written by Charitha Kankanamge 
 */

public class BaseDataTypesDocLitWClient {
	
	static BaseDataTypesDocLitWServiceStub stub;
	
	private static void echoBool(BaseDataTypesDocLitWServiceStub stub, boolean b) {
        try {

            if (stub.RetBool(b) == b){
            System.out.println("Boolean echo test is passed with " + b);}
            else{
            	System.out.println("Boolean test fails!!");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            
        }
    }
	
	private static void echoByte(BaseDataTypesDocLitWServiceStub stub, UnsignedByte b) {
        try {

            if (stub.RetByte(b).byteValue() == b.byteValue()){
            System.out.println("UnsignedByte echo test is passed with " + b);}
            else{
            	System.out.println("UnsignedByte test fails with " + b +"!!");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            
        }
    }
	
	private static void echoSByte(BaseDataTypesDocLitWServiceStub stub, Byte s) {
        try {

            if (stub.RetSByte(s) == s){
            System.out.println("Signed Byte echo test is passed with " + s);}
            else{
            	System.out.println("Signed Byte test fails with " +s+"!!");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            
        }
    }
	
	private static void echoDecimal(BaseDataTypesDocLitWServiceStub stub, BigDecimal d) {
        try {

            if (stub.RetDecimal(d).doubleValue() == d.doubleValue()){
            System.out.println("Decimal echo test is passed with " + d);}
            else{
            	System.out.println("Decimal test fails with " +d+"!!");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            
        }
    }
	
	private static void echoFloat(BaseDataTypesDocLitWServiceStub stub, Float f) {
        try {

            if (stub.RetFloat(f) == f){
            System.out.println("Float echo test is passed with " + f);}
            else{
            	System.out.println("Float test fails with " +f+"!!");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            
        }
    }
	
	private static void echoDouble(BaseDataTypesDocLitWServiceStub stub, Double d) {
        try {

            if (stub.RetDouble(d) == d){
            System.out.println("Double echo test is passed with " + d);}
            else{
            	System.out.println("Double test fails with " +d+"!!");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            
        }
    }
	
	private static void echoInt(BaseDataTypesDocLitWServiceStub stub, int i) {
        try {

            if (stub.RetInt(i) == i){
            System.out.println("Integer echo test is passed with " + i);}
            else{
            	System.out.println("Integer test fails with " +i+"!!");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            
        }
    }
	
	private static void echoShort(BaseDataTypesDocLitWServiceStub stub, Short s) {
        try {

            if (stub.RetShort(s) == s){
            System.out.println("Short echo test is passed with " + s);}
            else{
            	System.out.println("Short test fails with " +s+"!!");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            
        }
    }
	
	private static void echoChar(BaseDataTypesDocLitWServiceStub stub, char c) {
        try {
        	
        	_char aChar = new _char();
        	aChar.set_char(c);  	
            if (stub.RetChar(aChar).get_char() == c){
            System.out.println("Char echo test is passed with " + c);}
            else{
            	System.out.println("Char test fails with " +c+"!!");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            
        }
    }
	
	private static void echoLong(BaseDataTypesDocLitWServiceStub stub, Long l) {
        try {
        	
        	
            if (stub.RetLong(l) == l){
            System.out.println("Long echo test is passed with " + l);}
            else{
            	System.out.println("Long test fails with " +l+"!!");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            
        }
    }
	
	private static void echoQName(BaseDataTypesDocLitWServiceStub stub, QName q) {
        try {
        	
        	
            if (stub.RetQName(q) == q){
            System.out.println("QName echo test is passed with " + q);}
            else{
            	System.out.println("QName test fails with " +q+"!!");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            
        }
    }
	
	
	
	public static void main(String[] args)throws AxisFault{
		
		stub=new BaseDataTypesDocLitWServiceStub();
		
		echoBool(stub,true);
		echoBool(stub,false);
		
		echoByte(stub,new UnsignedByte(0));
		echoByte(stub,new UnsignedByte(255));
		echoByte(stub,new UnsignedByte(1));
		
		echoSByte(stub,new Byte("-1").byteValue());
		echoSByte(stub,new Byte("0").byteValue());
		echoSByte(stub,new Byte("1").byteValue());
		echoSByte(stub,new Byte("50").byteValue());
		echoSByte(stub,new Byte("-50").byteValue());
		echoSByte(stub,Byte.MAX_VALUE);
		echoSByte(stub,Byte.MIN_VALUE);
		
		echoDecimal(stub,new BigDecimal("-79228162514264337593543950335"));
		echoDecimal(stub,new BigDecimal("79228162514264337593543950335"));
		echoDecimal(stub,new BigDecimal("0"));
		echoDecimal(stub,new BigDecimal("1"));
		echoDecimal(stub,new BigDecimal("0.0"));
		echoDecimal(stub,new BigDecimal("-0.0"));
		echoDecimal(stub,new BigDecimal("1.0"));
		echoDecimal(stub,new BigDecimal("-1.0"));
		echoDecimal(stub,new BigDecimal("0.61803398874989484820458683"));
		echoDecimal(stub,new BigDecimal("-9223372036854775808"));
		//echoDecimal(stub,new BigDecimal("Math.PI"));
		//echoDecimal(stub,new BigDecimal("-Math.PI"));
		echoDecimal(stub,new BigDecimal("1289756.7765342"));
		echoDecimal(stub,new BigDecimal("234"));
		echoDecimal(stub,new BigDecimal("-1.56"));
		
		echoFloat(stub, Float.MAX_VALUE);
		echoFloat(stub, Float.MIN_VALUE);
		echoFloat(stub, Float.NEGATIVE_INFINITY);
		echoFloat(stub, Float.POSITIVE_INFINITY);
		echoFloat(stub, Float.parseFloat("0.0"));
		echoFloat(stub, Float.parseFloat("-0.0"));
		echoFloat(stub, Float.parseFloat("1.0"));
		echoFloat(stub, Float.parseFloat("-1.0"));
		echoFloat(stub, Float.parseFloat("0.61803398874989484820458683"));
		echoFloat(stub, Float.parseFloat("-9223372036854775808"));
		echoFloat(stub, new Float(Math.PI).floatValue());
		echoFloat(stub, new Float(-Math.PI).floatValue());
		
		echoDouble(stub, new Double("3.40282347E+38"));
		echoDouble(stub, new Double("-3.40282347E+38"));
		echoDouble(stub, new Double("0.0"));
		echoDouble(stub, new Double("-0.0"));
		echoDouble(stub, new Double("1.0"));
		echoDouble(stub, new Double("-1.0"));
		echoDouble(stub, new Double("0.61803398874989484820458683"));
		echoDouble(stub, new Double("-1.4142135623730952"));
		echoDouble(stub, new Double("1.4142135623730952"));
		echoDouble(stub, new Double("-9223372036854775808"));
		echoDouble(stub, new Double(Math.PI));
		echoDouble(stub, new Double(-Math.PI));
		echoDouble(stub, new Double(Double.NEGATIVE_INFINITY));
		echoDouble(stub, new Double(Double.POSITIVE_INFINITY));
		
		echoInt(stub,Integer.MAX_VALUE);
		echoInt(stub,Integer.MIN_VALUE);
		echoInt(stub,0);
		echoInt(stub,-1);
		echoInt(stub,1);
		echoInt(stub,50);
		echoInt(stub,-50);
		
		echoShort(stub,Short.MAX_VALUE);
		echoShort(stub,Short.MIN_VALUE);
		echoShort(stub,(short)0);
		echoShort(stub,(short)-1);
		echoShort(stub,(short)1);
		echoShort(stub,(short)50);
		echoShort(stub,(short)-50);
		
		echoChar(stub,' ');
		echoChar(stub,'&');
		echoChar(stub,'\t');
		echoChar(stub,'\n');
		echoChar(stub,'<');
		echoChar(stub,'>');
		echoChar(stub,'a');
		echoChar(stub,'b');
		echoChar(stub,Character.MAX_VALUE);
		echoChar(stub,Character.MIN_VALUE);
		echoChar(stub,(char)10);
		echoChar(stub,(char)23);
		
		echoLong(stub, (long)-1);
		echoLong(stub, Long.MAX_VALUE);
		echoLong(stub, Long.MIN_VALUE);
		echoLong(stub, (long)0);
		echoLong(stub, (long)50);
		echoLong(stub, (long)-50);
		
		echoQName(stub, new QName("http://qa.wso2.org", "localname", "qans"));
		echoQName(stub, new QName("urn:abcd:xyz", "localname", "d11"));
		
		
		
		
	
		
	}

}
