package com.lfh.touch.model.dto.user;

import lombok.Data;

import java.io.Serializable;
@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String account;

    private String password;

    private String checkPassword;
}
