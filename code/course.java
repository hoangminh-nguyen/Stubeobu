package code;

import java.util.ArrayList;

class year{
    ArrayList<semester> listSemester;
}
class semester{
    ArrayList<course> listCourse;
}
public class course {

    protected String id, name, room;
    protected professor teacher;
    protected ArrayList<student> listStu;

}
