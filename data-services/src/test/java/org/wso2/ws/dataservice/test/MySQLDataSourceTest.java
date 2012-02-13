package org.wso2.ws.dataservice.test;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.util.XMLPrettyPrinter;

public class MySQLDataSourceTest extends DataServiceBaseTestCase {

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
	public MySQLDataSourceTest(String testName) {
		super(testName);
	}

	/**
	 * @doc : exposes single mysql table as a service,
	 * no parameters
	 */
	public void testSingleTableNoParams() {
		String endPointUrl = epr + "MySQLService";
		System.out.println("###########################################");
		System.out.println("Testing service : "+endPointUrl);
		System.out.println("###########################################");
		EndpointReference targetEPR = new EndpointReference(endPointUrl);
		try {
			OMElement payload = getPayload();
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

	private static OMElement getPayload() {
		OMFactory fac = OMAbstractFactory.getOMFactory();
		OMNamespace omNs = fac.createOMNamespace(
				"http://example1.org/example1", "example1");
		OMElement method = fac.createOMElement("results1", omNs);
		return method;
	}

}
