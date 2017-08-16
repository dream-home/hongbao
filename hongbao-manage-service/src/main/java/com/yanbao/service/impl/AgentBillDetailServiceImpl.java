package com.yanbao.service.impl;

import com.mall.model.AgentBill;
import com.mall.model.AgentBillDetail;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.page.Page;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.AgentBillDao;
import com.yanbao.mapper.AgentBillDetailDao;
import com.yanbao.service.AgentBillDetailService;
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
public class AgentBillDetailServiceImpl extends CommonServiceImpl<AgentBillDetail> implements AgentBillDetailService {

    @Autowired
    private AgentBillDetailDao agentBillDetailDao;

    @Override
    protected CommonDao<AgentBillDetail> getDao() {
        return agentBillDetailDao;
    }

    @Override
    protected Class<AgentBillDetail> getModelClass() {
        return AgentBillDetail.class;
    }

    @Override
    public List<AgentBillDetail> getAgentBillDetail(AgentBillDetail agentBillDetail,Date fromTime, Date stopTime, Page page) throws Exception {
        return agentBillDetailDao.getAgentBillDetail(agentBillDetail,fromTime,stopTime,page);
    }

    @Override
    public int readAgentBillDetailCount(AgentBillDetail agentBillDetail, Date fromTime,Date stopTime) throws Exception {
        return agentBillDetailDao.readAgentBillDetailCount(agentBillDetail,fromTime,stopTime);
    }

    @Override
    public AgentBillDetail getAgentBillDetailByTime(String agentId, Date fromTime) throws Exception {
        return agentBillDetailDao.getAgentBillDetailByTime(agentId,fromTime);
    }

}
