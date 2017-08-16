package com.yanbao.dao;

import com.mall.model.PartnerBill;

import java.util.List;

/**
 * @author jay.zheng
 * @date 2017年08月01日
 */
public interface PartnerBillDao {

    Integer create(PartnerBill model);

    PartnerBill readById(String id);

    List<PartnerBill> readList(PartnerBill model, Integer startRow, Integer pageSize);

    PartnerBill readOne(PartnerBill model);

    Integer readCount(PartnerBill model);

    Integer updateById(PartnerBill model, String id);

    Integer deleteById(String id);

    List<PartnerBill> getAllPartner(String startTime, String endTime);
}
