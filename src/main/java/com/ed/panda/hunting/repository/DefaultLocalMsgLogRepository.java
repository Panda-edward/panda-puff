package com.ed.panda.hunting.repository;

import lombok.Setter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

/**
 * @author : Edward
 * @date : 2020/11/3 下午3:12
 */
@Setter
public class DefaultLocalMsgLogRepository implements ILocalMsgLogRepository {

    private JdbcTemplate jdbcTemplate;

    private static String INSERT_SQL = "insert into local_msg_log(biz_type, biz_serial_no, params, status,retry_count,next_retry_time) values (?,?,?,?,?,?)";

    private static String UPDATE_SQL = "update local_msg_log set retry_count = ?,next_retry_time=?,status=? where id=? and status < 2";

    private static String QUERY_SQL = "select * from local_msg_log where next_retry_time <= ? and status < 2 order by next_retry_time limit ?";


    /**
     * 保存事务日志
     *
     * @param localMsgLog
     * @return
     */
    @Override
    public boolean saveLocalMsgLog(LocalMsgLog localMsgLog) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rows = jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, localMsgLog.getBizType());
            ps.setString(2, localMsgLog.getBizSerialNo());
            ps.setString(3, localMsgLog.getParams());
            ps.setInt(4, localMsgLog.getStatus());
            ps.setInt(5, localMsgLog.getRetryCount());
            ps.setDate(6, new java.sql.Date(localMsgLog.getNextRetryTime().getTime()));
            return ps;
        }, keyHolder);
        localMsgLog.setId(keyHolder.getKey().longValue());
        return rows > 0;
    }

    /**
     * 更新localTxLog
     *
     * @param id
     * @param retryCount
     * @param nextRetryTime
     * @param status
     */
    @Override
    public boolean updateLocalMsgLog(Long id, int retryCount, Date nextRetryTime, Integer status) {
        int rows = jdbcTemplate.update(UPDATE_SQL, retryCount, nextRetryTime, status, id);
        return rows == 1;
    }

    /**
     * 查询需要重试的log
     *
     * @param count
     * @return
     */
    @Override
    public List<LocalMsgLog> queryRetryLogs(int count) {
        return jdbcTemplate.query(QUERY_SQL, new RowMapper<LocalMsgLog>() {
            @Override
            public LocalMsgLog mapRow(ResultSet rs, int rowNum) throws SQLException {
                LocalMsgLog log = new LocalMsgLog();
                log.setId(rs.getLong("id"));
                log.setBizSerialNo(rs.getString("biz_serial_no"));
                log.setBizType(rs.getString("biz_type"));
                log.setParams(rs.getString("params"));
                log.setStatus(rs.getInt("status"));
                log.setNextRetryTime(new Date(rs.getDate("next_retry_time").getTime()));
                log.setRetryCount(rs.getInt("retry_count"));
                return log;
            }
        }, new Date(),count);
    }
}
