package com.lfh.touch.model.vo;

import java.io.Serializable;

public class UserInfoVO implements Serializable {
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
     * 个性签名
     */
    private String description;

    private static final long serialVersionUID = 1L;
}
