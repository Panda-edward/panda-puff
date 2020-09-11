package com.ed.panda.puff.internal;

import com.ed.panda.puff.common.PuffRuntimeException;
import com.ed.panda.puff.repository.ITransLogRepository;
import com.ed.panda.puff.repository.PuffTransLog;
import lombok.Setter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.List;

/**
 * @author : Edward
 * @date : 2020/9/11 上午11:28
 */
public class InternalTransLogRepository implements ITransLogRepository {

    @Setter
    private JdbcTemplate jdbcTemplate;

    private static String SELECT_LIST_SQL = "select * from puff_trans_log limit ?";

    private static String DELETE_SQL = "delete from puff_trans_log where id = ?";

    private static String UPDATE_SQL = "update puff_trans_log set retry_count = ?,next_retry_time=? where id = ?";

    /**
     * 保存事务日志
     *
     * @param transLog
     * @return
     */
    @Override
    public boolean saveTransLog(PuffTransLog transLog) {
        String sql = "insert into puff_trans_log(puff_biz_id,biz_type,params,status,retry_count,next_retry_time) values (?,?,?,?,?,?)";
        int ret = jdbcTemplate.update(sql, transLog.getPuffBizId(), transLog.getBizType(), transLog.getParams(), transLog.getStatus(), transLog.getRetryCount(), transLog.getNextRetryTime());
        if (ret != 1) {
            throw new PuffRuntimeException("to db,save local trans log fail");
        }
        return true;
    }

    /**
     * 更新PuffTransLog
     *
     * @param puffBizId
     * @param targetStatus
     * @param sourceStatus
     * @param retryCount
     * @param nextRetryTime
     */
    @Override
    public boolean updateTransLog(String puffBizId, Integer targetStatus, Integer sourceStatus, int retryCount, Date nextRetryTime) {
        String sql = "update puff_trans_log set status=?,retry_count=?,next_retry_time=? where id = ? and status = ?";
        int ret = jdbcTemplate.update(sql, targetStatus, retryCount, nextRetryTime, puffBizId, sourceStatus);
        if (ret != 1) {
            throw new PuffRuntimeException("to db,save local trans log fail");
        }
        return true;
    }

    /**
     * 查询需要重试的log
     *
     * @param count
     * @return
     */
    @Override
    public List<PuffTransLog> queryRetryTranLogs(int count) {
        String sql = "select * from puff_trans_log where status < 2 order by next_retry_time limit ?";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(PuffTransLog.class), count);
    }

    /**
     * 根据bizId,查询事务日志
     *
     * @param puffBizId
     * @return
     */
    @Override
    public PuffTransLog getTransLog(String puffBizId) {
        String sql = "select * from puff_trans_log where id = ?";
        return jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(PuffTransLog.class), puffBizId);
    }

    /**
     * 计算下次重试时间
     *
     * @param transLog
     * @return
     */
    @Override
    public Date calculateNextRetryTime(PuffTransLog transLog) {
        return new Date(transLog.getNextRetryTime().getTime() + 1000 * 30);
    }
}
