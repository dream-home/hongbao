package com.yanbao.dao.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.yanbao.dao.WalletBillDao;
import com.yanbao.mapper.WalletBillMapper;
import com.mall.model.WalletBill;
/**
 * <p>Title:WalletBillDaoImpl</p>
 * <p>Description:店铺收益DAO实现类</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: 炎宝科技</p>
 * @author Pay - 1091945691@qq.com
 * @version v1.0 2017-06-05
 */
@Repository
public class WalletBillDaoImpl implements WalletBillDao {
	
	@Autowired
	private WalletBillMapper walletBillMapper;
	
	@Override
	public List<WalletBill> getList(WalletBill model) throws Exception {
		return walletBillMapper.getList(model);
	}

	@Override
	public WalletBill getById(String id) throws Exception {
		return walletBillMapper.getById(id);
	}

	@Override
	public Integer delById(String id) throws Exception {
		return walletBillMapper.delById(id);
	}

	@Override
	public Integer delInId(List<String> ids) throws Exception {
		return walletBillMapper.delInId(ids);
	}

	@Override
	public Integer add(WalletBill model) throws Exception {
		return walletBillMapper.add(model);
	}

	@Override
	public Integer delPo(WalletBill model) throws Exception {
		return walletBillMapper.delPo(model);
	}
	
}
	