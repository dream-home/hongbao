package com.yanbao.mapper;

import com.mall.model.AgentBill;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.page.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 代理月结账单汇总DAO类
 * Created by Administrator on 2017/6/15.
 */
@Repository
public interface AgentBillDao extends CommonDao<AgentBill> {

    //根据手机号、姓名的信息精确匹配，公司名称模糊匹配查询代理业绩
    List<AgentBill> getAgentPerformance(@Param("model") AgentBill agentBill,@Param("fromTime") Date fromTime, @Param("stopTime") Date stopTime, @Param("page")Page page);

    //根据手机号、姓名的信息精确匹配，公司名称模糊匹配查询代理业绩总数
    Integer readPerformanceCount(@Param("model") AgentBill agentBill,@Param("fromTime") Date fromTime, @Param("stopTime") Date stopTime);

    //循环修改记录
    Integer updateByIds(@Param("model") AgentBill agentBill,@Param("list") List<String> list);

    AgentBill getAgentBillByTime(@Param("agentId") String agentId,@Param("fromTime") Date fromTime)throws Exception;

}
