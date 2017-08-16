package com.yanbao.service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mall.model.AgentEpOrder;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.AgentEpOrderMapper;
import com.yanbao.service.AgentEpOrderService;
/**
 * @author Pay - 1091945691@qq.com
 */
@Service("hbAgentEpOrderServiceImpl")
public class AgentEpOrderServiceImpl extends CommonServiceImpl<AgentEpOrder> implements AgentEpOrderService  {
	
	@Autowired
	private AgentEpOrderMapper mapper;
	
	
	@Override
	protected CommonDao<AgentEpOrder> getDao() {
		return mapper;
	}

	@Override
	protected Class<AgentEpOrder> getModelClass() {
		// TODO Auto-generated method stub
		return null;
	}
	
}