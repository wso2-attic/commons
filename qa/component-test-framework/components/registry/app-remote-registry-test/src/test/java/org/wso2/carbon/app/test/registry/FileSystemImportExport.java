/*
 * Copyright 2004,2005 The Apache Software Foundation.
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

package org.wso2.carbon.app.test.registry;

import org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.registry.app.RemoteRegistry;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.utils.RegistryClientUtils;

import java.io.*;


public class FileSystemImportExport extends TestTemplate {
    public RemoteRegistry registry;

    @Override
    public void init() {
        InitializeAPI initializeAPI = new InitializeAPI();
        registry = initializeAPI.getRegistry(FrameworkSettings.CARBON_HOME, FrameworkSettings.HTTPS_PORT, FrameworkSettings.HTTP_PORT);
    }

    @Override
    public void runSuccessCase() {
        try {
            FileImportTest();
            FileExportTest();
            JarFileExportTest();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void runFailureCase() {

    }

    @Override
    public void cleanup() {

    }

    public void FileImportTest() throws RegistryException {

        String filePath = FrameworkSettings.CARBON_HOME + "/dbscripts";
        File file = new File(filePath);
        RegistryClientUtils.importToRegistry(file, "/framework", registry);
        assertTrue("Resource not found.", registry.resourceExists("/framework/dbscripts/db2.sql"));
        assertTrue("Resource not found.", registry.resourceExists("/framework/dbscripts/mysql.sql"));
        assertTrue("Resource not found.", registry.resourceExists("/framework/dbscripts/mssql.sql"));
        assertTrue("Resource not found.", registry.resourceExists("/framework/dbscripts/oracle.sql"));
        assertTrue("Resource not found.", registry.resourceExists("/framework/dbscripts/oracle_rac.sql"));
        assertTrue("Resource not found.", registry.resourceExists("/framework/dbscripts/postgresql.sql"));

        Resource r1 = registry.newResource();
        r1 = registry.get("/framework/dbscripts/mysql.sql");
        r1.getContent();
        String contain = new String((byte[]) r1.getContent());
        assertTrue("Resource contain not found", containString(contain, "CREATE"));

        r1 = registry.get("/framework/dbscripts/mssql.sql");
        r1.getContent();
        String containUm = new String((byte[]) r1.getContent());
        assertTrue("Resource contain not found", containString(containUm, "CREATE"));


        r1.discard();

        RegistryClientUtils.importToRegistry(file, "/framework", registry);

        Resource r2 = registry.newResource();
        r2 = registry.get("/framework/dbscripts/mysql.sql");
        r2.getContent();
        String contain2 = new String((byte[]) r2.getContent());
        assertTrue("Resource contain not found", containString(contain2, "CREATE"));

        r2 = registry.get("/framework/dbscripts/mssql.sql");
        r2.getContent();
        String containUm2 = new String((byte[]) r2.getContent());
        assertTrue("Resource contain not found", containString(containUm2, "CREATE"));


        String[] r1Versions12 = registry.getVersions("/framework/dbscripts/mssql.sql");
        assertTrue("Resource should have atleaset 1 version.", versionCount(r1Versions12));

        String[] r1Versions22 = registry.getVersions("/framework/dbscripts/mysql.sql");
        assertTrue("Resource should have atleaset 1 version.", versionCount(r1Versions22));

        r2.discard();

    }

    public void FileExportTest() throws RegistryException, FileNotFoundException {
        Process process;
        Runtime runTime = Runtime.getRuntime();
        String fileExportPath = FrameworkSettings.CARBON_HOME + "/export";

        File file = new File(FrameworkSettings.CARBON_HOME + "/export");
        String osName = "";
        try {
            osName = System.getProperty("os.name");
        } catch (Exception e) {
            System.out.println("Exception caught =" + e.getMessage());
        }

        if (osName.startsWith("Windows")) {
            try {
                process = runTime.exec("cmd.exe /C" + "" + "mkdir" + "" + fileExportPath, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                process = runTime.exec("mkdir" + " " + fileExportPath, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        RegistryClientUtils.exportFromRegistry(file, "/framework/", registry);

        File f = new File(FrameworkSettings.CARBON_HOME + "/export/dbscripts/h2.sql");
        assertTrue("File doesn't exist at the location", f.exists());

        File f1 = new File(FrameworkSettings.CARBON_HOME + "/export/dbscripts/mysql.sql");
        assertTrue("File doesn't exist at the location", f1.exists());

        assertTrue("Resource contain not found", fileContainString(FrameworkSettings.CARBON_HOME + "/export/dbscripts/oracle.sql", "CREATE"));
        assertTrue("Resource contain not found", fileContainString(FrameworkSettings.CARBON_HOME + "/export/dbscripts/h2.sql", "CREATE"));

        if (osName.startsWith("Windows")) {
            try {
                process = runTime.exec("cmd.exe /C" + "" + "del" + "" + fileExportPath, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                process = runTime.exec("rm -rf" + " " + fileExportPath, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void JarFileExportTest() throws RegistryException, FileNotFoundException {

        String jarFileName = "tcpmon-1.0.jar";
        String filePath =  FrameworkSettings.CARBON_HOME + "/lib/" + jarFileName;
        File file = new File(filePath);
        RegistryClientUtils.importToRegistry(file, "/framework", registry);

        assertTrue("Resource not found.", registry.resourceExists("/framework/" + jarFileName));

        Process process;
        Runtime runTime = Runtime.getRuntime();
        String fileExportPath = FrameworkSettings.CARBON_HOME + "/export";
        String osName = "";
        if (osName.startsWith("Windows")) {
            try {
                process = runTime.exec("cmd.exe /C" + "" + "mkdir" + "" + fileExportPath, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                process = runTime.exec("mkdir" + " " + fileExportPath, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        File file2 = new File(FrameworkSettings.CARBON_HOME + "/export");
        RegistryClientUtils.exportFromRegistry(file2, "/framework", registry);

        File f1 = new File(FrameworkSettings.CARBON_HOME + "/export/" + jarFileName);
        assertTrue("File doesn't exist at the location", f1.exists());

        if (osName.startsWith("Windows")) {
            try {
                process = runTime.exec("cmd.exe /C" + "" + "del" + "" + fileExportPath, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                process = runTime.exec("rm -rf" + " " + fileExportPath, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean containString(String str, String pattern) {
        int s = 0;
        int e = 0;
        boolean value = false;

        while ((e = str.indexOf(pattern, s)) >= 0) {
            value = true;
            return value;

        }
        return value;
    }

    public static boolean versionCount(String r1Versions[]) {
        boolean versionCount = false;
        //System.out.println("version length" + r1Versions.length);
        if (r1Versions.length >= 1) {
            versionCount = true;
        }
        return versionCount;
    }

    public static String slurp(InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1;) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }

    public static boolean fileContainString(String path, String pattern) throws FileNotFoundException {
        String st = null;
        boolean valuefile = false;
        InputStream is = new BufferedInputStream(new FileInputStream(path));
        try {
            st = slurp(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (containString(st, pattern)) {
            valuefile = true;
        }
        return valuefile;
    }
}
