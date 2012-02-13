package tools.soapui.core;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.tools.*;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
//import org.wso2.automation.tools.soapui.core.SoapUIProperties;

import java.io.File;
import java.util.Properties;


public class SoapUICore extends AbstractMojo {

    private String projectFile;
    private String testSuite;
    private String testCase;
    private String username;
    private String password;
    private String wssPasswordType;
    private String domain;
    private String host;
    private String endpoint;
    private String outputFolder;

    private String settingsFile;
    private boolean skip;
    private String projectPassword;
    private String settingsPassword;
    private boolean testFailIgnore;

    private boolean saveAfterRun;
    private Properties soapuiProperties;
    private String mockService = null;
    private String path = null;
    private String port = null;

    private String resourcePath = System.getProperty("user.dir") + File.separator + "core"
            + File.separator + "tools.test.core" + File.separator + "src" + File.separator + "main" + File.separator + "java"
            + File.separator + "org" + File.separator + "wso2" + File.separator + "automation" + File.separator + "tools" + File.separator + "soapui" + File.separator
            + "core" + File.separator + "resources" + File.separator;

    public void execute() throws MojoExecutionException, MojoFailureException {
        SoapUIProperties properties = new SoapUIProperties();
        settingsFile = resourcePath + "soapui-settings.xml";
        projectFile = properties.getProjectFile();
        outputFolder = properties.getOutputFolder();
        SoapUITestCaseRunner testRunner = new SoapUITestCaseRunner();
        testRunner.setProjectFile(projectFile);
        testRunner.setOutputFolder(outputFolder);
        runTestCase(testRunner);
        SoapUILoadTestRunner loadTestRunner = new SoapUILoadTestRunner();
        loadTestRunner.setProjectFile(projectFile);
        loadTestRunner.setOutputFolder(outputFolder);
        runLoadTest(loadTestRunner);
        SoapUIMockServiceRunner mockRunner = new SoapUIMockServiceRunner();
        mockRunner.setProjectFile(projectFile);
        mockRunner.setOutputFolder(outputFolder);
        runMockTest(mockRunner);
        SoapUISecurityTestRunner securityTestRunner = new SoapUISecurityTestRunner();
        securityTestRunner.setProjectFile(projectFile);
        securityTestRunner.setOutputFolder(outputFolder);
        runSecurityTest(securityTestRunner);
    }

    private void runTestCase(SoapUITestCaseRunner runner) throws MojoFailureException {

        runner.setPrintReport(true);
        runner.setExportAll(true);
        runner.setJUnitReport(true);
        runner.setSaveAfterRun(true);
        runner.getLog();
        runner.setSettingsFile(settingsFile);


        if (soapuiProperties != null && soapuiProperties.size() > 0)
            for (Object key : soapuiProperties.keySet()) {
                System.out.println("Setting " + (String) key + " value " + soapuiProperties.getProperty((String) key));
                System.setProperty((String) key, soapuiProperties.getProperty((String) key));
            }

        try {
            runner.run();
            System.out.println(runner.getLog());
        } catch (Exception e) {
            getLog().error(e.toString());
            // throw new MojoFailureException(this, "SoapUI Test(s) failed", e.getMessage());
        }
    }

    private void runLoadTest(SoapUILoadTestRunner runner) throws MojoExecutionException, MojoFailureException {

        if (endpoint != null)
            runner.setEndpoint(endpoint);

        if (testSuite != null)
            runner.setTestSuite(testSuite);

        if (testCase != null)
            runner.setTestCase(testCase);

        if (username != null)
            runner.setUsername(username);

        if (password != null)
            runner.setPassword(password);

        if (wssPasswordType != null)
            runner.setWssPasswordType(wssPasswordType);

        if (domain != null)
            runner.setDomain(domain);


        if (host != null)
            runner.setHost(host);

        runner.setSaveAfterRun(saveAfterRun);
        runner.setSettingsFile(settingsFile);

        if (projectPassword != null)
            runner.setProjectPassword(projectPassword);

        if (settingsPassword != null)
            runner.setSoapUISettingsPassword(settingsPassword);


        if (soapuiProperties != null && soapuiProperties.size() > 0)
            for (Object key : soapuiProperties.keySet()) {
                System.out.println("Setting " + (String) key + " value " + soapuiProperties.getProperty((String) key));
                System.setProperty((String) key, soapuiProperties.getProperty((String) key));
            }

        try {
            runner.run();
        } catch (Throwable e) {
            getLog().error(e.toString());
            throw new MojoFailureException(this, "SoapUI LoadTest(s) failed", e.getMessage());
        }
    }

    public void runMockTest(SoapUIMockServiceRunner runner) throws MojoExecutionException, MojoFailureException {

        if (mockService != null)
            runner.setMockService(mockService);


        if (path != null)
            runner.setPath(path);

        if (port != null)
            runner.setPort(port);

        if (settingsFile != null)
            runner.setSettingsFile(settingsFile);


        runner.setSaveAfterRun(saveAfterRun);

        if (projectPassword != null)
            runner.setProjectPassword(projectPassword);

        if (settingsPassword != null)
            runner.setSoapUISettingsPassword(settingsPassword);


        if (soapuiProperties != null && soapuiProperties.size() > 0)
            for (Object key : soapuiProperties.keySet()) {
                System.out.println("Setting " + (String) key + " value " + soapuiProperties.getProperty((String) key));
                System.setProperty((String) key, soapuiProperties.getProperty((String) key));
            }

        try {
            runner.run();
        } catch (Exception e) {
            getLog().error(e.toString());
            throw new MojoFailureException(this, "SoapUI MockService(s) failed", e.getMessage());
        }
    }

    private void runSecurityTest(SoapUISecurityTestRunner runner) throws MojoExecutionException, MojoFailureException {


        runner.setProjectFile(projectFile);

        if (endpoint != null)
            runner.setEndpoint(endpoint);

        if (testSuite != null)
            runner.setTestSuite(testSuite);

        if (testCase != null)
            runner.setTestCase(testCase);

        if (username != null)
            runner.setUsername(username);

        if (password != null)
            runner.setPassword(password);

        if (wssPasswordType != null)
            runner.setWssPasswordType(wssPasswordType);

        if (domain != null)
            runner.setDomain(domain);

        if (host != null)
            runner.setHost(host);

        if (outputFolder != null)
            runner.setOutputFolder(outputFolder);


        runner.setExportAll(true);
        runner.setJUnitReport(true);
        runner.setEnableUI(false);
        runner.setIgnoreError(testFailIgnore);
        runner.setSaveAfterRun(saveAfterRun);


        if (soapuiProperties != null && soapuiProperties.size() > 0)
            for (Object key : soapuiProperties.keySet()) {
                System.out.println("Setting " + (String) key + " value " + soapuiProperties.getProperty((String) key));
                System.setProperty((String) key, soapuiProperties.getProperty((String) key));
            }

        String securityTest = null;
        if (securityTest != null && securityTest.length() > 0)
            runner.setSecurityTestName(securityTest);

        try {
            runner.run();
        } catch (Exception e) {
            getLog().error(e.toString());
            throw new MojoFailureException(this, "SoapUI Test(s) failed", e.getMessage());
        }
    }

    private void runTestTools() throws MojoExecutionException, MojoFailureException {

        SoapUIToolRunner runner = new SoapUIToolRunner("soapUI " + SoapUI.SOAPUI_VERSION + " Maven2 Tool Runner");
        runner.setProjectFile(projectFile);

        String iface = null;
        String tool = null;
        if (iface != null)
            runner.setInterface(iface);


        if (tool != null)
            runner.setTool(tool);

        if (settingsFile != null)
            runner.setSettingsFile(settingsFile);


        if (outputFolder != null)
            runner.setOutputFolder(outputFolder);

        if (soapuiProperties != null && soapuiProperties.size() > 0)
            for (Object key : soapuiProperties.keySet()) {
                System.out.println("Setting " + (String) key + " value " + soapuiProperties.getProperty((String) key));
                System.setProperty((String) key, soapuiProperties.getProperty((String) key));
            }

        try {
            runner.run();
        } catch (Exception e) {
            getLog().error(e.toString());
            throw new MojoFailureException(this, "SoapUI Tool(s) failed", e.getMessage());
        }
    }


}
