package com.example.tests;

import com.thoughtworks.selenium.*;
import java.util.regex.Pattern;

public class NewTest extends SeleneseTestCase {
	public void testNew() throws Exception {
		selenium.open("/services/schemaTest?tryit");
		selenium.click("link=returnJSObject");
		selenium.type("input_returnJSObject_param1_0", "test");
		selenium.type("input_returnJSObject_param2_0", "1.1");
		selenium.click("input_returnJSObject_param3_0");
		selenium.click("button_returnJSObject");
		boolean storeResponse = selenium.isTextPresent("{param1 : \"test\", param2 : 1.1, param3 : true}");
		verifyTrue(selenium.isTextPresent("{param1 : \"test\", param2 : 1.1, param3 : true}"));
	}
}
