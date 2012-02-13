package org.wso2.tools.ksexplorer.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Member;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ognl.TypeConverter;

import org.apache.struts2.StrutsStatics;
import org.wso2.tools.ksexplorer.KeyStoreConverter;
import org.wso2.tools.ksexplorer.KeyInfo;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class GenerateZipAction extends ActionSupport implements Preparable {
    
    private List keyInfoList;
    
    private InputStream inputStream;
    
    private Map passwordMap;
    
    private KeyStore store;
    
    private String storePass;
    
    private String sessionID;
    
    public InputStream getInputStream() {
        return inputStream;
    }
    
    public String execute() throws Exception {   
        
        File zipFile = new File(sessionID +".zip");
        
        if (store.getType().equals("jks")) {
            KeyStoreConverter.covertJKS2PFX(store, zipFile, keyInfoList, sessionID );
        } else {
            KeyStoreConverter.covertPFX2JKS(store, zipFile, keyInfoList, sessionID, storePass);
        }
        
        inputStream = new FileInputStream(zipFile);
        zipFile.delete();
        
        
        return SUCCESS;
    }

    public List getKeyInfoList() {
        return keyInfoList;
    }

    public void setKeyInfoList(List keyInfoList) {
        this.keyInfoList = keyInfoList;
    }
    
    
    public void prepare() {

        keyInfoList = new ArrayList();
        passwordMap = new HashMap();
        
        HttpServletRequest request = (HttpServletRequest) ActionContext.
        getContext().get(StrutsStatics.HTTP_REQUEST);
        
        store = (KeyStore)request.getSession().getAttribute("KeyStore");
        
        storePass = (String)request.getSession().getAttribute("StorePass");
        
        sessionID = request.getSession().getId();
        
    }

}
