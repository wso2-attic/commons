
/**
 * MercurymsgsequenceserviceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: SNAPSHOT  Built on : Mar 12, 2008 (10:22:59 LKT)
 */

    package org.test;

    /**
     *  MercurymsgsequenceserviceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class MercurymsgsequenceserviceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public MercurymsgsequenceserviceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public MercurymsgsequenceserviceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for echoInt method
            * override this method for handling normal response from echoInt operation
            */
           public void receiveResultechoInt(
                    org.test.MercurymsgsequenceserviceStub.EchoIntResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from echoInt operation
           */
            public void receiveErrorechoInt(java.lang.Exception e) {
            }
                


    }
    