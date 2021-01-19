package com.ed.panda.hunting.repository;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author : Edward
 * @date : 2020/11/2 下午6:13
 */
@Data
@Accessors(chain = true)
public class LocalMsgLog {

    private Long id;

    /**
     * 业务类型
     * */
    private String bizType;

    /**
     * 业务序列号标识
     * */
    private String bizSerialNo;

    /**
     * 调用参数（json）
     * */
    private String params;

    /**
     * 状态
     */
    private int status;

    /**
     * 重试次数
     */
    private int retryCount;

    /**
     * 下次重试时间
     */
    private Date nextRetryTime;
}
