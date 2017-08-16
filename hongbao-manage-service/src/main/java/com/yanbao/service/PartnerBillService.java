package com.yanbao.service;

import java.util.List;

import com.mall.model.PartnerBill;
import com.yanbao.core.page.Page;
import com.yanbao.core.service.CommonService;

/**
 * @author Pay - 1091945691@qq.com
 */
public interface PartnerBillService extends CommonService<PartnerBill>{
	List<PartnerBill> getBySearch(Page page,PartnerBill pb)throws Exception;
}