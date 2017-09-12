package com.yanbao.dao;

import com.mall.model.PartnerBill;

import java.util.List;

/**
 * @author jay.zheng
 * @date 2017年08月01日
 */
public interface PartnerBillDao {

    Integer create(PartnerBill model);

    List<PartnerBill> getAllPartners(String startTime, String endTime);

    List<PartnerBill> getPartners(String startTime, String endTime);
}
