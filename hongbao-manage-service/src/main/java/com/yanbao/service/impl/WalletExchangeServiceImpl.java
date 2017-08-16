package com.yanbao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.UserDao;
import com.yanbao.mapper.WalletExchangeDao;
import com.mall.model.WalletExchange;
import com.yanbao.service.WalletExchangeService;
import com.yanbao.vo.ExchangeVo;

/**
 * Created by summer on 2016-12-09:09:34;
 */
@Service
public class WalletExchangeServiceImpl extends CommonServiceImpl<WalletExchange> implements WalletExchangeService{

    @Autowired
    WalletExchangeDao exchangeDao;
    @Autowired
    UserDao userDao;


    @Override
    protected CommonDao<WalletExchange> getDao() {
        return exchangeDao;
    }

    @Override
    protected Class<WalletExchange> getModelClass() {
        return WalletExchange.class;
    }

    @Override
    public double getIncome() {
        Double count=exchangeDao.getIncome();
        count=count==null?0:count;
        return count;
    }

    @Override
    public List<ExchangeVo> getExchangeVoList(ExchangeVo exchangeVo, int pageNo, int pageSize) {
        return exchangeDao.getExchangeVoList(exchangeVo,pageNo,pageSize);
    }

    @Override
    public Integer getExchangeVoCount(ExchangeVo exchangeVo) {
        Integer count=exchangeDao.getExchangeVoCount(exchangeVo);
        count=count==null?0:count;
        return count;
    }

    @Override
    public double getSomeSum(WalletExchange walletExchange) {
        Double sum=exchangeDao.getSomeSum(walletExchange);
        return sum==null?0:sum;
    }

    /**
     * 审核不通过 给用户退还积分
     * @param walletExchange
     * @throws RuntimeException
     */
    @Override
    @Transactional
    public void reFundScore(WalletExchange walletExchange) throws RuntimeException {
        try {
            exchangeDao.updateById(walletExchange.getId(),walletExchange);
            userDao.addScoreByUserId(walletExchange.getUserId(),-walletExchange.getScore());
        }catch (Exception e){
            throw new RuntimeException();
        }

    }

}
