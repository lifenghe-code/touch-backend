package com.lfh.touch.model.dto.user;

import com.lfh.touch.constant.MessageConstant;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserAddFriendRequest implements Serializable {


    private String type= MessageConstant.FRIEND_ADD_REQUEST;
    /**
     * 发起请求ID
     */
    private Integer senderUid;


    /**
     * 请求的UID
     */

    private Integer receiverUid;



    private static final long serialVersionUID = 1L;
}
