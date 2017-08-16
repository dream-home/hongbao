package com.yanbao.service;

import com.mall.model.WalletSubsidySign;

/**
 * @author zcj
 * @date 2017年06月05日
 */
public interface WalletSubsidySignService {

    Integer add(WalletSubsidySign model) throws Exception;

    Integer update(WalletSubsidySign model, String id) throws Exception;

    WalletSubsidySign getByUserId(String userid,Integer status) throws Exception;
}
