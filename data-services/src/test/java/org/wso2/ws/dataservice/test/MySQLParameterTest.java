package org.wso2.ws.dataservice.test;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.util.XMLPrettyPrinter;

public class MySQLParameterTest extends DataServiceBaseTestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public MySQLParameterTest(String testName) {
		super(testName);
	}

	/**
	 * @doc : exposes single mysql table as a service,
	 * checks for parameters with equal operator
	 */
	public void testParamsEqual() {
		String endPointUrl = epr + "MySQLParamTestService";
		System.out.println("###########################################");
		System.out.println("Testing service : "+endPointUrl);
		System.out.println("###########################################");
		EndpointReference targetEPR = new EndpointReference(endPointUrl);
		try {
			OMElement payload = getPayLoad();
			Options options = new Options();
			options.setTo(targetEPR);
			options.setAction("urn:results1");
			ServiceClient sender = new ServiceClient();
			sender.setOptions(options);
			OMElement result = sender.sendReceive(payload);
			XMLPrettyPrinter.prettify(result, System.out);
			assertNotNull(result);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		assertTrue(true);
	}

	private static OMElement getPayLoad() {
		OMFactory fac = OMAbstractFactory.getOMFactory();
		OMNamespace omNs = fac.createOMNamespace(
				"http://example1.org/example1", "example1");
		OMElement method = fac.createOMElement("results1", omNs);
		//customer number parameters
	    OMElement cusNo = fac.createOMElement("cusNo", omNs);
	    cusNo.setText("484");
		method.addChild(cusNo);
		//last name parameters
	    OMElement lastName = fac.createOMElement("lastName", omNs);
	    lastName.setText("Roel");
		method.addChild(lastName);		
		return method;
	}

	
	/**
	 * @doc : exposes single mysql table as a service,
	 * checks for parameters with like operator
	 */
	public void testParamsLike() {
		String endPointUrl = epr + "MySQLParamTestService";
		System.out.println("###########################################");
		System.out.println("Testing service : "+endPointUrl);
		System.out.println("###########################################");
		EndpointReference targetEPR = new EndpointReference(endPointUrl);
		try {
			OMElement payload = getPayLoad2();
			Options options = new Options();
			options.setTo(targetEPR);
			options.setAction("urn:results2");
			ServiceClient sender = new ServiceClient();
			sender.setOptions(options);
			OMElement result = sender.sendReceive(payload);
			XMLPrettyPrinter.prettify(result, System.out);
			assertNotNull(result);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		assertTrue(true);
	}

	private static OMElement getPayLoad2() {
		OMFactory fac = OMAbstractFactory.getOMFactory();
		OMNamespace omNs = fac.createOMNamespace(
				"http://example1.org/example1", "example1");
		OMElement method = fac.createOMElement("results2", omNs);
		//last name parameters
	    OMElement lastName = fac.createOMElement("customerName", omNs);
	    lastName.setText("A%");
		method.addChild(lastName);		
		return method;
	}
	
}
