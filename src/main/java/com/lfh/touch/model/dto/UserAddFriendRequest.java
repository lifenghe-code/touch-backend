package com.lfh.touch.model.dto;

import lombok.Data;

@Data
public class UserAddFriendRequest {
    /**
     * 发起请求ID
     */

    private Integer senderUid;

    /**
     * 查询UID
     */

    private Integer uid;



    private static final long serialVersionUID = 1L;
}
