package com.abaka.chat_room.server;

import com.abaka.chat_room.client.service.Connect2Server;
import com.abaka.chat_room.util.CommUtils;
import com.abaka.chat_room.vo.MessageVO;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Set;

/**
 * @author abaka
 * @date 2019/8/20 9:44
 */
public class GroupChatGUI {
    private JTextArea readFromServer;
    private JTextField send2Server;
    private JPanel groupPanel;
    private JPanel friendsPanel;

    private String groupName;
    private String myName;
    private Set<String> friends;
    private Connect2Server connect2Server;
    private JFrame frame;

    public GroupChatGUI(String groupName,Set<String> friends,String myName,Connect2Server connect2Server){
        this.groupName = groupName;
        this.connect2Server = connect2Server;
        this.friends = friends;
        this.myName = myName;
        frame = new JFrame(groupName );
        frame.setContentPane(groupPanel);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(400,400);
        frame.setVisible(true);
        //加载群中成员到好友列表
        friendsPanel.setLayout(new BoxLayout(friendsPanel,BoxLayout.Y_AXIS));
        Iterator<String> iterator = friends.iterator();
        while (iterator.hasNext()){
            String labelName = iterator.next();
            JLabel jLabel = new JLabel(labelName);
            friendsPanel.add(jLabel);
        }
        send2Server.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                StringBuilder sb = new StringBuilder();
                sb.append(send2Server.getText());
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    String str2Server = sb.toString();
                    //type:4
                    //content:myName-msg
                    //to:groupName
                    MessageVO messageVO = new MessageVO();
                    messageVO.setType("4");
                    messageVO.setContent(myName + "-" + str2Server);
                    messageVO.setTo(groupName);
                    try {
                        PrintStream out = new PrintStream(connect2Server.getOut(),true,"UTF-8");
                        out.println(CommUtils.object2Json(messageVO));
                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();
                    }
                    send2Server.setText("");
                }
            }
        });
    }

    public void readFromServer(String msg){
        readFromServer.append(msg + "    " + System.currentTimeMillis() + "\n");
    }

    public JFrame getFrame() {
        return frame;
    }
}
