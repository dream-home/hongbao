package com.yanbao.service;

import com.mall.model.AgentStaff;
import com.yanbao.core.service.CommonService;

import java.util.List;

/**
 * @author Pay - 1091945691@qq.com 
 */
public interface AgentStaffService extends CommonService<AgentStaff>{
	Integer addPo(AgentStaff model);
	
	AgentStaff getPoByPk(String id);
	
	Integer delPo(AgentStaff model);
	
	Integer modPo(AgentStaff model);
	
	Integer modPoNotNull(AgentStaff model);
	
	List<AgentStaff> getPoList(AgentStaff model);
	
	List<AgentStaff> getPoListByPage(AgentStaff model,Integer pageNum,Integer pageSize);
	
	Integer modPoNotNullByAgent(AgentStaff model);
}