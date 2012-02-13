package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.client.ESBSampleClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.Options;
import org.apache.axis2.Constants;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axiom.om.OMElement;

import java.io.File;


public class ESBSample5Test extends CommonSetup{

    public ESBSample5Test(String text) {
        super(text);
    }

//<sequence name="myFaultHandler">
//        <makefault>
//            <code value="tns:Receiver" xmlns:tns="http://www.w3.org/2003/05/soap-envelope"/>
//            <reason expression="get-property('ERROR_MESSAGE')"/>
//        </makefault>
//
//        <property name="RESPONSE" value="true"/>
//        <header name="To" expression="get-property('ReplyTo')"/>
//        <send/>
//    </sequence>
//
//    <sequence name="main" onError="myFaultHandler">
//        <in>
//            <switch source="//m0:getQuote/m0:request/m0:symbol"
//                    xmlns:m0="http://services.samples/xsd">
//                <case regex="MSFT">
//                    <send>
//                        <endpoint><address uri="http://bogus:9000/services/NonExistentStockQuoteService"/></endpoint>
//                    </send>
//                </case>
//                <case regex="SUN">
//                    <send>
//                        <endpoint><address uri="http://localhost:9009/services/NonExistentStockQuoteService"/></endpoint>
//                    </send>
//                </case>
//            </switch>
//            <drop/>
//        </in>
//
//        <out>
//            <send/>
//        </out>
//    </sequence>

    /*
    This method will create the myFaultHandler sequence
     */
    public void addMyFaultHandler() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBFaultMediatorTest esbFaultMediatorTest=new ESBFaultMediatorTest(selenium);
        ESBPropertyMediatorTest esbPropertyMediatorTest=new ESBPropertyMediatorTest(selenium);
        ESBHeaderMediatorTest esbHeaderMediatorTest=new ESBHeaderMediatorTest(selenium);

        esbCommon.viewSequences();
        boolean seqPresent=selenium.isTextPresent("myFaultHandler");

        if(!seqPresent){
            esbCommon.addSequence("myFaultHandler");
            esbCommon.addRootLevelChildren("Add Child","Transform","Fault");

            esbFaultMediatorTest.setSoap11Fault("0","Server");
            esbFaultMediatorTest.setFaultCodeExpression("get-property('ERROR_MESSAGE')");
            esbFaultMediatorTest.setSoap11FaultGeneralInfo("actor","detail");
            esbCommon.mediatorUpdate();

//            esbCommon.addMediators("Add Sibling","0","Core","Property");
//            esbPropertyMediatorTest.addBasicPropInfo("1","RESPONSE","Set");
//            esbPropertyMediatorTest.addPropertyMediator("Value","true","Synapse");
//            esbCommon.mediatorUpdate();
//
//            esbCommon.addMediators("Add Sibling","1","Transform","Header");
//            esbHeaderMediatorTest.addHeaderMediator("2","To");
//            esbHeaderMediatorTest.setHeaderAction("expression", "get-property('ReplyTo')");
//            esbCommon.mediatorUpdate();

            esbCommon.addMediators("Add Sibling","0","Core","Send");
            esbCommon.mediatorUpdate();

            esbCommon.sequenceSave();

        }
    }

    /*
    This method will create the  sample_5
     */
    public void addSequence(String seqName) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBSequenceTreePopulatorTest esbSequenceTreePopulatorTest = new ESBSequenceTreePopulatorTest(selenium);
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
        ESBSwitchMediatorTest esbSwitchMediatorTest = new ESBSwitchMediatorTest(selenium);
        
        esbCommon.addSequence(seqName);
        esbCommon.setOnErrorSeq("myFaultHandler");

        //Adding the In mediator
        esbCommon.addRootLevelChildren("Add Child","Filter","In");
        esbSequenceTreePopulatorTest.clickMediator("0");

        //Adding a Switch mediator
        esbCommon.addMediators("Add Child","0","Filter","Switch");
        esbSwitchMediatorTest.addSwitchMediator("0.0","//m0:getQuote/m0:request/m0:symbol");
        esbSwitchMediatorTest.addSwitchNamespace("m0","http://services.samples");
        esbCommon.mediatorUpdate();

        //Adding the first level Case mediator
        esbSwitchMediatorTest.addCase("0.0","MSFT","0.0.0");
        esbCommon.mediatorUpdate();
        esbSwitchMediatorTest.addCaseChildMediator("0.0.0", "Core","Send");
        esbSendMediatorTest.addAnonSendMediator("0.0.0.0");
        esbSendMediatorTest.addMandInfoSendMediator("http://bogus:9000/services/NonExistentStockQuoteService");
        esbSendMediatorTest.saveEndpoint();
        esbCommon.mediatorUpdate();

        //Adding the second level Case mediator
        esbSwitchMediatorTest.addCase("0.0","SUN","0.0.1");
        esbCommon.mediatorUpdate();
        esbSwitchMediatorTest.addCaseChildMediator("0.0.1", "Core","Send");
        esbSequenceTreePopulatorTest.clickMediator("0.0.1");
        esbSendMediatorTest.addAnonSendMediator("0.0.1.0");
        esbSendMediatorTest.addMandInfoSendMediator("http://localhost:9009/services/NonExistentStockQuoteService");
        esbSendMediatorTest.saveEndpoint();
        esbCommon.mediatorUpdate();

        //Adding the Drop mediator
        esbCommon.addMediators("Add Child","0","Core","Drop");

        //Adding an Out mediator
        esbCommon.addRootLevelChildren("Add Child", "Filter", "Out");
        esbSequenceTreePopulatorTest.clickMediator("1");

        //Adding a Send mediator
        esbCommon.addMediators("Add Child","1", "Core", "Send");
        esbSendMediatorTest.addNormalSendMediator("1.0");
        esbCommon.sequenceSave();
    }

    /*
    Invoking the client
     */
    public void invokeClient(String symbol) throws Exception{
        //ESBSampleClient esbSampleClient = new ESBSampleClient();
        ESBCommon esbCommon = new ESBCommon(selenium);
        boolean stockQuoteResponse=faultHandlingStockQuoteClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/",esbCommon.getServiceAddUrl("SimpleStockQuoteService"),symbol);
        if (stockQuoteResponse){
            System.out.println("The response received!!!!\n");
        }else{
            throw new MyCheckedException("Client Failed!!!!\n");
        }
        Thread.sleep(5000);
       esbCommon.closeFiles();
    }

    public boolean faultHandlingStockQuoteClient(String trpUrl, String addUrl,String symbol) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBSampleClient esbSampleClient = new ESBSampleClient();
        ConfigurationContext cc = ConfigurationContextFactory.createConfigurationContextFromFileSystem(esbCommon.getCarbonHome() + File.separator + "repository", null);

        OMElement payload=null;
        payload = esbSampleClient.createPayLoad(symbol);

        boolean output = false;
        ServiceClient serviceclient = new ServiceClient(cc, null);
        Options opts = new Options();

        if (trpUrl != null && !"null".equals(trpUrl)) {
            opts.setProperty(Constants.Configuration.TRANSPORT_URL, trpUrl);
        }
        if (addUrl != null && !"null".equals(addUrl)) {
            serviceclient.engageModule("addressing");
            opts.setTo(new EndpointReference(addUrl));
        }
        opts.setAction("urn:getQuote");
        serviceclient.setOptions(opts);

        try {
            OMElement res = serviceclient.sendReceive(payload);
            output = res.getChildren().next().toString().contains(symbol);
        }
        catch (AxisFault e) {
            esbCommon.viewSequences();
            esbCommon.clickEditSeq("main");
            esbCommon.clickMediatorSource("0");
            String mediator_source = selenium.getText("mediatorSrc");

            String seq_source,refering_seq,on_error_seq=null,temp;
            String fault_code,fault_string,fault_detail,fault_actor,fault_string_ex,fault_string_val,fault_node;
            boolean out1=true,out2=true,out3=true,out4=true;

            if(mediator_source.contains("key")){
                temp=mediator_source.substring(mediator_source.indexOf("key=\"")+5);
                refering_seq=temp.substring(0,temp.indexOf('"'));

                esbCommon.viewSequences();
                esbCommon.clickEditSeq(refering_seq);
                esbCommon.clickSequenceSource("0");

                seq_source=selenium.getText("sequence_source");
                if(seq_source.contains("onError")){
                    temp=seq_source.substring(seq_source.indexOf("onError=\"")+9);
                    on_error_seq=temp.substring(0,temp.indexOf('"'));
                }
                else if(seq_source.contains("<syn:makefault")){
                    on_error_seq=refering_seq;
                }

                if(on_error_seq!=null){
                esbCommon.viewSequences();
                esbCommon.clickEditSeq(on_error_seq);
                selenium.click("link=Fault");
                Thread.sleep(2000);

                fault_string=selenium.getValue("name_space");
                fault_actor=selenium.getValue("fault_actor");
                fault_detail=selenium.getValue("detail");
                fault_string_ex=selenium.getValue("fault_string");
                fault_string_val=selenium.getValue("//input[@name='fault_string' and @value='expression']");

                //check the soap version
                if(selenium.getValue("soap_version").equals("on")){//soap version 1.1
                    fault_code=selenium.getSelectedValue("fault_code1");

                    //verify the soap response that received by the client
                    out1=e.getFaultCodeElement().toString().equals("<faultcode xmlns:soap11Env=\"http://schemas.xmlsoap.org/soap/envelope/\">soap11Env:"+fault_code+"</faultcode>");
                    System.out.println(e.getFaultReasonElement());
                    if(!fault_actor.equals(""))
                        out2=e.getFaultRoleElement().toString().equals("<faultactor>"+fault_actor+"</faultactor>");
                    if(!fault_detail.equals(""))
                        out3=e.getFaultDetailElement().toString().equals("<detail>"+fault_detail+"</detail>");

                    System.out.println("Client received a SOAP1.1 fault!!!");
                }
                else if(selenium.getValue("//input[@id='soap_version' and @name='soap_version' and @value='2']").equals("on")){//soap version 1.2
                    fault_code=selenium.getSelectedValue("fault_code2");
                    fault_node=selenium.getValue("node");

                    //verify the soap response that received by the client
                    out1=e.getFaultCodeElement().toString().equals("<soapenv:Code xmlns:soapenv=\"http://www.w3.org/2003/05/soap-envelope\"><soapenv:Value xmlns:soap12Env=\"http://www.w3.org/2003/05/soap-envelope\">soap12Env:"+fault_code+"</soapenv:Value></soapenv:Code>");
                    System.out.println(e.getFaultReasonElement().toString());
                    if(!fault_actor.equals(""))
                        out2=e.getFaultRoleElement().toString().equals("<soapenv:Role xmlns:soapenv=\"http://www.w3.org/2003/05/soap-envelope\">"+fault_actor+"</soapenv:Role>");
                    if(!fault_detail.equals(""))
                        out3=e.getFaultDetailElement().toString().equals("<soapenv:Detail xmlns:soapenv=\"http://www.w3.org/2003/05/soap-envelope\">"+fault_detail+"</soapenv:Detail>");
                    if(!fault_node.equals(""))
                        out4=e.getFaultNodeElement().toString().equals("<soapenv:Node xmlns:soapenv=\"http://www.w3.org/2003/05/soap-envelope\">"+fault_node+"</soapenv:Node>");

                    System.out.println("Client received a SOAP1.2 fault!!!");
                }
            }
            System.out.println("Fault_code= "+out1+"   "+"Fault_actor= "+out2+"   "+"Fault_detail= "+out3+"   "+"Fault_node= "+out4);
            if((out1 && out2 && out3)||(out1 && out2 && out3 && out4))
                output=true;
        }
            }
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            return output;
    }

    public void testAll() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        ESBCommon esbCommon = new ESBCommon(selenium);

         boolean login = selenium.isTextPresent("Sign-out");

         if (login){
             seleniumTestBase.logOutUI();
         }

        seleniumTestBase.loginToUI("admin","admin");

        addMyFaultHandler();
        addSequence("Sample_5");

        esbCommon.setSequenceToSequence("main","Sample_5");

        //Invoke MSFT client
        invokeClient("MSFT");

        //Invoke SUN client
        invokeClient("SUN");

        seleniumTestBase.logOutUI();
    }


}
