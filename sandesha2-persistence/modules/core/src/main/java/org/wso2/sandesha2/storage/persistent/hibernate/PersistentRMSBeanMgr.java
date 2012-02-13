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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.sandesha2.storage.SandeshaStorageException;
import org.apache.sandesha2.storage.Transaction;
import org.apache.sandesha2.storage.beanmanagers.RMSBeanMgr;
import org.apache.sandesha2.storage.beans.RMSBean;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;

/**
 * Manages Create Sequence Beans using Hibernate.
 */
public class PersistentRMSBeanMgr implements RMSBeanMgr {

	public boolean delete(String msgId) throws SandeshaStorageException {

		RMSBean rmsBean = retrieve(msgId);
		
		Session session = HibernateUtil.currentSession();
		
		if (rmsBean!=null)
			session.delete(rmsBean);
		
		return true;
	}
	
	public List find(RMSBean bean) throws SandeshaStorageException {
		
		Session session = HibernateUtil.currentSession();
		
		RMSBean rmsExampleBean = bean;
		Example example = Example.create(rmsExampleBean).excludeNone().excludeZeroes();
		
		List list = session.createCriteria(RMSBean.class).add(example).list();
		
		return list;
	}
	
	public boolean insert(RMSBean bean) throws SandeshaStorageException {
		
		Session session = HibernateUtil.currentSession();
		
		session.save(bean);
		
		return true;
	}
	
	public RMSBean retrieve(String msgId) throws SandeshaStorageException {
		
		Session session = HibernateUtil.currentSession();
		
		RMSBean rmsBean = (RMSBean) session.createCriteria(RMSBean.class).add(Restrictions.eq(HibernateUtil.Constants.CREATE_SEQ_MSG_ID,msgId)).uniqueResult();
		
		return rmsBean;
	}
	
	public boolean update(RMSBean bean) throws SandeshaStorageException {
		
		RMSBean oldBean = retrieve(bean.getCreateSeqMsgID());
		if (oldBean==null)
			throw new SandeshaStorageException ("Cant update. The item was not found in the database");
		
		copy(oldBean,bean);
		
		Session session = HibernateUtil.currentSession();
		
		session.update(oldBean);
		
		return true;
	}
	
	public RMSBean findUnique(RMSBean bean) throws SandeshaStorageException {
		
		Session session = HibernateUtil.currentSession();
		
		RMSBean rmsExampleBean = bean;
		
		Map equalsMap = getEqualsMap (rmsExampleBean);
		
//		Example example = Example.create(rmsExampleBean).excludeNone().excludeZeroes();
		
		RMSBean rmsBean = (RMSBean) session.createCriteria(RMSBean.class).add(Restrictions.allEq(equalsMap)).uniqueResult();
		
		return rmsBean;
	}
	
	private void copy (RMSBean to, RMSBean from) {
		to.setInternalSequenceID(from.getInternalSequenceID());
		to.setSequenceID(from.getSequenceID());
		
		to.setAcksToEPR(from.getAcksToEPR());
		to.setAnonymousUUID(from.getAnonymousUUID());
		to.setAvoidAutoTermination(from.isAvoidAutoTermination());
		to.setClientCompletedMessages(from.getClientCompletedMessages());
		to.setClosed(from.isClosed());
		to.setCreateSeqMsgID(from.getCreateSeqMsgID());
		to.setCreateSequenceMsgStoreKey(from.getCreateSequenceMsgStoreKey());
		to.setExpectedReplies(from.getExpectedReplies());
		to.setHighestOutMessageNumber(from.getHighestOutMessageNumber());
		to.setHighestOutRelatesTo(from.getHighestOutRelatesTo());
		to.setInternalSequenceID(from.getInternalSequenceID());
		to.setLastActivatedTime(from.getLastActivatedTime());
		to.setLastOutMessage(from.getLastOutMessage());
		to.setLastSendError(from.getLastSendError());
		to.setLastSendErrorTimestamp(from.getLastSendErrorTimestamp());
		to.setNextMessageNumber(from.getNextMessageNumber());
		to.setOfferedEndPoint(from.getOfferedEndPoint());
		to.setOfferedSequence(from.getOfferedSequence());
		to.setPollingMode(from.isPollingMode());
		to.setReferenceMessageStoreKey(from.getReferenceMessageStoreKey());
		to.setReplyToEPR(from.getReplyToEPR());
		to.setRMVersion(from.getRMVersion());
		to.setSecurityTokenData(from.getSecurityTokenData());
		to.setSequenceClosedClient(from.isSequenceClosedClient());
		to.setSequenceID(from.getSequenceID());
		to.setServiceName(from.getServiceName());
		to.setSoapVersion(from.getSoapVersion());
		to.setTerminateAdded(from.isTerminateAdded());
		to.setTransportTo(from.getTransportTo());
	}
	
	private Map getEqualsMap (RMSBean exampleBean) {
		Map equalsMap = new HashMap ();
		
		int flags = exampleBean.getFlags();
		
		if (exampleBean.getSequenceID()!=null)
			equalsMap.put("sequenceID", exampleBean.getSequenceID());
		if (exampleBean.getInternalSequenceID()!=null)
			equalsMap.put("internalSequenceID", exampleBean.getInternalSequenceID());
		if (exampleBean.getToEPR()!=null)
			equalsMap.put("toEPR", exampleBean.getToEPR());
		if (exampleBean.getAcksToEPR()!=null)
			equalsMap.put("internalSequenceID", exampleBean.getAcksToEPR());
		if (exampleBean.getOfferedEndPoint()!=null)
			equalsMap.put("internalSequenceID", exampleBean.getOfferedEndPoint());
		if (exampleBean.getOfferedSequence()!=null)
			equalsMap.put("internalSequenceID", exampleBean.getOfferedSequence());
		if (exampleBean.getRMVersion()!=null)
			equalsMap.put("internalSequenceID", exampleBean.getRMVersion());
		if (exampleBean.getAnonymousUUID()!=null)
			equalsMap.put("internalSequenceID", exampleBean.getAnonymousUUID());
		if (exampleBean.getCreateSeqMsgID()!=null)
			equalsMap.put("internalSequenceID", exampleBean.getCreateSeqMsgID());
		if (exampleBean.getCreateSequenceMsgStoreKey()!=null)
			equalsMap.put("internalSequenceID", exampleBean.getCreateSequenceMsgStoreKey());	
		if (exampleBean.getHighestOutRelatesTo()!=null)
			equalsMap.put("internalSequenceID", exampleBean.getHighestOutRelatesTo());	
		if (exampleBean.getReferenceMessageStoreKey()!=null)
			equalsMap.put("internalSequenceID", exampleBean.getReferenceMessageStoreKey());
		if (exampleBean.getReplyToEPR()!=null)
			equalsMap.put("internalSequenceID", exampleBean.getReplyToEPR());	
		if (exampleBean.getSecurityTokenData()!=null)
			equalsMap.put("internalSequenceID", exampleBean.getSecurityTokenData());		
		if (exampleBean.getServiceName()!=null)
			equalsMap.put("internalSequenceID", exampleBean.getServiceName());
		if (exampleBean.getTransportTo()!=null)
			equalsMap.put("internalSequenceID", exampleBean.getTransportTo());
		
		//TODO do above for Non-String properties.
				
		return equalsMap;
	}
}
