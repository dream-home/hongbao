package com.yanbao.service.impl;
import java.util.Date;
import java.util.List;


import com.yanbao.constant.AgentType;
import com.yanbao.constant.RedisKey;
import com.yanbao.constant.StatusType;
import com.yanbao.core.page.Page;
import com.yanbao.redis.Sets;
import com.yanbao.redis.Strings;
import com.yanbao.util.DateTimeUtil;
import com.yanbao.util.Md5Util;
import com.yanbao.util.TokenUtil;
import com.yanbao.util.ToolUtil;
import com.yanbao.vo.AgentLoginVo;
import com.yanbao.vo.LoginReturnVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mall.model.AgentStaff;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.AgentStaffMapper;
import com.yanbao.service.HbAgentStaffService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Pay - 1091945691@qq.com
 */
@Service("hbAgentStaffServiceImpl")
public class HbAgentStaffServiceImpl extends CommonServiceImpl<AgentStaff> implements HbAgentStaffService {
	
	@Autowired
    private AgentStaffMapper mapper;
	
	@Override
	public Integer addPo(AgentStaff model) {
		String salt = DateTimeUtil.formatDate(new Date(), DateTimeUtil.PATTERN_B);
		model.setSalt(salt);
		model.setPassword(Md5Util.MD5Encode(model.getPassword(),salt));
		model.setPayPassWord(Md5Util.MD5Encode(model.getPayPassWord(),salt));
		mapper.create(model);
		return 1;
	}

	@Override
	public AgentStaff getPoByPk(String id) {
		return mapper.readById(id);
	}

	@Override
	public Integer delPo(AgentStaff model) {
		return mapper.delPo(model);
	}

	@Override
	public Integer modPo(AgentStaff model) {
		return mapper.modPo(model);
	}

	@Override
	public Integer modPoNotNull(AgentStaff model) {
		return mapper.modPoNotNull(model);
	}

	@Override
	public List<AgentStaff> getPoList(AgentStaff model) {
		return mapper.getPoList(model);
	}

	@Override
	public List<AgentStaff> getPoListByPage(AgentStaff model, Page page) {
		return mapper.getPoListByPage(model,page);
	}

	@Override
	public Integer countPoListByPage(AgentStaff model) {
		return mapper.countPoListByPage(model);
	}

	@Override
	protected CommonDao<AgentStaff> getDao() {
		return mapper;
	}

	@Override
	protected Class<AgentStaff> getModelClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AgentStaff getByCondition(AgentStaff condition) {
		if(condition == null){
			return null;
		}
		Page page = new Page();
		page.setPageNo(0);
		page.setPageSize(999);
		List<AgentStaff> list = getPoListByPage(condition,page);
		if(list == null || list.size() ==0){
			return null;
		}
		return list.get(0);
	}

	@Transactional
	@Override
	public LoginReturnVo staffLogin(AgentStaff condition,AgentLoginVo vo) throws Exception {

		//根据登录账号查询登录账户信息
		AgentStaff agentStaff = this.getByCondition(condition);
		LoginReturnVo loginReturnVo = new LoginReturnVo();

		//判断是否为空
		if (null == agentStaff) {
			loginReturnVo.setErrorCode(10001);
			loginReturnVo.setErrorMessage("用户不存在");
			loginReturnVo.setAgentInfo(null);
			loginReturnVo.setAgentStaff(null);
			return loginReturnVo;
		}

		if (agentStaff.getStatus() != 1) {
			loginReturnVo.setErrorCode(10002);
			loginReturnVo.setErrorMessage("您的帐号已被禁用");
			loginReturnVo.setAgentInfo(null);
			loginReturnVo.setAgentStaff(agentStaff);
			return loginReturnVo;
		}

		String password = agentStaff.getPassword();
		if (org.springframework.util.StringUtils.isEmpty(password)) {
			loginReturnVo.setErrorCode(10003);
			loginReturnVo.setErrorMessage("没有设置登录密码");
			loginReturnVo.setAgentInfo(null);
			loginReturnVo.setAgentStaff(agentStaff);
			return loginReturnVo;
		}

		//判断密码是否错误，并保存错误次数
		if (!password.equals(Md5Util.MD5Encode(vo.getPassword(), agentStaff.getSalt()))) {
			if (agentStaff.getErrorCount() == null || ToolUtil.isEmpty(agentStaff.getErrorCount()) || agentStaff.getErrorCount() == -1) {
				agentStaff.setErrorCount(0);
			}
			//密码输入错误，记录错误次数
			//定义变量保存错误次数
			int errorCount = agentStaff.getErrorCount() + 1;
			agentStaff.setErrorCount(errorCount);
			this.updateById(agentStaff.getId(), agentStaff);
			loginReturnVo.setErrorCode(10004);
			loginReturnVo.setErrorMessage("用户名或密码错误");
			agentStaff.setErrorCount(errorCount);
			loginReturnVo.setErrorCount(errorCount);
			loginReturnVo.setAgentInfo(null);
			loginReturnVo.setAgentStaff(agentStaff);
			return loginReturnVo;
		}

		//修改登录时间和修改时间
		AgentStaff updateAgentStaff = new AgentStaff();
		updateAgentStaff.setId(agentStaff.getId());
		updateAgentStaff.setLoginTime(new Date());
		updateAgentStaff.setUpdateTime(new Date());
		updateAgentStaff.setErrorCount(0);
		this.updateById(agentStaff.getId(), updateAgentStaff);
		// 登录
		String token = TokenUtil.generateToken(agentStaff.getId(), agentStaff.getUserName(), agentStaff.getNickName(), AgentType.STAFF_TYPE.getCode().intValue());
		// loginRedis(agentInfo, token);
//		String redisToken = Strings.get(RedisKey.TOKEN_API.getKey() + agentStaff.getId());
		Strings.setEx(RedisKey.TOKEN_API.getKey() + agentStaff.getId(), RedisKey.TOKEN_API.getSeconds(), token);
//		Strings.setEx(RedisKey.TOKEN_API.getKey() + user.getId(), RedisKey.TOKEN_API.getSeconds(), token);
		Sets.sadd(RedisKey.ALL_TOKENS.getKey(), token);
		if (logger.isInfoEnabled()) {
			logger.info(String.format("user login[%s]", TokenUtil.getTokenObject(token)));
		}

		//登录成功，返回信息
		loginReturnVo.setErrorCode(200);
		loginReturnVo.setErrorMessage("登录成功");
		loginReturnVo.setAgentInfo(null);
		loginReturnVo.setAgentStaff(agentStaff);
		loginReturnVo.setToken(token);
		return loginReturnVo;
	}
}