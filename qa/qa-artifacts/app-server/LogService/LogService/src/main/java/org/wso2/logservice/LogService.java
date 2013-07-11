package org.wso2.logservice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LogService{
	private static final Log log = LogFactory.getLog(LogService.class);
	
	public void LogAndDrop (String incoming) {		
		log.info("The message is: "+ incoming);
	}
	
	public String LogAndAck (String incoming) {
		log.info("The message is: "+ incoming);
		return "Logged: "+ incoming;
	}

}