package com.yanbao.service;

import com.mall.model.AgentBill;
import com.yanbao.core.page.Page;
import com.yanbao.core.service.CommonService;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/6/30.
 */
public interface AgentBillService extends CommonService<AgentBill> {

    //根根据代理id查询代理业绩
    List<AgentBill> getAgentPerformance(AgentBill agentBill, Page page) throws Exception;

    //根根据代理id查询代理业绩总数
    int readPerformanceCount(AgentBill agentBill) throws Exception;

    //根据代理id和当月第一天查询代理业绩
    AgentBill getAgentBillByTime(String agentId,Date fromTime) throws Exception;

}
