package com.abaka.chat_room.server;

import com.abaka.chat_room.client.dao.AccountDao;
import com.abaka.chat_room.client.entity.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author abaka
 * @date 2019/8/12 10:48
 */
public class UserReg {
    private JPanel usernamePanel;
    private JPanel passwordPanel;
    private JPanel briefPanel;
    private JPanel regPanel;
    private JLabel uesrnameLabel;
    private JTextField usernameText;
    private JLabel passwordLabel;
    private JPasswordField passwordText;
    private JLabel briefLabel;
    private JTextField briefText;
    private JButton register;
    private AccountDao accountDao = new AccountDao();

    public UserReg(){
        JFrame frame = new JFrame("用户注册");
        frame.setContentPane(regPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //设置位置
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);

        //执行注册活动
        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //获取用户输入的注册信息
                String username = usernameText.getText();
                String password = String.valueOf(passwordText.getPassword());
                String brief = briefText.getText();

                //将输入信息包装为user类保存到数据库中
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                user.setBrief(brief);
                boolean flag = accountDao.register(user);
                if (flag){
                    //返回登陆页面
                    JOptionPane.showMessageDialog(frame,"注册成功!","提示信息",JOptionPane.INFORMATION_MESSAGE);
                    frame.setVisible(false);
                }else {
                    //保存注册页面
                    JOptionPane.showMessageDialog(frame,"注册失败!","提示信息",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

}
