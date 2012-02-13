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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.sandesha2.storage.SandeshaStorageException;
import org.apache.sandesha2.storage.Transaction;
import org.apache.sandesha2.storage.beanmanagers.SenderBeanMgr;
import org.apache.sandesha2.storage.beans.InvokerBean;
import org.apache.sandesha2.storage.beans.SenderBean;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;

/**
 * Manages Sender Beans using Hibernate.
 */
public class PersistentSenderBeanMgr implements SenderBeanMgr {

	public boolean delete(String messageID) throws SandeshaStorageException {

		SenderBean senderBean = retrieve(messageID);
		
		Session session = HibernateUtil.currentSession();
		
		if (senderBean!=null)
			session.delete(senderBean);

		return true;
	}
	
	public List find(SenderBean bean) throws SandeshaStorageException {
		
		Session session = HibernateUtil.currentSession();
		
		SenderBean senderBeanExample = bean;
		Example example = Example.create(senderBeanExample).excludeNone().excludeZeroes();
		
		List list = session.createCriteria(SenderBean.class).add(example).list();
		
		return list;
	}
	
	public List find(String internalSequenceId) throws SandeshaStorageException {
		
		Session session = HibernateUtil.currentSession();
		
		List list = session.createCriteria(SenderBean.class).add(Restrictions.eq(HibernateUtil.Constants.INTERNAL_SEQ_ID,internalSequenceId)).list();
		
		return list;
	}
	
	public SenderBean getNextMsgToSend(String sequenceId) throws SandeshaStorageException {
		
		Session session = HibernateUtil.currentSession();
		
		Iterator iterator = session.createCriteria(SenderBean.class).list().iterator();
	
		while (iterator.hasNext()) {
			SenderBean temp = (SenderBean) iterator.next();

			if (temp.isSend()) {
				long timeToSend = temp.getTimeToSend();
				long timeNow = System.currentTimeMillis();
				if ((timeNow >= timeToSend) && sequenceId.equals(temp.getSequenceID())) {
					return temp;
				}
			}
		}
		
		return null;
	}
	
	public boolean insert(SenderBean bean) throws SandeshaStorageException {
		
		Session session = HibernateUtil.currentSession();
		
		session.save(bean);
		
		return true;
	}
	
	public SenderBean retrieve(String messageID)
			throws SandeshaStorageException {
		
		Session session = HibernateUtil.currentSession();
		
		SenderBean senderBean = (SenderBean) session.createCriteria(SenderBean.class).add(Restrictions.eq(HibernateUtil.Constants.MESSAGE_ID,messageID)).uniqueResult();

		return senderBean;
	}
	
	public boolean update(SenderBean bean) throws SandeshaStorageException {
		
		SenderBean oldBean = retrieve(bean.getMessageID());
		if (oldBean==null)
			throw new SandeshaStorageException ("Cant update. The item was not found in the database");
		
		copy(oldBean,bean);
		
		Session session = HibernateUtil.currentSession();
		
		session.update(oldBean);
		
		return true;
	}
	
	public SenderBean findUnique(SenderBean bean) throws SandeshaStorageException {
		
		Session session = HibernateUtil.currentSession();
		
		SenderBean senderExample = bean;

		Map equalsMap = getEqualsMap (senderExample);
		SenderBean senderBean = (SenderBean) session.createCriteria(SenderBean.class).add(Restrictions.allEq(equalsMap)).uniqueResult();
		
		return senderBean;
	}

	public SenderBean retrieveFromMessageRefKey(String messageContextRefKey) {
		
		Session session = HibernateUtil.currentSession();
		
		SenderBean senderBean = (SenderBean) session.createCriteria(SenderBean.class).add(Restrictions.eq(HibernateUtil.Constants.MESSAGE_CONTEXT_REF_KEY,messageContextRefKey)).uniqueResult();
		
		return senderBean;
	}
	
	private void copy (SenderBean to, SenderBean from) {
		to.setInboundMessageNumber(from.getInboundMessageNumber());
		to.setInboundSequenceId(from.getInboundSequenceId());
		to.setLastMessage(from.isLastMessage());
		to.setMessageID(from.getMessageID());
		to.setToAddress(from.getToAddress());
		to.setTransportAvailable(from.isTransportAvailable());
		to.setInternalSequenceID(from.getInternalSequenceID());
		to.setMessageContextRefKey(from.getMessageContextRefKey());
		to.setMessageNumber(from.getMessageNumber());
		to.setMessageType(from.getMessageType());
		to.setReSend(from.isReSend());
		to.setSend(from.isSend());
		to.setSentCount(from.getSentCount());
		to.setSequenceID(from.getSequenceID());
		to.setTimeToSend(from.getTimeToSend());
	}
	
	private Map getEqualsMap (SenderBean exampleBean) {
		Map equalsMap = new HashMap ();
		
		if (exampleBean.getInboundSequenceId()!=null)
			equalsMap.put("inboundSequenceId", exampleBean.getInboundSequenceId());
		if (exampleBean.getInternalSequenceID()!=null)
			equalsMap.put("internalSequenceID", exampleBean.getSequenceID());
		if (exampleBean.getMessageContextRefKey()!=null)
			equalsMap.put("messageContextRefKey", exampleBean.getMessageContextRefKey());
		if (exampleBean.getMessageID()!=null)
			equalsMap.put("messageID", exampleBean.getMessageID());
		if (exampleBean.getSequenceID()!=null)
			equalsMap.put("sequenceID", exampleBean.getSequenceID());
		if (exampleBean.getToAddress()!=null)
			equalsMap.put("toAddress", exampleBean.getToAddress());
		
		//TODO do above for Non-String properties.
				
		return equalsMap;
	}
	
	
}
