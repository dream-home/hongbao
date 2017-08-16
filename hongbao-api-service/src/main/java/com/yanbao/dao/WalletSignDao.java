package com.yanbao.dao;

import com.mall.model.WalletSign;
import com.yanbao.core.page.Page;

import java.util.List;

/**
 * @author zcj
 * @date 2017年03月03日
 */
public interface WalletSignDao {
    List<WalletSign> getList(String userId, Page page) throws Exception;

    Integer count(String userId);

    Integer add(WalletSign model) throws Exception;

    Double signTotal(WalletSign model, Integer countNo) throws Exception;

    Integer getSubsidyCount(String userId, String grade);

    Integer getSignCount(String userId);

    List<WalletSign> getCommonSignList(String userId, Page page);

    List<WalletSign> getMyDoudouList(String userId,Page page);

    Integer countMyDoudouListSize(String userId);

    Integer countCommonSignListSize(String userId);
}
