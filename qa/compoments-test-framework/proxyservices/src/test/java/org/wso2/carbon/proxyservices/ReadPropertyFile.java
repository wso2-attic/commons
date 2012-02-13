package org.wso2.carbon.proxyservices;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: chamara
 * Date: Jun 23, 2010
 * Time: 11:29:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReadPropertyFile {
    public String ESB_HOME;
    public String AXIS2C_HOME;
    public String AXIS2S_XML;
    public String HOST_NAME;
    public String HTTP_PORT;
    public String HTTPS_PORT;
    public String AXIS2S_HOME;

    public void getProperty() throws IOException {

        File filePath = new File("./");
        String relativePath = filePath.getCanonicalPath();
        Properties prop = new Properties();
        if (relativePath.endsWith("proxyservices")) {
            relativePath = relativePath.replaceAll("proxyservices", "");
        }
        FileInputStream fReader = new FileInputStream(relativePath + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
        prop.load(fReader);
        fReader.close();
        ESB_HOME = (prop.getProperty("esb.home"));
        HOST_NAME = (prop.getProperty("host.name"));
        HTTPS_PORT = (prop.getProperty("https.port"));
        HTTP_PORT = (prop.getProperty("http.port"));
        return;

    }
}
