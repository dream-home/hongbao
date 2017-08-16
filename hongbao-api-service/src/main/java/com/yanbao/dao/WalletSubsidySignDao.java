package com.yanbao.dao;

import com.mall.model.WalletSubsidySign;

/**
 * @author zcj
 * @date 2017年06月05日
 */
public interface WalletSubsidySignDao {


    Integer add(WalletSubsidySign model) throws Exception;

    Integer update(WalletSubsidySign model, String id);

    WalletSubsidySign getByUserId(String userid,Integer status);
}
