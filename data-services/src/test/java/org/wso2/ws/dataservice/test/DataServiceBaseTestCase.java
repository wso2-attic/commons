package org.wso2.ws.dataservice.test;

import junit.framework.TestCase;

import org.wso2.ws.dataservice.test.util.UtilServer;

public class DataServiceBaseTestCase extends TestCase {
	protected UtilServer utilServer = null;
	protected String repository = "C://KP//workspace//wso2-commons//data-services//target//repository";
	protected String axis2Conf = "C://KP//workspace//wso2-commons//data-services//src//test//resources//axis2.xml";
	protected String epr = "http://localhost:5555/axis2/services/";

	protected void setUp() throws Exception {
		utilServer = new UtilServer();
		utilServer.start(repository, axis2Conf);
	}

	protected void tearDown() throws Exception {
		utilServer.stop();
	}
	public DataServiceBaseTestCase(String testName) {
		super(testName);
	}
	
	public void testMock(){
		//Do nothing.Just a mock test to stop junit 
		//complain about missing test method
	}
}
