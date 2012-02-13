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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.sandesha2.storage.SandeshaStorageException;
import org.apache.sandesha2.storage.Transaction;
import org.apache.sandesha2.storage.beanmanagers.InvokerBeanMgr;
import org.apache.sandesha2.storage.beans.InvokerBean;
import org.apache.sandesha2.storage.beans.RMSBean;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;

/**
 * Manages Invoker Beans using Hibernate.
 */
public class PersistentInvokerBeanMgr implements InvokerBeanMgr {

	Log log = LogFactory.getLog(getClass());
	
	public boolean delete(String key) throws SandeshaStorageException {
		
		InvokerBean invokerBean = retrieve(key);
		
		Session session = HibernateUtil.currentSession();
		if (invokerBean!=null)
			session.delete(invokerBean);
		
		return true;
	}
	
	public List find(InvokerBean bean) throws SandeshaStorageException {
		
		Session session = HibernateUtil.currentSession();
		
		InvokerBean invokerExample = bean;
		Example example = Example.create(invokerExample).excludeNone().excludeZeroes();
		
		List list = session.createCriteria(InvokerBean.class).add(example).list();
		List list1 = session.createCriteria(InvokerBean.class).list();
		return list;
	}
	
	public boolean insert(InvokerBean bean) throws SandeshaStorageException {
		
		Session session = HibernateUtil.currentSession();
		
		session.save(bean);

		return true;
	}
	public InvokerBean retrieve(String key) throws SandeshaStorageException {
		
		Session session = HibernateUtil.currentSession();
		
		InvokerBean invokerBean = (InvokerBean) session.createCriteria(InvokerBean.class).add(Restrictions.eq(HibernateUtil.Constants.MESSAGE_CONTEXT_REF_KEY,key)).uniqueResult();

		return invokerBean;
	}
	public boolean update(InvokerBean bean) throws SandeshaStorageException {
		
		InvokerBean oldBean = retrieve(bean.getMessageContextRefKey());
		if (oldBean==null)
			throw new SandeshaStorageException ("Can't update. The item was not found in the database");
		
		copy(oldBean,bean);
		
		Session session = HibernateUtil.currentSession();
		
		session.update(oldBean);

		return true;
	}
	
	public InvokerBean findUnique(InvokerBean bean) throws SandeshaStorageException {
		
		Session session = HibernateUtil.currentSession();
		
		InvokerBean invokerExample = bean;

		//		Example example = Example.create(invokerExample).excludeNone().excludeZeroes();
		//		InvokerBean invokerBean = (InvokerBean) session.createCriteria(InvokerBean.class).add(example).uniqueResult();
		
		Map equalsMap = getEqualsMap (invokerExample);
		InvokerBean invokerBean = (InvokerBean) session.createCriteria(InvokerBean.class).add(Restrictions.allEq(equalsMap)).uniqueResult();
		
		return invokerBean;
	}
	
	private void copy (InvokerBean to, InvokerBean from) {
		to.setMsgNo(from.getMsgNo());
		to.setSequenceID(from.getSequenceID());
		to.setMessageContextRefKey(from.getMessageContextRefKey());
	}
	
	private Map getEqualsMap (InvokerBean exampleBean) {
		Map equalsMap = new HashMap ();
		
		int flags = exampleBean.getFlags();
		
		if (exampleBean.getMessageContextRefKey()!=null)
			equalsMap.put("messageContextRefKey", exampleBean.getMessageContextRefKey());
		if (exampleBean.getSequenceID()!=null)
			equalsMap.put("sequenceID", exampleBean.getSequenceID());
		
		//TODO do above for Non-String properties.
		
		return equalsMap;
	}
	
}
