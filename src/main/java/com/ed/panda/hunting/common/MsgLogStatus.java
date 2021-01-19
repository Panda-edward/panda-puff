package com.ed.panda.hunting.common;

/**
 * @author : Edward
 * @date : 2020/11/2 下午7:47
 */
public enum MsgLogStatus {

    INIT(0, "初始化"),

    FAIL(1, "失败"),

    SUCCESS(2, "成功"),
    ;


    private int status;

    private String desc;

    MsgLogStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
}
