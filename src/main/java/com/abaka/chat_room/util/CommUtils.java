package com.abaka.chat_room.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author abaka
 * @date 2019/8/9 10:52
 */
public class CommUtils {

    private static final Gson GSON = new GsonBuilder().create();

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

    /**
     * 将任意对象序列化为json字符串
     * @param obj
     * @return
     */
    public static String object2Json(Object obj){
        return GSON.toJson(obj);
    }

    /**
     * 将json反序列化为指定对象
     * @param jsonStr
     * @param object
     * @return
     */
    public static Object json2Object(String jsonStr,Class object){
        return GSON.fromJson(jsonStr,object);
    }
}
