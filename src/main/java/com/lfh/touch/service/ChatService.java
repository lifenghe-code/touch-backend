package com.lfh.touch.service;
import com.lfh.touch.model.domain.Chat;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lfh.touch.model.domain.ChatDetail;
import com.lfh.touch.model.dto.chat.ChatMessageDeleteRequest;
import com.lfh.touch.model.dto.chat.ChatSendRequest;

import java.util.List;

/**
* @author li_fe
* @description 针对表【chat(聊天表)】的数据库操作Service
* @createDate 2024-12-03 19:28:07
*/
public interface ChatService extends IService<Chat> {
    /**
     * 获取好友会话列表
     * uid是聊天发起方，根据索引的最左匹配原则，查询时会走索引
     * @param uid 用户的UID
     */
    List<Chat> getChatByUid(Integer uid);

    /**
     * 返回聊天详情
     * @param chatId
     * @return
     */
    List<ChatDetail> getChatDetailById(Integer chatId);

    /**
     * 发送消息
     */
    boolean sendMessage(ChatSendRequest chatSendRequest);

    /**
     * 撤回消息
     * @param messageId
     * @return
     */
    boolean chatMessageWithdraw(Integer messageId);

    /**
     * 用户删除消息
     * @param chatMessageDeleteRequest
     * @return
     */
    boolean chatMessageDelete(ChatMessageDeleteRequest chatMessageDeleteRequest);

}
