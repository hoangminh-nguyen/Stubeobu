package code;

import java.io.*;

import java.sql.*;
import java.util.Scanner;

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

    public abstract void read_account_file();
    public abstract void role_menu();

    public account main_menu() {
        Scanner s = new Scanner(System.in);
        
        System.out.println("Chào mừng bạn tới phần mềm Quản lí học sinh Stubeobu");
        System.out.println("Mời bạn chọn 1 chức năng sau:");
        System.out.println("1.Đăng nhập");
        System.out.print("2.Thoát\nChọn: ");
        int i = s.nextInt();

        s.nextLine();
        if (i == 1){
            String name;
            String pass;
            while(true)
            {
                System.out.print("Tài khoản: ");
                name = s.nextLine();
                System.out.print("Mật khẩu: ");
                pass = s.nextLine();
                if (sign_in(name, pass)) {
                    this.username = name;
                    this.password = pass;
                    break;
                }
                
                else System.out.println("\nSai tài khoản hoặc mật khẩu.");
            }
            switch (name.charAt(0)) {
                case 'S':
                    account st = new student(this.username, this.password);
                    return st;
                case 'T':
                    account pr = new professor(this.username, this.password);
                    return pr;
                case 'A':
                    account ad = new admin();
                    return ad;
                default:
                    break;
            }
        }
        return null;
    }
    

    public boolean sign_in(String name, String pass) {
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        ResultSet rs = null;
        String query = "SELECT * FROM Account WHERE username = ? and pass = ?";
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
