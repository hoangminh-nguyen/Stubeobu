package Login;
import java.io.*;
import java.sql.*;
import java.util.Scanner;
import java.awt.*;  
import java.awt.event.*;  
import java.util.Calendar;
import java.util.TimeZone;
import javax.swing.*;

public abstract class account {
    protected String username; // id 
    protected String password;
    protected String f_name;
    protected String l_name;
    protected String gender;
    protected String dob;

    account(){
        username="";
        password="";
        f_name="";
        l_name="";
        gender="";
    };
    public void change_password() {
        Scanner scan = new Scanner(System.in);
        int i = 0;
        String temp = "";
        while (i == 0) {
            System.out.print("Nhập mật khẩu cũ: ");
            temp = scan.nextLine();
            if (temp.equals(password)) {
                System.out.print("Nhập mật khẩu mới: ");
                password = scan.nextLine();
                i = 1;
            } else {
                System.out.println("Mật khẩu cũ bạn sai rồi.");
                continue;
            }
        }
    }

    public abstract void read_account_file(JTextField id, JTextField name, JTextField dob, JTextField gender);
    
    
    
    
    public static int getSemid(int year, int semester){
        int sem_id = 0;
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        ResultSet rs = null;
        String query = "select sem_id from semester where years = ? and semester = ?;";
        try{
            stm = conn.prepareStatement(query);
            stm.setInt(1, year);
            stm.setInt(2, semester);
            rs = stm.executeQuery();
            if(rs.next()){
                //System.out.println("Have id");
                sem_id = rs.getInt("sem_id");
                
            }
            
        } catch(SQLException exp) {
            System.out.println("getSemid" + exp);
            exp.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
                if (stm != null) stm.close();
            } catch (SQLException e) {
              e.printStackTrace();
            }
        }
        
        return sem_id;
    }

    public static int getSemidNow(){
        java.util.Date date = new java.util.Date(); // your date
        // Choose time zone in which you want to interpret your Date
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int sem;
        if (month<5) {sem = 2; year--;}
        else if (month<9) {sem = 3; year--;}
        else sem=1;
        return getSemid(year, sem);
    }
    public static boolean sign_in(String name, String pass) {
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        ResultSet rs = null;
        String query = "SELECT * FROM Account WHERE BINARY username = BINARY ? and BINARY pass = BINARY ?";
        try{
            stm = conn.prepareStatement(query);
            stm.setString(1, name);
            stm.setString(2, pass);
            rs = stm.executeQuery();
            if(rs.next()){
                return true;
            }
        } catch(SQLException exp) {
            System.out.println("Sign in" + exp);
            exp.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
                if (rs != null) rs.close();
                if (stm != null) stm.close();
            } catch (SQLException e) {
              e.printStackTrace();
            }
        }
        return false;
    }
    
    


}
