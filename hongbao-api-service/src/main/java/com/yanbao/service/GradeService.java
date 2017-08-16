package com.yanbao.service;

import com.mall.model.Grade;
import com.mall.model.User;
import com.mall.model.WalletSubsidySign;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author zcj
 * @date 2017年03月02日
 */
public interface GradeService {
    @Transactional
    WalletSubsidySign getSubsidyAmt(String userid) throws Exception;

    /**
     * 根据用户信息
     * */
    Integer getNewGrade(User user) throws Exception;
    /**
     * 根据会员所属等级获取详细信息
     * */
    Grade getGradeDetil(Integer grade) throws Exception;
    /*会员签到领取的积分*/
    Double memberSignIn(User user)throws Exception;
    /**
     * 会员续费
     * @param userid 用户唯一ID
     * @return 返回true和false,成功续费和续费失败
     * */
    @Transactional
    boolean renewGrade(String userid) throws Exception;

    //清算销售业绩
    @Transactional
    void clearAllPerformance(User user, Integer newGrade) throws Exception;

    //更新合伙人等级
    @Transactional
    void updatePartnerInfo(String userId) throws Exception;
}
