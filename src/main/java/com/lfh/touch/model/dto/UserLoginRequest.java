package com.lfh.touch.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginRequest implements Serializable {
    /**
     * 用户账号
     */
    private String account;

    /**
     * 用户密码
     */
    private String password;


}
