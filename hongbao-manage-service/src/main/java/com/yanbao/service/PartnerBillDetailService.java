package com.yanbao.service;

import com.mall.model.PartnerBillDetail;
import com.yanbao.core.service.CommonService;

/**
 * Created by Administrator on 2017/8/2.
 */
public interface PartnerBillDetailService extends CommonService<PartnerBillDetail> {

    /**
     * 创建ep业绩明细表
     */
    Integer createPartnerBillDetail(String storeId, String orderNo, Double score) throws Exception;

}
