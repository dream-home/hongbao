package com.yanbao.service.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mall.model.PartnerBill;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.page.Page;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.PartnerBillMapper;
import com.yanbao.service.PartnerBillService;
/**
 * @author Pay - 1091945691@qq.com
 */
@Service
public class PartnerBillServiceImpl extends CommonServiceImpl<PartnerBill> implements PartnerBillService  {
	/**注入合伙人业绩表DAO接口类*/
	@Autowired
    private PartnerBillMapper dao;

	@Override
	protected CommonDao<PartnerBill> getDao() {
		return dao;
	}

	@Override
	protected Class<PartnerBill> getModelClass() {
		return PartnerBill.class;
	}

	@Override
	public List<PartnerBill> getBySearch(Page page, PartnerBill pb)
			throws Exception {
		return dao.getBySearch(page, pb);
	}
}