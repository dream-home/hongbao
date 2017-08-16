package com.yanbao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yanbao.core.dao.CommonDao;
import com.mall.model.User;
import com.yanbao.vo.UserVo;

/**
 * Created by summer on 2016-12-08:16:48;
 */
public interface UserDao extends CommonDao<User> {

    List<UserVo> readUserVoList(@Param("model") UserVo userVo,@Param("pageNo") int pageNo, @Param("pageSize")int pageSize);

    List<User> getUnderlineAll(@Param("leaderId") String id,@Param("pageNo") int pageNo,@Param("pageSize")int pageSize);

    Integer getUnderlineAllCount(@Param("leaderId") String id);
    
    List<User> getByUserids(@Param("ids")List<String> ids);

    int addScoreByUserId(@Param("userId")String userId,@Param("score") double score);
    
    List<User> readAllByOR(@Param("model")User user);
    
    double getExchangeEP();
    double getScoreSUM();
    double sysScoreBalance();
    double sysEPBalance();
    
    /**
     * 修改用户ep值
     * @param userId
     * @param score
     * @return
     */
    int updateEpById(@Param("model") User user);

    void updatePerformanceOne(@Param("id") String id, @Param("count") Double count);

    void updatePerformanceTwo(@Param("id") String id,@Param("count") Double count);

    void updatePerformanceThree(@Param("id") String id, @Param("count") Double count);
}
