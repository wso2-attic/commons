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
package org.apache.openjpa.jdbc.sql;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.apache.openjpa.jdbc.kernel.JDBCFetchConfiguration;
import org.apache.openjpa.jdbc.kernel.JDBCStore;
import org.apache.openjpa.jdbc.kernel.exps.FilterValue;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.jdbc.schema.Sequence;
import org.apache.openjpa.jdbc.schema.Table;
import org.apache.openjpa.kernel.Filters;
import org.apache.openjpa.lib.jdbc.DelegatingConnection;
import org.apache.openjpa.lib.jdbc.DelegatingPreparedStatement;
import org.apache.openjpa.lib.util.ConcreteClassGenerator;
import org.apache.openjpa.lib.util.J2DoPrivHelper;
import org.apache.openjpa.lib.util.Localizer;
import org.apache.openjpa.meta.JavaTypes;
import org.apache.openjpa.util.InternalException;
import org.apache.openjpa.util.StoreException;
import org.postgresql.PGConnection;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;

/**
 * Dictionary for PostgreSQL.
 */
public class PostgresDictionary
    extends DBDictionary {

    private static final Localizer _loc = Localizer.forPackage
        (PostgresDictionary.class);

    private static Constructor<PostgresConnection> postgresConnectionImpl;
    private static Constructor<PostgresPreparedStatement> postgresPreparedStatementImpl;

    private Method dbcpGetDelegate;
    private Method connectionUnwrap;

    static {
        try {
            postgresConnectionImpl = ConcreteClassGenerator.getConcreteConstructor(
                    PostgresConnection.class,
                    Connection.class, 
                    PostgresDictionary.class);
            postgresPreparedStatementImpl = ConcreteClassGenerator.getConcreteConstructor(
                    PostgresPreparedStatement.class,
                    PreparedStatement.class, 
                    Connection.class, 
                    PostgresDictionary.class);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * SQL statement to load all sequence schema,name pairs from all schemas.
     */
    public String allSequencesSQL = "SELECT NULL AS SEQUENCE_SCHEMA, relname " +
        "AS SEQUENCE_NAME FROM pg_class WHERE relkind='S'";

    /**
     * SQL statement to load schema,name pairs for all sequences with a
     * certain name from all schemas.
     */
    public String namedSequencesFromAllSchemasSQL = "SELECT NULL AS " +
        "SEQUENCE_SCHEMA, relname AS SEQUENCE_NAME FROM pg_class " +
        "WHERE relkind='S' AND relname = ?";

    /**
     * SQL statement to load schema,name pairs from a named schema.
     */
    public String allSequencesFromOneSchemaSQL = "SELECT NULL AS " +
        "SEQUENCE_SCHEMA, relname AS SEQUENCE_NAME FROM pg_class, " +
        "pg_namespace WHERE relkind='S' AND pg_class.relnamespace = " +
        "pg_namespace.oid AND nspname = ?";

    /**
     * SQL statement to load a sequence's schema,name pair from one schema.
     */
    public String namedSequenceFromOneSchemaSQL = "SELECT NULL AS " +
        "SEQUENCE_SCHEMA, relname AS SEQUENCE_NAME FROM pg_class, " +
        "pg_namespace WHERE relkind='S' AND pg_class.relnamespace = " +
        "pg_namespace.oid AND relname = ? AND nspname = ?";

    /**
     * Some Postgres drivers do not support the {@link Statement#setFetchSize}
     * method.
     */
    public boolean supportsSetFetchSize = true;

    public PostgresDictionary() {
        platform = "PostgreSQL";
        validationSQL = "SELECT NOW()";
        datePrecision = CENTI;
        supportsAlterTableWithDropColumn = false;
        supportsDeferredConstraints = true;
        supportsSelectStartIndex = true;
        supportsSelectEndIndex = true;

        // PostgreSQL requires double-escape for strings
        searchStringEscape = "\\\\";

        maxTableNameLength = 63;
        maxColumnNameLength = 63;
        maxIndexNameLength = 63;
        maxConstraintNameLength = 63;
        maxAutoAssignNameLength = 63;
        schemaCase = SCHEMA_CASE_LOWER;
        rangePosition = RANGE_POST_LOCK;
        requiresAliasForSubselect = true;
        allowsAliasInBulkClause = false;

        // single-quote escape will result in SELECT CURVAL('mysequence')
        lastGeneratedKeyQuery = "SELECT CURRVAL(''{1}_{0}_seq'')";
        supportsAutoAssign = true;
        autoAssignTypeName = "BIGSERIAL";
        nextSequenceQuery = "SELECT NEXTVAL(''{0}'')";

        useGetBytesForBlobs = true;
        useSetBytesForBlobs = true;
        useGetStringForClobs = true;
        useSetStringForClobs = true;
        bitTypeName = "BOOL";
        smallintTypeName = "SMALLINT";
        realTypeName = "FLOAT8";
        tinyintTypeName = "SMALLINT";
        binaryTypeName = "BYTEA";
        blobTypeName = "BYTEA";
        longVarbinaryTypeName = "BYTEA";
        varbinaryTypeName = "BYTEA";
        clobTypeName = "TEXT";
        longVarcharTypeName = "TEXT";
        doubleTypeName = "DOUBLE PRECISION";
        timestampTypeName = "ABSTIME";
        fixedSizeTypeNameSet.addAll(Arrays.asList(new String[]{
            "BOOL", "BYTEA", "NAME", "INT8", "INT2", "INT2VECTOR", "INT4",
            "REGPROC", "TEXT", "OID", "TID", "XID", "CID", "OIDVECTOR",
            "SET", "FLOAT4", "FLOAT8", "ABSTIME", "RELTIME", "TINTERVAL",
            "MONEY",
        }));

        supportsLockingWithDistinctClause = false;
        supportsQueryTimeout = false;
        supportsLockingWithOuterJoin = false;
        supportsNullTableForGetImportedKeys = true;

        reservedWordSet.addAll(Arrays.asList(new String[]{
            "ABORT", "ACL", "AGGREGATE", "APPEND", "ARCHIVE", "ARCH_STORE",
            "BACKWARD", "BINARY", "CHANGE", "CLUSTER", "COPY", "DATABASE",
            "DELIMITER", "DELIMITERS", "DO", "EXPLAIN", "EXTEND",
            "FORWARD", "HEAVY", "INDEX", "INHERITS", "ISNULL", "LIGHT",
            "LISTEN", "LOAD", "MERGE", "NOTHING", "NOTIFY", "NOTNULL",
            "OID", "OIDS", "PURGE", "RECIPE", "RENAME", "REPLACE",
            "RETRIEVE", "RETURNS", "RULE", "SETOF", "STDIN", "STDOUT",
            "STORE", "VACUUM", "VERBOSE", "VERSION",
        }));

        // reservedWordSet subset that CANNOT be used as valid column names
        // (i.e., without surrounding them with double-quotes)
        invalidColumnWordSet.addAll(Arrays.asList(new String[] {
            "ALL", "AND", "ANY", "AS", "ASC", "AUTHORIZATION", "BETWEEN", 
            "BINARY", "BOTH", "CASE", "CAST", "CHECK", "COLLATE", "COLUMN",
            "CONSTRAINT", "CREATE", "CROSS", "CURRENT_DATE", "CURRENT_TIME",
            "CURRENT_TIMESTAMP", "CURRENT_USER", "DEFAULT", "DEFERRABLE", 
            "DESC", "DISTINCT", "DO", "ELSE", "END", "END", "EXCEPT", "FALSE",
            "FOR", "FOREIGN", "FROM", "FULL", "GRANT", "GROUP", "HAVING", "IN",
            "INITIALLY", "INNER", "INTERSECT", "INTO", "IS", "ISNULL", "JOIN",
            "LEADING", "LEFT", "LIKE", "NATURAL", "NOT", "NOTNULL", "NULL", 
            "ON", "ONLY", "OR", "ORDER", "OUTER", "OVERLAPS", "PRIMARY",
            "REFERENCES", "RIGHT", "SELECT", "SESSION_USER", "SOME", "TABLE",
            "THEN", "TO", "TRAILING", "TRUE", "UNION", "UNIQUE", "USER", 
            "USING", "VERBOSE", "WHEN", "WHERE",
        }));
    }

    public Date getDate(ResultSet rs, int column)
        throws SQLException {
        try {
            return super.getDate(rs, column);
        } catch (StringIndexOutOfBoundsException sioobe) {
            // there is a bug in some versions of the postgres JDBC
            // driver such that a date with not enough numbers in it
            // will throw a parsing exception: this tries to work
            // around it. The bug only occurs when there is a trailing
            // millisecond missing from the end. E.g., when the date is
            // like:
            // 2066-10-19 22:08:32.83
            // rather than what the driver expects:
            // 2066-10-19 22:08:32.830
            String dateStr = rs.getString(column);
            SimpleDateFormat fmt = new SimpleDateFormat(
                "yyyy-MM-dd hh:mm:ss.SS");
            try {
                return fmt.parse(dateStr);
            } catch (ParseException pe) {
                throw new SQLException(pe.toString());
            }
        }
    }

    public byte getByte(ResultSet rs, int column)
        throws SQLException {
        // postgres does not perform automatic conversions, so attempting to
        // get a whole number out of a decimal will throw an exception.
        // fall back to performing manual conversion if the initial get fails
        try {
            return super.getByte(rs, column);
        } catch (SQLException sqle) {
            return super.getBigDecimal(rs, column).byteValue();
        }
    }

    public short getShort(ResultSet rs, int column)
        throws SQLException {
        // postgres does not perform automatic conversions, so attempting to
        // get a whole number out of a decimal will throw an exception.
        // fall back to performing manual conversion if the initial get fails
        try {
            return super.getShort(rs, column);
        } catch (SQLException sqle) {
            return super.getBigDecimal(rs, column).shortValue();
        }
    }

    public int getInt(ResultSet rs, int column)
        throws SQLException {
        // postgres does not perform automatic conversions, so attempting to
        // get a whole number out of a decimal will throw an exception.
        // fall back to performing manual conversion if the initial get fails
        try {
            return super.getInt(rs, column);
        } catch (SQLException sqle) {
            return super.getBigDecimal(rs, column).intValue();
        }
    }

    public long getLong(ResultSet rs, int column)
        throws SQLException {
        // postgres does not perform automatic conversions, so attempting to
        // get a whole number out of a decimal will throw an exception.
        // fall back to performing manual conversion if the initial get fails
        try {
            return super.getLong(rs, column);
        } catch (SQLException sqle) {
            return super.getBigDecimal(rs, column).longValue();
        }
    }

    public void setBoolean(PreparedStatement stmnt, int idx, boolean val,
        Column col)
        throws SQLException {
        // postgres actually requires that a boolean be set: it cannot
        // handle a numeric argument.
        stmnt.setBoolean(idx, val);
    }

    /**
     * Handle XML and bytea/oid columns in a PostgreSQL way.
     */
    public void setNull(PreparedStatement stmnt, int idx, int colType,
        Column col)
        throws SQLException {
        if (col != null && col.isXML()) {
            stmnt.setNull(idx, Types.OTHER);
            return;
        }

        // OPENJPA-308
        if (colType == Types.BLOB)
            colType = Types.BINARY;
        stmnt.setNull(idx, colType);
    }

    protected void appendSelectRange(SQLBuffer buf, long start, long end,
        boolean subselect) {
        if (end != Long.MAX_VALUE)
            buf.append(" LIMIT ").appendValue(end - start);
        if (start != 0)
            buf.append(" OFFSET ").appendValue(start);
    }

    public void indexOf(SQLBuffer buf, FilterValue str, FilterValue find,
        FilterValue start) {
        buf.append("(POSITION(");
        find.appendTo(buf);
        buf.append(" IN ");
        if (start != null)
            substring(buf, str, start, null);
        else
            str.appendTo(buf);
        buf.append(") - 1");
        if (start != null) {
            buf.append(" + ");
            start.appendTo(buf);
        }
        buf.append(")");
    }

    public String[] getCreateSequenceSQL(Sequence seq) {
        String[] sql = super.getCreateSequenceSQL(seq);
        if (seq.getAllocate() > 1)
            sql[0] += " CACHE " + seq.getAllocate();
        return sql;
    }

    protected boolean supportsDeferredUniqueConstraints() {
        // Postgres only supports deferred foreign key constraints.
        return false;
    }

    protected String getSequencesSQL(String schemaName, String sequenceName) {
        if (schemaName == null && sequenceName == null)
            return allSequencesSQL;
        else if (schemaName == null)
            return namedSequencesFromAllSchemasSQL;
        else if (sequenceName == null)
            return allSequencesFromOneSchemaSQL;
        else
            return namedSequenceFromOneSchemaSQL;
    }

    public boolean isSystemSequence(String name, String schema,
        boolean targetSchema) {
        if (super.isSystemSequence(name, schema, targetSchema))
            return true;

        // filter out generated sequences used for bigserial cols, which are
        // of the form <table>_<col>_seq
        int idx = name.indexOf('_');
        return idx != -1 && idx != name.length() - 4
            && name.toUpperCase().endsWith("_SEQ");
    }

    public boolean isSystemTable(String name, String schema,
        boolean targetSchema) {
        // names starting with "pg_" are reserved for Postgresql internal use
        return super.isSystemTable(name, schema, targetSchema)
            || (name != null && name.toLowerCase().startsWith("pg_"));
    }

    public boolean isSystemIndex(String name, Table table) {
        // names starting with "pg_" are reserved for Postgresql internal use
        return super.isSystemIndex(name, table)
            || (name != null && name.toLowerCase().startsWith("pg_"));
    }

    public Connection decorate(Connection conn)
        throws SQLException {
        return ConcreteClassGenerator.newInstance(postgresConnectionImpl, super.decorate(conn), this);
    }

    public InputStream getLOBStream(JDBCStore store, ResultSet rs,
        int column) throws SQLException {
        DelegatingConnection conn = (DelegatingConnection)store
            .getConnection();
        conn.setAutoCommit(false);
        LargeObjectManager lom = getLargeObjectManager(conn);
        if (rs.getInt(column) != -1) {
            LargeObject lo = lom.open(rs.getInt(column));
            return lo.getInputStream();
        } else {
            return null;
        }
    }

    public void insertBlobForStreamingLoad(Row row, Column col, 
        JDBCStore store, Object ob, Select sel) throws SQLException {
        if (row.getAction() == Row.ACTION_INSERT) {
            insertPostgresBlob(row, col, store, ob);
        } else if (row.getAction() == Row.ACTION_UPDATE) {
            updatePostgresBlob(row, col, store, ob, sel);
        }
    }

    private void insertPostgresBlob(Row row, Column col, JDBCStore store,
        Object ob) throws SQLException {
        if (ob != null) {
            col.setType(Types.INTEGER);
            DelegatingConnection conn = (DelegatingConnection)store
            .getConnection();
            try {
                conn.setAutoCommit(false);
                LargeObjectManager lom = getLargeObjectManager(conn);
                // The create method is valid in versions previous to 8.3
                // in 8.3 this method is deprecated, use createLO
                int oid = lom.create();
                LargeObject lo = lom.open(oid, LargeObjectManager.WRITE);
                OutputStream os = lo.getOutputStream();
                copy((InputStream)ob, os);
                lo.close();
                row.setInt(col, oid);
            } catch (IOException ioe) {
                throw new StoreException(ioe);
            } finally {
                conn.close();
            }
        } else {
            row.setInt(col, -1);
        }
    }
    
    private void updatePostgresBlob(Row row, Column col, JDBCStore store,
        Object ob, Select sel) throws SQLException {
        JDBCFetchConfiguration fetch = store.getFetchConfiguration();
        SQLBuffer sql = sel.toSelect(true, fetch);
        ResultSet res = null;
        DelegatingConnection conn = 
            (DelegatingConnection) store.getConnection();
        PreparedStatement stmnt = null;
        try {
            stmnt = sql.prepareStatement(conn, fetch,
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            setTimeouts(stmnt, fetch, true);
            res = stmnt.executeQuery();
            if (!res.next()) {
                throw new InternalException(_loc.get("stream-exception"));
            }
            int oid = res.getInt(1);
            if (oid != -1) {
                conn.setAutoCommit(false);
                LargeObjectManager lom = getLargeObjectManager(conn);
                if (ob != null) {
                    LargeObject lo = lom.open(oid, LargeObjectManager.WRITE);
                    OutputStream os = lo.getOutputStream();
                    copy((InputStream)ob, os);
                    lo.close();
                } else {
                    lom.delete(oid);
                    row.setInt(col, -1);
                }
            } else {
                if (ob != null) {
                    conn.setAutoCommit(false);
                    LargeObjectManager lom = getLargeObjectManager(conn);
                    oid = lom.create();
                    LargeObject lo = lom.open(oid, LargeObjectManager.WRITE);
                    OutputStream os = lo.getOutputStream();
                    copy((InputStream)ob, os);
                    lo.close();
                    row.setInt(col, oid);
                }
            }

        } catch (IOException ioe) {
            throw new StoreException(ioe);
        } finally {
            if (res != null)
                try { res.close (); } catch (SQLException e) {}
            if (stmnt != null)
                try { stmnt.close (); } catch (SQLException e) {}
            if (conn != null)
                try { conn.close (); } catch (SQLException e) {}
        }

    }
    
    public void updateBlob(Select sel, JDBCStore store, InputStream is)
        throws SQLException {
        //Do nothing
    }

    public void deleteStream(JDBCStore store, Select sel) throws SQLException {
        JDBCFetchConfiguration fetch = store.getFetchConfiguration();
        SQLBuffer sql = sel.toSelect(true, fetch);
        ResultSet res = null;
        DelegatingConnection conn = 
            (DelegatingConnection) store.getConnection();
        PreparedStatement stmnt = null;
        try {
            stmnt = sql.prepareStatement(conn, fetch,
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            setTimeouts(stmnt, fetch, true);
            res = stmnt.executeQuery();
            if (!res.next()) {
                throw new InternalException(_loc.get("stream-exception"));
            }
            int oid = res.getInt(1);
            if (oid != -1) {
                conn.setAutoCommit(false);
                LargeObjectManager lom = getLargeObjectManager(conn);
                lom.delete(oid);
            }
        } finally {
            if (res != null)
                try { res.close (); } catch (SQLException e) {}
            if (stmnt != null)
                try { stmnt.close (); } catch (SQLException e) {}
            if (conn != null)
                try { conn.close (); } catch (SQLException e) {}
        }
    }

    /**
     * Determine whether XML column is supported.
     */
    public void connectedConfiguration(Connection conn) throws SQLException {
        super.connectedConfiguration(conn);
        
        DatabaseMetaData metaData = conn.getMetaData();
        int maj = 0;
        int min = 0;
        if (isJDBC3) {
            maj = metaData.getDatabaseMajorVersion();
            min = metaData.getDatabaseMinorVersion();
        } else {
            try {
                // The product version looks like "8.3.5".
                String productVersion = metaData.getDatabaseProductVersion();
                String majMin[] = productVersion.split("\\.");
                maj = Integer.parseInt(majMin[0]);
                min = Integer.parseInt(majMin[1]);
            } catch (Exception e) {
                // We don't understand the version format.
                if (log.isWarnEnabled())
                    log.warn(e.toString(),e);
            }
        }
        
        if ((maj >= 9 || (maj == 8 && min >= 3))) {
            supportsXMLColumn = true;
        }
    }
 
    /**
     * If column is an XML column, PostgreSQL requires that its value is set
     * by using {@link PreparedStatement#setObject(int, Object, int)}
     * with {@link Types#OTHER} as the third argument.
     */
    public void setClobString(PreparedStatement stmnt, int idx, String val,
        Column col) throws SQLException {
        if (col != null && col.isXML())
            stmnt.setObject(idx, val, Types.OTHER);
        else
            super.setClobString(stmnt, idx, val, col);
    }
    
    /**
     * Override the getOjbect() method to handle the case where the latest
     * Postgres JDBC driver returns a org.postgresql.util.PGobject instead of a
     * java.sql.Timestamp
     * 
     * @param rs
     * @param column
     * @param map
     * 
     * @return
     * @exception SQLException
     */
    public Object getObject(ResultSet rs, int column, Map map)
        throws SQLException {
        Object obj = super.getObject(rs, column, map);

        if (obj == null) {
            return null;
        }
        if (obj.getClass().getName().equals("org.postgresql.util.PGobject")) {
            try {
                Method m = obj.getClass().getMethod("getType", (Class[]) null);
                Object type = m.invoke(obj, (Object[]) null);
                if (((String) type).equalsIgnoreCase(timestampTypeName)) {
                    return rs.getTimestamp(column);
                }
            } catch (Throwable t) {
                if (t instanceof InvocationTargetException)
                    t = ((InvocationTargetException) t).getTargetException();
                if (t instanceof SQLException)
                    throw (SQLException) t;
                throw new SQLException(t.getMessage());
            }
        }
        return obj;
    }

    /**
     * Append XML comparison.
     * 
     * @param buf
     *            the SQL buffer to write the comparison
     * @param op
     *            the comparison operation to perform
     * @param lhs
     *            the left hand side of the comparison
     * @param rhs
     *            the right hand side of the comparison
     * @param lhsxml
     *            indicates whether the left operand maps to XML
     * @param rhsxml
     *            indicates whether the right operand maps to XML
     */
    public void appendXmlComparison(SQLBuffer buf, String op, FilterValue lhs,
        FilterValue rhs, boolean lhsxml, boolean rhsxml) {
        super.appendXmlComparison(buf, op, lhs, rhs, lhsxml, rhsxml);
        if (lhsxml)
            appendXmlValue(buf, lhs);
        else
            lhs.appendTo(buf);
        buf.append(" ").append(op).append(" ");
        if (rhsxml)
            appendXmlValue(buf, rhs);
        else
            rhs.appendTo(buf);
    }
    
    /**
     * Append XML column value so that it can be used in comparisons.
     * 
     * @param buf
     *            the SQL buffer to write the value
     * @param val
     *            the value to be written
     */
    private void appendXmlValue(SQLBuffer buf, FilterValue val) {
        Class rc = Filters.wrap(val.getType());
        int type = getJDBCType(JavaTypes.getTypeCode(rc), false);
        boolean isXmlAttribute = (val.getXmlMapping() == null) ? false
                : val.getXmlMapping().isXmlAttribute();
        SQLBuffer newBufer = new SQLBuffer(this);
        newBufer.append("(xpath('/*/");
        val.appendTo(newBufer);
        if (!isXmlAttribute)
            newBufer.append("/text()");
        newBufer.append("',").
            append(val.getColumnAlias(val.getFieldMapping().getColumns()[0])).
            append("))[1]");
        appendCast(buf, newBufer, type);
    }


    /**
     * Return a SQL string to act as a placeholder for the given column.
     */
    public String getPlaceholderValueString(Column col) {
        if (col.getType() == Types.BIT) {
            return "false";
        } else {
            return super.getPlaceholderValueString(col);
        }
    }

    /**
     * Get the native PostgreSQL Large Object Manager used for LOB handling.
     */
    protected LargeObjectManager getLargeObjectManager(DelegatingConnection conn) throws SQLException {
        return getPGConnection(conn).getLargeObjectAPI();
    }

    /**
     * Get the native PostgreSQL connection from the given connection.
     * Various attempts of unwrapping are being performed.
     */
    protected PGConnection getPGConnection(DelegatingConnection conn) {
        Connection innerConn = conn.getInnermostDelegate();
        if (innerConn instanceof PGConnection) {
            return (PGConnection) innerConn;
        }
        if (innerConn.getClass().getName().startsWith("org.apache.commons.dbcp")) {
            return (PGConnection) getDbcpDelegate(innerConn);
        }
        return (PGConnection) unwrapConnection(conn, PGConnection.class);
    }

    /**
     * Get the delegated connection from the given DBCP connection.
     * 
     * @param conn must be a DBCP connection
     * @return connection the DBCP connection delegates to
     */
    protected Connection getDbcpDelegate(Connection conn) {
        Connection delegate = null;
        try {
            if (dbcpGetDelegate == null) {
                Class<?> dbcpConnectionClass =
                    Class.forName("org.apache.commons.dbcp.DelegatingConnection", true, AccessController
                        .doPrivileged(J2DoPrivHelper.getContextClassLoaderAction()));
                
                dbcpGetDelegate = dbcpConnectionClass.getMethod("getInnermostDelegate");
            }
            delegate = (Connection) dbcpGetDelegate.invoke(conn);
        } catch (Exception e) {
            throw new InternalException(_loc.get("dbcp-unwrap-failed"), e);
        }
        if (delegate == null) {
            throw new InternalException(_loc.get("dbcp-unwrap-failed"));
        }
        return delegate;
    }

    /**
     * Get (unwrap) the delegated connection from the given connection.
     * Use reflection to attempt to unwrap a connection.
     * Note: This is a JDBC 4 operation, so it requires a Java 6 environment 
     * with a JDBC 4 driver or data source to have any chance of success.
     * 
     * @param conn a delegating connection
     * @param connectionClass the expected type of delegated connection
     * @return connection the given connection delegates to
     */
    private Connection unwrapConnection(Connection conn, Class<?> connectionClass) {
        try {
            if (connectionUnwrap == null) {
                connectionUnwrap = Connection.class.getMethod("unwrap", Class.class);
            }
            return (Connection) connectionUnwrap.invoke(conn, connectionClass);
        } catch (Exception e) {
            throw new InternalException(_loc.get("connection-unwrap-failed"), e);
        }
    }

    /**
     * Connection wrapper to work around the postgres empty result set bug.
     */
    protected abstract static class PostgresConnection
        extends DelegatingConnection {

        private final PostgresDictionary _dict;

        public PostgresConnection(Connection conn, PostgresDictionary dict) {
            super(conn);
            _dict = dict;
        }

        protected PreparedStatement prepareStatement(String sql, boolean wrap)
            throws SQLException {
           return ConcreteClassGenerator.newInstance(postgresPreparedStatementImpl, 
                   super.prepareStatement(sql, false), PostgresConnection.this, _dict);
        }

        protected PreparedStatement prepareStatement(String sql, int rsType,
            int rsConcur, boolean wrap)
            throws SQLException {
            return ConcreteClassGenerator.
                newInstance(postgresPreparedStatementImpl,
                    super.prepareStatement(sql, rsType, rsConcur, false),
                    PostgresConnection.this,
                    _dict);
        }
    }

    /**
     * Statement wrapper to work around the postgres empty result set bug.
     */
    protected abstract static class PostgresPreparedStatement
        extends DelegatingPreparedStatement {

        private final PostgresDictionary _dict;

        public PostgresPreparedStatement(PreparedStatement ps,
            Connection conn, PostgresDictionary dict) {
            super(ps, conn);
            _dict = dict;
        }

        protected ResultSet executeQuery(boolean wrap)
            throws SQLException {
            try {
                return super.executeQuery(wrap);
            } catch (SQLException se) {
                // we need to make our best guess whether this is the empty
                // ResultSet bug, since this exception could occur
                // for other reasons (like an invalid query string). Note
                // that Postgres error messages are localized, so we
                // cannot just parse the exception String.
                ResultSet rs = getResultSet(wrap);

                // ResultSet should be empty: if not, then maybe an
                // actual error occurred
                if (rs == null)
                    throw se;

                return rs;
            }
        }

        public void setFetchSize(int i)
            throws SQLException {
            // some postgres drivers do not support the setFetchSize method
            try {
                if (_dict.supportsSetFetchSize)
                    super.setFetchSize(i);
            } catch (SQLException e) {
                _dict.supportsSetFetchSize = false;
                if (_dict.log.isWarnEnabled())
                    _dict.log.warn(_loc.get("psql-no-set-fetch-size"), e);
            }
        }
    }
}