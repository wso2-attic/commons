package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.DataSource;
import org.wso2.carbon.web.test.client.ESBSampleClient;
import com.thoughtworks.selenium.Selenium;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.File;


public class ESBSample362Test extends CommonSetup{

    Properties properties = new Properties();
    Connection connection = null;
    static String DBName="esbdb" ;
    static String hostname ;
    static String DBPort;
    static String driverName;
    static String DBUserName;
    static String DBPassword;
    static String dburl;

    public ESBSample362Test(String text) {
        super(text);
    }

    public void createSequence(String seqName) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBLogMediatorTest esbLogMediatorTest = new ESBLogMediatorTest(selenium);
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
        ESBDBLookupMediatorTest esbDBLookupMediatorTest=new ESBDBLookupMediatorTest(selenium);
        ESBDBReportMediatorTest esbDBReportMediatorTest=new ESBDBReportMediatorTest(selenium);
        ESBSequenceTreePopulatorTest esbSequenceTreePopulatorTest = new ESBSequenceTreePopulatorTest(selenium);

        //Loging in and creating the sequence
        esbCommon.addSequence(seqName);

        //Adding the In mediator
        esbCommon.addRootLevelChildren("Add Child","Filter","In");
        esbSequenceTreePopulatorTest.clickMediator("0");
        
        //Adding the Send mediator
        esbCommon.addMediators("Add Child","0","Core","Send");
        esbSendMediatorTest.addAnonSendMediator("0.0");
        esbSendMediatorTest.addMandInfoSendMediator(esbCommon.getServiceAddUrl("SimpleStockQuoteService"));
        esbSendMediatorTest.saveEndpoint();
        esbCommon.mediatorUpdate();

        //Adding the Out mediator
        esbCommon.addMediators("Add Sibling","0","Filter","Out");

        //Adding the Log mediator
        esbCommon.addMediators("Add Child","1","Core","Log");
        esbLogMediatorTest.addLogMediator("1.0","Custom");
        esbLogMediatorTest.addLogPropety("text","Value","** Reporting to the Database **");
        esbCommon.mediatorUpdate();

        //Adding the DBReport mediator
        esbCommon.addMediators("Add Sibling","1.0","Advanced","DBReport");
        esbDBReportMediatorTest.addDBreportMediatorPoolInfo("1.1", "com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/esbdb", "root", "admin");
        esbCommon.mediatorUpdate();
        selenium.click("//a[@id='mediator-1.1']");
        esbDBReportMediatorTest.addDBreportMediatorSQLStatement("update company set price=? where name =?");
        esbDBReportMediatorTest.addDBLookupMediatorParameters("DOUBLE","Expression","//m0:return/m1:last/child::text()");
        esbDBReportMediatorTest.addDBReportParameterNamespase("m0", "http://services.samples");
        esbDBReportMediatorTest.addDBReportParameterNamespase("m1", "http://services.samples/xsd");
        esbDBReportMediatorTest.addDBLookupMediatorParameters("VARCHAR","Expression","//m0:return/m1:symbol/child::text()");
        esbDBReportMediatorTest.setParameter_Ns_no();
        esbDBReportMediatorTest.addDBReportParameterNamespase("m0", "http://services.samples");
        esbDBReportMediatorTest.addDBReportParameterNamespase("m1", "http://services.samples/xsd");
        esbCommon.mediatorUpdate();

        //Adding the Log mediator
        esbCommon.addMediators("Add Sibling","1.1","Core","Log");
        esbLogMediatorTest.addLogMediator("1.2","Custom");
        esbLogMediatorTest.setPropNo();
        esbLogMediatorTest.addLogPropety("text","Value","** Looking up from the Database **");
        esbCommon.mediatorUpdate();

        //Adding the DBLookup mediator
        esbCommon.addMediators("Add Sibling","1.2","Advanced","DBLookup");
        esbDBLookupMediatorTest.addDBLookupMediatorPoolInfo("1.3", "com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/esbdb", "root", "admin");
        esbCommon.mediatorUpdate();
        selenium.click("//a[@id='mediator-1.3']");
        esbDBLookupMediatorTest.addDBLookupMediatorSQLStatement("select * from company where name =?");
        esbDBLookupMediatorTest.addDBLookupMediatorParameters("VARCHAR","Expression","//m0:return/m1:symbol/child::text()");
        esbDBLookupMediatorTest.addDBRLookupParameterNamespase("m0", "http://services.samples");
        esbDBLookupMediatorTest.addDBRLookupParameterNamespase("m1", "http://services.samples/xsd");
        esbDBLookupMediatorTest.addDBRLookupResult("stock_price","price");
        esbCommon.mediatorUpdate();

        //Adding the Log mediator
        esbCommon.addMediators("Add Sibling","1.3","Core","Log");
        esbLogMediatorTest.addLogMediator("1.4","Custom");
        esbLogMediatorTest.setPropNo();
        esbLogMediatorTest.addLogPropety("text","Expression","fn:concat('Stock price - ',get-property('stock_price'))");
        esbCommon.mediatorUpdate();

        //Adding the Send mediator
        esbCommon.addMediators("Add Sibling","1.4","Core","Send");
        esbCommon.mediatorUpdate();

        esbCommon.sequenceSave();
    }


    public void testSetUpDB() throws Exception {
        try{
         FileInputStream file= new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
         properties.load(file);
         file.close();

         hostname = properties.getProperty("mysql.hostname");
         DBPort =properties.getProperty("mysql.DBPort");
         driverName = properties.getProperty("mysql.driverName");
         DBUserName = properties.getProperty("mysql.DBUserName");
         DBPassword = properties.getProperty("mysql.DBPassword");

         dburl = "jdbc:mysql://" + hostname + ":" + DBPort + "/" + DBName;

        }
        catch(Exception e){
                e.printStackTrace();
        }
        try {
            Class.forName(driverName);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            String url = "jdbc:mysql://" + hostname + ":" + DBPort;
            connection = DriverManager.getConnection(url, DBUserName, DBPassword);
            String create_database = "CREATE DATABASE " + DBName;
            Statement stmt = connection.createStatement();
            stmt.execute(create_database);
            stmt.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            Connection connect = DriverManager.getConnection(dburl, DBUserName, DBPassword);
            Statement stmt = connect.createStatement();
            String create_table_sql = "CREATE TABLE company(name VARCHAR(10), id VARCHAR(10), price DOUBLE)";
            String insert_row_sql1 = "INSERT INTO company(name, id, price) VALUES('IBM','c1',0.0) ";
            String insert_row_sql2 = "INSERT INTO company(name, id, price) VALUES('SUN','c2',0.0) ";
            String insert_row_sql3 = "INSERT INTO company(name, id, price) VALUES('MSFT','c3',0.0) ";

            stmt.executeUpdate(create_table_sql);
            stmt.executeUpdate(insert_row_sql1);
            stmt.executeUpdate(insert_row_sql2);
            stmt.executeUpdate(insert_row_sql3);
        } catch (SQLException e) {
        }
    }


//    public void testCreateSequence() throws Exception{
//        ESBCommon esbCommon = new ESBCommon(selenium);
//        esbCommon.logoutLogin();
//
//        esbCommon.setupMainSeq();
//        createSequence("sequence_DBReport_DBLookup");
//        //Setting the created sequence to the main sequence
//        esbCommon.setSequenceToSequence("main","sequence_DBReport_DBLookup");
//    }

    public void testinvokeClient() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBSampleClient esbSampleClient = new ESBSampleClient();

        boolean stockQuoteResponse = esbSampleClient.stockQuoteClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/", esbCommon.getServiceAddUrl("SimpleStockQuoteService"),"IBM");
        if (stockQuoteResponse){
            System.out.println("The response received!!!!");
        }else{
            throw new MyCheckedException("Client Failed!!!!");
        }
        Thread.sleep(5000);
       esbCommon.closeFiles();

    }

//    public void testUpdatedDB() throws Exception{
//        String priceSQL = "select price from company where name=\"IBM\"";
//        Connection connect = DriverManager.getConnection(dburl, DBUserName, DBPassword);
//        Statement stmt = connect.createStatement();
//        stmt.executeUpdate(priceSQL);
//        //System.out.println(stmt.executeUpdate(priceSQL));
//    }

    /*
     Deleting the database and the tables
    */
    public void testcleanDB() {
        Connection connect=null;
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            connect = DriverManager.getConnection(dburl, DBUserName, DBPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Statement s = connect.createStatement();
            s.executeUpdate("DROP TABLE company");
            s.executeUpdate("DROP DATABASE " + DBName);
        } catch (SQLException e) {
        }
    }
}


