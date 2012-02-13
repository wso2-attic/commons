package org.wso2.carbon.tenant.mgt.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 */
public class PropertyLoader {
    public static String SUPER_TENANT_UNAME;
    public static String SUPER_TENANT_PASSWORD;
    public static String CLOUD_NAME;
    public static int TENANT_COUNT;


    public static void loadProperty() throws IOException {
        Properties prop = new Properties();
        FileInputStream fReader = new FileInputStream(PropertyLoader.class.getResource("/test.properties").getPath());
        prop.load(fReader);
        fReader.close();
        SUPER_TENANT_UNAME = (prop.getProperty("super.username"));
        SUPER_TENANT_PASSWORD = (prop.getProperty("super.password"));
        CLOUD_NAME = (prop.getProperty("cloud.name"));
        TENANT_COUNT = Integer.parseInt(prop.getProperty("tenant.count"));
    }
}
