package com.yanbao.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.yanbao.constant.AgentType;
import com.yanbao.constant.RedisKey;
import com.yanbao.constant.StatusType;
import com.yanbao.redis.Sets;
import com.yanbao.redis.Strings;
import com.yanbao.util.Md5Util;
import com.yanbao.util.TokenUtil;
import com.yanbao.vo.AgentLoginVo;
import com.yanbao.vo.LoginReturnVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mall.model.AgentInfo;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.AgentInfoMapper;
import com.yanbao.service.AgentInfoService;
import com.yanbao.util.ToolUtil;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jay.zheng
 * @date 2017/6/16
 */
@Service
public class AgentInfoServiceImpl extends CommonServiceImpl<AgentInfo> implements AgentInfoService {
	
	@Autowired
	private AgentInfoMapper mapper;

    @Override
    public AgentInfo getById(String id) throws Exception {
        if(ToolUtil.isEmpty(id)){
            return null;
        }
        return mapper.readById(id);
    }

    @Override
    public AgentInfo getByUid(String uid) throws Exception {
        if(ToolUtil.isEmpty(uid)){
            return null;
        }
        return mapper.getByUid(uid);
    }

    @Override
    public AgentInfo getByCondition(AgentInfo condition) throws Exception {
        if(condition == null){
            return null;
        }
        return mapper.readOne(condition);
    }

    @Override
    public Integer update(String id, AgentInfo model) throws Exception {
        if(ToolUtil.isEmpty(id) || model == null){
            return null;
        }
        mapper.updateById(id, model);
        return 1;
    }

	@Override
	protected CommonDao<AgentInfo> getDao() {
		return mapper;
	}

	@Override
	protected Class<AgentInfo> getModelClass() {
		return null;
	}

	@Override
	public List<AgentInfo> readListByAreaid(List<String> list) throws Exception {
		return mapper.readListByAreaid(list);
	}

	@Transactional
    @Override
    public LoginReturnVo AgentLogin(AgentInfo condition,AgentLoginVo vo) throws Exception {

        //根据登录账号查询登录账户信息
        AgentInfo agentInfo = this.getByCondition(condition);
        LoginReturnVo loginReturnVo = new LoginReturnVo();

        //判断是否为空
        if (null == agentInfo) {
            loginReturnVo.setErrorCode(10001);
            loginReturnVo.setErrorMessage("用户不存在");
            loginReturnVo.setAgentInfo(null);
            loginReturnVo.setAgentStaff(null);
            return loginReturnVo;
        }

        if (agentInfo.getStatus() != 1) {
            loginReturnVo.setErrorCode(10002);
            loginReturnVo.setErrorMessage("您的帐号已被禁用");
            loginReturnVo.setAgentInfo(agentInfo);
            loginReturnVo.setAgentStaff(null);
            return loginReturnVo;
        }

        String password = agentInfo.getPassword();
        if (org.springframework.util.StringUtils.isEmpty(password)) {
            loginReturnVo.setErrorCode(10003);
            loginReturnVo.setErrorMessage("没有设置登录密码");
            loginReturnVo.setAgentInfo(agentInfo);
            loginReturnVo.setAgentStaff(null);
            return loginReturnVo;
        }

        //判断密码是否错误，并保存错误次数
//        System.out.print("旧密码为:" + password + "      " + agentInfo.getSalt());
//        System.out.print("密码为:" + vo.getPassword() + "      " + agentInfo.getSalt());
        if (!password.equals(Md5Util.MD5Encode(vo.getPassword(), agentInfo.getSalt()))) {
            if (agentInfo.getErrorCount() == null || ToolUtil.isEmpty(agentInfo.getErrorCount()) || agentInfo.getErrorCount() == -1) {
                agentInfo.setErrorCount(0);
            }
            //密码输入错误，记录错误次数
            //定义变量保存错误次数
            int errorCount = agentInfo.getErrorCount() + 1;
            //AgentInfo agentInfo1 = new AgentInfo();
            agentInfo.setErrorCount(errorCount);
            this.update(agentInfo.getId(), agentInfo);
            //提示错误
            loginReturnVo.setErrorCode(10004);
            loginReturnVo.setErrorMessage("用户名或密码错误");
            agentInfo.setErrorCount(errorCount);
            loginReturnVo.setErrorCount(errorCount);
            loginReturnVo.setAgentInfo(agentInfo);
            loginReturnVo.setAgentStaff(null);
            return loginReturnVo;
        }

        //修改登录时间和修改时间
        AgentInfo updateAgentInfo = new AgentInfo();
        updateAgentInfo.setId(agentInfo.getId());
        updateAgentInfo.setLoginTime(new Date());
        updateAgentInfo.setUpdateTime(new Date());
        updateAgentInfo.setErrorCount(0);
        this.update(agentInfo.getId(), updateAgentInfo);
        // 登录
        String token = TokenUtil.generateToken(agentInfo.getId(), agentInfo.getUserName(), agentInfo.getNickName(),AgentType.AGENT_TYPE.getCode().intValue());
//         loginRedis(agentInfo, token);
        //String redisToken = Strings.get(RedisKey.TOKEN_API.getKey() + agentInfo.getId());
//        System.out.println("token为:"+token);
        Strings.setEx(RedisKey.TOKEN_API.getKey() + agentInfo.getId(), RedisKey.TOKEN_API.getSeconds(), token);
//		Strings.setEx(RedisKey.TOKEN_API.getKey() + user.getId(), RedisKey.TOKEN_API.getSeconds(), token);
        Sets.sadd(RedisKey.ALL_TOKENS.getKey(), token);
        if (logger.isInfoEnabled()) {
            logger.info(String.format("user login[%s]", TokenUtil.getTokenObject(token)));
        }

        //登录成功，返回信息
        loginReturnVo.setErrorCode(200);
        loginReturnVo.setErrorMessage("登录成功");
        loginReturnVo.setAgentInfo(agentInfo);
        loginReturnVo.setAgentStaff(null);
        loginReturnVo.setToken(token);
        return loginReturnVo;
    }


    @Override
    public List<AgentInfo> getAgentStatisticsInfo(AgentInfo model) throws Exception {

        return mapper.getAgentStatisticsInfo(model);
    }

}
