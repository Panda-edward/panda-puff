package com.ed.nuts.repository;

import com.ed.nuts.repository.mapper.NutsMapper;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @author : Edward
 * @date : 2021/1/20 下午7:18
 */
@Setter
public class MybatisNutsRepository implements INutsRepository {

    private NutsMapper nutsMapper;

    /**
     * insert
     *
     * @param po
     * @return
     */
    @Override
    public boolean save(NutsRetryRecord po) {
        int ret = nutsMapper.insert(po);
        return ret == 1;
    }

    /**
     * update
     *
     * @param id
     * @param nextRetryTime
     * @param retryStatus
     */
    @Override
    public boolean updateStatus(Long id, Long nextRetryTime, Integer retryStatus) {
        int ret = nutsMapper.update(id, retryStatus, nextRetryTime, new Date());
        return ret == 1;
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
