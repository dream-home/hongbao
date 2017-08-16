package com.yanbao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mall.model.AgentInfo;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.vo.AgentInfoVo;

/**
 * 
 * @author zyc 2017-06-19 17:55
 *
 */


public interface AgentInfoDao extends CommonDao<AgentInfo> {
		void updateVo(@Param("id") String id,@Param("model") AgentInfoVo agentInfoVo);
		List<AgentInfo> getByUserids(@Param("ids")List<String> ids);
		List<AgentInfo> readAllByOR(@Param("model")AgentInfo agentInfo);
		List<AgentInfo> readListByAreaid(@Param("list")List<String> areaIds);
}
