package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;
import junit.framework.Test;
import junit.framework.TestSuite;

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

public class ESBLoggingConfigTest extends CommonSetup{


    public ESBLoggingConfigTest(String text) {
        super(text);
    }

    /*
    This mthod will be used to log into the management console
     */
    public void testLogin() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);

        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }

        seleniumTestBase.loginToUI("admin","admin");
    }

    /*
    This method is used to test Global Log4J Configuration
     */
    public void testGlobalLog4J() throws Exception{
        ServiceManagement serviceManagement = new ServiceManagement(selenium);
        serviceManagement.Login();        
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
        System.out.println("SUCCESS !!! - The Global Log4J Configuration verified successfully");
    }

    /*
    This method is used to test Log4J Appenders
     */
    public void testLog4JAppenders() throws Exception{
        ServiceManagement serviceManagement = new ServiceManagement(selenium);
        serviceManagement.Login();
		selenium.click("link=Logging");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("Configure Log4J Appenders"));
		assertTrue(selenium.isTextPresent("Name"));
		assertEquals("CARBON_CONSOLE CARBON_LOGFILE CARBON_MEMORY CARBON_SYS_LOG SERVICE_APPENDER TRACE_APPENDER TRACE_MEMORYAPPENDER", selenium.getText("appenderCombo"));
		assertTrue(selenium.isTextPresent("Log Pattern"));
		assertEquals("[%d{ISO8601}] %5p - %c{1} %m%n", selenium.getValue("appenderLogPattern"));
		assertTrue(selenium.isTextPresent("Threshold"));
		assertEquals("OFF TRACE DEBUG INFO WARN ERROR FATAL", selenium.getText("appenderThresholdCombo"));
		assertEquals("Update", selenium.getValue("appenderUpdate"));
        System.out.println("SUCCESS !!! - The Log4J Appenders verified successfully");
    }

    /*
    This method is used to test Log4J Loggers Configuration
     */
    public void testLog4JLoggers() throws Exception{
        ServiceManagement serviceManagement = new ServiceManagement(selenium);
        serviceManagement.Login();
		selenium.click("link=Logging");
		selenium.waitForPageToLoad("30000");        
		assertTrue(selenium.isTextPresent("Configure Log4J Loggers"));
		assertTrue(selenium.isTextPresent("Filter Loggers by"));
		assertEquals("Starts With", selenium.getValue("//input[@value='Starts With']"));
		assertEquals("Contains", selenium.getValue("//input[@value='Contains']"));
		assertTrue(selenium.isTextPresent("Logger"));
		assertEquals("Parent Logger", selenium.getText("//table[@id='loggersTable']/thead/tr/th[2]"));
		assertEquals("Logger", selenium.getText("//table[@id='loggersTable']/thead/tr/th[1]"));
		assertEquals("Level", selenium.getText("//table[@id='loggersTable']/thead/tr/th[3]"));
		assertEquals("Additivity", selenium.getText("//table[@id='loggersTable']/thead/tr/th[4]"));
		assertTrue(selenium.isTextPresent("root"));
		assertTrue(selenium.isTextPresent("org.apache.synapse"));
		assertTrue(selenium.isTextPresent("org.apache.synapse.Axis2SynapseController"));
		assertTrue(selenium.isTextPresent("org.apache.synapse.ServerConfigurationInformation"));
		assertTrue(selenium.isTextPresent("org.apache.synapse.ServerConfigurationInformationFactory"));
		assertTrue(selenium.isTextPresent("org.apache.synapse.ServerManager"));
		assertTrue(selenium.isTextPresent("org.apache.synapse.ServerManagerView"));
		assertTrue(selenium.isTextPresent("org.apache.synapse.SynapseControllerFactory"));
		assertTrue(selenium.isTextPresent("org.apache.synapse.aspects.statistics.view.SystemViewStrategy"));
		assertTrue(selenium.isTextPresent("org.apache.synapse.commons.datasource.DataSourceHelper"));
		assertTrue(selenium.isTextPresent("org.apache.synapse.commons.datasource.DataSourceInformationRepository"));
		assertTrue(selenium.isTextPresent("org.apache.synapse.commons.datasource.DataSourceRepositoryManager"));
		assertTrue(selenium.isTextPresent("org.apache.synapse.commons.datasource.DatasourceMBeanRepository"));
		assertTrue(selenium.isTextPresent("org.apache.synapse.commons.datasource.InMemoryDataSourceRepository"));
		assertTrue(selenium.isTextPresent("org.apache.synapse.commons.datasource.JNDIBasedDataSourceRepository"));
		assertTrue(selenium.isTextPresent("org.apache.synapse.commons.datasource.RepositoryBasedDataSourceFinder"));
		assertTrue(selenium.isTextPresent("org.apache.synapse.commons.datasource.factory.DataSourceInformationListFactory"));
		assertTrue(selenium.isTextPresent("org.apache.synapse.commons.datasource.factory.DataSourceInformationRepositoryFactory"));
		assertTrue(selenium.isTextPresent("org.apache.synapse.commons.jmx.MBeanRegistrar"));
		assertTrue(selenium.isTextPresent("org.apache.synapse.commons.security.secret.SecretCallbackHandlerFactory"));
		assertTrue(selenium.isTextPresent("org.apache.synapse.commons.security.secret.SecretManager"));
		assertTrue(selenium.isTextPresent("org.apache.synapse.commons.util.MiscellaneousUtil"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.Entry"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.SynapseConfigUtils"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.SynapseConfiguration"));
//        assertTrue(selenium.isTextPresent("org.apache.synapse.config.SynapseConfigurationBuilder"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.SynapsePropertiesLoader"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.AggregateMediatorFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.AggregateMediatorSerializer"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.AnnotatedCommandMediatorFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.CacheMediatorFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.CacheMediatorSerializer"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.CalloutMediatorFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.CalloutMediatorSerializer"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.ClassMediatorFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.ClassMediatorSerializer"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.CloneMediatorFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.CloneMediatorSerializer"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.ConfigurationFactoryAndSerializerFinder"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.DBLookupMediatorFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.DBLookupMediatorSerializer"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.DBReportMediatorFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.DBReportMediatorSerializer"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.DropMediatorFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.DropMediatorSerializer"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.EntryFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.EntrySerializer"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.FaultMediatorFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.FaultMediatorSerializer"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.FilterMediatorFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.FilterMediatorSerializer"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.HeaderMediatorFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.HeaderMediatorSerializer"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.InMediatorFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.InMediatorSerializer"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.IterateMediatorFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.IterateMediatorSerializer"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.LogMediatorFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.LogMediatorSerializer"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.MediatorFactoryFinder"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.MediatorPropertyFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.MediatorSerializerFinder"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.OMElementUtils"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.OutMediatorFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.OutMediatorSerializer"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.POJOCommandMediatorFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.POJOCommandMediatorSerializer"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.PropertyMediatorFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.PropertyMediatorSerializer"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.RMSequenceMediatorFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.RMSequenceMediatorSerializer"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.RegistryFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.RegistrySerializer"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.SendMediatorFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.SendMediatorSerializer"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.SequenceMediatorFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.SequenceMediatorSerializer"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.SwitchMediatorFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.SwitchMediatorSerializer"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.SynapseMediatorFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.SynapseMediatorSerializer"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.SynapseXMLConfigurationFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.SynapseXPathFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.SynapseXPathSerializer"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.TransactionMediatorFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.TransactionMediatorSerializer"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.ValidateMediatorFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.ValidateMediatorSerializer"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.XMLConfigurationBuilder"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.XMLConfigurationSerializer"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.XSLTMediatorFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.XSLTMediatorSerializer"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.eventing.EventPublisherMediatorFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.config.xml.eventing.EventPublisherMediatorSerializer"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.core.axis2.Axis2SynapseEnvironment"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.core.axis2.MessageContextCreatorForAxis2"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.core.axis2.SynapseMessageReceiver"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.mediators.base.SequenceMediator"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.mediators.bsf.ScriptMediator"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.mediators.bsf.ScriptMediatorFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.mediators.bsf.ScriptMediatorSerializer"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.mediators.builtin.LogMediator"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.mediators.builtin.SendMediator"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.mediators.filters.FilterMediator"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.mediators.filters.InMediator"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.mediators.filters.OutMediator"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.mediators.spring.SpringMediatorFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.mediators.spring.SpringMediatorSerializer"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.mediators.throttle.ThrottleMediatorFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.mediators.throttle.ThrottleMediatorSerializer"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.mediators.xquery.XQueryMediatorFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.mediators.xquery.XQueryMediatorSerializer"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.registry.AbstractRegistry"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.registry.RegistryEntry"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.task.DefaultTaskJobDetailFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.task.DefaultTaskTriggerFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.task.TaskDescriptionRepository"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.task.TaskDescriptionRepositoryFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.task.TaskHelper"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.task.TaskScheduler"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.transport"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.transport.nhttp.ClientHandler"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.transport.nhttp.HttpCoreNIOListener"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.transport.nhttp.HttpCoreNIOSSLListener"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.transport.nhttp.HttpCoreNIOSSLSender"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.transport.nhttp.HttpCoreNIOSender"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.transport.nhttp.HttpCoreRequestResponseTransport"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.transport.nhttp.NHttpConfiguration"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.transport.nhttp.ServerHandler"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.transport.nhttp.ServerWorker"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.transport.nhttp.headers"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.transport.nhttp.util.MessageFormatterDecoratorFactory"));
        assertTrue(selenium.isTextPresent("org.apache.synapse.util.xpath.SynapseXPath"));
        System.out.println("SUCCESS !!! - The Log4J Loggers Configuration verified successfully");        
    }

    /*
    This mthod will be used to log out from the management console
     */
    public void testLogout() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        seleniumTestBase.logOutUI();
    }    
}
