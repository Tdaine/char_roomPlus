package com.abaka.chat_room.client.service;

import com.abaka.chat_room.util.CommUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Properties;

/**
 * @author abaka
 * @date 2019/8/13 10:37
 */
public class Connect2Server {
    private static final Integer PORT;
    private static final String IP;
    static {
        Properties properties = CommUtils.loadProperties("socket.properties");
        PORT = Integer.valueOf(properties.getProperty("port"));
        IP = properties.getProperty("ip");
    }

    private Socket client;
    private InputStream in;
    private OutputStream out;

    public Connect2Server() {
        try {
            client = new Socket(IP,PORT);
            in = client.getInputStream();
            out = client.getOutputStream();
        } catch (IOException e) {
            System.out.println("连接服务器失败");
            e.printStackTrace();
        }
    }

    public InputStream getIn() {
        return in;
    }

    public OutputStream getOut() {
        return out;
    }
}
