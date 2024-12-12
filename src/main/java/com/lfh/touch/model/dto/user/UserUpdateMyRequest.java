package com.lfh.touch.model.dto.user;

import lombok.Data;

@Data
public class UserUpdateMyRequest {
    /**
     * id
     */
    private Integer uid;
    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像url
     */
    private String avatar;

    /**
     * 主页背景图url
     */
    private String background;

    /**
     * 性别 0女 1男 2未知
     */
    private Integer gender;

    /**
     * 个性签名
     */
    private String description;


    /**
     * 角色类型 0普通用户 1管理员 2超级管理员
     */
    private Integer role;


    private static final long serialVersionUID = 1L;
}
