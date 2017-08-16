package com.yanbao.mapper;

import com.mall.model.AgentBillDetail;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/7/10.
 */
public interface AgentBillDetailDao extends CommonDao<AgentBillDetail> {

    //根据手机号、姓名的信息精确匹配，公司名称模糊匹配查询代理业绩记录
    List<AgentBillDetail> getAgentBillDetail(@Param("model") AgentBillDetail agentBillDetail, @Param("fromTime") Date fromTime, @Param("stopTime") Date stopTime, @Param("page")Page page);

    //根据手机号、姓名的信息精确匹配，公司名称模糊匹配查询代理业绩记录总数
    Integer readAgentBillDetailCount(@Param("model") AgentBillDetail agentBillDetail,@Param("fromTime") Date fromTime, @Param("stopTime") Date stopTime);

    AgentBillDetail getAgentBillDetailByTime(@Param("agentId")String agentId,@Param("fromTime")Date fromTime)throws Exception;
}
