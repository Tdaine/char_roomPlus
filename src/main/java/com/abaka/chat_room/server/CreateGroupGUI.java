package com.abaka.chat_room.server;

import com.abaka.chat_room.client.service.Connect2Server;
import com.abaka.chat_room.util.CommUtils;
import com.abaka.chat_room.vo.MessageVO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author abaka
 * @date 2019/8/19 11:33
 */
public class CreateGroupGUI {
    private JPanel createGroupPanel;
    private JPanel friendLabelPanel;
    private JTextField groupNameText;
    private JButton conformBtn;

    private String myName;
    private Set<String> friends;
    private Connect2Server connect2Server;
    private FriendsList friendsList;

    public CreateGroupGUI(String myName,Set<String> friends,Connect2Server connect2Server,
                          FriendsList friendsList) {
        this.myName = myName;
        this.connect2Server = connect2Server;
        this.friends = friends;
        this.friendsList = friendsList;

        JFrame frame = new JFrame("创建群组");
        frame.setContentPane(createGroupPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        //将在线好友以checkBox展示到界面中
        friendLabelPanel.setLayout(new BoxLayout(friendLabelPanel,BoxLayout.Y_AXIS));
        Iterator<String> iterator = friends.iterator();

        while (iterator.hasNext()){
            String labelName = iterator.next();
            JCheckBox checkBox = new JCheckBox(labelName);
            friendLabelPanel.add(checkBox);
        }

        //点击提交按钮提交信息到服务端
        conformBtn.addActionListener(new ActionListener() {
            //Set<String> selectedFriends = new HashSet<>();

            @Override
            public void actionPerformed(ActionEvent e) {
                //1.判断哪些好友选中加入群聊
                Set<String> selectedFriends = new HashSet<>();
                Component[] comps = friendLabelPanel.getComponents();
                for (Component comp : comps) {
                    JCheckBox checkBox = (JCheckBox) comp;
                    if (checkBox.isSelected()) {
                        String labelName = checkBox.getText();
                        selectedFriends.add(labelName);
                    }
                }
                selectedFriends.add(myName);
                //2.获取群名输入框输入的群名
                String groupName = groupNameText.getText();
                if (selectedFriends.size() >= 3 && groupName.length() >= 1 && !groupName.contains(" ")){
                    //3.将群名+选中好友信息发送到服务端
                    //type:3
                    //content:groupName
                    //to:[user1,user2,user3]
                    MessageVO messageVO = new MessageVO();
                    messageVO.setType("3");
                    messageVO.setContent(groupName);
                    messageVO.setTo(CommUtils.object2Json(selectedFriends));
                    try {
                        PrintStream out = new PrintStream(connect2Server.getOut(),true,"UTF-8");
                        out.println(CommUtils.object2Json(messageVO));
                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();
                    }

                    //4.将当前创建群的界面隐藏，刷新好友列表界面的群列表
                    frame.setVisible(false);
                    //addGroupInfo
                    //loadGroup
                    friendsList.addGroup(groupName,selectedFriends);
                    friendsList.loadGroupList();
                }else {
                    JOptionPane.showMessageDialog(frame,"群聊成员必须大于2人，群名必须填写并且群名不能包含空格",
                            "提示信息",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
