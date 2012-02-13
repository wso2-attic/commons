package org.wso2.wsas.service;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService(name= "JaxWSEchoService", targetNamespace = "http://wso2.qa.org/jaxws")
public class EchoService {
	@WebMethod(operationName = "echoStringWebMethod")
	public String echoString(String s){
		return s;
	}

}
