package com.yanbao.service.impl;

import com.mall.model.AgentBillDetail;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.CommonService;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.AgentBillDetailMapper;
import com.yanbao.service.AgentBillDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/6/28.
 */
@Service
public class AgentBillDetailServiceImpl extends CommonServiceImpl<AgentBillDetail> implements AgentBillDetailService {

    @Autowired
    private AgentBillDetailMapper agentBillDetailMapper;

    @Override
    protected CommonDao<AgentBillDetail> getDao() {
        return agentBillDetailMapper;
    }

    @Override
    protected Class<AgentBillDetail> getModelClass() {
        return AgentBillDetail.class;
    }
}
