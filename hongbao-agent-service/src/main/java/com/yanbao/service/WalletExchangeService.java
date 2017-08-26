package com.yanbao.service;

import com.mall.model.WalletExchange;
import com.yanbao.core.service.CommonService;
import com.yanbao.vo.StoreExchangeRecord;

import java.util.Date;
import java.util.List;

/**
 * 根据userId分钟查询商家提现记录
 *
 * @author lxl
 * @date 2016年11月27日
 */
public interface WalletExchangeService extends CommonService<WalletExchange> {

    /**
     * 根据userId分组查询商家提现记录
     *
     * @param starttime
     * @param endtime
     * @return
     */
    List<StoreExchangeRecord> getStoreList(Date starttime, Date endtime);

    /**
     * 根据商家ID查询提现记录
     *
     * @param starttime
     * @param endtime
     * @return
     */
    List<WalletExchange> getByStoreIds(List<String> storeIds);

    Integer countCurrentDay(String userId);
}
