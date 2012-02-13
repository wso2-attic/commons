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

package org.wso2.sandesha2.storage.persistent.hibernate;

import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.impl.llom.factory.OMXMLBuilderFactory;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.impl.builder.StAXSOAPModelBuilder;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.AddressingConstants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.async.Callback;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.MessageContextConstants;
import org.apache.axis2.context.OperationContext;
import org.apache.axis2.context.OperationContextFactory;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.context.ServiceGroupContext;
import org.apache.axis2.description.AxisModule;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.AxisServiceGroup;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.description.TransportOutDescription;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.engine.MessageReceiver;
import org.apache.axis2.util.CallbackReceiver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.sandesha2.Sandesha2Constants;
import org.apache.sandesha2.SandeshaException;
import org.apache.sandesha2.policy.SandeshaPolicyBean;
import org.apache.sandesha2.polling.PollingManager;
import org.apache.sandesha2.storage.SandeshaStorageException;
import org.apache.sandesha2.storage.StorageManager;
import org.apache.sandesha2.storage.Transaction;
import org.apache.sandesha2.storage.beanmanagers.InvokerBeanMgr;
import org.apache.sandesha2.storage.beanmanagers.RMDBeanMgr;
import org.apache.sandesha2.storage.beanmanagers.RMSBeanMgr;
import org.apache.sandesha2.storage.beanmanagers.SenderBeanMgr;
import org.apache.sandesha2.storage.inmemory.InMemoryInvokerBeanMgr;
import org.apache.sandesha2.storage.inmemory.InMemoryRMDBeanMgr;
import org.apache.sandesha2.storage.inmemory.InMemoryRMSBeanMgr;
import org.apache.sandesha2.storage.inmemory.InMemorySenderBeanMgr;
import org.apache.sandesha2.util.SandeshaUtil;
import org.apache.sandesha2.workers.Invoker;
import org.apache.sandesha2.workers.SandeshaThread;
import org.apache.sandesha2.workers.Sender;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import org.wso2.sandesha2.storage.persistent.hibernate.beans.MessageStoreBean;
import org.wso2.sandesha2.storage.persistent.util.PersistentUtil;


/**
 * A Storage Manager implementation for managing Sandesha2 beans using Hibernate.
 * Uses MessageStore Beans to manage Message Contexts. 
 */
public class PersistentStorageManager extends StorageManager {

    private RMSBeanMgr  rMSBeanMgr = null;
    private RMDBeanMgr rMDBeanMgr = null;
    private SenderBeanMgr senderBeanMgr = null;
    private InvokerBeanMgr invokerBeanMgr = null;
    private Sender sender = null;
    private Invoker invoker = null;
    private PollingManager pollingManager = null;
    public static ThreadLocal currentTransaction = null;
    
	public SandeshaThread getInvoker() {
		// TODO Auto-generated method stub
		return invoker;
	}



	public PollingManager getPollingManager() {
		// TODO Auto-generated method stub
		return pollingManager;
	}



	public SandeshaThread getSender() {
		// TODO Auto-generated method stub
		return sender;
	}

	private HashMap messageContextCache = null;
	
	private static final Log log = LogFactory.getLog(PersistentStorageManager.class);
	
	public PersistentStorageManager(ConfigurationContext context) throws SandeshaException {
		super (context);
		messageContextCache = new HashMap ();
		
		currentTransaction = new ThreadLocal ();
		
		SandeshaPolicyBean policy = SandeshaUtil.getPropertyBean(context.getAxisConfiguration());
		
		// Note that while inOrder is a global property we can decide if we need the
		// invoker thread at this point. If we change this to be a sequence-level
		// property then we'll need to revisit this.
		boolean inOrder = policy.isInOrder();
		boolean polling = policy.isEnableMakeConnection();
		
		this.rMSBeanMgr = new PersistentRMSBeanMgr ();
		this.rMDBeanMgr = new PersistentRMDBeanMgr ();
		this.senderBeanMgr = new PersistentSenderBeanMgr ();
		this.invokerBeanMgr = new PersistentInvokerBeanMgr ();
		this.sender = new Sender();
		if(inOrder) this.invoker = new Invoker();
		if(polling) this.pollingManager = new PollingManager();
	
	}
	
	

	public InvokerBeanMgr getInvokerBeanMgr() {
		return new PersistentInvokerBeanMgr ();
	}



	public RMDBeanMgr getRMDBeanMgr() {
		return new PersistentRMDBeanMgr ();
	}



	public RMSBeanMgr getRMSBeanMgr() {
		return new PersistentRMSBeanMgr ();
	}



	public SenderBeanMgr getSenderBeanMgr() {
		return new PersistentSenderBeanMgr ();
	}

	public Transaction getTransaction() {
		
		Transaction transaction = (Transaction) currentTransaction.get();
		if (transaction==null) {
			transaction = new TransactionImpl ();
			currentTransaction.set(transaction);
		}
			
		if (transaction.isActive()) {
			return transaction;
		} else {
			transaction = new TransactionImpl ();
			currentTransaction.set(transaction);
			return transaction;
		}
	}
	
	public void  initStorage (AxisModule moduleDisc) throws SandeshaStorageException {
			
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		//replacing the context class loader before initializaton
		Thread.currentThread().setContextClassLoader(moduleDisc.getModuleClassLoader());
		
		try {
			Parameter connectionIDParam = moduleDisc.getParameter(DBConnectionParams.DB_CONNECTION_ID);
			Parameter driverParam = moduleDisc.getParameter(DBConnectionParams.DB_DRIVER);
			Parameter sqlDialectParam = moduleDisc.getParameter(DBConnectionParams.DB_SQL_DIALECT);
			Parameter usernameParam = moduleDisc.getParameter(DBConnectionParams.USERNAME);
			Parameter passwordParam = moduleDisc.getParameter(DBConnectionParams.PASSWORD);
			Parameter createModeParam = moduleDisc.getParameter(DBConnectionParams.DB_CREATE_MODE);
			
			if (connectionIDParam==null)
				throw new SandeshaStorageException ("Cannot instantiate Storage Manager. Parameter 'sandesha2.db.conn.id' note set.");
			if (driverParam==null)
				throw new SandeshaStorageException ("Cannot instantiate Storage Manager. Parameter 'sandesha2.db.driver' note set.");
			if (sqlDialectParam==null)
				throw new SandeshaStorageException ("Cannot instantiate Storage Manager. Parameter 'sandesha2.db.sql.dialect' note set.");
			if (usernameParam==null)
				throw new SandeshaStorageException ("Cannot instantiate Storage Manager. Parameter 'sandesha2.db.username' note set.");
			if (passwordParam==null)
				throw new SandeshaStorageException ("Cannot instantiate Storage Manager. Parameter 'sandesha2.db.password' note set.");
			if (createModeParam==null)
				throw new SandeshaStorageException ("Cannot instantiate Storage Manager. Parameter 'sandesha2.db.create.mode' note set.");
			
			String dbConnIdentifier = (String) connectionIDParam.getValue();
			String dbDriver = (String) driverParam.getValue();
			String sqlDialect = (String) sqlDialectParam.getValue();
			String username = (String) usernameParam.getValue();
			String password = (String) passwordParam.getValue();
			String dbCreateMode = (String) createModeParam.getValue();
			
			if (dbConnIdentifier!=null)
				dbConnIdentifier=dbConnIdentifier.trim();
			if (dbDriver!=null)
				dbDriver=dbDriver.trim();
			if (sqlDialect!=null)
				sqlDialect=sqlDialect.trim();
			if (username!=null)
				username=username.trim();
			if (password!=null)
				password=password.trim();
			if (dbCreateMode!=null)
				dbCreateMode=dbCreateMode.trim();
			
			if (dbConnIdentifier==null || dbDriver==null || sqlDialect==null)
				throw new SandeshaStorageException ("Can't proceed. Needed properties are not set.");
			
			String[] hbmFiles = new String[] {
					"Sandesha2Beans.hbm.xml"};
			
			//initializing hibernate
			HibernateUtil.configureHibernateUtil (dbConnIdentifier,dbDriver,sqlDialect,username,password,dbCreateMode,hbmFiles);

		} finally {
			//setting back the correct context class loader.
			Thread.currentThread().setContextClassLoader (contextClassLoader);
		}
		
	}

	public MessageContext retrieveMessageContext(String key,ConfigurationContext configContext) throws SandeshaStorageException {

		if (messageContextCache.containsKey(key)) {
			return (MessageContext) messageContextCache.get(key);
		}
		
		Session session = HibernateUtil.currentSession();
		
		MessageStoreBean messageStoreBean = (MessageStoreBean) session.createCriteria(MessageStoreBean.class).add(Restrictions.eq(HibernateUtil.Constants.MESSAGE_STORED_KEY,key)).uniqueResult();
		if (messageStoreBean==null) {
			return null;
		}
		
		MessageContext messageContext = new MessageContext ();
		messageContext.setProperty(Sandesha2Constants.POST_FAILURE_MESSAGE,Sandesha2Constants.VALUE_TRUE);
		try {
			String soapEnvelopeStr = messageStoreBean.getSOAPEnvelopeString();
			StringReader strReader = new StringReader (soapEnvelopeStr);
			
			XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(strReader);
			
			SOAPFactory factory = null;
			if (messageStoreBean.getSOAPVersion()==Sandesha2Constants.SOAPVersion.v1_1) {
				factory = OMAbstractFactory.getSOAP11Factory();
			} else if (messageStoreBean.getSOAPVersion()==Sandesha2Constants.SOAPVersion.v1_2) {
				factory = OMAbstractFactory.getSOAP12Factory();
			} else {
				throw new SandeshaStorageException ("Unknows SOAP version");
			}
			
			//TODO make this SOAP version indipendent
			StAXSOAPModelBuilder builder = (StAXSOAPModelBuilder) OMXMLBuilderFactory.createStAXSOAPModelBuilder(
			        factory, reader);
			SOAPEnvelope envelope = (SOAPEnvelope) builder.getSOAPEnvelope();
			messageContext.setEnvelope(envelope);
			
			messageContext.setMessageID(messageStoreBean.getMessageID());
			
			AxisConfiguration axisConfiguration = configContext.getAxisConfiguration();
			
			String transportOutStr = messageStoreBean.getTransportOut();
			if (transportOutStr!=null) {
				TransportOutDescription transportOut = configContext.getAxisConfiguration().getTransportOut(transportOutStr);
				messageContext.setTransportOut(transportOut);
			}
			
			String serviceGroupName = messageStoreBean.getAxisServiceGroup();
			if (serviceGroupName!=null) {
				AxisServiceGroup serviceGroup = axisConfiguration.getServiceGroup(serviceGroupName);
				if (serviceGroup!=null)
					messageContext.setAxisServiceGroup(serviceGroup);
			} else {
				AxisServiceGroup serviceGroup = new AxisServiceGroup (axisConfiguration);
				messageContext.setAxisServiceGroup(serviceGroup);
			}
			
			String serviceName = messageStoreBean.getAxisService();
			if (serviceName!=null) {
				AxisService service = axisConfiguration.getService(serviceName);
				if (service!=null)
					messageContext.setAxisService(service);
				else {
					String message = "Cant recreate the message since AxisService '" + serviceName +"' is not present";
					throw new SandeshaStorageException (message);
				}
			}
				
			
			String operationNameStr = messageStoreBean.getAxisOperation();
			String operationMEPStr = messageStoreBean.getAxisOperationMEP();
			if ((operationNameStr!=null || operationMEPStr!=null) && messageContext.getAxisService()!=null) {
				
				AxisOperation operation = null;
				if (operationNameStr!=null) {
					QName operationName = PersistentUtil.getQnameFromString (operationNameStr);
					operation = messageContext.getAxisService().getOperation(operationName);
				}
				
				AxisService service = messageContext.getAxisService();
				if (operation==null && operationMEPStr!=null && service!=null) {
					//finding an operation using the MEP.
					
					Iterator operations = service.getOperations();
					while (operations.hasNext()) {
						AxisOperation temp = (AxisOperation) operations.next();
						if (temp.getMessageExchangePattern().equals(operationMEPStr)) {
							operation = temp;
							break;
						}
					}
				}
				
				if (operation!=null) {
					messageContext.setAxisOperation(operation);
				} else {
					String message = "Cant find a suitable operation for the generated message";
					throw new SandeshaStorageException (message);
				}
			}
			
			//setting contexts TODO is this necessary?
			
//			if (messageContext.getAxisServiceGroup()!=null){
//				ServiceGroupContext serviceGroupCtx = new ServiceGroupContext (configContext,messageContext.getAxisServiceGroup());
//				messageContext.setServiceGroupContext(serviceGroupCtx);
//			}
//			
//			if (messageContext.getAxisService()!=null) {
//				ServiceContext serviceContext = new ServiceContext (messageContext.getAxisService(),messageContext.getServiceGroupContext());
//				serviceContext.setParent(messageContext.getServiceGroupContext());
//				messageContext.setServiceContext(serviceContext);
//			}
//			
//			if (messageContext.getAxisOperation()!=null) {
//				OperationContext operationContext = new OperationContext (messageContext.getAxisOperation());
//				operationContext.setParent(messageContext.getServiceContext());
//				
//				messageContext.setOperationContext(operationContext);
//				operationContext.addMessageContext(messageContext);
//				configContext.registerOperationContext(messageContext.getMessageID(),operationContext); //registering the operation context.
//			}
			
			
			messageContext.setServerSide(messageStoreBean.isServerSide());
			messageContext.setFLOW(messageStoreBean.getFlow());
			
			messageContext.setProperty(MessageContextConstants.TRANSPORT_URL,messageStoreBean.getTransportTo());
			messageContext.setTo(new EndpointReference (messageStoreBean.getToURL()));
			
			messageContext.getOptions().setAction(messageStoreBean.getAction());
			
			String persistentPropertyString = messageStoreBean.getPersistentPropertyString();
			if (persistentPropertyString!=null && !persistentPropertyString.trim().equals("")) {
				HashMap map = getPropertyMapFromPersistentString(persistentPropertyString);
				Iterator propertyKeyIter = map.keySet().iterator();
				while (propertyKeyIter.hasNext()) {
					String propertyKey = (String) propertyKeyIter.next();
					String value = (String) map.get(propertyKey);
					
					messageContext.setProperty(propertyKey,value);
				}
			}
			
			//setting the callback. Needed only for the client side.
			String callbackClassName = messageStoreBean.getCallbackClassName();
			if (callbackClassName!=null && !"".equals(callbackClassName.trim())) {
					callbackClassName = callbackClassName.trim();
					Callback callbackInstance = null;
					
					try {
						int dollarPos = callbackClassName.indexOf("$");
						if (dollarPos<0) {
							//instantiate it as a normal class
							Class callbackClass = Class.forName(callbackClassName);
							callbackInstance = (Callback) callbackClass.newInstance();
						} else {
							//instantiate it as a normal class
							String containerClassName = callbackClassName.substring(0,dollarPos);
							
							Class containerClass = Class.forName(containerClassName);
							Class innerClass = Class.forName(callbackClassName);
							
							Object containerInstance = containerClass.newInstance();
							
							Constructor innerConstructor = innerClass.getDeclaredConstructor(new Class[] {containerClass});
							callbackInstance = (Callback) innerConstructor.newInstance(new Object[] {containerInstance});
							
						}
					} catch (Exception e) {
						String message = "Cannot instantiate the given Callback class. Make sure that it has a default constructor";
						log.error(message,e);
					}

					String messageID = messageStoreBean.getMessageID();
					AxisOperation operation = messageContext.getAxisOperation();
					if (operation!=null) {
						CallbackReceiver receiver = (CallbackReceiver) operation.getMessageReceiver();
						if (receiver==null) {
							receiver = new CallbackReceiver ();
							operation.setMessageReceiver(receiver);
						}
						receiver.addCallback(messageID,callbackInstance);
					}
			}
			
		} catch (XMLStreamException e) {
			throw new SandeshaStorageException (e);
		} catch (AxisFault e) {
			throw new SandeshaStorageException (e);
		}
         
		return messageContext;
	}

	public void storeMessageContext(String key,MessageContext msgContext) throws SandeshaStorageException {		
		
		MessageStoreBean bean = getMessageStoreBean(msgContext);
		messageContextCache.put(key,msgContext);
		Session session = HibernateUtil.currentSession();
		bean.setStoredKey(key);
		session.save(bean);
	}

	public void updateMessageContext(String key,MessageContext msgContext) throws SandeshaStorageException {
		
		//TODO throw an exception if not already present
		
		Session session = HibernateUtil.currentSession();
		MessageStoreBean oldBean = (MessageStoreBean) session.createCriteria(MessageStoreBean.class).add(Restrictions.eq(HibernateUtil.Constants.MESSAGE_STORED_KEY,key)).uniqueResult();
		if (oldBean==null)
			throw new SandeshaStorageException ("Cant update message store bean. It is not present in the session");
		
		if (oldBean.getInMessageStoreKey()!=null)
			msgContext.setProperty(PersistenceConstants.IN_MESSAGE_STORAGE_KEY,
					oldBean.getInMessageStoreKey()); //to update the in message
		
		MessageStoreBean bean = getMessageStoreBean(msgContext);
		bean.setStoredKey(key);
		copy(bean,oldBean);
		session.update(oldBean);
	}
	
	public void removeMessageContext(String key) throws SandeshaStorageException {
		
		MessageContext messageInCache = (MessageContext) messageContextCache.get(key);
		if (messageInCache!=null)
			messageContextCache.remove(key);
		
		Session session = HibernateUtil.currentSession();
		
		MessageStoreBean bean = (MessageStoreBean) session.createCriteria(MessageStoreBean.class).add(Restrictions.eq(HibernateUtil.Constants.MESSAGE_STORED_KEY,key)).uniqueResult();
		if (bean!=null) {
			session.delete(bean);
			
			String inMessageKey = bean.getInMessageStoreKey();
			if (inMessageKey!=null)
				removeMessageContext(inMessageKey);
		}		
	}
	
	private void copy (MessageStoreBean from, MessageStoreBean to) {
		to.setAxisOperation(from.getAxisOperation());
		to.setAxisOperationMEP(from.getAxisOperationMEP());
		to.setAxisService(from.getAxisService());
		to.setAxisServiceGroup(from.getAxisServiceGroup());
		to.setExecutionChainString(from.getExecutionChainString());
		to.setFlow(from.getFlow());
		to.setInMessageStoreKey(from.getInMessageStoreKey());
		to.setMessageID(from.getMessageID());
		to.setMessageReceiverString(from.getMessageReceiverString());
		to.setPersistentPropertyString(from.getPersistentPropertyString());
		to.setServerSide(from.isServerSide());
		to.setSOAPEnvelopeString(from.getSOAPEnvelopeString());
		to.setSOAPVersion(from.getSOAPVersion());
		to.setToURL(from.getToURL());
		to.setTransportOut(from.getTransportOut());
		to.setTransportTo(from.getTransportTo());
		to.setCallbackClassName(from.getCallbackClassName());
		to.setAction(from.getAction());
	}
	
	
	private MessageStoreBean getMessageStoreBean (MessageContext msgContext) throws SandeshaStorageException {
		SOAPEnvelope envelope = msgContext.getEnvelope();
		String str = envelope.toString();
		
		int SOAPVersion = 0;
		if (msgContext.isSOAP11())
			SOAPVersion = Sandesha2Constants.SOAPVersion.v1_1;
		else
			SOAPVersion = Sandesha2Constants.SOAPVersion.v1_2;
				
		MessageStoreBean bean = new MessageStoreBean ();
		
		TransportOutDescription transportOutDescription = msgContext.getTransportOut();
		AxisServiceGroup serviceGroup = msgContext.getAxisServiceGroup();
		AxisService service = msgContext.getAxisService();
		AxisOperation operation = msgContext.getAxisOperation();
		
		if (transportOutDescription!=null) {
			String name = transportOutDescription.getName();
			bean.setTransportOut(name);
		}
		
		if (serviceGroup!=null) {
			bean.setAxisServiceGroup(serviceGroup.getServiceGroupName());
		}
		
		if (service!=null) {
			bean.setAxisService(service.getName());
		}
		
		if (operation!=null) {
			QName name = operation.getName();
			if (name!=null)
				bean.setAxisOperation(PersistentUtil.getStringFromQName(name));
			bean.setAxisOperationMEP(operation.getMessageExchangePattern());
		}
		
		bean.setFlow(msgContext.getFLOW());
		bean.setServerSide(msgContext.isServerSide());
	
		bean.setSOAPEnvelopeString(str);
		bean.setSOAPVersion(SOAPVersion);

		bean.setMessageID(msgContext.getMessageID());
		
		bean.setToURL(msgContext.getTo().getAddress());
		bean.setTransportTo((String) msgContext.getProperty(MessageContextConstants.TRANSPORT_URL));
		
		bean.setAction(msgContext.getOptions().getAction());
		
		String persistantPropertyString = getPersistentPropertyString (msgContext);
		bean.setPersistentPropertyString(persistantPropertyString);
		
		//setting the request message if this a response message.
		OperationContext operationContext = msgContext.getOperationContext();
		
		try {
			
			if (operationContext!=null) {
				if (operationContext.getMessageContexts().size()>1) {
					if (operationContext.getMessageContext(OperationContextFactory.MESSAGE_LABEL_OUT_VALUE)==msgContext) {
						MessageContext inMessageContext = operationContext.getMessageContext(OperationContextFactory.MESSAGE_LABEL_IN_VALUE);
						if (inMessageContext!=null) {
							
							//inMessageContext.setOperationContext(null);
							
							String inMessageStoreKey = (String) msgContext.getProperty(PersistenceConstants.IN_MESSAGE_STORAGE_KEY);
							boolean insert = false;
							
							if (inMessageStoreKey==null) {
								inMessageStoreKey = SandeshaUtil.getUUID();
								insert = true;
							}
							
							if (insert)
								storeMessageContext(inMessageStoreKey,inMessageContext);
							else 
								updateMessageContext(inMessageStoreKey,inMessageContext);
							
							bean.setInMessageStoreKey(inMessageStoreKey);
						}
					}
				}
			}
			
			//getting the callback class name (if present) as a string. Only done for the client side.
			if (!msgContext.isServerSide()) {
				String messageID = msgContext.getMessageID();
				AxisOperation axisOperation = msgContext.getAxisOperation();
				if (axisOperation!=null && messageID!=null) {
					MessageReceiver messageReceiver = axisOperation.getMessageReceiver();
					if ((messageReceiver instanceof CallbackReceiver) && messageReceiver!=null) {
						CallbackReceiver callbackReceiver = (CallbackReceiver) messageReceiver;
						Callback callback = (Callback) callbackReceiver.getCallbackStore().get(messageID);
						if (callback!=null) {
							String callbackClassName = callback.getClass().getName();
							bean.setCallbackClassName(callbackClassName);
						}
					}
				}
			}
			
		} catch (AxisFault e) {
			throw new SandeshaStorageException (e);
		}
		
		return bean;
	} 
	
	private String getPersistentPropertyString (MessageContext messageContext) {
		
		ArrayList propertyNames = new ArrayList ();
		
		propertyNames.add(AddressingConstants.WS_ADDRESSING_VERSION);
		propertyNames.add(Sandesha2Constants.QUALIFIED_FOR_SENDING);
		
		Iterator optionsPropertyIter = messageContext.getOptions().getProperties().keySet().iterator();
		while (optionsPropertyIter.hasNext())
			propertyNames.add(optionsPropertyIter.next());
		
		
		Iterator propertyNamesIter = propertyNames.iterator();
		String propertyString = "";
		while (propertyNamesIter.hasNext()) {
			String propertyName = (String) propertyNamesIter.next();
			Object propertyVal = messageContext.getProperty (propertyName);
			if (propertyVal!=null && (propertyVal instanceof String)) {
				if (propertyString.trim().length()>0) {
					propertyString = propertyString + PersistenceConstants.PERSISTANT_PROPERTY_SEPERATOR;
				}
				
				propertyString = propertyString + propertyName + PersistenceConstants.PERSISTANT_PROPERTY_SEPERATOR
										+ (String) propertyVal;
			}
		}
		
		return propertyString;
		
	}
	
	private HashMap getPropertyMapFromPersistentString (String string) throws SandeshaStorageException {
		HashMap map = new HashMap ();
		
		String[] values = string.split(PersistenceConstants.PERSISTANT_PROPERTY_SEPERATOR);
		if (values.length%2!=0 || (values.length==1 && "".equals(values[0])))
			throw new SandeshaStorageException ("Invalid persistence property String");
		
		for (int i=0;i<values.length;i=i+2) {
			String key = values[i];
			String val = values[i+1];
			
			map.put(key,val);
		}
		
		return map;
	}
	
}
