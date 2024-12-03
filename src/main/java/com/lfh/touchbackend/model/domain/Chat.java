package com.lfh.touchbackend.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 聊天表
 * @TableName chat
 */
@TableName(value ="chat")
@Data
public class Chat implements Serializable {
    /**
     * 唯一主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 对象UID
     */
    private Integer userId;

    /**
     * 用户UID
     */
    private Integer anotherId;

    /**
     * 是否移除聊天 0否 1是
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 消息未读数量
     */
    private Integer unread;

    /**
     * 最近接收消息的时间或最近打开聊天窗口的时间
     */
    private Date latestTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}