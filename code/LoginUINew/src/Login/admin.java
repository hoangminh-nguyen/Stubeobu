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
        System.out.println("Have id");
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
    
   
   
   public void load_professor_list(){
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        ResultSet rs = null;
        String query = "SELECT * FROM Account where left(username, 1) = 'T'";
        try{
            stm = conn.prepareStatement(query);
            rs = stm.executeQuery();
            list_Professor = new ArrayList<>();
            String id;
            while(rs.next()){
                id = rs.getString("username");
                professor temp = new professor(id);
                //temp.read_account_file();
                list_Professor.add(temp);
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
    public void load_course_list(){
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        ResultSet rs = null;
        String query = "SELECT ci.course_id, ci.course_name, co.number, CONCAT(te.lastname,' ',te.firstname) as fullname, te.teacher_id, se.sem_id, se.semester, se.years, co.room FROM Course co join Course_info ci on (co.course_id = ci.course_id) join teacher te on (co.teacher_id = te.teacher_id) join semester se on (co.sem_id = se.sem_id)";
        try{
            stm = conn.prepareStatement(query);
            rs = stm.executeQuery();
            list_Course = new ArrayList<>();
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
                list_Course.add(temp);
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

    public void see_student_info(String student_id){
        for (int i = 0; i < list_Student.size(); i++) {
            if(list_Student.get(i).username.equals(student_id)){
                // code here
                System.out.println("hehe");
                return;
            }
        }
    }
    public void see_professor_info(String professor_id){
        for (int i = 0; i < list_Professor.size(); i++) {
            if(list_Professor.get(i).username.equals(professor_id)){
                // code here
                System.out.println("hehe");
                return;
            }
        }
    }
    public void see_course_info(String course_id, int number, int semid){
        for (int i = 0; i < list_Course.size(); i++) {
            if(!list_Course.get(i).id.equals(course_id) || list_Course.get(i).number != number || list_Course.get(i).sem_id != semid){
                continue;
            }
            else{
                //code here
                System.out.println("hehe");
                return;
            }
        }
    }
    
//    public void load_student_course(){
//        for (int i = 0; i < list_Student.size(); i++) {
//            list_Student.get(i).load_course();
//        }
//    }
//    public void load_proffesor_course(){
//        for (int i = 0; i < list_Professor.size(); i++) {
//            list_Professor.get(i).load_course();
//        }
//    }
    public void load_student_in_course(String course_idz, int numberz, int semidz){
        for (int i = 0; i < list_Course.size(); i++){
            if (list_Course.get(i).get_course_id().equals(course_idz) && list_Course.get(i).get_number() == numberz && list_Course.get(i).get_sem_id() == semidz){
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
                    list_Course.get(i).listStu = new ArrayList<>();
                    while(rs.next()){
                        student temp = new student(rs.getString("student_id"));
                        //temp.read_account_file();
                        listStudent to_add = new listStudent(temp, rs.getDouble("midterm"), rs.getDouble("final"));
                        list_Course.get(i).listStu.add(to_add);
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




    //@Override
    public void read_account_file() {
        // TODO Auto-generated method stub

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
