package com.yanbao.service.impl;

import com.mall.model.Parameter;
import com.mall.model.User;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.UserDao;
import com.yanbao.service.UserService;
import com.yanbao.util.ParamUtil;
import com.yanbao.util.ToolUtil;
import com.yanbao.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by summer on 2016-12-08:16:49;
 */
@Service
public class UserServiceImpl extends CommonServiceImpl<User>  implements UserService {

    private static final Integer SYSTEM_USER_UID = 200000;

    @Autowired
    UserDao userDao;

    @Override
    protected CommonDao<User> getDao() {
        return userDao;
    }

    @Override
    protected Class<User> getModelClass() {
        return User.class;
    }

    @Override
    public List<UserVo> readUserVoList(UserVo userVo, int pageNo, int pageSize) {
        return userDao.readUserVoList(userVo,pageNo,pageSize);
    }

    @Override
    public List<User> getUnderlineAll(String leaderId,int pageNo,int pageSize) {

        return userDao.getUnderlineAll(leaderId,pageNo,pageSize);
    }

    /**
     * 获得所有下线数量
     * @param leaderId
     * @return
     */
    @Override
    public int getUnderlineAllCount(String leaderId) {
        Integer count=userDao.getUnderlineAllCount(leaderId);
        count=count==null?0:count;
        return count;
    }

    @Override
    public int addScoreByUserId(String userId,double score) {
        return userDao.addScoreByUserId(userId,score);
    }
    
    @Override
    public double getExchangeEP(){
    	return userDao.getExchangeEP();
    }
    @Override
    public double getScoreSUM(){
    	return userDao.getScoreSUM();
    }
    
    @Override
    public double sysScoreBalance(){
    	return userDao.sysScoreBalance();
    }
    
    
    @Override
    public  double sysEPBalance(){
    	return userDao.sysEPBalance();
    	
    }

	/**
	 * 根据用户id增加用户ep
	 */
	@Override
	public int updateEpById(User model) {
		return userDao.updateEpById(model);
	}


    @Override
    @Transactional
    public void updatePerformanceCount(String id, Double count) throws Exception {
        User user = this.readById(id);
        User referrer = this.readById(user.getFirstReferrer());
        // 获取向上统计层数
        int levelNo = ToolUtil.parseInt(ParamUtil.getIstance().get(Parameter.LEVELNO),0);
        // 更新分组统计
        for (int i = 0; i < levelNo; i++) {
            if (referrer == null || referrer.getUid() < SYSTEM_USER_UID) {
                break;
            }
            if ("A".equals(user.getGroupType())) {
                userDao.updatePerformanceOne(referrer.getId(), count);
            } else if ("B".equals(user.getGroupType())) {
                userDao.updatePerformanceTwo(referrer.getId(), count);
            } else if ("C".equals(user.getGroupType())) {
                userDao.updatePerformanceThree(referrer.getId(), count);
            } else {
                break;
            }
            user = referrer;
            referrer = this.readById(user.getFirstReferrer());
        }
    }

	@Override
	public List<User> getByUserids(List<String> ids) {
		return userDao.getByUserids(ids);
	}

	@Override
	public List<User> readAllByOR(User user) {
		return userDao.readAllByOR(user);
	}
}
