package code;

//package org.o7planning.tutorial.jdbc;
 
import java.sql.*;
 
public class MySQLConnUtils {
 public static void main(String[] args) {
    try
    {
      // create our mysql database connection
      String myDriver = "com.mysql.cj.jdbc.Driver";
      String myUrl = "jdbc:mysql://localhost:3306/bibeobu";
      Class.forName(myDriver);
      Connection conn = DriverManager.getConnection(myUrl, "root", "1234");
      
      // our SQL SELECT query. 
      // if you only need a few columns, specify them by name instead of using "*"
      String query = "SELECT * FROM Student";

      // create the java statement
      Statement st = conn.createStatement();
      
      // execute the query, and get a java resultset
      ResultSet rs = st.executeQuery(query);
      
      // iterate through the java resultset
      while (rs.next())
      {
        String student_id = rs.getString("student_id");
        String l_name = rs.getString("lastname");
        String f_name = rs.getString("firstname");
        String dob = rs.getString("dob");
        // print the results
        System.out.format("%s %s %s %s\n", student_id, l_name, f_name, dob);
      }
      st.close();
      conn.close();
      rs.close();
    }
    catch (Exception e)
    {
      System.err.println("Got an exception! ");
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
 }
 // Kết nối vào MySQL.
 public static Connection getMySQLConnection() throws SQLException,
         ClassNotFoundException {
     String hostName = "localhost";
 
     String dbName = "bibeobu";
     String userName = "root";
     String password = "1234";
 
     return getMySQLConnection(hostName, dbName, userName, password);
 }
 
 public static Connection getMySQLConnection(String hostName, String dbName,
    String userName, String password) throws SQLException, ClassNotFoundException 
    {
     Class.forName("com.mysql.cj.jdbc.Driver");
 
     // Cấu trúc URL Connection dành cho Oracle
     // Ví dụ: jdbc:mysql://localhost:3306/simplehr
     String connectionURL = "jdbc:mysql://" + hostName + ":3306/" + dbName;
 
     Connection conn = DriverManager.getConnection(connectionURL, userName,
             password);
     return conn;
  }
}
