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

package org.wso2.tools.ksexplorer.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.StrutsStatics;
import org.wso2.tools.ksexplorer.KSExplorerConstants;
import org.wso2.tools.ksexplorer.KeyStoreDescription;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class UploadKeyStoreAction extends ActionSupport {

    private static final long serialVersionUID = -8841477227660234617L;
    private Log log = LogFactory.getLog(UploadKeyStoreAction.class);
    
    private File keyStoreFile;
    private String keyStoreFileFileName; 

    public String execute() throws Exception {
        HttpServletRequest request = (HttpServletRequest) ActionContext
                .getContext().get(StrutsStatics.HTTP_REQUEST);

        String storeType = ("01".equals(request
                .getParameter(KSExplorerConstants.PARAM_STORE_TYPE))) ? "JKS"
                : "PKCS12";
        String storePass = request
                .getParameter(KSExplorerConstants.PARAM_STORE_PASSWORD);
        KeyStore store = KeyStore.getInstance(storeType);
        store.load(new FileInputStream(this.keyStoreFile), storePass
                .toCharArray());

        KeyStoreDescription ksDesc = new KeyStoreDescription(store, this.keyStoreFileFileName);
        
        HttpSession session = request.getSession();
        List keyStoreDescriptions = (List) session
                .getAttribute(KSExplorerConstants.SESSION_KEY_KS);
        if (keyStoreDescriptions == null) {
            keyStoreDescriptions = new ArrayList();
        }
        keyStoreDescriptions.add(ksDesc);
        session.setAttribute(KSExplorerConstants.SESSION_KEY_KS,
                keyStoreDescriptions);

        log.info("[WSO2KSE] : Added keystore : " + this.keyStoreFileFileName);
        log.info("[WSO2KSE] : This session now has : " + keyStoreDescriptions.size() + " keystores");
        
        return SUCCESS;
    }

    public File getKeyStoreFile() {
        return keyStoreFile;
    }

    public void setKeyStoreFile(File keyStoreFile) {
        this.keyStoreFile = keyStoreFile;
    }

    public String getKeyStoreFileFileName() {
        return keyStoreFileFileName;
    }

    public void setKeyStoreFileFileName(String keyStoreFileFileName) {
        this.keyStoreFileFileName = keyStoreFileFileName;
    }

}
