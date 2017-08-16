package com.yanbao.dao;

import com.mall.model.PartnerBillDetail;
import com.yanbao.core.page.Page;
import com.yanbao.vo.GoodsSalesVo;

import java.util.List;

/**
 * Created by Administrator on 2017/8/2.
 */
public interface PartnerBillDetailDao {

    List<PartnerBillDetail> readList(PartnerBillDetail model,Page page) throws Exception;

    Integer readCount(PartnerBillDetail model) throws Exception;

    Integer create(PartnerBillDetail model) throws Exception;

    Integer updateById(String id, PartnerBillDetail model) throws Exception;

    PartnerBillDetail readById(String id) throws Exception;

    Double deleteById(String id) throws Exception;
}
