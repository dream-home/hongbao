package com.yanbao.service.impl;

import com.mall.model.AgentBill;
import com.mall.model.AgentTimeBill;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.page.Page;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.AgentBillMapper;
import com.yanbao.mapper.AgentTimeBillMapper;
import com.yanbao.service.AgentBillService;
import com.yanbao.service.AgentTimeBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/6/30.
 */
@Service
public class AgentTimeBillServiceImpl extends CommonServiceImpl<AgentTimeBill> implements AgentTimeBillService {

    @Autowired
    private AgentTimeBillMapper agentTimeBillMapper;

    @Override
    protected CommonDao<AgentTimeBill> getDao() {
        return agentTimeBillMapper;
    }

    @Override
    protected Class<AgentTimeBill> getModelClass() {
        return AgentTimeBill.class;
    }

    @Override
    public List<AgentTimeBill> getAgentPerformance(AgentTimeBill agentTimeBill, Page page) throws Exception {
        return agentTimeBillMapper.getAgentPerformance(agentTimeBill,page);
    }

    @Override
    public int readPerformanceCount(AgentTimeBill agentTimeBill) throws Exception {
        return agentTimeBillMapper.readPerformanceCount(agentTimeBill);
    }
}
