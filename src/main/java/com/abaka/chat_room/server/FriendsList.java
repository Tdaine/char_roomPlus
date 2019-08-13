package com.abaka.chat_room.server;

import com.abaka.chat_room.client.service.Connect2Server;

import javax.swing.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author abaka
 * @date 2019/8/13 15:08
 */
public class FriendsList {
    private JPanel friendsPanel;
    private JScrollPane friendsList;
    private JScrollPane groupList;
    private JButton createGroupBtn;

    private JFrame frame;

    private String username;
    private Set<String> users;
    private Connect2Server connect2Server;

    public FriendsList(String username,Set<String> users,Connect2Server connect2Server){
        this.username = username;
        this.users = users;
        this.connect2Server = connect2Server;
        frame = new JFrame(username);
        frame.setContentPane(friendsPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,300);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
        loadUsers();
    }

    //加载所有在线的用户信息
    public void loadUsers(){
        JLabel[] userLabels = new JLabel[users.size()];
        JPanel friends = new JPanel();
        friends.setLayout(new BoxLayout(friends,BoxLayout.Y_AXIS));
        //set遍历
        Iterator<String> iterator = users.iterator();
        int i = 0;
        while (iterator.hasNext()){
            String userName = iterator.next();
            userLabels[i] = new JLabel(userName);
            friends.add(userLabels[i]);
            i++;
        }
        friendsList.setViewportView(friends);
        //设置滚动条垂直滚动
        friendsList.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        friends.revalidate();
        friendsList.revalidate();
    }


}
