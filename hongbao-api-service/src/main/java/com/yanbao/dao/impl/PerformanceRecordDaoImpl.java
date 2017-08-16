package com.yanbao.dao.impl;

import com.yanbao.dao.PerformanceRecordDao;
import com.yanbao.mapper.PerformanceMapper;
import com.mall.model.PerformanceRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author zzwei
 * @date 2017年06月21日
 */
@Repository
public class PerformanceRecordDaoImpl implements PerformanceRecordDao {

    @Autowired
    private PerformanceMapper performanceMapper;


    @Override
    public Integer add(PerformanceRecord model) throws Exception {
        return performanceMapper.add(model);
    }

}
