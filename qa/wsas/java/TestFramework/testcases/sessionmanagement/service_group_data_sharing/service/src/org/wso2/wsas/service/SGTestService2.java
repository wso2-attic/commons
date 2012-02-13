package org.wso2.wsas.service;

import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.ServiceGroupContext;

public class SGTestService2 {
	public int getResult(){
		 System.out.println("Getting results..........");
		 ServiceGroupContext sgContext =
           MessageContext.getCurrentMessageContext().getServiceGroupContext();
		 
   return ((Integer) sgContext.getProperty("VALUE")).intValue();
	 }

}
