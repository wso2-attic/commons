package org.wso2.carbon.web.test.esb;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;


public class ESBLoadbalanceFailoverTest extends TestCase {
    Selenium selenium;

    public ESBLoadbalanceFailoverTest(Selenium _browser){
        selenium = _browser;
    }

    /*
	 * This method will be used to add LoadBalance Group
	*/
    public void addLoadBalance() throws Exception {
		selenium.click("link=Add Load-balance Group");
		selenium.waitForPageToLoad("30000");
    }

    /*
    This method will be used to add LoadBalance Group mandatory information
     */
    public void addLoadBalanceMandatoryInfo(String eprName,String sessionMgt,String sessionTimeout) throws Exception{
        if (eprName !=null)
            selenium.type("load.name", eprName);
        if(sessionMgt!=null)
            selenium.select("_sesOptions", "label="+sessionMgt);
        if(sessionTimeout!=null)
            selenium.type("_sessionTimeOut", sessionTimeout);
    }

    /*
	 * This method will be used to add Failover Group
	*/
    public void addFailoverGroup() throws Exception {
		selenium.click("link=Add Failover Group");
		selenium.waitForPageToLoad("30000");
    }

    /*
    This method will be used to add Failover Group mandatory information
     */
    public void addFailoverGroupMandatoryInfo(String eprName) throws Exception{
        if (eprName !=null){
            selenium.type("failover.name", eprName);
        }
    }


    /*
    This method is used to add a endpoint to LoadBalance group or Failover Group
     */
    public void addEndpointToLoadBalanceFailoverGroup(String epType) throws Exception{

        selenium.click("link=Add Endpoint");
        if(epType.equalsIgnoreCase("Address"))
            selenium.click("link=Address");
        if(epType.equalsIgnoreCase("WSDL"))
            selenium.click("link=WSDL");
        if(epType.equalsIgnoreCase("Failover Group"))
            selenium.click("link=Failover Group");
        if(epType.equalsIgnoreCase("Load-balance group"))
            selenium.click("link=Load-balance group");
    }

    public void clickAddressEndpoint(String level) throws Exception{
        selenium.click("//div[@id='address."+level+"']/a");
        Thread.sleep(4000);
    }

    /*
    This method will be used to add address endpoint mandatory information
     */
    public void addAddressEprMandatoryInfo(String level,String epr) throws Exception{
        selenium.type("form_address."+level+"_address", epr);
    }

    /*
    This method will be used to add format and optimize information
     */
    public void addAddressEprFormatOptimizeInfo(String level,String format, String optimize) throws Exception{
        if (format !=null){
            selenium.select("form_address."+level+"_format", "label="+format);
        }

        if (optimize !=null){
            selenium.select("form_address."+level+"_optimize", "label="+optimize);
        }
    }


    /*
    This method will be used to add address endpoint suspend information
     */
    public void addAddressEprSuspendInfo(String level,String errCode, String durSec, String maxDur, String factor) throws Exception{
        if (errCode !=null){
            selenium.type("form_address."+level+"_errorCodes", errCode);
        }

        if (durSec !=null){
            selenium.type("form_address."+level+"_suspend", durSec);
        }

        if (maxDur !=null){
          selenium.type("form_address."+level+"_maxDur", maxDur);
        }

        if (factor !=null){
          selenium.type("form_address."+level+"_factor", factor);
        }
    }

    /*
    This method will be used to add endpoint retry information
     */
    public void addAddressEprRetryInfo(String level,String retryErroCode,String retryTimeOut, String retryDelay) throws Exception{
        if (retryErroCode !=null){
           selenium.type("form_address."+level+"_timeoutErrorCodes", retryErroCode);
        }

        if (retryTimeOut !=null){
          selenium.type("form_address."+level+"_retry", retryTimeOut);
        }

        if (retryDelay !=null){
          selenium.type("form_address."+level+"_retryDelay", retryDelay);
        }
    }

    /*
    This method will be used to add address endpoint retry information
     */
    public void addAddressEprTimeoutInfo(String level,String actionSelect, String actionDuration) throws Exception{
        if (actionSelect !=null){
          selenium.select("form_address."+level+"_timeoutAction", "label="+actionSelect);
        }

        if (actionDuration !=null){
          selenium.type("form_address."+level+"_duration", actionDuration);
        }
    }

    /*
    This method will be used to add address endpoint QoS information
     */
    public void addAddressEprQosInfo(String level,String wsAddressing, String sepListener, String wsSecurity, String secResource, String wsRM, String rmResource) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);

        if (wsAddressing !=null){
            selenium.click("form_address."+level+"_wsAddressing");
            Thread.sleep(2000);
        }

        if (sepListener != null){
            selenium.click("form_address."+level+"_separeteListener");
            Thread.sleep(2000);
        }

        if (wsSecurity != null){
            selenium.click("form_address."+level+"_wssecurity");
            selenium.click("//a[@onclick=\"showInLinedRegistryBrowser('form_address."+level+"_wssecurityPolicy')\"]");

            esbCommon.selectResource("Entry",secResource);
            Thread.sleep(2000);
        }

        if (wsRM != null){
            selenium.click("wsRM");
            Thread.sleep(2000);
        }

        if (rmResource != null){
            selenium.click("//a[@onclick=\"showInLinedRegistryBrowser('form_address."+level+"_wsrmPolicy')\"]");
            esbCommon.selectResource("Entry",rmResource);
            Thread.sleep(2000);
        }
    }

    /*
    This method is used to save the address endpoint info
     */
    public void saveAddressEp(String level) throws Exception{
        if(level.equals("1"))
            selenium.click("//input[@id='']");
        else
            selenium.click("//input[@id='' and @value='Update' and @type='button' and @onclick=\"upDateEndpoint('form_address."+level+"','address')\"]");
        selenium.click("//button[@type='button']");
    }

    /*
    This method is used to save the Failover group
     */
    public void saveGroup() throws Exception{
        selenium.click("save");
        selenium.waitForPageToLoad("30000");
    }

}
