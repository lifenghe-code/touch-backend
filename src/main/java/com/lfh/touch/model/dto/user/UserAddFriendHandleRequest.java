package com.lfh.touch.model.dto.user;

import lombok.Data;

@Data
public class UserAddFriendHandleRequest {
    /**
     * 发起请求ID
     */

    private Integer senderUid;

    /**
     * 查询UID
     */

    private Integer receiverUid;

    /**
     * 添加好友请求的结果，同意、拒绝
     */
    private Integer handle;


    private static final long serialVersionUID = 1L;
}
