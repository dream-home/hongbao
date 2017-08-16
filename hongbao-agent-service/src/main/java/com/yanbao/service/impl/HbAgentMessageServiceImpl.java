package com.yanbao.service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mall.model.AgentMessage;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.AgentMessageMapper;
import com.yanbao.service.HbAgentMessageService;
/**
 * @author Pay - 1091945691@qq.com
 */
@Service("hbAgentMessageServiceImpl")
public class HbAgentMessageServiceImpl extends CommonServiceImpl<AgentMessage> implements HbAgentMessageService  {
	/**注入消息表DAO接口类*/
	@Autowired
    private AgentMessageMapper mapper;

	@Override
	protected CommonDao<AgentMessage> getDao() {
		return mapper;
	}

	@Override
	protected Class<AgentMessage> getModelClass() {
		return null;
	}
	
	
}