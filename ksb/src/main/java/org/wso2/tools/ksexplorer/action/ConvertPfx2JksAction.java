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
import org.wso2.tools.ksexplorer.KeyInfo;
import org.wso2.tools.ksexplorer.KeyStoreDescription;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class ConvertPfx2JksAction extends ActionSupport {

    private static final long serialVersionUID = -8841477227660234617L;
    private Log log = LogFactory.getLog(UploadKeyStoreAction.class);
    
    private File keyStoreFile;
    
    private String keyStoreFileFileName;  
    
    private String keyStoreFileContentType;
    
    private String storePasswd;
    
    private String conversion;
    
    private ArrayList keyInfoList = new ArrayList();

    public String execute() throws Exception {
        
        HttpServletRequest request = (HttpServletRequest) ActionContext.
                                                      getContext().get(StrutsStatics.HTTP_REQUEST);
        HttpSession session = request.getSession();
        
        KeyStore store = null;
        
        if (KSExplorerConstants.JKS_TO_PFX.equals(conversion)) {
            store = KeyStore.getInstance("jks");
            store.load(new FileInputStream(keyStoreFile), storePasswd.toCharArray());
            session.setAttribute("KeyStore", store);
            
        } else if (KSExplorerConstants.PFX_TO_JKS.equals(conversion)) {          
            store = KeyStore.getInstance("pkcs12");     
            store.load(new FileInputStream(keyStoreFile), storePasswd.toCharArray());
            session.setAttribute("KeyStore", store);
            session.setAttribute("StorePass", storePasswd);
            
        }
                
        Enumeration aliases = store.aliases();
        
        while ( aliases.hasMoreElements() ) {
            String alias = (String) aliases.nextElement();
            
            KeyInfo keyInfo = new KeyInfo();
            keyInfo.setAlias(alias);
            
            if ( store.isKeyEntry(alias) ) {
                keyInfo.setPrivateKey(true);
            } 
            
            keyInfoList.add(keyInfo);
        }
       
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

    public ArrayList getKeyInfoList() {
        return keyInfoList;
    }

    public void setKeyInfoList(ArrayList keyInfoList) {
        this.keyInfoList = keyInfoList;
    }

    public String getStorePasswd() {
        return storePasswd;
    }

    public void setStorePasswd(String storePasswd) {
        this.storePasswd = storePasswd;
    }

    public String getKeyStoreFileContentType() {
        return keyStoreFileContentType;
    }

    public void setKeyStoreFileContentType(String keyStoreFileContentType) {
        this.keyStoreFileContentType = keyStoreFileContentType;
    }

    public String getConversion() {
        return conversion;
    }

    public void setConversion(String conversion) {
        this.conversion = conversion;
    }
  
}
