package com.yanbao.service;

import com.mall.model.PartnerBill;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author jay.zheng
 * @date 2017年08月01日
 */
public interface PartnerBillService {

    Integer create(PartnerBill model) throws Exception;

    List<PartnerBill> getAllPartner(String startTime, String endTime);

    //结算业绩（提现+商家赠送EP）
    @Transactional
    void inPartnerStatistics(String startTime, String endTime) throws Exception;
}
