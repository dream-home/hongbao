package com.yanbao.dao.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.yanbao.dao.PayCallbackDao;
import com.yanbao.mapper.PayCallbackMapper;
import com.mall.model.PayCallback;
/**
 * <p>Title:PayCallbackDaoImpl</p>
 * <p>Description:支付回调记录DAO实现类</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: 炎宝科技</p>
 * @author Pay - 1091945691@qq.com
 * @version v1.0 2017-05-26
 */
@Repository
public class PayCallbackDaoImpl implements PayCallbackDao {
	@Autowired
	private PayCallbackMapper payCallbackMapper;

	@Override
	public Integer add(PayCallback model) throws Exception {
		return payCallbackMapper.add(model);
	}

	@Override
	public Integer del(PayCallback model) throws Exception {
		return payCallbackMapper.del(model);
	}

	@Override
	public Integer delByPk(String id) throws Exception {
		return payCallbackMapper.delByPk(id);
	}

	@Override
	public Integer delList(List<String> ids) throws Exception {
		return payCallbackMapper.delList(ids);
	}

	@Override
	public Integer modUpdate(PayCallback model) throws Exception {
		return payCallbackMapper.modUpdate(model);
	}

	@Override
	public Integer modUpdateNotNull(PayCallback model) throws Exception {
		return payCallbackMapper.modUpdateNotNull(model);
	}

	@Override
	public PayCallback getByPk(String id) throws Exception {
		return payCallbackMapper.getByPk(id);
	}

	@Override
	public Long getCount(PayCallback model) throws Exception {
		return payCallbackMapper.getCount(model);
	}

	@Override
	public List<PayCallback> getList(PayCallback model) throws Exception {
		return payCallbackMapper.getList(model);
	}
}