package com.lfh.touch.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 聊天记录表
 * @TableName chat_detail
 */
@TableName(value ="chat_detail")
@Data
public class ChatDetail implements Serializable {
    /**
     * 唯一主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 消息发送者
     */
    private Integer userUid;

    /**
     * 消息接收者
     */
    private Integer receiverUid;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 发送者是否删除
     */
    private Integer senderDel;

    /**
     * 接受者是否删除
     */
    private Integer receiverDel;

    /**
     * 是否撤回
     */
    private Integer withdraw;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 消息发送时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}