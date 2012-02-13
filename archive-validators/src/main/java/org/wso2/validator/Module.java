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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class provide the facility to validate module.xml.
 */
public class Module {

    public static final String MODULE_XSL_LOCATION = "org/wso2/validator/module_validator.xsl";
    public static final String MODULE_XSL_FORMATTER_LOCATION =
            "org/wso2/validator/module_formatter.xsl";

    /**
     * This method will retrun the location of the generated html after validation of module.xml from
     * this given module archive. Method will be injected with file id, which will find the absolute
     * path from WSO2Constants.FILE_RESOURCE_MAP.
     *
     * @param fileId file id
     * @return String id of the generated html file
     * @throws AxisFault will be thrown for any fault that could encounter in the code
     */
    public String validate(String fileId) throws AxisFault {
        ConfigurationContext configCtx = MessageContext
                .getCurrentMessageContext().getConfigurationContext();
        String marFileLocation = Util.getFilePathFromFileId(fileId, configCtx);
        if (marFileLocation == null) {
            throw new AxisFault("Uploaded file is invalid and cannot continue the validation,");
        }
        String s;
        InputStream in;
        try {
            in = Util.locateXML(marFileLocation, ".mar", DeploymentConstants.MODULE_XML);
            Util.continueProcess(in);
            in = Util.locateXML(marFileLocation, ".mar", DeploymentConstants.MODULE_XML);
            s = Util.doTransformation(in, marFileLocation, MODULE_XSL_LOCATION,
                                      MODULE_XSL_FORMATTER_LOCATION,
                                      Module.class.getClassLoader());
            in.close();
        } catch (AxisFault axisFault) {
            throw axisFault;
        } catch (IOException e) {
            throw AxisFault.makeFault(e);
        }
        return s;
    }

    /**
     * This method will generate a html page after validating a module.xml.
     *
     * @param fileId file id
     * @return String id
     * @throws AxisFault will be thrown
     */
    public String validateModuleXML(String fileId) throws AxisFault {
        ConfigurationContext configCtx =
                MessageContext.getCurrentMessageContext().getConfigurationContext();
        String s;
        try {
            String marFileLocation = Util.getFilePathFromFileId(fileId, configCtx);
            if (marFileLocation == null) {
                throw new AxisFault("Uploaded file is invalid and cannot continue the validation,");
            }
            FileInputStream in = new FileInputStream(marFileLocation);
            Util.continueProcess(in);
            in = new FileInputStream(marFileLocation);
            s = Util.doTransformation(in, marFileLocation, MODULE_XSL_LOCATION,
                                      MODULE_XSL_FORMATTER_LOCATION,
                                      Module.class.getClassLoader());
            in.close();
        } catch (FileNotFoundException e) {
            String msg = "module.xml validation cannot be continued. This might cause due to " +
                         "uploading of invalid module.xml or internal server error. ";
            throw new AxisFault(msg, e);
        } catch (IOException e) {
            throw AxisFault.makeFault(e);
        }
        return s;
    }
}
