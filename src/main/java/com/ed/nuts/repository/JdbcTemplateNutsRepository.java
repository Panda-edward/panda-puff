package com.ed.nuts.repository;

import com.alibaba.fastjson.JSON;
import lombok.Setter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * @author : Edward
 * @date : 2021/1/20 上午11:07
 */
@Setter
public class JdbcTemplateNutsRepository implements INutsRepository {

    private JdbcTemplate jdbcTemplate;

    /**
     * insert
     *
     * @param po
     * @return
     */
    @Override
    public boolean save(NutsRetryRecord po) {
        System.out.println(JSON.toJSONString(po));
        return true;
    }

    /**
     * update
     *
     * @param id
     * @param nextRetryTime
     * @param status
     */
    @Override
    public boolean updateStatus(Long id, Long nextRetryTime, Integer status) {
        return true;
    }

    /**
     * query
     *
     * @param count
     * @return
     */
    @Override
    public List<NutsRetryRecord> queryRetries(int count) {
        return null;
    }
}
