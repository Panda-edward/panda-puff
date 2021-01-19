package com.ed.panda.hunting.repository;

import java.util.Date;

/**
 * @author : Edward
 * @date : 2020/11/3 上午11:20
 */
public interface IRetryStrategy {

    /**
     * 计算下次重试时间
     * @param msgLog
     * @return
     */
    Date calculateNextRetryTime(LocalMsgLog msgLog);
}
