package com.yanbao.service;

import java.util.List;

import com.yanbao.core.service.CommonService;
import com.mall.model.WalletExchange;
import com.yanbao.vo.ExchangeVo;

/**
 * Created by summer on 2016-12-09:09:34;
 */
public interface WalletExchangeService extends CommonService<WalletExchange>{

    double getIncome();

    List<ExchangeVo> getExchangeVoList(ExchangeVo exchangeVo, int pageNo, int pageSize);

    Integer getExchangeVoCount(ExchangeVo exchangeVo);

    double getSomeSum(WalletExchange walletExchange);

    void reFundScore (WalletExchange walletExchange) throws RuntimeException;
}
