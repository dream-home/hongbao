package com.yanbao.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.yanbao.core.page.Page;
import com.yanbao.dao.WalletSignDao;
import com.yanbao.mapper.WalletSignMapper;
import com.mall.model.WalletSign;

/**
 * @author zcj
 * @date 2017年03月03日
 */
@Repository
public class WalletSignDaoImpl implements WalletSignDao {
    @Autowired
    private WalletSignMapper signMapper;

    @Override
    public List<WalletSign> getList(String userId, Page page) throws Exception {
        return signMapper.getList(userId, page);
    }

    @Override
    public Integer count(String userId) {
        return signMapper.count(userId);
    }

    @Override
    public Integer add(WalletSign model) throws Exception {
        return signMapper.add(model);
    }

    @Override
    public Double signTotal(WalletSign model, Integer countNo) throws Exception {
        return signMapper.signTotal(model, countNo);
    }

    @Override
    public Integer getSubsidyCount(String userId, String grade) {
        return signMapper.getSubsidyCount(userId, grade);
    }

    @Override
    public Integer getSignCount(String userId) {
        return signMapper.getSignCount(userId);
    }

    /**
     * 获取普通万三万五斗斗签到 交易流水
     * @param userId
     * @param page
     * @return
     */
    @Override
    public List<WalletSign> getCommonSignList(String userId, Page page) {
        return signMapper.getCommonSignList(userId,page);
    }

    /**
     * 获取所有的斗斗交易流水
     * @param userId
     * @param page
     * @return
     */
    @Override
    public List<WalletSign> getMyDoudouList(String userId, Page page) {
        return signMapper.getMyDoudouList(userId,page);
    }

    @Override
    public Integer countMyDoudouListSize(String userId) {
        return signMapper.countMyDoudouListSize(userId);
    }

    @Override
    public Integer countCommonSignListSize(String userId) {
        return signMapper.countCommonSignListSize(userId);
    }
}
