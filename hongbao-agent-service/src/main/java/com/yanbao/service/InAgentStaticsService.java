package com.yanbao.service;

import com.mall.model.AgentBill;

import java.util.Date;
import java.util.List;

/**
 * 区域代理商家统计
 * Created by Administrator on 2017/6/19.
 */
public interface InAgentStaticsService {

    /**
     * 统计详情结算业绩
     * @param starttime
     * @param endtime
     * @return
     * @throws Exception
     */
    Boolean inAgentStatistics(Date starttime, Date endtime) throws Exception;
}
