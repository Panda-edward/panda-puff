package com.ed.panda.puff.repository;

import java.util.Date;
import java.util.List;

/**
 * @Author: Edward
 * @Date: 2019/11/27 下午11:58
 *
 * <p>
 * TransLog持久化接口
 */
public interface ITransLogRepository {

    /**
     * 保存事务日志
     *
     * @param transLog
     * @return
     */
    boolean saveTransLog(PuffTransLog transLog);

    /**
     * 更新PuffTransLog
     */
    boolean updateTransLog(String puffBizId, Integer targetStatus, Integer sourceStatus,
                           int retryCount, Date nextRetryTime);

    /**
     * 计算下次重试时间
     * @param transLog
     * @return
     */
    Date calculateNextRetryTime(PuffTransLog transLog);

    /**
     * 根据bizId,查询事务日志
     *
     * @param puffBizId
     * @return
     */
    PuffTransLog getTransLog(String puffBizId);

    /**
     * 查询需要重试的log
     *
     * @param count
     * @return
     */
    List<PuffTransLog> queryRetryTranLogs(int count);


}
