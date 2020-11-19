package code;

import java.util.Scanner;
import java.sql.*;

public class student extends account {
    
    student(String name, String pass){
        super.username = name;
        super.password = pass;
    }

    @Override
    public void read_account_file() {
        // TODO Auto-generated method stub

    }

    @Override
    public void role_menu() {
        // TODO Auto-generated method stub
        System.out.println("day la sinh vien");
        while(true){
            System.out.println("1. Xem thông tin cá nhân");
            System.out.println("2. Xem khóa học đã đăng ký");
            System.out.println("3. Xem thời khóa biểu");
            System.out.println("4. Xem điểm");
            System.out.println("5. Đăng ký môn học");
            System.out.println("0. Thoát");
            int choose;
            Scanner scan = new Scanner(System.in);
            choose = scan.nextInt();
            scan.nextLine();
            switch (choose) {
                case 1:
                    write_info();
                    break;
                case 2:
                    view_course();
                    break;
                default:
                    return;
            }
        }
        

    }

    @Override
    public void write_info() {
        // TODO Auto-generated method stub
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        ResultSet rs = null;
        String query = "SELECT * FROM Student WHERE student_id = ? ";
        try{
            stm = conn.prepareStatement(query);
            stm.setString(1, super.username);
            rs = stm.executeQuery();
            if(rs.next()){
                super.l_name = rs.getString("lastname");
                super.f_name = rs.getString("firstname");
                super.dob = rs.getString("dob");
            }
            System.out.println("Mã số sinh viên: " + super.username);
            System.out.println("Tên đầy đủ: " + super.l_name + " " + super.f_name);
            System.out.println("Ngày sinh: " +  super.dob);
        } catch(SQLException exp) {
            System.out.println("Write info " + exp);
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
    }
    public void view_course(){
        // TODO Auto-generated method stub
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        ResultSet rs = null;
        String query = "SELECT * FROM Student_Course WHERE student_id = ? ";
        try{
            stm = conn.prepareStatement(query);
            stm.setString(1, super.username);
            rs = stm.executeQuery();
            if(rs.next()){
                super.l_name = rs.getString("lastname");
                super.f_name = rs.getString("firstname");
                super.dob = rs.getString("dob");
            }
            System.out.println("Mã số sinh viên: " + super.username);
            System.out.println("Tên đầy đủ: " + super.l_name + " " + super.f_name);
            System.out.println("Ngày sinh: " +  super.dob);
        } catch(SQLException exp) {
            System.out.println("Write info " + exp);
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
    }
}
