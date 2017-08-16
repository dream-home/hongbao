package com.yanbao.service;

import com.mall.model.GoodsWin;
import com.mall.model.PartnerBillDetail;
import com.yanbao.core.page.Page;
import com.yanbao.core.service.CommonService;

import java.util.List;

/**
 * Created by Administrator on 2017/8/2.
 */
public interface PartnerBillDetailService {

    List<PartnerBillDetail> readList(PartnerBillDetail model, Page page) throws Exception;

    Integer readCount(PartnerBillDetail model) throws Exception;

    Integer create(PartnerBillDetail model) throws Exception;

    Integer updateById(String id, PartnerBillDetail model) throws Exception;

    PartnerBillDetail readById(String id) throws Exception;

    Double deleteById(String id) throws Exception;

    /**
     * 创建ep业绩明细表
     */
    Integer createPartnerBillDetail(String storeId,String orderNo,Double score) throws Exception;

}
