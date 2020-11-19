package code;

import java.io.*;
import java.util.Scanner;

public abstract class account {
    protected String username;
    protected String password;
    protected String f_name;
    protected String l_name;

    account(){
        username="";
        password="";
        f_name="";
        l_name="";
    };
    public void change_password() {
        Scanner scan = new Scanner(System.in);
        int i = 0;
        String temp = "";
        while (i == 0) {
            System.out.print("Nhập mật khẩu cũ: ");
            temp = scan.nextLine();
            if (temp.equals(password)) {
                System.out.print("Nhập mật khẩu mới: ");
                password = scan.nextLine();
                i = 1;
            } else {
                System.out.println("Mật khẩu cũ bạn sai rồi.");
                continue;
            }
        }
    }

    public abstract void read_account_file();

    public abstract void role_menu();

    public account main_menu() {
        Scanner s = new Scanner(System.in);
        
        System.out.println("Chào mừng bạn tới phần mềm Quản lí học sinh Stubeobu");
        System.out.println("Mời bạn chọn 1 chức năng sau:");
        System.out.println("1.Đăng nhập");
        System.out.print("2.Thoát\nChọn: ");
        int i = s.nextInt();

        s.nextLine();
        if (i == 1){
            String name;
            String pass;
            while(true)
            {
                System.out.print("Tài khoản: ");
                name = s.nextLine();
                System.out.print("Mật khẩu: ");
                pass = s.nextLine();
                if (sign_in(name, pass)) break;
                else System.out.println("\nSai tài khoản hoặc mật khẩu.");
            }
            switch (name.charAt(0)) {
                case 'S':
                    account st = new student();
                    return st;
                case 'T':
                    account pr = new professor();
                    return pr;
                case 'A':
                    account ad = new admin();
                    return ad;
                default:
                    break;
            }
        }
        return null;
    }
    

    public boolean sign_in(String name, String pass) {
        char type = name.charAt(0);
        String path;
        switch (type) {
            case 'S':
                path = "./code/student.csv";
                break;
            case 'T':
                path = "./code/teacher.csv";
                break;
            case 'A':
                path = "./code/admin.csv";
                break;
            default:
                return false;
        }
        String []arg;
        String s;
        try (BufferedReader buf = new BufferedReader(new FileReader(path)))
        {
            s=buf.readLine(); //đọc dòng tiêu đề
            while((s=buf.readLine())!=null){
                arg = s.split(",");
                if(name.equals(arg[0]) && pass.equals(arg[1])){
                    this.username = name;
                    this.password = pass;
                    return true;
                }
            }
            
        } catch (IOException exc) {
            System.out.println("I/O Error: " + exc);
        }
        return false;
    }
    public abstract void write_info();

}
