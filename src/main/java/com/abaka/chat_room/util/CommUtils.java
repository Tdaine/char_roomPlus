package com.abaka.chat_room.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author abaka
 * @date 2019/8/9 10:52
 */
public class CommUtils {

    /**
     * 加载配置文件
     * @param fileName 要加载的配置文件名称
     * @return
     */
    public static Properties loadProperties(String fileName){
        Properties properties = new Properties();
        InputStream in = CommUtils.class.getClassLoader().getResourceAsStream(fileName);

        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
