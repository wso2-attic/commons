package tools.jmeter.core;

import java.io.File;
import java.util.List;

public class JmeterProperties {
    public String _jmeterLogLevel = "1";
    public String _includeFile= null;
    public List<String> _includes= null;
    public List<String> _excludes= null;
    public boolean _remote;
    private String _proxyHost= null;
    private int _proxyPort;
    private String _proxyUsername= null;
    private String _proxyPassword= null;
    private File _testDir= null;
    private File _reportDir= null;
    private File _workDir= null;
    private File _jmeterLog= null;


    public void setProxy(String hostName, int port, String userName, String password) {
        _proxyHost = hostName;
        _proxyPort = port;
        _proxyUsername = userName;
        _proxyPassword = password;
    }

    public void setProperty(String testDir, String workingDir, String reportDir, String logFile) {
        _testDir = new File(testDir);
        _workDir = new File(workingDir);
        _reportDir = new File(reportDir);
        _jmeterLog = new File(logFile);
    }

    public String getLogLevel() {
        return _jmeterLogLevel;
    }

    public String getIncludeFile() {
        return _includeFile;
    }

    public List<String> getIncludes() {
        return _includes;
    }

    public List<String> getExcludes() {
        return _excludes;
    }

    public boolean getRemote() {
        return _remote;
    }

    public String getProxyHost() {
        return _proxyHost;
    }

    public int getProxyPort() {
        return _proxyPort;
    }

    public String getProxyUserName() {
        return _proxyUsername;
    }

    public String getProxyPassword() {
        return _proxyPassword;
    }

    public File getTestDir() {
        return _testDir;
    }

    public File getWorkingDir() {
        return _workDir;
    }

    public File getReportDir() {
        return _reportDir;
    }

    public File getJmeterLogFile() {
        return _jmeterLog;
    }

}
