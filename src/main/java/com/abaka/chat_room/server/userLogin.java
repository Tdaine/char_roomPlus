package com.abaka.chat_room.server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author abaka
 * @date 2019/8/12 10:50
 */
public class userLogin {
    private JPanel img;
    private JLabel imgLabel;
    private JPanel usernamePanel;
    private JLabel usernameLabel;
    private JTextField usernameText;
    private JLabel passwordLabel;
    private JTextField passwordText;
    private JPanel butLabel;
    private JButton registerBut;
    private JButton loginBut;
    private JPanel userLogin;


    public userLogin() {
        registerBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //调用注册
                new UserReg();
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("用户登陆");
        frame.setContentPane(new userLogin().userLogin);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }
}
