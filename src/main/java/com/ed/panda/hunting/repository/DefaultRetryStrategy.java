package com.ed.panda.hunting.repository;

import java.util.Date;

/**
 * @author : Edward
 * @date : 2020/11/3 下午3:05
 */
public class DefaultRetryStrategy implements IRetryStrategy {

    /**
     * 计算下次重试时间：阶梯策略
     *
     * @param msgLog
     */
    @Override
    public Date calculateNextRetryTime(LocalMsgLog msgLog) {
        return new Date(msgLog.getNextRetryTime().getTime() + 5000 * (msgLog.getRetryCount() + 1));
    }
}
