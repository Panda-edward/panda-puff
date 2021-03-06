package com.ed.panda.puff.common.enums;

/**
 * @author : Edward
 * @date : 2020/9/9 下午4:32
 */
public enum TransLogStatusEnum {

    INIT(0, "初始化"),
    FAIL(1, "失败"),
    SUCCESS(2, "成功"),
    ;

    private int status;

    private String msg;

    TransLogStatusEnum(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }
}
