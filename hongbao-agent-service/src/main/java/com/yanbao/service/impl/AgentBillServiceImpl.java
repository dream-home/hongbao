package com.yanbao.service.impl;

import com.mall.model.AgentBill;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.page.Page;
import com.yanbao.core.service.CommonService;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.AgentBillMapper;
import com.yanbao.service.AgentBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/6/30.
 */
@Service
public class AgentBillServiceImpl extends CommonServiceImpl<AgentBill> implements AgentBillService {

    @Autowired
    private AgentBillMapper agentBillMapper;

    @Override
    protected CommonDao<AgentBill> getDao() {
        return agentBillMapper;
    }

    @Override
    protected Class<AgentBill> getModelClass() {
        return AgentBill.class;
    }

    @Override
    public List<AgentBill> getAgentPerformance(AgentBill agentBill, Page page) throws Exception {
        return agentBillMapper.getAgentPerformance(agentBill,page);
    }

    @Override
    public int readPerformanceCount(AgentBill agentBill) throws Exception {
        return agentBillMapper.readPerformanceCount(agentBill);
    }

    @Override
    public AgentBill getAgentBillByTime(String agentId, Date fromTime) throws Exception {
        return agentBillMapper.getAgentBillByTime(agentId,fromTime);
    }
}
