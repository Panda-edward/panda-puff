package com.ed.panda.hunting.repository;

import java.util.Date;
import java.util.List;

/**
 * @author : Edward
 * @date : 2020/11/2 下午7:28
 */
public interface ILocalMsgLogRepository {

    /**
     * 保存本地消息日志
     *
     * @param localMsgLog
     * @return
     */
    boolean saveLocalMsgLog(LocalMsgLog localMsgLog);

    /**
     * 更新本地消息日志
     *
     * @param id
     * @param retryCount
     * @param nextRetryTime
     * @param status
     * @return
     */
    boolean updateLocalMsgLog(Long id, int retryCount, Date nextRetryTime, Integer status);

    /**
     * 查询需要重试的logs
     *
     * @param count
     * @return
     */
    List<LocalMsgLog> queryRetryLogs(int count);
}
