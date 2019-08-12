package com.abaka.chat_room.client.entity;

import lombok.Data;

/**
 * @author abaka
 * @date 2019/8/12 9:59
 * 实体类
 */
@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private String brief;
}
