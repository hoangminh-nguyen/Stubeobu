package code;

public class Main {
    public static void main(String[] args) throws Exception{
        account acc = new student("a", "b");
        if ((acc = acc.main_menu())!=null)
        {
             acc.role_menu();
        };

    }
}
