package com.abaka.chat_room.server;

import com.abaka.chat_room.client.service.Connect2Server;
import com.abaka.chat_room.util.CommUtils;
import com.abaka.chat_room.vo.MessageVO;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 * @author abaka
 * @date 2019/8/19 9:51
 */
public class PrivateChatGUI {
    private JPanel PrivateChatPanel;
    private JTextArea readFromServer;
    private JTextField sendToServer;

    private String friendName;
    private String myName;
    private Connect2Server connect2Server;
    private JFrame frame;
    private PrintStream out;

    public PrivateChatGUI(String friendName, String myName, Connect2Server connect2Server) {
        this.friendName = friendName;
        this.myName = myName;
        this.connect2Server = connect2Server;
        try {
            out = new PrintStream(connect2Server.getOut(),true,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        frame = new JFrame("与"+friendName + "聊天中...");
        frame.setContentPane(PrivateChatPanel);
        //设置窗口关闭的操作，将其设置为隐藏
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(400,400);
        frame.setVisible(true);

        //捕捉输入框的键盘输入
        sendToServer.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                StringBuilder sb = new StringBuilder();
                sb.append(sendToServer.getText());
                //1.当捕捉到按下Enter
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    //2.将当前信息发送到服务端
                    String msg = sb.toString();
                    MessageVO messageVO = new MessageVO();
                    messageVO.setType("2");
                    messageVO.setContent(myName + "-" + msg);
                    messageVO.setTo(friendName);
                    PrivateChatGUI.this.out.println(CommUtils.object2Json(messageVO));
                    //3.将自己发送的信息展示到当前私聊页面
                    readFromServer(myName + "说:" + msg);
                    sendToServer.setText("");
                }
            }
        });
    }
    //将自己输出的msg添加到聊天页面上
    public void readFromServer(String msg){
        readFromServer.append(msg + "    " + System.currentTimeMillis() + "\n");
    }

    public JFrame getFrame() {
        return frame;
    }

}
