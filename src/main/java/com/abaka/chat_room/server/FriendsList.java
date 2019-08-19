package com.abaka.chat_room.server;

import com.abaka.chat_room.client.service.Connect2Server;
import com.abaka.chat_room.util.CommUtils;
import com.abaka.chat_room.vo.MessageVO;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author abaka
 * @date 2019/8/13 15:08
 */
public class FriendsList {
    private JPanel friendsPanel;
    private JScrollPane friendsList;
    private JScrollPane groupListPanel;
    private JButton createGroupBtn;

    private JFrame frame;
    private String username;
    //存储所有在线好友
    private Set<String> users;
    private Connect2Server connect2Server;
    //保存所有私聊界面
    private Map<String,PrivateChatGUI> privateChatGUIList = new ConcurrentHashMap<>();

    //储存所有群名称以及群好友
    private Map<String,Set<String>> groupList = new ConcurrentHashMap<>();

    //好友列表后台任务，不断监听服务器发来的消息
    //好友上线信息，用户私聊、群聊
    private class DaemonTask implements Runnable{
        private Scanner in = new Scanner(connect2Server.getIn());
        @Override
        public void run() {
            while (true){
                //收到服务器发来的消息
                if (in.hasNextLine()){
                    String strFromServer = in.nextLine();
                    //此时服务器发来的是一个json字符串
                    if (strFromServer.startsWith("{")){
                        MessageVO messageVO = (MessageVO) CommUtils.json2Object(strFromServer,
                                MessageVO.class);
                        if (messageVO.getType().equals("2")){
                            //服务器发来的私聊信息
                            String friendName = messageVO.getContent().split("-")[0];
                            String msg = messageVO.getContent().split("-")[1];
                            //判断私聊是否是第一次创建
                            if (privateChatGUIList.containsKey(friendName)){
                                //通过用户名获取聊天界面
                                PrivateChatGUI privateChatGUI = privateChatGUIList.get(friendName);
                                privateChatGUI.getFrame().setVisible(true);
                                //将对方说的话添加到私聊界面上
                                privateChatGUI.readFromServer(friendName + "说:" + msg);
                            }else {
                                PrivateChatGUI privateChatGUI = new PrivateChatGUI(friendName,
                                        username,connect2Server);
                                privateChatGUI.readFromServer(friendName + "说:" + msg);
                            }

                        }
                    }else {
                        //newLogin:userName
                        if (strFromServer.startsWith("newLogin:")){
                            String newFriendName = strFromServer.split(":")[1];
                            users.add(newFriendName);
                            //弹框提示用户上线
                            JOptionPane.showMessageDialog(frame,newFriendName + "上线了！",
                                    "上线提醒",JOptionPane.INFORMATION_MESSAGE);
                            //刷新好友列表
                            loadUsers();
                        }
                    }
                }
            }
        }
    }
    //标签点击事件,创建私聊窗口
    private class PrivateLabelAction implements MouseListener{

        private String labelName;

        public PrivateLabelAction(String labelName) {
            this.labelName = labelName;
        }

        //鼠标点击执行事件
        @Override
        public void mouseClicked(MouseEvent e) {
            //判断好友列表私聊界面缓存是否已经有指定标签
            if (privateChatGUIList.containsKey(labelName)){
                PrivateChatGUI privateChatGUI = privateChatGUIList.get(labelName);
                privateChatGUI.getFrame().setVisible(true);
            }else {
                //第一次点击，创建私聊界面
                PrivateChatGUI privateChatGUI = new PrivateChatGUI(
                        labelName,username,connect2Server
                );
                //将新创建的私聊窗口保存到Map中
                privateChatGUIList.put(labelName,privateChatGUI);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    //标签点击事件，打开群聊窗口
    private class GroupLabelAction implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    public FriendsList(String username,Set<String> users,Connect2Server connect2Server){
        this.username = username;
        this.users = users;
        this.connect2Server = connect2Server;
        frame = new JFrame(username);
        frame.setContentPane(friendsPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        loadUsers();
        //启动后台线程不断监听服务器发来的消息
        Thread daemonThread = new Thread(new DaemonTask());
        daemonThread.setDaemon(true);
        daemonThread.start();
        createGroupBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CreateGroupGUI(username,users,connect2Server,
                        FriendsList.this);
            }
        });
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
            //添加标签点击事件
            userLabels[i].addMouseListener(new PrivateLabelAction(userName));
            friends.add(userLabels[i]);
            i++;
        }
        friendsList.setViewportView(friends);
        //设置滚动条垂直滚动
        friendsList.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        friends.revalidate();
        friendsList.revalidate();
    }

    public void loadGroupList(){
        //存储所有群名称标签Jpanel
        JPanel groupNamePanel = new JPanel();
        //设置窗口标签排放为竖向
        groupNamePanel.setLayout(new BoxLayout(groupNamePanel,
                BoxLayout.Y_AXIS));
        JLabel[] labels = new JLabel[groupList.size()];
        //Map遍历
        Set<Map.Entry<String,Set<String>>> entries = groupList.entrySet();
        Iterator<Map.Entry<String,Set<String>>> iterator = entries.iterator();
        int i = 0;
        while (iterator.hasNext()){
            Map.Entry<String,Set<String>> entry = iterator.next();
            labels[i] = new JLabel(entry.getKey());
            groupNamePanel.add(labels[i]);
            i++;
        }
        groupListPanel.setViewportView(groupNamePanel);
        groupListPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        groupListPanel.revalidate();
    }

    public void addGroup(String groupName,Set<String> friends){
        groupList.put(groupName,friends);
    }
}
