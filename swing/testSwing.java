package swing;

import java.awt.event.*;  
import javax.swing.*;    
public class testSwing {  
public static void main(String[] args) {  
    JFrame f=new JFrame("Button Example");  
    final JTextField tf=new JTextField();  
    tf.setBounds(50,50, 150,20);  
    final JTextField tf1=new JTextField();  
    tf1.setBounds(50,150, 150,20);  

    JButton b=new JButton("Click Here");  
    b.setBounds(50,100,95,30);  
    b.addActionListener(new ActionListener(){  
public void actionPerformed(ActionEvent e){  
            String text = tf.getText();  
            tf1.setText(text);
        }  
    });  
    f.add(b);
    f.add(tf);  
    f.add(tf1); 
    f.setSize(400,400);  
    f.setLayout(null);  
    f.setVisible(true);   
}  
}