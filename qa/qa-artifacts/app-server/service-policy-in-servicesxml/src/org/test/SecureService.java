

/**
 * SecureService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.1-wso2v1  Built on : Jun 24, 2011 (06:41:37 IST)
 */

    package org.test;

    /*
     *  SecureService java interface
     */

    public interface SecureService {
          

        /**
          * Auto generated method signature
          * 
                    * @param add0
                
         */

         
                     public int add(

                        int x1,int y2)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param add0
            
          */
        public void startadd(

            int x1,int y2,

            final org.test.SecureServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    