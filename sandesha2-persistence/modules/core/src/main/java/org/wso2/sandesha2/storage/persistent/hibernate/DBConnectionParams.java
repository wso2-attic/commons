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

/**
 * Constants that will be the key when refering to database constants.
 */
public interface DBConnectionParams {
	String DB_CONNECTION_ID = "sandesha2.db.conn.id";
	String DB_DRIVER = "sandesha2.db.driver";
	String DB_SQL_DIALECT = "sandesha2.db.sql.dialect";
	String USERNAME = "sandesha2.db.username";
	String PASSWORD = "sandesha2.db.password";
	String DB_CREATE_MODE = "sandesha2.db.create.mode";
}
