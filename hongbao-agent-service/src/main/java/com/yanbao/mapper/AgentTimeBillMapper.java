package com.yanbao.mapper;


import com.mall.model.AgentBill;
import com.mall.model.AgentTimeBill;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.page.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2017/6/30.
 */
@Repository
public interface AgentTimeBillMapper extends CommonDao<AgentTimeBill> {

    //根根据代理id查询代理业绩
    List<AgentTimeBill> getAgentPerformance(@Param("model") AgentTimeBill agentTimeBill, @Param("page") Page page);

    //根据代理id查询代理业绩总数
    Integer readPerformanceCount(@Param("model") AgentTimeBill agentTimeBill);

}
