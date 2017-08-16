package com.yanbao.service;

import java.util.List;

import com.mall.model.AgentInfo;
import com.yanbao.core.service.CommonService;
import com.yanbao.vo.AgentLoginVo;
import com.yanbao.vo.LoginReturnVo;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author jay.zheng
 * @date 2017/6/16
 */
public interface AgentInfoService extends CommonService<AgentInfo>{

    /**
     * 根据代理id查询代理资料
     * @param id
     * @return
     * @throws Exception
     */
    AgentInfo getById(String id) throws Exception;

    /**
     * 根据代理商家编号查询代理资料
     * @param uid
     * @return
     * @throws Exception
     */
    AgentInfo getByUid(String uid) throws Exception;

    /**
     * 根据代理条件查询代理
     * @param condition
     * @return
     * @throws Exception
     */
    AgentInfo getByCondition(AgentInfo condition) throws Exception;

    /**
     * 根据代理id修改代理资料
     * @param model
     * @param id
     * @return
     * @throws Exception
     */
    Integer update(String id,AgentInfo model) throws Exception;
    //根据地区查出所有的代理
    List<AgentInfo> readListByAreaid(List<String> list) throws Exception;

    /**
     * 代理登录业务
     */
    LoginReturnVo AgentLogin(AgentInfo condition,AgentLoginVo vo) throws Exception;

    /**
     * 查询当前需要结算的代理
     * @return
     * @throws Exception
     */
    List<AgentInfo> getAgentStatisticsInfo(AgentInfo model) throws Exception;

}
