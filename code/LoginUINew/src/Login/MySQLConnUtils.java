package Login;
//package org.o7planning.tutorial.jdbc;

import java.sql.*;

public class MySQLConnUtils {
  public static void main(String[] args) {
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;
    try {
      // create our mysql database connection
      // String myDriver = "com.mysql.cj.jdbc.Driver";
      // String myUrl = "jdbc:mysql://localhost:3306/bibeobu";
      // Class.forName(myDriver);
      // Connection conn = DriverManager.getConnection(myUrl, "root", "1234");
      conn = MySQLConnUtils.getMySQLConnection();
      // our SQL SELECT query.
      // if you only need a few columns, specify them by name instead of using "*"
      String query = "SELECT * FROM Course";

      // create the java statement
      st = conn.createStatement();

      // execute the query, and get a java resultset
      rs = st.executeQuery(query);

      // iterate through the java resultset
      while (rs.next()) {
        String student_id = rs.getString("course_id");
        Integer l_name = rs.getInt("number");
        // print the results
        System.out.format("%s %d\n", student_id, l_name);
      }
    } catch (Exception e) {
      System.err.println("Got an exception! ");
      System.err.println(e.getMessage());
      e.printStackTrace();
    } finally {
        try {
          if (st != null)
            st.close();
        } catch (SQLException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        try {
          if (conn != null)
          conn.close();
        } catch (SQLException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        try {
          if (rs != null)
          rs.close();
        } catch (SQLException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
    }
  }

  // Kết nối vào MySQL.
  public static Connection getMySQLConnection() {
    Connection conn = null;
    try {
      String myDriver = "com.mysql.cj.jdbc.Driver";
      String myUrl = "jdbc:mysql://localhost:3306/bibeobu";
      Class.forName(myDriver);
      conn = DriverManager.getConnection(myUrl, "root", "");
    } catch (SQLException | ClassNotFoundException exc) {
      System.err.println("Got an exception!, Cannot connect to server! ");
      System.err.println(exc.getMessage());
    } 
    return conn;
  }
}
