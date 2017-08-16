package com.yanbao.service;

import com.mall.model.AgentBill;
import com.mall.model.AgentTimeBill;
import com.yanbao.core.page.Page;
import com.yanbao.core.service.CommonService;

import java.util.List;

/**
 * Created by Administrator on 2017/6/30.
 */
public interface AgentTimeBillService extends CommonService<AgentTimeBill> {

    //根根据代理id查询代理业绩
    List<AgentTimeBill> getAgentPerformance(AgentTimeBill agentTimeBill, Page page) throws Exception;

    //根根据代理id查询代理业绩总数
    int readPerformanceCount(AgentTimeBill agentTimeBill) throws Exception;

}
