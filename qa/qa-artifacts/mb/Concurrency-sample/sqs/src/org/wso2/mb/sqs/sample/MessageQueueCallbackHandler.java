
/**
 * MessageQueueCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.1-wso2v1  Built on : Mar 30, 2011 (09:53:01 IST)
 */

    package org.wso2.mb.sqs.sample;

    /**
     *  MessageQueueCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class MessageQueueCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public MessageQueueCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public MessageQueueCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for setQueueAttributes method
            * override this method for handling normal response from setQueueAttributes operation
            */
           public void receiveResultsetQueueAttributes(
                    org.wso2.mb.sqs.sample.MessageQueueStub.SetQueueAttributesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from setQueueAttributes operation
           */
            public void receiveErrorsetQueueAttributes(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deleteQueue method
            * override this method for handling normal response from deleteQueue operation
            */
           public void receiveResultdeleteQueue(
                    org.wso2.mb.sqs.sample.MessageQueueStub.DeleteQueueResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteQueue operation
           */
            public void receiveErrordeleteQueue(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for changeMessageVisibility method
            * override this method for handling normal response from changeMessageVisibility operation
            */
           public void receiveResultchangeMessageVisibility(
                    org.wso2.mb.sqs.sample.MessageQueueStub.ChangeMessageVisibilityResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from changeMessageVisibility operation
           */
            public void receiveErrorchangeMessageVisibility(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for addPermission method
            * override this method for handling normal response from addPermission operation
            */
           public void receiveResultaddPermission(
                    org.wso2.mb.sqs.sample.MessageQueueStub.AddPermissionResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from addPermission operation
           */
            public void receiveErroraddPermission(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getQueueAttributes method
            * override this method for handling normal response from getQueueAttributes operation
            */
           public void receiveResultgetQueueAttributes(
                    org.wso2.mb.sqs.sample.MessageQueueStub.GetQueueAttributesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getQueueAttributes operation
           */
            public void receiveErrorgetQueueAttributes(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deleteMessage method
            * override this method for handling normal response from deleteMessage operation
            */
           public void receiveResultdeleteMessage(
                    org.wso2.mb.sqs.sample.MessageQueueStub.DeleteMessageResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteMessage operation
           */
            public void receiveErrordeleteMessage(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for sendMessage method
            * override this method for handling normal response from sendMessage operation
            */
           public void receiveResultsendMessage(
                    org.wso2.mb.sqs.sample.MessageQueueStub.SendMessageResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from sendMessage operation
           */
            public void receiveErrorsendMessage(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for receiveMessage method
            * override this method for handling normal response from receiveMessage operation
            */
           public void receiveResultreceiveMessage(
                    org.wso2.mb.sqs.sample.MessageQueueStub.ReceiveMessageResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from receiveMessage operation
           */
            public void receiveErrorreceiveMessage(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for removePermission method
            * override this method for handling normal response from removePermission operation
            */
           public void receiveResultremovePermission(
                    org.wso2.mb.sqs.sample.MessageQueueStub.RemovePermissionResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from removePermission operation
           */
            public void receiveErrorremovePermission(java.lang.Exception e) {
            }
                


    }
    