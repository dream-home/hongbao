package com.yanbao.service;

import com.mall.model.PerformanceRecord;

/**
 * @author zzwei
 * @date 2017年06月21日
 */
public interface PerformanceRecordService {

    Integer add(PerformanceRecord model) throws Exception;

    Integer create(String orderNo, Double consumeEp, String userId, Integer recordType, String remark) throws Exception;
}
