package com.abaka.chat_room.server;

import com.abaka.chat_room.util.CommUtils;
import com.abaka.chat_room.vo.MessageVO;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author abaka
 * @date 2019/8/13 10:29
 * 服务器端
 */
public class MultiThreadServer {

    private static final Integer PORT;
    private static final String IP;
    //缓存当前服务器注册的所有群名称以及群好友
    private static Map<String,Set<String>> groups = new ConcurrentHashMap<>();

    //缓存当前服务器所有在在线的客户端信息
    private static Map<String,Socket> clients = new ConcurrentHashMap<>();

    static {
        Properties properties = CommUtils.loadProperties("socket.properties");
        PORT = Integer.valueOf(properties.getProperty("port"));
        IP = properties.getProperty("ip");
    }

    public static class ExecuteClient implements Runnable{
        private Socket client;
        private Scanner in;
        private PrintStream out;

        public ExecuteClient(Socket client) {
            this.client = client;
            try {
                this.in = new Scanner(client.getInputStream());
                this.out = new PrintStream(client.getOutputStream(),true,"UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {

            while (true){
                if (in.hasNextLine()){
                    String jsonStrFromClient = in.nextLine();
                    MessageVO msgFromClient = (MessageVO) CommUtils.json2Object(jsonStrFromClient,MessageVO.class);
                    //新用户注册到服务端
                    if (msgFromClient.getType().equals("1")){
                        String username = msgFromClient.getContent();
                        //将当前在线的所有用户发回客户端
                        MessageVO msg2Client = new MessageVO();
                        msg2Client.setType("1");
                        msg2Client.setContent(CommUtils.object2Json(clients.keySet()));
                        out.println(CommUtils.object2Json(msg2Client));
                        //将新上线的用户信息发回给当前已在线的所有用户
                        sendUserLogin("newLogin:" + username);
                        //将新用户加到服务器缓冲区中
                        clients.put(username,client);
                        System.out.println(username + "上线了!");
                        System.out.println("当前聊天室共有" + clients.size() + "人");
                    }else if (msgFromClient.getType().equals("2")){
                        //用户私聊
                        //type:2
                        //content:myName-msg
                        //to:friendName
                        String friendName = msgFromClient.getTo();
                        Socket clientSocket = clients.get(friendName);
                        try {
                            PrintStream out = new PrintStream(clientSocket.getOutputStream(),
                                    true,"UTF-8");
                            MessageVO msg2Client = new MessageVO();
                            msg2Client.setType("2");
                            msg2Client.setContent(msgFromClient.getContent());
                            //System.out.println("收到私聊信息，内容为" + msgFromClient.getContent());
                            out.println(CommUtils.object2Json(msg2Client));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else if (msgFromClient.getType().equals("3")){
                        //注册群
                        String groupName = msgFromClient.getContent();
                        //该群的所有群成员
                        Set<String> friends = (Set<String>) CommUtils.json2Object(
                                msgFromClient.getTo(),Set.class);
                        groups.put(groupName,friends);
                        System.out.println("有新的群注册成功,群名称为"+groupName +
                                ",一共有" + groups.size() + "个群");
                    }
                }
            }

        }

        /**
         * 向所有在线用户发送用户上线信息
         * @param msg
         */
        private void sendUserLogin(String msg){
            for (Map.Entry<String,Socket> entry: clients.entrySet()){
                Socket socket = entry.getValue();
                try {
                    PrintStream out = new PrintStream(socket.getOutputStream(),true,"UTF-8");
                    out.println(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        ExecutorService executors = Executors.newFixedThreadPool(50);
        for (int i = 0; i < 50; i++){
            System.out.println("等待客户端连接...");
            Socket client = serverSocket.accept();
            System.out.println("客户端连接：" + client.getPort());
            executors.submit(new ExecuteClient(client));
        }
    }
}
