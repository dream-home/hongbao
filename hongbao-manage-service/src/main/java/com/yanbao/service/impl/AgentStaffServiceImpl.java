package com.yanbao.service.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mall.model.AgentStaff;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.AgentStaffMapper;
import com.yanbao.service.AgentStaffService;
/**
 * @author Pay - 1091945691@qq.com
 */
@Service("hbAgentStaffServiceImpl")
public class AgentStaffServiceImpl extends CommonServiceImpl<AgentStaff> implements AgentStaffService {
	
	@Autowired
    private AgentStaffMapper mapper;
	
	@Override
	public Integer addPo(AgentStaff model) {
		mapper.create(model);
		return 1;
	}

	@Override
	public AgentStaff getPoByPk(String id) {
		return mapper.readById(id);
	}

	@Override
	public Integer delPo(AgentStaff model) {
		return mapper.delPo(model);
	}

	@Override
	public Integer modPo(AgentStaff model) {
		return mapper.modPo(model);
	}

	@Override
	public Integer modPoNotNull(AgentStaff model) {
		return mapper.modPoNotNull(model);
	}

	@Override
	public List<AgentStaff> getPoList(AgentStaff model) {
		return mapper.getPoList(model);
	}

	@Override
	public List<AgentStaff> getPoListByPage(AgentStaff model, Integer pageNum,
			Integer pageSize) {
		return mapper.readList(model, pageNum, pageSize);
	}

	@Override
	public Integer modPoNotNullByAgent(AgentStaff model) {
		return mapper.modPoNotNullByAgent(model);
	}

	@Override
	protected CommonDao<AgentStaff> getDao() {
		return mapper;
	}

	@Override
	protected Class<AgentStaff> getModelClass() {
		// TODO Auto-generated method stub
		return null;
	}

    
}