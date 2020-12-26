package Login;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class professor extends account{

    protected String id;
    ArrayList<course> listCourse = null;
    ArrayList<String> allyear;
    professor(String name, String pass){
        super.username = name; //id
        super.password = pass;
    }
    professor(String name){
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

    @Override
    public void read_account_file(JTextField id, JTextField name, JTextField dob, JTextField gender) {
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        ResultSet rs = null;
        String query = "SELECT * FROM Teacher WHERE teacher_id = ? ";
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
            
            //chèn vô giao diện
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

    public void load_course(JTable table_course, JComboBox year){
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        ResultSet rs = null;
        String query = "SELECT ci.course_id, ci.course_name, co.number, CONCAT(te.lastname,' ',te.firstname) as fullname, te.teacher_id, se.sem_id, se.semester, se.years, co.room FROM Course co join Course_info ci on (co.course_id = ci.course_id) join teacher te on (co.teacher_id = te.teacher_id) join semester se on (co.sem_id = se.sem_id) WHERE co.teacher_id = ?";
        try{
            stm = conn.prepareStatement(query);
            stm.setString(1, super.username);
            rs = stm.executeQuery();
            listCourse = new ArrayList<>();
            int i = 0;
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
                if(check_year(temp.year)) {year.addItem(String.valueOf(temp.year));allyear.add(String.valueOf(temp.year));}
                listCourse.add(temp);
                load_course_ui(temp, table_course);
            }
            
            
        } catch(SQLException exp) {
            System.out.println("load course " + exp);
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
        Object[] row = {temp.id, temp.name + " " + temp.number, temp.teacher_name, temp.year, temp.semester};
        DefaultTableModel model = (DefaultTableModel) table_course.getModel();
        model.addRow(row);
    }


    public void load_student_in_course(String course_idz, int numberz, int semidz){
        for (int i = 0; i < listCourse.size(); i++){
            if (listCourse.get(i).get_course_id().equals(course_idz) && listCourse.get(i).get_number() == numberz && listCourse.get(i).get_sem_id() == semidz){
                PreparedStatement stm = null;
                Connection conn = MySQLConnUtils.getMySQLConnection();
                ResultSet rs = null;
                String query = "SELECT stc.student_id, stc.midterm, stc.final FROM Student_Course stc join Course co on (stc.course_id = co.course_id and stc.number = co.number and stc.sem_id = co.sem_id) WHERE co.course_id = ? and co.number = ? and co.sem_id = ?;";
                try {
                    stm = conn.prepareStatement(query);
                    stm.setString(1, course_idz);
                    stm.setInt(2, numberz);
                    stm.setInt(3, semidz);
                    rs = stm.executeQuery();
                    listCourse.get(i).listStu = new ArrayList<>();
                    while(rs.next()){
                        student temp = new student(rs.getString("student_id"));
                        //temp.read_account_file();
                        listStudent to_add = new listStudent(temp, rs.getDouble("midterm"), rs.getDouble("final"));
                        listCourse.get(i).listStu.add(to_add);
                        }
                    } catch(SQLException exp) {
                        System.out.println("load_student_in_course " + exp);
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
    }

    public void load_timetable(String course_idz, int numberz, int semidz){
        for (int i = 0; i < listCourse.size(); i++){
            if (listCourse.get(i).get_course_id().equals(course_idz) && listCourse.get(i).get_number() == numberz && listCourse.get(i).get_sem_id() == semidz){
                PreparedStatement stm = null;
                Connection conn = MySQLConnUtils.getMySQLConnection();
                ResultSet rs = null;
                String query = "SELECT ti.week_day, ti.period_no FROM Course co join Timetable ti on (co.course_id = ti.course_id and co.number = ti.number and co.sem_id = ti.sem_id) WHERE co.course_id = ? and co.number = ? and co.sem_id = ?";
                try{
                    
                    stm = conn.prepareStatement(query);
                    stm.setString(1, listCourse.get(i).get_course_id());
                    stm.setString(2, String.valueOf(listCourse.get(i).get_number()));
                    stm.setString(3, String.valueOf(listCourse.get(i).get_sem_id()));
                    rs = stm.executeQuery();
                    while(rs.next()){
                        timetable temp = new timetable();
                        temp.week_day = rs.getInt("week_day");
                        temp.period_no = rs.getInt("period_no");
                        listCourse.get(i).timetable.add(temp);
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
        }
        
    }

    public void delete_student(String student_id, String course_idz, int numberz, int semidz){
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        String query = "delete from Student_Course where student_id = ? and course_id = ? and number= ? and sem_id= ? ;";
        for (int i = 0; i < listCourse.size(); i++){
            if (listCourse.get(i).get_course_id() == course_idz && listCourse.get(i).get_number() == numberz && listCourse.get(i).get_sem_id() == semidz){
                try{
                    stm = conn.prepareStatement(query);
                    stm.setString(1, student_id);
                    stm.setString(2, listCourse.get(i).id);
                    stm.setInt(3, listCourse.get(i).number);
                    stm.setInt(4, listCourse.get(i).sem_id);
                    System.out.println(stm.toString());
                    
                    stm.executeUpdate(); // thực hiện lệnh delete
                    load_student_in_course(course_idz,numberz,semidz);
                    
                } catch(SQLException exp) {
                    System.out.println("delete stu " + exp);
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
    }

    public void add_student(String student_id, String course_idz, int numberz, int semidz){
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        String query = "insert into Student_Course values (?, ?, ?, ?, null, null)";
        for (int i = 0; i < listCourse.size(); i++){
            if (listCourse.get(i).get_course_id() == course_idz && listCourse.get(i).get_number() == numberz && listCourse.get(i).get_sem_id() == semidz){
                try{
                    stm = conn.prepareStatement(query);
                    stm.setString(1, student_id);
                    stm.setString(2, listCourse.get(i).id);
                    stm.setInt(3, listCourse.get(i).number);
                    stm.setInt(4, listCourse.get(i).sem_id);
                    System.out.println(stm.toString());


                    stm.executeUpdate(); // thực hiện lệnh delete
                    load_student_in_course(course_idz,numberz,semidz);
                    
                } catch(SQLException exp) {
                    System.out.println("add stu " + exp);
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
    }
    
    public void set_midterm(String student_id, String course_idz, int numberz, int semidz, Double new_midterm){
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        String query = "update student_course set midterm = ? where student_id = ? and course_id = ? and number= ? and sem_id= ? ;";
        for (int i = 0; i < listCourse.size(); i++){
            if (listCourse.get(i).get_course_id() == course_idz && listCourse.get(i).get_number() == numberz && listCourse.get(i).get_sem_id() == semidz){
                try{
                    stm = conn.prepareStatement(query);
                    stm.setDouble(1, new_midterm);
                    stm.setString(2, student_id);
                    stm.setString(3, listCourse.get(i).id);
                    stm.setInt(4, listCourse.get(i).number);
                    stm.setInt(5, listCourse.get(i).sem_id);
                    System.out.println(stm.toString());
                    
                    stm.executeUpdate(); // thực hiện lệnh delete
                    load_student_in_course(course_idz,numberz,semidz);
                    
                } catch(SQLException exp) {
                    System.out.println("update midterm " + exp);
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
    }
    
    public void set_final(String student_id, String course_idz, int numberz, int semidz, Double new_final){
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        String query = "update student_course set final = ? where student_id = ? and course_id = ? and number= ? and sem_id= ? ;";
        for (int i = 0; i < listCourse.size(); i++){
            if (listCourse.get(i).get_course_id() == course_idz && listCourse.get(i).get_number() == numberz && listCourse.get(i).get_sem_id() == semidz){
                try{
                    stm = conn.prepareStatement(query);
                    stm.setDouble(1, new_final);
                    stm.setString(2, student_id);
                    stm.setString(3, listCourse.get(i).id);
                    stm.setInt(4, listCourse.get(i).number);
                    stm.setInt(5, listCourse.get(i).sem_id);
                    System.out.println(stm.toString());
                    
                    stm.executeUpdate(); // thực hiện lệnh delete
                    load_student_in_course(course_idz,numberz,semidz);
                    
                } catch(SQLException exp) {
                    System.out.println("update final " + exp);
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
    }

    @Override
    public void role_menu() {
        // TODO Auto-generated method stub
        
    }

    
}

//now i go
