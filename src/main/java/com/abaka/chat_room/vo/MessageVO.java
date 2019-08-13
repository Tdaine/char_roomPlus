package com.abaka.chat_room.vo;

import lombok.Data;

/**
 * 服务器与客户端传递信息载体
 * @author abaka
 * @date 2019/8/13 10:26
 */

@Data
public class MessageVO {
    /**
     * 表示告知服务器要进行的动作，1表示用户注册，2表示私聊
     */
    private String type;
    /**
     * 发送到服务器的具体内容
     */
    private String content;
    /**
     * 私聊告知要发送给那个用户
     */
    private String to;
}
