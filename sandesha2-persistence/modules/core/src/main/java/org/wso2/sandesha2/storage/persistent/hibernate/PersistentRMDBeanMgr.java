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
import java.util.List;
import java.util.Map;

import org.apache.sandesha2.storage.SandeshaStorageException;
import org.apache.sandesha2.storage.Transaction;
import org.apache.sandesha2.storage.beanmanagers.RMDBeanMgr;
import org.apache.sandesha2.storage.beans.RMDBean;
import org.apache.sandesha2.storage.beans.SenderBean;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;

/**
 * Manages NextMsg Beans using Hibernate.
 */
public class PersistentRMDBeanMgr implements RMDBeanMgr {

	public boolean delete(String sequenceID) throws SandeshaStorageException {
		
		RMDBean rmdBean = retrieve(sequenceID);
		Session session = HibernateUtil.currentSession();
		if (rmdBean!=null)
			session.delete(rmdBean);
		
		return true;
	}
	
	public List find(RMDBean bean) throws SandeshaStorageException {
		
		Session session = HibernateUtil.currentSession();
		
		RMDBean nextMsgExample = bean;
		Example example = Example.create(nextMsgExample).excludeNone().excludeZeroes();
		
		List list = session.createCriteria(RMDBean.class).add(example).list();
		
		return list;
	}
	
	public boolean insert(RMDBean bean) throws SandeshaStorageException {
		
		Session session = HibernateUtil.currentSession();
		
		session.save(bean);
		
		return true;
	}
	
	public RMDBean retrieve(String sequenceID)
			throws SandeshaStorageException {
		
		Session session = HibernateUtil.currentSession();
		
		RMDBean rmdBean = (RMDBean) session.createCriteria(RMDBean.class).add(Restrictions.eq(HibernateUtil.Constants.SEQUENCE_ID,sequenceID)).uniqueResult();
		
		return rmdBean;
	}
	
	public Collection retrieveAll() {
		
		Session session = HibernateUtil.currentSession();
		
		List nextMsgBeanList = session.createCriteria(RMDBean.class).list();
		
		return nextMsgBeanList;
	}
	
	public boolean update(RMDBean bean) throws SandeshaStorageException {
		
		RMDBean oldBean = retrieve(bean.getSequenceID());
		if (oldBean==null)
			throw new SandeshaStorageException ("Cant update. The item was not found in the database");
		
		copy(oldBean,bean);
		
		Session session = HibernateUtil.currentSession();
		
		session.update(oldBean);
		
		return true;
	}
	
	public RMDBean findUnique(RMDBean bean) throws SandeshaStorageException {
		
		Session session = HibernateUtil.currentSession();
		
		RMDBean nextMsgExample = bean;

		Map equalsMap = getEqualsMap (nextMsgExample);
		RMDBean rmdBean = (RMDBean) session.createCriteria(RMDBean.class).add(Restrictions.allEq(equalsMap)).uniqueResult();
		
		return rmdBean;
	}
	
	private void copy (RMDBean to, RMDBean from) {
		to.setAcksToEPR(from.getAcksToEPR());
		to.setClosed(from.isClosed());
		to.setHighestInMessageId(from.getHighestInMessageId());
		to.setHighestInMessageNumber(from.getHighestInMessageNumber());
		to.setLastActivatedTime(from.getLastActivatedTime());
		to.setLastInMessageId(from.getLastInMessageId());
		to.setNextMsgNoToProcess(from.getNextMsgNoToProcess());
		to.setOutboundInternalSequence(from.getOutboundInternalSequence());
		to.setOutOfOrderRanges(from.getOutOfOrderRanges());
		to.setPollingMode(from.isPollingMode());
		to.setReferenceMessageKey(from.getReferenceMessageKey());
		to.setReplyToEPR(from.getReplyToEPR());
		to.setRMVersion(from.getRMVersion());
		to.setSecurityTokenData(from.getSecurityTokenData());
		to.setSequenceID(from.getSequenceID());
		to.setServerCompletedMessages(from.getServerCompletedMessages());
		to.setServiceName(from.getServiceName());
		to.setTerminated(from.isTerminated());
		to.setToAddress(from.getToAddress());
		to.setToEPR(from.getToEPR());
	}
	
	private Map getEqualsMap (RMDBean exampleBean) {
		
		Map equalsMap = new HashMap ();
		
		if (exampleBean.getAcksToEPR()!=null)
			equalsMap.put("acksToEPR", exampleBean.getAcksToEPR());
		if (exampleBean.getHighestInMessageId()!=null)
			equalsMap.put("highestInMessageId", exampleBean.getHighestInMessageId());
		if (exampleBean.getLastInMessageId()!=null)
			equalsMap.put("lastInMessageId", exampleBean.getLastInMessageId());
		if (exampleBean.getOutboundInternalSequence()!=null)
			equalsMap.put("outboundInternalSequence", exampleBean.getOutboundInternalSequence());
		if (exampleBean.getReferenceMessageKey()!=null)
			equalsMap.put("referenceMessageKey", exampleBean.getReferenceMessageKey());
		if (exampleBean.getToAddress()!=null)
			equalsMap.put("toAddress", exampleBean.getToAddress());
		if (exampleBean.getReplyToEPR()!=null)
			equalsMap.put("replyToEPR", exampleBean.getReplyToEPR());
		if (exampleBean.getRMVersion()!=null)
			equalsMap.put("RMVersion", exampleBean.getRMVersion());
		if (exampleBean.getSecurityTokenData()!=null)
			equalsMap.put("securityTokenData", exampleBean.getSecurityTokenData());
		if (exampleBean.getServiceName()!=null)
			equalsMap.put("serviceName", exampleBean.getServiceName());
		if (exampleBean.getToEPR()!=null)
			equalsMap.put("toEPR", exampleBean.getToEPR());
		if (exampleBean.getSequenceID()!=null)
			equalsMap.put("sequenceID", exampleBean.getSequenceID());
		
		//TODO do above for Non-String properties.
				
		return equalsMap;
	}
	
}
