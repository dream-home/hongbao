package com.yanbao.service;

import java.util.List;

import com.yanbao.core.page.Page;
import com.yanbao.core.service.CommonService;
import com.mall.model.WalletDonate;
import com.yanbao.vo.WalletDonateVo;

/**
 * Created by summer on 2016-12-13:16:30;
 */
public interface WalletDonateService extends CommonService<WalletDonate> {

    double getIncome();

    List<WalletDonateVo> readVoList(WalletDonateVo walletDonateVo, Page page);

    Integer readVoCount(WalletDonateVo walletDonateVo);
}
