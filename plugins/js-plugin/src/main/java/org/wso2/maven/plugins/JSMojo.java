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
package org.wso2.maven.plugins;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * This Mojo will create an single js file based on js'es in a given folder.
 * This default name of this js will be all.js
 *
 * @goal sum
 * @phase package
 */
public class JSMojo extends AbstractMojo {

    /**
     * inputDirs will contain the  root directories of js files. All the js file within that
     * directory will be cumulated into single all.js file
     *
     * @parameter
     * @required
     */
    private File[] inputDirs;

    /**
     * Exclustions
     *
     * @parameter
     */
    private String[] exclusions = new String[0];

    public void execute() throws MojoExecutionException, MojoFailureException {
        List dirsList = Arrays.asList(inputDirs);
        try {
            for (Iterator iterator = dirsList.iterator(); iterator.hasNext();) {
                File file = (File) iterator.next();
                cumulateJs(file);
            }
        } catch (IOException e) {
            throw new MojoExecutionException(
                    "Erorr occured when cumulating the contents of Js files", e);
        }

    }

    private void cumulateJs(File file) throws IOException {
        final List exclusionList = Arrays.asList(exclusions);
        File[] jsFiles = file.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.getName().endsWith(".js") && !exclusionList.contains(file.getName());
            }
        });
        OutputStream out = new FileOutputStream(new File(file, "all.js"));
        List jsFilesList = Arrays.asList(jsFiles);
        for (Iterator iterator = jsFilesList.iterator(); iterator.hasNext();) {
            File jsFile = (File) iterator.next();
            if (jsFile.isFile() && jsFile.getName().equals("init.js")) {
                InputStream in = new FileInputStream(jsFile);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
            }
        }
        for (Iterator iterator = jsFilesList.iterator(); iterator.hasNext();) {
            File jsFile = (File) iterator.next();
            if (jsFile.isFile() && jsFile.getName().equals("init.js")) {
                continue;
            }
            InputStream in = new FileInputStream(jsFile);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
        }
        out.close();
    }
}
