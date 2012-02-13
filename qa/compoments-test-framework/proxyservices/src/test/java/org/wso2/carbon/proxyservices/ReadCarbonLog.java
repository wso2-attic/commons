package org.wso2.carbon.proxyservices;

import java.io.*;


/**
 * Created by IntelliJ IDEA.
 * User: chamara
 * Date: Jun 21, 2010
 * Time: 10:55:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReadCarbonLog {
    ReadPropertyFile readProperty = new ReadPropertyFile();

    public void CleanCarbonLog() throws Exception {
        readProperty.getProperty();
        BufferedWriter writer = new BufferedWriter(new FileWriter(readProperty.ESB_HOME.toString() + File.separator + "repository" + File.separator + "logs" + File.separator + "wso2-esb.log"));
        writer.write(" ");
        writer.newLine();
        writer.close();
    }

    public boolean readServerLog(String searchString) throws Exception {

        readProperty.getProperty();
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        boolean flag = false;

        File file = new File(readProperty.ESB_HOME + File.separator + "repository" + File.separator + "logs" + File.separator + "wso2-esb.log");

        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            while (dis.available() != 0) {
                String currentLine = dis.readLine();
                if (currentLine.toString().contains(searchString.toString())) {
                    //System.out.println("Search word found");
                    flag = true;
                    break;
                } else {
                    //System.out.println("Search word not found");
                    flag = false;
                }
            }
            // dispose all the resources after using them.
            fis.close();
            bis.close();
            dis.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }
}
