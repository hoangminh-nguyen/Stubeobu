package Login;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.*;
import java.awt.*;  
import java.awt.event.*;  
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class student extends account {
    
    ArrayList<course> listCourse = null; //cac khoa hoc da dang ky
    ArrayList<course> listAvailableCourse = null; //cac khoa hoc chua dang ky
    ArrayList<String> allyear;
    student(String name, String pass){
        super.username = name;
        super.password = pass;
    }

    student(String name){
        super.username = name;
    }
    
    public boolean check_year(int year_temp){
        String temp = String.valueOf(year_temp);
        for(int i = 0 ; i<allyear.size(); i++){
            System.out.println(allyear.get(i));
            if (allyear.get(i).equals(temp)) {return false;}
        }
        return true;
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
    
    public void search_course(String id, String name, String teacher,JTable table_course){
        for(int i =0; i<listCourse.size(); i++){
            //if
        }
    }
    
    @Override
    public void read_account_file(JTextField id, JTextField name, JTextField dob, JTextField gender) {
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
            id.setText(super.username);
            name.setText(super.l_name + " " + super.f_name);
            gender.setText(super.gender);
            dob.setText(super.dob);
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
        
    }

    
    
    public void load_course(JTable table_course, JComboBox year){
        // TODO Auto-generated method stub
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        ResultSet rs = null;
        String query = "SELECT stc.course_id, ci.course_name, stc.number, CONCAT(te.lastname,' ',te.firstname) as fullname, te.teacher_id, se.sem_id, se.semester, se.years, co.room, stc.is_studied FROM Student_Course stc join Course co on (stc.course_id = co.course_id and stc.number = co.number and stc.sem_id = co.sem_id)  join Course_info ci on (stc.course_id = ci.course_id)  join teacher te on (co.teacher_id = te.teacher_id) join semester se on (stc.sem_id = se.sem_id) WHERE student_id = ?";
        try{
            stm = conn.prepareStatement(query);
            stm.setString(1, super.username);
            rs = stm.executeQuery();
            listCourse = new ArrayList<>();
            DefaultTableModel model = (DefaultTableModel) table_course.getModel();
            model.setRowCount(0);
            allyear = new ArrayList<String>();
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
                temp.is_studied =  rs.getInt("is_studied")==1;
                if(temp.is_studied){
                    this.load_mark(temp);
                    if(check_year(temp.year)) {year.addItem(String.valueOf(temp.year));allyear.add(String.valueOf(temp.year));}
                }
                
                listCourse.add(temp);
                load_course_ui(temp, table_course);
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
    
    public void load_course_ui(course temp, JTable table_course){
        if(temp.is_studied){
            String temp_mid = temp.midterm != null ? String.valueOf(temp.midterm) : "";
            String temp_final = temp.finall != null ? String.valueOf(temp.finall) : "";
            Object[] row = {temp.id, temp.name + " " + temp.number, temp.teacher_name, temp.year, temp.semester, temp_mid,temp_final};
            DefaultTableModel model = (DefaultTableModel) table_course.getModel();
            model.addRow(row);
        }
        
    }
    
    public void load_sign_course(JTable table_course){
        // TODO Auto-generated method stub
        DefaultTableModel model = (DefaultTableModel) table_course.getModel();
        model.setRowCount(0);
        for(int i =0; i<listCourse.size();i++){
            if(!listCourse.get(i).is_studied){
                load_ava_course_ui(listCourse.get(i), table_course);
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
    public void load_mark(course temp){
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        ResultSet rs = null;
        String query = "SELECT stc.midterm, stc.final FROM Student_Course stc WHERE stc.student_id = ? and stc.course_id = ? and stc.number = ? and stc.sem_id = ?";
        try{
            
            stm = conn.prepareStatement(query);
            stm.setString(1, super.username);
            stm.setString(2, temp.id);
            stm.setString(3, String.valueOf(temp.number));
            stm.setString(4, String.valueOf(temp.sem_id));
            rs = stm.executeQuery();
            while(rs.next()){
                temp.midterm = (Double)rs.getObject("midterm");
                //System.out.print(temp.midterm+" midterm 005");
                temp.finall = (Double)rs.getObject("final");
                //System.out.print(temp.finall+" midterm 005");
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
    public void load_available_course(JTable table_course){
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        ResultSet rs = null;
        String query = "SELECT co.course_id, ci.course_name, co.number, CONCAT(te.lastname,' ',te.firstname) as fullname, te.teacher_id, se.sem_id, se.semester, se.years, co.room FROM Course co join Course_info ci on (co.course_id = ci.course_id) join teacher te on (co.teacher_id = te.teacher_id) join semester se on (co.sem_id = se.sem_id)";
        try{
            stm = conn.prepareStatement(query);
            //stm.setString(1, super.username);
            rs = stm.executeQuery();
            listAvailableCourse = new ArrayList<>();
            DefaultTableModel model = (DefaultTableModel) table_course.getModel();
            model.setRowCount(0);
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
                    load_ava_course_ui(temp, table_course);
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
    
    public void load_ava_course_ui(course temp, JTable table_course){
        Object[] row = {temp.id, temp.name + " " + temp.number, temp.teacher_name, temp.year, temp.semester};
        DefaultTableModel model = (DefaultTableModel) table_course.getModel();
        model.addRow(row);
    }
    public boolean check_Joined(course a){
        for (int i = 0; i < listCourse.size(); i++) {
            if(a.id.equals(listCourse.get(i).id) && a.sem_id == listCourse.get(i).sem_id){
               return true;
            }
        }
        return false;
    }
    
    
    public int getSemid(int year, int semester){
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
                System.out.println("Have id");
                sem_id = rs.getInt("sem_id");
                
            }
            
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
        
        return sem_id;
    }
    
    public void enroll_course(String courseid, int number, int semid,JTable table_course1, JTable table_course2,JTable table_course3, JComboBox year){
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        String query = "insert into Student_Course values (?, ?, ?, ?, null, null, 0);";
        try{
            stm = conn.prepareStatement(query);
            stm.setString(1, super.username);
            stm.setString(2, courseid);
            stm.setInt(3, number);
            stm.setInt(4, semid);
            stm.executeUpdate();
            
            load_course(table_course1, year);
            load_sign_course(table_course2);
            load_available_course(table_course3);
            
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
    
    public void cancel_course(String courseid, int number, int semid,JTable table_course1, JTable table_course2,JTable table_course3, JComboBox year){
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
            
            load_course(table_course1,year);
            load_sign_course(table_course2);
            load_available_course(table_course3);
            
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
    
    public void parse_name(String name, String[] namez){
        String[] each = name.split(" ");

        namez[0] = each[each.length-1];
        for (int i = 0; i<each.length-1;i++){
            namez[1] += each[i] + " ";
        }
        namez[1]=namez[1].substring(0,namez[1].length() - 1);
    }
    
    @SuppressWarnings("empty-statement")
    public void edit_info(String name, String dob, String gender){
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        String query = "update student set lastname = ?, firstname = ?, dob = ?, gender = ? where student_id = ?;";
        String[] namez = {"",""};
        parse_name(name, namez);
        try{
            stm = conn.prepareStatement(query);
            stm.setString(1, namez[1]);
            stm.setString(2, namez[0]);
            stm.setString(3, dob);
            stm.setString(4, gender);
            stm.setString(5, super.username);
            System.out.println(stm.toString());

            stm.executeUpdate(); // thực hiện lệnh delete

        } catch(SQLException exp) {
            System.out.println("update infor " + exp);
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

    
}