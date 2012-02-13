package com.example.tests;

import com.thoughtworks.selenium.*;
import java.util.regex.Pattern;

public class NewTest extends SeleneseTestCase {
	public void testNew() throws Exception {
		selenium.open("/");
		selenium.type("txtUserName", "admin");
		selenium.type("txtPassword", "admin");
		selenium.click("signin");
	}
}
