package com.lfh.touch.model.dto.chat;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChatMessageDeleteRequest implements Serializable {

    Integer delUid;
    Integer messageId;

    private static final long serialVersionUID = 1L;
}
