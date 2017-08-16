package com.yanbao.service;

import java.util.List;

import com.mall.model.AgentStaff;
import com.yanbao.core.page.Page;
import com.yanbao.core.service.CommonService;
import com.yanbao.vo.AgentLoginVo;
import com.yanbao.vo.LoginReturnVo;
import org.apache.ibatis.annotations.Param;

/**
 * @author Pay - 1091945691@qq.com 
 */
public interface HbAgentStaffService extends CommonService<AgentStaff>{
	Integer addPo(AgentStaff model);
	
	AgentStaff getPoByPk(String id);
	
	Integer delPo(AgentStaff model);
	
	Integer modPo(AgentStaff model);
	
	Integer modPoNotNull(AgentStaff model);
	
	List<AgentStaff> getPoList(AgentStaff model);
	
	List<AgentStaff> getPoListByPage(AgentStaff model,Page page);

	Integer countPoListByPage(AgentStaff model);
	/**
	 * 根据员工条件查询代理
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	AgentStaff getByCondition(AgentStaff condition);

	/**
	 * 员工登录业务
	 */
	LoginReturnVo staffLogin(AgentStaff condition,AgentLoginVo vo) throws Exception;
}