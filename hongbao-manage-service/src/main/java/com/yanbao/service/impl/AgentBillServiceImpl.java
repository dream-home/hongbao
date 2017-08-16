package com.yanbao.service.impl;

import com.mall.model.AgentBill;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.page.Page;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.AgentBillDao;
import com.yanbao.service.AgentBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 代理月结账单汇总业务类
 * Created by Administrator on 2017/6/15.
 */
@Service
public class AgentBillServiceImpl extends CommonServiceImpl<AgentBill> implements AgentBillService {

    @Autowired
    private AgentBillDao agentBillDao;

    @Override
    protected CommonDao<AgentBill> getDao() {
        return agentBillDao;
    }

    @Override
    protected Class<AgentBill> getModelClass() {
        return AgentBill.class;
    }

    @Override
    public List<AgentBill> getAgentPerformance(AgentBill agentBill,Date fromTime, Date stopTime, Page page) throws Exception {
        return agentBillDao.getAgentPerformance(agentBill,fromTime,stopTime,page);
    }

    @Override
    public int readPerformanceCount(AgentBill agentBill, Date fromTime,Date stopTime) throws Exception {
        return agentBillDao.readPerformanceCount(agentBill,fromTime,stopTime);
    }

    @Override
    public Integer updateByIds(AgentBill agentBill, List<String> list) throws Exception {
        return agentBillDao.updateByIds(agentBill,list);
    }

    @Override
    public AgentBill getAgentBillByTime(String agentId, Date fromTime) throws Exception {
        return agentBillDao.getAgentBillByTime(agentId, fromTime);
    }

}
