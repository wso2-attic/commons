package com.cxfsample;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface IHello {
	
	@WebMethod
	public String sayHello(@WebParam(name = "name") String name);
}
