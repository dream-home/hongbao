package com.yanbao.mapper;

import java.util.List;

import com.yanbao.core.page.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.mall.model.AgentStaff;
import com.yanbao.core.dao.CommonDao;

@Repository
public interface AgentStaffMapper extends CommonDao<AgentStaff>{
	
	Integer delPo(@Param("model") AgentStaff model);
	
	Integer modPo(@Param("model") AgentStaff model);
	
	Integer modPoNotNull(@Param("model") AgentStaff model);
	
	List<AgentStaff> getPoList(@Param("model") AgentStaff model);

	List<AgentStaff> getPoListByPage(@Param("model") AgentStaff model,@Param("page")Page page);

	Integer countPoListByPage(@Param("model") AgentStaff model);

}
