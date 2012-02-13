/*
 * Copyright (c) 2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package tools.jmeter.core;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.jmeter.JMeter;
import org.apache.jmeter.util.ShutdownClient;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.tools.ant.DirectoryScanner;
//import org.wso2.automation.tools.jmeter.core.JmeterProperties;
//import org.wso2.automation.tools.jmeter.core.JmeterResults;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class JmeterCore extends AbstractMojo {

    private static final Pattern PAT_ERROR = Pattern.compile(".*\\s+ERROR\\s+.*");


    private String jmeterLogLevel;


    private String includeFile;
    private List<String> includes;
    private List<String> excludes;

    private boolean remote;
    private String proxyHost;
    private Integer proxyPort;
    private String proxyUsername;
    private String proxyPassword;
    private File testDir;
    private File reportDir;
    private File workDir;
    private File jmeterLog;

    private Map jmeterUserProperties;
    private Map jmeterJavaProperties;
    private DateFormat fmt = new SimpleDateFormat("yyMMdd");
    private File upgradeProps;
    private JMeter jmeterInstance;

    private String resourcePath = System.getProperty("user.dir") + File.separator + "core"
            + File.separator + "tools.test.core" + File.separator + "src" + File.separator + "main" + File.separator + "java"
            + File.separator + "org" + File.separator + "wso2" + File.separator + "automation" + File.separator + "tools" + File.separator + "jmeter" + File.separator
            + "core" + File.separator + "resources" + File.separator;
    private File jmeterProps = new File(resourcePath + "jmeter.properties");
    private File saveServiceProps = new File(resourcePath + "saveservice.properties");

    public  List<JmeterResults> runTest(JmeterProperties jmeterProperties) {
         List<JmeterResults> results = new LinkedList<JmeterResults>();
        includeFile = jmeterProperties.getIncludeFile();
        jmeterLogLevel = jmeterProperties.getLogLevel();
        includeFile = jmeterProperties.getIncludeFile();
        includes = jmeterProperties.getIncludes();
        excludes = jmeterProperties.getExcludes();
        remote = jmeterProperties.getRemote();
        proxyHost = jmeterProperties.getProxyHost();
        proxyPort = jmeterProperties.getProxyPort();
        proxyUsername = jmeterProperties.getProxyUserName();
        proxyPassword = jmeterProperties.getProxyPassword();
        testDir = jmeterProperties.getTestDir();
        reportDir = jmeterProperties.getReportDir();
        workDir = jmeterProperties.getWorkingDir();
        jmeterLog = jmeterProperties.getJmeterLogFile();
        try {
          results=  executeMe();
        } catch (MojoExecutionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MojoFailureException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return results;
    }

    private  List<JmeterResults> executeMe() throws MojoExecutionException, MojoFailureException {
        initSystemProps();
        Boolean resultState= true;
        List<JmeterResults> resultList = new LinkedList<JmeterResults>();
        try {
            DirectoryScanner scanner = new DirectoryScanner();
            scanner.setBasedir(testDir);
            if (null != includeFile) {
                if (null != includes) {
                    getLog().debug("Overwriting configured includes list by includefiles = '" + includeFile + "'");
                } else {
                    getLog().debug("Using includefiles = '" + includeFile + "'");
                }
                scanner.setIncludes(new String[]{includeFile});
            } else if (null == includes) {
                getLog().debug("Using default includes");
                scanner.setIncludes(new String[]{"**/*.jmx"});
            } else {
                getLog().debug("Using configured includes");
                scanner.setIncludes(includes.toArray(new String[]{}));
            }
            if (excludes != null) {
                scanner.setExcludes(excludes.toArray(new String[]{}));
            }
            scanner.scan();
            String[] finalIncludes = scanner.getIncludedFiles();
            getLog().debug("Finally using test files" + StringUtils.join(finalIncludes, ", "));
            for (String file : finalIncludes) {
                JmeterResults results = new JmeterResults();
                File testFile = new File(testDir, file);
                String result = executeTest(new File(testDir, file));
                try {
                    // Force shutdown
                    ShutdownClient.main(new String[]{"Shutdown"});
                } catch (IOException ex) {
                    getLog().error(ex);
                    resultState = false;
                }
                try {
                    results._assertReport =resultValidater(result);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    resultState= false;
                }
                results._fileName = testDir + File.separator + file;
                results._testPlan = testFile.getName().substring(0, testFile.getName().lastIndexOf("."));
                results._executionState = resultState;
                resultList.add(results);
            }
            checkForErrors();
        } finally {
            saveServiceProps.delete();
            upgradeProps.delete();
        }
        return resultList;
    }

    private void checkForErrors() throws MojoExecutionException, MojoFailureException {
        try {
            BufferedReader in = new BufferedReader(new FileReader(jmeterLog));
            String line;
            while ((line = in.readLine()) != null) {
                if (PAT_ERROR.matcher(line).find()) {
                    throw new MojoFailureException("There were test errors, see logfile '" + jmeterLog + "' for further information");
                }
            }
            in.close();
        } catch (IOException e) {
            throw new MojoExecutionException("Can't read log file", e);
        }
    }

    private void initSystemProps() throws MojoExecutionException {

        // Init JMeter
        jmeterInstance = new JMeter();
        workDir = new File("target" + File.separator + "jmeter");
        workDir.mkdirs();
        createSaveServiceProps();

        // now create lib dir for jmeter fallback mode
        File libDir = new File("target" + File.separator + "jmeter" + File.separator + "lib");
        if (!libDir.exists()) {
            libDir.mkdirs();
            libDir = new File("target" + File.separator + "jmeter"
                    + File.separator + "lib" + File.separator + "ext");
            if (!libDir.exists()) {
                libDir.mkdirs();
            }
            libDir = new File("target" + File.separator + "jmeter"
                    + File.separator + "lib" + File.separator + "junit");
            if (!libDir.exists()) {
                libDir.mkdirs();
            }
        }

        // jmeterLog = new File(workDir, "jmeter.log");
        try {
            System.setProperty("log_file", jmeterLog.getCanonicalPath());
        } catch (IOException e) {
            throw new MojoExecutionException("Can't get canonical path for log file", e);
        }
    }

    private void createSaveServiceProps() throws MojoExecutionException {

        File binDir = new File("target" + File.separator + "jmeter" + File.separator + "bin");
        if (!binDir.exists()) {
            binDir.mkdirs();
        }
        saveServiceProps = new File(binDir, "saveservice.properties");
        upgradeProps = new File(binDir, "upgrade.properties");

        FileWriter out;

        try {

            out = new FileWriter(saveServiceProps);
            IOUtils.copy(Thread.currentThread().getContextClassLoader().getResourceAsStream("saveservice.properties"), out);
            out.flush();
            out.close();
            System.setProperty("saveservice_properties",
                    File.separator + "bin" + File.separator
                            + "saveservice.properties");
        } catch (IOException e) {
            throw new MojoExecutionException("Could not create temporary saveservice.properties", e);
        }

        try {

            out = new FileWriter(upgradeProps);
            IOUtils.copy(Thread.currentThread().getContextClassLoader().getResourceAsStream("upgrade.properties"), out);
            out.flush();
            out.close();
            System.setProperty("upgrade_properties",
                    File.separator + "bin" + File.separator
                            + "upgrade.properties");

        } catch (IOException e) {
            throw new MojoExecutionException("Could not create temporary upgrade.properties", e);
        }

        try {
            // if the properties file is not specified in the papameters
            if (jmeterProps == null) {

                getLog().info("Loading default jmeter.properties...");

                jmeterProps = new File(binDir, "jmeter.properties");

                out = new FileWriter(jmeterProps);
                IOUtils.copy(Thread.currentThread().getContextClassLoader().getResourceAsStream("jmeter.properties"), out);
                out.flush();
                out.close();
                System.setProperty("jmeter_properties",
                        File.separator + "bin" + File.separator
                                + "jmeter.properties");

            }

        } catch (IOException e) {
            throw new MojoExecutionException("Could not create temporary upgrade.properties", e);
        }
    }

    private String executeTest(File test) throws MojoExecutionException {
        String reportFileName;
        String reportFileFullPath;
        try {
            getLog().info("Executing test: " + test.getCanonicalPath());
            reportFileName = test.getName().substring(0,
                    test.getName().lastIndexOf(".")) + "-"
                    + fmt.format(new Date()) + "jmeterResult" + ".xml";
            System.out.println(reportFileName);
            reportFileFullPath = reportDir.toString() + File.separator + reportFileName;
            List<String> argsTmp = Arrays.asList("-n",
                    "-t", test.getCanonicalPath(),
                    "-l", reportDir.toString() + File.separator + reportFileName,
                    "-p", jmeterProps.toString(),
                    "-d", System.getProperty("user.dir") + File.separator
                    + "target" + File.separator + "jmeter",
                    "-L", "jorphan=" + jmeterLogLevel,
                    "-L", "jmeter.util=" + jmeterLogLevel);

            List<String> args = new ArrayList<String>();

            args.addAll(argsTmp);
            args.addAll(getUserProperties());
            args.addAll(getJavaProperties());

            if (remote) {
                args.add("-r");
            }

            if (proxyHost != null && !proxyHost.equals("")) {
                args.add("-H");
                args.add(proxyHost);
                args.add("-P");
                args.add(proxyPort.toString());
                getLog().info("Setting HTTP proxy to " + proxyHost + ":" + proxyPort);
            }

            if (proxyUsername != null && !proxyUsername.equals("")) {
                args.add("-u");
                args.add(proxyUsername);
                args.add("-a");
                args.add(proxyPassword);
                getLog().info("Logging with " + proxyUsername + ":" + proxyPassword);
            }

            SecurityManager oldManager = System.getSecurityManager();

            UncaughtExceptionHandler oldHandler = Thread.getDefaultUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {

                public void uncaughtException(Thread t, Throwable e) {
                    if (e instanceof ExitException && ((ExitException) e).getCode() == 0) {
                        return;    //Ignore
                    }
                    getLog().error("Error in thread " + t.getName());
                }
            });

            try {
                logParamsAndProps(args);

                jmeterInstance.start(args.toArray(new String[]{}));

                BufferedReader in = new BufferedReader(new FileReader(jmeterLog));
                while (!checkForEndOfTest(in)) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }

            } catch (ExitException e) {
                if (e.getCode() != 0) {
                    throw new MojoExecutionException("Test failed", e);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());

            } finally {
                System.setSecurityManager(oldManager);
                Thread.setDefaultUncaughtExceptionHandler(oldHandler);
            }
        } catch (IOException e) {
            throw new MojoExecutionException("Can't execute test", e);
        }
        return reportFileFullPath;
    }

    private void logParamsAndProps(List<String> args) {
        getLog().debug("Starting JMeter with the following parameters:");
        for (String arg : args) {
            getLog().debug(arg);
        }
        Properties props = System.getProperties();
        Set<Object> keysUnsorted = props.keySet();
        SortedSet<Object> keys = new TreeSet<Object>(keysUnsorted);
        getLog().debug("... and the following properties:");
        for (Object k : keys) {
            String key = (String) k;
            String value = props.getProperty(key);
            getLog().debug(key + " = " + value);
        }
    }

    private Map resultValidater(String fileName) throws FileNotFoundException {
        boolean result = true;

        OMElement documentElement;
        FileInputStream inputStream;
        File file = new File(fileName);
        Map<String, String> serviceLoginStatus = new Hashtable<String, String>();

        if (file.exists()) {
            try {
                inputStream = new FileInputStream(file);
                XMLStreamReader parser = XMLInputFactory.newInstance().createXMLStreamReader(inputStream);

                StAXOMBuilder builder = new StAXOMBuilder(parser);
                documentElement = builder.getDocumentElement();

                OMElement omName = null;
                OMElement omFailure = null;
                OMElement omError = null;
                OMElement omfailureMessage = null;

                for (Iterator itrAssertionResult = documentElement.getChildrenWithName(new QName("httpSample")); itrAssertionResult.hasNext(); ) {
                    OMElement omDocument = (OMElement) itrAssertionResult.next();

                    for (Iterator itrResult = omDocument.getChildrenWithName(new QName("assertionResult")); itrResult.hasNext(); ) {
                        OMElement omDocumentResults = (OMElement) itrResult.next();

                        for (Iterator nameItr = omDocumentResults.getChildrenWithName(new QName("name")); nameItr.hasNext(); ) {
                            omName = (OMElement) nameItr.next();
                            System.out.println("XXX" + (omName.getText()));
                        }

                        for (Iterator Itrfailure = omDocumentResults.getChildrenWithName(new QName("failure")); Itrfailure.hasNext(); ) {
                            omFailure = (OMElement) Itrfailure.next();
                        }

                        for (Iterator errorItr = omDocumentResults.getChildrenWithName(new QName("error")); errorItr.hasNext(); ) {
                            omError = (OMElement) errorItr.next();
                            System.out.println((omError.toString()));
                        }

                        for (Iterator failureMessageItr = omDocumentResults.getChildrenWithName(new QName("failureMessage")); failureMessageItr.hasNext(); ) {
                            omfailureMessage = (OMElement) failureMessageItr.next();
                            System.out.println((omfailureMessage.toString()));
                        }

                        if (((omFailure.toString()).contains("true")) || (omError.toString().contains("true"))) {
                            serviceLoginStatus.put(omName.getText(), omfailureMessage.getText());
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (XMLStreamException e) {
                e.printStackTrace();
            }
        }
        else{
            throw new FileNotFoundException("Result File is not Created");
        }
        return serviceLoginStatus;
    }

    private boolean checkForEndOfTest(BufferedReader in) throws MojoExecutionException {
        boolean testEnded = false;
        try {
            String line;
            while ((line = in.readLine()) != null) {
                if (line.indexOf("Test has ended") != -1) {
                    testEnded = true;
                    break;
                }
            }

        } catch (IOException e) {
            throw new MojoExecutionException("Can't read log file", e);
        }
        return testEnded;
    }


    private ArrayList<String> getUserProperties() {
        ArrayList<String> propsList = new ArrayList<String>();
        if (jmeterUserProperties == null) {
            return propsList;
        }
        Set<String> keySet = (Set<String>) jmeterUserProperties.keySet();

        for (String key : keySet) {

            propsList.add("-J");
            propsList.add(key + "=" + jmeterUserProperties.get(key));
        }

        return propsList;
    }

    private ArrayList<String> getJavaProperties() {
        ArrayList<String> propsList = new ArrayList<String>();
        if (jmeterJavaProperties == null) {
            return propsList;
        }
        Set<String> keySet = (Set<String>) jmeterJavaProperties.keySet();

        for (String key : keySet) {

            propsList.add("-D");
            propsList.add(key + "=" + jmeterJavaProperties.get(key));
        }

        return propsList;
    }

    public void execute() throws MojoExecutionException, MojoFailureException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private static class ExitException extends SecurityException {

        private static final long serialVersionUID = 5544099211927987521L;
        public int _rc;

        public ExitException(int rc) {
            super(Integer.toString(rc));
            _rc = rc;
        }

        public int getCode() {
            return _rc;
        }
    }
}
