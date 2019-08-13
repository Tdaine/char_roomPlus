package com.abaka.chat_room.server;

import com.abaka.chat_room.client.dao.AccountDao;
import com.abaka.chat_room.client.entity.User;
import com.abaka.chat_room.client.service.Connect2Server;
import com.abaka.chat_room.util.CommUtils;
import com.abaka.chat_room.vo.MessageVO;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.util.Set;

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
    private JPasswordField passwordText;
    private JPanel butLabel;
    private JButton registerBut;
    private JButton loginBut;
    private JPanel userLogin;


    public userLogin() {
        JFrame frame = new JFrame("用户登陆");
        frame.setContentPane(userLogin);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);

        //注册按钮
        registerBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //调用注册
                new UserReg();
            }
        });

        //登陆按钮
        loginBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //校验用户信息
                String username = usernameText.getText();
                String password = String.valueOf(passwordText.getPassword());
                User user = new AccountDao().login(username,password);
                if (user != null){

                    //成功，加载用户列表
                    JOptionPane.showMessageDialog(frame,
                            "登陆成功","提示信息",JOptionPane.INFORMATION_MESSAGE);
                    frame.setVisible(false);
                    //与服务器建立连接，将当前用户的用户名与密码发送到客户端
                    Connect2Server connect2Server = new Connect2Server();
                    MessageVO mss2Server = new MessageVO();
                    mss2Server.setType("1");
                    mss2Server.setContent(username);
                    String json2Server = CommUtils.object2Json(mss2Server);
                    //将用户名发送到服务端
                    try {
                        PrintStream out = new PrintStream(connect2Server.getOut(),
                                true,"UTF-8");
                        out.println(json2Server);
                        //取得服务端发回的所有在线用户信息
                        Scanner in = new Scanner(connect2Server.getIn());
                        if (in.hasNextLine()){
                            String msgFromServerStr = in.nextLine();
                            MessageVO msgFromServer = (MessageVO) CommUtils.json2Object(
                                    msgFromServerStr,MessageVO.class);
                            Set<String> users = (Set<String>) CommUtils.json2Object(
                                    msgFromServer.getContent(),Set.class);
                            System.out.println("所有在线用户为:" + users);
                            //加载用户列表界面
                            //将当前用户名、所有在线好友、与服务器建立连接传递到好友列表界面
                            new FriendsList(username,users,connect2Server);
                        }
                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();
                    }
                }else {
                    //失败，停留在当前登陆页面，提示用户信息错误
                    JOptionPane.showMessageDialog(frame,"登陆失败",
                            "提示信息",JOptionPane.ERROR_MESSAGE);

                }
            }
        });
    }

    public static void main(String[] args) {
        userLogin login = new userLogin();
    }
}
