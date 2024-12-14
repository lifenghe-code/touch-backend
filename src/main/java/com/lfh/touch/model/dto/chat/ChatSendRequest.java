package com.lfh.touch.model.dto.chat;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChatSendRequest implements Serializable {
    Integer chatId;
    Integer senderUid;
    Integer receiverUid;
    String content;

    private static final long serialVersionUID = 1L;
}
