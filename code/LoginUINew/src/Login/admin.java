package Login;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.*;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class admin extends account{

    ArrayList<course> list_Course = null;
    ArrayList<student> list_Student = null;
    ArrayList<professor> list_Professor = null;

    public void load_student_list(JTable tablestudent){
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        ResultSet rs = null;
        String query = "SELECT * FROM Account ac join student st on ac.username=st.student_id where left(ac.username, 1) = 'S'";
        DefaultTableModel model = (DefaultTableModel) tablestudent.getModel();
        model.setRowCount(0);
        try{
            stm = conn.prepareStatement(query);
            rs = stm.executeQuery();
            list_Student = new ArrayList<>();
            String id, password, fname, lname, dob, gender;
            while(rs.next()){
                id = rs.getString("student_id");
                password = rs.getString("pass");
                fname = rs.getString("firstname");
                lname = rs.getString("lastname");
                dob = rs.getString("dob");
                gender = rs.getString("gender");
                load_student_list_ui(id,password,fname,lname,dob,gender, tablestudent);
            }
        } catch(SQLException exp) {
            System.out.println("load_student_list " + exp);
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
   public void load_student_list_ui(String id, String password, String fname, String lname, String dob, String gender, JTable tablestudent){
        // TODO Auto-generated method stub
        DefaultTableModel model = (DefaultTableModel) tablestudent.getModel();
        Object[] row = {id, lname + " " + fname, dob, gender, password};
        model.addRow(row);
    }
    
   public void edit_student_info(String mssv, String name, String dob, String gender, String password){
       edit_student_table(mssv, name, dob, gender);
       edit_account_table(mssv, password);
   }
   public void edit_student_table(String mssv, String name, String dob, String gender){
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
            stm.setString(5, mssv);
            stm.executeUpdate(); 

        } catch(SQLException exp) {
            System.out.println("Admin update student table " + exp);
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
   public void edit_account_table(String username, String password){
       PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        String query = "update account set pass = ? where username = ?;";
        try{
            stm = conn.prepareStatement(query);
            stm.setString(1, password);
            stm.setString(2, username);
            stm.executeUpdate(); 

        } catch(SQLException exp) {
            System.out.println("Admin update account table " + exp);
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
   
   public void delete_student(String mssv){
       delete_student_studentcourse(mssv);
       delete_student_student(mssv);
       delete_student_account(mssv);
       
   }
   public void delete_student_student(String mssv){
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        String query = "delete from Student where student_id = ?;";
        try{
            stm = conn.prepareStatement(query);
            stm.setString(1, mssv);
            stm.executeUpdate();
        } catch(SQLException exp) {
            System.out.println("admin delete_student_student " + exp);
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
   public void delete_student_account(String mssv){
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        String query = "delete from account where username = ?;";
        try{
            stm = conn.prepareStatement(query);
            stm.setString(1, mssv);
            stm.executeUpdate();
        } catch(SQLException exp) {
            System.out.println("admin delete_student_account " + exp);
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
   public void delete_student_studentcourse(String mssv){
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        String query = "delete from student_course where student_id = ?;";
        try{
            stm = conn.prepareStatement(query);
            stm.setString(1, mssv);
            stm.executeUpdate();
        } catch(SQLException exp) {
            System.out.println("admin delete_student_studentcourse " + exp);
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
    
   public void add_student(String mssv, String name, String dob, String gender, String password){
       add_account_table(mssv, password);
       add_student_table(mssv, name, dob, gender);
       
   }
   public void add_student_table(String mssv, String name, String dob, String gender){
       PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        String query = "insert into student values (?,?,?,?,?);";
        String[] namez = {"",""};
        parse_name(name, namez);
        try{
            stm = conn.prepareStatement(query);
            stm.setString(1, mssv);
            stm.setString(2, namez[1]);
            stm.setString(3, namez[0]);
            stm.setString(4, gender);
            stm.setString(5, dob);
            stm.executeUpdate(); 

        } catch(SQLException exp) {
            System.out.println("Admin add student table " + exp);
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
   public void add_account_table(String username, String password){
       PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        String query = "insert into account values (?, ?);";
        try{
            stm = conn.prepareStatement(query);
            stm.setString(1, username);
            stm.setString(2, password);
            stm.executeUpdate(); 

        } catch(SQLException exp) {
            System.out.println("Admin add student account table " + exp);
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
   
   public void load_course_of_student(String mssv, JTable course_of_student){
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        ResultSet rs = null;
        String query = "SELECT stc.course_id, ci.course_name, co.number, CONCAT(te.lastname,' ',te.firstname) as teacher, se.semester, se.years, stc.midterm, stc.final FROM student_course stc join course co on (stc.course_id=co.course_id and stc.number=co.number and stc.sem_id=co.sem_id) join course_info ci on ci.course_id=co.course_id join semester se on co.sem_id=se.sem_id join teacher te on te.teacher_id=co.teacher_id where stc.student_id=? ";
        DefaultTableModel model = (DefaultTableModel) course_of_student.getModel();
        model.setRowCount(0);
        try{
            stm = conn.prepareStatement(query);
            stm.setString(1, mssv);
            rs = stm.executeQuery();
            String course_id, course_name, teacher;
            Integer semester, year;
            Double midterm, finall;
            while(rs.next()){
                course_id = rs.getString("course_id");
                course_name = rs.getString("course_name") + " " + String.valueOf(rs.getInt("number"));
                teacher = rs.getString("teacher");
                semester = rs.getInt("semester");
                year = rs.getInt("years");
                midterm = (Double)rs.getObject("midterm");
                finall = (Double)rs.getObject("final");
                load_course_of_student_ui(course_id, course_name, teacher, semester, year, midterm, finall, course_of_student);
            }
        } catch(SQLException exp) {
            System.out.println("load_course_of_student " + exp);
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
   
   public void load_course_of_student_ui(String course_id, String  course_name, String  teacher, Integer semester, Integer year, Double midterm, Double finall, JTable course_of_student){
       String temp_mid = midterm != null ? String.valueOf(midterm) : "";
       String temp_final = finall != null ? String.valueOf(finall) : ""; 
       DefaultTableModel model = (DefaultTableModel) course_of_student.getModel();
        Object[] row = {course_id, course_name, teacher, semester, year, temp_mid, temp_final};
        model.addRow(row);
   }
   
   public void load_teacher_list(JTable tableteacher){
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        ResultSet rs = null;
        String query = "SELECT * FROM Account ac join teacher st on ac.username=st.teacher_id where left(ac.username, 1) = 'T'";
        DefaultTableModel model = (DefaultTableModel) tableteacher.getModel();
        model.setRowCount(0);
        try{
            stm = conn.prepareStatement(query);
            rs = stm.executeQuery();
            list_Student = new ArrayList<>();
            String id, password, fname, lname, dob, gender;
            while(rs.next()){
                id = rs.getString("teacher_id");
                password = rs.getString("pass");
                fname = rs.getString("firstname");
                lname = rs.getString("lastname");
                dob = rs.getString("dob");
                gender = rs.getString("gender");
                load_teacher_list_ui(id,password,fname,lname,dob,gender, tableteacher);
            }
        } catch(SQLException exp) {
            System.out.println("load_teacher_list " + exp);
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
   public void load_teacher_list_ui(String id, String password, String fname, String lname, String dob, String gender, JTable tableteacher){
        // TODO Auto-generated method stub
        DefaultTableModel model = (DefaultTableModel) tableteacher.getModel();
        Object[] row = {id, lname + " " + fname, dob, gender, password};
        model.addRow(row);
    }
   
   public void load_course_of_teacher(String msgv, JTable course_of_teacher){
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        ResultSet rs = null;
        String query = "SELECT co.course_id, ci.course_name, co.number, CONCAT(te.lastname,' ',te.firstname) as teacher, se.semester, se.years, co.room FROM course co join course_info ci on ci.course_id=co.course_id join semester se on co.sem_id=se.sem_id join teacher te on te.teacher_id=co.teacher_id where co.teacher_id=? ";
        DefaultTableModel model = (DefaultTableModel) course_of_teacher.getModel();
        model.setRowCount(0);
        try{
            stm = conn.prepareStatement(query);
            stm.setString(1, msgv);
            rs = stm.executeQuery();
            String course_id, course_name, teacher, room;
            Integer semester, year;
            while(rs.next()){
                course_id = rs.getString("course_id");
                course_name = rs.getString("course_name") + " " + String.valueOf(rs.getInt("number"));
                teacher = rs.getString("teacher");
                semester = rs.getInt("semester");
                year = rs.getInt("years");
                room = rs.getString("room");
                load_course_of_teacher_ui(course_id, course_name, teacher, semester, year, room, course_of_teacher);
            }
        } catch(SQLException exp) {
            System.out.println("load_course_of_teacher " + exp);
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
   public void load_course_of_teacher_ui(String course_id, String  course_name, String  teacher, Integer semester, Integer year, String room, JTable course_of_teacher){
       DefaultTableModel model = (DefaultTableModel) course_of_teacher.getModel();
        Object[] row = {course_id, course_name, teacher, semester, year, room};
        model.addRow(row);
   }
   
    public void add_teacher(String msgv, String name, String dob, String gender, String password){
        add_account_table(msgv, password);
        add_teacher_table(msgv, name, dob, gender);
    }
    public void add_teacher_table(String msgv, String name, String dob, String gender){
       PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        String query = "insert into teacher values (?,?,?,?,?);";
        String[] namez = {"",""};
        parse_name(name, namez);
        try{
            stm = conn.prepareStatement(query);
            stm.setString(1, msgv);
            stm.setString(2, namez[1]);
            stm.setString(3, namez[0]);
            stm.setString(4, gender);
            stm.setString(5, dob);
            stm.executeUpdate(); 

        } catch(SQLException exp) {
            System.out.println("Admin add teacher table " + exp);
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
   
   public void delete_teacher(String msgv){
       delete_teacher_course(msgv);
       delete_teacher_teacher(msgv);
       delete_teacher_account(msgv);
       
   }
   public void delete_teacher_course(String msgv){
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        String query = "update course set teacher_id=null where teacher_id = ?;";
        try{
            stm = conn.prepareStatement(query);
            stm.setString(1, msgv);
            stm.executeUpdate();
        } catch(SQLException exp) {
            System.out.println("admin delete_teacher_teacher " + exp);
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
   public void delete_teacher_account(String msgv){
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        String query = "delete from account where username = ?;";
        try{
            stm = conn.prepareStatement(query);
            stm.setString(1, msgv);
            stm.executeUpdate();
        } catch(SQLException exp) {
            System.out.println("admin delete_teacher_account " + exp);
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
   public void delete_teacher_teacher(String msgv){
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        String query = "delete from teacher where teacher_id = ?;";
        
        try{
            stm = conn.prepareStatement(query);
            stm.setString(1, msgv);
            System.out.println(stm);
            stm.executeUpdate();
        } catch(SQLException exp) {
            System.out.println("admin delete_teacher_teacher " + exp);
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
   
    public void edit_teacher_info(String mssv, String name, String dob, String gender, String password){
       edit_teacher_table(mssv, name, dob, gender);
       edit_account_table(mssv, password);
   }
   public void edit_teacher_table(String mssv, String name, String dob, String gender){
       PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        String query = "update teacher set lastname = ?, firstname = ?, dob = ?, gender = ? where teacher_id = ?;";
        String[] namez = {"",""};
        parse_name(name, namez);
        try{
            stm = conn.prepareStatement(query);
            stm.setString(1, namez[1]);
            stm.setString(2, namez[0]);
            stm.setString(3, dob);
            stm.setString(4, gender);
            stm.setString(5, mssv);
            stm.executeUpdate(); 

        } catch(SQLException exp) {
            System.out.println("Admin update teacher table " + exp);
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
   
   public void delete_course_of_teacher(String teacher_id, String course_id, int number, int semidcuakhoahoc){
       PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        String query = "update course set teacher_id = null where teacher_id = ? and course_id=? and number =? and sem_id=?;";
        try{
            stm = conn.prepareStatement(query);
            stm.setString(1, teacher_id);
            stm.setString(2, course_id);
            stm.setInt(3, number);
            stm.setInt(4, semidcuakhoahoc);
            stm.executeUpdate();
        } catch(SQLException exp) {
            System.out.println("admin delete_course_of_teacher " + exp);
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
   
   

    @Override
    public void role_menu() {
        // TODO Auto-generated method stub
        System.out.println("day la admin");
    }

    @Override
    public void read_account_file(JTextField id, JTextField name, JTextField dob, JTextField gender) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
}
