package com.example.tests;

import com.thoughtworks.selenium.*;
import java.util.regex.Pattern;

public class HomeURLs extends SeleneseTestCase {
	public void testHomeURLs() throws Exception {
		selenium.open("/");
		selenium.click("link=mashups.wso2.org");
		verifyEquals("WSO2 Mashup Community Portal", selenium.getTitle());
		selenium.click("//a[contains(text(),'User\n                    guide')]");
		String Welcome to the WSO2 Mashup Server = selenium.getTitle();
		selenium.click("//a[contains(text(),'Reference\n                    documentation')]");
		String WSO2 Mashup Server Reference - WSO2 Mashup Server - Confluence = selenium.getTitle();
		boolean OT2 = selenium.isTextPresent("Welcome to the WSO2 Mashup Server Reference documentation!");
		selenium.click("//a[contains(text(),'WSO2 Mashup\n                    Server issue tracker')]");
		String Browse Project - WSO2 JIRA = selenium.getTitle();
		selenium.click("link=Forum");
		String exact:Project: WSO2 Mashup Server | WSO2 Oxygen Tank = selenium.getTitle();
	}
}
