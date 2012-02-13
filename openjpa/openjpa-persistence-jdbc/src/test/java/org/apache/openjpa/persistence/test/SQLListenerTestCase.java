/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */
package org.apache.openjpa.persistence.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.openjpa.lib.jdbc.AbstractJDBCListener;
import org.apache.openjpa.lib.jdbc.JDBCEvent;
import org.apache.openjpa.lib.jdbc.JDBCListener;

/**
 * Base class for tests that need to check generated SQL.
 *
 * @author Patrick Linskey
 */
public abstract class SQLListenerTestCase
    extends SingleEMFTestCase {
    private static String _nl = System.getProperty("line.separator");
    protected List<String> sql = new ArrayList<String>();

    @Override
    public void setUp(Object... props) {
        Object[] copy = new Object[props.length + 2];
        System.arraycopy(props, 0, copy, 0, props.length);
        copy[copy.length - 2] = "openjpa.jdbc.JDBCListeners";
        copy[copy.length - 1] = new JDBCListener[] { new Listener() };
        super.setUp(copy); 
    }

    /**
     * Confirm that the specified SQL has been executed.
     *
     * @param sqlExp the SQL expression. E.g., "SELECT FOO .*"
     */
    public void assertSQL(String sqlExp) {
        for (String statement : sql) {
            if (statement.matches(sqlExp))
                return;
        }

        fail("Expected regular expression\r\n <" + sqlExp 
           + ">\r\n to have existed in SQL statements: \r\n" + toString(sql));
    }
    
    /**
     * Confirm that the specified SQL has not been executed.
     *
     * @param sqlExp the SQL expression. E.g., "SELECT BADCOLUMN .*"
     */
    public void assertNotSQL(String sqlExp) {
        for (String statement : sql) {
            if (statement.matches(sqlExp)) {
                fail("Regular expression\r\n <"
                    + sqlExp
                    + ">\r\n should not have been executed in SQL statements:"
                    + "\r\n" + toString(sql));
                break;
            }
        }
    }

    /**
     * Confirm that the executed SQL String contains the specified sqlExp.
     *
     * @param sqlExp the SQL expression. E.g., "SELECT BADCOLUMN .*"
     */
    public void assertContainsSQL(String sqlExp) {
        for (String statement : sql) {
            if (statement.contains(sqlExp))
                return;
        }

        fail("Expected regular expression\r\n <" + sqlExp + ">\r\n to be"
            + " contained in SQL statements: \r\n" + toString(sql));
    }

    /**
     * Confirm the list of expected SQL expressions have been executed in the
     * order specified. I.e. additional SQL statements can be executed in
     * between expected SQLs.
     * 
     * @param expected
     *            SQL expressions. E.g., ("SELECT FOO .*", "UPDATE .*")
     */
    public void assertAllSQLInOrder(String... expected) {
        assertSQLInOrder(false, expected);
    }

    /**
     * Confirm the list of expected SQL expressions have been executed in the
     * exact number and order specified.
     * 
     * @param expected
     *            SQL expressions. E.g., ("SELECT FOO .*", "UPDATE .*")
     */
    public void assertAllExactSQLInOrder(String... expected) {
        assertSQLInOrder(true, expected);
    }

    private void assertSQLInOrder(boolean exact, String... expected) {
        boolean match = false;
        int sqlSize = sql.size();
        if (expected.length <= sqlSize) {
            int hits = 0;
            for (String executedSQL : sql) {
                if (executedSQL.matches(expected[hits])) {
                    if (++hits == expected.length)
                        break;
                }
            }
            match = hits == (exact ? sqlSize : expected.length);
        }

        if (!match) {
            StringBuilder sb = new StringBuilder();
            sb.append("Did not find SQL in expected order : ").append(_nl);
            for (String s : expected) {
                sb.append(s).append(_nl);
            }

            sb.append("SQL Statements issued : ");
            for (String s : sql) {
                sb.append(s).append(_nl);
            }
            fail(sb.toString());
        }
    }

    /**
     * Confirm the list of expected SQL expressions have executed in any order.
     * 
     * @param expected
     *            SQL expressions. E.g., ("SELECT FOO .*", "UPDATE .*")
     */
    public void assertAllSQLAnyOrder(String... expected) {
        for (String statement : expected) {
            assertSQL(statement);
        }
    }

    /**
     * Confirm the list of expected SQL expressions have not executed in any
     * order.
     * 
     * @param expected
     *            SQL expressions. E.g., ("SELECT FOO .*", "UPDATE .*")
     */
    public void assertNoneSQLAnyOrder(String... expected) {
        for (String statement : expected) {
            assertNotSQL(statement);
        }
    }

    /**
     * Confirm the any of expected SQL expressions have executed in any order.
     * 
     * @param expected
     *            SQL expressions. E.g., ("SELECT FOO .*", "UPDATE .*")
     */
    public void assertAnySQLAnyOrder(String... expected) {
        for (String sqlExp : expected) {
            for (String statement : sql) {
                if (statement.matches(sqlExp))
                    return;
            }
        }
        fail("Expected regular expression\r\n <"
            + toString(Arrays.asList(expected)) + ">\r\n to be"
            + " contained in SQL statements: \r\n" + toString(sql));
    }
    
    /**
     * Gets the number of SQL issued since last reset.
     */
    public int getSQLCount() {
        return sql.size();
    }
    
    /**
     * Resets SQL count.
     * @return number of SQL counted since last reset.
     */
    public int resetSQL() {
        int tmp = sql.size();
        sql.clear();
        return tmp;
    }

    public String toString(List<String> list) {
    	StringBuffer buf = new StringBuffer();
    	for (String s : list)
    		buf.append(s).append("\r\n");
    	return buf.toString();
    }

    public class Listener
        extends AbstractJDBCListener {

        @Override
        public void beforeExecuteStatement(JDBCEvent event) {
            if (event.getSQL() != null && sql != null) {
                sql.add(event.getSQL());
            }
        }
    }

    public enum SQLAssertType {
        SQL, NotSQL, ContainsSQL, AllSQLInOrder, AllExactSQLInOrder, 
        AllSQLAnyOrder, NoneSQLAnyOrder, AnySQLAnyOrder
    };

    public class SQLAssertions {
        SQLAssertType type;
        String[] template;

        public SQLAssertions(SQLAssertType type, String... template) {
            this.type = type;
            this.template = template;
        }

        public void validate() {
            switch (type) {
            case SQL:
                assertSQL(template[0]);
                break;
            case NotSQL:
                assertNotSQL(template[0]);
                break;
            case ContainsSQL:
                assertContainsSQL(template[0]);
                break;
            case AllSQLInOrder:
                assertAllSQLInOrder(template);
                break;
            case AllExactSQLInOrder:
                assertAllExactSQLInOrder(template);
                break;
            case AllSQLAnyOrder:
                assertAllSQLAnyOrder(template);
                break;
            case AnySQLAnyOrder:
                assertAnySQLAnyOrder(template);
                break;
            case NoneSQLAnyOrder:
                assertNoneSQLAnyOrder(template);
            }
        }
    }
}