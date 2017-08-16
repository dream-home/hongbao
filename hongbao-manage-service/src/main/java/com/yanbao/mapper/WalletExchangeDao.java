package com.yanbao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yanbao.core.dao.CommonDao;
import com.mall.model.WalletExchange;
import com.yanbao.vo.ExchangeVo;

/**
 * Created by summer on 2016-12-09:09:33;
 */
public interface WalletExchangeDao extends CommonDao<WalletExchange> {

    Double getIncome();

    List<ExchangeVo> getExchangeVoList(@Param("model") ExchangeVo exchangeVo, @Param("pageNo") int pageNo,@Param("pageSize") int pageSize);

    Integer getExchangeVoCount(@Param("model") ExchangeVo exchangeVo);

    Double getSomeSum(@Param("model") WalletExchange walletExchange);
}
