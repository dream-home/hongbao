package com.yanbao.service;

import java.util.List;


import com.mall.model.AgentInfo;
import com.yanbao.core.service.CommonService;
import com.yanbao.vo.AgentInfoVo;

/**
 * 
 * @author zyc 2017-06-19 17:55
 *
 */


public interface AgentInfoService extends CommonService<AgentInfo> {
		void updateVo(String id,AgentInfoVo agentInfoVo);
		List<AgentInfo> getByUserids(List<String> ids);
		List<AgentInfo> readAllByOR(AgentInfo ai);
		List<AgentInfo> readListByAreaid(List<String> areaIds);
}
