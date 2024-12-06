package com.lfh.touch.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 好友申请表
 * @TableName friendship
 */
@TableName(value ="friendship")
@Data
public class Friendship implements Serializable {
    /**
     * 唯一主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 请求发送者
     */
    private Integer senderId;

    /**
     * 请求接收者
     */
    private Integer receiverId;

    /**
     * 好友关系的状态，比如请求中 0、已接受 1、已拒绝 2
     */
    private Integer status;

    /**
     * 好友申请时间
     */
    private Date createTime;

    /**
     * 处理请求的时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}