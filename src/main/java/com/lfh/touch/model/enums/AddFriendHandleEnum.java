package com.lfh.touch.model.enums;

/**
 * @author lifh
 */
public enum AddFriendHandleEnum {
    /**
     * 同意申请
     */
    APPROVE("approve",1),

    /**
     * 拒绝申请
     */

    REJECT("reject",2);

    private final String text;

    private final Integer value;

    AddFriendHandleEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
