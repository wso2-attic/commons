package org.wso2.logservice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LogService{
	static int count=1;
	private static final Log log = LogFactory.getLog(LogService.class);
	
	public void LogAndDrop (String incoming) {		
		log.info("The message is: "+ incoming+"         Recieved Count  "+count);
		count++;
	}
	
	public String LogAndAck (String incoming) {
		log.info("The message is: "+ incoming+"         Recieved Count  "+count);
		
		count++;
		return "Logged: "+ incoming;
		
	}

}