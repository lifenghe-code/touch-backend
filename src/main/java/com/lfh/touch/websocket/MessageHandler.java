package com.lfh.touch.websocket;

import cn.hutool.json.JSONUtil;
import com.lfh.touch.constant.MessageConstant;
import com.lfh.touch.mapper.ChatDetailMapper;
import com.lfh.touch.model.domain.ChatDetail;
import com.lfh.touch.model.dto.user.UserAddFriendRequest;
import com.lfh.touch.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
@Slf4j
public class MessageHandler{
    public static void handleFriendAddRequest(Session toSession, UserAddFriendRequest userAddFriendRequest){
        String jsonString = JSONUtil.toJsonStr(userAddFriendRequest);
        toSession.getAsyncRemote().sendText(jsonString);
    }

    public static void sendToOne(Session fromSession, Session toSession, ChatDetail chatDetail) {
        // 通过sid查询map中是否存在
        Integer toUid = chatDetail.getReceiverUid();
        String message = chatDetail.getContent();
        if (toSession == null) {
            log.error("服务端给客户端发送消息 ==> toSid = {} 不存在, message = {}", toUid, message);
            return;
        }
        // 异步发送
        ChatDetailMapper chatDetailMapper = SpringContextUtils.getBean(ChatDetailMapper.class);
        log.info("--------------", chatDetailMapper.toString());
        int insert = chatDetailMapper.insert(chatDetail);
        chatDetail.setId(insert);
        log.info("服务端给客户端发送消息 ==> toSid = {}, message = {}", toUid, message);
        String jsonString = JSONUtil.toJsonStr(chatDetail);
        toSession.getAsyncRemote().sendText(jsonString);
        fromSession.getAsyncRemote().sendText(jsonString);
    }
}
