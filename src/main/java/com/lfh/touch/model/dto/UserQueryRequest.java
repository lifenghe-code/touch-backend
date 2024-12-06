package com.lfh.touch.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lfh.touch.common.PageRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class UserQueryRequest extends PageRequest implements Serializable {
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


    private static final long serialVersionUID = 1L;
}
