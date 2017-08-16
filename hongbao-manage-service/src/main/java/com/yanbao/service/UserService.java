package com.yanbao.service;

import com.yanbao.core.service.CommonService;
import com.mall.model.User;
import com.yanbao.vo.UserVo;

import java.util.List;

/**
 * Created by summer on 2016-12-08:16:48;
 */
public interface UserService extends CommonService<User> {

    List<UserVo> readUserVoList(UserVo userVo,int pageNo,int pageSize);
    //获取用户所有下线
    List<User> getUnderlineAll( String leaderId,int pageNo,int pageSize);
    //获取用户下线数量
    int getUnderlineAllCount( String leaderId);
    List<User> getByUserids(List<String> ids);
    List<User> readAllByOR(User user);
    /**
     * 积分充值
     * @param score
     * @return
     */
    int addScoreByUserId(String userId,double score);
    
    double getExchangeEP();
    double getScoreSUM();
    double sysScoreBalance();
    double sysEPBalance();
    
    /**
     * 增加ep
     * @param userId
     * @param ep
     * @return
     */
    int updateEpById(User model);
    /**
     * 更新上线合伙人销售业绩
     *
     * @param id
     *            用户id
     * @param count
     *            正数：向上统计增加销售业绩
     */
    void updatePerformanceCount(String id, Double count) throws Exception;
}
