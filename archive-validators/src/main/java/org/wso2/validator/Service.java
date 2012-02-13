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
package org.wso2.validator;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.deployment.DeploymentConstants;

import java.io.*;

/**
 * This class the functionality to validate a given aar's services.xml or a seperately
 * given services.xml.
 */
public class Service {

    public static final String SERVICES_XSL_LOCATION = "org/wso2/validator/service-validator.xsl";
    public static final String SERVICES_XSL_FORMATTER_LOCATION =
            "org/wso2/validator/service-formatter.xsl";

    /**
     * This method will retrun the location of the generated html after validation of services.xml from
     * this given service archive. Method will be injected with file id, which will find the absolute
     * path from WSO2Constants.FILE_RESOURCE_MAP.
     *
     * @param fileId file id
     * @return String id of the generated html file
     * @throws AxisFault will be thrown for any fault that could encounter in the code
     */
    public String validate(String fileId) throws AxisFault {
        ConfigurationContext configCtx = MessageContext
                .getCurrentMessageContext().getConfigurationContext();
        String aarFileLocation = Util.getFilePathFromFileId(fileId, configCtx);
        if (aarFileLocation == null) {
            throw new AxisFault("Uploaded file is invalid and cannot continue the validation,");
        }
        String s;
        InputStream in;
        try {
            in = Util.locateXML(aarFileLocation, ".aar", DeploymentConstants.SERVICES_XML);
            Util.continueProcess(in);
            in = Util.locateXML(aarFileLocation, ".aar", DeploymentConstants.SERVICES_XML);
            s = Util.doTransformation(in, aarFileLocation, SERVICES_XSL_LOCATION,
                                      SERVICES_XSL_FORMATTER_LOCATION,
                                      Service.class.getClassLoader());
            in.close();
        } catch (AxisFault axisFault) {
            throw axisFault;
        } catch (IOException e) {
            throw AxisFault.makeFault(e);
        }
        return s;
    }

    /**
     * This method will generate a html page after validating a services.xml.
     *
     * @param fileId file id
     * @return String id
     * @throws AxisFault will be thrown
     */
    public String validateServicesXML(String fileId) throws AxisFault {
        ConfigurationContext configCtx =
                MessageContext.getCurrentMessageContext().getConfigurationContext();
        String s;
        try {
            String aarFileLocation = Util.getFilePathFromFileId(fileId, configCtx);
            if (aarFileLocation == null) {
                throw new AxisFault("Uploaded file is invalid and cannot continue the validation.");
            }
            FileInputStream in = new FileInputStream(aarFileLocation);
            Util.continueProcess(in);
            in = new FileInputStream(aarFileLocation);
            s = Util.doTransformation(in, aarFileLocation, SERVICES_XSL_LOCATION,
                                      SERVICES_XSL_FORMATTER_LOCATION,
                                      Service.class.getClassLoader());
            in.close();
        } catch (FileNotFoundException e) {
            String msg = "services.xml validation cannot be continued. This might cause due to " +
                         "uploading of invalid services.xml or internal server error. ";
            throw new AxisFault(msg, e);
        } catch (IOException e) {
            throw AxisFault.makeFault(e);
        }
        return s;
    }
}
