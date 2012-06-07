package com.example.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by IntelliJ IDEA.
 * User: charitha
 * Date: Jun 7, 2012
 * Time: 12:57:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class CustomerServlet extends HttpServlet {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/customerdb";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "root";


    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {

        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        // Set response content type
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String title = "Customer Details";
        String docType =
                "<!doctype html public \"-//w3c//dtd html 4.0 " +
                "transitional//en\">\n";
        out.println(docType +
                    "<html>\n" +
                    "<head><title>" + title + "</title></head>\n" +
                    "<body bgcolor=\"#f0f0f0\">\n" +
                    "<h1 align=\"center\">" + title + "</h1>\n");
        try {
            // Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            // Open a connection
            connection = DriverManager.getConnection(DB_URL, USER, PASS);

            // Execute SQL query
            String customerID = request.getParameter("id");
            stmt = connection.createStatement();
            String sql;
            sql = "SELECT * FROM customer where id =" + customerID + " ";
            rs = stmt.executeQuery(sql);

            // Extract data from result set
            while (rs.next()) {
                //Retrieve by column name
                int id = rs.getInt("id");
                String first = rs.getString("firstname");
                String last = rs.getString("lastname");
                String address = rs.getString("address");

                //Display values
                out.println("ID: " + id + "<br>");
                out.println(", First Name: " + first + "<br>");
                out.println(", Last Name: " + last + "<br>");
                out.println(", Address: " + address + "<br>");
            }
            out.println("</body></html>");

            // Clean-up environment
            rs.close();
            stmt.close();
            connection.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        } //end try
    }


    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
            throws ServletException, IOException {

        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        // Set response content type
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String title = "Customer Details";
        String docType =
                "<!doctype html public \"-//w3c//dtd html 4.0 " +
                "transitional//en\">\n";
        out.println(docType +
                    "<html>\n" +
                    "<head><title>" + title + "</title></head>\n" +
                    "<body bgcolor=\"#f0f0f0\">\n" +
                    "<h1 align=\"center\">" + title + "</h1>\n");
        try {
            // Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            // Open a connection
            connection = DriverManager.getConnection(DB_URL, USER, PASS);

            // Execute SQL query
            String customerID = request.getParameter("id");
            stmt = connection.createStatement();
            String sql;
            sql = "SELECT * FROM customer where id =" + customerID + " ";
            rs = stmt.executeQuery(sql);

            // Extract data from result set
            while (rs.next()) {
                //Retrieve by column name
                int id = rs.getInt("id");
                String first = rs.getString("firstname");
                String last = rs.getString("lastname");
                String address = rs.getString("address");

                //Display values
                out.println("ID: " + id + "<br>");
                out.println(", First Name: " + first + "<br>");
                out.println(", Last Name: " + last + "<br>");
                out.println(", Address: " + address + "<br>");
            }
            out.println("</body></html>");

            // Clean-up environment
            rs.close();
            stmt.close();
            connection.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        } //end try
    }


}
