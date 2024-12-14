package com.lfh.touch.websocket;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.lfh.touch.constant.MessageConstant;
import com.lfh.touch.mapper.ChatDetailMapper;
import com.lfh.touch.mapper.UserMapper;
import com.lfh.touch.model.domain.ChatDetail;
import com.lfh.touch.model.domain.User;
import com.lfh.touch.model.dto.user.UserAddFriendRequest;
import com.lfh.touch.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
import java.util.Map;

@Slf4j
public class MessageHandler{
    public static void handleFriendAddRequest(Session toSession, UserAddFriendRequest userAddFriendRequest){
        UserMapper userMapper = SpringContextUtils.getBean(UserMapper.class);
        User user = userMapper.selectById(userAddFriendRequest.getSenderUid());
        String jsonString = JSONUtil.toJsonStr(user);
        toSession.getAsyncRemote().sendText(jsonString);
    }

    public static void sendToOne(String type, Session fromSession, Session toSession, ChatDetail chatDetail) {
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
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(chatDetail);
        stringObjectMap.put("type",type);
        String jsonString = JSONUtil.toJsonStr(stringObjectMap);

        toSession.getAsyncRemote().sendText(jsonString);
        fromSession.getAsyncRemote().sendText(jsonString);
    }
}
