package com.lfh.touch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lfh.touch.common.ErrorCode;
import com.lfh.touch.exception.BusinessException;
import com.lfh.touch.mapper.ChatDetailMapper;
import com.lfh.touch.model.domain.ChatDetail;
import com.lfh.touch.model.dto.chat.ChatMessageDeleteRequest;
import com.lfh.touch.model.dto.chat.ChatSendRequest;
import com.lfh.touch.service.ChatService;
import com.lfh.touch.websocket.WebSocketServer;
import org.springframework.stereotype.Service;
import com.lfh.touch.mapper.ChatMapper;
import com.lfh.touch.model.domain.Chat;
import javax.annotation.Resource;
import java.util.List;

/**
* @author li_fe
* @description 针对表【chat(聊天表)】的数据库操作Service实现
* @createDate 2024-12-03 19:28:07
*/
@Service
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Chat>
    implements ChatService{
    @Resource
    ChatMapper chatMapper;
    @Resource
    ChatDetailMapper chatDetailMapper;
//    @Resource
//    WebSocketServer webSocketServer;
    @Override
    public List<Chat> getChatByUid(Integer uid) {
        QueryWrapper<Chat> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("sender_uid",uid).or().eq("receiver_uid",uid);

        List<Chat> list = this.list(queryWrapper);
        return list;
    }

    @Override
    public List<ChatDetail> getChatDetailById(Integer chatId) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("chat_id",chatId);
        List<ChatDetail> list = chatDetailMapper.selectList(queryWrapper);
        return list;
    }

    @Override
    public boolean sendMessage(ChatSendRequest chatSendRequest) {
        Integer chatId = chatSendRequest.getChatId();
        Integer senderUid = chatSendRequest.getSenderUid();
        Integer receiverUid = chatSendRequest.getReceiverUid();
        String message = chatSendRequest.getContent();
        // todo 消息未读数
        /*
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sender_uid",senderUid);
        queryWrapper.eq("receiver_uid",receiverUid);
        Chat chat = chatMapper.selectOne(queryWrapper);
        Integer unread = chat.getUnread();
        chat.setUnread(unread+1);
        int update = chatMapper.updateById(chat);
        if(!(update > 0)){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        */

        ChatDetail chatDetail = new ChatDetail();
        chatDetail.setSenderUid(senderUid);
        chatDetail.setReceiverUid(receiverUid);
        chatDetail.setContent(message);
        chatDetail.setChatId(chatId);
        int insert = chatDetailMapper.insert(chatDetail);
        if(!(insert > 0)){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        //webSocketServer.sendToOne(chatDetail);
        return true;
    }

    @Override
    public boolean chatMessageWithdraw(Integer messageId) {
        ChatDetail chatDetail = chatDetailMapper.selectById(messageId);
        chatDetail.setWithdraw(1);
        int update = chatDetailMapper.updateById(chatDetail);
        if(!(update > 0)){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return true;
    }

    @Override
    public boolean chatMessageDelete(ChatMessageDeleteRequest chatMessageDeleteRequest) {
        Integer messageId = chatMessageDeleteRequest.getMessageId();
        Integer delUid = chatMessageDeleteRequest.getDelUid();
        ChatDetail chatDetail = chatDetailMapper.selectById(messageId);
        if (delUid.equals(chatDetail.getSenderUid())){
            chatDetail.setSenderDel(delUid);
        }
        else {
            chatDetail.setReceiverDel(delUid);
        }
        int update = chatDetailMapper.updateById(chatDetail);
        if(!(update > 0)){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return true;
    }
}




