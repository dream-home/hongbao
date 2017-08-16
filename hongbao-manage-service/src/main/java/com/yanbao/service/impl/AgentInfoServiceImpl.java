package com.yanbao.service.impl;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mall.model.AgentInfo;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.AgentInfoDao;

import com.yanbao.service.AgentInfoService;
import com.yanbao.vo.AgentInfoVo;
/**
 * 
 * @author zyc 2017-06-19 17:55
 *
 */


@Service
public class AgentInfoServiceImpl extends CommonServiceImpl<AgentInfo> implements AgentInfoService {

	@Autowired
    AgentInfoDao agentInfoDao;

    @Override
    protected CommonDao<AgentInfo> getDao() {
        return agentInfoDao;
    }

    @Override
    protected Class<AgentInfo> getModelClass() {
        return AgentInfo.class;
    }
    
    
    @Override
    public void updateVo(String id, AgentInfoVo agentInfoVo){
    	agentInfoDao.updateVo(id, agentInfoVo);
    }

	@Override
	public List<AgentInfo> getByUserids(List<String> ids) {
		return agentInfoDao.getByUserids(ids);
	}

	@Override
	public List<AgentInfo> readAllByOR(AgentInfo agentInfo) {
		return agentInfoDao.readAllByOR(agentInfo);
	}

    @Override
    public List<AgentInfo> readListByAreaid(List<String> areaIds) {
        return agentInfoDao.readListByAreaid(areaIds);
    }


}
