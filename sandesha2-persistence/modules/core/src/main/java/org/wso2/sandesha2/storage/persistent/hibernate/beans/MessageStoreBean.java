/*
* Copyright 2005,2006 WSO2, Inc. http://wso2.com
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
*
*/

package org.wso2.sandesha2.storage.persistent.hibernate.beans;

/**
 * Defines a set of properties which extracted from a Message Context
 * when serializing it.
 */
public class MessageStoreBean {

	private String SOAPEnvelopeString;
	
	private String storedKey;
	
	private int SOAPVersion = 0;

	private String transportOut;
	
	private String axisServiceGroup;
	
	private String axisService;
	
	private String axisOperation;
	
	private String axisOperationMEP;
	
	private String toURL;
	
	private String transportTo;
	
	private int flow;
	
	private String executionChainString;
	
	private String messageReceiverString;
	
	private boolean serverSide;
	
	private String inMessageStoreKey;
	
	private String messageID;
	
	private String persistentPropertyString;
	
	private String callbackClassName;
	
	private String action;
	
	public String getMessageID() {
		return messageID;
	}

	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}

	public String getStoredKey() {
		return storedKey;
	}

	public void setStoredKey(String key) {
		this.storedKey = key;
	}

	public String getSOAPEnvelopeString() {
		return SOAPEnvelopeString;
	}

	public void setSOAPEnvelopeString(String SOAPEnvelopeString) {
		this.SOAPEnvelopeString = SOAPEnvelopeString;
	}

	public int getSOAPVersion() {
		return SOAPVersion;
	}

	public void setSOAPVersion(int SOAPVersion) {		
		this.SOAPVersion = SOAPVersion;
	}

	public String getTransportOut() {
		return transportOut;
	}

	public void setTransportOut(String transportSender) {
		this.transportOut = transportSender;
	}

	public String getAxisOperation() {
		return axisOperation;
	}

	public void setAxisOperation(String axisOperation) {
		this.axisOperation = axisOperation;
	}

	public String getAxisService() {
		return axisService;
	}

	public void setAxisService(String axisService) {
		this.axisService = axisService;
	}

	public String getAxisServiceGroup() {
		return axisServiceGroup;
	}

	public void setAxisServiceGroup(String axisServiceGroup) {
		this.axisServiceGroup = axisServiceGroup;
	}

	public String getAxisOperationMEP() {
		return axisOperationMEP;
	}

	public void setAxisOperationMEP(String axisOperationAdd) {
		this.axisOperationMEP = axisOperationAdd;
	}

	public String getToURL() {
		return toURL;
	}

	public void setToURL(String toURL) {
		this.toURL = toURL;
	}

	public String getTransportTo() {
		return transportTo;
	}

	public void setTransportTo(String transportTo) {
		this.transportTo = transportTo;
	}

	public String getExecutionChainString() {
		return executionChainString;
	}

	public void setExecutionChainString(String executionChainString) {
		this.executionChainString = executionChainString;
	}

	public int getFlow() {
		return flow;
	}

	public void setFlow(int flow) {
		this.flow = flow;
	}

	public String getMessageReceiverString() {
		return messageReceiverString;
	}

	public void setMessageReceiverString(String messageReceiverString) {
		this.messageReceiverString = messageReceiverString;
	}

	public boolean isServerSide() {
		return serverSide;
	}

	public void setServerSide(boolean serverSide) {
		this.serverSide = serverSide;
	}

	public String getInMessageStoreKey() {
		return inMessageStoreKey;
	}

	public void setInMessageStoreKey(String requestMessageKey) {
		this.inMessageStoreKey = requestMessageKey;
	}

	public String getPersistentPropertyString() {
		return persistentPropertyString;
	}

	public void setPersistentPropertyString(String persistentPropertyString) {
		this.persistentPropertyString = persistentPropertyString;
	}

	public String getCallbackClassName() {
		return callbackClassName;
	}

	public void setCallbackClassName(String callbackClassName) {
		this.callbackClassName = callbackClassName;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}
