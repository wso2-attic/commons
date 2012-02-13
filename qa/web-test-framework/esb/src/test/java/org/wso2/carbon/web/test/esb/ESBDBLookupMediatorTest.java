package org.wso2.carbon.web.test.esb;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;


public class ESBDBLookupMediatorTest extends TestCase {
    Selenium selenium;

    public ESBDBLookupMediatorTest(Selenium _browser){
		selenium = _browser;
    }

    /*
     This  method will add an DBLookup mediator and it's mandatory properties for Pool connections
     */
    public void addDBLookupMediatorPoolInfo(String level, String driver, String url, String user, String password) throws Exception {
		selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(4000);

		selenium.click("radio_pool");
        selenium.type("driver", driver);
		selenium.type("url", url);
		selenium.type("user", user);
		selenium.type("password", password);
    }

    /*
     This  method will add an DBLookup mediator and it's mandatory properties for Data source connections
     */
    public void addDBLookupMediatorDSInfo(String level, String dsType, String initCtx, String dataSource, String url, String user, String password) throws Exception {
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);
        selenium.click("radio_datasource");
		if (dsType.equals("sourceTypeInline")){
            selenium.click("sourceTypeInline");
            selenium.type("init_ctx", initCtx);
            selenium.type("data_source", dataSource);
            selenium.type("url", url);
            selenium.type("user", user);
            selenium.type("password", password);
        } else {
		    selenium.click("radio_datasource");
		    selenium.click("link=Load Data Sources");
        }
    }

    /*
     This  method will add Properties of the DBLookup mediator
     */
    public void addDBLookupMediatorProperties(String property, String propertyVal) throws Exception {
        int prop_no=1;
		selenium.click("link=Add Property");
        Thread.sleep(2000);
        selenium.select("property"+prop_no, "label="+property);
		selenium.select("property_value"+prop_no, "label="+propertyVal);
        prop_no++;
    }

    /*
     This  method will add SQL Statment information of the DBLookup mediator
     */
    public void addDBLookupMediatorSQLStatement(String sqlValue) throws Exception {
        selenium.click("link=Add Statement");
        Thread.sleep(2000);
        selenium.type("sql_val1", sqlValue);
    }

    /*
      This  method will add Parameter information of the DBLookup mediator
     */
    int parameter_no=1;
    public void addDBLookupMediatorParameters(String paramType, String propType, String value) throws Exception{

        selenium.click("link=Add Parameter");
        selenium.select("javaType1."+parameter_no, "label="+paramType);

        if (propType.equals("Value")){
            selenium.select("parameterType1."+parameter_no, "label=Value");
            selenium.type("parameter_value1."+parameter_no, value);
        } else {
            selenium.select("parameterType1."+parameter_no, "label=Expression");
            selenium.type("parameter_value1."+parameter_no, value);
        }
        parameter_no++;
    }

    public void setParameter_no() throws Exception{
        parameter_no=1;
    }

    public int getParameter_no() throws Exception{
       return parameter_no-1;
    }

    int parameter_Ns_no=0;
    public void addDBRLookupParameterNamespase(String namepsacePrefix, String namespaceUri) throws Exception{
        selenium.click("paramNS1."+getParameter_no());
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.nsLevel=parameter_Ns_no;
        esbCommon.addNamespace(namepsacePrefix,namespaceUri);
        parameter_Ns_no++;
    }

    public void setParameter_Ns_no() throws Exception{
        parameter_Ns_no=0;
    }

    /*
    This method is used to add result for DBLookup mediator
     */
    int result_no=1;
    public void  addDBRLookupResult(String resultNm,String column) throws Exception{
        selenium.click("link=Add Result");
        selenium.type("property_name1."+result_no, resultNm);
        selenium.type("property_value1."+result_no, column);
        result_no++;
    }

    public void setResult_no() throws Exception{
        result_no=1;
    }

    public int getResult_no() throws Exception{
       return result_no-1;
    }



}
