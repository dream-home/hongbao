package com.yanbao.dao.impl;

import com.mall.model.Ad;
import com.mall.model.PartnerBill;
import com.yanbao.dao.AdDao;
import com.yanbao.dao.PartnerBillDao;
import com.yanbao.mapper.AdMapper;
import com.yanbao.mapper.PartnerBillMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Repository
public class PartnerBillDaoImpl implements PartnerBillDao {

	@Autowired
	private PartnerBillMapper partnerBillMapper;

	@Override
	public Integer create(PartnerBill model) {
		return partnerBillMapper.create(model);
	}

	@Override
	public PartnerBill readById(String id) {
		return partnerBillMapper.readById(id);
	}

	@Override
	public List<PartnerBill> readList(PartnerBill model, Integer startRow, Integer pageSize) {
		return partnerBillMapper.readList(model,startRow,pageSize);
	}

	@Override
	public PartnerBill readOne(PartnerBill model) {
		return partnerBillMapper.readOne(model);
	}

	@Override
	public Integer readCount(PartnerBill model) {
		return partnerBillMapper.readCount(model);
	}

	@Override
	public Integer updateById(PartnerBill model, String id) {
		return partnerBillMapper.updateById(model,id);
	}

	@Override
	public Integer deleteById(String id) {
		return partnerBillMapper.deleteById(id);
	}

	@Override
	public List<PartnerBill> getAllPartner(String startTime, String endTime) {
		return partnerBillMapper.getAllPartner(startTime,endTime);
	}
}
