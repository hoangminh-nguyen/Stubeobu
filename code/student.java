package code;

import java.util.ArrayList;
import java.util.Scanner;
import java.sql.*;
import java.awt.*;  
import java.awt.event.*;  
import javax.swing.*;

public class student extends account {
    
    ArrayList<course> listCourse = null; //cac khoa hoc da dang ky
    ArrayList<course> listAvailableCourse = null; //cac khoa hoc chua dang ky
    student(String name, String pass){
        super.username = name;
        super.password = pass;
    }

    student(String name){
        super.username = name;
    }


    public Double get_midterm(String course_id){
        for(course x : listCourse){
            if (x.id == course_id){
                return x.midterm;
            }
        }
        return 0.0;
    }

    public Double get_final(String course_id){
        for(course x : listCourse){
            if (x.id == course_id){
                return x.finall;
            }
        }
        return 0.0;
    }

    @Override
    public void read_account_file() {
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
                super.gender = rs.getString("gender");
                super.dob = rs.getString("dob");
            }
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

    @Override
    public void role_menu() {
        // TODO Auto-generated method stub
        System.out.println("day la sinh vien");
        while(true){
            System.out.println("1. Xem thông tin cá nhân");
            System.out.println("2. Xem môn học đã đăng ký");
            System.out.println("3. Xem thời khóa biểu");
            System.out.println("4. Xem điểm");
            System.out.println("5. Đăng ký môn học");
            System.out.println("6. Hủy đăng ký môn học");
            System.out.println("0. Thoát");
            int choose;
            Scanner scan = new Scanner(System.in);
            choose = scan.nextInt();
            scan.nextLine();

            if (listCourse == null){
                load_course();
            }
            
            if (listAvailableCourse == null){
                load_available_course();
            }
            switch (choose) {
                case 1:
                    read_account_file();
                    break;
                case 2:
                    view_course();
                    break;
                case 3:
                    load_timetable();
                    view_timetable();
                    break;
                case 4:
                    load_mark();
                    view_mark();
                    break;
                // case 5:
                //     enroll_course();
                //     break;
                // case 6:
                //     cancel_course();
                //     break;
                default:
                    return;
            }
        }
    }

    
    
    public void load_course(){
        // TODO Auto-generated method stub
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        ResultSet rs = null;
        String query = "call load_course(?);";
        try{
            stm = conn.prepareStatement(query);
            stm.setString(1, super.username);
            rs = stm.executeQuery();
            listCourse = new ArrayList<>();
            while(rs.next()){
                course temp = new course();
                temp.id = rs.getString("course_id");
                temp.name = rs.getString("course_name");
                temp.number = rs.getInt("number");
                temp.teacher_name = rs.getString("fullname");
                temp.teacher_id = rs.getString("teacher_id");
                temp.sem_id = rs.getInt("sem_id");
                temp.semester = rs.getInt("Semester");
                temp.year = rs.getInt("years");
                temp.room = rs.getString("room");
                listCourse.add(temp);
            }
            
        } catch(SQLException exp) {
            System.out.println("load courses " + exp);
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
    
    public void load_timetable(){
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        ResultSet rs = null;
        String query = "SELECT ti.week_day, ti.period_no FROM Course co join Timetable ti on (co.course_id = ti.course_id and co.number = ti.number and co.sem_id = ti.sem_id) WHERE co.course_id = ? and co.number = ? and co.sem_id = ?";
        try{
            for (int i = 0; i < listCourse.size(); i++) {
                listCourse.get(i).timetable = new ArrayList<>();
                stm = conn.prepareStatement(query);
                stm.setString(1, listCourse.get(i).id);
                stm.setString(2, String.valueOf(listCourse.get(i).number));
                stm.setString(3, String.valueOf(listCourse.get(i).sem_id));
                rs = stm.executeQuery();
                while(rs.next()){
                    timetable temp = new timetable();
                    temp.week_day = rs.getInt("week_day");
                    temp.period_no = rs.getInt("period_no");
                    listCourse.get(i).timetable.add(temp);
                }
            }
        } catch(SQLException exp) {
            System.out.println("load timetable " + exp);
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
    public void load_mark(){
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        ResultSet rs = null;
        String query = "SELECT stc.midterm, stc.final FROM Student_Course stc WHERE stc.student_id = ? and stc.course_id = ? and stc.number = ? and stc.sem_id = ?";
        try{
            for (int i = 0; i < listCourse.size(); i++) {
                stm = conn.prepareStatement(query);
                stm.setString(1, super.username);
                stm.setString(2, listCourse.get(i).id);
                stm.setString(3, String.valueOf(listCourse.get(i).number));
                stm.setString(4, String.valueOf(listCourse.get(i).sem_id));
                rs = stm.executeQuery();
                while(rs.next()){
                    listCourse.get(i).midterm = rs.getDouble("midterm");
                    listCourse.get(i).finall = rs.getDouble("final");
                }
            }
        } catch(SQLException exp) {
            System.out.println("load mark " + exp);
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
    public void load_available_course(){
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        ResultSet rs = null;
        String query = "SELECT co.course_id, ci.course_name, co.number, CONCAT(te.lastname,' ',te.firstname) as fullname, te.teacher_id, se.sem_id, se.semester, se.years, co.room FROM Course co join Course_info ci on (co.course_id = ci.course_id) join teacher te on (co.teacher_id = te.teacher_id) join semester se on (co.sem_id = se.sem_id)";
        try{
            stm = conn.prepareStatement(query);
            //stm.setString(1, super.username);
            rs = stm.executeQuery();
            listAvailableCourse = new ArrayList<>();
            while(rs.next()){
                course temp = new course();
                temp.id = rs.getString("course_id");
                temp.name = rs.getString("course_name");
                temp.number = rs.getInt("number");
                temp.teacher_name = rs.getString("fullname");
                temp.teacher_id = rs.getString("teacher_id");
                temp.sem_id = rs.getInt("sem_id");
                temp.semester = rs.getInt("Semester");
                temp.year = rs.getInt("years");
                temp.room = rs.getString("room");
                if (!check_Joined(temp)){
                    listAvailableCourse.add(temp);
                }
            }
            
        } catch(SQLException exp) {
            System.out.println("load available courses " + exp);
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
        System.out.format("%3s %8s %30s %45s %6s %7s %10s\n", "STT", "ID", "Tên môn học", "Tên giáo viên", "Học kỳ", "Năm học", "phòng học");
        for (int i = 0; i < listCourse.size(); i++) {
            System.out.format("%3d %8s %30s %45s %6d %7d %10s\n", i+1, listCourse.get(i).id, listCourse.get(i).name +" "+listCourse.get(i).number, listCourse.get(i).teacher_name, listCourse.get(i).semester, listCourse.get(i).year, listCourse.get(i).room);
        }
    }

    public void view_timetable(){
        System.out.println("Thời khóa biểu");
        for (int i = 0; i < listCourse.size(); i++) {
            System.out.println((i+1)+". " + listCourse.get(i).name + " " + listCourse.get(i).number + ":  Phòng " + listCourse.get(i).room);
            for (int j = 0; j < listCourse.get(i).timetable.size(); j++) {
                System.out.println("\tThứ "+ listCourse.get(i).timetable.get(j).week_day + " - ca " +listCourse.get(i).timetable.get(j).period_no);
            }
        }
    }
    public void view_mark(){
        System.out.format("%3s %8s %30s %10s %10s\n", "STT", "ID", "Tên môn học", "Midterm", "Final");
        for (int i = 0; i < listCourse.size(); i++) {
            System.out.format("%3d %8s %30s %10f %10f\n", i+1, listCourse.get(i).id, listCourse.get(i).name +" "+listCourse.get(i).number, listCourse.get(i).midterm, listCourse.get(i).finall);
        }
    }

    public void view_available_course(){
        System.out.format("%3s %8s %30s %45s %6s %7s %10s\n", "STT", "ID", "Tên môn học", "Tên giáo viên", "Học kỳ", "Năm học", "phòng học");
        for (int i=0; i < listAvailableCourse.size(); i++) {
            System.out.format("%3d %8s %30s %45s %6d %7d %10s\n", i+1, listAvailableCourse.get(i).id, listAvailableCourse.get(i).name +" "+listAvailableCourse.get(i).number, listAvailableCourse.get(i).teacher_name, listAvailableCourse.get(i).semester, listAvailableCourse.get(i).year, listAvailableCourse.get(i).room);
        }
    }
    public boolean check_Joined(course a){
        for (int i = 0; i < listCourse.size(); i++) {
            if(a.id.equals(listCourse.get(i).id) && a.number == listCourse.get(i).number && a.sem_id == listCourse.get(i).sem_id){
               return true;
            }
        }
        return false;
    }
    

    public void enroll_course(String courseid, int number, int semid){
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        String query = "insert into Student_Course values (?, ?, ?, ?, null, null);";
        try{
            stm = conn.prepareStatement(query);
            stm.setString(1, super.username);
            stm.setString(2, courseid);
            stm.setInt(3, number);
            stm.setInt(4, semid);
            stm.executeUpdate();
            load_course();
            load_available_course();
            
        } catch(SQLException exp) {
            System.out.println("enroll_course" + exp);
            exp.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
                if (stm != null) stm.close();
            } catch (SQLException e) {
              e.printStackTrace();
            }
        }
    }
    
    public void cancel_course(String courseid, int number, int semid){
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        String query = "delete from Student_Course where student_id = ? and course_id = ? and number= ? and sem_id= ? ;";
        try{
            stm = conn.prepareStatement(query);
            stm.setString(1, super.username);
            stm.setString(2, courseid);
            stm.setInt(3, number);
            stm.setInt(4, semid);
            System.out.println(stm.toString());
            
            stm.executeUpdate(); // thực hiện lệnh delete

            load_course();
            load_available_course();
            
        } catch(SQLException exp) {
            System.out.println("cancel_course " + exp);
            exp.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
                if (stm != null) stm.close();
            } catch (SQLException e) {
              e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        student stu = new student("S18127202");
        //stu.swing_LoginPage();
        stu.load_course();
        stu.view_course();
        
    }
    
}