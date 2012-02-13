package org;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;

public class AxiomReader {

public static void main(String[] args) throws FileNotFoundException, XMLStreamException {

	EditFile(args[0],args[1],args[2],args[3]);
	
}

public static void EditFile(String FileName,String DBurl,String UserName,String Password) throws FileNotFoundException, XMLStreamException
{
	
	String protocol	= "org.wso2.ws.dataservice.protocol";
	String user		= "org.wso2.ws.dataservice.user";
	String Pwd	= "org.wso2.ws.dataservice.password";
	
	FileInputStream fis = new FileInputStream(FileName);
	StAXOMBuilder builder = new StAXOMBuilder(fis);
	  OMElement documentElement = builder.getDocumentElement();	  
	   Iterator iterator = documentElement.getFirstElement().getChildElements();
	   while(iterator.hasNext()){
		   OMElement ome = (OMElement) iterator.next();
		   String AttValue = ome.getAttributeValue(new QName("","name"));

		   if(AttValue.equals(protocol))
		   {
			   ome.setText(DBurl);
		   }
		   else if (AttValue.equals(user))
		   {
			   ome.setText(UserName);
			}
		   else if (AttValue.equals(Pwd))
		   {
			   ome.setText(Password);
			}
		   
		   FileOutputStream Fout= new FileOutputStream(FileName);
		   documentElement.serialize(Fout);
		   
		   
		   try {
			   	Fout.close();
		   		} 
		   catch (IOException e) {
			   	e.printStackTrace();
		}
	}
	   	System.out.println(FileName+" file Updated ");
  }
/*	public static void ReadFile(String FilePath,String DBurl,String DBuser,String DBpwd ) throws FileNotFoundException, XMLStreamException
	{
		String protocol	= "org.wso2.ws.dataservice.protocol";
		String user		= "org.wso2.ws.dataservice.user";
		String Pwd	= "org.wso2.ws.dataservice.password";
		
		FileInputStream fis = new FileInputStream(FilePath);
		StAXOMBuilder builder = new StAXOMBuilder(fis);
		  OMElement documentElement = builder.getDocumentElement();	  
		   Iterator iterator = documentElement.getFirstElement().getChildElements();
		   while(iterator.hasNext()){
			   OMElement ome = (OMElement) iterator.next();
			   String AttValue = ome.getAttributeValue(new QName("","name"));
			   System.out.println(AttValue);
			   System.out.println(ome.getText());
		   }
	}
	*/  
}