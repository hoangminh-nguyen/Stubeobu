package code;

import java.io.*;
import java.sql.*;
import java.util.Scanner;
import java.awt.*;  
import java.awt.event.*;  
import javax.swing.*;

public abstract class account {
    protected String username; // id 
    protected String password;
    protected String f_name;
    protected String l_name;
    protected String gender;
    protected String dob;

    account(){
        username="";
        password="";
        f_name="";
        l_name="";
        gender="";
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
        swing_LoginPage();
        if (i == 1){
            String name;
            String pass;
            while(true)
            {
                System.out.print("Tài khoản: ");
                name = s.nextLine();
                System.out.print("Mật khẩu: ");
                pass = s.nextLine();
                if (sign_in(name, pass)) {
                    this.username = name;
                    this.password = pass;
                    break;
                }
                
                else System.out.println("\nSai tài khoản hoặc mật khẩu.");
            }
            switch (name.charAt(0)) {
                case 'S':
                    account st = new student(this.username, this.password);
                    return st;
                case 'T':
                    account pr = new professor(this.username, this.password);
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
    

    public static boolean sign_in(String name, String pass) {
        PreparedStatement stm = null;
        Connection conn = MySQLConnUtils.getMySQLConnection();
        ResultSet rs = null;
        String query = "SELECT * FROM Account WHERE username = ? and pass = ?";
        try{
            stm = conn.prepareStatement(query);
            stm.setString(1, name);
            stm.setString(2, pass);
            rs = stm.executeQuery();
            if(rs.next()){
                return true;
            }
        } catch(SQLException exp) {
            System.out.println("Sign in" + exp);
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
        return false;
    }
    
    public static void swing_LoginPage(){
        JFrame f = new JFrame("STUBEOBU"); 
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
          
        JPanel panel = new JPanel();
        panel.setLayout(null);

        // Tạo chỗ nhập id và password
        JLabel label_id = new JLabel("Tài khoản");
        JLabel label_pass = new JLabel("Mật khẩu");
        JTextField text_id = new JTextField();  
        JPasswordField text_pass = new JPasswordField();  

        // Sắp xếp xị trí chỗ nhập id và password
        label_id.setBounds(50, 50, 150, 25);
        label_pass.setBounds(50, 100, 150, 25);
        text_id.setBounds(120,50, 150,25);  
        text_pass.setBounds(120,100, 150,25);  

        // Tạo nút đăng nhập
        JButton b = new JButton("Đăng nhập");  
        b.setBounds(50,150,95,30);
        b.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e) {
                String id = text_id.getText();  
                String pass = text_pass.getText();
                if(sign_in(id, pass)){
                    switch (id.charAt(0)) {
                        case 'S':
                            account st = new student(id, pass);
                            st.role_menu();
                        case 'T':
                            account pr = new professor(id, pass);
                            pr.role_menu();
                        case 'A':
                            account ad = new admin();
                        default:
                            break;
                    }   
                    f.setVisible(false);
                    System.exit(0); // stop program
                    f.dispose(); // close window
                }
                else{
                    JOptionPane.showMessageDialog(f, "Sai tài khoản hoặc mật khẩu", "", 0);
                    text_pass.setText("");
                }
            }  
        });  

        f.add(panel);
        panel.add(label_id);
        panel.add(label_pass);
        panel.add(text_id);
        panel.add(text_pass);
        panel.add(b);

        f.setSize(400, 300);
        f.setLocationRelativeTo(null); 
        f.setVisible(true);  
    }


}
