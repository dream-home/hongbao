package com.yanbao.dao.impl;

import com.mall.model.WalletExchange;
import com.yanbao.constant.RedisKey;
import com.yanbao.core.page.Page;
import com.yanbao.dao.WalletExchangeDao;
import com.yanbao.mapper.WalletExchangeMapper;
import com.yanbao.redis.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Repository
public class WalletExchangeDaoImpl implements WalletExchangeDao {

    @Autowired
    private WalletExchangeMapper walletExchangeMapper;

    @Override
    public WalletExchange getByOrderNo(String orderNo, String userId) throws Exception {
        return walletExchangeMapper.getByOrderNo(orderNo, userId);
    }

    @Override
    public Integer count(String userId) throws Exception {
        Integer count = null;
        String temp = Strings.get(RedisKey.USER_EXCHANGE_COUNT.getKey() + userId);
        if (null != temp) {
            count = Integer.parseInt(temp);
        } else {
            count = walletExchangeMapper.count(userId);
            Strings.setEx(RedisKey.USER_EXCHANGE_COUNT.getKey() + userId, RedisKey.USER_EXCHANGE_COUNT.getSeconds(), count.toString());
        }
        return count;
    }

    @Override
    public List<WalletExchange> getList(String userId, Page page) throws Exception {
        return walletExchangeMapper.getList(userId, page);
    }

    @Override
    public Integer add(WalletExchange model) throws Exception {
        return walletExchangeMapper.add(model);
    }

    @Override
    public Integer update(String id, WalletExchange model) throws Exception {
        return walletExchangeMapper.update(id, model);
    }

    @Override
    public Integer countCurrentDay(String userId) throws Exception {
        return walletExchangeMapper.countCurrentDay(userId);
    }
}
