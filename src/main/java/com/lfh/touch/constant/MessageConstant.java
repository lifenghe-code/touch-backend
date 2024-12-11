package com.lfh.touch.constant;

/**
 * Netty实现通信的消息类型常量
 */
public interface MessageConstant {
    // 登录信息
    String LOGIN = "login";

    // 退出信息
    String LOGOUT = "logout";

    String MESSAGE = "message";

    String MESSAGE_RECALL = "message_recall";



    // 添加好友请求
    String FRIEND_ADD_REQUEST = "friend_add_request";

    // 添加好友请求通过
    String FRIEND_ADD_APPROVE = "friend_add_approve";

    // 添加好友请求拒绝
    String FRIEND_ADD_REJECT = "friend_add_reject";

    // 消息回复
    String FRIEND_RESPONSE = "friend_response";




}
