package com.lfh.touch.controller;

import com.lfh.touch.common.BaseResponse;
import com.lfh.touch.common.ErrorCode;
import com.lfh.touch.common.ResultUtils;
import com.lfh.touch.exception.BusinessException;
import com.lfh.touch.model.domain.Chat;
import com.lfh.touch.model.domain.ChatDetail;
import com.lfh.touch.model.dto.chat.ChatSendRequest;
import com.lfh.touch.model.dto.user.UserRegisterRequest;
import com.lfh.touch.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping("chat")
@RestController
@Slf4j
public class ChatController {
    @Resource
    ChatService chatService;

    @GetMapping("/get-chat/{uid}")
    public BaseResponse<List<Chat>> getChatByUid(@PathVariable("uid") Integer uid){
        List<Chat> chatById = chatService.getChatByUid(uid);
        return ResultUtils.success(chatById);
    }
    @GetMapping("/get-chat-detail/{chatId}")
    public BaseResponse<List<ChatDetail>> getChatDetail(@PathVariable("chatId") Integer chatId){
        List<ChatDetail> chatById = chatService.getChatDetailById(chatId);
        return ResultUtils.success(chatById);
    }

    /**
     * 发送消息
     * @param chatSendRequest
     * @return
     */
    @PostMapping("/send")
    public BaseResponse<Boolean> chatSend(@RequestBody ChatSendRequest chatSendRequest) {
        if (chatSendRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = chatService.sendMessage(chatSendRequest);
        return ResultUtils.success(b);
    }
    /**
     * 撤回消息
     */
    @PostMapping("/withdraw")
    public BaseResponse<Boolean> chatMessageWithdraw(Integer messageId) {
        // messageId 要撤回的消息
        boolean b = chatService.chatMessageWithdraw(messageId);
        return ResultUtils.success(true);
    }
    @PostMapping("/message-delete")
    public BaseResponse<Boolean> chatMessageDelete(Integer messageId) {
        // messageId 要撤回的消息
        boolean b = chatService.chatMessageWithdraw(messageId);
        return ResultUtils.success(true);
    }
}
