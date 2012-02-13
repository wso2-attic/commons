package org.wso2.ws.dataservice.test;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.util.XMLPrettyPrinter;

public class AttributeTest extends DataServiceBaseTestCase {

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
	public AttributeTest(String testName) {
		super(testName);
	}

	/**
	 * @doc : Tests for generating output using attributes
	 */
	public void testAttributesInOutput() {
		String endPointUrl = epr + "AttributeTestService";
		System.out.println("###########################################");
		System.out.println("Testing service : "+endPointUrl);
		System.out.println("###########################################");
		EndpointReference targetEPR = new EndpointReference(endPointUrl);
		try {
			OMElement payload = getPayLoad();
			Options options = new Options();
			options.setTo(targetEPR);
			options.setAction("urn:resultsA");
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
		return method;
	}

}
