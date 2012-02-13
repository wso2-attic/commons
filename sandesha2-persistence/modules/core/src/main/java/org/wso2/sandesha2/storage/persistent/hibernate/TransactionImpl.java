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

import org.apache.sandesha2.storage.Transaction;
import org.hibernate.Session;

/**
 * An implementaion of the Sandesha2 transaction interace that wraps a 
 * Hibernate transaction object.
 */
public class TransactionImpl implements Transaction {

	org.hibernate.Transaction transaction = null;
	boolean active = false;
	
	public TransactionImpl () {
		Session session = HibernateUtil.currentSession();
		transaction = session.beginTransaction();
		active = true;
	}
	
	public void commit() {
		transaction.commit();
		closeSession();
		active = false;
	}
	
	public void rollback() {
		transaction.rollback();
		active = false;
	}
	
	private void closeSession () {
		HibernateUtil.closeSession();
	}
	
	public boolean isActive () {
		return active;
	}
	
}
