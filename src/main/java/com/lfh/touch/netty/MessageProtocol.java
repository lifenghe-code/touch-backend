package com.lfh.touch.netty;

import lombok.Data;

import java.io.Serializable;

@Data
/**
 * 一定要 implements Serializable
 */
public class MessageProtocol implements Serializable {
    private String type;    // 消息类型：FRIEND_REQUEST, FRIEND_RESPONSE 等
    private Integer senderId;
    private Integer receiverId;
    private String content;
    private long timestamp;

    // 构造器、getter和setter省略
    private static final long serialVersionUID = 1L;
}
