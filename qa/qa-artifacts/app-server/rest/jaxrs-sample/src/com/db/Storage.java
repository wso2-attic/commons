package com.db;

import com.beans.Customer;

import java.sql.*;


public class Storage {

    Connection connection = null;
    Statement statement = null;
    ResultSet rs = null;


    public void addCustomer(Customer customer) {


        try {
            connection = getConnection();
            statement = connection.createStatement();

            String sqlStatement = "INSERT INTO CUSTOMER_T VALUES (" + customer.getCustomerID()
                    + ",'" + customer.getCustomerName() + "', " + customer.getCustomerAge() + ",'" + customer.getCustomerAddress() + "')";
            statement.execute(sqlStatement);

        } catch (SQLException e) {

        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }


    }


    public void updateCustomer(String customerName, String customerAddress) {

        String address = "Colombo";
        int age = 32;
        try {
            connection = getConnection();
            statement = connection.createStatement();

            String sqlStatement = "UPDATE CUSTOMER_T set customerAddress = '" + customerAddress + "' where customerName = '" + customerName + "'";
            System.out.println("sql statement is" + sqlStatement);
            statement.execute(sqlStatement);

        } catch (SQLException e) {

        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }


    }

    public void deleteCustomer(int customerID) {


        try {
            connection = getConnection();
            statement = connection.createStatement();

            String sqlStatement = "DELETE from CUSTOMER_T where customerID = " + customerID + "";
            System.out.println("sql statement is" + sqlStatement);
            statement.execute(sqlStatement);

        } catch (SQLException e) {

        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }


    }


    public Customer getCustomerDetails(int customerID) throws SQLException {

        String custName = null;
        String custAddress = null;
        int custAge = 0;
        int custID = 0;


        connection = getConnection();
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {

        }


        String sqlStatement = "SELECT * FROM CUSTOMER_T WHERE customerID = " + customerID + "";

        rs = statement.executeQuery(sqlStatement);

        Customer customer = null;
        while (rs.next()) {
            customer = new Customer();

            custName = rs.getString("customerName");
            custAddress = rs.getString("customerAddress");
            custAge = rs.getInt("customerAge");

            customer.setCustomerName(custName);
            customer.setCustomerAge(custAge);
            customer.setCustomerAddress(custAddress);

        }
        statement.close();
        connection.close();
        return customer;


    }


    private Connection getConnection() {
        String driverName = "com.mysql.jdbc.Driver";
        String conectionURI = "jdbc:mysql://localhost:3306/CUSTOMER_DB";
        String userName = "root";
        String password = "root";


        try {
            Class.forName(driverName);
            //Connection connection = null;
            try {
                connection = DriverManager.getConnection(conectionURI, userName, password);
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return connection;
    }


//    public static void main(String[] args) throws SQLException, ClassNotFoundException {
//
//
//        connection = getConnection();
//        Statement stmt = connection.createStatement();
//        ResultSet rs = stmt.executeQuery("SELECT * FROM USER_T");
//        while (rs.next()) {
//
//       String s = rs.getString("name");
//            System.out.println("user name is "+s);
//    }
//
//    }
}


    

