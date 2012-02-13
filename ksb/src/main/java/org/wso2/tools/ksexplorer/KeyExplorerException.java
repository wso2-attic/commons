package org.wso2.tools.ksexplorer;

public class KeyExplorerException extends Exception {

    public KeyExplorerException(String message) {
        super(message);
    }
    
    public KeyExplorerException(String message, Exception e) {
        super(message,e);
    }
    
}
