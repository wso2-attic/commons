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

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import java.util.Iterator;
import java.util.Set;
import java.io.*;

/**
 * @goal download
 * @phase package
 * @requiresDependencyResolution
 */
public class POMDownloadMojo extends AbstractMojo {


    /**
     * POM
     *
     * @parameter expression="${project}"
     * @readonly
     * @required
     */
    protected MavenProject project;

    /**
     * The output directory of the assembled distribution file.
     *
     * @parameter default-value="${project.build.directory}/pom-files/"
     */
    private File outputDirectory;

    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            dependencyResol();
        } catch (IOException e) {
            throw new MojoExecutionException("IOException", e);
        }
    }

    public void dependencyResol() throws IOException {
        Set artifacts = project.getArtifacts();
        for (Iterator iterator = artifacts.iterator(); iterator.hasNext();) {
            Artifact artifact = (Artifact) iterator.next();
            File artifactFile = artifact.getFile();
            String fileOutDirString =  outputDirectory.getAbsolutePath() + File.separator;

            File outFileDir = new File(fileOutDirString);
            if (!outFileDir.exists()) {
                outFileDir.mkdirs();
            }
            File pomFile = new File(outFileDir, artifact.getArtifactId() + "-" +
                                                artifact.getVersion() + ".pom");
            OutputStream outS = new FileOutputStream(pomFile);

            String artifactDir = artifactFile.getAbsolutePath();
            if (File.separatorChar == '\\') {
                artifactDir = artifactDir.replaceAll("\\", "/");
            }
            artifactDir = artifactDir.substring(0, artifactDir.lastIndexOf("/") + 1);
            File repoPomFile = new File(artifactDir, artifact.getArtifactId() + "-" +
                                                     artifact.getVersion() + ".pom");
            if (repoPomFile.exists() && repoPomFile.isFile()) {
                getLog().info("Writing pom as pom.xml " + repoPomFile.getAbsolutePath());
                InputStream in = new FileInputStream(repoPomFile);
                //write
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                try {
                    while ((len = in.read(buf)) > 0) {
                        outS.write(buf, 0, len);
                    }
                    in.close();
                    outS.close();
                } catch (IOException e) {
                    throw e;
                }
            } else {
                getLog().info("pom is not found " + repoPomFile.getAbsolutePath());
            }
        }
    }
}
