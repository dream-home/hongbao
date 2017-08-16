package com.yanbao.service;

import com.yanbao.core.page.Page;
import com.mall.model.WalletSign;

import java.util.List;

/**
 * @author zcj
 * @date 2017年03月03日
 */
public interface WalletSignService {

    List<WalletSign> getList(String userid, Page page) throws Exception;

    Integer count(String userId) throws Exception;

    Integer add(WalletSign model) throws Exception;

    Double signTotal(WalletSign model, Integer countNo) throws Exception;

    Integer getSubsidyCount(String userId, String grade) throws Exception;

    Integer getSignCount(String userId) throws Exception;

    Double doudouSignIn(String userId) throws Exception;

    List<WalletSign> getCommonSignList(String userId, Page page);

    List<WalletSign> getMyDoudouList(String userId, Page page);

    Integer countMyDoudouListSize(String userId);

    Integer countCommonSignListSize(String userId);
}
