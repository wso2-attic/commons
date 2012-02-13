package org.wso2.wsas.service;

import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.ServiceGroupContext;

public class SGTestService1 {
	
	public int multiply(int x, int y){
		 System.out.println("Setting context............");
		 ServiceGroupContext sgContext =
           MessageContext.getCurrentMessageContext().getServiceGroupContext();
		 sgContext.setProperty("VALUE", new Integer(x * y));
		 return 0;
		 
}

}
