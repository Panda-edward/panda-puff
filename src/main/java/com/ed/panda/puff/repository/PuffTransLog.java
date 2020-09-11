package com.ed.panda.puff.repository;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author : Edward
 * @date : 2020/9/8 上午12:16
 */
@Data
@Accessors(chain = true)
public class PuffTransLog {

    /**
     * 自增主键
     */
    private Long id;

    /**
     * 业务唯一标识
     */
    private String puffBizId;

    /**
     * 业务类型
     */
    private int bizType;

    /**
     * 当次调用的真实参数（json格式）
     */
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
