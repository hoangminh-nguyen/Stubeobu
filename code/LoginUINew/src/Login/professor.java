package Login;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.*;
import java.util.Calendar;
import java.util.TimeZone;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Date; 

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
                //System.out.println("Have id");
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


    public void load_student_in_course(String course_idz, int numberz, int semidz, JTable student_course){
        
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        ResultSet rs = null;
        String query = "SELECT stc.student_id, concat(st.lastname ,' ',st.firstname) as student_name, st.gender, st.dob, stc.midterm, stc.final FROM Student_Course stc join Course co on (stc.course_id = co.course_id and stc.number = co.number and stc.sem_id = co.sem_id) join student st on (st.student_id = stc.student_id) WHERE co.course_id = ? and co.number = ? and co.sem_id = ?;";
        try {
            stm = conn.prepareStatement(query);
            stm.setString(1, course_idz);
            stm.setInt(2, numberz);
            stm.setInt(3, semidz);
            rs = stm.executeQuery();
            DefaultTableModel model = (DefaultTableModel) student_course.getModel();
            model.setRowCount(0);
            while(rs.next()){
                String student_id = rs.getString("student_id");
                String name = rs.getString("student_name");
                String dob = rs.getString("dob");
                String gender = rs.getString("gender");
                Double midterm = (Double)rs.getObject("midterm");
                Double finalz = (Double)rs.getObject("final");
                load_student_mark_ui(student_id, name, dob, gender, midterm, finalz, student_course);
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
    
    public void load_student_mark_ui(String id, String name, String dob, String gender, Double midterm, Double finalz, JTable table_course){
        
        String temp_mid = midterm != null ? String.valueOf(midterm) : "";
        String temp_final = finalz != null ? String.valueOf(finalz) : "";
        Object[] row = {id, name, dob, gender,temp_mid,temp_final};
        DefaultTableModel model = (DefaultTableModel) table_course.getModel();
        model.addRow(row);
        
    }
    
    private int getSemidNow(){
        java.util.Date date = null; // your date
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

    public void load_timetable(JTable timetable){
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        ResultSet rs = null;
        String query = "SELECT distinct ti.week_day, ti.period_no, ci.course_name FROM Course co join Timetable ti on (co.course_id = ti.course_id and co.number = ti.number and co.sem_id = ti.sem_id) join student_course stc on (co.course_id = stc.course_id and co.number = stc.number and co.sem_id = stc.sem_id) join course_info ci on (co.course_id = ci.course_id) WHERE co.teacher_id = ? and stc.is_studied = 1 and co.sem_id = ?";
        try{
                DefaultTableModel model = (DefaultTableModel) timetable.getModel();
		model.setRowCount(4);
		model.setColumnCount(6);
                stm = conn.prepareStatement(query);
                stm.setString(1, super.username);
                stm.setInt(2, getSemidNow());
                rs = stm.executeQuery();
                while(rs.next()){
                    int week_day = rs.getInt("week_day");
                    int period_no = rs.getInt("period_no");
		    String tempname = rs.getString("course_name");
		    model.setValueAt(tempname,  period_no-1,week_day-2 );
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
                    //load_student_in_course(course_idz,numberz,semidz);
                    
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
    
    public boolean checkStudiedCourse(String courseid, int number, int sem_id){
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        ResultSet rs = null;
        String query = "select stc.is_studied from Student_Course stc where stc.course_id = ? and stc.number = ? and stc.sem_id = ? limit 1;";
        try{
            stm = conn.prepareStatement(query);
            stm.setString(1, courseid);
            stm.setInt(2, number);
            stm.setInt(3, sem_id);
            rs = stm.executeQuery();
            while(rs.next()){  
                return rs.getInt("is_studied")==1;
            }
                    
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
        return false;
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
                    //load_student_in_course(course_idz,numberz,semidz);
                    
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
       
        try{
            stm = conn.prepareStatement(query);
            stm.setDouble(1, new_midterm);
            stm.setString(2, student_id);
            stm.setString(3, course_idz);
            stm.setInt(4, numberz);
            stm.setInt(5, semidz);
            System.out.println(stm.toString());

            stm.executeUpdate(); 

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
    
    public void set_final(String student_id, String course_idz, int numberz, int semidz, Double new_final){
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        String query = "update student_course set final = ? where student_id = ? and course_id = ? and number= ? and sem_id= ? ;";
       
        try{
            stm = conn.prepareStatement(query);
            stm.setDouble(1, new_final);
            stm.setString(2, student_id);
            stm.setString(3, course_idz);
            stm.setInt(4, numberz);
            stm.setInt(5, semidz);
            System.out.println(stm.toString());

            stm.executeUpdate(); 

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
    
    public void parse_name(String name, String[] namez){
        String[] each = name.split(" ");

        namez[0] = each[each.length-1];
        for (int i = 0; i<each.length-1;i++){
            namez[1] += each[i] + " ";
        }
        namez[1]=namez[1].substring(0,namez[1].length() - 1);
    }
    
    public void edit_info(String name, String dob, String gender){
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
            stm.setString(5, super.username);
            System.out.println(stm.toString());

            stm.executeUpdate(); 

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

    @Override
    public void role_menu() {
        // TODO Auto-generated method stub
        
    }

    
}

//now i go
