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

import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.Iterator;
import java.util.List;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class ShowPEMPrivateKeyAction extends ActionSupport {

    private static final long serialVersionUID = -4061122808487798237L;
    
    private static Log log = LogFactory.getLog(ShowPEMPrivateKeyAction.class);
    
    private String pemKey;
    private String alias;
    private String storeName;
    
    
    public String getAlias() {
        return alias;
    }

    public String getStoreName() {
        return storeName;
    }

    public String execute() throws Exception {

        HttpServletRequest request = (HttpServletRequest) ActionContext
                .getContext().get(StrutsStatics.HTTP_REQUEST);
        HttpSession session = request.getSession();
        List keyStoreDescriptions = (List) session
                .getAttribute(KSExplorerConstants.SESSION_KEY_KS);

        String ksId = request.getParameter("ksId");
        KeyStoreDescription ksDesc = null; 
        for (Iterator iterator = keyStoreDescriptions.iterator(); iterator
                .hasNext();) {
            KeyStoreDescription desc = (KeyStoreDescription) iterator.next();
            if(desc.getUuid().equals(ksId)) {
                ksDesc = desc;
            }
        }
        
        KeyStore store = ksDesc.getKeyStore();
        this.storeName = ksDesc.getName();
        this.alias = request.getParameter("alias");
        String keyPasswd = request.getParameter("keyPasswd");
        PrivateKey key = (PrivateKey)store.getKey(alias, keyPasswd.toCharArray());

        log.info("[WSO2KSE] : Showing key : " + alias + " in keystore : " + 
                this.storeName + " (store id :" + ksId + ")");
        
        BASE64Encoder encoder = new BASE64Encoder();
        pemKey = "-----BEGIN PRIVATE KEY-----\n";
        pemKey += encoder.encode(key.getEncoded());
        pemKey += "\n-----END PRIVATE KEY-----";
        
        return SUCCESS;
    }

    public String getPemKey() {
        return pemKey;
    }

}
