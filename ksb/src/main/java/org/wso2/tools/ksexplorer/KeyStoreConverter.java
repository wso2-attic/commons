package org.wso2.tools.ksexplorer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.KeyStore.Entry;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

public class KeyStoreConverter {
    

    public static void covertJKS2PFX(KeyStore jks, File zipFile, List keyInfoList, String dirName)
                                                                        throws KeyExplorerException{
    
        File zipDir = null;
        String alias = null;
    
        try {      
            zipDir = new File(dirName);
            zipDir.mkdir();
            
            Set privateKeyAlias = new HashSet();
            HashMap privateKeyInfo = new HashMap();
            
            Enumeration aliases = jks.aliases();
            
            while ( aliases.hasMoreElements() ) {
                
                alias = (String) aliases.nextElement();
                
                if (jks.isKeyEntry(alias)) {
                 
                    KeyInfo keyInfo = getKeyInfoFromAlias(alias, keyInfoList);
                    
                    if (keyInfo.isInclude()) {
                        
                        String password = keyInfo.getPassword();
                    
                        KeyStore nks = KeyStore.getInstance("pkcs12");
                        nks.load(null, password.toCharArray());
                        
                        // Get the private key entry and store it in the new keystore
                        KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry) jks.getEntry
                        (alias, new KeyStore.PasswordProtection(password.toCharArray()));
                        nks.setEntry(alias, pkEntry, new KeyStore.PasswordProtection(password.toCharArray()));
                                  
                        FileOutputStream fos = new FileOutputStream(zipDir.getCanonicalPath()+File.separator+alias+".pfx");
                        nks.store(fos, password.toCharArray() );
                        fos.close();
                    
                    }
                
                } else if (jks.isCertificateEntry(alias)) {
                    
                    KeyInfo keyInfo = getKeyInfoFromAlias(alias, keyInfoList);
                    
                    if (keyInfo.isInclude()) {
                    
                        Certificate cert = jks.getCertificate(alias);
                        
                        FileOutputStream fos = new FileOutputStream(zipDir.getCanonicalPath()+File.separator+alias+".cer");
                        fos.write(cert.getEncoded());
                        fos.flush();
                        fos.close();     
                    
                    }
                 
                }   
                
            }
            
            zipDir(zipDir,zipFile); 
            
      } catch (KeyStoreException e) {
          throw new KeyExplorerException("KeyStoreException",e);     
      } catch (IOException e) {
          throw new KeyExplorerException("IOException",e);
      } catch (CertificateException e) {
          throw new KeyExplorerException("CertificateException",e);
      } catch (NoSuchAlgorithmException e) {
          throw new KeyExplorerException("NoSuchAlgorithmException",e);
      } catch (UnrecoverableEntryException e) {
        throw new KeyExplorerException("Wrong password for private key : alias = "+ alias,e);
      } finally {
          deleteDir(zipDir);
      }
                
    }
    
    public static void covertPFX2JKS(KeyStore pfx, File zipFile, List keyInfoList, String dirName,
                                                    String storePass) throws KeyExplorerException{
        
        File zipDir = null;
        
        
        try {
            
            zipDir = new File(dirName);
            zipDir.mkdir();
            
            KeyStore jks = KeyStore.getInstance("jks");
            jks.load(null, storePass.toCharArray());
            FileOutputStream fos = new FileOutputStream(zipDir.getCanonicalPath()+
                                                                    File.separator+"keystore.jks");
            
            Enumeration aliases = pfx.aliases();
            
            while ( aliases.hasMoreElements() ) {
                String alias = (String) aliases.nextElement();  
                
                KeyInfo keyInfo = getKeyInfoFromAlias(alias, keyInfoList);
                
                if (pfx.isKeyEntry(alias)){
                    Entry entry = pfx.getEntry(alias, new KeyStore.PasswordProtection(keyInfo.getPassword().toCharArray()));
                    jks.setEntry(alias, entry, new KeyStore.PasswordProtection(keyInfo.getPassword().toCharArray()));
                    break;
                }
               
            }
            
            jks.store(fos, storePass.toCharArray() );
            fos.close();
            
            zipDir(zipDir,zipFile); 
            
          } catch (Exception e) { 
              System.out.println(e); 
          } finally {
              deleteDir(zipDir);
          }
          
    }
    
    public static void zipDir(File zipDir, File zipFile) throws KeyExplorerException {
        
        try {
               
            if (!zipDir.isDirectory()) {
                throw new RuntimeException("this is not a directory");
            }
        
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
            
            //get a listing of the directory content
            String[] dirList = zipDir.list();
            byte[] readBuffer = new byte[40960];
            int bytesIn = 0;
            //loop through dirList, and zip the files
            for (int i = 0; i < dirList.length; i++) {
                File f = new File(zipDir, dirList[i]);
                //place the zip entry in the ZipOutputStream object
                zos.putNextEntry(new ZipEntry(dirList[i]));
    
                //if we reached here, the File object f was not a directory
                //create a FileInputStream on top of f
                FileInputStream fis = new FileInputStream(f);
    
                //now write the content of the file to the ZipOutputStream
                while ((bytesIn = fis.read(readBuffer)) != -1) {
                    zos.write(readBuffer, 0, bytesIn);
                }
                //close the Stream
                fis.close();
            }
        
            zos.close();
        
        } catch ( FileNotFoundException e ){
            throw new KeyExplorerException("FileNotFoundException",e);
        } catch (IOException e) {
            throw new KeyExplorerException("IOException",e);
        }
    }
    
    /**
     * Deletes all files and subdirectories under dir.
     * Returns true if all deletions were successful.
     * If a deletion fails, the method stops attempting to delete and returns false.
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }
    
    public static KeyInfo getKeyInfoFromAlias(String alias, List keyInfoList) {
        
        Iterator iter = keyInfoList.iterator();
        
        while (iter.hasNext()) {
            KeyInfo keyInfo = (KeyInfo)iter.next();
            if (alias.equals(keyInfo.getAlias())) {
                return keyInfo;
            }            
        }
        
        return null;
    }
    
}
