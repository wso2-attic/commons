package org.wso2.bpel;

import java.io.*;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.util.Enumeration;


public class Util {
    public static void main(String[] args) {
        unzip("E:\\Projects\\LSF\\Axis2\\Axis-trunk\\bepl/ode-axis2-war-1.1.1.zip");
    }
    private static void copyInputStream(InputStream in, OutputStream out)
            throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) >= 0)
            out.write(buffer, 0, len);

        in.close();
        out.close();
    }

    public static  boolean unzip(String fileName) {
        Enumeration entries;
        ZipFile zipFile;
        try {
            zipFile = new ZipFile(fileName);
            String zipFileName;
            int dotIndex = fileName.lastIndexOf(".");
            String unzipFileName = fileName.substring(0,dotIndex);
            File tempFile = new File(unzipFileName);
            if (tempFile.exists()) {
                return false;
            }


            int index =fileName.lastIndexOf(File.separator);
            zipFileName = fileName.substring(0,index);

            entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();

                if (entry.isDirectory()) {
                    // Assume directories are stored parents first then children.
                    System.err.println("Extracting directory: " + entry.getName());
                    // This is not robust, just for demonstration purposes.
                    (new File(zipFileName + "/" + entry.getName())).mkdirs();
                    continue;
                }

                System.err.println("Extracting file: " + zipFileName + "/" + entry.getName());
                copyInputStream(zipFile.getInputStream(entry),
                        new BufferedOutputStream(new FileOutputStream(zipFileName + "/" +entry.getName())));
            }
            zipFile.close();
        } catch (IOException ioe) {
            System.err.println("Unhandled exception:");
            ioe.printStackTrace();
        }
        return true;
    }
}
