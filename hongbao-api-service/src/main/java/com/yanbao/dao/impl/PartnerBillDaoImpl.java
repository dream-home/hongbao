package com.yanbao.dao.impl;


import com.mall.model.PartnerBill;
import com.yanbao.dao.PartnerBillDao;
import com.yanbao.mapper.PartnerBillMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Repository
public class PartnerBillDaoImpl implements PartnerBillDao {

	@Resource
	private PartnerBillMapper partnerSumBillMapper;

	@Override
	public Integer create(PartnerBill model) {
		return partnerSumBillMapper.create(model);
	}

	@Override
	public List<PartnerBill> getAllPartners(String startTime, String endTime) {
		System.out.println(startTime);
		System.out.println(endTime);
		return partnerSumBillMapper.getAllPartners(startTime,endTime);
	}

	@Override
	public List<PartnerBill> getPartners(String startTime, String endTime) {
		System.out.println(startTime);
		System.out.println(endTime);
		return partnerSumBillMapper.getPartners(startTime,endTime);
	}
}
