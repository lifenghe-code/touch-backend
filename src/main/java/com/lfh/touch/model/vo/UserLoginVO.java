package com.lfh.touch.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserLoginVO implements Serializable {
    /**
     * 用户ID
     */
    private Integer uid;

    /**
     * 用户账号
     */
    private String account;

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

    /**
     * 创建时间
     */
    private Date createTime;



    private static final long serialVersionUID = 1L;

}
