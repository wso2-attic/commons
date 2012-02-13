package org.wso2.carbon.web.test.esb;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

public class ESBLoggingConfigTest  extends TestCase {
    Selenium selenium;

    public ESBLoggingConfigTest(Selenium _browser){
		selenium = _browser;
    }

    /*
    This method is used to 
     */
    public void testLoggingConfig() throws Exception{
		selenium.click("link=Logging");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("Logging Configuration"));
		assertTrue(selenium.isTextPresent("Persist All Configuration Changes"));
		assertTrue(selenium.isTextPresent("Global Log4J Configuration"));
		assertTrue(selenium.isTextPresent("Log Level"));
		assertEquals("OFF TRACE DEBUG INFO WARN ERROR FATAL", selenium.getText("globalLogLevel"));
		assertTrue(selenium.isTextPresent("Log Pattern"));
		assertEquals("[%d] %5p - %x %m {%c}%n", selenium.getValue("globalLogPattern"));
		assertTrue(selenium.isTextPresent(""));
		assertEquals("Restore Defaults", selenium.getValue("restoreGlobalConfig"));
		assertEquals("Update", selenium.getValue("globalLog4jUpdate"));
		assertTrue(selenium.isTextPresent("Configure Log4J Appenders"));
		assertTrue(selenium.isTextPresent("Name"));
		assertEquals("CARBON_CONSOLE CARBON_LOGFILE CARBON_MEMORY SERVICE_APPENDER TRACE_APPENDER TRACE_MEMORYAPPENDER", selenium.getText("appenderCombo"));
		assertTrue(selenium.isTextPresent("Log Pattern"));
		assertEquals("%d{ISO8601} [%X{ip}-%X{host}] [%t] %5p %c{1} %m%n", selenium.getValue("appenderLogPattern"));
		assertTrue(selenium.isTextPresent("Threshold"));
		assertEquals("OFF TRACE DEBUG INFO WARN ERROR FATAL", selenium.getText("appenderThresholdCombo"));
		assertEquals("Update", selenium.getValue("appenderUpdate"));
		assertTrue(selenium.isTextPresent("Configure Log4J Loggers"));
		assertTrue(selenium.isTextPresent("Filter Loggers by"));
		assertEquals("Starts With", selenium.getValue("//input[@value='Starts With']"));
		assertEquals("Contains", selenium.getValue("//input[@value='Contains']"));
		assertTrue(selenium.isTextPresent("Logger"));
		assertEquals("Parent Logger", selenium.getText("//table[@id='loggersTable']/thead/tr/th[2]"));
		assertEquals("Logger", selenium.getText("//table[@id='loggersTable']/thead/tr/th[1]"));
		assertEquals("Level", selenium.getText("//table[@id='loggersTable']/thead/tr/th[3]"));
		assertEquals("Additivity", selenium.getText("//table[@id='loggersTable']/thead/tr/th[4]"));
    }
}

