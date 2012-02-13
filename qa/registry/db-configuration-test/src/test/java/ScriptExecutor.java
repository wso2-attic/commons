/*
 * Copyright 2004,2005 The Apache Software Foundation.
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
 */


import javax.sql.DataSource;
import java.sql.*;
import java.util.StringTokenizer;
import java.io.*;

public class ScriptExecutor {


    private DataSource dataSource;
    private String delimiter = ";";
    Connection conn = null;
    Statement statement;
    String path;

    public ScriptExecutor(String path) {
        this.path = path;
    }


    /**
     * Creates registry database
     * @throws Exception
     */
    public void createRegistryDatabase() throws Exception {
        try {

//            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            String connString = "jdbc:oracle:thin:@krishantha:1523:registry";
            conn = DriverManager.getConnection(connString, "test", "test");
            statement = conn.createStatement();

//            conn = dataSource.getConnection();
//            conn.setAutoCommit(false);
//            statement = conn.createStatement();
            executeSQLScript();
            conn.commit();

        } catch (SQLException e) {
            String msg = "Failed to create database tables for registry resource store. " + e.getMessage();

            throw new Exception(msg,e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                    System.out.println("connection null");
                }
            } catch (SQLException e) {

            }
        }
    }


    /**
     * executes given sql
     * @param sql
     * @throws Exception
     */
    private void executeSQL(String sql) throws Exception {
        // Check and ignore empty statements
        if ("".equals(sql.trim())) {
            return;
        }

        ResultSet resultSet = null;
        try {


            boolean ret;
            int updateCount = 0, updateCountTotal = 0;

            ret = statement.execute(sql);
            updateCount = statement.getUpdateCount();
            resultSet = statement.getResultSet();
            do {
                if (!ret) {
                    if (updateCount != -1) {
                        updateCountTotal += updateCount;
                    }
                }
                ret = statement.getMoreResults();
                if (ret) {
                    updateCount = statement.getUpdateCount();
                    resultSet = statement.getResultSet();
                }
            } while (ret);

            SQLWarning warning = conn.getWarnings();
            while (warning != null) {

                warning = warning.getNextWarning();
            }
            conn.clearWarnings();
        } catch (SQLException e) {
            if (e.getSQLState().equals("X0Y32")) {
                // eliminating the table already exception for the derby database

            }
            else {
                throw new Exception("Error occurred while executing : "+sql,e);
            }
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {

                }
            }
        }
    }

    /**
     * computes relatational database type using database name
     * @return String
     * @throws Exception*
     */
    private String getDatabaseType() throws Exception{
        String type = null;
        try {
            if(conn != null && (!conn.isClosed())){
                DatabaseMetaData metaData = conn.getMetaData();
                //TODO : Add more types (postgres,db2,etc...)
                if (metaData.getDatabaseProductName().matches("(?i).*hsql.*")) {
                    type = "hsql";
                } else if (metaData.getDatabaseProductName().matches("(?i).*derby.*")) {
                    type = "derby";
                } else if (metaData.getDatabaseProductName().matches("(?i).*mysql.*")) {
                    type = "mysql";
                } else if (metaData.getDatabaseProductName().matches("(?i).*oracle.*")) {
                    type = "oracle";
                } else if (metaData.getDatabaseProductName().matches("(?i).*microsoft.*")) {
                    type = "mssql";
                } else if (metaData.getDatabaseProductName().matches("(?i).*h2.*")) {
                    type = "h2";
                } else {
                    String msg = "Unsupported database: " + metaData.getDatabaseProductName() +
                            ". Database will not be created automatically by the WSO2 Registry. " +
                            "Please create the database using appropriate database scripts for " +
                            "the database.";
                    throw new Exception(msg);
                }
            }
        } catch (SQLException e) {
            String msg = "Failed to create registry database." + e.getMessage();

            throw new Exception(msg, e);
        }
        return type;
    }

    /**
     * executes content in SQL script
     * @return StringBuffer
     * @throws Exception
     */
    private void executeSQLScript()throws Exception {

        String dbscriptName = path;

        StringBuffer sql = new StringBuffer();
        BufferedReader reader;
        boolean keepformat = false;

        try {
            InputStream is = new FileInputStream(dbscriptName);
            reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!keepformat) {
                    if (line.startsWith("//")) {
                        continue;
                    }

                    if (line.startsWith("--")) {
                        continue;
                    }
                    StringTokenizer st = new StringTokenizer(line);
                    if (st.hasMoreTokens()) {
                        String token = st.nextToken();
                        if ("REM".equalsIgnoreCase(token)) {
                            continue;
                        }
                    }
                }
                sql.append(keepformat ? "\n" : " ").append(line);

                // SQL defines "--" as a comment to EOL
                // and in Oracle it may contain a hint
                // so we cannot just remove it, instead we must end it
                if (!keepformat && line.indexOf("--") >= 0) {
                    sql.append("\n");
                }
                if ((checkStringBufferEndsWith(sql, delimiter))) {
                    executeSQL(sql.substring(0, sql.length() - delimiter.length()));
                    sql.replace(0, sql.length(), "");
                }
            }
            // Catch any statements not followed by ;
            if (sql.length() > 0) {
                System.out.println(sql.toString());
                executeSQL(sql.toString());
            }
        } catch (IOException e) {

            throw new Exception("Error occurred while executing SQL script for creating registry database",e);
        }
    }

    /**
     * Checks that a string buffer ends up with a given string. It may sound
     * trivial with the existing
     * JDK API but the various implementation among JDKs can make those
     * methods extremely resource intensive
     * and perform poorly due to massive memory allocation and copying. See
     * @param buffer the buffer to perform the check on
     * @param suffix the suffix
     * @return  <code>true</code> if the character sequence represented by the
     *          argument is a suffix of the character sequence represented by
     *          the StringBuffer object; <code>false</code> otherwise. Note that the
     *          result will be <code>true</code> if the argument is the
     *          empty string.
     */

    public static boolean checkStringBufferEndsWith(StringBuffer buffer, String suffix) {
        if (suffix.length() > buffer.length()) {
            return false;
        }
        // this loop is done on purpose to avoid memory allocation performance
        // problems on various JDKs
        // StringBuffer.lastIndexOf() was introduced in jdk 1.4 and
        // implementation is ok though does allocation/copying
        // StringBuffer.toString().endsWith() does massive memory
        // allocation/copying on JDK 1.5
        // See http://issues.apache.org/bugzilla/show_bug.cgi?id=37169
        int endIndex = suffix.length() - 1;
        int bufferIndex = buffer.length() - 1;
        while (endIndex >= 0) {
            if (buffer.charAt(bufferIndex) != suffix.charAt(endIndex)) {
                return false;
            }
            bufferIndex--;
            endIndex--;
        }
        return true;
    }

    public static void main(String[] args) throws Exception {        

        ScriptExecutor sc = new ScriptExecutor("/home/krishantha/Desktop/kalpani/scripts/um-oracle.sql");
        sc.createRegistryDatabase();

    }
}
