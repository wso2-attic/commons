
/**
 * RM_MTOM_ServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: SNAPSHOT  Built on : Mar 12, 2008 (10:22:59 LKT)
 */

    package org.apache.ws.axis2.mtomsample;

    /**
     *  RM_MTOM_ServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class RM_MTOM_ServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public RM_MTOM_ServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public RM_MTOM_ServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for attachment method
            * override this method for handling normal response from attachment operation
            */
           public void receiveResultattachment(
                    org.apache.ws.axis2.mtomsample.RM_MTOM_ServiceStub.AttachmentResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from attachment operation
           */
            public void receiveErrorattachment(java.lang.Exception e) {
            }
                


    }
    