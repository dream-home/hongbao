package com.yanbao.mapper;

import java.util.List;

import com.mall.model.AgentInfo;
import com.yanbao.core.dao.CommonDao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentInfoMapper extends CommonDao<AgentInfo>{

    /**
     * 根据代理商家编号查询代理
     * @param uid
     * @return
     */
    AgentInfo getByUid(@Param("uid") String uid) throws Exception;
    
    List<AgentInfo> readListByAreaid(@Param("list") List<String> list);

    /**
     * 查询结算日期大于30天
     * @return
     * @throws Exception
     */
    List<AgentInfo> getAgentStatisticsInfo(@Param("model") AgentInfo model) throws Exception;
}
