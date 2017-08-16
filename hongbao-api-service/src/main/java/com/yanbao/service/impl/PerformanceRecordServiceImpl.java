package com.yanbao.service.impl;

import com.yanbao.constant.StatusType;
import com.yanbao.dao.PerformanceRecordDao;
import com.mall.model.PerformanceRecord;
import com.yanbao.service.PerformanceRecordService;
import com.yanbao.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author zzwei
 * @date 2017年06月21日
 */
@Service
public class PerformanceRecordServiceImpl implements PerformanceRecordService {

    @Autowired
    private PerformanceRecordDao performanceRecordDao;


    @Override
    public Integer add(PerformanceRecord model) throws Exception {
        if (model == null) {
            return null;
        }
        setDefaultValue(model);
        return performanceRecordDao.add(model);
    }

    private void setDefaultValue(PerformanceRecord model) {
        model.setId(UUIDUtil.getUUID());
        model.setStatus(StatusType.TRUE.getCode());
        model.setCreateTime(new Date());
    }

    @Override
    public Integer create(String orderNo,Double consumeEp ,String userId,Integer recordType,String remark) throws Exception {
        //新增业绩处理记录
        PerformanceRecord performanceRecord=new PerformanceRecord();
        performanceRecord.setOrderNo(orderNo);
        performanceRecord.setConsumeEp(consumeEp);
        performanceRecord.setUserId(userId);
        performanceRecord.setRecordType(recordType);
        performanceRecord.setRemark(remark);
        return add(performanceRecord);
    }
}
