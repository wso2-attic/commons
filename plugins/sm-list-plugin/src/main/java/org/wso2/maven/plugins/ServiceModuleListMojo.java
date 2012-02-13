/*
 * Copyright 2005-2007 WSO2, Inc. (http://wso2.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.maven.plugins;

import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 * This Mojo will create two seperate files; services.list and  modules.list based on
 * the requiresDependencyResolution
 *
 * @requiresDependencyResolution
 * @goal write
 * @phase package
 */
public class ServiceModuleListMojo extends AbstractMojo {

    /**
     * The set of dependencies required by the project
     *
     * @parameter default-value="${project.dependencies}"
     * @readonly
     */
    private List dependencies;

    /**
     * The output directory of the assembled distribution file.
     *
     * @parameter default-value="${project.build.directory}/sm/"
     * @parameter default-value="${project.build.directory}"
     */
    private File outputDirectory;

    /**
     * Hold the names of the extensions that needed to be added to the services.list.
     * @parameter
     */
    private List extAarNames;

    /**
     * Hold the names of the extensions that needed to be added to the modules.list
     * @parameter
     */
    private List extMarNames;

    public void execute() throws MojoExecutionException, MojoFailureException {
        if (dependencies != null && !dependencies.isEmpty()) {
            List marList = new ArrayList();
            List aarList = new ArrayList();
            for (Iterator iterator = dependencies.iterator(); iterator.hasNext();) {
                Dependency dependency = (Dependency) iterator.next();
                if (dependency.getType().equals("aar")) {
                    String o = dependency.getArtifactId() + "." + dependency.getType();
                    aarList.add(o);
                } else if (dependency.getType().equals("mar")) {
                    String o = dependency.getArtifactId() + "-" + dependency.getVersion() + "." +
                               dependency.getType();
                    marList.add(o);
                }
            }
            if (extAarNames != null && extAarNames.size() > 0) {
                aarList.addAll(extAarNames);
            }
            if (extMarNames != null && extMarNames.size() > 0) {
                marList.addAll(extMarNames);
            }
            serializeList(marList, "modules.list");
            serializeList(aarList, "services.list");
        }
    }

    /**
     * This will serialize the list to the the given output location calculated from outputDirectory
     * and outputFileName.
     *
     * @param list           list to be serialized
     * @param outputFileName outputfile name
     */
    private void serializeList(List list, String outputFileName) throws MojoFailureException {
        getLog().info("Generating " + outputFileName);
        if (!(outputDirectory.isDirectory() && outputDirectory.exists())) {
            outputDirectory.mkdir();
        }
        File outLocation = new File(outputDirectory, outputFileName);
        if (outLocation.exists()) {
            outLocation.delete();
        }
        try {
            outLocation.createNewFile();
            FileWriter fileWriter = new FileWriter(outLocation);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (Iterator iterator = list.iterator(); iterator.hasNext();) {
                String archiveName = (String) iterator.next();
                bufferedWriter.write(archiveName);
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            String msg = outLocation.getAbsolutePath() + "cannot be created";
            throw new MojoFailureException(e, msg, msg);
        }


    }
}
