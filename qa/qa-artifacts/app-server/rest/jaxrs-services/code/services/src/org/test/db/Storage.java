package org.test.db;

import org.test.beans.User;

import java.sql.*;


public class Storage {

    Connection connection = null;
    Statement statement = null;
    ResultSet rs = null;


    public void addUser(User user) {


        try {
            connection = getConnection();
            statement = connection.createStatement();

            String sqlStatement = "INSERT INTO USER_T VALUES ('" + user.getName()
                    + "','" + user.getAddress() + "', " + user.getAge() + ")";
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

    public void addUsername(String username) {

           String address = "Colombo";
           int age = 32;
           try {
               connection = getConnection();
               statement = connection.createStatement();

               String sqlStatement = "INSERT INTO USER_T VALUES ('" + username
                       + "'," + age + ", '" + address + "')";
               System.out.println("sql statement is"+sqlStatement);
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

    public void updateUsername(String username) {

              String address = "Colombo";
              int age = 32;
              try {
                  connection = getConnection();
                  statement = connection.createStatement();

                  String sqlStatement = "UPDATE USER_T set age = 44 where name = '" + username + "'";
                  System.out.println("sql statement is"+sqlStatement);
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

public void deleteUser(String username) {

              
              try {
                  connection = getConnection();
                  statement = connection.createStatement();

                  String sqlStatement = "DELETE from USER_T where name = '" + username + "'";
                  System.out.println("sql statement is"+sqlStatement);
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




    public User getUserDetails(String name) throws SQLException {

        String userName = null;
        String userAddress = null;
        int userAge = 0;


        connection = getConnection();
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {

        }



        String sqlStatement = "SELECT * FROM USER_T WHERE name = '" + name + "'";

            rs = statement.executeQuery(sqlStatement);

            User user = null;
            while (rs.next()) {
               user = new User();

                userName = rs.getString("name");
                userAddress = rs.getString("address");
                userAge = rs.getInt("age");

                user.setName(userName);
                user.setAge(userAge);
                user.setAddress(userAddress);

            }
         statement.close();
         connection.close();
            return user;
      



    }




    private Connection getConnection() {
        String driverName = "com.mysql.jdbc.Driver";
        String conectionURI = "jdbc:mysql://localhost:3306/JAXRS_DB";
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
