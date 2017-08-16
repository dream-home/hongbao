package com.yanbao.service;

import com.mall.model.AgentBill;
import com.yanbao.core.page.Page;
import com.yanbao.core.service.CommonService;

import java.util.Date;
import java.util.List;

/**
 * 代理月结账单汇总业务接口
 * Created by Administrator on 2017/6/15.
 */
public interface AgentBillService extends CommonService<AgentBill> {

    //根据手机号、姓名的信息精确匹配，公司名称模糊匹配查询代理业绩
    List<AgentBill> getAgentPerformance(AgentBill agentBill, Date fromTime,Date stopTime, Page page) throws Exception;

    //根据手机号、姓名的信息精确匹配，公司名称模糊匹配查询代理业绩总数
    int readPerformanceCount(AgentBill agentBill, Date fromTime,Date stopTime) throws Exception;

    //循环修改记录
    Integer updateByIds(AgentBill agentBill,List<String> list) throws Exception;

    AgentBill getAgentBillByTime(String agentId,Date fromTime)throws Exception;

}
