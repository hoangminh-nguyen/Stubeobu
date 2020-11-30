package code;

import java.util.ArrayList;

class timetable{
    int week_day, period_no;
}
class listStudent{
    student student;
    Double midterm, finall;
    listStudent(student studentz, Double m, Double f){
        student = studentz;
        midterm = m;
        finall = f;
    }

}
public class course {
    protected String id, name, room;
    protected String teacher_name, teacher_id;
    protected ArrayList<listStudent> listStu;
    protected Double midterm, finall;
    protected int semester, year, sem_id, number;
    protected ArrayList<timetable> timetable;
    String get_course_id(){
        return id;
    }
    int get_number(){
        return number;
    }
    int get_sem_id(){
        return sem_id;
    }


}
