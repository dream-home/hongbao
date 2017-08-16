package com.yanbao.service;

import com.yanbao.core.page.Page;
import com.mall.model.WalletSubsidySign;
import com.yanbao.vo.WalletSubsidySignVo;

import java.util.List;

/**
 * @author zcj
 * @date 2017年06月05日
 */
public interface WalletSubsidySignService {


    Integer count(String userid, Integer status) throws Exception;

    List<WalletSubsidySign> getList(Page page, String userid, Integer status) throws Exception;
    int readSubsidyCount(WalletSubsidySignVo walletSubsidySignVo);
    List<WalletSubsidySignVo> readList1(WalletSubsidySignVo walletSubsidySignVo,Page page);
}
