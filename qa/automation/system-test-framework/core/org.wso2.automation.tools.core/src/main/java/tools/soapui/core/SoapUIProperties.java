package tools.soapui.core;

public class SoapUIProperties {

    private String _projectFile = null;
    private String _testSuite = null;
    private String _testCase = null;
    private String _username = null;
    private String _password = null;
    private String _domain = null;
    private String _host = null;
    private String _endpoint = null;
    private String _outputFolder = null;
    private String _settingsFile = null;
    private String _path = null;
    private String _port = null;


    public void setProject(String projectFile, String outPutFolder) {
        _projectFile = projectFile;
        _outputFolder = outPutFolder;
    }

    public String getProjectFile() {
        return _projectFile;
    }

    public String getOutputFolder() {
        return _outputFolder;
    }


    public String getTestSuite() {
        return _testSuite;
    }

    public String getTestCase() {
        return _testCase;
    }

    public String getUsername() {
        return _username;
    }

    public String getPassword() {
        return _password;
    }

    public String getDomain() {
        return _domain;
    }

    public String getHost() {
        return _host;
    }

    public String getEndpoint() {
        return _endpoint;
    }

    public String getSettingsFile() {
        return _settingsFile;
    }

    public String getPath() {
        return _path;
    }

    public String getPort() {
        return _port;
    }


}
