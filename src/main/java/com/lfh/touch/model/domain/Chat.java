package com.lfh.touch.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
    private Integer senderUid;

    /**
     * 用户UID
     */
    private Integer receiverUid;

    /**
     * 是否移除聊天 0否 1是
     */
    private Integer isDelete;

    /**
     * 消息未读数量
     */
    private Integer unread;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最近接收消息的时间或最近打开聊天窗口的时间
     */
    private Date updateTime;

    /**
     * 0表示单聊，1表示群聊
     */
    private Integer type;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}