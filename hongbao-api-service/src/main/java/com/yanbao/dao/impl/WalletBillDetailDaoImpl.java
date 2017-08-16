package com.yanbao.dao.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.yanbao.dao.WalletBillDetailDao;
import com.yanbao.mapper.WalletBillDetailMapper;
import com.mall.model.WalletBillDetail;
/**
 * <p>Title:WalletBillDetailDaoImpl</p>
 * <p>Description:店铺收益详细DAO实现类</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: 炎宝科技</p>
 * @author Pay - 1091945691@qq.com
 * @version v1.0 2017-06-05
 */
@Repository
public class WalletBillDetailDaoImpl implements WalletBillDetailDao {
	@Autowired
	private WalletBillDetailMapper walletBillDetailMapper;
	
	@Override
	public List<WalletBillDetail> getList(WalletBillDetail model)
			throws Exception {
		return walletBillDetailMapper.getList(model);
	}

	@Override
	public WalletBillDetail getById(String id) throws Exception {
		return walletBillDetailMapper.getById(id);
	}

	@Override
	public Integer delById(String id) throws Exception {
		return walletBillDetailMapper.delById(id);
	}

	@Override
	public Integer delInId(List<String> ids) throws Exception {
		return walletBillDetailMapper.delInId(ids);
	}

	@Override
	public Integer add(WalletBillDetail model) throws Exception {
		return walletBillDetailMapper.add(model);
	}

	@Override
	public Integer delPo(WalletBillDetail model) throws Exception {
		return walletBillDetailMapper.delPo(model);
	}
}