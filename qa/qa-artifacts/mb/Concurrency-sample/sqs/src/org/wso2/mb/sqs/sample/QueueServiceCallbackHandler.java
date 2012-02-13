
/**
 * QueueServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.1-wso2v1  Built on : Mar 30, 2011 (09:53:01 IST)
 */

    package org.wso2.mb.sqs.sample;

    /**
     *  QueueServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class QueueServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public QueueServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public QueueServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for listQueues method
            * override this method for handling normal response from listQueues operation
            */
           public void receiveResultlistQueues(
                    org.wso2.mb.sqs.sample.QueueServiceStub.ListQueuesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from listQueues operation
           */
            public void receiveErrorlistQueues(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for createQueue method
            * override this method for handling normal response from createQueue operation
            */
           public void receiveResultcreateQueue(
                    org.wso2.mb.sqs.sample.QueueServiceStub.CreateQueueResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from createQueue operation
           */
            public void receiveErrorcreateQueue(java.lang.Exception e) {
            }
                


    }
    