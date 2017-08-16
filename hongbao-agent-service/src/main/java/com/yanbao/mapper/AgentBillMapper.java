package com.yanbao.mapper;


import com.mall.model.AgentBill;
import com.mall.model.AgentInfo;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.page.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/6/30.
 */
@Repository
public interface AgentBillMapper extends CommonDao<AgentBill> {

    //根根据代理id查询代理业绩
    List<AgentBill> getAgentPerformance(@Param("model") AgentBill agentBill, @Param("page")Page page);

    //根据代理id查询代理业绩总数
    Integer readPerformanceCount(@Param("model") AgentBill agentBill);

    //根据代理id和当月第一天查询代理业绩
    AgentBill getAgentBillByTime(@Param("agentId") String agentId,@Param("fromTime") Date fromTime) throws Exception;
}
